package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/10/9.
 */

public class NearTopEvent {
   private int flag;

    public NearTopEvent(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
