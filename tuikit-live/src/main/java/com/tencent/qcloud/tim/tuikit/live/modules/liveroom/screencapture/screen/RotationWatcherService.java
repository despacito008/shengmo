package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.screencapture.screen;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.IRotationWatcher;

public class RotationWatcherService extends Service {
    public static final String TAG = "RotationWatcherService";

    private IRotationCallback mCallback;

    public void setCallback(IRotationCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        try {
            int rotation = WindowUtils.watchRotation(new RotationWatcher());
            Log.d(TAG, "onRotationChanged = " + rotation);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new RotationWatcherBinder(this);
    }

    public class RotationWatcher extends IRotationWatcher.Stub {

        @Override
        public void onRotationChanged(int rotation) {
            Log.d(TAG, "onRotationChanged = " + rotation);
            mCallback.onRotationChanged(rotation);
        }

        @Override
        public IBinder asBinder() {
            return new RotationWatcher();
        }
    }


}
