package com.aiwujie.shengmo.timlive.bean;

public class RewardGiftInfo {
    public String retcode;
    public String msg;
    public CommandInfo data;
    public class CommandInfo {
        public String command;
        public String version;
        public String action;
        public SendGiftAvChatRoom message;

        public class SendGiftAvChatRoom {
            public String costomMassageType;
            public ExtraInfo extra;

            public class ExtraInfo{
                public String uid;
                public String nickname;
                public String head_pic;
                public String user_level;
                public String anchor_level;
                public String beans_current_count;
                public String content;
                public String gift_id;
                public String gift_name;
                public String gift_beans;
                public String gift_lottieurl;
                public String gift_image;
                public String gift_count;
            }
        }
    }
}
