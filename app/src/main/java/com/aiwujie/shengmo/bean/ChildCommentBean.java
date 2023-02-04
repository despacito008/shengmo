package com.aiwujie.shengmo.bean;

public class ChildCommentBean {

    /**
     * retcode : 2000
     * msg : 回复成功！
     * data : {"commentinfo":{"cmid":"6139505","uid":"407920","otheruid":"402583","nickname":"我","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-03-19/20210319094125716.jpg?x-oss-process=image/resize,p_30","vipannual":"0","is_volunteer":"0","svip":"0","svipannual":"0","content":"好的","sendtime":"2021-03-29 16:43","is_hidden_admin":"0","is_admin":"0","is_hand":"0","vip":"0","bkvip":"0","blvip":"0","atuid":"","atuname":"","likenum":"0","reportstate":0,"is_like":"0","othernickname":"测试99","markname":""}}
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
         * commentinfo : {"cmid":"6139505","uid":"407920","otheruid":"402583","nickname":"我","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-03-19/20210319094125716.jpg?x-oss-process=image/resize,p_30","vipannual":"0","is_volunteer":"0","svip":"0","svipannual":"0","content":"好的","sendtime":"2021-03-29 16:43","is_hidden_admin":"0","is_admin":"0","is_hand":"0","vip":"0","bkvip":"0","blvip":"0","atuid":"","atuname":"","likenum":"0","reportstate":0,"is_like":"0","othernickname":"测试99","markname":""}
         */

        private CommentinfoBean commentinfo;

        public CommentinfoBean getCommentinfo() {
            return commentinfo;
        }

        public void setCommentinfo(CommentinfoBean commentinfo) {
            this.commentinfo = commentinfo;
        }

        public static class CommentinfoBean {
            /**
             * cmid : 6139505
             * uid : 407920
             * otheruid : 402583
             * nickname : 我
             * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-03-19/20210319094125716.jpg?x-oss-process=image/resize,p_30
             * vipannual : 0
             * is_volunteer : 0
             * svip : 0
             * svipannual : 0
             * content : 好的
             * sendtime : 2021-03-29 16:43
             * is_hidden_admin : 0
             * is_admin : 0
             * is_hand : 0
             * vip : 0
             * bkvip : 0
             * blvip : 0
             * atuid :
             * atuname :
             * likenum : 0
             * reportstate : 0
             * is_like : 0
             * othernickname : 测试99
             * markname :
             */

            private String cmid;
            private String uid;
            private String otheruid;
            private String nickname;
            private String head_pic;
            private String vipannual;
            private String is_volunteer;
            private String svip;
            private String svipannual;
            private String content;
            private String sendtime;
            private String is_hidden_admin;
            private String is_admin;
            private String is_hand;
            private String vip;
            private String bkvip;
            private String blvip;
            private String atuid;
            private String atuname;
            private String likenum;
            private int reportstate;
            private String is_like;
            private String othernickname;
            private String markname;

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

            public String getHead_pic() {
                return head_pic;
            }

            public void setHead_pic(String head_pic) {
                this.head_pic = head_pic;
            }

            public String getVipannual() {
                return vipannual;
            }

            public void setVipannual(String vipannual) {
                this.vipannual = vipannual;
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

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getSendtime() {
                return sendtime;
            }

            public void setSendtime(String sendtime) {
                this.sendtime = sendtime;
            }

            public String getIs_hidden_admin() {
                return is_hidden_admin;
            }

            public void setIs_hidden_admin(String is_hidden_admin) {
                this.is_hidden_admin = is_hidden_admin;
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

            public String getVip() {
                return vip;
            }

            public void setVip(String vip) {
                this.vip = vip;
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

            public String getLikenum() {
                return likenum;
            }

            public void setLikenum(String likenum) {
                this.likenum = likenum;
            }

            public int getReportstate() {
                return reportstate;
            }

            public void setReportstate(int reportstate) {
                this.reportstate = reportstate;
            }

            public String getIs_like() {
                return is_like;
            }

            public void setIs_like(String is_like) {
                this.is_like = is_like;
            }

            public String getOthernickname() {
                return othernickname;
            }

            public void setOthernickname(String othernickname) {
                this.othernickname = othernickname;
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
