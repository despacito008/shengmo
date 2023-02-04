package com.aliyun.apsara.alivclittlevideo.sts;

import android.text.TextUtils;

import com.aliyun.apsara.alivclittlevideo.constants.AlivcLittleHttpConfig;
import com.aliyun.apsara.alivclittlevideo.constants.AlivcLittleServerApiConstants;
import com.aliyun.apsara.alivclittlevideo.net.HttpEngine;
import com.aliyun.apsara.alivclittlevideo.net.LittleHttpManager;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleHttpResponse;

import com.aliyun.apsara.alivclittlevideo.net.data.LittleUserInfo;
import com.aliyun.apsara.alivclittlevideo.utils.HttpClientUtil;
import com.aliyun.apsara.alivclittlevideo.view.mine.AlivcLittleUserManager;

import com.aliyun.utils.VcPlayerLog;

import com.aliyun.svideo.common.utils.ThreadUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author zsy_18 data:2019/1/3
 */
public class StsInfoManager {
    private StsTokenInfo mStsTokenInfo;
    private StsInfoManager() {

    }
    private static class Holder {
        private final static StsInfoManager INSTANCE = new StsInfoManager();
    }
    public static StsInfoManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 获取sts信息
     */
    public StsTokenInfo getStsToken() {
        if (!isCurrentStsValid()) {
            ThreadUtils.runOnSubThread(new Runnable() {
                @Override
                public void run() {
                    refreshStsToken();
                }
            });
        }
        return mStsTokenInfo;
    }
    /**
     * 刷新sts信息
     * @param resultListener
     */
    public void refreshStsToken(final OnStsResultListener resultListener) {
        LittleHttpManager.getInstance().getStsInfo(
        new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleStsInfoResponse>() {
            @Override
            public void onResponse(boolean result, String message,
                                   LittleHttpResponse.LittleStsInfoResponse data) {
                if (result) {
                    resultListener.onSuccess(data.data);
                    mStsTokenInfo = data.data;
                } else {
                    if (mStsTokenInfo == null) {
                        mStsTokenInfo = new StsTokenInfo();
                    }
                    resultListener.onFail();
                }

            }
        });
    }

    /**
     * 刷新sts信息，会阻塞线程，禁止在UI线程调用
     * @return
     */
    public StsTokenInfo refreshStsToken() {
        try {
            LittleUserInfo userInfo = AlivcLittleUserManager.getInstance().getUserInfo();

            String token;
            if (userInfo != null) {
                token = userInfo.getToken();
            } else {
                token = "";
            }
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(AlivcLittleHttpConfig.RequestKey.FORM_KEY_TOKEN, token);
            String response = HttpClientUtil.doGet(urlAppend(AlivcLittleServerApiConstants.URL_GET_STS_INFO, hashMap));
            JSONObject jsonObject = new JSONObject(response);
            JSONObject data = jsonObject.getJSONObject("data");
            if (data == null) {
                return null;
            }

            String accessKeyId = data.getString("accessKeyId");
            String accessKeySecret = data.getString("accessKeySecret");
            String securityToken = data.getString("securityToken");
            String epiration = data.getString("expiration");

            mStsTokenInfo = new StsTokenInfo(accessKeySecret, securityToken, epiration, accessKeyId);

            return mStsTokenInfo;

        } catch (Exception e) {
            VcPlayerLog.e("StsInfoManager", "e = " + e.getMessage());
            return null;
        }
    }
    /**
     * 拼接get 请求的url
     * @param baseUrl baseUrl
     * @param params 要拼接的参数
     * @return
     */
    private String urlAppend(String baseUrl, HashMap<String, String> params) {
        StringBuffer absUrl = new StringBuffer();
        int size = 0;
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> item : entries) {
                if (size == 0) {
                    absUrl.append(baseUrl).append("?" + item.getKey() + "=" + item.getValue());
                    size++;
                } else {
                    absUrl.append("&" + item.getKey() + "=" + item.getValue());
                }
            }
        }
        return absUrl.toString();
    }
    /**
     * 当前sts是否有效
     * @return
     */
    public boolean isCurrentStsValid() {
        if (mStsTokenInfo == null) {
            return false;
        }
        String accessKeyId = mStsTokenInfo.getAccessKeyId();
        String accessKeySecret = mStsTokenInfo.getAccessKeySecret();
        String securityToken = mStsTokenInfo.getSecurityToken();
        String expiration = mStsTokenInfo.getExpiration();
        if (TextUtils.isEmpty(accessKeyId) || TextUtils.isEmpty(accessKeySecret) || TextUtils.isEmpty(securityToken) || TextUtils.isEmpty(expiration)) {
            return false;
        }

        return true;
    }

}
