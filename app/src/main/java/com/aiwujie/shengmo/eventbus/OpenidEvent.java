package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/5/24.
 */

public class OpenidEvent {
    private String openid;

    public OpenidEvent(String openid) {
        this.openid = openid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
