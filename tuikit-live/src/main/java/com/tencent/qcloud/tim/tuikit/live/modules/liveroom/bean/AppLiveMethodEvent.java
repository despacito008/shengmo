package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean;

public class AppLiveMethodEvent {
    String type;
    String tag;
    String data;

    public AppLiveMethodEvent(String type, String tag, String data) {
        this.type = type;
        this.tag = tag;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
