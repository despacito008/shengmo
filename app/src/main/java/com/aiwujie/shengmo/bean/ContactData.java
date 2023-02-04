package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/1/14.
 */
public class ContactData {

    /**
     * retcode : 2000
     * msg :
     * data : {"EMAIL":"501898200@qq.com","KFDH":"010-87771330","KFSJ":"13436537298","QYQQ":"4006685539","SMQQ":"501898200"}
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
         * EMAIL : 501898200@qq.com
         * KFDH : 010-87771330
         * KFSJ : 13436537298
         * QYQQ : 4006685539
         * SMQQ : 501898200
         */

        private String EMAIL;
        private String KFDH;
        private String KFSJ;
        private String QYQQ;
        private String SMQQ;
        private String KFWX;
        private String WANG_ZHI;
        private String WEI_BO;
        private String OFFICIAL_ACCOUNT;

        public String getKFWX() {
            return KFWX;
        }

        public void setKFWX(String KFWX) {
            this.KFWX = KFWX;
        }

        public String getWANG_ZHI() {
            return WANG_ZHI;
        }

        public void setWANG_ZHI(String WANG_ZHI) {
            this.WANG_ZHI = WANG_ZHI;
        }

        public String getWEI_BO() {
            return WEI_BO;
        }

        public void setWEI_BO(String WEI_BO) {
            this.WEI_BO = WEI_BO;
        }

        public String getOFFICIAL_ACCOUNT() {
            return OFFICIAL_ACCOUNT;
        }

        public void setOFFICIAL_ACCOUNT(String OFFICIAL_ACCOUNT) {
            this.OFFICIAL_ACCOUNT = OFFICIAL_ACCOUNT;
        }

        public String getEMAIL() {
            return EMAIL;
        }

        public void setEMAIL(String EMAIL) {
            this.EMAIL = EMAIL;
        }

        public String getKFDH() {
            return KFDH;
        }

        public void setKFDH(String KFDH) {
            this.KFDH = KFDH;
        }

        public String getKFSJ() {
            return KFSJ;
        }

        public void setKFSJ(String KFSJ) {
            this.KFSJ = KFSJ;
        }

        public String getQYQQ() {
            return QYQQ;
        }

        public void setQYQQ(String QYQQ) {
            this.QYQQ = QYQQ;
        }

        public String getSMQQ() {
            return SMQQ;
        }

        public void setSMQQ(String SMQQ) {
            this.SMQQ = SMQQ;
        }
    }
}
