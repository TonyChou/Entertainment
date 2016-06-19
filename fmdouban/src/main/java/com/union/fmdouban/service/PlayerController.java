package com.union.fmdouban.service;

import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;
import com.union.commonlib.ui.anim.AnimCallbackImp;
import com.union.commonlib.ui.anim.AnimUtils;
import com.union.commonlib.ui.view.CircleProgressBar;
import com.union.commonlib.ui.view.TintUtils;
import com.union.fmdouban.R;
import com.union.fmdouban.api.bean.FMLyric;
import com.union.fmdouban.api.bean.FMRichChannel;
import com.union.fmdouban.api.bean.FMSinger;
import com.union.fmdouban.api.bean.FMSong;
import com.union.fmdouban.play.PlayerControllerListener;
import com.union.fmdouban.ui.fragment.FMPlayerFragment;
import com.union.fmdouban.ui.lyric.LyricUtils;
import com.union.fmdouban.ui.lyric.widget.LyricView;

/**
 * Created by zhouxiaming on 2016/3/14.
 */

public class PlayerController implements View.OnClickListener, PlayerControllerListener {
    private String TAG = PlayerController.class.getSimpleName();


    private View mControlPanelView;
    private static PlayerController instance;
    private View mPreButton, mPlayButton, mNextButton;
    private View mCoverBgMask;
    ImageView mCover;
    private AppCompatTextView mPreIcon, mPlayIcon, mNextIcon;
    private FMPlayerFragment mFragment;
    Animation mCoverBgMaskAnimation, mCoverRotateAnimation;
    LyricView mLyricView;
    private CircleProgressBar mProgressBar;

    private boolean mCoverAnimIsRun = false;
    private TextView mChannelNameView, mSongNameView;

    private PlayerController() {

    }

    public static synchronized PlayerController getInstance() {
        if (instance == null) {
            instance = new PlayerController();
        }

        return instance;
    }

    public void init(FMPlayerFragment flagment, View panelView) {
        this.mControlPanelView = panelView;
        this.mFragment = flagment;
        mLyricView = (LyricView) mControlPanelView.findViewById(R.id.lyric_panel);
        mCover = (ImageView) mControlPanelView.findViewById(R.id.cover);
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
        addListener();

        mCoverAnimIsRun = false;
    }

    private void addListener() {
        mPreButton.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
    }




    private void setChannelName(FMRichChannel channel) {
        if (channel == null) {
            mChannelNameView.setVisibility(View.INVISIBLE);
        } else {
            mChannelNameView.setVisibility(View.VISIBLE);
            mChannelNameView.setText(channel.getName());
        }
    }

    private void setSongName(FMSong song) {
        if (song== null) {
            mSongNameView.setVisibility(View.INVISIBLE);
        } else {
            mSongNameView.setVisibility(View.VISIBLE);

            StringBuffer sb = new StringBuffer();
            if (song.getTitle() != null) {
                sb.append(song.getTitle());
            } else {
                sb.append("Unknown");
            }
            if (song.getSingers() != null && song.getSingers().size() > 0) {
                sb.append("--");
                for (FMSinger singer : song.getSingers()) {
                    sb.append(singer.getName());
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
    }

    /**
     * 加载歌曲专辑图片
     */
    public void loadCover() {
        FMSong song = mFragment.getPlayerService().getCurrentSong();
        if (song == null) {
            return;
        }
        Picasso.with(mFragment.getActivity()).load(song.getPicture()).resize(mCover.getWidth(), mCover.getHeight())
                .centerCrop().config(Bitmap.Config.ARGB_8888).placeholder(R.drawable.example)
                .into(mCover);
    }

    /**
     * 渲染歌词
     */
    public void renderLyric(FMSong song) {
        //TODO
        FMLyric lyric = song.getLyric();
        if (lyric != null && lyric.getLyric() != null) {
            Log.i(TAG, lyric.toString());
            Log.i(TAG, "lyric: " + lyric.getLyric());
            mLyricView.reset();
            mLyricView.setLyric(LyricUtils.parseLyric(lyric.getLyric()));
            mLyricView.setLyricIndex(0);
            mLyricView.play();
        }
    }

    @Override
    public void refreshControllerView(FMRichChannel channel, FMSong song, FMPlayerService.PlayState state, FMPlayerService.StateFrom stateFrom) {
        refreshViews(state, stateFrom);
        setChannelName(channel);
        setSongName(song);
    }

    @Override
    public void sendProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

}
