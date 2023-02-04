package com.aiwujie.shengmo.bean;

/**
 * Created by Administrator on 2020/6/5.
 */

public class LiveAuthData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"idcardStatus":"0","realidcardStatus":"0","bindingMobileStatus":"0","bankCardStatus":"0","applicationAnchorStatus":"1","is_live":"0","head_pic":"http://thirdqq.qlogo.cn/g?b=oidb&k=N1U2guZs8nc1dicwJmJWvWA&s=100&t=1563240715","live_poster":"mock","live_title":"mock","mobile":"mock"}
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
         * idcardStatus : 0
         * realidcardStatus : 0
         * bindingMobileStatus : 0
         * bankCardStatus : 0
         * applicationAnchorStatus : 1
         * is_live : 0
         * head_pic : http://thirdqq.qlogo.cn/g?b=oidb&k=N1U2guZs8nc1dicwJmJWvWA&s=100&t=1563240715
         * live_poster : mock
         * live_title : mock
         * mobile : mock
         */

        private String idcardStatus;
        private String realidcardStatus;
        private String bindingMobileStatus;
        private String bankCardStatus;
        private String applicationAnchorStatus;
        private String is_live;
        private String head_pic;
        private String live_poster;
        private String live_title;
        private String mobile;

        public String getIdcardStatus() {
            return idcardStatus;
        }

        public void setIdcardStatus(String idcardStatus) {
            this.idcardStatus = idcardStatus;
        }

        public String getRealidcardStatus() {
            return realidcardStatus;
        }

        public void setRealidcardStatus(String realidcardStatus) {
            this.realidcardStatus = realidcardStatus;
        }

        public String getBindingMobileStatus() {
            return bindingMobileStatus;
        }

        public void setBindingMobileStatus(String bindingMobileStatus) {
            this.bindingMobileStatus = bindingMobileStatus;
        }

        public String getBankCardStatus() {
            return bankCardStatus;
        }

        public void setBankCardStatus(String bankCardStatus) {
            this.bankCardStatus = bankCardStatus;
        }

        public String getApplicationAnchorStatus() {
            return applicationAnchorStatus;
        }

        public void setApplicationAnchorStatus(String applicationAnchorStatus) {
            this.applicationAnchorStatus = applicationAnchorStatus;
        }

        public String getIs_live() {
            return is_live;
        }

        public void setIs_live(String is_live) {
            this.is_live = is_live;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getLive_poster() {
            return live_poster;
        }

        public void setLive_poster(String live_poster) {
            this.live_poster = live_poster;
        }

        public String getLive_title() {
            return live_title;
        }

        public void setLive_title(String live_title) {
            this.live_title = live_title;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
