package com.aiwujie.shengmo.bean;

import java.util.ArrayList;
import java.util.List;

public class UserInfoBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"realids":"0","location_switch":"0","location_city_switch":"0","uid":"623754","photo_rule":"0","dynamic_rule":"0","comment_rule":"0","char_rule":"0","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-04-26/20210426140702509.jpg","mediastate":"0","media":"","nickname":"iiccer","realname":"0","sex":"1","age":"27","starchar":"摩羯座","tall":"175cm","weight":"60kg","role":"~","sexual":"CDTS","along":"1年以下","experience":"无","level":"重度","want":"结婚","culture":"大专","monthly":"1万-2万","introduce":"uwuwuw","vip":"1","vipannual":"1","fans_num":"3","follow_num":"1","group_num":"2","photo":["http://image.aiwujie.com.cn/Uploads/Picture/2021-04-14/20210414104852767.jpg","http://image.aiwujie.com.cn/Uploads/Picture/2021-04-14/20210414104846574.jpg"],"photo_lock":"1","lat":"40.073818","lng":"116.360260","city":"北京市","province":"北京市","last_login_time":"刚刚","login_time_switch":"0","is_admin":"0","is_hidden_admin":"0","is_hand":"0","is_volunteer":"0","svip":"1","svipannual":"0","dvisit_num":"0","visit_num":"0","charm_val":"1254613","wealth_val":"20041","wealth_val_switch":"0","charm_val_switch":"0","mediaalong":"0","is_likeliar":"0","nickname_rule":"0","bkvip":"0","blvip":"0","attribute":"","join_group_status":"0","wealth_val_new":"20041","charm_val_new":"1254613","admin_mark":"","sypic":[],"onlinestate":1,"timePoorState":1,"distance":"0km","follow_state":2,"black_state":0,"reg_time":"2021-04-13","dynamic_num":"8","comment_num":"10","realpicstate":0,"markname":"","lmarkname":"","friend_quiet":0,"fgroups":""}
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
         * realids : 0
         * location_switch : 0
         * location_city_switch : 0
         * uid : 623754
         * photo_rule : 0
         * dynamic_rule : 0
         * comment_rule : 0
         * char_rule : 0
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-04-26/20210426140702509.jpg
         * mediastate : 0
         * media :
         * nickname : iiccer
         * realname : 0
         * sex : 1
         * age : 27
         * starchar : 摩羯座
         * tall : 175cm
         * weight : 60kg
         * role : ~
         * sexual : CDTS
         * along : 1年以下
         * experience : 无
         * level : 重度
         * want : 结婚
         * culture : 大专
         * monthly : 1万-2万
         * introduce : uwuwuw
         * vip : 1
         * vipannual : 1
         * fans_num : 3
         * follow_num : 1
         * group_num : 2
         * photo : ["http://image.aiwujie.com.cn/Uploads/Picture/2021-04-14/20210414104852767.jpg","http://image.aiwujie.com.cn/Uploads/Picture/2021-04-14/20210414104846574.jpg"]
         * photo_lock : 1
         * lat : 40.073818
         * lng : 116.360260
         * city : 北京市
         * province : 北京市
         * last_login_time : 刚刚
         * login_time_switch : 0
         * is_admin : 0
         * is_hidden_admin : 0
         * is_hand : 0
         * is_volunteer : 0
         * svip : 1
         * svipannual : 0
         * dvisit_num : 0
         * visit_num : 0
         * charm_val : 1254613
         * wealth_val : 20041
         * wealth_val_switch : 0
         * charm_val_switch : 0
         * mediaalong : 0
         * is_likeliar : 0
         * nickname_rule : 0
         * bkvip : 0
         * blvip : 0
         * attribute :
         * join_group_status : 0
         * wealth_val_new : 20041
         * charm_val_new : 1254613
         * admin_mark :
         * sypic : []
         * onlinestate : 1
         * timePoorState : 1
         * distance : 0km
         * follow_state : 2
         * black_state : 0
         * reg_time : 2021-04-13
         * dynamic_num : 8
         * comment_num : 10
         * realpicstate : 0
         * markname :
         * lmarkname :
         * friend_quiet : 0
         * fgroups :
         */

        private String realids;
        private String location_switch;
        private String location_city_switch;
        private String uid;
        private String photo_rule;
        private String dynamic_rule;
        private String comment_rule;
        private String char_rule;
        private String head_pic;
        private String mediastate;
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
        private String is_hidden_admin;
        private String is_hand;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String dvisit_num;
        private String visit_num;
        private String charm_val;
        private String wealth_val;
        private String wealth_val_switch;
        private String charm_val_switch;
        private String mediaalong;
        private String is_likeliar;
        private String nickname_rule;
        private String bkvip;
        private String blvip;
        private String attribute;
        private String join_group_status;
        private String wealth_val_new;
        private String charm_val_new;
        private String admin_mark;
        private int onlinestate;
        private int timePoorState;
        private String distance;
        private int follow_state;
        private int black_state;
        private String reg_time;
        private String dynamic_num;
        private String comment_num;
        private int realpicstate;
        private String markname;
        private String lmarkname;
        private String friend_quiet;
        private String fgroups;
        private ArrayList<String> photo;
        private ArrayList<String> sypic;
        private ArrayList<String> lsypic;
        private String black_val;
        private String video_auth_status;
        private String follow_list_switch;
        private String likeliar_info;
        private String fans_list_switch;
        private String anchor_room_id;
        private String anchor_is_live;
        private String live_status;
        private String live_prohibition;
        private String live_rule;
        private String user_level;
        private String anchor_level;
        private String is_record;
        private String live_record_times;
        private String role_string;

        public String getIs_record() {
            return is_record;
        }

        public void setIs_record(String is_record) {
            this.is_record = is_record;
        }

        public String getLive_record_times() {
            return live_record_times;
        }

        public void setLive_record_times(String live_record_times) {
            this.live_record_times = live_record_times;
        }


        public String getRole_string() {
            return role_string;
        }

        public void setRole_string(String role_string) {
            this.role_string = role_string;
        }


        public String getUser_level() {
            return user_level;
        }

        public void setUser_level(String user_level) {
            this.user_level = user_level;
        }

        public String getAnchor_level() {
            return anchor_level;
        }

        public void setAnchor_level(String anchor_level) {
            this.anchor_level = anchor_level;
        }

        public String getLive_rule() {
            return live_rule;
        }

        public void setLive_rule(String live_rule) {
            this.live_rule = live_rule;
        }

        public String getLive_prohibition() {
            return live_prohibition;
        }

        public void setLive_prohibition(String live_prohibition) {
            this.live_prohibition = live_prohibition;
        }

        public String getLive_status() {
            return live_status;
        }

        public void setLive_status(String live_status) {
            this.live_status = live_status;
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

        public String getFans_list_switch() {
            return fans_list_switch;
        }

        public void setFans_list_switch(String fans_list_switch) {
            this.fans_list_switch = fans_list_switch;
        }

        public String getLikeliar_info() {
            return likeliar_info;
        }

        public void setLikeliar_info(String likeliar_info) {
            this.likeliar_info = likeliar_info;
        }

        public String getFollow_list_switch() {
            return follow_list_switch;
        }

        public void setFollow_list_switch(String follow_list_switch) {
            this.follow_list_switch = follow_list_switch;
        }

        public String getVideo_auth_status() {
            return video_auth_status;
        }

        public void setVideo_auth_status(String video_auth_status) {
            this.video_auth_status = video_auth_status;
        }

        public ArrayList<String> getLsypic() {
            return lsypic;
        }

        public void setLsypic(ArrayList<String> lsypic) {
            this.lsypic = lsypic;
        }

        public String getBlack_val() {
            return black_val;
        }

        public void setBlack_val(String black_val) {
            this.black_val = black_val;
        }

        public String getRealids() {
            return realids;
        }

        public void setRealids(String realids) {
            this.realids = realids;
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

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getChar_rule() {
            return char_rule;
        }

        public void setChar_rule(String char_rule) {
            this.char_rule = char_rule;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getMediastate() {
            return mediastate;
        }

        public void setMediastate(String mediastate) {
            this.mediastate = mediastate;
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

        public String getMediaalong() {
            return mediaalong;
        }

        public void setMediaalong(String mediaalong) {
            this.mediaalong = mediaalong;
        }

        public String getIs_likeliar() {
            return is_likeliar;
        }

        public void setIs_likeliar(String is_likeliar) {
            this.is_likeliar = is_likeliar;
        }

        public String getNickname_rule() {
            return nickname_rule;
        }

        public void setNickname_rule(String nickname_rule) {
            this.nickname_rule = nickname_rule;
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

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public String getJoin_group_status() {
            return join_group_status;
        }

        public void setJoin_group_status(String join_group_status) {
            this.join_group_status = join_group_status;
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

        public String getAdmin_mark() {
            return admin_mark;
        }

        public void setAdmin_mark(String admin_mark) {
            this.admin_mark = admin_mark;
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

        public int getFollow_state() {
            return follow_state;
        }

        public void setFollow_state(int follow_state) {
            this.follow_state = follow_state;
        }

        public int getBlack_state() {
            return black_state;
        }

        public void setBlack_state(int black_state) {
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

        public String getComment_num() {
            return comment_num;
        }

        public void setComment_num(String comment_num) {
            this.comment_num = comment_num;
        }

        public int getRealpicstate() {
            return realpicstate;
        }

        public void setRealpicstate(int realpicstate) {
            this.realpicstate = realpicstate;
        }

        public String getMarkname() {
            return markname;
        }

        public void setMarkname(String markname) {
            this.markname = markname;
        }

        public String getLmarkname() {
            return lmarkname;
        }

        public void setLmarkname(String lmarkname) {
            this.lmarkname = lmarkname;
        }

        public String getFriend_quiet() {
            return friend_quiet;
        }

        public void setFriend_quiet(String friend_quiet) {
            this.friend_quiet = friend_quiet;
        }

        public String getFgroups() {
            return fgroups;
        }

        public void setFgroups(String fgroups) {
            this.fgroups = fgroups;
        }

        public List<String> getPhoto() {
            return photo;
        }

        public void setPhoto(ArrayList<String> photo) {
            this.photo = photo;
        }

        public ArrayList<String> getSypic() {
            return sypic;
        }

        public void setSypic(ArrayList<String> sypic) {
            this.sypic = sypic;
        }
    }
}
