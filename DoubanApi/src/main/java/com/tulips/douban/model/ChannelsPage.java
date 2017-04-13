package com.tulips.douban.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhouxiaming on 2017/4/11.
 */
public class ChannelsPage extends BasePage {
    @SerializedName("groups")
    public List<Groups> groupList;
    /**
     * 频道组
     */
    public class Groups extends BaseCard {
        @SerializedName("group_id")
        public int groupId;

        @SerializedName("group_name")
        public String groupName;

        @SerializedName("chls")
        public List<Channel> channels;
    }

    public class Channel extends BaseCard {
        @SerializedName("name")
        public String channelName;

        @SerializedName("id")
        public String channelId;

        @SerializedName("cover")
        public String cover;

        @SerializedName("channel_type")
        public int channelType;

        @SerializedName("intro")
        public String introduce;

        @SerializedName("song_num")
        public int songNum;

        @SerializedName("collected")
        public String collected;

        @SerializedName("style")
        public ChannelStyle style;

        @SerializedName("channel_relation")
        public ChannelRelation relation;


    }

    public class ChannelStyle extends BaseCard {
        @SerializedName("display_text")
        public String displayText;

        @SerializedName("bg_color")
        public String bgColor;

        @SerializedName("layout_type")
        public int layoutType;

        @SerializedName("bg_image")
        public String bgImage;
    }

    public class ChannelRelation extends BaseCard {
        @SerializedName("artist")
        public Artist artist;
    }

    public class Artist extends BaseCard {
        @SerializedName("id")
        public String artistId;
    }
}
