package com.union.commonlib.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.union.commonlib.ui.ActionBarPage;
import com.union.commonlib.ui.fragment.BaseFragment;

/**
 * Created by zhouxiaming on 2016/4/6.
 */

public class BaseActivity extends AppCompatActivity {
    protected String TAG = this.getClass().getSimpleName();
    SystemBarTintManager tintManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tintManager = new SystemBarTintManager(this);
//        // enable status bar tint
//        tintManager.setStatusBarTintEnabled(true);
//        // enable navigation bar tint
//        tintManager.setNavigationBarTintEnabled(true);
    }

    /**
     * 设置状态栏颜色
     * @param colorId
     */
    public void setStatusBarColor(int colorId) {
        tintManager.setStatusBarTintColor(getResources().getColor(colorId));
    }

    /**
     * 显示ActionBar
     */
    public void showActionBar() {

    }

    public void hideActionBar() {

    }

    /**
     * replace container fragment
     *
     * @param fragment your's fragment
     * @param isAddToBackStack is add to fragment back stack
     */
    public void replaceContainerFragmemt(int containerId, BaseFragment fragment, boolean isAddToBackStack) {
        if (null == fragment) {
            return;
        }
        try {
            // setmPayBaseFragment(mPayBaseFragment);
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(containerId, fragment, fragment.getClass().toString());
            if (isAddToBackStack) {
                transaction.addToBackStack(fragment.getClass().toString());
            }
            transaction.commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Actionbar
     * @param actionBarPage
     */
    public void syncToolBarStatus(ActionBarPage actionBarPage) {

    }
}
