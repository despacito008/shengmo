package com.aiwujie.shengmo.tim.bean;

import com.aiwujie.shengmo.tim.utils.CustomMessageType;

public class LiveAnchorMessageBean {
    private String costomMassageType = CustomMessageType.TYPE_LIVE_ANCHOR;
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
        public String anchorId;
        public String roomId;
        public String livePoster;
        public String liveTitle;

        public String getAnchorId() {
            return anchorId;
        }

        public void setAnchorId(String anchorId) {
            this.anchorId = anchorId;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getLivePoster() {
            return livePoster;
        }

        public void setLivePoster(String livePoster) {
            this.livePoster = livePoster;
        }

        public String getLiveTitle() {
            return liveTitle;
        }

        public void setLiveTitle(String liveTitle) {
            this.liveTitle = liveTitle;
        }
    }
}
