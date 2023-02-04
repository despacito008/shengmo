package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/6/6.
 */

public class ConversationListData  {
    private String uid;
    private String headurl;
    private String name;
    private String lastContent;
    //1.私聊 2.群组
    private int type;
    private boolean ischeck;
    public String getUid() {
        return uid;
    }

    public boolean isIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastContent() {
        return lastContent;
    }

    public void setLastContent(String lastContent) {
        this.lastContent = lastContent;
    }
}
