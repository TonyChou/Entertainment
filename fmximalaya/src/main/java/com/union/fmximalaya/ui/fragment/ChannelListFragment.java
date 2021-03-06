package com.union.fmximalaya.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.fmximalaya.R;
import com.union.fmximalaya.bean.ItemData;
import com.union.fmximalaya.ui.adapter.ChannelListAdapter;

/**
 * 显示channels列表
 */
public class ChannelListFragment extends BaseFragment implements AbsListView.OnItemClickListener {
    private RecyclerView mListView;
    private ChannelListAdapter mAdapter;

    public static ChannelListFragment newInstance(String param1, String param2) {
        ChannelListFragment fragment = new ChannelListFragment();
        return fragment;
    }

    public ChannelListFragment() {
    }



    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_channel;
    }

    @Override
    protected void initView() {
        super.initView();
        mListView = (RecyclerView) mRootView.findViewById(R.id.list_view);
        mAdapter = new ChannelListAdapter(this.getActivity(), ItemData.genItemData());
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


}
