package com.union.commonlib.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * Created by zhouxiaming on 2017/4/14.
 */

public class InsetsFrameLayout extends FrameLayout {
    public InsetsFrameLayout(Context context) {
        this(context, null);
    }

    public InsetsFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InsetsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        int r = insets.getSystemWindowInsetRight();
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight() + r, getPaddingBottom());
        return super.onApplyWindowInsets(insets);
    }
}
