package com.union.fmdouban.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.tulips.douban.model.ChannelsPage;
import com.union.commonlib.ui.anim.AnimCallbackImp;
import com.union.commonlib.ui.anim.AnimListener;
import com.union.commonlib.ui.anim.FaceBookRebound;
import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.commonlib.ui.view.TintUtils;
import com.union.commonlib.utils.LogUtils;
import com.union.fmdouban.R;
import com.union.fmdouban.play.FMController;
import com.union.fmdouban.service.FMPlayerService;
import com.union.fmdouban.service.PlayerUIController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 2016/3/14.
 */

public class FMPlayerFragment extends BaseFragment implements FMController.FMChannelChangedListener{
    private final int REFRESH_CHANNEL_INFO = 0x000012;
    private static final Interpolator sDecelerator = new DecelerateInterpolator();

    private PlayerUIController mPlayerController;
    private View mControllerView;
    View mShowHideButton;
    ImageView mCoverImage;
    private BaseSpringSystem mSpringSystem;
    private List<Spring> springMap = new ArrayList<Spring>();
    AnimCallBack mAnimCallback;
    FMPlayerService mPlayerService;
    private ChannelsPage.Channel channel;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }
    };

    public static FMPlayerFragment newInstance() {
        FMPlayerFragment fragment = new FMPlayerFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initChannel();
        mPlayerController = PlayerUIController.getInstance();
        mSpringSystem = SpringSystem.create();
        mAnimCallback = new AnimCallBack();
        bindPlayerService();
        FMController.registerFMChannelListener(this);
    }

    private void initChannel() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            channel = (ChannelsPage.Channel)bundle.getSerializable("channel");
        }
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_player_layout;
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (channel != null) {
            //switchChannel(channel);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mCoverImage = (ImageView) mRootView.findViewById(R.id.cover);
        mControllerView = mRootView.findViewById(R.id.player_panel);
        mPlayerController.init(this, mControllerView);
        mShowHideButton = mRootView.findViewById(R.id.foot_button);
        addOnTouchSpringAnimation(mShowHideButton);

        TintUtils.setBackgroundTint(this.getActivity(), (AppCompatTextView) mShowHideButton.findViewById(R.id.button_icon), R.color.white);
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
        FMController.removeFMChannelListener(this);
    }

    @Override
    public boolean onBackPress() {
        return super.onBackPress();
    }

    public void switchChannel(ChannelsPage.Channel channel) {
        if (channel == null) {
            LogUtils.i(TAG, "Channel is null ----");
        }
        getPlayerService().switchChannel(channel);
    }

    @Override
    public void changeFMChannel(ChannelsPage.Channel channel) {
        switchChannel(channel);
    }

    class AnimCallBack extends AnimCallbackImp {
        @Override
        public void animationEnd(View v) {
            if (v == mShowHideButton) {
                //showOrHideChannelsPanel();
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
            switchChannel(channel);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayerService = null;
        }
    };



}
