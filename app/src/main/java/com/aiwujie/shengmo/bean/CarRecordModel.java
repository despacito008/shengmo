package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @program: newshengmo
 * @description: 座驾明细 Model
 * @author: whl
 * @create: 2022-05-27 10:44
 **/
public class CarRecordModel {


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public String  msg;
    public  int   retcode;
    public List<DataBean> data;


    public class DataBean {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String name;

        public String getRecord_id() {
            return record_id;
        }

        public void setRecord_id(String record_id) {
            this.record_id = record_id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String record_id;
        public String amount;
        public String pay_time;
        public String week;
    }

}
