package com.aliyun.apsara.alivclittlevideo.net.data;

import com.aliyun.apsara.alivclittlevideo.sts.StsTokenInfo;

/**
 * http response
 * 包含公共部分和独有部分
 */
public class LittleHttpResponse {
    public boolean result;
    public String requestId;
    public String message;
    public int code;



    /**
     * 生成随机用户
     */
    public class LittleUserInfoResponse extends LittleHttpResponse {
        public LittleUserInfo data;
        @Override
        public String toString() {
            return "LittleUserInfoResponse{" +
                   "userData=" + data +
                   '}';
        }
    }
    /**
     * 获取个人中心视频列表
     */
    public class LittleMyVideoListResponse extends LittleHttpResponse {
        public LittleMineVideoInfo data;
    }
    /**
     * 获取sts信息
     */
    public class LittleStsInfoResponse extends LittleHttpResponse {
        public StsTokenInfo data;
    }
    /**
     * 发布视频
     */
    public class LittlePublishResponse extends LittleHttpResponse {
        public LittleMineVideoInfo.VideoListBean data;
    }

    /**
     * 视频上传凭证
     */
    public static class LittleVideoUploadAuthResponse extends LittleHttpResponse {
        public LittleVideoUploadAuthInfo data;
    }
    /**
     * 图片上传凭证
     */
    public static class LittleImageUploadAuthResponse extends LittleHttpResponse {
        public LittleImageUploadAuthInfo data;
    }
    /**
     * 获取直播列表
     */
    public static class LittleLiveListResponse extends LittleHttpResponse{
        public LittleLiveVideoInfo data;
    }
}
