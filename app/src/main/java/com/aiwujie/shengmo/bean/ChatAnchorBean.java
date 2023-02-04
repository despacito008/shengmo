package com.aiwujie.shengmo.bean;

import java.util.List;

public class ChatAnchorBean {

    /**
     * retcode : 2000
     * msg : 成功
     * data : [{"uid":"407943","vip":"1","vipannual":"0","is_admin":"0","is_hidden_admin":"0","svip":"1","svipannual":"0","watchsum":"0","live_title":"测试一下吧1","live_poster":"http://image.aiwujie.com.cn/Uploads/Picture/2022-01-14/20220114162856448.jpg","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-19/20211119173223394.jpg","is_live":"0","room_id":"407943","age":"26","role":"~","sex":"1","nickname":"测试一下吧","is_interaction":"0","camera_switch":"0","anchor_status":"3","is_ticket":"0","ticket_beans":"0","is_pwd":"0","room_pwd":"","chat_status":"1","chat_voice_beans":"200","chat_video_beans":"30","live_chat_status":"1"},{"uid":"407921","vip":"1","vipannual":"0","is_admin":"0","is_hidden_admin":"0","svip":"1","svipannual":"1","watchsum":"0","live_title":"123456ggggg","live_poster":"http://image.aiwujie.com.cn/Uploads/Picture/2022-03-07/20220307105601815.jpg","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-12-29/20211229105735316.jpg","is_live":"0","room_id":"407921","age":"21","role":"M","sex":"3","nickname":"测试ok棒","is_interaction":"0","camera_switch":"0","anchor_status":"2","is_ticket":"0","ticket_beans":"0","is_pwd":"0","room_pwd":"","chat_status":"1","chat_voice_beans":"100","chat_video_beans":"250","live_chat_status":"1"},{"uid":"250385","vip":"1","vipannual":"0","is_admin":"0","is_hidden_admin":"0","svip":"1","svipannual":"1","watchsum":"1","live_title":"鸿雁传书一二三四五鸡鸭鱼肉无线网继续教育学院异界之九阳真经自己天津跨界喜剧王","live_poster":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg","is_live":"0","room_id":"333227","age":"30","role":"~","sex":"1","nickname":"鸿雁传书12","is_interaction":"0","camera_switch":"0","anchor_status":"2","is_ticket":"0","ticket_beans":"0","is_pwd":"0","room_pwd":"","chat_status":"1","chat_voice_beans":"20","chat_video_beans":"10","live_chat_status":"1"}]
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
         * uid : 407943
         * vip : 1
         * vipannual : 0
         * is_admin : 0
         * is_hidden_admin : 0
         * svip : 1
         * svipannual : 0
         * watchsum : 0
         * live_title : 测试一下吧1
         * live_poster : http://image.aiwujie.com.cn/Uploads/Picture/2022-01-14/20220114162856448.jpg
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-11-19/20211119173223394.jpg
         * is_live : 0
         * room_id : 407943
         * age : 26
         * role : ~
         * sex : 1
         * nickname : 测试一下吧
         * is_interaction : 0
         * camera_switch : 0
         * anchor_status : 3
         * is_ticket : 0
         * ticket_beans : 0
         * is_pwd : 0
         * room_pwd :
         * chat_status : 1
         * chat_voice_beans : 200
         * chat_video_beans : 30
         * live_chat_status : 1
         */

        private String uid;
        private String vip;
        private String vipannual;
        private String is_admin;
        private String is_hidden_admin;
        private String svip;
        private String svipannual;
        private String watchsum;
        private String live_title;
        private String live_poster;
        private String head_pic;
        private String is_live;
        private String room_id;
        private String age;
        private String role;
        private String sex;
        private String nickname;
        private String is_interaction;
        private String camera_switch;
        private String anchor_status;
        private String is_ticket;
        private String ticket_beans;
        private String is_pwd;
        private String room_pwd;
        private String chat_status;
        private String chat_voice_beans;
        private String chat_video_beans;
        private String live_chat_status;
        private String all_live_beans;
        private String all_time_length;

        public String getAll_live_beans() {
            return all_live_beans;
        }

        public void setAll_live_beans(String all_live_beans) {
            this.all_live_beans = all_live_beans;
        }

        public String getAll_time_length() {
            return all_time_length;
        }

        public void setAll_time_length(String all_time_length) {
            this.all_time_length = all_time_length;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getIs_hidden_admin() {
            return is_hidden_admin;
        }

        public void setIs_hidden_admin(String is_hidden_admin) {
            this.is_hidden_admin = is_hidden_admin;
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

        public String getWatchsum() {
            return watchsum;
        }

        public void setWatchsum(String watchsum) {
            this.watchsum = watchsum;
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

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getIs_live() {
            return is_live;
        }

        public void setIs_live(String is_live) {
            this.is_live = is_live;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getIs_interaction() {
            return is_interaction;
        }

        public void setIs_interaction(String is_interaction) {
            this.is_interaction = is_interaction;
        }

        public String getCamera_switch() {
            return camera_switch;
        }

        public void setCamera_switch(String camera_switch) {
            this.camera_switch = camera_switch;
        }

        public String getAnchor_status() {
            return anchor_status;
        }

        public void setAnchor_status(String anchor_status) {
            this.anchor_status = anchor_status;
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

        public String getIs_pwd() {
            return is_pwd;
        }

        public void setIs_pwd(String is_pwd) {
            this.is_pwd = is_pwd;
        }

        public String getRoom_pwd() {
            return room_pwd;
        }

        public void setRoom_pwd(String room_pwd) {
            this.room_pwd = room_pwd;
        }

        public String getChat_status() {
            return chat_status;
        }

        public void setChat_status(String chat_status) {
            this.chat_status = chat_status;
        }

        public String getChat_voice_beans() {
            return chat_voice_beans;
        }

        public void setChat_voice_beans(String chat_voice_beans) {
            this.chat_voice_beans = chat_voice_beans;
        }

        public String getChat_video_beans() {
            return chat_video_beans;
        }

        public void setChat_video_beans(String chat_video_beans) {
            this.chat_video_beans = chat_video_beans;
        }

        public String getLive_chat_status() {
            return live_chat_status;
        }

        public void setLive_chat_status(String live_chat_status) {
            this.live_chat_status = live_chat_status;
        }
    }
}
