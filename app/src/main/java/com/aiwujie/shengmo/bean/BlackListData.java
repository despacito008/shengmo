package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/7.
 */
public class BlackListData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"26","nickname":"yroxgixhk","age":"24","head_pic":"http://59.110.28.150:888/Uploads/Picture/2016-12-28/20161228072526669.jpg","sex":"1","role":"SM","vip":"0","realname":"0","last_login_time":"1482881170","city":"北京市","add_time":"1483795562","onlinestate":0,"black_time":"6分钟前"}]
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
         * uid : 26
         * nickname : yroxgixhk
         * age : 24
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2016-12-28/20161228072526669.jpg
         * sex : 1
         * role : SM
         * vip : 0
         * realname : 0
         * last_login_time : 1482881170
         * city : 北京市
         * add_time : 1483795562
         * onlinestate : 0
         * black_time : 6分钟前
         */

        private String uid;
        private String nickname;
        private String age;
        private String head_pic;
        private String sex;
        private String role;
        private String vip;
        private String realname;
        private String last_login_time;
        private String city;
        private String add_time;
        private int onlinestate;
        private String black_time;
        private String is_admin;
        private String is_hand;
        private String vipannual;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String bkvip;
        private String blvip;

        public String getBkvip() {
            return bkvip;
        }

        public void setBkvip(String bkvip) {
            this.bkvip = bkvip;
        }

        public String getBlvip() {
            return blvip;
        }

        public void setBlvip(String blvip) {
            this.blvip = blvip;
        }

        public String getIs_volunteer() {
            return is_volunteer;
        }

        public void setIs_volunteer(String is_volunteer) {
            this.is_volunteer = is_volunteer;
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

        public String getIs_hand() {
            return is_hand;
        }

        public void setIs_hand(String is_hand) {
            this.is_hand = is_hand;
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

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public int getOnlinestate() {
            return onlinestate;
        }

        public void setOnlinestate(int onlinestate) {
            this.onlinestate = onlinestate;
        }

        public String getBlack_time() {
            return black_time;
        }

        public void setBlack_time(String black_time) {
            this.black_time = black_time;
        }
    }
}
