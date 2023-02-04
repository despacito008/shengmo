package com.aiwujie.shengmo.bean;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: LoginResultBean
 * @Author: xmf
 * @CreateDate: 2022/4/26 15:12
 * @Description:
 */
public class LoginResultBean {

    /**
     * retcode : 2000
     * msg : 登录成功！
     * data : {"uid":"402583","t_sign":"eJwtzMEKgkAUheF3mXXInWmumtDKhVBKlJPQMnDUqymTWSnRu2fq8nwH-g9TYWy9dMs8Jixgq2lTqpuOMppYgkB3vTyPtLoaQynzuARAKSTi-OjeUKtHR0QBALN2VP-NRtigw7m9VCgfw9Utke-64qsia5Sqj8*gH6J9UJb*kOS7k*NGcVi2cD8U5y37-gDgrzFm","sex":"1","sexual":"2","chatstatus":"1","match_photo":"http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg","nickname":"圣魔圣魔圣魔","token":"010630fc292d259c32d844ac27a3919f"}
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
         * uid : 402583
         * t_sign : eJwtzMEKgkAUheF3mXXInWmumtDKhVBKlJPQMnDUqymTWSnRu2fq8nwH-g9TYWy9dMs8Jixgq2lTqpuOMppYgkB3vTyPtLoaQynzuARAKSTi-OjeUKtHR0QBALN2VP-NRtigw7m9VCgfw9Utke-64qsia5Sqj8*gH6J9UJb*kOS7k*NGcVi2cD8U5y37-gDgrzFm
         * sex : 1
         * sexual : 2
         * chatstatus : 1
         * match_photo : http://image.aiwujie.com.cn/Uploads/Picture/2021-10-22/20211022165513848.jpg
         * nickname : 圣魔圣魔圣魔
         * token : 010630fc292d259c32d844ac27a3919f
         */

        private String uid;
        private String t_sign;
        private String sex;
        private String sexual;
        private String chatstatus;
        private String match_photo;
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

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
