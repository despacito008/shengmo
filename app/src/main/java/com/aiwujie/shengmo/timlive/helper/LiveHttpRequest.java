package com.aiwujie.shengmo.timlive.helper;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.TopToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.AppLiveFollowEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 接口数据请求
 */
public class LiveHttpRequest {
    // 两次点击按钮之间的点击间隔不能少于2000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static HashMap<Integer, Long> sLastClickTimeMap = new HashMap<>();

    public static boolean isFastClick(Context context, int viewId) {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        long lastClickTime = getLastClickTime(viewId);
        if ((curClickTime - lastClickTime) < MIN_CLICK_DELAY_TIME) {
            ToastUtil.show(context, context.getString(R.string.is_fast_click));
            flag = true;
        }
        sLastClickTimeMap.put(viewId, curClickTime);
        return flag;
    }

    public static void clear() {
        sLastClickTimeMap.clear();
    }

    private static Long getLastClickTime(int viewId) {
        Long lastClickTime = sLastClickTimeMap.get(viewId);
        if (lastClickTime == null) {
            lastClickTime = 0L;
        }
        return lastClickTime;
    }

    private static String TAG = LiveHttpRequest.class.getSimpleName();
    private static boolean isLoading = false; //无加载状态

    /**
     * 关注主播
     *
     * @param tvAnchorFollow
     * @param mContext
     * @param uid
     * @param follow_state   状态值
     */
    public static void followAnchor(TextView tvAnchorFollow, Context mContext, String uid, int follow_state) {
        if (tvAnchorFollow == null) return;
        if (isFastClick(mContext, tvAnchorFollow.getId())) return; //快速点击
        LogUtil.d("follow_state: " + follow_state + "\n" + tvAnchorFollow.getText().toString().trim());
        String followTxt = tvAnchorFollow.getText().toString().trim();
        boolean isFollow = follow_state == 2 || follow_state == 4; //调用关注接口
        boolean hasFollow = follow_state == 1 || follow_state == 3; //调用取消关注接口
        if (followTxt.equals(mContext.getResources().getString(R.string.live_has_followed))) { //取消关注
            LiveHttpRequest.unFollowUserById(mContext, uid, tvAnchorFollow);//调用取消关注接口
        } else if (followTxt.equals(mContext.getResources().getString(R.string.live_follow))) {//没关注
            LiveHttpRequest.followUserById(mContext, uid, tvAnchorFollow); //调用关注接口
        }
    }

    /**
     * 点击头像顶部的关注按钮
     *
     * @param context
     * @param userId
     * @param tvAnchorFollow
     */
    public static void unFollowUserById(final Context context, final String userId, final TextView tvAnchorFollow) {
        do {
            isLoading = true;
//            LiveHttpHelper.getInstance().unFollowAnchor(userId, new HttpListener() {
//                @Override
//                public void onSuccess(String data) {
//                    LogUtil.e(TAG, data);
//                    Log.d("onSuccess", data);
//                    try {
//                        JSONObject obj = new JSONObject(data);
//                        ToastUtil.show(context, obj.getString("msg"));
//                        tvAnchorFollow.setText(context.getResources().getString(R.string.live_follow));
//                        isLoading = false;
//                        EventBus.getDefault().post(new AppLiveFollowEvent("unFollowUserById"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFail(String msg) {
//                    LogUtil.e(TAG, msg);
//                    ToastUtil.show(context, msg);
//                    EventBus.getDefault().post(new AppLiveFollowEvent("error"));
//                }
//            });
            HttpHelper.getInstance().followAnchor(userId, false, new HttpCodeListener() {
                @Override
                public void onSuccess(String data) {
                    LogUtil.e(TAG, data);
                    Log.d("onSuccess", data);
                    try {
                        JSONObject obj = new JSONObject(data);
                        ToastUtil.show(context, obj.getString("msg"));
                        tvAnchorFollow.setText(context.getResources().getString(R.string.live_follow));
                        isLoading = false;
                        EventBus.getDefault().post(new AppLiveFollowEvent("unFollowUserById"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(int code, String msg) {
                    LogUtil.e(TAG, msg);
                    ToastUtil.show(context, msg);
                    EventBus.getDefault().post(new AppLiveFollowEvent("error"));
                }
            });
        } while (!isLoading);
    }

    /**
     * 点击卡片底部右侧的关注按钮
     *
     * @param context
     * @param userId
     * @param tvAnchorFollow
     */
    public static void followUserById(final Context context, String userId, final TextView tvAnchorFollow) {
        do {
            isLoading = true;
//            LiveHttpHelper.getInstance().followAnchor(userId, new HttpListener() {
//                @Override
//                public void onSuccess(String data) {
//                    LogUtil.e(TAG,data);
//                    Log.d("onSuccess",data);
//                    try {
//                        JSONObject obj = new JSONObject(data);
//                        ToastUtil.show(context, obj.getString("msg"));
//                        tvAnchorFollow.setText(context.getResources().getString(R.string.live_has_followed));
//                        isLoading = false;
//                        EventBus.getDefault().post(new AppLiveFollowEvent("followUserById"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFail(String msg) {
//                    LogUtil.e(TAG,msg);
//                    ToastUtil.show(context,msg);
//                    EventBus.getDefault().post(new AppLiveFollowEvent("error"));
//                }
//            });

            HttpHelper.getInstance().followAnchor(userId, true, new HttpCodeListener() {
                @Override
                public void onSuccess(String data) {
                    LogUtil.e(TAG, data);
                    Log.d("onSuccess", data);
                    try {
                        JSONObject obj = new JSONObject(data);
                        ToastUtil.show(context, obj.getString("msg"));
                        tvAnchorFollow.setText(context.getResources().getString(R.string.live_has_followed));
                        isLoading = false;
                        EventBus.getDefault().post(new AppLiveFollowEvent("followUserById"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(int code, String msg) {
                    LogUtil.e(TAG, msg);
                    ToastUtil.show(context, msg);
                    EventBus.getDefault().post(new AppLiveFollowEvent("error"));
                }
            });


        } while (!isLoading);
    }

    /**
     * 顶部头像关注按钮
     *
     * @param context
     * @param userId
     * @param mLayoutTopToolBar
     */
    public static void followUserById(final Context context, String userId, final TopToolBarLayout mLayoutTopToolBar) {
        do {
            isLoading = true;
//            LiveHttpHelper.getInstance().followAnchor(userId, new HttpListener() {
//                @Override
//                public void onSuccess(String data) {
//                    LogUtil.e(TAG,data);
//                    Log.d("onSuccess",data);
//                    try {
//                        JSONObject obj = new JSONObject(data);
//                        ToastUtil.show(context, obj.getString("msg"));
//                        mLayoutTopToolBar.setHasFollowed(true);
//                        isLoading = false;
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFail(String msg) {
//                    LogUtil.e(TAG,msg);
//                    ToastUtil.show(context,msg);
//                }
//            });

            HttpHelper.getInstance().followAnchor(userId, true, new HttpCodeListener() {
                @Override
                public void onSuccess(String data) {
                    LogUtil.e(TAG, data);
                    Log.d("onSuccess", data);
                    try {
                        JSONObject obj = new JSONObject(data);
                        ToastUtil.show(context, obj.getString("msg"));
                        mLayoutTopToolBar.setHasFollowed(true);
                        isLoading = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(int code, String msg) {
                    LogUtil.e(TAG, msg);
                    ToastUtil.show(context, msg);
                }
            });

        } while (!isLoading);
    }
}
