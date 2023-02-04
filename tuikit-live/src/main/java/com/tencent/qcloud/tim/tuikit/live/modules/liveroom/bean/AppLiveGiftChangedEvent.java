package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean;

import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;

public class AppLiveGiftChangedEvent {
    public int changedNo;
    public GiftInfo changedInfo;

    public AppLiveGiftChangedEvent(int changeNum, GiftInfo changedInfo) {
        this.changedNo = changeNum;
        this.changedInfo = changedInfo;
    }
}
