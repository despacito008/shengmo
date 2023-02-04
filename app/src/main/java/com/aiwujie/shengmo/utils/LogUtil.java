package com.aiwujie.shengmo.utils;

import android.util.Log;

import com.aiwujie.shengmo.application.MyApp;

public class LogUtil {
    private static String TAG = "logUtil";

    public static void d(Object msg) {
        if(VersionUtils.isApkInDebug(MyApp.getInstance())) {
            Log.d(TAG,String.valueOf(msg));
        }
    }

    public static void i(Object msg) {
        if(VersionUtils.isApkInDebug(MyApp.getInstance())) {
            Log.i(TAG,String.valueOf(msg));
        }
    }

    public static void e(Object msg) {
        if(VersionUtils.isApkInDebug(MyApp.getInstance())) {
            Log.e(TAG,String.valueOf(msg));
        }
    }

    public static void d(String TAG,Object msg) {
        if(VersionUtils.isApkInDebug(MyApp.getInstance())) {
            Log.d(TAG,String.valueOf(msg));
        }
    }

    public static void i(String TAG,Object msg) {
        if(VersionUtils.isApkInDebug(MyApp.getInstance())) {
            Log.i(TAG,String.valueOf(msg));
        }
    }

    public static void e(String TAG,Object msg) {
        if(VersionUtils.isApkInDebug(MyApp.getInstance())) {
            Log.e(TAG,String.valueOf(msg));
        }
    }
}
