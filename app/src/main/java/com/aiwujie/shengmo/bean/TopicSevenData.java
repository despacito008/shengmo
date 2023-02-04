package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/7/20.
 */

public class TopicSevenData {

    private String tid;
    private String title;

    public TopicSevenData() {

    }

    public TopicSevenData(String tid, String title) {
        this.tid = tid;
        this.title = title;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
