package com.aiwujie.shengmo.bean;

/**
 * Created by Administrator on 2019/6/13.
 */

public class getOpenChatData {
    /**
     * retcode : 2000
     * msg : 以使用过邮票可以打开会话！
     * data : {"filiation":0,"info":{"uid":"392795","is_admin":"0","svip":"1","vip":"1","svipannual":"0","vipannual":"0","is_volunteer":"0"}}
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
         * filiation : 0
         * info : {"uid":"392795","is_admin":"0","svip":"1","vip":"1","svipannual":"0","vipannual":"0","is_volunteer":"0"}
         */

        private int filiation;
        private InfoBean info;

        public int getFiliation() {
            return filiation;
        }

        public void setFiliation(int filiation) {
            this.filiation = filiation;
        }

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * uid : 392795
             * is_admin : 0
             * svip : 1
             * vip : 1
             * svipannual : 0
             * vipannual : 0
             * is_volunteer : 0
             */

            private String uid;
            private String is_admin;
            private String svip;
            private String vip;
            private String svipannual;
            private String vipannual;
            private String is_volunteer;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getIs_admin() {
                return is_admin;
            }

            public void setIs_admin(String is_admin) {
                this.is_admin = is_admin;
            }

            public String getSvip() {
                return svip;
            }

            public void setSvip(String svip) {
                this.svip = svip;
            }

            public String getVip() {
                return vip;
            }

            public void setVip(String vip) {
                this.vip = vip;
            }

            public String getSvipannual() {
                return svipannual;
            }

            public void setSvipannual(String svipannual) {
                this.svipannual = svipannual;
            }

            public String getVipannual() {
                return vipannual;
            }

            public void setVipannual(String vipannual) {
                this.vipannual = vipannual;
            }

            public String getIs_volunteer() {
                return is_volunteer;
            }

            public void setIs_volunteer(String is_volunteer) {
                this.is_volunteer = is_volunteer;
            }
        }
    }
}
