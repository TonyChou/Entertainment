package com.union.entertainment.ui.fragment;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.union.entertainment.R;
import com.union.entertainment.module.picture.Photo;
import com.union.entertainment.module.picture.PhotosController;
import com.union.entertainment.module.picture.PhotosQuery;
import com.union.entertainment.ui.adapter.PhotoGridAdapter;

import java.util.List;

/**
 * Created by zhouxiaming on 2016/3/4.
 */
public class GalleryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor>{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    PhotoGridAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public GalleryFragment() {
        // Required empty public constructor
    }

    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_gallery, container, false);
        initView(contentView);
        return contentView;
    }


    private void initView(View contentView) {
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler);
        refreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setProgressBackgroundColor(R.color.refresh_bg);

    }

    @Override
    public void onStart() {
        super.onStart();
        initGridView();
    }

    private View initGridView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this.getActivity(), 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        adapter = new PhotoGridAdapter(this.getActivity());
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(PhotosQuery._TOKEN, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(PhotosQuery._TOKEN, null, this);
    }

    private void refreshPhotos(Cursor cursor) {
        List<Photo> photoList = PhotosController.loadPhotoFromCursor(cursor);
        adapter.setData(photoList);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        },3000);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader = null;
        if(id == PhotosQuery._TOKEN) {
            loader = new CursorLoader(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PhotosQuery.PROJECTION, null, null, null);
        }

        refreshLayout.setRefreshing(true);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == PhotosQuery._TOKEN) {
            //TODO
            refreshPhotos(data);
        } else {
            data.close();
        }
        refreshLayout.setRefreshing(false);
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(loader.getId() == PhotosQuery._TOKEN) {
            //TODO
        }
    }
}
