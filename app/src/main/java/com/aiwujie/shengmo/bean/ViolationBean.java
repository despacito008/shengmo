package com.aiwujie.shengmo.bean;

/**
 * Created by Administrator on 2019/8/14.
 */

public class ViolationBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"uid":"396447","dynamicstatusouttimes":"3","chatstatusouttimes":"0","infostatusouttimes":"0","statusouttimes":"0","reporttimes":"0","bereportedtimesright":"14"}
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
         * uid : 396447
         * dynamicstatusouttimes : 3 动态
         * chatstatusouttimes : 0 聊天
         * infostatusouttimes : 0 封资料
         * statusouttimes : 0 封号
         * reporttimes : 0 举报
         * bereportedtimesright : 14 被举报
         * devicestatusouttimes 封设
         */

        private String uid;
        private String dynamicstatusouttimes;
        private String chatstatusouttimes;
        private String infostatusouttimes;
        private String statusouttimes;
        private String reporttimes;
        private String bereportedtimesright;
        private String devicestatusouttimes;
        private String accountnumbercount;
        private String anchor_warning;
        private String montior_kick;
        private String anchor_montior;
        private String chattimes;

        public String getChattimes() {
            return chattimes;
        }

        public void setChattimes(String chattimes) {
            this.chattimes = chattimes;
        }

        public String getAnchor_warning() {
            return anchor_warning;
        }

        public void setAnchor_warning(String anchor_warning) {
            this.anchor_warning = anchor_warning;
        }

        public String getMontior_kick() {
            return montior_kick;
        }

        public void setMontior_kick(String montior_kick) {
            this.montior_kick = montior_kick;
        }

        public String getAnchor_montior() {
            return anchor_montior;
        }

        public void setAnchor_montior(String anchor_montior) {
            this.anchor_montior = anchor_montior;
        }

        public String getAccountnumbercount() {
            return accountnumbercount;
        }

        public void setAccountnumbercount(String accountnumbercount) {
            this.accountnumbercount = accountnumbercount;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getDynamicstatusouttimes() {
            return dynamicstatusouttimes;
        }

        public void setDynamicstatusouttimes(String dynamicstatusouttimes) {
            this.dynamicstatusouttimes = dynamicstatusouttimes;
        }

        public String getChatstatusouttimes() {
            return chatstatusouttimes;
        }

        public void setChatstatusouttimes(String chatstatusouttimes) {
            this.chatstatusouttimes = chatstatusouttimes;
        }

        public String getInfostatusouttimes() {
            return infostatusouttimes;
        }

        public void setInfostatusouttimes(String infostatusouttimes) {
            this.infostatusouttimes = infostatusouttimes;
        }

        public String getStatusouttimes() {
            return statusouttimes;
        }

        public void setStatusouttimes(String statusouttimes) {
            this.statusouttimes = statusouttimes;
        }

        public String getReporttimes() {
            return reporttimes;
        }

        public void setReporttimes(String reporttimes) {
            this.reporttimes = reporttimes;
        }

        public String getBereportedtimesright() {
            return bereportedtimesright;
        }

        public void setBereportedtimesright(String bereportedtimesright) {
            this.bereportedtimesright = bereportedtimesright;
        }

        public String getDevicestatusouttimes() {
            return devicestatusouttimes;
        }

        public void setDevicestatusouttimes(String devicestatusouttimes) {
            this.devicestatusouttimes = devicestatusouttimes;
        }
    }
}
