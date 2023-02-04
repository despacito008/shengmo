package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/7/5.
 */

public class StampBuyData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"id":"35","amount":"0","beans":"210","num":"7","addtime":"1498793619","addtime_format":"2017-06-30 11:33:39","week":"星期五"},{"id":"34","amount":"0","beans":"210","num":"7","addtime":"1498793554","addtime_format":"2017-06-30 11:32:34","week":"星期五"}]
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
         * id : 35
         * amount : 0
         * beans : 210
         * num : 7
         * addtime : 1498793619
         * addtime_format : 2017-06-30 11:33:39
         * week : 星期五
         */

        private String id;
        private String amount;
        private String beans;
        private String num;
        private String addtime;
        private String addtime_format;
        private String week;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBeans() {
            return beans;
        }

        public void setBeans(String beans) {
            this.beans = beans;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getAddtime_format() {
            return addtime_format;
        }

        public void setAddtime_format(String addtime_format) {
            this.addtime_format = addtime_format;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }
    }
}
