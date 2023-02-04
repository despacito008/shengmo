package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/9/12.
 */

public class FenzuListData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"564","nickname":"深浅。","age":"21","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-05-27/20170527113901611.jpg","sex":"2","role":"M","vip":"0","vipannual":"0","realname":"0","last_login_time":"1505179994","distance":"0.00","province":"北京市","city":"北京市","lat":"39.871319","lng":"116.689484","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","onlinestate":1},{"uid":"12","nickname":"小清新","age":"25","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-16/20170616143502250.jpg","sex":"1","role":"S","vip":"1","vipannual":"0","realname":"1","last_login_time":"1505178353","distance":"0.01","province":"北京市","city":"北京市","lat":"39.871338","lng":"116.689354","is_admin":"1","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"8","wealth_val":"0","onlinestate":1},{"uid":"175","nickname":"深藏功与名.3","age":"24","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-05-19/20170519175817473.jpg","sex":"1","role":"~","vip":"1","vipannual":"1","realname":"1","last_login_time":"1505178509","distance":"0.02","province":"北京市","city":"北京市","lat":"39.871426","lng":"116.689240","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"6","wealth_val":"0","onlinestate":1},{"uid":"2604","nickname":"CD主","age":"28","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-04-25/20170425180808736.jpg","sex":"3","role":"S","vip":"0","vipannual":"0","realname":"0","last_login_time":"1505113831","distance":"0.02","province":"北京市","city":"北京市","lat":"39.871422","lng":"116.689339","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","onlinestate":1},{"uid":"13","nickname":"斯慕群组管理员","age":"29","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-06/20170606230045882.jpg","sex":"1","role":"S","vip":"1","vipannual":"0","realname":"1","last_login_time":"1504923658","distance":"0.03","province":"北京市","city":"北京市","lat":"39.871391","lng":"116.689156","is_admin":"1","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","onlinestate":1},{"uid":"25882","nickname":"爱好者157","age":"25","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-05-19/20170519210756572.jpg","sex":"1","role":"~","vip":"0","vipannual":"0","realname":"0","last_login_time":"1505176125","distance":"0.03","province":"北京市","city":"北京市","lat":"39.871410","lng":"116.689140","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","onlinestate":1},{"uid":"35240","nickname":"斯慕客服小魔","age":"27","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-28/20170628122832627.jpg","sex":"2","role":"~","vip":"1","vipannual":"1","realname":"1","last_login_time":"1505121294","distance":"0.03","province":"北京市","city":"北京市","lat":"39.871437","lng":"116.689163","is_admin":"1","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"3","wealth_val":"42","onlinestate":1}]
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
         * uid : 564
         * nickname : 深浅。
         * age : 21
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-05-27/20170527113901611.jpg
         * sex : 2
         * role : M
         * vip : 0
         * vipannual : 0
         * realname : 0
         * last_login_time : 1505179994
         * distance : 0.00
         * province : 北京市
         * city : 北京市
         * lat : 39.871319
         * lng : 116.689484
         * is_admin : 0
         * is_hand : 0
         * is_volunteer : 0
         * svip : 0
         * svipannual : 0
         * charm_val : 0
         * wealth_val : 0
         * onlinestate : 1
         */

        private String uid;
        private String nickname;
        private String age;
        private String head_pic;
        private String sex;
        private String role;
        private String vip;
        private String vipannual;
        private String realname;
        private String last_login_time;
        private String distance;
        private String province;
        private String city;
        private String lat;
        private String lng;
        private String is_admin;
        private String is_hand;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String charm_val_new;
        private String wealth_val_new;
        private int onlinestate;
        private String location_switch;
        private String login_time_switch;
        private String location_city_switch;
        private String bkvip;
        private String blvip;
        private String lmarkname;
        private String state;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getLmarkname() {
            return lmarkname;
        }

        public void setLmarkname(String lmarkname) {
            this.lmarkname = lmarkname;
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

        public String getVipannual() {
            return vipannual;
        }

        public void setVipannual(String vipannual) {
            this.vipannual = vipannual;
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

        public int getOnlinestate() {
            return onlinestate;
        }

        public void setOnlinestate(int onlinestate) {
            this.onlinestate = onlinestate;
        }
    }
}
