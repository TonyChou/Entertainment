package com.union.net;

import android.support.v4.util.ArrayMap;

import com.union.commonlib.utils.LogUtils;

/**
 * Created by zhouxiaming on 2017/4/12.
 */

public class ApiClient {
    private static ArrayMap<String, RetrofitClient> clientsMap = new ArrayMap<String, RetrofitClient>();

    /**
     * 获取豆瓣FM Api 请求的client对象
     * @param host
     * @return
     */
    public static RetrofitClient getDoubanAPiClient(String host) {
        RetrofitClient client = clientsMap.get(host);
        if (client == null) {
            client = new RetrofitClient(host, LogUtils.isDebug, true, true);
            clientsMap.put(host, client);
        }
        return client;
    }

}
