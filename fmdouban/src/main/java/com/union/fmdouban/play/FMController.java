package com.union.fmdouban.play;


import com.tulips.douban.model.ChannelsPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 16/6/18.
 */
public class FMController {
    /**
     * 频道切换监听器
     */
    private static List<FMChannelChangedListener> fmchangedListeners = new ArrayList<FMChannelChangedListener>();

    /**
     * 切换频道
     * @param channel
     */
    public static void switchChannel(ChannelsPage.Channel channel) {
        synchronized (fmchangedListeners) {
            for (FMChannelChangedListener listener : fmchangedListeners) {
                listener.changeFMChannel(channel);
            }
        }
    }


    /**
     * 注册频道切换监听
     * @param listener
     */
    public static void registerFMChannelListener(FMChannelChangedListener listener) {
        synchronized (fmchangedListeners) {
            if (!fmchangedListeners.contains(listener)) {
                fmchangedListeners.add(listener);
            }
        }
    }

    /**
     * 删除监听
     * @param listener
     */
    public static void removeFMChannelListener(FMChannelChangedListener listener) {
        synchronized (fmchangedListeners) {
            if (fmchangedListeners.contains(listener)) {
                fmchangedListeners.remove(listener);
            }
        }
    }



    public interface FMChannelChangedListener{
        public void changeFMChannel(ChannelsPage.Channel channel);
    }
}
