package com.union.commonlib.ui.anim;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by zhouxiaming on 2016/3/11.
 */

public class AnimUtils {
    /**
     * 给点击的view 添加点击动画，动画执行结束之后，执行点击事件后面的逻辑
     * @param v
     * @param animId
     * @param callback
     */
    public static void addClickAnimation(Context context, final View v, int animId, final IAnimCallback callback) {
        executeAnimation(context, v, animId, callback);
    }

    /**
     * 显示或隐藏Button的时候带上动画效果
     * @param context
     * @param show
     * @param v
     * @param animId
     * @param callback
     */
    public static void showOrHideButtonWithAnimation(Context context, boolean show, final View v, int animId, final IAnimCallback callback) {
        if (show) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.INVISIBLE);
        }
        executeAnimation(context, v, animId, callback);
    }


    /**
     * 执行动画效果
     * @param context
     * @param v
     * @param animId
     * @param callback
     */
    private static void executeAnimation(Context context, View v, int animId, final IAnimCallback callback) {
        Animation animation = AnimationUtils.loadAnimation(context, animId);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (callback != null) {
                    callback.animationStart();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callback != null) {
                    callback.animationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        v.startAnimation(animation);
    }
}
