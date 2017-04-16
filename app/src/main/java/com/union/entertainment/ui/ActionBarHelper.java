package com.union.entertainment.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.union.commonlib.ui.ActionBarPage;
import com.union.entertainment.R;
import com.union.entertainment.ui.activity.NavigationActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouxiaming on 2017/4/15.
 */

public class ActionBarHelper {
    private static Map<Integer, SoftReference<Drawable>> sBackgroundCache;
    private Toolbar mToolBar;
    private NavigationActivity mActivity;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    public ActionBarHelper(NavigationActivity activity, Toolbar toolBar, DrawerLayout drawerLayout, NavigationView navigationView, ActionBarDrawerToggle drawerToggle) {
        this.mActivity = activity;
        this.mToolBar = toolBar;
        this.mDrawerLayout = drawerLayout;
        this.mNavigationView = navigationView;
        this.mDrawerToggle = drawerToggle;
        sBackgroundCache = new HashMap<>();
    }

    /**
     * 设置ToolBar背景色
     * @param color
     */
    public void setToolBarBackgroundColor(@ColorRes int color) {
        mToolBar.setBackgroundColor(mActivity.getResources().getColor(color));
    }

    /**
     * 根据不同的页面更新ActionBar样式
     * @param actionBarPage
     */
    public void syncToolBarStatus(ActionBarPage actionBarPage) {
        if (actionBarPage == ActionBarPage.DOUBAN_FM_MAIN_PAGE) {
            updateDouBanMainPageActionBar();
        } else if (actionBarPage == ActionBarPage.LOCAL_PHOTO_PAGE) {
            updateLocalPhotoPageActionBar();
        }
    }

    /**
     * 豆瓣FM主页的ActionBar
     */
    private void updateDouBanMainPageActionBar() {
        setToolBarBackgroundColor(R.color.douban_colorPrimary);
        //ViewCompat.setAlpha(mToolBar, .0f);
        mToolBar.setVisibility(View.GONE);
        setStatusBarColor(R.color.douban_colorPrimary);
    }

    private void updateLocalPhotoPageActionBar() {
        setToolBarBackgroundColor(R.color.main_colorPrimary);
        //ViewCompat.setAlpha(mToolBar, .0f);
        mToolBar.setVisibility(View.VISIBLE);
        setStatusBarColor(R.color.main_colorPrimary);
    }

    private void setStatusBarColor(int color) {
        mDrawerLayout.setStatusBarBackgroundColor(mActivity.getResources().getColor(color));
    }

    private static Drawable getBackgroundColorDrawable(int color) {
        SoftReference<Drawable> ref = (SoftReference) sBackgroundCache.get(Integer.valueOf(color));
        if (ref == null || ref.get() == null) {
            ref = new SoftReference(new PaintDrawable(color));
            sBackgroundCache.put(Integer.valueOf(color), ref);
        }
        return (Drawable) ref.get();
    }
}
