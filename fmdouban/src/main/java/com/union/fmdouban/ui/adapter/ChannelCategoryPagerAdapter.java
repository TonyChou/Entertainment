package com.union.fmdouban.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.union.fmdouban.ui.fragment.FMChannelsFragment;
import com.union.fmdouban.ui.fragment.FMPlayerFragment;

/**
 * Created by zhouxiaming on 16/3/23.
 */
public class ChannelCategoryPagerAdapter extends FragmentPagerAdapter {

    private String[] mCategory = new String[]{"热门", "经典", "怀旧", "音乐", "电影", "咖啡", "hello android"};

    public ChannelCategoryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return mCategory.length;
    }

    @Override
    public Fragment getItem(int position) {
        return FMChannelsFragment.newInstance();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCategory[position];
    }
}
