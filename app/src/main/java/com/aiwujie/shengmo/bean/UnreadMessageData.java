package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/1/24.
 */
public class UnreadMessageData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"laudnum":"4","rewardnum":"6","comnum":0}
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
         * laudnum : 4
         * rewardnum : 6
         * comnum : 0
         */

        private String laudnum;
        private String rewardnum;
        private String comnum;
        private String topnum;
        private String atnum;

        public String getAtnum() {
            return atnum;
        }

        public void setAtnum(String atnum) {
            this.atnum = atnum;
        }

        public String getTopnum() {
            return topnum;
        }

        public void setTopnum(String topnum) {
            this.topnum = topnum;
        }

        public String getLaudnum() {
            return laudnum;
        }

        public void setLaudnum(String laudnum) {
            this.laudnum = laudnum;
        }

        public String getRewardnum() {
            return rewardnum;
        }

        public void setRewardnum(String rewardnum) {
            this.rewardnum = rewardnum;
        }

        public String getComnum() {
            return comnum;
        }

        public void setComnum(String comnum) {
            this.comnum = comnum;
        }
    }
}
