package com.union.fmdouban.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewTreeObserver;

import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.commonlib.ui.view.slidinguppanel.SlidingUpPanelLayout;
import com.union.commonlib.utils.LogUtils;
import com.union.fmdouban.R;

/**
 * Created by zhouxiaming on 2017/4/18.
 */

public class DouBanSlideFragment extends BaseFragment {
    private SlidingUpPanelLayout mSlideLayout;
    private BaseFragment mMainFragment;
    private BaseFragment mPlayerFragment;
    public static DouBanSlideFragment newInstance() {
        return new DouBanSlideFragment();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_slide_layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        mSlideLayout = (SlidingUpPanelLayout) mRootView.findViewById(R.id.slide_layout_panel);
        setSlideLayoutListener();
        mMainFragment = DouBanMainFragment.newInstance();
        mPlayerFragment = FMPlayerFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.down_fragment, mPlayerFragment, "Player");
        transaction.replace(R.id.up_fragment, mMainFragment, "MainContainer");
        transaction.commitAllowingStateLoss();

    }

    private void setSlideLayoutListener() {
        mSlideLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                ((FMPlayerFragment)mPlayerFragment).onPanelSlide(panel, slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                LogUtils.i(TAG, "onPanelStateChanged previousState: " + previousState + " newState: " + newState);
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED || newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    ((DouBanMainFragment)mMainFragment).onPanelStateChanged(newState);
                }
            }

        });
    }
}
