package com.union.commonlib.ui.fragment;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.union.commonlib.ui.ActionBarPage;
import com.union.commonlib.ui.activity.BaseActivity;
import com.union.commonlib.utils.LogUtils;

/**
 * Created by zhouxiaming on 16/3/6.
 */
public abstract class BaseFragment extends Fragment {
    protected String TAG = this.getClass().getSimpleName();
    protected static final String ARG_PARAM1 = "param1";
    protected static final String ARG_PARAM2 = "param2";
    protected View mRootView;
    protected String mParam1;
    protected String mParam2;
    protected Activity mActivity;
    private HandlerThread mWorkThread;
    private Handler mWorkHandler;
    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutResourceId(), container, false);
        initView();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long begin = System.currentTimeMillis();
        loadData();
        long end = System.currentTimeMillis();
        LogUtils.i(TAG, "loaddata time : " + (end - begin));
    }

    protected abstract int getLayoutResourceId();
    protected void initView() {

    }
    protected void loadData() {

    }

    protected Handler getWorkHandler() {
        if (mWorkThread == null) {
            mWorkThread = new HandlerThread(String.format("Work Thread: %s", TAG));
            mWorkThread.start();
            mWorkHandler = new Handler(mWorkThread.getLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    handleWorkThreadMessage(msg);
                }
            };
        }
        return mWorkHandler;
    }

    protected void handleWorkThreadMessage(Message msg) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWorkHandler != null) {
            mWorkHandler.removeCallbacksAndMessages(null);
            mWorkHandler = null;
        }
        if (mWorkThread != null) {
            mWorkThread.quit();
            mWorkThread = null;
        }

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    public boolean onBackPress() {
        return true;
    }

    public void showWithAnimation() {

    }

    public void hideWithAnimation() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(TAG, "onResume  ========  ");
    }

    /**
     * 设置状态栏颜色
     * @param colorId
     */
    public void setStatusBarColor(int colorId) {
        if (mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).setStatusBarColor(colorId);
        }
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 设置Actionbar
     * @param actionBarPage
     */
    public void syncToolBarStatus(ActionBarPage actionBarPage) {
        if (mActivity instanceof BaseActivity) {
            ((BaseActivity) mActivity).syncToolBarStatus(actionBarPage);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
