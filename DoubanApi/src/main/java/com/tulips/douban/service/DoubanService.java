package com.tulips.douban.service;

import com.google.gson.JsonObject;
import com.tulips.douban.model.ChannelsPage;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

import java.util.Map;

/**
 * Created by zhouxiaming on 2017/4/11.
 */
public interface DoubanService {
    @GET("v2/fm/app_channels")
    Call<ChannelsPage> appChannels(@QueryMap Map<String, String> params);
}
