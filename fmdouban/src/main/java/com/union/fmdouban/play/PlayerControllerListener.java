package com.union.fmdouban.play;

import com.union.fmdouban.bean.Channel;
import com.union.fmdouban.bean.Song;
import com.union.fmdouban.service.FMPlayerService;

/**
 * Created by zhouxiaming on 16/3/19.
 */
public interface PlayerControllerListener {
    public void loadCover();
    public void refreshControllerView(Channel channel, Song song, FMPlayerService.PlayState state);
    public void sendProgress(int progress);
}
