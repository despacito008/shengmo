package com.aiwujie.shengmo.bean;

/**
 * Created by Administrator on 2020/6/17.
 */

public class StartPushData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"pushAddress":"rtmp://push.aiwujie.com.cn/live/8013261578637222","pullAddress":"mock","nickname":"mock"}
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
         * pushAddress : rtmp://push.aiwujie.com.cn/live/8013261578637222
         * pullAddress : mock
         * nickname : mock
         */

        private String pushAddress;
        private String pullAddress;
        private String nickname;

        public String getPushAddress() {
            return pushAddress;
        }

        public void setPushAddress(String pushAddress) {
            this.pushAddress = pushAddress;
        }

        public String getPullAddress() {
            return pullAddress;
        }

        public void setPullAddress(String pullAddress) {
            this.pullAddress = pullAddress;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
