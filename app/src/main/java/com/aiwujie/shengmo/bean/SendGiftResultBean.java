package com.aiwujie.shengmo.bean;

/**
 * @ProjectName: workspace2
 * @Package: com.aiwujie.shengmo.bean
 * @ClassName: SendGiftResultBean
 * @Author: xmf
 * @CreateDate: 2022/5/6 18:32
 * @Description:
 */
public class SendGiftResultBean {

    /**
     * retcode : 2000
     * msg : 操作成功
     * data : {"gift_id":"63","gift_name":"糖果福袋","gift_image":"http://image.aiwujie.com.cn/Uploads/gift/image/tangguofudai02.png","gift_beans":"38","gift_lottie_status":"1","gift_lottieurl":"http://image.aiwujie.com.cn/Uploads/gift/lot/tangguofudai02.json","gift_svgaurl":"http://image.aiwujie.com.cn/Uploads/gift/svg/tangguofudai02.svga","num":"1","wallet":"263828"}
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
         * gift_id : 63
         * gift_name : 糖果福袋
         * gift_image : http://image.aiwujie.com.cn/Uploads/gift/image/tangguofudai02.png
         * gift_beans : 38
         * gift_lottie_status : 1
         * gift_lottieurl : http://image.aiwujie.com.cn/Uploads/gift/lot/tangguofudai02.json
         * gift_svgaurl : http://image.aiwujie.com.cn/Uploads/gift/svg/tangguofudai02.svga
         * num : 1
         * wallet : 263828
         */

        private String gift_id;
        private String gift_name;
        private String gift_image;
        private String gift_beans;
        private String gift_lottie_status;
        private String gift_lottieurl;
        private String gift_svgaurl;
        private String num;
        private String wallet;
        private String orderid;

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

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

        public String getGift_lottie_status() {
            return gift_lottie_status;
        }

        public void setGift_lottie_status(String gift_lottie_status) {
            this.gift_lottie_status = gift_lottie_status;
        }

        public String getGift_lottieurl() {
            return gift_lottieurl;
        }

        public void setGift_lottieurl(String gift_lottieurl) {
            this.gift_lottieurl = gift_lottieurl;
        }

        public String getGift_svgaurl() {
            return gift_svgaurl;
        }

        public void setGift_svgaurl(String gift_svgaurl) {
            this.gift_svgaurl = gift_svgaurl;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }
    }
}
