package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/6/8.
 */

public class InviteGroupEvent  {
    private String groupIdstr;

    public InviteGroupEvent(String groupIdstr) {
        this.groupIdstr = groupIdstr;
    }

    public String getGroupIdstr() {
        return groupIdstr;
    }

    public void setGroupIdstr(String groupIdstr) {
        this.groupIdstr = groupIdstr;
    }
}
