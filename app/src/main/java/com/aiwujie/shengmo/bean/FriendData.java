package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/8.
 */
public class FriendData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"10","state":"1","userInfo":{"uid":"10","nickname":"强哥","age":"28","head_pic":"http://59.110.28.150:888/Uploads/Picture/2016-12-24/20161224213545897.jpg","sex":"1","role":"SM","vip":"0","realname":"0","last_login_time":"10分钟前","distance":"26.67","introduce":"本MVP图图找我哦哦哦哦哦哦来咯人来咯摸摸摸了旅途快乐了来咯摸摸摸了可口可乐了结局了可口可乐了可口可乐了吗涂抹OK了路兔兔","lat":"39.855137","lng":"116.706062","onlinestate":1}}]
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
         * uid : 10
         * state : 1
         * userInfo : {"uid":"10","nickname":"强哥","age":"28","head_pic":"http://59.110.28.150:888/Uploads/Picture/2016-12-24/20161224213545897.jpg","sex":"1","role":"SM","vip":"0","realname":"0","last_login_time":"10分钟前","distance":"26.67","introduce":"本MVP图图找我哦哦哦哦哦哦来咯人来咯摸摸摸了旅途快乐了来咯摸摸摸了可口可乐了结局了可口可乐了可口可乐了吗涂抹OK了路兔兔","lat":"39.855137","lng":"116.706062","onlinestate":1}
         */

        private String uid;
        private String state;
        private UserInfoBean userInfo;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public static class UserInfoBean {
            /**
             * uid : 10
             * nickname : 强哥
             * age : 28
             * head_pic : http://59.110.28.150:888/Uploads/Picture/2016-12-24/20161224213545897.jpg
             * sex : 1
             * role : SM
             * vip : 0
             * realname : 0
             * last_login_time : 10分钟前
             * distance : 26.67
             * introduce : 本MVP图图找我哦哦哦哦哦哦来咯人来咯摸摸摸了旅途快乐了来咯摸摸摸了可口可乐了结局了可口可乐了可口可乐了吗涂抹OK了路兔兔
             * lat : 39.855137
             * lng : 116.706062
             * onlinestate : 1
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
            private String introduce;
            private String lat;
            private String lng;
            private int onlinestate;
            private String is_admin;
            private String is_hand;
            private String vipannual;
            private String is_volunteer;
            private String svip;
            private String svipannual;
            private String charm_val_new;
            private String wealth_val_new;
            private String markname;
            private String bkvip;
            private String blvip;
            private String location_city_switch;
            private String city;
            private String province;
            private String lmarkname;

            public String getLmarkname() {
                return lmarkname;
            }

            public void setLmarkname(String lmarkname) {
                this.lmarkname = lmarkname;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getLocation_city_switch() {
                return location_city_switch;
            }

            public void setLocation_city_switch(String location_city_switch) {
                this.location_city_switch = location_city_switch;
            }

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

            public String getMarkname() {
                return markname;
            }

            public void setMarkname(String markname) {
                this.markname = markname;
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

            public String getIntroduce() {
                return introduce;
            }

            public void setIntroduce(String introduce) {
                this.introduce = introduce;
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
}
