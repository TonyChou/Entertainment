package com.union.fmdouban.api;

/**
 * Created by zhouxiaming on 16/3/20.
 */
public class FMUrl {
    public static String HOST = "http://douban.fm/";
    public static String FM_CHANNELS_URL = "https://www.douban.com/j/app/radio/channels";
    public static String FM_PLAYLIST_URL = "https://douban.fm/j/mine/playlist?type=%s&pt=0.0&channel=%s&pb=128&from=mainsite&r=ffdf27397";
    public static String FM_LYRIC_URL = "https://api.douban.com/v2/fm/lyric&ssid=d8d5&sid=660042";
    public static String FM_GET_CHANNEL_LIST_BY_TYPE = "https://douban.fm/j/explore/genre?gid=%s&start=%d&limit=%d";
    public static String FM_CHANGE_CHANNELS_URL = "https://douban.fm/j/change_channel?fcid=%s&tcid=%s&area=recent_chls";
}
