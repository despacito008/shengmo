package com.aiwujie.shengmo.timlive.bean;

import java.io.Serializable;

public class LiveRoomInfo implements Serializable {
        /**
         * retcode : 2000
         * msg : 获取成功！
         * data : [{"uid":"564","nickname":"深浅。","age":"21","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-05-27/20170527113901611.jpg","sex":"2","role":"M","vip":"0","vipannual":"0","realname":"0","last_login_time":"1505179994","distance":"0.00","province":"北京市","city":"北京市","lat":"39.871319","lng":"116.689484","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","onlinestate":1},{"uid":"12","nickname":"小清新","age":"25","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-16/20170616143502250.jpg","sex":"1","role":"S","vip":"1","vipannual":"0","realname":"1","last_login_time":"1505178353","distance":"0.01","province":"北京市","city":"北京市","lat":"39.871338","lng":"116.689354","is_admin":"1","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"8","wealth_val":"0","onlinestate":1},{"uid":"175","nickname":"深藏功与名.3","age":"24","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-05-19/20170519175817473.jpg","sex":"1","role":"~","vip":"1","vipannual":"1","realname":"1","last_login_time":"1505178509","distance":"0.02","province":"北京市","city":"北京市","lat":"39.871426","lng":"116.689240","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"6","wealth_val":"0","onlinestate":1},{"uid":"2604","nickname":"CD主","age":"28","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-04-25/20170425180808736.jpg","sex":"3","role":"S","vip":"0","vipannual":"0","realname":"0","last_login_time":"1505113831","distance":"0.02","province":"北京市","city":"北京市","lat":"39.871422","lng":"116.689339","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","onlinestate":1},{"uid":"13","nickname":"斯慕群组管理员","age":"29","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-06/20170606230045882.jpg","sex":"1","role":"S","vip":"1","vipannual":"0","realname":"1","last_login_time":"1504923658","distance":"0.03","province":"北京市","city":"北京市","lat":"39.871391","lng":"116.689156","is_admin":"1","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","onlinestate":1},{"uid":"25882","nickname":"爱好者157","age":"25","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-05-19/20170519210756572.jpg","sex":"1","role":"~","vip":"0","vipannual":"0","realname":"0","last_login_time":"1505176125","distance":"0.03","province":"北京市","city":"北京市","lat":"39.871410","lng":"116.689140","is_admin":"0","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"0","wealth_val":"0","onlinestate":1},{"uid":"35240","nickname":"斯慕客服小魔","age":"27","head_pic":"http://59.110.28.150:888/Uploads/Picture/2017-06-28/20170628122832627.jpg","sex":"2","role":"~","vip":"1","vipannual":"1","realname":"1","last_login_time":"1505121294","distance":"0.03","province":"北京市","city":"北京市","lat":"39.871437","lng":"116.689163","is_admin":"1","is_hand":"0","is_volunteer":"0","svip":"0","svipannual":"0","charm_val":"3","wealth_val":"42","onlinestate":1}]
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

        public static class DataBean implements Serializable {
            /**
             * {
             * "retcode":2000,
             * "msg":"获取主播信息成功！",
             * "data":{
             * "uid":"623093",
             * "is_live":"1",
             * "watchsum":"0",
             * "nickname":"Android.",
             * "head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-08-27/20210827120208312.jpg",
             * "room_id":"10003",
             * "sex":"2",
             * "follow_state":2
             * }
             * }
             */

            private String uid;
            private String nickname;
            private String head_pic;
            private String sex;
            private String is_live;
            private String watchsum;
            private String room_id;
            private String follow_state;
            private String age;
            private String beans_current_count;
            private String role;
            private String live_title;

            public void setLive_title(String live_title) {
                this.live_title = live_title;
            }

            public String getLive_title() {
                return live_title;
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

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getIs_live() {
                return is_live;
            }

            public void setIs_live(String is_live) {
                this.is_live = is_live;
            }

            public String getWatchsum() {
                return watchsum;
            }

            public void setWatchsum(String watchsum) {
                this.watchsum = watchsum;
            }

            public String getRoom_id() {
                return room_id;
            }

            public void setRoom_id(String room_id) {
                this.room_id = room_id;
            }

            public String getFollow_state() {
                return follow_state;
            }

            public void setFollow_state(String follow_state) {
                this.follow_state = follow_state;
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

            public String getBeans_current_count() {
                return beans_current_count;
            }

            public void setBeans_current_count(String beans_current_count) {
                this.beans_current_count = beans_current_count;
            }
            public DataBean(String uid, String nickname, String head_pic, String sex, String is_live, String watchsum, String room_id, String follow_state,String age,String role,String beans_current_count) {
                this.uid = uid;
                this.nickname = nickname;
                this.head_pic = head_pic;
                this.sex = sex;
                this.is_live = is_live;
                this.watchsum = watchsum;
                this.room_id = room_id;
                this.follow_state = follow_state;
                this.age = age;
                this.role = role;
                this.beans_current_count = beans_current_count;
            }

            @Override
            public String toString() {
                return "DataBean{" +
                        "uid='" + uid + '\'' +
                        ", nickname='" + nickname + '\'' +
                        ", head_pic='" + head_pic + '\'' +
                        ", sex='" + sex + '\'' +
                        ", is_live='" + is_live + '\'' +
                        ", watchsum='" + watchsum + '\'' +
                        ", room_id='" + room_id + '\'' +
                        ", follow_state='" + follow_state + '\'' +
                        ", age='" + age + "\'" +
                        ",role='" + role + "\'" +
                        ",beans_current_count='" + beans_current_count + "\'" +
                        '}';
            }
        }
}