package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/3/30.
 */

public class DynamicUpMediaEditDataEvent {
    /**
     * flag 0:动态 1:上传语音 2:编辑资料
     */
    int flag;

    public DynamicUpMediaEditDataEvent(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
