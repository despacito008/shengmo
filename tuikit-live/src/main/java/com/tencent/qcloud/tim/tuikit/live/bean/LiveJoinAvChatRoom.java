package com.tencent.qcloud.tim.tuikit.live.bean;

import java.util.List;

public class LiveJoinAvChatRoom {

    /**
     * costomMassageType : joinAvChatRoom
     * uid : 402583
     * level : 1
     * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg
     * is_admin : 1
     * setNotalking : 0
     * is_group_admin : 1
     * time : 1639037296
     * anchor_info : {"live_time":"1639036675","uid":"250385","nickname":"鸿雁传书12","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg","sex":"1","role":"~","age":"30","beans_current_count":"6","vip":"1","vipannual":"0","is_admin":"0","is_hidden_admin":"0","svip":"1","svipannual":"0","is_interaction":"0","anchor_status":"2","camera_switch":"0","live_poster":"http://image.aiwujie.com.cn/Uploads/Picture/2021-12-06/20211206174402459.jpg","live_title":"这里是标题\n我换一个行","hour_ranking":1,"follow_state":3}
     * showType : 0
     * wealth_val : 100005
     * wealth_val_switch : 0
     * anchor_level : 15
     * user_level : 8
     * name_color : #e4caff
     * is_interaction : 0
     * interaction_time : 180
     * interaction_tips : 该房间为互动房间，观众每3分钟至少要发言一次，主播很孤单，跟Ta说句话吧~
     * complete_schedule : 0/5
     * pkInfo : {"is_pk":1,"current":{"uid":"250385","nickname":"鸿雁传书12","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg","room_id":"333227","follow_state":3,"pk_score":6,"level_role":"0","pk_top":[{"uid":"402583","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg"}],"pk_start_time":"1639036687"},"other":{"uid":"407943","nickname":"测试一下吧","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-19/20211119173223394.jpg","room_id":"407943","follow_state":3,"pk_score":2,"level_role":"0","pk_top":[{"uid":"402583","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg"}],"pk_start_time":"1639036687"}}
     */

    private String costomMassageType;
    private String uid;
    private int level;
    private String head_pic;
    private String is_admin;
    private String setNotalking;
    private String is_group_admin;
    private int time;
    private AnchorInfoBean anchor_info;
    private String showType;
    private String wealth_val;
    private int wealth_val_switch;
    private String name_color;
    private String is_interaction;
    private int interaction_time;
    private String interaction_tips;
    private String complete_schedule;
    private PkInfoBean pkInfo;
    private String room_token;
    private String manager_link_list;
    private String is_mute;
    private String anchor_level;
    private String user_level;
    private String fanclub_level;//粉丝团等级
    private String fanclub_card;//粉丝团名
    private String fanclub_status;//展示状态

    public String getFanclub_level() {
        return fanclub_level;
    }

    public void setFanclub_level(String fanclub_level) {
        this.fanclub_level = fanclub_level;
    }

    public String getFanclub_card() {
        return fanclub_card;
    }

    public void setFanclub_card(String fanclub_card) {
        this.fanclub_card = fanclub_card;
    }

    public String getFanclub_status() {
        return fanclub_status;
    }

    public void setFanclub_status(String fanclub_status) {
        this.fanclub_status = fanclub_status;
    }

    public String getIs_mute() {
        return is_mute;
    }

    public void setIs_mute(String is_mute) {
        this.is_mute = is_mute;
    }

    public String getManager_link_list() {
        return manager_link_list;
    }

    public void setManager_link_list(String manager_link_list) {
        this.manager_link_list = manager_link_list;
    }

    public String getRoom_token() {
        return room_token;
    }

    public void setRoom_token(String room_token) {
        this.room_token = room_token;
    }

    public String getCostomMassageType() {
        return costomMassageType;
    }

    public void setCostomMassageType(String costomMassageType) {
        this.costomMassageType = costomMassageType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    public String getSetNotalking() {
        return setNotalking;
    }

    public void setSetNotalking(String setNotalking) {
        this.setNotalking = setNotalking;
    }

    public String getIs_group_admin() {
        return is_group_admin;
    }

    public void setIs_group_admin(String is_group_admin) {
        this.is_group_admin = is_group_admin;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public AnchorInfoBean getAnchor_info() {
        return anchor_info;
    }

    public void setAnchor_info(AnchorInfoBean anchor_info) {
        this.anchor_info = anchor_info;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getWealth_val() {
        return wealth_val;
    }

    public void setWealth_val(String wealth_val) {
        this.wealth_val = wealth_val;
    }

    public int getWealth_val_switch() {
        return wealth_val_switch;
    }

    public void setWealth_val_switch(int wealth_val_switch) {
        this.wealth_val_switch = wealth_val_switch;
    }

    public String getAnchor_level() {
        return anchor_level;
    }

    public void setAnchor_level(String anchor_level) {
        this.anchor_level = anchor_level;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getName_color() {
        return name_color;
    }

    public void setName_color(String name_color) {
        this.name_color = name_color;
    }

    public String getIs_interaction() {
        return is_interaction;
    }

    public void setIs_interaction(String is_interaction) {
        this.is_interaction = is_interaction;
    }

    public int getInteraction_time() {
        return interaction_time;
    }

    public void setInteraction_time(int interaction_time) {
        this.interaction_time = interaction_time;
    }

    public String getInteraction_tips() {
        return interaction_tips;
    }

    public void setInteraction_tips(String interaction_tips) {
        this.interaction_tips = interaction_tips;
    }

    public String getComplete_schedule() {
        return complete_schedule;
    }

    public void setComplete_schedule(String complete_schedule) {
        this.complete_schedule = complete_schedule;
    }

    public PkInfoBean getPkInfo() {
        return pkInfo;
    }

    public void setPkInfo(PkInfoBean pkInfo) {
        this.pkInfo = pkInfo;
    }

    public static class AnchorInfoBean {
        /**
         * live_time : 1639036675
         * uid : 250385
         * nickname : 鸿雁传书12
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg
         * sex : 1
         * role : ~
         * age : 30
         * beans_current_count : 6
         * vip : 1
         * vipannual : 0
         * is_admin : 0
         * is_hidden_admin : 0
         * svip : 1
         * svipannual : 0
         * is_interaction : 0
         * anchor_status : 2
         * camera_switch : 0
         * live_poster : http://image.aiwujie.com.cn/Uploads/Picture/2021-12-06/20211206174402459.jpg
         * live_title : 这里是标题
         我换一个行
         * hour_ranking : 1
         * follow_state : 3
         */

        private long live_time;
        private String uid;
        private String nickname;
        private String head_pic;
        private String sex;
        private String role;
        private String age;
        private String beans_current_count;
        private String vip;
        private String vipannual;
        private String is_admin;
        private String is_hidden_admin;
        private String svip;
        private String svipannual;
        private String is_interaction;
        private String anchor_status;
        private String camera_switch;
        private String live_poster;
        private String live_title;
        private int hour_ranking;
        private int follow_state;

        public long getLive_time() {
            return live_time;
        }

        public void setLive_time(long live_time) {
            this.live_time = live_time;
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

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getBeans_current_count() {
            return beans_current_count;
        }

        public void setBeans_current_count(String beans_current_count) {
            this.beans_current_count = beans_current_count;
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

        public String getIs_interaction() {
            return is_interaction;
        }

        public void setIs_interaction(String is_interaction) {
            this.is_interaction = is_interaction;
        }

        public String getAnchor_status() {
            return anchor_status;
        }

        public void setAnchor_status(String anchor_status) {
            this.anchor_status = anchor_status;
        }

        public String getCamera_switch() {
            return camera_switch;
        }

        public void setCamera_switch(String camera_switch) {
            this.camera_switch = camera_switch;
        }

        public String getLive_poster() {
            return live_poster;
        }

        public void setLive_poster(String live_poster) {
            this.live_poster = live_poster;
        }

        public String getLive_title() {
            return live_title;
        }

        public void setLive_title(String live_title) {
            this.live_title = live_title;
        }

        public int getHour_ranking() {
            return hour_ranking;
        }

        public void setHour_ranking(int hour_ranking) {
            this.hour_ranking = hour_ranking;
        }

        public int getFollow_state() {
            return follow_state;
        }

        public void setFollow_state(int follow_state) {
            this.follow_state = follow_state;
        }
    }


}
