package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/8/1.
 */

public class EditDynamicIndexEvent {
    private int index;

    public EditDynamicIndexEvent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
