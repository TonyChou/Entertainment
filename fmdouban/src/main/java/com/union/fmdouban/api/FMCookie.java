package com.union.fmdouban.api;

import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
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

    /**
     * 解析Bid
     * @param response
     * @return
     */
    public static void parserBid(Response response) {
        List<String> cookie = response.headers("set-cookie");
        String bid = null;
        if (cookie != null && cookie.size() > 0) {
            for (String str : cookie) {
                String[] array = str.split(";");
                if (array != null && array.length > 0) {
                    for(String s : array) {
                        if (s.contains("bid")) {
                            bid = s.split("=")[1].replaceAll("\"", "");
                            break;
                        }
                    }
                }
            }
        }

        if (bid != null) {
            Cookie.put(BID_KEY, bid);
        }
    }
}
