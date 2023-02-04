package com.aiwujie.shengmo.tim.bean;
import com.aiwujie.shengmo.tim.utils.CustomMessageType;
public class FlashMessageBean {

    /**
     * contentDict :
     * costomMassageType : costomMassageTypeRed
     */

    private ContentDictBean contentDict;
    private String costomMassageType;

    public FlashMessageBean() {
        costomMassageType = CustomMessageType.TYPE_FLASH;
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

        private int isopen = 0;
        private String contTitle  = "[闪图]";
        private String imageUrl;


        public int getIsopen() {
            return isopen;
        }

        public void setIsopen(int isopen) {
            this.isopen = isopen;
        }

        public String getContTitle() {
            return contTitle;
        }

        public void setContTitle(String contTitle) {
            this.contTitle = contTitle;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
