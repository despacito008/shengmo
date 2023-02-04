package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean;

public class AppLiveDialogEvent {
    private String content;
    private String type;
    private int errorCode;

    public AppLiveDialogEvent(String content, String type, int errorCode) {
        this.content = content;
        this.type = type;
        this.errorCode = errorCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
