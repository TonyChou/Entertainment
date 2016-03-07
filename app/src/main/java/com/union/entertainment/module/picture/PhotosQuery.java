package com.union.entertainment.module.picture;

import android.provider.MediaStore;

/**
 * Created by zhouxiaming on 16/3/6.
 */
public interface PhotosQuery {
    int _TOKEN = 0;

    String[] PROJECTION = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT

    };

    int _ID = 0;
    int DATA = 1;
}
