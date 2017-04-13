package com.union.commonlib.ui.recyclerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.union.commonlib.R;

/**
 * Created by zhouxiaming on 2017/4/13.
 */

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {
    public CustomSwipeRefreshLayout(Context context) {
        super(context);
        setStyle();
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setStyle();
    }

    private void setStyle() {
        setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        setProgressBackgroundColor(R.color.light_blue);
    }

}
