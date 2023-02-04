package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/6/27.
 */

public class ConsumptionGiveData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"addtime":"1498547564","beans":"2","amount":"0.2","type":"10","num":"1","state":"1","nickname":"哎呀哎","addtime_format":"2017-06-27 15:12:44","week":"星期二"},{"addtime":"1498547533","beans":"2","amount":"0.2","type":"10","num":"1","state":"1","nickname":"哎呀哎","addtime_format":"2017-06-27 15:12:13","week":"星期二"}]
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
         * addtime : 1498547564
         * beans : 2
         * amount : 0.2
         * type : 10
         * num : 1
         * state : 1（1：礼物 2：会员 3：邮票）
         * nickname : 哎呀哎
         * addtime_format : 2017-06-27 15:12:44
         * week : 星期二
         */

        private String addtime;
        private String beans;
        private String amount;
        private String type;
        private String num;
        private String state;
        private String nickname;
        private String addtime_format;
        private String week;
        private String type_name;

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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
