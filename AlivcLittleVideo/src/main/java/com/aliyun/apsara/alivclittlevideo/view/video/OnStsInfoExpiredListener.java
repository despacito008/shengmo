package com.aliyun.apsara.alivclittlevideo.view.video;

import com.aliyun.apsara.alivclittlevideo.sts.StsTokenInfo;

/**
 * @author zsy_18 data:2019/1/9
 * sts信息过期监听
 */
public interface OnStsInfoExpiredListener {
    /**
     * 通知外部sts信息过期
     */
    void onTimeExpired();

    /**
     * 重新刷新并获取sts信息，需要在子线程调用，并且外部进行同步获取sts信息
     * @return 从网络重新获取的sts信息
     */
    StsTokenInfo refreshSts();
}
