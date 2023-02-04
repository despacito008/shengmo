package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/8/14.
 */

public class RedWomenApplyEvent {
    private int flag;

    public RedWomenApplyEvent(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
