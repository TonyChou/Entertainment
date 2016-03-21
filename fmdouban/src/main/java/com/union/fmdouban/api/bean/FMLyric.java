package com.union.fmdouban.api.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhouxiaming on 16/3/20.
 */
public class FMLyric extends BaseObject {
    private String lyric;
    private String name;
    private String sid;

    public FMLyric(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            parser(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public FMLyric(JSONObject jsonObject) {
        parser(jsonObject);
    }

    private void parser(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }

        try {
            lyric = getStringValue("lyric", jsonObject);
            name = getStringValue("name", jsonObject);
            sid = getStringValue("sid", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
