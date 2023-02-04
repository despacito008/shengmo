package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2016/12/23.
 */
public class HomeGridviewData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"1","nickname":"哈哈","age":"28","head_pic":"http://59.110.28.150:888/Uploads/Picture/2016-12-19/2016121920542213.jpg","sex":"1","vip":"0","realname":"0","last_login_time":"1482503873","distance":"0.50","lat":"39.855186","lng":"116.706001","onlinestate":0},{"uid":"5","nickname":"屌","age":"3","head_pic":"http://59.110.28.150:888/Uploads/Picture/2016-12-19/20161219224411226.jpg","sex":"1","vip":"1","realname":"0","last_login_time":"1482499744","distance":"0.03","lat":"39.856293","lng":"116.712029","onlinestate":0}]
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
         * uid : 1
         * nickname : 哈哈
         * age : 28
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2016-12-19/2016121920542213.jpg
         * sex : 1
         * vip : 0
         * realname : 0
         * last_login_time : 1482503873
         * distance : 0.50
         * lat : 39.855186
         * lng : 116.706001
         * onlinestate : 0
         */

        private String uid;
        private String nickname;
        private String age;
        private String head_pic;
        private String sex;
        private String vip;
        private String realname;
        private String last_login_time;
        private String distance;
        private String lat;
        private String lng;
        private int onlinestate;
        private String introduce;
        private String is_admin;
        private String is_hand;
        private String vipannual;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String charm_val_new;
        private String wealth_val_new;
        private String location_switch;
        private String login_time_switch;
        private String location_city_switch;
        private String bkvip;
        private String blvip;
        private String province;
        private String anchor_room_id;
        private String anchor_is_live;

        public String getCharm_val_new() {
            return charm_val_new;
        }

        public void setCharm_val_new(String charm_val_new) {
            this.charm_val_new = charm_val_new;
        }

        public String getWealth_val_new() {
            return wealth_val_new;
        }

        public void setWealth_val_new(String wealth_val_new) {
            this.wealth_val_new = wealth_val_new;
        }

        public String getAnchor_room_id() {
            return anchor_room_id;
        }

        public void setAnchor_room_id(String anchor_room_id) {
            this.anchor_room_id = anchor_room_id;
        }

        public String getAnchor_is_live() {
            return anchor_is_live;
        }

        public void setAnchor_is_live(String anchor_is_live) {
            this.anchor_is_live = anchor_is_live;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        private String city;
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

        public String getLocation_switch() {
            return location_switch;
        }

        public void setLocation_switch(String location_switch) {
            this.location_switch = location_switch;
        }

        public String getLogin_time_switch() {
            return login_time_switch;
        }

        public void setLogin_time_switch(String login_time_switch) {
            this.login_time_switch = login_time_switch;
        }

        public String getLocation_city_switch() {
            return location_city_switch;
        }

        public void setLocation_city_switch(String location_city_switch) {
            this.location_city_switch = location_city_switch;
        }

        public String getCharm_val() {
            return charm_val_new;
        }

        public void setCharm_val(String charm_val) {
            this.charm_val_new = charm_val;
        }

        public String getWealth_val() {
            return wealth_val_new;
        }

        public void setWealth_val(String wealth_val) {
            this.wealth_val_new = wealth_val;
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
