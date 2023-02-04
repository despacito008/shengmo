package com.aiwujie.shengmo.bean;

import java.util.List;

public class LiveRankBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"allamount":"52313","uid":"402586","wealth_val_switch":"0","charm_val_switch":"0","charm_val":"5955","wealth_val":"100384","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-09/20211109162827594.jpg","nickname":"圣魔iOS工程师～","vip":"1","vipannual":"0","realname":"1","sex":"1","age":"27","role":"S","is_admin":"0","is_volunteer":"0","svip":"1","svipannual":"0","bkvip":"0","blvip":"1","rewardeduserinfo":[{"fuid":"402624","allamount":"52008","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-11/20210811175712236.jpg"},{"fuid":"402279","allamount":"313","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2019-09-26/20190926170626351.jpg"},{"fuid":"402586","allamount":"8","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-09/20211109162827594.jpg"}],"markname":"","wealth_val_new":"100384","charm_val_new":"5955"},{"allamount":"898","uid":"402277","wealth_val_switch":"0","charm_val_switch":"0","charm_val":"1307","wealth_val":"0","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-11/20211111120235505.jpg","nickname":"测试号哈1","vip":"1","vipannual":"0","realname":"1","sex":"1","age":"20","role":"SM","is_admin":"0","is_volunteer":"0","svip":"1","svipannual":"0","bkvip":"0","blvip":"0","rewardeduserinfo":[{"fuid":"402277","allamount":"888","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-11/20211111120235505.jpg"},{"fuid":"402583","allamount":"10","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg"}],"markname":"","wealth_val_new":"0","charm_val_new":"1307"},{"allamount":"18","uid":"407943","wealth_val_switch":"0","charm_val_switch":"0","charm_val":"0","wealth_val":"30000","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2019-08-03/2019080318275285.jpg","nickname":"图片规则","vip":"0","vipannual":"0","realname":"0","sex":"1","age":"26","role":"~","is_admin":"0","is_volunteer":"0","svip":"0","svipannual":"0","bkvip":"0","blvip":"1","rewardeduserinfo":[{"fuid":"402624","allamount":"20","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-11/20210811175712236.jpg"}],"markname":"","wealth_val_new":"30000","charm_val_new":"0"}]
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
         * allamount : 52313
         * uid : 402586
         * wealth_val_switch : 0
         * charm_val_switch : 0
         * charm_val : 5955
         * wealth_val : 100384
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-11-09/20211109162827594.jpg
         * nickname : 圣魔iOS工程师～
         * vip : 1
         * vipannual : 0
         * realname : 1
         * sex : 1
         * age : 27
         * role : S
         * is_admin : 0
         * is_volunteer : 0
         * svip : 1
         * svipannual : 0
         * bkvip : 0
         * blvip : 1
         * rewardeduserinfo : [{"fuid":"402624","allamount":"52008","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-11/20210811175712236.jpg"},{"fuid":"402279","allamount":"313","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2019-09-26/20190926170626351.jpg"},{"fuid":"402586","allamount":"8","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-09/20211109162827594.jpg"}]
         * markname :
         * wealth_val_new : 100384
         * charm_val_new : 5955
         */

        private String allamount;
        private String uid;
        private String wealth_val_switch;
        private String charm_val_switch;
        private String charm_val;
        private String wealth_val;
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
        private String bkvip;
        private String blvip;
        private String markname;
        private String wealth_val_new;
        private String charm_val_new;
        private List<RewardeduserinfoBean> rewardeduserinfo;
        private String anchor_level;
        private String user_level;
        private String anchor_room_id;
        private String anchor_is_live;

        public String getAnchor_is_live() {
            return anchor_is_live;
        }

        public void setAnchor_is_live(String anchor_is_live) {
            this.anchor_is_live = anchor_is_live;
        }

        public String getAnchor_room_id() {
            return anchor_room_id;
        }

        public void setAnchor_room_id(String anchor_room_id) {
            this.anchor_room_id = anchor_room_id;
        }


        public String getAnchor_level() {
            return anchor_level;
        }

        public void setAnchor_level(String anchor_level) {
            this.anchor_level = anchor_level;
        }

        public String getUser_level() {
            return user_level;
        }

        public void setUser_level(String user_level) {
            this.user_level = user_level;
        }

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

        public String getWealth_val_switch() {
            return wealth_val_switch;
        }

        public void setWealth_val_switch(String wealth_val_switch) {
            this.wealth_val_switch = wealth_val_switch;
        }

        public String getCharm_val_switch() {
            return charm_val_switch;
        }

        public void setCharm_val_switch(String charm_val_switch) {
            this.charm_val_switch = charm_val_switch;
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

        public String getMarkname() {
            return markname;
        }

        public void setMarkname(String markname) {
            this.markname = markname;
        }

        public String getWealth_val_new() {
            return wealth_val_new;
        }

        public void setWealth_val_new(String wealth_val_new) {
            this.wealth_val_new = wealth_val_new;
        }

        public String getCharm_val_new() {
            return charm_val_new;
        }

        public void setCharm_val_new(String charm_val_new) {
            this.charm_val_new = charm_val_new;
        }

        public List<RewardeduserinfoBean> getRewardeduserinfo() {
            return rewardeduserinfo;
        }

        public void setRewardeduserinfo(List<RewardeduserinfoBean> rewardeduserinfo) {
            this.rewardeduserinfo = rewardeduserinfo;
        }

        public static class RewardeduserinfoBean {
            /**
             * fuid : 402624
             * allamount : 52008
             * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-08-11/20210811175712236.jpg
             */

            private String fuid;
            private String allamount;
            private String head_pic;

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
        }
    }
}
