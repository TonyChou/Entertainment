package com.union.fmdouban.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.union.fmdouban.api.bean.FMChannel;
import com.union.fmdouban.api.bean.FMSong;

import java.util.List;
import java.util.Queue;

/**
 * Created by zhouxiaming on 16/3/21.
 */
public class FMParserFactory {
    private static Gson mGson = new Gson();
    private static JsonParser mParser = new JsonParser();

    /**
     * 解析频道列表
     * @param channelList
     * @return
     */
    public static List<FMChannel> parserToChannelList(String channelList) {
        JsonObject jsonElement = (JsonObject)mParser.parse(channelList);
        JsonArray channelArray = jsonElement.getAsJsonArray("channels");
        List<FMChannel> channels = mGson.fromJson(channelArray, new TypeToken<List<FMChannel>>(){}.getType());
        return channels;
    }

    /**
     * 解析PlayList
     * @param playList
     * @return
     */
    public static Queue<FMSong> parserToSongList(String playList) {
        JsonObject jsonElement = (JsonObject)mParser.parse(playList);
        int ret = jsonElement.get("r").getAsInt();
        if (ret != 0) {
            return null;
        }

        JsonArray jsonArray = jsonElement.getAsJsonArray("song");
        Queue<FMSong> songList = mGson.fromJson(jsonArray, new TypeToken<Queue<FMSong>>() {}.getType());

        return songList;
    }
}
