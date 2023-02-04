package com.tencent.qcloud.tim.tuikit.live.bean;

public class OtherLiveRoomEvent {
    private String uid;
    private String roomId;

    public OtherLiveRoomEvent(String uid, String roomId) {
        this.uid = uid;
        this.roomId = roomId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
