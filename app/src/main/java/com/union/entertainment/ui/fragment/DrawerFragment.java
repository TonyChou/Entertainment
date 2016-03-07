package com.union.entertainment.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.union.entertainment.R;


public class DrawerFragment extends Fragment {

    private OnDrawerItemSelectedListener mListener;

    private ListView mDrawerListView;

    private int mCurrentSelectedPosition = 0;

    public DrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectItem(mCurrentSelectedPosition);
    }

    public void setOnDrawerItemSelectedListener(OnDrawerItemSelectedListener listener) {
        mListener = listener;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(R.layout.fragment_drawer, container, false);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1);
        Object[] array = getActivity().getResources().getStringArray(R.array.drawer_items);
        for (Object obj : array) {
            adapter.add(obj);
        }


        mDrawerListView.setDividerHeight(2);
        mDrawerListView.setAdapter(adapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mDrawerListView;
    }

    public void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mListener != null) {
            mListener.onDrawerItemSelected(position);
        }
    }


    public interface OnDrawerItemSelectedListener {
        void onDrawerItemSelected(int position);
    }
}
