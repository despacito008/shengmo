package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2016/12/24.
 */
public class HomeListviewData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"5","nickname":"屌","age":"3","head_pic":"http://59.110.28.150:888/Uploads/Picture/2016-12-19/20161219224411226.jpg","sex":"1","role":"S","vip":"1","realname":"0","last_login_time":"46分钟前","distance":"0.03","sign":"志伟篮子志伟篮子志伟篮子","lat":"39.856144","lng":"116.712029","onlinestate":0},{"uid":"7","nickname":"张 三","age":"116","head_pic":"http://59.110.28.150:888/Uploads/Picture/2016-12-24/20161224130658949.jpg","sex":"1","role":"M","vip":"0","realname":"0","last_login_time":"2小时前","distance":"29.36","sign":"志伟篮子志伟篮子志伟篮子志伟篮子","lat":"40.000000","lng":"117.000000","onlinestate":0},{"uid":"6","nickname":"外'婆破/PK/的/","age":"4","head_pic":"http://59.110.28.150:888/Uploads/Picture/2016-12-19/20161219225521563.jpg","sex":"1","role":"S","vip":"0","realname":"0","last_login_time":"4天前","distance":"29.36","sign":"志伟篮子志伟篮子志伟篮子志伟篮子","lat":"40.000000","lng":"117.000000","onlinestate":0}]
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
         * uid : 5
         * nickname : 屌
         * age : 3
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2016-12-19/20161219224411226.jpg
         * sex : 1
         * role : S
         * vip : 1
         * realname : 0
         * last_login_time : 46分钟前
         * distance : 0.03
         * sign : 志伟篮子志伟篮子志伟篮子
         * lat : 39.856144
         * lng : 116.712029
         * onlinestate : 0
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
        private String distance;
        private String sign;
        private String lat;
        private String lng;
        private int onlinestate;
        private String introduce;
        private String visit_time;
        private String is_admin;
        private String is_hand;
        private String vipannual;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String charm_val;
        private String wealth_val;
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

        public String getCharm_val() {
            return charm_val;
        }

        public void setCharm_val(String charm_val) {
            this.charm_val = charm_val;
        }

        public String getWealth_val() {
            return wealth_val;
        }

        public void setWealth_val(String wealth_val) {
            this.wealth_val = wealth_val;
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

        public String getVisit_time() {
            return visit_time;
        }

        public void setVisit_time(String visit_time) {
            this.visit_time = visit_time;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
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

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public int getOnlinestate() {
            return onlinestate;
        }

        public void setOnlinestate(int onlinestate) {
            this.onlinestate = onlinestate;
        }
    }
}
