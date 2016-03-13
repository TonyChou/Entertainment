package com.union.fmdouban.play;

/**
 * 播放器监听
 * Created by zhouxiaming on 2015/4/16.
 */

public interface PlayerListener {
    //播放出错
    public void onError(String errorMsg);
    //播放结束
    public void onFinish();
    //播放已经准备好了
    public void onPrepared();

}
