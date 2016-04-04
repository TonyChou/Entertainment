package com.union.fmdouban.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.union.commonlib.cache.CacheManager;
import com.union.commonlib.ui.anim.AnimCallbackImp;
import com.union.commonlib.ui.anim.AnimListener;
import com.union.commonlib.ui.anim.FaceBookRebound;
import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.commonlib.ui.view.TintUtils;
import com.union.fmdouban.R;
import com.union.fmdouban.api.ExecuteResult;
import com.union.fmdouban.api.FMApi;
import com.union.fmdouban.api.FMCallBack;
import com.union.fmdouban.api.bean.FMRichChannel;
import com.union.fmdouban.service.FMPlayerService;
import com.union.fmdouban.service.PlayerController;
import com.union.fmdouban.ui.listener.ChannelSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 2016/3/14.
 */

public class FMPlayerFragment extends BaseFragment implements ChannelSelectedListener{
    private final int REFRESH_CHANNEL_INFO = 0x000012;
    private static final Interpolator sDecelerator = new DecelerateInterpolator();
    private View mRootView;
    private PlayerController mPlayerController;
    private View mControllerView;
    View mShowHideButton;
    ImageView mCoverImage;
    private BaseSpringSystem mSpringSystem;
    private List<Spring> springMap = new ArrayList<Spring>();
    AnimCallBack mAnimCallback;
    FMPlayerService mPlayerService;
    FMChannelsFragment mChannelsFragment;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            int waht = msg.what;
//            if (waht == REFRESH_CHANNEL_INFO) {
//                ExecuteResult result = (ExecuteResult)msg.obj;
//                renderChannelsOnUiThread(result);
//            }
        }
    };

    public static FMPlayerFragment newInstance() {
        FMPlayerFragment fragment = new FMPlayerFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayerController = PlayerController.getInstance();
        mSpringSystem = SpringSystem.create();
        mAnimCallback = new AnimCallBack();
        bindPlayerService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_player_layout, container, false);
        initView(mRootView);
        //loadChannel();
        return mRootView;
    }

    private void initView(View rootView) {
        mCoverImage = (ImageView) rootView.findViewById(R.id.cover);
        mControllerView = rootView.findViewById(R.id.player_controller_view);
        mPlayerController.init(this, mControllerView);
        mShowHideButton = rootView.findViewById(R.id.foot_button);
        addOnTouchSpringAnimation(mShowHideButton);

        TintUtils.setBackgroundTint(this.getActivity(), (AppCompatTextView) mShowHideButton.findViewById(R.id.button_icon), R.color.white);
    }

    /**
     *显示频道选择面板
     */
    private void showOrHideChannelsFragment() {
        if (mChannelsFragment == null) {
            mChannelsFragment = FMChannelsFragment.newInstance();
            mChannelsFragment.setChannelSelectedListener(this);
        }
        FragmentTransaction transaction = this.getChildFragmentManager().beginTransaction();

        if (mChannelsFragment.isVisible()) {
            hideFragment(transaction);
        } else {
            if ( !mChannelsFragment.isAdded()) {
                transaction.add(R.id.fragment_container, mChannelsFragment).commit();
            } else {
                showFragment(transaction);
            }
        }
    }

    private void showFragment(final FragmentTransaction transaction) {
        transaction.show(mChannelsFragment).commit();
        mChannelsFragment.showWithAnimation();
    }

    private void hideFragment(final FragmentTransaction transaction) {
        mChannelsFragment.hideWithAnimation();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                transaction.hide(mChannelsFragment).commit();
            }
        }, 300);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void addOnTouchSpringAnimation(View... v) {
        for (View view : v) {
            Spring spring = mSpringSystem.createSpring();
            springMap.add(spring);
            FaceBookRebound.addSpringAnimation(view, spring, mAnimCallback);
        }
    }


    private void loadChannel() {
        FMApi.getInstance().getFmChannels(new FMCallBack() {
            @Override
            public void onRequestResult(ExecuteResult result) {
                Message msg = handler.obtainMessage(REFRESH_CHANNEL_INFO);
                msg.obj = result;
                msg.sendToTarget();
            }
        });
    }



    /**
     * Y轴平移播放控制面板到屏幕中间位置
     */
    public void showControllerView() {
        ViewHelper.setAlpha(mControllerView, 0.5f);
        ViewHelper.setScaleX(mControllerView, 0.5f);
        ViewHelper.setScaleY(mControllerView, 0.5f);
        int dy = mRootView.getHeight() / 2 - mControllerView.getHeight() / 2;
        ViewPropertyAnimator.animate(mControllerView).alpha(1).
                scaleX(1).scaleY(1).
                translationX(0).translationY(dy).
                setDuration(500).
                setInterpolator(sDecelerator).
                setListener(new AnimListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ViewPropertyAnimator.animate(mControllerView).alpha(1);
                    }
                });


        int recycleViewDy = mRootView.getHeight();
//        ViewPropertyAnimator.animate(mRecycleView).alpha(1).
//                scaleX(1).scaleY(1).
//                translationX(0).translationY(recycleViewDy).
//                setDuration(500).
//                setInterpolator(sDecelerator).
//                setListener(new AnimListener() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        mRecycleView.setVisibility(View.INVISIBLE);
//                    }
//                });
        mShowHideButton.setBackgroundResource(R.drawable.circle_light_yellow_shape);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Spring spring : springMap) {
            spring.removeAllListeners();
        }
        if (mSpringSystem != null) {
            mSpringSystem.removeAllListeners();
            mSpringSystem = null;
        }

        mPlayerController.release();
        getActivity().unbindService(connection);
        CacheManager cacheManager = new CacheManager(this.getActivity());
        cacheManager.clearPicassoCache();
        cacheManager.clearVolleyCache();
    }

    @Override
    public boolean onBackPress() {
        if (!mChannelsFragment.isHidden()) {
            showOrHideChannelsFragment();
            return false;
        }
        return super.onBackPress();
    }

    @Override
    public boolean switchChannel(FMRichChannel channel) {
        getPlayerService().switchChannel(channel);
        showOrHideChannelsFragment(); 
        return true;
    }

    class AnimCallBack extends AnimCallbackImp {
        @Override
        public void animationEnd(View v) {
            if (v == mShowHideButton) {
                //showOrHideChannelsPanel();
                showOrHideChannelsFragment();
            }
        }
    }

    public FMPlayerService getPlayerService() {
        return mPlayerService;
    }

    private void bindPlayerService() {
        this.getActivity().bindService(new Intent(this.getActivity(), FMPlayerService.class), connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayerService = ((FMPlayerService.LocalBinder) service).getPlayerService();
            mPlayerService.setControllerListener(mPlayerController); //设置播放监听
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayerService = null;
        }
    };
}
