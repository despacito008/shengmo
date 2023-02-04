package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/4/13.
 */

public class VisitEvent {
    private int visitcount;

    public VisitEvent(int visitcount) {
        this.visitcount = visitcount;
    }

    public int getVisitcount() {
        return visitcount;
    }

    public void setVisitcount(int visitcount) {
        this.visitcount = visitcount;
    }
}
