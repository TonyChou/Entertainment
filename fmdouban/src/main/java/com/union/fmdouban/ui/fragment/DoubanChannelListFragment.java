package com.union.fmdouban.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.liblistview.widget.PinnedHeaderExpandableListView;
import com.tulips.douban.model.ChannelsPage;
import com.tulips.douban.model.PlayerPage;
import com.tulips.douban.service.DoubanParamsGen;
import com.tulips.douban.service.DoubanService;
import com.tulips.douban.service.DoubanUrl;
import com.union.commonlib.ui.ActionBarPage;
import com.union.commonlib.ui.activity.BaseActivity;
import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.commonlib.ui.view.CircularProgress;
import com.union.commonlib.ui.view.RefreshHeaderView;
import com.union.commonlib.utils.LogUtils;
import com.union.fmdouban.R;
import com.union.fmdouban.ui.adapter.ChannelGroupAdapter;
import com.union.net.ApiClient;
import com.union.net.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhouxiaming on 2017/4/12.
 */

public class DouBanChannelListFragment extends BaseFragment implements View.OnClickListener, RefreshHeaderView.RefreshListener, ChannelGroupAdapter.OnChannelClickListener {
    private PinnedHeaderExpandableListView mListView;
    private DoubanService douBanService;
    private ChannelGroupAdapter mAdapter;
    private RefreshHeaderView mHeaderView;
    private CircularProgress mLoadingBar;
    private View mEmptyView;
    public static DouBanChannelListFragment newInstance() {
        DouBanChannelListFragment fragment = new DouBanChannelListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_doubanfm_layout;
    }

    @Override
    protected void initView() {
        super.initView();
        this.mLoadingBar = (CircularProgress)mRootView.findViewById(R.id.rl_loading);
        this.mLoadingBar.setVisibility(View.VISIBLE);
        this.mEmptyView = mRootView.findViewById(R.id.empty_layout);
        this.mListView = (PinnedHeaderExpandableListView) mRootView.findViewById(R.id.channel_list);
        this.mAdapter = new ChannelGroupAdapter(mActivity, mListView, this);
        this.mListView.setAdapter(mAdapter);
        this.mListView.setOnScrollListener(mAdapter);
        this.mListView.setGroupIndicator(null);
        addHeaderView();
        //自动刷新数据
        mListView.showOverScrollHeader();
        this.mEmptyView.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        syncToolBarStatus(ActionBarPage.DOUBAN_FM_MAIN_PAGE);
    }

    private void addHeaderView() {
        mHeaderView = new RefreshHeaderView(mActivity);
        mHeaderView.setRefreshListener(this);
        mListView.setOverScrollHeader(mHeaderView);
        mListView.setOverScrollListener(mHeaderView);
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (douBanService == null) {
            RetrofitClient mApiClient = ApiClient.getDoubanAPiClient(DoubanUrl.API_HOST);
            douBanService = mApiClient.createApi(DoubanService.class);
        }

        douBanService.appChannels(DoubanParamsGen.genGetAppChannelsParams())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ChannelsPage>() {
                    @Override
                    public void onNext(ChannelsPage channelsPage) {
                        LogUtils.i(TAG, "onNext ==== ");
                        updateData(channelsPage);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i(TAG, "onError ==== ");

                        if (LogUtils.isDebug) {
                            e.printStackTrace();
                        }
                        mListView.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                        mLoadingBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.i(TAG, "onComplete ==== ");
                        mHeaderView.refreshComplete();
                        mLoadingBar.setVisibility(View.GONE);
                    }
                });
    }

    private void updateData(ChannelsPage page) {
        if (page != null && page.groupList != null && page.groupList.size() > 0) {
            List<ChannelsPage.Groups> groups = new ArrayList<>();
            for (ChannelsPage.Groups group : page.groupList) {
                if (group.channels.size() > 0) {
                    groups.add(group);
                }
            }
            this.mListView.setVisibility(View.VISIBLE);
            this.mLoadingBar.setVisibility(View.GONE);
            this.mEmptyView.setVisibility(View.GONE);
            this.mAdapter.setGroupList(groups);
            this.mAdapter.notifyDataSetChanged();
        } else {
            this.mListView.setVisibility(View.GONE);
            this.mLoadingBar.setVisibility(View.GONE);
            this.mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.empty_layout) {
            reloadData();
        }
    }

    private void reloadData() {
        this.mListView.setVisibility(View.GONE);
        this.mLoadingBar.setVisibility(View.VISIBLE);
        this.mEmptyView.setVisibility(View.GONE);
        loadData();
    }

    @Override
    public void onRefreshDoing() {
        loadData();
    }


    @Override
    public void onChannelClick(ChannelsPage.Channel channel) {
        BaseFragment fragment = FMPlayerFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable("channel", channel);
        fragment.setArguments(bundle);
        ((BaseActivity)mActivity).replaceContainerFragment(fragment, true);
    }
}
