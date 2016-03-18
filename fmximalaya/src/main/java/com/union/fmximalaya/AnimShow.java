package com.union.fmximalaya;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.union.commonlib.ui.anim.AnimUtils;
import com.union.commonlib.ui.anim.IAnimCallback;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhouxiaming on 2016/3/15.
 */

public class AnimShow {

    static AtomicInteger atomicInteger = new AtomicInteger();
    static Handler handler = new Handler();
    public static void main(String[] args) {
        System.out.println(atomicInteger.getAndDecrement());
    }
    public static void showAnim(final Context context, final View v) {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showAnim(context, R.anim.activity_close_enter, v);
//            }
//        }, atomicInteger.addAndGet(1));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showAnim(context, R.anim.send_left_to_right_anim, v);
            }
        }, atomicInteger.addAndGet(1));
    }
//     private static void anim1(Context context, View v) {
//        showAnim(context, R.anim.activity_close_enter, v);
//    }

     private static void showAnim(Context context, int animId, View v) {
        AnimUtils.showOrHideButtonWithAnimation(context, true, v, animId, new IAnimCallback() {

            @Override
            public void handleClickEvent() {

            }

            @Override
            public void animationEnd() {

            }

            @Override
            public void animationStart() {

            }

            @Override
            public void animationRepeat() {

            }
        });
    }
}
