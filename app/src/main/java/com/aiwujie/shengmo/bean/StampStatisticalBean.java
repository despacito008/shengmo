package com.aiwujie.shengmo.bean;

import java.util.List;

public class StampStatisticalBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"amount":"0","beans":"20","num":"1","addtime":"1643246921","addtime_format":"2022-01-27 09:28:41","week":"星期四"}]
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
         * amount : 0
         * beans : 20
         * num : 1
         * addtime : 1643246921
         * addtime_format : 2022-01-27 09:28:41
         * week : 星期四
         */

        private String amount;
        private String beans;
        private String num;
        private String addtime;
        private String addtime_format;
        private String week;
        private String type;
        private String nickname;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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
