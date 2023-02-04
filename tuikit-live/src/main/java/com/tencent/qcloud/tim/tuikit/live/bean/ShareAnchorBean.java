package com.tencent.qcloud.tim.tuikit.live.bean;

public class ShareAnchorBean {
    private String anchorId;
    private String roomId;
    private String livePoster;
    private String liveTitle;

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

    public String getLivePoster() {
        return livePoster;
    }

    public void setLivePoster(String livePoster) {
        this.livePoster = livePoster;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public void setLiveTitle(String liveTitle) {
        this.liveTitle = liveTitle;
    }
}
