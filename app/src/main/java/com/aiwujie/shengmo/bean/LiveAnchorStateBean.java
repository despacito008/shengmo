package com.aiwujie.shengmo.bean;

public class LiveAnchorStateBean {

    /**
     * retcode : 2000
     * msg : 查询成功
     * data : {"recommend_status":"0","prohibition_status":"0"}
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
         * recommend_status : 0
         * prohibition_status : 0
         */

        private String recommend_status;
        private String prohibition_status;
        private String anchor_status;

        public String getAnchor_status() {
            return anchor_status;
        }

        public void setAnchor_status(String anchor_status) {
            this.anchor_status = anchor_status;
        }

        public String getRecommend_status() {
            return recommend_status;
        }

        public void setRecommend_status(String recommend_status) {
            this.recommend_status = recommend_status;
        }

        public String getProhibition_status() {
            return prohibition_status;
        }

        public void setProhibition_status(String prohibition_status) {
            this.prohibition_status = prohibition_status;
        }
    }
}
