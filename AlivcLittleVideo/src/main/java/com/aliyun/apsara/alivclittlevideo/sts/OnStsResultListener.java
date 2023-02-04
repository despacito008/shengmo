package com.aliyun.apsara.alivclittlevideo.sts;

/**
 * @author zsy_18 data:2019/1/3
 * 获取sts信息回调
 */
public interface OnStsResultListener {
    void onSuccess(StsTokenInfo tokenInfo);
    void onFail();
}
