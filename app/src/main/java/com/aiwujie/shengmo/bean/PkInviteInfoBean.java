package com.aiwujie.shengmo.bean;

public class PkInviteInfoBean {

    /**
     * retcode : 2000
     * msg : 获取成功
     * data : {"uid":"250385","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg","nickname":"鸿雁传书12","sex":"1","age":"30","charm_val":"23572","wealth_val":"505000","charm_val_switch":"0","wealth_val_switch":"0","camera_switch":"0","beans_current_count":"0","room_id":"333227","is_live":"1","role":"7","watch_sum":"0"}
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
         * uid : 250385
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg
         * nickname : 鸿雁传书12
         * sex : 1
         * age : 30
         * charm_val : 23572
         * wealth_val : 505000
         * charm_val_switch : 0
         * wealth_val_switch : 0
         * camera_switch : 0
         * beans_current_count : 0
         * room_id : 333227
         * is_live : 1
         * role : 7
         * watch_sum : 0
         */

        private String uid;
        private String head_pic;
        private String nickname;
        private String sex;
        private String age;
        private String charm_val;
        private String wealth_val;
        private String charm_val_switch;
        private String wealth_val_switch;
        private String camera_switch;
        private String beans_current_count;
        private String room_id;
        private String is_live;
        private String role;
        private String watch_sum;
        private String level_role;
        private String room_type_name;

        public String getRoom_type_name() {
            return room_type_name;
        }

        public void setRoom_type_name(String room_type_name) {
            this.room_type_name = room_type_name;
        }

        public String getLevel_role() {
            return level_role;
        }

        public void setLevel_role(String level_role) {
            this.level_role = level_role;
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

        public String getCharm_val_switch() {
            return charm_val_switch;
        }

        public void setCharm_val_switch(String charm_val_switch) {
            this.charm_val_switch = charm_val_switch;
        }

        public String getWealth_val_switch() {
            return wealth_val_switch;
        }

        public void setWealth_val_switch(String wealth_val_switch) {
            this.wealth_val_switch = wealth_val_switch;
        }

        public String getCamera_switch() {
            return camera_switch;
        }

        public void setCamera_switch(String camera_switch) {
            this.camera_switch = camera_switch;
        }

        public String getBeans_current_count() {
            return beans_current_count;
        }

        public void setBeans_current_count(String beans_current_count) {
            this.beans_current_count = beans_current_count;
        }

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

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getWatch_sum() {
            return watch_sum;
        }

        public void setWatch_sum(String watch_sum) {
            this.watch_sum = watch_sum;
        }
    }
}
