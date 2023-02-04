package com.aiwujie.shengmo.eventbus;

/**
 * @program: newshengmo
 * @description: 连线 广播
 * @author: whl
 * @create: 2022-05-30 10:26
 **/
public class FeedLinkPageTurnEvent {
    private   int position;

    public FeedLinkPageTurnEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
