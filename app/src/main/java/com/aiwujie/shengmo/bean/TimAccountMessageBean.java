package com.aiwujie.shengmo.bean;

public class TimAccountMessageBean {
    public static final String MESSAGE_BAN_CHAT = "forbiduserchat";
    public static final String MESSAGE_RESUME_CHAT = "resumechat";
    public static final String MESSAGE_BAN_ACCOUNT = "forbiduser";
    public static final String MESSAGE_VIP_EXPIRE = "vip_expire";

    String Text;
    String costomMassageType;
    String cloudCustomType;

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getCostomMassageType() {
        return costomMassageType;
    }

    public void setCostomMassageType(String costomMassageType) {
        this.costomMassageType = costomMassageType;
    }

    public String getCloudCustomType() {
        return cloudCustomType;
    }

    public void setCloudCustomType(String cloudCustomType) {
        this.cloudCustomType = cloudCustomType;
    }
}
