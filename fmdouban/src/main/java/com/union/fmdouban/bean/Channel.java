package com.union.fmdouban.bean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 16/3/13.
 */
public class Channel extends BaseObject {
    private String nameZh; //中文显示名称
    private String nameEn; //英文显示名称
    private int channelId; //channel id
    private int seqId;  //序列ID
    private String abbrEn; //未知
    private boolean isPlaying;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

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

    public static List<Channel> parserJson(JSONObject jsonObject) throws JSONException {
        List<Channel> channels = new ArrayList<Channel>();
        JSONArray jsonArray = jsonObject.getJSONArray("channels");
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Channel channel = new Channel();
            channel.setNameZh(getStringValue("name", object));
            channel.setSeqId(getIntValue("seq_id", object));
            channel.setNameEn(getStringValue("name_en", object));
            channel.setAbbrEn(getStringValue("abbr_en", object));
            channel.setChannelId(getIntValue("channel_id", object));
            channels.add(channel);
        }
        return channels;
    }
}
