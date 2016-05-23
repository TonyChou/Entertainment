package com.union.commonlib.ui.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.liblistview.widget.ListView;
import com.liblistview.widget.OverScrollViewListener;
import com.union.commonlib.R;


/**
 * Created by zhouxiaming on 16/5/21.
 */
public class RefreshHeaderView extends FrameLayout implements OverScrollViewListener {
    public static final int REFRESH_BEFORE    = 0x0001;//刷新之前
    public static final int REFRESH_DOING     = REFRESH_BEFORE + 1; //正在刷新
    public static final int REFRESH_COMPLETE  = REFRESH_DOING + 1; //刷新结束
    public int CURRENT_STATUS                 = -1; //当前的刷新状态

    Context            mContext;
    CircularProgress   mRefreshLoading;
    AppCompatTextView  mRefreshImage;
    TextView           mRefreshText;
    ListView           mListView;
    RefreshListener    mRefreshListener;
    public RefreshHeaderView(Context context) {
        super(context);
        init(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View headerView = LayoutInflater.from(context).inflate(R.layout.refresh_list_header, null);
        addView(headerView);
        mRefreshLoading = (CircularProgress) findViewById(R.id.rl_loading);
        mRefreshImage   = (AppCompatTextView) findViewById(R.id.refresh_icon);
        TintUtils.setBackgroundTint(mContext, mRefreshImage, R.color.light_blue);
        mRefreshText    = (TextView) findViewById(R.id.refresh_text);
    }

    /**
     * 初始状态
     */
    public void reset() {
        mRefreshLoading.setVisibility(View.GONE);
        mRefreshImage.setVisibility(View.VISIBLE);
        mRefreshText.setText(mContext.getString(R.string.refresh_pulldown));
        CURRENT_STATUS = REFRESH_BEFORE;
    }

    /**
     * 正在刷新
     */
    public void refreshDoing() {
        mRefreshLoading.setVisibility(View.VISIBLE);
        mRefreshImage.setVisibility(View.GONE);
        mRefreshText.setText(mContext.getString(R.string.refresh_doing));
        CURRENT_STATUS = REFRESH_DOING;
        if (mRefreshListener != null) {
            mRefreshListener.onRefreshDoing();
        }
    }

    /**
     * 刷新结束
     */
    public void refreshComplete() {
        mRefreshLoading.setVisibility(View.GONE);
        mRefreshImage.setVisibility(View.VISIBLE);
        mRefreshText.setText(mContext.getString(R.string.refresh_complete));
        CURRENT_STATUS = REFRESH_COMPLETE;
        if (mListView != null) {
            mListView.post(new Runnable() {
                @Override
                public void run() {
                    mListView.springBackOverScrollHeaderView();
                }
            });
        }
    }

    @Override
    public void onNotCompleteVisable(int overScrollPosition, View overScrollerView, ListView listView) {
        reset();
    }

    @Override
    public void onViewCompleteVisable(int overScrollPosition, View overScrollerView, ListView listView) {
        reset();
    }

    @Override
    public boolean onViewCompleteVisableAndReleased(int overScrollPosition, View overScrollerView, ListView listView) {
        refreshDoing();
        mListView = listView;
        return true;
    }

    @Override
    public void onViewNotCompleteVisableAndReleased(int overScrollPosition, View overScrollerView, ListView listView) {
        reset();
    }

    public void setRefreshListener(RefreshListener listener) {
        this.mRefreshListener = listener;
    }

    public interface RefreshListener {
        void onRefreshDoing();
    }
}
