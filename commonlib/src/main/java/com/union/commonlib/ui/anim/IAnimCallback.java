package com.union.commonlib.ui.anim;

import android.view.View;

/**
 * Created by zhouxiaming on 2016/3/11.
 */
public interface IAnimCallback {
    public void handleClickEvent(View v);
    public void animationEnd(View v);
    public void animationStart(View v);
    public void animationRepeat(View v);
}
