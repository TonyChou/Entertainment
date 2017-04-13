package com.union.fmdouban.ui.fragment;

import android.view.View;

import com.liblistview.widget.PinnedHeaderExpandableListView;
import com.tulips.douban.model.ChannelsPage;
import com.tulips.douban.service.DoubanParamsGen;
import com.tulips.douban.service.DoubanService;
import com.tulips.douban.service.DoubanUrl;
import com.union.commonlib.ui.fragment.BaseFragment;
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

public class DoubanFMFragment extends BaseFragment implements View.OnClickListener, RefreshHeaderView.RefreshListener{
    private PinnedHeaderExpandableListView mListView;
    private RetrofitClient mApiClient;
    private ChannelGroupAdapter mAdapter;
    private RefreshHeaderView mHeaderView;
    public static DoubanFMFragment newInstance() {
        DoubanFMFragment fragment = new DoubanFMFragment();
        return fragment;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_doubanfm_layout;
    }

    @Override
    protected void initView() {
        super.initView();
        this.mListView = (PinnedHeaderExpandableListView) mRootView.findViewById(R.id.channel_list);
        this.mAdapter = new ChannelGroupAdapter(mActivity, mListView, this);
        this.mListView.setAdapter(mAdapter);
        this.mListView.setOnScrollListener(mAdapter);
        this.mListView.setGroupIndicator(null);
        addHeaderView();
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
        mApiClient = ApiClient.getDoubanAPiClient(DoubanUrl.API_HOST);
        DoubanService douBanService = mApiClient.createApi(DoubanService.class);
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
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.i(TAG, "onComplete ==== ");
                        mHeaderView.refreshComplete();
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
            this.mAdapter.setGroupList(groups);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefreshDoing() {
        loadData();
    }
}
