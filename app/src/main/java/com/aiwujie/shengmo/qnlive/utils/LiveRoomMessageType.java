package com.aiwujie.shengmo.qnlive.utils;

public class LiveRoomMessageType {
    public static final String USER_DATA = "userData";//更新用户信息消息
    public static final String JOIN_ROOM_MSG = "joinAvChatRoom";//进入直播间
    public static final String QUIT_ROOM_MSG = "quitAvChatRoom";//退出直播间
    public static final String CLOSE_ROOM_MSG = "closeAvChatRoom";//关闭直播间
    public static final String CAVEAT = "liveCaveat";//直播间违规警示
    //public static final String CAVERT_ROOM = "createAvChatRoom";//房间警告
    public static final String PROMPT_MSG = "promptContent"; //公告
    public static final String WATCH_ROOM = "watchAvChatRoom";//更新观看人数
    public static final String CREATE_ROOM = "createAvChatRoom";//创建房间
    public static final String CHANGE_ROOM = "changeInfoAvChatRoom"; //更新直播间信息
    public static final String ROOM_STATE_PK = "liveRoomPKAvChatRoom"; //pk信息
    public static final String RETRY_INVITE_PK = "invitePkAvChatRoom"; //pk再来一局

    public static final String LIVE_MANAGER_CHANGE = "changeManagerAvChatRoom";

    public static final String LIVE_RED_ENVELOPES_CHANGE = "redBagAvChatRoom";
}
