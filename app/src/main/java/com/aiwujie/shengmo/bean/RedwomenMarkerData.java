package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/8/18.
 */

public class RedwomenMarkerData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"11","nickname":"绿色斯慕","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-08-09/20170809175850902.jpg"},{"uid":"12","nickname":"小清新","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-16/20170616143502250.jpg"},{"uid":"175","nickname":"深藏功与名.3","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-05-19/20170519175817473.jpg"}]
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
         * uid : 11
         * nickname : 绿色斯慕
         * head_pic : http://hao.shengmo.org:888/Uploads/Picture/2017-08-09/20170809175850902.jpg
         */

        private String uid;
        private String nickname;
        private String head_pic;
        private String svip;
        private String svipannual;
        private String vip;
        private String is_admin;
        private String vipannual;

        public String getSvip() {
            return svip;
        }

        public void setSvip(String svip) {
            this.svip = svip;
        }

        public String getSvipannual() {
            return svipannual;
        }

        public void setSvipannual(String svipannual) {
            this.svipannual = svipannual;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(String is_admin) {
            this.is_admin = is_admin;
        }

        public String getVipannual() {
            return vipannual;
        }

        public void setVipannual(String vipannual) {
            this.vipannual = vipannual;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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
    }
}
