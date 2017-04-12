package com.union.commonlib.ui.fragment;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;

import com.union.commonlib.ui.activity.BaseActivity;

/**
 * Created by zhouxiaming on 16/3/6.
 */
public class BaseFragment extends Fragment {
    protected String TAG = this.getClass().getSimpleName();
    protected static final String ARG_PARAM1 = "param1";
    protected static final String ARG_PARAM2 = "param2";
    protected View mRootView;
    protected String mParam1;
    protected String mParam2;
    protected Activity mActivity;
    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgument();
        mActivity = this.getActivity();
    }

    protected void getArgument() {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    protected void setArguments(String param1, String param2) {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        setArguments(args);
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
}
