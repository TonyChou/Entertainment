package com.union.player;

import android.media.MediaPlayer;

/**
 * Created by zhouxiaming on 2017/5/1.
 */

public interface IPlayerListener extends MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {
}
