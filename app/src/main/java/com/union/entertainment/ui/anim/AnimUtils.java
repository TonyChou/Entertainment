package com.union.entertainment.ui.anim;

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
    public static void addClickAnimation(Context context, final View v, int animId, final AnimCallback callback) {
        Animation animation = AnimationUtils.loadAnimation(context, animId);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callback != null) {
                    callback.handleClickEvent();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        v.startAnimation(animation);
    }
}
