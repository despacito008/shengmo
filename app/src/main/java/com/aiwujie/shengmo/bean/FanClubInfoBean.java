package com.aiwujie.shengmo.bean;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: FanClubInfoBean
 * @Author: xmf
 * @CreateDate: 2022/5/31 14:53
 * @Description:
 */
public class FanClubInfoBean {

    /**
     * retcode : 2000
     * msg : 操作成功
     * data : {"uid":"703485","nickname":"你好2022","head_pic":"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLGdq3ibCGJ9bpX2ibk8KdS0HE8MHIx6T0LsySMiaqOVgrfyrWCre4mLZpic8G7RsOXQDZ3QAb4KqWgkg/132","is_hidden_fanclubcard":"0","is_fanclub":0,"fanclub_card":"","is_self":1,"fanclub_name":"你好2022的粉丝团","is_add_fanclub":0,"fanclub_level":0,"member":"0","fanclub_rank":"","member_rank":"","gid":""}
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
         * uid : 703485
         * nickname : 你好2022
         * head_pic : https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLGdq3ibCGJ9bpX2ibk8KdS0HE8MHIx6T0LsySMiaqOVgrfyrWCre4mLZpic8G7RsOXQDZ3QAb4KqWgkg/132
         * is_hidden_fanclubcard : 0
         * is_fanclub : 0
         * fanclub_card :
         * is_self : 1
         * fanclub_name : 你好2022的粉丝团
         * is_add_fanclub : 0
         * fanclub_level : 0
         * member : 0
         * fanclub_rank :
         * member_rank :
         * gid :
         */

        private String uid;
        private String nickname;
        private String head_pic;
        private String is_hidden_fanclubcard;
        private String is_fanclub;
        private String fanclub_card;
        private int is_self;
        private String fanclub_name;
        private int is_add_fanclub;
        private int fanclub_level;
        private String member;
        private String fanclub_rank;
        private String member_rank;
        private String gid;
        private String is_anchor;

        public String getIs_anchor() {
            return is_anchor;
        }

        public void setIs_anchor(String is_anchor) {
            this.is_anchor = is_anchor;
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

        public String getIs_hidden_fanclubcard() {
            return is_hidden_fanclubcard;
        }

        public void setIs_hidden_fanclubcard(String is_hidden_fanclubcard) {
            this.is_hidden_fanclubcard = is_hidden_fanclubcard;
        }

        public String getIs_fanclub() {
            return is_fanclub;
        }

        public void setIs_fanclub(String is_fanclub) {
            this.is_fanclub = is_fanclub;
        }

        public String getFanclub_card() {
            return fanclub_card;
        }

        public void setFanclub_card(String fanclub_card) {
            this.fanclub_card = fanclub_card;
        }

        public int getIs_self() {
            return is_self;
        }

        public void setIs_self(int is_self) {
            this.is_self = is_self;
        }

        public String getFanclub_name() {
            return fanclub_name;
        }

        public void setFanclub_name(String fanclub_name) {
            this.fanclub_name = fanclub_name;
        }

        public int getIs_add_fanclub() {
            return is_add_fanclub;
        }

        public void setIs_add_fanclub(int is_add_fanclub) {
            this.is_add_fanclub = is_add_fanclub;
        }

        public int getFanclub_level() {
            return fanclub_level;
        }

        public void setFanclub_level(int fanclub_level) {
            this.fanclub_level = fanclub_level;
        }

        public String getMember() {
            return member;
        }

        public void setMember(String member) {
            this.member = member;
        }

        public String getFanclub_rank() {
            return fanclub_rank;
        }

        public void setFanclub_rank(String fanclub_rank) {
            this.fanclub_rank = fanclub_rank;
        }

        public String getMember_rank() {
            return member_rank;
        }

        public void setMember_rank(String member_rank) {
            this.member_rank = member_rank;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }
    }
}
