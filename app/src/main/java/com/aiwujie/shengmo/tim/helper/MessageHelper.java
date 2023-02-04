package com.aiwujie.shengmo.tim.helper;


import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

public class MessageHelper {
    /**
     * 消息类型 - 礼物
     */
    public static final int MSG_TYPE_GIFT = 0x1010;
    //消息类型-红包
    public static final int MSG_TYPE_RED_ENVELOPES = 0x1020;




    /**
     * 创建一条自定义消息
     *
     * @param data 自定义消息内容，可以是任何内容
     * @return
     */
    public static MessageInfo buildCustomMessage(String data) {
        MessageInfo info = new MessageInfo();
        V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createCustomMessage(data.getBytes());
        info.setSelf(true);
        info.setTimMessage(v2TIMMessage);
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        info.setFromUser(V2TIMManager.getInstance().getLoginUser());
        info.setExtra("自定义消息");
        return info;
    }


    /**
     * 自定义消息 - 礼物
     *
     */
    public static MessageInfo buildGiftMessage(String data) {
        MessageInfo info = new MessageInfo();
        V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createCustomMessage(data.getBytes());

        info.setSelf(true);
        info.setTimMessage(v2TIMMessage);
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        info.setFromUser(V2TIMManager.getInstance().getLoginUser());
        info.setExtra("[礼物]");
        return info;
    }


    /**
     * 自定义消息 - 红包
     *
     */
    public static MessageInfo buildRedEnvelopesMessage(String data) {
        MessageInfo info = new MessageInfo();
        V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createCustomMessage(data.getBytes());
        info.setSelf(true);
        info.setTimMessage(v2TIMMessage);
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        info.setFromUser(V2TIMManager.getInstance().getLoginUser());
        info.setExtra("[红包]");
        return info;
    }

    /**
     * 自定义消息 - 闪照
     *
     */
    public static MessageInfo buildFlashMessage(String data) {
        MessageInfo info = new MessageInfo();
        V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createCustomMessage(data.getBytes());

        info.setSelf(true);
        info.setTimMessage(v2TIMMessage);
        info.setMsgTime(System.currentTimeMillis() / 1000);
        info.setMsgType(MessageInfo.MSG_TYPE_CUSTOM);
        info.setFromUser(V2TIMManager.getInstance().getLoginUser());
        info.setExtra("[闪照]");
        return info;
    }



}
