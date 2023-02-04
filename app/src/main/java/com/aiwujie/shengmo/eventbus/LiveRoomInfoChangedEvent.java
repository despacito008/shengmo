package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/5/19.
 */

public class LiveRoomInfoChangedEvent {
    int roomId;
    String type;

    public LiveRoomInfoChangedEvent(int roomId, String type) {
        this.roomId = roomId;
        this.type = type;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
