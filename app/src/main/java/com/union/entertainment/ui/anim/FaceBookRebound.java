package com.union.entertainment.ui.anim;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringUtil;

/**
 * Created by zhouxiaming on 2016/3/10.
 */

public class FaceBookRebound {
    private static String TAG = "FaceBookRebound";
    public static void addSpringAnimation(final View v, final Spring spring) {
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
                        break;
                }
                return false;
            }
        });

        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.8);
                Log.i(TAG, "CurrentValue: " +spring.getCurrentValue() + "   mappedValue: " + mappedValue + "  " + v.getId());
                v.setScaleX(mappedValue);
                v.setScaleY(mappedValue);
            }

            @Override
            public void onSpringAtRest(Spring spring) {
                Log.i(TAG, "onSpringAtRest");
            }
        });
    }
}
