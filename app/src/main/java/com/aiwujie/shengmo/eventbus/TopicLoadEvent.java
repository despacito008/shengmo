package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/7/21.
 */

public class TopicLoadEvent {
    private int flag;

    public TopicLoadEvent(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
