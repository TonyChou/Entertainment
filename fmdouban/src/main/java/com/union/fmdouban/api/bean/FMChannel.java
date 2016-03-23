package com.union.fmdouban.api.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhouxiaming on 2016/3/23.
 */

public class FMChannel extends BaseObject {
    @SerializedName("channel_id")
    private int channelId;

    @SerializedName("name")
    private String nameZh;

    @SerializedName("seq_id")
    private int seqId;

    @SerializedName("name_en")
    private String nameEn;

    @SerializedName("abbr_en")
    private String abbrEn;

    private boolean isPlaying;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getAbbrEn() {
        return abbrEn;
    }

    public void setAbbrEn(String abbrEn) {
        this.abbrEn = abbrEn;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
}
