package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/9.
 */
public class BankListData {

    /**
     * retcode : 2000
     * msg : 用户银行卡信息信息获取成功！
     * data : [{"bid":"59","bankcard":"6221882400086645257","realname":"邮储银行-绿卡银联标准卡-借记卡","bankname":"金融好"}]
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
         * bid : 59
         * bankcard : 6221882400086645257
         * realname : 邮储银行-绿卡银联标准卡-借记卡
         * bankname : 金融好
         */

        private String bid;
        private String bankcard;
        private String realname;
        private String bankname;
        private String pay_status; //提现类型 1-支付宝 0-银行卡
        private String bank_status; //是否默认 1

        public String getPay_status() {
            return pay_status;
        }

        public void setPay_status(String pay_status) {
            this.pay_status = pay_status;
        }

        public String getBank_status() {
            return bank_status;
        }

        public void setBank_status(String bank_status) {
            this.bank_status = bank_status;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getBankcard() {
            return bankcard;
        }

        public void setBankcard(String bankcard) {
            this.bankcard = bankcard;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getBankname() {
            return bankname;
        }

        public void setBankname(String bankname) {
            this.bankname = bankname;
        }
    }
}
