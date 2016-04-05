package com.union.fmdouban.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.union.commonlib.utils.LogUtils;
import com.union.fmdouban.Constant;
import com.union.fmdouban.R;
import com.union.fmdouban.api.ExecuteResult;
import com.union.fmdouban.api.FMApi;
import com.union.fmdouban.api.FMCallBack;
import com.union.fmdouban.api.FMParserFactory;
import com.union.fmdouban.api.FMReport;
import com.union.fmdouban.api.bean.FMLyric;
import com.union.fmdouban.api.bean.FMRichChannel;
import com.union.fmdouban.api.bean.FMSong;
import com.union.fmdouban.play.FMMediaPlayer;
import com.union.fmdouban.play.PlayerControllerListener;
import com.union.fmdouban.play.PlayerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhouxiaming on 2015/4/16.
 */

public class FMPlayerService extends Service implements PlayerListener {
    private static List<FMSong> songCache = new ArrayList<FMSong>();
    private final int PROGRESS_UPDATE = 0x000011; //进度条更新
    private final int PLAYLIST_LOADED_RESULT = 0x000012; //歌曲加载结果
    private String TAG = "FMMediaPlayer";
    private static FMMediaPlayer mPlayer;
    private PlayerListener playerListener;
    private final IBinder mBinder = new LocalBinder();
    private static FMRichChannel mChannel;
    private static PlayState mCurrentPLayState = PlayState.NONE;
    RequestQueue mQueue;
    private static int mSongIndex = 0;
    Context mContext;
    private Timer mProgressTimer;
    private boolean isTimerStarted = false;
    PlayerControllerListener controllerListener;
    Queue<FMSong> mPlayList = new LinkedList<FMSong>();


    @Override
    public void onError(String errorMsg) {
        mCurrentPLayState = PlayState.NONE;
        stopProgressTimer();
        refreshView();
    }

    @Override
    public void onFinish() {
        mCurrentPLayState = PlayState.STOP;
        refreshView();
        stopProgressTimer();
        loadSongAndReport();
    }

    @Override
    public void onPrepared() {
        mCurrentPLayState = PlayState.PLAYING;
        startProgressTimer();
        refreshView();
    }

    public enum PlayState {
        NONE,
        PLAYING,
        PAUSE,
        STOP,
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        mQueue = Volley.newRequestQueue(this);
        mPlayer = FMMediaPlayer.getInstance();
        mPlayer.setPlayerListener(this);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == PROGRESS_UPDATE) {
                int progress = msg.arg1;
                sendProgressCallback(progress);
            } else if (what == PLAYLIST_LOADED_RESULT) {
                handleSongResult((ExecuteResult)msg.obj);
            }
        }
    };

    public FMPlayerService() {

    }

    public class LocalBinder extends Binder {
        public FMPlayerService getPlayerService() {
            return FMPlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * 再次进入播放界面，需要恢复一下界面状态
     *
     * @param controllerListener
     */
    public void setControllerListener(PlayerControllerListener controllerListener) {
        this.controllerListener = controllerListener;
        refreshView();
        controllerListener.loadCover();
    }

    public void sendProgressCallback(int progress) {
        if (controllerListener != null) {
            controllerListener.sendProgress(progress);
        }
    }


    public void play(String url) {
        stopProgressTimer();
        sendProgressCallback(0);
        mPlayer.play(url);
        mCurrentPLayState = PlayState.PLAYING;
        if (controllerListener != null) {
            controllerListener.loadCover();
        }
    }


    public void pause() {
        mPlayer.pause();
        mCurrentPLayState = PlayState.PAUSE;
        refreshView();
    }

    public void resume() {
        mPlayer.resume();
        mCurrentPLayState = PlayState.PLAYING;
        refreshView();
    }

    public void stop() {
        mPlayer.stop();
        mCurrentPLayState = PlayState.NONE;
        refreshView();
    }

    private void refreshView() {
        if (controllerListener != null) {
            controllerListener.refreshControllerView(mChannel, getCurrentSong(), getPlayState());
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
            FMSong song = getSong();
            if (song != null) {
                play(song.getUrl());
            } else if (getCurrentChannel() != null) {
                loadSongAndReport();
            } else {
                mCurrentPLayState = PlayState.NONE;
            }
        }


    }

    private FMSong getSong() {
        if (songCache.size() > 0 && mSongIndex <= songCache.size() - 1) {
            return songCache.get(mSongIndex);
        } else {
            return null;
        }
    }


    public int getCurrentPosition() {
        if (mPlayer != null) {
            return mPlayer.getCurrentPosition();
        } else {
            return 0;
        }

    }


    public int getDuration() {
        if (mPlayer != null) {
            return mPlayer.getDuration();
        } else {
            return 0;
        }
    }


    public void seekTo(int position) {
        LogUtils.i(TAG, "seekTo: " + position);
        mPlayer.seekTo(position);
    }


    public void release() {
        if (mPlayer != null) {
            mPlayer.release();
        }
    }


    public void playNext() {
        mCurrentPLayState = PlayState.NONE;
        stop();
        loadSongAndReport();
    }


    public void playPriority() {
        int tempIndex = mSongIndex - 1;
        if (tempIndex <= songCache.size() - 1 && tempIndex >= 0) {
            mSongIndex--;
            stop();
            play(songCache.get(mSongIndex).getUrl());
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.no_more_pre_songs), Toast.LENGTH_SHORT).show();
        }
    }

    public void notifyToRefreshView() {
        //TODO
    }

    public static boolean isPlaying() {
        if (mPlayer == null) {
            return false;
        } else {
            return mPlayer.isPlaying();
        }
    }

    public boolean switchChannel(FMRichChannel channel) {
        mChannel = channel;
        songCache.clear();
        mPlayList.clear();
        mCurrentPLayState = PlayState.NONE;
        loadSongAndReport();
        return true;
    }





    /**
     * 加载歌曲信息
     */
    private void loadSongAndReport() {
        if (mChannel == null) {
            return;
        }
        // 请求下一个playList
        FMSong song = getCurrentSong();
        String sid = song != null?song.getSid() : null;
        String reportType = FMReport.genReportType(song, mPlayList, false);
        FMApi.getInstance().reportAndGetPlayList(String.valueOf(mChannel.getChannelId()), sid, reportType, new FMCallBack() {
            @Override
            public void onRequestResult(ExecuteResult result) {
                Message msg = handler.obtainMessage(PLAYLIST_LOADED_RESULT);
                msg.obj = result;
                msg.sendToTarget();
            }
        });
    }

    private boolean addTOCacheAndPlay() {
        if (mPlayList == null || mPlayList.size() < 1) {
            return false;
        }
        FMSong song = mPlayList.poll();
        //最多只缓存50首歌曲信息
        if (songCache.size() - 1 == Constant.MAX_CACHE_SONG) {
            songCache.remove(0);
        }
        if (song.getUrl() != null) { //有时候返回的Json里面不包含歌曲信息
            songCache.add(song);
            mSongIndex = songCache.size() - 1;
            play(song.getUrl());
            return true;
        }
        return false;
    }

    /**
     * 解析Playlist
     * @param result
     */
    private void handleSongResult(ExecuteResult result) {
        if (result.getResult() == ExecuteResult.OK && result.getResponseString() != null) {
            Queue<FMSong> list = FMParserFactory.parserToSongList(result.getResponseString());
            if (list != null && list.size() > 0) {
                mPlayList.addAll(list);
            }
            if (!addTOCacheAndPlay()) {
                loadSongAndReport();
            }
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.load_song_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadLyric() {
        final FMSong song = getCurrentSong();
        if (song == null) {
            return;
        }
        final Map<String, String> params = new HashMap<String, String>();
        params.put("sid", song.getSid());
        params.put("ssid", song.getSid());

        StringRequest request = new StringRequest(Request.Method.POST, Constant.SONG_LYRIC_DOUBAN_FM, new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonObject) {
                Log.i(TAG, "onResponse: " + jsonObject);
                FMLyric lyric = new FMLyric(jsonObject);
                song.setLyric(lyric);
                //TODO 通知界面刷新歌词显示
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtils.e(TAG, "load lyric failed: " + song.getTitle());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        mQueue.add(request);
        mQueue.start();
    }


    public static FMRichChannel getCurrentChannel() {
        return mChannel;
    }

    public FMSong getCurrentSong() {
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

}
