package com.union.fmdouban.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by zhouxiaming on 16/3/13.
 */
public class BaseObject implements Serializable {

    protected static String getStringValue(String key, JSONObject jsonObject) throws JSONException {
        return jsonObject.isNull(key)? null : jsonObject.getString(key);
    }
    protected static int getIntValue(String key, JSONObject jsonObject) throws JSONException {
        return jsonObject.isNull(key)? null : jsonObject.getInt(key);
    }
}
