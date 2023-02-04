package com.aiwujie.shengmo.tim.helper;

import android.app.Activity;

public class ChatContextUtil {

    static Activity currentactivity;

    public static Activity getActivity() {
        return currentactivity;
    }

    public static void setActivity(Activity activity) {
        currentactivity = activity;
    }
}
