package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/5/23.
 */

public class BindingData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"mobile":"13029000604","email":"","openid":"","channel":"0"}
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
         * mobile : 13029000604
         * email :
         * openid :
         * channel : 0
         */

        private String mobile;
        private String email;
        private String openid;
        private String channel;
        private String password_status;

        public String getPassword_status() {
            return password_status;
        }

        public void setPassword_status(String password_status) {
            this.password_status = password_status;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }
    }
}
