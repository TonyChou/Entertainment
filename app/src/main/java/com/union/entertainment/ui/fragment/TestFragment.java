package com.union.entertainment.ui.fragment;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.entertainment.R;
import com.union.entertainment.ui.view.ColorPickerView;
import com.union.commonlib.ui.anim.FaceBookRebound;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 2016/3/9.
 */
public class TestFragment extends BaseFragment implements View.OnClickListener {
    private String TAG = this.getClass().getSimpleName();
    private View editBtn, recBtn, cancelBtn, sendBtn, lightLayout;
    private ColorPickerView colorPickerView;
    private View colorPickerReviewBg;
    private ImageView colorPickerReview;
    private Bitmap colorPickerBitmap;
    private ShapeDrawable shapeDrawable;
    private List<Spring> springMap = new ArrayList<Spring>();
    private final BaseSpringSystem mSpringSystem = SpringSystem.create();
    Animation animation, rootViewAnimation;
    private View mRootView;



    public TestFragment() {
        // Required empty public constructor
    }

    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootViewAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.send_left_to_right_anim);
        rootViewAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i("veve", " onAnimationStart ");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i("veve", " onAnimationEnd ");
               // mRootView.startAnimation(AnimationUtils.loadAnimation(TestFragment.this.getActivity(), R.anim.send_left_to_right_anim));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.record_anim);

        Log.i(TAG, "onCreate ");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_test, container, false);
        initView(mRootView);
        //Find the +1 button
        Log.i(TAG, "onCreateView ");
        return mRootView;
    }

    private void initView(View view) {
        colorPickerView = (ColorPickerView) view.findViewById(R.id.color_picker);
        colorPickerReviewBg = view.findViewById(R.id.color_preview_bg);
        colorPickerReview = (ImageView) view.findViewById(R.id.color_preview);
        colorPickerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                refreshColorPreviewView(v, event);
                return false;
            }
        });

        editBtn = view.findViewById(R.id.edit);
        recBtn = view.findViewById(R.id.rec);
        cancelBtn = view.findViewById(R.id.cancel);
        sendBtn = view.findViewById(R.id.send);
        editBtn.setOnClickListener(this);
        recBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        addOnTouchSpringAnimation(editBtn, recBtn, cancelBtn, sendBtn);
        lightLayout = view.findViewById(R.id.light_layout);
    }

    /**
     * 刷新取色值View
     *
     * @param event
     */
    private void refreshColorPreviewView(View pickerView, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (colorPickerBitmap == null) {
                    colorPickerBitmap = ((BitmapDrawable) colorPickerView.getDrawable()).getBitmap();
                }
                updateColorPreview(pickerView, event);
                colorPickerReviewBg.setVisibility(View.VISIBLE);
                Log.i(TAG, "================== ACTION_DOWN ====================");
                break;
            case MotionEvent.ACTION_MOVE:
                updateColorPreview(pickerView, event);
                Log.i(TAG, "================== ACTION_MOVE ====================");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "================== ACTION_UP ====================");
                colorPickerReviewBg.setVisibility(View.INVISIBLE);
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "================== ACTION_CANCEL ====================");
                colorPickerReviewBg.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void updateColorPreview(View pickerView, MotionEvent event) {
        int[] location = new int[2];
        colorPickerView.getLocationOnScreen(location);
        //colorPickerView.getHitRect(rect);
        int eX = (int)event.getX();
        int eY = (int)event.getY();
        //计算有效touch移动值
        if (eY < 0 || eY >= colorPickerView.getMeasuredHeight() || colorPickerBitmap == null) {
            return;
        }
        Log.i(TAG, "== " + location[0]+ "  " + location[1] + "    " + colorPickerView.getMeasuredHeight() + "   " + eY);
        int pixel = colorPickerBitmap.getPixel(eX, eY);
        shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(pixel);
        colorPickerReview.setBackgroundDrawable(shapeDrawable);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)colorPickerReviewBg.getLayoutParams();
        int colorPickerReviewBgWidth = colorPickerReviewBg.getMeasuredWidth();
        int pickerViewWidth = pickerView.getMeasuredWidth();
        if (eX + colorPickerReviewBgWidth < pickerViewWidth) {
            layoutParams.leftMargin = eX;
        }
        colorPickerReviewBg.setLayoutParams(layoutParams);
    }



    @Override
    public void onResume() {
        super.onResume();

    }



    private void addOnTouchSpringAnimation(View... v) {
        for (View view: v) {
            Spring spring = mSpringSystem.createSpring();
            springMap.add(spring);
            FaceBookRebound.addSpringAnimation(view, spring, null);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.edit:
            case R.id.rec:
            case R.id.cancel:
            case R.id.send:
                Log.i(TAG, " click button ======= ");
                lightLayout.startAnimation(animation);
                mRootView.startAnimation(rootViewAnimation);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shapeDrawable != null) {
            shapeDrawable = null;
        }

        if (colorPickerBitmap != null) {
            colorPickerBitmap.recycle();
            colorPickerBitmap = null;
        }
        colorPickerView = null;
        for (Spring spring : springMap) {
            spring.removeAllListeners();
        }
        Log.i(TAG, "onDestroy ");
    }

}
