package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/12/15.
 */

public class RedWomenStateEvent {
    private String redState;

    public RedWomenStateEvent(String redState) {
        this.redState = redState;
    }

    public String getRedState() {
        return redState;
    }

    public void setRedState(String redState) {
        this.redState = redState;
    }
}
