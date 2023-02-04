package com.aliyun.apsara.alivclittlevideo.view.publish;

/**
 * @author zsy_18 data:2019/3/11
 */
public interface OnAuthInfoExpiredListener {
    /**
     * 图片上传凭证过期
     */
    void onImageAuthInfoExpired();

    /**
     * 视频上传凭证过期
     */
    void onVideoAuthInfoExpired(String videoId);
}
