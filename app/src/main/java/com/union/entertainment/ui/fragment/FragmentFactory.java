package com.union.entertainment.ui.fragment;

import com.union.commonlib.ui.fragment.BaseFragment;
import com.union.fmdouban.ui.fragment.DouBanMainFragment;
import com.union.fmdouban.ui.fragment.FMPlayerFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouxiaming on 2016/3/4.
 */
public class FragmentFactory {

	public static final int FRAGMENT_LOCAL_PIC = 0x01;
	public static final int FRAGMENT_NETWORK_PIC = 0x10;

	public static final int FRAGMENT_DOUBAN_FM = 0x20;
	public static final int FRAGMeNT_DOUBAN_PLAYER = 0x21;

	public static final int FRAGMENT_LOCAL_MUSIC = 0x30;
	public static final int FRAGMENT_SPOTIFY = 0x40;
	public static final int FRAGMENT_QQ = 0x50;

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
				case FRAGMeNT_DOUBAN_PLAYER:
					fragment = FMPlayerFragment.newInstance();
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
