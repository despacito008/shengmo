package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/8/4.
 */

public class KeyWordEvent {

    private String keyword;

    public KeyWordEvent(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
