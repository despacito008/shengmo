package com.aiwujie.shengmo.bean;

/**
 * @program: newshengmo
 * @description:
 * @author: whl
 * @create: 2022-06-15 14:31
 **/
public class UserIdCardModel {


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

    public  int  retcode;
    public  String  msg;
    public  DataBean   data ;


    public  class DataBean {
        public  String card_pic;

        public String getCard_pic() {
            return card_pic;
        }

        public void setCard_pic(String card_pic) {
            this.card_pic = card_pic;
        }

        public String getCard_hand() {
            return card_hand;
        }

        public void setCard_hand(String card_hand) {
            this.card_hand = card_hand;
        }

        public  String card_hand;
        public  String card_z;

        public String getCard_z() {
            return card_z;
        }

        public void setCard_z(String card_z) {
            this.card_z = card_z;
        }

        public String getCard_f() {
            return card_f;
        }

        public void setCard_f(String card_f) {
            this.card_f = card_f;
        }

        public  String card_f;
    }

}
