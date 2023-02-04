package com.aiwujie.shengmo.bean;

import java.util.List;

public class OpenHighRuleModel {

    public int retcode;
    public String msg;

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

    public List<OpenHighRuleBean> getData() {
        return data;
    }

    public void setData(List<OpenHighRuleBean> data) {
        this.data = data;
    }

    public List<OpenHighRuleBean> data;
}
