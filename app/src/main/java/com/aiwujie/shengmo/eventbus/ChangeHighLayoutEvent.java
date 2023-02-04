package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/3/28.
 */

public class ChangeHighLayoutEvent {
    //1宫格 0列表
    int falg;

    public ChangeHighLayoutEvent(int falg) {
        this.falg = falg;
    }

    public int getFalg() {
        return falg;
    }

    public void setFalg(int falg) {
        this.falg = falg;
    }
}
