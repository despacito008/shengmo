package com.tencent.qcloud.tim.tuikit.live.bean;

public class LivePermissionBean {

    /**
     * retcode : 2000
     * msg : 获取成功
     * data : {"try_num":"1","try_msg":"您已经免费看过了，请购买后继续观看哦~","try_time":"10","uid":"402279","room_id":"227521","nickname":"圣魔iOS～工程","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2019-09-26/20190926170626351.jpg","live_title":"圣魔iOS～工程","live_poster":"http://image.aiwujie.com.cn/Uploads/Picture/2019-09-26/20190926170626351.jpg","is_ticket":"1","ticket_beans":"410","is_buy":"0"}
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
         * try_num : 1
         * try_msg : 您已经免费看过了，请购买后继续观看哦~
         * try_time : 10
         * uid : 402279
         * room_id : 227521
         * nickname : 圣魔iOS～工程
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2019-09-26/20190926170626351.jpg
         * live_title : 圣魔iOS～工程
         * live_poster : http://image.aiwujie.com.cn/Uploads/Picture/2019-09-26/20190926170626351.jpg
         * is_ticket : 1
         * ticket_beans : 410
         * is_buy : 0
         */

        private String try_num;
        private String try_msg;
        private String try_time;
        private String uid;
        private String room_id;
        private String nickname;
        private String head_pic;
        private String live_title;
        private String live_poster;
        private String is_ticket;
        private String ticket_beans;
        private String is_buy;
        private String is_screenshot;

        public String getIs_screenshot() {
            return is_screenshot;
        }

        public void setIs_screenshot(String is_screenshot) {
            this.is_screenshot = is_screenshot;
        }

        public String getTry_num() {
            return try_num;
        }

        public void setTry_num(String try_num) {
            this.try_num = try_num;
        }

        public String getTry_msg() {
            return try_msg;
        }

        public void setTry_msg(String try_msg) {
            this.try_msg = try_msg;
        }

        public String getTry_time() {
            return try_time;
        }

        public void setTry_time(String try_time) {
            this.try_time = try_time;
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

        public String getIs_ticket() {
            return is_ticket;
        }

        public void setIs_ticket(String is_ticket) {
            this.is_ticket = is_ticket;
        }

        public String getTicket_beans() {
            return ticket_beans;
        }

        public void setTicket_beans(String ticket_beans) {
            this.ticket_beans = ticket_beans;
        }

        public String getIs_buy() {
            return is_buy;
        }

        public void setIs_buy(String is_buy) {
            this.is_buy = is_buy;
        }
    }
}
