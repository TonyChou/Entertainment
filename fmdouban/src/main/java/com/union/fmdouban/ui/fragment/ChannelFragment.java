package com.union.fmdouban.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.google.gson.reflect.TypeToken;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.squareup.picasso.LruCache;
import com.union.commonlib.cache.CacheManager;
import com.union.commonlib.ui.anim.AnimCallbackImp;
import com.union.commonlib.ui.anim.AnimListener;
import com.union.commonlib.ui.anim.FaceBookRebound;
import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.commonlib.ui.listener.ItemClickListener;
import com.union.commonlib.ui.view.TintUtils;
import com.union.fmdouban.Constant;
import com.union.fmdouban.R;
import com.union.fmdouban.bean.Channel;
import com.union.fmdouban.service.FMPlayerService;
import com.union.fmdouban.ui.adapter.ChannelAdapter;
import com.union.fmdouban.service.PlayerController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 2016/3/14.
 */

public class ChannelFragment extends BaseFragment implements ItemClickListener {
    private static final Interpolator sDecelerator = new DecelerateInterpolator();
    private View mRootView;
    private RecyclerView mRecycleView;
    private ChannelAdapter mAdapter;
    RequestQueue mQueue;
    private PlayerController mPlayerController;
    private View mControllerView;
    View mShowHideButton;
    ImageView mCoverImage;
    private BaseSpringSystem mSpringSystem;
    private List<Spring> springMap = new ArrayList<Spring>();
    AnimCallBack mAnimCallback;
    FMPlayerService mPlayerService;
    Channel mCurrentChannel;

    public static ChannelFragment newInstance() {
        ChannelFragment fragment = new ChannelFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this.getActivity());
        mPlayerController = PlayerController.getInstance();
        mSpringSystem = SpringSystem.create();
        mAnimCallback = new AnimCallBack();
        bindPlayerService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_channel_list_layout, container, false);
        initView(mRootView);
        initListView();
        loadChannel();
        return mRootView;
    }

    private void initView(View rootView) {
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.list_view);
        mCoverImage = (ImageView) rootView.findViewById(R.id.cover);
        mControllerView = rootView.findViewById(R.id.player_controller_view);
        mPlayerController.init(this, mControllerView);
        mShowHideButton = rootView.findViewById(R.id.foot_button);
        addOnTouchSpringAnimation(mShowHideButton);

        TintUtils.setBackgroundTint(this.getActivity(), (AppCompatTextView) mShowHideButton.findViewById(R.id.button_icon), R.color.white);
    }

    private View initListView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this.getActivity(), 2);
        // LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ChannelAdapter(this.getActivity(), new ArrayList<Channel>(), this);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setVisibility(View.INVISIBLE);
        mRecycleView.setHasFixedSize(true);
        return mRecycleView;
    }

    private void addOnTouchSpringAnimation(View... v) {
        for (View view : v) {
            Spring spring = mSpringSystem.createSpring();
            springMap.add(spring);
            FaceBookRebound.addSpringAnimation(view, spring, mAnimCallback);
        }
    }

    private void updateAdapterData(List<Channel> channels) {
        Channel currentChannel = mPlayerService != null ? mPlayerService.getCurrentChannel() : null;
        if (currentChannel != null) {
            for (Channel channel : channels) {
                if (channel.getChannelId() == currentChannel.getChannelId()) {
                    channel.setIsPlaying(true);
                } else {
                    channel.setIsPlaying(false);
                }
            }
        }
        mAdapter.setData(channels);
        mAdapter.notifyDataSetChanged();
    }

    private void loadChannel() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constant.CHANNEL_URL_DOUBAN, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i(TAG, "jsonObject: " + jsonObject);
                try {
                    List<Channel> channels = Channel.parserJson(jsonObject);
                    renderChannelsData(channels);
                    saveChannelsToCache(channels);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, "volleyError: " + volleyError.getMessage());
                List<Channel> channels = loadChannelsFromCache();
                renderChannelsData(channels);
            }
        });
        mQueue.add(jsonObjectRequest);
        mQueue.start();
    }

    public void renderChannelsData(List<Channel> channels) {
        if (channels != null && channels.size() > 0) {
            updateAdapterData(channels);
            boolean isPlaying = mPlayerService != null ? mPlayerService.isPlaying() : false;
            if (!isPlaying) {
                showChannelListView();
            }
        }
    }

    /**
     * 讲频道数据保存到缓存中
     * @param channels
     */
    private void saveChannelsToCache(List<Channel> channels) {
        if (channels != null && channels.size() > 0) {
            CacheManager cacheManager = new CacheManager(this.getActivity());
            cacheManager.saveToCache(CacheManager.FM_DOUBAN_CHANNELS, channels);
        }
    }

    /**
     * 从缓存中加载数据
     * @return
     */
    private List<Channel> loadChannelsFromCache() {
        CacheManager cacheManager = new CacheManager(this.getActivity());
        return cacheManager.getFromCache(CacheManager.FM_DOUBAN_CHANNELS, new TypeToken<List<Channel>>(){});
    }


    @Override
    public void onItemClick(int position) {
        showOrHideChannelsPanel();
        Channel selectedChannel = mAdapter.getChannel(position);
        if (mCurrentChannel != null && selectedChannel.getChannelId() == mCurrentChannel.getChannelId()) {
            return;
        }

        for(Channel channel : mAdapter.getData()) {
            channel.setIsPlaying(false);
        }

        selectedChannel.setIsPlaying(true);
        mCurrentChannel = selectedChannel;
        mAdapter.notifyDataSetChanged();
        switchChannel();
    }

    public RequestQueue getQueue() {
        return mQueue;
    }

    /**
     * 切换频道播放歌曲
     */
    private void switchChannel() {
        getPlayerSercie().switchChannel(mCurrentChannel);
    }

    public void showOrHideChannelsPanel() {
        if (mRecycleView.getVisibility() == View.INVISIBLE) {
            showChannelListView();
        } else {
            hideChannelListView();
        }
    }

    /**
     * Y轴平移播放控制面板到屏幕中间位置
     */
    public void hideChannelListView() {
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
        ViewPropertyAnimator.animate(mRecycleView).alpha(1).
                scaleX(1).scaleY(1).
                translationX(0).translationY(recycleViewDy).
                setDuration(500).
                setInterpolator(sDecelerator).
                setListener(new AnimListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mRecycleView.setVisibility(View.INVISIBLE);
                    }
                });
        mShowHideButton.setBackgroundResource(R.drawable.circle_light_yellow_shape);
    }

    /**
     * 显示频道列表
     */
    private void showChannelListView() {
        mRecycleView.setVisibility(View.VISIBLE);
        //播放控制面板上移到顶端
        ViewPropertyAnimator.animate(mControllerView).alpha(0).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0).
                setDuration(500).
                setInterpolator(sDecelerator).
                setListener(new AnimListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ViewPropertyAnimator.animate(mControllerView).alpha(0);
                    }
                });
        mRecycleView.setVisibility(View.VISIBLE);
        ViewHelper.setAlpha(mRecycleView, 0.5f);
        ViewHelper.setScaleX(mRecycleView, 0.5f);
        ViewHelper.setScaleY(mRecycleView, 0.5f);

        ViewPropertyAnimator.animate(mRecycleView).alpha(1).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0).
                setDuration(500).
                setInterpolator(sDecelerator).
                setListener(new AnimListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }
                });
        mShowHideButton.setBackgroundResource(R.drawable.circle_blue_shape);
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

    class AnimCallBack extends AnimCallbackImp {
        @Override
        public void animationEnd(View v) {
            if (v == mShowHideButton) {
                showOrHideChannelsPanel();
            }
        }
    }

    public FMPlayerService getPlayerSercie() {
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
