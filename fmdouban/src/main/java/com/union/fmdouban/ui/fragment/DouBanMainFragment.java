package com.union.fmdouban.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;

import com.union.commonlib.cache.CacheManager;
import com.union.commonlib.ui.ActionBarPage;
import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.fmdouban.R;
import com.union.fmdouban.ui.adapter.CategoryPagerAdapter;

/**
 * Created by zhouxiaming on 2017/4/16.
 */

public class DouBanMainFragment extends BaseFragment {
    private ViewPager mViewPage;
    private TabLayout mTabLayout;
    private CategoryPagerAdapter mPageAdapter;
    public static DouBanMainFragment newInstance() {
        return new DouBanMainFragment();
    }
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_main_layout;
    }

    @Override
    protected void initView() {
        super.initView();
        mViewPage = (ViewPager) mRootView.findViewById(R.id.pager_category);
        mPageAdapter = new CategoryPagerAdapter(this.getChildFragmentManager());
        mViewPage.setAdapter(mPageAdapter);
        mTabLayout = (TabLayout) mRootView.findViewById(R.id.tab_category);
        mTabLayout.setupWithViewPager(mViewPage);
        mViewPage.setCurrentItem(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        syncToolBarStatus(ActionBarPage.DOUBAN_FM_MAIN_PAGE);
        ViewCompat.setFitsSystemWindows(mTabLayout, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CacheManager cacheManager = new CacheManager(this.getActivity());
        cacheManager.clearPicassoCache();
        cacheManager.clearVolleyCache();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
