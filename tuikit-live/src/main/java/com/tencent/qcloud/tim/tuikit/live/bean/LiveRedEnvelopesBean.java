package com.tencent.qcloud.tim.tuikit.live.bean;

import java.util.List;

public class LiveRedEnvelopesBean {

    /**
     * retcode : 2000
     * msg : 获取成功
     * data : [{"orderid":"402583_407943_1642059038_43921","addtime":"1642059038"},{"orderid":"402583_407943_1642061866_39537","addtime":"1642061866"},{"orderid":"402583_407943_1642062084_60585","addtime":"1642062084"},{"orderid":"402583_407943_1642062168_42259","addtime":"1642062168"}]
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
         * orderid : 402583_407943_1642059038_43921
         * addtime : 1642059038
         */

        private String orderid;
        private String addtime;
        private String nickname;
        private String head_pic;
        private String uid;
        private String isReceive = "0";
        private String state = "";

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getIsReceive() {
            return isReceive;
        }

        public void setIsReceive(String isReceive) {
            this.isReceive = isReceive;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}
