package com.union.fmdouban.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.fmdouban.api.bean.FMChannelType;
import com.union.fmdouban.ui.fragment.FMChannelList;
import com.union.fmdouban.ui.fragment.FMChannelListNew;
import com.union.fmdouban.ui.listener.ChannelSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 16/3/23.
 */
public class ChannelCategoryPagerAdapter extends FragmentStatePagerAdapter {
    List<FMChannelList> fragmentList = new ArrayList<FMChannelList>();
    List<FMChannelType> typeList;
    public ChannelCategoryPagerAdapter(FragmentManager fm, List<FMChannelType> types, ChannelSelectedListener channelSelectedListener) {
        super(fm);
        typeList = types;
        for (int i = 0; i < typeList.size(); i++) {
            //FMChannelList channelList = FMChannelList.newInstance();
            FMChannelListNew fragment = FMChannelListNew.newInstance();
            fragment.setChannelType(typeList.get(i));
            fragment.setChannelSelectedListener(channelSelectedListener);
            fragmentList.add(fragment);
        }
    }

    public List<FMChannelList> getFragmentList() {
        return fragmentList;
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
