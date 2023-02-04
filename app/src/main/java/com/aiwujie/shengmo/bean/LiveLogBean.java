package com.aiwujie.shengmo.bean;

import java.util.List;

public class LiveLogBean {

    /**
     * retcode : 2000
     * msg : 成功
     * data : [{"live_log_id":"3463","live_poster":"http://image.aiwujie.com.cn/Uploads/Picture/2022-03-07/20220307105601815.jpg","live_title":"123456ggggg","uid":"407921","room_id":"407921","watchsum":"0","start_time":"1646789327","end_time":"1646789346","time_lenght":"19","beans_current_count":"0","hot_num":"0","like_count":"0","warning_num":"0","watch_num":"0","is_free":"0"}]
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
         * live_log_id : 3463
         * live_poster : http://image.aiwujie.com.cn/Uploads/Picture/2022-03-07/20220307105601815.jpg
         * live_title : 123456ggggg
         * uid : 407921
         * room_id : 407921
         * watchsum : 0
         * start_time : 1646789327
         * end_time : 1646789346
         * time_lenght : 19
         * beans_current_count : 0
         * hot_num : 0
         * like_count : 0
         * warning_num : 0
         * watch_num : 0
         * is_free : 0
         */
        private String id;
        private String live_poster;
        private String live_title;
        private String uid;
        private String room_id;
        private String watchsum;
        private String start_time;
        private String end_time;
        private String time_lenght;
        private String beans_current_count;
        private String hot_num;
        private String like_count;
        private String warning_num;
        private String watch_num;
        private String is_free;
        private String nickname;
        private String sex;
        private String live_beans;
        private String all_live_beans;

        public String getAll_live_beans() {
            return all_live_beans;
        }

        public void setAll_live_beans(String all_live_beans) {
            this.all_live_beans = all_live_beans;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getLive_beans() {
            return live_beans;
        }

        public void setLive_beans(String live_beans) {
            this.live_beans = live_beans;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLive_poster() {
            return live_poster;
        }

        public void setLive_poster(String live_poster) {
            this.live_poster = live_poster;
        }

        public String getLive_title() {
            return live_title;
        }

        public void setLive_title(String live_title) {
            this.live_title = live_title;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getWatchsum() {
            return watchsum;
        }

        public void setWatchsum(String watchsum) {
            this.watchsum = watchsum;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getTime_lenght() {
            return time_lenght;
        }

        public void setTime_lenght(String time_lenght) {
            this.time_lenght = time_lenght;
        }

        public String getBeans_current_count() {
            return beans_current_count;
        }

        public void setBeans_current_count(String beans_current_count) {
            this.beans_current_count = beans_current_count;
        }

        public String getHot_num() {
            return hot_num;
        }

        public void setHot_num(String hot_num) {
            this.hot_num = hot_num;
        }

        public String getLike_count() {
            return like_count;
        }

        public void setLike_count(String like_count) {
            this.like_count = like_count;
        }

        public String getWarning_num() {
            return warning_num;
        }

        public void setWarning_num(String warning_num) {
            this.warning_num = warning_num;
        }

        public String getWatch_num() {
            return watch_num;
        }

        public void setWatch_num(String watch_num) {
            this.watch_num = watch_num;
        }

        public String getIs_free() {
            return is_free;
        }

        public void setIs_free(String is_free) {
            this.is_free = is_free;
        }
    }
}
