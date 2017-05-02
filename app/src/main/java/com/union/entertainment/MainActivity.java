package com.union.entertainment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.union.entertainment.ui.activity.NavigationActivity;
import com.union.commonlib.ui.anim.AnimCallbackImp;
import com.union.commonlib.ui.anim.AnimUtils;

import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener, SurfaceHolder.Callback{
    private String TAG = "MainActivity";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;
    private View mSkipButton, mPlayButton;
    private DisplayMetrics mDisplayMetrics;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDisplayMetrics = new DisplayMetrics();
        ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(mDisplayMetrics);
        initView();
        initSurfaceHolder();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayButton.setVisibility(View.INVISIBLE);
        mSkipButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void initView() {
        mSurfaceView = (SurfaceView)findViewById(R.id.guide_video);
        mSkipButton = findViewById(R.id.skip);
        mSkipButton.setOnClickListener(this);

        mPlayButton = findViewById(R.id.replay);
        mPlayButton.setOnClickListener(this);
    }

    private void initSurfaceHolder() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    private void playVideo() {
        try {
            mMediaPlayer = new MediaPlayer();
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.guide_video);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(this, uri);
            mMediaPlayer.setOnCompletionListener(new VideoPlayListener());
            //mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            mSurfaceHolder.setFixedSize(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mSkipButton || v== mPlayButton) {
            runButtonAnim(v);
        }
    }

    private void runButtonAnim(final View v) {
        AnimUtils.addClickAnimation(this, v, R.anim.click_anim, new AnimCallbackImp() {
            @Override
            public void animationEnd(View view) {
                if (view == mSkipButton) {
                    skip();
                } else if (view == mPlayButton){
                    //playVideo();
                    replayVideo();
                    showButton(mPlayButton, false);
                }

            }
        });
    }

    private void replayVideo() {
        mMediaPlayer.start();
    }

    private void skip() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated");
        playVideo();
        showButton(mSkipButton, true);
        showButton(mPlayButton, false);
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            showButton(mPlayButton, true);
        } else {
            mPlayButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    class VideoPlayListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.i(TAG, "VideoPlayListener onCompletion");
            showButton(mPlayButton, true);
        }
    }

    /**
     * 显示Replay button
     */
    private void showButton(View v, boolean show) {
        int animId = -1;
        if (show) {
            animId = R.anim.btn_view_in;
        } else {
            animId = R.anim.btn_view_out;
        }
        AnimUtils.showOrHideButtonWithAnimation(this, show, v, animId, null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();

        }

        mMediaPlayer = null;
        mSurfaceView = null;
    }
}
