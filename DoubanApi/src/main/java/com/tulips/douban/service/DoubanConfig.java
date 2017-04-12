package com.tulips.douban.service;

/**
 * Created by zhouxiaming on 2017/4/11.
 */
public class DoubanConfig {
    private String mAppKey;
    private String mAppName;
    private String mClient;
    private String mUdid;
    private String mDeviceId;

    /**
     * 配置APP key
     * @param appKey
     * @return
     */
    public DoubanConfig addAppKey(String appKey) {
        this.mAppKey = appKey;
        if (this.mAppKey != null) {
            DoubanConstant.APP_KEY = this.mAppKey;
        }
        return this;
    }

    /**
     * 配置APP name
     * @param appName
     * @return
     */
    public DoubanConfig addAppName(String appName) {
        this.mAppName = appName;
        if (this.mAppName != null) {
            DoubanConstant.APP_NAME = this.mAppName;
        }
        return this;
    }

    /**
     * 配置客户端信息
     * s:mobile|v:4.6.7|y:android 6.0.1|f:647|m:Xiaomi|d:6aba7f108504dd47509e335b0b6335c5fee027a3|e:xiaomi_mi_note_lte
     * @param clientInfo
     * @return
     */
    public DoubanConfig addClientInfo(String clientInfo) {
        this.mClient = clientInfo;
        if (this.mClient != null) {
            DoubanConstant.CLIENT = this.mClient;
        }
        return this;
    }

    /**
     * 配置udid，此参数作用暂时不详，可以UUID工具生成
     * @param udid
     * @return
     */
    public DoubanConfig addUDID(String udid) {
        this.mUdid = udid;
        if (this.mUdid != null) {
            DoubanConstant.UDID = this.mUdid;
        }
        return this;
    }

    /**
     * 配置设备唯一标识
     * @param deviceId
     * @return
     */
    public DoubanConfig addDevideId(String deviceId) {
        this.mDeviceId = deviceId;
        if (this.mDeviceId != null) {
            DoubanConstant.PUSH_DEVICE_ID = this.mDeviceId;
        }
        return this;
    }
}
