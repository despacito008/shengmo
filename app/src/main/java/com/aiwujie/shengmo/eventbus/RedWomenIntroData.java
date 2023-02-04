package com.aiwujie.shengmo.eventbus;

import java.util.List;

/**
 * Created by 290243232 on 2017/12/21.
 */

public class RedWomenIntroData {


    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"id":"2","fuid":"11","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-11-21/20171121172935299.jpg","nickname":"绿色斯慕","realname":"0","age":"26","sex":"1","role":"~","sexual":"1","province":"湖南省","city":"岳阳市","remarks":"配对失败！","addtime":"2017年12月20日"},{"id":"3","fuid":"12","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-12-13/20171213174802531.jpg","nickname":"小清新","realname":"1","age":"25","sex":"1","role":"S","sexual":"2","province":"北京市","city":"北京市","remarks":"配对失败！","addtime":"2017年12月20日"}]
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
         * id : 2
         * fuid : 11
         * head_pic : http://hao.shengmo.org:888/Uploads/Picture/2017-11-21/20171121172935299.jpg
         * nickname : 绿色斯慕
         * realname : 0
         * age : 26
         * sex : 1
         * role : ~
         * sexual : 1
         * province : 湖南省
         * city : 岳阳市
         * remarks : 配对失败！
         * addtime : 2017年12月20日
         */

        private String mid;
        private String fuid;
        private String head_pic;
        private String nickname;
        private String realname;
        private String age;
        private String sex;
        private String role;
        private String sexual;
        private String province;
        private String city;
        private String remarks;
        private String addtime;

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getFuid() {
            return fuid;
        }

        public void setFuid(String fuid) {
            this.fuid = fuid;
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

        public String getSexual() {
            return sexual;
        }

        public void setSexual(String sexual) {
            this.sexual = sexual;
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

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}
