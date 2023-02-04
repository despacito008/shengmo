package com.aiwujie.shengmo.bean;

public class AppUpdateBean {

    /**
     * retcode : 2000
     * msg : success！
     * data : {"user_version":"5.3.3","down_url":"http://image.aiwujie.com.cn/shengmo5.2.3.apk"}
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
         * user_version : 5.3.3
         * down_url : http://image.aiwujie.com.cn/shengmo5.2.3.apk
         */

        private String user_version;
        private String down_url;

        public String getUser_version() {
            return user_version;
        }

        public void setUser_version(String user_version) {
            this.user_version = user_version;
        }

        public String getDown_url() {
            return down_url;
        }

        public void setDown_url(String down_url) {
            this.down_url = down_url;
        }
    }
}
