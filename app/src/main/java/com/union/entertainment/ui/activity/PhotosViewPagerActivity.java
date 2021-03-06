/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.union.entertainment.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.squareup.picasso.Picasso;
import com.union.entertainment.R;
import com.union.entertainment.module.picture.HackyViewPager;
import com.union.entertainment.module.picture.Photo;
import com.union.commonlib.utils.UiUtils;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class PhotosViewPagerActivity extends Activity {

	private static final String ISLOCKED_ARG = "isLocked";
	
	private ViewPager mViewPager;
	private MenuItem menuLockItem;
	private List<Photo> photoList;
	private int position;
	//private static Queue<PhotoView> photoViews = new LinkedList<PhotoView>();
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_show_layout);

		Bundle bundle = getIntent().getExtras();
		photoList = (List<Photo>)bundle.getSerializable("photos");
		position = bundle.getInt("position");
		if (photoList == null || photoList.size() < 1) {
			finish();
			return;
		}
        mViewPager = (HackyViewPager) findViewById(R.id.pager);


		mViewPager.setAdapter(new SamplePagerAdapter(this, photoList));
		
		if (savedInstanceState != null) {
			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
			((HackyViewPager) mViewPager).setLocked(isLocked);
		}

		mViewPager.setCurrentItem(position);
	}

	static class SamplePagerAdapter extends PagerAdapter {
		private Context mContext;
		private List<Photo> photoList;
		private int[] screen;
		public SamplePagerAdapter(Activity context, List<Photo> photoList) {
			this.mContext = context;
			this.photoList = photoList;
			this.screen = UiUtils.getScreenWidthAndHeight(context);
		}

		@Override
		public int getCount() {
			return photoList.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = null;
//			if (!photoViews.isEmpty()) {
//				PhotoView cacheView = photoViews.poll();
//				if (cacheView.getParent() == null) {
//					photoView = cacheView;
//				}
//			} else {
//				photoView = new PhotoView(mContext);
//			}
			photoView = new PhotoView(mContext);

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			Photo photo = photoList.get(position);

			if (photo != null) {
				Bitmap bitmap = BitmapFactory.decodeFile(photo.getPath());
				photoView.setImageBitmap(bitmap);
				photoView.setTag(bitmap);
				//Picasso.with(mContext).load(new File(photo.getPath())).config(Bitmap.Config.ARGB_8888).into(photoView);
			}

			addScaleListener(photoView);

			return photoView;
		}

		private void addScaleListener(PhotoView photoView) {
			photoView.setOnScaleChangeListener(new PhotoViewAttacher.OnScaleChangeListener() {
				@Override
				public void onScaleChange(float v, float v1, float v2) {
				}
			});
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (object instanceof PhotoView) {
				((PhotoView)object).destroyDrawingCache();
				container.removeView((View) object);
				//photoViews.add((PhotoView) object);
				Bitmap bitmap = ((PhotoView)object).getVisibleRectangleBitmap();
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}

				Bitmap bm = (Bitmap) ((PhotoView)object).getTag();
				if (bm != null) {
					bm.recycle();
					bm = null;
				}
			}
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.viewpager_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        menuLockItem = menu.findItem(R.id.menu_lock);
//        toggleLockBtnTitle();
//        menuLockItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//			@Override
//			public boolean onMenuItemClick(MenuItem item) {
//				toggleViewPagerScrolling();
//				toggleLockBtnTitle();
//				return true;
//			}
//		});

        return super.onPrepareOptionsMenu(menu);
    }


    
    private void toggleViewPagerScrolling() {
    	if (isViewPagerActive()) {
    		((HackyViewPager) mViewPager).toggleLock();
    	}
    }
    
//    private void toggleLockBtnTitle() {
//    	boolean isLocked = false;
//    	if (isViewPagerActive()) {
//    		isLocked = ((HackyViewPager) mViewPager).isLocked();
//    	}
//    	String title = (isLocked) ? getString(R.string.menu_unlock) : getString(R.string.menu_lock);
//    	if (menuLockItem != null) {
//    		menuLockItem.setTitle(title);
//    	}
//    }

    private boolean isViewPagerActive() {
    	return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }
    
	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		if (isViewPagerActive()) {
			outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
    	}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		for (PhotoView photoView : photoViews) {
//			photoView.destroyDrawingCache();
//		}
//
//		photoViews.clear();
	}
}
