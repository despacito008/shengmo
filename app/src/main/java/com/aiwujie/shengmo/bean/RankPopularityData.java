package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/5/12.
 */

public class RankPopularityData {


    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"allnum":"68","uid":"49355","nickname":"喵喵酱的日常","head_pic":"http://q.qlogo.cn/qqapp/1105968084/6EDE3D00F622F5E51E3E155CF7F38487/100","vip":"0","vipannual":"0","realname":"0","sex":"2","age":"29","role":"M","is_admin":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0"},{"allnum":"66","uid":"48973","nickname":"喃喃自语_","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-07-06/20170706152903610.jpg","vip":"0","vipannual":"0","realname":"0","sex":"2","age":"24","role":"M","is_admin":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0"},{"allnum":"58","uid":"49201","nickname":"妍霏_m","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-07-07/201707070010556.jpg","vip":"0","vipannual":"0","realname":"0","sex":"2","age":"22","role":"M","is_admin":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0"},{"allnum":"34","uid":"48916","nickname":"青柠๑","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-07-06/20170706130426724.jpg","vip":"0","vipannual":"0","realname":"0","sex":"2","age":"18","role":"M","is_admin":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0"},{"allnum":"33","uid":"47112","nickname":"人在画桥西","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-07-04/2017070423244340.jpg","vip":"0","vipannual":"0","realname":"0","sex":"1","age":"22","role":"M","is_admin":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0"},{"allnum":"29","uid":"11499","nickname":"小娇娇╭╯爱吃大白兔","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-10/201706101925302.jpg","vip":"1","vipannual":"0","realname":"1","sex":"2","age":"23","role":"S","is_admin":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0"},{"allnum":"29","uid":"45120","nickname":"相宜","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-07-02/20170702132716424.jpg","vip":"0","vipannual":"0","realname":"0","sex":"2","age":"117","role":"M","is_admin":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0"},{"allnum":"28","uid":"48925","nickname":"最可爱的莹妹","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-07-06/20170706125834658.jpg","vip":"0","vipannual":"0","realname":"1","sex":"2","age":"18","role":"M","is_admin":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0"},{"allnum":"26","uid":"47995","nickname":"阳光\u2026","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-07-07/20170707102413996.jpg","vip":"0","vipannual":"0","realname":"0","sex":"2","age":"28","role":"M","is_admin":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0"},{"allnum":"26","uid":"31509","nickname":"AresAtlant","head_pic":"http://q.qlogo.cn/qqapp/1105968084/DAE72E5157DCD34F1995EF828E731A7F/100","vip":"1","vipannual":"0","realname":"1","sex":"2","age":"18","role":"S","is_admin":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0"}]
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
         * allnum : 68
         * uid : 49355
         * nickname : 喵喵酱的日常
         * head_pic : http://q.qlogo.cn/qqapp/1105968084/6EDE3D00F622F5E51E3E155CF7F38487/100
         * vip : 0
         * vipannual : 0
         * realname : 0
         * sex : 2
         * age : 29
         * role : M
         * is_admin : 0
         * is_volunteer : 0
         * svip : 0
         * svipannual : 0
         * charm_val : 0
         * wealth_val : 0
         */

        private String allnum;
        private String uid;
        private String nickname;
        private String head_pic;
        private String vip;
        private String vipannual;
        private String realname;
        private String sex;
        private String age;
        private String role;
        private String is_admin;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String charm_val_new;
        private String wealth_val_new;
        private String bkvip;
        private String blvip;

        public String getBkvip() {
            return bkvip;
        }

        public void setBkvip(String bkvip) {
            this.bkvip = bkvip;
        }

        public String getBlvip() {
            return blvip;
        }

        public void setBlvip(String blvip) {
            this.blvip = blvip;
        }

        public String getAllnum() {
            return allnum;
        }

        public void setAllnum(String allnum) {
            this.allnum = allnum;
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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
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

        public String getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(String is_admin) {
            this.is_admin = is_admin;
        }

        public String getIs_volunteer() {
            return is_volunteer;
        }

        public void setIs_volunteer(String is_volunteer) {
            this.is_volunteer = is_volunteer;
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
            return charm_val_new;
        }

        public void setCharm_val(String charm_val) {
            this.charm_val_new = charm_val;
        }

        public String getWealth_val() {
            return wealth_val_new;
        }

        public void setWealth_val(String wealth_val) {
            this.wealth_val_new = wealth_val;
        }
    }
}
