package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean;

public class GiftMessage {
    public String costomMassageType;
    public ExtraInfo extra;

    public class ExtraInfo{
        public String uid;
        public String nickname;
        public String head_pic;
        public String user_level;
        public String anchor_level;
        public String beans_current_count;
        public String rich_beans;
        public String content;
        public String gift_id;
        public String gift_name;
        public String gift_beans;
        public String gift_lottieurl;
        public String gift_lottie_status;
        public String gift_image;
        public String gift_count;
        public String gift_type;
        public String gift_svgaurl;
    }
}