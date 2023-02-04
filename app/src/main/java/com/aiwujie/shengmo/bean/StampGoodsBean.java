package com.aiwujie.shengmo.bean;

import java.util.List;

public class StampGoodsBean {

    /**
     * retcode : 2000
     * msg : 版本
     * data : [{"modou":"1张","money":"2","subject":"7"},{"modou":"3张","money":"6","subject":"8"},{"modou":"5张","money":"10","subject":"9"},{"modou":"10张","money":"20","subject":"10"},{"modou":"30张","money":"60","subject":"11"},{"modou":"50张","money":"100","subject":"12"},{"modou":"100张","money":"200","subject":"13"},{"modou":"200张","money":"400","subject":"14"},{"modou":"300张","money":"600","subject":"15"}]
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
         * modou : 1张
         * money : 2
         * subject : 7
         */

        private String modou;
        private String money;
        private String subject;

        public String getModou() {
            return modou;
        }

        public void setModou(String modou) {
            this.modou = modou;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }
}
