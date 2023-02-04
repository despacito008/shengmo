package com.aiwujie.shengmo.bean;

import java.util.List;

public class LiveTaskRankBean {

    /**
     * retcode : 2000
     * msg : 获取成功
     * data : [{"nickname":"鸿雁传书12","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg","role":7,"uid":250385,"complete_schedule":2},{"nickname":"圣魔iOS～工程","head_pic":"http://image.aiwujie.com.cn/Uploads/Picture/2019-09-26/20190926170626351.jpg","role":2,"uid":402279,"complete_schedule":1}]
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
         * nickname : 鸿雁传书12
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-11-27/20211127173638373.jpg
         * role : 7
         * uid : 250385
         * complete_schedule : 2
         */

        private String nickname;
        private String head_pic;
        private int role;
        private int uid;
        private String complete_schedule;

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

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getComplete_schedule() {
            return complete_schedule;
        }

        public void setComplete_schedule(String complete_schedule) {
            this.complete_schedule = complete_schedule;
        }
    }
}
