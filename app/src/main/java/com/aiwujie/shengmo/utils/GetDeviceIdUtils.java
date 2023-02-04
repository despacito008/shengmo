package com.aiwujie.shengmo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 290243232 on 2017/6/12.
 */

public class GetDeviceIdUtils {
    public static String getSN(Context context){
        String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        String SN = android.os.Build.SERIAL; //序列号，部分手机可以烧录修改
//        Log.i("mainmainAndr", "onCreate: "+ANDROID_ID+","+SN);
        return ANDROID_ID+SN;
    }

    //判断是否是模拟器登录
    public static boolean isAdopt(Context context) {
        IntentFilter intentFilter = new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatusIntent = context.registerReceiver(null, intentFilter);
        int voltage = batteryStatusIntent.getIntExtra("voltage", 99999);
        int temperature = batteryStatusIntent.getIntExtra("temperature", 99999);
       // LogUtil.d("voltage" + voltage);
        //LogUtil.d("temperature" + temperature);
        if (((voltage == 0) && (temperature == 0))
                || ((voltage == 10000) && (temperature == 0))) {
            //这是通过电池的伏数和温度来判断是真机还是模拟器
            return true;
        } else {
            return false;
        }
    }

    private static byte[] getMacInArray() {
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            if (enumeration == null) {
                return null;
            }
            while (enumeration.hasMoreElements()) {
                NetworkInterface netInterface = enumeration.nextElement();
                if (netInterface.getName().equals("wlan0")) {
                    return netInterface.getHardwareAddress();
                }
            }
        } catch (Exception e) {
            LogUtil.e("tag", e.getMessage());
        }
        return null;
    }

    public static long getLongMac() {
        byte[] bytes = getMacInArray();
        if (bytes == null || bytes.length != 6) {
            return 0L;
        }
        long mac = 0L;
        for (int i = 0; i < 6; i++) {
            mac |= bytes[i] & 0xFF;
            if(i != 5){
                mac <<= 8;
            }
        }
        return mac;
    }


    private static final char[] HEX_DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private static final String INVALID_MAC_ADDRESS = "02:00:00:00:00:00";

    private static final String INVALID_ANDROID_ID = "9774d56d682e549c";

    public static String getMacAddress() {
        String mac = formatMac(getMacInArray());
        if (TextUtils.isEmpty(mac) || mac.equals(INVALID_MAC_ADDRESS)) {
            return "";
        }
        return mac;
    }

    private static String formatMac(byte[] bytes) {
        if (bytes == null || bytes.length != 6) {
            return "";
        }
        char[] mac = new char[17];
        int p = 0;
        for (int i = 0; i <= 5; i++) {
            byte b = bytes[i];
            mac[p] = HEX_DIGITS[(b & 0xF0) >> 4];
            mac[p + 1] = HEX_DIGITS[b & 0xF];
            if (i != 5) {
                mac[p + 2] = ':';
                p += 3;
            }
        }
        return new String(mac);
    }

    public static String getAndroidID(Context context) {
        if (context != null) {
            @SuppressLint("HardwareIds")
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (!TextUtils.isEmpty(androidId) && !INVALID_ANDROID_ID.equals(androidId)) {
                return androidId;
            }
        }
        return "";
    }

    public static String getSerialNo() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            return "";
        }
        String serialNo = Build.SERIAL;
        if (TextUtils.isEmpty(serialNo) || serialNo.equals(Build.UNKNOWN)) {
            return "";
        }
        return serialNo;
    }
}
