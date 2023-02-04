package com.aiwujie.shengmo.tim.bean;

import com.aiwujie.shengmo.tim.utils.CustomMessageType;

public class RedEnvelopesMessageBean {

    /**
     * contentDict : {"isopen":"0","message":"恭喜发财，大吉大利","orderid":"6222266237541618452044"}
     * costomMassageType : costomMassageTypeRed
     */

    private ContentDictBean contentDict;
    private String costomMassageType;

    public RedEnvelopesMessageBean() {
        costomMassageType = CustomMessageType.TYPE_RED_ENVELOPES;
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
         * isopen : 0
         * message : 恭喜发财，大吉大利
         * orderid : 6222266237541618452044
         */

        private String isopen;
        private String message;
        private String orderid;
        private String contTitle  = "[红包]";

        public String getContTitle() {
            return contTitle;
        }

        public void setContTitle(String contTitle) {
            this.contTitle = contTitle;
        }

        public String getIsopen() {
            return isopen;
        }

        public void setIsopen(String isopen) {
            this.isopen = isopen;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }
    }
}
