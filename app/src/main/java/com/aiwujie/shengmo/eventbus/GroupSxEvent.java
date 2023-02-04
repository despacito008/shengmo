package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/5/19.
 */

public class GroupSxEvent {
    String sexFlag;

    public GroupSxEvent(String sexFlag) {
        this.sexFlag = sexFlag;
    }

    public String getSexFlag() {
        return sexFlag;
    }

    public void setSexFlag(String sexFlag) {
        this.sexFlag = sexFlag;
    }
}
