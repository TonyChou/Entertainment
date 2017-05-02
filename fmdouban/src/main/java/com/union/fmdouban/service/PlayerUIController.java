package com.union.fmdouban.service;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
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
import com.union.commonlib.ui.view.slidinguppanel.SlidingUpPanelLayout;
import com.union.commonlib.utils.LogUtils;
import com.union.fmdouban.R;

import com.union.fmdouban.play.PlayerControllerListener;
import com.union.fmdouban.ui.fragment.FMPlayerFragment;
import com.union.fmdouban.ui.lyric.widget.LyricView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by zhouxiaming on 2016/3/14.
 */

public class PlayerUIController implements View.OnClickListener, PlayerControllerListener {
    private String TAG = "PlayerUIController";


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
    private int playerButtonSize, playerButtonMarginRight, favButtonMarginRight;
    private int playerButtonMargin;
    private TextView mErrorPanel;
    private SlidingUpPanelLayout mSlideLayout;
    private View mBottomTitleView;
    private TextView mBottonChannelNameView, mBottomSongNameView;
    public int VIEW_LOAD_FLAG = 0;
    public int VIEW_LOAD_FINISHED = 4;
    private Display display;
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
        this.display = mActivity.getWindowManager().getDefaultDisplay();
        this.VIEW_LOAD_FLAG = 0;
        mSlideLayout = (SlidingUpPanelLayout) mActivity.getWindow().getDecorView().findViewById(R.id.slide_layout_panel);
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
        mFavIcon = (AppCompatTextView) mControlPanelView.findViewById(R.id.fav_button);
        mFavIcon.setBackgroundResource(R.drawable.ic_favorite_black_48dp);
        mErrorPanel = (TextView) mControlPanelView.findViewById(R.id.error_panel);

        mBottomTitleView = mControlPanelView.findViewById(R.id.bottom_title_view);
        mBottonChannelNameView = (TextView) mBottomTitleView.findViewById(R.id.bottom_channel_name_view);
        mBottomSongNameView = (TextView) mBottomTitleView.findViewById(R.id.bottom_song_name_view);
        mCover.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                loadCover();
                mCover.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                VIEW_LOAD_FLAG++;
            }
        });

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
        TintUtils.setBackgroundTint(this.mFragment.getActivity(), R.color.white, mPreIcon, mPlayIcon, mNextIcon);
        TintUtils.setBackgroundTint(mActivity, R.color.fav_button_unselected_color,  mFavIcon);
        addListener();

        mCoverAnimIsRun = false;
        measureComponentSize();
    }


    public void initSlidePanelState() {
        if (mSlideLayout == null) {
            return;
        }

        SlidingUpPanelLayout.PanelState mInitSate = mSlideLayout.getPanelState();
        if (mInitSate == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            onPanelSlide(mSlideLayout, 0.0f);
        } else if (mInitSate == SlidingUpPanelLayout.PanelState.EXPANDED) {
            onPanelSlide(mSlideLayout, 1.0f);
        }
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
        favButtonMarginRight = mActivity.getResources().getDimensionPixelSize(R.dimen.fab_margin);
        //控制按钮面板需要整体向右偏移的值
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
                VIEW_LOAD_FLAG++;
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
                VIEW_LOAD_FLAG++;
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
                mFavButtonsScaleDes = ((float) slidePanelHeight - (float)playerButtonMargin)/ (float) height;
                float dx = mControlButtonScaleWidth + favButtonMarginRight * mFavButtonsScaleDes / 2.0f;
                mFavButtonsScaleDes = mControlButtonsScaleDes; //保证按钮缩放之后大小一致
                mFavButtonsBeginX = x + ((float) width) / 2.0f- dx;
                mFavButtonsBeginY = y + ((float) height  - (float)slidePanelHeight) / 2.0f;
                mFavButtonsDx = mFavButtonsBeginX;
                mFavButtonsDy = mFavButtonsBeginY;
                mFavButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                VIEW_LOAD_FLAG++;
            }
        });
    }

    public void onPanelSlide(View view, float slideOffset) {
        //cover 图片动画
        float coverTranslationX = slideOffset * mProgressBarLayoutDx - mProgressBarLayoutBeginX;
        float coverTranslationY = slideOffset * mProgressBarLayoutDy - mProgressBarLayoutBeginY;
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
        ViewCompat.setAlpha(mPreButton, slideOffset);

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
        updateBottomTitleAndSongNamePosition(slideOffset);
    }

    private void updateBottomTitleAndSongNamePosition(float slideOffset) {
        int i = mActivity.getResources().getDimensionPixelSize(R.dimen.slide_panel_height) + 8;
        int j = (i - this.mBottomTitleView.getHeight()) / 2;
        int topTabBarHeight = mActivity.getResources().getDimensionPixelSize(R.dimen.tab_title_height);
        float dx = i * (1.0F - slideOffset);
        float dy = - (getScreenHeight() - topTabBarHeight - getStatusBarHeight(mActivity) - this.mBottomTitleView.getHeight() - j) * (1.0F - slideOffset);
        Point rect = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(rect);
        }
        ViewCompat.setTranslationX(mBottomTitleView, dx);
        ViewCompat.setTranslationY(mBottomTitleView, dy);
        ViewCompat.setAlpha(mBottomTitleView, 1.0f - slideOffset); //展开的时候不显示底部
    }
    public int getScreenHeight()  {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();

        ((WindowManager)mActivity.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics.heightPixels;
    }
    public static int getStatusBarHeight(Context paramContext)
    {
        int i = paramContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int j = 0;
        if (i > 0) {
            j = paramContext.getResources().getDimensionPixelSize(i);
        }
        return j;
    }

    private void addListener() {
//        mPreButton.setOnClickListener(this);
//        mPlayButton.setOnClickListener(this);
//        mNextButton.setOnClickListener(this);
//        mFavButton.setOnClickListener(this);
        addOnTouchSpringAnimation(1.0f, mFavButton);
        addOnTouchSpringAnimation(1.0f, mPreButton);
        addOnTouchSpringAnimation(1.0f, mPlayButton);
        addOnTouchSpringAnimation(1.0f, mNextButton);
    }




    private void setChannelName(ChannelsPage.Channel channel) {
        if (channel == null) {
            mChannelNameView.setVisibility(View.INVISIBLE);
            mBottonChannelNameView.setVisibility(View.INVISIBLE);
        } else {
            mChannelNameView.setVisibility(View.VISIBLE);
            mBottonChannelNameView.setVisibility(View.VISIBLE);
            mChannelNameView.setText(channel.channelName);
            mBottonChannelNameView.setText(channel.channelName);
        }
    }

    private void setSongName(PlayerPage.DouBanSong song) {
        if (song == null) {
            mSongNameView.setVisibility(View.INVISIBLE);
            mBottomSongNameView.setVisibility(View.INVISIBLE);
        } else {
            mSongNameView.setVisibility(View.VISIBLE);
            mBottomSongNameView.setVisibility(View.VISIBLE);
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
            mBottomSongNameView.setText(name);
        }
    }


    private void refreshViews(FMPlayerController.PlayState state, FMPlayerController.StateFrom stateFrom) {
        if (state == FMPlayerController.PlayState.PLAYING) {
            mPlayIcon.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_48dp);
            mCoverBgMask.startAnimation(mCoverBgMaskAnimation);
            if (!mCoverAnimIsRun) {
                mCover.startAnimation(mCoverRotateAnimation);
            }

            if (stateFrom == FMPlayerController.StateFrom.RESUME) {
                mLyricView.resume();
            }
        } else {
            mPlayIcon.setBackgroundResource(R.drawable.ic_play_circle_outline_black_48dp);
            mCoverBgMaskAnimation.cancel();
            mCoverBgMask.clearAnimation();

            mCover.clearAnimation();
            mCoverRotateAnimation.cancel();
            if(state == FMPlayerController.PlayState.NONE){
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
                    handleFavButtonClick();
                }

            }
        });
    }

    private void handleFavButtonClick() {
        Object tag = mFavButton.getTag();
        if (tag != null) {
            if (tag.equals("true")) {
                TintUtils.setBackgroundTint(mActivity, mFavIcon, R.color.fav_button_unselected_color);
                mFavButton.setTag("false");
            } else {
                TintUtils.setBackgroundTint(mActivity, mFavIcon, R.color.fav_button_selected_color);
                mFavButton.setTag("true");
            }
        } else {
            TintUtils.setBackgroundTint(mActivity, mFavIcon, R.color.fav_button_selected_color);
            mFavButton.setTag("true");
        }


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
        Picasso.with(mFragment.getActivity()).load(song.picture).resize(mCover.getMeasuredWidth(), mCover.getMeasuredHeight())
                .centerCrop().config(Bitmap.Config.ARGB_8888).placeholder(R.drawable.example)
                .into(mCover);
    }


    @Override
    public void refreshControllerView(ChannelsPage.Channel channel, PlayerPage.DouBanSong song, FMPlayerController.PlayState state, FMPlayerController.StateFrom stateFrom) {
        mErrorPanel.setVisibility(View.GONE);
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

    @Override
    public void showErrorInfo(String error) {
        mErrorPanel.setVisibility(View.VISIBLE);
        mErrorPanel.setText(error);
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
        public void animationEnd(View view) {
            if (view == mPreButton) {
                mFragment.getPlayerService().playPriority();
            } else if (view == mPlayButton) {
                mFragment.getPlayerService().playOrPause();
            } else if (view == mNextButton) {
                mFragment.getPlayerService().playNext();
            } else if (view == mFavButton) {
                handleFavButtonClick();
            }
        }
    }

}
