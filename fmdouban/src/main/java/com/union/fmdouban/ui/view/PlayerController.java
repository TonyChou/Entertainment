package com.union.fmdouban.ui.view;

import android.content.Context;
import android.view.View;

import com.union.commonlib.ui.anim.AnimCallbackImp;
import com.union.commonlib.ui.anim.AnimUtils;
import com.union.commonlib.utils.UiUtils;
import com.union.fmdouban.R;

/**
 * Created by zhouxiaming on 2016/3/14.
 */

public class PlayerController implements View.OnClickListener{
    private View mControlPanelView;
    private static PlayerController instance;
    private View mPreButton, mPlayButton, mNextButton;
    private View mPreIcon, mPlayIcon, mNextIcon;
    private Context mContext;

    private PlayerController() {

    }

    public static synchronized PlayerController getInstance() {
        if (instance == null) {
            instance = new PlayerController();
        }

        return instance;
    }

    public void init(Context context, View panelView) {
        this.mControlPanelView = panelView;
        this.mContext = context;
        mPreButton = mControlPanelView.findViewById(R.id.pre);
        mPlayButton = mControlPanelView.findViewById(R.id.play);
        mNextButton = mControlPanelView.findViewById(R.id.next);
        addListener();
    }

    private void addListener() {
        mPreButton.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
    }

    private void runButtonAnim(final View v) {
        AnimUtils.addClickAnimation(mContext, v, R.anim.click_anim, new AnimCallbackImp() {
            @Override
            public void animationEnd() {
                if (v == mPreButton) {

                } else if (v == mPlayButton) {

                } else if (v == mNextButton) {

                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        runButtonAnim(v);
    }
}
