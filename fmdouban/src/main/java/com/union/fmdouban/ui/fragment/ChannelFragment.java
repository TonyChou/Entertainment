package com.union.fmdouban.ui.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

public class ChannelFragment extends BaseFragment implements ItemClickListener{
    private View mRootView;
    private RecyclerView mRecycleView;
    private ChannelAdapter mAdapter;
    RequestQueue mQueue;
    private PlayerController mPlayerController;

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

    private void initView(View rootView) {
        mRecycleView = (RecyclerView)rootView.findViewById(R.id.list_view);
        mPlayerController.init(this.getActivity(), rootView.findViewById(R.id.player_controller_view));
    }

    @Override
    public void onItemClick(int position) {

    }
}
