package com.aiwujie.shengmo.eventbus;

/**
 * Created by 290243232 on 2018/3/13.
 */

public class SessionEvent2 {
    private String sessionId;
    private byte[] picByte;

    public SessionEvent2(String sessionId, byte[] picByte) {
        this.sessionId = sessionId;
        this.picByte = picByte;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public byte[] getPicByte() {
        return picByte;
    }

    public void setPicByte(byte[] picByte) {
        this.picByte = picByte;
    }

}
