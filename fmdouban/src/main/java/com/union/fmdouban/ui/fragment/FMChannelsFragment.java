package com.union.fmdouban.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.fmdouban.R;
import com.union.fmdouban.ui.adapter.ChannelCategoryPagerAdapter;

/**
 * Created by zhouxiaming on 16/3/23.
 */
public class FMChannelsFragment extends BaseFragment {

    private View mRootView;
    private ViewPager mViewPage;
    private TabLayout mTabLayout;

    public static FMChannelsFragment newInstance() {
        return new FMChannelsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_channels_layout, null);
        initView(mRootView);
        return mRootView;
    }

    private void initView(View mRootView) {
        mViewPage = (ViewPager) mRootView.findViewById(R.id.pager_channel_category);
        mViewPage.setAdapter(new ChannelCategoryPagerAdapter(this.getActivity().getSupportFragmentManager()));
        mTabLayout = (TabLayout) mRootView.findViewById(R.id.tab_channel_category);
        mTabLayout.setupWithViewPager(mViewPage);
    }


}
