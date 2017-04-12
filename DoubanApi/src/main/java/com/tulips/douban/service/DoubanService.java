package com.tulips.douban.service;

import com.tulips.douban.model.ChannelsPage;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

import java.util.Map;

/**
 * Created by zhouxiaming on 2017/4/11.
 */
public interface DoubanService {
    @GET("v2/fm/app_channels")
    Observable<ChannelsPage> appChannels(@QueryMap Map<String, String> params);
}
