package com.union.fmdouban.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.fmdouban.R;

/**
 * Created by zhouxiaming on 16/3/26.
 */
public class FMChannelList extends BaseFragment {
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
        return mRootView;
    }
}
