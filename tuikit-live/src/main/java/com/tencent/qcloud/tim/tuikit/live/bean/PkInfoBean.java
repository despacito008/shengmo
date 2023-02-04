package com.tencent.qcloud.tim.tuikit.live.bean;

import java.util.List;

public class PkInfoBean {
    /**
     * current : {"uid":"250385","nickname":"鸿雁传书12","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg","room_id":"333227","follow_state":3,"pk_score":8,"level_role":"7","pk_top":[{"uid":"402583","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg"},{"uid":"250385","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg"}],"pk_start_time":"1639041057","is_win":1}
     * other : {"uid":"407943","nickname":"测试一下吧","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-19/20211119173223394.jpg","room_id":"407943","follow_state":3,"pk_score":2,"level_role":"7","pk_top":[{"uid":"402583","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg"}],"pk_start_time":"1639041057","is_win":0}
     */

    private PkInfoBean.CurrentBean current;
    private PkInfoBean.OtherBean other;
    private int is_pk; //是否在pk
    private int pk_status;  //pk状态 1-pk时间  2-惩罚时间

    public int getPk_status() {
        return pk_status;
    }

    public void setPk_status(int pk_status) {
        this.pk_status = pk_status;
    }

    public int getIs_pk() {
        return is_pk;
    }

    public void setIs_pk(int is_pk) {
        this.is_pk = is_pk;
    }

    public PkInfoBean.CurrentBean getCurrent() {
        return current;
    }

    public void setCurrent(PkInfoBean.CurrentBean current) {
        this.current = current;
    }

    public PkInfoBean.OtherBean getOther() {
        return other;
    }

    public void setOther(PkInfoBean.OtherBean other) {
        this.other = other;
    }

    public static class CurrentBean {
        /**
         * uid : 250385
         * nickname : 鸿雁传书12
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg
         * room_id : 333227
         * follow_state : 3
         * pk_score : 8
         * level_role : 7
         * pk_top : [{"uid":"402583","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg"},{"uid":"250385","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg"}]
         * pk_start_time : 1639041057
         * is_win : 1
         */

        private String uid;
        private String nickname;
        private String head_pic;
        private String room_id;
        private int follow_state;
        private int pk_score;
        private String level_role;
        private String pk_start_time;
        private int is_win;
        private int pk_end_pk_time_second;
        private int pk_end_punish_time_second;

        public int getPk_end_pk_time_second() {
            return pk_end_pk_time_second;
        }

        public void setPk_end_pk_time_second(int pk_end_pk_time_second) {
            this.pk_end_pk_time_second = pk_end_pk_time_second;
        }

        public int getPk_end_punish_time_second() {
            return pk_end_punish_time_second;
        }

        public void setPk_end_punish_time_second(int pk_end_punish_time_second) {
            this.pk_end_punish_time_second = pk_end_punish_time_second;
        }

        private List<PkInfoBean.CurrentBean.PkTopBean> pk_top;

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

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public int getFollow_state() {
            return follow_state;
        }

        public void setFollow_state(int follow_state) {
            this.follow_state = follow_state;
        }

        public int getPk_score() {
            return pk_score;
        }

        public void setPk_score(int pk_score) {
            this.pk_score = pk_score;
        }

        public String getLevel_role() {
            return level_role;
        }

        public void setLevel_role(String level_role) {
            this.level_role = level_role;
        }

        public String getPk_start_time() {
            return pk_start_time;
        }

        public void setPk_start_time(String pk_start_time) {
            this.pk_start_time = pk_start_time;
        }

        public int getIs_win() {
            return is_win;
        }

        public void setIs_win(int is_win) {
            this.is_win = is_win;
        }

        public List<PkInfoBean.CurrentBean.PkTopBean> getPk_top() {
            return pk_top;
        }

        public void setPk_top(List<PkInfoBean.CurrentBean.PkTopBean> pk_top) {
            this.pk_top = pk_top;
        }

        public static class PkTopBean {
            /**
             * uid : 402583
             * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg
             */

            private String uid;
            private String head_pic;

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
        }
    }

    public static class OtherBean {
        /**
         * uid : 407943
         * nickname : 测试一下吧
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-11-19/20211119173223394.jpg
         * room_id : 407943
         * follow_state : 3
         * pk_score : 2
         * level_role : 7
         * pk_top : [{"uid":"402583","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg"}]
         * pk_start_time : 1639041057
         * is_win : 0
         */

        private String uid;
        private String nickname;
        private String head_pic;
        private String room_id;
        private int follow_state;
        private int pk_score;
        private String level_role;
        private String pk_start_time;
        private int is_win;
        private List<PkInfoBean.OtherBean.PkTopBeanX> pk_top;

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

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public int getFollow_state() {
            return follow_state;
        }

        public void setFollow_state(int follow_state) {
            this.follow_state = follow_state;
        }

        public int getPk_score() {
            return pk_score;
        }

        public void setPk_score(int pk_score) {
            this.pk_score = pk_score;
        }

        public String getLevel_role() {
            return level_role;
        }

        public void setLevel_role(String level_role) {
            this.level_role = level_role;
        }

        public String getPk_start_time() {
            return pk_start_time;
        }

        public void setPk_start_time(String pk_start_time) {
            this.pk_start_time = pk_start_time;
        }

        public int getIs_win() {
            return is_win;
        }

        public void setIs_win(int is_win) {
            this.is_win = is_win;
        }

        public List<PkInfoBean.OtherBean.PkTopBeanX> getPk_top() {
            return pk_top;
        }

        public void setPk_top(List<PkInfoBean.OtherBean.PkTopBeanX> pk_top) {
            this.pk_top = pk_top;
        }

        public static class PkTopBeanX {
            /**
             * uid : 402583
             * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg
             */

            private String uid;
            private String head_pic;

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
        }
    }
}
