// IMediaPlayerCallBack.aidl
package com.union.player;

// Declare any non-default types here with import statements

interface IMediaPlayerCallBack {
     //播放出错
     void onError(String errorMsg);
     //播放结束
     void onFinish();
     //播放已经准备好了
     void onPrepared();
     //通知栏播放控制
     void onPlayCommand(int action);
}
