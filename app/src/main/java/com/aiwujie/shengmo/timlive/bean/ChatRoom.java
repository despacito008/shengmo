package com.aiwujie.shengmo.timlive.bean;

import java.io.Serializable;
import java.util.List;

public class ChatRoom implements Serializable {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"type":"37","num":"11"},{"type":"38","num":"8"},{"type":"39","num":"7"}]
     */

    private int retcode;
    private String msg;
    private List<DataBean> data;

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

    public static class DataBean {
        /**
         * type : 37
         * num : 11
         */

        private String type;
        private String num;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
