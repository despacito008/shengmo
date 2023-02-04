package com.tencent.qcloud.tim.tuikit.live.component.gift.imp;

import java.io.Serializable;
import java.util.List;

/**
 * 从网络端获取到的礼物数据信息
 */
public class GiftBeanPlatform {

    private int retcode;
    private String msg;
    private GiftListBean data;

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

    public GiftListBean getGiftList() {
        return data;
    }

    public void setGiftList(GiftListBean data) {
        this.data = data;
    }

    public static class GiftListBean implements Serializable {
        public List<Goods> goods;
        public List<Auspicious> auspicious;
        public int auspicious_max_beans;
        public int rich_beans;
        public List<KnapsackBean> knapsack;
    }

    class Goods{
        /**
         "gift_id": "1",
         "gift_name": "棒棒糖",
         "gift_beans": "2",
         "gift_type": "0",
         "gift_index": "10",
         "gift_image": "http://image.aiwujie.com.cn/Uploads/Picture/2021-08-31/20210831172609366.png",
         "gift_status": "1",
         "gift_lottieurl": "http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-09-08/20210908203221497.json",
         "gift_lottie_status": "1"
         */

        public String gift_id;
        public String gift_image;
        public String gift_index;
        public int gift_beans;
        public String gift_name;
        public int gift_type;
        public String gift_status;
        public String gift_lottieurl;
        public String gift_lottie_status;
        public String getId() {
            return gift_id;
        }

        public void setId(String id) {
            this.gift_id = id;
        }

        public String getGift_image() {
            return gift_image;
        }

        public void setGift_image(String gift_image) {
            this.gift_image = gift_image;
        }

        public String getGift_index() {
            return gift_index;
        }

        public void setGift_index(String gift_index) {
            this.gift_index = gift_index;
        }

        public int getGift_beans() {
            return gift_beans;
        }

        public void setGift_beans(int gift_beans) {
            this.gift_beans = gift_beans;
        }

        public String getGift_name() {
            return gift_name;
        }

        public void setGift_name(String gift_name) {
            this.gift_name = gift_name;
        }

        public int getGift_type() {
            return gift_type;
        }

        public void setGift_type(int gift_type) {
            this.gift_type = gift_type;
        }

        public String getGift_status() {
            return gift_status;
        }

        public void setGift_status(String gift_status) {
            this.gift_status = gift_status;
        }

        public String getGift_lottieurl() {
            return gift_lottieurl;
        }

        public void setGift_lottieurl(String gift_lottieurl) {
            this.gift_lottieurl = gift_lottieurl;
        }

        public String getGift_lottie_status() {
            return gift_lottie_status;
        }

        public void setGift_lottie_status(String gift_lottie_status) {
            this.gift_lottie_status = gift_lottie_status;
        }
    }

    public class Auspicious{
        public String num;
        public String name;
        public String gift_lottieurl;

        @Override
        public String toString() {
            return num + "  " + name;
        }
    }

    public static class KnapsackBean {
        /**
         * num : 11
         * gift_image : http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew28.png
         * gift_id : 37
         * gift_lottieurl :
         * gift_name : 幸运草
         * gift_beans : 1
         */

        public String num;
        public String gift_image;
        public String gift_id;
        public String gift_lottieurl;
        public String gift_name;
        public String gift_beans;
    }
}
