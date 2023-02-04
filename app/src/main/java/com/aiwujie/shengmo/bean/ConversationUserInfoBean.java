package com.aiwujie.shengmo.bean;

public class ConversationUserInfoBean {

    /**
     * retcode : 2000
     * msg : 以使用过邮票可以打开会话！
     * data : {"filiation":1,"info":{"uid":"622226","is_admin":"1","is_hidden_admin":"0","svip":1,"bkvip":"0","blvip":"1","vip":"1","svipannual":"0","vipannual":"0","is_volunteer":"0","char_rule":"0"},"markname":""}
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
         * filiation : 1
         * info : {"uid":"622226","is_admin":"1","is_hidden_admin":"0","svip":1,"bkvip":"0","blvip":"1","vip":"1","svipannual":"0","vipannual":"0","is_volunteer":"0","char_rule":"0"}
         * markname :
         */

        private int filiation;
        private InfoBean info;
        private String markname;

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

        public String getMarkname() {
            return markname;
        }

        public void setMarkname(String markname) {
            this.markname = markname;
        }

        public static class InfoBean {
            /**
             * uid : 622226
             * is_admin : 1
             * is_hidden_admin : 0
             * svip : 1
             * bkvip : 0
             * blvip : 1
             * vip : 1
             * svipannual : 0
             * vipannual : 0
             * is_volunteer : 0
             * char_rule : 0
             * is_likeliar:0
             */

            private String uid;
            private String is_admin;
            private String is_hidden_admin;
            private String svip;
            private String bkvip;
            private String blvip;
            private String vip;
            private String svipannual;
            private String vipannual;
            private String is_volunteer;
            private String char_rule;
            private String is_likeliar;

            public String getIs_likeliar() {
                return is_likeliar;
            }

            public void setIs_likeliar(String is_likeliar) {
                this.is_likeliar = is_likeliar;
            }

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

            public String getBkvip() {
                return bkvip;
            }

            public void setBkvip(String bkvip) {
                this.bkvip = bkvip;
            }

            public String getBlvip() {
                return blvip;
            }

            public void setBlvip(String blvip) {
                this.blvip = blvip;
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

            public String getChar_rule() {
                return char_rule;
            }

            public void setChar_rule(String char_rule) {
                this.char_rule = char_rule;
            }
        }
    }
}
