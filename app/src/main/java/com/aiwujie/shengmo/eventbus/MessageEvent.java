package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/6/2.
 */

public class MessageEvent {
    int msgCount;
    int type = 0;

    public MessageEvent(int msgCount) {
        this.msgCount = msgCount;
    }

    public MessageEvent(int msgCount,int type) {
        this.msgCount = msgCount;
        this.type = type;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
