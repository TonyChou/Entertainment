package com.union.commonlib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouxiaming on 2016/3/7.
 */

public class DateUtil {

    private static SimpleDateFormat df_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat df_MM_dd_HH_mm_ss = new SimpleDateFormat("MM-dd HH:mm:ss");
    private static SimpleDateFormat df_MM_dd_HH_mm = new SimpleDateFormat("MM-dd HH:mm");
    private static SimpleDateFormat df_HH_mm_ss = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat df_HH_mm = new SimpleDateFormat("HH:mm");
    private static Date data;



    public static long getCurrentDateYMDUNIX() {
        Date date = new Date();
        return date.getTime();
    }

    public static long getCurrentDateYMDUNIX(String date) {
        try {
            SimpleDateFormat sdf_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf_yyyy_MM_dd.parse(date);
            return d.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getCurrentDateYMDHMSUNIX(String date) {
        try {
            SimpleDateFormat sdf_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date d = sdf_yyyy_MM_dd.parse(date);
            return d.getTime();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public static String getDateLikeYYYY_MM_DD(long time) {
        try {
            Date date = new Date(time);

            return df_MM_dd_HH_mm_ss.format(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    public static String getCurrentDateYMD() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }

    public static String getProgramDateMDHMS(String programDate) {
        try {
            Date date = df_yyyy_MM_dd_HH_mm_ss.parse(programDate);
            return df_MM_dd_HH_mm_ss.format(date);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return programDate;
    }

    public static String getProgramDateMDHM(String programDate) {
        try {
            Date date = df_yyyy_MM_dd_HH_mm_ss.parse(programDate);
            return df_MM_dd_HH_mm.format(date);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return programDate;
    }

    public static String getProgramDateHMS(String programDate) {
        try {
            Date date = df_yyyy_MM_dd_HH_mm_ss.parse(programDate);
            return df_HH_mm_ss.format(date);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return programDate;
    }

    public static String getProgramDateHM(String programDate) {
        try {
            Date date = df_yyyy_MM_dd_HH_mm_ss.parse(programDate);
            return df_HH_mm.format(date);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return programDate;
    }

    public static int dayForWeek() {
        Calendar c = Calendar.getInstance();
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    public static int getHour() {
        Calendar c1 = Calendar.getInstance();
        return c1.get(Calendar.HOUR_OF_DAY);
    }

    public static String[] getFromDateToWeek() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] arr = new String[7];
        for (int i = 0; i < arr.length; i++) {
            Date date = new Date();
            long myTime = (date.getTime() / 1000) + 60 * 60 * 24 * i;
            date.setTime(myTime * 1000);
            arr[i] = simpleDateFormat.format(date);
        }
        return arr;
    }

    public static String getFromDateToWeek(int days) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        long myTime = (date.getTime() / 1000) + 60 * 60 * 24 * days;
        date.setTime(myTime * 1000);
        return simpleDateFormat.format(date);
    }
    public static String getCurrentDateYMDHMS() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentTimeHMS() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    public static int getCurrentMinutes() {
        Date date = new Date();
        return date.getHours() * 60 + date.getMinutes();
    }

    public static boolean isGender(String sex) {
        if (sex.equalsIgnoreCase("male") || sex.equalsIgnoreCase("female"))
            return true;
        else
            return false;
    }

    public static boolean isUsrNameValid(String str) {
        Pattern p = Pattern.compile("^[A-Za-z][A-Za-z0-9_]{4,16}[A-Za-z0-9]$");
        Matcher m = p.matcher(str);
        boolean isValid = m.matches();
        return isValid;
    }

    public static boolean isEmailValid(String str) {
        Pattern p = Pattern.compile("\\w+@(\\w+\\.)+[a-z]{2,3}");
        Matcher m = p.matcher(str);
        boolean isValid = m.matches();
        return isValid;
    }

    public static boolean isEmailLengthRange(String str) {
        if (str != null && str.length() > 0&&str.length()<=50)
            return true;
        else
            return false;
    }


    public static boolean isPhoneNumValid(String str) {
        // Pattern p = Pattern
        // .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        // Matcher m = p.matcher(str);
        // boolean isValid = m.matches();
        // return isValid;
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(str);
        return m.matches();

    }



    public static boolean isDateValid(String str) {
        Pattern p = Pattern.compile("\\d{4}-(((0[1-9])|(1[0-2])))(-((0[1-9])|([1-2][0-9])|(3[0-1])))?");
        Matcher m = p.matcher(str);
        boolean isValid = m.matches();
        return isValid;
    }

    public static String getAndroidDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     *
     * @param time
     * @return
     */
    public static String getDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String d = format.format(Long.parseLong(time));
        return d;
    }

    public static Date getDate2(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.valueOf(time));
        Date d = cal.getTime();
        return d;
    }

    public static Long unixTimestamp2String(String srcTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date result_date;
        long result_time = 0;
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            result_date = sdf.parse(srcTime + " 00:00:00");
            result_time = result_date.getTime() / 1000;
        } catch (Exception e) {
        }
        return result_time;
    }

    public static Long unixTimestamp2String2(String srcTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date result_date;
        long result_time = 0;
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            result_date = sdf.parse(srcTime + " 00:00:00");
            result_time = result_date.getTime() / 1000;
        } catch (Exception e) {
        }
        return result_time;
    }

    public static String string2unixTimestamp(long utime) {
        Date date = new Date(utime * 1000);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(date);
    }

    public static String string2unixTimestamp(String utime) {
        long ultime;
        if (utime == null || utime.equals("")) {
            ultime = 0;
        } else {
            ultime = Long.valueOf(utime);
        }
        Date date = new Date(ultime * 1000);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(date);
    }

    public static String string2unixTimestamp2(long utime) {
        Date date = new Date(utime * 1000);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(date);
    }

    public static String string2unixTimestamp2(String utime) {
        long ultime;
        if (utime == null || utime.equals("")) {
            ultime = 0;
        } else {
            ultime = Long.valueOf(utime);
        }
        Date date = new Date(ultime * 1000);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(date);
    }

    public static String formatToTime(String time) {
        String datetime = string2unixTimestamp(Long.parseLong(time));
        String timenew = datetime.substring(new String("yyyy-MM-dd ").length() - 1);
        return timenew;
    }

    public static String formatToDate(String time) {
        String datetime = string2unixTimestamp(Long.parseLong(time));
        String datenew = datetime.substring(0, new String("yyyy-MM-dd ").length() - 1);
        return datenew;
    }

    public static String formatToDateTime(String time) {
        String datetime = string2unixTimestamp(Long.parseLong(time));
        return datetime;
    }

    public static String formatToWeek(String time) {
        Date date = new Date(Long.parseLong(time) * 1000);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat myFmt3 = new SimpleDateFormat("MM-dd HH:mm");
        // SimpleDateFormat myFmt3=new SimpleDateFormat("MM-dd E  HH:mm");
        myFmt3.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return myFmt3.format(date);
    }

    public static void deleteFile(File cache) {
        for (File file : cache.listFiles()) {
            if (file.isFile()) {
                file.delete();
            } else {
                deleteFile(file);
                file.delete();
            }
        }
    }

    public static long getStartTimeOfDay(long time) {
        Date date = new Date(time);
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
    public static long getTimeInDay(long time) {
        Date date = new Date(time);
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time - date.getTime();
    }

    /**将long类型转换String 类型日期*/
    public static String dateToStrLong(Long time) {
        data = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(data);
        return dateString;
    }

}
