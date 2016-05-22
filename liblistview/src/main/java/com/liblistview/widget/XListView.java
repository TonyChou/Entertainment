package com.liblistview.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by zhouxiaming on 16/5/15.
 */
public class XListView extends ListView {
    public XListView(Context context) {
        super(context);
        mOverscrollDistance = Integer.MAX_VALUE;
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mOverscrollDistance = Integer.MAX_VALUE;
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mOverscrollDistance = Integer.MAX_VALUE;
    }
}
