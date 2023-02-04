package com.tencent.qcloud.tim.tuikit.live.component.gift.imp;

import android.annotation.TargetApi;
import android.os.Build;

import com.google.gson.Gson;
import com.tencent.qcloud.tim.tuikit.live.base.HttpGetRequest;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftData;
import com.tencent.qcloud.tim.tuikit.live.component.gift.OnGiftListQueryCallback;
import com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultGiftAdapterImp extends GiftAdapter implements HttpGetRequest.HttpListener {
    private static final String TAG = "DefaultGiftAdapterImp";

    private static final int    CORE_POOL_SIZE = 5;
    //private static final String GIFT_DATA_URL = "https://liteav.sdk.qcloud.com/app/res/picture/live/gift/gift_data.json";
    //private static final String GIFT_DATA_URL = "http://cs.aiwujie.net/Api/Live/liveGiftList";
    private GiftBeanThreadPool      mGiftBeanThreadPool;
    private OnGiftListQueryCallback mOnGiftListQueryCallback;

    @Override
    public void queryGiftInfoList(final OnGiftListQueryCallback callback,String GIFT_DATA_URL,String tokenApp) {
        mOnGiftListQueryCallback = callback;
        ThreadPoolExecutor threadPoolExecutor = getThreadExecutor();
        HttpGetRequest request = new HttpGetRequest(GIFT_DATA_URL, this,tokenApp);
        threadPoolExecutor.execute(request);
    }

    private synchronized ThreadPoolExecutor getThreadExecutor() {
        if (mGiftBeanThreadPool == null || mGiftBeanThreadPool.isShutdown()) {
            mGiftBeanThreadPool = new GiftBeanThreadPool(CORE_POOL_SIZE);
        }
        return mGiftBeanThreadPool;
    }

    @Override
    public void success(String response) {
        handleResponseMessage(response);
    }

    @Override
    public void onFailed(String message) {
        if (mOnGiftListQueryCallback != null) {
            mOnGiftListQueryCallback.onGiftListQueryFailed(message);
        }
    }

    private void handleResponseMessage(String response) {
        if (response == null) return;
        if (mOnGiftListQueryCallback != null) {
            GiftBeanPlatform giftBean = GsonUtil.GsonToBean(response, GiftBeanPlatform.class);
            final List<GiftData> giftDataList = transformGiftInfoList(giftBean);
            int auspiciousMaxBeans = giftBean.getGiftList().auspicious_max_beans;
            List<GiftBeanPlatform.Auspicious> auspicious = giftBean.getGiftList().auspicious;
            List<GiftBeanPlatform.KnapsackBean> knapsacks = giftBean.getGiftList().knapsack;
            mOnGiftListQueryCallback.onGiftListQuerySuccess(giftDataList, knapsacks, auspicious,giftBean.getGiftList().rich_beans,auspiciousMaxBeans);
            mOnGiftListQueryCallback.onGiftListQueryData(response);
        }
    }

    private List<GiftData> transformGiftInfoList(GiftBeanPlatform giftBean) {
        if (giftBean == null) {
            return null;
        }
        List<GiftBeanPlatform.Goods> giftBeanList = giftBean.getGiftList().goods;
        if (giftBeanList == null) {
            return null;
        }
        List<GiftData> giftInfoList = new ArrayList<>();
        for (GiftBeanPlatform.Goods bean : giftBeanList) {
            GiftData giftData = new GiftData();
            giftData.giftId = bean.getId();
            giftData.title = bean.getGift_name();
            giftData.type = bean.getGift_type();
            giftData.price = bean.getGift_beans();
            giftData.giftPicUrl = bean.getGift_image();
            giftData.giftIndex = bean.getGift_index();
            giftData.lottieUrl = bean.gift_lottieurl;
            giftData.giftBeans = bean.gift_beans;
            giftInfoList.add(giftData);
        }
        return giftInfoList;
    }

    public static class GiftBeanThreadPool extends ThreadPoolExecutor {
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        public GiftBeanThreadPool(int poolSize) {
            super(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(),
                    Executors.defaultThreadFactory(), new AbortPolicy());
        }
    }
}
