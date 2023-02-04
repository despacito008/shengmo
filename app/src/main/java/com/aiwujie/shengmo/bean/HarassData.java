package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/6/13.
 */

public class HarassData {
    /**
     * retcode : 2000
     * msg : 1设置成功！
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
