package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/23.
 */
public class LaudListData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"lid":"22","uid":"11","nickname":"哈哈哈是吧","age":"27","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","sex":"1","role":"S","vip":"1","realname":"0","city":"北京市","onlinestate":1,"state":1},{"lid":"30","uid":"12","nickname":"屌不屌","age":"20","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-21/2017012100224932.jpg","sex":"2","role":"SM","vip":"1","realname":"0","city":"北京市","onlinestate":1,"state":4}]
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
         * lid : 22
         * uid : 11
         * nickname : 哈哈哈是吧
         * age : 27
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg
         * sex : 1
         * role : S
         * vip : 1
         * realname : 0
         * city : 北京市
         * onlinestate : 1
         * state : 1
         */

        private String lid;
        private String uid;
        private String nickname;
        private String age;
        private String head_pic;
        private String sex;
        private String role;
        private String vip;
        private String realname;
        private String city;
        private int onlinestate;
        private int state;
        private String is_admin;
        private String is_hand;
        private String sendtime;
        private String vipannual;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String charm_val;
        private String wealth_val;
        private String bkvip;
        private String blvip;
        private String location_city_switch;

        public String getLocation_city_switch() {
            return location_city_switch;
        }

        public void setLocation_city_switch(String location_city_switch) {
            this.location_city_switch = location_city_switch;
        }

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
        public String getVipannual() {
            return vipannual;
        }

        public void setVipannual(String vipannual) {
            this.vipannual = vipannual;
        }
        public String getSendtime() {
            return sendtime;
        }

        public void setSendtime(String sendtime) {
            this.sendtime = sendtime;
        }

        public String getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(String is_admin) {
            this.is_admin = is_admin;
        }

        public String getIs_hand() {
            return is_hand;
        }

        public void setIs_hand(String is_hand) {
            this.is_hand = is_hand;
        }
        public String getLid() {
            return lid;
        }

        public void setLid(String lid) {
            this.lid = lid;
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

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getOnlinestate() {
            return onlinestate;
        }

        public void setOnlinestate(int onlinestate) {
            this.onlinestate = onlinestate;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}
