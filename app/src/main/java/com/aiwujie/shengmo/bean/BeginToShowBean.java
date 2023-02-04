package com.aiwujie.shengmo.bean;

public class BeginToShowBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"nickname":"圣魔圣魔圣","room_id":"402583","live_title":"圣魔圣魔圣急急急急急急急急急急急急急急急人与自然婆婆婆哄你民工民工go哄你民工公民哄你你明明公共公民","live_poster":"http://image.aiwujie.com.cn/Uploads/Picture/2021-12-13/20211213170506646.jpg","room_token":"G3YgeigyOBA3euIxOmrg9RfQi4ezvyAeP9t036lP:YPhCv7SNSqCEJi_WIQ_9ladd6Lc=:eyJhcHBJZCI6ImczbzVlMXNoaiIsInVzZXJJZCI6IjQwMjU4MyIsInJvb21OYW1lIjoiNDAyNTgzIiwicGVybWlzc2lvbiI6ImFkbWluIiwiZXhwaXJlQXQiOjE2NDAwNzUwMTV9"}
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
         * nickname : 圣魔圣魔圣
         * room_id : 402583
         * live_title : 圣魔圣魔圣急急急急急急急急急急急急急急急人与自然婆婆婆哄你民工民工go哄你民工公民哄你你明明公共公民
         * live_poster : http://image.aiwujie.com.cn/Uploads/Picture/2021-12-13/20211213170506646.jpg
         * room_token : G3YgeigyOBA3euIxOmrg9RfQi4ezvyAeP9t036lP:YPhCv7SNSqCEJi_WIQ_9ladd6Lc=:eyJhcHBJZCI6ImczbzVlMXNoaiIsInVzZXJJZCI6IjQwMjU4MyIsInJvb21OYW1lIjoiNDAyNTgzIiwicGVybWlzc2lvbiI6ImFkbWluIiwiZXhwaXJlQXQiOjE2NDAwNzUwMTV9
         */

        private String nickname;
        private String room_id;
        private String live_title;
        private String live_poster;
        private String room_token;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getLive_title() {
            return live_title;
        }

        public void setLive_title(String live_title) {
            this.live_title = live_title;
        }

        public String getLive_poster() {
            return live_poster;
        }

        public void setLive_poster(String live_poster) {
            this.live_poster = live_poster;
        }

        public String getRoom_token() {
            return room_token;
        }

        public void setRoom_token(String room_token) {
            this.room_token = room_token;
        }
    }
}
