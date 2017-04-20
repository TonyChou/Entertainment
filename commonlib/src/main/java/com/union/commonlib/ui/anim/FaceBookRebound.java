package com.union.commonlib.ui.anim;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringUtil;
import com.nineoldandroids.view.ViewHelper;
import com.union.commonlib.utils.LogUtils;

/**
 * Created by zhouxiaming on 2016/3/10.
 */

public class FaceBookRebound {
    private static String TAG = "FaceBookRebound";
    private static float BEGIN_VALUE = 1f;
    private static float END_VALUE = 1f;
    public static void addSpringAnimation(final View v, final Spring spring, final IAnimCallback callback) {
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // When pressed start solving the spring to 1.
                        spring.setEndValue(1);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // When released start solving the spring to 0.
                        spring.setEndValue(0);
                        if (callback != null) {
                            callback.animationEnd(v);
                        }
                        break;
                }
                return true;
            }
        });



        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.8);
                ViewHelper.setScaleX(v, mappedValue);
                ViewHelper.setScaleY(v, mappedValue);
            }

            @Override
            public void onSpringAtRest(Spring spring) {

            }

            @Override
            public void onSpringEndStateChange(Spring spring) {
                super.onSpringEndStateChange(spring);
            }
        });
    }

    public static void addSpringAnimation(final View view, final Spring spring, final float scale, final IAnimCallback callback) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // When pressed start solving the spring to 1.
                        spring.setEndValue(scale);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // When released start solving the spring to 0.
                        spring.setEndValue(0);
                        if (callback != null) {
                            callback.animationEnd(v);
                        }
                        break;
                }
                return true;
            }
        });



        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, scale, scale, 0.8 * scale);
                ViewHelper.setScaleX(view, mappedValue);
                ViewHelper.setScaleY(view, mappedValue);
            }

            @Override
            public void onSpringAtRest(Spring spring) {

            }

            @Override
            public void onSpringEndStateChange(Spring spring) {
                super.onSpringEndStateChange(spring);
            }
        });
    }
}
