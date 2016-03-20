package com.union.fmdouban.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouxiaming on 16/3/20.
 */
public class FMCookie {

    public static final String BID_KEY = "bid";
    public static String BID_FORMAT = "bid=\"%s\";";
    public static final String FLAG = "flag";
    public static String FLAG_FORMAT = "flag=\"%s\";";
    public static Map<String, String> Cookie = new HashMap<String, String>();
    static {
        Cookie.put(BID_KEY, "RboHwvYBJtg");
        Cookie.put(FLAG, "ok");
    }

    /**
     * 拼接Cookie
     * @return
     */
    public static String getCookie() {
        StringBuffer cookieBuild = new StringBuffer();
        cookieBuild.append(String.format(FLAG_FORMAT, Cookie.get(FLAG)))
                   .append(String.format(BID_FORMAT, Cookie.get(BID_KEY)));
        return cookieBuild.toString();
    }
}
