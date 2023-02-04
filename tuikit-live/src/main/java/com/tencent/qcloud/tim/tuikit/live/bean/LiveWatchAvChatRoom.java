package com.tencent.qcloud.tim.tuikit.live.bean;

import java.util.List;

/**
 * 更新观看人数
 */
public class LiveWatchAvChatRoom {

    /**
     * costomMassageType : watchAvChatRoom
     * watchsum : 2
     * watchuser : ["623790","623786"]
     * top_gift_uid : []
     * showType : 1
     */

    private String costomMassageType;
    private int watchsum;
    private String showType;
    private List<String> watchuser;
    private List<String> top_gift_uid;

    public String getCostomMassageType() {
        return costomMassageType;
    }

    public void setCostomMassageType(String costomMassageType) {
        this.costomMassageType = costomMassageType;
    }

    public int getWatchsum() {
        return watchsum;
    }

    public void setWatchsum(int watchsum) {
        this.watchsum = watchsum;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public List<String> getWatchuser() {
        return watchuser;
    }

    public void setWatchuser(List<String> watchuser) {
        this.watchuser = watchuser;
    }

    public List<String> getTop_gift_uid() {
        return top_gift_uid;
    }

    public void setTop_gift_uid(List<String> top_gift_uid) {
        this.top_gift_uid = top_gift_uid;
    }
}
