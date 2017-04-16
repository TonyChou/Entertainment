package com.union.entertainment.ui.fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.union.commonlib.data.LoaderToken;
import com.union.commonlib.ui.ActionBarPage;
import com.union.commonlib.ui.fragment.BaseFragment;
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
    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    private static final int REQUEST_CODE = 0x123123;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    PhotoGridAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_gallery;
    }


    private void loadPicture() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                getLoaderManager().restartLoader(LoaderToken.PhotosQuery, null, this);
            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
        } else {
            getLoaderManager().restartLoader(LoaderToken.PhotosQuery, null, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults!= null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadPicture();
            } else {
                Toast.makeText(mActivity, "权限被禁止，无法读取本地图片", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void initView() {
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler);
        refreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setProgressBackgroundColor(R.color.light_blue);

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
    }

    @Override
    public void onResume() {
        super.onResume();
        syncToolBarStatus(ActionBarPage.LOCAL_PHOTO_PAGE);
        loadPicture();
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
        loadPicture();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader = null;
        if(id == LoaderToken.PhotosQuery) {
            loader = new CursorLoader(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PhotosQuery.PROJECTION, null, null, null);
        }

        refreshLayout.setRefreshing(true);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == LoaderToken.PhotosQuery) {
            refreshPhotos(data);
        } else {
            data.close();
        }
        refreshLayout.setRefreshing(false);
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(loader.getId() == LoaderToken.PhotosQuery) {
            //TODO
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(LoaderToken.PhotosQuery);
    }
}
