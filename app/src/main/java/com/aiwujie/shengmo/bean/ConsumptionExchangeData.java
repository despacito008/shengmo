package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/6/27.
 */

public class ConsumptionExchangeData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"addtime":"0","beans":"450","state":"1","num":"1","type":"1","addtime_format":"1970-01-01 08:00:00","week":"星期四","amount":45},{"addtime":"0","beans":"450","state":"1","num":"1","type":"1","addtime_format":"1970-01-01 08:00:00","week":"星期四","amount":45}]
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
         * addtime : 0
         * beans : 450
         * state : 1（1：会员 2：邮票）
         * num : 1
         * type : 1
         * addtime_format : 1970-01-01 08:00:00
         * week : 星期四
         * amount : 45
         */

        private String addtime;
        private String beans;
        private String state;
        private String num;
        private String type;
        private String addtime_format;
        private String week;
        private String amount;
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getBeans() {
            return beans;
        }

        public void setBeans(String beans) {
            this.beans = beans;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
