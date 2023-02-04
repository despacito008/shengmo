package com.aiwujie.shengmo.bean;

import java.util.List;

public class RedEnvelopesBean {

    /**
     * retcode : 2000
     * msg : success！
     * data : [{"orderid":"6237546237621618401512679","state":"1","uid":"623754","fuid":"623762","num":"10"},{"orderid":"6237546237621618401514243","state":"1","uid":"623754","fuid":"623762","num":"10"},{"orderid":"6237546237621618401536135","state":"1","uid":"623754","fuid":"623762","num":"100"},{"orderid":"6237546237621618401537668","state":"1","uid":"623754","fuid":"623762","num":"100"},{"orderid":"6237546237621618401539874","state":"1","uid":"623754","fuid":"623762","num":"100"},{"orderid":"6237546237621618401559866","state":"1","uid":"623754","fuid":"623762","num":"100"},{"orderid":"6237546237621618401565337","state":"1","uid":"623754","fuid":"623762","num":"100"},{"orderid":"6237546237621618401567185","state":"1","uid":"623754","fuid":"623762","num":"100"},{"orderid":"6237546237621618401587747","state":"1","uid":"623754","fuid":"623762","num":"100"},{"orderid":"6237546237621618401611379","state":"1","uid":"623754","fuid":"623762","num":"100"},{"orderid":"6237546237621618401659408","state":"1","uid":"623754","fuid":"623762","num":"100"},{"orderid":"6237546237621618405877231","state":"1","uid":"623754","fuid":"623762","num":"10"},{"orderid":"6237546237621618466969881","state":"1","uid":"623754","fuid":"623762","num":"10"},{"orderid":"6237546237621618467011438","state":"1","uid":"623754","fuid":"623762","num":"10"},{"orderid":"6237546237621618467333705","state":"1","uid":"623754","fuid":"623762","num":"10"},{"orderid":"6237546237621618481509307","state":"1","uid":"623754","fuid":"623762","num":"10"},{"orderid":"6237546237621618488627919","state":"1","uid":"623754","fuid":"623762","num":"23"},{"orderid":"6237546237621618493775072","state":"1","uid":"623754","fuid":"623762","num":"10"}]
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
         * orderid : 6237546237621618401512679
         * state : 1
         * uid : 623754
         * fuid : 623762
         * num : 10
         */

        private String orderid;
        private String state;
        private String uid;
        private String fuid;
        private String num;

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getFuid() {
            return fuid;
        }

        public void setFuid(String fuid) {
            this.fuid = fuid;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
