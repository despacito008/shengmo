package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/1/9.
 */
public class WalletData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"wallet":"0","nickname":"屌炸天","pay":0.8}
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
         * wallet : 0
         * nickname : 屌炸天
         * pay : 0.8
         */

        private String wallet;
        private String nickname;
        private double pay;

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public double getPay() {
            return pay;
        }

        public void setPay(double pay) {
            this.pay = pay;
        }
    }
}
