package com.aiwujie.shengmo.eventbus;

/**
 * @program: newshengmo
 * @description:
 * @author: whl
 * @create: 2022-05-30 10:28
 **/
public class PlayBackPageTurnEvent {
    private   int position;

    public PlayBackPageTurnEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
