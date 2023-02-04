package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/11/7.
 */

public class AllUserStates {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"status":"1","dynamicstatus":"1","infostatus":"1","chatstatus":"1","devicestatus":"1"}
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
         * status : 1
         * dynamicstatus : 1
         * infostatus : 1
         * chatstatus : 1
         * devicestatus : 1
         */

        private String status;
        private String dynamicstatus;
        private String infostatus;
        private String chatstatus;
        private String devicestatus;
        private String livestatus;

        public String getLivestatus() {
            return livestatus;
        }

        public void setLivestatus(String livestatus) {
            this.livestatus = livestatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDynamicstatus() {
            return dynamicstatus;
        }

        public void setDynamicstatus(String dynamicstatus) {
            this.dynamicstatus = dynamicstatus;
        }

        public String getInfostatus() {
            return infostatus;
        }

        public void setInfostatus(String infostatus) {
            this.infostatus = infostatus;
        }

        public String getChatstatus() {
            return chatstatus;
        }

        public void setChatstatus(String chatstatus) {
            this.chatstatus = chatstatus;
        }

        public String getDevicestatus() {
            return devicestatus;
        }

        public void setDevicestatus(String devicestatus) {
            this.devicestatus = devicestatus;
        }
    }
}
