package com.tencent.qcloud.tim.tuikit.live.bean;

public class LiveCreateAvChatRoom {

    /**
     * costomMassageType : createAvChatRoom
     * uid : 623791
     * level : 1
     * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-09-02/20210902155546582.jpg
     * is_group_admin : 0
     * time : 1634216609
     * anchor_info : {"uid":"623791","nickname":"shengmo5","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-09-02/20210902155546582.jpg","sex":"1","role":"M","age":"26","beans_current_count":"0","vip":"1","vipannual":"0","is_admin":"0","is_hidden_admin":"0","svip":"1","svipannual":"1","hour_ranking":0,"follow_state":1}
     * showType : 1
     */

    private String costomMassageType;
    private String uid;
    private int level;
    private String head_pic;
    private String is_group_admin;
    private long time;
    private AnchorInfoBean anchor_info;
    private String showType;
    private String wealth_val; //财富值
    private String wealth_val_switch; //是否显示财富值
    private String name_color;
    private String anchor_level;
    private String user_level;
    private String background_color;

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
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

    public String getIs_group_admin() {
        return is_group_admin;
    }

    public void setIs_group_admin(String is_group_admin) {
        this.is_group_admin = is_group_admin;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
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

    public static class AnchorInfoBean {
        /**
         * uid : 623791
         * nickname : shengmo5
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-09-02/20210902155546582.jpg
         * sex : 1
         * role : M
         * age : 26
         * beans_current_count : 0
         * vip : 1
         * vipannual : 0
         * is_admin : 0
         * is_hidden_admin : 0
         * svip : 1
         * svipannual : 1
         * hour_ranking : 0
         * follow_state : 1
         */

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
        private int hour_ranking;
        private int follow_state;
        private String live_poster;
        private String live_title;

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
