package com.union.fmdouban.ui.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.union.commonlib.utils.UiUtils;
import com.union.fmdouban.R;

/**
 * Created by zhouxiaming on 2016/3/14.
 */

public class PlayerControllerView extends LinearLayout {
    private Context mContext;
    public PlayerControllerView(Context context) {
        super(context);
        init(context, null, -1);
    }

    public PlayerControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public PlayerControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        setGravity(Gravity.CENTER);
        LinearLayout view = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_player_pannel, null);
        addView(view);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, 0, t, r, b);
    }
}
