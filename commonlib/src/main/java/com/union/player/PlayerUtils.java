package com.union.player;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;

import com.union.commonlib.utils.LogUtils;

import java.util.HashMap;

/**
 * Created by zhouxiaming on 2017/5/1.
 */

public class PlayerUtils {
    private static String TAG = "PlayerUtils";
    public static IMediaPlaybackService sService = null;
    private static HashMap<Context, ServiceBinder> sConnectionMap = new HashMap<Context, ServiceBinder>();

    public static class ServiceToken {
        ContextWrapper mWrappedContext;
        ServiceToken(ContextWrapper context) {
            mWrappedContext = context;
        }
    }

    public static ServiceToken bindToService(Activity context) {
        return bindToService(context, null);
    }

    public static ServiceToken bindToService(Activity context, ServiceConnection callback) {
        LogUtils.i(TAG, "bindToService: " + context);
        Activity realActivity = context.getParent();
        if (realActivity == null) {
            realActivity = context;
        }
        ContextWrapper cw = new ContextWrapper(realActivity);
        cw.startService(new Intent(cw, MediaPlayBackService.class));
        ServiceBinder sb = new ServiceBinder(callback);
        if (cw.bindService((new Intent()).setClass(cw, MediaPlayBackService.class), sb, 0)) {
            sConnectionMap.put(cw, sb);
            return new ServiceToken(cw);
        }
        LogUtils.e(TAG, "Failed to bind to service");
        return null;
    }

    public static void unbindFromService(ServiceToken token) {
        LogUtils.i(TAG, "unbindFromService: " + token.mWrappedContext);
        if (token == null) {
            LogUtils.e("MusicUtils", "Trying to unbind with null token");
            return;
        }
        ContextWrapper cw = token.mWrappedContext;
        ServiceBinder sb = sConnectionMap.remove(cw);
        if (sb == null) {
            LogUtils.e("MusicUtils", "Trying to unbind for unknown Context");
            return;
        }
        cw.unbindService(sb);
        if (sConnectionMap.isEmpty()) {
            // presumably there is nobody interested in the service at this point,
            // so don't hang on to the ServiceConnection
            sService = null;
        }
    }

    private static class ServiceBinder implements ServiceConnection {
        ServiceConnection mCallback;
        ServiceBinder(ServiceConnection callback) {
            mCallback = callback;
        }

        public void onServiceConnected(ComponentName className, android.os.IBinder service) {
            sService = IMediaPlaybackService.Stub.asInterface(service);
            if (mCallback != null) {
                mCallback.onServiceConnected(className, service);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            if (mCallback != null) {
                mCallback.onServiceDisconnected(className);
            }
            sService = null;
        }
    }

    public static boolean isServiceAlive() {
        if (sService != null && sService.asBinder().isBinderAlive()) {
            return true;
        }
        return false;
    }

    public static boolean isPlaying() {
        if (isServiceAlive()) {
            try {
                return sService.isPlaying();
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean seekTo(int position) {
        if (isServiceAlive()) {
            try {
                sService.seek(position);
                return true;
            } catch (RemoteException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static int getDuration() {
        if (isServiceAlive()) {
            try {
                return (int)sService.duration();
            } catch (RemoteException e) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public static int getCurrentPosition() {
        if (isServiceAlive()) {
            try {
                return (int)sService.position();
            } catch (RemoteException e) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public static boolean stop() {
        if (isServiceAlive()) {
            try {
                sService.stop();
                return true;
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean resume() {
        if (isServiceAlive()) {
            try {
                sService.resume();
                return true;
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean pause() {
        if(isServiceAlive()) {
            try {
                sService.pause();
                return true;
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean play(String url) {
        if (isServiceAlive()) {
            try {
                sService.play(url);
                return true;
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean setCallBack(IMediaPlayerCallBack callBack) {
        if (isServiceAlive()) {
            try {
                sService.setCallBack(callBack);
                return true;
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean playMusic(MusicInfo musicInfo) {
        if (isServiceAlive()) {
            try {
                sService.playMusic(musicInfo);
                return true;
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }
}
