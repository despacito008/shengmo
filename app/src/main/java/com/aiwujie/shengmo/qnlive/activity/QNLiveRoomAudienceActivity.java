package com.aiwujie.shengmo.qnlive.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.MainActivity;
import com.aiwujie.shengmo.activity.MyPurseActivity;
import com.aiwujie.shengmo.activity.ReportActivity;
import com.aiwujie.shengmo.activity.ranking.LiveRankingActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.base.BaseActivity;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.customview.SharedPop;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.bean.NormalMenuItem;
import com.aiwujie.shengmo.kt.bean.NormalShareBean;
import com.aiwujie.shengmo.kt.listener.OnSimplePopListener;
import com.aiwujie.shengmo.kt.ui.activity.HomePageActivity;
import com.aiwujie.shengmo.kt.ui.activity.MyLiveLevelActivity;
import com.aiwujie.shengmo.kt.ui.view.LiveAuTaskRankPop;
import com.aiwujie.shengmo.kt.ui.view.LiveAudienceTaskPop;
import com.aiwujie.shengmo.kt.ui.view.LiveBuyTicketPop;
import com.aiwujie.shengmo.kt.ui.view.LiveGiftRankPop;
import com.aiwujie.shengmo.kt.ui.view.LiveInfoPop;
import com.aiwujie.shengmo.kt.ui.view.LiveLinkMicUserPop;
import com.aiwujie.shengmo.kt.ui.view.LiveOnlineUserPop;
import com.aiwujie.shengmo.kt.ui.view.LivePkTopAudiencePop;
import com.aiwujie.shengmo.kt.ui.view.NormalMenuPopup;
import com.aiwujie.shengmo.kt.ui.view.NormalSharePop;
import com.aiwujie.shengmo.kt.ui.view.QNConversationPop;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.qnlive.bean.CloudCustomDataBean;
import com.aiwujie.shengmo.qnlive.ui.QNLiveRtcLayout;
import com.aiwujie.shengmo.qnlive.ui.QNPkViewLayout;
import com.aiwujie.shengmo.qnlive.utils.Config;
import com.aiwujie.shengmo.qnlive.utils.LiveMessageHelper;
import com.aiwujie.shengmo.qnlive.utils.LiveRoomMessageType;
import com.aiwujie.shengmo.qnlive.utils.PermissionChecker;
import com.aiwujie.shengmo.qnlive.utils.TimLiveRoomHelper;
import com.aiwujie.shengmo.qnlive.utils.ToastUtils;
import com.aiwujie.shengmo.timlive.kt.ui.view.LiveLotteryDrawPop;
import com.aiwujie.shengmo.timlive.ui.LiveRoomSwitchActivity;
import com.aiwujie.shengmo.timlive.view.GiftRankingListPop;
import com.aiwujie.shengmo.timlive.view.GiftViewPagerPop;
import com.aiwujie.shengmo.timlive.view.LiveAnchorSettingPop;
import com.aiwujie.shengmo.timlive.view.LiveRedEnvelopesPop;
import com.aiwujie.shengmo.timlive.view.LiveRoomHeadPop;
import com.aiwujie.shengmo.timlive.view.SendLiveRedEnvelopesPop;
import com.aiwujie.shengmo.utils.CropUtils;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.PhotoUploadUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.NormalTipsPop;
import com.aiwujie.shengmo.zdyview.CountDownButton;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnVideoSizeChangedListener;
import com.pili.pldroid.player.widget.PLVideoView;
import com.qiniu.droid.rtc.QNCameraSwitchResultCallback;
import com.qiniu.droid.rtc.QNCaptureVideoCallback;
import com.qiniu.droid.rtc.QNClientRole;
import com.qiniu.droid.rtc.QNCustomMessage;
import com.qiniu.droid.rtc.QNLocalAudioPacketCallback;
import com.qiniu.droid.rtc.QNMediaRelayState;
import com.qiniu.droid.rtc.QNRTCEngine;
import com.qiniu.droid.rtc.QNRTCEngineEventListener;
import com.qiniu.droid.rtc.QNRTCSetting;
import com.qiniu.droid.rtc.QNRoomState;
import com.qiniu.droid.rtc.QNSourceType;
import com.qiniu.droid.rtc.QNStatisticsReport;
import com.qiniu.droid.rtc.QNSurfaceView;
import com.qiniu.droid.rtc.QNTrackInfo;
import com.qiniu.droid.rtc.QNVideoFormat;
import com.qiniu.droid.rtc.model.QNAudioDevice;
import com.qiniu.droid.rtc.model.QNForwardJob;
import com.qiniu.droid.rtc.model.QNImage;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.custom.Constents;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.bean.LinkMicStateBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveAnnouncementBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveChangeInfoChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveCreateAvChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveJoinAvChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveManagerChangeBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveMessageBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveMessageCustomDataBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveRedEnvelopesBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveRoomInfo;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveSettingStateBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveUserData;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveWatchAvChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.NormalEventBean;
import com.tencent.qcloud.tim.tuikit.live.bean.OtherLiveRoomEvent;
import com.tencent.qcloud.tim.tuikit.live.bean.PkChatRoomBean;
import com.tencent.qcloud.tim.tuikit.live.bean.PkInfoBean;
import com.tencent.qcloud.tim.tuikit.live.bean.TimCustomMessage;
import com.tencent.qcloud.tim.tuikit.live.component.bottombar.BottomToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.component.common.CircleImageView;
import com.tencent.qcloud.tim.tuikit.live.component.danmaku.DanmakuManager;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.DefaultGiftAdapterImp;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftAnimatorLayout;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfoDataHandler;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftPanelViewImp;
import com.tencent.qcloud.tim.tuikit.live.component.input.InputTextMsgDialog;
import com.tencent.qcloud.tim.tuikit.live.component.like.HeartLayout;
import com.tencent.qcloud.tim.tuikit.live.component.like.LikeFrequencyControl;
import com.tencent.qcloud.tim.tuikit.live.component.link.ChooseLinkTypePanel;
import com.tencent.qcloud.tim.tuikit.live.component.manager.LiveManagerLayout;
import com.tencent.qcloud.tim.tuikit.live.component.message.ChatEntity;
import com.tencent.qcloud.tim.tuikit.live.component.message.ChatLayout;
import com.tencent.qcloud.tim.tuikit.live.component.other.LiveNormalTipPanel;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.TopToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.CustomMessage;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.GiftMessage;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.room.impl.IMProtocol;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.clearscreen.ClearScreenConstraintLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.clearscreen.SlideDirection;
import com.tencent.qcloud.tim.tuikit.live.utils.IMUtils;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Semaphore;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.tiui.TiPanelLayout;
import master.flame.danmaku.ui.widget.DanmakuView;
import pub.devrel.easypermissions.EasyPermissions;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;
import static com.aiwujie.shengmo.timlive.view.LiveRoomHeadPop.TAG_ANCHOR;
import static com.tencent.qcloud.tim.tuikit.live.utils.IMUtils.handleCustomTextMsg;

public class QNLiveRoomAudienceActivity extends BaseActivity implements QNRTCEngineEventListener, EasyPermissions.PermissionCallbacks {


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
    @BindView(R.id.playing_player_view)
    PLVideoView playingPlayerView;
    @BindView(R.id.heart_layout)
    HeartLayout heartLayout;
    @BindView(R.id.root_view)
    ClearScreenConstraintLayout mRootView;
    @BindView(R.id.lottie_animator_layout)
    GiftAnimatorLayout mGiftAnimatorLayout;
    @BindView(R.id.fl_video_view)
    FrameLayout flVideoView;
    @BindView(R.id.count_down_bt_ticket)
    CountDownButton countDownBtTicket;
    @BindView(R.id.ti_panel)
    TiPanelLayout tiPanel;
    @BindView(R.id.qn_live_ric_layout)
    QNLiveRtcLayout qnLiveRtcLayout;
    @BindView(R.id.layout_live_manager)
    LiveManagerLayout layoutLiveManager;
    @BindView(R.id.layout_pk_view)
    QNPkViewLayout layoutPkView;


    private Semaphore captureStoppedSem = new Semaphore(1);
    private boolean mStreamingStopped;
    QNRTCEngine mEngine;
    private List<QNTrackInfo> mLocalTrackList = null;
    private List<QNTrackInfo> mRemoteTrackList = null;

    private QNTrackInfo mLocalVideoTrack;
    private QNTrackInfo mLocalAudioTrack;

    //private QNSurfaceView mLocalVideoSurfaceView;
    private QNSurfaceView mRemoteVideoSurfaceView;

    private DanmakuManager mDanmakuManager;          // 弹幕的管理类

    private volatile boolean mIsMicrophoneOn = true;
    private volatile boolean mIsSpeakerOn = true;

    private String mRoomId;
    private String mRoomToken;
    private String mAnchorId;
    private String live_user_role;
    private int limitTime;


//    private Handler mHandler = new Handler(Looper.getMainLooper());
//    private Runnable mShowAnchorLeave = new Runnable() {      //如果一定时间内主播没出现
//        @Override
//        public void run() {
//           // mLayoutVideoManager.setAnchorOfflineViewVisibility(mIsAnchorEnter ? View.GONE : View.VISIBLE);
//           // mLayoutBottomToolBar.setVisibility(mIsAnchorEnter ? View.VISIBLE : View.GONE);
//           // mButtonReportUser.setVisibility(mIsAnchorEnter ? View.VISIBLE : View.GONE);
//        }
//    };


    private QNRoomState mCurrentRoomState = QNRoomState.IDLE;

    private QNCaptureVideoCallback mCaptureVideoCallback = new QNCaptureVideoCallback() {
        @Override
        public int[] onCaptureOpened(List<Size> list, List<Integer> list1) {
            return new int[0];
        }

        @Override
        public void onCaptureStarted() {
//            mByteDancePlugin.init(mEffectResourcePath);
//            // 由于切换到后台时会 destroy mByteDancePlugin，所以从后台切回来的时候需要先恢复特效
//            mByteDancePlugin.recoverEffects();
            //   updateProcessTypes();
        }

        @Override
        public void onRenderingFrame(VideoFrame.TextureBuffer textureBuffer, long timestampNs) {
//            if (mGLHandler == null) {
//                mGLHandler = new Handler();
//            }
//
//            if (mByteDancePlugin.isUsingEffect()) {
//                boolean isOES = textureBuffer.getType() == VideoFrame.TextureBuffer.Type.OES;
//                int newTexture = mByteDancePlugin.drawFrame(textureBuffer.getTextureId(), textureBuffer.getWidth(), textureBuffer.getHeight(), timestampNs, mProcessTypes, isOES);
//                if (newTexture != textureBuffer.getTextureId()) {
//                    textureBuffer.setType(VideoFrame.TextureBuffer.Type.RGB);
//                    textureBuffer.setTextureId(newTexture);
//                }
//            }
        }

        @Override
        public void onPreviewFrame(byte[] data, int width, int height, int rotation, int fmt, long timestampNs) {
//            if (mTextureRotation != rotation) {
//                mTextureRotation = rotation;
//                updateProcessTypes();
//            }
        }

        @Override
        public void onCaptureStopped() {
            //停止连麦或者切到后台时候会调用
            //  mByteDancePlugin.destroy();
            // mTextureRotation = 0;
            // mIsFrontCamera = true;
            captureStoppedSem.release();
        }
    };
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_qn_live_room_audience);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mDanmakuManager = new DanmakuManager(this);
        mDanmakuManager.setDanmakuView(viewDanmaku);
        mContext = QNLiveRoomAudienceActivity.this;
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
        }
        imgpre = SharedPreferencesUtils.geParam(this, "image_host", "");
        mRoomId = getIntent().getStringExtra("roomId");
        mAnchorId = getIntent().getStringExtra("anchorId");
        limitTime = getIntent().getIntExtra("limitTime", 0);
        if (!getIntent().getBooleanExtra("canRecord", false)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
        initQNRTCEngine();
        //initLocalTrackInfoList();
        initPlayer(getPlayUrl());
        initBottomToolBar();
        initTopToolBar();
        initClearScreenLayout();
        initTicketView();
        initBeautyPanel();
        initRTCLayout();
        initLiveManagerLayout();
        joinGroup();
        initPkViewLayout();
    }

    private void initClearScreenLayout() {
        mRootView.addClearViews(mLayoutChatMessage, mGiftAnimatorLayout);
        mRootView.setSlideDirection(SlideDirection.LEFT);
        mRootView.setOnOtherEventListener(new ClearScreenConstraintLayout.OnOtherEventListener() {
            @Override
            public void onLeftBack() {
                onBackPressed();
            }

            @Override
            public void onDoubleClick() {
                clickLikeFrequencyControl();
            }
        });
        mRootView.setOnSlideListener(new ClearScreenConstraintLayout.OnSlideClearListener() {
            @Override
            public void onCleared() {
                //  isClear = true;
                //  mLayoutBottomToolBar.updateInputState(isClear);
                mLayoutTopToolBar.hideLiveNoticeViewWithClear();
            }

            @Override
            public void onRestored() {
                //   isClear = false;
                //   mLayoutBottomToolBar.updateInputState(isClear);
                mLayoutTopToolBar.showLiveNoticeViewWithClear();
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


    void initRTCLayout() {
        qnLiveRtcLayout.setOnQNLiveRtcListener(new QNLiveRtcLayout.OnQNLiveRtcListener() {
            @Override
            public void doSurfaceRender(QNTrackInfo qnTrackInfo, QNSurfaceView surfaceView) {
                mEngine.setRenderWindow(qnTrackInfo, surfaceView);
            }

            @Override
            public void showCard(String uid) {
                showAudienceCard(uid);
            }
        });
    }

    /**
     * 初始化 QNRTCEngine
     */
//    private void initQNRTCEngine() {
//        // 1. VideoPreviewFormat 和 VideoEncodeFormat 建议保持一致
//        // 2. 如果远端连麦出现回声的现象，可以通过配置 setLowAudioSampleRateEnabled(true) 或者 setAEC3Enabled(true) 后再做进一步测试，并将设备信息反馈给七牛技术支持
//        QNVideoFormat format = new QNVideoFormat(Config.STREAMING_HEIGHT, Config.STREAMING_WIDTH, Config.STREAMING_FPS);
//        QNRTCSetting setting = new QNRTCSetting();
//        setting.setCameraID(QNRTCSetting.CAMERA_FACING_ID.FRONT)
//                .setHWCodecEnabled(false)
//                .setMaintainResolution(true)
//                .setVideoBitrate(Config.STREAMING_BITRATE)
//                .setVideoEncodeFormat(format)
//                .setVideoPreviewFormat(format);
//        mEngine = QNRTCEngine.createEngine(getApplicationContext(), setting, this);
//      //  mEngine.setCapturePreviewWindow(mLocalVideoSurfaceView);
//        mEngine.setCaptureVideoCallBack(mCaptureVideoCallback);
//        mEngine.muteLocalAudio(!mIsMicrophoneOn);
//        mEngine.muteRemoteAudio(!mIsSpeakerOn);
//    }
    private void initQNRTCEngine() {
        QNRTCSetting setting = new QNRTCSetting();
        QNVideoFormat format = new QNVideoFormat(Config.SMALL_STREAMING_WIDTH, Config.SMALL_STREAMING_HEIGHT, Config.STREAMING_FPS);
        setting.setVideoEncodeFormat(format);
        mEngine = QNRTCEngine.createEngine(getApplicationContext(), setting, this);
        mEngine.setCaptureVideoCallBack(new QNCaptureVideoCallback() {
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
//                int tex = TiSDKManager.getInstance().renderOESTexture(
//                        textureBuffer.getTextureId(),
//                        textureBuffer.getWidth(),
//                        textureBuffer.getHeight(),
//                        isFrontCamera ? TiRotation.CLOCKWISE_ROTATION_270 : TiRotation.CLOCKWISE_ROTATION_90,
//                        isFrontCamera);
//                // LogUtil.d("id = " + tex);
//                textureBuffer.setType(VideoFrame.TextureBuffer.Type.RGB);
//                textureBuffer.setTextureId(tex);
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
        });
    }

    private void startCaptureAfterAcquire() {
        try {
            captureStoppedSem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mEngine.startCapture();
    }

    /**
     * 初始化本地音视频 track
     * 关于 Track 的概念介绍 https://doc.qnsdk.com/rtn/android/docs/preparation#5
     */
    private void initLocalTrackInfoList() {
        mLocalTrackList = new ArrayList<>();
        // 创建本地音频 track
        mLocalAudioTrack = mEngine.createTrackInfoBuilder()
                .setSourceType(QNSourceType.AUDIO)
                .setMaster(false)
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

        // 创建 Camera 采集的视频 Track
        mLocalVideoTrack = mEngine.createTrackInfoBuilder()
                .setSourceType(QNSourceType.VIDEO_CAMERA)
                .setMaster(false)
                .create();
        mLocalTrackList.add(mLocalVideoTrack);
    }

    @Override
    public void onRoomStateChanged(QNRoomState qnRoomState) {
        mCurrentRoomState = qnRoomState;
        switch (qnRoomState) {
            case CONNECTED:
                LogUtil.d("join connected");
                if (isManagerSpeak) {
                    mEngine.publishAudio();
                    mEngine.muteLocalAudio(false);
                    isAudioMute = false;
                } else {
                    mEngine.publish();
                }
                linkState = 3;
                refreshLinkButton();
                if (liveLinkMicUserPop != null && liveLinkMicUserPop.isShowing()) {
                    liveLinkMicUserPop.dismiss();
                }
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
        }
    }

    @Override
    public void onRoomLeft() {
        mIsBeingLinkMic = false;
        playingPlayerView.start();
        qnLiveRtcLayout.hideRtcView();
        updateBottomFunctionLayout();
    }

    @Override
    public void onRemoteUserJoined(String s, String s1) {

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
        mIsBeingLinkMic = true;
        updateBottomFunctionLayout();
        if (isVideoMute) {
            doCameraClose();
        } else {
            if (isFrontCamera) {
                doCameraFront();
            } else {
                doCameraBack();
            }
        }
        mEngine.muteLocalAudio(isAudioMute);
        playingPlayerView.pause();
        qnLiveRtcLayout.showRtcView();
        qnLiveRtcLayout.initUsSurface(list);
    }

    @Override
    public void onRemotePublished(String s, List<QNTrackInfo> list) {

    }

    @Override
    public void onRemoteUnpublished(String remoteUserId, List<QNTrackInfo> list) {
        if (remoteUserId.equals(mAnchorId) || remoteUserId.equals(MyApp.uid)) {

        } else {
            LogUtil.d("onRemoteUnpublished " + remoteUserId + " -- " + list.size());
            if (list.size() == 2) {
                qnLiveRtcLayout.removeOtherSurface(remoteUserId, list);
            }
        }
    }

    @Override
    public void onRemoteUserMuted(String s, List<QNTrackInfo> list) {

    }

    @Override
    public void onSubscribed(String s, List<QNTrackInfo> list) {
        LogUtil.d("onSubscribed" + list.size());
        playingPlayerView.pause();
        if (s.equals(mAnchorId)) {
            qnLiveRtcLayout.initAnchorSurface(list);
        } else if (s.equals(MyApp.uid)) {
            qnLiveRtcLayout.initUsSurface(list);
        } else {
            if (list.size() == 2) {
                qnLiveRtcLayout.addOtherSurface(s, list);
            }
        }
    }

    @Override
    public void onSubscribedProfileChanged(String s, List<QNTrackInfo> list) {

    }

    @Override
    public void onKickedOut(String s) {
        //LogUtil.d("onkickedout " + s);
        mEngine.unPublish();
        mEngine.leaveRoom();
        linkState = 1;
        mIsBeingLinkMic = false;
        updateBottomFunctionLayout();
        qnLiveRtcLayout.hideRtcView();
        playingPlayerView.start();
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
    public void onCreateMergeJobSuccess(String s) {

    }

    @Override
    public void onCreateForwardJobSuccess(String s) {

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


    //预览页 - 点击更换封面
    public static final String TAKE_PHOTO = "拍照";
    public static final String CHOOSE_PHOTO = "从相册中选择";

    private void showSelectPic() {
        new AlertView(null, null, "取消", null,
                new String[]{TAKE_PHOTO, CHOOSE_PHOTO},
                QNLiveRoomAudienceActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
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
        LiveAnchorSettingPop liveAnchorSettingPop = new LiveAnchorSettingPop(QNLiveRoomAudienceActivity.this, liveSettingStateBean, true);
        liveAnchorSettingPop.showPopupWindow();
        liveAnchorSettingPop.setOnLiveSettingListener(new LiveAnchorSettingPop.OnLiveSettingListener() {
            @Override
            public void onChooseCameraFront() {
                mEngine.muteLocalVideo(false);
                isVideoMute = false;
                if (!isFrontCamera) {
                    mEngine.switchCamera(new QNCameraSwitchResultCallback() {
                        @Override
                        public void onCameraSwitchDone(boolean b) {
                            isFrontCamera = true;
                        }

                        @Override
                        public void onCameraSwitchError(String s) {
                            ToastUtil.show(QNLiveRoomAudienceActivity.this, s);
                        }
                    });
                }
            }

            @Override
            public void onChooseCameraBack() {
                mEngine.muteLocalVideo(false);
                isVideoMute = false;
                if (isFrontCamera) {
                    mEngine.switchCamera(new QNCameraSwitchResultCallback() {
                        @Override
                        public void onCameraSwitchDone(boolean b) {
                            isFrontCamera = false;
                        }

                        @Override
                        public void onCameraSwitchError(String s) {
                            ToastUtil.show(QNLiveRoomAudienceActivity.this, s);
                        }
                    });
                }
            }

            @Override
            public void onChooseCameraClose() {
                mEngine.muteLocalVideo(true);
                isVideoMute = true;
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
                HttpHelper.getInstance().reportHeartBeat(mAnchorId);
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
        WeakReference<QNLiveRoomAudienceActivity> liveRoomAnchorActivityWeakReference;

        public MyHandler(QNLiveRoomAudienceActivity activity) {
            liveRoomAnchorActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            QNLiveRoomAudienceActivity activity = liveRoomAnchorActivityWeakReference.get();
            switch (msg.what) {
                case 152:
                    if (activity.picFlag == 1) {
                        String s = (String) msg.obj;
                        BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
                        String imgUrl = activity.imgpre + beanicon.getData();
//                        if (liveInfoPop != null) {
//                            liveInfoPop.refreshImageUrl(imgUrl);
//                        }
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
                                        //String tempPoster = activity.imgpre + beanicon.getData();
                                        //activity.mLayoutPreview.showCover(activity, tempPoster);
                                        //ToastUtil.show(activity, "上传完成");
                                        //SharedPreferencesUtils.setParam(activity, Constants.EDIT_LIVE_ICON, tempPoster);
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


    /**
     *--------------------------------------------------------------------------------
     *
     */
    /**
     * -------------------------------------------------------------------------------
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
        //Log.i(TAG, "create forward job : " + mForwardJob.getForwardJobId());
        mEngine.createForwardJob(mForwardJob);
    }

    String getForwardJobId() {
        return "forward-" + MyApp.uid;
    }

    String getPublishUrl() {
        String url = "rtmp://pili-publish.aiwujie.net/testsm";
        return url + "_" + mRoomId + "_" + MyApp.uid + "?serialnum=" + mSerialNum++;
    }

    String getPlayUrl() {
//        String url = "rtmp://pili-live-rtmp.aiwujie.net/testsm";
//        return url + "/" + mRoomId + "_" + mAnchorId;

        //String url = "http://pili-live-hdl.aiwujie.net/testsm";
        String url = "http://pili-live-hdl.shengmo.cn/testsm";
        return url + "/" + mRoomId + "_" + mAnchorId + ".flv";

//        String url = "http://pili-live-hls.aiwujie.net/testsm";
//        return url + "/" + mRoomId + "_" + mAnchorId + ".m3u8";
    }

    private void initPlayer(String playUrl) {
        AVOptions options = new AVOptions();
        // the unit of timeout is ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_SW_DECODE);
        options.setInteger(AVOptions.KEY_PREFER_FORMAT, AVOptions.PREFER_FORMAT_FLV);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        // 快开模式，启用后会加快该播放器实例再次打开相同协议的视频流的速度
        options.setInteger(AVOptions.KEY_FAST_OPEN, 1);
        playingPlayerView.setAVOptions(options);

        // Set some listeners
        //  playingPlayerView.setOnInfoListener(mOnInfoListener);
        // playingPlayerView.setOnErrorListener(mOnErrorListener);
        playingPlayerView.setOnInfoListener(new PLOnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                if (what == MEDIA_INFO_CONNECTED) {

                }
            }
        });
        playingPlayerView.setOnErrorListener(new PLOnErrorListener() {

            @Override
            public boolean onError(int errorCode) {
                LogUtil.d("error + " + errorCode);
                switch (errorCode) {
                    case ERROR_CODE_IO_ERROR:
                        // ToastUtil.show(QNLiveRoomAudienceActivity.this, "主播已关播");
                        break;
                }

                return true;
            }
        });
        playingPlayerView.setOnCompletionListener(new PLOnCompletionListener() {
            @Override
            public void onCompletion() {
                //joinGroup();
            }
        });

        playingPlayerView.setOnVideoSizeChangedListener(new PLOnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(int i, int i1) {
                LogUtil.d("size = " + i + "---" + i1);
            }
        });
        // playUrl = "rtmp://pili-live-rtmp.aiwujie.net/testsm/402583_402583";
        // playUrl = "rtmp://pili-live-rtmp.aiwujie.net/testsm/867108_402586";
        LogUtil.d("开始播放" + playUrl);
        playingPlayerView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
        playingPlayerView.setVideoPath(playUrl);
        playingPlayerView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        playingPlayerView.stopPlayback();
        TiSDKManager.getInstance().destroy();
        unInitGroupListener();
        quitGroup();
        stopHeaderReport();
        mEngine.leaveRoom();
        mEngine.destroy();
        if (mDanmakuManager != null) {
            mDanmakuManager.destroy();
        }
        mLayoutTopToolBar.removeCountDown();//停止倒计时
    }

    /**
     * ----------------------------------------------------
     * IM相关
     */
    boolean isJoinRoom = false;

    //加入直播群
    public void joinGroup() {
        V2TIMManager.getInstance().joinGroup(mRoomId, "", new V2TIMCallback() {
            @Override
            public void onSuccess() {
                //ToastUtil.show(QNLiveRoomAudienceActivity.this, "加入直播间成功");
                //  LogUtil.d(playingPlayerView.getSurfaceView().);
                isJoinRoom = true;
                startHeaderReport();
                initGroupListener();
                initChatMessageLayout();
            }

            @Override
            public void onError(int i, String s) {
                if (i == 10013) {
                    onSuccess();
                } else {
                    //ToastUtil.show(QNLiveRoomAudienceActivity.this, s);
                    showExitDialog("主播已关播");
                }
            }
        });
    }

    //退出群组
    public void quitGroup() {
        V2TIMManager.getInstance().quitGroup(mRoomId, new V2TIMCallback() {
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


    //群组监听
    void initGroupListener() {
        mLiveGroupListener = new TimLiveRoomHelper.OnTimLiveListener() {
            @Override
            public void onMemberEnter(String groupId, String uid) {

            }

            @Override
            public void onMemberLeave(String groupId, String uid) {

            }

            @Override
            public void onGroupDismiss(String groupId) {
                if (groupId.equals(mRoomId)) {
                    if (FloatWindowLayout.getInstance().mWindowMode == Constants.WINDOW_MODE_FLOAT) {
                        FloatWindowLayout.getInstance().closeFloatWindow();
                        return;
                    }
                    showExitDialog("主播已关播");
                }
            }
        };
        mSignalingListener = new TimLiveRoomHelper.OnTimSignalingListener() {
            @Override
            public void onReceiveLinkRequest(String inviteId, String userId, String reason) {
                showLinkRequestPop(inviteId, userId, reason);
            }

            @Override
            public void onCancelLinkRequest(String inviteId, String userId, String reason) {
                //ToastUtil.show(mContext, "onCancelLinkRequest");
            }

            @Override
            public void onAcceptLinkRequest(String inviteId, String userId, String reason) {
                //ToastUtil.show(mContext,"主播同意连麦");
                isManagerSpeak = false;
                startLink();
            }

            @Override
            public void onRejectLinkRequest(String inviteId, String userId, String reason) {
                if (!userId.equals(MyApp.uid)) {
                    onLinkRequestReject(reason);
                }
            }

            @Override
            public void onTimeoutLinkRequest(String inviteId, String userId, String reason) {
                if (userId.equals(mAnchorId)) { //发给主播的邀请超时了
                    onLinkRequestTimeout();
                } else if (userId.equals(MyApp.uid)) { //主播发给我的邀请超时了
                    if (invitePop != null && invitePop.isShowing()) {
                        invitePop.dismiss();
                    }
                }
            }

            @Override
            public void onReceivePkRequest(String inviteId, String userId, String reason) {

            }

            @Override
            public void onCancelPkRequest(String inviteId, String userId, String reason) {

            }

            @Override
            public void onAcceptPkRequest(String inviteId, String userId, String reason) {

            }

            @Override
            public void onRejectPkRequest(String inviteId, String userId, String reason) {

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

    //设置主播关闭房间
    private void showExitDialog(String content) {
        final NormalTipsPop normalTipsPop = new NormalTipsPop.Builder(QNLiveRoomAudienceActivity.this)
                .setTitle("直播结束啦！")
                .setInfo(content)
                .setCancelStr("")
                .setConfirmStr("好的")
                .build();
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
                finish();
            }
        });
        normalTipsPop.showPopupWindow();
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
//        LogUtil.d("单聊消息 ----------------------------------- ");
//        LogUtil.d("单聊消息 " + event.getCloudCustomData());
//        LogUtil.d("单聊消息 " + event.getCustomElemData());
//        LogUtil.d("单聊消息 " + event.getMessage().getTextElem().getText());
//        LogUtil.d("单聊消息 ----------------------------------- ");

        //服务器发的直播间消息
        if (!TextUtil.isEmpty(event.getCloudCustomData()) && event.getCloudCustomData().contains("costomMassageType")) {
            CloudCustomDataBean cloudCustomDataBean = GsonUtil.GsonToBean(event.getCloudCustomData(), CloudCustomDataBean.class);
            if (cloudCustomDataBean == null || TextUtil.isEmpty(cloudCustomDataBean.getCostomMassageType())) {
                return;
            }
            String messageType = cloudCustomDataBean.getCostomMassageType();
            switch (messageType) {
                case LiveRoomMessageType.USER_DATA:
                    LiveUserData liveUserData = GsonUtil.GsonToBean(event.getCloudCustomData(), LiveUserData.class);
                    if (liveUserData == null) {
                        return;
                    }
                    switch (liveUserData.getActionType()) {
                        case "1":
                            is_group_admin = liveUserData.getIs_group_admin();
                            break;
                        case "2":
                            isNoTalking = liveUserData.getSetNotalking();
                            break;
                        case "3":
                            audienceLevel = liveUserData.getUser_level();
                            break;
                        case "4":
                            taskSchedule = liveUserData.getComplete_schedule();
                            updateTaskScheduleView();
                            break;
                    }
                    break;
            }
        }

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
        //updateIMMessageList(LiveMessageHelper.buildNormalEntity(v2TIMMessage.getUserID(), v2TIMMessage.getNickName(), text));
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
                    mLayoutTopToolBar.updateDurationTime(liveCreateAvChatRoom.getTime());
                    name_color = liveCreateAvChatRoom.getName_color();
                    liveTitle = liveCreateAvChatRoom.getAnchor_info().getLive_title();
                    livePoster = liveCreateAvChatRoom.getAnchor_info().getLive_poster();
                    anchorLevel = liveCreateAvChatRoom.getAnchor_level();
                    audienceLevel = liveCreateAvChatRoom.getUser_level();
                    break;
                case LiveRoomMessageType.JOIN_ROOM_MSG:
                    CustomMessage joinMsg = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
                    LiveJoinAvChatRoom liveJoinAvChatRoom = GsonUtil.GsonToBean(event.getCloudCustomData(), LiveJoinAvChatRoom.class);
                    String nickname = !TextUtils.isEmpty(event.getMessage().getNickName()) ? event.getMessage().getNickName() : event.getMessage().getSender();
                    String sendId = !TextUtils.isEmpty(event.getMessage().getSender()) ? event.getMessage().getSender() : event.getMessage().getSender();
                    //updateIMMessageListStartAnim(IMUtils.joinRoom(joinMsg,nickname,sendId));
                    // showJoinInfo(IMUtils.joinRoom(joinMsg, nickname, sendId));
                    updateIMMessageList(IMUtils.joinRoom(joinMsg,liveJoinAvChatRoom, nickname, sendId));
                    if (event.getMessage().getSender().equals(MyApp.uid)) {
                        name_color = joinMsg.getName_color();
                        is_group_admin = liveJoinAvChatRoom.getIs_group_admin();
                        String mute = ("1".equals(liveJoinAvChatRoom.getSetNotalking()) || "1".equals(liveJoinAvChatRoom.getIs_mute())) ? "1" : "0";
                        isNoTalking = mute;
                        //wealth_val = joinMsg.getWealth_val();
                        //wealth_val_switch = joinMsg.getWealth_val_switch();
                        audienceLevel = liveJoinAvChatRoom.getUser_level();
                        anchorLevel = liveJoinAvChatRoom.getAnchor_level();
                        live_user_role = joinMsg.getLive_user_role();
                        is_admin = liveJoinAvChatRoom.getIs_admin();
                        name_color = joinMsg.getName_color();
                        livePoster = liveJoinAvChatRoom.getAnchor_info().getLive_poster();
                        liveTitle = liveJoinAvChatRoom.getAnchor_info().getLive_title();
                        mRoomToken = liveJoinAvChatRoom.getRoom_token();
                        SharedPreferencesUtils.setParam(QNLiveRoomAudienceActivity.this,"roomToken",mRoomToken);
                        mLayoutTopToolBar.updateDurationTime(liveJoinAvChatRoom.getAnchor_info().getLive_time());
//                        if ("1".equals(liveJoinAvChatRoom.getIs_interaction())) { //当前房间为互动房 开始倒计时
//                            interactLimitTime = liveJoinAvChatRoom.getInteraction_time();
//                            interactTip = liveJoinAvChatRoom.getInteraction_tips();
//                            startInteractTime();
//                        }
                        if (liveJoinAvChatRoom.getPkInfo().getIs_pk() == 1) {
                            showPkView(liveJoinAvChatRoom.getPkInfo());
                        }
                        taskSchedule = liveJoinAvChatRoom.getComplete_schedule();
                        updateTaskScheduleView();
                        if (!TextUtils.isEmpty(liveJoinAvChatRoom.getManager_link_list())) {
                            refreshLinkManager(liveJoinAvChatRoom.getManager_link_list());
                        }
                        getLiveRedBagList(mRoomId);
                    }
                    break;
                case LiveRoomMessageType.CHANGE_ROOM:
                    LiveChangeInfoChatRoom liveChangeInfoChatRoom = com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil.GsonToBean(event.getCloudCustomData(), LiveChangeInfoChatRoom.class);
                    switch (liveChangeInfoChatRoom.getActionType()) {
                        case "1":
                            mLayoutTopToolBar.updateCurrentBeansCount(liveChangeInfoChatRoom.getBeans_current_count());
                            //mLocalBeans = Long.parseLong(liveChangeInfoChatRoom.getBeans_current_count());
                            anchorLevel = liveChangeInfoChatRoom.getAnchor_level();
                            audienceLevel = liveChangeInfoChatRoom.getUser_level();
                            break;
                        case "2":
                            mLayoutTopToolBar.updateContestNo(liveChangeInfoChatRoom.getHour_ranking());
                            break;
                        case "3":
                            if ("1".equals(is_admin)) {
                                mLayoutTopToolBar.updateOnlineNum(liveChangeInfoChatRoom.getReal_watchsum());
                            } else {
                                mLayoutTopToolBar.hideOnlineNum();
                            }
                            break;
                        case "5":
                            break;
                        case "6":
                            anchorLevel = liveChangeInfoChatRoom.getAnchor_level();
                            audienceLevel = liveChangeInfoChatRoom.getUser_level();
                            break;
                        case "7":
                            liveTitle = liveChangeInfoChatRoom.getLive_title();
                            livePoster = liveChangeInfoChatRoom.getLive_poster();
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
                    //mTotalMemberCount = watchAvChatRoom.getWatchsum();
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
                case LiveRoomMessageType.LIVE_MANAGER_CHANGE:
                    LiveManagerChangeBean liveManagerChangeBean = com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil.GsonToBean(event.getCloudCustomData(), LiveManagerChangeBean.class);
                    if (liveManagerChangeBean != null) {
                        if ("0".equals(liveManagerChangeBean.getActionType())) {
                            String managerLinkStr = liveManagerChangeBean.getManager_link_list();
                            refreshLinkManager(managerLinkStr);
                        }
                    }
                    break;
                case LiveRoomMessageType.ROOM_STATE_PK:
                    PkChatRoomBean pkChatRoomBean = GsonUtil.GsonToBean(event.getCloudCustomData(), PkChatRoomBean.class);
                    if ("0".equals(pkChatRoomBean.getActionType())) {
                        if (!layoutPkView.isShow()) {
                            layoutPkView.showPkView();
                        }
                        if (pkChatRoomBean.getIs_reset().equals("1")) {
                            layoutPkView.clearPkStatus();
                            layoutPkView.initPkData(pkChatRoomBean.getPkInfo());
                        } else {
                            layoutPkView.initPkData(pkChatRoomBean.getPkInfo());
                        }
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

    /**-----------------------------------------------------*/


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
                        entity.setType(Constants.TEXT_TYPE);
                        entity.setUid(V2TIMManager.getInstance().getLoginUser());
                        //增加场控/vip标识
                        String admin = !TextUtils.isEmpty(is_group_admin) ? is_group_admin : "0";
                        entity.setIs_group_admin(admin);
                        entity.setLive_group_role(IMUtils.getGroupRole(admin, mAnchorId));
                        entity.setLive_user_role(live_user_role);
                        //entity.setLive_user_role(String.valueOf(TUIKitLive.getLoginUserInfo().getRole()));
                        entity.setWealth_val("");
                        entity.setWealth_val_switch("");
                        entity.setUser_level(audienceLevel);
                        entity.setAnchor_level(anchorLevel);
                        entity.setName_color(name_color);
                        reportChatMessage(msg);
                        if (tanmuOpen) { //弹幕消息
                            sendDanmaku(msg);
                        } else { //普通消息
                            if (!TextUtils.isEmpty(isNoTalking)) { //从服务器获取 禁言状态
                                if (("1").equals(isNoTalking)) {//已禁言
                                    ToastUtil.show(QNLiveRoomAudienceActivity.this, "您在直播间已禁言，无法发送消息");
                                } else if (("0").equals(isNoTalking)) {//未禁言
                                    LiveMessageHelper.getInstance().sendLiveMessage(mRoomId, entity, new LiveMessageHelper.OnMessageCallback() {
                                        @Override
                                        public void onSendSuc() {
                                            updateIMMessageList(entity);
                                        }

                                        @Override
                                        public void onSendFail(String msg) {
                                            ToastUtil.show(QNLiveRoomAudienceActivity.this, msg);
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

    boolean mIsBeingLinkMic = false;
    String taskSchedule = "";
    TextView mTvTask;

    //刷新底部button状态
    void updateBottomFunctionLayout() {
        // 初始化分享按钮
        CircleImageView buttonShare = new CircleImageView(mContext);
        // buttonShare.setId(mRoomId);
        buttonShare.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.ic_live_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSharePanel();
            }
        });

        // 初始化连麦按钮
        CircleImageView mButtonLink = new CircleImageView(mContext);
        // mButtonLink.setId(mRoomId);
        mButtonLink.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_linkmic_on);
        // mButtonLink.setImageResource(mIsBeingLinkMic ? R.drawable.live_linkmic_off : R.drawable.live_linkmic_on);
        mButtonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLinkMicUserPop();
            }
        });
        // 初始化点赞按钮
        CircleImageView buttonLike = new CircleImageView(mContext);
        //buttonLike.setId(mRoomId);
        buttonLike.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_bottom_bar_like);
        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickLikeFrequencyControl();
            }
        });
        // 初始化礼物按钮
        CircleImageView buttonGift = new CircleImageView(mContext);
        //buttonGift.setId(mRoomId);
        buttonGift.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_gift_btn_icon);
        buttonGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGiftPanel();
            }
        });
        // 初始化切换摄像头按钮
        CircleImageView buttonSwitchCam = new CircleImageView(TUIKitLive.getAppContext());
        buttonSwitchCam.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_ic_switch_camera_on);
        buttonSwitchCam.setVisibility(mIsBeingLinkMic ? View.VISIBLE : View.GONE);
        buttonSwitchCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFrontCamera = !isFrontCamera;
            }
        });


        // 初始化美颜按钮
        CircleImageView buttonBeauty = new CircleImageView(TUIKitLive.getAppContext());
        buttonBeauty.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_ic_beauty);
        buttonBeauty.setVisibility(mIsBeingLinkMic ? View.VISIBLE : View.GONE);
        buttonBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tiPanel != null) {
                    tiPanel.showBeautyPanel();
                }
            }
        });

        //初始化任务按钮
        CircleImageView buttonTask = new CircleImageView(TUIKitLive.getAppContext());
        //buttonTask.setId(mRoomId);
        buttonTask.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.ic_button_live_task);
        buttonTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLiveTaskPop();
            }
        });


        //初始化设置按钮
        CircleImageView buttonSetting = new CircleImageView(TUIKitLive.getAppContext());
        // buttonSetting.setId(mRoomId);
        buttonSetting.setImageResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.live_setting_btn_icon);
        buttonSetting.setVisibility(mIsBeingLinkMic && !isManagerSpeak ? View.VISIBLE : View.GONE);
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_ANCHOR_SETTING, "",
//                        com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil.getInstance().toJson(new LiveSettingStateBean(isAudioMute, isVideoMute, isFrontCamera))));
//
                showLiveAudienceSettingPop();
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


        FrameLayout frameLayout = new FrameLayout(TUIKitLive.getAppContext());

        int iconSize = mContext.getResources().getDimensionPixelSize(com.tencent.qcloud.tim.tuikit.live.R.dimen.live_bottom_toolbar_btn_icon_size);
        FrameLayout.LayoutParams mFunctionLayoutParams = new FrameLayout.LayoutParams(iconSize, iconSize);
        mFunctionLayoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;

        frameLayout.addView(buttonTask, mFunctionLayoutParams);


        mTvTask = new TextView(mContext);
        mTvTask.setTextColor(Color.parseColor("#ffffff"));
        mTvTask.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 7);
        mTvTask.setPadding(8, 3, 8, 3);
        mTvTask.setBackgroundResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.bg_round_audience_task_bg);
        mTvTask.setVisibility(TextUtils.isEmpty(taskSchedule) ? View.GONE : View.VISIBLE);
        mTvTask.setText(taskSchedule);
        FrameLayout.LayoutParams mFunctionLayoutParams2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mFunctionLayoutParams2.gravity = Gravity.RIGHT | Gravity.TOP;

        frameLayout.addView(mTvTask, mFunctionLayoutParams2);
        //frameLayout.setBackgroundResource(R.drawable.bg_round_audience_top_2);

        List<View> buttonList = new ArrayList<>();
        buttonList.add(buttonShare);
        buttonList.add(frameLayout);
        buttonList.add(mButtonLink);
        buttonList.add(buttonLike);
        buttonList.add(buttonGift);
        buttonList.add(buttonLottery);
        //buttonList.add(buttonBeauty);
        buttonList.add(buttonSetting);

        //mLayoutBottomToolBar.setRightButtonsLayout(Arrays.asList(buttonShare,buttonTask, mButtonLink, buttonLike, buttonGift, buttonSetting));
        mLayoutBottomToolBar.setRightButtonsLayout(buttonList);
    }

    void initTopToolBar() {
        mLayoutTopToolBar.setVisibility(View.VISIBLE);
        getAnchorBaseInfo();
        mLayoutTopToolBar.setTopToolBarDelegate(new TopToolBarLayout.TopToolBarDelegate() {
            @Override
            public void onClickAnchorAvatar() {
                if ("1".equals(is_admin)) {
                    showLiveCardPop(mAnchorId);
                } else {
                    showAudienceCard(mAnchorId);
                }
            }

            @Override
            public void onClickFollow(TRTCLiveRoomDef.LiveAnchorInfo liveAnchorInfo) {
                followAnchor(true);
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
                // preExitRoom();
                finish();
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
                if (String.valueOf(mRoomId).equals(messageBean.getRoom_id())) {
                    return;
                }
                showAnnouncementTipPop(messageBean);
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

        TextView tvReport = mLayoutTopToolBar.findViewById(R.id.report_user);
        tvReport.setVisibility(View.VISIBLE);
        tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReportActivity.class);
                intent.putExtra("uid", mAnchorId);
                intent.putExtra("source_type", 1);
                startActivity(intent);
            }
        });
    }

    void showAnnouncementTipPop(final LiveAnnouncementBean.MessageBean messageBean) {
        String title = "是否前往 " + messageBean.getReceive_nickname() + " 的直播间?";
        LiveNormalTipPanel liveNormalTipPanel = new LiveNormalTipPanel(mContext,title);
        liveNormalTipPanel.show();
        liveNormalTipPanel.setOnNormalTipListener(new LiveNormalTipPanel.OnNormalTipListener() {
            @Override
            public void doNormalLeftClick() {

            }

            @Override
            public void doNormalRightClick() {
                gotoOtherLiveRoom(messageBean.getReceive_uid(),messageBean.getRoom_id());
            }
        });
    }

    void gotoOtherLiveRoom(String uid,String roomId) {
        EventBus.getDefault().post(new OtherLiveRoomEvent(uid,roomId));
        mLayoutTopToolBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent intent2 = new Intent(QNLiveRoomAudienceActivity.this, MainActivity.class);
                Intent intent2 = new Intent(QNLiveRoomAudienceActivity.this, HomePageActivity.class);
                startActivity(intent2);
                finish();
            }
        },300);
    }


    //观众画面设置
    void showLiveAudienceSettingPop() {
        LiveSettingStateBean liveSettingStateBean = new LiveSettingStateBean(isAudioMute, isVideoMute, isFrontCamera);
        LiveAnchorSettingPop liveAnchorSettingPop = new LiveAnchorSettingPop(mContext, false, liveSettingStateBean);
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
            }

            @Override
            public void onChooseManagerSpeak() {
//                if (getLiveAnchorFragment() != null) {
//                    getLiveAnchorFragment().doLiveManagerSpeak();
//                }
            }
        });
    }

    LiveRoomHeadPop headPop;

    //显示用户卡片
    void showAudienceCard(String uid) {
        if (!TextUtils.isEmpty(uid)) {
            TimCustomMessage timRoomMessageBean = new TimCustomMessage();
            timRoomMessageBean.setUid(uid);
            timRoomMessageBean.setRoomId(mRoomId);
            timRoomMessageBean.setIs_group_admin(!TextUtils.isEmpty(is_group_admin) ? is_group_admin : "");
            timRoomMessageBean.setAnchor_uid(mAnchorId);
            headPop = new LiveRoomHeadPop(mContext, timRoomMessageBean, TAG_ANCHOR);
            headPop.showPopupWindow();
            headPop.setOnPopOperateListener(new LiveRoomHeadPop.OnPopOperateListener() {
                @Override
                public void onFollow(TextView tvAnchorFollow, int follow_state, String uid) {
                    // LiveHttpRequest.followAnchor(tvAnchorFollow, mContext, uid, follow_state);
                    //boolean isFollow = follow_state == 2 || follow_state == 4; //调用关注接口
                    //boolean hasFollow = follow_state == 1 || follow_state == 3; //调用取消关注接口
                    if (follow_state == 2 || follow_state == 4) { //未关注
                        followAnchor(true);
                    } else { //已关注
                        followAnchor(false);
                    }
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

            }

            @Override
            public void onLiveUserLink(@NotNull TimCustomMessage tcm) {

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
        Log.v("showRewardRankPop","11111");
        GiftViewPagerPop pop = new GiftViewPagerPop(QNLiveRoomAudienceActivity.this, R.style.BottomFullDialog, mAnchorId);
        pop.show();

    }

    //显示小时热度榜
    void showHourRankPop() {
        GiftRankingListPop pop = new GiftRankingListPop(QNLiveRoomAudienceActivity.this, R.style.BottomFullDialog, mAnchorId);
        pop.show();
        pop.setOnPopListener(new GiftRankingListPop.OnPopListener() {
            @Override
            public void doPopAnchorClick() {
                showGiftPanel();
            }

            @Override
            public void doPopTopAnchorClick(String uid, String roomId) {
                gotoOtherLiveRoom(uid,roomId);
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
            // mLayoutTopToolBar.setHasFollowed(true); //主播自己不能关注自己，隐藏关注视图
            //类型：Number  必有字段  备注：关注状态 1已关注 2没关注 3互相关注 4被关注
            if ("1".equals(liveRoomInfo.getData().getFollow_state()) || "3".equals(liveRoomInfo.getData().getFollow_state())) {
                mLayoutTopToolBar.setHasFollowed(true);
            } else if ("2".equals(liveRoomInfo.getData().getFollow_state()) || "4".equals(liveRoomInfo.getData().getFollow_state())) {
                mLayoutTopToolBar.setHasFollowed(false);
            } else {
                mLayoutTopToolBar.setHasFollowed(true);
                ;//默认隐藏关注按钮
            }
        }
        mLayoutTopToolBar.setAnchorInfo(mAnchorInfo); //传递主播信息
        mLayoutTopToolBar.updateCurrentBeansCount(liveRoomInfo.getData().getBeans_current_count());
        if (!liveRoomInfo.getData().getNickname().equals(liveRoomInfo.getData().getLive_title())) {
            mLayoutTopToolBar.updateLiveTitle(liveRoomInfo.getData().getLive_title());
        }
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
                ToastUtil.show(QNLiveRoomAudienceActivity.this, "弹幕发射成功");
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(QNLiveRoomAudienceActivity.this, msg);
            }
        });
    }

    GiftPanelViewImp giftPanelView;

    //展示礼物面板
    public void showGiftPanel() {
        String token = SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", "");
        String url = LiveHttpHelper.getInstance().liveGiftList(HttpUrl.liveGiftList);
        giftPanelView = new GiftPanelViewImp(mContext, url, token, mAnchorId, mRoomId, false);
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
                    ToastUtil.show(mContext, s);
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
                    ToastUtil.show(mContext, s);
                }
            });
        }
    }

    void doCameraClose() {

        //  final View coverView = LayoutInflater.from(QNLiveRoomAudienceActivity.this).inflate(com.tencent.qcloud.tim.tuikit.live.R.layout.layout_live_cover, null, false);
        //  final ImageView ivCover = coverView.findViewById(com.tencent.qcloud.tim.tuikit.live.R.id.iv_live_cover);

        Glide.with(mContext)
                .asBitmap()
                .load(ProfileManager.getInstance().getUserModel().userAvatar)
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        QNImage qnImage = new QNImage(mContext);
                        qnImage.setResourceBitmap(resource);
                        qnImage.setImageSize(Config.STREAMING_WIDTH, Config.STREAMING_HEIGHT);
                        mEngine.pushCameraTrackWithImage(qnImage);
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

    //更新任务小圆点
    void updateTaskScheduleView() {
        if (mTvTask != null) {
            if (TextUtils.isEmpty(taskSchedule)) {
                mTvTask.setVisibility(View.GONE);
            } else {
                mTvTask.setVisibility(View.VISIBLE);
                mTvTask.setText(taskSchedule);
            }
        }
    }

    //  底部抽奖
    public void showLotteryDrawLayout() {
        LiveLotteryDrawPop liveLotteryDrawPop = new LiveLotteryDrawPop(this);
        liveLotteryDrawPop.showPopupWindow();
    }


    //任务卡片
    void showLiveTaskPop() {
        LiveAudienceTaskPop liveAudienceTaskPop = new LiveAudienceTaskPop(QNLiveRoomAudienceActivity.this, MyApp.uid);
        liveAudienceTaskPop.showPopupWindow();
        liveAudienceTaskPop.setOnLiveTaskPopListener(new LiveAudienceTaskPop.OnLiveTaskPopListener() {
            @Override
            public void doTaskRank() {
                showTaskRankPop();
            }
        });
    }

    //任务排行
    void showTaskRankPop() {
        LiveAuTaskRankPop liveAuTaskRankPop = new LiveAuTaskRankPop(QNLiveRoomAudienceActivity.this, MyApp.uid);
        liveAuTaskRankPop.showPopupWindow();
    }


    int linkState = 1;  //1-未申请 2-申请中 3-连麦中
    boolean isOpenCamera = true;
    LiveLinkMicUserPop liveLinkMicUserPop;

    void showLinkMicUserPop() {
        LinkMicStateBean linkMicStateBean = new LinkMicStateBean();
        linkMicStateBean.setAnchorId(mAnchorId);
        linkMicStateBean.setCameraOpen(!isVideoMute);
        linkMicStateBean.setLinkState(linkState);
        liveLinkMicUserPop = new LiveLinkMicUserPop(QNLiveRoomAudienceActivity.this, linkMicStateBean);
        liveLinkMicUserPop.showPopupWindow();
        liveLinkMicUserPop.setOnLiveUserListener(new LiveLinkMicUserPop.OnLiveUserListener() {
            @Override
            public void onLiveUserKickMic(@NotNull TimCustomMessage tcm) {

            }

            @Override
            public void onLiveUserClick(@NotNull TimCustomMessage tcm) {
                showAudienceCard(tcm.getUid());
            }

            @Override
            public void onLiveUserDoCamera(boolean isOpen) {
//                if (getAudienceFragment() != null) {
//                    getAudienceFragment().doCamera(isOpen);
//                }
                doCamera(isOpen);
                //isVideoMute = isOpen;
            }

            @Override
            public void onLiveUserLink(int type) {
//                if (getAudienceFragment() != null) {
//                    getAudienceFragment().doLinkAnchor(type);
//                }
//                if (!TextUtil.isEmpty(mRoomId)) {
//                    mEngine.joinRoom(mRoomToken);
//                }
                if (isPermissionOK()) {
                    doLinkAnchor(type);
                }
            }
        });
    }

    public void doCamera(boolean isOpen) {
//        if (isOpen) {
//            if (linkState == 3) {
//                doCameraFront();
//                isOpenCamera = true;
//            } else {
//                isOpenCamera = true;
//            }
//        } else {
//            if (linkState == 3) {
//                doCameraClose();
//                isOpenCamera = false;
//            } else {
//                isOpenCamera = false;
//            }
//        }
        isVideoMute = !isOpen;
        if (linkState == 3) {
            if (isVideoMute) {
                doCameraClose();
            } else {
                if (isFrontCamera) {
                    doCameraFront();
                } else {
                    doCameraBack();
                }
            }
        }
    }

    private LikeFrequencyControl mLikeFrequencyControl;    //点赞频率的控制类

    public void clickLikeFrequencyControl() {
        if (heartLayout != null) {
            heartLayout.addFavor();
        }
        //点赞发送请求限制
        if (mLikeFrequencyControl == null) {
            mLikeFrequencyControl = new LikeFrequencyControl();
            mLikeFrequencyControl.init(8, 1);
        }
        if (mLikeFrequencyControl.canTrigger()) {
            LiveMessageHelper.getInstance().sendPraiseMessage(mRoomId);
            HttpHelper.getInstance().likeanchor(mAnchorId);
        }
    }


    /**
     * -----------------------------------------------------
     */

    @Override
    public void onBackPressed() {
        if (isJoinRoom) {
            showFloatView();
        } else {
            super.onBackPressed();
        }
    }

    void showFloatView() {
        updateLiveWindowMode(Constants.WINDOW_MODE_FLOAT);
    }

    /**
     * 最小化窗口
     *
     * @param mode
     */
    protected void updateLiveWindowMode(int mode) {
        if (FloatWindowLayout.getInstance().mWindowMode == mode) return;

        if (mode == Constants.WINDOW_MODE_FLOAT) {
            if (mContext == null) {
                return;
            }
            FloatWindowLayout.IntentParams intentParams = new FloatWindowLayout.IntentParams(mContext.getClass(), Integer.parseInt(mRoomId), mAnchorId, true, "");
            boolean result = FloatWindowLayout.getInstance().showFloatWindow(playingPlayerView, intentParams);
            if (!result) {
                // exitRoom();
                return;
            }
//            FloatWindowLayout.getInstance().setOnTouchFloatWindowListener(new FloatWindowLayout.OnTouchFloatWindowListener() {
//                @Override
//                public void onTouchUpFloatWindow() {
////                    if (mOnTouchFloatWindowListener != null) {
////                        mOnTouchFloatWindowListener.onTouchUpFloatWindow();
////                    }
//                //    RoomManager.gotoQiNiuRoom(mContext, mAnchorId, mRoomId);
//                }
//            });
            FloatWindowLayout.getInstance().mWindowMode = mode;
            // mLayoutVideoManager.updateVideoLayoutByWindowStatus();
            FloatWindowLayout.getInstance().setFloatWindowLayoutDelegate(new FloatWindowLayout.FloatWindowLayoutDelegate() {
                @Override
                public void onClose() {
                    // exitRoom();
                    finish();
                }
            });
            moveTaskToBack(true);
        } else if (mode == Constants.WINDOW_MODE_FULL) {
            FloatWindowLayout.getInstance().closeFloatWindow();
            FloatWindowLayout.getInstance().mWindowMode = mode;
            // isFloatRecovery = true;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Constents.isShowAudienceFloatWindow) {
            View mLiveView = Constents.liveView;
            if (mLiveView != null) {
                ((ViewGroup) mLiveView.getParent()).removeView(mLiveView);
                flVideoView.addView(mLiveView);
                Constents.liveView = null;
            }
            FloatWindowLayout.getInstance().closeFloatWindowWithoutDestory();
            FloatWindowLayout.getInstance().mWindowMode = Constants.WINDOW_MODE_FULL;
        }
    }


    void initTicketView() {
        countDownBtTicket.setOnCountClickListener(new CountDownButton.onCountClickListener() {
            @Override
            public void onCounting(double process) {

            }

            @Override
            public void onCountingClick() {
                showBuyTicketView();
            }

            @Override
            public void onCountOverClick() {
                showBuyTicketView();
            }
        });

        if (limitTime != 0) { //当前房间为门票房
            countDownBtTicket.setVisibility(View.VISIBLE);
            countDownBtTicket.setTotalTime(limitTime * 1000);
            countDownBtTicket.setDelayTime(500);
            countDownBtTicket.startTimer();
        } else {
            countDownBtTicket.setVisibility(View.GONE);
        }
    }

    void showBuyTicketView() {
        countDownBtTicket.cancle();
        countDownBtTicket.setVisibility(View.GONE);
        final LiveBuyTicketPop liveBuyTicketPop = new LiveBuyTicketPop(mContext, mAnchorId);
        liveBuyTicketPop.showPopupWindow();
        liveBuyTicketPop.setOnTicketPopListener(new LiveBuyTicketPop.OnTicketPopListener() {
            @Override
            public void doPopBuySuc() {
                ToastUtil.show(mContext, "购买成功");
                liveBuyTicketPop.dismiss();
            }

            @Override
            public void doPopBuyFail(@org.jetbrains.annotations.Nullable String msg) {
                ToastUtil.show(mContext, msg);
                liveBuyTicketPop.dismiss();
            }

            @Override
            public void doPopDismiss() {
                finish();
            }
        });
    }

    /**
     * 判断是否开启了悬浮窗权限
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
                    return;
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //6.0版本
                super.startActivity(intent);
                onBackPressed();
//                if (!Settings.canDrawOverlays(LiveRoomSwitchActivity.this)) {
//
//                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().showFloatPermissionDialog("主播最小化窗口，需要开启悬浮窗权限，是否开启？");
//                } else {
//                    mLayoutTuiLiverRomAnchor.getLiveRomAnchor().onBackPressed();
//                    super.startActivity(intent);
//                }
            } else {
                super.startActivity(intent);
            }

        } else {
            super.startActivity(intent);
        }
    }

    /**
     * -------------------------------------------------------------------------------
     * 信令相关
     */

    public void doLinkAnchor(int type) {
        switch (type) {
            case 1: //发起连麦申请
                if ("1".equals(is_admin) || "1".equals(is_group_admin)) { //黑v或者是场控
                    showLinkTypePop();
                } else {
                    requestLinkNormal();
                }
                break;
            case 2: //取消连麦申请
                // cancelLink();
                cancelRequestLink();
                break;
            case 3: //关闭连麦
                stopLinkMic();
                break;
        }
    }

    boolean isManagerSpeak = false;

    void showLinkTypePop() {
        ChooseLinkTypePanel chooseLinkTypePanel = new ChooseLinkTypePanel(mContext);
        chooseLinkTypePanel.setPkStopListener(new ChooseLinkTypePanel.OnChooseLinkTypeListener() {
            @Override
            public void onChooseManagerLink() {
                reportLinkManager();
            }

            @Override
            public void onChooseUserLink() {
                requestLinkNormal();
            }
        });
        chooseLinkTypePanel.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                liveLinkMicUserPop.doLinkRequestSuc();
            }
        });
        chooseLinkTypePanel.show();
    }

    void requestLinkNormal() {
        requestLink(ProfileManager.getInstance().getUserModel().userName + " 申请连麦");
    }

    String inviteId = "";

    void requestLink(String reason) {
        inviteId = V2TIMManager.getSignalingManager().invite(mAnchorId, IMProtocol.getJoinReqJsonStr(reason), true, null, 15, new V2TIMCallback() {
            @Override
            public void onSuccess() {
                ToastUtil.show(mContext, "连麦申请发送成功");
                linkState = 2;
                refreshLinkButton();
            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.show(mContext, i + s);
                if (liveLinkMicUserPop != null) {
                    liveLinkMicUserPop.doLinkRequestSuc();
                }
            }
        });
    }

    void cancelRequestLink() {
        String reason = ProfileManager.getInstance().getUserModel().userName + " 取消了连麦申请";
        V2TIMManager.getSignalingManager().cancel(inviteId, IMProtocol.getCancelReqJsonStr(reason), new V2TIMCallback() {
            @Override
            public void onSuccess() {
                ToastUtil.show(mContext, "取消连麦申请");
                linkState = 1;
                refreshLinkButton();
            }

            @Override
            public void onError(int i, String s) {
                ToastUtil.show(mContext, i + s);
                if (liveLinkMicUserPop != null) {
                    liveLinkMicUserPop.doLinkRequestSuc();
                }
            }
        });
    }

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
                rejectLinkInvite(inviteId);
            }

            @Override
            public void confirmClick() {
                invitePop.dismiss();
                acceptLinkInvite(inviteId);
            }
        });
        invitePop.showPopupWindow();
    }

    void rejectLinkInvite(String inviteId) {
        String reason = ProfileManager.getInstance().getUserModel().userName + "拒绝连麦";
        V2TIMManager.getSignalingManager().reject(inviteId, IMProtocol.getJoinRspJsonStr(reason), null);
    }

    void acceptLinkInvite(String inviteId) {
        String reason = ProfileManager.getInstance().getUserModel().userName + "同意连麦";
        V2TIMManager.getSignalingManager().accept(inviteId, IMProtocol.getJoinRspJsonStr(reason), null);
    }

    void onLinkRequestTimeout() {
        ToastUtil.show(mContext, "连麦申请超时");
        linkState = 1;
        refreshLinkButton();
    }

    void onLinkRequestReject(String reason) {
        //ToastUtil.show(mContext,"连麦申请超时");
        ToastUtil.show(mContext, reason);
        linkState = 1;
        refreshLinkButton();
    }

    void startLink() {
//        if (!isOpenCamera) {
//            doCameraClose();
//        }
        mEngine.joinRoom(mRoomToken);
    }

    //
    void stopLinkMic() {
        mEngine.unPublish();
        mEngine.leaveRoom();
        linkState = 1;
        refreshLinkButton();
    }

    //刷新申请连麦的按钮
    void refreshLinkButton() {
        if (liveLinkMicUserPop != null) {
            liveLinkMicUserPop.doLinkRequestSuc();
            liveLinkMicUserPop.refreshLinkState(linkState);
        }
    }

    /**
     * -----------------------------------------------------------------------------
     */


    private boolean isPermissionOK() {
        PermissionChecker checker = new PermissionChecker(QNLiveRoomAudienceActivity.this);
        boolean isPermissionOK = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checker.checkPermission();
        if (!isPermissionOK) {
            ToastUtils.showShortToast(this, "连麦需要授权权限");
        }
        return isPermissionOK;
    }

    public void followAnchor(boolean isFollow) {
        HttpHelper.getInstance().followAnchor(mAnchorId, isFollow, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                if (isFollow) {
                    ToastUtil.show(mContext, "关注成功");
                    mLayoutTopToolBar.setHasFollowed(true);
                } else {
                    ToastUtil.show(mContext, "取消关注");
                    mLayoutTopToolBar.setHasFollowed(false);
                }
                if (headPop != null && headPop.isShowing()) {
                    headPop.refreshData();
                }
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
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
        LiveRedEnvelopesPop redEnvelopesPop = new LiveRedEnvelopesPop(QNLiveRoomAudienceActivity.this, redEnvelopesList.get(index),isReceive);
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

    void initLiveManagerLayout() {
        layoutLiveManager.setOnLiveManagerListener(new LiveManagerLayout.OnLiveManagerListener() {
            @Override
            public void doManagerClick(String uid) {
                showAudienceCard(uid);
            }
        });
    }

    //上报场控发言
    void reportLinkManager() {
        HttpHelper.getInstance().reportManagerLink(mAnchorId, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                isManagerSpeak = true;
                ToastUtil.show(mContext, "开始场控发言");
                startLink();
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });

    }

    void refreshLinkManager(String linkStr) {
        if (TextUtils.isEmpty(linkStr)) {
            layoutLiveManager.refreshManager(new ArrayList<String>());
        } else {
            String[] split = linkStr.split(",");
            layoutLiveManager.refreshManager(Arrays.asList(split));
        }
    }

    /**
     * -------------------------------------------------------------------------------------------
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

            }

            @Override
            public void onPkTimeOut(String uid) {

            }

            @Override
            public void onPunishTimeOut(String uid) {

            }

            @Override
            public void onMuteOtherVoice(String uid, String name, boolean isMute) {

            }
        });
    }

    void showPkView(PkInfoBean pkInfoBean) {
        layoutPkView.showPkView();
        layoutPkView.initPkData(pkInfoBean);
    }

    void showPkTopAudienceView(String uid, String otherId, boolean isUs) {
        LivePkTopAudiencePop livePkTopAudiencePop = new LivePkTopAudiencePop(QNLiveRoomAudienceActivity.this, uid, otherId, isUs);
        livePkTopAudiencePop.showPopupWindow();
    }

    /**
     * -------------------------------------------------------------------------------------------
     */

}
