package com.union.fmdouban.ui.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.union.commonlib.ui.anim.AnimListener;
import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.commonlib.ui.listener.ItemClickListener;
import com.union.fmdouban.Constant;
import com.union.fmdouban.R;
import com.union.fmdouban.bean.Channel;
import com.union.fmdouban.ui.adapter.ChannelAdapter;
import com.union.fmdouban.ui.view.PlayerController;

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
    FloatingActionButton fab;
    ImageView mCoverImage;

    public static ChannelFragment newInstance() {
        ChannelFragment fragment = new ChannelFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this.getActivity());
        mPlayerController = PlayerController.getInstance();
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
        mPlayerController.init(this.getActivity(), mControllerView);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFloatButtonClick();
            }
        });
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

        //mRecycleView.setItemAnimator(new SlideInFromLeftItemAnimator(recyclerView));

        mRecycleView.setHasFixedSize(true);
        //mRecycleView.setBackgroundColor(Color.WHITE);
        return mRecycleView;
    }

    private void updateAdapterData(List<Channel> channels) {
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
                    if (channels != null) {
                        updateAdapterData(channels);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, "volleyError: " + volleyError.getMessage());
            }
        });
        mQueue.add(jsonObjectRequest);
        mQueue.start();
    }


    @Override
    public void onItemClick(int position) {

    }

    public void onFloatButtonClick() {
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
                setDuration(800).
                setInterpolator(sDecelerator).
                setListener(new AnimListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ViewPropertyAnimator.animate(mControllerView).alpha(1);
                        //放大Cover Image
//                        ViewPropertyAnimator.animate(mCoverImage).alpha(1).
//                                scaleX(1.5f).scaleY(1.5f).
//                                translationX(0).translationY(-500).
//                                setDuration(800).
//                                setInterpolator(sDecelerator);
                    }
                });



        int recycleViewDy = mRootView.getHeight();
        ViewPropertyAnimator.animate(mRecycleView).alpha(1).
                scaleX(1).scaleY(1).
                translationX(0).translationY(recycleViewDy).
                setDuration(800).
                setInterpolator(sDecelerator).
                setListener(new AnimListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mRecycleView.setVisibility(View.INVISIBLE);
                    }
                });
    }

    /**
     * 显示频道列表
     */
    private void showChannelListView() {
        //播放控制面板上移到顶端
        ViewPropertyAnimator.animate(mControllerView).alpha(1).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0).
                setDuration(800).
                setInterpolator(sDecelerator).
                setListener(new AnimListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ViewPropertyAnimator.animate(mControllerView).alpha(1);
                        mRecycleView.setVisibility(View.VISIBLE);
                    }
                });

//        //缩小Cover Image
//        ViewPropertyAnimator.animate(mCoverImage).alpha(1).
//                translationX(0).translationY(0).
//                scaleX(1f).scaleY(1f).
//                setDuration(800).
//                setInterpolator(sDecelerator);
        int screenHeight = mRootView.getHeight();

        //ViewHelper.setTranslationY(mRecycleView, screenHeight);
        mRecycleView.setVisibility(View.VISIBLE);
        ViewHelper.setAlpha(mRecycleView, 0.5f);
        ViewHelper.setScaleX(mRecycleView, 0.5f);
        ViewHelper.setScaleY(mRecycleView, 0.5f);

        ViewPropertyAnimator.animate(mRecycleView).alpha(1).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0).
                setDuration(800).
                setInterpolator(sDecelerator).
                setListener(new AnimListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ViewPropertyAnimator.animate(mControllerView).alpha(1);
                        mRecycleView.setVisibility(View.VISIBLE);
                    }
                });
    }

}
