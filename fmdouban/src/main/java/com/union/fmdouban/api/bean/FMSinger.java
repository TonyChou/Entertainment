package com.union.fmdouban.api.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhouxiaming on 16/3/13.
 */
public class FMSinger extends BaseObject {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("related_site_id")
    private int relatedSiteId;

    @SerializedName("is_site_artist")
    private boolean isSiteArtist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRelatedSiteId() {
        return relatedSiteId;
    }

    public void setRelatedSiteId(int relatedSiteId) {
        this.relatedSiteId = relatedSiteId;
    }

    public boolean isSiteArtist() {
        return isSiteArtist;
    }

    public void setIsSiteArtist(boolean isSiteArtist) {
        this.isSiteArtist = isSiteArtist;
    }
}
