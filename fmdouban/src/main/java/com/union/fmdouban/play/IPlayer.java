package com.union.fmdouban.play;

/**
 * Created by zhouxiaming on 2015/4/16.
 */
public interface IPlayer {
    public void play(String url);
    public void pause();
    public void resume();
    public void stop();
    public int getCurrentPosition();
    public int getDuration();
    public void seekTo(int position);
    public void release();

    /**
     * @param url
     * @param streamType : STREAM_RING, STREAM_MUSIC
     */
    public void playStream(String url, int streamType); //指定类型来播放
}
