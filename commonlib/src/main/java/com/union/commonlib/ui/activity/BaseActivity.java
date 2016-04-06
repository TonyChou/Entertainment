package com.union.commonlib.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by zhouxiaming on 2016/4/6.
 */

public class BaseActivity extends AppCompatActivity {
    SystemBarTintManager tintManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tintManager = new SystemBarTintManager(this);
//        // enable status bar tint
//        tintManager.setStatusBarTintEnabled(true);
//        // enable navigation bar tint
//        tintManager.setNavigationBarTintEnabled(true);
    }

    /**
     * 设置状态栏颜色
     * @param colorId
     */
    public void setStatusBarColor(int colorId) {
        tintManager.setStatusBarTintColor(getResources().getColor(colorId));
    }
}
