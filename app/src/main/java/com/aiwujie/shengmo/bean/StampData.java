package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/6/30.
 */

public class StampData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"wallet_stamp":"47","mobile":"13029000604","commentappstate":"0","basicstampX":0,"basicstampY":0,"basicstampZ":0,"senddynamic":0,"commentdynamic":0,"lauddynamic":0,"shareapp":0,"rewarddynamic":0}
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
         * wallet_stamp : 47
         * mobile : 13029000604
         * commentappstate : 0
         * basicstampX : 0
         * basicstampY : 0
         * basicstampZ : 0
         * senddynamic : 0
         * commentdynamic : 0
         * lauddynamic : 0
         * shareapp : 0
         * rewarddynamic : 0
         */

        private String wallet_stamp;
        private String mobile;
        private String commentappstate;
        private String narmaluser ;
        private String vipuser;
        private int basicstampX;
        private int basicstampY;
        private int basicstampZ;
        private int senddynamic;
        private int commentdynamic;
        private int lauddynamic;
        private int shareapp;
        private int rewarddynamic;

        public String getNarmaluser() {
            return narmaluser;
        }

        public void setNarmaluser(String narmaluser) {
            this.narmaluser = narmaluser;
        }

        public String getVipuser() {
            return vipuser;
        }

        public void setVipuser(String vipuser) {
            this.vipuser = vipuser;
        }

        public String getWallet_stamp() {
            return wallet_stamp;
        }

        public void setWallet_stamp(String wallet_stamp) {
            this.wallet_stamp = wallet_stamp;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getCommentappstate() {
            return commentappstate;
        }

        public void setCommentappstate(String commentappstate) {
            this.commentappstate = commentappstate;
        }

        public int getBasicstampX() {
            return basicstampX;
        }

        public void setBasicstampX(int basicstampX) {
            this.basicstampX = basicstampX;
        }

        public int getBasicstampY() {
            return basicstampY;
        }

        public void setBasicstampY(int basicstampY) {
            this.basicstampY = basicstampY;
        }

        public int getBasicstampZ() {
            return basicstampZ;
        }

        public void setBasicstampZ(int basicstampZ) {
            this.basicstampZ = basicstampZ;
        }

        public int getSenddynamic() {
            return senddynamic;
        }

        public void setSenddynamic(int senddynamic) {
            this.senddynamic = senddynamic;
        }

        public int getCommentdynamic() {
            return commentdynamic;
        }

        public void setCommentdynamic(int commentdynamic) {
            this.commentdynamic = commentdynamic;
        }

        public int getLauddynamic() {
            return lauddynamic;
        }

        public void setLauddynamic(int lauddynamic) {
            this.lauddynamic = lauddynamic;
        }

        public int getShareapp() {
            return shareapp;
        }

        public void setShareapp(int shareapp) {
            this.shareapp = shareapp;
        }

        public int getRewarddynamic() {
            return rewarddynamic;
        }

        public void setRewarddynamic(int rewarddynamic) {
            this.rewarddynamic = rewarddynamic;
        }
    }
}
