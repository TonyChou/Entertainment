package com.union.fmdouban.ui.view;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.union.commonlib.ui.anim.AnimCallbackImp;
import com.union.commonlib.ui.anim.AnimUtils;
import com.union.commonlib.ui.view.CircleProgressBar;
import com.union.commonlib.ui.view.TintUtils;
import com.union.fmdouban.R;
import com.union.fmdouban.play.Player;
import com.union.fmdouban.play.PlayerListener;
import com.union.fmdouban.ui.fragment.ChannelFragment;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhouxiaming on 2016/3/14.
 */

public class PlayerController implements View.OnClickListener, PlayerListener{
    private final int PROGRESS_UPDATE = 0x000011; //进度条更新
    private View mControlPanelView;
    private static PlayerController instance;
    private View mPreButton, mPlayButton, mNextButton;
    private View mCoverBgMask;
    private AppCompatTextView mPreIcon, mPlayIcon, mNextIcon;
    private ChannelFragment mFragment;
    Animation mCoverBgMaskAnimation;
    private PlayState mCurrentPLayState = PlayState.NONE;
    private CircleProgressBar mProgressBar;
    private Timer mProgressTimer;
    private boolean isTimerStarted = false;
    private String test_url = "http://mr7.doubanio.com/e7e1ad76d9983cb12ba5285712af6b8a/0/fm/song/p180_128k.mp3";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case PROGRESS_UPDATE:
                    int progress = msg.arg1;
                    mProgressBar.setProgress(progress);
                    break;
                default:
                    break;
            }
        }
    };


    enum PlayState {
        NONE,
        PLAYING,
        PAUSE,
        STOP,
    }

    private PlayerController() {

    }

    public static synchronized PlayerController getInstance() {
        if (instance == null) {
            instance = new PlayerController();
        }

        return instance;
    }

    public void init(ChannelFragment flagment, View panelView) {
        this.mControlPanelView = panelView;
        this.mFragment = flagment;
        mPreButton = mControlPanelView.findViewById(R.id.pre);
        mPreIcon = (AppCompatTextView)mPreButton.findViewById(R.id.button_icon);
        mPreIcon.setBackgroundResource(R.drawable.ic_play_arrow_black_48dp);
        mPreIcon.setRotation(180f);
        mPlayButton = mControlPanelView.findViewById(R.id.play);
        mPlayIcon = (AppCompatTextView)mPlayButton.findViewById(R.id.button_icon);
        mNextButton = mControlPanelView.findViewById(R.id.next);
        mNextIcon = (AppCompatTextView)mNextButton.findViewById(R.id.button_icon);
        mNextIcon.setBackgroundResource(R.drawable.ic_play_arrow_black_48dp);

        mCoverBgMask = mControlPanelView.findViewById(R.id.cover_bg_mask);
        mProgressBar = (CircleProgressBar) mControlPanelView.findViewById(R.id.music_progress);
        mProgressBar.setProgress(0);
        mCoverBgMaskAnimation = AnimationUtils.loadAnimation(this.mFragment.getActivity(), R.anim.record_anim);
        TintUtils.setBackgroundTint(this.mFragment.getActivity(), R.color.white, mPreIcon, mPlayIcon, mNextIcon);
        addListener();
    }

    private void addListener() {
        mPreButton.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
    }

    private void startProgressTimer() {
        if (isTimerStarted) {
            return;
        }
        isTimerStarted = true;
        mProgressTimer = new Timer();
        mProgressTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Player player = mFragment.getPlayer();
                if (player == null || !player.isPlaying()) {
                    SystemClock.sleep(1000);
                    return;
                }
                int position = player.getCurrentPosition();
                int duration = player.getDuration();
                int progress = (int)((double) position/duration * 1000);
                Message msg = handler.obtainMessage(PROGRESS_UPDATE);
                msg.arg1 = progress;
                msg.sendToTarget();
            }
        }, 0, 300);
    }




    private void playOrPause(String url) {
        Player player = mFragment.getPlayer();
        if (player == null) {
            return;
        }

        if (url == null) {
            if(player.isPlaying()) {
                player.pause();
                mCurrentPLayState = PlayState.PAUSE;
            } else if (mCurrentPLayState == PlayState.PAUSE) {
                player.resume();
                mCurrentPLayState = PlayState.PLAYING;
            } else {
                player.play(test_url);
                mCurrentPLayState = PlayState.PLAYING;
            }
        } else {
            player.play(url);
            mCurrentPLayState = PlayState.PLAYING;
        }

        refreshPlayButtonIcon();
    }


    private void playNext() {
        //TODO

    }

    private void playPre() {
        //TODO
    }

    private void refreshPlayButtonIcon() {
        Log.i("veve", "  refreshPlayButtonIcon   ");
        if (mCurrentPLayState == PlayState.PLAYING) {
            mPlayIcon.setBackgroundResource(R.drawable.ic_pause_circle_outline_black_48dp);
            mCoverBgMask.startAnimation(mCoverBgMaskAnimation);
        } else {
            mPlayIcon.setBackgroundResource(R.drawable.ic_play_circle_outline_black_48dp);
            mCoverBgMaskAnimation.cancel();
            mCoverBgMask.clearAnimation();
        }
    }

    private void runButtonAnim(final View v) {
        AnimUtils.addClickAnimation(mFragment.getActivity(), v, R.anim.click_anim, new AnimCallbackImp() {
            @Override
            public void animationEnd(View view) {
                if (view == mPreButton) {
                    playPre();
                } else if (view == mPlayButton) {
                    playOrPause(null);
                } else if (view == mNextButton) {
                    playNext();
                }

            }
        });
    }

    @Override
    public void onError(String errorMsg) {
        mCurrentPLayState = PlayState.STOP;
        refreshPlayButtonIcon();
    }

    @Override
    public void onFinish() {
        playNext();
    }

    @Override
    public void onPrepared() {
        mCurrentPLayState = PlayState.PLAYING;
        refreshPlayButtonIcon();
        startProgressTimer();
    }


    @Override
    public void onClick(View v) {
        runButtonAnim(v);
    }

    public void release() {
        mCoverBgMaskAnimation.cancel();
        mCoverBgMask.clearAnimation();
        if (mProgressTimer != null) {
            mProgressTimer.cancel();
            mProgressTimer = null;
        }
        handler = null;
    }
}
