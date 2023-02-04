package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/11/29.
 */

public class VipTimeData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"vip":"1","vipovertime":"2018年11月30日","svip":"1","svipovertime":"2018年07月29日"}
     * （0：非会员 1：会员）
     */

    private int retcode;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * vip : 1
         * vipovertime : 2018年11月30日
         * svip : 1
         * svipovertime : 2018年07月29日
         */

        private String vip;
        private String vipovertime;
        private String svip;
        private String svipovertime;

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getVipovertime() {
            return vipovertime;
        }

        public void setVipovertime(String vipovertime) {
            this.vipovertime = vipovertime;
        }

        public String getSvip() {
            return svip;
        }

        public void setSvip(String svip) {
            this.svip = svip;
        }

        public String getSvipovertime() {
            return svipovertime;
        }

        public void setSvipovertime(String svipovertime) {
            this.svipovertime = svipovertime;
        }
    }
}
