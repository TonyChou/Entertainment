package com.union.fmdouban.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.widget.Toast;

import com.tulips.douban.model.ChannelsPage;
import com.tulips.douban.model.PlayerPage;
import com.tulips.douban.service.DoubanParamsGen;
import com.tulips.douban.service.DoubanService;
import com.tulips.douban.service.DoubanUrl;
import com.union.commonlib.utils.LogUtils;
import com.union.fmdouban.Constant;
import com.union.fmdouban.R;
import com.union.fmdouban.play.PlayerControllerListener;
import com.union.fmdouban.play.PlayerStatusListener;
import com.union.net.ApiClient;
import com.union.net.RetrofitClient;
import com.union.player.IMediaPlayerCallBack;
import com.union.player.PlayerUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhouxiaming on 2015/4/16.
 */

public class FMPlayerController implements PlayerStatusListener {
    private static List<PlayerPage.DouBanSong> songCache = new ArrayList<PlayerPage.DouBanSong>();
    private final int PROGRESS_UPDATE = 0x000011; //进度条更新
    private final int PLAYLIST_LOADED_RESULT = PROGRESS_UPDATE + 1; //歌曲加载结果
    private final int SONGINFO_LOAD_FAILED = PLAYLIST_LOADED_RESULT + 1; //歌曲信息加载失败
    private String TAG = "FMPlayerController";
    private static ChannelsPage.Channel mCurrentChannel;
    private static PlayState mCurrentPLayState = PlayState.NONE;
    private static int mSongIndex = 0;
    Context mContext;
    private Timer mProgressTimer;
    private boolean isTimerStarted = false;
    PlayerControllerListener controllerListener;
    private DoubanService douBanService;
    private int SONG_LOAD_RETRY_TIMES = 3;
    Queue<PlayerPage.DouBanSong> mPlayList = new LinkedList<PlayerPage.DouBanSong>();


    @Override
    public void onError(String errorMsg) {
        mCurrentPLayState = PlayState.NONE;
        stopProgressTimer();
        refreshView(StateFrom.ERROR);
    }

    @Override
    public void onFinish() {
        mCurrentPLayState = PlayState.STOP;
        refreshView(StateFrom.FINISH);
        stopProgressTimer();
        loadSongInfoFromServer(SONG_LOAD_RETRY_TIMES);
    }

    @Override
    public void onPrepared() {
        mCurrentPLayState = PlayState.PLAYING;
        startProgressTimer();
        refreshView(StateFrom.PLAY);
    }

    public enum PlayState {
        NONE,
        PLAYING,
        PAUSE,
        STOP,
    }

    public enum StateFrom {
        NONE,
        PLAY,
        RESUME,
        PAUSE,
        STOP,
        FINISH,
        ERROR,
    }



    public FMPlayerController(Context context) {
        mContext = context;
        //mPlayerService.setPlayerListener(this);
        RetrofitClient mApiClient = ApiClient.getDoubanAPiClient(DoubanUrl.API_HOST);
        douBanService = mApiClient.createApi(DoubanService.class);
        PlayerUtils.setCallBack(callBack);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == PROGRESS_UPDATE) {
                int progress = msg.arg1;
                sendProgressCallback(progress);
            } else if (what == PLAYLIST_LOADED_RESULT) {
                handleSongResult((PlayerPage)msg.obj);
            } else if (what == SONGINFO_LOAD_FAILED) {
                Toast.makeText(mContext, R.string.change_channel_failed, Toast.LENGTH_SHORT).show();
                controllerListener.showErrorInfo((String)msg.obj);
            }
        }
    };



    /**
     * 再次进入播放界面，需要恢复一下界面状态
     *
     * @param controllerListener
     */
    public void setControllerListener(PlayerControllerListener controllerListener) {
        this.controllerListener = controllerListener;
        refreshView(StateFrom.NONE);
    }

    public void sendProgressCallback(int progress) {
        if (controllerListener != null) {
            controllerListener.sendProgress(progress);
        }
    }


    public void play(String url) {
        if (PlayerUtils.play(url)) {
            stopProgressTimer();
            sendProgressCallback(0);

            mCurrentPLayState = PlayState.PLAYING;
            refreshView(StateFrom.PLAY);
            if (controllerListener != null) {
                controllerListener.loadCover();
            }
        } else {
            setStatusNone();
        }
    }


    public void pause() {
        if (PlayerUtils.pause()) {
            mCurrentPLayState = PlayState.PAUSE;
            refreshView(StateFrom.PAUSE);
        } else {
            setStatusNone();
        }
    }

    public void resume() {
        if (PlayerUtils.resume()) {
            mCurrentPLayState = PlayState.PLAYING;
            refreshView(StateFrom.RESUME);
        } else {
            setStatusNone();
        }
    }

    public void stop() {
        PlayerUtils.stop();
        mCurrentPLayState = PlayState.NONE;
        refreshView(StateFrom.STOP);
    }

    private void setStatusNone() {
        mCurrentPLayState = PlayState.NONE;
        refreshView(StateFrom.STOP);
    }

    private void refreshView(StateFrom stateFrom) {
        if (controllerListener != null) {
            controllerListener.refreshControllerView(mCurrentChannel, getCurrentSong(), getPlayState(), stateFrom);
        }
    }

    public void playOrPause() {
        if (mCurrentPLayState == PlayState.PLAYING) {
            mCurrentPLayState = PlayState.PAUSE;
            pause();
        } else if (mCurrentPLayState == PlayState.PAUSE) {
            mCurrentPLayState = PlayState.PLAYING;
            resume();
        } else {
            PlayerPage.DouBanSong song = getSong();
            if (song != null) {
                play(song.url);
            } else if (getCurrentChannel() != null) {
                loadSongInfoFromServer(SONG_LOAD_RETRY_TIMES);
            } else {
                mCurrentPLayState = PlayState.NONE;
            }
        }


    }

    private PlayerPage.DouBanSong getSong() {
        if (songCache.size() > 0 && mSongIndex <= songCache.size() - 1) {
            return songCache.get(mSongIndex);
        } else {
            return null;
        }
    }


    public int getCurrentPosition() {
        int position = PlayerUtils.getCurrentPosition();
        if (position == -1) {
            setStatusNone();
        }
        return position;
    }


    public int getDuration() {
        int duration = PlayerUtils.getDuration();
        if (duration == -1) {
            setStatusNone();
        }
        return duration;
    }


    public void seekTo(int position) {
        if (!PlayerUtils.seekTo(position)) {
            setStatusNone();
        }
    }


    public void release() {

    }


    public void playNext() {
        mCurrentPLayState = PlayState.NONE;
        stop();
        loadSongInfoFromServer(SONG_LOAD_RETRY_TIMES);
    }


    public void playPriority() {
        int tempIndex = mSongIndex - 1;
        if (tempIndex <= songCache.size() - 1 && tempIndex >= 0) {
            mSongIndex--;
            stop();
            play(songCache.get(mSongIndex).url);
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.no_more_pre_songs), Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isPlaying() {
        return PlayerUtils.isPlaying();
    }

    /**
     * 切换频道
     * @param channel
     * @return
     */
    public void switchChannel(ChannelsPage.Channel channel) {
        if (mCurrentChannel != null && mCurrentChannel.channelId == channel.channelId) {
            return;
        }
        songCache.clear();
        mPlayList.clear();
        mCurrentPLayState = PlayState.NONE;
        mCurrentChannel = channel;
        loadSongInfoFromServer(SONG_LOAD_RETRY_TIMES);
    }

    /**
     * 加载歌曲信息
     */
    private void loadSongInfoFromServer(final int retryTimes) {
        if (mCurrentChannel == null) {
            return;
        }
        douBanService.playList(DoubanParamsGen.genGetPlayListParams(mCurrentChannel.channelId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<PlayerPage>() {
                    @Override
                    public void onNext(PlayerPage playerPage) {
                        LogUtils.i(TAG, "onNext ==== ");
                        Message msg = handler.obtainMessage(PLAYLIST_LOADED_RESULT);
                        msg.obj = playerPage;
                        msg.sendToTarget();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError ==== " + e);
                        if (retryTimes == 0) {
                            Message msg = handler.obtainMessage(SONGINFO_LOAD_FAILED);
                            msg.obj = e.getMessage();
                            msg.sendToTarget();
                        } else {
                            int newRetryTimes = retryTimes - 1;
                            loadSongInfoFromServer(newRetryTimes);
                        }

                    }

                    @Override
                    public void onComplete() {
                        LogUtils.i(TAG, "onComplete ==== ");
                    }
                });
    }




    private boolean addTOCacheAndPlay() {
        if (mPlayList == null || mPlayList.size() < 1) {
            return false;
        }
        PlayerPage.DouBanSong song = mPlayList.poll();
        //最多只缓存50首歌曲信息
        if (songCache.size() - 1 == Constant.MAX_CACHE_SONG) {
            songCache.remove(0);
        }
        if (song.url != null) { //有时候返回的Json里面不包含歌曲信息
            songCache.add(song);
            mSongIndex = songCache.size() - 1;
            play(song.url);
            return true;
        }
        return false;
    }

    /**
     * 解析Playlist
     * @param result
     */
    private void handleSongResult(PlayerPage result) {
        if (result != null && result.songList != null && result.songList.size() > 0) {

            mPlayList.addAll(result.songList);
            if (!addTOCacheAndPlay()) {
                loadSongInfoFromServer(SONG_LOAD_RETRY_TIMES);
            }
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.load_song_failed), Toast.LENGTH_SHORT).show();
        }
    }






    public static ChannelsPage.Channel getCurrentChannel() {
        return mCurrentChannel;
    }

    public PlayerPage.DouBanSong getCurrentSong() {
        if (songCache.size() > 0 && songCache.size() - 1 >= mSongIndex) {
            return songCache.get(mSongIndex);
        }
        return null;
    }

    public PlayState getPlayState() {
        return mCurrentPLayState;
    }

    private void stopProgressTimer() {
        if (isTimerStarted) {
            mProgressTimer.cancel();
            mProgressTimer = null;
            isTimerStarted = false;
        }
    }

    private void startProgressTimer() {
        if (isTimerStarted) {
            return;
        }
        isTimerStarted = true;
        mProgressTimer = new Timer();
        mProgressTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isPlaying() || getDuration() == 0) {
                    SystemClock.sleep(1000);
                    return;
                }

                if (isPlaying()) {
                    mCurrentPLayState = PlayState.PLAYING;
                }
                int position = getCurrentPosition();
                int duration = getDuration();
                int progress = (int) ((double) position / duration * 1000);
                Message msg = handler.obtainMessage(PROGRESS_UPDATE);
                msg.arg1 = progress;
                msg.sendToTarget();
            }
        }, 0, 300);
    }

    IMediaPlayerCallBack callBack = new IMediaPlayerCallBack.Stub() {

        @Override
        public void onError(String errorMsg) throws RemoteException {

        }

        @Override
        public void onFinish() throws RemoteException {

        }
    };

    private boolean isServiceAlive() {
        boolean isAlive = false;
        if (PlayerUtils.isServiceAlive()) {
            isAlive = true;
        }

        if (!isAlive) {
            setStatusNone();
        }
        return isAlive;
    }
}
