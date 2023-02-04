package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/3/28.
 */

public class ClearRedPointEvent {
    String str;
    int flag;

    public ClearRedPointEvent(String str, int flag) {
        this.str = str;
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
