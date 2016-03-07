package com.union.entertainment.module.picture;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 16/3/6.
 */
public class PhotosController {
    /**
     * 从Cursor 里面加载图片信息到列表
     * @param cursor
     * @return
     */
    public static List<Photo> loadPhotoFromCursor(Cursor cursor) {
        List<Photo> list = new ArrayList<Photo>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Photo photo = new Photo();
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                long date = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
                int width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
                int height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
                photo.setName(name);
                photo.setPath(path);
                photo.setModifyDate(date);
                photo.setWidth(width);
                photo.setHeight(height);
                list.add(photo);
            }
            cursor.close();
        }

        return list;
    }


}
