package com.aliyun.apsara.alivclittlevideo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {

    private static final String SHAREDPRE_FILE = "little_video";
    private static final String USER_INFO = "little_user_info";

    public static void setUser(Context context, String user) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(USER_INFO, user);
        editor.apply();
    }

    public static String getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_FILE,
            Activity.MODE_PRIVATE);
        return sharedPreferences.getString(USER_INFO, "");
    }
}
