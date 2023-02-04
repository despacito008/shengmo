package com.aiwujie.shengmo.bean;

/**
 * Created by Administrator on 2019/9/15.
 */

public class ChatRoomMaiPeopleBean {

    String maiid;
    String jianghua;
    String peopleicon;

    public ChatRoomMaiPeopleBean() {
    }

    public ChatRoomMaiPeopleBean(String maiid, String jianghua, String peopleicon) {
        this.maiid = maiid;
        this.jianghua = jianghua;
        this.peopleicon = peopleicon;
    }

    public String getMaiid() {
        return maiid;
    }

    public void setMaiid(String maiid) {
        this.maiid = maiid;
    }

    public String getJianghua() {
        return jianghua;
    }

    public void setJianghua(String jianghua) {
        this.jianghua = jianghua;
    }

    public String getPeopleicon() {
        return peopleicon;
    }

    public void setPeopleicon(String peopleicon) {
        this.peopleicon = peopleicon;
    }
}
