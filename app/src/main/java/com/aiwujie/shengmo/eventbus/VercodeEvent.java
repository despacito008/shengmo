package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2018/3/14.
 */

public class VercodeEvent {
    String response;

    public VercodeEvent(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
