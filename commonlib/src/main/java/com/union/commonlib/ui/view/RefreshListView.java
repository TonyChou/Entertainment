package com.union.commonlib.ui.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
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
    private int MAX_Y_DISTANCE = 300;
    private float mStartY = 0;
    private float mLastY = 0;
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
                Log.i("veve", "====== ACTION_DOWN y1 = " + y1);
                mIsBeingDragged = false;
                mStartY = y1;
                break;
            case MotionEvent.ACTION_MOVE:
                pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                final float y2 = MotionEventCompat.getY(ev, pointerIndex);
                Log.i("veve", "====== ACTION_MOVE y2 = " + y2);
                mLastY = y2;
                translateY((int)mStartY, (int)mLastY);
                mIsBeingDragged = true;
                break;
            case MotionEvent.ACTION_UP:
                mStartY = 0;
                mLastY = 0;
                translateY((int)mStartY, (int)mLastY);
                break;

        }
        return true;
    }

    private void translateY(int beginY, int endY) {
        int dy = endY - beginY;
        if (Math.abs(dy) <= MAX_Y_DISTANCE) {
            ViewCompat.setTranslationY(this, dy);
        }

    }
}
