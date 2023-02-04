package com.aiwujie.shengmo.bean;

import java.util.List;

public class TopicPageBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"pid":"-10","pname":"关注"},{"pid":"1","pname":"兴趣"}]
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
         * pid : -10
         * pname : 关注
         */

        private String pid;
        private String pname;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }
    }
}
