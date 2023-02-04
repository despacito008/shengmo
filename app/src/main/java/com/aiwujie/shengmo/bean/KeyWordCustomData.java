package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/8/7.
 */

public class KeyWordCustomData {
    private String pid;
    private List<String> keywords;

    public KeyWordCustomData() {
    }

    public KeyWordCustomData(String pid, List<String> keywords) {
        this.pid = pid;
        this.keywords = keywords;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
