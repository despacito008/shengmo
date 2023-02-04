package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/7/3.
 */

public class ShareCallBackEvent {
    private int way;

    public ShareCallBackEvent(int way) {
        this.way = way;
    }

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }
}
