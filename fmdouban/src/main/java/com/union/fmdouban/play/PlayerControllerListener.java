package com.union.fmdouban.play;

import com.union.fmdouban.api.bean.FMChannel;
import com.union.fmdouban.api.bean.FMSong;
import com.union.fmdouban.service.FMPlayerService;

/**
 * Created by zhouxiaming on 16/3/19.
 */
public interface PlayerControllerListener {
    public void loadCover();
    public void refreshControllerView(FMChannel channel, FMSong song, FMPlayerService.PlayState state);
    public void sendProgress(int progress);
}
