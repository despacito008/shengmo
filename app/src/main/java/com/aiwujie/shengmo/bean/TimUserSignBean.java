package com.aiwujie.shengmo.bean;

public class TimUserSignBean {

    /**
     * retcode : 2000
     * msg : 获取成功
     * data : {"uid":"407948","t_sign":"eJwtzF0LgjAYhuH-stNCX2VbU*gsCUTQ-AgPPAk268Uc00YI0X-P1MPneuD*kDIpnLcaSUh8B8h*2SiVttjiwhQOARXb85LdzRiUJPQoAAMeMLE*ajI4qtkZYz4ArGqx-xv3acCp4HSr4H0OX*rqkVVppJL42cdelken2naNm*qiKOXZXnWOw7SbhsZtxZF8fzSaMac_"}
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
         * uid : 407948
         * t_sign : eJwtzF0LgjAYhuH-stNCX2VbU*gsCUTQ-AgPPAk268Uc00YI0X-P1MPneuD*kDIpnLcaSUh8B8h*2SiVttjiwhQOARXb85LdzRiUJPQoAAMeMLE*ajI4qtkZYz4ArGqx-xv3acCp4HSr4H0OX*rqkVVppJL42cdelken2naNm*qiKOXZXnWOw7SbhsZtxZF8fzSaMac_
         */

        private String uid;
        private String t_sign;

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
    }
}
