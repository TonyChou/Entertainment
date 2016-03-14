package com.union.commonlib.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.view.TintableBackgroundView;


/**
 * Created by zhouxiaming on 16/3/12.
 */
public class TintUtils {
    public static void setBackgroundTint(Context context, TintableBackgroundView view, int color) {
        ColorStateList csl=(ColorStateList)context.getResources().getColorStateList(color);
        view.setSupportBackgroundTintList(csl);
    }
}
