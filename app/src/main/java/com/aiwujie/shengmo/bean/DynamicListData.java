package com.aiwujie.shengmo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 290243232 on 2017/1/21.
 */
public class DynamicListData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"did":"11","uid":"11","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","nickname":"哈哈哈是吧","vip":"1","realname":"1","sex":"1","age":"27","role":"S","readtimes":"50","distance":"1.93","addtime":"1天前","content":"力强卢全江力强卢全江入口母女力强宋菊宋菊力强Mr 宋菊宋菊力强力强宋菊力强力强力强宋菊力强力强力强力强卢全江肃然起敬力强卢全江了力强力强宋菊力强力强力强力强力强j宋菊力强力强力强力强力强力强力强力强力强力强","pic":[],"laudnum":"2","rewardnum":"3","comnum":"3","onlinestate":0,"laudstate":1,"comArr":[{"cmid":"11","uid":"11","otheruid":"12","nickname":"我","othernickname":"屌不屌","content":""},{"cmid":"13","uid":"11","otheruid":"12","nickname":"我","othernickname":"屌不屌","content":""}]},{"did":"10","uid":"11","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","nickname":"哈哈哈是吧","vip":"1","realname":"1","sex":"1","age":"27","role":"S","readtimes":"17","distance":"1.93","addtime":"1天前","content":"卢建奎旅途托某兔兔兔卢建奎了卢建奎巨兔兔卢建奎咯哦哦卢建奎卢建奎卢建奎卢建奎卢建奎卢建奎卢建奎了卢建奎卢建奎","pic":[],"laudnum":"1","rewardnum":"2","comnum":"0","onlinestate":0,"laudstate":0,"comArr":[]},{"did":"7","uid":"11","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","nickname":"哈哈哈是吧","vip":"1","realname":"1","sex":"1","age":"27","role":"S","readtimes":"13","distance":"1.93","addtime":"2天前","content":"","pic":["http://59.110.28.150:888/Uploads/Picture/2017-01-21/20170121195552764.jpg","http://59.110.28.150:888/Uploads/Picture/2017-01-21/20170121195554462.jpg"],"laudnum":"1","rewardnum":"0","comnum":"0","onlinestate":0,"laudstate":0,"comArr":[]},{"did":"5","uid":"11","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","nickname":"哈哈哈是吧","vip":"1","realname":"1","sex":"1","age":"27","role":"S","readtimes":"9","distance":"1.64","addtime":"2天前","content":"是滴","pic":["http://59.110.28.150:888/Uploads/Picture/2017-01-21/2017012119420118.jpg","http://59.110.28.150:888/Uploads/Picture/2017-01-21/20170121194205820.jpg","http://59.110.28.150:888/Uploads/Picture/2017-01-21/20170121194212644.jpg","http://59.110.28.150:888/Uploads/Picture/2017-01-21/20170121194217819.jpg","http://59.110.28.150:888/Uploads/Picture/2017-01-21/20170121194222407.jpg"],"laudnum":"1","rewardnum":"0","comnum":"0","onlinestate":0,"laudstate":0,"comArr":[]},{"did":"3","uid":"11","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg","nickname":"哈哈哈是吧","vip":"1","realname":"1","sex":"1","age":"27","role":"S","readtimes":"0","distance":"1.93","addtime":"2天前","content":"哈哈","pic":[],"laudnum":"1","rewardnum":"0","comnum":"0","onlinestate":0,"laudstate":0,"comArr":[]}]
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

    public static class DataBean  implements Serializable {
        /**
         * did : 11
         * uid : 11
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-01-14/2017011415172656.jpg
         * nickname : 哈哈哈是吧
         * vip : 1
         * realname : 1
         * sex : 1
         * age : 27
         * role : S
         * readtimes : 50
         * distance : 1.93
         * addtime : 1天前
         * content : 力强卢全江力强卢全江入口母女力强宋菊宋菊力强Mr 宋菊宋菊力强力强宋菊力强力强力强宋菊力强力强力强力强卢全江肃然起敬力强卢全江了力强力强宋菊力强力强力强力强力强j宋菊力强力强力强力强力强力强力强力强力强力强
         * pic : []
         * laudnum : 2
         * rewardnum : 3
         * comnum : 3
         * onlinestate : 0
         * laudstate : 1
         * comArr : [{"cmid":"11","uid":"11","otheruid":"12","nickname":"我","othernickname":"屌不屌","content":""},{"cmid":"13","uid":"11","otheruid":"12","nickname":"我","othernickname":"屌不屌","content":""}]
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
        private String distance;
        private String addtime;
        private String content;
        private String laudnum;
        private String rewardnum;
        private String comnum;
        private String topnum;
        private int onlinestate;
        private String laudstate;
        private ArrayList<String> pic;
        private ArrayList<String> sypic;
        private List<ComArrBean> comArr;
        private String is_admin;
        private String is_hand;
        private String recommend;
        private String vipannual;
        private String is_volunteer;
        private String svip;
        private String svipannual;
        private String charm_val_new;
        private String wealth_val_new;
        private String topictitle;
        private String commenttime;
        private String stickstate;
        private String recommendall;
        private String is_hidden;
        private String dynamic_num;
        private String rdynamic_num;
        private String atuid;
        private String atuname;
        private String alltopnums;
        private String usetopnums;
        private String location_switch;
        private String auditMark;
        private String bkvip;
        private String blvip;
        private String playUrl;
        private String coverUrl;
        private String width;
        private String height;
        private  int follow_state;
        private String anchor_room_id;
        private String anchor_is_live;
        private String user_level;
        private String anchor_level;
        private String realids;

        public String getIs_likeliar() {
            return is_likeliar;
        }

        public void setIs_likeliar(String is_likeliar) {
            this.is_likeliar = is_likeliar;
        }

        private String is_likeliar;

        public String getRealids() {
            return realids;
        }

        public void setRealids(String realids) {
            this.realids = realids;
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


        public int getFollow_state() {
            return follow_state;
        }

        public void setFollow_state(int follow_state) {
            this.follow_state = follow_state;
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

        public String getTopnum() {
            return topnum;
        }

        public void setTopnum(String topnum) {
            this.topnum = topnum;
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

        public String getRecommendall() {
            return recommendall;
        }

        public void setRecommendall(String recommendall) {
            this.recommendall = recommendall;
        }

        public String getStickstate() {
            return stickstate;
        }

        public void setStickstate(String stickstate) {
            this.stickstate = stickstate;
        }

        public String getCommenttime() {
            return commenttime;
        }

        public void setCommenttime(String commenttime) {
            this.commenttime = commenttime;
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
            return sypic;
        }

        public void setSypic(ArrayList<String> sypic) {
            this.sypic = sypic;
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

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
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

        public int getOnlinestate() {
            return onlinestate;
        }

        public void setOnlinestate(int onlinestate) {
            this.onlinestate = onlinestate;
        }

        public String getLaudstate() {
            return laudstate;
        }

        public void setLaudstate(String laudstate) {
            this.laudstate = laudstate;
        }

        public ArrayList<String> getPic() {
            return pic;
        }

        public void setPic(ArrayList<String> pic) {
            this.pic = pic;
        }

        public List<ComArrBean> getComArr() {
            return comArr;
        }

        public void setComArr(List<ComArrBean> comArr) {
            this.comArr = comArr;
        }

        public static class ComArrBean implements Serializable {
            /**
             * cmid : 11
             * uid : 11
             * otheruid : 12
             * nickname : 我
             * othernickname : 屌不屌
             * content :
             */

            private String cmid;
            private String uid;
            private String otheruid;
            private String nickname;
            private String othernickname;
            private String content;

            public String getCmid() {
                return cmid;
            }

            public void setCmid(String cmid) {
                this.cmid = cmid;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getOtheruid() {
                return otheruid;
            }

            public void setOtheruid(String otheruid) {
                this.otheruid = otheruid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getOthernickname() {
                return othernickname;
            }

            public void setOthernickname(String othernickname) {
                this.othernickname = othernickname;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
