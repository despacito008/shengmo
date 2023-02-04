package com.aiwujie.shengmo.bean;

import java.util.List;

public class WithdrawLogBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"money":"2.00","beans":"50","state":"1","add_time":"1634526186","bankname":"工商银行-牡丹卡普卡-借记卡","bankcard":"6212262502558865"},{"money":"2.00","beans":"50","state":"2","add_time":"1634526139","bankname":"工商银行-牡丹卡普卡-借记卡","bankcard":"6212262502558865"},{"money":"1.20","beans":"30","state":"2","add_time":"1634525704","bankname":"工商银行-牡丹卡普卡-借记卡","bankcard":"6212262502558865"}]
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
         * money : 2.00
         * beans : 50
         * state : 1
         * add_time : 1634526186
         * bankname : 工商银行-牡丹卡普卡-借记卡
         * bankcard : 6212262502558865
         */

        private String money;
        private String beans;
        private String state;
        private String add_time;
        private String bankname;
        private String bankcard;
        private String anchor_status;

        public String getAnchor_status() {
            return anchor_status;
        }

        public void setAnchor_status(String anchor_status) {
            this.anchor_status = anchor_status;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
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

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getBankname() {
            return bankname;
        }

        public void setBankname(String bankname) {
            this.bankname = bankname;
        }

        public String getBankcard() {
            return bankcard;
        }

        public void setBankcard(String bankcard) {
            this.bankcard = bankcard;
        }
    }
}
