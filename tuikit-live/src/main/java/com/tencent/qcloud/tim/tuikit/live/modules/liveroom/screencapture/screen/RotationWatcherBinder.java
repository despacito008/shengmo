package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.screencapture.screen;

import android.os.Binder;

public class RotationWatcherBinder extends Binder {
    private RotationWatcherService mService;

    public RotationWatcherBinder(RotationWatcherService mService) {
        this.mService = mService;
    }

    public RotationWatcherService getService() {
        return this.mService;
    }
}
