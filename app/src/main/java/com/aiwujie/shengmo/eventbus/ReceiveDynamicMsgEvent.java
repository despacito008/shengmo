package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/3/28.
 */

public class ReceiveDynamicMsgEvent {
    int flag;

    public ReceiveDynamicMsgEvent(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
