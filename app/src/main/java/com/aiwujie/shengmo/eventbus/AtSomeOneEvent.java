package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/5/4.
 */

public class AtSomeOneEvent {
    int flag;

    public AtSomeOneEvent(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
