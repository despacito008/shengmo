package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/1/5.
 */
public class SecretStateData {


    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"follow_list_switch":"0","group_list_switch":"0","photo_lock":"1","login_time_switch":"0","black_limit":"1/50"}
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
         * follow_list_switch : 0
         * group_list_switch : 0
         * photo_lock : 1
         * login_time_switch : 0
         * black_limit : 1/50
         */

        private String follow_list_switch;
        private String group_list_switch;
        private String photo_lock;
        private String login_time_switch;
        private String black_limit;
        private String nickname_rule;
        private String location_city_switch;
        private String location_switch;
        private String wealth_val_switch;  //财富值
        private String charm_val_switch;  //魅力值
        private String fans_list_switch;
        private String live_rule;

        public String getLive_rule() {
            return live_rule;
        }

        public void setLive_rule(String live_rule) {
            this.live_rule = live_rule;
        }

        public String getFans_list_switch() {
            return fans_list_switch;
        }

        public void setFans_list_switch(String fans_list_switch) {
            this.fans_list_switch = fans_list_switch;
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



        public String getLocation_city_switch() {
            return location_city_switch;
        }

        public void setLocation_city_switch(String location_city_switch) {
            this.location_city_switch = location_city_switch;
        }

        public String getLocation_switch() {
            return location_switch;
        }

        public void setLocation_switch(String location_switch) {
            this.location_switch = location_switch;
        }

        public String getNickname_rule() {
            return nickname_rule;
        }

        public void setNickname_rule(String nickname_rule) {
            this.nickname_rule = nickname_rule;
        }

        public String getPhoto_rule() {
            return photo_rule;
        }

        public void setPhoto_rule(String photo_rule) {
            this.photo_rule = photo_rule;
        }

        private String photo_rule;//查看相册权限

        public String getComment_rule() {
            return comment_rule;
        }

        public void setComment_rule(String comment_rule) {
            this.comment_rule = comment_rule;
        }

        private String comment_rule;//评论

        public String getDynamic_rule() {
            return dynamic_rule;
        }

        public void setDynamic_rule(String dynamic_rule) {
            this.dynamic_rule = dynamic_rule;
        }

        private String  dynamic_rule;//主页动态查看权限

        public String getFollow_list_switch() {
            return follow_list_switch;
        }

        public void setFollow_list_switch(String follow_list_switch) {
            this.follow_list_switch = follow_list_switch;
        }

        public String getGroup_list_switch() {
            return group_list_switch;
        }

        public void setGroup_list_switch(String group_list_switch) {
            this.group_list_switch = group_list_switch;
        }

        public String getPhoto_lock() {
            return photo_lock;
        }

        public void setPhoto_lock(String photo_lock) {
            this.photo_lock = photo_lock;
        }

        public String getLogin_time_switch() {
            return login_time_switch;
        }

        public void setLogin_time_switch(String login_time_switch) {
            this.login_time_switch = login_time_switch;
        }

        public String getBlack_limit() {
            return black_limit;
        }

        public void setBlack_limit(String black_limit) {
            this.black_limit = black_limit;
        }
    }
}
