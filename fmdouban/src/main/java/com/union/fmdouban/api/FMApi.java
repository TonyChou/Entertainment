package com.union.fmdouban.api;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.union.commonlib.utils.LogUtils;

import java.io.IOException;

/**
 * Created by zhouxiaming on 16/3/20.
 */
public class FMApi {
    private String TAG = "FMApi";
    private static final int TIMEOUT = 10;
    private OkHttpClient client;
    private static FMApi instance = new FMApi();
    private FMParser parser;
    private FMApi() {
        client = new OkHttpClient();
        parser = new FMParser();
    }

    public static FMApi getInstance() {
        return instance;
    }


    /**
     * 第一步
     * 获取bid
     * @return
     */
    public void getFMBid(FMCallBack callBack) {
        Request request = new Request.Builder().url(FMUrl.HOST).headers(FMHeader.genGetBidRequestHeader()).build();
        sendRequest(request, callBack);
    }

    /**
     * 获取频道列表
     * @param callBack
     */
    public void getFmChannels(FMCallBack callBack) {
        Request request = new Request.Builder().url(FMUrl.FM_CHANNELS_URL).headers(FMHeader.genRequestHeader()).build();
        sendRequest(request, callBack);
    }

    /**
     * 获取歌曲
     * @param channelId
     * @param sid
     * @param reportType
     * @param callBack
     */
    public void reportAndGetPlayList(String channelId, String sid, String reportType, FMCallBack callBack) {
        String url = String.format(FMUrl.FM_PLAYLIST_URL, reportType, channelId);
        if (!reportType.equals("n") && sid != null) {
            url = url + "&sid=" + sid;
        }

        LogUtils.i(TAG, "reportAndGetPlayList url: " + url);
        Request request = new Request.Builder().url(url).headers(FMHeader.genRequestHeader()).build();
        sendRequest(request, callBack);
    }



    private ExecuteResult sendRequest(final Request request, final FMCallBack callBack) {
        final ExecuteResult result = new ExecuteResult();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                result.setResult(ExecuteResult.FAILED);
                callBack.onRequestResult(result);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response)  {
                try {
                    FMCookie.parserBid(response);
                    result.setResponseString(response.body().string());
                    result.setResponse(response);
                    result.setResult(ExecuteResult.OK);
                    LogUtils.i(TAG, "Response: " + result.getResponseString());
                } catch (IOException e) {
                    result.setResult(ExecuteResult.FAILED);
                    e.printStackTrace();
                }
                callBack.onRequestResult(result);
            }
        });
        return result;
    }

}
