package com.union.commonlib.cache;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by zhouxiaming on 16/3/19.
 */
public class CacheManager {
    public static final String PICASSO_CACHE = "picasso-cache";
    public static final String VOLLEY_CACHE = "volley";
    public static final String FM_DOUBAN_CHANNELS = "fm_douban_channels"; //豆瓣FM频道信息
    private Context context;
    private static Gson gson = new Gson();
    public CacheManager(Context context) {
        this.context = context;
    }
    public enum CacheType {
        DoubanFmChannels, All
    }


    public void clearPicassoCache() {
        File picassoCache = new File(context.getCacheDir(), PICASSO_CACHE);
        if (picassoCache.exists()) {
            deleteDirectoryTree(picassoCache);
        }
    }

    public void clearVolleyCache() {
        File volleyCache = new File(context.getCacheDir(), VOLLEY_CACHE);
        if (volleyCache.exists()) {
            deleteDirectoryTree(volleyCache);
        }
    }

    private void deleteDirectoryTree(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteDirectoryTree(child);
            }
        }

        fileOrDirectory.delete();
    }

    /**
     * 根据类型清除缓存信息
     * @param fileName
     */
    public void clearCacheInfo(String fileName) {
        deleteCache(fileName);
    }


    /**
     * 根据Cache文件名删除缓存文件
     * @param fileName
     */
    private void deleteCache(String fileName) {
        File cacheFile = getCacheFile(fileName);
        if (cacheFile != null && cacheFile.exists()) {
            cacheFile.delete();
        }
    }


    /**
     * 获取缓存信息
     *
     * @param cacheFileName
     *            缓存文件类型
     * @param typeToken
     *            缓存类的TypeToken
     * @param <T>
     *            缓存的类型
     * @return 缓存的信息
     */
    public <T> T getFromCache(String cacheFileName, TypeToken<T> typeToken) {
        String json = getJsonStrFromFile(getCacheFile(cacheFileName));
        if (json == null) {
            return null;
        }

        T cacheInfo = null;
        try {
            cacheInfo = gson.fromJson(json, typeToken.getType());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return cacheInfo;
    }

    /**
     * 保存缓存信息
     *
     * @param paramT
     *            缓存的信息
     * @param cacheFileName
     *            缓存文件名
     * @param <T>
     *            缓存的类型
     */
    public <T> void saveToCache(String cacheFileName, T paramT) {
        String json = gson.toJson(paramT);
        saveStrToFile(getCacheFile(cacheFileName), json);
    }

    /**
     * 将缓存数据保存到文件中
     *
     * @param targetFile
     *            目标文件
     * @param json
     *            存储的json内容
     */
    private void saveStrToFile(File targetFile, String json) {
        FileWriter writer;
        try {
            writer = new FileWriter(targetFile);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从指定的JSON文件当中读取JSON串
     *
     * @param file
     *            文件路径
     * @return String JSON串
     */
    private String getJsonStrFromFile(File file) {

        if (file == null || !file.exists()) {
            return null;
        }

        BufferedReader reader = null;
        FileInputStream inputStream = null;
        StringBuffer sb = new StringBuffer();
        try {
            String jsonStr;
            inputStream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((jsonStr = reader.readLine()) != null) {
                sb.append(jsonStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
            }

            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (Exception e2) {
            }
        }
        return sb.toString();
    }

    /**
     * 根据文件名获取缓存文件
     *
     * @param fileName
     *            缓存文件名字
     * @return 文件对象
     */
    private File getCacheFile(String fileName) {
        return new File(getInternalUserRootDir(), fileName);
    }

    /**
     * 获取内部存储的根路径
     *
     */
    private File getInternalUserRootDir() {
        return context.getCacheDir();
    }

}
