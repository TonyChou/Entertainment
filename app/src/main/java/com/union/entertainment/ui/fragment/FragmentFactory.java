package com.union.entertainment.ui.fragment;

import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.fmdouban.ui.fragment.DouBanMainFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouxiaming on 2016/3/4.
 */
public class FragmentFactory {

	public static final int FRAGMENT_LOCAL_PIC = 0;
	public static final int FRAGMENT_NETWORK_PIC = 1;

	public static final int FRAGMENT_DOUBAN_FM = 2;

	public static final int FRAGMENT_LOCAL_MUSIC = 3;
	public static final int FRAGMENT_SPOTIFY = 4;
	public static final int FRAGMENT_QQ = 5;

	public static final int FRAGMENT_TEST = -1;


	private static Map<Integer, BaseFragment> mFragmentCache = new HashMap<Integer, BaseFragment>();

	public static BaseFragment createFragment(int position){
		BaseFragment fragment = mFragmentCache.get(position);
		if (fragment == null) {
			switch (position) {
				case FRAGMENT_LOCAL_PIC:
					fragment = GalleryFragment.newInstance(null, null);
					break;
				case FRAGMENT_DOUBAN_FM:
					//fragment = FMPlayerFragment.newInstance();
					fragment = DouBanMainFragment.newInstance();
					break;
				case FRAGMENT_SPOTIFY:
					fragment = TestFragment.newInstance(null, null);
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
