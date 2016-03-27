package com.union.fmdouban.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.fmdouban.api.bean.FMChannelType;
import com.union.fmdouban.ui.fragment.FMChannelList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 16/3/23.
 */
public class ChannelCategoryPagerAdapter extends FragmentStatePagerAdapter {
    List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
    List<FMChannelType> typeList;
    public ChannelCategoryPagerAdapter(FragmentManager fm, List<FMChannelType> types) {
        super(fm);
        typeList = types;
        for (int i = 0; i < typeList.size(); i++) {
            fragmentList.add(FMChannelList.newInstance());
        }
    }

    @Override
    public int getCount() {
        return typeList.size();
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
        return typeList.get(position).getName();
    }
}
