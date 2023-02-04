package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/6/28.
 */

public class RankRegalTowData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"fuid":"12","allamount":"11554","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-16/20170616143502250.jpg","nickname":"小清新","vip":"1","vipannual":"0","realname":"1","sex":"1","age":"25","role":"S","is_admin":"1","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"11875","rewarduserinfo":{"allamount":"11554","uid":"7598","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-17/20170617135111787.jpg"}},{"fuid":"7598","allamount":"906","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-17/20170617135111787.jpg","nickname":"依人","vip":"1","vipannual":"0","realname":"0","sex":"2","age":"27","role":"M","is_admin":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"84","rewarduserinfo":{"allamount":"906","uid":"12","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-16/20170616143502250.jpg"}}]
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
         * fuid : 12
         * allamount : 11554
         * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-06-16/20170616143502250.jpg
         * nickname : 小清新
         * vip : 1
         * vipannual : 0
         * realname : 1
         * sex : 1
         * age : 25
         * role : S
         * is_admin : 1
         * is_volunteer : 0
         * svip : 0
         * svipannual : 0
         * charm_val : 11875
         * rewarduserinfo : {"allamount":"11554","uid":"7598","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-17/20170617135111787.jpg"}
         */

        private String fuid;
        private String allamount;
        private String head_pic;
        private String nickname;
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
        private RewarduserinfoBean rewarduserinfo;
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

        public String getFuid() {
            return fuid;
        }

        public void setFuid(String fuid) {
            this.fuid = fuid;
        }

        public String getAllamount() {
            return allamount;
        }

        public void setAllamount(String allamount) {
            this.allamount = allamount;
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

        public RewarduserinfoBean getRewarduserinfo() {
            return rewarduserinfo;
        }

        public void setRewarduserinfo(RewarduserinfoBean rewarduserinfo) {
            this.rewarduserinfo = rewarduserinfo;
        }

        public static class RewarduserinfoBean {
            /**
             * allamount : 11554
             * uid : 7598
             * head_pic : http://59.110.28.150:888/Uploads/Picture/2017-06-17/20170617135111787.jpg
             */

            private String allamount;
            private String uid;
            private String head_pic;

            public String getAllamount() {
                return allamount;
            }

            public void setAllamount(String allamount) {
                this.allamount = allamount;
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
        }
    }
}
