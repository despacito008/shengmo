package com.aiwujie.shengmo.tim.bean;

import com.aiwujie.shengmo.tim.utils.CustomMessageType;

public class GroupInviteMessageBean {

    /**
     * costomMassageType : costomMassageTypeJoinGroup
     * contentDict : {"title":"群邀请","content":"圣魔测试\n测试找Bug群...","imageUri":"http://image.aiwujie.com.cn/Uploads/Picture/2021-04-20/20210420181911400.jpg","url":"groupInvite","gid":"10004829","groupState":0,"contTitle":"[邀请加入群组]"}
     */

    private String costomMassageType = CustomMessageType.TYPE_GROUP_INVITE;
    private ContentDictBean contentDict;

    public String getCostomMassageType() {
        return costomMassageType;
    }

    public void setCostomMassageType(String costomMassageType) {
        this.costomMassageType = costomMassageType;
    }

    public ContentDictBean getContentDict() {
        return contentDict;
    }

    public void setContentDict(ContentDictBean contentDict) {
        this.contentDict = contentDict;
    }

    public static class ContentDictBean {
        /**
         * title : 群邀请
         * content : 圣魔测试
         测试找Bug群...
         * imageUri : http://image.aiwujie.com.cn/Uploads/Picture/2021-04-20/20210420181911400.jpg
         * url : groupInvite
         * gid : 10004829
         * groupState : 0
         * contTitle : [邀请加入群组]
         */

        private String title;
        private String content;
        private String imageUri;
        private String url;
        private String gid;
        private int groupState;
        private String contTitle;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImageUri() {
            return imageUri;
        }

        public void setImageUri(String imageUri) {
            this.imageUri = imageUri;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public int getGroupState() {
            return groupState;
        }

        public void setGroupState(int groupState) {
            this.groupState = groupState;
        }

        public String getContTitle() {
            return contTitle;
        }

        public void setContTitle(String contTitle) {
            this.contTitle = contTitle;
        }
    }
}
