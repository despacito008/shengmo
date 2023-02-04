package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean;

public class TimRoomMessageBean {
    public  String costomMassageType;
    public  String uid;
    public  String roomId;
    public  String nickname;
    public  String head_pic;
    public  String content;
    public int level;
    public String actionType;
    public String is_group_admin;
    public String setNotalking;
    public String rich_beans;
    public String touid;

    public String getType() {
        return costomMassageType;
    }

    public void setType(String type) {
        this.costomMassageType = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return nickname;
    }

    public void setName(String name) {
        this.nickname = name;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public String getTouid() {
        return touid;
    }

    public void setTouid(String touid) {
        this.touid = touid;
    }

    public String getSetNotalking() {
        return setNotalking;
    }

    public void setSetNotalking(String setNotalking) {
        this.setNotalking = setNotalking;
    }

    public String getRich_beans() {
        return rich_beans;
    }

    public void setRich_beans(String rich_beans) {
        this.rich_beans = rich_beans;
    }

    public String getRoomId() { return roomId; }

    public void setRoomId(String roomId) { this.roomId = roomId; }
}
