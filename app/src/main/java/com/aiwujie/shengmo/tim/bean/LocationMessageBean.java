package com.aiwujie.shengmo.tim.bean;

import com.aiwujie.shengmo.tim.utils.CustomMessageType;

public class LocationMessageBean {

    /**
     * contentDict : {"isopen":"0","message":"恭喜发财，大吉大利","orderid":"6222266237541618452044"}
     * costomMassageType : costomMassageTypeRed
     */

    private ContentDictBean contentDict;
    private String costomMassageType;

    public LocationMessageBean() {
        costomMassageType = CustomMessageType.TYPE_LOCATION;
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
         * latitude = 40.07370442708334,
         *  longitude = 116.3600081380208,
         *  contTitle = [位置],
         *  addressName = 科星西路106号院国风美唐综合楼
         */

        private String latitude;
        private String longitude;
        private String addressName;
        private String contTitle  = "[位置]";

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }

        public String getContTitle() {
            return contTitle;
        }

        public void setContTitle(String contTitle) {
            this.contTitle = contTitle;
        }
    }
}
