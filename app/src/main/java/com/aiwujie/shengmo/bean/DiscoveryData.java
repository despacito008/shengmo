package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/9/15.
 */

public class DiscoveryData {
    private int imgeId;
    private String title;
    private String content;

    public DiscoveryData(int imgeId, String title, String content) {
        this.imgeId = imgeId;
        this.title = title;
        this.content = content;
    }

    public int getImgeId() {
        return imgeId;
    }

    public void setImgeId(int imgeId) {
        this.imgeId = imgeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
