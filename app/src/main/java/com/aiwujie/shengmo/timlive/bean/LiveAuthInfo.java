package com.aiwujie.shengmo.timlive.bean;

public class LiveAuthInfo {


    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"room_id":"","is_live":"0","live_poster":"","live_title":"","idcardStatus":"0","realidcardStatus":"1","bindingMobileStatus":"1","bankCardStatus":"1","applicationAnchorStatus":"3","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-14/20211014203345264.jpg"}
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
         * room_id :
         * is_live : 0
         * live_poster :
         * live_title :
         * idcardStatus : 0
         * realidcardStatus : 1
         * bindingMobileStatus : 1
         * bankCardStatus : 1
         * applicationAnchorStatus : 3
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-10-14/20211014203345264.jpg
         */

        private String room_id;
        private String is_live;
        private String live_poster;
        private String live_title;
        private String idcardStatus;
        private String realidcardStatus;
        private String bindingMobileStatus;
        private String bankCardStatus;
        private String applicationAnchorStatus;
        private String head_pic;

        private String is_withdrawal;

        public String getIs_withdrawal() {
            return is_withdrawal;
        }

        public void setIs_withdrawal(String is_withdrawal) {
            this.is_withdrawal = is_withdrawal;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getIs_live() {
            return is_live;
        }

        public void setIs_live(String is_live) {
            this.is_live = is_live;
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

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }
    }
}
