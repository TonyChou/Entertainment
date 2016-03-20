package com.union.fmdouban.api;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by zhouxiaming on 16/3/20.
 */
public class ApiTest {
    public static void main(String[] args) {
       //testGetBid();
        //testGetChannels();
        test();

    }


    static void test() {
        OkHttpClient client = new OkHttpClient();

        String SONG_URL_DOUBAN_FM = "http://douban.fm/j/mine/playlist?type=n&pt=0.0&channel=1&pb=128&from=mainsite&r=ffdf27397";
        String url = "http://douban.fm/";
        String channelUrl = "https://www.douban.com/j/app/radio/channels";
        String moreChannel = "http://douban.fm/j/explore/genre?gid=326&start=0";
        String cookie = "bid=\"0K2GlfFr6y0\";";
        Request request = new Request.Builder()
                .url(moreChannel)
                //.addHeader("Host", "douban.fm")
//                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//                //.addHeader("Accept-Encoding", "gzip, deflate, sdch")
//                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
//                .addHeader("Cache-Control", "max-age=0")
//                .addHeader("Connection", "keep-alive")
//                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("Cookie", cookie)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }
}
