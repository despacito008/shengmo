package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/6/14.
 */

public class RecorderEvent {
    private int flag;

    public RecorderEvent(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
