package com.union.commonlib.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.union.commonlib.R;

/**
 * Created by zhouxiaming on 2016/3/18.
 */

public class CircleProgressBar extends LinearLayout {
    private String TAG = this.getClass().getSimpleName();
    private int mXCenterPosition = 0;
    private int mYCenterPosition = 0;
    private Paint mProgressBarPaint;
    private int mProgressBarPaintWidth = 6;
    private float mProgressBarPaintRadius = 0;
    private RectF oval;
    private Context mContext;
    private boolean isInit = false;
    private int MAX_PROGRESS = 1000;
    private int mCurrentProgress = 90;

    public CircleProgressBar(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }



    public void setProgress(int progress) {
        this.mCurrentProgress = progress;
        this.invalidate();
    }


    public void setMaxProgress(int progress){
        MAX_PROGRESS = progress;
    }

    private void init() {
        setBackgroundResource(R.color.transparent); //必须要设置一个背景颜色，不然显示不出绘制的界面
        mProgressBarPaint = new Paint();
        mProgressBarPaint.setColor(getResources().getColor(R.color.light_blue));
        mProgressBarPaint.setAntiAlias(true);
        mProgressBarPaint.setStyle(Paint.Style.STROKE);
        mProgressBarPaint.setStrokeWidth(mProgressBarPaintWidth);
        //mProgressBarPaint.setStrokeCap(Paint.Cap.ROUND);
        oval = new RectF();
        isInit = true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInit) {
            init();
        }
        oval.set(mXCenterPosition - mProgressBarPaintRadius, mYCenterPosition - mProgressBarPaintRadius,
                mXCenterPosition + mProgressBarPaintRadius, mYCenterPosition + mProgressBarPaintRadius);
        canvas.drawArc(oval, -90, ((float) mCurrentProgress / MAX_PROGRESS) * 360, false, mProgressBarPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mXCenterPosition = this.getMeasuredWidth() / 2;
        this.mYCenterPosition = this.getMeasuredHeight() / 2;
        int mMin = Math.min(mXCenterPosition, mYCenterPosition);
        mProgressBarPaintRadius = (float)mMin - 4;
        int padding = getPaddingLeft();
        mProgressBarPaintWidth = padding;
        mProgressBarPaintRadius = (float)(mProgressBarPaintRadius - (mProgressBarPaintWidth/2));
    }
}
