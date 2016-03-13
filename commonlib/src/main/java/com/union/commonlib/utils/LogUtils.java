package com.union.commonlib.utils;

import android.util.Log;

import com.union.commonlib.Constant;


/**
 * Created by zhouxiaming on 2015/4/14.
 */

public class LogUtils {
    private static String TAG = "LogUtils";

    public static void e(String tag, String msg){
        if (msg == null) {
            return;
        }
        if (Constant.IS_DEBUG) {
            Log.e(tag == null? TAG : tag, msg);
        }
    }

    public static void i(String msg){
        if (msg == null) {
            return;
        }
        if (Constant.IS_DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg){
        if (msg == null) {
            return;
        }
        if (Constant.IS_DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void d(String msg){
        if (msg == null) {
            return;
        }
        if (Constant.IS_DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg){
        if (msg == null) {
            return;
        }
        if (Constant.IS_DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void i(String tag, String msg){
        if (msg == null) {
            return;
        }
        if (Constant.IS_DEBUG) {
            Log.i(tag == null? TAG : tag, msg);
        }
    }

    public static void w(String tag, String msg){
        if (msg == null) {
            return;
        }
        if (Constant.IS_DEBUG) {
            Log.w(tag == null? TAG : tag, msg);
        }
    }

    public static void d(String tag, String msg){
        if (msg == null) {
            return;
        }
        if (Constant.IS_DEBUG) {
            Log.d(tag == null? TAG : tag, msg);
        }
    }
}
