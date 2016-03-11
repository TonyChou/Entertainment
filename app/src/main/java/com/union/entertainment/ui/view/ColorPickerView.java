package com.union.entertainment.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by zhouxiaming on 2016/3/9.
 */

public class ColorPickerView extends ImageView {
    private String TAG = this.getClass().getSimpleName();
    private Bitmap bitmapBG; //background
    private Bitmap bitmapFG; //forceground
    private int width, height;
    private boolean hasDraw = false;

    public ColorPickerView(Context context) {
        super(context);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void createBitmap() {
        Canvas localCanvas = new Canvas();
        this.bitmapBG = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
        localCanvas.setBitmap(this.bitmapBG);
        Paint localPaint = new Paint();
        localPaint.setAntiAlias(true);
        int[] arrayOfInt = {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.BLACK, Color.WHITE};
        float[] arrayOfFloat = {0.0F, 0.1F, 0.17F, 0.34F, 0.51F, 0.68F, 0.85F, 1.0F};
        localPaint.setShader(new LinearGradient(0.0F, 0.0F, this.width, 0.0F, arrayOfInt, arrayOfFloat, Shader.TileMode.CLAMP));
        localCanvas.drawRect(0.0F, 0.0F, this.width, this.height, localPaint);
        this.bitmapFG = Bitmap.createBitmap(this.bitmapBG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw =======");
        super.onDraw(canvas);
        if (!this.hasDraw) {
            this.width = getMeasuredWidth();
            this.height = getMeasuredHeight();
            createBitmap();
            setImageBitmap(this.bitmapFG);
            this.hasDraw = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
