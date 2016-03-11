package com.union.entertainment;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;

import com.union.entertainment.ui.activity.NavigationActivity;
import com.union.entertainment.ui.anim.AnimCallback;
import com.union.entertainment.ui.anim.AnimUtils;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback{
    private String TAG = "MainActivity";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;
    private View mSkipButton, mPlayButton;
    private DisplayMetrics mDisplayMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDisplayMetrics = new DisplayMetrics();
        ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(mDisplayMetrics);
        initView();
        initSurfaceHolder();
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
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.guide_video);
        mMediaPlayer = new MediaPlayer();
        try {
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
        AnimUtils.addClickAnimation(this, v, R.anim.click_anim, new AnimCallback() {
            @Override
            public void handleClickEvent() {
                if (v == mSkipButton) {
                    skip();
                } else {
                    playVideo();
                }

            }
        });
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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
        mMediaPlayer = null;
        mSurfaceView = null;
    }
}
