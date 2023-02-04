package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/12/21.
 */

public class RedWomenJsEvent {
    private int position;
    private String redJs;

    public RedWomenJsEvent(int position, String redJs) {
        this.position = position;
        this.redJs = redJs;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getRedJs() {
        return redJs;
    }

    public void setRedJs(String redJs) {
        this.redJs = redJs;
    }
}
