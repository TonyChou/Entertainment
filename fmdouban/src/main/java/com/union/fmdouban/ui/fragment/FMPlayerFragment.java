package com.union.fmdouban.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SpringSystem;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.tulips.douban.model.ChannelsPage;
import com.union.commonlib.ui.anim.AnimCallbackImp;
import com.union.commonlib.ui.anim.AnimListener;
import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.commonlib.utils.LogUtils;
import com.union.fmdouban.R;
import com.union.fmdouban.play.FMController;
import com.union.fmdouban.service.FMPlayerService;
import com.union.fmdouban.service.PlayerUIController;

/**
 * Created by zhouxiaming on 2016/3/14.
 */

public class FMPlayerFragment extends BaseFragment implements FMController.FMChannelChangedListener{
    private final int REFRESH_CHANNEL_INFO = 0x000012;
    private static final Interpolator sDecelerator = new DecelerateInterpolator();

    private PlayerUIController mPlayerController;
    private View mControllerView;

    ImageView mCoverImage;
    private BaseSpringSystem mSpringSystem;

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
        mControllerView = mRootView.findViewById(R.id.player_layout);
        mPlayerController.init(this, mControllerView, mSpringSystem);

    }



    @Override
    public void onResume() {
        super.onResume();
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
       // mShowHideButton.setBackgroundResource(R.drawable.circle_light_yellow_shape);
    }

    public void onPanelSlide(View view, float slideOffset) {
        mPlayerController.onPanelSlide(view, slideOffset);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPlayerController.release();
        if (mSpringSystem != null) {
            mSpringSystem.removeAllListeners();
            mSpringSystem = null;
        }


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
            return;
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
