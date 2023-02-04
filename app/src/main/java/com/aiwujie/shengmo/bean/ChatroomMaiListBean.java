package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/9/17.
 */

public class ChatroomMaiListBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"402624","nickname":"ceshi7*","age":"18","head_pic":"http://aiwujie.com.cn/Uploads/Picture/2019-08-21/20190821181411991.jpg","sex":"1","role":"M","vip":"1","vipannual":"0","svip":"1","svipannual":"0","realname":"1"},{"uid":"431288","nickname":"yuanchaoya","age":"24","head_pic":"http://aiwujie.com.cn/Uploads/Picture/2019-09-11/20190911105601811.jpg","sex":"1","role":"SM","vip":"0","vipannual":"0","svip":"0","svipannual":"0","realname":"0"}]
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
         * uid : 402624
         * nickname : ceshi7*
         * age : 18
         * head_pic : http://aiwujie.com.cn/Uploads/Picture/2019-08-21/20190821181411991.jpg
         * sex : 1
         * role : M
         * vip : 1
         * vipannual : 0
         * svip : 1
         * svipannual : 0
         * realname : 1
         */

        private String uid;
        private String nickname;
        private String age;
        private String head_pic;
        private String sex;
        private String role;
        private String vip;
        private String vipannual;
        private String svip;
        private String svipannual;
        private String realname;
        private String rule;

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
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

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }
    }
}
