package com.aiwujie.shengmo.bean;

import java.util.List;

public class LotteryDrawResultBean {

    /**
     * retcode : 2000
     * msg : 成功
     * data : {"wallet":"9306860","list":[{"gift_id":"12","gift_name":"秋裤","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/Live/gift/2021-11-17/20211117212808766.png","gift_beans":"10","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-12-08/20211208175945229.json","num":1}]}
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
         * wallet : 9306860
         * list : [{"gift_id":"12","gift_name":"秋裤","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/Live/gift/2021-11-17/20211117212808766.png","gift_beans":"10","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-12-08/20211208175945229.json","num":1}]
         */

        private String wallet;
        private String tips;
        private List<ListBean> list;

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * gift_id : 12
             * gift_name : 秋裤
             * gift_image : http://image.aiwujie.com.cn/Uploads/Picture/Live/gift/2021-11-17/20211117212808766.png
             * gift_beans : 10
             * gift_lottieurl : http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-12-08/20211208175945229.json
             * num : 1
             */

            private String gift_id;
            private String gift_name;
            private String gift_image;
            private String gift_beans;
            private String gift_lottieurl;
            private int num;

            public String getGift_id() {
                return gift_id;
            }

            public void setGift_id(String gift_id) {
                this.gift_id = gift_id;
            }

            public String getGift_name() {
                return gift_name;
            }

            public void setGift_name(String gift_name) {
                this.gift_name = gift_name;
            }

            public String getGift_image() {
                return gift_image;
            }

            public void setGift_image(String gift_image) {
                this.gift_image = gift_image;
            }

            public String getGift_beans() {
                return gift_beans;
            }

            public void setGift_beans(String gift_beans) {
                this.gift_beans = gift_beans;
            }

            public String getGift_lottieurl() {
                return gift_lottieurl;
            }

            public void setGift_lottieurl(String gift_lottieurl) {
                this.gift_lottieurl = gift_lottieurl;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }
        }
    }
}
