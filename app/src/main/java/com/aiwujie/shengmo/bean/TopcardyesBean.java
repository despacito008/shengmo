package com.aiwujie.shengmo.bean;

/**
 * Created by Administrator on 2019/7/9.
 */

public class TopcardyesBean {
    int pos;
    String did;

    public TopcardyesBean(int pos, String did) {
        this.pos = pos;
        this.did = did;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }
}
