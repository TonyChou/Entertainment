package com.tulips.douban.service;

import android.support.v4.util.ArrayMap;

import com.tulips.douban.model.ChannelsPage;
import com.tulips.douban.model.PlayerPage;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import java.util.Map;

/**
 * Created by zhouxiaming on 2017/4/11.
 */
public interface DoubanService {
    @GET("v2/fm/app_channels")
    Observable<ChannelsPage> appChannels(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("v2/fm/playlist")
    Observable<PlayerPage> playList(@FieldMap Map<String, Object> params);
}
