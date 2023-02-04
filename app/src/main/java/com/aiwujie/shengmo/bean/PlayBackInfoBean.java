package com.aiwujie.shengmo.bean;

import java.util.List;

public class PlayBackInfoBean {

    /**
     * retcode : 2000
     * msg : success
     * data : {"is_free":"0","is_del":"0","video_price":"0","is_pay":"1","title":"但愿人长久","introduce":"","video_list":[{"episode_url":"http://1500008850.vod2.myqcloud.com/6c9bf67dvodcq1500008850/c6e226e4387702299976029645/f0.flv","episode_title":"第1场"}],"live_poster":"http://image.aiwujie.com.cn/Uploads/Picture/2021-12-05/20211205131213344.jpg","watch_num":"194","all_live_beans":0,"nickname":"阿冬啊","uid":"701511","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-12-05/20211205131213344.jpg","sex":"2","role":"~","age":"122","addtime":"2022-05-04 11:29","follow_state":2,"live_log_info":{"id":"44751","uid":"701511","room_id":"242649","live_poster":"http://image.aiwujie.com.cn/Uploads/Picture/2021-12-05/20211205131213344.jpg","live_title":"但愿人长久","watchsum":"175","start_time":"2022-04-28 19:57:59","end_time":"2022-04-28 21:01:28","time_lenght":"1小时3分","beans_current_count":"24","hot_num":"382","like_count":"0","barrage_num":"0","warning_num":"0","is_prohibition":"0","is_downcast":"0","is_live":"0","is_record":"1","is_del":"0","is_free":"0","live_beans":"0","watch_num":"194","addtime":"1651634983","is_news":"0","is_admin_hidden":"0","nickname":"阿冬啊","sex":"2"}}
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
         * is_free : 0
         * is_del : 0
         * video_price : 0
         * is_pay : 1
         * title : 但愿人长久
         * introduce :
         * video_list : [{"episode_url":"http://1500008850.vod2.myqcloud.com/6c9bf67dvodcq1500008850/c6e226e4387702299976029645/f0.flv","episode_title":"第1场"}]
         * live_poster : http://image.aiwujie.com.cn/Uploads/Picture/2021-12-05/20211205131213344.jpg
         * watch_num : 194
         * all_live_beans : 0
         * nickname : 阿冬啊
         * uid : 701511
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-12-05/20211205131213344.jpg
         * sex : 2
         * role : ~
         * age : 122
         * addtime : 2022-05-04 11:29
         * follow_state : 2
         * live_log_info : {"id":"44751","uid":"701511","room_id":"242649","live_poster":"http://image.aiwujie.com.cn/Uploads/Picture/2021-12-05/20211205131213344.jpg","live_title":"但愿人长久","watchsum":"175","start_time":"2022-04-28 19:57:59","end_time":"2022-04-28 21:01:28","time_lenght":"1小时3分","beans_current_count":"24","hot_num":"382","like_count":"0","barrage_num":"0","warning_num":"0","is_prohibition":"0","is_downcast":"0","is_live":"0","is_record":"1","is_del":"0","is_free":"0","live_beans":"0","watch_num":"194","addtime":"1651634983","is_news":"0","is_admin_hidden":"0","nickname":"阿冬啊","sex":"2"}
         */

        private String is_free;
        private String is_del;
        private String video_price;
        private String is_pay;
        private String title;
        private String introduce;
        private String live_poster;
        private String watch_num;
        private String all_live_beans;
        private String nickname;
        private String uid;
        private String head_pic;
        private String sex;
        private String role;
        private String age;
        private String addtime;
        private String follow_state;
        private LiveLogInfoBean live_log_info;
        private List<VideoListBean> video_list;
        private String comment_num;

        public String getComment_num() {
            return comment_num;
        }

        public void setComment_num(String comment_num) {
            this.comment_num = comment_num;
        }

        public String getIs_free() {
            return is_free;
        }

        public void setIs_free(String is_free) {
            this.is_free = is_free;
        }

        public String getIs_del() {
            return is_del;
        }

        public void setIs_del(String is_del) {
            this.is_del = is_del;
        }

        public String getVideo_price() {
            return video_price;
        }

        public void setVideo_price(String video_price) {
            this.video_price = video_price;
        }

        public String getIs_pay() {
            return is_pay;
        }

        public void setIs_pay(String is_pay) {
            this.is_pay = is_pay;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getLive_poster() {
            return live_poster;
        }

        public void setLive_poster(String live_poster) {
            this.live_poster = live_poster;
        }

        public String getWatch_num() {
            return watch_num;
        }

        public void setWatch_num(String watch_num) {
            this.watch_num = watch_num;
        }

        public String getAll_live_beans() {
            return all_live_beans;
        }

        public void setAll_live_beans(String all_live_beans) {
            this.all_live_beans = all_live_beans;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getFollow_state() {
            return follow_state;
        }

        public void setFollow_state(String follow_state) {
            this.follow_state = follow_state;
        }

        public LiveLogInfoBean getLive_log_info() {
            return live_log_info;
        }

        public void setLive_log_info(LiveLogInfoBean live_log_info) {
            this.live_log_info = live_log_info;
        }

        public List<VideoListBean> getVideo_list() {
            return video_list;
        }

        public void setVideo_list(List<VideoListBean> video_list) {
            this.video_list = video_list;
        }

        public static class LiveLogInfoBean {
            /**
             * id : 44751
             * uid : 701511
             * room_id : 242649
             * live_poster : http://image.aiwujie.com.cn/Uploads/Picture/2021-12-05/20211205131213344.jpg
             * live_title : 但愿人长久
             * watchsum : 175
             * start_time : 2022-04-28 19:57:59
             * end_time : 2022-04-28 21:01:28
             * time_lenght : 1小时3分
             * beans_current_count : 24
             * hot_num : 382
             * like_count : 0
             * barrage_num : 0
             * warning_num : 0
             * is_prohibition : 0
             * is_downcast : 0
             * is_live : 0
             * is_record : 1
             * is_del : 0
             * is_free : 0
             * live_beans : 0
             * watch_num : 194
             * addtime : 1651634983
             * is_news : 0
             * is_admin_hidden : 0
             * nickname : 阿冬啊
             * sex : 2
             */

            private String id;
            private String uid;
            private String room_id;
            private String live_poster;
            private String live_title;
            private String watchsum;
            private String start_time;
            private String end_time;
            private String time_lenght;
            private String beans_current_count;
            private String hot_num;
            private String like_count;
            private String barrage_num;
            private String warning_num;
            private String is_prohibition;
            private String is_downcast;
            private String is_live;
            private String is_record;
            private String is_del;
            private String is_free;
            private String live_beans;
            private String watch_num;
            private String addtime;
            private String is_news;
            private String is_admin_hidden;
            private String nickname;
            private String sex;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getBarrage_num() {
                return barrage_num;
            }

            public void setBarrage_num(String barrage_num) {
                this.barrage_num = barrage_num;
            }

            public String getWarning_num() {
                return warning_num;
            }

            public void setWarning_num(String warning_num) {
                this.warning_num = warning_num;
            }

            public String getIs_prohibition() {
                return is_prohibition;
            }

            public void setIs_prohibition(String is_prohibition) {
                this.is_prohibition = is_prohibition;
            }

            public String getIs_downcast() {
                return is_downcast;
            }

            public void setIs_downcast(String is_downcast) {
                this.is_downcast = is_downcast;
            }

            public String getIs_live() {
                return is_live;
            }

            public void setIs_live(String is_live) {
                this.is_live = is_live;
            }

            public String getIs_record() {
                return is_record;
            }

            public void setIs_record(String is_record) {
                this.is_record = is_record;
            }

            public String getIs_del() {
                return is_del;
            }

            public void setIs_del(String is_del) {
                this.is_del = is_del;
            }

            public String getIs_free() {
                return is_free;
            }

            public void setIs_free(String is_free) {
                this.is_free = is_free;
            }

            public String getLive_beans() {
                return live_beans;
            }

            public void setLive_beans(String live_beans) {
                this.live_beans = live_beans;
            }

            public String getWatch_num() {
                return watch_num;
            }

            public void setWatch_num(String watch_num) {
                this.watch_num = watch_num;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getIs_news() {
                return is_news;
            }

            public void setIs_news(String is_news) {
                this.is_news = is_news;
            }

            public String getIs_admin_hidden() {
                return is_admin_hidden;
            }

            public void setIs_admin_hidden(String is_admin_hidden) {
                this.is_admin_hidden = is_admin_hidden;
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
        }

        public static class VideoListBean {
            /**
             * episode_url : http://1500008850.vod2.myqcloud.com/6c9bf67dvodcq1500008850/c6e226e4387702299976029645/f0.flv
             * episode_title : 第1场
             */

            private String episode_url;
            private String episode_title;

            public String getEpisode_url() {
                return episode_url;
            }

            public void setEpisode_url(String episode_url) {
                this.episode_url = episode_url;
            }

            public String getEpisode_title() {
                return episode_title;
            }

            public void setEpisode_title(String episode_title) {
                this.episode_title = episode_title;
            }
        }
    }
}
