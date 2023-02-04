package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/11/17.
 */

public class WarningListData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"devicestatus":"1","status":"1","chatstatus":"0","infostatus":"1","dynamicstatus":"0","uid":"48554","nickname":"蓝色的天","age":"117","head_pic":"http://hao.shengmo.org:888/Uploads/Picture/2017-11-17/20171117120003977.jpg ","sex":"1","role":"S","vip":"0","vipannual":"0","realname":"0","last_login_time":"32分钟前","province":"","city":"","lat":"0.000000","lng":"0.000000","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","onlinestate":1},{"devicestatus":"1","status":"1","chatstatus":"1","infostatus":"1","dynamicstatus":"0","uid":"41346","nickname":"在哪里嗨","age":"19","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-19/20170619200159788.jpg","sex":"1","role":"M","vip":"0","vipannual":"0","realname":"0","last_login_time":"1小时前","province":"福建省","city":"福州市","lat":"26.071552","lng":"119.178406","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","onlinestate":1}]
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
         * devicestatus : 1（为0时设备被封）
         * status : 1（为0时账号被封）
         * chatstatus : 0 （为0是聊天被封）
         * infostatus : 1 （为0时资料被封）
         * dynamicstatus : 0（为0是动态被封）
         * uid : 48554
         * nickname : 蓝色的天
         * age : 117
         * head_pic : http://hao.shengmo.org:888/Uploads/Picture/2017-11-17/20171117120003977.jpg
         * sex : 1
         * role : S
         * vip : 0
         * vipannual : 0
         * realname : 0
         * last_login_time : 32分钟前
         * province :
         * city :
         * lat : 0.000000
         * lng : 0.000000
         * is_admin : 0
         * is_hand : 0
         * is_volunteer : 0
         * svip : 0
         * svipannual : 0
         * charm_val : 0
         * wealth_val : 0
         * onlinestate : 1
         */

        private String devicestatus;
        private String status;
        private String chatstatus;
        private String infostatus;
        private String dynamicstatus;
        private String uid;
        private String nickname;
        private String age;
        private String head_pic;
        private String sex;
        private String role;
        private String vip;
        private String vipannual;
        private String realname;
        private String last_login_time;
        private String province;
        private String city;
        private String lat;
        private String lng;
        private String is_admin;
        private String is_hand;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String charm_val_new;
        private String wealth_val_new;
        private int onlinestate;
        private String bkvip;
        private String blvip;
        private String dynamic_blockingalong;
        private String info_blockingalong;
        private String chat_blockingalong;
        private String account_blockingalong;
        private String device_blockingalong;
        private String handletime;
        private String prohibition_status;//禁播
        private String livestatus;//禁看
        private String live_chat_blockingalong; //禁播天数
        private String  watchlive_blockingalong;//禁看天数

        public String getProhibition_status() {
            return prohibition_status;
        }

        public void setProhibition_status(String prohibition_status) {
            this.prohibition_status = prohibition_status;
        }

        public String getLivestatus() {
            return livestatus;
        }

        public void setLivestatus(String livestatus) {
            this.livestatus = livestatus;
        }

        public String getLive_chat_blockingalong() {
            return live_chat_blockingalong;
        }

        public void setLive_chat_blockingalong(String live_chat_blockingalong) {
            this.live_chat_blockingalong = live_chat_blockingalong;
        }

        public String getWatchlive_blockingalong() {
            return watchlive_blockingalong;
        }

        public void setWatchlive_blockingalong(String watchlive_blockingalong) {
            this.watchlive_blockingalong = watchlive_blockingalong;
        }

        public String getHandletime() {
            return handletime;
        }

        public void setHandletime(String handletime) {
            this.handletime = handletime;
        }

        public String getCharm_val_new() {
            return charm_val_new;
        }

        public void setCharm_val_new(String charm_val_new) {
            this.charm_val_new = charm_val_new;
        }

        public String getWealth_val_new() {
            return wealth_val_new;
        }

        public void setWealth_val_new(String wealth_val_new) {
            this.wealth_val_new = wealth_val_new;
        }

        public String getDynamic_blockingalong() {
            return dynamic_blockingalong;
        }

        public void setDynamic_blockingalong(String dynamic_blockingalong) {
            this.dynamic_blockingalong = dynamic_blockingalong;
        }

        public String getInfo_blockingalong() {
            return info_blockingalong;
        }

        public void setInfo_blockingalong(String info_blockingalong) {
            this.info_blockingalong = info_blockingalong;
        }

        public String getChat_blockingalong() {
            return chat_blockingalong;
        }

        public void setChat_blockingalong(String chat_blockingalong) {
            this.chat_blockingalong = chat_blockingalong;
        }

        public String getAccount_blockingalong() {
            return account_blockingalong;
        }

        public void setAccount_blockingalong(String account_blockingalong) {
            this.account_blockingalong = account_blockingalong;
        }

        public String getDevice_blockingalong() {
            return device_blockingalong;
        }

        public void setDevice_blockingalong(String device_blockingalong) {
            this.device_blockingalong = device_blockingalong;
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

        public String getDevicestatus() {
            return devicestatus;
        }

        public void setDevicestatus(String devicestatus) {
            this.devicestatus = devicestatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getChatstatus() {
            return chatstatus;
        }

        public void setChatstatus(String chatstatus) {
            this.chatstatus = chatstatus;
        }

        public String getInfostatus() {
            return infostatus;
        }

        public void setInfostatus(String infostatus) {
            this.infostatus = infostatus;
        }

        public String getDynamicstatus() {
            return dynamicstatus;
        }

        public void setDynamicstatus(String dynamicstatus) {
            this.dynamicstatus = dynamicstatus;
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

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
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

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
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

        public int getOnlinestate() {
            return onlinestate;
        }

        public void setOnlinestate(int onlinestate) {
            this.onlinestate = onlinestate;
        }
    }
}
