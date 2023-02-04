package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean;

/**
 * @ProjectName: workspace2
 * @Package: com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean
 * @ClassName: LiveMessageSignBean
 * @Author: xmf
 * @CreateDate: 2022/5/31 20:04
 * @Description:
 */
public class LiveMessageSignBean {
    private String anchor_level; //主播等级
    private String user_level;   //用户等级
    private String fanclub_level;//粉丝团等级
    private String fanclub_card;//粉丝团名
    private String fanclub_status;//展示状态

    public String getAnchor_level() {
        return anchor_level;
    }

    public void setAnchor_level(String anchor_level) {
        this.anchor_level = anchor_level;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getFanclub_level() {
        return fanclub_level;
    }

    public void setFanclub_level(String fanclub_level) {
        this.fanclub_level = fanclub_level;
    }

    public String getFanclub_card() {
        return fanclub_card;
    }

    public void setFanclub_card(String fanclub_card) {
        this.fanclub_card = fanclub_card;
    }

    public String getFanclub_status() {
        return fanclub_status;
    }

    public void setFanclub_status(String fanclub_status) {
        this.fanclub_status = fanclub_status;
    }
}
