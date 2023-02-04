package com.aiwujie.shengmo.timlive.bean;


import java.util.List;

/**
 * 在线用户榜
 */
public class LiveOnlineUserListInfo {

    /**
     * retcode : 2000
     * msg : success！
     * data : [{"uid":"623791","vip":"1","vipannual":"0","is_admin":"0","is_hidden_admin":"0","svip":"1","svipannual":"1","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-09-02/20210902155546582.jpg","age":"26","role":"M","sex":"1","nickname":"shengmo5"}]
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
         * uid : 623791
         * vip : 1
         * vipannual : 0
         * is_admin : 0
         * is_hidden_admin : 0
         * svip : 1
         * svipannual : 1
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-09-02/20210902155546582.jpg
         * age : 26
         * role : M
         * sex : 1
         * nickname : shengmo5
         */

        private String uid;
        private String vip;
        private String vipannual;
        private String is_admin;
        private String is_hidden_admin;
        private String svip;
        private String svipannual;
        private String head_pic;
        private String age;
        private String role;
        private String sex;
        private String nickname;
        private String mute_type;
        private String is_live_admin;
        private String is_link_mic;

        public String getIs_link_mic() {
            return is_link_mic;
        }

        public void setIs_link_mic(String is_link_mic) {
            this.is_link_mic = is_link_mic;
        }

        public String getIs_live_admin() {
            return is_live_admin;
        }

        public void setIs_live_admin(String is_live_admin) {
            this.is_live_admin = is_live_admin;
        }

        public String getMute_type() {
            return mute_type;
        }

        public void setMute_type(String mute_type) {
            this.mute_type = mute_type;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getVipannual() {
            return vipannual;
        }

        public void setVipannual(String vipannual) {
            this.vipannual = vipannual;
        }

        public String getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(String is_admin) {
            this.is_admin = is_admin;
        }

        public String getIs_hidden_admin() {
            return is_hidden_admin;
        }

        public void setIs_hidden_admin(String is_hidden_admin) {
            this.is_hidden_admin = is_hidden_admin;
        }

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

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
