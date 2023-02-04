package com.aiwujie.shengmo.timlive.bean;

import java.io.Serializable;
import java.util.List;

public class ScenesRoomInfo implements Serializable {
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
        public int memberCount;
        public String roomName;
        public String roomId;
        public String anchorName;
        public String coverUrl;
        public String anchorId;
        public String vLevelUrl;
        public String roomType;


        public static class DataBean implements Serializable {
            /**
             *              "uid":"623776",                //类型：String  必有字段  备注：无
             *             "location_switch":"0",                //类型：String  必有字段  备注：无
             *             "location_city_switch":"0",                //类型：String  必有字段  备注：无
             *             "login_time_switch":"0",                //类型：String  必有字段  备注：无
             *             "age":"26",                //类型：String  必有字段  备注：无
             *             "head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-06-07/2021060716395041.jpg",                //类型：String  必有字段  备注：无
             *             "sex":"1",                //类型：String  必有字段  备注：无
             *             "vip":"1",                //类型：String  必有字段  备注：无
             *             "video_auth_status":"0",                //类型：String  必有字段  备注：无
             *             "realname":"0",                //类型：String  必有字段  备注：无
             *             "realids":"0",                //类型：String  必有字段  备注：无
             *             "recommend":"0",                //类型：String  必有字段  备注：无
             *             "vipannual":"0",                //类型：String  必有字段  备注：无
             *             "last_login_time":"隐身-",                //类型：String  必有字段  备注：无
             *             "province":"北京市",                //类型：String  必有字段  备注：无
             *             "city":"北京市",                //类型：String  必有字段  备注：无
             *             "lat":"40.073811",                //类型：String  必有字段  备注：无
             *             "lng":"116.360275",                //类型：String  必有字段  备注：无
             *             "is_admin":"1",                //类型：String  必有字段  备注：无
             *             "is_hidden_admin":"0",                //类型：String  必有字段  备注：无
             *             "is_hand":"0",                //类型：String  必有字段  备注：无
             *             "is_volunteer":"0",                //类型：String  必有字段  备注：无
             *             "svip":"0",                //类型：String  必有字段  备注：无
             *             "svipannual":"0",                //类型：String  必有字段  备注：无
             *             "charm_val":"702",                //类型：String  必有字段  备注：无
             *             "wealth_val":"9990",                //类型：String  必有字段  备注：无
             *             "bkvip":"0",                //类型：String  必有字段  备注：无
             *             "blvip":"0",                //类型：String  必有字段  备注：无
             *             "wealth_val_switch":"0",                //类型：String  必有字段  备注：无
             *             "charm_val_switch":"0",                //类型：String  必有字段  备注：无
             *             "t_online_status":"0",                //类型：String  必有字段  备注：无
             *             "is_live":"0",                //类型：String  必有字段  备注：无
             *             "room_id":"10002",                //类型：String  必有字段  备注：无
             *             "pullAddress":"http://livepull.aiwujie.com.cn/live/10002.flv",                //类型：String  必有字段  备注：无
             *             "wealth_val_new":"9990",                //类型：String  必有字段  备注：无
             *             "charm_val_new":"702",                //类型：String  必有字段  备注：无
             *             "onlinestate":0,                //类型：Number  必有字段  备注：无
             *             "distance":"12216.16km",                //类型：String  必有字段  备注：无
             *             "markname":"mock"                //类型：String  必有字段  备注：无
             */

            private String uid;
            private String markname;
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
            private String recommend;
            private String is_hidden_admin;
            private String is_hand;
            private String is_live;
            private String is_volunteer;
            private String svip;
            private String svipannual;
            private String watch_num;
            private String watchsum;
            private String live_title;
            private String live_poster;
            private String room_id;
            private String charm_val_new;
            private String wealth_val_new;
            private int onlinestate;
            private String lastonlinetime;
            private String location_switch;
            private String login_time_switch;
            private String location_city_switch;
            private String charm_val_switch;
            private String wealth_val_switch;
            private String bkvip;
            private String blvip;
            private String realids ;
            private String video_auth_status ;
            private String pullAddress;
            private String t_online_status;
            private String follow_state;

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

            public String getWatch_num() {
                return watch_num;
            }

            public void setWatch_num(String watch_num) {
                this.watch_num = watch_num;
            }

            public String getWatchsum() {
                return watchsum;
            }

            public void setWatchsum(String watchsum) {
                this.watchsum = watchsum;
            }

            public String getLive_title() {
                return live_title;
            }

            public void setLive_title(String live_title) {
                this.live_title = live_title;
            }

            public String getLive_poster() {
                return live_poster;
            }

            public void setLive_poster(String live_poster) {
                this.live_poster = live_poster;
            }

            public String getRoom_id() {
                return room_id;
            }

            public void setRoom_id(String room_id) {
                this.room_id = room_id;
            }


            public String getMarkname() {
                return markname;
            }

            public void setMarkname(String markname) {
                this.markname = markname;
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

            public String getRecommend() {
                return recommend;
            }

            public void setRecommend(String recommend) {
                this.recommend = recommend;
            }


            public String getIs_hand() {
                return is_hand;
            }

            public void setIs_hand(String is_hand) {
                this.is_hand = is_hand;
            }

            public String getIs_live() {
                return is_live;
            }

            public void setIs_live(String is_live) {
                this.is_live = is_live;
            }

            public String getIs_volunteer() {
                return is_volunteer;
            }

            public void setIs_volunteer(String is_volunteer) {
                this.is_volunteer = is_volunteer;
            }

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

            public int getOnlinestate() {
                return onlinestate;
            }

            public void setOnlinestate(int onlinestate) {
                this.onlinestate = onlinestate;
            }

            public String getLastonlinetime() {
                return lastonlinetime;
            }

            public void setLastonlinetime(String lastonlinetime) {
                this.lastonlinetime = lastonlinetime;
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

            public String getCharm_val_switch() {
                return charm_val_switch;
            }

            public void setCharm_val_switch(String charm_val_switch) {
                this.charm_val_switch = charm_val_switch;
            }

            public String getWealth_val_switch() {
                return wealth_val_switch;
            }

            public void setWealth_val_switch(String wealth_val_switch) {
                this.wealth_val_switch = wealth_val_switch;
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

            public String getRealids() {
                return realids;
            }

            public void setRealids(String realids) {
                this.realids = realids;
            }

            public String getVideo_auth_status() {
                return video_auth_status;
            }

            public void setVideo_auth_status(String video_auth_status) {
                this.video_auth_status = video_auth_status;
            }

            public String getPullAddress() {
                return pullAddress;
            }

            public void setPullAddress(String pullAddress) {
                this.pullAddress = pullAddress;
            }

            public String getT_online_status() {
                return t_online_status;
            }

            public void setT_online_status(String t_online_status) {
                this.t_online_status = t_online_status;
            }

            public String getFollow_state() {
                return follow_state;
            }

            public void setFollow_state(String follow_state) {
                this.follow_state = follow_state;
            }
        }
}