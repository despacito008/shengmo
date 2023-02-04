package com.aiwujie.shengmo.bean;

/**
 * @program: newshengmo
 * @description:
 * @author: whl
 * @create: 2022-06-16 19:40
 **/
public class LinkStatuskMolde {

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

    public  int retcode;
    public  String   msg;
    public  DataBean   data ;


    public class DataBean {

        public String getChat_status() {
            return chat_status;
        }

        public void setChat_status(String chat_status) {
            this.chat_status = chat_status;
        }

        public String  chat_status;
    }

}
