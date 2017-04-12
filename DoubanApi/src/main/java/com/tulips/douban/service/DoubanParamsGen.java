package com.tulips.douban.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouxiaming on 2017/4/11.
 */
public class DoubanParamsGen {
    /**
     * 构建获取所有的channels参数
     * @return
     */
    public static Map<String, String> genGetAppChannelsParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("app_name", DoubanConstant.APP_NAME);
        params.put("apikey", DoubanConstant.APP_KEY);
        params.put("client", DoubanConstant.CLIENT);
        params.put("udid", DoubanConstant.UDID);
        params.put("push_device_id", DoubanConstant.PUSH_DEVICE_ID);
        params.put("version", DoubanConstant.VERSION);
        return params;
    }
}
