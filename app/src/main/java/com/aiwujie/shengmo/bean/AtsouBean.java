package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/7/27.
 */

public class AtsouBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"173795","nickname":"行行好~渴死了！","age":"25","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-05-18/20190518104345301.jpg","sex":"1","role":"M","vip":"0","vipannual":"0","realname":"0"},{"uid":"409944","nickname":"伤心了","age":"25","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-07-11/20190711011128472.jpg","sex":"1","role":"SM","vip":"1","vipannual":"0","realname":"0"},{"uid":"168650","nickname":"没有比我更sao的了","age":"20","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2019-07-19/20190719123928274.jpg","sex":"1","role":"SM","vip":"0","vipannual":"0","realname":"1"},{"uid":"252905","nickname":"尿尿了","age":"29","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2018-10-17/20181017011441391.jpg","sex":"1","role":"M","vip":"0","vipannual":"0","realname":"1"}]
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
         * uid : 173795
         * nickname : 行行好~渴死了！
         * age : 25
         * head_pic : http://hao.shengmo.org:888/Uploads/Picture/2019-05-18/20190518104345301.jpg
         * sex : 1
         * role : M
         * vip : 0
         * vipannual : 0
         * realname : 0
         */

        private String uid;
        private String nickname;
        private String age;
        private String head_pic;
        private String sex;
        private String role;
        private String vip;
        private String vipannual;
        private String realname;

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

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }
    }
}
