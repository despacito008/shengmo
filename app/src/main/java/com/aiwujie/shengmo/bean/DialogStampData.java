package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/7/3.
 */

public class DialogStampData {

    /**
     * retcode : 3001
     * msg : 需要使用邮票！
     * data : {"wallet_stamp":"47","sex":"1","basicstampX":3,"basicstampY":3,"basicstampZ":3}
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
         * sex : 1
         * basicstampX : 3
         * basicstampY : 3
         * basicstampZ : 3
         */

        private String wallet_stamp;
        private String sex;
        private int basicstampX;
        private int basicstampY;
        private int basicstampZ;

        public String getWallet_stamp() {
            return wallet_stamp;
        }

        public void setWallet_stamp(String wallet_stamp) {
            this.wallet_stamp = wallet_stamp;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
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
    }
}
