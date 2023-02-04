package com.aiwujie.shengmo.bean;

import java.util.ArrayList;

/**
 * Created by 290243232 on 2016/12/28.
 */
public class UserInfoData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"uid":"12","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-16/20170616143502250.jpg","media":"http://hao.shengmo.org:888/Uploads/Media/2017-08-08/20170808163448991.wav","nickname":"小清新","realname":"1","sex":"1","age":"25","starchar":"双鱼座","tall":"180cm","weight":"71kg","role":"S","sexual":"女","along":"1年以下","experience":"无","level":"轻度","want":"聊天,现实,结婚","culture":"本科","monthly":"1万-2万","introduce":"Android 技术人员。😍😍😍","vip":"1","vipannual":"0","fans_num":"39","follow_num":"26","group_num":"8","photo":["http://hao.shengmo.org:888/Uploads/Picture/2017-04-07/20170407175934104.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-04-13/20170413174706130.jpg"],"photo_lock":"1","lat":"39.871429","lng":"116.689194","city":"北京市","province":"北京市","last_login_time":"3分钟前","login_time_switch":"0","is_admin":"1","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","dvisit_num":"30","visit_num":"18941","charm_val":"8","wealth_val":"0","mediaalong":"7","onlinestate":1,"timePoorState":1,"distance":"0km","follow_state":2,"black_state":0,"reg_time":"11-24","dynamic_num":"0"}
     */

    private int retcode;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 12
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-06-16/20170616143502250.jpg
         * media : http://hao.shengmo.org:888/Uploads/Media/2017-08-08/20170808163448991.wav
         * nickname : 小清新
         * realname : 1
         * sex : 1
         * age : 25
         * starchar : 双鱼座
         * tall : 180cm
         * weight : 71kg
         * role : S
         * sexual : 女
         * along : 1年以下
         * experience : 无
         * level : 轻度
         * want : 聊天,现实,结婚
         * culture : 本科
         * monthly : 1万-2万
         * introduce : Android 技术人员。😍😍😍
         * vip : 1
         * vipannual : 0
         * fans_num : 39
         * follow_num : 26
         * group_num : 8
         * photo : ["http://hao.shengmo.org:888/Uploads/Picture/2017-04-07/20170407175934104.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-04-13/20170413174706130.jpg"]
         * photo_lock : 1
         * lat : 39.871429
         * lng : 116.689194
         * city : 北京市
         * province : 北京市
         * last_login_time : 3分钟前
         * login_time_switch : 0
         * is_admin : 1
         * is_hand : 0
         * is_volunteer : 0
         * svip : 0
         * svipannual : 0
         * dvisit_num : 30
         * visit_num : 18941
         * charm_val : 8
         * wealth_val : 0
         * mediaalong : 7
         * onlinestate : 1
         * timePoorState : 1
         * distance : 0km
         * follow_state : 2
         * black_state : 0
         * reg_time : 11-24
         * dynamic_num : 0
         */

        private String uid;
        private String head_pic;
        private String media;
        private String nickname;
        private String realname;
        private String sex;
        private String age;
        private String starchar;
        private String tall;
        private String weight;
        private String role;
        private String sexual;
        private String along;
        private String experience;
        private String level;
        private String want;
        private String culture;
        private String monthly;
        private String introduce;
        private String vip;
        private String vipannual;
        private String fans_num;
        private String follow_num;
        private String group_num;
        private String photo_lock;
        private String lat;
        private String lng;
        private String city;
        private String province;
        private String last_login_time;
        private String login_time_switch;
        private String is_admin;
        private String is_hand;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String dvisit_num;
        private String visit_num;
        private String charm_val_new;
        private String wealth_val_new;
        private String mediaalong;
        private int onlinestate;
        private int timePoorState;
        private String distance;
        private String follow_state;
        private String black_state;
        private String reg_time;
        private String dynamic_num;
        private String comment_num;
        private ArrayList<String> photo;
        private ArrayList<String> sypic;
        private String is_likeliar;
        private String char_rule;
        private String realids;
        private String nickname_rule;
        private String friend_quiet;
        private String bkvip;
        private String blvip;
        private String join_group_status;  //0是可以 1是不可以  加群
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

        public String getFriend_quiet() {
            return friend_quiet;
        }

        public void setFriend_quiet(String friend_quiet) {
            this.friend_quiet = friend_quiet;
        }

        public String getNickname_rule() {
            return nickname_rule;
        }

        public void setNickname_rule(String nickname_rule) {
            this.nickname_rule = nickname_rule;
        }

        public String getRealids() {
            return realids;
        }

        public void setRealids(String realids) {
            this.realids = realids;
        }

        public String getChar_rule() {
            return char_rule;
        }

        public void setChar_rule(String char_rule) {
            this.char_rule = char_rule;
        }

        public String getPhoto_rule() {
            return photo_rule;
        }

        public void setPhoto_rule(String photo_rule) {
            this.photo_rule = photo_rule;
        }

        public String getDynamic_rule() {
            return dynamic_rule;
        }

        public void setDynamic_rule(String dynamic_rule) {
            this.dynamic_rule = dynamic_rule;
        }

        public String getComment_rule() {
            return comment_rule;
        }

        public void setComment_rule(String comment_rule) {
            this.comment_rule = comment_rule;
        }

        public ArrayList<String> getSypic() {
            return sypic;
        }

        public void setSypic(ArrayList<String> sypic) {
            this.sypic = sypic;
        }

        //新添加三个字段  判断 照片动态评论是否可以看
        private String photo_rule;
        private String dynamic_rule;
        private String comment_rule;
        private String admin_mark;



        //认证照（1：vip可见，0：不可见）
        private String realpicstate;
        private String markname;
        private String lmarkname;
        private String location_city_switch;

        public String getLocation_city_switch() {
            return location_city_switch;
        }

        public void setLocation_city_switch(String location_city_switch) {
            this.location_city_switch = location_city_switch;
        }

        public String getMarkname() {
            return markname;
        }

        public String getLmarkname() {
            return lmarkname;
        }

        public void setLmarkname(String lmarkname) {
            this.lmarkname = lmarkname;
        }

        public void setMarkname(String markname) {
            this.markname = markname;
        }

        public String getAdmin_mark() {
            return admin_mark;
        }

        public void setAdmin_mark(String admin_mark) {
            this.admin_mark = admin_mark;
        }

        public String getRealpicstate() {
            return realpicstate;
        }

        public void setRealpicstate(String realpicstate) {
            this.realpicstate = realpicstate;
        }

        public String getIs_likeliar() {
            return is_likeliar;
        }

        public void setIs_likeliar(String is_likeliar) {
            this.is_likeliar = is_likeliar;
        }

        public String getComment_num() {
            return comment_num;
        }

        public void setComment_num(String comment_num) {
            this.comment_num = comment_num;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getMedia() {
            return media;
        }

        public void setMedia(String media) {
            this.media = media;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getStarchar() {
            return starchar;
        }

        public void setStarchar(String starchar) {
            this.starchar = starchar;
        }

        public String getTall() {
            return tall;
        }

        public void setTall(String tall) {
            this.tall = tall;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getSexual() {
            return sexual;
        }

        public void setSexual(String sexual) {
            this.sexual = sexual;
        }

        public String getAlong() {
            return along;
        }

        public void setAlong(String along) {
            this.along = along;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getWant() {
            return want;
        }

        public void setWant(String want) {
            this.want = want;
        }

        public String getCulture() {
            return culture;
        }

        public void setCulture(String culture) {
            this.culture = culture;
        }

        public String getMonthly() {
            return monthly;
        }

        public void setMonthly(String monthly) {
            this.monthly = monthly;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
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

        public String getFans_num() {
            return fans_num;
        }

        public void setFans_num(String fans_num) {
            this.fans_num = fans_num;
        }

        public String getFollow_num() {
            return follow_num;
        }

        public void setFollow_num(String follow_num) {
            this.follow_num = follow_num;
        }

        public String getGroup_num() {
            return group_num;
        }

        public void setGroup_num(String group_num) {
            this.group_num = group_num;
        }

        public String getPhoto_lock() {
            return photo_lock;
        }

        public void setPhoto_lock(String photo_lock) {
            this.photo_lock = photo_lock;
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

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
        }

        public String getLogin_time_switch() {
            return login_time_switch;
        }

        public void setLogin_time_switch(String login_time_switch) {
            this.login_time_switch = login_time_switch;
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

        public String getDvisit_num() {
            return dvisit_num;
        }

        public void setDvisit_num(String dvisit_num) {
            this.dvisit_num = dvisit_num;
        }

        public String getVisit_num() {
            return visit_num;
        }

        public void setVisit_num(String visit_num) {
            this.visit_num = visit_num;
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

        public int getTimePoorState() {
            return timePoorState;
        }

        public void setTimePoorState(int timePoorState) {
            this.timePoorState = timePoorState;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getFollow_state() {
            return follow_state;
        }

        public void setFollow_state(String follow_state) {
            this.follow_state = follow_state;
        }

        public String getBlack_state() {
            return black_state;
        }

        public void setBlack_state(String black_state) {
            this.black_state = black_state;
        }

        public String getReg_time() {
            return reg_time;
        }

        public void setReg_time(String reg_time) {
            this.reg_time = reg_time;
        }

        public String getDynamic_num() {
            return dynamic_num;
        }

        public void setDynamic_num(String dynamic_num) {
            this.dynamic_num = dynamic_num;
        }

        public ArrayList<String> getPhoto() {
            return photo;
        }

        public void setPhoto(ArrayList<String> photo) {
            this.photo = photo;
        }

        public String getJoin_group_status() {
            return join_group_status;
        }

        public void setJoin_group_status(String join_group_status) {
            this.join_group_status = join_group_status;
        }
    }
}
