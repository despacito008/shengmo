package com.aiwujie.shengmo.tim.bean;

import com.aiwujie.shengmo.tim.utils.CustomMessageType;

public class DynamicMessageBean {

    /**
     * contentDict : {"userId":"623753","contTitle":"hi,给你推荐一个动态","Newid":"2173276","content":"","shareType":0,"icon":"http://image.aiwujie.com.cn/Uploads/Picture/2021-04-22/20210422093157955sy.jpeg?x-oss-process=image/resize,p_30"}
     * costomMassageType : costomMassageTypeServiceShare
     */

    private ContentDictBean contentDict;
    private String costomMassageType = CustomMessageType.TYPE_DYNAMIC;

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
         * userId : 623753
         * contTitle : hi,给你推荐一个动态
         * Newid : 2173276
         * content :
         * shareType : 0
         * icon : http://image.aiwujie.com.cn/Uploads/Picture/2021-04-22/20210422093157955sy.jpeg?x-oss-process=image/resize,p_30
         */

        private String userId;
        private String contTitle;
        private String Newid;
        private String content;
        private int shareType;
        private String icon;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getContTitle() {
            return contTitle;
        }

        public void setContTitle(String contTitle) {
            this.contTitle = contTitle;
        }

        public String getNewid() {
            return Newid;
        }

        public void setNewid(String Newid) {
            this.Newid = Newid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getShareType() {
            return shareType;
        }

        public void setShareType(int shareType) {
            this.shareType = shareType;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
