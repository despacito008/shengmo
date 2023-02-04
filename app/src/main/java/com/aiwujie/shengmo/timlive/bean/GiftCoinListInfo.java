package com.aiwujie.shengmo.timlive.bean;


import java.util.List;

/**
 * 热度榜单
 */
public class GiftCoinListInfo {

    /**
     * retcode : 2000
     * msg : 排行榜获取成功！
     * data : {"rankingList":[{"uid":"623786","nickname":"iOS工程师测试设备","age":"31","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-09-13/20210913144618620.jpg","sex":"1","role":"~","vip":"0","vipannual":"0","is_admin":"1","svip":"0","svipannual":"0","charm_val":"100","wealth_val_switch":"0","charm_val_switch":"0","coin":314,"rankinginfo":"","wealth_val_new":"1001610","charm_val_new":"100"},{"uid":"623791","nickname":"shengmo5","age":"26","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-09-02/20210902155546582.jpg","sex":"1","role":"M","vip":"1","vipannual":"0","is_admin":"0","svip":"1","svipannual":"1","charm_val":"0","wealth_val_switch":"0","charm_val_switch":"0","coin":20,"rankinginfo":"","wealth_val_new":"999990","charm_val_new":"0"}],"hasmore":0}
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
         * rankingList : [{"uid":"623786","nickname":"iOS工程师测试设备","age":"31","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-09-13/20210913144618620.jpg","sex":"1","role":"~","vip":"0","vipannual":"0","is_admin":"1","svip":"0","svipannual":"0","charm_val":"100","wealth_val_switch":"0","charm_val_switch":"0","coin":314,"rankinginfo":"","wealth_val_new":"1001610","charm_val_new":"100"},{"uid":"623791","nickname":"shengmo5","age":"26","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-09-02/20210902155546582.jpg","sex":"1","role":"M","vip":"1","vipannual":"0","is_admin":"0","svip":"1","svipannual":"1","charm_val":"0","wealth_val_switch":"0","charm_val_switch":"0","coin":20,"rankinginfo":"","wealth_val_new":"999990","charm_val_new":"0"}]
         * hasmore : 0
         */

        private int hasmore;
        private List<RankingListBean> rankingList;
        private RankingListBean anchorInfo;

        public RankingListBean getAnchorInfo() {
            return anchorInfo;
        }

        public void setAnchorInfo(RankingListBean anchorInfo) {
            this.anchorInfo = anchorInfo;
        }

        public int getHasmore() {
            return hasmore;
        }

        public void setHasmore(int hasmore) {
            this.hasmore = hasmore;
        }

        public List<RankingListBean> getRankingList() {
            return rankingList;
        }

        public void setRankingList(List<RankingListBean> rankingList) {
            this.rankingList = rankingList;
        }

        public static class RankingListBean {
            /**
             * uid : 623786
             * nickname : iOS工程师测试设备
             * age : 31
             * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-09-13/20210913144618620.jpg
             * sex : 1
             * role : ~
             * vip : 0
             * vipannual : 0
             * is_admin : 1
             * svip : 0
             * svipannual : 0
             * charm_val : 100
             * wealth_val_switch : 0
             * charm_val_switch : 0
             * coin : 314
             * rankinginfo :
             * wealth_val_new : 1001610
             * charm_val_new : 100
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
            private String rankinginfo;
            private String wealth_val_new;
            private String charm_val_new;
            private String provide_score_name;

            public String getProvide_score_name() {
                return provide_score_name;
            }

            public void setProvide_score_name(String provide_score_name) {
                this.provide_score_name = provide_score_name;
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

            public String getRankinginfo() {
                return rankinginfo;
            }

            public void setRankinginfo(String rankinginfo) {
                this.rankinginfo = rankinginfo;
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
    }
}
