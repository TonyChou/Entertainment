package com.union.fmdouban.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liblistview.widget.XListView;
import com.union.commonlib.ui.listener.ItemClickListener;
import com.union.commonlib.ui.view.RefreshFooterView;
import com.union.commonlib.ui.view.RefreshHeaderView;
import com.union.fmdouban.R;
import com.union.fmdouban.api.ExecuteResult;
import com.union.fmdouban.api.FMApi;
import com.union.fmdouban.api.FMCallBack;
import com.union.fmdouban.api.bean.FMChannelType;
import com.union.fmdouban.api.bean.FMRichChannel;
import com.union.fmdouban.api.data.FMCache;
import com.union.fmdouban.play.FMController;
import com.union.fmdouban.ui.adapter.ChannelAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 16/3/26.
 */
public class FMChannelListNew extends FMChannelList implements ItemClickListener, RefreshHeaderView.RefreshListener, RefreshFooterView.LoadmoreListener {
    private final int CHANNEL_LIST_LOADED_RESULT = 0x000013; //频道列表加载结果
    private XListView mListView;
    RefreshHeaderView mHeaderView;
    RefreshFooterView mFooterView;
    private List<FMRichChannel> channelList = new ArrayList<FMRichChannel>();
    private ChannelAdapter mAdapter;
    Gson mGson = new Gson();
    FMChannelType mType;
    private int limit = 20;
    private int currentPage = 0;  //分页查询当前页码
    private int totalPage = 1;  //总共页码
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == CHANNEL_LIST_LOADED_RESULT) {
                handleResult((ExecuteResult)msg.obj);
            }
        }
    };
    public static FMChannelListNew newInstance() {
        return new FMChannelListNew();
    }

    public void setChannelType(FMChannelType channelType) {
        mType = channelType;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_channels_list_new;
    }

    @Override
    protected void loadData() {
        super.loadData();
        initData();
    }

    @Override
    protected void initView() {
        Log.i(TAG, "HotChannel size: " + channelList.size());
        mListView = (XListView) mRootView.findViewById(R.id.list_view);

        addHeaderView();
        addFooterView();
        mAdapter = new ChannelAdapter(mActivity, channelList, this);
        mListView.setAdapter(mAdapter);
    }

    private void addHeaderView() {
        mHeaderView = new RefreshHeaderView(mActivity);
        mHeaderView.setRefreshListener(this);
        mListView.setOverScrollHeader(mHeaderView);
        mListView.setOverScrollListener(mHeaderView);

    }

    private void addFooterView() {
        mFooterView = new RefreshFooterView(mActivity);
        mFooterView.setLoadMoreListener(this);
        mListView.addFooterView(mFooterView);
        mListView.setOnScrollListener(mFooterView);
    }


    private void initData() {
        if (mType == null) {
            return;
        }
        if (mType.getId() == FMChannelType.hotTypeId) {
            channelList.addAll(FMCache.getHotChannelsFromCache());
            mAdapter.notifyDataSetChanged();
            //mLoadMoreListViewContainer.loadMoreFinish(false, false);
        } else {
            getChannelList(mType.getId(), 0, limit);
        }
    }

    private void getChannelList(String typeId, int startPosition, int limit) {
        if (currentPage < totalPage) {
            FMApi.getInstance().getChannelListByType(typeId, startPosition, limit, new FMCallBack() {
                @Override
                public void onRequestResult(ExecuteResult result) {
                    Message msg = handler.obtainMessage(CHANNEL_LIST_LOADED_RESULT);
                    msg.obj = result;
                    msg.sendToTarget();
                }
            });
        } else {
            mFooterView.loadMoreComplete(false);
        }
    }

    @Override
    public void onItemClick(int position) {
        FMController.switchChannel(channelList.get(position));
        mActivity.finish();
    }

    public void refreshData() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void handleResult(ExecuteResult result) {
        if (result.getResult() == ExecuteResult.OK && result.getResponseString() != null) {
            try {
                JSONObject jsonObject = new JSONObject(result.getResponseString());
                boolean status = jsonObject.getBoolean("status");
                JSONObject data = jsonObject.getJSONObject("data");
                totalPage = data.getInt("total");
                String channels = data.getString("channels");


                if (channels != null) {
                    List<FMRichChannel> list = mGson.fromJson(channels, new TypeToken<List<FMRichChannel>>(){}.getType());
                    if (list != null && list.size() > 0) {
                        for (FMRichChannel channel : list) {
                            if (!channelList.contains(channel)) {
                                channelList.add(channel);
                            }
                        }
                        currentPage++;
                    }
                }

                if (status && totalPage == currentPage) {
                    mFooterView.loadMoreComplete(false);
                } else {
                    mFooterView.loadMoreComplete(true);
                }
                mAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                mFooterView.loadMoreComplete(true);
            }
        } else {
            mFooterView.loadMoreComplete(true);
        }
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            FMChannelType type = (FMChannelType)savedInstanceState.getSerializable("type");

            if (type != null) {
                mType = type;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null && mType != null) {
            outState.putSerializable("type", mType);
        }
    }

    @Override
    public void onRefreshDoing() {
        //TODO REFRESH
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHeaderView.refreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onLoadMoreDoing() {
        int startPosition = currentPage * limit;
        getChannelList(mType.getId(), startPosition, limit);
    }
}
