package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/6/27.
 */

public class MyPurseChangeEvent {
    int tab;

    public MyPurseChangeEvent(int tab) {
        this.tab = tab;
    }

    public int getTab() {
        return tab;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }
}
