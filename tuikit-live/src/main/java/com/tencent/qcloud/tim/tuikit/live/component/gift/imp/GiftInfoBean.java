package com.tencent.qcloud.tim.tuikit.live.component.gift.imp;

public class GiftInfoBean implements Cloneable {
    /**
     * num : 6
     * gift_id : 36
     * gift_name : 城堡
     * gift_beans : 99999
     * gift_type : 0
     * gift_index : 36
     * gift_image : http://image.aiwujie.com.cn/Uploads/Picture/gift/presentnew27.png
     * gift_status : 1
     * gift_lottieurl : http://image.aiwujie.com.cn/Uploads/Picture/Live/lottie/2021-11-17/20211117202625947.json
     * gift_lottie_status : 1
     * gift_source :
     * is_new : 0
     */

    private String num;
    private String gift_id;
    private String gift_name;
    private String gift_beans;
    private String gift_type;
    private String gift_index;
    private String gift_image;
    private String gift_status;
    private String gift_lottieurl;
    private String gift_lottie_status;
    private String gift_source;
    private String is_new;
    private String source_type;
    private String tip_url;

    public String getTip_url() {
        return tip_url;
    }

    public void setTip_url(String tip_url) {
        this.tip_url = tip_url;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
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

    public String getGift_index() {
        return gift_index;
    }

    public void setGift_index(String gift_index) {
        this.gift_index = gift_index;
    }

    public String getGift_image() {
        return gift_image;
    }

    public void setGift_image(String gift_image) {
        this.gift_image = gift_image;
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

    public String getGift_source() {
        return gift_source;
    }

    public void setGift_source(String gift_source) {
        this.gift_source = gift_source;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        GiftInfoBean giftInfo = (GiftInfoBean) super.clone();
        return giftInfo;
    }
}
