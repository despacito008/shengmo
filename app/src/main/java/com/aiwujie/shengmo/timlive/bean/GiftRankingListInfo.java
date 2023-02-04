package com.aiwujie.shengmo.timlive.bean;


import java.util.List;

/**
 * 礼物贡献榜
 */
public class GiftRankingListInfo {


    /**
     * retcode : 2000
     * msg : 排行榜获取成功！
     * data : {"hourRanking":[{"uid":"623795","nickname":"iOS公里刘丽敏你你","age":"26","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-09-27/20210927143741121.jpg","sex":"1","role":"SM","vip":"0","vipannual":"0","is_admin":"0","svip":"0","svipannual":"0","charm_val":"1341","wealth_val_switch":"0","charm_val_switch":"0","coin":686,"ranking":1,"wealth_val_new":"9990","charm_val_new":"1341","rankinginfo":"领先430热度值"},{"uid":"623786","nickname":"iOS工程师测试设备","age":"31","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-14/20211014203315882.jpg","sex":"1","role":"~","vip":"0","vipannual":"0","is_admin":"1","svip":"0","svipannual":"0","charm_val":"110","wealth_val_switch":"0","charm_val_switch":"0","coin":10,"ranking":3,"wealth_val_new":"1001610","charm_val_new":"110","rankinginfo":"落后246热度值"}],"anchorInfo":{"uid":"623791","nickname":"shengmo5","age":"26","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-09-02/20210902155546582.jpg","sex":"1","role":"M","vip":"1","vipannual":"0","is_admin":"0","svip":"1","svipannual":"1","charm_val":"175","wealth_val_switch":"0","charm_val_switch":"0","coin":256,"ranking":2,"wealth_val_new":"999990","charm_val_new":"175"}}
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

        private RankingListBean anchorInfo;
        private List<RankingListBean> hourRanking;

        public RankingListBean getAnchorInfo() {
            return anchorInfo;
        }

        public void setAnchorInfo(RankingListBean anchorInfo) {
            this.anchorInfo = anchorInfo;
        }

        public List<RankingListBean> getHourRanking() {
            return hourRanking;
        }

        public void setHourRanking(List<RankingListBean> hourRanking) {
            this.hourRanking = hourRanking;
        }

        public static class AnchorInfoBean {
            /**
             * uid : 623791
             * nickname : shengmo5
             * age : 26
             * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-09-02/20210902155546582.jpg
             * sex : 1
             * role : M
             * vip : 1
             * vipannual : 0
             * is_admin : 0
             * svip : 1
             * svipannual : 1
             * charm_val : 175
             * wealth_val_switch : 0
             * charm_val_switch : 0
             * coin : 256
             * ranking : 2
             * wealth_val_new : 999990
             * charm_val_new : 175
             */

            private String uid;
            private String nickname;
            private String age;
            private String head_pic;
            private String sex;
            private String role;
            private String vip;
            private String vipannual;
            private String is_admin;
            private String svip;
            private String svipannual;
            private String charm_val;
            private String wealth_val_switch;
            private String charm_val_switch;
            private int coin;
            private int ranking;
            private String wealth_val_new;
            private String charm_val_new;

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

            public String getIs_admin() {
                return is_admin;
            }

            public void setIs_admin(String is_admin) {
                this.is_admin = is_admin;
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
                return charm_val;
            }

            public void setCharm_val(String charm_val) {
                this.charm_val = charm_val;
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

            public int getCoin() {
                return coin;
            }

            public void setCoin(int coin) {
                this.coin = coin;
            }

            public int getRanking() {
                return ranking;
            }

            public void setRanking(int ranking) {
                this.ranking = ranking;
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
        }

        public static class RankingListBean {
            /**
             * uid : 623795
             * nickname : iOS公里刘丽敏你你
             * age : 26
             * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-09-27/20210927143741121.jpg
             * sex : 1
             * role : SM
             * vip : 0
             * vipannual : 0
             * is_admin : 0
             * svip : 0
             * svipannual : 0
             * charm_val : 1341
             * wealth_val_switch : 0
             * charm_val_switch : 0
             * coin : 686
             * ranking : 1
             * wealth_val_new : 9990
             * charm_val_new : 1341
             * rankinginfo : 领先430热度值
             */

            private String uid;
            private String nickname;
            private String age;
            private String head_pic;
            private String sex;
            private String role;
            private String vip;
            private String vipannual;
            private String is_admin;
            private String svip;
            private String svipannual;
            private String charm_val;
            private String wealth_val_switch;
            private String charm_val_switch;
            private int coin;
            private int ranking;
            private String wealth_val_new;
            private String charm_val_new;
            private String rankinginfo;
            private String is_live;
            private String room_id;

            public String getRoom_id() {
                return room_id;
            }

            public void setRoom_id(String room_id) {
                this.room_id = room_id;
            }

            public String getIs_live() {
                return is_live;
            }

            public void setIs_live(String is_live) {
                this.is_live = is_live;
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

            public String getIs_admin() {
                return is_admin;
            }

            public void setIs_admin(String is_admin) {
                this.is_admin = is_admin;
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
                return charm_val;
            }

            public void setCharm_val(String charm_val) {
                this.charm_val = charm_val;
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

            public int getCoin() {
                return coin;
            }

            public void setCoin(int coin) {
                this.coin = coin;
            }

            public int getRanking() {
                return ranking;
            }

            public void setRanking(int ranking) {
                this.ranking = ranking;
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

            public String getRankinginfo() {
                return rankinginfo;
            }

            public void setRankinginfo(String rankinginfo) {
                this.rankinginfo = rankinginfo;
            }
        }
    }
}
