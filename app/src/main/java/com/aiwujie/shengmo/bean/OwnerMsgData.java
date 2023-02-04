package com.aiwujie.shengmo.bean;

import java.util.ArrayList;

/**
 * Created by 290243232 on 2017/1/3.
 */
public class OwnerMsgData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"uid":"5","head_pic":"http://59.110.28.150:888/Uploads/Picture/2016-12-19/20161219224411226.jpg","nickname":"屌","sex":"2","tall":"127","weight":"60","role":"S","sexual":"1","along":"1","experience":"2","level":"3","want":"2","culture":"1","monthly":"1","introduce":"哈哈","photo":["Uploads/Picture/2016-12-28/20161228211209139.jpg","Uploads/Picture/2016-12-28/20161228211209139.jpg","Uploads/Picture/2016-12-28/20161228211209139.jpg","Uploads/Picture/2016-12-28/20161228211209139.jpg","Uploads/Picture/2016-12-28/20161228211209139.jpg","Uploads/Picture/2016-12-28/20161228211209139.jpg"],"birthday":"1993-01-01"}
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
         * uid : 5
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2016-12-19/20161219224411226.jpg
         * nickname : 屌
         * sex : 2
         * tall : 127
         * weight : 60
         * role : S
         * sexual : 1
         * along : 1
         * experience : 2
         * level : 3
         * want : 2
         * culture : 1
         * monthly : 1
         * introduce : 哈哈
         * photo : ["Uploads/Picture/2016-12-28/20161228211209139.jpg","Uploads/Picture/2016-12-28/20161228211209139.jpg","Uploads/Picture/2016-12-28/20161228211209139.jpg","Uploads/Picture/2016-12-28/20161228211209139.jpg","Uploads/Picture/2016-12-28/20161228211209139.jpg","Uploads/Picture/2016-12-28/20161228211209139.jpg"]
         * birthday : 1993-01-01
         */

        private String uid;
        private String head_pic;
        private String nickname;
        private String sex;
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
        private String attribute;
        private String introduce;
        private String birthday;
        private ArrayList<String> photo;
        private String changeState;
        private String vip;
        private String svip;
        private String is_admin;

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

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getChangeState() {
            return changeState;
        }

        public void setChangeState(String changeState) {
            this.changeState = changeState;
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

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getRole() {
            return role;
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
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

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public ArrayList<String> getPhoto() {
            return photo;
        }

        public void setPhoto(ArrayList<String> photo) {
            this.photo = photo;
        }
    }
}
