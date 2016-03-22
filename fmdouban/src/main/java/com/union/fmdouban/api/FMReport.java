package com.union.fmdouban.api;

import com.union.fmdouban.api.bean.FMSong;

import java.util.List;
import java.util.Queue;

/**
 * Created by zhouxiaming on 16/3/21.
 * 类型	  需要参数	含义	                                                        报告长度
   b	  sid	    bye，不再播放，并放回一个新的歌曲列表                          	长报告
   e	  sid	    end，当前歌曲播放完毕，但是歌曲队列中还有歌曲	                    短报告
   n		        new，没有歌曲播放，歌曲队列也没有任何歌曲，需要返回新播放列表	        长报告
   p		        playing，歌曲正在播放，队列中还有歌曲，需要返回新的播放列表	        长报告
   s	  sid	    skip，歌曲正在播放，队列中还有歌曲，适用于用户点击下一首	            短报告
   r	  sid	    rate，歌曲正在播放，标记喜欢当前歌曲	                            短报告
   u	  sid	    unrate，歌曲正在播放，标记取消喜欢当前歌曲	                        短报告
 */
public class FMReport {
    public static String genReportType(FMSong song, Queue<FMSong> playList, boolean manualNext) {
        if (manualNext && playList != null && playList.size() > 0) {
            return "s";
        }

        if (song != null && playList != null && playList.size() == 1) {
            return "p";
        }

        if (song != null && playList != null && playList.size() > 1) {
            return "e";
        }



        return "n";
    }
}
