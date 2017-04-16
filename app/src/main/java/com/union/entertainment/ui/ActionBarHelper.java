package com.union.entertainment.ui;

import android.support.annotation.ColorRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.union.commonlib.ui.ActionBarPage;
import com.union.entertainment.R;
import com.union.entertainment.ui.activity.NavigationActivity;

/**
 * Created by zhouxiaming on 2017/4/15.
 */

public class ActionBarHelper {
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
        }
    }

    /**
     * 豆瓣FM主页的ActionBar
     */
    private void updateDouBanMainPageActionBar() {
        setToolBarBackgroundColor(R.color.douban_colorPrimary);
        //ViewCompat.setAlpha(mToolBar, .0f);
    }
}
