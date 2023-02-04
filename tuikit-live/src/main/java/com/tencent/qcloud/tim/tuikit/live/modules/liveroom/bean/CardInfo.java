package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean;

public class CardInfo {

    /**
     * retcode : 2000
     * msg : 成功
     * data : {"uid":"623093","nickname":"Android","age":"20","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-27/20210827120208312.jpg","sex":"2","role":"~","vip":"1","vipannual":"0","location_switch":"0","is_admin":"1","svip":"0","svipannual":"0","wealth_val_switch":"0","charm_val_switch":"0","wealth_val_new":"999990","charm_val_new":"18","follow_state":1,"city":"北京市 北京市","time":1632297211,"group_role":"1"}
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
         * uid : 623093
         * nickname : Android
         * age : 20
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-08-27/20210827120208312.jpg
         * sex : 2
         * role : ~
         * vip : 1
         * vipannual : 0
         * location_switch : 0
         * is_admin : 1
         * svip : 0
         * svipannual : 0
         * wealth_val_switch : 0
         * charm_val_switch : 0
         * wealth_val_new : 999990
         * charm_val_new : 18
         * follow_state : 1
         * city : 北京市 北京市
         * time : 1632297211
         * group_role : 1
         */

        private String uid;
        private String nickname;
        private String age;
        private String head_pic;
        private String sex;
        private String role;
        private String vip;
        private String vipannual;
        private String location_switch;
        private String is_admin;
        private String svip;
        private String svipannual;
        private String wealth_val_switch;
        private String charm_val_switch;
        private String wealth_val_new;
        private String charm_val_new;
        private int follow_state;
        private String city;
        private int time;
        private String group_role;
        private String is_online;
        private String is_kick;
        private String is_mute;

        public String getIs_mute() {
            return is_mute;
        }

        public void setIs_mute(String is_mute) {
            this.is_mute = is_mute;
        }

        public String getIs_kick() {
            return is_kick;
        }

        public void setIs_kick(String is_kick) {
            this.is_kick = is_kick;
        }

        public String getIs_online() {
            return is_online;
        }

        public void setIs_online(String is_online) {
            this.is_online = is_online;
        }

        private String setNotalking;

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

        public String getLocation_switch() {
            return location_switch;
        }

        public void setLocation_switch(String location_switch) {
            this.location_switch = location_switch;
        }

        public String getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(String is_admin) {
            this.is_admin = is_admin;
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

        public int getFollow_state() {
            return follow_state;
        }

        public void setFollow_state(int follow_state) {
            this.follow_state = follow_state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getGroup_role() {
            return group_role;
        }

        public void setGroup_role(String group_role) {
            this.group_role = group_role;
        }

        public String getSetNotalking() {
            return setNotalking;
        }

        public void setSetNotalking(String setNotalking) {
            this.setNotalking = setNotalking;
        }
    }
}
