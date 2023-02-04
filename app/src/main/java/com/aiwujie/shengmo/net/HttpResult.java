package com.aiwujie.shengmo.net;

import java.util.List;

public class HttpResult {

    /**
     * retcode : 2001
     * msg : 魔豆余额不足！
     * data : []
     */

    private int retcode;
    private String msg;
    private List<?> data;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
