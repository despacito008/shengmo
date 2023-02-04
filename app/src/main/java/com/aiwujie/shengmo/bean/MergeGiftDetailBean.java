package com.aiwujie.shengmo.bean;

public class MergeGiftDetailBean {

    /**
     * retcode : 2000
     * msg : 成功
     * data : {"is_can":1,"tips":"2个棒棒糖合成1个黄瓜","gift":{"gift_id":"10","gift_name":"棒棒糖","gift_beans":"2","gift_type":"0","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/Live/gift/2022-01-17/202201171001302.png","level_num":"2","level_gift_id":"13"},"merge_gift":{"gift_id":"13","gift_name":"黄瓜","gift_beans":"38","gift_type":"0","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew04.png","level_num":"1","level_gift_id":"13"}}
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
         * is_can : 1
         * tips : 2个棒棒糖合成1个黄瓜
         * gift : {"gift_id":"10","gift_name":"棒棒糖","gift_beans":"2","gift_type":"0","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/Live/gift/2022-01-17/202201171001302.png","level_num":"2","level_gift_id":"13"}
         * merge_gift : {"gift_id":"13","gift_name":"黄瓜","gift_beans":"38","gift_type":"0","gift_image":"http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew04.png","level_num":"1","level_gift_id":"13"}
         */

        private int is_can;
        private String tips;
        private GiftBean gift;
        private MergeGiftBean merge_gift;

        public int getIs_can() {
            return is_can;
        }

        public void setIs_can(int is_can) {
            this.is_can = is_can;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public GiftBean getGift() {
            return gift;
        }

        public void setGift(GiftBean gift) {
            this.gift = gift;
        }

        public MergeGiftBean getMerge_gift() {
            return merge_gift;
        }

        public void setMerge_gift(MergeGiftBean merge_gift) {
            this.merge_gift = merge_gift;
        }

        public static class GiftBean {
            /**
             * gift_id : 10
             * gift_name : 棒棒糖
             * gift_beans : 2
             * gift_type : 0
             * gift_image : http://image.aiwujie.com.cn/Uploads/Picture/Live/gift/2022-01-17/202201171001302.png
             * level_num : 2
             * level_gift_id : 13
             */

            private String gift_id;
            private String gift_name;
            private String gift_beans;
            private String gift_type;
            private String gift_image;
            private String level_num;
            private String level_gift_id;

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

            public String getGift_beans() {
                return gift_beans;
            }

            public void setGift_beans(String gift_beans) {
                this.gift_beans = gift_beans;
            }

            public String getGift_type() {
                return gift_type;
            }

            public void setGift_type(String gift_type) {
                this.gift_type = gift_type;
            }

            public String getGift_image() {
                return gift_image;
            }

            public void setGift_image(String gift_image) {
                this.gift_image = gift_image;
            }

            public String getLevel_num() {
                return level_num;
            }

            public void setLevel_num(String level_num) {
                this.level_num = level_num;
            }

            public String getLevel_gift_id() {
                return level_gift_id;
            }

            public void setLevel_gift_id(String level_gift_id) {
                this.level_gift_id = level_gift_id;
            }
        }

        public static class MergeGiftBean {
            /**
             * gift_id : 13
             * gift_name : 黄瓜
             * gift_beans : 38
             * gift_type : 0
             * gift_image : http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew04.png
             * level_num : 1
             * level_gift_id : 13
             */

            private String gift_id;
            private String gift_name;
            private String gift_beans;
            private String gift_type;
            private String gift_image;
            private String level_num;
            private String level_gift_id;

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

            public String getGift_beans() {
                return gift_beans;
            }

            public void setGift_beans(String gift_beans) {
                this.gift_beans = gift_beans;
            }

            public String getGift_type() {
                return gift_type;
            }

            public void setGift_type(String gift_type) {
                this.gift_type = gift_type;
            }

            public String getGift_image() {
                return gift_image;
            }

            public void setGift_image(String gift_image) {
                this.gift_image = gift_image;
            }

            public String getLevel_num() {
                return level_num;
            }

            public void setLevel_num(String level_num) {
                this.level_num = level_num;
            }

            public String getLevel_gift_id() {
                return level_gift_id;
            }

            public void setLevel_gift_id(String level_gift_id) {
                this.level_gift_id = level_gift_id;
            }
        }
    }
}
