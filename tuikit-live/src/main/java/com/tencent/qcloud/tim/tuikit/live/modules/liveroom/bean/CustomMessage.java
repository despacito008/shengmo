package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean;

public class CustomMessage {

    /**
     * text : 来了
     * live_group_role : 0
     * live_user_role : 4
     * content_type : 1
     * content_text_color : #cccccc
     * wealth_val : 5
     * wealth_val_switch : 0
     */

    public String text;
    public String live_group_role;
    public String live_user_role;
    public String content_type;
    public String content_text_color;
    private String name_color;
    public String wealth_val;
    public String wealth_val_switch;
    private String anchor_level;
    private String user_level;
    private String background_color;
    private String gift_svgaurl;
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

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

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

    public String getName_color() {
        return name_color;
    }

    public void setName_color(String name_color) {
        this.name_color = name_color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLive_group_role() {
        return live_group_role;
    }

    public void setLive_group_role(String live_group_role) {
        this.live_group_role = live_group_role;
    }

    public String getLive_user_role() {
        return live_user_role;
    }

    public void setLive_user_role(String live_user_role) {
        this.live_user_role = live_user_role;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getContent_text_color() {
        return content_text_color;
    }

    public void setContent_text_color(String content_text_color) {
        this.content_text_color = content_text_color;
    }

    public String getWealth_val() {
        return wealth_val;
    }

    public void setWealth_val(String wealth_val) {
        this.wealth_val = wealth_val;
    }

    public String getWealth_val_switch() {
        return wealth_val_switch;
    }

    public void setWealth_val_switch(String wealth_val_switch) {
        this.wealth_val_switch = wealth_val_switch;
    }
}
