package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/6/28.
 */

public class PresentExchangeWithdrawalsData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"success_time":"1498541711","beans":"1218","money":"87.00","success_time_format":"2017-06-27 13:35:11","week":"星期二"},{"success_time":"1496559260","beans":"5250","money":"375.00","success_time_format":"2017-06-04 14:54:20","week":"星期日"}]
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
         * success_time : 1498541711
         * beans : 1218
         * money : 87.00
         * success_time_format : 2017-06-27 13:35:11
         * week : 星期二
         */

        private String success_time;
        private String beans;
        private String money;
        private String success_time_format;
        private String week;

        public String getSuccess_time() {
            return success_time;
        }

        public void setSuccess_time(String success_time) {
            this.success_time = success_time;
        }

        public String getBeans() {
            return beans;
        }

        public void setBeans(String beans) {
            this.beans = beans;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getSuccess_time_format() {
            return success_time_format;
        }

        public void setSuccess_time_format(String success_time_format) {
            this.success_time_format = success_time_format;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }
    }
}
