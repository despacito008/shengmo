package com.aliyun.apsara.alivclittlevideo.view.video;

/**
 * 上传完成的listener
 *
 * @author xlx
 */
public interface OnUploadCompleteListener {
    /**
     * 上传成功
     * @param videoId videoId
     * @param imageUrl imageUrl
     */
    void onSuccess(String videoId, String imageUrl,String videoDescribe);

    /**
     * 上传失败
     * @param code 错误码
     * @param msg 错误信息
     */
    void onFailure(String code, String msg);
}
