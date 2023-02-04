package com.aiwujie.shengmo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.aiwujie.shengmo.R;

public class ActivityJumpUtil {

    public static void toActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }

    public static void toActivity(Context context, Intent intent) {
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity)context).overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        }
    }
}
