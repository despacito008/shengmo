package com.tencent.qcloud.tim.tuikit.live.bean;

public class LiveChangeInfoChatRoom {

    /**
     * costomMassageType : changeInfoAvChatRoom
     * actionType : 2
     * hour_ranking : 1
     * showType : 1
     */

    private String costomMassageType;
    private String actionType;
    private int hour_ranking;
    private String showType;
    private String beans_current_count;
    private String real_watchsum;
    private String link_mic_num;
    private String user_level;
    private String anchor_level;
    private String live_title;
    private String live_poster;
    private String fanclub_level;//粉丝团等级
    private String fanclub_card;//粉丝团名
    private String fanclub_status;//展示状态

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

    public String getLive_title() {
        return live_title;
    }

    public void setLive_title(String live_title) {
        this.live_title = live_title;
    }

    public String getLive_poster() {
        return live_poster;
    }

    public void setLive_poster(String live_poster) {
        this.live_poster = live_poster;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getAnchor_level() {
        return anchor_level;
    }

    public void setAnchor_level(String anchor_level) {
        this.anchor_level = anchor_level;
    }

    public String getLink_mic_num() {
        return link_mic_num;
    }

    public void setLink_mic_num(String link_mic_num) {
        this.link_mic_num = link_mic_num;
    }

    public String getReal_watchsum() {
        return real_watchsum;
    }

    public void setReal_watchsum(String real_watchsum) {
        this.real_watchsum = real_watchsum;
    }

    public String getCostomMassageType() {
        return costomMassageType;
    }

    public void setCostomMassageType(String costomMassageType) {
        this.costomMassageType = costomMassageType;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public int getHour_ranking() {
        return hour_ranking;
    }

    public void setHour_ranking(int hour_ranking) {
        this.hour_ranking = hour_ranking;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getBeans_current_count() {
        return beans_current_count;
    }

    public void setBeans_current_count(String beans_current_count) {
        this.beans_current_count = beans_current_count;
    }
}
