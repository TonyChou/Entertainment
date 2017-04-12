package com.union.fmdouban.ui.fragment;

import com.tulips.douban.model.ChannelsPage;
import com.tulips.douban.service.DoubanParamsGen;
import com.tulips.douban.service.DoubanService;
import com.tulips.douban.service.DoubanUrl;
import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.fmdouban.R;
import com.union.net.ApiClient;
import com.union.net.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhouxiaming on 2017/4/12.
 */

public class DoubanFMFragment extends BaseFragment {
    private RetrofitClient mApiClient;
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
                    public void onNext(ChannelsPage value) {
                        System.out.println("onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }
}
