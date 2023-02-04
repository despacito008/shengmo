package com.aiwujie.shengmo.bean;

public class PushTopBean {

    String name;
    String interval;

    public PushTopBean(String name, String interval) {
        this.name = name;
        this.interval = interval;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}
