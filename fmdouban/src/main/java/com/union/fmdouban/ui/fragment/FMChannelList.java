package com.union.fmdouban.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.commonlib.ui.listener.ItemClickListener;
import com.union.commonlib.ui.view.PTRHeaderView;
import com.union.fmdouban.R;
import com.union.fmdouban.api.ExecuteResult;
import com.union.fmdouban.api.FMApi;
import com.union.fmdouban.api.FMCallBack;
import com.union.fmdouban.api.bean.FMChannelType;
import com.union.fmdouban.api.bean.FMRichChannel;
import com.union.fmdouban.api.data.FMCache;
import com.union.fmdouban.ui.adapter.ChannelAdapter;
import com.union.fmdouban.ui.listener.ChannelSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by zhouxiaming on 16/3/26.
 */
public class FMChannelList extends BaseFragment implements ItemClickListener {
    private final int CHANNEL_LIST_LOADED_RESULT = 0x000013; //频道列表加载结果
    private PtrFrameLayout mPtrFrameLayout;
    private PTRHeaderView mHeaderView;
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    private ListView mListView;
    private List<FMRichChannel> channelList = new ArrayList<FMRichChannel>();
    private ChannelAdapter mAdapter;
    ChannelSelectedListener listener;
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
    public static FMChannelList newInstance() {
        return new FMChannelList();
    }

    public void setChannelType(FMChannelType channelType) {
        mType = channelType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_channels_list, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {

        Log.i(TAG, "HotChannel size: " + channelList.size());
        mListView = (ListView) mRootView.findViewById(R.id.channel_list);
        // 为listview的创建一个headerview,注意，如果不加会影响到加载的footview的显示！
        View headerMarginView = new View(this.getActivity());
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(20)));
        mListView.addHeaderView(headerMarginView);

        //3.设置下拉刷新组件和事件监听
        mPtrFrameLayout = (PtrFrameLayout) mRootView.findViewById(R.id.load_more_list_view_ptr_frame);
        mPtrFrameLayout.setEnabled(false);
        mHeaderView = new PTRHeaderView(this.getActivity());
//        mPtrFrameLayout.setHeaderView(mHeaderView);
//        mPtrFrameLayout.addPtrUIHandler(mHeaderView);

        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // here check list view, not content.
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //实现下拉刷新的功能
                Log.i("test", "-----onRefreshBegin-----");
                mPtrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       mPtrFrameLayout.refreshComplete();
//                        //第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        mLoadMoreListViewContainer.loadMoreFinish(false, true);
                    }
                }, 500);
            }
        });


        //4.加载更多的组件
        mLoadMoreListViewContainer = (LoadMoreListViewContainer) mRootView.findViewById(R.id.load_more_list_view_container);
        mLoadMoreListViewContainer.setAutoLoadMore(true);//设置是否自动加载更多
        mLoadMoreListViewContainer.useDefaultHeader();

        //5.添加加载更多的事件监听
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                int startPosition = currentPage * limit;
                getChannelList(mType.getId(), startPosition, limit);
            }
        });

        mAdapter = new ChannelAdapter(this.getActivity(), channelList, this);
        mListView.setAdapter(mAdapter);
    }


    private void initData() {
        if (mType == null) {
            return;
        }
        if (mType.getId() == FMChannelType.hotTypeId) {
            channelList.addAll(FMCache.getHotChannelsFromCache());
            mAdapter.notifyDataSetChanged();
            mLoadMoreListViewContainer.loadMoreFinish(false, false);
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
            mLoadMoreListViewContainer.loadMoreFinish(false, false);
        }
    }

    @Override
    public void onItemClick(int position) {
        if (listener != null) {
            boolean switchResult = listener.switchChannel(channelList.get(position));
        }
    }

    public void setChannelSelectedListener(ChannelSelectedListener listener) {
        this.listener = listener;
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

                currentPage++;
                if (channels != null) {
                    List<FMRichChannel> list = mGson.fromJson(channels, new TypeToken<List<FMRichChannel>>(){}.getType());
                    if (list != null && list.size() > 0) {
                        for (FMRichChannel channel : list) {
                            if (!channelList.contains(channel)) {
                                channelList.add(channel);
                            }
                        }
                    }
                }
                if (status && totalPage == currentPage) {
                    mLoadMoreListViewContainer.loadMoreFinish(false, false);
                } else {
                    mLoadMoreListViewContainer.loadMoreFinish(false, true);
                }
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                mLoadMoreListViewContainer.loadMoreFinish(false, true);
            }
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
}
