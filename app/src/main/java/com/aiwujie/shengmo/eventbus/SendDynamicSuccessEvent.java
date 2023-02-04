package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2017/3/29.
 */

public class SendDynamicSuccessEvent {
    private int success;

    public SendDynamicSuccessEvent(int success) {
        this.success = success;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
