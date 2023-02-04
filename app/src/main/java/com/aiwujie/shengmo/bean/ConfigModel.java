package com.aiwujie.shengmo.bean;

public class ConfigModel {


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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public  int retcode;
    public  String  msg;
    public  DataBean  data;

    public class DataBean {

        public String getTop_text() {
            return top_text;
        }

        public void setTop_text(String top_text) {
            this.top_text = top_text;
        }

        public String top_text;

        public String getTop_lady_tips() {
            return top_lady_tips;
        }

        public void setTop_lady_tips(String top_lady_tips) {
            this.top_lady_tips = top_lady_tips;
        }

        public String top_lady_tips;

    }
}
