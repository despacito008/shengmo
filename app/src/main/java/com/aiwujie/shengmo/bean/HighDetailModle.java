package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @program: newshengmo
 * @description:
 * @author: whl
 * @create: 2022-05-13 17:30
 **/
public class HighDetailModle {

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public  int retcode;
    public  String  msg;

    public List<DataBean>  data ;

    public  class DataBean {
        public String history_desc;

        public String getHistory_desc() {
            return history_desc;
        }

        public void setHistory_desc(String history_desc) {
            this.history_desc = history_desc;
        }

        public String getHistory_addtime() {
            return history_addtime;
        }

        public void setHistory_addtime(String history_addtime) {
            this.history_addtime = history_addtime;
        }

        public String getHistory_week() {
            return history_week;
        }

        public void setHistory_week(String history_week) {
            this.history_week = history_week;
        }

        public String getHistory_amount() {
            return history_amount;
        }

        public void setHistory_amount(String history_amount) {
            this.history_amount = history_amount;
        }

        public String history_addtime;
        public String history_week;
        public String history_amount;
    }

}
