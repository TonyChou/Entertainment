package com.union.commonlib.ui.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by zhouxiaming on 16/3/27.
 */
public class RefreshListView extends SwipeRefreshLayout {
    private boolean mIsBeingDragged;
    private int MAX_Y_DISTANCE = 120;
    private int mStartY = 0;
    private int mLastY = 0;
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId = INVALID_POINTER;
    public RefreshListView(Context context) {
        super(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        int pointerIndex = -1;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float y1 = MotionEventCompat.getY(ev, pointerIndex);
                Log.i("veve", "====== ACTION_DOWN y = " + y1);
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                final float y2 = MotionEventCompat.getY(ev, pointerIndex);
                Log.i("veve", "====== ACTION_MOVE y = " + y2);
                mIsBeingDragged = true;
                break;
            case MotionEvent.ACTION_UP:
                break;



        }
        return true;
    }
}
