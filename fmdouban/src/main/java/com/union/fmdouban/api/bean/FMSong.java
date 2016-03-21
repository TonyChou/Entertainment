package com.union.fmdouban.api.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by zhouxiaming on 16/3/13.
 */
public class FMSong extends BaseObject {

    @SerializedName("album")
    private String album;

    @SerializedName("status")
    private int status;

    @SerializedName("picture")
    private String picture;

    @SerializedName("ssid")
    private String ssid;

    @SerializedName("artist")
    private String artist;

    @SerializedName("url")
    private String url;

    @SerializedName("title")
    private String title;

    @SerializedName("length")
    private int length;

    @SerializedName("like")
    private String like;

    @SerializedName("subtype")
    private String subtype;

    @SerializedName("public_time")
    private String publicTime;

    @SerializedName("sid")
    private String sid;

    private List<FMSinger> singers;

    @SerializedName("aid")
    private String aid;

    @SerializedName("file_ext")
    private String mimeType;

    @SerializedName("sha256")
    private String sha256;

    @SerializedName("kbps")
    private String kbps;

    @SerializedName("albumtitle")
    private String albumTitle;

    @SerializedName("alert_msg")
    private String alertMsg;

    private FMLyric lyric;

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

    public List<FMSinger> getSingers() {
        return singers;
    }

    public void setSingers(List<FMSinger> singers) {
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

    public FMLyric getLyric() {
        return lyric;
    }

    public void setLyric(FMLyric lyric) {
        this.lyric = lyric;
    }
}
