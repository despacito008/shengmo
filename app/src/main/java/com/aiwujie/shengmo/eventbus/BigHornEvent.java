package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/6/27.
 */

public class BigHornEvent {
    /**
     * isRedPoint 0.为不显示喇叭红点 1.为显示
     */
    int isRedPoint;

    public BigHornEvent(int isRedPoint) {
        this.isRedPoint = isRedPoint;
    }

    public int getIsRedPoint() {
        return isRedPoint;
    }

    public void setIsRedPoint(int isRedPoint) {
        this.isRedPoint = isRedPoint;
    }
}
