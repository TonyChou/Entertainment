package com.union.fmdouban.play;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;


import com.union.commonlib.utils.LogUtils;

import java.io.IOException;

/**
 * Created by zhouxiaming on 2015/4/16.
 */

public class FMMediaPlayer implements IPlayer {
    private String TAG = "FMMediaPlayer";
    private static FMMediaPlayer instance;
    private static PlayerListener playerListener;
    private MediaPlayer mediaPlayer;
    private FMMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        init();
    }
    public synchronized static FMMediaPlayer getInstance() {
        if (instance == null) {
            instance = new FMMediaPlayer();
        }
        return instance;
    }

    /**
     *初始化
     */
    public void init() {
        //设置播放结束监听
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (playerListener != null) {
                    playerListener.onFinish();
                }
            }
        });

        //设置错误回调监听
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                stop();
                if (playerListener != null) {
                    playerListener.onError("MediaPlayer is error!");
                }
                return false;
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (playerListener != null) {
                    playerListener.onPrepared();
                }
                mediaPlayer.start();
            }
        });


        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                LogUtils.i(TAG, "onSeekComplete ==== " + mp.getCurrentPosition());
            }
        });

        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                LogUtils.i(TAG, "onInfo ==== what = " + what + "  extra = " + extra);
                return false;
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                LogUtils.i(TAG, "onBufferingUpdate percent: " + percent);
            }
        });




    }


    /**
     * 设置监听
     * @param listener
     */
    public void setPlayerListener(PlayerListener listener) {
        this.playerListener = listener;
    }


    @Override
    public void play(String url) {
        playStream(url, AudioManager.STREAM_MUSIC);
    }

    @Override
    public void playStream(String url, int streamType) {
        try {
            LogUtils.i(TAG, "playStream:  [url = " + url + " ]");
            LogUtils.i(TAG, "playStream:  [streamType = " + streamType + " ]");
            resetPlayer();
            mediaPlayer.setAudioStreamType(streamType);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();

            //seekTo(mediaPlayer.getDuration() - 20000);
        } catch (Exception e) {
            if (playerListener != null) {
                playerListener.onError("Play IOException: " + url);
            }
            e.printStackTrace();
        }
    }

    private void resetPlayer() {
        if ( mediaPlayer.isPlaying()) {
            stop();
        } else {
            mediaPlayer.reset();
        }
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void resume() {
        mediaPlayer.start();
    }

    @Override
    public void stop() {
        Log.i(TAG, "Stop media player");
        mediaPlayer.stop();
        mediaPlayer.reset();//必须加上，要不然下次播放会出问题
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public void seekTo(int position) {
        LogUtils.i(TAG, "seekTo: " + position);
        mediaPlayer.seekTo(position);
    }

    @Override
    public void release() {
//        mediaPlayer.stop();
//        mediaPlayer.release();
    }



    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

}
