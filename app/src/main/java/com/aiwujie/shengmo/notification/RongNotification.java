package com.aiwujie.shengmo.notification;

import android.content.Context;
import android.content.Intent;

import com.aiwujie.shengmo.activity.WelcomeActivity;
import com.xiaomi.mipush.sdk.PushMessageReceiver;


/**
 * Created by 290243232 on 2016/12/12.
 */
public class RongNotification extends PushMessageReceiver {

 /*   @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
//        int alertflag = (int) SharedPreferencesUtils.getParam(context.getApplicationContext(), "alertflag", 1);
//        Log.i("rongcloudflag", "onNotificationMessageArrived: "+alertflag);
//        MediaPlayerManager mediaPlayer=MediaPlayerManager.getInstance(context.getApplicationContext(), R.raw.dog);
//        mediaPlayer.start();
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {

        return true;
    }*/

//    @Override
//    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
//        return false;
//    }
//
//    @Override
//    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
//        Intent intent = new Intent(context, WelcomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        context.startActivity(intent);
//        return true;
//    }
}
