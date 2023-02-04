package com.tencent.qcloud.tim.tuikit.live.component.gift.imp;

import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftData;
import com.tencent.qcloud.tim.tuikit.live.component.gift.OnGiftListQueryCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiftInfoDataHandler {
    private static final String TAG = "GiftInfoManager";

    private Map<String, GiftInfo> mGiftInfoMap = new HashMap<>();

    public void queryGiftInfoList(GiftAdapter mGiftAdapter,final GiftQueryCallback callback,String GIFT_DATA_URL,String token) {
        if (mGiftAdapter != null) {
            mGiftAdapter.queryGiftInfoList(new OnGiftListQueryCallback() {

                @Override
                public void onGiftListQuerySuccess(List<GiftData> giftDataList, List<GiftBeanPlatform.KnapsackBean> knapsacks, List<GiftBeanPlatform.Auspicious> mAuspicious, int giftCoinNo, int auspiciousMaxBeans) {
                    List<GiftInfo> giftInfoList = transformGiftInfoList(giftDataList);
                    if (callback != null) {
                        callback.onQuerySuccess(giftInfoList,knapsacks,mAuspicious,giftCoinNo,auspiciousMaxBeans);
                    }
                }

                @Override
                public void onGiftListQueryFailed(String errorMessage) {
                    if (callback != null) {
                        callback.onQueryFailed(errorMessage);
                    }
                }

                @Override
                public void onGiftListQueryData(String data) {
                    if (callback != null) {
                        callback.onQueryFailed(data);
                    }
                }
            },GIFT_DATA_URL,token);
        }
    }

    private List<GiftInfo> transformGiftInfoList(List<GiftData> giftDataList) {
        List<GiftInfo> giftInfoList = new ArrayList<>();
        mGiftInfoMap.clear();
        if (giftDataList != null) {
            for (GiftData giftData : giftDataList) {
                GiftInfo giftInfo = new GiftInfo();
                giftInfo.giftId = giftData.giftId;
                giftInfo.title = giftData.title;
                giftInfo.type = giftData.type;
                giftInfo.price = giftData.price;
                giftInfo.giftPicUrl = giftData.giftPicUrl;
                giftInfo.lottieUrl = giftData.lottieUrl;
                giftInfoList.add(giftInfo.copy());
                mGiftInfoMap.put(giftInfo.giftId, giftInfo);
            }
        }
        return giftInfoList;
    }

    public GiftInfo getGiftInfo(String giftId) {
        return mGiftInfoMap.get(giftId);
    }

    public interface GiftQueryCallback {
        void onQuerySuccess(List<GiftInfo> giftInfoList,List<GiftBeanPlatform.KnapsackBean> knapsacks,List<GiftBeanPlatform.Auspicious> mAuspicious,int giftCoinNo,int auspiciousMaxBeans);

        void onQueryFailed(String errorMsg);

        void onQueryDataGet(String data);
    }

}
