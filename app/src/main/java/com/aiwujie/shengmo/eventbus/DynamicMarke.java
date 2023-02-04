package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/1/22.
 */
public class DynamicMarke {
    int position;
    int zancount;

    public DynamicMarke(int position, int zancount) {
        this.position = position;
        this.zancount = zancount;
    }

    public int getZancount() {
        return zancount;
    }

    public void setZancount(int zancount) {
        this.zancount = zancount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
