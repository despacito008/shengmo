package com.aiwujie.shengmo.qnlive.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.constraint.Guideline;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.aiwujie.shengmo.activity.MyPurseActivity;
import com.aiwujie.shengmo.activity.ReportActivity;
import com.aiwujie.shengmo.activity.ranking.LiveRankingActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.bean.BeginToShowBean;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.bean.SearchUserData;
import com.aiwujie.shengmo.customview.SharedPop;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.bean.NormalMenuItem;
import com.aiwujie.shengmo.kt.bean.NormalShareBean;
import com.aiwujie.shengmo.kt.listener.OnSimplePopListener;
import com.aiwujie.shengmo.kt.ui.activity.MyLiveLevelActivity;
import com.aiwujie.shengmo.kt.ui.view.LiveGiftRankPop;
import com.aiwujie.shengmo.kt.ui.view.LiveInfoPop;
import com.aiwujie.shengmo.kt.ui.view.LiveOnlineUserPop;
import com.aiwujie.shengmo.kt.ui.view.LivePkInvitedPop;
import com.aiwujie.shengmo.kt.ui.view.LivePkTopAudiencePop;
import com.aiwujie.shengmo.kt.ui.view.LivePlayBackInfoPop;
import com.aiwujie.shengmo.kt.ui.view.NormalMenuPopup;
import com.aiwujie.shengmo.kt.ui.view.NormalSharePop;
import com.aiwujie.shengmo.kt.ui.view.QNConversationPop;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.qnlive.adapter.QNRemoteViewAdapter;
import com.aiwujie.shengmo.qnlive.bean.CloudCustomDataBean;
import com.aiwujie.shengmo.qnlive.ui.LinkRequestPop;
import com.aiwujie.shengmo.qnlive.ui.QNPkViewLayout;
import com.aiwujie.shengmo.qnlive.utils.Config;
import com.aiwujie.shengmo.qnlive.utils.LiveMessageHelper;
import com.aiwujie.shengmo.qnlive.utils.LiveRoomMessageType;
import com.aiwujie.shengmo.qnlive.utils.TimLiveRoomHelper;
import com.aiwujie.shengmo.tim.utils.Constants;
import com.aiwujie.shengmo.timlive.helper.LiveHttpRequest;
import com.aiwujie.shengmo.timlive.kt.ui.view.LiveLotteryDrawPop;
import com.aiwujie.shengmo.timlive.ui.LiveRoomSwitchActivity;
import com.aiwujie.shengmo.timlive.view.GiftRankingListPop;
import com.aiwujie.shengmo.timlive.view.GiftViewPagerPop;
import com.aiwujie.shengmo.timlive.view.LiveAnchorSettingPop;
import com.aiwujie.shengmo.timlive.view.LiveRedEnvelopesPop;
import com.aiwujie.shengmo.timlive.view.LiveRoomHeadPop;
import com.aiwujie.shengmo.timlive.view.SendLiveRedEnvelopesPop;
import com.aiwujie.shengmo.utils.CropUtils;
import com.aiwujie.shengmo.utils.DensityUtil;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PhotoUploadUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.NormalTipsPop;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.qiniu.droid.rtc.QNCameraSwitchResultCallback;
import com.qiniu.droid.rtc.QNCaptureVideoCallback;
import com.qiniu.droid.rtc.QNClientRole;
import com.qiniu.droid.rtc.QNCustomMessage;
import com.qiniu.droid.rtc.QNLocalAudioPacketCallback;
import com.qiniu.droid.rtc.QNMediaRelayState;
import com.qiniu.droid.rtc.QNRTCEngine;
import com.qiniu.droid.rtc.QNRTCEngineEventListener;
import com.qiniu.droid.rtc.QNRTCSetting;
import com.qiniu.droid.rtc.QNRTCUser;
import com.qiniu.droid.rtc.QNRoomState;
import com.qiniu.droid.rtc.QNSourceType;
import com.qiniu.droid.rtc.QNStatisticsReport;
import com.qiniu.droid.rtc.QNSurfaceView;
import com.qiniu.droid.rtc.QNTrackInfo;
import com.qiniu.droid.rtc.QNVideoFormat;
import com.qiniu.droid.rtc.model.QNAudioDevice;
import com.qiniu.droid.rtc.model.QNForwardJob;
import com.qiniu.droid.rtc.model.QNImage;
import com.qiniu.droid.rtc.model.QNMergeJob;
import com.qiniu.droid.rtc.model.QNMergeTrackOption;
import com.qiniu.droid.rtc.model.QNStretchMode;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.bean.AnchorLiveCardBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveAnnouncementBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveChangeInfoChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveCreateAvChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveJoinAvChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveMessageBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveMessageCustomDataBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveRedEnvelopesBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveRoomInfo;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveSettingStateBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveWatchAvChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.PkChatRoomBean;
import com.tencent.qcloud.tim.tuikit.live.bean.ShareAnchorBean;
import com.tencent.qcloud.tim.tuikit.live.bean.TimCustomMessage;
import com.tencent.qcloud.tim.tuikit.live.component.bottombar.BottomToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.component.common.CircleImageView;
import com.tencent.qcloud.tim.tuikit.live.component.common.SelectMemberView;
import com.tencent.qcloud.tim.tuikit.live.component.countdown.CountDownTimerView;
import com.tencent.qcloud.tim.tuikit.live.component.countdown.ICountDownTimerView;
import com.tencent.qcloud.tim.tuikit.live.component.danmaku.DanmakuManager;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.DefaultGiftAdapterImp;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftAnimatorLayout;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfoDataHandler;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftPanelViewImp;
import com.tencent.qcloud.tim.tuikit.live.component.input.InputTextMsgDialog;
import com.tencent.qcloud.tim.tuikit.live.component.like.HeartLayout;
import com.tencent.qcloud.tim.tuikit.live.component.manager.LiveManagerLayout;
import com.tencent.qcloud.tim.tuikit.live.component.manager.LiveManagerTipPanel;
import com.tencent.qcloud.tim.tuikit.live.component.message.ChatEntity;
import com.tencent.qcloud.tim.tuikit.live.component.message.ChatLayout;
import com.tencent.qcloud.tim.tuikit.live.component.other.LiveNormalTipPanel;
import com.tencent.qcloud.tim.tuikit.live.component.pk.PkStopTipPanel;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.TopToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.CustomMessage;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.GiftMessage;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.room.impl.IMProtocol;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.QNLiveRoomPreviewLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.ExitConfirmDialog;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.FinishDetailDialog;
import com.tencent.qcloud.tim.tuikit.live.utils.IMUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;
import com.yalantis.ucrop.UCrop;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.Size;
import org.webrtc.VideoFrame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Semaphore;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiRotation;
import cn.tillusory.tiui.TiPanelLayout;
import master.flame.danmaku.ui.widget.DanmakuView;
import pub.devrel.easypermissions.EasyPermissions;
import razerdp.basepopup.BasePopupWindow;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;
import static com.aiwujie.shengmo.timlive.view.LiveRoomHeadPop.TAG_ANCHOR;
import static com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.FinishDetailDialog.ANCHOR_LOCAL_BEANS;
import static com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.FinishDetailDialog.LIVE_TOTAL_TIME;
import static com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.FinishDetailDialog.TOTAL_AUDIENCE_COUNT;
import static com.tencent.qcloud.tim.tuikit.live.utils.IMUtils.handleCustomTextMsg;
import static java.security.AccessController.getContext;

public class QNLiveRoomAnchorActivity extends AppCompatActivity implements QNRTCEngineEventListener, EasyPermissions.PermissionCallbacks {
    @BindView(R.id.live_room_local_surface_view)
    QNSurfaceView mLocalVideoSurfaceView;
    @BindView(R.id.layout_preview)
    QNLiveRoomPreviewLayout mLayoutPreview;
    @BindView(R.id.countdown_timer_view)
    CountDownTimerView countdownTimerView;
    @BindView(R.id.layout_top_toolbar)
    TopToolBarLayout mLayoutTopToolBar;
    @BindView(R.id.view_danmaku)
    DanmakuView viewDanmaku;
    @BindView(R.id.layout_bottom_toolbar)
    BottomToolBarLayout mLayoutBottomToolBar;
    @BindView(R.id.group_live)
    Group groupLive;
    @BindView(R.id.layout_chat)
    ChatLayout mLayoutChatMessage;
    @BindView(R.id.heart_layout)
    HeartLayout heartLayout;
    @BindView(R.id.lottie_animator_layout)
    GiftAnimatorLayout mGiftAnimatorLayout;
    @BindView(R.id.ti_panel)
    TiPanelLayout tiPanel;
    @BindView(R.id.rv_remote_view)
    RecyclerView rvRemoteView;
    @BindView(R.id.tv_live_room_link_request)
    TextView tvLiveRoomLinkRequest;
    @BindView(R.id.iv_test)
    ImageView ivTest;
    @BindView(R.id.layout_live_manager)
    LiveManagerLayout layoutLiveManager;
    @BindView(R.id.view_pk_other_window)
    QNSurfaceView viewPkOtherWindow;
    @BindView(R.id.guide_line_center_line)
    Guideline guideLineCenterLine;
    @BindView(R.id.layout_pk_view)
    QNPkViewLayout layoutPkView;

    private SelectMemberView mSelectMemberView;
    private Semaphore captureStoppedSem = new Semaphore(1);
    private boolean mStreamingStopped;

    private List<QNTrackInfo> mLocalTrackList = null;
    private List<QNTrackInfo> mRemoteTrackList = null;
    QNRTCEngine mEngine;
    private QNTrackInfo mLocalVideoTrack;
    private QNTrackInfo mLocalAudioTrack;

    //private QNSurfaceView mLocalVideoSurfaceView;
    private QNSurfaceView mRemoteVideoSurfaceView;
    private DanmakuManager mDanmakuManager;          // ??????????????????

    private volatile boolean mIsMicrophoneOn = true;
    private volatile boolean mIsSpeakerOn = true;

    private String mRoomId;
    private String mRoomToken;

    private QNRoomState mCurrentRoomState = QNRoomState.IDLE;

    private long mLocalBeans = 0;   // ????????????
    private long mStartTime = 0;   // ????????????????????????
    private int mTotalMemberCount;

    private QNCaptureVideoCallback mCaptureVideoCallback = new QNCaptureVideoCallback() {
        @Override
        public int[] onCaptureOpened(List<Size> list, List<Integer> list1) {
            // ?????????????????????????????????????????????
            int wantSize = -1; // ????????????????????????, -1 ??????????????????, ?????? QNRTCSetting ?????????
            int wantFps = -1;  // ?????????????????????, -1 ??????????????????, ?????? QNRTCSetting ?????????
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).height == Config.STREAMING_HEIGHT) {
                    wantSize = i;
                    if (list.get(i).width == Config.STREAMING_WIDTH) {
                        break;
                    }
                }
            }
            LogUtil.d(wantSize + "---" + wantFps);
            return new int[]{wantSize, wantFps};
        }

        @Override
        public void onCaptureStarted() {
        }

        @Override
        public void onRenderingFrame(VideoFrame.TextureBuffer textureBuffer, long timestampNs) {
            // LogUtil.d("idz = " + textureBuffer.getTextureId());
            int tex = TiSDKManager.getInstance().renderOESTexture(
                    textureBuffer.getTextureId(),
                    textureBuffer.getWidth(),
                    textureBuffer.getHeight(),
                    isFrontCamera ? TiRotation.CLOCKWISE_ROTATION_270 : TiRotation.CLOCKWISE_ROTATION_90,
                    isFrontCamera);
            // LogUtil.d("id = " + tex);
            textureBuffer.setType(VideoFrame.TextureBuffer.Type.RGB);
            textureBuffer.setTextureId(tex);
        }

        @Override
        public void onPreviewFrame(byte[] data, int width, int height, int rotation, int fmt, long timestampNs) {
        }

        @Override
        public void onCaptureStopped() {
            // captureStoppedSem.drainPermits();
            //  captureStoppedSem.release();
            TiSDKManager.getInstance().destroy();
        }
    };
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_qn_live_room_anchor);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mContext = QNLiveRoomAnchorActivity.this;
        mDanmakuManager = new DanmakuManager(mContext);
        mDanmakuManager.setDanmakuView(viewDanmaku);
        mergeMarginBottom = DensityUtil.dip2px(mContext, 30);
        mergeMarginBetween = DensityUtil.dip2px(mContext, 10);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        imgpre = SharedPreferencesUtils.geParam(this, "image_host", "");
        initPreViewLayout();
        initBeautyPanel();
        initQNRTCEngine();
        initLocalTrackInfoList();
        initLiveManagerLayout();
        getAnchorCard();
        initSelectPkView();
        initPkViewLayout();
    }

    private int mCaptureMode = Config.CAMERA_CAPTURE;

    @Override
    protected void onResume() {
        super.onResume();
//        // ??????????????????
//        if (mCaptureMode == Config.CAMERA_CAPTURE || mCaptureMode == Config.MUTI_TRACK_CAPTURE) {
//            startCaptureAfterAcquire();
//        }
        //    startCaptureAfterAcquire();

        if (!isActive) {
            onAnchorComeBack();
        }
    }

    boolean isActive;

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d("?????? onStop");
        if (!isAppOnForeground()) {
            isActive = false;
            onAnchorLeave();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //  mEngine.stopCapture();
    }

    /**
     * ????????? QNRTCEngine
     */
    private void initQNRTCEngine() {
        // 1. VideoPreviewFormat ??? VideoEncodeFormat ??????????????????
        // 2. ???????????????????????????????????????????????????????????? setLowAudioSampleRateEnabled(true) ?????? setAEC3Enabled(true) ????????????????????????????????????????????????????????????????????????
        QNVideoFormat format = new QNVideoFormat(Config.STREAMING_WIDTH, Config.STREAMING_HEIGHT, Config.STREAMING_FPS);
        QNRTCSetting setting = new QNRTCSetting();
        setting.setCameraID(QNRTCSetting.CAMERA_FACING_ID.FRONT)
                .setHWCodecEnabled(true)
                .setMaintainResolution(true)
                .setVideoBitrate(Config.STREAMING_BITRATE)
                .setVideoEncodeFormat(format)
                .setVideoPreviewFormat(format);
        mEngine = QNRTCEngine.createEngine(getApplicationContext(), setting, this);
        mEngine.setCapturePreviewWindow(mLocalVideoSurfaceView);
        mEngine.setCaptureVideoCallBack(mCaptureVideoCallback);
        mEngine.muteLocalAudio(!mIsMicrophoneOn);
        mEngine.muteRemoteAudio(!mIsSpeakerOn);
        //mEngine.muteRemoteAudio(true);
        //mEngine.muteLocalAudio(true);
    }


//    /**
//     * ????????? QNRTCEngine
//     */
//    private void initQNRTCEngine() {
//        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
//        int videoWidth = preferences.getInt(Config.WIDTH, DEFAULT_RESOLUTION[1][0]);
//        int videoHeight = preferences.getInt(Config.HEIGHT, DEFAULT_RESOLUTION[1][1]);
//        int fps = preferences.getInt(Config.FPS, DEFAULT_FPS[1]);
//        boolean isHwCodec = preferences.getInt(Config.CODEC_MODE, Config.HW) == Config.HW;
//        int videoBitrate = preferences.getInt(Config.BITRATE, DEFAULT_BITRATE[1]);
//        /**
//         * ????????????????????????????????? SDK ??????????????????????????????????????????????????????????????????????????????
//         * ?????????????????????????????????????????????????????????????????????????????????
//         */
//        boolean isMaintainRes = preferences.getBoolean(Config.MAINTAIN_RES, false);
//
//        /**
//         * ???????????????????????????????????????????????????????????????????????? QNRTCSetting#setLowAudioSampleRateEnabled
//         * ??? QNRTCSetting#setAEC3Enabled ??? true ???????????????????????????
//         */
//        boolean isLowSampleRateEnabled = preferences.getInt(Config.SAMPLE_RATE, Config.HIGH_SAMPLE_RATE) == Config.LOW_SAMPLE_RATE;
//        boolean isAec3Enabled = preferences.getBoolean(Config.AEC3_ENABLE, true);
//        mCaptureMode = preferences.getInt(Config.CAPTURE_MODE, Config.CAMERA_CAPTURE);
//
//        /**
//         * VideoPreviewFormat ??? VideoEncodeFormat ??????????????????
//         */
//        QNVideoFormat format = new QNVideoFormat(videoWidth, videoHeight, fps);
//        QNRTCSetting setting = new QNRTCSetting();
//        setting.setCameraID(QNRTCSetting.CAMERA_FACING_ID.FRONT)
//                .setHWCodecEnabled(isHwCodec)
//                .setMaintainResolution(isMaintainRes)
//                .setVideoBitrate(videoBitrate)
//                .setLowAudioSampleRateEnabled(isLowSampleRateEnabled)
//                .setAEC3Enabled(isAec3Enabled)
//                .setVideoEncodeFormat(format)
//                .setVideoPreviewFormat(format);
//
//        //todo --- tillusory start ---
//        mEngine = QNRTCEngine.createEngine(getApplicationContext(), setting, this);
//        mEngine.setCaptureVideoCallBack(new QNCaptureVideoCallback() {
//
//            @Override
//            public int[] onCaptureOpened(List<Size> sizes, List<Integer> fpsAscending) {
//                // ?????????????????????????????????????????????
//                int wantSize = -1; // ????????????????????????, -1 ??????????????????, ?????? QNRTCSetting ?????????
//                int wantFps = -1;  // ?????????????????????, -1 ??????????????????, ?????? QNRTCSetting ?????????
//                for (int i = 0; i < sizes.size(); i++) {
//                    if (sizes.get(i).height == videoHeight) {
//                        wantSize = i;
//                        if (sizes.get(i).width == videoWidth) {
//                            break;
//                        }
//                    }
//                }
//                return new int[] {wantSize, wantFps};
//            }
//
//            @Override
//            public void onCaptureStarted() {
//
//            }
//
//            @Override
//            public void onRenderingFrame(VideoFrame.TextureBuffer  textureBuffer, long timestampNs) {
//
//                int tex = TiSDKManager.getInstance().renderTexture2D(
//                        textureBuffer.getTextureId(),
//                        textureBuffer.getWidth(),
//                        textureBuffer.getHeight(),
//                        isFrontCamera ? TiRotation.CLOCKWISE_ROTATION_270 : TiRotation.CLOCKWISE_ROTATION_90,
//                        isFrontCamera);
//              //  textureBuffer.setTextureId(tex);
//            }
//
//            @Override
//            public void onPreviewFrame(byte[] data, int width, int height, int rotation, int fmt, long timestampNs) {
//
//            }
//
//            @Override
//            public void onCaptureStopped() {
//                captureStoppedSem.drainPermits();
//                captureStoppedSem.release();
//                TiSDKManager.getInstance().destroy();
//            }
//        });
//
//        //todo --- tillusory end ---
//
//    }

    private void startCaptureAfterAcquire() {
        try {
            captureStoppedSem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mEngine.startCapture();
    }

    boolean mAddExtraAudioData = false;
    boolean mEnableAudioEncrypt = false;

//    /**
//     * ???????????????????????? track
//     * ?????? Track ??????????????? https://doc.qnsdk.com/rtn/android/docs/preparation#5
//     */
//    private void initLocalTrackInfoList() {
//        mLocalTrackList = new ArrayList<>();
//        mLocalAudioTrack = mEngine.createTrackInfoBuilder()
//                .setSourceType(QNSourceType.AUDIO)
//                .setMaster(true)
//                .create();
//        mEngine.setLocalAudioPacketCallback(mLocalAudioTrack, new QNLocalAudioPacketCallback() {
//            @Override
//            public int onPutExtraData(ByteBuffer extraData, int extraDataMaxSize) {
//                // ????????? extraData ??????????????????????????????????????? QNRemoteAudioPacketCallback ??????
//                if (mAddExtraAudioData) {
//                    extraData.rewind();
//                    extraData.put((byte) 0x11);
//                    extraData.flip();
//                    return extraData.remaining();
//                }
//                return 0;
//            }
//
//            @Override
//            public int onSetMaxEncryptSize(int frameSize) {
//                // ?????????????????????????????????????????????????????????????????????????????? SDK ??????????????????????????????
//                if (mEnableAudioEncrypt) {
//                    return frameSize + 10;
//                }
//                return 0;
//            }
//
//            @Override
//            public int onEncrypt(ByteBuffer frame, int frameSize, ByteBuffer encryptedFrame) {
//                // ??????????????????????????????????????????????????? encryptedFrame ??????????????????????????????
//                if (mEnableAudioEncrypt) {
//                    encryptedFrame.rewind();
//                    frame.rewind();
//                    encryptedFrame.put((byte) 0x18);
//                    encryptedFrame.put((byte) 0x19);
//                    encryptedFrame.put(frame);
//                    encryptedFrame.flip();
//                    return encryptedFrame.remaining();
//                }
//                return 0;
//            }
//        });
//        mLocalTrackList.add(mLocalAudioTrack);
//
//        switch (mCaptureMode) {
//            case Config.CAMERA_CAPTURE:
//                // ?????? Camera ??????????????? Track
//                mLocalVideoTrack = mEngine.createTrackInfoBuilder()
//                        .setSourceType(QNSourceType.VIDEO_CAMERA)
//                        .setMaster(true)
//                        .setTag("camera").create();
//                mLocalTrackList.add(mLocalVideoTrack);
//                break;
//            case Config.ONLY_AUDIO_CAPTURE:
//              //  mControlFragment.setAudioOnly(true);
//                break;
//            case Config.SCREEN_CAPTURE:
//                // ??????????????????????????? Track
////                createScreenTrack();
////                mControlFragment.setAudioOnly(true);
//                break;
//            case Config.MUTI_TRACK_CAPTURE:
//                // ???????????? + ?????????????????? track
//               // createScreenTrack();
//                mLocalVideoTrack = mEngine.createTrackInfoBuilder()
//                        .setSourceType(QNSourceType.VIDEO_CAMERA)
//                        .setTag("camera").create();
//                mLocalTrackList.add(mLocalVideoTrack);
//                break;
//        }
//    }

    /**
     * ???????????????????????? track
     * ?????? Track ??????????????? https://doc.qnsdk.com/rtn/android/docs/preparation#5
     */
    private void initLocalTrackInfoList() {
        mLocalTrackList = new ArrayList<>();
        // ?????????????????? track
        mLocalAudioTrack = mEngine.createTrackInfoBuilder()
                .setSourceType(QNSourceType.AUDIO)
                .setMaster(true)
                .create();
        mEngine.setLocalAudioPacketCallback(mLocalAudioTrack, new QNLocalAudioPacketCallback() {
            @Override
            public int onPutExtraData(ByteBuffer extraData, int extraDataMaxSize) {
                return 0;
            }

            @Override
            public int onSetMaxEncryptSize(int frameSize) {
                return 0;
            }

            @Override
            public int onEncrypt(ByteBuffer frame, int frameSize, ByteBuffer encryptedFrame) {
                return 0;
            }
        });
        mLocalTrackList.add(mLocalAudioTrack);
        QNVideoFormat format = new QNVideoFormat(480, 640, Config.STREAMING_FPS);
        // ?????? Camera ??????????????? Track
        mLocalVideoTrack = mEngine.createTrackInfoBuilder()
                .setSourceType(QNSourceType.VIDEO_CAMERA)
                .setVideoEncodeFormat(format)
                .setMaster(true)
                .create();
        mLocalTrackList.add(mLocalVideoTrack);
    }

    @Override
    public void onRoomStateChanged(QNRoomState qnRoomState) {
        mCurrentRoomState = qnRoomState;
        LogUtil.d("room state change " + qnRoomState);
        switch (qnRoomState) {
            case CONNECTED:
//                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
//                    mLoadingDialog.dismiss();
//                }
//                if (mExecutor == null || mExecutor.isShutdown()) {
//                    mExecutor = Executors.newSingleThreadScheduledExecutor();
//                    mExecutor.scheduleAtFixedRate(mAudienceNumGetter, 0, Config.GET_AUDIENCE_NUM_PERIOD, TimeUnit.SECONDS);
//                }
                // endTime = System.currentTimeMillis();
                // LogUtil.d("join finish " + (endTime - startTime));
                mEngine.publishTracks(mLocalTrackList);
//                updateUIAfterLiving();
//                if (mIsPkMode) {
//                    // PK ??????????????????????????????????????????????????????
//                    createMergeJob();
//                    mPkUserList.add(mTargetPkRoomInfo.getCreator());
//                }
                break;
            case RECONNECTING:
//                ToastUtils.showShortToast(getString(R.string.toast_reconnecting));
//                if (mForwardJob != null) {
//                    mForwardJob = null;
//                }
//                if (mQNMergeJob != null) {
//                    mQNMergeJob = null;
//                }
                break;
            case RECONNECTED:
//                ToastUtils.showShortToast(getString(R.string.toast_reconnected));
//                if (mIsPkMode) {
//                    createMergeJob();
//                } else {
//                    createForwardJob();
//                }
                break;
//            case CONNECTING:
//                ToastUtil.show(this, "?????????");
//                break;
        }
    }

    @Override
    public void onRoomLeft() {
        if (isPkStatus) {
            mTargetPkRoomToken = (String) SharedPreferencesUtils.getParam(QNLiveRoomAnchorActivity.this,"roomToken","");
            mEngine.joinRoom(mTargetPkRoomToken);
        }
    }

    @Override
    public void onRemoteUserJoined(String s, String s1) {
        LogUtil.d("onRemoteUserJoined");
        LogUtil.d("onRemoteUserJoined" + s + "--" + s1);
    }

    @Override
    public void onRemoteUserReconnecting(String s) {

    }

    @Override
    public void onRemoteUserReconnected(String s) {

    }

    @Override
    public void onRemoteUserLeft(String s) {

    }

    @Override
    public void onLocalPublished(List<QNTrackInfo> list) {
        //LogUtil.d("???????????? " + list.size());
        // ????????????????????????????????????????????????????????? PK ?????????
        // setMergeOptions(list, false);

        if (isPkStatus) {

        } else {
            mTargetPkRoomToken = "";
            createForwardJob();
        }
        mLocalTrackInfoList = list;
    }

    HashMap<String, List<QNTrackInfo>> remoteStreamMap;
    List<List<QNTrackInfo>> remoteStreamList; //???????????????
    List<List<QNTrackInfo>> linkMicStreamList;  //????????????
    List<QNTrackInfo> mLocalTrackInfoList;

    //??????????????????
    @Override
    public void onRemotePublished(String remoteUserId, List<QNTrackInfo> list) {
        // LogUtil.d("onRemotePublished uid = " + remoteUserId);
        // LogUtil.d("onRemotePublished size = " + list.size());
//        if (isPkStatus) {
//            if (list.size() == 2) {
//                for (int i = 0; i < list.size(); i++) {
//                    if (list.get(i).isVideo()) {
//                        mEngine.setRenderWindow(list.get(i),viewPkOtherWindow);
//                    }
//                }
//            }
//        } else {
            if (list.size() == 0) {
                return;
            } else if (list.size() == 1) { //??????????????????
                if (remoteStreamMap.containsKey(remoteUserId)) {
                    remoteStreamList.remove(remoteStreamMap.get(remoteUserId));
                }
                remoteStreamMap.put(remoteUserId, list);
                remoteStreamList.add(list);
                layoutLiveManager.addManager(remoteUserId);
                //remoteViewAdapter.notifyDataSetChanged();
                refreshMergeOption();
                createMergeJob();
            } else if (list.size() == 2) { //?????????????????? && pk
                if (isPkStatus) { //pk??????
                    remoteStreamMap.clear();
                    remoteStreamList.clear();
                    remoteStreamList.add(list);
                    refreshMergeOption();
                    createMergeJob();
                    renderOtherAnchor(list);
                } else {
                    if (remoteStreamMap.containsKey(remoteUserId)) {
                        remoteStreamList.remove(remoteStreamMap.get(remoteUserId));
                        remoteStreamList.remove(remoteStreamMap.get(remoteUserId));
                    }
                    remoteStreamMap.put(remoteUserId, list);
                    remoteStreamList.add(list);
                    linkMicStreamList.add(list);
                    remoteViewAdapter.notifyDataSetChanged();
                    refreshMergeOption();
                    createMergeJob();
                }
            }
    }

    @Override
    public void onRemoteUnpublished(String remoteUserId, List<QNTrackInfo> list) {
        if (list.size() == 0) {
            return;
        } else if (list.size() == 1) {
            if (remoteStreamMap.containsKey(remoteUserId)) {
                remoteStreamList.remove(remoteStreamMap.get(remoteUserId));
                remoteStreamMap.remove(remoteUserId);
            }
            remoteViewAdapter.notifyDataSetChanged();
            layoutLiveManager.removeManager(remoteUserId);
            // mEngine.muteTracks(list);
            refreshMergeOption();
            if (remoteStreamMap.size() == 0) { //?????????????????????????????????
                createForwardJob();
            } else {
                createMergeJob();
            }
        } else if (list.size() == 2) {
            if (isPkStatus) {
                onStopPk();
            } else {
                if (remoteStreamMap.containsKey(remoteUserId)) {
                    remoteStreamList.remove(remoteStreamMap.get(remoteUserId));
                    linkMicStreamList.remove(remoteStreamMap.get(remoteUserId));
                    remoteStreamMap.remove(remoteUserId);
                }
                remoteViewAdapter.notifyDataSetChanged();
                // mEngine.muteTracks(list);
                refreshMergeOption();
                if (remoteStreamMap.size() == 0) { //?????????????????????????????????
                    createForwardJob();
                } else {
                    createMergeJob();
                }
            }
        }
    }

    @Override
    public void onRemoteUserMuted(String s, List<QNTrackInfo> list) {

    }

    @Override
    public void onSubscribed(String s, List<QNTrackInfo> list) {

    }

    @Override
    public void onSubscribedProfileChanged(String s, List<QNTrackInfo> list) {

    }

    @Override
    public void onKickedOut(String s) {

    }

    @Override
    public void onStatisticsUpdated(QNStatisticsReport qnStatisticsReport) {

    }

    @Override
    public void onRemoteStatisticsUpdated(List<QNStatisticsReport> list) {

    }

    @Override
    public void onAudioRouteChanged(QNAudioDevice qnAudioDevice) {

    }

    @Override
    public void onCreateMergeJobSuccess(String s) { //??????????????????
        mMergeJobId = s;
        mEngine.setMergeStreamLayouts(mMergeTrackOptions, mMergeJobId);
        if (mForwardJob != null) {
            mEngine.stopForwardJob(mForwardJob.getForwardJobId());
            mForwardJob = null;
        }
        updateLinkMicUserList();
    }

    @Override
    public void onCreateForwardJobSuccess(String s) { //????????????????????????
        if (mMergeJobId != null) {
            stopMergeJob();
        }
        if (remoteStreamMap != null) {
            updateLinkMicUserList();
        }
    }

    @Override
    public void onError(int i, String s) {
        LogUtil.d("onError " + i + "---" + s);
    }

    @Override
    public void onMessageReceived(QNCustomMessage qnCustomMessage) {

    }

    @Override
    public void onClientRoleChanged(QNClientRole qnClientRole) {

    }

    @Override
    public void onMediaRelayStateChanged(Map<String, QNMediaRelayState> map) {

    }


    void startStreaming() {

    }

    boolean isVideoMute = false;
    boolean isAudioMute = false;
    boolean isFrontCamera = true;

    /**
     * ----------------------------------------------------------------------
     * ???????????????
     */
    //????????? - ????????????
    void initPreViewLayout() {
        mLayoutPreview.setPreviewCallback(new QNLiveRoomPreviewLayout.PreviewCallback() {
            @Override
            public void onClose() {
                finish();
            }

            @Override
            public void onBeautyPanel() {
                if (tiPanel != null) {
                    tiPanel.showBeautyPanel();
                }
            }

            @Override
            public void onSwitchCamera() {
                showLiveAnchorPreViewSettingPop();
            }

            @Override
            public void onClickCover() {
                showSelectPic();
            }

            @Override
            public void onStartLive(String roomName, String coverUrl, int audioQualityType) {
                beginToShow(roomName, coverUrl);
            }
        });

    }

    //????????? - ??????????????????
    public static final String TAKE_PHOTO = "??????";
    public static final String CHOOSE_PHOTO = "??????????????????";

    private void showSelectPic() {
        new AlertView(null, null, "??????", null,
                new String[]{TAKE_PHOTO, CHOOSE_PHOTO},
                QNLiveRoomAnchorActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                if (data.equals(TAKE_PHOTO)) {
                    tryToTakePhone();
                } else if (data.equals(CHOOSE_PHOTO)) {
                    tryToChoosePhone();
                }
            }
        }).show();
    }

    //????????? - ???????????????????????????
    public void showLiveAnchorPreViewSettingPop() {
        // LiveSettingStateBean liveSettingStateBean = GsonUtil.GsonToBean(liveSettingStateJson, LiveSettingStateBean.class);
        LiveSettingStateBean liveSettingStateBean = new LiveSettingStateBean(isAudioMute, isVideoMute, isFrontCamera);
        LiveAnchorSettingPop liveAnchorSettingPop = new LiveAnchorSettingPop(QNLiveRoomAnchorActivity.this, liveSettingStateBean, true);
        liveAnchorSettingPop.showPopupWindow();
        liveAnchorSettingPop.setOnLiveSettingListener(new LiveAnchorSettingPop.OnLiveSettingListener() {
            @Override
            public void onChooseCameraFront() {
                doCameraFront();
            }

            @Override
            public void onChooseCameraBack() {
                doCameraBack();
            }

            @Override
            public void onChooseCameraClose() {
                doCameraClose();
            }

            @Override
            public void onChooseAudioOpen() {

            }

            @Override
            public void onChooseAudioClose() {

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

    String roomToken = "";
    long startTime = 0L;
    long endTime = 0L;

    //????????? - ??????
    private void beginToShow(final String live_title, final String live_poster) {
        String tid = mLayoutPreview.getChooseLabel();
        int isTicket = mLayoutPreview.getIsTicket();
        int ticketBean = mLayoutPreview.getTicketBeans();
        int isInteract = mLayoutPreview.getIsInteract();


        if (isTicket == 1 && ticketBean == 0) {
            ToastUtil.show(QNLiveRoomAnchorActivity.this, "???????????????????????????????????????");
            return;
        }

        int isRecord = mLayoutPreview.getRecord();

        String password = mLayoutPreview.getPassword();
        String is_pwd = TextUtil.isEmpty(password) ? "0" : "1";

        HttpHelper.getInstance().beginToShow(live_poster, live_title, tid,
                String.valueOf(isInteract), String.valueOf(isTicket), String.valueOf(ticketBean),
                String.valueOf(isRecord), is_pwd, password,
                new HttpCodeListener() {

                    @Override
                    public void onSuccess(String data) {
                        BeginToShowBean beginToShowBean = GsonUtil.GsonToBean(data, BeginToShowBean.class);
                        if (beginToShowBean != null && beginToShowBean.getData() != null) {
                            startHeaderReport();
                            mIsEnterRoom = true;
                            mRoomToken = beginToShowBean.getData().getRoom_token();
                            mRoomId = beginToShowBean.getData().getRoom_id();
                            startTime = System.currentTimeMillis();
                            LogUtil.d("start join room = " + mRoomToken);
                            mEngine.joinRoom(mRoomToken);
                            startCountDown();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        ToastUtil.show(QNLiveRoomAnchorActivity.this, msg);
                    }
                });

//        LiveHttpHelper.getInstance().beginToShow(live_poster, live_title, tid, String.valueOf(isInteract), String.valueOf(isTicket), String.valueOf(ticketBean), new HttpListener() {
//            @Override
//            public void onSuccess(String data) {
//                LogUtil.e(data);
//                Log.d("onSuccess", data);
//                try {
//                    JSONObject obj = new JSONObject(data);
//                    switch (obj.getInt("retcode")) {
//                        case 2000:
//                            JSONObject root = obj.getJSONObject("data");
//                            if (root != null || root.length() != 0) {
//                                String nickName = root.getString("nickname");
//                                String roomId = root.getString("room_id");
//                                String live_title = root.getString("live_title");
//                                String live_poster = root.getString("live_poster");
//                                mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getLayoutPreview().startLive(roomId, live_poster);
//                                LiveRoomAnchorFragment mLiveRoomAnchorFragment = mLayoutTuiLiverRomAnchor.getLiveRomAnchor();
//                                Bundle b = new Bundle();
//                                //b.putString("push_address",pushAddress);
//                                if (roomId != null) {
//                                    b.putInt("room_id", Integer.valueOf(!TextUtils.isEmpty(roomId) && !"null".equals(roomId) ? roomId : ""));
//                                    SharedPreferencesUtils.setParam(LiveRoomAnchorActivity.this, "room_id", roomId);
//                                    mLiveRoomAnchorFragment.setArguments(b);
//                                    mLiveRoomAnchorFragment.initData();
//                                    getSupportFragmentManager().beginTransaction().replace(R.id.live_anchor_container, mLiveRoomAnchorFragment, "tuikit-live-anchor-fragment");
//                                }
//                            }
//                            //???????????????????????????
//                            LiveRoomAnchorFragment mLiveRoomAnchorFragment = mLayoutTuiLiverRomAnchor.getLiveRomAnchor();
//                            mLiveRoomAnchorFragment.setOnLiveCreateRoomCallBack(new LiveRoomAnchorFragment.OnLiveCreateRoomCallBack() {
//                                @Override
//                                public void createRoomSuccess() {
//                                    final String selfUserId = ProfileManager.getInstance().getUserModel().userId;
//                                    getAnchorInfo(selfUserId);
//                                }
//                            });
//
//                            break;
//                        default:
//                            ToastUtil.show(LiveRoomAnchorActivity.this, obj.getString("msg"));
//                            break;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFail(String msg) {
//                LogUtil.e(msg);
//                //????????????????????????roomid??????
//                String roomId = SharedPreferencesUtils.geParam(LiveRoomAnchorActivity.this, "room_id", "");
//                LiveHttpHelper.getInstance().closeLive(roomId, new HttpListener() {
//                    @Override
//                    public void onSuccess(String data) {
//                        LogUtil.d(data);
//                        // beginToShow(live_title);
//                    }
//
//                    @Override
//                    public void onFail(String msg) {
//                        LogUtil.d(msg);
//                    }
//                });
//                if (!TextUtils.isEmpty(msg)) {
//                    ToastUtil.show(LiveRoomAnchorActivity.this, msg);
//                    return;
//                }
//            }
//        });
//
    }

    /**-------------------------------------------------------------------------------*/


    /**
     * ----------------------------------------------------------------------
     * ??????????????????
     */
    Timer mTimer;

    //????????????????????????
    public void startHeaderReport() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                HttpHelper.getInstance().reportAnchorHeartBeat();
            }
        }, 100, 30000);
    }

    //????????????????????????
    public void stopHeaderReport() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    /**
     * --------------------------------------------------------------------------------
     * ??????????????????
     */

    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private static final int MY_PERMISSIONS_REQUEST_SELECT_PHONE = 8;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private Uri cropUri;
    private File cropImage;

    //??????
    public void tryToTakePhone() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(this, perms)) {//???????????????????????????
            //????????????????????????????????????????????????????????????
            //???????????????????????????
            //????????????????????????????????????
            EasyPermissions.requestPermissions(this, "??????????????????????????????:\n\n1.????????????????????????\n\n2.??????", MY_PERMISSIONS_REQUEST_CALL_PHONE, perms);
        } else {
            takePhoto();
        }

    }

    //??????
    public void tryToChoosePhone() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {//???????????????????????????
            //????????????????????????????????????????????????????????????
            //???????????????????????????
            //????????????????????????????????????
            EasyPermissions.requestPermissions(this, "??????????????????????????????:\n\n1.????????????????????????\n\n2.??????", MY_PERMISSIONS_REQUEST_CALL_PHONE2, perms);
        } else {
            choosePhoto();
        }
    }

    /**
     * ??????
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
     * ?????????????????????
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
            //  choosePhotoBySelector();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (perms.contains(Manifest.permission.CAMERA)) {
            ToastUtil.show(getApplicationContext(), "????????????,?????????????????????");
            return;
        }
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(getApplicationContext(), "????????????,?????????????????????");
            return;
        }
    }


    /**-------------------------------------------------------------------------------*/


    /**
     * --------------------------------------------------------------------------------
     * activityResult
     */

    private Handler handler = new MyHandler(this);
    int picFlag;
    String imgpre;

    static class MyHandler extends Handler {
        WeakReference<QNLiveRoomAnchorActivity> liveRoomAnchorActivityWeakReference;

        public MyHandler(QNLiveRoomAnchorActivity activity) {
            liveRoomAnchorActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            QNLiveRoomAnchorActivity activity = liveRoomAnchorActivityWeakReference.get();
            switch (msg.what) {
                case 152:
                    if (activity.picFlag == 1) {
                        String s = (String) msg.obj;
                        BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
                        String imgUrl = activity.imgpre + beanicon.getData();
                        if (activity.liveInfoPop != null) {
                            activity.liveInfoPop.refreshImageUrl(imgUrl);
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
                                        ToastUtil.show(activity, jsonObject.getString("msg"));
                                        return;
                                    }
                                    BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
                                    if (beanicon.getRetcode() == 2000) {
//                                        headurl = beanicon.getData();
//                                        ImageLoader.loadImage(activity, imgpre + headurl, mEditPersonmsgIcon);
//                                        livePoster = imgpre + headurl;
//                                        if (getLiveAnchorFragment() != null) {
//                                            getLiveAnchorFragment().changeCoverImg(livePoster);
//                                        }
                                        String tempPoster = activity.imgpre + beanicon.getData();
                                        activity.mLayoutPreview.showCover(activity, tempPoster);
                                        ToastUtil.show(activity, "????????????");
                                        SharedPreferencesUtils.setParam(activity, Constants.EDIT_LIVE_ICON, tempPoster);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtil.show(activity, s);
                        }
                        break;
                    }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)????????????
                        switch (picFlag) {
                            case 0:
                                //??????
                                RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), photo);
                                circleDrawable.getPaint().setAntiAlias(true);
                                circleDrawable.setCircular(true);
                                //mEditPersonmsgIcon.setImageDrawable(circleDrawable); //??????????????????ImageView?????????

                                break;
                            case 10:
                                //??????

                                break;

                        }
                        ToastUtil.show(getApplicationContext(), "???????????????,???????????????...");
                    }
                    if (picFlag == 0) {
                        // ???????????????
                        ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
                        PhotoUploadTask put = new PhotoUploadTask(
                                NetPic() + "Api/Api/fileUpload"  //"http://59.110.28.150:888/Api/Api/fileUpload"
                                , is,
                                this, handler);
                        put.start();
                    } else {
                        // ???????????????
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
//            case 10000:
//                if (data != null) {
//                    ToastUtil.show(QNLiveRoomAnchorActivity.this, "???????????????");
//                    //??????????????????????????????
//                    ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
//                    for (int i = 0; i < images.size(); i++) {
//                        LogUtil.d(images.get(i));
//                        uploadImage(images.get(i));
//                        //sendMessageByPath(images.get(i));
//                    }
//                }
//                break;
        }
    }

    /**
     * -------------------------------------------------------------------------------
     */


    public void getAnchorCard() {
        HttpHelper.getInstance().getAnchorCard("", new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                AnchorLiveCardBean labelBean = GsonUtil.GsonToBean(data, AnchorLiveCardBean.class);
                if (labelBean == null) {
                    ToastUtil.show(QNLiveRoomAnchorActivity.this, "????????????????????????,?????????");
                    finish();
                    return;
                }
                mLayoutPreview.showLiveLabel(labelBean.getData());
                livePoster = labelBean.getData().getLive_poster();
                liveTitle = labelBean.getData().getLive_title();
                mLayoutPreview.showTitle(liveTitle);
                mLayoutPreview.showCover(QNLiveRoomAnchorActivity.this, livePoster);
                mLayoutPreview.showHistoryInteraction(labelBean.getData().getIs_interaction());
                mLayoutPreview.showHistoryTicket(labelBean.getData().getIs_ticket(), labelBean.getData().getTicketBeans());
                mLayoutPreview.showInteractTip(labelBean.getData().getInteraction_tips());
                mLayoutPreview.showTicketPermission(labelBean.getData().getAnchor_status(), labelBean.getData().getTicket_tips());
                if ("1".equals(labelBean.getData().getIs_record())) {
                    mLayoutPreview.showRecordBtn();
                }
                if ("1".equals(labelBean.getData().getIs_apply_pwd())) {
                    mLayoutPreview.showPasswordEdit();
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

    /**
     *--------------------------------------------------------------------------------
     *
     */


    /**
     * -------------------------------------------------------------------------------
     * ????????????
     */

    QNForwardJob mForwardJob;
    int mSerialNum;

    //?????????
    private void createForwardJob() {
        if (mForwardJob == null) {
            mForwardJob = new QNForwardJob();
            mForwardJob.setForwardJobId(getForwardJobId());
            mForwardJob.setPublishUrl(getPublishUrl());
            mForwardJob.setAudioTrack(mLocalAudioTrack);
            mForwardJob.setVideoTrack(mLocalVideoTrack);
            mForwardJob.setInternalForward(true);
        }
        // ToastUtil.show(this, "????????????" + mForwardJob.getPublishUrl());
        //LogUtil.d(mForwardJob.getForwardJobId());
        //LogUtil.d(mForwardJob.getPublishUrl());
        //Log.i(TAG, "create forward job : " + mForwardJob.getForwardJobId());
        mEngine.createForwardJob(mForwardJob);
    }

    String getForwardJobId() {
        return "forward-" + MyApp.uid;
    }

    String getMergeJobId() {
        return "merge-" + MyApp.uid;
    }

    QNMergeJob mQNMergeJob;
    String mMergeJobId;

    private void createMergeJob() {
        // ????????????????????????
        if (mQNMergeJob == null) {
            mQNMergeJob = new QNMergeJob();
        }
        // ?????????????????? id?????? id ?????????????????????????????????
        mQNMergeJob.setMergeJobId(getMergeJobId());
        // ???????????????????????????????????????????????????????????????
        mQNMergeJob.setPublishUrl(getPublishUrl());
        mQNMergeJob.setWidth(Config.STREAMING_WIDTH);
        mQNMergeJob.setHeight(Config.STREAMING_HEIGHT);
        // QNMergeJob ?????????????????? bps?????????????????????????????? 1200kbps???????????????????????????????????? 1200 * 1000
        mQNMergeJob.setBitrate(Config.STREAMING_BITRATE);
        mQNMergeJob.setFps(Config.STREAMING_FPS);
        mQNMergeJob.setStretchMode(QNStretchMode.ASPECT_FILL);

//        QNBackGround qnBackGround = new QNBackGround();
//        qnBackGround.setFile(Config.STREAMING_BACKGROUND);
//        qnBackGround.setX(0);
//        qnBackGround.setY(0);
//        qnBackGround.setH(Config.STREAMING_HEIGHT);
//        qnBackGround.setW(Config.STREAMING_WIDTH);
//        mQNMergeJob.setBackground(qnBackGround);
        // ??????????????????
        mEngine.createMergeJob(mQNMergeJob);
    }

    private void stopForwardJob() {
        if (mForwardJob != null) {
            mEngine.stopForwardJob(mForwardJob.getForwardJobId());
            mForwardJob = null;
        }
    }

    private void stopMergeJob() {
        mEngine.stopMergeStream(mMergeJobId);
        mMergeJobId = "";
    }

    List<QNMergeTrackOption> mMergeTrackOptions;
    int mergeMarginBottom;
    int mergeMarginBetween;

    private void setMergeOptions(List<QNTrackInfo> trackInfoList, boolean isRemote) {
        for (QNTrackInfo info : trackInfoList) {
            QNMergeTrackOption option = new QNMergeTrackOption();
            option.setTrackId(info.getTrackId());
            if (info.isVideo()) {
                if (isPkStatus) { //pk????????????
                    if (isRemote) { //????????????
                        option.setX(Config.STREAMING_WIDTH / 2);
                        option.setY(Config.STREAMING_HEIGHT/2 - Config.STREAMING_WIDTH * 2 / 3);
                        option.setZ(0);
                        option.setWidth(Config.STREAMING_WIDTH / 2);
                        option.setHeight(Config.STREAMING_WIDTH * 2 / 3);
                    } else { //??????
                        option.setX(0);
                        option.setY(Config.STREAMING_HEIGHT/2 - Config.STREAMING_WIDTH * 2 / 3);
                        option.setZ(0);
                        option.setWidth(Config.STREAMING_WIDTH / 2);
                        option.setHeight(Config.STREAMING_WIDTH * 2 / 3);
                    }
                } else { //??????????????????
                    if (isRemote) {
                        option.setX(Config.STREAMING_WIDTH / 3 * 2);
                        option.setY(Config.STREAMING_HEIGHT - (++remoteUserIndex) * ((Config.STREAMING_WIDTH / 3) + mergeMarginBetween) - mergeMarginBottom);
                        option.setZ(1);
                        option.setWidth(Config.STREAMING_WIDTH / 4);
                        option.setHeight(Config.STREAMING_WIDTH / 3);
                    } else {
                        option.setX(0);
                        option.setY(0);
                        option.setZ(0);
                        option.setWidth(Config.STREAMING_WIDTH);
                        option.setHeight(Config.STREAMING_HEIGHT);
                    }
                }
            }
            mMergeTrackOptions.add(option);
        }
    }

    int remoteUserIndex = 0;

    private void refreshMergeOption() {
        remoteUserIndex = 0;
        if (mMergeTrackOptions == null) {
            mMergeTrackOptions = new ArrayList<>();
        } else {
            mMergeTrackOptions.clear();
        }
        setMergeOptions(mLocalTrackInfoList, false);
        for (List<QNTrackInfo> trackInfoList : remoteStreamList) {
            setMergeOptions(trackInfoList, true);
        }

        for (QNRTCUser user : mEngine.getUserList()) {
            LogUtil.d(user.getUserId());
            LogUtil.d(user.getUserData());
        }
    }


    //pk???
    private void setMergeOptions2(List<QNTrackInfo> trackInfoList, boolean isRemote) {
        for (QNTrackInfo info : trackInfoList) {
            QNMergeTrackOption option = new QNMergeTrackOption();
            option.setTrackId(info.getTrackId());
            if (info.isVideo()) {
                option.setX(isRemote ? Config.STREAMING_WIDTH / 2 : 0);
                option.setY(Config.STREAMING_HEIGHT * 4 / 23);
                option.setZ(0);
                option.setWidth(Config.STREAMING_WIDTH / 2);
                option.setHeight(Config.STREAMING_HEIGHT / 2);
            }
            if (mMergeTrackOptions == null) {
                mMergeTrackOptions = new ArrayList<>();
            }
            mMergeTrackOptions.add(option);

        }
    }

    /**
     * ------------------------------------------------------------------
     */

    String getPublishUrl() {
        // String url = "rtmp://pili-publish.aiwujie.net/testsm";
        String url = "rtmp://pili-publish.shengmo.cn/testsm";
        return url + "/" + mRoomId + "_" + MyApp.uid + "?serialnum=" + mSerialNum++;
    }

    void startCountDown() {
        mLayoutPreview.setVisibility(View.GONE);
        countdownTimerView.countDownAnimation(CountDownTimerView.DEFAULT_COUNTDOWN_NUMBER);
        countdownTimerView.setOnCountDownListener(new ICountDownTimerView.ICountDownListener() {
            @Override
            public void onCountDownComplete() {
                initLiveRoomView();
                createGroup();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mForwardJob != null) {
            mEngine.stopForwardJob(mForwardJob.getForwardJobId());
        }
        if (TextUtil.isEmpty(mMergeJobId)) {
            mEngine.stopMergeStream(mMergeJobId);
        }
        mEngine.leaveRoom();
        mEngine.destroy();
        TiSDKManager.getInstance().destroy();
        unInitGroupListener();
        destroyGroup();
        closeLiveRoom();
        stopHeaderReport();
        EventBus.getDefault().unregister(this);
        mLayoutTopToolBar.removeCountDown();//???????????????
    }

    @Override
    public void onBackPressed() {
        preExitRoom();
    }


    boolean mIsEnterRoom = false;

    private void preExitRoom() {
        if (mIsEnterRoom) {
            showExitInfoDialog("??????????????????????????????????????????", false);
        } else {
            finishRoom();
        }
    }

    void finishRoom() {
        finish();
    }

    void destroyRoom() {

    }

    /**
     * ??????????????????
     *
     * @param msg     ????????????
     * @param isError true?????????????????????????????? false???????????????????????????????????????
     */
    public void showExitInfoDialog(String msg, Boolean isError) {
        final ExitConfirmDialog dialogFragment = new ExitConfirmDialog();
        dialogFragment.setCancelable(false);
        dialogFragment.setMessage(msg);
        if (dialogFragment.isAdded()) {
            dialogFragment.dismiss();
            return;
        }
        if (isError) {
            destroyRoom();
            dialogFragment.setPositiveClickListener(new ExitConfirmDialog.PositiveClickListener() {
                @Override
                public void onClick() {
                    dialogFragment.dismiss();
                    showPublishFinishDetailsDialog();
                }
            });
            dialogFragment.show(getSupportFragmentManager(), "ExitConfirmDialog");
            return;
        }
        dialogFragment.setPositiveClickListener(new ExitConfirmDialog.PositiveClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();
                destroyRoom();
                showPublishFinishDetailsDialog();
            }
        });
        dialogFragment.setNegativeClickListener(new ExitConfirmDialog.NegativeClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "ExitConfirmDialog");
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????
     */
    protected void showPublishFinishDetailsDialog() {
        FinishDetailDialog dialog = new FinishDetailDialog(mRoomId,(1 == mLayoutPreview.getRecord()));
        Bundle args = new Bundle();
        long second = 0;
        if (mStartTime != 0) {
            second = (System.currentTimeMillis() - mStartTime) / 1000;
        }
        args.putString(LIVE_TOTAL_TIME, UIUtil.formattedTime(second));
        args.putString(ANCHOR_LOCAL_BEANS, String.format(Locale.CHINA, "%d", mLocalBeans));
        args.putString(TOTAL_AUDIENCE_COUNT, String.format(Locale.CHINA, "%d", mTotalMemberCount));
        dialog.setArguments(args);
        dialog.setCancelable(false);
        if (dialog.isAdded()) {
            dialog.dismiss();
        } else {
            dialog.show(getSupportFragmentManager(), "");
        }
    }

    /**
     * -------------------------------------------------------
     * IM??????
     */

    //???????????????
    public void createGroup() {
        V2TIMManager.getInstance().createGroup(V2TIMManager.GROUP_TYPE_AVCHATROOM, mRoomId, MyApp.uid + "????????????", new V2TIMValueCallback<String>() {
            @Override
            public void onError(int code, String s) {
                String msg = s;
                if (code == 10036) {
                    msg = "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????IM?????????????????????????????????https://cloud.tencent.com/document/product/269/11673";
                }
                if (code == 10037) {
                    msg = "????????????????????????????????????????????????????????????????????????????????????,???????????????https://cloud.tencent.com/document/product/269/11673";
                }
                if (code == 10038) {
                    msg = "?????????????????????????????????????????????????????????????????????????????????https://cloud.tencent.com/document/product/269/11673";
                }
                if (code == 10025) {
                    // 10025 ??????????????????????????????????????????????????????
                    onSuccess("success");
                } else {
                    ToastUtil.show(QNLiveRoomAnchorActivity.this, "??????????????????");
                    finish();
                }
            }

            @Override
            public void onSuccess(String s) {
                //ToastUtil.show(QNLiveRoomAnchorActivity.this, "??????????????????");
                updateGroupInfo();
                initGroupListener();
                initChatMessageLayout();
                initRequestLinkLayout();
            }
        });
    }

    void updateGroupInfo() {
        V2TIMGroupInfo v2TIMGroupInfo = new V2TIMGroupInfo();
        v2TIMGroupInfo.setGroupID(mRoomId);
        v2TIMGroupInfo.setGroupName(ProfileManager.getInstance().getUserModel().userName);
        v2TIMGroupInfo.setFaceUrl(livePoster);
        V2TIMManager.getGroupManager().setGroupInfo(v2TIMGroupInfo, new V2TIMCallback() {
            @Override
            public void onSuccess() {
                // LogUtil.d("?????????????????????");
            }

            @Override
            public void onError(int i, String s) {
                // LogUtil.d("???????????????" + i + s);
            }
        });
    }

    //???????????????
    void destroyGroup() {
        V2TIMManager.getInstance().dismissGroup(mRoomId, new V2TIMCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    TimLiveRoomHelper.OnTimSignalingListener mSignalingListener;
    TimLiveRoomHelper.OnTimLiveListener mLiveGroupListener;

    List<String> linkRequestUserList; //??????????????????
    Map<String, String> requestLinkMap;
    List<String> inviteLinkUserList; //???????????????????????????

    //????????????
    void initGroupListener() {
        linkRequestUserList = new ArrayList<>();
        requestLinkMap = new HashMap<>();
        inviteLinkUserList = new ArrayList<>();
        mLiveGroupListener = new TimLiveRoomHelper.OnTimLiveListener() {
            @Override
            public void onMemberEnter(String groupId, String uid) {

            }

            @Override
            public void onMemberLeave(String groupId, String uid) {

            }

            @Override
            public void onGroupDismiss(String groupId) {

            }
        };
        mSignalingListener = new TimLiveRoomHelper.OnTimSignalingListener() {
            @Override
            public void onReceiveLinkRequest(String inviteId, String userId, String reason) {
                if (checkLinkNumLimit()) {
                    rejectLinkInvite(inviteId, "??????????????????????????????????????????,????????????");
                    return;
                }
                if (reason.contains("auto")) {
                    acceptLinkInvite(inviteId);
                } else {
                    // showLinkRequestPop(inviteId, userId, reason);
                    addLinkUser(userId, inviteId);
                    refreshLinkButtonState();
                }
            }

            @Override
            public void onCancelLinkRequest(String inviteId, String userId, String reason) {
                //??????????????????????????? ???????????????????????????????????????
               // onCancelRequestLink(reason);
                removeLinkUser(userId);
                refreshLinkButtonState();
            }

            @Override
            public void onAcceptLinkRequest(String inviteId, String userId, String reason) {
                // ToastUtil.show(mContext, "onAcceptLinkRequest");
                if (!userId.equals(MyApp.uid)) { //??????????????????
                    inviteLinkUserList.remove(userId);
                }
            }

            @Override
            public void onRejectLinkRequest(String inviteId, String userId, String reason) {
                //ToastUtil.show(mContext, "onRejectLinkRequest");
                if (!userId.equals(MyApp.uid)) { //??????????????????
                    ToastUtil.show(mContext, reason);
                    inviteLinkUserList.remove(userId);
                }
            }

            @Override
            public void onTimeoutLinkRequest(String inviteId, String userId, String reason) {
                //ToastUtil.show(mContext,"onTimeoutLinkRequest");
                if (!TextUtil.isEmpty(userId)) {
                    if (userId.equals(MyApp.uid)) { //????????????????????????
                        removeLinkUserByInviteId(inviteId);
                    } else { //???????????????????????????
                        inviteLinkUserList.remove(userId);
                    }
                }
            }

            @Override
            public void onReceivePkRequest(String inviteId, String userId, String reason) {
                if (requestPkMap != null) {
                    requestLinkMap = new HashMap<>();
                }
                requestLinkMap.put(userId, inviteId);
//                if (isPkStatus) {
//                    doLivePkRefuse(userId,"????????????pk???");
//                    return;
//                }
                if (isAutoRefusePk) {
                    doLivePkRefuse(userId,ProfileManager.getInstance().getUserModel().userName + " ???????????????pk??????");
                    return;
                }


                showPkInvitePop(userId);
            }

            @Override
            public void onCancelPkRequest(String inviteId, String userId, String reason) {

            }

            @Override
            public void onAcceptPkRequest(String inviteId, String userId, String reason) {
                if (!userId.equals(MyApp.uid)) {  //????????????pk??????
                    //1.????????????
                    if (mForwardJob != null) {
                        mEngine.stopForwardJob(mForwardJob.getForwardJobId());
                        mForwardJob = null;
                    }
                    isPkStatus = true;
                    //2.??????????????????
                    mEngine.leaveRoom();
                    //3.????????????????????????

                    //LogUtil.d("room token = " + token);
                    // mEngine.switchRoom(token);
                    //4.????????????pk
                    showPkView();
                }
            }

            @Override
            public void onRejectPkRequest(String inviteId, String userId, String reason) {
                if (!userId.equals(MyApp.uid)) { //????????????pk
                    ToastUtil.show(mContext, reason);
                }
            }

            @Override
            public void onTimeoutPkRequest(String inviteId, String userId, String reason) {

            }
        };
        TimLiveRoomHelper.getInstance().initListener(mLiveGroupListener, mSignalingListener);
        TimLiveRoomHelper.getInstance().initRoom(mRoomId);
    }

    //????????????
    void unInitGroupListener() {
        TimLiveRoomHelper.getInstance().unitListener();
    }

    //????????????????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveLiveEvent(LiveMessageBean event) {
        if (TextUtils.isEmpty(event.getMessage().getGroupID())) { //??????
            handlerSingleCustomLiveMessage(event);
        } else {//??????
            handlerGroupCustomLiveMessage(event);
        }
    }

    //????????????????????????????????????
    private void handlerGroupCustomLiveMessage(LiveMessageBean event) {
        LogUtil.d("???????????? ----------------------------------- ");
        LogUtil.d("???????????? " + event.getCloudCustomData());
        LogUtil.d("???????????? " + event.getCustomElemData());
        LogUtil.d("???????????? ----------------------------------- ");
        if (!event.getMessage().getGroupID().equals(mRoomId)) {
            return;
        }
        LiveMessageCustomDataBean liveMessageCustomDataBean = GsonUtil.GsonToBean(event.getCustomElemData(), LiveMessageCustomDataBean.class);
        if (liveMessageCustomDataBean == null && TextUtil.isEmpty(liveMessageCustomDataBean.getCommand())) {
            return;
        }
        int cmd = Integer.parseInt(liveMessageCustomDataBean.getCommand());
        switch (cmd) {
            case LiveMessageHelper.IMCMD_PRAISE:
                handlerPraiseMessage();
                break;
            case LiveMessageHelper.IMCMD_DANMU:
                handlerDanmuMessage(event.getMessage(), liveMessageCustomDataBean.getMessage());
                break;
            case LiveMessageHelper.IMCMD_GIFT:
                handlerGiftMessage(liveMessageCustomDataBean.getMessage());
                break;
            case LiveMessageHelper.IMCMD_CUSTOM:
                handlerLiveMessage(event);
                break;
            case LiveMessageHelper.IMCMD_ANNOUNCEMENT:
                handlerLiveAnnouncement(liveMessageCustomDataBean);
                break;
            case LiveMessageHelper.IMCMD_DANMU_NEW:
                handlerNewDanmuMessage(event, liveMessageCustomDataBean);
                break;
        }
    }


    //?????????????????????????????????
    private void handlerSingleCustomLiveMessage(LiveMessageBean event) {
        LogUtil.d("???????????? ----------------------------------- ");
        LogUtil.d("???????????? " + event.getCloudCustomData());
        LogUtil.d("???????????? " + event.getCustomElemData());
        LogUtil.d("???????????? ----------------------------------- ");
    }

    /**
     * ??????????????????
     */
    void handlerPraiseMessage() {
        heartLayout.addFavor();
    }


    /**
     * ??????????????????
     */
    public void handlerDanmuMessage(V2TIMMessage v2TIMMessage, String text) {
        // updateIMMessageList(LiveMessageHelper.buildNormalEntity(v2TIMMessage.getUserID(), v2TIMMessage.getNickName(), text));
        if (mDanmakuManager != null) {
            mDanmakuManager.addDanmu(v2TIMMessage.getFaceUrl(), v2TIMMessage.getNickName(), text);
        }
    }

    /**
     * ??????????????????-??????????????????
     */
    private void handlerGiftMessage(final String message) {
        GiftMessage giftMessage = GsonUtil.GsonToBean(message, GiftMessage.class);
        if (giftMessage != null && giftMessage.costomMassageType.equals("sendGiftAvChatRoom")) {
            GiftMessage.ExtraInfo extraInfo = giftMessage.extra;
            if (extraInfo != null) {
                GiftInfo giftInfo = new GiftInfo();
                giftInfo.sendUserHeadIcon = extraInfo.head_pic;
                giftInfo.sendUser = extraInfo.nickname;
                giftInfo.count = !TextUtils.isEmpty(extraInfo.gift_count) ? Integer.valueOf(extraInfo.gift_count) : 0;
                giftInfo.title = !TextUtils.isEmpty(extraInfo.content) ? extraInfo.content : "";
                String giftLottieStatus = !TextUtils.isEmpty(extraInfo.gift_lottie_status) ? extraInfo.gift_lottie_status : "";
                giftInfo.giftLottieStatus = Integer.valueOf(giftLottieStatus);
                giftInfo.lottieUrl = !TextUtils.isEmpty(extraInfo.gift_lottieurl) ? extraInfo.gift_lottieurl : "";
                giftInfo.svgaUrl = extraInfo.gift_svgaurl;
                mGiftAnimatorLayout.show(giftInfo);
                mLayoutTopToolBar.updateCurrentBeansCount(extraInfo.beans_current_count);
                if (MyApp.uid.equals(giftMessage.extra.uid)) {
                    if (giftPanelView != null && giftPanelView.isShowing()) {
                        giftPanelView.refreshUserLevelProgress();
                        giftPanelView.refreshBalance(extraInfo.rich_beans);
                    }
                }
            }
        }
    }

    String liveTitle;
    String livePoster;

    /**
     * ???????????????????????????
     */
    void handlerLiveMessage(LiveMessageBean event) {
        //??????????????????????????????
        if (!TextUtil.isEmpty(event.getCloudCustomData()) && event.getCloudCustomData().contains("costomMassageType")) {
            CloudCustomDataBean cloudCustomDataBean = GsonUtil.GsonToBean(event.getCloudCustomData(), CloudCustomDataBean.class);
            if (cloudCustomDataBean == null || TextUtil.isEmpty(cloudCustomDataBean.getCostomMassageType())) {
                return;
            }
            LiveMessageCustomDataBean liveMessageCustomDataBean = GsonUtil.GsonToBean(event.getCustomElemData(), LiveMessageCustomDataBean.class);
            if (liveMessageCustomDataBean == null) {
                return;
            }
            String messageType = cloudCustomDataBean.getCostomMassageType();
            switch (messageType) {
                case LiveRoomMessageType.PROMPT_MSG:
                    CustomMessage timCustomRoom = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
                    if (!TextUtil.isEmpty(cloudCustomDataBean.getTouid())) {
                        String touid = cloudCustomDataBean.getTouid();
                        if (TextUtils.isEmpty(touid) || MyApp.uid.equals(touid)) { //????????????
                            updateIMMessageList(IMUtils.handlePrompt(timCustomRoom, event.getMessage()));
                        }
                    }
                    break;
                case LiveRoomMessageType.CREATE_ROOM:
                    LiveCreateAvChatRoom liveCreateAvChatRoom = GsonUtil.GsonToBean(event.getCloudCustomData(), LiveCreateAvChatRoom.class);
                    mLayoutTopToolBar.updateContestNo(liveCreateAvChatRoom.getAnchor_info().getHour_ranking());
                    mLayoutTopToolBar.startDurationTime(liveCreateAvChatRoom.getTime());
                    mStartTime = System.currentTimeMillis();
                    name_color = liveCreateAvChatRoom.getName_color();
                    liveTitle = liveCreateAvChatRoom.getAnchor_info().getLive_title();
                    livePoster = liveCreateAvChatRoom.getAnchor_info().getLive_poster();
                    anchorLevel = liveCreateAvChatRoom.getAnchor_level();
                    audienceLevel = liveCreateAvChatRoom.getUser_level();
                    break;
                case LiveRoomMessageType.JOIN_ROOM_MSG:
                    CustomMessage joinMsg = com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
                    LiveJoinAvChatRoom liveJoinAvChatRoom = com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil.GsonToBean(event.getCloudCustomData(), LiveJoinAvChatRoom.class);
                    String nickname = !TextUtils.isEmpty(event.getMessage().getNickName()) ? event.getMessage().getNickName() : event.getMessage().getSender();
                    String sendId = !TextUtils.isEmpty(event.getMessage().getSender()) ? event.getMessage().getSender() : event.getMessage().getSender();
                    //updateIMMessageListStartAnim(IMUtils.joinRoom(joinMsg,nickname,sendId));
                    // showJoinInfo(IMUtils.joinRoom(joinMsg, nickname, sendId));
                    updateIMMessageList(IMUtils.joinRoom(joinMsg,liveJoinAvChatRoom, nickname, sendId));
                    if (event.getMessage().getSender().equals(ProfileManager.getInstance().getUserModel().userId)) {
                        name_color = joinMsg.getName_color();
                    }
                    break;
                case LiveRoomMessageType.CHANGE_ROOM:
                    LiveChangeInfoChatRoom liveChangeInfoChatRoom = com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil.GsonToBean(event.getCloudCustomData(), LiveChangeInfoChatRoom.class);
                    switch (liveChangeInfoChatRoom.getActionType()) {
                        case "1":
                            mLayoutTopToolBar.updateCurrentBeansCount(liveChangeInfoChatRoom.getBeans_current_count());
                            mLocalBeans = Long.parseLong(liveChangeInfoChatRoom.getBeans_current_count());
                            anchorLevel = liveChangeInfoChatRoom.getAnchor_level();
                            audienceLevel = liveChangeInfoChatRoom.getUser_level();
                            break;
                        case "2":
                            mLayoutTopToolBar.updateContestNo(liveChangeInfoChatRoom.getHour_ranking());
                            break;
                        case "3":
                            mLayoutTopToolBar.updateOnlineNum(liveChangeInfoChatRoom.getReal_watchsum());
                            break;
                        case "5":
                            break;
                        case "6":
                            anchorLevel = liveChangeInfoChatRoom.getAnchor_level();
                            audienceLevel = liveChangeInfoChatRoom.getUser_level();
                            break;
                        case "7":
                            refreshLiveInfo(liveChangeInfoChatRoom.getLive_title(), liveChangeInfoChatRoom.getLive_poster());
                            if (liveTitle.equals(mLayoutTopToolBar.getAnchorInfo().userName)) {
                                mLayoutTopToolBar.refreshLiveNoticeTitle("");
                            } else {
                                mLayoutTopToolBar.refreshLiveNoticeTitle(liveTitle);
                            }
                            break;
                    }
                    break;
                case LiveRoomMessageType.WATCH_ROOM:
                    LiveWatchAvChatRoom watchAvChatRoom = com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil.GsonToBean(event.getCloudCustomData(), LiveWatchAvChatRoom.class);
                    mLayoutTopToolBar.updateAudienceNumber(watchAvChatRoom.getWatchsum());
                    updateTopAudienceInfoFromPlatform(watchAvChatRoom.getWatchuser(), watchAvChatRoom.getTop_gift_uid());
                    mTotalMemberCount = watchAvChatRoom.getWatchsum();
                    break;
                case LiveRoomMessageType.LIVE_RED_ENVELOPES_CHANGE:
                    String actionType = cloudCustomDataBean.getActionType();
                    if ("0".equals(actionType)) {
                        getLiveRedBagList(mRoomId);
                        String showType = cloudCustomDataBean.getShowType();
                        if (!"1".equals(showType)) {
                            CustomMessage customMessage = com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
                            String enNickname = !TextUtils.isEmpty(event.getMessage().getNickName()) ? event.getMessage().getNickName() : event.getMessage().getSender();
                            String enSendId = !TextUtils.isEmpty(event.getMessage().getSender()) ? event.getMessage().getSender() : event.getMessage().getSender();
                            updateIMMessageList(handleCustomTextMsg(enNickname, enSendId, customMessage));
                        }
                    }
                    break;
                case LiveRoomMessageType.ROOM_STATE_PK:
                    PkChatRoomBean pkChatRoomBean = GsonUtil.GsonToBean(event.getCloudCustomData(), PkChatRoomBean.class);
                    if ("0".equals(pkChatRoomBean.getActionType())) {
                        if (pkChatRoomBean.getIs_reset().equals("1")) {
                            layoutPkView.clearPkStatus();
                            layoutPkView.initPkData(pkChatRoomBean.getPkInfo());
                        } else {
                            layoutPkView.initPkData(pkChatRoomBean.getPkInfo());
                        }
                    }
                    break;
                case LiveRoomMessageType.RETRY_INVITE_PK://pk????????????
                    String actionType2 = cloudCustomDataBean.getActionType();
                    if ("0".equals(actionType2)) {
                        showPkAgainTip();
                    } else if ("1".equals(actionType2)) {
                        ToastUtil.show(QNLiveRoomAnchorActivity.this,"??????????????????pk");
                    }
                break;
            }
        }
        //????????????????????????
        else if (TextUtil.isEmpty(event.getCloudCustomData()) && !TextUtil.isEmpty(event.getCustomElemData())) {
            LiveMessageCustomDataBean liveMessageCustomDataBean = GsonUtil.GsonToBean(event.getCustomElemData(), LiveMessageCustomDataBean.class);
            CustomMessage customMessage = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
            String nickname = !TextUtils.isEmpty(event.getMessage().getNickName()) ? event.getMessage().getNickName() : event.getMessage().getSender();
            String sendId = !TextUtils.isEmpty(event.getMessage().getSender()) ? event.getMessage().getSender() : event.getMessage().getSender();
            updateIMMessageList(handleCustomTextMsg(nickname, sendId, customMessage));
        }
    }

    void handlerNewDanmuMessage(LiveMessageBean event, LiveMessageCustomDataBean liveMessageCustomDataBean) {
        CustomMessage customMessage = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
        String nickname = !TextUtils.isEmpty(event.getMessage().getNickName()) ? event.getMessage().getNickName() : event.getMessage().getSender();
        String sendId = !TextUtils.isEmpty(event.getMessage().getSender()) ? event.getMessage().getSender() : event.getMessage().getSender();
        updateIMMessageList(handleCustomTextMsg(nickname, sendId, customMessage));
    }

    void handlerLiveAnnouncement(LiveMessageCustomDataBean liveMessageCustomDataBean) {
        LiveAnnouncementBean.MessageBean messageBean = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), LiveAnnouncementBean.MessageBean.class);
        mLayoutTopToolBar.showLiveAnnouncement(messageBean);
    }

    /** -----------------------------------------------------------------*/


    /**
     * ----------------------------------------------------
     * ?????????ui????????????
     */
    String anchorLevel; //?????????????????????
    String audienceLevel;   //??????????????????
    String is_group_admin; //?????????????????????
    String is_admin; //????????????v
    String name_color; //????????????
    String isNoTalking = "0"; //???????????????
    String mAnchorId = MyApp.uid;

    private void initLiveRoomView() {
        groupLive.setVisibility(View.VISIBLE);
        initBottomToolBar();
        initTopToolBar();
        initRemoteStreamView();
    }

    private void initBottomToolBar() {
        mLayoutBottomToolBar.setVisibility(View.VISIBLE);
        mLayoutBottomToolBar.setOnTextSendListener(new InputTextMsgDialog.OnTextSendDelegate() {
            @Override
            public void onTextSend(final String msg, final boolean tanmuOpen) {
                //??????????????????
                V2TIMManager.getInstance().getUsersInfo(Arrays.asList(V2TIMManager.getInstance().getLoginUser()), new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
                    @Override
                    public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                        V2TIMUserFullInfo v2TIMUserFullInfo = v2TIMUserFullInfos.get(0);
                        final ChatEntity entity = new ChatEntity();
                        String anchorUserName = !TextUtils.isEmpty(v2TIMUserFullInfo.getNickName()) ? v2TIMUserFullInfo.getNickName() : "";
                        entity.setSenderName(anchorUserName);
                        entity.setContent(msg);
                        entity.setLevel(-1);
                        entity.setType(com.tencent.qcloud.tim.tuikit.live.base.Constants.TEXT_TYPE);
                        entity.setUid(V2TIMManager.getInstance().getLoginUser());
                        //????????????/vip??????
                        String admin = !TextUtils.isEmpty(is_group_admin) ? is_group_admin : "0";
                        entity.setIs_group_admin(admin);
                        entity.setLive_group_role(IMUtils.getGroupRole(admin, mAnchorId));
                        entity.setLive_user_role(String.valueOf(TUIKitLive.getLoginUserInfo().getRole()));
                        entity.setWealth_val("");
                        entity.setWealth_val_switch("");
                        entity.setUser_level(audienceLevel);
                        entity.setAnchor_level(anchorLevel);
                        entity.setName_color(name_color);

                        if (tanmuOpen) { //????????????
                            sendDanmaku(msg);
                        } else { //????????????
                            if (!TextUtils.isEmpty(isNoTalking)) { //?????????????????? ????????????
                                if (("1").equals(isNoTalking)) {//?????????
                                    ToastUtil.show(QNLiveRoomAnchorActivity.this, "?????????????????????????????????????????????");
                                } else if (("0").equals(isNoTalking)) {//?????????
                                    LiveMessageHelper.getInstance().sendLiveMessage(mRoomId, entity, new LiveMessageHelper.OnMessageCallback() {
                                        @Override
                                        public void onSendSuc() {
                                            updateIMMessageList(entity);
                                        }

                                        @Override
                                        public void onSendFail(String msg) {
                                            ToastUtil.show(QNLiveRoomAnchorActivity.this, msg);
                                        }
                                    });
//                                    IMUtils.sendText(mLiveRoom, entity, new IMUtils.OnTextSendListener() {
//                                        @Override
//                                        public void sendSuc() {
//                                            resumeInteractTime();
//                                            updateIMMessageList(entity);
//                                        }
//                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(int code, String desc) {

                    }
                });
            }
        });

        updateBottomFunctionLayout();
    }

    CircleImageView mButtonInvitationPk;

    void updateBottomFunctionLayout() {

        // ?????????PK??????
        mButtonInvitationPk = new CircleImageView(mContext);
        mButtonInvitationPk.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_pk_btn_icon);
        mButtonInvitationPk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPkStatus) {
                    showStopPkTips();
                } else {
                    getPkList();
                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    requestPermissions(PermissionUtils.getLivePermissions(), new BaseFragment.OnPermissionGrandCallback() {
//                        @Override
//                        public void onAllPermissionsGrand() {
//
//                        }
//                    });
//                } else {
//                    getPkList();
//                }
            }
        });
        mButtonInvitationPk.setVisibility(com.tencent.qcloud.tim.tuikit.live.base.Config.getPKButtonStatus() ? View.VISIBLE : View.GONE);

        // ?????????????????????
        CircleImageView btnMusic = new CircleImageView(mContext);
        btnMusic.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_ic_music);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mAnchorAudioPanel != null) {
//                    mAnchorAudioPanel.show();
//                }
            }
        });

        // ?????????????????????
        CircleImageView buttonShare = new CircleImageView(mContext);
        //buttonShare.setId(mRoomId);
        buttonShare.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.ic_live_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSharePanel();
            }
        });

        // ?????????????????????
        CircleImageView buttonMessage = new CircleImageView(mContext);
        // buttonMessage.setId(mRoomId);
        buttonMessage.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_message_btn_icon);
        buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConversationCard();
            }
        });


        // ?????????????????????
        CircleImageView buttonBeauty = new CircleImageView(mContext);
        buttonBeauty.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_ic_beauty);
        buttonBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mLayoutBeautyPanel != null) {
//                    mLayoutBeautyPanel.show();
//                }
//                if (tiPanelLayout != null) {
//                    tiPanelLayout.showBeautyPanel();
//                }
            }
        });

        // ?????????????????????
        CircleImageView buttonGift = new CircleImageView(mContext);
        // buttonGift.setId(mRoomId);
        buttonGift.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_gift_btn_icon);
        buttonGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGiftPanel();
            }
        });

        //?????????????????????
        CircleImageView buttonSetting = new CircleImageView(mContext);
        //buttonSetting.setId(mRoomId);
        buttonSetting.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_setting_btn_icon);
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLiveAnchorSettingPop();
            }
        });


        //??????
        CircleImageView buttonLottery = new CircleImageView(TUIKitLive.getAppContext());
        buttonLottery.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.ic_live_bottom_lottery_draw);
        buttonLottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLotteryDrawLayout();
            }
        });


        List<View> buttonList = new ArrayList<>();
        buttonList.add(buttonShare);
        buttonList.add(mButtonInvitationPk);
        buttonList.add(buttonMessage);
        buttonList.add(buttonGift);
        buttonList.add(buttonLottery);
        buttonList.add(buttonSetting);
        mLayoutBottomToolBar.setRightButtonsLayout(buttonList);
    }

    void initTopToolBar() {
        mLayoutTopToolBar.setVisibility(View.VISIBLE);
        getAnchorBaseInfo();
        mLayoutTopToolBar.setTopToolBarDelegate(new TopToolBarLayout.TopToolBarDelegate() {
            @Override
            public void onClickAnchorAvatar() {
                showLiveCardPop(MyApp.uid);
            }

            @Override
            public void onClickFollow(TRTCLiveRoomDef.LiveAnchorInfo liveAnchorInfo) {

            }

            @Override
            public void onClickAudience(TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo) {
                showAudienceCard(audienceInfo.userId);
            }

            @Override
            public void onClickOnlineNum() {

            }

            @Override
            public void onClickOnlineUser() {
                showOnLineUserPop();
            }

            @Override
            public void onClickTopRank() {
                gotoLiveRankPage();
            }

            @Override
            public void onClickClose() {
                preExitRoom();
            }
        });
        mLayoutTopToolBar.setOnTopToolBarListener(new TopToolBarLayout.OnTopToolBarListener() {
            @Override
            public void doRewardBeanClick() {
                showRewardRankPop();
            }

            @Override
            public void doHourRankClick() {
                showHourRankPop();
            }

            @Override
            public void doAnnouncementClick(LiveAnnouncementBean.MessageBean messageBean) {

            }

            @Override
            public void doLiveNoticeClick() {
                showCloseLiveNoticePop();
            }

            @Override
            public void doLiveRedEnvelopesClick(int index) {
                showRedEnvelopes(index);
            }

            @Override
            public void doLiveRedEnvelopesShowResult(int index) {
                showRedEnvelopesResult(index);
            }
        });
    }

    void initChatMessageLayout() {
        mLayoutChatMessage.setOnChatLayoutItemClickListener(new ChatLayout.OnChatLayoutItemClickListener() {
            @Override
            public void onChatUserItemClick(String uid) {
                showAudienceCard(uid);
            }
        });
    }

    void initRequestLinkLayout() {
        tvLiveRoomLinkRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkRequestUserPop();
            }
        });
    }

    //??????????????????
    void showLiveAnchorSettingPop() {
        LiveSettingStateBean liveSettingStateBean = new LiveSettingStateBean(isAudioMute, isVideoMute, isFrontCamera);
        LiveAnchorSettingPop liveAnchorSettingPop = new LiveAnchorSettingPop(mContext, liveSettingStateBean);
        liveAnchorSettingPop.showPopupWindow();
        liveAnchorSettingPop.setOnLiveSettingListener(new LiveAnchorSettingPop.OnLiveSettingListener() {
            @Override
            public void onChooseCameraFront() {
                doCameraFront();
            }

            @Override
            public void onChooseCameraBack() {
                doCameraBack();
            }

            @Override
            public void onChooseCameraClose() {
                doCameraClose();
            }

            @Override
            public void onChooseAudioOpen() {
                mEngine.muteLocalAudio(false);
                isAudioMute = false;
            }

            @Override
            public void onChooseAudioClose() {
                mEngine.muteLocalAudio(true);
                isAudioMute = true;
            }

            @Override
            public void onChooseAudioSetting() {
//                if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
//                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().doAudioSetting();
//                }
            }

            @Override
            public void onChooseBeauty() {
//                if (mLayoutTuiLiverRomAnchor != null && mLayoutTuiLiverRomAnchor.getLiveRomAnchor() != null) {
//                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().doVideoBeauty();
//                }
                if (tiPanel != null) {
                    tiPanel.showBeautyPanel();
                }
            }

            @Override
            public void onChooseManagerSpeak() {
//                if (getLiveAnchorFragment() != null) {
//                    getLiveAnchorFragment().doLiveManagerSpeak();
//                }
            }
        });
    }

    //??????????????????
    void showAudienceCard(String uid) {
        if (!TextUtils.isEmpty(uid)) {
            TimCustomMessage timRoomMessageBean = new TimCustomMessage();
            timRoomMessageBean.setUid(uid);
            timRoomMessageBean.setRoomId(mRoomId);
            timRoomMessageBean.setIs_group_admin(!TextUtils.isEmpty(is_group_admin) ? is_group_admin : "");
            timRoomMessageBean.setAnchor_uid(mAnchorId);
            final LiveRoomHeadPop headPop = new LiveRoomHeadPop(mContext, timRoomMessageBean, TAG_ANCHOR);
            headPop.showPopupWindow();
            headPop.setOnPopOperateListener(new LiveRoomHeadPop.OnPopOperateListener() {
                @Override
                public void onFollow(TextView tvAnchorFollow, int follow_state, String uid) {
                    LiveHttpRequest.followAnchor(tvAnchorFollow, mContext, uid, follow_state);
                }

                @Override
                public void onAt(String uid, String name) {
                    mLayoutBottomToolBar.onAt("@" + name);
                }

                @Override
                public void onReport(String mySelfId) {
                    Intent intent = new Intent(mContext, ReportActivity.class);
                    intent.putExtra("source_type", 1);
                    intent.putExtra("uid", mySelfId);
                    startActivity(intent);
                }
            });

        }
    }

    //??????????????????
    void showOnLineUserPop() {
        LiveOnlineUserPop liveOnlineUserPop = new LiveOnlineUserPop(mContext, MyApp.uid, 2);
        liveOnlineUserPop.showPopupWindow();
        liveOnlineUserPop.setOnLiveUserListener(new LiveOnlineUserPop.OnLiveUserListener() {
            @Override
            public void onLiveUserKitOut(@NotNull TimCustomMessage tcm) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("????????????????????????/?????????????????????ta?????????/?????????")
                        .setPositiveButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        HttpHelper.getInstance().kickOutUser(tcm.getUid(), new HttpCodeListener() {
                            @Override
                            public void onSuccess(String data) {
                                liveOnlineUserPop.refreshData();
                            }

                            @Override
                            public void onFail(int code, String msg) {
                                ToastUtil.show(mContext, msg);
                            }
                        });
                    }
                }).create().show();
            }

            @Override
            public void onLiveUserLink(@NotNull TimCustomMessage tcm) {
                inviteLinkMic(tcm.getUid());
            }

            @Override
            public void onLiveUserClick(@NotNull TimCustomMessage tcm) {
                showAudienceCard(tcm.getUid());
            }
        });

    }

    //???????????????
    void gotoLiveRankPage() {
        Intent intent = new Intent(mContext, LiveRankingActivity.class);
        startActivity(intent);
    }

    //???????????????????????????
    void showRewardRankPop() {
        GiftViewPagerPop pop = new GiftViewPagerPop(QNLiveRoomAnchorActivity.this, R.style.BottomFullDialog, mAnchorId);
        pop.show();
        Log.v("showRewardRankPop","222222");

    }

    //?????????????????????
    void showHourRankPop() {
        GiftRankingListPop pop = new GiftRankingListPop(QNLiveRoomAnchorActivity.this, R.style.BottomFullDialog, mAnchorId);
        pop.show();
        pop.setOnPopListener(new GiftRankingListPop.OnPopListener() {
            @Override
            public void doPopAnchorClick() {
                showGiftPanel();
            }

            @Override
            public void doPopTopAnchorClick(String uid, String roomId) {

            }
        });
    }

    LiveInfoPop liveInfoPop;

    //???????????????????????????
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
                liveTitle = title;
                livePoster = cover;
                if (isVideoMute) {
                    mEngine.pushCameraTrackWithImage(null);
                    doCameraClose();
                }
            }
        });
    }

    void showChoosePhoto() {
        final List<NormalMenuItem> normalMenuItemList = new ArrayList<>();
        normalMenuItemList.add(new NormalMenuItem(0, "???????????????"));
        final NormalMenuPopup menuPopup = new NormalMenuPopup(mContext, normalMenuItemList);
        menuPopup.showPopupWindow();
        menuPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                //requestPhotoPermission();
                picFlag = 1;
                tryToChoosePhone();
                menuPopup.dismiss();
            }
        });
    }

    //????????????????????????
    void getAnchorBaseInfo() {
        HttpHelper.getInstance().getAnchorInfo(mAnchorId, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                LiveRoomInfo liveRoomInfo = GsonUtil.GsonToBean(data, LiveRoomInfo.class);
                if (liveRoomInfo != null) {
                    updateTopToolBarFromPlatform(liveRoomInfo);
                }
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    //????????????toolBar
    private void updateTopToolBarFromPlatform(LiveRoomInfo liveRoomInfo) {
        if (liveRoomInfo == null) {
            return;
        }
        TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo = new TRTCLiveRoomDef.LiveAnchorInfo();
        if (liveRoomInfo.getData().getUid().equals(mAnchorId)) { //?????????????????????
            mAnchorInfo.userId = liveRoomInfo.getData().getUid();
            mAnchorInfo.userName = liveRoomInfo.getData().getNickname();
            mAnchorInfo.avatarUrl = liveRoomInfo.getData().getHead_pic();
            mAnchorInfo.sex = liveRoomInfo.getData().getSex();
            mAnchorInfo.type = liveRoomInfo.getData().getRole();
            mAnchorInfo.age = liveRoomInfo.getData().getAge();
            mAnchorInfo.watchNo = liveRoomInfo.getData().getWatchsum();
            mLayoutTopToolBar.setHasFollowed(true); //???????????????????????????????????????????????????
        }
        mLayoutTopToolBar.setAnchorInfo(mAnchorInfo); //??????????????????
        mLayoutTopToolBar.updateCurrentBeansCount(liveRoomInfo.getData().getBeans_current_count());
        mLayoutTopToolBar.updateLiveTitle(liveRoomInfo.getData().getLive_title());
    }

    //???????????????????????????
    public void updateTopAudienceInfoFromPlatform
    (List<String> mAnchorUserIdList, List<String> mTopIdList) {
        if (mAnchorUserIdList != null && mAnchorUserIdList.size() > 0) {
            updateTopAudienceList(mTopIdList);
            updateNormalAudienceList(mAnchorUserIdList);
        }
    }

    //????????????
    public void updateNormalAudienceList(List<String> userList) {
        mLayoutTopToolBar.addAudienceListUser(userList);
    }

    //top3??????
    public void updateTopAudienceList(List<String> userList) {
        mLayoutTopToolBar.setAudienceTopListUser(userList);
    }

    //???????????????????????????
    public void updateIMMessageList(ChatEntity entity) {
        mLayoutChatMessage.addMessageToList(entity);
    }

    //??????????????????
    public void reportChatMessage(String message) {
        HttpHelper.getInstance().reportLiveMessage(mAnchorId, mRoomId, message, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    //????????????
    public void sendDanmaku(String message) {
        HttpHelper.getInstance().sendBarrage(mAnchorId, message, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(mContext, "??????????????????");
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(mContext, msg);
            }
        });
    }



    //  ????????????
    public void showLotteryDrawLayout() {
        LiveLotteryDrawPop liveLotteryDrawPop = new LiveLotteryDrawPop(this);
        liveLotteryDrawPop.showPopupWindow();
    }


    GiftPanelViewImp giftPanelView;

    //??????????????????
    public void showGiftPanel() {
        String token = SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", "");
        String url = LiveHttpHelper.getInstance().liveGiftList(HttpUrl.liveGiftList);
        giftPanelView = new GiftPanelViewImp(mContext, url, token, MyApp.uid, mRoomId, true);
        ((GiftPanelViewImp) giftPanelView).setOnGiftPanelOperateListener(new GiftPanelViewImp.OnGiftPanelOperateListener() {
            @Override
            public void OnGiftPanel() {
                //sendLiveEvent("OnGiftPanel", "");
            }

            @Override
            public void OnGiftLevelClick() {
                gotoMyLevelPage();
            }
        });
        ((GiftPanelViewImp) giftPanelView).setOnCoinNumberChangeListener(new GiftPanelViewImp.OnCoinNumberChangeListener() {
            @Override
            public void onCoinNumberChanged(TextView giftCoin) {
                //tvCoin = giftCoin;
            }
        });
        GiftInfoDataHandler mGiftInfoDataHandler = new GiftInfoDataHandler();
        DefaultGiftAdapterImp mGiftAdapter = new DefaultGiftAdapterImp();
        giftPanelView.init(mGiftInfoDataHandler, mGiftAdapter);
        giftPanelView.setGiftPanelDelegate(new GiftPanelDelegate() {
            @Override
            public void onGiftItemClick(GiftInfo giftInfo) {
                sendGift(giftInfo);
            }

            @Override
            public void onChargeClick() {
                gotoCharge();
            }
        });
        giftPanelView.setOnNewGiftListener(new GiftPanelViewImp.OnNewGiftListener() {
            @Override
            public void OnClickRedEnvelop() {
                showSendRedEnvelopesPop();
            }
        });
        giftPanelView.show();
    }

    //????????????
    void sendGift(GiftInfo giftInfo) {
        HttpHelper.getInstance().sendGift(mAnchorId, giftInfo.giftId,
                String.valueOf(giftInfo.count), String.valueOf(giftInfo.presentType),
                new HttpCodeListener() {
                    @Override
                    public void onSuccess(String data) {
                        ToastUtil.show(mContext, "????????????");
                        //???????????????????????????
                        if (giftInfo.presentType == 2) {
                            int changeNum = Integer.valueOf(giftInfo.num) - giftInfo.count;
                            giftInfo.num = String.valueOf(changeNum);
                            giftPanelView.notifyGiftController(changeNum, giftInfo);
                        }
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        ToastUtil.show(mContext, msg);
                    }
                });
    }

    //???????????????
    void gotoCharge() {
        Intent intent = new Intent(mContext, MyPurseActivity.class);
        startActivity(intent);
    }

    //?????????????????????
    void gotoMyLevelPage() {
        Intent intent = new Intent(mContext, MyLiveLevelActivity.class);
        intent.putExtra("type", "1");
        startActivity(intent);
    }

    /**
     * -----------------------------------------------------
     */


    void doCameraFront() {
        mEngine.muteLocalVideo(false);
        mEngine.pushCameraTrackWithImage(null);
        isVideoMute = false;
        if (!isFrontCamera) {
            mEngine.switchCamera(new QNCameraSwitchResultCallback() {
                @Override
                public void onCameraSwitchDone(boolean b) {
                    isFrontCamera = true;
                }

                @Override
                public void onCameraSwitchError(String s) {
                    ToastUtil.show(QNLiveRoomAnchorActivity.this, s);
                }
            });
        }
    }

    void doCameraBack() {
        mEngine.muteLocalVideo(false);
        mEngine.pushCameraTrackWithImage(null);
        isVideoMute = false;
        if (isFrontCamera) {
            mEngine.switchCamera(new QNCameraSwitchResultCallback() {
                @Override
                public void onCameraSwitchDone(boolean b) {
                    isFrontCamera = false;
                }

                @Override
                public void onCameraSwitchError(String s) {
                    ToastUtil.show(QNLiveRoomAnchorActivity.this, s);
                }
            });
        }
    }

    void doCameraClose() {

        final View coverView = LayoutInflater.from(QNLiveRoomAnchorActivity.this).inflate(R.layout.layout_live_room_cover, null, false);
        final ImageView ivCover = coverView.findViewById(com.tencent.qcloud.tim.tuikit.live.R.id.iv_live_cover);

        Glide.with(mContext)
                .load(livePoster)
                .centerCrop()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        ivCover.setImageDrawable(resource);
                        Bitmap coverBitmap = createBitmap3(coverView,
                                Config.STREAMING_WIDTH, Config.STREAMING_HEIGHT
                                // mLocalVideoSurfaceView.getMeasuredWidth(),
                                //  mLocalVideoSurfaceView.getMeasuredHeight()
                                //getWindowManager().getDefaultDisplay().getWidth(),
                                // getWindowManager().getDefaultDisplay().getHeight()
                        );
                        QNImage qnImage = new QNImage(mContext);
                        qnImage.setImageSize(Config.STREAMING_WIDTH, Config.STREAMING_HEIGHT);
                        qnImage.setResourceBitmap(coverBitmap);
                        mEngine.pushCameraTrackWithImage(qnImage);
                        //  ivTest.setImageBitmap(coverBitmap);
                        //mEngine.muteLocalVideo(true);
                        isVideoMute = true;
                    }
                });
    }

    public Bitmap createBitmap3(View v, int width, int height) {
        //????????????view????????????
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth, measuredHeight);
        //??????layout??????????????????????????????view???????????????
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        v.draw(c);
        return bmp;
    }

    //??????????????????
    public void showSharePanel() {
//        SharedPop sharedPop = new SharedPop(this, mAnchorId, liveTitle, livePoster);
//        sharedPop.showAtLocation(mLayoutBottomToolBar, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        final WindowManager.LayoutParams[] params = {getWindow().getAttributes()};
//        //?????????Popupwindow????????????????????????
//        params[0].alpha = 0.7f;
//        getWindow().setAttributes(params[0]);
//        //??????Popupwindow??????????????????Popupwindow?????????????????????1f
//        sharedPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                params[0] = getWindow().getAttributes();
//                params[0].alpha = 1f;
//                getWindow().setAttributes(params[0]);
//            }
//        });
        String uid = mAnchorId;
        String nickname = liveTitle;
        String pic = livePoster;
        NormalShareBean normalShareBean = new NormalShareBean(4,uid,
                "????????????",
                nickname,
                nickname+"?????????????????????????????????????????????~",
                HttpUrl.SMaddress,
                pic);
        NormalSharePop normalSharePop = new NormalSharePop(this,normalShareBean,true);
        normalSharePop.showPopupWindow();
    }

    //??????????????????
    void showConversationCard() {
        QNConversationPop conversationPop = new QNConversationPop(mContext);
        conversationPop.showPopupWindow();
    }

    void initBeautyPanel() {
        tiPanel.init(TiSDKManager.getInstance());
        tiPanel.setOnPanelItemClickListener(new TiPanelLayout.OnPanelItemClickListener() {
            @Override
            public void onBeautyItemClick() {
                requestBeautyPermission();
            }
        });
    }

    void requestBeautyPermission() {
        HttpHelper.getInstance().reportBeautyPermission(new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(mContext, msg);
            }
        });
    }

    //????????????
    public void closeLiveRoom() {
        HttpHelper.getInstance().closeLive(mRoomId);
    }

    QNRemoteViewAdapter remoteViewAdapter;

    void initRemoteStreamView() {
        remoteStreamMap = new HashMap<>();
        remoteStreamList = new ArrayList<>();
        linkMicStreamList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setReverseLayout(true);
        remoteViewAdapter = new QNRemoteViewAdapter(mContext, linkMicStreamList);
        rvRemoteView.setLayoutManager(layoutManager);
        rvRemoteView.setAdapter(remoteViewAdapter);
        remoteViewAdapter.setOnRemoteViewListener(new QNRemoteViewAdapter.OnRemoteViewListener() {
            @Override
            public void doRemoteClose(int index) {
                // mEngine.kickOutUser(remoteStreamList.get(index).get(0).getUserId());
                HttpHelper.getInstance().kickOutUser(remoteStreamList.get(index).get(0).getUserId(), null);
            }

            @Override
            public void doRemoteClick(int index) {
                showAudienceCard(remoteStreamList.get(index).get(0).getUserId());
            }

            @Override
            public void doRemoteRender(QNSurfaceView surfaceView, QNTrackInfo qnTrackInfo) {
                mEngine.setRenderWindow(qnTrackInfo, surfaceView);
            }
        });
    }


    /**
     * -------------------------------------------------------------------------------
     * ????????????
     */
    NormalTipsPop invitePop;

    private void showLinkRequestPop(String inviteId, String userId, String reason) {
        invitePop = new NormalTipsPop.Builder(mContext)
                .setTitle("????????????")
                .setInfo(reason)
                .setCancelStr("??????")
                .setConfirmStr("??????")
                .build();
        invitePop.setOnPopClickListener(new NormalTipsPop.OnPopClickListener() {
            @Override
            public void cancelClick() {
                invitePop.dismiss();
                rejectLinkInvite(inviteId, "??????????????????");
            }

            @Override
            public void confirmClick() {
                invitePop.dismiss();
                acceptLinkInvite(inviteId);
            }
        });
        invitePop.showPopupWindow();
    }

    void inviteLinkMic(String uid) {
        if (remoteStreamList != null && remoteStreamList.size() >= getMaxLinkMicNum()) {
            ToastUtil.show(mContext, "??????????????????????????????????????????,????????????");
            return;
        }
        if (remoteStreamMap.containsKey(uid)) {
            ToastUtil.show(mContext, "?????????????????????,????????????");
            return;
        }

        if (inviteLinkUserList.contains(uid)) {
            ToastUtil.show(mContext, "????????????????????????????????????????????????");
            return;
        }

        V2TIMManager.getSignalingManager().invite(uid, IMProtocol.getJoinReqJsonStr("???????????????????????????"), true, null, 15, new V2TIMCallback() {
            @Override
            public void onSuccess() {
                ToastUtil.show(mContext, "????????????");
                inviteLinkUserList.add(uid);
            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.show(mContext, i + s);
            }
        });
    }

    void rejectLinkInvite(String inviteId, String reason) {
        String data = IMProtocol.getJoinRspJsonStr(reason);
        V2TIMManager.getSignalingManager().reject(inviteId, data, new V2TIMCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    void acceptLinkInvite(String inviteId) {
        String data = IMProtocol.getJoinRspJsonStr("????????????");
        V2TIMManager.getSignalingManager().accept(inviteId, data, null);
    }

    void onCancelRequestLink(String reason) {
        if (!TextUtil.isEmpty(reason)) {
            ToastUtil.show(mContext, reason);
        }
        if (invitePop != null && invitePop.isShowing()) {
            invitePop.dismiss();
        }
    }

    LinkRequestPop linkRequestPop;

    void showLinkRequestUserPop() {
        linkRequestPop = new LinkRequestPop(mContext, linkRequestUserList);
        linkRequestPop.showPopupWindow();
        linkRequestPop.setOnLinkPopListener(new LinkRequestPop.OnLinkPopListener() {
            @Override
            public void doLinkAgree(String uid) {
                String inviteId = requestLinkMap.get(uid);
                if (!TextUtil.isEmpty(inviteId)) {
                    acceptLinkInvite(inviteId);
                    removeLinkUser(uid);
                    refreshLinkUserListPop();
                    refreshLinkButtonState();
                }
            }

            @Override
            public void doLinkReject(String uid) {
                String inviteId = requestLinkMap.get(uid);
                if (!TextUtil.isEmpty(inviteId)) {
                    rejectLinkInvite(inviteId, "???????????????????????????");
                    removeLinkUser(uid);
                    refreshLinkUserListPop();
                    refreshLinkButtonState();
                }
            }
        });
    }

    void refreshLinkButtonState() {
        if (linkRequestUserList.isEmpty()) {
            tvLiveRoomLinkRequest.setVisibility(View.GONE);
        } else {
            tvLiveRoomLinkRequest.setVisibility(View.VISIBLE);
        }
    }


    void addLinkUser(String userId, String inviteId) {
        requestLinkMap.put(userId, inviteId);
        linkRequestUserList.add(userId);
    }

    void removeLinkUser(String userId) {
        requestLinkMap.remove(userId);
        linkRequestUserList.remove(userId);
    }

    void removeLinkUserByInviteId(String inviteId) {
        for (String userId : requestLinkMap.keySet()) {
            if (inviteId.equals(requestLinkMap.get(userId))) {
                removeLinkUser(userId);
                refreshLinkButtonState();
            }
        }
    }

    void refreshLinkUserListPop() {
        if (linkRequestPop != null && linkRequestPop.isShowing()) {
            linkRequestPop.refreshData(linkRequestUserList);
        }
    }

    public void refreshLiveInfo(String title, String imgUrl) {
        liveTitle = title;
        livePoster = imgUrl;
        changeCoverImg(livePoster);
    }

    public void changeCoverImg(String imgUrl) {
        livePoster = imgUrl;
        if (isVideoMute) {
//            if (mIsPkStatus) {
//                suitCover2(livePoster);
//            } else {
//                suitCover(livePoster);
//            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveLiveEvent(LiveMethodEvent event) {
        switch (event.getType()) {
            case LiveEventConstant.link_LIVE_USER:
                inviteLinkMic(event.getData());
                break;
        }
    }

    //??????????????????????????????
    boolean checkLinkNumLimit() {
        if (remoteStreamList == null) {
            return false;
        }
        int linkNum = 0;
        for (List<QNTrackInfo> trackInfoList : remoteStreamList) {
            if (trackInfoList.size() == 2) {
                linkNum++;
            }
        }
        return linkNum >= getMaxLinkMicNum();
    }

    int getMaxLinkMicNum() {
        return 3;
    }

    //??????????????????????????????????????????
    void updateLinkMicUserList() {
        Set<String> remoteUserIdSet = remoteStreamMap.keySet();
        StringBuilder linkMicUserList = new StringBuilder("");
        StringBuilder linkMicManagerList = new StringBuilder("");
        for (String uid : remoteUserIdSet) {
            if (remoteStreamMap.get(uid).size() == 2) {
                linkMicUserList.append(uid);
                linkMicUserList.append(",");
            } else if (remoteStreamMap.get(uid).size() == 1) {
                linkMicManagerList.append(uid);
                linkMicManagerList.append(",");
            }
        }
        String linkMicStr = linkMicUserList.toString().endsWith(",") ? linkMicUserList.substring(0, linkMicUserList.length() - 1) : linkMicUserList.toString();
        HttpHelper.getInstance().updateLiveLinkMicList(linkMicStr);
        String managerMicStr = linkMicManagerList.toString().endsWith(",") ? linkMicManagerList.substring(0, linkMicManagerList.length() - 1) : linkMicManagerList.toString();
        HttpHelper.getInstance().updateManagerLinkMicList(managerMicStr);
    }

    /**
     * -----------------------------------------------------------------------------
     */


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showTip(TimCustomMessage timCustomMessage) {
        showCaveat(timCustomMessage);
    }

    NormalTipsPop normalTipsPop;

    //???????????????
    private void showCaveat(TimCustomMessage timRoomMessageBean) {
        if (normalTipsPop == null) {
            normalTipsPop = new NormalTipsPop.Builder(QNLiveRoomAnchorActivity.this)
                    .setTitle("??????")
                    .setInfo(timRoomMessageBean.getContent())
                    .setCancelStr("")
                    .setConfirmStr("??????")
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

    void showSendRedEnvelopesPop() {
        SendLiveRedEnvelopesPop sendLiveRedEnvelopesPop = new SendLiveRedEnvelopesPop(mContext, mRoomId);
        sendLiveRedEnvelopesPop.showPopupWindow();
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
                refreshLiveRedEnvelop();
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
        if (isShow) {
            showLiveRedEnvelopesSign(redEnvelopesList.getFirst().getHead_pic(), redEnvelopesList.size());
        } else {
            hideLiveRedEnvelopesSign();
        }
    }

    public void showLiveRedEnvelopesSign(String url, int num) {
        mLayoutTopToolBar.showLiveRedEnvelopesView(url, num);
    }

    public void hideLiveRedEnvelopesSign() {
        mLayoutTopToolBar.hideLiveRedEnvelopesView();
    }

    void showCloseLiveNoticePop() {
        String title = "????????????????????????????";
        LiveNormalTipPanel liveNormalTipPanel = new LiveNormalTipPanel(mContext, title);
        liveNormalTipPanel.show();
        liveNormalTipPanel.setOnNormalTipListener(new LiveNormalTipPanel.OnNormalTipListener() {
            @Override
            public void doNormalLeftClick() {

            }

            @Override
            public void doNormalRightClick() {
                mLayoutTopToolBar.hideLiveNoticeView();
            }
        });
    }

    void showRedEnvelopes(int index) {
        showRedEnvelopesPop(mRoomId,index,false);
    }

    void showRedEnvelopesResult(int index) {
        showRedEnvelopesPop(mRoomId,index,true);
    }

    void showRedEnvelopesPop(String roomId,int index,boolean isReceive) {
        LiveRedEnvelopesPop redEnvelopesPop = new LiveRedEnvelopesPop(QNLiveRoomAnchorActivity.this, redEnvelopesList.get(index),isReceive);
        redEnvelopesPop.showPopupWindow();
        redEnvelopesPop.setOnLiveRedEnvelopes(new LiveRedEnvelopesPop.OnLiveRedEnvelopesListener() {
            @Override
            public void doLiveUserClick(String uid) {
                showAudienceCard(uid);
            }

            @Override
            public void refreshRedEnvelopes() {
                getLiveRedBagList(roomId);
            }
        });
    }

    /**
     * APP??????????????????????????????
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

    //???????????????????????????
    void onAnchorLeave() {
        Glide.with(getApplicationContext())
                .asBitmap()
                .load("http://image.aiwujie.com.cn/live_bg.png")
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        QNImage qnImage = new QNImage(mContext);
                        qnImage.setImageSize(Config.STREAMING_WIDTH, Config.STREAMING_HEIGHT);
                        qnImage.setResourceBitmap(resource);
                        mEngine.pushCameraTrackWithImage(qnImage);
                    }
                });

    }

    void onAnchorComeBack() {

    }

    void initLiveManagerLayout() {
        layoutLiveManager.setOnLiveManagerListener(new LiveManagerLayout.OnLiveManagerListener() {
            @Override
            public void doManagerClick(String uid) {
                // showAudienceCard(uid);
                showManagerTipPop(uid);
            }
        });
    }

    void showManagerTipPop(String uid) {
        boolean isMangerMute = false;
//        if (managerMuteMap == null) {
//            managerMuteMap = new HashMap<>();
//        } else {
//            if (managerMuteMap.containsKey(uid)) {
//                isMangerMute = managerMuteMap.get(uid);
//            }
//        }
        final LiveManagerTipPanel liveManagerTipPanel = new LiveManagerTipPanel(QNLiveRoomAnchorActivity.this, uid, isMangerMute);
        liveManagerTipPanel.setCanceledOnTouchOutside(true);
        liveManagerTipPanel.setTipListener(new LiveManagerTipPanel.OnManagerTipListener() {
            @Override
            public void onManagerMute(String uid, boolean isMute) {
//                mLiveRoom.muteRemoteAudio(uid, isMute);
////                managerMuteMap.put(uid, isMute);
////                if (isMute) {
////                    com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastShortMessage("??????????????????");
////                } else {
////                    com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastShortMessage("??????????????????");
////                }
                liveManagerTipPanel.dismiss();
            }

            @Override
            public void onManagerKick(String uid) {
                HttpHelper.getInstance().kickOutUser(uid, null);
            }
        });
        liveManagerTipPanel.show();
    }

    /**
     * ---------------------------------------
     * pk??????
     */

    void initPkViewLayout() {
        layoutPkView.setOnPkViewListener(new QNPkViewLayout.OnPkViewListener() {
            @Override
            public void doPkAudienceClick(String uid, String otherUid, boolean isUs) {
                showPkTopAudienceView(uid,otherUid,isUs);
            }

            @Override
            public void doPkOtherAnchorClick(String uid) {
                showAudienceCard(uid);
            }

            @Override
            public void onPkTimeOut(String uid) {
                reportCompletePk(uid);
            }

            @Override
            public void onPunishTimeOut(String uid) {
                reportStopPk(uid);
            }

            @Override
            public void onMuteOtherVoice(String uid, String name, boolean isMute) {

            }
        });
    }

    boolean isPkStatus = false;
    Map<String, String> requestPkMap;
    String mTargetPkRoomToken = "";
    void getPkList() {
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
                    getPkListDetail(roomIdList);
                }
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    void getPkListDetail(List<String> roomIdList) {
        mSelectMemberView.show();
        mSelectMemberView.refreshView();
        V2TIMManager.getGroupManager().getGroupsInfo(roomIdList, new V2TIMValueCallback<List<V2TIMGroupInfoResult>>() {
            @Override
            public void onSuccess(List<V2TIMGroupInfoResult> v2TIMGroupInfoResults) {
                List<SelectMemberView.MemberEntity> memberEntityList = new ArrayList<>();
                for (V2TIMGroupInfoResult info : v2TIMGroupInfoResults) {
                    //?????????????????? userId ??????????????????????????????
//                    if (info.roomId == mRoomId || TextUtils.isEmpty(info.ownerId)) {
//                        continue;
//                    }
                    SelectMemberView.MemberEntity memberEntity = new SelectMemberView.MemberEntity();
                    memberEntity.userId = info.getGroupInfo().getOwner();
                    memberEntity.userAvatar = info.getGroupInfo().getFaceUrl();
                    memberEntity.userName = info.getGroupInfo().getGroupName();
                    memberEntity.roomId = Integer.parseInt(info.getGroupInfo().getGroupID());
                    memberEntityList.add(memberEntity);
                }
                if (memberEntityList.size() == 0) {
                    mSelectMemberView.setTitle("?????????PK??????");
                } else {
                    mSelectMemberView.setTitle("PK??????");
                }
                mSelectMemberView.setList(memberEntityList);
            }

            @Override
            public void onError(int i, String s) {
                mSelectMemberView.setTitle("?????????PK??????");
            }
        });
    }

    void initSelectPkView() {
        mSelectMemberView = new SelectMemberView(QNLiveRoomAnchorActivity.this);
        mSelectMemberView.setOnSelectedCallback(new SelectMemberView.onSelectedCallback() {
            @Override
            public void onSelected(int seatIndex, SelectMemberView.MemberEntity memberEntity) {
                String reason = IMProtocol.getPKReqJsonStr(mRoomId, MyApp.uid);
                V2TIMManager.getSignalingManager().invite(memberEntity.userId, reason, true, null, 15, new V2TIMCallback() {
                    @Override
                    public void onSuccess() {
                        ToastUtil.show(mContext, "????????????");
                        //inviteLinkUserList.add(uid);
                    }

                    @Override
                    public void onError(int i, String s) {
                        ToastUtil.show(mContext, i + s);
                    }
                });
                mSelectMemberView.dismiss();
            }

            @Override
            public void onCancel() {
                mSelectMemberView.dismiss();
            }
        });
    }

    public void showPkInvitePop(String uid) {
        LivePkInvitedPop livePkInvitedPop = new LivePkInvitedPop(QNLiveRoomAnchorActivity.this, uid);
        livePkInvitedPop.showPopupWindow();
        livePkInvitedPop.setPkListener(new LivePkInvitedPop.OnPkInviteListener() {
            @Override
            public void doPkRefuse() {
                doLivePkRefuse(uid,ProfileManager.getInstance().getUserModel().userName + " ???????????????pk??????");
            }

            @Override
            public void doPkAccept() {
                doLivePkAccept(uid);
            }

            @Override
            public void doAutoPkRefuse(boolean autoRefuse) {
                doPkAutoRefuse(autoRefuse);
            }
        });
    }

    public void doLivePkRefuse(String uid,String reason) {
        //mLiveRoom.responseRoomPK(pkInviteId, false, TUIKitLive.getLoginUserInfo().getNickName() + "???????????????pk??????");
        //mGroupPkView.setVisibility(View.GONE);
        String data = IMProtocol.getPKRspJsonStr(reason,MyApp.uid);
        V2TIMManager.getSignalingManager().reject(requestLinkMap.get(uid),data,null);
    }

    public void doLivePkAccept(String uid) {
        if (getContext() == null) {
            // TUILiveLog.d(TAG, "getContext is null!");
            return;
        }

//        if (mIsLinkMicStatus) {
//            Toast.makeText(getContext(), com.tencent.qcloud.tim.tuikit.live.R.string.live_link_mic_status, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        mLiveRoom.responseRoomPK(pkInviteId, true, "");
//        reportStartPk(pkInviteId);
//        mPkAnchorId = uid;
//        //mGroupPkView.setVisibility(View.VISIBLE);
//        // mPkViewLayout.showPkView();
//        mBottomStopPkBtn.setVisibility(View.VISIBLE);
//        mIsPkStatus = true;
        String data = IMProtocol.getPKRspJsonStr(ProfileManager.getInstance().getUserModel().userName + " ?????????pk??????", MyApp.uid);
        V2TIMManager.getSignalingManager().accept(requestLinkMap.get(uid), data, new V2TIMCallback() {
            @Override
            public void onSuccess() {
                startPk(uid);
                showPkView();
                isPkStatus = true;
            }

            @Override
            public void onError(int i, String s) {

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
                        ToastUtil.show(QNLiveRoomAnchorActivity.this,msg);
                    }
                });
//                if (getLiveAnchorFragment() != null) {
//                    getLiveAnchorFragment().reportStopPkByQuit(uid);
//                }
            }
        });
    }

    boolean isAutoRefusePk = false;

    public void doPkAutoRefuse(boolean isAutoRefuse) {
        this.isAutoRefusePk = isAutoRefuse;
    }

    void showPkView() {
        layoutPkView.showPkView();
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mLocalVideoSurfaceView.getLayoutParams();
        layoutParams.width = 0;
        layoutParams.height = 0;
        layoutParams.dimensionRatio = "3:4";
        layoutParams.matchConstraintPercentWidth = 0.5f;
        layoutParams.bottomToTop = R.id.guide_line_center_line;
        layoutParams.leftToLeft = R.id.parent;
        mLocalVideoSurfaceView.setLayoutParams(layoutParams);
        viewPkOtherWindow.setVisibility(View.VISIBLE);
    }

    void hidePkView() {
        layoutPkView.clearPkStatus();
        layoutPkView.hidePkView();
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mLocalVideoSurfaceView.getLayoutParams();
        layoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT;
//        layoutParams.topToTop = R.id.parent;
//        layoutParams.leftToLeft = R.id.parent;
//        layoutParams.rightToRight = R.id.parent;
//        layoutParams.bottomToBottom = R.id.parent;
        mLocalVideoSurfaceView.setLayoutParams(layoutParams);
        viewPkOtherWindow.setVisibility(View.GONE);
    }

    void onStopPk() {
        isPkStatus = false;
        stopMergeJob();
        hidePkView();
        if (!TextUtil.isEmpty(mTargetPkRoomToken)) {
            mEngine.leaveRoom();
            mLayoutTopToolBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEngine.joinRoom(roomToken);
                }
            }, 1500);
        }

    }

    public void showStopPkTips() {
        PkStopTipPanel pkStopTipPanel = new PkStopTipPanel(QNLiveRoomAnchorActivity.this, "");
        pkStopTipPanel.setCanceledOnTouchOutside(true);
        pkStopTipPanel.setPkStopListener(new PkStopTipPanel.OnPkStopListener() {
            @Override
            public void onPkQuit() {
                reportStopPkByQuit("");
            }

            @Override
            public void onPkAgain() {
                if (layoutPkView.isLinkStatus()) { //????????????
                    retryPk();
                } else {
                    com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastShortMessage("pk?????????????????????????????????Pk");
                }
            }

        });
        pkStopTipPanel.show();
    }

    public void showPkAgainTip() {
        new AlertDialog.Builder(QNLiveRoomAnchorActivity.this)
                .setTitle("?????????????????????pk")
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastShortMessage("?????????");
                        responseAnchorPk(1);
                    }
                })
                .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastShortMessage("?????????");
                        responseAnchorPk(2);
                    }
                })
                .show();
    }


    void showPkTopAudienceView(String uid, String otherId, boolean isUs) {
        LivePkTopAudiencePop livePkTopAudiencePop = new LivePkTopAudiencePop(QNLiveRoomAnchorActivity.this, uid, otherId, isUs);
        livePkTopAudiencePop.showPopupWindow();
    }

    void reportCompletePk(String uid) {
        HttpHelper.getInstance().completePk(uid, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    //??????pk ???????????? ????????????
    public void reportStopPk(String uid) {
        //EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.STOP_PK, "", uid));
        //mPkViewLayout.clearPkStatus();
        layoutPkView.showLinkView();
    }

    //??????pk ????????????
    public void reportStopPkByQuit(String uid) {
       // EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.STOP_PK, "", uid));
        //mLiveRoom.quitRoomPK(null);
        layoutPkView.hidePkView();
        stopPk(uid);
        onStopPk();
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

    public void renderOtherAnchor(List<QNTrackInfo> list) {
        for (QNTrackInfo qnTrackInfo : list) {
            if (qnTrackInfo.isVideo()) {
                mPkAnchorId = qnTrackInfo.getUserId();
                mEngine.setRenderWindow(qnTrackInfo,viewPkOtherWindow);
            }
        }
    }
    String mPkAnchorId;
    void retryPk() {
        HttpHelper.getInstance().retryPk(mPkAnchorId, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                ToastUtil.show(QNLiveRoomAnchorActivity.this,"????????????");
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(QNLiveRoomAnchorActivity.this,msg);
            }
        });
    }

    void responseAnchorPk(int type) {
        HttpHelper.getInstance().responsePkAgain(type, mPkAnchorId, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {

            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    /**----------------------------------------------- */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveLiveEvent2(com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent event) {
        if (event.getType().equals(LiveEventConstant.SET_PLAY_BACK_TICKET)) {
            showSetTickPop();
        }
    }

    public void showSetTickPop() {
        LivePlayBackInfoPop livePlayBackInfoPop = new LivePlayBackInfoPop(QNLiveRoomAnchorActivity.this,"",false,0);
        livePlayBackInfoPop.showPopupWindow();
        livePlayBackInfoPop.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                finish();
            }
        });
    }
}
