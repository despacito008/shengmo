package com.tencent.qcloud.tim.tuikit.live.component.gift;

import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftBeanPlatform;

import java.util.List;

public interface OnGiftListQueryCallback {
    /**
     * 查询成功 响应结果
     * @param giftInfoList
     */
    void onGiftListQuerySuccess(List<GiftData> giftInfoList,List<GiftBeanPlatform.KnapsackBean> knapsacks,List<GiftBeanPlatform.Auspicious> mAuspicious,int giftCoinNo,int auspiciousMaxBeans);
    /**
     * 查询失败
     */
    void onGiftListQueryFailed(String errorMsg);

    void onGiftListQueryData(String data);
}
