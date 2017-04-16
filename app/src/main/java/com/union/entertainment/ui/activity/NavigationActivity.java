package com.union.entertainment.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.union.commonlib.ui.activity.BaseActivity;
import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.commonlib.utils.LogUtils;
import com.union.entertainment.R;
import com.union.entertainment.ui.fragment.FragmentFactory;


/**
 * Created by zhouxiaming on 2016/3/4.
 */

public class NavigationActivity extends BaseActivity implements DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener, BaseFragment.OnFragmentInteractionListener {
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private BaseFragment mCurrentFragment;
    Toolbar toolbar;
    private MenuItem clickedItem;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_layout);
        initToolBar();
        initDrawer();
        initNavigationMenuItem();
        switchFragment(FragmentFactory.FRAGMENT_LOCAL_PIC);
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerListener(this);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerToggle.syncState();
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    private void initNavigationMenuItem() {
        TextView spotifyNews = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.music_spotify)).findViewById(R.id.msg);
        spotifyNews.setText("hello");
    }



    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.pic_local);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        //getSupportActionBar().setHomeAsUpIndicator(upArrow);
        //toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }

    }

    private void showToolBar(boolean show) {
        if (show) {
            toolbar.setVisibility(View.VISIBLE);
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }

    private void switchFragment(int position) {
        BaseFragment f = FragmentFactory.createFragment(position);
        if (f != mCurrentFragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (mCurrentFragment != null && mCurrentFragment.isAdded()) {
                transaction.hide(mCurrentFragment);
            }
            if ( !f.isAdded()) {
                transaction.add(R.id.frame_content, f, f.getClass().toString()).commit();
            } else {
                transaction.show(f).commit();
            }

            mCurrentFragment = f;
        }
    }

    /**
     * Drawer的回调方法，需要在该方法中对Toggle做对应的操作
     */
    @Override
    public void onDrawerOpened(View drawerView) {// 打开drawer
        invalidateOptionsMenu();
        mDrawerToggle.onDrawerOpened(drawerView);
    }

    @Override
    public void onDrawerClosed(View drawerView) {// 关闭drawer
        LogUtils.i(TAG, "onDrawerClosed");
        mDrawerToggle.onDrawerClosed(drawerView);
        invalidateOptionsMenu();
        if (clickedItem != null) {
            switchFragment(getFragmentPosition(clickedItem.getItemId()));
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {// drawer滑动的回调
        LogUtils.i(TAG, "onDrawerSlide");
        mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
    }

    @Override
    public void onDrawerStateChanged(int newState) {// drawer状态改变的回调
        LogUtils.i(TAG, "onDrawerStateChanged");
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        setToolBarTitle(item.getTitle());
        item.setCheckable(true);//设置选项可选
        item.setChecked(true);
        clickedItem = item;
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private int getFragmentPosition(int id) {
        int position = FragmentFactory.FRAGMENT_LOCAL_PIC;
        switch (id) {
            case R.id.pic_local:
                position = FragmentFactory.FRAGMENT_LOCAL_PIC;
                showToolBar(true);
                break;
            case R.id.pic_network:
                position = FragmentFactory.FRAGMENT_NETWORK_PIC;
                break;
            case R.id.fm_douban:
                position = FragmentFactory.FRAGMENT_DOUBAN_FM;
                showToolBar(true);
                break;
            case R.id.music_local:
                position = FragmentFactory.FRAGMENT_LOCAL_MUSIC;
                break;
            case R.id.music_spotify:
                position = FragmentFactory.FRAGMENT_SPOTIFY;
                break;
            case R.id.music_qq:
                position = FragmentFactory.FRAGMENT_QQ;
                break;
            default:
                break;

        }
        return position;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        } else if (mCurrentFragment != null && !mCurrentFragment.isHidden()){
            if (!mCurrentFragment.onBackPress()) {
                return;
            }
        }
        super.onBackPressed();
    }

    private void setToolBarTitle(CharSequence title) {
        toolbar.setTitle(title);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        if(!false){
            setBackTask(false);
        }else {
            setBackTask(true);
        }
    }

    private void setBackTask(boolean isRealFinished) {
        if (!isRealFinished) {
            this.moveTaskToBack(true);
        } else {
            super.finish();
        }
    }
}
