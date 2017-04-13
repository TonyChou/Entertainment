package com.union.commonlib.ui.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.union.commonlib.utils.LogUtils;

/**
 * Created by zhouxiaming on 2017/4/13.
 */

public class CustomRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private String TAG = "CustomRecyclerViewScrollListener";
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LogUtils.i(TAG, "onScrolled --> dx = " + dx + "  dy = " + dy );
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LogUtils.i(TAG, "onScrollStateChanged newState = " + newState);
        if (needLoadMore()) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int lastVisibleItemPosition = -1;
            if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            } else if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            }

            //已经是最后一个
            if (lastVisibleItemPosition + 1 == layoutManager.getItemCount()) {
                loadMore();
            }
        }
    }

    /**
     * 入股需要加载更多则要重写这个方法, 并且返回true
     */
    protected boolean needLoadMore() {
        return false;
    }

    /**
     * 加载更多
     */
    public void loadMore() {

    }
}
