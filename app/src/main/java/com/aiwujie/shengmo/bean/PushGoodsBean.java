package com.aiwujie.shengmo.bean;

import java.util.List;

public class PushGoodsBean {
    private int retcode;
    private String msg;
    private List<PushGoodsBean.DataBean> data;

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

    public List<PushGoodsBean.DataBean> getData() {
        return data;
    }

    public void setData(List<PushGoodsBean.DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String push_num;
        private String push_name;
        private String push_price;
        private String push_original_price;
        private String push_average_price;
        private String push_discount;

        public DataBean(String push_num, String push_name, String push_price, String push_original_price, String push_average_price, String push_discount) {
            this.push_num = push_num;
            this.push_name = push_name;
            this.push_price = push_price;
            this.push_original_price = push_original_price;
            this.push_average_price = push_average_price;
            this.push_discount = push_discount;
        }

        public String getPush_num() {
            return push_num;
        }

        public void setPush_num(String push_num) {
            this.push_num = push_num;
        }

        public String getPush_name() {
            return push_name;
        }

        public void setPush_name(String push_name) {
            this.push_name = push_name;
        }

        public String getPush_price() {
            return push_price;
        }

        public void setPush_price(String push_price) {
            this.push_price = push_price;
        }

        public String getPush_original_price() {
            return push_original_price;
        }

        public void setPush_original_price(String push_original_price) {
            this.push_original_price = push_original_price;
        }

        public String getPush_average_price() {
            return push_average_price;
        }

        public void setPush_average_price(String push_average_price) {
            this.push_average_price = push_average_price;
        }

        public String getPush_discount() {
            return push_discount;
        }

        public void setPush_discount(String push_discount) {
            this.push_discount = push_discount;
        }
    }
}
