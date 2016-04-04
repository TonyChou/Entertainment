package com.union.fmdouban.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.commonlib.ui.listener.ItemClickListener;
import com.union.commonlib.ui.view.PTRHeaderView;
import com.union.fmdouban.R;
import com.union.fmdouban.api.bean.FMChannel;
import com.union.fmdouban.api.bean.FMRichChannel;
import com.union.fmdouban.api.data.FMCache;
import com.union.fmdouban.ui.adapter.ChannelAdapter;
import com.union.fmdouban.ui.listener.ChannelSelectedListener;

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
    private PtrFrameLayout mPtrFrameLayout;
    private PTRHeaderView mHeaderView;
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    private ListView mListView;
    private List<FMRichChannel> channelList = new ArrayList<FMRichChannel>();
    private ChannelAdapter mAdapter;
    ChannelSelectedListener listener;
    public static FMChannelList newInstance() {
        return new FMChannelList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_channels_list, null);
        initView();
        return mRootView;
    }

    private void initView() {
        channelList.addAll(FMCache.getHotChannelsFromCache());
        Log.i(TAG, "HotChannel size: " + channelList.size());
        mListView = (ListView) mRootView.findViewById(R.id.channel_list);
        // 为listview的创建一个headerview,注意，如果不加会影响到加载的footview的显示！
        View headerMarginView = new View(this.getActivity());
        headerMarginView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(20)));
        mListView.addHeaderView(headerMarginView);
        //2.绑定模拟的数据
        mAdapter = new ChannelAdapter(this.getActivity(), channelList, this);
        mListView.setAdapter(mAdapter);
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
//                        mockStrList.clear();
//                        start = 0;
//                        mockStrList.addAll(getMockData(start, count));
                       mPtrFrameLayout.refreshComplete();
//                        //第一个参数是：数据是否为空；第二个参数是：是否还有更多数据
                        mLoadMoreListViewContainer.loadMoreFinish(false, true);
//                        adapter.notifyDataSetChanged();
                    }
                }, 500);
            }
        });
        //设置延时自动刷新数据
//        mPtrFrameLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPtrFrameLayout.autoRefresh(true);
//            }
//        }, 200);


        //4.加载更多的组件

        mLoadMoreListViewContainer = (LoadMoreListViewContainer) mRootView.findViewById(R.id.load_more_list_view_container);
        mLoadMoreListViewContainer.setAutoLoadMore(true);//设置是否自动加载更多
        mLoadMoreListViewContainer.useDefaultHeader();
        mLoadMoreListViewContainer.loadMoreFinish(false, false);
        //5.添加加载更多的事件监听
        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                //模拟加载更多的业务处理
                mLoadMoreListViewContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLoadMoreListViewContainer.loadMoreFinish(false, true);
                    }
                }, 1000);
            }
        });
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
}
