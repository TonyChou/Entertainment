// IMediaPlaybackService.aidl
package com.union.player;

import com.union.player.IMediaPlayerCallBack;
import com.union.player.MusicInfo;
interface IMediaPlaybackService {
        void play(String url);
        void playTracks(in List<String> tracks);
        boolean isPlaying();
        void stop();
        void pause();
        void resume();
        void prev();
        void next();
        long duration();
        long position();
        long seek(long pos);
        void setCallBack(in IMediaPlayerCallBack callBack);
        void playMusic(in MusicInfo music);
}
