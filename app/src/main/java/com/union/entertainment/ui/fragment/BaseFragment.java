package com.union.entertainment.ui.fragment;

import android.net.Uri;
import android.support.v4.app.Fragment;

/**
 * Created by zhouxiaming on 16/3/6.
 */
public class BaseFragment extends Fragment {

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
