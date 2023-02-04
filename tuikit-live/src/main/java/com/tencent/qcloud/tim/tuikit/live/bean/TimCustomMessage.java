package com.tencent.qcloud.tim.tuikit.live.bean;

import java.util.List;

/**
 * 通用消息类型
 */
public class TimCustomMessage {
    public  String costomMassageType;//必须项 消息类型
    public  String uid;//必须项 用户id
    public  String roomId;
    public  String nickname;//必须项 昵称
    public  String head_pic;
    public  String content;//必须项 文本类型
    public int level;
    public String actionType;
    public String is_group_admin;
    public String setNotalking;
    public String rich_beans;
    public String touid;
    public String anchor_uid;
    public AnchorInfoBean anchor_info;
    public String is_admin;

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

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

    public String getAnchor_uid() { return anchor_uid; }

    public void setAnchor_uid(String anchor_uid) { this.anchor_uid = anchor_uid; }


    public WatchAvChatRoom getWatchAvChatRoom() {
        return watchAvChatRoom;
    }

    public void setWatchAvChatRoom(WatchAvChatRoom watchAvChatRoom) {
        this.watchAvChatRoom = watchAvChatRoom;
    }

    private WatchAvChatRoom watchAvChatRoom;

    //更新观看人数
    public class WatchAvChatRoom{
        /**
         * costomMassageType : watchAvChatRoom
         * watchsum : 2
         * watchuser : ["623093","623786","623093","623786"]
         * top_gift_uid : ["623093","623786"]
         */

        private String costomMassageType;
        private int watchsum;
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

    //附加信息
    public static class AnchorInfoBean {
        /**
         * uid : 623093
         * nickname : -略略略~
         * head_pic : http://image.aiwujie.com.cn/Uploads/Picture/2021-02-03/20210203165612678.jpg
         * sex : 1
         * role : ~
         * age : 61
         * beans_current_count : 34
         * vip : 0
         * vipannual : 0
         * is_admin : 0
         * is_hidden_admin : 0
         * svip : 0
         * svipannual : 0
         * hour_ranking : 0
         * follow_state : 3
         */

        public String uid;
        public String nickname;
        public String head_pic;
        public String sex;
        public String role;
        public String age;
        public String beans_current_count;
        public String vip;
        public String vipannual;
        public String is_admin;
        public String is_hidden_admin;
        public String svip;
        public String svipannual;
        public int hour_ranking;
        public int follow_state;
    }
}
