package com.union.fmdouban.api;


import com.squareup.okhttp.Headers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouxiaming on 16/3/20.
 */
public class FMHeader {
    public static final String COOKIE = "Cookie";
    public static final FMPair<String, String> ACCEPT_TEXT = new FMPair<String, String>("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    public static final FMPair<String, String> ACCEPT_ANYTHING = new FMPair<String, String>("Accept", "*/*");

    public static final FMPair<String, String> ACCEPT_CENCODING_GZIP = new FMPair<String, String>("Accept-Encoding", "gzip, deflate, sdch");
    public static final FMPair<String, String> ACCEPT_LANGUAGE = new FMPair<String, String>("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
    public static final FMPair<String, String> CACHE_CONTROL = new FMPair<String, String>("Cache-Control", "max-age=0");
    public static final FMPair<String, String> CONNECTION = new FMPair<String, String>("Connection", "keep-alive");
    public static final FMPair<String, String> UPGRATE_INSECURE_REQUESTS = new FMPair<String, String>("Upgrade-Insecure-Requests", "1");
    public static final FMPair<String, String> CONTENT_TYPE = new FMPair<String, String>("Content-Type", "application/x-www-form-urlencoded");
    public static final FMPair<String, String> USER_AGENT = new FMPair<String, String>("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");

    /**
     * 构建获取Html content 的header
     * @return
     */
    public static Headers genGetHtmlContentHeader() {
        Map<String, String> headMap = new HashMap<String, String>();
        headMap.put(USER_AGENT.key, USER_AGENT.value);
        headMap.put(CONTENT_TYPE.key, CONTENT_TYPE.value);
        headMap.put(COOKIE, FMCookie.getCookie());
        Headers headers = Headers.of(headMap);
        return headers;
    }

    /**
     * 构建获取bid的requestHeader
     * @return
     */
    public static Headers genGetBidRequestHeader() {
        Map<String, String> headMap = new HashMap<String, String>();
        headMap.put(ACCEPT_TEXT.key, ACCEPT_TEXT.value);
        headMap.put(ACCEPT_CENCODING_GZIP.key, ACCEPT_CENCODING_GZIP.value);
        headMap.put(ACCEPT_LANGUAGE.key, ACCEPT_LANGUAGE.value);
        headMap.put(CACHE_CONTROL.key, CACHE_CONTROL.value);
        headMap.put(CONNECTION.key, CONNECTION.value);
        headMap.put(UPGRATE_INSECURE_REQUESTS.key, UPGRATE_INSECURE_REQUESTS.value);
        headMap.put(CONTENT_TYPE.key, CONTENT_TYPE.value);
        headMap.put(USER_AGENT.key, USER_AGENT.value);
        Headers headers = Headers.of(headMap);
        return headers;
    }



    /**
     * 构建获取channels 的requestHeader
     * @return
     */
    public static Headers genRequestHeader() {
        Map<String, String> headMap = new HashMap<String, String>();
        //headMap.put(ACCEPT_ANYTHING.key, ACCEPT_ANYTHING.value);
        headMap.put(USER_AGENT.key, USER_AGENT.value);
        headMap.put(CONTENT_TYPE.key, CONTENT_TYPE.value);
        headMap.put(COOKIE, FMCookie.getCookie());
        Headers headers = Headers.of(headMap);
        return headers;
    }


}
