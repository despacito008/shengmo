package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/8/28.
 */

public class BeautifulData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"175","nickname":"深藏功与名.3","sex":"1","role":"~","match_photo":"Uploads/Picture/2017-06-11/20170611112112130.jpg","match_photo_lock":"0","province":"北京市","city":"北京市"}]
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
         * uid : 175
         * nickname : 深藏功与名.3
         * sex : 1
         * role : ~
         * match_photo : Uploads/Picture/2017-06-11/20170611112112130.jpg
         * match_photo_lock : 0
         * province : 北京市
         * city : 北京市
         */

        private String uid;
        private String nickname;
        private String sex;
        private String role;
        private String match_photo;
        private String match_photo_lock;
        private String province;
        private String city;
        private String realname;
        private String age;
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

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
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

        public String getMatch_photo() {
            return match_photo;
        }

        public void setMatch_photo(String match_photo) {
            this.match_photo = match_photo;
        }

        public String getMatch_photo_lock() {
            return match_photo_lock;
        }

        public void setMatch_photo_lock(String match_photo_lock) {
            this.match_photo_lock = match_photo_lock;
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
    }
}
