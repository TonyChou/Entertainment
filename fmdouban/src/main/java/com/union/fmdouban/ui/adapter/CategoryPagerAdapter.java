package com.union.fmdouban.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.fmdouban.CategoryType;
import com.union.fmdouban.ui.fragment.DoubanFMFragment;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by zhouxiaming on 16/3/23.
 */
public class CategoryPagerAdapter extends FragmentPagerAdapter {
    List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
    List<CategoryType> categoryTypes = new ArrayList<CategoryType>();
    public CategoryPagerAdapter(FragmentManager fm) {
        super(fm);
        EnumSet<CategoryType> enumSet = EnumSet.allOf(CategoryType.class);
        for (CategoryType type : enumSet) {
            categoryTypes.add(type);
            BaseFragment fragment = DoubanFMFragment.newInstance();
            fragmentList.add(fragment);
        }
    }


    @Override
    public int getCount() {
        return categoryTypes.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categoryTypes.get(position).getName();
    }
}
