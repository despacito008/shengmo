package com.aiwujie.shengmo.timlive.ui;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.projection.MediaProjectionManager;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.EditPersonMsgActivity;
import com.aiwujie.shengmo.activity.MyPurseActivity;
import com.aiwujie.shengmo.activity.ReportActivity;
import com.aiwujie.shengmo.activity.ranking.LiveRankingActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.ApplyLiveAuthData;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.bean.LiveRedEnvelopesReceiveBean;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.bean.SearchUserData;
import com.aiwujie.shengmo.customview.SharedPop;
import com.aiwujie.shengmo.eventbus.StopLiveEvent;
import com.aiwujie.shengmo.eventbus.TIMLoginEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.bean.NormalMenuItem;
import com.aiwujie.shengmo.kt.bean.NormalShareBean;
import com.aiwujie.shengmo.kt.listener.CompressCallback;
import com.aiwujie.shengmo.kt.listener.OnSimplePopListener;
import com.aiwujie.shengmo.kt.ui.activity.MyLiveLevelActivity;
import com.aiwujie.shengmo.kt.ui.view.ConversationPop;
import com.aiwujie.shengmo.kt.ui.view.LiveAudienceTaskPop;
import com.aiwujie.shengmo.kt.ui.view.LiveBlindBoxRulePop;
import com.aiwujie.shengmo.kt.ui.view.LiveInfoPop;
import com.aiwujie.shengmo.kt.ui.view.LiveOnlineUserPop;
import com.aiwujie.shengmo.kt.ui.view.LivePkInvitedPop;
import com.aiwujie.shengmo.kt.ui.view.LivePkTopAudiencePop;
import com.aiwujie.shengmo.kt.ui.view.LivePlayBackInfoPop;
import com.aiwujie.shengmo.kt.ui.view.NormalMenuPopup;
import com.aiwujie.shengmo.kt.ui.view.NormalSharePop;
import com.aiwujie.shengmo.kt.util.NormalConstant;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.HttpResult;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.tim.utils.Constants;
import com.aiwujie.shengmo.timlive.PhoneStateReceiver;
import com.aiwujie.shengmo.timlive.bean.ScenesRoomInfo;
import com.aiwujie.shengmo.timlive.helper.LiveHttpRequest;
import com.aiwujie.shengmo.timlive.kt.ui.view.LiveLotteryDrawPop;
import com.aiwujie.shengmo.timlive.net.HeartbeatManager;
import com.aiwujie.shengmo.timlive.net.RoomManager;
import com.aiwujie.shengmo.tim.utils.DemoLog;
import com.aiwujie.shengmo.timlive.view.GiftRankingListPop;
import com.aiwujie.shengmo.timlive.view.GiftViewPagerPop;
import com.aiwujie.shengmo.timlive.view.LiveAnchorSettingPop;
import com.aiwujie.shengmo.timlive.view.LiveRedEnvelopesPop;
import com.aiwujie.shengmo.timlive.view.LiveRoomHeadPop;
import com.aiwujie.shengmo.timlive.view.SendLiveRedEnvelopesPop;
import com.aiwujie.shengmo.utils.CropUtils;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.ImageUtils;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.PhotoRemoteUtil;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PhotoUploadUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UriUtil;
import com.aiwujie.shengmo.view.NormalTipsPop;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.audiosettingkit.CircleImageView;
import com.tencent.liteav.custom.Constents;
import com.tencent.liteav.custom.FloatVideoWindowService2;
import com.tencent.liteav.custom.FloatVideoWindowService3;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.qcloud.tim.tuikit.live.bean.AnchorLiveCardBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveRedEnvelopesBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveSettingStateBean;
import com.tencent.qcloud.tim.tuikit.live.bean.PkTopAudienceMessageBean;
import com.tencent.qcloud.tim.tuikit.live.bean.ShareAnchorBean;
import com.tencent.qcloud.tim.tuikit.live.bean.TimCustomMessage;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.component.message.LiveMessageSpan;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.TopToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.TUILiveRoomAnchorLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.AppLiveGiftChangedEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.CustomMessage;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.ReportLiveMessageBean;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.base.TRTCLogger;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.room.impl.GroupListenerConstants;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.screencapture.screen.IRotationCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.screencapture.screen.RotationWatcherBinder;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.screencapture.screen.RotationWatcherService;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.LiveRoomAnchorFragment;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.LiveRoomAudienceFragment;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.LiveRoomPreviewLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.LiveVideoView;
import com.tencent.qcloud.tim.tuikit.live.utils.PermissionUtils;
import com.tencent.qcloud.tim.uikit.base.IBaseMessageSender;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.base.TUIKitListenerManager;
import com.tencent.liteav.model.LiveMessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.ImageUtil;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import pub.devrel.easypermissions.EasyPermissions;
import razerdp.basepopup.BasePopupWindow;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;
import static com.aiwujie.shengmo.timlive.view.LiveRoomHeadPop.TAG_ANCHOR;

public class LiveRoomAnchorActivity extends AppCompatActivity implements TUILiveRoomAnchorLayout.TUILiveRoomAnchorLayoutDelegate, OnItemClickListener, EasyPermissions.PermissionCallbacks, LiveRoomAudienceFragment.OnLiveOperateClickListener {
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
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 152:
//                    if (picFlag == 1) {
//                        String s = (String) msg.obj;
//                        BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
//                        String imgUrl = imgpre + beanicon.getData();
//                        if (liveInfoPop != null) {
//                            liveInfoPop.refreshImageUrl(imgUrl);
//                        }
//                    } else {
//                        String s = (String) msg.obj;
//                        boolean isGson = GsonUtil.isJson(s);
//                        if (isGson) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(s);
//                                if (!jsonObject.isNull("retcode")) {
//                                    String isCode = jsonObject.getString("retcode");
//                                    if (isCode.equals("400")) {
//                                        ToastUtil.show(LiveRoomAnchorActivity.this, jsonObject.getString("msg"));
//                                        return;
//                                    }
//                                    BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
//                                    if (beanicon.getRetcode() == 2000) {
//                                        headurl = beanicon.getData();
//                                        ImageLoader.loadImage(LiveRoomAnchorActivity.this, imgpre + headurl, mEditPersonmsgIcon);
//                                        livePoster = imgpre + headurl;
//                                        if (getLiveAnchorFragment() != null) {
//                                            getLiveAnchorFragment().changeCoverImg(livePoster);
//                                        }
//                                    }
//                                    ToastUtil.show(getApplicationContext(), "上传完成");
//                                    SharedPreferencesUtils.setParam(LiveRoomAnchorActivity.this, Constants.EDIT_LIVE_ICON, imgpre + headurl);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            ToastUtil.show(LiveRoomAnchorActivity.this, s);
//                        }
//                        break;
//                    }
//            }
//            super.handleMessage(msg);
//        }
//    };
    private String headurl;
    private String imgpre;
    private String mCoverUrl;
    private TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo;
    //private String pushAddress;

    public static void start(Context context, String groupID) {
        Intent starter = new Intent(context, LiveRoomAnchorActivity.class);
        if (context instanceof Application) {
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        starter.putExtra(RoomManager.GROUP_ID, groupID);
        context.startActivity(starter);

    }

    PhoneStateReceiver phoneStateReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
        } else {
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //imgpre = SharedPreferencesUtils.geParam(LiveRoomAnchorActivity.this, "image_host", "");
        setContentView(R.layout.test_activity_live_room_anchor);
        //getAuthData();

        imgpre = SharedPreferencesUtils.geParam(this, "image_host", "");
        mGroupID = getIntent().getStringExtra(RoomManager.GROUP_ID);
        // 判断当前的房间类型
        mLayoutTuiLiverRomAnchor = findViewById(R.id.tui_liveroom_anchor_layout);
        onLoadPreview();
        getAnchorCard();
        mLayoutTuiLiverRomAnchor.setLiveRoomAnchorLayoutDelegate(this);
        mLayoutTuiLiverRomAnchor.getLiveRomAnchor().setOnLiveOperateClickListener(this);
        mLayoutTuiLiverRomAnchor.initWithRoomId(getSupportFragmentManager());
        mLayoutTuiLiverRomAnchor.enablePK(!TextUtils.isEmpty(mGroupID));

        EventBus.getDefault().register(this);
        mContext = this;

        phoneStateReceiver = new PhoneStateReceiver(new PhoneStateReceiver.OnPhoneStateListener() {
            @Override
            public void onPhoneCall() {
                LogUtil.d("onPhone Call");
                onAnchorLeave();
            }

            @Override
            public void onPhoneCancel() {
                LogUtil.d("onPhone Cancel");
                onAnchorComeBack();
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(phoneStateReceiver, intentFilter);
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
                        previewLayout.showHistoryCover(LiveRoomAnchorActivity.this, livePoster);
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
            mCoverUrl = SharedPreferencesUtils.geParam(LiveRoomAnchorActivity.this, Constants.EDIT_LIVE_ICON, imgpre + headurl);
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
        int isTicket = mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getLayoutPreview().getIsTicket();
        int ticketBean = mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getLayoutPreview().getTicketBeans();

        if (isTicket == 1 && ticketBean == 0) {
            ToastUtil.show(LiveRoomAnchorActivity.this, "开启门票房前请设置门票价格");
            return;
        }


        int isRecord = mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getLayoutPreview().getRecord();

        String password = mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getLayoutPreview().getPassword();
        String is_pwd = TextUtil.isEmpty(password) ? "0" : "1";

        LiveHttpHelper.getInstance().beginToShow(livePoster, live_title, tid,
                is_interact, String.valueOf(isTicket), String.valueOf(ticketBean),
                String.valueOf(isRecord), is_pwd, password, new HttpListener() {
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
                                        mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getLayoutPreview().startLive(roomId, live_poster);
                                        LiveRoomAnchorFragment mLiveRoomAnchorFragment = mLayoutTuiLiverRomAnchor.getLiveRomAnchor();
                                        Bundle b = new Bundle();
                                        //b.putString("push_address",pushAddress);
                                        if (roomId != null) {
                                            b.putInt("room_id", Integer.valueOf(!TextUtils.isEmpty(roomId) && !"null".equals(roomId) ? roomId : ""));
                                            SharedPreferencesUtils.setParam(LiveRoomAnchorActivity.this, "room_id", roomId);
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
                                    ToastUtil.show(LiveRoomAnchorActivity.this, obj.getString("msg"));
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
                        String roomId = SharedPreferencesUtils.geParam(LiveRoomAnchorActivity.this, "room_id", "");
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
                            ToastUtil.show(LiveRoomAnchorActivity.this, msg);
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
                                if (!mLiveRoomAnchorFragment.isHidden()) {
                                    mLiveRoomAnchorFragment.setArguments(b);
                                    mLiveRoomAnchorFragment.initData();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.live_anchor_container, mLiveRoomAnchorFragment, "tuikit-live-anchor-fragment");
                                }
                            }
                            break;
                        default:
                            ToastUtil.show(LiveRoomAnchorActivity.this, obj.getString("msg"));
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
                    previewLayout.showHistoryCover(LiveRoomAnchorActivity.this, livePoster);
                    previewLayout.showHistoryInteraction(labelBean.getData().getIs_interaction());
                    previewLayout.showHistoryTicket(labelBean.getData().getIs_ticket(), labelBean.getData().getTicketBeans());
                    previewLayout.showInteractTip(labelBean.getData().getInteraction_tips());
                    previewLayout.showTicketPermission(labelBean.getData().getAnchor_status(), labelBean.getData().getTicket_tips());
                    if ("1".equals(labelBean.getData().getIs_record())) {
                        previewLayout.showRecordBtn();
                    }
                    if ("1".equals(labelBean.getData().getIs_apply_pwd())) {
                        previewLayout.showPasswordEdit();
                    }
                }
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
//        mLayoutTuiLiverRomAnchor.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                testNetSpeed();
//            }
//        },2000);
    }

    private void showSelectPic() {
        new AlertView(null, null, "取消", null,
                new String[]{"拍照", "从相册中选择"},
                LiveRoomAnchorActivity.this, AlertView.Style.ActionSheet, LiveRoomAnchorActivity.this).show();
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
        //onRoomDestroy();
        unbindService(mConn);
        stopHeaderReport();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(phoneStateReceiver);
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
            //主播端 会话卡片会覆盖掉首页会话列表的监听 销毁房间后 首页会话列表重新初始化
            EventBus.getDefault().post(new TIMLoginEvent(true));
        }
    }

    @Override
    public void getRoomPKList(final TUILiveRoomAnchorLayout.OnRoomListCallback callback) {
        HttpHelper.getInstance().getPkAnchorList(new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                final SearchUserData pkBean = GsonUtil.GsonToBean(data, SearchUserData.class);
                if (pkBean != null && pkBean.getData() != null) {
                    List<ScenesRoomInfoBean> roomInfoList = pkBean.getData();
                    List<String> roomIdList = new ArrayList<>();
                    for (ScenesRoomInfoBean scenesRoomInfoBean : roomInfoList) {
                        roomIdList.add(String.valueOf(scenesRoomInfoBean.getRoom_id()));
                    }
                    if (callback != null) {
                        callback.onSuccess(roomIdList);
                    }
                }
            }

            @Override
            public void onFail(int code, String msg) {
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
//        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
//        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
//            //第二个参数是被拒绝后再次申请该权限的解释
//            //第三个参数是请求码
//            //第四个参数是要申请的权限
//            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", MY_PERMISSIONS_REQUEST_CALL_PHONE, perms);
//        } else {
//            takePhoto();
//        }

        XXPermissions.with(this)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .permission(Manifest.permission.CAMERA)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            takePhoto();
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        ToastUtil.show("拍照需要以下权限:\n\n1.设备存储权限\n\n2.拍照");
                    }
                });

    }

    //相册
    public void choosePhone(View view) {
//        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
//            //第二个参数是被拒绝后再次申请该权限的解释
//            //第三个参数是请求码
//            //第四个参数是要申请的权限
//            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", MY_PERMISSIONS_REQUEST_CALL_PHONE2, perms);
//        } else {
//            choosePhoto();
//        }


        XXPermissions.with(this)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        choosePhoto();
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        ToastUtil.show("图片选择需要以下权限:\n\n1.访问设备上的照片");
                    }
                });

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
                    //LogUtil.d("ucropuriuri",resultUri);
                    String path = UriUtil.getFilePathByUri(this, resultUri);
                    uploadImage(path);
                   // LogUtil.d("ucropuriuri",path);
//                    HttpHelper.getInstance().uploadImage(path, new HttpCodeMsgListener() {
//                            @Override
//                            public void onSuccess(String data, String msg) {
//                                if (picFlag == 0) {
//                                    BeanIcon beanicon = new Gson().fromJson(data, BeanIcon.class);
//                                    headurl = beanicon.getData();
//                                    ImageLoader.loadImage(LiveRoomAnchorActivity.this, imgpre + headurl, mEditPersonmsgIcon);
//                                    livePoster = imgpre + headurl;
//                                    if (getLiveAnchorFragment() != null) {
//                                        getLiveAnchorFragment().changeCoverImg(livePoster);
//                                    }
//                                }
//
//                            }
//
//                            @Override
//                            public void onFail(int code, String msg) {
//
//                            }
//                        });

                   // uploadImage(resultUri);
//                    ImageUtils.compressImage(path, new CompressCallback() {
//                        @Override
//                        public void doSuc(@NotNull String filePath) {
//
//                        }
//                    });
//                    if (true) {return;}
//
//                    Bitmap photo = PhotoUploadUtils.decodeUriAsBitmap(resultUri, this);
//                    MyApp.resultUri = resultUri;
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    if (photo != null) {
//                        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)压缩文件
//                        switch (picFlag) {
//                            case 0:
//                                //头像
//                                RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), photo);
//                                circleDrawable.getPaint().setAntiAlias(true);
//                                circleDrawable.setCircular(true);
//                                mEditPersonmsgIcon.setImageDrawable(circleDrawable); //把图片显示在ImageView控件上
//                                break;
//                            case 10:
//                                //相册
//
//                                break;
//
//                        }
//                        ToastUtil.show(getApplicationContext(), "图片上传中,请稍后提交...");
//                    }
//                    if (picFlag == 0) {
//                        // 获得字节流
//                        ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
//                        PhotoUploadTask put = new PhotoUploadTask(
//                                NetPic() + "Api/Api/fileUpload"  //"http://59.110.28.150:888/Api/Api/fileUpload"
//                                , is,
//                                this, handler);
//                        put.start();
//                    } else {
//                        // 获得字节流
//                        ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
//                        PhotoUploadTask put = new PhotoUploadTask(
//                                NetPic() + "Api/Api/filePhoto"//  "http://59.110.28.150:888/Api/Api/filePhoto"
//                                , is,
//                                this, handler);
//                        put.start();
//
//                    }
                } else {
                    return;
                }
                break;
            case 10000:
                if (data != null) {
                    ToastUtil.show(LiveRoomAnchorActivity.this, "图片上传中");
                    //获取选择器返回的数据
                    ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                    for (int i = 0; i < images.size(); i++) {
                        LogUtil.d(images.get(i));
                        picFlag = 1;
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
        GiftViewPagerPop pop = new GiftViewPagerPop(LiveRoomAnchorActivity.this, R.style.BottomFullDialog, anchor_id);
        pop.show();
        pop.setOnPopListener(new GiftViewPagerPop.OnPopListener() {
            @Override
            public void doItemClick(String uid) {
                if (getLiveAnchorFragment() != null) {
                    getLiveAnchorFragment().showAudienceCard(uid);
                }
            }
        });
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
        final LiveRoomHeadPop onClickAudiencePop = new LiveRoomHeadPop(LiveRoomAnchorActivity.this, tb, TAG_ANCHOR);
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
        GiftRankingListPop pop = new GiftRankingListPop(LiveRoomAnchorActivity.this, R.style.BottomFullDialog, anchor_id);
        pop.show();
    }

    @Override
    public void clickLikeFrequencyControl(final String anchor_id) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.show(LiveRoomAnchorActivity.this, anchor_id + "===");
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
            normalTipsPop = new NormalTipsPop.Builder(LiveRoomAnchorActivity.this)
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
                final LiveRoomHeadPop headPop = new LiveRoomHeadPop(LiveRoomAnchorActivity.this, dataBean, TAG_ANCHOR);
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
                Intent intent = new Intent(LiveRoomAnchorActivity.this, LiveRankingActivity.class);
                startActivity(intent);
                break;
            case LiveEventConstant.SHOW_CONVERSATION:
                showConversationPop();
                break;
            case LiveEventConstant.SHOW_LIVE_PREVIEW_SETTING:
                showLiveAnchorPreViewSettingPop(event.getData());
                break;
            case "OnGiftPanel":
                Intent intent1 = new Intent(LiveRoomAnchorActivity.this, MyPurseActivity.class);
                startActivity(intent1);
                break;
            case LiveEventConstant.TEST_NET_SPEED:
                // testNetSpeed();
                break;
            case LiveEventConstant.SHOW_PK_INVITED_POP:
                showPkInvitePop(event.getData());
                break;
            case LiveEventConstant.START_PK:
                startPk(event.getData());
                break;
            case LiveEventConstant.COMPLETE_PK:
                completePk(event.getData());
                break;
            case LiveEventConstant.STOP_PK:
                stopPk(event.getData());
                break;
            case LiveEventConstant.SHOW_PK_TOP_AUDIENCE:
                showPkTopAudienceTop(event.getData());
                break;
            case LiveEventConstant.SHOW_MY_LIVE_LEVEL_PAGE:
                gotoLevelPage(event.getData());
                break;
            case LiveEventConstant.SET_PLAY_BACK_TICKET:
                showSetTickPop();
                break;
            case LiveEventConstant.SHOW_SEND_LIVE_RED_ENVELOPES:
                showSendRedEnvelopesPop(event.getData());
                break;
            case LiveEventConstant.SHOW_LIVE_RED_ENVELOPES:
//                int index = 0;
//                if (!TextUtil.isEmpty(event.getTag())) {
//                    index = Integer.parseInt(event.getTag());
//                }
//                showRedEnvelopesPop(event.getData(),index,false);
                if (!TextUtil.isEmpty(event.getTag())) {
                    LiveRedEnvelopesBean.DataBean dataBean1 = GsonUtil.GsonToBean(event.getTag(), LiveRedEnvelopesBean.DataBean.class);
                    showRedEnvelopesPop(event.getData(), dataBean1, false);
                }
                break;
            case LiveEventConstant.SHOW_LIVE_RED_ENVELOPES_2:
//                int index2 = 0;
//                if (!TextUtil.isEmpty(event.getTag())) {
//                    index2 = Integer.parseInt(event.getTag());
//                }
//                showRedEnvelopesPop(event.getData(),index2,true);
                if (!TextUtil.isEmpty(event.getTag())) {
                    LiveRedEnvelopesBean.DataBean dataBean2 = GsonUtil.GsonToBean(event.getTag(), LiveRedEnvelopesBean.DataBean.class);
                    showRedEnvelopesPop(event.getData(), dataBean2, true);
                }
                break;
            case LiveEventConstant.GET_LIVE_RED_ENVELOPES_LIST:
                getLiveRedBagList(event.getData());
                break;
            case LiveEventConstant.SHOW_LIVE_LOTTERY_DRAW:
                showLotteryDrawLayout();
                break;
            case LiveEventConstant.START_SCREEN_CAPTURE:
                reportScreenCaptureLive(true);
                break;
            case LiveEventConstant.STOP_SCREEN_CAPTURE:
                reportScreenCaptureLive(false);
                break;
            case LiveEventConstant.SHOW_LIVE_BLIND_RULE:
                LiveBlindBoxRulePop pop = new LiveBlindBoxRulePop(this);
                pop.show();
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
                Intent intent = new Intent(LiveRoomAnchorActivity.this, ReportActivity.class);
                intent.putExtra("source_type", 1);
                intent.putExtra("uid", mySelfId);
                LiveRoomAnchorActivity.this.startActivity(intent);
            }
        });
    }

    void showLiveUserPop(String roomId) {
        LiveOnlineUserPop liveOnlineUserPop = new LiveOnlineUserPop(LiveRoomAnchorActivity.this, MyApp.uid, 2);
        liveOnlineUserPop.showPopupWindow();
        liveOnlineUserPop.setOnLiveUserListener(new LiveOnlineUserPop.OnLiveUserListener() {
            @Override
            public void onLiveUserKitOut(@NotNull TimCustomMessage tcm) {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setMessage("当前用户正在连麦/发言，是否关闭ta的连麦/发言？")
//                        .setPositiveButton("否", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).setNegativeButton("是", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
////                        HttpHelper.getInstance().kickOutUser(tcm.getUid(), new HttpCodeListener() {
////                            @Override
////                            public void onSuccess(String data) {
////                                liveOnlineUserPop.refreshData();
////                            }
////
////                            @Override
////                            public void onFail(int code, String msg) {
////                                ToastUtil.show(mContext,msg);
////                            }
////                        });
//                    }
//                }).create().show();
            }

            @Override
            public void onLiveUserLink(@NotNull TimCustomMessage tcm) {
                if (mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().inviteLinkUser(tcm.getUid());
                }
            }

            @Override
            public void onLiveUserClick(@NotNull TimCustomMessage tcm) {
                final LiveRoomHeadPop headPop = new LiveRoomHeadPop(LiveRoomAnchorActivity.this, tcm, TAG_ANCHOR);
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
            FloatVideoWindowService3.MyBinder binder = (FloatVideoWindowService3.MyBinder) service;
            binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void startFloat() {
        if (!PermissionUtils.requestFloatPermission(this, 24)) {
            ToastUtil.show(LiveRoomAnchorActivity.this, "最小化需要开启悬浮窗权限");
            return;
        }
        isStartService = true;
        //Constents.mAnchorTextureView = mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getVideoView().getGLSurfaceView();
        //Constents.mAnchorViewView = mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getVideoView();
        Intent intent = new Intent(this, FloatVideoWindowService3.class);//开启服务显示悬浮框
        bindService(intent, mVideoServiceConnection, Context.BIND_AUTO_CREATE);
        mLayoutTuiLiverRomAnchor.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveTaskToBack(true);
            }
        }, 1000);
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

        NormalShareBean normalShareBean = new NormalShareBean(4, uid,
                "直播推荐",
                nickname,
                nickname + "正在精彩直播中，快来圣魔观看吧~",
                HttpUrl.SMaddress,
                pic);
        NormalSharePop normalSharePop = new NormalSharePop(LiveRoomAnchorActivity.this, normalShareBean, true);
        normalSharePop.showPopupWindow();

        // SharedPop sharedPop = new SharedPop(this, HttpUrl.NetPic() + HttpUrl.ShareUserDetail + uid, "正在直播", nickname + "正在圣魔精彩直播中", pic, 0, 3, "", "", "", uid);
//        SharedPop sharedPop = new SharedPop(this, uid, nickname, pic);
//        sharedPop.showAtLocation(mLayoutTuiLiverRomAnchor, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        final WindowManager.LayoutParams[] params = {getWindow().getAttributes()};
//        //当弹出Popupwindow时，背景变半透明
//        params[0].alpha = 0.7f;
//        getWindow().setAttributes(params[0]);
//        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
//        sharedPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                params[0] = getWindow().getAttributes();
//                params[0].alpha = 1f;
//                getWindow().setAttributes(params[0]);
//            }
//        });
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
        liveInfoPop.setPopListener(new LiveInfoPop.OnLiveInfoPopListener() {
            @Override
            public void doLiveInfoRefresh(@NotNull String title, @NotNull String cover) {
                if (getLiveAnchorFragment() != null) {
                    getLiveAnchorFragment().refreshLiveInfo(title, cover);
                }
            }
        });
    }

    void showChoosePhoto() {
        final List<NormalMenuItem> normalMenuItemList = new ArrayList<>();
        normalMenuItemList.add(new NormalMenuItem(0, "从相册选择"));
        final NormalMenuPopup menuPopup = new NormalMenuPopup(LiveRoomAnchorActivity.this, normalMenuItemList);
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
                .start(LiveRoomAnchorActivity.this, 10000);
    }

    void uploadImage(String path) {
//        picFlag = 1;
//        Bitmap loadbitmap = BitmapFactory.decodeFile(path, getBitmapOption(2));
//        Bitmap rotaBitmap = PhotoRemoteUtil.rotaingImageView(PhotoRemoteUtil.getBitmapDegree(path), loadbitmap);
//        ByteArrayInputStream is = new ByteArrayInputStream(Bitmap2Bytes(rotaBitmap));
//        PhotoUploadTask put = new PhotoUploadTask(
//                NetPic() + "Api/Api/filePhoto"//  "http://59.110.28.150:888/Api/Api/filePhoto"
//                , is,
//                this, handler);
//        put.start();
        HttpHelper.getInstance().uploadImage(path, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                if (picFlag == 0) {
                    BeanIcon beanicon = new Gson().fromJson(data, BeanIcon.class);
                    headurl = beanicon.getData();
                    ImageLoader.loadImage(LiveRoomAnchorActivity.this, imgpre + headurl, mEditPersonmsgIcon);
                    livePoster = imgpre + headurl;
                    if (getLiveAnchorFragment() != null) {
                        getLiveAnchorFragment().changeCoverImg(livePoster);
                    }
                } else if (picFlag == 1) {
                    BeanIcon beanicon = new Gson().fromJson(data, BeanIcon.class);
                    String imgUrl = imgpre + beanicon.getData();
                    if (liveInfoPop != null) {
                        liveInfoPop.refreshImageUrl(imgUrl);
                    }
                }

            }

            @Override
            public void onFail(int code, String msg) {

            }
        });


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
        }, 100, 30000);
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


//    /**
//     * 判断主播是否开启了悬浮窗权限
//     * 如果开启了 直接跳转页面 并且开启悬浮窗
//     * 未开启 显示开启提示框 不跳转页面
//     *
//     * @param //intent
//     */
//    @Override
//    public void startActivity(Intent intent) {
//        if (intent.getData() == null || !intent.getData().toString().contains("package")) {
//            if (intent.getExtras() != null) {
//                if (intent.getExtras().containsKey("_mmessage_appPackage")) { //微信分享
//                    super.startActivity(intent);
//                }
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //6.0版本
//                if (!Settings.canDrawOverlays(LiveRoomAnchorActivity.this)) {
//                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().showFloatPermissionDialog("主播最小化窗口，需要开启悬浮窗权限，是否开启？");
//                } else {
//                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().onBackPressed();
//                    super.startActivity(intent);
//                }
//            } else {
//                super.startActivity(intent);
//            }
//
//        } else {
//            super.startActivity(intent);
//        }
//    }

    void reportBeautyPermission() {
        HttpHelper.getInstance().reportBeautyPermission(new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(LiveRoomAnchorActivity.this, msg);
            }
        });
    }

    boolean isActive;

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d("页面 onStop");
//        if (!isAppOnForeground()) {
//            isActive = false;
//            onAnchorLeave();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isActive) {
            onAnchorComeBack();
        }
    }

    //最小化窗口时 退出app 结束直播
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void StopLive(StopLiveEvent event) {
        if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
            mLayoutTuiLiverRomAnchor.getLiveRomAnchor().destroyRoom();
            mLayoutTuiLiverRomAnchor.getLiveRomAnchor().finishRoom();
        }
    }

    public LiveRoomAnchorFragment getLiveAnchorFragment() {
        if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
            return mLayoutTuiLiverRomAnchor.getLiveRomAnchor();
        } else {
            return null;
        }
    }

    void showLiveAnchorSettingPop(String liveSettingStateJson) {
        LiveSettingStateBean liveSettingStateBean = GsonUtil.GsonToBean(liveSettingStateJson, LiveSettingStateBean.class);
        LiveAnchorSettingPop liveAnchorSettingPop = new LiveAnchorSettingPop(LiveRoomAnchorActivity.this, liveSettingStateBean);
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
                if (getLiveAnchorFragment() != null) {
                    getLiveAnchorFragment().doLiveManagerSpeak();
                }
            }
        });
    }

    public void showLiveAnchorPreViewSettingPop(String liveSettingStateJson) {
        LiveSettingStateBean liveSettingStateBean = GsonUtil.GsonToBean(liveSettingStateJson, LiveSettingStateBean.class);
        LiveAnchorSettingPop liveAnchorSettingPop = new LiveAnchorSettingPop(LiveRoomAnchorActivity.this, liveSettingStateBean, true);
        liveAnchorSettingPop.showPopupWindow();
        liveAnchorSettingPop.setOnLiveSettingListener(new LiveAnchorSettingPop.OnLiveSettingListener() {
            @Override
            public void onChooseCameraFront() {
                if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().doPreViewCamera(true, true);
                    updateCameraState(true);
                }
            }

            @Override
            public void onChooseCameraBack() {
                if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().doPreViewCamera(true, false);
                    updateCameraState(true);
                }
            }

            @Override
            public void onChooseCameraClose() {
                if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().changeCoverImg(livePoster);
                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().doPreViewCamera(false, false);
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

            }

            @Override
            public void onChooseBeauty() {

            }

            @Override
            public void onChooseManagerSpeak() {

            }
        });
    }

    void updateCameraState(boolean isOpen) {
        HttpHelper.getInstance().updateAnchorCameraState(isOpen);
    }


    public void closeLiveRoom(String roomId) {
        HttpHelper.getInstance().closeLive(roomId);
    }

    void showConversationPop() {
        ConversationPop conversationPop = new ConversationPop(LiveRoomAnchorActivity.this);
        conversationPop.showPopupWindow();
    }


//    public void testNetSpeed() {
//        TRTCCloudDef.TRTCSpeedTestParams speedTestParams = new TRTCCloudDef.TRTCSpeedTestParams();
//        speedTestParams.sdkAppId = MyApp.TEST_SDK_APP_ID;
//        speedTestParams.userId = MyApp.uid;
//        speedTestParams.userSig = MyApp.token;
//        TRTCCloud.sharedInstance(LiveRoomAnchorActivity.this).startSpeedTest(speedTestParams);
//    }

    public void showPkInvitePop(String uid) {
        LivePkInvitedPop livePkInvitedPop = new LivePkInvitedPop(LiveRoomAnchorActivity.this, uid);
        livePkInvitedPop.showPopupWindow();
        livePkInvitedPop.setPkListener(new LivePkInvitedPop.OnPkInviteListener() {
            @Override
            public void doPkRefuse() {
                if (getLiveAnchorFragment() != null) {
                    getLiveAnchorFragment().doPkRefuse();
                }
            }

            @Override
            public void doPkAccept() {
                if (getLiveAnchorFragment() != null) {
                    getLiveAnchorFragment().doPkAccept(uid);
                }
            }

            @Override
            public void doAutoPkRefuse(boolean autoRefuse) {
                if (getLiveAnchorFragment() != null) {
                    getLiveAnchorFragment().doPkAutoRefuse(autoRefuse);
                }
            }
        });
    }

    void startPk(final String uid) {
        HttpHelper.getInstance().startPk(uid, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, final String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(LiveRoomAnchorActivity.this, msg);
                    }
                });
                if (getLiveAnchorFragment() != null) {
                    getLiveAnchorFragment().reportStopPkByQuit(uid);
                }
            }
        });
    }

    void completePk(String uid) {
        HttpHelper.getInstance().completePk(uid, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    void stopPk(String uid) {
        HttpHelper.getInstance().stopPk(uid, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    void showPkTopAudienceTop(String topData) {
        PkTopAudienceMessageBean pkBean = GsonUtil.GsonToBean(topData, PkTopAudienceMessageBean.class);
        LivePkTopAudiencePop livePkTopAudiencePop = new LivePkTopAudiencePop(LiveRoomAnchorActivity.this, pkBean.getAnchorId(), pkBean.getOtherId(), pkBean.isUs());
        livePkTopAudiencePop.showPopupWindow();
    }

    void gotoLevelPage(String type) {
        Intent intent = new Intent(LiveRoomAnchorActivity.this, MyLiveLevelActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    void showSetTickPop() {
        LivePlayBackInfoPop livePlayBackInfoPop = new LivePlayBackInfoPop(LiveRoomAnchorActivity.this, "", false, 0);
        livePlayBackInfoPop.showPopupWindow();
        livePlayBackInfoPop.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                finish();
            }
        });
    }

    void showSendRedEnvelopesPop(String roomId) {
        SendLiveRedEnvelopesPop sendLiveRedEnvelopesPop = new SendLiveRedEnvelopesPop(LiveRoomAnchorActivity.this, roomId);
        sendLiveRedEnvelopesPop.showPopupWindow();
    }

    void showRedEnvelopesPop(String roomId, int index, boolean isReceive) {
        //LiveRedEnvelopesPop redEnvelopesDialog = new LiveRedEnvelopesPop(LiveRoomSwitchActivity.this);
        //redEnvelopesDialog.showPopupWindow();
        //getLiveRedBagList(roomId);
        LiveRedEnvelopesPop redEnvelopesPop = new LiveRedEnvelopesPop(LiveRoomAnchorActivity.this, redEnvelopesList.get(index), isReceive);
        redEnvelopesPop.showPopupWindow();
        redEnvelopesPop.setOnLiveRedEnvelopes(new LiveRedEnvelopesPop.OnLiveRedEnvelopesListener() {
            @Override
            public void doLiveUserClick(String uid) {
                if (getLiveAnchorFragment() != null) {
                    getLiveAnchorFragment().showAudienceCard(uid);
                }
            }

            @Override
            public void refreshRedEnvelopes() {
                getLiveRedBagList(roomId);
            }
        });
    }


    void showRedEnvelopesPop(String roomId, LiveRedEnvelopesBean.DataBean redEnvelopesBean, boolean isReceive) {
        LiveRedEnvelopesPop redEnvelopesPop = new LiveRedEnvelopesPop(LiveRoomAnchorActivity.this, redEnvelopesBean, isReceive);
        redEnvelopesPop.showPopupWindow();
        redEnvelopesPop.setOnLiveRedEnvelopes(new LiveRedEnvelopesPop.OnLiveRedEnvelopesListener() {
            @Override
            public void doLiveUserClick(String uid) {
                if (getLiveAnchorFragment() != null) {
                    getLiveAnchorFragment().showAudienceCard(uid);
                }
            }

            @Override
            public void refreshRedEnvelopes() {
                //getLiveRedBagList(roomId);
                getLiveAnchorFragment().getRedBagList();
            }
        });
    }

    LinkedList<LiveRedEnvelopesBean.DataBean> redEnvelopesList;

    void getLiveRedBagList(String roomId) {
        if (redEnvelopesList == null) {
            redEnvelopesList = new LinkedList<>();
        }
        HttpHelper.getInstance().getLiveRedBagList(roomId, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                LiveRedEnvelopesBean liveRedEnvelopesBean = GsonUtil.GsonToBean(data, LiveRedEnvelopesBean.class);
                if (liveRedEnvelopesBean != null) {
                    List<LiveRedEnvelopesBean.DataBean> tempList = liveRedEnvelopesBean.getData();
                    redEnvelopesList.clear();
                    redEnvelopesList.addAll(tempList);
                    refreshLiveRedEnvelop();
                }
            }

            @Override
            public void onFail(int code, String msg) {
                if (getLiveAnchorFragment() != null) {
                    refreshLiveRedEnvelop();
                }
            }
        });
    }

    public void refreshLiveRedEnvelop() {
        if (redEnvelopesList == null) {
            showOrHideRedEnvelopesView(false);
            return;
        }
        if (redEnvelopesList.isEmpty()) {
            showOrHideRedEnvelopesView(false);
        } else {
            showOrHideRedEnvelopesView(true);
        }
    }

    public void showOrHideRedEnvelopesView(boolean isShow) {
        if (getLiveAnchorFragment() != null) {
            if (isShow) {
                getLiveAnchorFragment().showLiveRedEnvelopesSign(redEnvelopesList.getFirst().getHead_pic(), redEnvelopesList.size());
            } else {
                getLiveAnchorFragment().hideLiveRedEnvelopesSign();
            }
        }
    }


    /**
     * APP是否处于前台唤醒状态
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    //主播离开
    void onAnchorLeave() {
        if (getLiveAnchorFragment() != null) {
            getLiveAnchorFragment().doAnchorLeave();
        }
    }

    //主播回来
    void onAnchorComeBack() {
        if (getLiveAnchorFragment() != null) {
            getLiveAnchorFragment().doAnchorComeBack();
        }
    }

    public void showLotteryDrawLayout() {
        LiveLotteryDrawPop liveLotteryDrawPop = new LiveLotteryDrawPop(this);
        liveLotteryDrawPop.showPopupWindow();
    }

    public void requestRecordScreen() {
        MediaProjectionManager mMediaProjectionManager = (MediaProjectionManager) this.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), NormalConstant.REQUEST_CODE_1);
    }

    public void reportScreenCaptureLive(boolean isOpen) {
        HttpHelper.getInstance().reportScreenCapture(isOpen);
    }

    public void uploadImage(Uri uri) {
        String path = UriUtil.getRealPathFromURI(this, uri);
        uploadImage(path);
    }


    private RotationWatcherBinder mBinder;
    private RotationWatcherServiceConn mConn;
    private TRTCCloud mTRTCCloud;
    private class RotationWatcherServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("RotationWatcherService", "onServiceConnected: 服务已绑定");
            mBinder = (RotationWatcherBinder) service;

            mBinder.getService().setCallback(new IRotationCallback() {
                @Override
                public void onRotationChanged(int rotation) {
                    LogUtil.d("屏幕方向变动 " + rotation);
//                    switch (rotation) {
//                        case 0:
//                            mTRTCCloud.setVideoEncoderRotation(0);
//                            break;
//                        case 1:
//                            mTRTCCloud.setVideoEncoderRotation(90);
//                            break;
//                        case 3:
//                            mTRTCCloud.setVideoEncoderRotation(270);
//                            break;
//                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("RotationWatcherService", "onServiceDisconnected: 服务绑定失败");
            mBinder = null;
            unbindService(mConn);
        }
    }

    public void bindService() {
        mConn = new RotationWatcherServiceConn();
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        bindService(new Intent(this, RotationWatcherService.class), mConn, Context.BIND_AUTO_CREATE);
    }

}


