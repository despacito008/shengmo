package com.aiwujie.shengmo.bean;

import java.util.List;

public class OpenHighRuleBean {

    public int retcode;
    public String msg;

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

    public List<DataBean> data;

    public OpenHighRuleBean() {

    }

    public  class DataBean {
        public String getBag_id() {
            return bag_id;
        }

        public void setBag_id(String bag_id) {
            this.bag_id = bag_id;
        }

        private String bag_id;

        public String getBag_mark() {
            return bag_mark;
        }

        public void setBag_mark(String bag_mark) {
            this.bag_mark = bag_mark;
        }

        public String getBag_year() {
            return bag_year;
        }

        public void setBag_year(String bag_year) {
            this.bag_year = bag_year;
        }

        public String getBag_price() {
            return bag_price;
        }

        public void setBag_price(String bag_price) {
            this.bag_price = bag_price;
        }

        private String bag_mark;
        private String bag_year;
        private String bag_price;

        public String getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(String original_price) {
            this.original_price = original_price;
        }

        public String getDiscount_desc() {
            return discount_desc;
        }

        public void setDiscount_desc(String discount_desc) {
            this.discount_desc = discount_desc;
        }

        private String original_price;
        private String discount_desc;

    }



}
