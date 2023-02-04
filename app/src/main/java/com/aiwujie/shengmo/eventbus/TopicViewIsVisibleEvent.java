package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/7/18.
 */

public class TopicViewIsVisibleEvent {
    //显示还是隐藏 0，隐藏 1，显示
    private int isVisible;

    public TopicViewIsVisibleEvent(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }
}
