package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/11/22.
 */

public class VipEvent {
    //1.为vip 2.为svip
    private int type;
    private int buyamount;
    private int buysubject;
    public VipEvent(int type,int buyamount, int buysubject) {
        this.type=type;
        this.buyamount = buyamount;
        this.buysubject = buysubject;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBuyamount() {
        return buyamount;
    }

    public void setBuyamount(int buyamount) {
        this.buyamount = buyamount;
    }

    public int getBuysubject() {
        return buysubject;
    }

    public void setBuysubject(int buysubject) {
        this.buysubject = buysubject;
    }
}
