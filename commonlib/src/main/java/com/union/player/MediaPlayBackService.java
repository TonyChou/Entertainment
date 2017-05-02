package com.union.player;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.union.commonlib.utils.LogUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by zhouxiaming on 2017/4/29.
 */

public class MediaPlayBackService extends Service {
    private static String TAG = "MediaPlayBackService";
    private AudioManager mAudioManager;
    private PowerManager.WakeLock mWakeLock;
    private LocalMediaPlayer mPlayer;
    private IMediaPlayerCallBack mCallBack;
    @Override
    public void onCreate() {
        super.onCreate();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
        mWakeLock.setReferenceCounted(false);
        mPlayer = new LocalMediaPlayer(this, playerListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWakeLock.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * 播放音乐
     * @param url
     */
    private void play(String url) {
        mAudioManager.requestAudioFocus(
                mAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        mPlayer.play(url);
    }

    /**
     * 播放专辑列表
     * @param tracks
     */
    private void playTracks(List<String> tracks) {
        //TODO
    }

    private boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    private void next() {
    }

    private void prev() {
    }

    private void pause() {
        mPlayer.pause();
    }

    private long duration() {
        return mPlayer.duration();
    }

    private long position() {
        return mPlayer.position();
    }

    private long seek(long pos) {
        return mPlayer.seek(pos);
    }

    private void stop() {
        mPlayer.stop();
    }

    private void resume() {
        mPlayer.start();
    }

    private void setCallBack(IMediaPlayerCallBack callBack) {
        this.mCallBack = callBack;
    }

    static class LocalMediaPlayer {
        public static final int SET_DATA_SOURECE_ERROR = 0x001;
        private MediaPlayer mediaPlayer;
        private Context mContext;
        private IPlayerListener listener;
        public LocalMediaPlayer(Context context, IPlayerListener listener) {
            mediaPlayer = new MediaPlayer();
            this.mContext = context;
            mediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
            initListener();
        }

        private void initListener() {
            //设置播放结束监听
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (listener != null) {
                        listener.onCompletion(mp);
                    }
                }
            });

            //设置错误回调监听
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (listener != null) {
                        listener.onError(mp, what, extra);
                    }
                    return false;
                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (listener != null) {
                        listener.onPrepared(mp);
                    }
                    mediaPlayer.start();
                }
            });


            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    LogUtils.i(TAG, "onSeekComplete ==== " + mp.getCurrentPosition());
                    if (listener != null) {
                        listener.onSeekComplete(mp);
                    }
                }
            });

            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    LogUtils.i(TAG, "onInfo ==== what = " + what + "  extra = " + extra);
                    if (listener != null) {
                        return listener.onInfo(mp, what, extra);
                    }
                    return false;
                }
            });

            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    LogUtils.i(TAG, "onBufferingUpdate percent: " + percent);
                    if (listener != null) {
                        listener.onBufferingUpdate(mp, percent);
                    }
                }
            });
        }

        public void play(String url) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(url);
                mediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onError(mediaPlayer, SET_DATA_SOURECE_ERROR, SET_DATA_SOURECE_ERROR);
                }
            }
        }

        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        public void pause() {
            mediaPlayer.pause();
        }

        public long duration() {
           return mediaPlayer.getDuration();
        }

        public long position() {
            return mediaPlayer.getCurrentPosition();
        }

        public long seek(long whereto) {
            mediaPlayer.seekTo((int)whereto);
            return  whereto;
        }

        public void stop() {
            mediaPlayer.stop();
        }

        public void release() {
            mediaPlayer.release();
        }

        public void start() {
            mediaPlayer.start();
        }
    }


    private final IBinder mBinder = new ServiceStub(this);

    static class ServiceStub extends IMediaPlaybackService.Stub {
        WeakReference<MediaPlayBackService> mService;

        ServiceStub(MediaPlayBackService service) {
            mService = new WeakReference<MediaPlayBackService>(service);
        }
        @Override
        public void play(String url) throws RemoteException {
            mService.get().play(url);
        }

        @Override
        public void playTracks(List<String> tracks) throws RemoteException {
            mService.get().playTracks(tracks);
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return mService.get().isPlaying();
        }

        @Override
        public void stop() throws RemoteException {
            mService.get().stop();
        }

        @Override
        public void pause() throws RemoteException {
            mService.get().pause();
        }

        @Override
        public void prev() throws RemoteException {
            mService.get().prev();
        }

        @Override
        public void next() throws RemoteException {
            mService.get().next();
        }

        @Override
        public long duration() throws RemoteException {
            return mService.get().duration();
        }

        @Override
        public long position() throws RemoteException {
            return mService.get().position();
        }

        @Override
        public long seek(long pos) throws RemoteException {
            return mService.get().seek(pos);
        }
        @Override
        public void resume() throws RemoteException {
            mService.get().resume();
        }
        @Override
        public void setCallBack(IMediaPlayerCallBack callBack) {
            mService.get().setCallBack(callBack);
        }


    }



    IPlayerListener playerListener = new IPlayerListener() {

        @Override
        public void onSeekComplete(MediaPlayer mp) {

        }

        @Override
        public void onPrepared(MediaPlayer mp) {

        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return false;
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            if (mCallBack != null) {
                try {
                    mCallBack.onError("MediaPlayer is error!");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mCallBack != null) {
                try {
                    mCallBack.onFinish();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {

        }
    };

    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            //mMediaplayerHandler.obtainMessage(FOCUSCHANGE, focusChange, 0).sendToTarget();
        }
    };



}
