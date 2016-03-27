package com.union.fmdouban.api;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Set;

/**
 * Created by zhouxiaming on 16/3/20.
 */
public class FMParser {
    /**
     * 解析Bid
     * @param response
     * @return
     */
    public String parserBid(Response response) {
        Headers headers = response.headers();
        Set<String> name = headers.names();
        return headers.get("Set-Cookie");
    }

    /**
     *  解析频道列表
     * @param response
     * @return
     */
    public String parserChannels(Response response) {
        try {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
