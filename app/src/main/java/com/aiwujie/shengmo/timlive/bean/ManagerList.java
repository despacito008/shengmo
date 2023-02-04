package com.aiwujie.shengmo.timlive.bean;

import java.util.List;

public class ManagerList {

    /**
     * retcode : 2000
     * msg : 获取场控成功
     * data : [{"uid":"623791","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-09-02/20210902155546582.jpg","nickname":"shengmo5","age":"26","role":"M","sex":"1","tall":"175","location_switch":"0","vip":"1","vipannual":"0","is_admin":"0","is_hidden_admin":"0","svip":"1","svipannual":"1","charm_val":"181","wealth_val":"999990","wealth_val_switch":"0","charm_val_switch":"0","location_city_switch":"0","wealth_val_new":"999990","charm_val_new":"181","city":"北京市 北京市"},{"uid":"623793","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-26/20211026145920699.jpg","nickname":"ceshi6","age":"31","role":"~","sex":"1","tall":"175","location_switch":"0","vip":"0","vipannual":"0","is_admin":"1","is_hidden_admin":"0","svip":"0","svipannual":"0","charm_val":"224","wealth_val":"999990","wealth_val_switch":"0","charm_val_switch":"0","location_city_switch":"0","wealth_val_new":"999990","charm_val_new":"224","city":"北京市 "}]
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
         * uid : 623791
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-09-02/20210902155546582.jpg
         * nickname : shengmo5
         * age : 26
         * role : M
         * sex : 1
         * tall : 175
         * location_switch : 0
         * vip : 1
         * vipannual : 0
         * is_admin : 0
         * is_hidden_admin : 0
         * svip : 1
         * svipannual : 1
         * charm_val : 181
         * wealth_val : 999990
         * wealth_val_switch : 0
         * charm_val_switch : 0
         * location_city_switch : 0
         * wealth_val_new : 999990
         * charm_val_new : 181
         * city : 北京市 北京市
         */

        private String uid;
        private String head_pic;
        private String nickname;
        private String age;
        private String role;
        private String sex;
        private String tall;
        private String location_switch;
        private String vip;
        private String vipannual;
        private String is_admin;
        private String is_hidden_admin;
        private String svip;
        private String svipannual;
        private String charm_val;
        private String wealth_val;
        private String wealth_val_switch;
        private String charm_val_switch;
        private String location_city_switch;
        private String wealth_val_new;
        private String charm_val_new;
        private String city;
        private String unblock_time;

        public String getUnblock_time() {
            return unblock_time;
        }

        public void setUnblock_time(String unblock_time) {
            this.unblock_time = unblock_time;
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

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getTall() {
            return tall;
        }

        public void setTall(String tall) {
            this.tall = tall;
        }

        public String getLocation_switch() {
            return location_switch;
        }

        public void setLocation_switch(String location_switch) {
            this.location_switch = location_switch;
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

        public String getLocation_city_switch() {
            return location_city_switch;
        }

        public void setLocation_city_switch(String location_city_switch) {
            this.location_city_switch = location_city_switch;
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

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
