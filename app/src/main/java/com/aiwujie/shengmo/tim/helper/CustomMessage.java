package com.aiwujie.shengmo.tim.helper;



import java.util.List;

/**
 * 自定义消息的bean实体，用来与json的相互转化
 */
public class CustomMessage {

    private static final String TAG = CustomMessage.class.getSimpleName();

    public static final int VIDEO_CALL_ACTION_UNKNOWN = -1;
    /** 正在呼叫*/
    public static final int VIDEO_CALL_ACTION_DIALING = 0;
    /** 发起人取消  */
    public static final int VIDEO_CALL_ACTION_SPONSOR_CANCEL = 1;
    /** 拒接电话 */
    public static final int VIDEO_CALL_ACTION_REJECT = 2;
    /** 无人接听  */
    public static final int VIDEO_CALL_ACTION_SPONSOR_TIMEOUT = 3;
    /** 连接进入通话 */
    public static final int VIDEO_CALL_ACTION_ACCEPTED = 4;
    /** 挂断 */
    public static final int VIDEO_CALL_ACTION_HANGUP = 5;
    /** 电话占线 */
    public static final int VIDEO_CALL_ACTION_LINE_BUSY = 6;

    public static final int JSON_VERSION_1_HELLOTIM = 1;
    public static final int JSON_VERSION_2_ONLY_IOS_TRTC = 2;
    public static final int JSON_VERSION_3_ANDROID_IOS_TRTC = 3;

    // 一个欢迎提示富文本
    public static final int HELLO_TXT = 1;
    // 视频通话
    public static final int VIDEO_CALL = 2;

    private String partner = "";

    String text = "欢迎加入云通信IM大家庭！";
    String link = "https://cloud.tencent.com/document/product/269/3794";

    /**
     * 1: 仅仅是一个带链接的文本消息
     * 2: iOS支持的视频通话版本，后续已经不兼容
     * 3: Android/iOS/Web互通的视频通话版本
     */
    int version = 1;
    /**
     * 表示一次通话的唯一ID
     */
    String call_id;
    int room_id = 0;
    int action = VIDEO_CALL_ACTION_UNKNOWN;
    int duration = 0;
    /**
     * 群组时需要添加邀请人，接受者判断自己是否在邀请队列来决定是否加入通话
     */
    String[] invited_list;

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }


}
