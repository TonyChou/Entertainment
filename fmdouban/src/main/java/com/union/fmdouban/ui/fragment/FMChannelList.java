package com.union.fmdouban.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.fmdouban.R;

/**
 * Created by zhouxiaming on 16/3/26.
 */
public class FMChannelList extends BaseFragment {
    RecyclerView recyclerView;
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
        recyclerView = (RecyclerView)mRootView.findViewById(R.id.recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getActivity(), 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }
}
