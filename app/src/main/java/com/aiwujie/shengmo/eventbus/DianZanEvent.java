package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/6/28.
 * 点赞
 */

public class DianZanEvent {
    private  int position;
    public DianZanEvent(int position) {
        this.position=position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
