package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: CallUserBean
 * @Author: xmf
 * @CreateDate: 2022/5/26 10:03
 * @Description: 呼唤成员列表
 */
public class CallUserBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"anchor_room_id":"444029","anchor_is_live":"0","uid":"703916","location_switch":"0","location_city_switch":"0","login_time_switch":"0","nickname":"notalone","age":"18","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2022-04-13/20220413111249102.jpg","sex":"1","role":"~","vip":"1","vipannual":"0","video_auth_status":"0","realname":"0","realids":"1","last_login_time":"1653383837","distance":"0km","province":"北京市","city":"北京市","lat":"40.073811","lng":"116.360199","is_admin":"0","is_hidden_admin":"0","is_hand":"0","is_volunteer":"0","svip":"1","svipannual":"0","charm_val":"10149","wealth_val":"111","bkvip":"0","blvip":"0","wealth_val_switch":"0","charm_val_switch":"0","t_online_status":"0","wealth_val_new":"111","charm_val_new":"10149","onlinestate":1,"markname":"","user_level":0,"anchor_level":0}]
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
         * anchor_room_id : 444029
         * anchor_is_live : 0
         * uid : 703916
         * location_switch : 0
         * location_city_switch : 0
         * login_time_switch : 0
         * nickname : notalone
         * age : 18
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2022-04-13/20220413111249102.jpg
         * sex : 1
         * role : ~
         * vip : 1
         * vipannual : 0
         * video_auth_status : 0
         * realname : 0
         * realids : 1
         * last_login_time : 1653383837
         * distance : 0km
         * province : 北京市
         * city : 北京市
         * lat : 40.073811
         * lng : 116.360199
         * is_admin : 0
         * is_hidden_admin : 0
         * is_hand : 0
         * is_volunteer : 0
         * svip : 1
         * svipannual : 0
         * charm_val : 10149
         * wealth_val : 111
         * bkvip : 0
         * blvip : 0
         * wealth_val_switch : 0
         * charm_val_switch : 0
         * t_online_status : 0
         * wealth_val_new : 111
         * charm_val_new : 10149
         * onlinestate : 1
         * markname :
         * user_level : 0
         * anchor_level : 0
         */

        private String anchor_room_id;
        private String anchor_is_live;
        private String uid;
        private String location_switch;
        private String location_city_switch;
        private String login_time_switch;
        private String nickname;
        private String age;
        private String head_pic;
        private String sex;
        private String role;
        private String vip;
        private String vipannual;
        private String video_auth_status;
        private String realname;
        private String realids;
        private String last_login_time;
        private String distance;
        private String province;
        private String city;
        private String lat;
        private String lng;
        private String is_admin;
        private String is_hidden_admin;
        private String is_hand;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String charm_val;
        private String wealth_val;
        private String bkvip;
        private String blvip;
        private String wealth_val_switch;
        private String charm_val_switch;
        private String t_online_status;
        private String wealth_val_new;
        private String charm_val_new;
        private int onlinestate;
        private String markname;
        private int user_level;
        private int anchor_level;
        private String vip_type;

        public String getVip_type() {
            return vip_type;
        }

        public void setVip_type(String vip_type) {
            this.vip_type = vip_type;
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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getLocation_switch() {
            return location_switch;
        }

        public void setLocation_switch(String location_switch) {
            this.location_switch = location_switch;
        }

        public String getLocation_city_switch() {
            return location_city_switch;
        }

        public void setLocation_city_switch(String location_city_switch) {
            this.location_city_switch = location_city_switch;
        }

        public String getLogin_time_switch() {
            return login_time_switch;
        }

        public void setLogin_time_switch(String login_time_switch) {
            this.login_time_switch = login_time_switch;
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

        public String getVideo_auth_status() {
            return video_auth_status;
        }

        public void setVideo_auth_status(String video_auth_status) {
            this.video_auth_status = video_auth_status;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getRealids() {
            return realids;
        }

        public void setRealids(String realids) {
            this.realids = realids;
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

        public String getIs_hidden_admin() {
            return is_hidden_admin;
        }

        public void setIs_hidden_admin(String is_hidden_admin) {
            this.is_hidden_admin = is_hidden_admin;
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

        public String getWealth_val_switch() {
            return wealth_val_switch;
        }

        public void setWealth_val_switch(String wealth_val_switch) {
            this.wealth_val_switch = wealth_val_switch;
        }

        public String getCharm_val_switch() {
            return charm_val_switch;
        }

        public void setCharm_val_switch(String charm_val_switch) {
            this.charm_val_switch = charm_val_switch;
        }

        public String getT_online_status() {
            return t_online_status;
        }

        public void setT_online_status(String t_online_status) {
            this.t_online_status = t_online_status;
        }

        public String getWealth_val_new() {
            return wealth_val_new;
        }

        public void setWealth_val_new(String wealth_val_new) {
            this.wealth_val_new = wealth_val_new;
        }

        public String getCharm_val_new() {
            return charm_val_new;
        }

        public void setCharm_val_new(String charm_val_new) {
            this.charm_val_new = charm_val_new;
        }

        public int getOnlinestate() {
            return onlinestate;
        }

        public void setOnlinestate(int onlinestate) {
            this.onlinestate = onlinestate;
        }

        public String getMarkname() {
            return markname;
        }

        public void setMarkname(String markname) {
            this.markname = markname;
        }

        public int getUser_level() {
            return user_level;
        }

        public void setUser_level(int user_level) {
            this.user_level = user_level;
        }

        public int getAnchor_level() {
            return anchor_level;
        }

        public void setAnchor_level(int anchor_level) {
            this.anchor_level = anchor_level;
        }
    }
}
