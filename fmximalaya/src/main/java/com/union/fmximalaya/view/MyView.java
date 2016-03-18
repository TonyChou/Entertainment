package com.union.fmximalaya.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by zhouxiaming on 2016/3/16.
 */

public class MyView extends View {
    private String TAG = MyView.class.getSimpleName();
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        //setMeasuredDimension(600, 100);
        log("width = " + width + "   height " + height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        log("changed = " + changed + "   left" + left + "   top" + top + "   right" + right + "   bottom" + bottom);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        log("layout = ");
    }

    private void log(String msg) {
        Log.i(TAG, "===== " + msg);
    }
}
