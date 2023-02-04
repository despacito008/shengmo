package com.aiwujie.shengmo.bean;

/**
 * Created by Administrator on 2019/9/18.
 */

public class ChatroomSendLiwu {
    String msgtext;
    String sendtype;
    String giftname;

    public ChatroomSendLiwu(String msgtext) {
        this.msgtext = msgtext;
    }

    public ChatroomSendLiwu(String msgtext, String sendtype) {
        this.msgtext = msgtext;
        this.sendtype = sendtype;
    }

    public ChatroomSendLiwu(String msgtext, String sendtype, String giftname) {
        this.msgtext = msgtext;
        this.sendtype = sendtype;
        this.giftname = giftname;
    }

    public String getGiftname() {
        return giftname;
    }

    public void setGiftname(String giftname) {
        this.giftname = giftname;
    }

    public String getSendtype() {
        return sendtype;
    }

    public void setSendtype(String sendtype) {
        this.sendtype = sendtype;
    }

    public String getMsgtext() {
        return msgtext;
    }

    public void setMsgtext(String msgtext) {
        this.msgtext = msgtext;
    }
}
