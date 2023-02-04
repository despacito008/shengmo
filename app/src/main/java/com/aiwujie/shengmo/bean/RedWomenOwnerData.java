package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/12/15.
 */

public class RedWomenOwnerData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"uid":"12","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-12-13/20171213174802531.jpg","realname":"1","match_state":"0","nickname":"小清新","sex":"1","role":"S","age":"25","province":"北京市","city":"北京市"}
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
         * head_pic : http://hao.shengmo.org:888/Uploads/Picture/2017-12-13/20171213174802531.jpg
         * realname : 1
         * match_state : 0
         * nickname : 小清新
         * sex : 1
         * role : S
         * age : 25
         * province : 北京市
         * city : 北京市
         */

        private String uid;
        private String head_pic;
        private String realname;
        private String match_state;
        private String nickname;
        private String sex;
        private String role;
        private String age;
        private String province;
        private String city;

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

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getMatch_state() {
            return match_state;
        }

        public void setMatch_state(String match_state) {
            this.match_state = match_state;
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
    }
}
