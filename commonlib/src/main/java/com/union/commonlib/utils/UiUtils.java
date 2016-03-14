package com.union.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by zhouxiaming on 2016/3/7.
 */

public class UiUtils {
    /**
     * 获取屏幕宽高
     * @param context
     * @return
     */
    public static int[] getScreenWidthAndHeight(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int[] screen = new int[2];
        screen[0] = metrics.widthPixels;
        screen[1] = metrics.heightPixels;
        return screen;
    }
}
