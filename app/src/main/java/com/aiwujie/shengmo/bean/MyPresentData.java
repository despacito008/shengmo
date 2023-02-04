package com.aiwujie.shengmo.bean;

import java.util.List;

/**
 * Created by 290243232 on 2017/6/27.
 */

public class MyPresentData {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : {"allnum":"37","allamount":"2304","useableBeans":"226","giftArr":[{"type":"10","num":"18"},{"type":"11","num":"9"}]}
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
         * allnum : 37
         * allamount : 2304
         * useableBeans : 226
         * giftArr : [{"type":"10","num":"18"},{"type":"11","num":"9"}]
         */

        private String allnum;
        private String allamount;
        private String useableBeans;
        private double pay;
        private List<GiftArrBean> giftArr;
        private String free;
        private String topcard;
        private String usedbeans;


        public String getUsedbeans() {
            return usedbeans;
        }

        public void setUsedbeans(String usedbeans) {
            this.usedbeans = usedbeans;
        }

        public String getFree() {
            return free;
        }

        public void setFree(String free) {
            this.free = free;
        }

        public String getTopcard() {
            return topcard;
        }

        public void setTopcard(String topcard) {
            this.topcard = topcard;
        }

        public double getPay() {
            return pay;
        }

        public void setPay(double pay) {
            this.pay = pay;
        }

        public String getAllnum() {
            return allnum;
        }

        public void setAllnum(String allnum) {
            this.allnum = allnum;
        }

        public String getAllamount() {
            return allamount;
        }

        public void setAllamount(String allamount) {
            this.allamount = allamount;
        }

        public String getUseableBeans() {
            return useableBeans;
        }

        public void setUseableBeans(String useableBeans) {
            this.useableBeans = useableBeans;
        }

        public List<GiftArrBean> getGiftArr() {
            return giftArr;
        }

        public void setGiftArr(List<GiftArrBean> giftArr) {
            this.giftArr = giftArr;
        }

        public static class GiftArrBean {
            /**
             * type : 10
             * num : 18
             */

            private String type;
            private String num;
            private String gift_name;
            private String gift_img;
            private String gift_beans;
            private String gift_sum;

            public String getGift_name() {
                return gift_name;
            }

            public void setGift_name(String gift_name) {
                this.gift_name = gift_name;
            }

            public String getGift_img() {
                return gift_img;
            }

            public void setGift_img(String gift_img) {
                this.gift_img = gift_img;
            }

            public String getGift_beans() {
                return gift_beans;
            }

            public void setGift_beans(String gift_beans) {
                this.gift_beans = gift_beans;
            }

            public String getGift_sum() {
                return gift_sum;
            }

            public void setGift_sum(String gift_sum) {
                this.gift_sum = gift_sum;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }
        }
    }
}
