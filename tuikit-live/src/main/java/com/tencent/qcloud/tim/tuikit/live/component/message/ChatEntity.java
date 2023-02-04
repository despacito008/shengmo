package com.tencent.qcloud.tim.tuikit.live.component.message;


/**
 * Module:   TCChatEntity
 * <p>
 * Function: 消息载体类。
 */
public class ChatEntity {
    private String grpSendName;    // 发送者的名字
    private String content;        // 消息内容
    private int type;            // 消息类型
    private int level; //等级
    private String head_pic; //头像url
    private String uid;
    private String actionType;
    private String is_group_admin; //是否场控 1是 0不是
    private String live_group_role; //0:普通 1：场控  2：主播
    private String live_user_role;  // role 是否vip
    private String contentType;
    private String content_text_color; //文字内容颜色
    private String wealth_val_switch;   //是否显示财富值
    private String wealth_val;  //财富值
    private String name_color;//名字颜色
    private String anchor_level;
    private String user_level;
    private String background_color;//背景颜色
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

    public String getWealth_val_switch() {
        return wealth_val_switch;
    }

    public void setWealth_val_switch(String wealth_val_switch) {
        this.wealth_val_switch = wealth_val_switch;
    }

    public String getWealth_val() {
        return wealth_val;
    }

    public void setWealth_val(String wealth_val) {
        this.wealth_val = wealth_val;
    }

    public String getContent_text_color() {
        return content_text_color;
    }

    public void setContent_text_color(String content_text_color) {
        this.content_text_color = content_text_color;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSenderName() {
        return grpSendName != null ? grpSendName : "";
    }

    public void setSenderName(String grpSendName) {
        this.grpSendName = grpSendName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String context) {
        this.content = context;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getIs_group_admin() {
        return is_group_admin;
    }

    public void setIs_group_admin(String is_group_admin) {
        this.is_group_admin = is_group_admin;
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
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ChatEntity)) return false;

        ChatEntity that = (ChatEntity) object;

        if (getType() != that.getType()) return false;
        if (grpSendName != null ? !grpSendName.equals(that.grpSendName) : that.grpSendName != null)
            return false;
        return getContent() != null ? getContent().equals(that.getContent()) : that.getContent() == null;

    }

    @Override
    public int hashCode() {
        int result = grpSendName != null ? grpSendName.hashCode() : 0;
        result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
        result = 31 * result + getType();
        return result;
    }

    @Override
    public String toString() {
        return "ChatEntity{" +
                "grpSendName='" + grpSendName + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", level=" + level +
                ", head_pic='" + head_pic + '\'' +
                ", uid='" + uid + '\'' +
                ", actionType='" + actionType + '\'' +
                ", is_group_admin='" + is_group_admin + '\'' +
                ", live_group_role='" + live_group_role + '\'' +
                ", live_user_role='" + live_user_role + '\'' +
                '}';
    }
}
