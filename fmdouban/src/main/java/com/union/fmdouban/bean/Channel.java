package com.union.fmdouban.bean;

/**
 * Created by zhouxiaming on 16/3/13.
 */
public class Channel extends BaseObject {
    private String nameZh; //中文显示名称
    private String nameEn; //英文显示名称
    private int channelId; //channel id
    private int seqId;  //序列ID
    private String abbrEn; //未知

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public String getAbbrEn() {
        return abbrEn;
    }

    public void setAbbrEn(String abbrEn) {
        this.abbrEn = abbrEn;
    }
}
