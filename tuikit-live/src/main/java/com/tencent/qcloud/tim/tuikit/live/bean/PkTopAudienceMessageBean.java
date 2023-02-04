package com.tencent.qcloud.tim.tuikit.live.bean;

public class PkTopAudienceMessageBean {
    String anchorId;
    String otherId;
    boolean isUs;

    public PkTopAudienceMessageBean(String anchorId, String otherId, boolean isUs) {
        this.anchorId = anchorId;
        this.otherId = otherId;
        this.isUs = isUs;
    }

    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public boolean isUs() {
        return isUs;
    }

    public void setUs(boolean us) {
        isUs = us;
    }
}
