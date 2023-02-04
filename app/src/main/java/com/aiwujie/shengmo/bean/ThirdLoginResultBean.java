package com.aiwujie.shengmo.bean;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: thirdLoginResultBean
 * @Author: xmf
 * @CreateDate: 2022/4/27 10:18
 * @Description:
 */
public class ThirdLoginResultBean {

    /**
     * retcode : 2000
     * msg : 登录成功！
     * data : {"uid":"703485","t_sign":"eJwtzMEKgkAUheF3mXXY9TY3G6FF1BCREKFQizaCk9xkalIpMXr3TF2e78D-EUkUey9TilCgB2LSb87MveYr9xzATC5ofKqsSJ3jTIS*BCCJkmh4TOO4NJ0TEQLAoDXbv83JB6QA5VjhvAvTI8l3OfP2vT6r*LbfYGMPRx1hCu1JK2f1KtDtZfosVLUU3x-z0zC4","status":"1","deblockingtime":"0","chatstatus":"1","unionid":"o4yfIv9hgh01VrSnJlTcPEInUHDU","openidtwo":"oRb_xwoNdTPcm80dqGjMss_MiPEA","new_device_token":"effaaa8a798e2623unknown","new_device_brand":"Xiaomi-M2002J9E","new_device_version":"11","new_device_appversion":"6.1.1","sex":"1","sexual":"2","head_pic":"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLGdq3ibCGJ9bpX2ibk8KdS0HE8MHIx6T0LsySMiaqOVgrfyrWCre4mLZpic8G7RsOXQDZ3QAb4KqWgkg/132","nickname":"你好2021","token":"78b4af80cf1d66a88d147c26a29f65a0"}
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
         * uid : 703485
         * t_sign : eJwtzMEKgkAUheF3mXXY9TY3G6FF1BCREKFQizaCk9xkalIpMXr3TF2e78D-EUkUey9TilCgB2LSb87MveYr9xzATC5ofKqsSJ3jTIS*BCCJkmh4TOO4NJ0TEQLAoDXbv83JB6QA5VjhvAvTI8l3OfP2vT6r*LbfYGMPRx1hCu1JK2f1KtDtZfosVLUU3x-z0zC4
         * status : 1
         * deblockingtime : 0
         * chatstatus : 1
         * unionid : o4yfIv9hgh01VrSnJlTcPEInUHDU
         * openidtwo : oRb_xwoNdTPcm80dqGjMss_MiPEA
         * new_device_token : effaaa8a798e2623unknown
         * new_device_brand : Xiaomi-M2002J9E
         * new_device_version : 11
         * new_device_appversion : 6.1.1
         * sex : 1
         * sexual : 2
         * head_pic : https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLGdq3ibCGJ9bpX2ibk8KdS0HE8MHIx6T0LsySMiaqOVgrfyrWCre4mLZpic8G7RsOXQDZ3QAb4KqWgkg/132
         * nickname : 你好2021
         * token : 78b4af80cf1d66a88d147c26a29f65a0
         */

        private String uid;
        private String t_sign;
        private String status;
        private String deblockingtime;
        private String chatstatus;
        private String unionid;
        private String openidtwo;
        private String new_device_token;
        private String new_device_brand;
        private String new_device_version;
        private String new_device_appversion;
        private String sex;
        private String sexual;
        private String head_pic;
        private String nickname;
        private String token;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getT_sign() {
            return t_sign;
        }

        public void setT_sign(String t_sign) {
            this.t_sign = t_sign;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDeblockingtime() {
            return deblockingtime;
        }

        public void setDeblockingtime(String deblockingtime) {
            this.deblockingtime = deblockingtime;
        }

        public String getChatstatus() {
            return chatstatus;
        }

        public void setChatstatus(String chatstatus) {
            this.chatstatus = chatstatus;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getOpenidtwo() {
            return openidtwo;
        }

        public void setOpenidtwo(String openidtwo) {
            this.openidtwo = openidtwo;
        }

        public String getNew_device_token() {
            return new_device_token;
        }

        public void setNew_device_token(String new_device_token) {
            this.new_device_token = new_device_token;
        }

        public String getNew_device_brand() {
            return new_device_brand;
        }

        public void setNew_device_brand(String new_device_brand) {
            this.new_device_brand = new_device_brand;
        }

        public String getNew_device_version() {
            return new_device_version;
        }

        public void setNew_device_version(String new_device_version) {
            this.new_device_version = new_device_version;
        }

        public String getNew_device_appversion() {
            return new_device_appversion;
        }

        public void setNew_device_appversion(String new_device_appversion) {
            this.new_device_appversion = new_device_appversion;
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

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
