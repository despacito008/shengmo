package com.aiwujie.shengmo.timlive.net;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.kt.listener.SimpleCallback;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.qnlive.activity.QNLiveRoomAudienceActivity;
import com.aiwujie.shengmo.qnlive.utils.QNRoomManager;
import com.aiwujie.shengmo.timlive.bean.ScenesRoomInfo;
import com.aiwujie.shengmo.timlive.ui.LiveRoomAnchorActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.signature.GenerateTestUserSig;
import com.aiwujie.shengmo.timlive.ui.LiveRoomSwitchActivity;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.NormalTipsPop;
import com.google.gson.Gson;
import com.tencent.liteav.custom.Constents;
import com.tencent.qcloud.tim.tuikit.live.bean.LivePermissionBean;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;


import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class RoomManager {

    private static final String TAG = "RoomManager";

    private static final String URL = "https://service-c2zjvuxa-1252463788.gz.apigw.tencentcs.com/release/forTest";

    private static final String REQUEST_PARAM_METHOD = "method";
    private static final String REQUEST_PARAM_APP_ID = "appId";
    private static final String REQUEST_PARAM_ROOM_ID = "roomId";
    private static final String REQUEST_PARAM_TYPE = "type";

    private static final String REQUEST_VALUE_CREATE_ROOM = "createRoom";
    private static final String REQUEST_VALUE_UPDATE_ROOM = "updateRoom";
    private static final String REQUEST_VALUE_DESTROY_ROOM = "destroyRoom";
    private static final String REQUEST_VALUE_GET_ROOM_LIST = "getRoomList";

    public static final String TYPE_LIVE_ROOM = "liveRoom";
    public static final String TYPE_VOICE_ROOM = "voiceRoom";
    public static final String TYPE_GROUP_LIVE = "groupLive";

    public static final String ROOM_TITLE = "room_title";
    public static final String GROUP_ID = "group_id";
    public static final String USE_CDN_PLAY = "use_cdn_play";
    public static final String PUSHER_NAME = "pusher_name";
    public static final String COVER_PIC = "cover_pic";
    public static final String PUSHER_AVATAR = "pusher_avatar";
    public static final String ANCHOR_ID = "anchor_id";
    public static final String ROOM_ITEM = "item";
    public static final String ROOM_INFO = "info";
    public static final String ROOM_CLICK_FROM = "from";
    public static final String TICKET_ROOM_TRY_TIME = "ticketTryTime";
    private static final RoomManager mOurInstance = new RoomManager();

    public static final int ERROR_CODE_UNKNOWN = -1;


    private Handler mHandler = new Handler(Looper.getMainLooper());

    public static RoomManager getInstance() {
        return mOurInstance;
    }

    public void createRoom(int roomId, String type, final ActionCallback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(REQUEST_PARAM_METHOD, REQUEST_VALUE_CREATE_ROOM)
                .add(REQUEST_PARAM_APP_ID, String.valueOf(GenerateTestUserSig.SDKAPPID))
                .add(REQUEST_PARAM_ROOM_ID, String.valueOf(roomId))
                .add(REQUEST_PARAM_TYPE, type)
                .build();
        final Request request = new Request.Builder()
                .url(URL)
                .post(formBody)
                .build();
        okHttpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: ", e);
                        if (callback != null) {
                            callback.onFailed(ERROR_CODE_UNKNOWN, MyApp.instance().getString(R.string.unknow_error));
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            doFailed(ERROR_CODE_UNKNOWN, MyApp.instance().getString(R.string.unknow_error));
                            return;
                        }
                        String result = response.body().string();
                        Log.i(TAG, "createRoom, onResponse: result -> " + result);
                        if (!TextUtils.isEmpty(result)) {
                            Gson gson = new Gson();
                            ResponseEntity res = gson.fromJson(result, ResponseEntity.class);
                            if (res.errorCode == 0) {
                                doSuccess();
                            } else {
                                doFailed(res.errorCode, res.errorMessage);
                            }
                        } else {
                            doFailed(ERROR_CODE_UNKNOWN, MyApp.instance().getString(R.string.unknow_error));
                        }
                    }

                    private void doFailed(final int code, final String msg) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onFailed(code, msg);
                                }
                            }
                        });
                    }

                    private void doSuccess() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onSuccess();
                                }
                            }
                        });
                    }
                });
    }

    public void updateRoom(int roomId, String type, final ActionCallback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(REQUEST_PARAM_METHOD, REQUEST_VALUE_UPDATE_ROOM)
                .add(REQUEST_PARAM_APP_ID, String.valueOf(GenerateTestUserSig.SDKAPPID))
                .add(REQUEST_PARAM_ROOM_ID, String.valueOf(roomId))
                .add(REQUEST_PARAM_TYPE, type)
                .build();
        final Request request = new Request.Builder()
                .url(URL)
                .post(formBody)
                .build();
        okHttpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: ", e);
                        if (callback != null) {
                            callback.onFailed(ERROR_CODE_UNKNOWN, MyApp.instance().getString(R.string.unknow_error));
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            doFailed(ERROR_CODE_UNKNOWN, MyApp.instance().getString(R.string.unknow_error));
                            return;
                        }
                        String result = response.body().string();
                        Log.i(TAG, "updateRoom, onResponse: result -> " + result);
                        if (!TextUtils.isEmpty(result)) {
                            Gson gson = new Gson();
                            ResponseEntity res = gson.fromJson(result, ResponseEntity.class);
                            if (res.errorCode == 0) {
                                doSuccess();
                            } else {
                                doFailed(res.errorCode, res.errorMessage);
                            }
                        } else {
                            doFailed(ERROR_CODE_UNKNOWN, MyApp.instance().getString(R.string.unknow_error));
                        }
                    }

                    private void doFailed(final int code, final String msg) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onFailed(code, msg);
                                }
                            }
                        });
                    }

                    private void doSuccess() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onSuccess();
                                }
                            }
                        });
                    }
                });
    }

    public void destroyRoom(int roomId, String type, final ActionCallback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(REQUEST_PARAM_METHOD, REQUEST_VALUE_DESTROY_ROOM)
                .add(REQUEST_PARAM_APP_ID, String.valueOf(GenerateTestUserSig.SDKAPPID))
                .add(REQUEST_PARAM_ROOM_ID, String.valueOf(roomId))
                .add(REQUEST_PARAM_TYPE, type)
                .build();
        final Request request = new Request.Builder()
                .url(URL)
                .post(formBody)
                .build();
        okHttpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: ", e);
                        if (callback != null) {
                            callback.onFailed(ERROR_CODE_UNKNOWN, MyApp.instance().getString(R.string.unknow_error));
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            doFailed(ERROR_CODE_UNKNOWN, MyApp.instance().getString(R.string.unknow_error));
                            return;
                        }
                        String result = response.body().string();
                        Log.i(TAG, "destroyRoom, onResponse: result -> " + result);
                        if (!TextUtils.isEmpty(result)) {
                            Gson gson = new Gson();
                            ResponseEntity res = gson.fromJson(result, ResponseEntity.class);
                            if (res.errorCode == 0) {
                                doSuccess();
                            } else {
                                doFailed(res.errorCode, res.errorMessage);
                            }
                        } else {
                            doFailed(ERROR_CODE_UNKNOWN, MyApp.instance().getString(R.string.unknow_error));
                        }
                    }

                    private void doFailed(final int code, final String msg) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onFailed(code, msg);
                                }
                            }
                        });
                    }

                    private void doSuccess() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onSuccess();
                                }
                            }
                        });
                    }
                });
    }

    public void getRoomList(String type, final GetRoomListCallback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(REQUEST_PARAM_METHOD, REQUEST_VALUE_GET_ROOM_LIST)
                .add(REQUEST_PARAM_APP_ID, String.valueOf(GenerateTestUserSig.SDKAPPID))
                .add(REQUEST_PARAM_TYPE, type)
                .build();
        final Request request = new Request.Builder()
                .url(URL)
                .post(formBody)
                .build();
        okHttpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: ", e);
                        if (callback != null) {
                            callback.onFailed(ERROR_CODE_UNKNOWN, MyApp.instance().getString(R.string.unknow_error));
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            doFailed(ERROR_CODE_UNKNOWN, MyApp.instance().getString(R.string.unknow_error));
                            return;
                        }
                        String result = response.body().string();
                        Log.i(TAG, "getRoomList, onResponse: result -> " + result);
                        if (!TextUtils.isEmpty(result)) {
                            Gson gson = new Gson();
                            ResponseEntity res = gson.fromJson(result, ResponseEntity.class);
                            if (res.errorCode == 0 && res.data != null) {
                                List<RoomInfo> roomInfoList = res.data;
                                List<String> roomIdList = new ArrayList<>();
                                for (RoomInfo info : roomInfoList) {
                                    roomIdList.add(info.roomId);
                                }
                                doSuccess(roomIdList);
                            } else {
                                doFailed(res.errorCode, res.errorMessage);
                            }
                        } else {
                            doFailed(ERROR_CODE_UNKNOWN, MyApp.instance().getString(R.string.unknow_error));
                        }
                    }

                    private void doFailed(final int code, final String msg) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onFailed(code, msg);
                                }
                            }
                        });
                    }

                    private void doSuccess(final List<String> roomIdList) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onSuccess(roomIdList);
                                }
                            }
                        });
                    }
                });
    }

    public void getScenesRoomInfos(final Context context, String type, final GetScenesRoomInfosCallback callback) {
        RoomManager.getInstance().getRoomList(type, new RoomManager.GetRoomListCallback() {
            @Override
            public void onSuccess(List<String> roomIdList) {
                if (roomIdList != null && !roomIdList.isEmpty()) {
                    // 从组件出获取房间信息
                    List<Integer> roomList = new ArrayList<>();
                    for (String id : roomIdList) {
                        try {
                            roomList.add(Integer.parseInt(id));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
//                    TRTCLiveRoom.sharedInstance(context).getRoomInfos(roomList, new TRTCLiveRoomCallback.RoomInfoCallback() {
//                        @Override
//                        public void onCallback(int code, String msg, List<TRTCLiveRoomDef.TRTCLiveRoomInfo> list) {
//                            if (code == 0) {
//                                List<ScenesRoomInfoBean> scenesRoomInfos = new ArrayList<>();
//                                for (int i = 0, listSize = list.size(); i < listSize; i++) {
//                                    TRTCLiveRoomDef.TRTCLiveRoomInfo liveRoomInfo = list.get(i);
//                                    ScenesRoomInfo scenesRoomInfo = new ScenesRoomInfo();
//                                    scenesRoomInfo.anchorId = liveRoomInfo.ownerId;
//                                    scenesRoomInfo.anchorName = liveRoomInfo.ownerName;
//                                    scenesRoomInfo.roomId = String.valueOf(liveRoomInfo.roomId);
//                                    scenesRoomInfo.coverUrl = liveRoomInfo.coverUrl;
//                                    scenesRoomInfo.roomName = liveRoomInfo.roomName;
//                                    scenesRoomInfo.memberCount = liveRoomInfo.memberCount;
//                                    scenesRoomInfos.add(scenesRoomInfo);
//                                }
//                                doSuccess(scenesRoomInfos);
//                            } else {
//                                doSuccess(new ArrayList<ScenesRoomInfo>());
//                            }
//                        }
//                    });
                } else {
                    doSuccess(new ArrayList<ScenesRoomInfoBean>());
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                doFailed(code, msg);
            }

            private void doFailed(final int code, final String msg) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFailed(code, msg);
                        }
                    }
                });
            }

            private void doSuccess(final List<ScenesRoomInfoBean> roomInfos) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onSuccess(roomInfos);
                        }
                    }
                });
            }
        });
    }

    public void checkRoomExist(String type, final int roomId, final ActionCallback callback) {
        getRoomList(type, new GetRoomListCallback() {
            @Override
            public void onSuccess(List<String> roomIdList) {
                if (roomIdList.contains(String.valueOf(roomId))) {
                    doSuccess();
                } else {
                    doFailed(-1, "roomId not exist.");
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                doFailed(code, msg);
            }

            private void doFailed(final int code, final String msg) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFailed(code, msg);
                        }
                    }
                });
            }

            private void doSuccess() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }
                });
            }
        });
    }

    private class ResponseEntity {
        public int errorCode;
        public String errorMessage;
        public List<RoomInfo> data;
    }

    public static class RoomInfo {
        public String roomId;
    }

    // 操作回调
    public interface ActionCallback {
        void onSuccess();

        void onFailed(int code, String msg);
    }

    // 操作回调
    public interface GetRoomListCallback {
        void onSuccess(List<String> roomIdList);

        void onFailed(int code, String msg);
    }

    public interface GetScenesRoomInfosCallback {
        void onSuccess(List<ScenesRoomInfoBean> scenesRoomInfos);

        void onFailed(int code, String msg);
    }

    //显示pk按钮
    public static void createRoom(Context context) {
        LiveRoomAnchorActivity.start(context, "");
    }

    //不显示pk按钮
    public static void createRoom(Context context, String groupId) {
        LiveRoomAnchorActivity.start(context, groupId);
    }

    /**
     * gridView和listView的item跳转
     *
     * @param context
     * @param info
     * @param currentItem
     */
    public static void enterRoom(Context context, List<ScenesRoomInfoBean> info, int currentItem, String from) {
        if (Constents.isShowAnchorFloatWindow) {
            ToastUtil.show(context, "直播中无法去其他直播间");
            return;
        }
        if (Constents.isShowAudienceFloatWindow) {
            if (String.valueOf(info.get(currentItem).getUid()).equals(FloatWindowLayout.getInstance().getAnchorId())) { //同一个主播
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.AUDIENCE_RESUME_LIVING, "", ""));
            } else {
                FloatWindowLayout.getInstance().closeFloatWindow();
                Intent intent = new Intent(context, LiveRoomSwitchActivity.class);
                intent.putExtra(RoomManager.ROOM_INFO, (Serializable) info);
                intent.putExtra(RoomManager.ROOM_ITEM, currentItem);
                intent.putExtra(RoomManager.ROOM_CLICK_FROM, from);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } else {
            Intent intent = new Intent(context, LiveRoomSwitchActivity.class);
            intent.putExtra(RoomManager.ROOM_INFO, (Serializable) info);
            intent.putExtra(RoomManager.ROOM_ITEM, currentItem);
            intent.putExtra(RoomManager.ROOM_CLICK_FROM, from);
            context.startActivity(intent);
        }
    }

    public static void enterRoom(Context context, String uid, int roomId) {
        ScenesRoomInfoBean scenesRoomInfoBean = new ScenesRoomInfoBean();
        scenesRoomInfoBean.setUid(uid);
        scenesRoomInfoBean.setRoom_id(roomId);
        enterRoom(context, scenesRoomInfoBean);
    }

    public static void enterRoom(Context context, String uid, String roomId) {
        ScenesRoomInfoBean scenesRoomInfoBean = new ScenesRoomInfoBean();
        scenesRoomInfoBean.setUid(uid);
        scenesRoomInfoBean.setRoom_id(Integer.parseInt(roomId));
        enterRoom(context, scenesRoomInfoBean);
    }

    public static void enterRoom(Context context, ScenesRoomInfo scenesRoomInfoBean) {
        if (Constents.isShowAnchorFloatWindow) {
            ToastUtil.show(context, "直播中无法去其他直播间");
            return;
        }

        Intent intent = new Intent(context, LiveRoomSwitchActivity.class);
        intent.putExtra(RoomManager.ROOM_INFO, (Serializable) Arrays.asList(scenesRoomInfoBean));
        intent.putExtra(RoomManager.ROOM_ITEM, 0);
        intent.putExtra(RoomManager.ROOM_CLICK_FROM, "");
        context.startActivity(intent);
    }


    public static void enterRoom(Context context, ScenesRoomInfoBean roomInfoBean) {
        if (Constents.isShowAudienceFloatWindow) {
            if (roomInfoBean.getUid().equals(FloatWindowLayout.getInstance().getAnchorId())) { //同一个主播
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.AUDIENCE_RESUME_LIVING, "", ""));
            } else {
                getWatchPermission(context, roomInfoBean);
            }
        } else {
            getWatchPermission(context, roomInfoBean);
        }
    }

    static boolean isRecord = false;
    public static void getWatchPermission(final Context context, final ScenesRoomInfoBean scenesRoomInfo) {
        HttpHelper.getInstance().getWatchLivePower(scenesRoomInfo.getUid(), new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                LivePermissionBean livePermissionBean = GsonUtil.GsonToBean(data, LivePermissionBean.class);
                if (livePermissionBean != null && livePermissionBean.getData() != null) {
                    LivePermissionBean.DataBean permissionBean = livePermissionBean.getData();
                    isRecord = livePermissionBean.getData().getIs_screenshot().equals("1");
                    if ("1".equals(permissionBean.getIs_ticket())) {
                        if ("1".equals(permissionBean.getIs_buy())) {
                            gotoLiveRoom(context, scenesRoomInfo);
                        } else {
                            if ("0".equals(permissionBean.getTry_num())) {
                                //ToastUtil.show(context,"该买票了");
                                showTicketBuyPop(context, permissionBean.getTry_msg(), scenesRoomInfo);
                            } else {
                                gotoLiveRoomLimit(context, scenesRoomInfo, Integer.parseInt(permissionBean.getTry_time()));
                            }
                        }
                    } else {
                        gotoLiveRoom(context, scenesRoomInfo);
                    }
                }
            }

            @Override
            public void onFail(int code, String msg) {
                if (code == 8888) {
                    showInputPwdDialog(context,scenesRoomInfo);
                } else {
                    ToastUtil.show(context, msg);
                }
            }
        });
    }

    public static void gotoLiveRoom(Context context, ScenesRoomInfoBean scenesRoomInfo) {
        if (Constents.isShowAudienceFloatWindow) {
            FloatWindowLayout.getInstance().closeFloatWindow();
        }
        Intent intent = new Intent(context, LiveRoomSwitchActivity.class);
        intent.putExtra(RoomManager.ROOM_INFO, (Serializable) Arrays.asList(scenesRoomInfo));
        intent.putExtra(RoomManager.ROOM_ITEM, 0);
        intent.putExtra(RoomManager.ROOM_CLICK_FROM, "from");
        intent.putExtra("canRecord",isRecord);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void gotoLiveRoomLimit(Context context, ScenesRoomInfoBean scenesRoomInfo, int tryTime) {
        if (Constents.isShowAudienceFloatWindow) {
            FloatWindowLayout.getInstance().closeFloatWindow();
        }
        Intent intent = new Intent(context, LiveRoomSwitchActivity.class);
        intent.putExtra(RoomManager.ROOM_INFO, (Serializable) Arrays.asList(scenesRoomInfo));
        intent.putExtra(RoomManager.ROOM_ITEM, 0);
        intent.putExtra(RoomManager.ROOM_CLICK_FROM, "from");
        intent.putExtra(RoomManager.TICKET_ROOM_TRY_TIME, tryTime);
        intent.putExtra("canRecord",isRecord);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showTicketBuyPop(final Context context, String tip, final ScenesRoomInfoBean scenesRoomInfo) {
        final NormalTipsPop normalTipsPop = new NormalTipsPop
                .Builder(context)
                .setTitle("温馨提示")
                .setInfo(tip)
                .setCancelStr("取消")
                .setConfirmStr("支付")
                .build();
        normalTipsPop.showPopupWindow();
        normalTipsPop.setOnPopClickListener(new NormalTipsPop.OnPopClickListener() {
            @Override
            public void cancelClick() {
                normalTipsPop.dismiss();
            }

            @Override
            public void confirmClick() {
                normalTipsPop.dismiss();
                payTicket(context, scenesRoomInfo);
            }
        });
    }

    public static void payTicket(final Context context, final ScenesRoomInfoBean scenesRoomInfo) {
        HttpHelper.getInstance().buyTicket(scenesRoomInfo.getUid(), new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(context, "购买成功");
                gotoLiveRoom(context, scenesRoomInfo);
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(context, msg);
            }
        });
    }

    public static void gotoQiNiuRoom(final Context context, String uid, String roomId) {
        if (Constents.isShowAudienceFloatWindow) {
            FloatWindowLayout.getInstance().closeFloatWindow();
        }
        Intent intent = new Intent(context, QNLiveRoomAudienceActivity.class);
        intent.putExtra("roomId", roomId);
        intent.putExtra("uid", uid);
        context.startActivity(intent);
        //intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
    }


    /**
     * ------------------------------------------------------------------------
     */

    public static void enterLiveRoom(Context context,String anchorId,int roomId) {
        ScenesRoomInfoBean scenesRoomInfoBean = new ScenesRoomInfoBean();
        scenesRoomInfoBean.setUid(anchorId);
        scenesRoomInfoBean.setRoom_id(roomId);
        enterRoom(context, scenesRoomInfoBean);
       // QNRoomManager.getInstance().gotoLiveRoom(context,anchorId,roomId);
    }

    private static void showInputPwdDialog(Context context, final ScenesRoomInfoBean scenesRoomInfo) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_photo_inpsw_dialog);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;//显示dialog的时候,就显示软键盘
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(params);
        final EditText etPsw = (EditText) window.findViewById(R.id.item_photo_inpsw_edittext);
        TextView tvConfirm = (TextView) window.findViewById(R.id.item_photo_inpsw_confim);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPsw.getText().toString();
                if (TextUtils.equals(password, "")) {
                    ToastUtil.show(context, "请输入密码");
                } else {
                    //验证密码
                    //chargePhotoPwd(password);
                    checkPassword(context,scenesRoomInfo,password);
                    alertDialog.dismiss();
                }
            }
        });
        TextView tvCancel = (TextView) window.findViewById(R.id.item_photo_inpsw_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    public static void checkPassword(Context context, final ScenesRoomInfoBean scenesRoomInfo,String pwd) {
        HttpHelper.getInstance().verifyLivePassword(scenesRoomInfo.getUid(), pwd, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                isRecord = "1".equals(MyApp.isAdmin);
                gotoLiveRoom(context,scenesRoomInfo);
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(context,msg);
            }
        });
    }

}
