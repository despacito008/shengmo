package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/12.
 */
public class BillData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"date":"2017-01-12 01:03","week":"星期四","beans":"+420","amount":"充值 RMB 60.00"}]
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
         * date : 2017-01-12 01:03
         * week : 星期四
         * beans : +420
         * amount : 充值 RMB 60.00
         */

        private String date;
        private String week;
        private String beans;
        private String amount;

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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
