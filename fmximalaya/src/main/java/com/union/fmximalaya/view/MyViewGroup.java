package com.union.fmximalaya.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zhouxiaming on 2016/3/16.
 */

public class MyViewGroup extends ViewGroup {
    private String TAG = MyViewGroup.class.getSimpleName();
    private final int UPDATE_MSG = 0x0001;
    final int width = 900;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int w = msg.arg1;
            TextView child = (TextView)getChildAt(0);
            child.layout(0, 0, w, 300);
            child.setText("hello android");
        }
    };
    public MyViewGroup(Context context) {
        super(context);
        log("MyViewGroup 1");
        startRefresh();
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        log("MyViewGroup 2");
        startRefresh();
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        log("MyViewGroup 3");
        startRefresh();
    }

    private void startRefresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int w = 0;
                int childCount = getChildCount();


                while (w < width) {
                    Message msg = handler.obtainMessage(UPDATE_MSG);
                    msg.arg1 = w;
                    msg.sendToTarget();
                    w++;
                    SystemClock.sleep(50);
                }
            }
        }).start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        log("width = " + width + "   height = " + height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childrenCount = getChildCount();
        log("layout = " + l + "  " + t + "  " + r + "  " + b) ;
        for (int i = 0; i < childrenCount; i++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            log(childHeight + "   " + childHeight);
            //child.layout(0, 0, 800, 300);
        }
    }
    private void log(String msg) {
        Log.i(TAG, "===== " + msg);
    }
}
