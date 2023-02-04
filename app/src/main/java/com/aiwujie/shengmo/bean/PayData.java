package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/24.
 */
public class PayData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"date":"2017-01-23 14:23","week":"星期一","beans":"-50魔豆","fuid":"屌不屌"},{"date":"2017-01-23 14:23","week":"星期一","beans":"-50魔豆","fuid":"屌不屌"},{"date":"2017-01-23 19:52","week":"星期一","beans":"-50魔豆","fuid":"哈哈哈是吧"},{"date":"2017-01-23 20:00","week":"星期一","beans":"-50魔豆","fuid":"哈哈哈是吧"},{"date":"2017-01-23 21:15","week":"星期一","beans":"-1魔豆","fuid":"哈哈哈是吧"},{"date":"2017-01-23 22:10","week":"星期一","beans":"-99魔豆","fuid":"哈哈哈是吧"},{"date":"2017-01-23 22:10","week":"星期一","beans":"-1魔豆","fuid":"哈哈哈是吧"},{"date":"2017-01-24 10:21","week":"星期二","beans":"-1魔豆","fuid":"哈哈哈是吧"},{"date":"2017-01-24 10:32","week":"星期二","beans":"-1魔豆","fuid":"哈哈哈是吧"}]
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
         * date : 2017-01-23 14:23
         * week : 星期一
         * beans : -50魔豆
         * fuid : 屌不屌
         */

        private String date;
        private String week;
        private String beans;
        private String fuid;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getBeans() {
            return beans;
        }

        public void setBeans(String beans) {
            this.beans = beans;
        }

        public String getFuid() {
            return fuid;
        }

        public void setFuid(String fuid) {
            this.fuid = fuid;
        }
    }
}
