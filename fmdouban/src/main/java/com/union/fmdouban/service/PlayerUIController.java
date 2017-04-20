package com.union.fmdouban.service;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.Spring;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;
import com.tulips.douban.model.ChannelsPage;
import com.tulips.douban.model.PlayerPage;
import com.union.commonlib.ui.anim.AnimCallbackImp;
import com.union.commonlib.ui.anim.AnimUtils;
import com.union.commonlib.ui.anim.FaceBookRebound;
import com.union.commonlib.ui.view.CircleProgressBar;
import com.union.commonlib.ui.view.TintUtils;
import com.union.commonlib.utils.LogUtils;
import com.union.fmdouban.R;

import com.union.fmdouban.play.PlayerControllerListener;
import com.union.fmdouban.ui.fragment.FMPlayerFragment;
import com.union.fmdouban.ui.lyric.widget.LyricView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 2016/3/14.
 */

public class PlayerUIController implements View.OnClickListener, PlayerControllerListener {
    private String TAG = PlayerUIController.class.getSimpleName();


    private View mControlPanelView;
    private static PlayerUIController instance;
    private View mPreButton, mPlayButton, mNextButton;
    private View mCoverBgMask;
    ImageView mCover;
    private AppCompatTextView mPreIcon, mPlayIcon, mNextIcon, mFavIcon;
    private FMPlayerFragment mFragment;
    Animation mCoverBgMaskAnimation, mCoverRotateAnimation;
    LyricView mLyricView;
    private CircleProgressBar mProgressBar;
    private View mProgressBarLayout;
    private Activity mActivity;
    private View mFavButton;
    private View mControlButtons;
    private BaseSpringSystem mSpringSystem;

    private boolean mCoverAnimIsRun = false;
    private TextView mChannelNameView, mSongNameView;
    private int slidePanelHeight;
    private float mProgressBarLayoutBeginX, mProgressBarLayoutBeginY, mProgressBarLayoutDx, mProgressBarLayoutDy, mProgressBarLayoutScaleDes;
    private float mControlButtonsBeginX, mControlButtonsBeginY, mControlButtonsDx, mControlButtonsDy, mControlButtonsScaleDes, mControlButtonScaleWidth;
    private float mFavButtonsBeginX, mFavButtonsBeginY, mFavButtonsDx, mFavButtonsDy, mFavButtonsScaleDes;
    private List<Spring> springMap = new ArrayList<Spring>();
    private float BUTTON_TRANSLATION_DX = 100; //播放控制按钮X轴的偏移量
    private int playerButtonSize, playerButtonMarginRight;
    private int playerButtonMargin;
    private PlayerUIController() {

    }

    public static synchronized PlayerUIController getInstance() {
        if (instance == null) {
            instance = new PlayerUIController();
        }

        return instance;
    }

    public void init(FMPlayerFragment fragment, View panelView, BaseSpringSystem springSystem) {
        this.mControlPanelView = panelView;
        this.mFragment = fragment;
        this.mActivity = mFragment.getActivity();
        this.mSpringSystem = springSystem;
        mProgressBarLayout = mControlPanelView.findViewById(R.id.cover_and_progress);
        mLyricView = (LyricView) mControlPanelView.findViewById(R.id.lyric_panel);
        mLyricView.setVisibility(View.GONE);
        mCover = (ImageView) mControlPanelView.findViewById(R.id.cover);
        mControlButtons = mControlPanelView.findViewById(R.id.control_buttons);
        mPreButton = mControlPanelView.findViewById(R.id.pre);
        mPreIcon = (AppCompatTextView)mPreButton.findViewById(R.id.button_icon);
        mPreIcon.setBackgroundResource(R.drawable.ic_play_arrow_black_48dp);
        ViewHelper.setRotation(mPreIcon, 180f);
        mPlayButton = mControlPanelView.findViewById(R.id.play);
        mPlayIcon = (AppCompatTextView)mPlayButton.findViewById(R.id.button_icon);
        mNextButton = mControlPanelView.findViewById(R.id.next);
        mNextIcon = (AppCompatTextView)mNextButton.findViewById(R.id.button_icon);
        mNextIcon.setBackgroundResource(R.drawable.ic_play_arrow_black_48dp);
        mChannelNameView = (TextView) mControlPanelView.findViewById(R.id.channel_name);
        mSongNameView = (TextView) mControlPanelView.findViewById(R.id.song_name);

        mCoverBgMask = mControlPanelView.findViewById(R.id.cover_bg_mask);
        mProgressBar = (CircleProgressBar) mControlPanelView.findViewById(R.id.music_progress);
        mProgressBar.setProgress(0);
        mCoverBgMaskAnimation = AnimationUtils.loadAnimation(this.mFragment.getActivity(), R.anim.record_anim);
        mCoverRotateAnimation = AnimationUtils.loadAnimation(this.mFragment.getActivity(), R.anim.cover_rotate_repeat_anim);
        mCoverRotateAnimation.setInterpolator(new LinearInterpolator());
        mFavButton = mControlPanelView.findViewById(R.id.fav_button);
        mFavIcon = (AppCompatTextView) mFavButton.findViewById(R.id.button_icon);
        mFavIcon.setBackgroundResource(R.drawable.ic_playlist_add_black_48dp);
        addOnTouchSpringAnimation(1.0f, mFavButton);

        TintUtils.setBackgroundTint(mActivity, (AppCompatTextView) mFavButton.findViewById(R.id.button_icon), R.color.white);
        mCoverRotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mCoverAnimIsRun = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCoverAnimIsRun = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        TintUtils.setBackgroundTint(this.mFragment.getActivity(), R.color.white, mPreIcon, mPlayIcon, mNextIcon, mFavIcon);
        addListener();

        mCoverAnimIsRun = false;
        measureComponentSize();
    }

    /**
     * 计算控件的size
     */
    private void measureComponentSize() {
        if (mControlPanelView == null) {
            return;
        }

        slidePanelHeight = mActivity.getResources().getDimensionPixelSize(R.dimen.slide_panel_height);
        playerButtonMargin = mActivity.getResources().getDimensionPixelSize(R.dimen.player_button_margin_10);
        playerButtonSize = mActivity.getResources().getDimensionPixelSize(R.dimen.circle_button_height);
        playerButtonMarginRight = mActivity.getResources().getDimensionPixelSize(R.dimen.player_button_margin);
        //控制按钮面板需要整体向右偏移的值
        Display display = mActivity.getWindowManager().getDefaultDisplay();


        final float controlButtonDxRight = (float) (display.getWidth() - playerButtonSize * 3 - playerButtonMarginRight * 2) / 2.0f;
        final float controlButtonDxRightReal = controlButtonDxRight * (1.0f - slidePanelHeight / controlButtonDxRight);
        mProgressBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                int width = mProgressBarLayout.getWidth();
                int height = mProgressBarLayout.getHeight();
                int size = Math.min(width, height);
                float x = ViewCompat.getX(mProgressBarLayout);
                float y = ViewCompat.getY(mProgressBarLayout);
                mProgressBarLayoutBeginX = x + ((float) width  - (float)slidePanelHeight) / 2.0f;
                mProgressBarLayoutBeginY = y + ((float) height  - (float)slidePanelHeight) / 2.0f;
                mProgressBarLayoutDx = mProgressBarLayoutBeginX;
                mProgressBarLayoutDy = mProgressBarLayoutBeginY;
                mProgressBarLayoutScaleDes = (float) slidePanelHeight/ (float) size;
                mProgressBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mControlButtons.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                int width = mControlButtons.getWidth();
                int height = mControlButtons.getHeight();
                float x = ViewCompat.getX(mControlButtons);
                float y = ViewCompat.getY(mControlButtons);

                mControlButtonsScaleDes = (float) slidePanelHeight/ (float) height;
                mControlButtonScaleWidth = (float)width *  mControlButtonsScaleDes;

                mControlButtonsBeginX = x + ((float) width  - mControlButtonScaleWidth) / 2.0f + controlButtonDxRightReal;
                mControlButtonsBeginY = y + ((float) height  - (float)slidePanelHeight) / 2.0f;
                mControlButtonsDx = mControlButtonsBeginX;
                mControlButtonsDy = mControlButtonsBeginY;
                LogUtils.i(TAG, "mControlButtonsDx = " + mControlButtonsDx + " mControlButtonsDy = " + mControlButtonsDy + " mControlButtonsScaleDes = " + mProgressBarLayoutScaleDes);
                LogUtils.i(TAG, String.format("slidePanelHeight: %d,  width: %d, height: %d, x: %f, y: %f", slidePanelHeight, width, height, x, y));
                mControlButtons.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mFavButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                int width = mFavButton.getWidth();
                int height = mFavButton.getHeight();
                float x = ViewCompat.getX(mFavButton);
                float y = ViewCompat.getY(mFavButton);
                LogUtils.i("veve", " mFavButton width " + width);
                mFavButtonsScaleDes = ((float) slidePanelHeight - (float)playerButtonMargin)/ (float) height;
                //两个按钮分别位移了200和100，因此需要将位移的值加上
                float dx = mControlButtonScaleWidth + BUTTON_TRANSLATION_DX * 3 + controlButtonDxRightReal + controlButtonDxRight;
                mFavButtonsBeginX = x + ((float) width - dx) / 2.0f;
                mFavButtonsBeginY = y + ((float) height  - (float)slidePanelHeight) / 2.0f;
                mFavButtonsDx = mFavButtonsBeginX;
                mFavButtonsDy = mFavButtonsBeginY;
                mFavButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void onPanelSlide(View view, float slideOffset) {
        //cover 图片动画
        float coverTranslationX = slideOffset * mProgressBarLayoutDx - mProgressBarLayoutBeginX;
        float coverTranslationY = slideOffset * mProgressBarLayoutDy - mProgressBarLayoutBeginY;
        LogUtils.i(TAG, "translationX = " + coverTranslationX + "   slideOffset = " + slideOffset + "  " + " translationY = " + coverTranslationY);
        ViewCompat.setTranslationX(mProgressBarLayout, coverTranslationX);
        ViewCompat.setTranslationY(mProgressBarLayout, coverTranslationY);
        float coverScale = slideOffset * (1.0f - mProgressBarLayoutScaleDes)  + mProgressBarLayoutScaleDes;
        ViewCompat.setScaleX(mProgressBarLayout, coverScale);
        ViewCompat.setScaleY(mProgressBarLayout, coverScale);

        //文字text view动画
        ViewCompat.setAlpha(mSongNameView, slideOffset);
        ViewCompat.setAlpha(mChannelNameView, slideOffset);


        float playButtonTranslationX = BUTTON_TRANSLATION_DX - slideOffset * BUTTON_TRANSLATION_DX;
        ViewCompat.setTranslationX(mPlayButton, playButtonTranslationX);
        float preButtonTranslationX = BUTTON_TRANSLATION_DX * 2.0f - slideOffset * BUTTON_TRANSLATION_DX * 2.0f;
        ViewCompat.setTranslationX(mPreButton, preButtonTranslationX);

        //Control button 动画
        float controlButtonsTranslationX = mControlButtonsBeginX - slideOffset * mControlButtonsDx;
        float controlButtonsTranslationY = slideOffset * mControlButtonsDy - mControlButtonsBeginY;
        ViewCompat.setTranslationX(mControlButtons, controlButtonsTranslationX);
        ViewCompat.setTranslationY(mControlButtons, controlButtonsTranslationY);
        float controlButtonScale = slideOffset * (1.0f - mControlButtonsScaleDes)  + mControlButtonsScaleDes;
        ViewCompat.setScaleX(mControlButtons, controlButtonScale);
        ViewCompat.setScaleY(mControlButtons, controlButtonScale);

        //收藏按钮动画
        float favButtonTranslationX = slideOffset * mFavButtonsDx - mFavButtonsBeginX;
        float favButtonTranslationY = slideOffset * mFavButtonsDy - mFavButtonsBeginY;
        ViewCompat.setTranslationX(mFavButton, favButtonTranslationX);
        ViewCompat.setTranslationY(mFavButton, favButtonTranslationY);
        float favButtonScale = slideOffset * (1.0f - mFavButtonsScaleDes)  + mFavButtonsScaleDes;
        ViewCompat.setScaleX(mFavButton, favButtonScale);
        ViewCompat.setScaleY(mFavButton, favButtonScale);
        if (slideOffset == 0 || slideOffset == 1) {
            addOnTouchSpringAnimation(favButtonScale, mFavButton);
        }
    }

    private void addListener() {
        mPreButton.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        //mFavButton.setOnClickListener(this);
    }




    private void setChannelName(ChannelsPage.Channel channel) {
        if (channel == null) {
            mChannelNameView.setVisibility(View.INVISIBLE);
        } else {
            mChannelNameView.setVisibility(View.VISIBLE);
            mChannelNameView.setText(channel.channelName);
        }
    }

    private void setSongName(PlayerPage.DouBanSong song) {
        if (song == null) {
            mSongNameView.setVisibility(View.INVISIBLE);
        } else {
            mSongNameView.setVisibility(View.VISIBLE);

            StringBuffer sb = new StringBuffer();
            if (song.title != null) {
                sb.append(song.title);
            } else {
                sb.append("Unknown");
            }
            if (song.singers != null && song.singers.size() > 0) {
                sb.append("--");
                for (PlayerPage.DouBanSongSinger singer : song.singers) {
                    sb.append(singer.name);
                    sb.append("&");
                }
            }

            String name = sb.toString();
            if (sb.toString().endsWith("&")) {
                name = name.substring(0, name.length() - 1);
            }

            mSongNameView.setText(name);
        }
    }


    private void refreshViews(FMPlayerService.PlayState state, FMPlayerService.StateFrom stateFrom) {
        if (state == FMPlayerService.PlayState.PLAYING) {
            mPlayIcon.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_48dp);
            mCoverBgMask.startAnimation(mCoverBgMaskAnimation);
            if (!mCoverAnimIsRun) {
                mCover.startAnimation(mCoverRotateAnimation);
            }

            if (stateFrom == FMPlayerService.StateFrom.RESUME) {
                mLyricView.resume();
            }
        } else {
            mPlayIcon.setBackgroundResource(R.drawable.ic_play_circle_outline_black_48dp);
            mCoverBgMaskAnimation.cancel();
            mCoverBgMask.clearAnimation();

            mCover.clearAnimation();
            mCoverRotateAnimation.cancel();
            if(state == FMPlayerService.PlayState.NONE){
                mProgressBar.setProgress(0);
            }

            mLyricView.stop();
        }
    }



    private void runButtonAnim(final View v) {
        AnimUtils.addClickAnimation(mFragment.getActivity(), v, R.anim.click_anim, new AnimCallbackImp() {
            @Override
            public void animationEnd(View view) {
                if (view == mPreButton) {
                    mFragment.getPlayerService().playPriority();
                } else if (view == mPlayButton) {
                    mFragment.getPlayerService().playOrPause();
                } else if (view == mNextButton) {
                    mFragment.getPlayerService().playNext();
                } else if (view == mFavButton) {
                    //TODO
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        runButtonAnim(v);
    }

    public void release() {
        mCoverBgMaskAnimation.cancel();
        mCoverBgMask.clearAnimation();
        for (Spring spring : springMap) {
            spring.removeAllListeners();
        }
    }

    /**
     * 加载歌曲专辑图片
     */
    public void loadCover() {
        PlayerPage.DouBanSong song = mFragment.getPlayerService().getCurrentSong();
        if (song == null) {
            return;
        }
        Picasso.with(mFragment.getActivity()).load(song.picture).resize(mCover.getWidth(), mCover.getHeight())
                .centerCrop().config(Bitmap.Config.ARGB_8888).placeholder(R.drawable.example)
                .into(mCover);
    }


    @Override
    public void refreshControllerView(ChannelsPage.Channel channel, PlayerPage.DouBanSong song, FMPlayerService.PlayState state, FMPlayerService.StateFrom stateFrom) {
        refreshViews(state, stateFrom);
        setChannelName(channel);
        setSongName(song);
    }

    @Override
    public void sendProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    @Override
    public void renderLyric(PlayerPage.DouBanSong song) {

    }

    private void addOnTouchSpringAnimation(float scale, View... v) {
        for (View view : v) {
            Spring spring = mSpringSystem.createSpring();
            springMap.add(spring);
            FaceBookRebound.addSpringAnimation(view, spring, scale, new AnimCallBack());
        }
    }

    class AnimCallBack extends AnimCallbackImp {
        @Override
        public void animationEnd(View v) {

        }
    }

}
