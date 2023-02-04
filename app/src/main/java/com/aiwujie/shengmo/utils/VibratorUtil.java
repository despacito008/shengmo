package com.aiwujie.shengmo.utils;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * Created by 290243232 on 2017/3/27.
 */

public class VibratorUtil {
    public static void Vibrate(final Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }
    public static void Vibrate(final Context context, long[] pattern,boolean isRepeat) {
        Vibrator vib = (Vibrator) context.getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

}
