package com.aiwujie.shengmo.bean;

import java.util.List;

class LargeGiftBean {

    /**
     * retcode : 2000
     * msg : 获取成功！
     * data : [{"gift_id":"49","gift_name":"女神01","gift_svgaurl":"http://image.aiwujie.com.cn/Uploads/gift/svg/nvshen01.svga"},{"gift_id":"50","gift_name":"嫁给我","gift_svgaurl":"http://image.aiwujie.com.cn/Uploads/gift/svg/jiageiwo.svga"},{"gift_id":"53","gift_name":"带你飞","gift_svgaurl":"http://image.aiwujie.com.cn/Uploads/gift/svg/dainifei.svga"},{"gift_id":"54","gift_name":"情人节","gift_svgaurl":"http://image.aiwujie.com.cn/Uploads/gift/svg/qingrenjie.svga"},{"gift_id":"55","gift_name":"我们的故事","gift_svgaurl":"http://image.aiwujie.com.cn/Uploads/gift/svg/womendegushi.svga"},{"gift_id":"58","gift_name":"爱情树","gift_svgaurl":"http://image.aiwujie.com.cn/Uploads/gift/svg/aiqingshu.svga"},{"gift_id":"59","gift_name":"牛郎织女04","gift_svgaurl":"http://image.aiwujie.com.cn/Uploads/gift/svg/niulangzhinv04.svga"},{"gift_id":"61","gift_name":"甜蜜秋千","gift_svgaurl":"http://image.aiwujie.com.cn/Uploads/gift/svg/tianmiqiuqian.svga"},{"gift_id":"62","gift_name":"穿云箭","gift_svgaurl":"http://image.aiwujie.com.cn/Uploads/gift/svg/chuanyunjian.svga"},{"gift_id":"65","gift_name":"花仙子05","gift_svgaurl":"http://image.aiwujie.com.cn/Uploads/gift/svg/huaxianzi05.svga"},{"gift_id":"70","gift_name":"齐心飞翔","gift_svgaurl":"http://image.aiwujie.com.cn/Uploads/gift/svg/qixinfeixiang.svga"}]
     */

    private int retcode;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * gift_id : 49
         * gift_name : 女神01
         * gift_svgaurl : http://image.aiwujie.com.cn/Uploads/gift/svg/nvshen01.svga
         */

        private String gift_id;
        private String gift_name;
        private String gift_svgaurl;

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

        public String getGift_svgaurl() {
            return gift_svgaurl;
        }

        public void setGift_svgaurl(String gift_svgaurl) {
            this.gift_svgaurl = gift_svgaurl;
        }
    }
}
