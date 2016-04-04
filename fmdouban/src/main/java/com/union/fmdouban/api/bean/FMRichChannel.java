package com.union.fmdouban.api.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhouxiaming on 16/3/26.
 */
public class FMRichChannel {
    @SerializedName("id")
    private int channelId;

    @SerializedName("name")
    private String name;

    @SerializedName("intro")
    private String introduce;

    @SerializedName("rec_reason")
    private String recReason;

    @SerializedName("banner")
    private String bannerUrl;

    @SerializedName("cover")
    private String coverUrl;

    @SerializedName("song_to_start")
    private String songToStart;

    @SerializedName("song_num")
    private String songNum;

    @SerializedName("hot_songs")
    private String[] hotSongs;


    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getRecReason() {
        return recReason;
    }

    public void setRecReason(String recReason) {
        this.recReason = recReason;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getSongToStart() {
        return songToStart;
    }

    public void setSongToStart(String songToStart) {
        this.songToStart = songToStart;
    }

    public String getSongNum() {
        return songNum;
    }

    public void setSongNum(String songNum) {
        this.songNum = songNum;
    }

    public String[] getHotSongs() {
        return hotSongs;
    }

    public void setHotSongs(String[] hotSongs) {
        this.hotSongs = hotSongs;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof FMRichChannel && ((FMRichChannel) o).getChannelId() == getChannelId()) {
            return true;
        }
        return super.equals(o);
    }
}
