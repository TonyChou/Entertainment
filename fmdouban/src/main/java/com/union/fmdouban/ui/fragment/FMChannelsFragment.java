package com.union.fmdouban.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.union.commonlib.data.LoaderToken;
import com.union.commonlib.ui.anim.AnimCallbackImp;
import com.union.commonlib.ui.anim.AnimListener;
import com.union.commonlib.ui.anim.FaceBookRebound;
import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.commonlib.ui.view.CircularProgress;
import com.union.commonlib.ui.view.TintUtils;
import com.union.commonlib.utils.UiUtils;
import com.union.fmdouban.R;
import com.union.fmdouban.api.bean.FMChannelType;
import com.union.fmdouban.api.bean.FMRichChannel;
import com.union.fmdouban.api.data.ChannelLoader;
import com.union.fmdouban.api.data.FMCache;
import com.union.fmdouban.ui.adapter.ChannelCategoryPagerAdapter;

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
    private CircularProgress mLoadingBar;
    private ChannelCategoryPagerAdapter fragmentAdapter;
    View mRefreshButton;
    private BaseSpringSystem mSpringSystem;
    AnimCallBack mAnimCallback;
    public static FMChannelsFragment newInstance() {
        return new FMChannelsFragment();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpringSystem = SpringSystem.create();
        mAnimCallback = new AnimCallBack();
        //getLoaderManager().restartLoader(LoaderToken.PhotosQuery, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_channels_layout, null);
        initView(mRootView);
        //showWithAnimation();
        getLoaderManager().initLoader(LoaderToken.DoubanFMChannelType, null, this);
        return mRootView;
    }


    private void initView(View mRootView) {
        channelPanelView = mRootView.findViewById(R.id.channel_layout);
        noChannelLayout = mRootView.findViewById(R.id.no_channel_layout);
        mLoadingBar = (CircularProgress)mRootView.findViewById(R.id.rl_loading);
        mRefreshButton = noChannelLayout.findViewById(R.id.refresh_button);
        addOnTouchSpringAnimation(mRefreshButton);
        TintUtils.setBackgroundTint(this.getActivity(), (AppCompatTextView) mRefreshButton.findViewById(R.id.button_icon), R.color.white);
    }
    private void addOnTouchSpringAnimation(View... v) {
        for (View view : v) {
            Spring spring = mSpringSystem.createSpring();
            FaceBookRebound.addSpringAnimation(view, spring, mAnimCallback);
        }
    }

    private void showChannelPanel() {
        FMChannelType hotType = new FMChannelType(FMChannelType.hotTypeId, getString(R.string.hot_channel));
        typeList.add(0, hotType);
        channelPanelView.setVisibility(View.VISIBLE);
        noChannelLayout.setVisibility(View.INVISIBLE);
        mViewPage = (ViewPager) mRootView.findViewById(R.id.pager_channel_category);
        fragmentAdapter = new ChannelCategoryPagerAdapter(this.getChildFragmentManager(), typeList);
        mViewPage.setAdapter(fragmentAdapter);
        mTabLayout = (TabLayout) mRootView.findViewById(R.id.tab_channel_category);
        mTabLayout.setupWithViewPager(mViewPage);
        mViewPage.setCurrentItem(0);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i(TAG, "onHiddenChanged   " + hidden);
        if (!hidden && fragmentAdapter != null) {
            List<FMChannelList> fragmentList = fragmentAdapter.getFragmentList();
            if (fragmentList != null && fragmentList.size() > 0) {
                for (FMChannelList frag : fragmentList) {
                    frag.refreshData();
                }
            }
        }
    }

    private void showNoChannelLayout() {
        channelPanelView.setVisibility(View.INVISIBLE);
        noChannelLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
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
        mLoadingBar.setVisibility(View.VISIBLE);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
        Log.i("ChannelLoader", "onLoadFinished");
        typeList = FMCache.getTypeList();
        channels = FMCache.getHotChannelsFromCache();
        mLoadingBar.setVisibility(View.GONE);
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
        if (mSpringSystem != null) {
            mSpringSystem.removeAllListeners();
            mSpringSystem = null;
        }
        getLoaderManager().destroyLoader(LoaderToken.DoubanFMChannelType);
    }

    private void restartLoader() {
        noChannelLayout.setVisibility(View.INVISIBLE);
        getLoaderManager().restartLoader(LoaderToken.DoubanFMChannelType, null, this);
    }

    class AnimCallBack extends AnimCallbackImp {
        @Override
        public void animationEnd(View v) {
            if (v == mRefreshButton) {
                //TODO
                restartLoader();
            }
        }
    }


}
