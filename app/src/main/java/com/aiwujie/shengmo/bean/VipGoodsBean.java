package com.aiwujie.shengmo.bean;

import java.util.List;

public class VipGoodsBean {
    private int retcode;
    private String msg;
    private List<VipGoodsBean.DataBean> data;

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

    public List<VipGoodsBean.DataBean> getData() {
        return data;
    }

    public void setData(List<VipGoodsBean.DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String subject;
        private String vip_name;
        private String vip_price;
        private String vip_original_price;
        private String vip_discount;
        private String vip_info;

        public String getVip_info() {
            return vip_info;
        }

        public void setVip_info(String vip_info) {
            this.vip_info = vip_info;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getVip_name() {
            return vip_name;
        }

        public void setVip_name(String vip_name) {
            this.vip_name = vip_name;
        }

        public String getVip_price() {
            return vip_price;
        }

        public void setVip_price(String vip_price) {
            this.vip_price = vip_price;
        }

        public String getVip_original_price() {
            return vip_original_price;
        }

        public void setVip_original_price(String vip_original_price) {
            this.vip_original_price = vip_original_price;
        }

        public String getVip_discount() {
            return vip_discount;
        }

        public void setVip_discount(String vip_discount) {
            this.vip_discount = vip_discount;
        }
    }
}
