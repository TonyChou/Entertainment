package com.tulips.douban.service;


import com.tulips.douban.Logger;
import okhttp3.Headers;

import java.util.*;

/**
 * Created by zhouxiaming on 2017/4/11.
 */
public class CookieManager {
    public static String DOUBAN_BID = "X-DOUBAN-NEWBID";
    public static String DOUBAN_APP = "X-DAE-App";
    private static String TAG = "CookieManager";
    /**
     * 缓存Cookie信息
     */
    private static Map<String, String> cacheValues = new HashMap<String, String>();

    public static void updateCookieCache(Headers headers) {
        Set<String> names = headers.names();
        Iterator<String> iterator = names.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            List<String> values = headers.values(name);
            if (values != null && values.size() > 0) {
                cacheValues.put(name, values.get(0));
            }
            Logger.print(TAG, "Header name: " + name);
        }
    }
}
