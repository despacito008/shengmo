package com.tencent.qcloud.tim.tuikit.live.bean;

public class LinkMicStateBean {
    String anchorId;
    int linkState;
    boolean isCameraOpen;

    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    public int getLinkState() {
        return linkState;
    }

    public void setLinkState(int linkState) {
        this.linkState = linkState;
    }

    public boolean isCameraOpen() {
        return isCameraOpen;
    }

    public void setCameraOpen(boolean cameraOpen) {
        isCameraOpen = cameraOpen;
    }
}
