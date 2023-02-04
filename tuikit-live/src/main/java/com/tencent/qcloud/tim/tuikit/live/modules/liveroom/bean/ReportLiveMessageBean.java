package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean;

public class ReportLiveMessageBean {
    private String anchorId;
    private String roomId;
    private String content;


    public ReportLiveMessageBean(String anchorId, String roomId, String content) {
        this.anchorId = anchorId;
        this.roomId = roomId;
        this.content = content;
    }

    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
