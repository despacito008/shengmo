package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/3/24.
 */

public class RedPointEvent {
    private int redCount;
    //1、新人2、访问3、群组 4.动态新 5.动态关注 6.动态推荐
    private int flag;

    public RedPointEvent(int redCount, int flag) {
        this.redCount = redCount;
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getRedCount() {
        return redCount;
    }

    public void setRedCount(int redCount) {
        this.redCount = redCount;
    }
}
