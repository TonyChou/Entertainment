package com.union.entertainment.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.union.entertainment.R;
import com.union.entertainment.ui.fragment.BaseFragment;
import com.union.entertainment.ui.fragment.FragmentFactory;


/**
 * Created by zhouxiaming on 2016/3/4.
 */

public class NavigationActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener, BaseFragment.OnFragmentInteractionListener {
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private Fragment mCurrentFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_layout);
        initDrawer();
        initToolBar();
        initNavigationMenuItem();
        initPageView();
        switchFragment(FragmentFactory.FRAGMENT_LOCAL_PIC);
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setDrawerListener(this);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    protected void initPageView() {
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, getToolBar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }


    private void initNavigationMenuItem() {
        TextView spotifyNews = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.music_spotify)).findViewById(R.id.msg);
        spotifyNews.setText("hello");
    }

    private Toolbar getToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        return toolbar;
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.pic_local);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void switchFragment(int position) {
        Fragment f = FragmentFactory.createFragment(position);
        if (f != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            mCurrentFragment = f;
            fragmentManager.beginTransaction().replace(R.id.frame_content, f).commit();
        }
    }

    /**
     * Drawer的回调方法，需要在该方法中对Toggle做对应的操作
     */
    @Override
    public void onDrawerOpened(View drawerView) {// 打开drawer
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerClosed(View drawerView) {// 关闭drawer
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {// drawer滑动的回调
    }

    @Override
    public void onDrawerStateChanged(int newState) {// drawer状态改变的回调
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        setToolBarTitle(item.getTitle());
        item.setChecked(true);
        switchFragment(getFragmentPosition(item.getItemId()));
        return true;
    }

    private int getFragmentPosition(int id) {
        int position = FragmentFactory.FRAGMENT_LOCAL_PIC;
        switch (id) {
            case R.id.pic_local:
                position = FragmentFactory.FRAGMENT_LOCAL_PIC;
                break;
            case R.id.pic_network:
                position = FragmentFactory.FRAGMENT_NETWORK_PIC;
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
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    private void setToolBarTitle(CharSequence title) {
        getToolBar().setTitle(title);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
