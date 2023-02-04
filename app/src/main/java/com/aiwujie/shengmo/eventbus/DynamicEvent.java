package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/1/22.
 */
public class DynamicEvent {
    int position;
    int laudstate;
    int zancount;

    public DynamicEvent(int position, int laudstate, int zancount) {
        this.position = position;
        this.laudstate = laudstate;
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

    public int getLaudstate() {
        return laudstate;
    }

    public void setLaudstate(int laudstate) {
        this.laudstate = laudstate;
    }
}
