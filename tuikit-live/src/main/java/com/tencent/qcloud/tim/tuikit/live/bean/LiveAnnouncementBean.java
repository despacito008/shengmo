package com.tencent.qcloud.tim.tuikit.live.bean;

public class LiveAnnouncementBean {

    /**
     * command : 7
     * version : 1.0.0
     * action : 301
     * businessID : av_live
     * message : {"receive_uid":"222222","receive_nickname":"接收人","room_id":"123","content":"内容"}
     */

    private String command;
    private String version;
    private String action;
    private String businessID;
    private MessageBean message;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public static class MessageBean {
        /**
         * receive_uid : 222222
         * receive_nickname : 接收人
         * room_id : 123
         * content : 内容
         */

        private String receive_uid;
        private String receive_nickname;
        private String room_id;
        private String content;

        public String getReceive_uid() {
            return receive_uid;
        }

        public void setReceive_uid(String receive_uid) {
            this.receive_uid = receive_uid;
        }

        public String getReceive_nickname() {
            return receive_nickname;
        }

        public void setReceive_nickname(String receive_nickname) {
            this.receive_nickname = receive_nickname;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
