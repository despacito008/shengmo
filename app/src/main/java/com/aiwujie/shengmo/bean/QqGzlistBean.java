package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/8/10.
 */

public class QqGzlistBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"uid":"435631","state":1,"userInfo":{"uid":"435631","nickname":"Lirios","age":"20","head_pic":"http://hao.shengmo.org/Uploads/Picture/2019-07-29/20190729194915817.jpg","sex":"2","role":"M","vip":"1","vipannual":"0","realname":"0","city":"","province":"","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"1","svipannual":"0","charm_val":"11825","wealth_val":"43586","location_city_switch":"0","onlinestate":1,"markname":""}},{"uid":"392587","state":1,"userInfo":{"uid":"392587","nickname":"摩洛哥","age":"119","head_pic":"http://thirdqq.qlogo.cn/g?b=oidb&k=eugEHJWhCZbwyJCjTnicVjw&s=100","sex":"1","role":"~","vip":"1","vipannual":"0","realname":"1","city":"朔州市","province":"山西省","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"1","svipannual":"1","charm_val":"1918","wealth_val":"0","location_city_switch":"0","onlinestate":1,"markname":""}},{"uid":"396447","state":1,"userInfo":{"uid":"396447","nickname":"肉球球","age":"18","head_pic":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLRDnru6NiaXic8Q7A36s8RPNAibRf2akQWkdHAzsiakmNlgsTO2rrexOsDib4aJjyKicXuwmFEtrjuzchA/132","sex":"2","role":"M","vip":"1","vipannual":"0","realname":"1","city":"西安市","province":"陕西省","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"1","svipannual":"0","charm_val":"8987","wealth_val":"378","location_city_switch":"0","onlinestate":1,"markname":""}}]
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
         * uid : 435631
         * state : 1
         * userInfo : {"uid":"435631","nickname":"Lirios","age":"20","head_pic":"http://hao.shengmo.org/Uploads/Picture/2019-07-29/20190729194915817.jpg","sex":"2","role":"M","vip":"1","vipannual":"0","realname":"0","city":"","province":"","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"1","svipannual":"0","charm_val":"11825","wealth_val":"43586","location_city_switch":"0","onlinestate":1,"markname":""}
         */

        private String uid;
        private int state;
        private UserInfoBean userInfo;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public static class UserInfoBean {
            /**
             * uid : 435631
             * nickname : Lirios
             * age : 20
             * head_pic : http://hao.shengmo.org/Uploads/Picture/2019-07-29/20190729194915817.jpg
             * sex : 2
             * role : M
             * vip : 1
             * vipannual : 0
             * realname : 0
             * city :
             * province :
             * is_admin : 0
             * is_hand : 0
             * is_volunteer : 0
             * svip : 1
             * svipannual : 0
             * charm_val : 11825
             * wealth_val : 43586
             * location_city_switch : 0
             * onlinestate : 1
             * markname :
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
            private String city;
            private String province;
            private String is_admin;
            private String is_hand;
            private String is_volunteer;
            private String svip;
            private String svipannual;
            private String charm_val_new;
            private String wealth_val_new;
            private String location_city_switch;
            private int onlinestate;
            private String markname;
            private String bkvip;
            private String blvip;
            private String lmarkname;

            public String getLmarkname() {
                return lmarkname;
            }

            public void setLmarkname(String lmarkname) {
                this.lmarkname = lmarkname;
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

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
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

            public String getLocation_city_switch() {
                return location_city_switch;
            }

            public void setLocation_city_switch(String location_city_switch) {
                this.location_city_switch = location_city_switch;
            }

            public int getOnlinestate() {
                return onlinestate;
            }

            public void setOnlinestate(int onlinestate) {
                this.onlinestate = onlinestate;
            }

            public String getMarkname() {
                return markname;
            }

            public void setMarkname(String markname) {
                this.markname = markname;
            }
        }
    }
}
