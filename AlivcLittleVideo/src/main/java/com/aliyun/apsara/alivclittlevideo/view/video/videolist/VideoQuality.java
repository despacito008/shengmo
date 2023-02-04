package com.aliyun.apsara.alivclittlevideo.view.video.videolist;

/**
 * @author zsy_18 data:2019/4/17
 */
public enum VideoQuality {
    /**
     *
     */
    DEFAULT("OD"),
    /**
     *
     */
    DOWNLOAD("LD"),
    /**
     *
     */
    PLAY("FD");

    VideoQuality(String value) {
        this.mValue = value;
    }
    private String mValue;

    public String getValue() {
        return mValue;
    }
}
