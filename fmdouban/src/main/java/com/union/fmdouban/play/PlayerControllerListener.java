package com.union.fmdouban.play;


import com.tulips.douban.model.ChannelsPage;
import com.tulips.douban.model.PlayerPage;
import com.union.fmdouban.service.FMPlayerController;

/**
 * Created by zhouxiaming on 16/3/19.
 */
public interface PlayerControllerListener {
    public void loadCover();
    public void refreshControllerView(ChannelsPage.Channel channel, PlayerPage.DouBanSong song, FMPlayerController.PlayState state, FMPlayerController.StateFrom stateFrom);
    public void sendProgress(int progress);
    public void renderLyric(PlayerPage.DouBanSong song);
    public void showErrorInfo(String error);
}
