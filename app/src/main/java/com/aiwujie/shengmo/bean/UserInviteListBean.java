package com.aiwujie.shengmo.bean;

import java.util.List;

public class UserInviteListBean {

    /**
     * retcode : 2000
     * msg : 获取成功
     * data : [{"nickname":"中医养生SPA","register_time":"2021-11-17 15:28:12","sex":"1","head_pic":"http://image.aiwujie.com.cn/Uploads/refuse.jpg"}]
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
         * nickname : 中医养生SPA
         * register_time : 2021-11-17 15:28:12
         * sex : 1
         * head_pic : http://image.aiwujie.com.cn/Uploads/refuse.jpg
         */

        private String nickname;
        private String register_time;
        private String sex;
        private String head_pic;
        private String uid;

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

        public String getRegister_time() {
            return register_time;
        }

        public void setRegister_time(String register_time) {
            this.register_time = register_time;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }
    }
}
