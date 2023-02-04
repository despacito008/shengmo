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
    private DanmakuManager mDanmakuManager;          // 弹幕的管理类

    private volatile boolean mIsMicrophoneOn = true;
    private volatile boolean mIsSpeakerOn = true;

    private String mRoomId;
    private String mRoomToken;

    private QNRoomState mCurrentRoomState = QNRoomState.IDLE;

    private long mLocalBeans = 0;   // 本场收益
    private long mStartTime = 0;   // 主播开始直播时间
    private int mTotalMemberCount;

    private QNCaptureVideoCallback mCaptureVideoCallback = new QNCaptureVideoCallback() {
        @Override
        public int[] onCaptureOpened(List<Size> list, List<Integer> list1) {
            // 根据设备能力选择匹配的采集参数
            int wantSize = -1; // 选择的分辨率下标, -1 表示不做选择, 使用 QNRTCSetting 的设置
            int wantFps = -1;  // 选择的帧率下标, -1 表示不做选择, 使用 QNRTCSetting 的设置
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
//        // 开始视频采集
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
        LogUtil.d("页面 onStop");
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
     * 初始化 QNRTCEngine
     */
    private void initQNRTCEngine() {
        // 1. VideoPreviewFormat 和 VideoEncodeFormat 建议保持一致
        // 2. 如果远端连麦出现回声的现象，可以通过配置 setLowAudioSampleRateEnabled(true) 或者 setAEC3Enabled(true) 后再做进一步测试，并将设备信息反馈给七牛技术支持
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
//     * 初始化 QNRTCEngine
//     */
//    private void initQNRTCEngine() {
//        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
//        int videoWidth = preferences.getInt(Config.WIDTH, DEFAULT_RESOLUTION[1][0]);
//        int videoHeight = preferences.getInt(Config.HEIGHT, DEFAULT_RESOLUTION[1][1]);
//        int fps = preferences.getInt(Config.FPS, DEFAULT_FPS[1]);
//        boolean isHwCodec = preferences.getInt(Config.CODEC_MODE, Config.HW) == Config.HW;
//        int videoBitrate = preferences.getInt(Config.BITRATE, DEFAULT_BITRATE[1]);
//        /**
//         * 默认情况下，网络波动时 SDK 内部会降低帧率或者分辨率来保证带宽变化下的视频质量；
//         * 如果打开分辨率保持开关，则只会调整帧率来适应网络波动。
//         */
//        boolean isMaintainRes = preferences.getBoolean(Config.MAINTAIN_RES, false);
//
//        /**
//         * 如果您的使用场景需要双讲，建议按照默认设置，保持 QNRTCSetting#setLowAudioSampleRateEnabled
//         * 和 QNRTCSetting#setAEC3Enabled 为 true 以防止出现对讲回声
//         */
//        boolean isLowSampleRateEnabled = preferences.getInt(Config.SAMPLE_RATE, Config.HIGH_SAMPLE_RATE) == Config.LOW_SAMPLE_RATE;
//        boolean isAec3Enabled = preferences.getBoolean(Config.AEC3_ENABLE, true);
//        mCaptureMode = preferences.getInt(Config.CAPTURE_MODE, Config.CAMERA_CAPTURE);
//
//        /**
//         * VideoPreviewFormat 和 VideoEncodeFormat 建议保持一致
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
//                // 根据设备能力选择匹配的采集参数
//                int wantSize = -1; // 选择的分辨率下标, -1 表示不做选择, 使用 QNRTCSetting 的设置
//                int wantFps = -1;  // 选择的帧率下标, -1 表示不做选择, 使用 QNRTCSetting 的设置
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
//     * 初始化本地音视频 track
//     * 关于 Track 的概念介绍 https://doc.qnsdk.com/rtn/android/docs/preparation#5
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
//                // 可以向 extraData 填充自定义数据并在对端通过 QNRemoteAudioPacketCallback 解析
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
//                // 当需要根据自己的算法加密音频数据时，需要在该方法告知 SDK 加密后的最大数据大小
//                if (mEnableAudioEncrypt) {
//                    return frameSize + 10;
//                }
//                return 0;
//            }
//
//            @Override
//            public int onEncrypt(ByteBuffer frame, int frameSize, ByteBuffer encryptedFrame) {
//                // 自主加密接口，将加密后的数据放置到 encryptedFrame 中，并返回加密后大小
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
//                // 创建 Camera 采集的视频 Track
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
//                // 创建屏幕录制的视频 Track
////                createScreenTrack();
////                mControlFragment.setAudioOnly(true);
//                break;
//            case Config.MUTI_TRACK_CAPTURE:
//                // 视频通话 + 屏幕共享两路 track
//               // createScreenTrack();
//                mLocalVideoTrack = mEngine.createTrackInfoBuilder()
//                        .setSourceType(QNSourceType.VIDEO_CAMERA)
//                        .setTag("camera").create();
//                mLocalTrackList.add(mLocalVideoTrack);
//                break;
//        }
//    }

    /**
     * 初始化本地音视频 track
     * 关于 Track 的概念介绍 https://doc.qnsdk.com/rtn/android/docs/preparation#5
     */
    private void initLocalTrackInfoList() {
        mLocalTrackList = new ArrayList<>();
        // 创建本地音频 track
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
        // 创建 Camera 采集的视频 Track
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
//                    // PK 场景下需要创建合流任务并重新进行推流
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
//                ToastUtil.show(this, "连接中");
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
        //LogUtil.d("发布成功 " + list.size());
        // 预先对本地合流布局进行配置，以应对需要 PK 的场景
        // setMergeOptions(list, false);

        if (isPkStatus) {

        } else {
            mTargetPkRoomToken = "";
            createForwardJob();
        }
        mLocalTrackInfoList = list;
    }

    HashMap<String, List<QNTrackInfo>> remoteStreamMap;
    List<List<QNTrackInfo>> remoteStreamList; //远端所有流
    List<List<QNTrackInfo>> linkMicStreamList;  //连麦的流
    List<QNTrackInfo> mLocalTrackInfoList;

    //用户连麦推流
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
            } else if (list.size() == 1) { //场控只有音频
                if (remoteStreamMap.containsKey(remoteUserId)) {
                    remoteStreamList.remove(remoteStreamMap.get(remoteUserId));
                }
                remoteStreamMap.put(remoteUserId, list);
                remoteStreamList.add(list);
                layoutLiveManager.addManager(remoteUserId);
                //remoteViewAdapter.notifyDataSetChanged();
                refreshMergeOption();
                createMergeJob();
            } else if (list.size() == 2) { //普通连麦用户 && pk
                if (isPkStatus) { //pk模式
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
            if (remoteStreamMap.size() == 0) { //没有合流，开启单流转推
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
                if (remoteStreamMap.size() == 0) { //没有合流，开启单流转推
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
    public void onCreateMergeJobSuccess(String s) { //合流成功回调
        mMergeJobId = s;
        mEngine.setMergeStreamLayouts(mMergeTrackOptions, mMergeJobId);
        if (mForwardJob != null) {
            mEngine.stopForwardJob(mForwardJob.getForwardJobId());
            mForwardJob = null;
        }
        updateLinkMicUserList();
    }

    @Override
    public void onCreateForwardJobSuccess(String s) { //单流转推成功回调
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
     * 预览页相关
     */
    //预览页 - 设置监听
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

    //预览页 - 点击更换封面
    public static final String TAKE_PHOTO = "拍照";
    public static final String CHOOSE_PHOTO = "从相册中选择";

    private void showSelectPic() {
        new AlertView(null, null, "取消", null,
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

    //预览页 - 点击设置摄像头状态
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

    //预览页 - 开播
    private void beginToShow(final String live_title, final String live_poster) {
        String tid = mLayoutPreview.getChooseLabel();
        int isTicket = mLayoutPreview.getIsTicket();
        int ticketBean = mLayoutPreview.getTicketBeans();
        int isInteract = mLayoutPreview.getIsInteract();


        if (isTicket == 1 && ticketBean == 0) {
            ToastUtil.show(QNLiveRoomAnchorActivity.this, "开启门票房前请设置门票价格");
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
//                            //开启直播间消息通知
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
//                //轮询去调用，直到roomid唯一
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
     * 主播心跳相关
     */
    Timer mTimer;

    //开始上报主播心跳
    public void startHeaderReport() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                HttpHelper.getInstance().reportAnchorHeartBeat();
            }
        }, 100, 30000);
    }

    //停止上报主播心跳
    public void stopHeaderReport() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    /**
     * --------------------------------------------------------------------------------
     * 拍照上传相关
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

    //拍照
    public void tryToTakePhone() {
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
    public void tryToChoosePhone() {
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
            ToastUtil.show(getApplicationContext(), "授权失败,请开启相机权限");
            return;
        }
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(getApplicationContext(), "授权失败,请开启读写权限");
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
                                        ToastUtil.show(activity, "上传完成");
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
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)压缩文件
                        switch (picFlag) {
                            case 0:
                                //头像
                                RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), photo);
                                circleDrawable.getPaint().setAntiAlias(true);
                                circleDrawable.setCircular(true);
                                //mEditPersonmsgIcon.setImageDrawable(circleDrawable); //把图片显示在ImageView控件上

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
//            case 10000:
//                if (data != null) {
//                    ToastUtil.show(QNLiveRoomAnchorActivity.this, "图片上传中");
//                    //获取选择器返回的数据
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
                    ToastUtil.show(QNLiveRoomAnchorActivity.this, "获取主播信息失败,请重试");
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
     * 推流相关
     */

    QNForwardJob mForwardJob;
    int mSerialNum;

    //转推流
    private void createForwardJob() {
        if (mForwardJob == null) {
            mForwardJob = new QNForwardJob();
            mForwardJob.setForwardJobId(getForwardJobId());
            mForwardJob.setPublishUrl(getPublishUrl());
            mForwardJob.setAudioTrack(mLocalAudioTrack);
            mForwardJob.setVideoTrack(mLocalVideoTrack);
            mForwardJob.setInternalForward(true);
        }
        // ToastUtil.show(this, "转推成功" + mForwardJob.getPublishUrl());
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
        // 创建合流任务对象
        if (mQNMergeJob == null) {
            mQNMergeJob = new QNMergeJob();
        }
        // 设置合流任务 id，该 id 为合流任务的唯一标识符
        mQNMergeJob.setMergeJobId(getMergeJobId());
        // 设置合流任务的推流地址，该场景下需保持一致
        mQNMergeJob.setPublishUrl(getPublishUrl());
        mQNMergeJob.setWidth(Config.STREAMING_WIDTH);
        mQNMergeJob.setHeight(Config.STREAMING_HEIGHT);
        // QNMergeJob 中码率单位为 bps，所以，若期望码率为 1200kbps，则实际传入的参数值应为 1200 * 1000
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
        // 创建合流任务
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
                if (isPkStatus) { //pk合流转推
                    if (isRemote) { //对方主播
                        option.setX(Config.STREAMING_WIDTH / 2);
                        option.setY(Config.STREAMING_HEIGHT/2 - Config.STREAMING_WIDTH * 2 / 3);
                        option.setZ(0);
                        option.setWidth(Config.STREAMING_WIDTH / 2);
                        option.setHeight(Config.STREAMING_WIDTH * 2 / 3);
                    } else { //自己
                        option.setX(0);
                        option.setY(Config.STREAMING_HEIGHT/2 - Config.STREAMING_WIDTH * 2 / 3);
                        option.setZ(0);
                        option.setWidth(Config.STREAMING_WIDTH / 2);
                        option.setHeight(Config.STREAMING_WIDTH * 2 / 3);
                    }
                } else { //连麦合流转推
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


    //pk用
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
        mLayoutTopToolBar.removeCountDown();//停止倒计时
    }

    @Override
    public void onBackPressed() {
        preExitRoom();
    }


    boolean mIsEnterRoom = false;

    private void preExitRoom() {
        if (mIsEnterRoom) {
            showExitInfoDialog("当前正在直播，是否退出直播？", false);
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
     * 显示确认消息
     *
     * @param msg     消息内容
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
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
     * 显示直播结果的弹窗，如：观看数量、点赞数量、直播时长数
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
     * IM相关
     */

    //创建直播群
    public void createGroup() {
        V2TIMManager.getInstance().createGroup(V2TIMManager.GROUP_TYPE_AVCHATROOM, mRoomId, MyApp.uid + "的直播间", new V2TIMValueCallback<String>() {
            @Override
            public void onError(int code, String s) {
                String msg = s;
                if (code == 10036) {
                    msg = "您当前使用的云通讯账号未开通音视频聊天室功能，创建聊天室数量超过限额，请前往腾讯云官网开通【IM音视频聊天室】，地址：https://cloud.tencent.com/document/product/269/11673";
                }
                if (code == 10037) {
                    msg = "单个用户可创建和加入的群组数量超过了限制，请购买相关套餐,价格地址：https://cloud.tencent.com/document/product/269/11673";
                }
                if (code == 10038) {
                    msg = "群成员数量超过限制，请参考，请购买相关套餐，价格地址：https://cloud.tencent.com/document/product/269/11673";
                }
                if (code == 10025) {
                    // 10025 表明群主是自己，那么认为创建房间成功
                    onSuccess("success");
                } else {
                    ToastUtil.show(QNLiveRoomAnchorActivity.this, "创建群组失败");
                    finish();
                }
            }

            @Override
            public void onSuccess(String s) {
                //ToastUtil.show(QNLiveRoomAnchorActivity.this, "创建群组成功");
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
                // LogUtil.d("更新群资料成功");
            }

            @Override
            public void onError(int i, String s) {
                // LogUtil.d("更新群资料" + i + s);
            }
        });
    }

    //解散直播群
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

    List<String> linkRequestUserList; //连麦申请用户
    Map<String, String> requestLinkMap;
    List<String> inviteLinkUserList; //主播主动邀请的用户

    //群组监听
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
                    rejectLinkInvite(inviteId, "当前房间超出最大连麦人数限制,无法连麦");
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
                //主播端没有取消邀请 所以目前都是用户的取消申请
               // onCancelRequestLink(reason);
                removeLinkUser(userId);
                refreshLinkButtonState();
            }

            @Override
            public void onAcceptLinkRequest(String inviteId, String userId, String reason) {
                // ToastUtil.show(mContext, "onAcceptLinkRequest");
                if (!userId.equals(MyApp.uid)) { //用户同意连麦
                    inviteLinkUserList.remove(userId);
                }
            }

            @Override
            public void onRejectLinkRequest(String inviteId, String userId, String reason) {
                //ToastUtil.show(mContext, "onRejectLinkRequest");
                if (!userId.equals(MyApp.uid)) { //用户拒绝连麦
                    ToastUtil.show(mContext, reason);
                    inviteLinkUserList.remove(userId);
                }
            }

            @Override
            public void onTimeoutLinkRequest(String inviteId, String userId, String reason) {
                //ToastUtil.show(mContext,"onTimeoutLinkRequest");
                if (!TextUtil.isEmpty(userId)) {
                    if (userId.equals(MyApp.uid)) { //用户发的邀请超时
                        removeLinkUserByInviteId(inviteId);
                    } else { //主播发起的邀请超时
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
//                    doLivePkRefuse(userId,"主播正在pk中");
//                    return;
//                }
                if (isAutoRefusePk) {
                    doLivePkRefuse(userId,ProfileManager.getInstance().getUserModel().userName + " 拒绝了你的pk请求");
                    return;
                }


                showPkInvitePop(userId);
            }

            @Override
            public void onCancelPkRequest(String inviteId, String userId, String reason) {

            }

            @Override
            public void onAcceptPkRequest(String inviteId, String userId, String reason) {
                if (!userId.equals(MyApp.uid)) {  //主播同意pk请求
                    //1.关闭转推
                    if (mForwardJob != null) {
                        mEngine.stopForwardJob(mForwardJob.getForwardJobId());
                        mForwardJob = null;
                    }
                    isPkStatus = true;
                    //2.离开自己房间
                    mEngine.leaveRoom();
                    //3.加入对方的直播间

                    //LogUtil.d("room token = " + token);
                    // mEngine.switchRoom(token);
                    //4.合流转推pk
                    showPkView();
                }
            }

            @Override
            public void onRejectPkRequest(String inviteId, String userId, String reason) {
                if (!userId.equals(MyApp.uid)) { //主播拒绝pk
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

    //取消监听
    void unInitGroupListener() {
        TimLiveRoomHelper.getInstance().unitListener();
    }

    //收到直播间的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveLiveEvent(LiveMessageBean event) {
        if (TextUtils.isEmpty(event.getMessage().getGroupID())) { //单聊
            handlerSingleCustomLiveMessage(event);
        } else {//群聊
            handlerGroupCustomLiveMessage(event);
        }
    }

    //收到直播间相关的群聊消息
    private void handlerGroupCustomLiveMessage(LiveMessageBean event) {
        LogUtil.d("群聊消息 ----------------------------------- ");
        LogUtil.d("群聊消息 " + event.getCloudCustomData());
        LogUtil.d("群聊消息 " + event.getCustomElemData());
        LogUtil.d("群聊消息 ----------------------------------- ");
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


    //收到直播间相关单聊消息
    private void handlerSingleCustomLiveMessage(LiveMessageBean event) {
        LogUtil.d("单聊消息 ----------------------------------- ");
        LogUtil.d("单聊消息 " + event.getCloudCustomData());
        LogUtil.d("单聊消息 " + event.getCustomElemData());
        LogUtil.d("单聊消息 ----------------------------------- ");
    }

    /**
     * 处理点赞消息
     */
    void handlerPraiseMessage() {
        heartLayout.addFavor();
    }


    /**
     * 处理弹幕消息
     */
    public void handlerDanmuMessage(V2TIMMessage v2TIMMessage, String text) {
        // updateIMMessageList(LiveMessageHelper.buildNormalEntity(v2TIMMessage.getUserID(), v2TIMMessage.getNickName(), text));
        if (mDanmakuManager != null) {
            mDanmakuManager.addDanmu(v2TIMMessage.getFaceUrl(), v2TIMMessage.getNickName(), text);
        }
    }

    /**
     * 处理礼物消息-调用平台接口
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
     * 收到其他的业务消息
     */
    void handlerLiveMessage(LiveMessageBean event) {
        //服务器发的直播间消息
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
                        if (TextUtils.isEmpty(touid) || MyApp.uid.equals(touid)) { //自身显示
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
                case LiveRoomMessageType.RETRY_INVITE_PK://pk再来一局
                    String actionType2 = cloudCustomDataBean.getActionType();
                    if ("0".equals(actionType2)) {
                        showPkAgainTip();
                    } else if ("1".equals(actionType2)) {
                        ToastUtil.show(QNLiveRoomAnchorActivity.this,"对方拒绝再次pk");
                    }
                break;
            }
        }
        //用户发的文本消息
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
     * 直播间ui交互相关
     */
    String anchorLevel; //用户的主播等级
    String audienceLevel;   //用户观众等级
    String is_group_admin; //是否是本场场控
    String is_admin; //是否是黑v
    String name_color; //昵称颜色
    String isNoTalking = "0"; //是否被禁言
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
                //点击发送按钮
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
                        //增加场控/vip标识
                        String admin = !TextUtils.isEmpty(is_group_admin) ? is_group_admin : "0";
                        entity.setIs_group_admin(admin);
                        entity.setLive_group_role(IMUtils.getGroupRole(admin, mAnchorId));
                        entity.setLive_user_role(String.valueOf(TUIKitLive.getLoginUserInfo().getRole()));
                        entity.setWealth_val("");
                        entity.setWealth_val_switch("");
                        entity.setUser_level(audienceLevel);
                        entity.setAnchor_level(anchorLevel);
                        entity.setName_color(name_color);

                        if (tanmuOpen) { //弹幕消息
                            sendDanmaku(msg);
                        } else { //普通消息
                            if (!TextUtils.isEmpty(isNoTalking)) { //从服务器获取 禁言状态
                                if (("1").equals(isNoTalking)) {//已禁言
                                    ToastUtil.show(QNLiveRoomAnchorActivity.this, "您在直播间已禁言，无法发送消息");
                                } else if (("0").equals(isNoTalking)) {//未禁言
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

        // 初始化PK按钮
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

        // 初始化音乐按钮
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

        // 初始化分享按钮
        CircleImageView buttonShare = new CircleImageView(mContext);
        //buttonShare.setId(mRoomId);
        buttonShare.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.ic_live_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSharePanel();
            }
        });

        // 初始化消息按钮
        CircleImageView buttonMessage = new CircleImageView(mContext);
        // buttonMessage.setId(mRoomId);
        buttonMessage.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_message_btn_icon);
        buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConversationCard();
            }
        });


        // 初始化美颜按钮
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

        // 初始化礼物按钮
        CircleImageView buttonGift = new CircleImageView(mContext);
        // buttonGift.setId(mRoomId);
        buttonGift.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_gift_btn_icon);
        buttonGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGiftPanel();
            }
        });

        //初始化设置按钮
        CircleImageView buttonSetting = new CircleImageView(mContext);
        //buttonSetting.setId(mRoomId);
        buttonSetting.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_setting_btn_icon);
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLiveAnchorSettingPop();
            }
        });


        //抽奖
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

    //主播画面设置
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

    //显示用户卡片
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

    //显示在线列表
    void showOnLineUserPop() {
        LiveOnlineUserPop liveOnlineUserPop = new LiveOnlineUserPop(mContext, MyApp.uid, 2);
        liveOnlineUserPop.showPopupWindow();
        liveOnlineUserPop.setOnLiveUserListener(new LiveOnlineUserPop.OnLiveUserListener() {
            @Override
            public void onLiveUserKitOut(@NotNull TimCustomMessage tcm) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("当前用户正在连麦/发言，是否关闭ta的连麦/发言？")
                        .setPositiveButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("是", new DialogInterface.OnClickListener() {
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

    //去排行榜页
    void gotoLiveRankPage() {
        Intent intent = new Intent(mContext, LiveRankingActivity.class);
        startActivity(intent);
    }

    //显示主播粉丝贡献榜
    void showRewardRankPop() {
        GiftViewPagerPop pop = new GiftViewPagerPop(QNLiveRoomAnchorActivity.this, R.style.BottomFullDialog, mAnchorId);
        pop.show();
        Log.v("showRewardRankPop","222222");

    }

    //显示小时热度榜
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

    //显示主播信息编辑页
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
        normalMenuItemList.add(new NormalMenuItem(0, "从相册选择"));
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

    //获取主播基础信息
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

    //更新顶部toolBar
    private void updateTopToolBarFromPlatform(LiveRoomInfo liveRoomInfo) {
        if (liveRoomInfo == null) {
            return;
        }
        TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo = new TRTCLiveRoomDef.LiveAnchorInfo();
        if (liveRoomInfo.getData().getUid().equals(mAnchorId)) { //表明当前是主播
            mAnchorInfo.userId = liveRoomInfo.getData().getUid();
            mAnchorInfo.userName = liveRoomInfo.getData().getNickname();
            mAnchorInfo.avatarUrl = liveRoomInfo.getData().getHead_pic();
            mAnchorInfo.sex = liveRoomInfo.getData().getSex();
            mAnchorInfo.type = liveRoomInfo.getData().getRole();
            mAnchorInfo.age = liveRoomInfo.getData().getAge();
            mAnchorInfo.watchNo = liveRoomInfo.getData().getWatchsum();
            mLayoutTopToolBar.setHasFollowed(true); //主播自己不能关注自己，隐藏关注视图
        }
        mLayoutTopToolBar.setAnchorInfo(mAnchorInfo); //传递主播信息
        mLayoutTopToolBar.updateCurrentBeansCount(liveRoomInfo.getData().getBeans_current_count());
        mLayoutTopToolBar.updateLiveTitle(liveRoomInfo.getData().getLive_title());
    }

    //更新右上角观众信息
    public void updateTopAudienceInfoFromPlatform
    (List<String> mAnchorUserIdList, List<String> mTopIdList) {
        if (mAnchorUserIdList != null && mAnchorUserIdList.size() > 0) {
            updateTopAudienceList(mTopIdList);
            updateNormalAudienceList(mAnchorUserIdList);
        }
    }

    //普通观众
    public void updateNormalAudienceList(List<String> userList) {
        mLayoutTopToolBar.addAudienceListUser(userList);
    }

    //top3观众
    public void updateTopAudienceList(List<String> userList) {
        mLayoutTopToolBar.setAudienceTopListUser(userList);
    }

    //直播间消息添加信息
    public void updateIMMessageList(ChatEntity entity) {
        mLayoutChatMessage.addMessageToList(entity);
    }

    //上报聊天消息
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

    //发送弹幕
    public void sendDanmaku(String message) {
        HttpHelper.getInstance().sendBarrage(mAnchorId, message, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(mContext, "弹幕发射成功");
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(mContext, msg);
            }
        });
    }



    //  底部抽奖
    public void showLotteryDrawLayout() {
        LiveLotteryDrawPop liveLotteryDrawPop = new LiveLotteryDrawPop(this);
        liveLotteryDrawPop.showPopupWindow();
    }


    GiftPanelViewImp giftPanelView;

    //展示礼物面板
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

    //赠送礼物
    void sendGift(GiftInfo giftInfo) {
        HttpHelper.getInstance().sendGift(mAnchorId, giftInfo.giftId,
                String.valueOf(giftInfo.count), String.valueOf(giftInfo.presentType),
                new HttpCodeListener() {
                    @Override
                    public void onSuccess(String data) {
                        ToastUtil.show(mContext, "赠送成功");
                        //更新免费礼物的数量
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

    //前往充值页
    void gotoCharge() {
        Intent intent = new Intent(mContext, MyPurseActivity.class);
        startActivity(intent);
    }

    //前往等级详情页
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
        //测量使得view指定大小
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth, measuredHeight);
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        v.draw(c);
        return bmp;
    }

    //分享直播面板
    public void showSharePanel() {
//        SharedPop sharedPop = new SharedPop(this, mAnchorId, liveTitle, livePoster);
//        sharedPop.showAtLocation(mLayoutBottomToolBar, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
        String uid = mAnchorId;
        String nickname = liveTitle;
        String pic = livePoster;
        NormalShareBean normalShareBean = new NormalShareBean(4,uid,
                "直播推荐",
                nickname,
                nickname+"正在精彩直播中，快来圣魔观看吧~",
                HttpUrl.SMaddress,
                pic);
        NormalSharePop normalSharePop = new NormalSharePop(this,normalShareBean,true);
        normalSharePop.showPopupWindow();
    }

    //会话列表卡片
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

    //上报关播
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
     * 信令相关
     */
    NormalTipsPop invitePop;

    private void showLinkRequestPop(String inviteId, String userId, String reason) {
        invitePop = new NormalTipsPop.Builder(mContext)
                .setTitle("连麦请求")
                .setInfo(reason)
                .setCancelStr("拒绝")
                .setConfirmStr("同意")
                .build();
        invitePop.setOnPopClickListener(new NormalTipsPop.OnPopClickListener() {
            @Override
            public void cancelClick() {
                invitePop.dismiss();
                rejectLinkInvite(inviteId, "主播拒绝连麦");
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
            ToastUtil.show(mContext, "当前房间超出最大连麦人数限制,无法连麦");
            return;
        }
        if (remoteStreamMap.containsKey(uid)) {
            ToastUtil.show(mContext, "该用户已在连麦,无法邀请");
            return;
        }

        if (inviteLinkUserList.contains(uid)) {
            ToastUtil.show(mContext, "已向用户发出邀请，请等待用户回应");
            return;
        }

        V2TIMManager.getSignalingManager().invite(uid, IMProtocol.getJoinReqJsonStr("主播邀请你进行连麦"), true, null, 15, new V2TIMCallback() {
            @Override
            public void onSuccess() {
                ToastUtil.show(mContext, "邀请成功");
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
        String data = IMProtocol.getJoinRspJsonStr("同意连麦");
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
                    rejectLinkInvite(inviteId, "主播拒绝了连麦申请");
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

    //检查连麦人数是否上限
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

    //服务器上报连麦用户和场控用户
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

    //弹出警告框
    private void showCaveat(TimCustomMessage timRoomMessageBean) {
        if (normalTipsPop == null) {
            normalTipsPop = new NormalTipsPop.Builder(QNLiveRoomAnchorActivity.this)
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
        String title = "是否隐藏直播间公告?";
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

    //主播暂时离开直播间
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
////                    com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastShortMessage("静音场控成功");
////                } else {
////                    com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastShortMessage("取消静音场控");
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
     * pk相关
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
                    //过滤哪些没有 userId 的房间（主播不在线）
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
                    mSelectMemberView.setTitle("暂无可PK主播");
                } else {
                    mSelectMemberView.setTitle("PK列表");
                }
                mSelectMemberView.setList(memberEntityList);
            }

            @Override
            public void onError(int i, String s) {
                mSelectMemberView.setTitle("暂无可PK主播");
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
                        ToastUtil.show(mContext, "邀请成功");
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
                doLivePkRefuse(uid,ProfileManager.getInstance().getUserModel().userName + " 拒绝了你的pk请求");
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
        //mLiveRoom.responseRoomPK(pkInviteId, false, TUIKitLive.getLoginUserInfo().getNickName() + "拒绝了你的pk邀请");
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
        String data = IMProtocol.getPKRspJsonStr(ProfileManager.getInstance().getUserModel().userName + " 同意了pk邀请", MyApp.uid);
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
                if (layoutPkView.isLinkStatus()) { //连线状态
                    retryPk();
                } else {
                    com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastShortMessage("pk完成之后，才能再次发起Pk");
                }
            }

        });
        pkStopTipPanel.show();
    }

    public void showPkAgainTip() {
        new AlertDialog.Builder(QNLiveRoomAnchorActivity.this)
                .setTitle("主播邀请你再次pk")
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastShortMessage("已同意");
                        responseAnchorPk(1);
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastShortMessage("已拒绝");
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

    //停止pk 并且上报 开始连线
    public void reportStopPk(String uid) {
        //EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.STOP_PK, "", uid));
        //mPkViewLayout.clearPkStatus();
        layoutPkView.showLinkView();
    }

    //停止pk 并且上报
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
                ToastUtil.show(QNLiveRoomAnchorActivity.this,"邀请成功");
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
