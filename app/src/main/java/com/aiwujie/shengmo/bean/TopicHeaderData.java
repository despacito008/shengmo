package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/7/19.
 */

public class TopicHeaderData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"tid":"1","title":"456"},{"tid":"5","title":"ceshi"},{"tid":"6","title":"麻辣鸡丝"},{"tid":"7","title":"we"},{"tid":"9","title":"lol"},{"tid":"10","title":"创建话题测试"},{"tid":"11","title":"哦哦叉叉"}]
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
         * title : 456
         */

        private String tid;
        private String title;

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
