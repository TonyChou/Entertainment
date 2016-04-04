package com.union.fmdouban.api.data;

import com.union.fmdouban.api.bean.FMChannelType;
import com.union.fmdouban.api.bean.FMRichChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 16/3/27.
 */
public class FMCache {
    private static List<FMRichChannel> hotChannels = new ArrayList<>();
    private static List<FMChannelType> typeList = new ArrayList<>();
    public static void addHotChannelsToCache(List<FMRichChannel> list) {
        synchronized (hotChannels) {
            hotChannels.clear();
            for(FMRichChannel channel : list) {
                if (!hotChannels.contains(channel)) {
                    hotChannels.add(channel);
                }
            }
        }
    }

    public static List<FMRichChannel> getHotChannelsFromCache() {
        List<FMRichChannel> list = new ArrayList<FMRichChannel>();
        list.addAll(hotChannels);
        return list;
    }

    public static void addTypeListToCache(List<FMChannelType> types) {
        synchronized (typeList) {
            typeList.clear();
            typeList.addAll(types);
        }
    }

    public static List<FMChannelType> getTypeList() {
        List<FMChannelType> list = new ArrayList<>();
        list.addAll(typeList);
        return list;
    }
}
