package com.tulips.douban;

/**
 * Created by zhouxiaming on 2017/4/11.
 */
public class Logger {
    public static boolean isDebug = true;
    public static void print(String tag, String message) {
        if (isDebug) {
            System.out.println(tag + ":  " + message);
        }
    }
}
