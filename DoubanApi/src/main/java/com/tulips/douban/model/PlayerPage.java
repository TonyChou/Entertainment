package com.tulips.douban.model;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * 播放器界面数据
 * Created by zhouxiaming on 2017/4/13.
 */

public class PlayerPage extends BasePage {
    @SerializedName("version_max")
    public int versionMax; //647 作用不详

    @SerializedName("song")
    public List<DouBanSong> songList;

    public class DouBanSong extends BaseCard {
        @SerializedName("albumtitle")
        public String albumTitle;

        @SerializedName("url")
        public String url;

        @SerializedName("file_ext")
        public String fileExt; //文件扩展名

        @SerializedName("album")
        public String album;

        @SerializedName("ssid")
        public String ssid;

        @SerializedName("title")
        public String title;

        @SerializedName("sid")
        public String sid;

        @SerializedName("sha256")
        public String sha256;

        @SerializedName("picture")
        public String picture;

        @SerializedName("update_time")
        public long updateTime;

        @SerializedName("public_time")
        public String publicYear;

        @SerializedName("like")
        public int like;

        @SerializedName("artist")
        public String artist;

        @SerializedName("length")
        public int length;

        @SerializedName("singers")
        public List<DouBanSongSinger> singers;

        @SerializedName("aid")
        public String aid;
    }

    public class DouBanSongSinger extends BaseCard {
        @SerializedName("name")
        public String name;

        @SerializedName("region")
        public JsonArray region; //地区

        @SerializedName("name_usual")
        public String usualName;

        @SerializedName("genre")
        public JsonArray genre; //风格

        @SerializedName("avatar")
        public String avatar;

        @SerializedName("related_site_id")
        public int related_site_id;

        @SerializedName("is_site_artist")
        public boolean is_site_artist;

        @SerializedName("id")
        public String id;
    }
}
