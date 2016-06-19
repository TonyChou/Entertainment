package com.union.fmdouban.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.union.commonlib.ui.activity.BaseActivity;
import com.union.fmdouban.R;
import com.union.fmdouban.ui.fragment.FMChannelsFragment;

public class FMChannelSelectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fmchannel_select);
        addFragment();
    }

    private void addFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FMChannelsFragment mChannelsFragment = FMChannelsFragment.newInstance();
        fragmentManager.beginTransaction().add(R.id.container, mChannelsFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
