package com.union.entertainment.ui.fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouxiaming on 2016/3/4.
 */
public class FragmentFactory {

	public static final int FRAGMENT_LOCAL_PIC = 0;
	public static final int FRAGMENT_NETWORK_PIC = 1;
	public static final int FRAGMENT_LOCAL_MUSIC = 2;
	public static final int FRAGMENT_SPOTIFY = 3;
	public static final int FRAGMENT_QQ = 4;

	public static final int FRAGMENT_TEST = -1;


	private static Map<Integer, Fragment> mFragmentCache = new HashMap<Integer, Fragment>();

	public static Fragment createFragment(int position){
		Fragment fragment = mFragmentCache.get(position);
		if (fragment == null) {
			switch (position) {
				case FRAGMENT_LOCAL_PIC:
					fragment = GalleryFragment.newInstance(null, null);
					break;
				default:
					fragment = TestFragment.newInstance(null, null);
					break;
			}
			mFragmentCache.put(position,fragment);
		}
		return fragment;
	}

	public static void clear(){
		mFragmentCache.clear();
	}
}
