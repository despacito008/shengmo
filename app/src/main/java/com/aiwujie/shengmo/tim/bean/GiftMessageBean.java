package com.aiwujie.shengmo.tim.bean;

import com.aiwujie.shengmo.tim.utils.CustomMessageType;

public class GiftMessageBean {

    /**
     * contentDict : {"imageName":"狗粮","orderid":"622226-750780592","giftText":"狗粮 × 1"}
     * costomMassageType : costomMassageTypeGift
     */

    private ContentDictBean contentDict;
    private String costomMassageType;

    public GiftMessageBean() {
        costomMassageType = CustomMessageType.TYPE_GIFT;
    }

    public ContentDictBean getContentDict() {
        return contentDict;
    }

    public void setContentDict(ContentDictBean contentDict) {
        this.contentDict = contentDict;
    }

    public String getCostomMassageType() {
        return costomMassageType;
    }

    public void setCostomMassageType(String costomMassageType) {
        this.costomMassageType = costomMassageType;
    }

    public static class ContentDictBean {
        /**
         * imageName : 狗粮
         * orderid : 622226-750780592
         * giftText : 狗粮 × 1
         */

        private String imageName;
        private String orderid;
        private String giftText;
        private String contTitle  = "[礼物]";
        private String number;
        // 新版 礼物url
        private String giftUrl;
        // 新版  礼物动画url
        private String lottieUrl;
        private String svgaUrl;

        public String getSvgaUrl() {
            return svgaUrl;
        }

        public void setSvgaUrl(String svgaUrl) {
            this.svgaUrl = svgaUrl;
        }

        public String getGiftUrl() {
            return giftUrl;
        }

        public void setGiftUrl(String giftUrl) {
            this.giftUrl = giftUrl;
        }

        public String getLottieUrl() {
            return lottieUrl;
        }

        public void setLottieUrl(String lottieUrl) {
            this.lottieUrl = lottieUrl;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getContTitle() {
            return contTitle;
        }

        public void setContTitle(String contTitle) {
            this.contTitle = contTitle;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getGiftText() {
            return giftText;
        }

        public void setGiftText(String giftText) {
            this.giftText = giftText;
        }
    }
}
