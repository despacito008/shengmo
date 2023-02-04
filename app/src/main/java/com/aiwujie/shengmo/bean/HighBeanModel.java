package com.aiwujie.shengmo.bean;

import java.util.ArrayList;
import java.util.List;

public class HighBeanModel {

    public int retcode;
    public String msg;
    public List<HighUserBean> data;
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

    public List<HighUserBean> getData() {
        return data;
    }

    public void setData(List<HighUserBean> data) {
        this.data = data;
    }


}
