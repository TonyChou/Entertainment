package com.union.fmdouban.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhouxiaming on 16/3/13.
 */
public class Song extends BaseObject {
    private String album;
    private int status;
    private String picture;
    private String ssid;
    private String artist;
    private String url;
    private String title;
    private int length;
    private String like;
    private String subtype;
    private String publicTime;
    private String sid;
    private List<Singer> singers;
    private String aid;
    private String mimeType;
    private String sha256;
    private String kbps;
    private String albumTitle;
    private String alertMsg;

    public Song() {
        this(null);
    }

    public Song(String jsonData) {
        singers = new ArrayList<Singer>();
        if (jsonData == null) {
            return;
        }
        try {
            parserJsonData(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parserJsonData(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);
        if (jsonObject == null) {
            return;
        }
        this.album = getStringValue("album", jsonObject);
        this.status = getIntValue("status", jsonObject);
        this.ssid = getStringValue("ssid", jsonObject);
        this.picture = getStringValue("picture", jsonObject);
        this.artist = getStringValue("artist", jsonObject);
        this.url = getStringValue("url", jsonObject);
        this.title = getStringValue("title", jsonObject);
        this.length = getIntValue("length", jsonObject);
        this.like = getStringValue("like", jsonObject);
        this.subtype = getStringValue("subtype", jsonObject);
        this.publicTime = getStringValue("public_time", jsonObject);
        this.sid = getStringValue("sid", jsonObject);
        this.aid = getStringValue("aid", jsonObject);
        this.mimeType = getStringValue("file_ext", jsonObject);
        this.sha256 = getStringValue("sha256", jsonObject);
        this.kbps = getStringValue("kbps", jsonObject);
        this.albumTitle = getStringValue("albumtitle", jsonObject);
        this.alertMsg = getStringValue("alert_msg", jsonObject);
        parserSingers(jsonObject);
    }

    private void parserSingers(JSONObject jsonObject) throws JSONException {
        JSONArray singerArr = jsonObject.isNull("singers")? null : jsonObject.getJSONArray("singers");
        if (singerArr != null && singerArr.length() > 0) {
            for (int i = 0; i < singerArr.length(); i++) {
                JSONObject singerJson = singerArr.getJSONObject(i);
                Singer s = new Singer();
                s.setId(getStringValue("id", singerJson));
                s.setName(getStringValue("name", singerJson));
                singers.add(s);
            }
        }
    }

    private String getStringValue(String key, JSONObject jsonObject) throws JSONException {
        return jsonObject.isNull(key)? null : jsonObject.getString(key);
    }
    private int getIntValue(String key, JSONObject jsonObject) throws JSONException {
        return jsonObject.isNull(key)? null : jsonObject.getInt(key);
    }


    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(String publicTime) {
        this.publicTime = publicTime;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public List<Singer> getSingers() {
        return singers;
    }

    public void setSingers(List<Singer> singers) {
        this.singers = singers;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getKbps() {
        return kbps;
    }

    public void setKbps(String kbps) {
        this.kbps = kbps;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getAlertMsg() {
        return alertMsg;
    }

    public void setAlertMsg(String alertMsg) {
        this.alertMsg = alertMsg;
    }
}
