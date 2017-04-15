package com.union.entertainment.ui;

import android.support.annotation.ColorRes;
import android.support.v7.widget.Toolbar;

import com.union.entertainment.ui.activity.NavigationActivity;

/**
 * Created by zhouxiaming on 2017/4/15.
 */

public class ActionBarHelper {
    private Toolbar mToolBar;
    private static ActionBarHelper instance;
    private NavigationActivity mActivity;
    public ActionBarHelper(NavigationActivity activity, Toolbar toolBar) {
        this.mActivity = activity;
        this.mToolBar = toolBar;
    }

    /**
     * 设置ToolBar背景色
     * @param color
     */
    public void setToolBarBackgroundColor(@ColorRes int color) {
        mToolBar.setBackgroundColor(mActivity.getResources().getColor(color));
    }

}
