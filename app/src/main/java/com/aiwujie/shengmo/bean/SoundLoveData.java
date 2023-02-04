package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/6/15.
 */

public class SoundLoveData {


    /**
     * retcode : 2001
     * msg : 获取成功！
     * data : [{"uid":"6631","nickname":"s   feel","media":"http://hao.shengmo.org:888/Uploads/Media/2017-09-12/20170912092626349.wav","age":"24","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-04-04/20170404085607187.jpg","sex":"1","role":"S","vip":"0","vipannual":"0","realname":"1","last_login_time":"1505179602","province":"河北省","city":"秦皇岛市","lat":"39.955471","lng":"119.608521","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","media_change_time":"1505179586","mediaalong":"3","onlinestate":1,"update_media_time":"1小时前"},{"uid":"85272","nickname":"摆渡灵魂师","media":"http://hao.shengmo.org:888/Uploads/Media/2017-09-12/20170912084953328.wav","age":"27","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-09-11/20170911095751472.jpg","sex":"1","role":"~","vip":"0","vipannual":"0","realname":"0","last_login_time":"1505185000","province":"湖北省","city":"黄冈市","lat":"30.437290","lng":"114.949211","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","media_change_time":"1505177394","mediaalong":"7","onlinestate":1,"update_media_time":"2小时前"},{"uid":"85588","nickname":"食罂粟长大的猫m","media":"http://hao.shengmo.org:888/Uploads/Media/2017-09-12/20170912033545849.wav","age":"21","head_pic":"http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJb8FpYbLeI2WNv6ns1iaOowuIfv1wCHko2cvrhOgaUlwIfeLlG3Ys2hLNnOjIibe0u9lekY6A2Aolw/0","sex":"2","role":"M","vip":"1","vipannual":"0","realname":"0","last_login_time":"1505159547","province":"天津市","city":"天津市","lat":"39.129738","lng":"117.203697","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","media_change_time":"1505158545","mediaalong":"0","onlinestate":1,"update_media_time":"7小时前"},{"uid":"40173","nickname":"差不多少女","media":"http://hao.shengmo.org:888/Uploads/Media/2017-09-12/20170912022023585.wav","age":"18","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-09-08/20170908004442697.jpg","sex":"2","role":"M","vip":"0","vipannual":"0","realname":"1","last_login_time":"1505185033","province":"陕西省","city":"西安市","lat":"34.298706","lng":"108.946457","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","media_change_time":"1505154023","mediaalong":"7","onlinestate":1,"update_media_time":"8小时前"},{"uid":"69185","nickname":"玺","media":"http://hao.shengmo.org:888/Uploads/Media/2017-09-11/2017091123591195.wav","age":"18","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-09-02/20170902233114513.jpg","sex":"1","role":"~","vip":"0","vipannual":"0","realname":"0","last_login_time":"1505146090","province":"天津市","city":"天津市","lat":"39.264893","lng":"117.206436","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","media_change_time":"1505145552","mediaalong":"10","onlinestate":1,"update_media_time":"10小时前"}]
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
         * uid : 6631
         * nickname : s   feel
         * media : http://hao.shengmo.org:888/Uploads/Media/2017-09-12/20170912092626349.wav
         * age : 24
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-04-04/20170404085607187.jpg
         * sex : 1
         * role : S
         * vip : 0
         * vipannual : 0
         * realname : 1
         * last_login_time : 1505179602
         * province : 河北省
         * city : 秦皇岛市
         * lat : 39.955471
         * lng : 119.608521
         * is_admin : 0
         * is_hand : 0
         * is_volunteer : 0
         * svip : 0
         * svipannual : 0
         * charm_val : 0
         * wealth_val : 0
         * media_change_time : 1505179586
         * mediaalong : 3
         * onlinestate : 1
         * update_media_time : 1小时前
         */

        private String uid;
        private String nickname;
        private String media;
        private String age;
        private String head_pic;
        private String sex;
        private String role;
        private String vip;
        private String vipannual;
        private String realname;
        private String last_login_time;
        private String province;
        private String city;
        private String lat;
        private String lng;
        private String is_admin;
        private String is_hand;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String charm_val;
        private String wealth_val;
        private String media_change_time;
        private String mediaalong;
        private int onlinestate;
        private String update_media_time;
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

        public String getMedia() {
            return media;
        }

        public void setMedia(String media) {
            this.media = media;
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

        public String getMedia_change_time() {
            return media_change_time;
        }

        public void setMedia_change_time(String media_change_time) {
            this.media_change_time = media_change_time;
        }

        public String getMediaalong() {
            return mediaalong;
        }

        public void setMediaalong(String mediaalong) {
            this.mediaalong = mediaalong;
        }

        public int getOnlinestate() {
            return onlinestate;
        }

        public void setOnlinestate(int onlinestate) {
            this.onlinestate = onlinestate;
        }

        public String getUpdate_media_time() {
            return update_media_time;
        }

        public void setUpdate_media_time(String update_media_time) {
            this.update_media_time = update_media_time;
        }
    }
}
