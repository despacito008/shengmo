package com.tencent.qcloud.tim.tuikit.live.bean;

public class LiveSettingStateBean {
    boolean isAudioMute;
    boolean isVideoMute;
    boolean isCameraFront;

    public LiveSettingStateBean(boolean isAudioMute, boolean isVideoMute, boolean isCameraFront) {
        this.isAudioMute = isAudioMute;
        this.isVideoMute = isVideoMute;
        this.isCameraFront = isCameraFront;
    }

    public boolean isAudioMute() {
        return isAudioMute;
    }

    public void setAudioMute(boolean audioMute) {
        isAudioMute = audioMute;
    }

    public boolean isVideoMute() {
        return isVideoMute;
    }

    public void setVideoMute(boolean videoMute) {
        isVideoMute = videoMute;
    }

    public boolean isCameraFront() {
        return isCameraFront;
    }

    public void setCameraFront(boolean cameraFront) {
        isCameraFront = cameraFront;
    }
}
