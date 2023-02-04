package com.aliyun.apsara.alivclittlevideo.view.mine;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.aliyun.apsara.alivclittlevideo.net.HttpEngine;
import com.aliyun.apsara.alivclittlevideo.net.LittleHttpManager;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleHttpResponse;
import com.aliyun.apsara.alivclittlevideo.net.data.LittleUserInfo;
import com.aliyun.apsara.alivclittlevideo.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 用户信息管理类
 *
 * @author xlx
 */
public class AlivcLittleUserManager {

    private final String TAG = "AlivcLittleUserManager";

    private static AlivcLittleUserManager instance;

    private LittleUserInfo mUserInfo;
    /**
     * 是否允许更换用户
     */
    private boolean isAllowChangeUser = true;
    public static AlivcLittleUserManager getInstance() {
        if (instance == null) {
            synchronized (AlivcLittleUserManager.class) {
                if (instance == null) {
                    instance = new AlivcLittleUserManager();
                }
            }
        }
        return instance;
    }

    public void init(final Context context) {
        if (mUserInfo == null) {
            String dataString = SharedPreferenceUtils.getUser(context);
            // 先从本地shared中获取用户信息json
            if (!TextUtils.isEmpty(dataString)) {
                mUserInfo = new Gson().fromJson(dataString, new TypeToken<LittleUserInfo>() {
                } .getType());

            }

            // 从本地获取数据为空, 则重新生成新的userInfo
            if (mUserInfo == null) {
                newUser(context);
            } else {
                if (mRequestInitUserCallback != null) {
                    mRequestInitUserCallback.onSuccess();
                }
            }
        } else {
            if (mRequestInitUserCallback != null) {
                mRequestInitUserCallback.onSuccess();
            }
        }
    }

    public LittleUserInfo getUserInfo(Context context) {
        if (mUserInfo == null) {
            String dataString = SharedPreferenceUtils.getUser(context);
            if (!TextUtils.isEmpty(dataString)) {
                mUserInfo = new Gson().fromJson(dataString, new TypeToken<LittleUserInfo>() {
                } .getType());
            }
        }
        return mUserInfo;
    }
    public LittleUserInfo getUserInfo() {
        return mUserInfo;
    }
    /**
     * 创建新用户
     *
     * @param context Context
     */
    public void newUser(final Context context) {
        LittleHttpManager.getInstance().newGuest(
        new HttpEngine.HttpResponseResultCallback<LittleHttpResponse.LittleUserInfoResponse>() {
            @Override
            public void onResponse(boolean result, String message, LittleHttpResponse.LittleUserInfoResponse data) {
                if (result) {
                    if (data != null) {
                        LittleUserInfo userInfo = data.data;
                        if (userInfo != null) {
                            mUserInfo = userInfo;
                            setUserInfo(context, userInfo);
                        }
                    }
                } else {
                    Log.e(TAG, "new guest request is failure");
                    mRequestInitUserCallback.onFailure(message);
                }
            }
        });
    }

    /**
     * 将用户信息保存在本地
     *
     * @param context  Context
     * @param userInfo LittleUserInfo
     */
    public void setUserInfo(Context context, LittleUserInfo userInfo) {
        if (mUserInfo == null) {
            mUserInfo = new LittleUserInfo();
        }
        mUserInfo.setUserId(userInfo.getUserId());
        mUserInfo.setNickName(userInfo.getNickName());
        mUserInfo.setAvatarUrl(userInfo.getAvatarUrl());
        mUserInfo.setToken(userInfo.getToken());
        String userString = new Gson().toJson(mUserInfo);
        SharedPreferenceUtils.setUser(context, userString);
        if (mRequestInitUserCallback != null) {
            mRequestInitUserCallback.onSuccess();
        }
    }

    private OnRequestInitUserCallback mRequestInitUserCallback;

    public void setmRequestInitUserCallback(OnRequestInitUserCallback callback) {
        this.mRequestInitUserCallback = callback;
    }

    /**
     * 初始化用户信息
     */
    public interface OnRequestInitUserCallback {
        /**
         * success
         */
        void onSuccess();

        /**
         * failure
         *
         * @param errorMsg
         */
        void onFailure(String errorMsg);
    }

    public boolean isAllowChangeUser() {
        return isAllowChangeUser;
    }

    public void setAllowChangeUser(boolean allowChangeUser) {
        isAllowChangeUser = allowChangeUser;
    }
}
