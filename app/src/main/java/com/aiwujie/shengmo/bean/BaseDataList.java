package com.aiwujie.shengmo.bean;

import java.util.ArrayList;

/**
 * @author：zq 2021/4/13 10:08
 * 邮箱：80776234@qq.com
 */
public class BaseDataList<T> {
    private int retcode;
    private String msg;
    private ArrayList<T> data;

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

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }
}
