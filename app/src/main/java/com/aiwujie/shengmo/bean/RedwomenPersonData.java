package com.aiwujie.shengmo.bean;

import java.util.ArrayList;

/**
 * Created by 290243232 on 2017/8/28.
 */

public class RedwomenPersonData {


    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"uid":"175","match_photo":["http://hao.shengmo.org:888/Uploads/Picture/2017-04-06/20170406202447581.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-04-06/20170406202504491.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-04-06/20170406202531133.jpg"],"match_state":"2","match_photo_lock":"0","nickname":"深藏功与名.3","sex":"1","role":"~","age":"24","province":"北京市","city":"北京市","starchar":"白羊座","tall":"175","weight":"60","culture":"本科","monthly":"1万-2万","along":"1年以下","experience":"无","sexual":"女","want":"聊天,现实,结婚","level":"轻度,中度,重度","match_introduce":"123","match_makerintroduce":"123"}
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
         * uid : 175
         * match_photo : ["http://hao.shengmo.org:888/Uploads/Picture/2017-04-06/20170406202447581.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-04-06/20170406202504491.jpg","http://hao.shengmo.org:888/Uploads/Picture/2017-04-06/20170406202531133.jpg"]
         * match_state : 2
         * match_photo_lock : 0
         * nickname : 深藏功与名.3
         * sex : 1
         * role : ~
         * age : 24
         * province : 北京市
         * city : 北京市
         * starchar : 白羊座
         * tall : 175
         * weight : 60
         * culture : 本科
         * monthly : 1万-2万
         * along : 1年以下
         * experience : 无
         * sexual : 女
         * want : 聊天,现实,结婚
         * level : 轻度,中度,重度
         * match_introduce : 123
         * match_makerintroduce : 123
         */

        private String uid;
        private String match_state;
        private String match_photo_lock;
        private String nickname;
        private String sex;
        private String role;
        private String age;
        private String province;
        private String city;
        private String starchar;
        private String tall;
        private String weight;
        private String culture;
        private String monthly;
        private String along;
        private String experience;
        private String sexual;
        private String want;
        private String level;
        private String match_introduce;
        private String match_makerintroduce;
        private ArrayList<String> match_photo;
        private String realname;
        private String match_num;

        public String getMatch_num() {
            return match_num;
        }

        public void setMatch_num(String match_num) {
            this.match_num = match_num;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getMatch_state() {
            return match_state;
        }

        public void setMatch_state(String match_state) {
            this.match_state = match_state;
        }

        public String getMatch_photo_lock() {
            return match_photo_lock;
        }

        public void setMatch_photo_lock(String match_photo_lock) {
            this.match_photo_lock = match_photo_lock;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public String getSexual() {
            return sexual;
        }

        public void setSexual(String sexual) {
            this.sexual = sexual;
        }

        public String getWant() {
            return want;
        }

        public void setWant(String want) {
            this.want = want;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getMatch_introduce() {
            return match_introduce;
        }

        public void setMatch_introduce(String match_introduce) {
            this.match_introduce = match_introduce;
        }

        public String getMatch_makerintroduce() {
            return match_makerintroduce;
        }

        public void setMatch_makerintroduce(String match_makerintroduce) {
            this.match_makerintroduce = match_makerintroduce;
        }

        public ArrayList<String> getMatch_photo() {
            return match_photo;
        }

        public void setMatch_photo(ArrayList<String> match_photo) {
            this.match_photo = match_photo;
        }
    }
}
