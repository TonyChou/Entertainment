package com.union.entertainment;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.union.commonlib.multidex.MultiDex;
import com.union.fmdouban.api.data.ChannelLoader;

/**
 * Created by zhouxiaming on 2016/4/7.
 */

public class UnionApplication extends Application{
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadDataFromServer();
    }

    /**
     * 从网络上预加载数据
     */
    private void loadDataFromServer() {
        //加载豆瓣频道数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChannelLoader.loadChannelData();
            }
        }).start();
    }
}
