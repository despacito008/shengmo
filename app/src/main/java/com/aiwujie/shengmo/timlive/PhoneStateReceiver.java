package com.aiwujie.shengmo.timlive;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneStateReceiver extends BroadcastReceiver {

    public PhoneStateReceiver(OnPhoneStateListener onPhoneStateListener) {
        this.onPhoneStateListener = onPhoneStateListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
           // Log.e("hg", "呼出……OUTING");
        }
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                     //Log.e("hg", "电话状态……RINGING");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                     //Log.e("hg", "电话状态……OFFHOOK");
                     if (onPhoneStateListener != null) {
                         onPhoneStateListener.onPhoneCall();
                     }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                     //Log.e("hg", "电话状态……IDLE");
                     if (onPhoneStateListener != null) {
                         onPhoneStateListener.onPhoneCancel();
                     }
                    break;
            }
        }
    }

    public boolean isTelephonyCalling(Context context){
        boolean calling = false;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(TelephonyManager.CALL_STATE_OFFHOOK==telephonyManager.getCallState()){
                calling = true;
        }
        return calling;
    }

    public interface OnPhoneStateListener {
        void onPhoneCall();
        void onPhoneCancel();
    }

    OnPhoneStateListener onPhoneStateListener;
}
