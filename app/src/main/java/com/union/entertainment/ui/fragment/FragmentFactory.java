package com.union.entertainment.ui.fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouxiaming on 2016/3/4.
 */
public class FragmentFactory {

	public static final int FRAGEMENT_LOCAL_PIC = 0;
	public static final int FRAGEMENT_NETWORK_PIC = 0;
	public static final int FRAGEMENT_LOCAL_MUSIC = 0;
	public static final int FRAGEMENT_SPOTIFY = 0;
	public static final int FRAGEMENT_QQ = 0;


	private static Map<Integer, Fragment> mFragmentCache = new HashMap<Integer, Fragment>();

	public static Fragment createFragment(int position){
		Fragment fragment = mFragmentCache.get(position);
		if (fragment == null) {
			switch (position) {
				case FRAGEMENT_LOCAL_PIC:
					fragment = GalleryFragment.newInstance(null, null);
					break;
				default:
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
