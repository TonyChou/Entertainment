package com.union.fmximalaya.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.union.fmximalaya.R;
import com.union.fmximalaya.bean.ItemData;
import com.union.fmximalaya.ui.adapter.ChannelListAdapter;

/**
 * Created by zhouxiaming on 2016/3/23.
 */

public class ListTestActivity extends AppCompatActivity {
    private RecyclerView mRecycleView;
    private ChannelListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_channel);
        mRecycleView = (RecyclerView) findViewById(R.id.list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ChannelListAdapter(this, ItemData.genItemData());
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setHasFixedSize(true);
        Log.i("veve", "============== ");
        mRecycleView.setAdapter(mAdapter);
    }
}
