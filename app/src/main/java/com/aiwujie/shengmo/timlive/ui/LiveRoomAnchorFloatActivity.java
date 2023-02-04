package com.aiwujie.shengmo.timlive.ui;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.ReportActivity;
import com.aiwujie.shengmo.activity.ranking.LiveRankingActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.ApplyLiveAuthData;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.customview.SharedPop;
import com.aiwujie.shengmo.eventbus.StopLiveEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.bean.NormalMenuItem;
import com.aiwujie.shengmo.kt.listener.OnSimplePopListener;
import com.aiwujie.shengmo.kt.ui.view.ConversationPop;
import com.aiwujie.shengmo.kt.ui.view.LiveInfoPop;
import com.aiwujie.shengmo.kt.ui.view.LiveOnlineUserPop;
import com.aiwujie.shengmo.kt.ui.view.NormalMenuPopup;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.HttpResult;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.tim.utils.Constants;
import com.aiwujie.shengmo.tim.utils.DemoLog;
import com.aiwujie.shengmo.timlive.helper.LiveHttpRequest;
import com.aiwujie.shengmo.timlive.net.HeartbeatManager;
import com.aiwujie.shengmo.timlive.net.RoomManager;
import com.aiwujie.shengmo.timlive.view.GiftRankingListPop;
import com.aiwujie.shengmo.timlive.view.GiftViewPagerPop;
import com.aiwujie.shengmo.timlive.view.LiveAnchorSettingPop;
import com.aiwujie.shengmo.timlive.view.LiveRoomHeadPop;
import com.aiwujie.shengmo.utils.CropUtils;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.PhotoRemoteUtil;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PhotoUploadUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.NormalTipsPop;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.google.gson.Gson;
import com.tencent.liteav.custom.Constents;
import com.tencent.liteav.custom.FloatVideoWindowService2;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.model.LiveMessageInfo;
import com.tencent.qcloud.tim.tuikit.live.bean.AnchorLiveCardBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveSettingStateBean;
import com.tencent.qcloud.tim.tuikit.live.bean.ShareAnchorBean;
import com.tencent.qcloud.tim.tuikit.live.bean.TimCustomMessage;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.TopToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.TUILiveRoomAnchorLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.AppLiveGiftChangedEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.ReportLiveMessageBean;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.LiveRoomAnchorFragment;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.LiveRoomAudienceFragment;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.LiveRoomPreviewLayout;
import com.tencent.qcloud.tim.tuikit.live.utils.PermissionUtils;
import com.tencent.qcloud.tim.uikit.base.IBaseMessageSender;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.base.TUIKitListenerManager;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.yalantis.ucrop.UCrop;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;
import static com.aiwujie.shengmo.timlive.view.LiveRoomHeadPop.TAG_ANCHOR;

public class LiveRoomAnchorFloatActivity extends AppCompatActivity implements TUILiveRoomAnchorLayout.TUILiveRoomAnchorLayoutDelegate, OnItemClickListener, EasyPermissions.PermissionCallbacks, LiveRoomAudienceFragment.OnLiveOperateClickListener {
    private Context mContext;
    private static final String TAG = "LiveRoomAnchorActivity";

    private TUILiveRoomAnchorLayout mLayoutTuiLiverRomAnchor;
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private static final int MY_PERMISSIONS_REQUEST_SELECT_PHONE = 8;
    private String mGroupID;
    private Uri cropUri;
    private File cropImage;
    private int picFlag;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private ImageView mEditPersonmsgIcon;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 152:
                    if (picFlag == 1) {
                        String s = (String) msg.obj;
                        BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
                        String imgUrl = imgpre + beanicon.getData();
                        if (liveInfoPop != null) {
                            liveInfoPop.refreshImageUrl(imgUrl);
                        }
                    } else {
                        String s = (String) msg.obj;
                        boolean isGson = GsonUtil.isJson(s);
                        if (isGson) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if (!jsonObject.isNull("retcode")) {
                                    String isCode = jsonObject.getString("retcode");
                                    if (isCode.equals("400")) {
                                        ToastUtil.show(LiveRoomAnchorFloatActivity.this, jsonObject.getString("msg"));
                                        return;
                                    }
                                    BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
                                    if (beanicon.getRetcode() == 2000) {
                                        headurl = beanicon.getData();
                                        ImageLoader.loadImage(LiveRoomAnchorFloatActivity.this, imgpre + headurl, mEditPersonmsgIcon);
                                        livePoster = imgpre + headurl;
                                    }
                                    ToastUtil.show(getApplicationContext(), "上传完成");
                                    SharedPreferencesUtils.setParam(LiveRoomAnchorFloatActivity.this, Constants.EDIT_LIVE_ICON, imgpre + headurl);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtil.show(LiveRoomAnchorFloatActivity.this, s);
                        }
                        break;
                    }
            }
            super.handleMessage(msg);
        }
    };
    private String headurl;
    private String imgpre;
    private String mCoverUrl;
    private TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo;
    //private String pushAddress;

    public static void start(Context context, String groupID) {
        Intent starter = new Intent(context, LiveRoomAnchorFloatActivity.class);
        if (context instanceof Application) {
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        starter.putExtra(RoomManager.GROUP_ID, groupID);
        context.startActivity(starter);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
        } else {
        }
        //imgpre = SharedPreferencesUtils.geParam(LiveRoomAnchorActivity.this, "image_host", "");
        setContentView(R.layout.test_activity_live_room_anchor);
        //getAuthData();
        getAnchorCard();
        imgpre = SharedPreferencesUtils.geParam(this, "image_host", "");
        mGroupID = getIntent().getStringExtra(RoomManager.GROUP_ID);
        // 判断当前的房间类型
        mLayoutTuiLiverRomAnchor = findViewById(R.id.tui_liveroom_anchor_layout);
        onLoadPreview();

        mLayoutTuiLiverRomAnchor.setLiveRoomAnchorLayoutDelegate(this);
        mLayoutTuiLiverRomAnchor.getLiveRomAnchor().setOnLiveOperateClickListener(this);
        mLayoutTuiLiverRomAnchor.initWithRoomId(getSupportFragmentManager());
        mLayoutTuiLiverRomAnchor.enablePK(TextUtils.isEmpty(mGroupID));

        EventBus.getDefault().register(this);
        mContext = this;

        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    String livePoster = "";
    String liveTitle = "";

    void getAuthData() {
        LiveHttpHelper.getInstance().isAuth(new HttpListener() {
            @Override
            public void onSuccess(String data) {
                LiveRoomPreviewLayout previewLayout = mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getLayoutPreview();
                ApplyLiveAuthData authData = new Gson().fromJson(data, ApplyLiveAuthData.class);
                if (!TextUtil.isEmpty(authData.getData().getLive_poster())) {
                    livePoster = authData.getData().getLive_poster();
                    if (previewLayout != null) {
                        previewLayout.showHistoryCover(LiveRoomAnchorFloatActivity.this, livePoster);
                    }
                }
                if (!TextUtil.isEmpty(authData.getData().getLive_title())) {
                    liveTitle = authData.getData().getLive_title();
                    if (previewLayout != null) {
                        previewLayout.showHistoryTitle(liveTitle);
                    }
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    //给预览页的view添加回调事件
    private void onLoadPreview() {
        //刚进入直播间顶部的CircleImageView，加载网图
        if (mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {//fragment对象实例
            //给直播间传递参数-covelUrl
            mCoverUrl = SharedPreferencesUtils.geParam(LiveRoomAnchorFloatActivity.this, Constants.EDIT_LIVE_ICON, imgpre + headurl);
            //预览页上传封皮
            mLayoutTuiLiverRomAnchor.getLiveRomAnchor().setPreviewOnClickCover(new LiveRoomPreviewLayout.PreviewOnClickCover() {
                @Override
                public void onClickCover(ImageView view) {
                    mEditPersonmsgIcon = view;
                    picFlag = 0;
                    showSelectPic();
                }
            });


            mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getLayoutPreview();

            mLayoutTuiLiverRomAnchor.getLiveRomAnchor().setPreviewOnLoadCover(new LiveRoomPreviewLayout.PreviewOnLoadCover() {

                @Override
                public void onLoadCover(final ImageView view) {
                    /*if(MyApp.resultUri != null){//加载缓存uri
                        Bitmap photo = PhotoUploadUtils.decodeUriAsBitmap(MyApp.resultUri, LiveRoomAnchorActivity.this);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        if (photo != null) {
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)压缩文件
                            //头像
                            RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), photo);
                            circleDrawable.getPaint().setAntiAlias(true);
                            circleDrawable.setCircular(true);
                            view.setImageDrawable(circleDrawable); //把图片显示在ImageView控件上
                        }
                    }else */
//                    V2TIMManager.getInstance().getUsersInfo(Arrays.asList(MyApp.uid), new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
//                        @Override
//                        public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
//                           if(v2TIMUserFullInfos != null && v2TIMUserFullInfos.size() > 0){
//                               String headPic = v2TIMUserFullInfos.get(0).getFaceUrl();
//                               if (!TextUtils.isEmpty(headPic)) {//加载缓存url
//                                   Glide.with(LiveRoomAnchorActivity.this).load(headPic).into(view);
//                                   livePoster = headPic;
//                               } else {
//                                   //默认背景图
//                                   Glide.with(LiveRoomAnchorActivity.this).load(R.drawable.live_ic_avatar).centerCrop().into(view);
//                               }
//                           }
//                        }
//
//                        @Override
//                        public void onError(int code, String desc) {
//                            LogUtil.e(desc);
//                        }
//                    });
                }

                @Override
                public void onLoadLiveTitle(String live_title, String is_interact) {
                    beginToShow(live_title, is_interact);
                }
            });
        }
    }

    //开播
    private void beginToShow(final String live_title, final String is_interact) {
//       if (TextUtil.isEmpty(headurl)) {
//           mCoverUrl = SharedPreferencesUtils.geParam(LiveRoomAnchorActivity.this,Constants.EDIT_LIVE_ICON,"");
//       } else {
//           mCoverUrl = SharedPreferencesUtils.geParam(LiveRoomAnchorActivity.this,Constants.EDIT_LIVE_ICON,imgpre + headurl);
//       }

        String tid = mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getLayoutPreview().getChooseLabel();


        LiveHttpHelper.getInstance().beginToShow(livePoster, live_title, tid, is_interact,"","" ,"","","",new HttpListener() {
            @Override
            public void onSuccess(String data) {
                LogUtil.e(data);
                Log.d("onSuccess", data);
                try {
                    JSONObject obj = new JSONObject(data);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            JSONObject root = obj.getJSONObject("data");
                            if (root != null || root.length() != 0) {
                                String nickName = root.getString("nickname");
                                String roomId = root.getString("room_id");
                                String live_title = root.getString("live_title");
                                String live_poster = root.getString("live_poster");
                                mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getLayoutPreview().startLive(live_title, live_poster);
                                LiveRoomAnchorFragment mLiveRoomAnchorFragment = mLayoutTuiLiverRomAnchor.getLiveRomAnchor();
                                Bundle b = new Bundle();
                                //b.putString("push_address",pushAddress);
                                if (roomId != null) {
                                    b.putInt("room_id", Integer.valueOf(!TextUtils.isEmpty(roomId) && !"null".equals(roomId) ? roomId : ""));
                                    SharedPreferencesUtils.setParam(LiveRoomAnchorFloatActivity.this, "room_id", roomId);
                                    mLiveRoomAnchorFragment.setArguments(b);
                                    mLiveRoomAnchorFragment.initData();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.live_anchor_container, mLiveRoomAnchorFragment, "tuikit-live-anchor-fragment");
                                }
                            }
                            //开启直播间消息通知
                            LiveRoomAnchorFragment mLiveRoomAnchorFragment = mLayoutTuiLiverRomAnchor.getLiveRomAnchor();
                            mLiveRoomAnchorFragment.setOnLiveCreateRoomCallBack(new LiveRoomAnchorFragment.OnLiveCreateRoomCallBack() {
                                @Override
                                public void createRoomSuccess() {
                                    final String selfUserId = ProfileManager.getInstance().getUserModel().userId;
                                    getAnchorInfo(selfUserId);
                                }
                            });
                            startHeaderReport();
                            break;
                        default:
                            ToastUtil.show(LiveRoomAnchorFloatActivity.this, obj.getString("msg"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String msg) {
                LogUtil.e(msg);
                //轮询去调用，直到roomid唯一
                String roomId = SharedPreferencesUtils.geParam(LiveRoomAnchorFloatActivity.this, "room_id", "");
                LiveHttpHelper.getInstance().closeLive(roomId, new HttpListener() {
                    @Override
                    public void onSuccess(String data) {
                        LogUtil.d(data);
                        // beginToShow(live_title);
                    }

                    @Override
                    public void onFail(String msg) {
                        LogUtil.d(msg);
                    }
                });
                if (!TextUtils.isEmpty(msg)) {
                    ToastUtil.show(LiveRoomAnchorFloatActivity.this, msg);
                    return;
                }
            }
        });
    }

    /**
     * 进入直播间获取消息
     *
     * @param selfUserId
     */
    private void getAnchorInfo(final String selfUserId) {
        LiveHttpHelper.getInstance().getAnchorInfo(selfUserId, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject obj = new JSONObject(data);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            Log.e(TAG, data);
                            //LiveRoomInfo liveRoomInfo =  GsonUtil.GsonToBean(data, LiveRoomInfo.class);
                            //Log.e(TAG,liveRoomInfo.toString());
                            final LiveRoomAnchorFragment mLiveRoomAnchorFragment = mLayoutTuiLiverRomAnchor.getLiveRomAnchor();
                            Bundle b = new Bundle();
                            if (data != null) {
                                b.putString("liveRoomInfo", data);
                                b.putString("gift_url", LiveHttpHelper.getInstance().liveGiftList(HttpUrl.liveGiftList));
                                mLiveRoomAnchorFragment.setArguments(b);
                                mLiveRoomAnchorFragment.initData();
                                getSupportFragmentManager().beginTransaction().replace(R.id.live_anchor_container, mLiveRoomAnchorFragment, "tuikit-live-anchor-fragment");
                            }
                            break;
                        default:
                            ToastUtil.show(LiveRoomAnchorFloatActivity.this, obj.getString("msg"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String msg) {
                Log.e(TAG, msg);
            }
        });
    }

    public void getAnchorCard() {
        HttpHelper.getInstance().getAnchorCard("", new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                LiveRoomPreviewLayout previewLayout = mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getLayoutPreview();
                AnchorLiveCardBean labelBean = GsonUtil.GsonToBean(data, AnchorLiveCardBean.class);
                if (previewLayout != null) {
                    previewLayout.showLiveLabel(labelBean.getData());
                    livePoster = labelBean.getData().getLive_poster();
                    liveTitle = labelBean.getData().getLive_title();
                    previewLayout.showHistoryTitle(liveTitle);
                    previewLayout.showHistoryCover(LiveRoomAnchorFloatActivity.this, livePoster);
                    previewLayout.showHistoryInteraction(labelBean.getData().getIs_interaction());
                    previewLayout.showInteractTip(labelBean.getData().getInteraction_tips());
                }
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    private void showSelectPic() {
        new AlertView(null, null, "取消", null,
                new String[]{"拍照", "从相册中选择"},
                LiveRoomAnchorFloatActivity.this, AlertView.Style.ActionSheet, LiveRoomAnchorFloatActivity.this).show();
    }

    /*private int getRoomId() {
        // 这里我们用简单的 userId hashcode，然后
        // 您的room id应该是您后台生成的唯一值
        String ownerId =  V2TIMManager.getInstance().getLoginUser();
        return (mGroupID + ownerId + "liveRoom").hashCode() & 0x7FFFFFFF;
    }*/

    @Override
    public void onBackPressed() {
        mLayoutTuiLiverRomAnchor.onBackPress();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {
        }
    }

    @Override
    public void onClose() {
        finish();
    }

    @Override
    public void onError(TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo, int errorCode, String errorMsg) {
        //参照TXRoomService-createRoom
        if (errorCode != 0) {
            ToastUtil.show(mContext, getString(R.string.live_enter_room_fail));
        }
    }

    @Override
    public void onRoomCreate(final TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo) {
        this.roomInfo = roomInfo;
        // 创建房间
        String type = RoomManager.TYPE_LIVE_ROOM;
        if (!TextUtils.isEmpty(mGroupID)) {
            sendLiveGroupMessage(roomInfo, 1);
            type = RoomManager.TYPE_GROUP_LIVE;
        }
        final String finalType = type;
        RoomManager.getInstance().createRoom(roomInfo.roomId, finalType, new RoomManager.ActionCallback() {
            @Override
            public void onSuccess() {
                HeartbeatManager.getInstance().start(finalType, roomInfo.roomId);
            }

            @Override
            public void onFailed(int code, String msg) {

            }
        });
    }

    @Override
    public void onRoomDestroy(TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo) {
        // 销毁房间
        onRoomDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁房间
        onRoomDestroy();
        stopHeaderReport();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mHomeKeyEventReceiver);
    }

    private void onRoomDestroy() {
        String type = RoomManager.TYPE_LIVE_ROOM;
        if (!TextUtils.isEmpty(mGroupID)) {
            sendLiveGroupMessage(roomInfo, 0);
            type = RoomManager.TYPE_GROUP_LIVE;
        }
        if (roomInfo != null) {
            RoomManager.getInstance().destroyRoom(roomInfo.roomId, type, null);
            HeartbeatManager.getInstance().stop();
            closeLiveRoom(String.valueOf(roomInfo.roomId));
        }
    }

    @Override
    public void getRoomPKList(final TUILiveRoomAnchorLayout.OnRoomListCallback callback) {
        RoomManager.getInstance().getRoomList(RoomManager.TYPE_LIVE_ROOM, new RoomManager.GetRoomListCallback() {
            @Override
            public void onSuccess(List<String> roomIdList) {
                if (callback != null) {
                    callback.onSuccess(roomIdList);
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                if (callback != null) {
                    callback.onFailed();
                }
            }
        });
    }

    private void sendLiveGroupMessage(TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo, int roomStatus) {
        if (roomInfo == null) {
            return;
        }
        LiveMessageInfo liveMessageInfo = new LiveMessageInfo();
        liveMessageInfo.version = 1;
        liveMessageInfo.roomId = roomInfo.roomId;
        liveMessageInfo.roomName = roomInfo.roomName;
        liveMessageInfo.roomType = RoomManager.TYPE_LIVE_ROOM;
        liveMessageInfo.roomCover = roomInfo.coverUrl;
        liveMessageInfo.roomStatus = roomStatus;
        liveMessageInfo.anchorId = roomInfo.ownerId;
        liveMessageInfo.anchorName = roomInfo.ownerName;
        sendLiveGroupMessage(mGroupID, liveMessageInfo, null);
    }


    public void sendLiveGroupMessage(String groupID, LiveMessageInfo info, final IUIKitCallBack callBack) {
        IBaseMessageSender baseMessageSender = TUIKitListenerManager.getInstance().getMessageSender();
        if (baseMessageSender == null) {
            DemoLog.e(TAG, "sendLiveGroupMessage failed messageSender is null");
            return;
        }
        Gson gson = new Gson();
        String data = gson.toJson(info);
        MessageInfo messageInfo = MessageInfoUtil.buildCustomMessage(data);
        baseMessageSender.sendMessage(messageInfo, null, groupID, true, false, callBack);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (!perms.contains(Manifest.permission.CAMERA)) {
                return;
            }
            if (!perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return;
            }
            takePhoto();
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2) {
            if (!perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return;
            }
            choosePhoto();
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_SELECT_PHONE) {
            if (!perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return;
            }
            choosePhotoBySelector();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (perms.contains(Manifest.permission.CAMERA)) {
            ToastUtil.show(getApplicationContext(), "授权失败,请开启相机权限");
            return;
        }
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(getApplicationContext(), "授权失败,请开启读写权限");
            return;
        }
    }

    //拍照
    public void takePhone(View view) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", MY_PERMISSIONS_REQUEST_CALL_PHONE, perms);
        } else {
            takePhoto();
        }

    }

    //相册
    public void choosePhone(View view) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", MY_PERMISSIONS_REQUEST_CALL_PHONE2, perms);
        } else {
            choosePhoto();
        }
    }


    @Override
    public void onItemClick(Object o, int position, String data) {
        switch (position) {
            case 0:
                takePhone(mEditPersonmsgIcon);
                break;
            case 1:
                choosePhone(mEditPersonmsgIcon);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                Uri crUri = data.getData();
                CropUtils.startUCrop(this, crUri, CROP_REQUEST_CODE, 1, 1, 700, 700);
//                startCrop(crUri);
                break;
            case CAMERA_REQUEST_CODE:
                CropUtils.startUCrop(this, cropUri, CROP_REQUEST_CODE, 1, 1, 700, 700);
                break;
            case CROP_REQUEST_CODE:
                Uri resultUri = null;
                try {
                    resultUri = UCrop.getOutput(data);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if (resultUri != null) {
                    Bitmap photo = PhotoUploadUtils.decodeUriAsBitmap(resultUri, this);
                    MyApp.resultUri = resultUri;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    if (photo != null) {
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)压缩文件
                        switch (picFlag) {
                            case 0:
                                //头像
                                RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), photo);
                                circleDrawable.getPaint().setAntiAlias(true);
                                circleDrawable.setCircular(true);
                                mEditPersonmsgIcon.setImageDrawable(circleDrawable); //把图片显示在ImageView控件上
                                break;
                            case 10:
                                //相册

                                break;

                        }
                        ToastUtil.show(getApplicationContext(), "图片上传中,请稍后提交...");
                    }
                    if (picFlag == 0) {
                        // 获得字节流
                        ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
                        PhotoUploadTask put = new PhotoUploadTask(
                                NetPic() + "Api/Api/fileUpload"  //"http://59.110.28.150:888/Api/Api/fileUpload"
                                , is,
                                this, handler);
                        put.start();
                    } else {
                        // 获得字节流
                        ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
                        PhotoUploadTask put = new PhotoUploadTask(
                                NetPic() + "Api/Api/filePhoto"//  "http://59.110.28.150:888/Api/Api/filePhoto"
                                , is,
                                this, handler);
                        put.start();
                    }
                } else {
                    return;
                }
                break;
            case 10000:
                if (data != null) {
                    ToastUtil.show(LiveRoomAnchorFloatActivity.this, "图片上传中");
                    //获取选择器返回的数据
                    ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                    for (int i = 0; i < images.size(); i++) {
                        LogUtil.d(images.get(i));
                        uploadImage(images.get(i));
                        //sendMessageByPath(images.get(i));
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 拍照
     */
    void takePhoto() {
        try {
            cropImage = new File(Environment.getExternalStorageDirectory(), "/" + UUID.randomUUID() + ".jpg");
            cropUri = Uri.fromFile(cropImage);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (Exception e) {

        }
    }

    /**
     * 从相册选取图片
     */
    void choosePhoto() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
            startActivityForResult(intent, ALBUM_REQUEST_CODE);
        } catch (Exception e) {

        }
    }

    @Override
    public void onMoreLiveClick() {

    }

    @Override
    public void onLiveSetHeadViewInfo(TopToolBarLayout mLayoutTopToolBar, String mAnchorId) {

    }

    @Override
    public void onLiveStarNumber(View view, String anchor_id) {
        GiftViewPagerPop pop = new GiftViewPagerPop(LiveRoomAnchorFloatActivity.this, R.style.BottomFullDialog, anchor_id);
        pop.show();
    }

    @Override
    public void onClickAnchorAvatar(String uid, String anchorId) {
//        TimCustomMessage tb = new TimCustomMessage();
//        tb.setUid(uid);
//        tb.setAnchor_uid(anchorId);
//        final LiveRoomHeadPop onClickAnchorAvatar = new LiveRoomHeadPop(LiveRoomAnchorActivity.this,tb,TAG_ANCHOR);
//        onClickAnchorAvatar.showPopupWindow();
        showLiveCardPop(anchorId);
    }

    @Override
    public void onClickAudience(final TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo) {
        TimCustomMessage tb = new TimCustomMessage();
        tb.setUid(audienceInfo.userId);
        tb.setAnchor_uid(MyApp.uid);
        final LiveRoomHeadPop onClickAudiencePop = new LiveRoomHeadPop(LiveRoomAnchorFloatActivity.this, tb, TAG_ANCHOR);
        initPopListener(onClickAudiencePop, tb.getUid());
        onClickAudiencePop.showPopupWindow();
    }

    @Override
    public void followAnchor(String uid, TopToolBarLayout mLayoutTopToolBar) {
        LiveHttpRequest.followUserById(mContext, uid, mLayoutTopToolBar);
    }

    @Override
    public void showGiftPanel() {
        String token = SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", "");
        mLayoutTuiLiverRomAnchor.getLiveRomAnchor().showGiftPanel(LiveHttpHelper.getInstance().liveGiftList(HttpUrl.liveGiftList), token);
    }

    @Override
    public void reportUser(String mSelfUserId, String uid) {

    }

    @Override
    public void onClickContestNo(View v, String anchor_id) {
        GiftRankingListPop pop = new GiftRankingListPop(LiveRoomAnchorFloatActivity.this, R.style.BottomFullDialog, anchor_id);
        pop.show();
    }

    @Override
    public void clickLikeFrequencyControl(final String anchor_id) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.show(LiveRoomAnchorFloatActivity.this, anchor_id + "===");
            }
        });
    }

    @Override
    public void closeLive(String uid) {
    }


    NormalTipsPop normalTipsPop;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showTip(TimCustomMessage timCustomMessage) {
        showCaveat(timCustomMessage);
    }

    //弹出警告框
    private void showCaveat(TimCustomMessage timRoomMessageBean) {
        if (normalTipsPop == null) {
            normalTipsPop = new NormalTipsPop.Builder(LiveRoomAnchorFloatActivity.this)
                    .setTitle("提醒")
                    .setInfo(timRoomMessageBean.getContent())
                    .setCancelStr("")
                    .setConfirmStr("好的")
                    .build();
        } else {
            if (normalTipsPop.isShowing()) {
                return;
            }
            normalTipsPop.updateInfo(timRoomMessageBean.getContent());
        }

        normalTipsPop.setOutSideTouchable(false);
        normalTipsPop.update();
        normalTipsPop.setOnPopClickListener(new NormalTipsPop.OnPopClickListener() {
            @Override
            public void cancelClick() {
                normalTipsPop.dismiss();
            }

            @Override
            public void confirmClick() {
                normalTipsPop.dismiss();
            }
        });
        normalTipsPop.showPopupWindow();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveLiveEvent(LiveMethodEvent event) {
        switch (event.getType()) {
            case LiveEventConstant.CLICK_LIVE_USER:
                final String data = event.getData();
                final TimCustomMessage dataBean = GsonUtil.GsonToBean(data, TimCustomMessage.class);
                final LiveRoomHeadPop headPop = new LiveRoomHeadPop(LiveRoomAnchorFloatActivity.this, dataBean, TAG_ANCHOR);
                initPopListener(headPop, dataBean.getUid());
                headPop.showPopupWindow();
                break;
            case "tanmuOpenSendTxt":
                String json = event.getData();
                TimCustomMessage dammuBean = GsonUtil.GsonToBean(json, TimCustomMessage.class);
                sendDanmu(dammuBean);
                break;
            case LiveEventConstant.SHOW_ONLINE_USER:
                showLiveUserPop(event.getData());
                break;
            case LiveEventConstant.link_LIVE_USER:
                if (mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().inviteLinkUser(event.getData());
                }
                break;
            case LiveEventConstant.ANCHOR_START_FLOAT:
                startFloat();
                break;
            case LiveEventConstant.SEND_GIFT:
                String giftJson = event.getData();
                GiftInfo giftInfo = (GiftInfo) GsonUtil.GsonToBean(giftJson, GiftInfo.class);
                sendGift(giftInfo);
                break;
            case LiveEventConstant.SHARE_LIVE:
                showSharePop(event.getData());
                break;
            case LiveEventConstant.SHOW_LIVE_CARD:
                showLiveCardPop("");
                break;
            case LiveEventConstant.SHOW_GIFT_PANEL:
                showGiftPanel();
                break;
            case LiveEventConstant.UPDATE_LINK_MIC:
                updateLinkMicUser(event.getData());
                break;
            case LiveEventConstant.REPORT_LIVE_MESSAGE:
                reportChatMessage(event.getData());
                break;
            case LiveEventConstant.GET_BEAUTY_PERMISSION:
                reportBeautyPermission();
                break;
            case LiveEventConstant.SHOW_LIVE_ANCHOR_SETTING:
                showLiveAnchorSettingPop(event.getData());
                break;
            case LiveEventConstant.SHOW_LIVE_TOP_RANK:
                Intent intent = new Intent(LiveRoomAnchorFloatActivity.this, LiveRankingActivity.class);
                startActivity(intent);
                break;
            case LiveEventConstant.SHOW_CONVERSATION:
                showConversationPop();
                break;
        }
    }

    //发送弹幕接口
    private void sendDanmu(final TimCustomMessage dammuBean) {
        if (dammuBean == null) return;
        LiveHttpHelper.getInstance().sendBarrage(dammuBean.anchor_uid, dammuBean.content, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                HttpResult httpResult = GsonUtil.GsonToBean(data, HttpResult.class);
                if (httpResult.getRetcode() == 2001) {
                    ToastUtil.show(mContext, httpResult.getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d(msg);
                ToastUtil.show(mContext, msg);
            }
        });
    }

    //送礼物调用接口
    private void sendGift(final GiftInfo giftInfo) {
        LiveHttpHelper.getInstance().sendGift(giftInfo.sendUser, giftInfo.giftId, String.valueOf(giftInfo.count), String.valueOf(giftInfo.presentType), new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                HttpResult httpResult = GsonUtil.GsonToBean(data, HttpResult.class);
                switch (httpResult.getRetcode()) {
                    case 2000:
                        //更新免费礼物的数量
                        if (giftInfo.presentType == 2) {
                            int changeNum = Integer.valueOf(giftInfo.num) - giftInfo.count;
                            giftInfo.num = String.valueOf(changeNum);
                            EventBus.getDefault().post(new AppLiveGiftChangedEvent(changeNum, giftInfo));
                        }
                        break;
                    case 2001:
                        ToastUtil.show(mContext, httpResult.getMsg());
                        break;
                }
            }

            @Override
            public void onFail(int code, String msg) {
                LogUtil.d(msg);
                ToastUtil.show(mContext, msg);
            }
        });
    }

    private void initPopListener(final LiveRoomHeadPop headPop, final String uid) {
        headPop.setOnPopOperateListener(new LiveRoomHeadPop.OnPopOperateListener() {

            @Override
            public void onFollow(TextView tvAnchorFollow, int follow_state, String uid) {
                LiveHttpRequest.followAnchor(tvAnchorFollow, mContext, uid, follow_state);
            }

            @Override
            public void onAt(String uid, String name) {
                headPop.dismiss();
                String sender = !TextUtil.isEmpty(name) ? name : uid;
                String at = "@" + sender;
                mLayoutTuiLiverRomAnchor.getLiveRomAnchor().onAt(at);
//                LiveHttpHelper.getInstance().getAnchorInfo(uid, new HttpListener() {
//                    @Override
//                    public void onSuccess(String data) {
//                        com.aiwujie.shengmo.timlive.bean.LiveRoomInfo liveRoomInfo = GsonUtil.GsonToBean(data, com.aiwujie.shengmo.timlive.bean.LiveRoomInfo.class);
//                        String  sender = !TextUtil.isEmpty(liveRoomInfo.getData().getNickname()) ? liveRoomInfo.getData().getNickname() : liveRoomInfo.getData().getUid();
//                        String at = "@" + sender;
//                        mLayoutTuiLiverRomAnchor.getLiveRomAnchor().onAt(at);
//                    }
//
//                    @Override
//                    public void onFail(String msg) {
//                        LogUtil.d(msg);
//                    }
//                });
            }

            @Override
            public void onReport(String mySelfId) {
                Intent intent = new Intent(LiveRoomAnchorFloatActivity.this, ReportActivity.class);
                intent.putExtra("source_type", 1);
                intent.putExtra("uid", mySelfId);
                LiveRoomAnchorFloatActivity.this.startActivity(intent);
            }
        });
    }

    void showLiveUserPop(String roomId) {
        LiveOnlineUserPop liveOnlineUserPop = new LiveOnlineUserPop(LiveRoomAnchorFloatActivity.this, MyApp.uid, 2);
        liveOnlineUserPop.showPopupWindow();
        liveOnlineUserPop.setOnLiveUserListener(new LiveOnlineUserPop.OnLiveUserListener() {
            @Override
            public void onLiveUserKitOut(@NotNull TimCustomMessage tcm) {

            }

            @Override
            public void onLiveUserLink(@NotNull TimCustomMessage tcm) {
                if (mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().inviteLinkUser(tcm.getUid());
                }
            }

            @Override
            public void onLiveUserClick(@NotNull TimCustomMessage tcm) {
                final LiveRoomHeadPop headPop = new LiveRoomHeadPop(LiveRoomAnchorFloatActivity.this, tcm, TAG_ANCHOR);
                initPopListener(headPop, tcm.getUid());
                headPop.showPopupWindow();
            }
        });
    }

    private boolean isStartService = false;
    private ServiceConnection mVideoServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获取服务的操作对象
            FloatVideoWindowService2.MyBinder binder = (FloatVideoWindowService2.MyBinder) service;
            binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void startFloat() {
        if (!PermissionUtils.requestFloatPermission(this, 24)) {
            ToastUtil.show(LiveRoomAnchorFloatActivity.this, "最小化需要开启悬浮窗权限");
            //com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastShortMessage("最小化需要开启悬浮窗权限");
            return;
        }
        isStartService = true;
        Constents.mAnchorTextureView = mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getVideoView().getGLSurfaceView();
        Constents.mAnchorViewView = mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getVideoView();
        Intent intent = new Intent(this, FloatVideoWindowService2.class);//开启服务显示悬浮框
        bindService(intent, mVideoServiceConnection, Context.BIND_AUTO_CREATE);
        moveTaskToBack(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isStartService) {
            isStartService = false;
            unbindService(mVideoServiceConnection);
        }
        mLayoutTuiLiverRomAnchor.getLiveRomAnchor().onActivityRestart();
    }

    public void showSharePop(String data) {
        ShareAnchorBean shareAnchorBean = GsonUtil.GsonToBean(data, ShareAnchorBean.class);
        String uid = shareAnchorBean.getAnchorId();
        String nickname = shareAnchorBean.getLiveTitle();
        String pic = shareAnchorBean.getLivePoster();
        // SharedPop sharedPop = new SharedPop(this, HttpUrl.NetPic() + HttpUrl.ShareUserDetail + uid, "正在直播", nickname + "正在圣魔精彩直播中", pic, 0, 3, "", "", "", uid);
        SharedPop sharedPop = new SharedPop(this, uid, nickname, pic,5);
        sharedPop.showAtLocation(mLayoutTuiLiverRomAnchor, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final WindowManager.LayoutParams[] params = {getWindow().getAttributes()};
        //当弹出Popupwindow时，背景变半透明
        params[0].alpha = 0.7f;
        getWindow().setAttributes(params[0]);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        sharedPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params[0] = getWindow().getAttributes();
                params[0].alpha = 1f;
                getWindow().setAttributes(params[0]);
            }
        });
    }

    LiveInfoPop liveInfoPop;

    void showLiveCardPop(String uid) {
        liveInfoPop = new LiveInfoPop(this, uid);
        liveInfoPop.showPopupWindow();
        liveInfoPop.setOnSimpleListener(new OnSimplePopListener() {
            @Override
            public void doSimplePop() {
                showChoosePhoto();
            }
        });
    }

    void showChoosePhoto() {
        final List<NormalMenuItem> normalMenuItemList = new ArrayList<>();
        normalMenuItemList.add(new NormalMenuItem(0, "从相册选择"));
        final NormalMenuPopup menuPopup = new NormalMenuPopup(LiveRoomAnchorFloatActivity.this, normalMenuItemList);
        menuPopup.showPopupWindow();
        menuPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                requestPhotoPermission();
                menuPopup.dismiss();
            }
        });
    }

    void requestPhotoPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片", MY_PERMISSIONS_REQUEST_SELECT_PHONE, perms);
        } else {
            choosePhotoBySelector();
        }
    }

    void choosePhotoBySelector() {
        ImageSelector.builder()
                .useCamera(false)
                .setSingle(false)
                .setMaxSelectCount(1)
                .canPreview(true)
                .setCrop(true)
                .setCropRatio(1f)
                .start(LiveRoomAnchorFloatActivity.this, 10000);
    }

    void uploadImage(String path) {
        picFlag = 1;
        Bitmap loadbitmap = BitmapFactory.decodeFile(path, getBitmapOption(2));
        Bitmap rotaBitmap = PhotoRemoteUtil.rotaingImageView(PhotoRemoteUtil.getBitmapDegree(path), loadbitmap);
        ByteArrayInputStream is = new ByteArrayInputStream(Bitmap2Bytes(rotaBitmap));
        PhotoUploadTask put = new PhotoUploadTask(
                NetPic() + "Api/Api/filePhoto"//  "http://59.110.28.150:888/Api/Api/filePhoto"
                , is,
                this, handler);
        put.start();
    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    Timer mTimer;

    public void startHeaderReport() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                HttpHelper.getInstance().reportAnchorHeartBeat();
            }
        }, 100, 60000);
    }

    public void stopHeaderReport() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    void updateLinkMicUser(String userStr) {
        HttpHelper.getInstance().updateLiveLinkMicList(userStr);
    }

    public void reportChatMessage(String liveMessageData) {
        ReportLiveMessageBean reportLiveMessageBean = GsonUtil.GsonToBean(liveMessageData, ReportLiveMessageBean.class);
        if (reportLiveMessageBean != null) {
            HttpHelper.getInstance().reportLiveMessage(reportLiveMessageBean.getAnchorId(),
                    reportLiveMessageBean.getRoomId(), reportLiveMessageBean.getContent(), new HttpCodeListener() {
                        @Override
                        public void onSuccess(String data) {

                        }

                        @Override
                        public void onFail(int code, String msg) {

                        }
                    });
        }
    }


    /**
     * 判断主播是否开启了悬浮窗权限
     * 如果开启了 直接跳转页面 并且开启悬浮窗
     * 未开启 显示开启提示框 不跳转页面
     *
     * @param //intent
     */
    @Override
    public void startActivity(Intent intent) {
        if (intent.getData() == null || !intent.getData().toString().contains("package")) {
            if (intent.getExtras() != null) {
                if (intent.getExtras().containsKey("_mmessage_appPackage")) { //微信分享
                    super.startActivity(intent);
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //6.0版本
                if (!Settings.canDrawOverlays(LiveRoomAnchorFloatActivity.this)) {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().showFloatPermissionDialog("主播最小化窗口，需要开启悬浮窗权限，是否开启？");
                } else {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().onBackPressed();
                    super.startActivity(intent);
                }
            } else {
                super.startActivity(intent);
            }

        } else {
            super.startActivity(intent);
        }
    }

    void reportBeautyPermission() {
        HttpHelper.getInstance().reportBeautyPermission(new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(LiveRoomAnchorFloatActivity.this, msg);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d("页面 onStop");
    }

    //最小化窗口时 退出app 结束直播
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void StopLive(StopLiveEvent event) {
        if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
            mLayoutTuiLiverRomAnchor.getLiveRomAnchor().destroyRoom();
            mLayoutTuiLiverRomAnchor.getLiveRomAnchor().finishRoom();
        }
    }

    void showLiveAnchorSettingPop(String liveSettingStateJson) {
        LiveSettingStateBean liveSettingStateBean = GsonUtil.GsonToBean(liveSettingStateJson, LiveSettingStateBean.class);
        LiveAnchorSettingPop liveAnchorSettingPop = new LiveAnchorSettingPop(LiveRoomAnchorFloatActivity.this, liveSettingStateBean);
        liveAnchorSettingPop.showPopupWindow();
        liveAnchorSettingPop.setOnLiveSettingListener(new LiveAnchorSettingPop.OnLiveSettingListener() {
            @Override
            public void onChooseCameraFront() {
                if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().doCameraFront();
                    updateCameraState(true);
                }
            }

            @Override
            public void onChooseCameraBack() {
                if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().doCameraBack();
                    updateCameraState(true);
                }
            }

            @Override
            public void onChooseCameraClose() {
                if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().doCameraClose();
                    updateCameraState(false);
                }
            }

            @Override
            public void onChooseAudioOpen() {
                if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().doAudioOpen();
                }
            }

            @Override
            public void onChooseAudioClose() {
                if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().doAudioClose();
                }
            }

            @Override
            public void onChooseAudioSetting() {
                if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().doAudioSetting();
                }
            }

            @Override
            public void onChooseBeauty() {
                if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().doVideoBeauty();
                }
            }

            @Override
            public void onChooseManagerSpeak() {

            }
        });
    }

    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        private String SYSTEM_REASON = "reason";
        private String SYSTEM_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    if (!Constents.isShowAnchorFloatWindow) {
                        onBackPressed();
                    }
                }
            }
        }
    };

    void updateCameraState(boolean isOpen) {
        HttpHelper.getInstance().updateAnchorCameraState(isOpen);
    }


    public void closeLiveRoom(String roomId) {
        HttpHelper.getInstance().closeLive(roomId);
    }

    void showConversationPop() {
        ConversationPop conversationPop = new ConversationPop(LiveRoomAnchorFloatActivity.this);
        conversationPop.showPopupWindow();
    }


}
