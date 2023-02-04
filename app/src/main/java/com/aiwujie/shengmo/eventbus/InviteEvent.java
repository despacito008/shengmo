package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/6/8.
 */

public class InviteEvent {
    private String uidStr;
    //1.聊天 2.附近 3.查看 4.关注
    private int source;

    public InviteEvent(String uidStr, int source) {
        this.uidStr = uidStr;
        this.source=source;
    }


    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }


    public String getUidStr() {
        return uidStr;
    }

    public void setUidStr(String uidStr) {
        this.uidStr = uidStr;
    }
}
