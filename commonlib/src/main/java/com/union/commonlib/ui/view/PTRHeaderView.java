package com.union.commonlib.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by zhouxiaming on 16/4/3.
 */
public class PTRHeaderView extends LinearLayout implements PtrUIHandler {
    private String TAG = "PTRHeaderView";
    public PTRHeaderView(Context context) {
        super(context);
        setBackgroundColor(Color.RED);
    }

    public PTRHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        Log.i(TAG, "onUIReset ====== ");
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        Log.i(TAG, "onUIRefreshPrepare ====== ");
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        Log.i(TAG, "onUIRefreshBegin ====== ");
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        Log.i(TAG, "onUIRefreshComplete ====== ");
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        Log.i(TAG, "onUIPositionChange ====== ");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(100, 100);
    }
}
