package com.aiwujie.shengmo.bean;

/**
 * Created by 290243232 on 2017/3/24.
 */

public class RedPointData {

    /**
     * retcode : 2000
     * msg : 成功！
     * data : {"visitNum":0,"groupNum":0,"newRegerNum":0,"dynamic":0}
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
         * visitNum : 0
         * groupNum : 0
         * newRegerNum : 0
         * dynamic : 0
         */

        private int visitNum;
        private int groupNum;
        private int newRegerNum;
        private int dynamic;
        private int followDyNum;
        private  int dyRecommendNum;
        private int dyTopNum;
        private int allNum;
        private int alldynum_nolaud;

        public int getAlldynum_nolaud() {
            return alldynum_nolaud;
        }

        public void setAlldynum_nolaud(int alldynum_nolaud) {
            this.alldynum_nolaud = alldynum_nolaud;
        }

        public int getAllNum() {
            return allNum;
        }

        public void setAllNum(int allNum) {
            this.allNum = allNum;
        }

        public int getDyTopNum() {
            return dyTopNum;
        }

        public void setDyTopNum(int dyTopNum) {
            this.dyTopNum = dyTopNum;
        }

        public int getDyRecommendNum() {
            return dyRecommendNum;
        }

        public void setDyRecommendNum(int dyRecommendNum) {
            this.dyRecommendNum = dyRecommendNum;
        }

        public int getFollowDyNum() {
            return followDyNum;
        }

        public void setFollowDyNum(int followDyNum) {
            this.followDyNum = followDyNum;
        }

        public int getVisitNum() {
            return visitNum;
        }

        public void setVisitNum(int visitNum) {
            this.visitNum = visitNum;
        }

        public int getGroupNum() {
            return groupNum;
        }

        public void setGroupNum(int groupNum) {
            this.groupNum = groupNum;
        }

        public int getNewRegerNum() {
            return newRegerNum;
        }

        public void setNewRegerNum(int newRegerNum) {
            this.newRegerNum = newRegerNum;
        }

        public int getDynamic() {
            return dynamic;
        }

        public void setDynamic(int dynamic) {
            this.dynamic = dynamic;
        }
    }
}
