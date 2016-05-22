package com.union.commonlib.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.liblistview.widget.AbsListView;
import com.liblistview.widget.ListView;
import com.liblistview.widget.OverScrollViewListener;
import com.union.commonlib.R;


/**
 * Created by zhouxiaming on 16/5/21.
 */
public class RefreshFooterView extends FrameLayout implements AbsListView.OnScrollListener {
    public static final int LOADMORE_BEFORE    = 0x0001;//刷新之前
    public static final int LOADMORE_DOING     = LOADMORE_BEFORE + 1; //正在刷新
    public static final int LOADMORE_COMPLETE  = LOADMORE_DOING + 1; //刷新结束
    public int CURRENT_STATUS                 = -1; //当前的刷新状态

    Context            mContext;
    CircularProgress   mRefreshLoading;
    TextView           mRefreshText;
    ListView           mListView;
    LoadmoreListener   mLoadMoreListener;
    boolean            mHasMoreData = true;
    public RefreshFooterView(Context context) {
        super(context);
        init(context);
    }

    public RefreshFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View headerView = LayoutInflater.from(context).inflate(R.layout.refresh_list_footer, null);
        addView(headerView);
        mRefreshLoading = (CircularProgress) findViewById(R.id.rl_loading);
        mRefreshText    = (TextView) findViewById(R.id.refresh_text);
    }

    /**
     * 初始状态
     */
    public void reset() {
        mRefreshLoading.setVisibility(View.INVISIBLE);
        mRefreshText.setText(mContext.getString(R.string.loadmore_begin));
        CURRENT_STATUS = LOADMORE_BEFORE;
    }

    /**
     * 正在刷新
     */
    public void loadMoreDoing() {
        mRefreshLoading.setVisibility(View.VISIBLE);
        mRefreshText.setText(mContext.getString(R.string.loadmore_doing));
        CURRENT_STATUS = LOADMORE_DOING;
        if (mLoadMoreListener != null) {
            mLoadMoreListener.onLoadMoreDoing();
        }
    }

    /**
     * 刷新结束
     */
    public void loadMoreComplete(boolean hasMore) {
        mRefreshLoading.setVisibility(View.INVISIBLE);

        CURRENT_STATUS = LOADMORE_COMPLETE;
        mHasMoreData = hasMore;
        if (mHasMoreData) {
            mRefreshText.setText(mContext.getString(R.string.loadmore_begin));
        } else {
            mRefreshText.setText(mContext.getString(R.string.loadmore_no));
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.i("veve", "scrollState = " + scrollState + "   " + view.getCount());
        if (scrollState == 0 && CURRENT_STATUS != LOADMORE_DOING && view.getLastVisiblePosition() == view.getCount() -1 ) {
            loadMoreDoing();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //Log.i("veve", "firstVisibleItem = "+ firstVisibleItem + "  visibleItemCount = " + visibleItemCount + "  totalItemCount = " + totalItemCount  + "  last = " + view.getLastVisiblePosition());
    }

    public void setLoadMoreListener(LoadmoreListener listener) {
        this.mLoadMoreListener = listener;
    }



    public interface LoadmoreListener {
        void onLoadMoreDoing();
    }
}
