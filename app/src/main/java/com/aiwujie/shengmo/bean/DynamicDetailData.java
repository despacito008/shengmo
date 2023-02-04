package com.aiwujie.shengmo.bean;

import java.util.ArrayList;

/**
 * Created by 290243232 on 2017/3/20.
 */

public class DynamicDetailData {


    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"did":"621","uid":"960","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-03-19/20170319130111676.jpg","nickname":"，","vip":"0","realname":"0","sex":"1","age":"21","role":"S","readtimes":"12","distance":1600,"addtime":"14分钟前","content":"附图自己领会","pic":["http://59.110.28.150:888/Uploads/Picture/2017-03-22/20170322104213661.jpg"],"laudnum":"0","rewardnum":"0","comnum":"0","is_admin":"0","is_hand":"0","onlinestate":1,"laudstate":0}
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
         * did : 621
         * uid : 960
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-03-19/20170319130111676.jpg
         * nickname : ，
         * vip : 0
         * realname : 0
         * sex : 1
         * age : 21
         * role : S
         * readtimes : 12
         * distance : 1600
         * addtime : 14分钟前
         * content : 附图自己领会
         * pic : ["http://59.110.28.150:888/Uploads/Picture/2017-03-22/20170322104213661.jpg"]
         * laudnum : 0
         * rewardnum : 0
         * comnum : 0
         * is_admin : 0
         * is_hand : 0
         * onlinestate : 1
         * laudstate : 0
         */

        private String did;
        private String uid;
        private String tid;
        private String head_pic;
        private String nickname;
        private String vip;
        private String realname;
        private String sex;
        private String age;
        private String role;
        private String readtimes;
        private double distance;
        private String addtime;
        private String content;
        private String laudnum;
        private String rewardnum;
        private String comnum;
        private String topnum;
        private String is_admin;
        private String is_hand;
        private int onlinestate;
        private int laudstate;
        private ArrayList<String> pic;
        private ArrayList<String> sypic;
        private String recommend;
        private String vipannual;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String charm_val_new;
        private String wealth_val_new;
        private String reportstate;
        private String topictitle;
        private String stickstate;
        private String collectstate;
        private String is_hidden;
        private String dynamic_num;
        private String rdynamic_num;
        private String recommendall;
        private String dynamicstatusouttimes;
        private String hidedstatustimes;
        private String atuid;
        private String atuname;
        private String alltopnums;
        private String usetopnums;
        private String location_switch;
        private String auditMark;
        private String bkvip;
        private String blvip;
        private String playUrl;
        private String  coverUrl;
        private String width;
        private String height;
        private String videoId;
        private String topic_topping_status;
        private String anchor_room_id;
        private String anchor_is_live;
        private String user_level;
        private String anchor_level;
        private String is_likeliar;
        public String getIs_likeliar() {
            return is_likeliar;
        }

        public void setIs_likeliar(String is_likeliar) {
            this.is_likeliar = is_likeliar;
        }



        public String getUser_level() {
            return user_level;
        }

        public void setUser_level(String user_level) {
            this.user_level = user_level;
        }

        public String getAnchor_level() {
            return anchor_level;
        }

        public void setAnchor_level(String anchor_level) {
            this.anchor_level = anchor_level;
        }

        public String getAnchor_room_id() {
            return anchor_room_id;
        }

        public void setAnchor_room_id(String anchor_room_id) {
            this.anchor_room_id = anchor_room_id;
        }

        public String getAnchor_is_live() {
            return anchor_is_live;
        }

        public void setAnchor_is_live(String anchor_is_live) {
            this.anchor_is_live = anchor_is_live;
        }


        public String getTopic_topping_status() {
            return topic_topping_status;
        }

        public void setTopic_topping_status(String topic_topping_status) {
            this.topic_topping_status = topic_topping_status;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getPlayUrl() {
            return playUrl==null?"":playUrl;
//            return "http://tv.aiwujie.com.cn/sv/12996381-16f662039a2/12996381-16f662039a2.mp4";
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public String getCoverUrl() {
            return coverUrl;
//            return "http://39.105.50.200:888/Uploads/Picture/2020-04-30/20200430105622601.jpg";
        }


        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
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

        public String getAuditMark() {
            return auditMark;
        }

        public void setAuditMark(String auditMark) {
            this.auditMark = auditMark;
        }

        public String getLocation_switch() {
            return location_switch;
        }

        public void setLocation_switch(String location_switch) {
            this.location_switch = location_switch;
        }

        public String getAlltopnums() {
            return alltopnums;
        }

        public void setAlltopnums(String alltopnums) {
            this.alltopnums = alltopnums;
        }

        public String getUsetopnums() {
            return usetopnums;
        }

        public void setUsetopnums(String usetopnums) {
            this.usetopnums = usetopnums;
        }

        public String getAtuid() {
            return atuid;
        }

        public void setAtuid(String atuid) {
            this.atuid = atuid;
        }

        public String getAtuname() {
            return atuname;
        }

        public void setAtuname(String atuname) {
            this.atuname = atuname;
        }

        public String getDynamicstatusouttimes() {
            return dynamicstatusouttimes;
        }

        public void setDynamicstatusouttimes(String dynamicstatusouttimes) {
            this.dynamicstatusouttimes = dynamicstatusouttimes;
        }

        public String getHidedstatustimes() {
            return hidedstatustimes;
        }

        public void setHidedstatustimes(String hidedstatustimes) {
            this.hidedstatustimes = hidedstatustimes;
        }

        public String getTopnum() {
            return topnum;
        }

        public void setTopnum(String topnum) {
            this.topnum = topnum;
        }

        public String getRecommendall() {
            return recommendall;
        }

        public void setRecommendall(String recommendall) {
            this.recommendall = recommendall;
        }

        public String getDynamic_num() {
            return dynamic_num;
        }

        public void setDynamic_num(String dynamic_num) {
            this.dynamic_num = dynamic_num;
        }

        public String getRdynamic_num() {
            return rdynamic_num;
        }

        public void setRdynamic_num(String rdynamic_num) {
            this.rdynamic_num = rdynamic_num;
        }

        public String getIs_hidden() {
            return is_hidden;
        }

        public void setIs_hidden(String is_hidden) {
            this.is_hidden = is_hidden;
        }

        public String getCollectstate() {
            return collectstate;
        }

        public void setCollectstate(String collectstate) {
            this.collectstate = collectstate;
        }

        public String getStickstate() {
            return stickstate;
        }

        public void setStickstate(String stickstate) {
            this.stickstate = stickstate;
        }

        public String getTopictitle() {
            return topictitle;
        }

        public void setTopictitle(String topictitle) {
            this.topictitle = topictitle;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }
        public String getReportstate() {
            return reportstate;
        }

        public void setReportstate(String reportstate) {
            this.reportstate = reportstate;
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
        public String getRecommend() {
            return recommend;
        }

        public void setRecommend(String recommend) {
            this.recommend = recommend;
        }

        public ArrayList<String> getSypic() {
            return sypic==null?new ArrayList<String>():sypic;
        }

        public void setSypic(ArrayList<String> sypic) {
            this.sypic = sypic;
        }

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public String getReadtimes() {
            return readtimes;
        }

        public void setReadtimes(String readtimes) {
            this.readtimes = readtimes;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLaudnum() {
            return laudnum;
        }

        public void setLaudnum(String laudnum) {
            this.laudnum = laudnum;
        }

        public String getRewardnum() {
            return rewardnum;
        }

        public void setRewardnum(String rewardnum) {
            this.rewardnum = rewardnum;
        }

        public String getComnum() {
            return comnum;
        }

        public void setComnum(String comnum) {
            this.comnum = comnum;
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

        public int getOnlinestate() {
            return onlinestate;
        }

        public void setOnlinestate(int onlinestate) {
            this.onlinestate = onlinestate;
        }

        public int getLaudstate() {
            return laudstate;
        }

        public void setLaudstate(int laudstate) {
            this.laudstate = laudstate;
        }

        public ArrayList<String> getPic() {
            return pic==null?new ArrayList<String>():pic;
        }

        public void setPic(ArrayList<String> pic) {
            this.pic = pic;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }
    }
}
