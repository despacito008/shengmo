package com.aiwujie.shengmo.bean;

/**
 * Created by Administrator on 2019/7/18.
 */

public class Three_loginBean {

    /**
     * retcode : 2000
     * msg : 登录成功！
     * data : {"uid":"407943","r_token":"3Fz1tkeCwtaGdk6LInC2OUF+nNKxz5AQXoUkpNZUqsfwVMi43FbZHtW8TIz/N1WfdZBOVQIa7/qO+zYLW+th2A==","sex":"1","sexual":"1","chatstatus":"1","match_photo":"","nickname":"桐月L"}
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
         * uid : 407943
         * r_token : 3Fz1tkeCwtaGdk6LInC2OUF+nNKxz5AQXoUkpNZUqsfwVMi43FbZHtW8TIz/N1WfdZBOVQIa7/qO+zYLW+th2A==
         * sex : 1
         * sexual : 1
         * chatstatus : 1
         * match_photo :
         * nickname : 桐月L
         */

        private String uid;
        private String r_token;
        private String sex;
        private String sexual;
        private String chatstatus;
        private String match_photo;
        private String nickname;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getR_token() {
            return r_token;
        }

        public void setR_token(String r_token) {
            this.r_token = r_token;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSexual() {
            return sexual;
        }

        public void setSexual(String sexual) {
            this.sexual = sexual;
        }

        public String getChatstatus() {
            return chatstatus;
        }

        public void setChatstatus(String chatstatus) {
            this.chatstatus = chatstatus;
        }

        public String getMatch_photo() {
            return match_photo;
        }

        public void setMatch_photo(String match_photo) {
            this.match_photo = match_photo;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
