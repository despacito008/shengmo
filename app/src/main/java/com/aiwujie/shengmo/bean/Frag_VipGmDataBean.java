package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/6/26.
 */

public class Frag_VipGmDataBean {

    /**
     * retcode : 2000
     * msg : 获取成功!
     * data : [{"id":"19307","order_no":"2019062454525598s","uid":"402624","fuid":"0","along":"31","amount":"0","beans":"1920","channel":"beans","addtime":"1561383558","state":"1","status":"1"},{"id":"19302","order_no":"2019062410252101s","uid":"402624","fuid":"0","along":"31","amount":"0","beans":"1920","channel":"beans","addtime":"1561371823","state":"1","status":"1"},{"id":"19166","order_no":"2019062298575499s","uid":"402624","fuid":"0","along":"31","amount":"0","beans":"1920","channel":"beans","addtime":"1561192715","state":"1","status":"1"},{"id":"19064","order_no":"","uid":"402624","fuid":"0","along":"100","amount":"0","beans":"0","channel":"admin","addtime":"1561084293","state":"0","status":"1"},{"id":"18714","order_no":"","uid":"402624","fuid":"0","along":"10","amount":"0","beans":"0","channel":"admin","addtime":"1560657461","state":"0","status":"1"}]
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
         * addtime : 1561546970
         * beans : 450
         * amount : 0
         * pay_type : vip
         * long : 31
         * channel : beans
         * nickname :
         * addtime_format : 2019-06-26 19:02:50
         * week : 星期三
         */

        private String addtime;
        private String beans;
        private String amount;
        private String pay_type;
        private String days;
        private String channel;
        private String nickname;
        private String addtime_format;
        private String week;

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

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
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
