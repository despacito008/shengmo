package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/5/19.
 */

public class DynamicSxEvent {
    String sex;
    String sexual;

    public DynamicSxEvent(String sex, String sexual) {
        this.sex = sex;
        this.sexual = sexual;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSexual() {
        return sexual;
    }

    public void setSexual(String sexual) {
        this.sexual = sexual;
    }
}
