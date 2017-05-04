package com.union.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;
import com.union.commonlib.R;
import com.union.commonlib.utils.LogUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by zhouxiaming on 2017/4/29.
 */

public class MediaPlayBackService extends Service {
    public static String PLAY_OR_PAUSE_ACTION = "com.union.player.play_or_pause";
    public static String PAUSE_ACTION = "com.union.player.pause";
    public static String NEXT_ACTION = "com.union.player.next";
    private static final int RELEASE_WAKELOCK = 2;
    private static final int FOCUSCHANGE = 4;
    private static final int FADEDOWN = 5;
    private static final int FADEUP = 6;
    public static final int PLAYBACKSERVICE_STATUS = 1;
    private static String TAG = "MediaPlayBackService";
    private AudioManager mAudioManager;
    private PowerManager.WakeLock mWakeLock;

    private static LocalMediaPlayer mPlayer;
    private IMediaPlayerCallBack mCallBack;
    private boolean mPausedByTransientLossOfFocus = false;
    private ImageView mCover;
    private static MusicInfo mCurrentMusicInfo;
    private static Context mContext;
    private NotificationCompat.Builder mNotificationBuilder;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
        mWakeLock.setReferenceCounted(false);
        mPlayer = new LocalMediaPlayer(this, playerListener);
        mNotificationBuilder = new NotificationCompat.Builder(mContext);
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            handleIntent(intent);
        }
        return START_STICKY;
    }

    /**
     * 处理Notification 控制按钮事件
     * @param intent
     */
    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        LogUtils.i(TAG, "handleIntent action: " + action);
        if (action == null || mCallBack == null) {
            return;
        }

        if (PLAY_OR_PAUSE_ACTION.equals(action)) {
            if (isPlaying()) {
                pause();
                sendPlayerCommand(PlayerCommand.PAUSE);
            } else {
                resume();
                sendPlayerCommand(PlayerCommand.RESUME);
            }
        } else if (NEXT_ACTION.equals(action)) {
            sendPlayerCommand(PlayerCommand.NEXT);
        }
    }

    private void sendPlayerCommand(int action) {
        if (mCallBack == null) {
            return;
        }
        try {
            mCallBack.onPlayCommand(action);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Handler mMediaplayerHandler = new Handler() {
        float mCurrentVolume = 1.0f;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FADEDOWN:
                    mCurrentVolume -= .05f;
                    if (mCurrentVolume > .2f) {
                        mMediaplayerHandler.sendEmptyMessageDelayed(FADEDOWN, 10);
                    } else {
                        mCurrentVolume = .2f;
                    }
                    mPlayer.setVolume(mCurrentVolume);
                    break;
                case FADEUP:
                    mCurrentVolume += .01f;
                    if (mCurrentVolume < 1.0f) {
                        mMediaplayerHandler.sendEmptyMessageDelayed(FADEUP, 10);
                    } else {
                        mCurrentVolume = 1.0f;
                    }
                    mPlayer.setVolume(mCurrentVolume);
                    break;
                case RELEASE_WAKELOCK:
                    mWakeLock.release();
                    break;

                case FOCUSCHANGE:
                    // This code is here so we can better synchronize it with the code that
                    // handles fade-in
                    switch (msg.arg1) {
                        case AudioManager.AUDIOFOCUS_LOSS:
                            LogUtils.i(TAG, "AudioFocus: received AUDIOFOCUS_LOSS");
                            if (isPlaying()) {
                                mPausedByTransientLossOfFocus = false;
                            }
                            pause();
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            mMediaplayerHandler.removeMessages(FADEUP);
                            mMediaplayerHandler.sendEmptyMessage(FADEDOWN);
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            LogUtils.i(TAG, "AudioFocus: received AUDIOFOCUS_LOSS_TRANSIENT");
                            if (isPlaying()) {
                                mPausedByTransientLossOfFocus = true;
                            }
                            pause();
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN:
                            LogUtils.i(TAG, "AudioFocus: received AUDIOFOCUS_GAIN");
                            if (!isPlaying() && mPausedByTransientLossOfFocus) {
                                mPausedByTransientLossOfFocus = false;
                                mCurrentVolume = 0f;
                                mPlayer.setVolume(mCurrentVolume);
                                mPlayer.start(); // also queues a fade-in
                            } else {
                                mMediaplayerHandler.removeMessages(FADEDOWN);
                                mMediaplayerHandler.sendEmptyMessage(FADEUP);
                            }
                            break;
                        default:
                            LogUtils.i(TAG, "Unknown audio focus change code");
                    }
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 播放音乐
     * @param url
     */
    private void play(String url) {
        mAudioManager.requestAudioFocus(mAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        mPlayer.play(url);
        //updateNotification();
    }

    /**
     * 播放专辑列表
     * @param tracks
     */
    private void playTracks(List<String> tracks) {
        //TODO
    }

    private static boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    private void next() {
    }

    private void prev() {
    }

    private void pause() {
        mPlayer.pause();
        updateNotification();
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
        updateNotification();
    }
    private void playMusic(MusicInfo music) {
        mCurrentMusicInfo = music;
        mPlayer.playMusic(mCurrentMusicInfo);
    }

    private void setCallBack(IMediaPlayerCallBack callBack) {
        this.mCallBack = callBack;
    }

    static class LocalMediaPlayer {
        public static final int SET_DATA_SOURECE_ERROR = 0x001;
        private MediaPlayer mediaPlayer;
        private Context mContext;
        private IPlayerListener listener;
        private WifiManager.WifiLock mWifiLock;
        public LocalMediaPlayer(Context context, IPlayerListener listener) {
            mediaPlayer = new MediaPlayer();
            this.listener = listener;
            this.mContext = context;
            mWifiLock = ((WifiManager) context.getSystemService(WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, this.getClass().getName());
            mWifiLock.setReferenceCounted(false);
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
                    acquireWifiLock();
                    mediaPlayer.start();
                    if (listener != null) {
                        listener.onPrepared(mp);
                    }
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

        public void setVolume(float vol) {
            mediaPlayer.setVolume(vol, vol);
        }

        public void play(String url) {
            try {
                acquireWifiLock();
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
            releaseWifiLock();
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
            releaseWifiLock();
        }

        public void release() {
            mediaPlayer.release();
            releaseWifiLock();
        }

        public void start() {
            acquireWifiLock();
            mediaPlayer.start();
        }

        private void acquireWifiLock() {
            if (!mWifiLock.isHeld()) {
                mWifiLock.acquire();
            }
        }

        private void releaseWifiLock() {
            if (mWifiLock.isHeld()) {
                mWifiLock.release();
            }
        }

        public void playMusic(MusicInfo mCurrentMusicInfo) {
            String url = mCurrentMusicInfo.getMusicUrl();
            if (url != null) {
                play(url);
            }
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

        @Override
        public void playMusic(MusicInfo music) {
            mService.get().playMusic(music);
        }


    }




    IPlayerListener playerListener = new IPlayerListener() {

        @Override
        public void onSeekComplete(MediaPlayer mp) {

        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            if (mCallBack != null) {
                try {
                    mCallBack.onPrepared();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            updateNotification();
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

    private void updateNotification() {
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.player_statusbar_layout);
        mRemoteViews.setImageViewResource(R.id.cover, R.drawable.example);
        Notification notification = new Notification();
        notification.contentView = mRemoteViews;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.icon = R.drawable.example;
        notification.contentIntent =
                PendingIntent.getActivity(this, 0, new Intent("com.android.music.PLAYBACK_VIEWER")
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                        0);
        if (mRemoteViews != null && mCurrentMusicInfo != null && mCurrentMusicInfo.getMusicCover() != null) {
            Picasso.with(mContext).load(mCurrentMusicInfo.getMusicCover())
                    .config(Bitmap.Config.ARGB_8888)
                    .into(mRemoteViews, R.id.cover, PLAYBACKSERVICE_STATUS, notification);
        }

        if (mRemoteViews != null && mCurrentMusicInfo != null && mCurrentMusicInfo.getTrackName() != null && mCurrentMusicInfo.getMusicTitle() != null) {
            mRemoteViews.setTextViewText(R.id.trackname, mCurrentMusicInfo.getTrackName());
            mRemoteViews.setTextViewText(R.id.artistalbum, mCurrentMusicInfo.getMusicTitle());
        }

        if (isPlaying()) {
            mRemoteViews.setImageViewResource(R.id.btn_play, R.drawable.ic_pause_circle_outline_black_36dp);
        } else {
            mRemoteViews.setImageViewResource(R.id.btn_play, R.drawable.ic_play_circle_outline_black_36dp);
        }

        ComponentName componentName = new ComponentName(this, MediaPlayBackService.class);

        Intent nextIntent = new Intent(NEXT_ACTION);
        nextIntent.setComponent(componentName);
        PendingIntent nextPendingIntent= PendingIntent.getService(this, R.id.btn_next, nextIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_next, nextPendingIntent);

        Intent pauseOrPlayIntent = new Intent(PLAY_OR_PAUSE_ACTION);
        pauseOrPlayIntent.setComponent(componentName);
        PendingIntent pauseOrPlayPendingIntent= PendingIntent.getService(this, R.id.btn_play, pauseOrPlayIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_play, pauseOrPlayPendingIntent);

        startForeground(PLAYBACKSERVICE_STATUS, notification);
    }
}
