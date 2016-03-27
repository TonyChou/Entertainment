package com.union.fmdouban.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Adapter;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.union.commonlib.data.LoaderToken;
import com.union.commonlib.ui.anim.AnimListener;
import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.commonlib.utils.UiUtils;
import com.union.fmdouban.R;
import com.union.fmdouban.api.bean.FMChannelType;
import com.union.fmdouban.api.bean.FMRichChannel;
import com.union.fmdouban.api.data.ChannelLoader;
import com.union.fmdouban.api.data.FMCache;
import com.union.fmdouban.ui.adapter.ChannelCategoryPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 16/3/23.
 */
public class FMChannelsFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Boolean>{
    Interpolator sDecelerator = new DecelerateInterpolator();
    Loader<Boolean> loader = null;

    private ViewPager mViewPage;
    private TabLayout mTabLayout;
    private List<FMChannelType> typeList;
    private List<FMRichChannel> channels;
    private View channelPanelView, noChannelLayout;
    public static FMChannelsFragment newInstance() {
        return new FMChannelsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(LoaderToken.DoubanFMChannelType, null, this);
        //getLoaderManager().restartLoader(LoaderToken.PhotosQuery, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_channels_layout, null);
        initView(mRootView);
        showWithAnimation();
        return mRootView;
    }

    private void initView(View mRootView) {
        channelPanelView = mRootView.findViewById(R.id.channel_layout);
        noChannelLayout = mRootView.findViewById(R.id.no_channel_layout);

    }

    private void showChannelPanel() {
        channelPanelView.setVisibility(View.VISIBLE);
        noChannelLayout.setVisibility(View.INVISIBLE);
        mViewPage = (ViewPager) mRootView.findViewById(R.id.pager_channel_category);
        FragmentStatePagerAdapter adapter = new ChannelCategoryPagerAdapter(this.getChildFragmentManager(), typeList);
        mViewPage.setAdapter(adapter);
        mTabLayout = (TabLayout) mRootView.findViewById(R.id.tab_channel_category);
        mTabLayout.setupWithViewPager(mViewPage);
        mViewPage.setCurrentItem(0);
    }

    private void showNoChannelLayout() {
        channelPanelView.setVisibility(View.INVISIBLE);
        noChannelLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("veve", " onStop============= ");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void showWithAnimation() {
//        ViewHelper.setAlpha(mRootView, 0.5f);
//        ViewHelper.setScaleX(mRootView, 0.5f);
//        ViewHelper.setScaleY(mRootView, 0.5f);
        int[] screenSize = UiUtils.getScreenWidthAndHeight(this.getActivity());
        ViewHelper.setTranslationX(mRootView, screenSize[0]);
        ViewPropertyAnimator.animate(mRootView).alpha(1).
                scaleX(1).scaleY(1).
                translationX(0).
                setDuration(300).
                setInterpolator(sDecelerator).
                setListener(new AnimListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }
                });
    }

    @Override
    public void hideWithAnimation() {
        super.hideWithAnimation();
        int[] screenSize = UiUtils.getScreenWidthAndHeight(this.getActivity());
        ViewPropertyAnimator.animate(mRootView).alpha(1).
                scaleX(1).scaleY(1).translationX(screenSize[0]).
                setDuration(300).
                setInterpolator(sDecelerator).
                setListener(new AnimListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }
                });
    }


    @Override
    public Loader<Boolean> onCreateLoader(int id, Bundle args) {
        if (id == LoaderToken.DoubanFMChannelType) {
            loader = new ChannelLoader(this.getActivity());

        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
        Log.i("ChannelLoader", "onLoadFinished");
        typeList = FMCache.getTypeList();
        channels = FMCache.getHotChannelsFromCache();
        if (typeList.size() > 0 && channels.size() > 0) {
            showChannelPanel();
        } else {
            showNoChannelLayout();
        }
    }

    @Override
    public void onLoaderReset(Loader<Boolean> loader) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loader != null) {
            loader.cancelLoad();
        }

        getLoaderManager().destroyLoader(LoaderToken.DoubanFMChannelType);
    }
}
