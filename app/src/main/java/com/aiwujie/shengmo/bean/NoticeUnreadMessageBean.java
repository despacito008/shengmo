package com.aiwujie.shengmo.bean;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: NoticeUnreadMessageBean
 * @Author: xmf
 * @CreateDate: 2022/4/7 19:44
 * @Description:
 */
public class NoticeUnreadMessageBean {

    /**
     * retcode : 2000
     * msg : 成功！
     * data : {"bigHorn":{"giftnum":0,"redbagnum":0,"vipnum":0,"topcardnum":0,"allnum":0},"other":{"visitNum":0,"groupNum":0,"dynamic":0,"followDyNum":0,"newRegerNum":0,"dyRecommendNum":0,"dyTopNum":0,"allNum":0,"alldynum_nolaud":0}}
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
         * bigHorn : {"giftnum":0,"redbagnum":0,"vipnum":0,"topcardnum":0,"allnum":0}
         * other : {"visitNum":0,"groupNum":0,"dynamic":0,"followDyNum":0,"newRegerNum":0,"dyRecommendNum":0,"dyTopNum":0,"allNum":0,"alldynum_nolaud":0}
         */

        private BigHornBean bigHorn;
        private OtherBean other;

        public BigHornBean getBigHorn() {
            return bigHorn;
        }

        public void setBigHorn(BigHornBean bigHorn) {
            this.bigHorn = bigHorn;
        }

        public OtherBean getOther() {
            return other;
        }

        public void setOther(OtherBean other) {
            this.other = other;
        }

        public static class BigHornBean {
            /**
             * giftnum : 0
             * redbagnum : 0
             * vipnum : 0
             * topcardnum : 0
             * allnum : 0
             */

            private int giftnum;
            private int redbagnum;
            private int vipnum;
            private int topcardnum;
            private int allnum;

            public int getGiftnum() {
                return giftnum;
            }

            public void setGiftnum(int giftnum) {
                this.giftnum = giftnum;
            }

            public int getRedbagnum() {
                return redbagnum;
            }

            public void setRedbagnum(int redbagnum) {
                this.redbagnum = redbagnum;
            }

            public int getVipnum() {
                return vipnum;
            }

            public void setVipnum(int vipnum) {
                this.vipnum = vipnum;
            }

            public int getTopcardnum() {
                return topcardnum;
            }

            public void setTopcardnum(int topcardnum) {
                this.topcardnum = topcardnum;
            }

            public int getAllnum() {
                return allnum;
            }

            public void setAllnum(int allnum) {
                this.allnum = allnum;
            }
        }

        public static class OtherBean {
            /**
             * visitNum : 0
             * groupNum : 0
             * dynamic : 0
             * followDyNum : 0
             * newRegerNum : 0
             * dyRecommendNum : 0
             * dyTopNum : 0
             * allNum : 0
             * alldynum_nolaud : 0
             */

            private int visitNum;
            private int groupNum;
            private int dynamic;
            private int followDyNum;
            private int newRegerNum;
            private int dyRecommendNum;
            private int dyTopNum;
            private int allNum;
            private int alldynum_nolaud;

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

            public int getDynamic() {
                return dynamic;
            }

            public void setDynamic(int dynamic) {
                this.dynamic = dynamic;
            }

            public int getFollowDyNum() {
                return followDyNum;
            }

            public void setFollowDyNum(int followDyNum) {
                this.followDyNum = followDyNum;
            }

            public int getNewRegerNum() {
                return newRegerNum;
            }

            public void setNewRegerNum(int newRegerNum) {
                this.newRegerNum = newRegerNum;
            }

            public int getDyRecommendNum() {
                return dyRecommendNum;
            }

            public void setDyRecommendNum(int dyRecommendNum) {
                this.dyRecommendNum = dyRecommendNum;
            }

            public int getDyTopNum() {
                return dyTopNum;
            }

            public void setDyTopNum(int dyTopNum) {
                this.dyTopNum = dyTopNum;
            }

            public int getAllNum() {
                return allNum;
            }

            public void setAllNum(int allNum) {
                this.allNum = allNum;
            }

            public int getAlldynum_nolaud() {
                return alldynum_nolaud;
            }

            public void setAlldynum_nolaud(int alldynum_nolaud) {
                this.alldynum_nolaud = alldynum_nolaud;
            }
        }
    }
}
