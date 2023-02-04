package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/23.
 */
public class RewardData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"rwid":"1","amount":"50.00","uid":"12","nickname":"屌不屌","age":"20","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-21/2017012100224932.jpg","sex":"2","role":"SM","vip":"1","realname":"0","last_login_time":"1485178998","city":"北京市","onlinestate":1,"state":4},{"rwid":"3","amount":"1.00","uid":"12","nickname":"屌不屌","age":"20","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-21/2017012100224932.jpg","sex":"2","role":"SM","vip":"1","realname":"0","last_login_time":"1485178998","city":"北京市","onlinestate":1,"state":4}]
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
         * rwid : 1
         * amount : 50.00
         * uid : 12
         * nickname : 屌不屌
         * age : 20
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-01-21/2017012100224932.jpg
         * sex : 2
         * role : SM
         * vip : 1
         * realname : 0
         * last_login_time : 1485178998
         * city : 北京市
         * onlinestate : 1
         * state : 4
         */

        private String rwid;
        private String amount;
        private String uid;
        private String nickname;
        private String age;
        private String head_pic;
        private String sex;
        private String role;
        private String vip;
        private String realname;
        private String last_login_time;
        private String city;
        private int onlinestate;
        private int state;
        private String is_admin;
        private String is_hand;
        private String psid;
        private String sendtime;
        private String vipannual;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String num;
        private String sum;
        private String bkvip;
        private String blvip;
        private String location_city_switch;
        private String gift_image;

        public String getGift_image() {
            return gift_image;
        }

        public void setGift_image(String gift_image) {
            this.gift_image = gift_image;
        }

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

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
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

        public String getPsid() {
            return psid;
        }

        public void setPsid(String psid) {
            this.psid = psid;
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
        public String getRwid() {
            return rwid;
        }

        public void setRwid(String rwid) {
            this.rwid = rwid;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
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

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
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
