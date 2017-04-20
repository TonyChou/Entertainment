package com.union.commonlib;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.union.commonlib.utils.LogUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by zhouxiaming on 2017/4/18.
 */

public class DeviceUtils {
    private static String TAG = "DeviceUtils";
    /**
     * 获取设备ID
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String deviceId = "";
        String str1 = "";
        String str2 = "";
        String str3 = "";
        try {
            TelephonyManager localTelephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            str1 = localTelephonyManager.getDeviceId();
        } catch (Exception e) {
            LogUtils.i(TAG, "TelephonyManager getDeviceId exception : "  + e);
        }

        try {
            str2 = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            LogUtils.i(TAG, "Settings.Secure ANDROID_ID exception : "  + e);
        }

        str3 = Build.SERIAL;

        if (str1 == null) {
            str1 = "";
        }

        if (str2 == null) {
            str2 = "";
        }

        if (str3 == null) {
            str3 = "";
        }
        deviceId = str1 + str2 + str3;
        return getSha1(deviceId);

    }

    public static String getSha1(String str){
        if (null == str || 0 == str.length()){
            return null;
        }
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

}
