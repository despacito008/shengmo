package com.aiwujie.shengmo.tim.bean;

import com.aiwujie.shengmo.tim.utils.CustomMessageType;

public class SystemTipsMessageBean {

    /**
     * contentDict : {"isopen":"0","message":"恭喜发财，大吉大利","orderid":"6222266237541618452044"}
     * costomMassageType : costomMassageTypeRed
     */

    private ContentDictBean contentDict;
    private String costomMassageType;

    public SystemTipsMessageBean() {
        costomMassageType = CustomMessageType.TYPE_SYSTEM_TIPS;
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

        private String selfShowString;
        private String otherShowString;
        private String contTitle  = "[收到一条提示消息]";

        public String getContTitle() {
            return contTitle;
        }

        public void setContTitle(String contTitle) {
            this.contTitle = contTitle;
        }

        public String getSelfShowString() {
            return selfShowString;
        }

        public void setSelfShowString(String selfShowString) {
            this.selfShowString = selfShowString;
        }

        public String getOtherShowString() {
            return otherShowString;
        }

        public void setOtherShowString(String otherShowString) {
            this.otherShowString = otherShowString;
        }
    }
}
