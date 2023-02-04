package com.aiwujie.shengmo.bean;

import java.util.List;

public class HomeLiveLabelBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"tid":"1","name":"热门","is_active":"0"},{"tid":"2","name":"附近","is_active":"0"},{"tid":"3","name":"健身","is_active":"0"},{"tid":"4","name":"唱歌","is_active":"0"},{"tid":"5","name":"跳舞","is_active":"0"},{"tid":"6","name":"打假","is_active":"0"},{"tid":"7","name":"才艺","is_active":"0"},{"tid":"8","name":"玩儿","is_active":"0"},{"tid":"9","name":"男的","is_active":"0"},{"tid":"10","name":"女的","is_active":"0"}]
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
         * tid : 1
         * name : 热门
         * is_active : 0
         */

        private String tid;
        private String name;
        private String is_active;

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIs_active() {
            return is_active;
        }

        public void setIs_active(String is_active) {
            this.is_active = is_active;
        }
    }
}
