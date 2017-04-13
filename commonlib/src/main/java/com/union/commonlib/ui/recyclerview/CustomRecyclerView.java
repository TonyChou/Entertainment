package com.union.commonlib.ui.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 添加上拉更多控件
 * Created by zhouxiaming on 2017/4/13.
 */

public class CustomRecyclerView extends RecyclerView {
    RecyclerView.Adapter mAdapter;
    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
