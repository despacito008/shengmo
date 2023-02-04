package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.opensource.svgaplayer.utils.log.LogUtils;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.audiosettingkit.AudioEffectPanel;
import com.tencent.liteav.custom.Constents;
import com.tencent.liteav.custom.CountDownButton;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.base.BaseFragment;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.base.HttpPostRequest;
import com.tencent.qcloud.tim.tuikit.live.base.TUILiveRequestCallback;
import com.tencent.qcloud.tim.tuikit.live.bean.LinkMicStateBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveAnnouncementBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveChangeInfoChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveHistoryMessageBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveJoinAvChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveManagerChangeBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveMessageBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveMessageCustomDataBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveRedEnvelopesBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveSettingStateBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveUserData;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveWatchAvChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.NormalEventBean;
import com.tencent.qcloud.tim.tuikit.live.bean.PkChatRoomBean;
import com.tencent.qcloud.tim.tuikit.live.bean.PkInfoBean;
import com.tencent.qcloud.tim.tuikit.live.bean.PkTopAudienceMessageBean;
import com.tencent.qcloud.tim.tuikit.live.bean.ShareAnchorBean;
import com.tencent.qcloud.tim.tuikit.live.bean.TimCustomMessage;
import com.tencent.qcloud.tim.tuikit.live.component.bottombar.BottomToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.component.common.CircleImageView;
import com.tencent.qcloud.tim.tuikit.live.component.danmaku.DanmakuManager;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;
import com.tencent.qcloud.tim.tuikit.live.component.gift.LiveGiftPanel;
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
import com.tencent.qcloud.tim.tuikit.live.component.pk.PkViewLayout;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.TopToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.helper.UserIdentityUtils;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.TUILiveRoomAudienceLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.AppLiveDialogEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.AppLiveFollowEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.AppLiveGiftChangedEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.CustomMessage;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.GiftMessage;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMessageSignBean;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.ReportLiveMessageBean;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoom;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDelegate;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.av.trtc.TXTRTCLiveRoom;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.room.impl.IMProtocol;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.clearscreen.ClearScreenConstraintLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.clearscreen.SlideDirection;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.ExitConfirmDialog;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.LiveInteractTipDialog;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.LiveVideoManagerLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.LiveVideoView;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.NormalTipDialog;
import com.tencent.qcloud.tim.tuikit.live.utils.AnimationUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil;
import com.tencent.qcloud.tim.tuikit.live.utils.IMUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.PermissionUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.TUILiveLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiRotation;
import cn.tillusory.tiui.TiPanelLayout;
import master.flame.danmaku.controller.IDanmakuView;

import static com.tencent.qcloud.tim.tuikit.live.base.Constants.REQUEST_LINK_MIC_TIME_OUT;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.CAVEAT;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.CHANGE_ROOM;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.JOIN_ROOM_MSG;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.LIVE_ANIMATION_MSG;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.LIVE_MANAGER_CHANGE;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.LIVE_RED_ENVELOPES_CHANGE;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.PROMPT_MSG;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.ROOM_STATE_PK;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.USER_DATA;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.WATCH_ROOM;
import static com.tencent.qcloud.tim.tuikit.live.utils.IMUtils.handleCustomTextMsg;
import static com.tencent.trtc.TRTCCloudDef.TRTC_GSENSOR_MODE_DISABLE;
import static com.tencent.trtc.TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG;

public class LiveRoomAudienceFragment extends BaseFragment implements TopToolBarLayout.TopToolBarDelegate {
    private static final String TAG = "LiveAudienceFragment";
    private GiftPanelViewImp giftPanelView;
    private static final long LINK_MIC_INTERVAL = 3 * 1000;    //连麦间隔控制
    private static TextView giftCoin;

    private Context mContext;
    private TopToolBarLayout mLayoutTopToolBar;
    private BottomToolBarLayout mLayoutBottomToolBar;
    private ChatLayout mLayoutChatMessage;
    private IDanmakuView mDanmakuView;             // 负责弹幕的显示
    private DanmakuManager mDanmakuManager;          // 弹幕的管理类
    private AlertDialog mDialogError;             // 错误提示的Dialog
    private TRTCLiveRoom mLiveRoom;                // 组件类
    private LikeFrequencyControl mLikeFrequencyControl;    //点赞频率的控制类
    private GiftAnimatorLayout mGiftAnimatorLayout;
    private ClearScreenConstraintLayout mRootView;
    private LiveVideoManagerLayout mLayoutVideoManager;
    private HeartLayout mHeartLayout;
    private CircleImageView mButtonLink;
    private ImageView mImagePkLayer;
    private TextView mStatusTipsView;
    private Button mButtonReportUser;         //举报按钮
    private LinearLayout mLiveMore, mLiveContestStarNo;//更多直播/礼物星榜
    private TextView mTvLiveContestNo, mTvLiveSex, mTvLiveType, mTvLiveWatchNo, mTvAnchorName; //头部左侧信息
    private CircleImageView mIvAnchorHead;
    private LinearLayout mLLLiveContestNo;//小时榜
    private LinearLayout mLLLiveContestStarNo;//礼物星榜
    private RelativeLayout mRlPkTime; //倒计时
    private AudioEffectPanel mAnchorAudioPanel;//音乐和变声
    private boolean isEnterRoom = false;    // 表示当前是否已经进房成功
    private boolean isUseCDNPlay = false;    // 表示当前是否是CDN模式
    private boolean mIsAnchorEnter = false;    // 表示主播是否已经进房
    private boolean mIsBeingLinkMic = false;    // 表示当前是否在连麦状态
    private boolean isFloatRecovery = false;    // 表示是否是从悬浮窗恢复过来的
    private int mCurrentStatus = TRTCLiveRoomDef.ROOM_STATUS_NONE;
    private int mRoomId;
    private long mLastLinkMicTime;
    private String mAnchorId;
    private String mSelfUserId;
    private String mCdnUrl;
    private String is_group_admin; //备注：是否场控1是0不是
    private String isNoTalking;  //备注：是否禁言1是0不是
    private String is_admin; //是否是黑v
    private String live_user_role;//用户姓名
    private String wealth_val; //财富值
    private String wealth_val_switch; //是否显示财富值
    private String name_color;//名字的颜色
    private String backgroundColor; //背景颜色
    private String livePoster;// 封面
    private String liveTitle;//标题
    private String audienceLevel;//用户等级
    private String anchorLevel;//作为主播等级
    private String fanclubLevel;//粉丝团等级
    private String fanclubCard;//粉丝团名
    private String fanclubStatus;//展示
    private LiveMessageSignBean mLiveMessageSignBean; //整合五个显示字段

    private TextView tvLinkMic; //显示连麦列表按钮

    private CountDownButton cdbTicket; //门票房倒计时按钮

    private PkViewLayout mPkViewLayout; //pk模式的ui

    private String taskSchedule = "";

    private Group mGroupPkView;

    private LiveManagerLayout mLiveManagerLayout;

    /***************************** Setter方法 *************************************/
    public void setIs_group_admin(String is_group_admin) {
        this.is_group_admin = is_group_admin;
    }

    public void setIsNoTalking(String isNoTalking) {
        this.isNoTalking = isNoTalking;
    }
    //private String mPullAddress;
    //private String giftUrl;

    private LinearLayout llAlertGiftList;
    private GiftAdapter mGiftAdapter;
    private Runnable mGetAudienceRunnable;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mShowAnchorLeave = new Runnable() {      //如果一定时间内主播没出现
        @Override
        public void run() {
            mLayoutVideoManager.setAnchorOfflineViewVisibility(mIsAnchorEnter ? View.GONE : View.VISIBLE);
            mLayoutBottomToolBar.setVisibility(mIsAnchorEnter ? View.VISIBLE : View.GONE);
            mButtonReportUser.setVisibility(mIsAnchorEnter ? View.VISIBLE : View.GONE);
        }
    };

    private TUILiveRoomAudienceLayout.TUILiveRoomAudienceDelegate mLiveRoomAudienceDelegate;
    private TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo = new TRTCLiveRoomDef.LiveAnchorInfo();
    private TRTCLiveRoomDelegate mTRTCLiveRoomDelegate = new TRTCLiveRoomDelegate() {
        @Override
        public void onError(int code, String message) {
            TUILiveLog.e(TAG, "onError: " + code + " " + message);
            if (mLiveRoomAudienceDelegate != null) {
                mLiveRoomAudienceDelegate.onError(code, message);
            }
        }

        @Override
        public void onWarning(int code, String message) {

        }

        @Override
        public void onDebugLog(String message) {

        }

        @Override
        public void onRoomInfoChange(TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo) {
            mCurrentStatus = roomInfo.roomStatus;
            // 由于CDN模式下是只播放一路画面，所以不需要做画面的设置
            if (isUseCDNPlay) {
                if (mCurrentStatus != TRTCLiveRoomDef.ROOM_STATUS_PK) {
                    mImagePkLayer.setVisibility(View.GONE);
                    //mGroupPkView.setVisibility(View.GONE);
                    mPkViewLayout.hidePkView();
                } else {
                    mImagePkLayer.setVisibility(View.VISIBLE);
                    // mGroupPkView.setVisibility(View.VISIBLE);
                    mPkViewLayout.showPkView();
                }
                return;
            }
            mLayoutVideoManager.updateRoomStatus(roomInfo.roomStatus, mRlPkTime);
            TUILiveLog.d(TAG, "onRoomInfoChange: " + mCurrentStatus);
        }

        @Override
        public void onRoomDestroy(String roomId) {
            if (FloatWindowLayout.getInstance().mWindowMode == Constants.WINDOW_MODE_FLOAT) {
                FloatWindowLayout.getInstance().closeFloatWindow();
                return;
            }
            if (roomId.equals(String.valueOf(mRoomId))) { //判断是不是当前房间
                if (isAdded()) {
                    showErrorAndQuit(0, getString(R.string.live_warning_room_disband));
                }
            }
        }

        @Override
        public void onAnchorEnter(final String userId) {
            Log.d(TAG, "onAnchorEnter userId: " + userId + ", mOwnerId -> " + mAnchorId);
            if (userId.equals(mAnchorId)) {
                // 如果是大主播的画面
                mIsAnchorEnter = true;
                mButtonReportUser.setVisibility(View.VISIBLE);
                mHandler.removeCallbacks(mShowAnchorLeave);
                //调用后台的通知（xxx进入直播间）
                if (onAnchorEnterListener != null) {
                    onAnchorEnterListener.OnAnchorEnter(userId);
                }
                //TRTC模式播放
                mLayoutVideoManager.startAnchorVideo(userId, true, new TRTCLiveRoomCallback.ActionCallback() {
                    @Override
                    public void onCallback(int code, String msg) {
                        if (code != 0) {
                            onAnchorExit(userId);
                        }
                    }
                });
                //RTMP模式播放
                /*mLayoutVideoManager.startAnchorVideoFlv(userId,mPullAddress, true, new TRTCLiveRoomCallback.ActionCallback() {
                    @Override
                    public void onCallback(int code, String msg) {
                        if (code != 0) {
                            onAnchorExit(userId);
                        }
                    }
                });*/
                initBottomToolBar();
                //updateFollowView(userId);
            } else {
                // mLayoutVideoManager.startAnchorVideo(userId, false, null);
                checkIsManagerEnter(userId);
                //mLayoutVideoManager.startAnchorVideoFlv(userId,mPullAddress, false, null);
            }
        }

        @Override
        public void onAnchorExit(String userId) {
            if (userId.equals(mAnchorId)) {
                mLayoutVideoManager.stopAnchorVideo(userId, true, null);
                mLayoutBottomToolBar.setVisibility(View.GONE);
                mButtonReportUser.setVisibility(View.GONE);
            } else {
                mLayoutVideoManager.stopAnchorVideo(userId, false, null);
            }
        }

        @Override
        public void onAudienceEnter(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
            //左下角显示用户加入消息
            ChatEntity entity = new ChatEntity();
            entity.setSenderName(mContext.getString(R.string.live_notification));
            if (TextUtils.isEmpty(userInfo.userName)) {
                entity.setContent(mContext.getString(R.string.live_user_join_live, userInfo.userId));
            } else {
                entity.setContent(mContext.getString(R.string.live_user_join_live, userInfo.userName));
            }
            entity.setType(Constants.MEMBER_ENTER);
            //updateIMMessageList(entity);
            //addAudienceListLayout(userInfo);
        }

        @Override
        public void onAudienceExit(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
//            ChatEntity entity = new ChatEntity();
//            entity.setSenderName(mContext.getString(R.string.live_notification));
//            if (TextUtils.isEmpty(userInfo.userName)) {
//                entity.setContent(mContext.getString(R.string.live_user_quit_live, userInfo.userId));
//            } else {
//                entity.setContent(mContext.getString(R.string.live_user_quit_live, userInfo.userName));
//            }
//            entity.setType(Constants.MEMBER_EXIT);
            //updateIMMessageList(entity);
            //removeAudienceListLayout(userInfo);
            //exitRoom();
            if (userInfo.userId.equals(ProfileManager.getInstance().getUserId())) { //自己被踢出
                ToastUtil.toastShortMessage("你已被踢出直播间");
                if (Constents.isShowAudienceFloatWindow) {
                    FloatWindowLayout.getInstance().closeFloatWindow();
                } else {
                    closeRoom();
                }
            }
        }

        @Override
        public void onRequestJoinAnchor(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, String inviteId) {
            // ToastUtil.toastShortMessage("收到主播的连麦请求1" + userInfo.userId);
            if (userInfo.userId.equals(mAnchorId)) {
                //ToastUtil.toastShortMessage("收到主播的连麦请求");
                showAnchorInviteLinkPop(inviteId);
            }
        }

        @Override
        public void onAudienceRequestJoinAnchorTimeout(String userId) {

        }

        @Override
        public void onAudienceCancelRequestJoinAnchor(String userId) {

        }

        @Override
        public void onKickoutJoinAnchor() {
            stopLinkMic();
        }

        @Override
        public void onRequestRoomPK(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {

        }

        @Override
        public void onAnchorCancelRequestRoomPK(String userId) {

        }

        @Override
        public void onAnchorRequestRoomPKTimeout(String userId) {

        }

        @Override
        public void onQuitRoomPK() {

        }

        @Override
        public void onRecvRoomTextMsg(String message, TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, String roomId) {
            if (roomId.equals(mRoomId)) {
                ChatEntity entity = IMUtils.handleTextMsg(userInfo, message);
                updateIMMessageList(entity);
            }
        }

        @Override
        public void onRecvRoomCustomMsg(String roomId, String cmd, String message, TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
            int type = Integer.valueOf(cmd);
            // Log.d("直播间消息", "cmd = " + cmd + ",msg = " + message);
            switch (type) {
                case Constants.IMCMD_PRAISE:
                    //handlePraiseMsg(userInfo);
                    if (mHeartLayout != null) {
                        mHeartLayout.addFavor();
                    }
                    break;
                case Constants.IMCMD_DANMU:
                    handleDanmuMsg(userInfo, message);
                    break;
                case Constants.IMCMD_GIFT:
                    handleGiftMsg(userInfo, message);
                    break;
                default:
                    break;
            }
        }
    };


    public void setLiveRoomAudienceDelegate(TUILiveRoomAudienceLayout.TUILiveRoomAudienceDelegate liveRoomAudienceDelegate) {
        mLiveRoomAudienceDelegate = liveRoomAudienceDelegate;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(getContext());
        EventBus.getDefault().register(this);
    }


    //最好把数据放在onCreateView里边加载
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.live_fragment_live_room_audience, container, false);
        initView(rootView);
        PermissionUtils.checkLivePermission(getActivity());
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (isFloatRecovery) {
            mLiveRoom.exitRoom(new TRTCLiveRoomCallback.ActionCallback() {
                @Override
                public void onCallback(int code, String msg) {
                    enterRoom();
                }
            });
        } else {
//            if (isEnterRoom) {
//                enterRoom();
//            }
            enterRoom();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mShowAnchorLeave);
        mHandler.removeCallbacks(mGetAudienceRunnable);
        EventBus.getDefault().unregister(this);
        mLayoutTopToolBar.removeCountDown();//停止倒计时
        stopInteractTime();
    }

    TiPanelLayout tiPanelLayout;
    boolean isClear = false;

    private void initView(final View rootView) {
        mLiveMessageSignBean = new LiveMessageSignBean();
        // 请求恢复全屏模式
        updateLiveWindowMode(Constants.WINDOW_MODE_FULL);
        mLayoutTopToolBar = rootView.findViewById(R.id.layout_top_toolbar);
        mLayoutTopToolBar.findViewById(R.id.ll_audiences_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeRoom();
            }
        });
        mLayoutBottomToolBar = rootView.findViewById(R.id.layout_bottom_toolbar);
        mLayoutChatMessage = rootView.findViewById(R.id.layout_chat);
        mGiftAnimatorLayout = rootView.findViewById(R.id.lottie_animator_layout);
        mDanmakuView = rootView.findViewById(R.id.view_danmaku);
        mDanmakuManager.setDanmakuView(mDanmakuView);
        mRootView = rootView.findViewById(R.id.root);
        mHeartLayout = rootView.findViewById(R.id.heart_layout);
        mImagePkLayer = rootView.findViewById(R.id.iv_pk_layer);
        mRlPkTime = rootView.findViewById(R.id.rl_pk_time_display_container);
        mStatusTipsView = rootView.findViewById(R.id.state_tips);
        mLayoutTopToolBar.setTopToolBarDelegate(this);
        mButtonReportUser = mLayoutTopToolBar.findViewById(R.id.report_user);
        mLiveMore = mLayoutTopToolBar.findViewById(R.id.ll_live_more);
        mIvAnchorHead = mLayoutTopToolBar.findViewById(R.id.iv_anchor_head);
        mTvAnchorName = mLayoutTopToolBar.findViewById(R.id.tv_anchor_name);
        mLiveContestStarNo = mLayoutTopToolBar.findViewById(R.id.ll_live_contest_star_number);
        llAlertGiftList = mLayoutTopToolBar.findViewById(R.id.ll_alert_gift_list);
        mTvLiveContestNo = mLayoutTopToolBar.findViewById(R.id.tv_live_contest_number);
        mTvLiveSex = mLayoutTopToolBar.findViewById(R.id.tv_live_top_tool_bar_sex);
        mTvLiveType = mLayoutTopToolBar.findViewById(R.id.tv_live_top_tool_bar_type);
        mTvLiveWatchNo = mLayoutTopToolBar.findViewById(R.id.tv_audience_number);
        mLLLiveContestNo = mLayoutTopToolBar.findViewById(R.id.ll_live_contest_number);
        mLLLiveContestStarNo = mLayoutTopToolBar.findViewById(R.id.ll_live_contest_star_number);
        tvLinkMic = rootView.findViewById(R.id.tv_live_audience_link_mic);
        tiPanelLayout = rootView.findViewById(R.id.ti_panel);
        tiPanelLayout.init(TiSDKManager.getInstance());
        tiPanelLayout.setOnPanelItemClickListener(new TiPanelLayout.OnPanelItemClickListener() {
            @Override
            public void onBeautyItemClick() {
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.GET_BEAUTY_PERMISSION, "", ""));
            }
        });
        initPkViewLayout(rootView);
        initBottomMessageLayout(rootView);
        mAnchorAudioPanel = new AudioEffectPanel(getContext(), true);
        mAnchorAudioPanel.setAudioEffectManager(mLiveRoom.getAudioEffectManager());
        mAnchorAudioPanel.initPanelDefaultBackground();
        tvLinkMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkMicUserPop();
            }
        });

        //clearScreenLayout = rootView.findViewById(R.id.clear_screen_layout);

        mRootView.addClearViews(mLayoutChatMessage, mGiftAnimatorLayout);
        mRootView.setSlideDirection(SlideDirection.LEFT);
        mRootView.setOnOtherEventListener(new ClearScreenConstraintLayout.OnOtherEventListener() {
            @Override
            public void onLeftBack() {
                getActivity().onBackPressed();
            }

            @Override
            public void onDoubleClick() {
                clickLikeFrequencyControl();
            }
        });
        mRootView.setOnSlideListener(new ClearScreenConstraintLayout.OnSlideClearListener() {
            @Override
            public void onCleared() {
                isClear = true;
                mLayoutBottomToolBar.updateInputState(isClear);
                mLayoutTopToolBar.hideLiveNoticeViewWithClear();
            }

            @Override
            public void onRestored() {
                isClear = false;
                mLayoutBottomToolBar.updateInputState(isClear);
                mLayoutTopToolBar.showLiveNoticeViewWithClear();
            }
        });


        //给主工程app设置回调监听事件
        mLiveMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLiveOperateClickListener != null) {
                    onLiveOperateClickListener.onMoreLiveClick();
                }
            }
        });
        //礼物星榜
        llAlertGiftList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLiveOperateClickListener != null) {
                    String anchor_id = !TextUtils.isEmpty(mAnchorId) ? mAnchorId : "";
                    onLiveOperateClickListener.onLiveStarNumber(v, anchor_id);
                }
            }
        });
        //热度榜单
        mLLLiveContestNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLiveOperateClickListener != null) {
                    String anchor_id = !TextUtils.isEmpty(mAnchorId) ? mAnchorId : "";
                    onLiveOperateClickListener.onClickContestNo(v, anchor_id);
                }
            }
        });
        //把view回调传递给工程目录下的activity
        if (onLiveOperateClickListener != null) {
            onLiveOperateClickListener.onLiveSetHeadViewInfo(mLayoutTopToolBar, mAnchorId);
        }
        mLayoutVideoManager = rootView.findViewById(R.id.ll_video_view);
        mLayoutVideoManager.updateVideoLayoutByWindowStatus();
        mLayoutVideoManager.setVideoManagerLayoutDelegate(new LiveVideoManagerLayout.VideoManagerLayoutDelegate() {
            @Override
            public void onClose() {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        mButtonReportUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reportUser();
                if (onLiveOperateClickListener != null) {
                    onLiveOperateClickListener.reportUser(mSelfUserId, mAnchorId);
                }
            }
        });
        initListener();

        cdbTicket = rootView.findViewById(R.id.count_down_bt_ticket);
        cdbTicket.setOnCountClickListener(new CountDownButton.onCountClickListener() {
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


        mGroupPkView = rootView.findViewById(R.id.group_pk_view);
        mLiveManagerLayout = rootView.findViewById(R.id.layout_live_manager);
        mLiveManagerLayout.setOnLiveManagerListener(new LiveManagerLayout.OnLiveManagerListener() {
            @Override
            public void doManagerClick(String uid) {
                showUserCard(uid);
            }
        });


        mLayoutTopToolBar.setOnTopToolBarListener(new TopToolBarLayout.OnTopToolBarListener() {
            @Override
            public void doRewardBeanClick() {

            }

            @Override
            public void doHourRankClick() {

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
    }

    TextView tvBottomMsg;
    ImageView groupRole;
    ImageView userIdentity;
    private LinearLayout llItemJoinMessage;

    private void initBottomMessageLayout(View convertView) {
        llItemJoinMessage = convertView.findViewById(R.id.ll_bottom_message);
        tvBottomMsg = (TextView) convertView.findViewById(R.id.tv_bottom_message_msg);
        groupRole = (ImageView) convertView.findViewById(R.id.iv_bottom_message_role);
        userIdentity = (ImageView) convertView.findViewById(R.id.iv_bottom_message_level);
        userIdentity.setImageResource(R.drawable.user_manager);
    }


    private void initPkViewLayout(View convertView) {
        mPkViewLayout = convertView.findViewById(R.id.layout_pk_view);

        mPkViewLayout.setOnPkViewListener(new PkViewLayout.OnPkViewListener() {
            @Override
            public void doPkAudienceClick(String uid, String otherUid, boolean isUs) {
                showPkTopAudienceView(uid, otherUid, isUs);
            }

            @Override
            public void doPkOtherAnchorClick(String uid) {
                showUserCard(uid);
            }

            @Override
            public void onPkTimeOut(String uid) {

            }

            @Override
            public void onPunishTimeOut(String uid) {
                mPkViewLayout.showLinkView();
            }

            @Override
            public void onMuteOtherVoice(String uid, String name, boolean isMute) {

            }
        });
    }


    private void showJoinInfo(final ChatEntity entity) {
        updateIMMessageList(entity);

//
//        TRTCCloudDef.TRTCRenderParams trtcRenderParams = new TRTCCloudDef.TRTCRenderParams();
//        trtcRenderParams.fillMode = TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT;
//        trtcRenderParams.rotation = TRTCCloudDef.TRTC_VIDEO_ROTATION_90;
//        TRTCCloud.sharedInstance(getActivity()).setRemoteRenderParams(mAnchorId, TRTC_VIDEO_STREAM_TYPE_BIG,trtcRenderParams);

        if (true) {
            return;
        }

        // -- 底部显示 --
        Log.d("logutil", entity.getSenderName() + " " + entity.getLive_group_role() + " " + entity.getLive_user_role());
        llItemJoinMessage.setVisibility(View.VISIBLE);
        String joinStr = entity.getSenderName() + " " + entity.getContent();

        SpannableString spanString = new SpannableString(joinStr);
        // 根据名称计算颜色
        spanString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.live_color_send_name5)),
                0, entity.getSenderName().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvBottomMsg.setText(spanString);

        if ("1".equals(entity.getLive_group_role())) {
            groupRole.setImageResource(R.drawable.live_manager_icon);
            groupRole.setVisibility(View.VISIBLE);
        } else if ("2".equals(entity.getLive_group_role())) {
            groupRole.setImageResource(R.drawable.live_anchor_icon);
            groupRole.setVisibility(View.VISIBLE);
        } else {
            groupRole.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(entity.getLive_user_role())) {
            UserIdentityUtils.showUserIdentity(mContext, Integer.valueOf(entity.getLive_user_role()), userIdentity);
        } else {
            userIdentity.setVisibility(View.GONE);
        }

        llItemJoinMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserCard(entity.getUid());
//                if(!TextUtils.isEmpty(entity.getUid())){
//                    TimCustomMessage timRoomMessageBean = new TimCustomMessage();
//                    timRoomMessageBean.setUid(entity.getUid());
//                    timRoomMessageBean.setRoomId(TextUtils.isEmpty(String.valueOf(mRoomId)) ? String.valueOf(mRoomId) : "");
//                    timRoomMessageBean.setIs_group_admin(!TextUtils.isEmpty(is_group_admin) ? is_group_admin : "");
//                    timRoomMessageBean.setIs_admin(!TextUtils.isEmpty(is_admin)?is_admin:"");
//                    if(!TextUtils.isEmpty(mAnchorId)){
//                        timRoomMessageBean.setAnchor_uid(!TextUtils.isEmpty(mAnchorId) ? mAnchorId : "");
//                    }
//                    String data = GsonUtil.GsonString(timRoomMessageBean);
//                    sendLiveEvent("onChatLayoutItemClick",data);
//                }
            }
        });
//        ObjectAnimator amin1 = AnimationUtils.createFadesInFromLtoR(
//                llItemJoinMessage, -llItemJoinMessage.getWidth(), 0, 400, new OvershootInterpolator());

        ObjectAnimator amin1 = AnimationUtils.createFadesInFromLtoR(
                llItemJoinMessage, mLayoutTopToolBar.getMeasuredWidth(), 0, 500, new AccelerateDecelerateInterpolator());
        amin1.start();

        amin1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                llItemJoinMessage.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator amin2 = AnimationUtils.createFadesInFromLtoR(
                                llItemJoinMessage, 0, (-llItemJoinMessage.getWidth() - 50), 500, new AccelerateDecelerateInterpolator());
                        amin2.start();
                    }
                }, 2000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    private void initData(Context context) {
        mContext = context;
        mSelfUserId = V2TIMManager.getInstance().getLoginUser();
        mLiveRoom = TRTCLiveRoom.sharedInstance(context);
        mLiveRoom.setDelegate(mTRTCLiveRoomDelegate);
        mDanmakuManager = new DanmakuManager(context);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mRoomId = bundle.getInt(Constants.ROOM_ID);
            isUseCDNPlay = bundle.getBoolean(Constants.USE_CDN);
            mCdnUrl = bundle.getString(Constants.CDN_URL);
            mAnchorId = bundle.getString(Constants.ANCHOR_ID);
            //mPullAddress = bundle.getString(Constants.PUSHER_ADDRESS);
            //giftUrl = bundle.getString(Constants.GIFT_URL);
            //Log.d(TAG,mAnchorId + "========" + mPullAddress);
        }
    }

    public void enterRoom() {
        if (isEnterRoom) {
            return;
        }
        if (mLiveRoom == null)
            return;
        mLiveRoom.enterRoom(mRoomId, isUseCDNPlay, mCdnUrl, new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code == 0) {
                    isEnterRoom = true;
                    updateTopToolBar();
                    // getHistoryMessage();
                    getLiveHistoryMessage();
                    getTicketState();
                } else {
                    exitRoom();
                }
            }
        });
        mHandler.postDelayed(mShowAnchorLeave, 6 * 1000);
    }

    public void exitRoom() {
        if (mIsBeingLinkMic) {  //连麦中 关闭连麦
            stopLinkMic();
        }
        Log.d("logutil", (isEnterRoom) + "--" + (mLiveRoom != null));
        if (isEnterRoom && mLiveRoom != null) {
            Log.d("logutil", "exit room");
            mLiveRoom.exitRoom(null);
            if (onLiveOperateClickListener != null) {
                String anchor_id = !TextUtils.isEmpty(mAnchorId) ? mAnchorId : "";
                onLiveOperateClickListener.closeLive(anchor_id);
            }
            isEnterRoom = false;
        }
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
                        entity.setType(Constants.TEXT_TYPE);
                        entity.setUid(V2TIMManager.getInstance().getLoginUser());
                        //增加场控/vip标识
                        String admin = !TextUtils.isEmpty(is_group_admin) ? is_group_admin : "0";
                        entity.setIs_group_admin(admin);
                        entity.setLive_group_role(IMUtils.getGroupRole(admin, mAnchorId));
                        entity.setLive_user_role(live_user_role);
                        //entity.setLive_user_role(String.valueOf(TUIKitLive.getLoginUserInfo().getRole()));
                        entity.setWealth_val(wealth_val);
                        entity.setWealth_val_switch(wealth_val_switch);
                        entity.setUser_level(audienceLevel);
                        entity.setAnchor_level(anchorLevel);
                        entity.setName_color(name_color);
                        entity.setBackground_color(backgroundColor);
                        entity.setUser_level(mLiveMessageSignBean.getUser_level());
                        entity.setAnchor_level(mLiveMessageSignBean.getAnchor_level());
                        entity.setFanclub_status(mLiveMessageSignBean.getFanclub_status());
                        entity.setFanclub_level(mLiveMessageSignBean.getFanclub_level());
                        entity.setFanclub_card(mLiveMessageSignBean.getFanclub_card());
                        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.REPORT_LIVE_MESSAGE,
                                "", GsonUtil.getInstance().toJson(new ReportLiveMessageBean(mAnchorId, String.valueOf(mRoomId), msg))));

                        if (tanmuOpen) { //弹幕消息
                            TimCustomMessage timRoomMessageBean = new TimCustomMessage();
                            if (mAnchorInfo != null) {
                                String anchorUid = !TextUtils.isEmpty(mAnchorInfo.userId) ? mAnchorInfo.userId : mAnchorId;
                                timRoomMessageBean.setAnchor_uid(anchorUid);
                            }
                            timRoomMessageBean.setContent(msg);
                            String data = GsonUtil.GsonString(timRoomMessageBean);
                            sendLiveEvent("tanmuOpenSendTxt", data);
                        } else { //普通消息
                            String anchorUid = "";
                            if (mAnchorInfo != null) {
                                anchorUid = !TextUtils.isEmpty(mAnchorInfo.userId) ? mAnchorInfo.userId : mAnchorId;
                            }
                            //userData里边的字段
                            if (!TextUtils.isEmpty(isNoTalking)) { //从服务器获取 禁言状态
                                if (("1").equals(isNoTalking)) {//已禁言
                                    ToastUtil.toastShortMessage(getString(R.string.banned));
                                } else if (("0").equals(isNoTalking)) {//未禁言
                                    IMUtils.sendText(mLiveRoom, entity, new IMUtils.OnTextSendListener() {
                                        @Override
                                        public void sendSuc() {
                                            resumeInteractTime();
                                            updateIMMessageList(entity);
                                        }
                                    });
                                }
                            } else {
                                //腾讯SDK监听
                                getStatus(String.valueOf(mRoomId), mSelfUserId, anchorUid, msg, admin);
                                resumeInteractTime();
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

    /**
     * 发送弹幕消息
     *
     * @param msg
     */
    public void tanmuOpenSendTxt(String msg) {
        if (mDanmakuManager != null) {
            mDanmakuManager.addDanmu(mAnchorInfo.avatarUrl, mAnchorInfo.userName, msg);
        }
        mLiveRoom.sendRoomTextMsg(msg, new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code != 0) {
                    Toast.makeText(TUIKitLive.getAppContext(), R.string.live_message_send_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //com.tencent.imsdk:imsdk-plus:5.6.1202 'commentTime'升级SDK之后才能拿到值
    private void getStatus(String roomId, final String mySelfId, final String mAnchorId, final String msg, final String role) {
        //监听是否禁言
        V2TIMManager.getGroupManager().getGroupMembersInfo(roomId, Arrays.asList(mySelfId), new V2TIMValueCallback<List<V2TIMGroupMemberFullInfo>>() {
            @Override
            public void onSuccess(List<V2TIMGroupMemberFullInfo> v2TIMGroupMemberFullInfos) {
                for (V2TIMGroupMemberFullInfo v2TIMGroupMemberFullInfo : v2TIMGroupMemberFullInfos) {
                    if (v2TIMGroupMemberFullInfo.getUserID().equals(mySelfId)) {
                        long commentTime = v2TIMGroupMemberFullInfo.getMuteUntil();//获取群成员禁言结束时间戳
                        if (commentTime == 0) { //获取不到禁言状态值
                            //从接口获取
                            IMUtils.sendText(mLiveRoom, msg, role, mAnchorId, null);
                        } else { //获取到禁言状态值
                            if (System.currentTimeMillis() > commentTime) {//取消禁言成功
                                IMUtils.sendText(mLiveRoom, msg, role, mAnchorId, null);
                            } else {
                                ToastUtil.toastShortMessage(getString(R.string.banned));
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(int code, String desc) {
                Log.d(TAG, desc);
            }
        });
    }

    TextView mTvTask;

    private void updateBottomFunctionLayout() {

        // 初始化分享按钮
        CircleImageView buttonShare = new CircleImageView(mContext);
        buttonShare.setId(mRoomId);
        buttonShare.setImageResource(R.drawable.ic_live_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSharePanel();
            }
        });

        // 初始化连麦按钮
        mButtonLink = new CircleImageView(mContext);
        mButtonLink.setId(mRoomId);
        mButtonLink.setImageResource(R.drawable.live_linkmic_on);
        // mButtonLink.setImageResource(mIsBeingLinkMic ? R.drawable.live_linkmic_off : R.drawable.live_linkmic_on);
        mButtonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLinkMicUserPop();
            }
        });
        // 初始化点赞按钮
        CircleImageView buttonLike = new CircleImageView(mContext);
        buttonLike.setId(mRoomId);
        buttonLike.setImageResource(R.drawable.live_bottom_bar_like);
        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickLikeFrequencyControl();
            }
        });
        // 初始化礼物按钮
        CircleImageView buttonGift = new CircleImageView(mContext);
        buttonGift.setId(mRoomId);
        buttonGift.setImageResource(R.drawable.live_gift_btn_icon);
        buttonGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (onLiveOperateClickListener != null) {
//                    onLiveOperateClickListener.showGiftPanel();
//                }
                showGiftPanel();
            }
        });
        // 初始化切换摄像头按钮
        CircleImageView buttonSwitchCam = new CircleImageView(TUIKitLive.getAppContext());
        buttonSwitchCam.setImageResource(R.drawable.live_ic_switch_camera_on);
        buttonSwitchCam.setVisibility(mIsBeingLinkMic ? View.VISIBLE : View.GONE);
        buttonSwitchCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLiveRoom.switchCamera();
                isFrontCamera = !isFrontCamera;
                if (isFrontCamera) {
                    mLiveRoom.setMirror(true); //前置改为镜像
                } else {
                    mLiveRoom.setMirror(false); //后置改为非镜像
                }
//                mLivePusher.getDeviceManager().switchCamera(mFrontCamera);
//                if (mLivePusher.getDeviceManager().isFrontCamera()) {
//                    mLiveRoom.setMirror(true); //前置改为镜像
//                } else {
//                    mLiveRoom.setMirror(false); //后置改为非镜像
//                }
            }
        });


        // 初始化美颜按钮
        CircleImageView buttonBeauty = new CircleImageView(TUIKitLive.getAppContext());
        buttonBeauty.setImageResource(R.drawable.live_ic_beauty);
        buttonBeauty.setVisibility(mIsBeingLinkMic ? View.VISIBLE : View.GONE);
        buttonBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tiPanelLayout != null) {
                    tiPanelLayout.showBeautyPanel();
                }
            }
        });

        //初始化设置按钮
        CircleImageView buttonTask = new CircleImageView(TUIKitLive.getAppContext());
        buttonTask.setId(mRoomId);
        buttonTask.setImageResource(R.drawable.ic_button_live_task);
        buttonTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_TASK, "", ""));
            }
        });


        //初始化设置按钮
        CircleImageView buttonSetting = new CircleImageView(TUIKitLive.getAppContext());
        buttonSetting.setId(mRoomId);
        buttonSetting.setImageResource(R.drawable.live_setting_btn_icon);
        buttonSetting.setVisibility(mIsBeingLinkMic ? View.VISIBLE : View.GONE);
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_ANCHOR_SETTING, "",
                        GsonUtil.getInstance().toJson(new LiveSettingStateBean(isAudioMute, isVideoMute, isFrontCamera))));
            }
        });

        //抽奖
        CircleImageView buttonLottery = new CircleImageView(TUIKitLive.getAppContext());
        buttonLottery.setId(mRoomId);
        buttonLottery.setImageResource(R.drawable.ic_live_bottom_lottery_draw);
        buttonLottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLotteryDrawLayout();
            }
        });

        //横屏
        CircleImageView buttonOrientation = new CircleImageView(TUIKitLive.getAppContext());
        buttonOrientation.setId(mRoomId);
        buttonOrientation.setImageResource(R.drawable.ic_live_room_orientation);
        buttonOrientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeOrientation();
            }
        });

        /*// 初始化退出房间按钮
        CircleImageView buttonExitRoom = new CircleImageView(mContext);
        buttonExitRoom.setImageResource(R.drawable.live_exit_room);
        buttonExitRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitRoom();
                mLiveRoomAudienceDelegate.onClose();
            }
        });
        mLayoutBottomToolBar.setRightButtonsLayout(Arrays.asList(mButtonLink, buttonLike, buttonGift, buttonSwitchCam, buttonExitRoom));*/


        FrameLayout frameLayout = new FrameLayout(TUIKitLive.getAppContext());

        int iconSize = mContext.getResources().getDimensionPixelSize(R.dimen.live_bottom_toolbar_btn_icon_size);
        FrameLayout.LayoutParams mFunctionLayoutParams = new FrameLayout.LayoutParams(iconSize, iconSize);
        mFunctionLayoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;

        frameLayout.addView(buttonTask, mFunctionLayoutParams);


        mTvTask = new TextView(mContext);
        mTvTask.setTextColor(Color.parseColor("#ffffff"));
        mTvTask.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 7);
        mTvTask.setPadding(8, 3, 8, 3);
        mTvTask.setBackgroundResource(R.drawable.bg_round_audience_task_bg);
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
        //buttonList.add(buttonLike);
        buttonList.add(buttonGift);
        buttonList.add(buttonLottery);
        buttonList.add(buttonSetting);
        //buttonList.add(buttonOrientation);

        //mLayoutBottomToolBar.setRightButtonsLayout(Arrays.asList(buttonShare,buttonTask, mButtonLink, buttonLike, buttonGift, buttonSetting));
        mLayoutBottomToolBar.setRightButtonsLayout(buttonList);
    }

    public void showSharePanel() {
        mAnchorInfo = mLayoutTopToolBar.getAnchorInfo();
        if (mAnchorInfo != null) {
            ShareAnchorBean shareAnchorBean = new ShareAnchorBean();
            shareAnchorBean.setRoomId(String.valueOf(mRoomId));
            shareAnchorBean.setLiveTitle(TextUtils.isEmpty(liveTitle) ? mAnchorInfo.userName : liveTitle);
            shareAnchorBean.setLivePoster(TextUtils.isEmpty(livePoster) ? mAnchorInfo.avatarUrl : livePoster);
            shareAnchorBean.setAnchorId(mAnchorId);
            EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHARE_LIVE, "", GsonUtil.getInstance().toJson(shareAnchorBean)));
        }
    }

    public void clickLikeFrequencyControl() {
        if (mHeartLayout != null) {
            mHeartLayout.addFavor();
        }
        //点赞发送请求限制
        if (mLikeFrequencyControl == null) {
            mLikeFrequencyControl = new LikeFrequencyControl();
            mLikeFrequencyControl.init(8, 1);
        }
        if (mLikeFrequencyControl.canTrigger()) {
            if (mLiveRoom != null) {
                //向ChatRoom发送点赞消息
                mLiveRoom.sendRoomCustomMsg(String.valueOf(Constants.IMCMD_PRAISE), "", null);
            }
            if (onLiveOperateClickListener != null) {
                onLiveOperateClickListener.clickLikeFrequencyControl(mAnchorId);
            }
        }
    }

    private void startLinkMic() {
        if (("1").equals(isNoTalking)) {//已禁言
            ToastUtil.toastShortMessage(getString(R.string.banned));
            return;
        }
        linkState = 2;
        //mButtonLink.setEnabled(false);
        // mButtonLink.setImageResource(R.drawable.live_linkmic_off);
        mStatusTipsView.setText(R.string.live_wait_anchor_accept);
        mStatusTipsView.setVisibility(View.VISIBLE);
        mLiveRoom.requestJoinAnchor(getString(R.string.live_request_link_mic_anchor, mSelfUserId), REQUEST_LINK_MIC_TIME_OUT, new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (getActivity() == null) {
                    TUILiveLog.d(TAG, "getActivity is null");
                    return;
                }

                mStatusTipsView.setText("");
                mStatusTipsView.setVisibility(View.GONE);
                if (code == 0) {
                    joinPusher();
                    return;
                }
                if (code == -1) {
                    // 拒绝请求
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                } else if (code == -2) {
                    // 主播超时未响应
                    Toast.makeText(getActivity(), getString(R.string.live_request_time_out), Toast.LENGTH_SHORT).show();
                } else {
                    //出现错误
                    Toast.makeText(getActivity(), getString(R.string.live_error_request_link_mic, msg), Toast.LENGTH_SHORT).show();
                }
                mButtonLink.setEnabled(true);
                //                hideNoticeToast();
                mIsBeingLinkMic = false;
                mButtonLink.setImageResource(R.drawable.live_linkmic_on);
            }
        });
    }

    boolean isFrontCamera = true;
    LiveVideoView mSelfVideoView;
    ImageView mSelfImageView;

    private void joinPusher() {
        LiveVideoView videoView = mLayoutVideoManager.applyVideoView(mSelfUserId);
        if (videoView == null) {
            Toast.makeText(getActivity(), R.string.live_anchor_view_error, Toast.LENGTH_SHORT).show();
            return;
        }
        mSelfVideoView = videoView;
        mSelfImageView = new ImageView(getActivity());
        if (!isTiBeauty) {
            startTiBeauty();
        }

        mLiveRoom.startCameraPreview(true, videoView.getPlayerVideo(), new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code == 0) {
                    //添加水印
//                    TextView textView = new TextView(getActivity());
//                    textView.setText(TUIKitLive.getLoginUserInfo().getNickName());
//                    textView.setTextColor(Color.parseColor("#131415"));
//                    textView.setTextSize(100);
//                    textView.setMinWidth(100);
//                    textView.setMinHeight(50);
//                    Bitmap bitmap = LiveMessageSpan.view2Bitmap(textView);
//                    TRTCCloud.sharedInstance(getActivity()).setWatermark(bitmap, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG, 0.05F, 0.1F, (0.1f * TUIKitLive.getLoginUserInfo().getNickName().length()));
                    TRTCCloud mTRTCCloud = TRTCCloud.sharedInstance(getActivity());
                    mTRTCCloud.setGSensorMode(TRTC_GSENSOR_MODE_DISABLE);
                    TXTRTCLiveRoom.getInstance().setIsRecord(false);
                    mLiveRoom.startPublish("", new TRTCLiveRoomCallback.ActionCallback() {
                        @Override
                        public void onCallback(int code, String msg) {
                            isFrontCamera = true;
                            mLiveRoom.setMirror(true);
                            if (getActivity() == null) {
                                TUILiveLog.d(TAG, "getActivity is null");
                                return;
                            }
                            if (code == 0) {
                                mButtonLink.setEnabled(true);
                                mIsBeingLinkMic = true;
                                updateBottomFunctionLayout();
                                linkState = 3;
                                tryToCloseLinkPop();
                                pauseInteractTime();
                                if (isOpenCamera) {
                                    openLocalVideo();
                                } else {
                                    closeLocalVideo();
                                }
                            } else {
                                stopLinkMic();
                                mButtonLink.setEnabled(true);
                                mButtonLink.setImageResource(R.drawable.live_linkmic_on);
                                Toast.makeText(getActivity(), getString(R.string.live_fail_link_mic, msg), Toast.LENGTH_SHORT).show();
                            }
//                            TRTCCloud.sharedInstance(getActivity()).setVideoMuteImage(BitmapFactory.decodeResource(getResources(),R.drawable.ic_avatar),5);
//                            TRTCCloud.sharedInstance(getActivity()).muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG,true);
//                            TRTCCloud.sharedInstance(getActivity())
//                                    .setWatermark(BitmapFactory.decodeResource(getResources(),R.drawable.ic_avatar),TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SMALL,0.5F,0.5F,500);
                        }
                    });
                } else {
                    ToastUtil.toastShortMessage(code + msg);
                }
            }
        });
    }

    private void joinManagerPusher() {
        TRTCCloud mTRTCCloud = TRTCCloud.sharedInstance(getActivity());
        mTRTCCloud.setGSensorMode(TRTC_GSENSOR_MODE_DISABLE);
        TXTRTCLiveRoom.getInstance().setIsRecord(false);
        mLiveRoom.startPublish("", new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                isFrontCamera = true;
                mLiveRoom.setMirror(true);
                if (getActivity() == null) {
                    TUILiveLog.d(TAG, "getActivity is null");
                    return;
                }
                if (code == 0) {
                    mButtonLink.setEnabled(true);
                    mIsBeingLinkMic = true;
                    updateBottomFunctionLayout();
                    linkState = 3;
                    tryToCloseLinkPop();
                    pauseInteractTime();
                    if (isOpenCamera) {
                        openLocalVideo();
                    } else {
                        closeLocalVideo();
                    }
                } else {
                    stopLinkMic();
                    mButtonLink.setEnabled(true);
                    mButtonLink.setImageResource(R.drawable.live_linkmic_on);
                    Toast.makeText(getActivity(), getString(R.string.live_fail_link_mic, msg), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    boolean isTiBeauty = false;

    public void startTiBeauty() {
        TRTCCloud mTRTCCloud = TRTCCloud.sharedInstance(getActivity());
        isTiBeauty = true;
        //拓幻美颜
        //todo --- tillusory start ---
//        mTRTCCloud.setLocalVideoProcessListener(TRTCCloudDef.TRTC_VIDEO_PIXEL_FORMAT_Texture_2D, TRTCCloudDef.TRTC_VIDEO_BUFFER_TYPE_TEXTURE,
//                new TRTCCloudListener.TRTCVideoFrameListener() {
//                    @Override
//                    public void onGLContextCreated() {
//
//                    }
//
//                    @Override
//                    public int onProcessVideoFrame(TRTCCloudDef.TRTCVideoFrame srcFrame, TRTCCloudDef.TRTCVideoFrame dstFrame) {
//                        dstFrame.texture.textureId = TiSDKManager.getInstance().renderTexture2D(srcFrame.texture.textureId,
//                                srcFrame.width, srcFrame.height, TiRotation.fromValue(srcFrame.rotation), true);
//                        return 0;
//                    }
//
//                    @Override
//                    public void onGLContextDestory() {
//                        TiSDKManager.getInstance().destroy();
//                    }
//                });
        //todo --- tillusory end ---
        mTRTCCloud.setLocalVideoProcessListener(TRTCCloudDef.TRTC_VIDEO_PIXEL_FORMAT_NV21,
                TRTCCloudDef.TRTC_VIDEO_BUFFER_TYPE_BYTE_ARRAY,
                new TRTCCloudListener.TRTCVideoFrameListener() {
                    @Override
                    public void onGLContextCreated() {

                    }

                    @Override
                    public int
                    onProcessVideoFrame(TRTCCloudDef.TRTCVideoFrame srcFrame,
                                        TRTCCloudDef.TRTCVideoFrame dstFrame) {

                        TiSDKManager.getInstance().renderPixels(srcFrame.data,
                                TiSDKManager.FORMAT_NV21,
                                srcFrame.width,
                                srcFrame.height,
                                TiRotation.fromValue(srcFrame.rotation),
                                isFrontCamera
                        );

                        dstFrame.data = srcFrame.data;
                        return 0;
                    }

                    @Override
                    public void onGLContextDestory() {
                        TiSDKManager.getInstance().destroy();
                    }
                });
    }

    private void stopLinkMic() {
        mIsBeingLinkMic = false;
        linkState = 1;
        resumeInteractTime();
        mLiveRoom.stopCameraPreview();
        mLiveRoom.stopPublish(null);
        if (mLayoutVideoManager != null) {
            mLayoutVideoManager.recycleVideoView(mSelfUserId);
        }
        updateBottomFunctionLayout();
    }


    //展示礼物面板
    public void showGiftPanel(String url, String token) {
        giftPanelView = new GiftPanelViewImp(getContext(), url, token, mAnchorId, String.valueOf(mRoomId));
        ((GiftPanelViewImp) giftPanelView).setOnGiftPanelOperateListener(new GiftPanelViewImp.OnGiftPanelOperateListener() {
            @Override
            public void OnGiftPanel() {
                sendLiveEvent("OnGiftPanel", "");
            }

            @Override
            public void OnGiftLevelClick() {
                sendLiveEvent(LiveEventConstant.SHOW_MY_LIVE_LEVEL_PAGE, "2");
            }
        });
        ((GiftPanelViewImp) giftPanelView).setOnCoinNumberChangeListener(new GiftPanelViewImp.OnCoinNumberChangeListener() {
            @Override
            public void onCoinNumberChanged(TextView giftCoin) {
                LiveRoomAudienceFragment.giftCoin = giftCoin;
            }
        });
        GiftInfoDataHandler mGiftInfoDataHandler = new GiftInfoDataHandler();
        mGiftAdapter = new DefaultGiftAdapterImp();
        giftPanelView.init(mGiftInfoDataHandler, mGiftAdapter);
        giftPanelView.setGiftPanelDelegate(new GiftPanelDelegate() {
            @Override
            public void onGiftItemClick(GiftInfo giftInfo) {
                //sendGift(giftInfo);
                if (TextUtils.isEmpty(mAnchorId) || giftInfo == null) return;
                giftInfo.sendUser = mAnchorId;
                sendLiveEvent("onGiftItemClick", GsonUtil.GsonString(giftInfo));
            }

            @Override
            public void onChargeClick() {

            }
        });
        giftPanelView.show();
    }

    LiveGiftPanel liveGiftPanel;

    public void showGiftPanel() {
        liveGiftPanel = new LiveGiftPanel(getActivity(), String.valueOf(mRoomId), mAnchorId);
        liveGiftPanel.show();
        liveGiftPanel.setOnNewGiftListener(new LiveGiftPanel.OnNewGiftListener() {
            @Override
            public void doClickRedEnvelop() {
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_SEND_LIVE_RED_ENVELOPES, "", String.valueOf(mRoomId)));
            }

            @Override
            public void doRecharge() {
                sendLiveEvent("OnGiftPanel", "");
            }

            @Override
            public void doClickProgress() {
                sendLiveEvent(LiveEventConstant.SHOW_MY_LIVE_LEVEL_PAGE, "2");
            }

            @Override
            public void doOnclickRule() {
                sendLiveEvent(LiveEventConstant.SHOW_LIVE_BLIND_RULE, "");
            }
        });
    }

    //发送礼物消息出去同时展示礼物动画和弹幕
    public void sendGift(GiftInfo giftInfo) {
        GiftInfo giftInfoCopy = giftInfo.copy();
        giftInfoCopy.sendUser = mContext.getString(R.string.live_message_me);
        giftInfoCopy.sendUserHeadIcon = TUIKitLive.getLoginUserInfo().getFaceUrl();
        mGiftAnimatorLayout.show(giftInfoCopy);
        mLiveRoom.sendRoomCustomMsg(String.valueOf(Constants.IMCMD_GIFT), giftInfoCopy.giftId + "," + giftInfo.count, new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code != 0) {
                    Toast.makeText(TUIKitLive.getAppContext(), R.string.live_message_send_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateTopToolBar() {
        if (!isEnterRoom) {
            return;
        }
        mGetAudienceRunnable = new Runnable() {
            @Override
            public void run() {
                //updateTopAnchorInfo();
                updateTopAudienceInfo();
            }
        };
        //为了防止进房后立即获取列表不全，所以增加了一个延迟
        mHandler.postDelayed(mGetAudienceRunnable, 2000);
    }

    private void updateTopAnchorInfo() {
        mLiveRoom.getAnchorList(new TRTCLiveRoomCallback.UserListCallback() {
            @Override
            public void onCallback(int code, String msg, List<TRTCLiveRoomDef.TRTCLiveUserInfo> list) {
                if (code == 0) {
                    for (TRTCLiveRoomDef.TRTCLiveUserInfo info : list) {
                        if (info.userId.equals(mAnchorId)) {
                            mAnchorInfo.userId = info.userId;
                            mAnchorInfo.userName = info.userName;
                            mAnchorInfo.avatarUrl = info.avatarUrl;
                            mLayoutTopToolBar.setAnchorInfo(mAnchorInfo);
                            mLayoutVideoManager.updateAnchorOfflineViewBackground(mAnchorInfo.avatarUrl);
                        }
                    }
                } else {
                    Log.e(TAG, "code: " + code + " msg: " + msg + " list size: " + list.size());
                }
            }
        });
    }

    private void updateTopAudienceInfo() {
        mLiveRoom.getAudienceList(new TRTCLiveRoomCallback.UserListCallback() {
            @Override
            public void onCallback(int code, String msg, List<TRTCLiveRoomDef.TRTCLiveUserInfo> list) {
                if (code == 0) {
                    //addAudienceListLayout(list);
                }
            }
        });
    }


    //更新观看人数
    public void updateImWatchData(int size) {
        mLayoutTopToolBar.updateAudienceNumber(size);
    }

    //更新用户数量
    public void updateTopAudienceInfoFromPlatform(List<String> mIdList, List<String> mTopIdList) {

        if (mTopIdList != null && mTopIdList.size() >= 0) {
            setAudienceTopListLayout(mTopIdList);
        }

        if (mIdList != null && mIdList.size() >= 0) {
            addAudienceListLayout(mIdList);
        }
    }

    public void addAudienceListLayout(List<String> list) {
        mLayoutTopToolBar.addAudienceListUser(list);
    }

    public void addAudienceListLayout(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        mLayoutTopToolBar.addAudienceListUser(userInfo);
    }

    public void removeAudienceListLayout(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        mLayoutTopToolBar.removeAudienceUser(userInfo);
    }

    public void updateIMMessageList(ChatEntity entity) {
        mLayoutChatMessage.addMessageToList(entity);
    }

    public void updateIMMessageListStartAnim(ChatEntity entity) {
        //mLayoutChatMessage.addMessageToFooterList(entity);

    }

    private void setAudienceTopListLayout(List<String> list) {
        mLayoutTopToolBar.setAudienceTopListUser(list);
    }


    /**
     * 处理弹幕消息
     */
    public void handleDanmuMsg(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, String text) {
        //updateIMMessageList(IMUtils.handleTextMsg(userInfo, text));
        if (mDanmakuManager != null) {
            //这里暂时没有头像，所以用默认的一个头像链接代替
            mDanmakuManager.addDanmu(userInfo.avatarUrl, userInfo.userName, text);
        }
    }

    /**
     * 处理礼物消息
     */
    private void handleGiftMsg(final TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, final String message) {
        Log.d("logutil", "message = " + message);
        String type = GsonUtil.getType(message);
        if (type.equals("Number") || type.equals("String")) {
            ToastUtil.toastShortMessage("无效的JSON格式");
            return;
        }
        GiftMessage giftMessage = GsonUtil.GsonToBean(message, GiftMessage.class);
        if (giftMessage != null && giftMessage.costomMassageType.equals("sendGiftAvChatRoom")) {
            GiftMessage.ExtraInfo extraInfo = giftMessage.extra;
            if (extraInfo != null) {
                GiftInfo giftInfo = new GiftInfo();
                giftInfo.sendUserHeadIcon = userInfo.avatarUrl;
                giftInfo.sendUser = !TextUtils.isEmpty(userInfo.userName) ? userInfo.userName : userInfo.userId;
                giftInfo.count = !TextUtils.isEmpty(extraInfo.gift_count) ? Integer.valueOf(extraInfo.gift_count) : 0;
                giftInfo.title = !TextUtils.isEmpty(extraInfo.content) ? extraInfo.content : "";
                String giftLottieStatus = !TextUtils.isEmpty(extraInfo.gift_lottie_status) ? extraInfo.gift_lottie_status : "";
                giftInfo.giftLottieStatus = Integer.valueOf(giftLottieStatus);
                giftInfo.lottieUrl = !TextUtils.isEmpty(extraInfo.gift_lottieurl) ? extraInfo.gift_lottieurl : "";
                giftInfo.giftPicUrl = extraInfo.gift_image;
                giftInfo.svgaUrl = extraInfo.gift_svgaurl;
                mGiftAnimatorLayout.show(giftInfo);
                mLayoutTopToolBar.updateCurrentBeansCount(extraInfo.beans_current_count);
                if (V2TIMManager.getInstance().getLoginUser().equals(giftMessage.extra.uid)) {
                    if (liveGiftPanel != null && liveGiftPanel.isShowing()) {
                        liveGiftPanel.updateCoin(extraInfo.rich_beans);
                        liveGiftPanel.getMyUserLevelDetail();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isTicket && !isBuyTicket) {
            closeRoom();
        } else {
            showFloatView();
        }
    }

    void showFloatView() {
//        if (mDanmakuManager != null) {
//            mDanmakuManager.destroy();
//            mDanmakuManager = null;
//        }
        updateLiveWindowMode(Constants.WINDOW_MODE_FLOAT);
    }

    void showNewFloatView() {

    }


    protected void updateLiveWindowMode(int mode) {
        if (FloatWindowLayout.getInstance().mWindowMode == mode) return;

        if (mode == Constants.WINDOW_MODE_FLOAT) {
            if (getActivity() == null) {
                return;
            }
            FloatWindowLayout.IntentParams intentParams = new FloatWindowLayout.IntentParams(getActivity().getClass(), mRoomId, mAnchorId, isUseCDNPlay, mCdnUrl);
            boolean result = FloatWindowLayout.getInstance().showFloatWindow(mLayoutVideoManager.mLayoutRoot, intentParams);
            if (!result) {
                exitRoom();
                return;
            }
            FloatWindowLayout.getInstance().setOnTouchFloatWindowListener(new FloatWindowLayout.OnTouchFloatWindowListener() {
                @Override
                public void onTouchUpFloatWindow() {
                    if (mOnTouchFloatWindowListener != null) {
                        mOnTouchFloatWindowListener.onTouchUpFloatWindow();
                    }
                }
            });
            FloatWindowLayout.getInstance().mWindowMode = mode;
            mLayoutVideoManager.updateVideoLayoutByWindowStatus();
            FloatWindowLayout.getInstance().setFloatWindowLayoutDelegate(new FloatWindowLayout.FloatWindowLayoutDelegate() {
                @Override
                public void onClose() {
                    exitRoom();
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
            });
            getActivity().moveTaskToBack(true);
        } else if (mode == Constants.WINDOW_MODE_FULL) {
            FloatWindowLayout.getInstance().closeFloatWindow();
            FloatWindowLayout.getInstance().mWindowMode = mode;
            isFloatRecovery = true;
        }
    }

    //  判断activity是否为空
    public static boolean isActivityFinish(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return true;
        }
        return false;
    }

    /**
     * 显示错误并且退出直播
     *
     * @param errorCode
     * @param errorMsg
     */
    protected void showErrorAndQuit(int errorCode, String errorMsg) {
        EventBus.getDefault().post(new AppLiveDialogEvent(errorMsg, "showErrorAndQuit", errorCode));
    }

    @Override
    public void onClickAnchorAvatar() {
        showUserCard(mAnchorId);
    }

    @Override
    public void onClickFollow(TRTCLiveRoomDef.LiveAnchorInfo liveAnchorInfo) {
        if (liveAnchorInfo != null) {
            if (onLiveOperateClickListener != null) {
                onLiveOperateClickListener.followAnchor(liveAnchorInfo.userId, mLayoutTopToolBar);
            }
        } else {
            ToastUtil.toastShortMessage("关注的用户不存在");
        }

    }

    @Override
    public void onClickAudience(TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo) {
        if (onLiveOperateClickListener != null) {
            onLiveOperateClickListener.onClickAudience(audienceInfo);
        }
    }

    @Override
    public void onClickOnlineNum() {

    }

    @Override
    public void onClickOnlineUser() {
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_ONLINE_USER, "", mAnchorId));
    }

    @Override
    public void onClickTopRank() {
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_TOP_RANK, "", ""));
    }

    @Override
    public void onClickClose() {

    }

    public static LiveRoomAudienceFragment newInstance(int roomId, String anchorId, boolean useCdn, String cdnURL) {
        LiveRoomAudienceFragment liveRoomAudienceFragment = new LiveRoomAudienceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ROOM_ID, roomId);
        bundle.putString(Constants.ANCHOR_ID, anchorId);
        bundle.putBoolean(Constants.USE_CDN, useCdn);
        bundle.putString(Constants.CDN_URL, cdnURL);
        liveRoomAudienceFragment.setArguments(bundle);
        return liveRoomAudienceFragment;
    }

    public void onAt(String at) {
        if (!TextUtils.isEmpty(at) && mLayoutBottomToolBar != null) {
            mLayoutBottomToolBar.onAt(at);
        }
    }

    public interface OnLiveOperateClickListener {
        void onMoreLiveClick();

        void onLiveSetHeadViewInfo(TopToolBarLayout mLayoutTopToolBar, String mAnchorId);

        void onLiveStarNumber(View view, String anchor_id);

        void onClickAnchorAvatar(String uid, String anchorId);

        void onClickAudience(TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo);

        void followAnchor(String uid, TopToolBarLayout mLayoutTopToolBar);

        void showGiftPanel();

        void reportUser(String selfId, String uid);

        void onClickContestNo(View v, String anchor_id);

        void clickLikeFrequencyControl(String anchor_id);

        void closeLive(String uid);
    }

    public void setOnLiveOperateClickListener(OnLiveOperateClickListener onLiveOperateClickListener) {
        this.onLiveOperateClickListener = onLiveOperateClickListener;
    }

    public OnLiveOperateClickListener onLiveOperateClickListener;

    public void setmOnTouchFloatWindowListener(FloatWindowLayout.OnTouchFloatWindowListener mOnTouchFloatWindowListener) {
        this.mOnTouchFloatWindowListener = mOnTouchFloatWindowListener;
    }

    private FloatWindowLayout.OnTouchFloatWindowListener mOnTouchFloatWindowListener;

    public interface OnAnchorEnterListener {
        void OnAnchorEnter(String anchor_uid);
    }

    public void setOnAnchorEnterListener(OnAnchorEnterListener onAnchorEnterListener) {
        this.onAnchorEnterListener = onAnchorEnterListener;
    }

    private OnAnchorEnterListener onAnchorEnterListener;

    void initListener() {
        mLayoutChatMessage.setOnChatLayoutItemClickListener(new ChatLayout.OnChatLayoutItemClickListener() {
            @Override
            public void onChatUserItemClick(String uid) {
                showUserCard(uid);
            }
        });
    }

    public void sendLiveEvent(String type, String data) {
        EventBus.getDefault().post(new LiveMethodEvent(type, "", data));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveLiveEvent(LiveMessageBean event) {
        if (TextUtils.isEmpty(event.getMessage().getGroupID())) { //单聊
            handlerSingleCustomLiveMessage(event);
        } else {//群聊
            handlerGroupCustomLiveMessage(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveLiveFollowEvent(AppLiveFollowEvent event) {
        String type = event.type;
        if (type.equals("unFollowUserById")) { //取消关注
            mLayoutTopToolBar.setHasFollowed(false);
        } else if (type.equals("followUserById")) {//没关注
            mLayoutTopToolBar.setHasFollowed(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveGiftChangedItemNotifyEvent(AppLiveGiftChangedEvent event) {
        ((GiftPanelViewImp) giftPanelView).notifyGiftController(event.changedNo, event.changedInfo);
    }

    /**
     * 单聊自定义消息
     *
     * @param event
     */
    private void handlerSingleCustomLiveMessage(LiveMessageBean event) {
        if (!TextUtils.isEmpty(event.getCloudCustomData())) {//
            Log.e(TAG, event.getCloudCustomData());
            try {
                JSONObject jsonObject = new JSONObject(event.getCloudCustomData());
                String msgType = jsonObject.optString("costomMassageType");
                if (!TextUtils.isEmpty(msgType)) {
                    if (msgType.equals(USER_DATA)) {//更新用户信息
                        LiveUserData liveUserData = GsonUtil.GsonToBean(event.getCloudCustomData(), LiveUserData.class);
                        if (liveUserData.getActionType().equals("1")) {
                            setIs_group_admin(liveUserData.getIs_group_admin());
                        } else if (liveUserData.getActionType().equals("2")) {
                            setIsNoTalking(liveUserData.getSetNotalking());
                        } else if (liveUserData.getActionType().equals("3")) {
                            // mLayoutTopToolBar.updateCurrentBeansCount(liveUserData.getRich_beans());
                            if (giftCoin != null) {
                                giftCoin.setText(liveUserData.getRich_beans());
                            }
                            wealth_val_switch = liveUserData.getWealth_val_switch();
                            wealth_val = liveUserData.getWealth_val();
                            audienceLevel = liveUserData.getUser_level();
                            mLiveMessageSignBean.setUser_level(liveUserData.getUser_level());
                        } else if ("4".equals(liveUserData.getActionType())) {
                            taskSchedule = liveUserData.getComplete_schedule();
                            updateTaskScheduleView();
                        } else if ("5".equals(liveUserData.getActionType())) {
                            ToastUtil.toastShortMessage("你已被踢出直播间");
                            if (Constents.isShowAudienceFloatWindow) {
                                FloatWindowLayout.getInstance().closeFloatWindow();
                            } else {
                                closeRoom();
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 群聊自定义消息
     *
     * @param event
     */
    private void handlerGroupCustomLiveMessage(LiveMessageBean event) {
        if (!event.getMessage().getGroupID().equals(String.valueOf(mRoomId))) { //过滤掉非本直播间的消息
            return;
        }
        LiveMessageCustomDataBean liveMessageCustomDataBean = GsonUtil.GsonToBean(event.getCustomElemData(), LiveMessageCustomDataBean.class);
        if (liveMessageCustomDataBean.getCommand().equals(String.valueOf(Constants.IMCMD_CUSTOM))) { //233
            if (!TextUtils.isEmpty(event.getCloudCustomData())) {//
                Log.e(TAG, event.getCloudCustomData());
                try {
                    JSONObject jsonObject = new JSONObject(event.getCloudCustomData());
                    String msgType = jsonObject.optString("costomMassageType");
                    if (!TextUtils.isEmpty(msgType)) {
//                        if (msgType.equals(USER_DATA)) {//更新用户信息
//                            TimCustomMessage timCustomMessage = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), TimCustomMessage.class);
//                            ChatEntity chatEntity = IMUtils.handleUserData(timCustomMessage);
//                            updateIMMessageList(chatEntity);
//                            setIs_group_admin(timCustomMessage.is_group_admin);
//                            setIsNoTalking(timCustomMessage.setNotalking);
//                        } else
                        if (msgType.equals(JOIN_ROOM_MSG)) {//加入直播间(text/nickName/senderUserID)
                            CustomMessage joinMsg = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
                            LiveJoinAvChatRoom liveJoinAvChatRoom = GsonUtil.GsonToBean(event.getCloudCustomData(), LiveJoinAvChatRoom.class);
                            String nickname = !TextUtils.isEmpty(event.getMessage().getNickName()) ? event.getMessage().getNickName() : event.getMessage().getSender();
                            String sendId = !TextUtils.isEmpty(event.getMessage().getSender()) ? event.getMessage().getSender() : event.getMessage().getSender();
                            mLayoutTopToolBar.updateContestNo(liveJoinAvChatRoom.getAnchor_info().getHour_ranking());
                            if (!"1".equals(liveJoinAvChatRoom.getShowType())) {
                                showJoinInfo(IMUtils.joinRoom(joinMsg, liveJoinAvChatRoom, nickname, sendId));
                            }
                            if (event.getMessage().getSender().equals(V2TIMManager.getInstance().getLoginUser())) {
                                setIs_group_admin(liveJoinAvChatRoom.getIs_group_admin());
                                String mute = ("1".equals(liveJoinAvChatRoom.getSetNotalking()) || "1".equals(liveJoinAvChatRoom.getIs_mute())) ? "1" : "0";
                                setIsNoTalking(mute);
                                wealth_val = joinMsg.getWealth_val();
                                wealth_val_switch = joinMsg.getWealth_val_switch();
                                audienceLevel = liveJoinAvChatRoom.getUser_level();
                                anchorLevel = liveJoinAvChatRoom.getAnchor_level();
                                mLiveMessageSignBean.setAnchor_level(liveJoinAvChatRoom.getAnchor_level());
                                mLiveMessageSignBean.setUser_level(liveJoinAvChatRoom.getUser_level());
                                mLiveMessageSignBean.setFanclub_card(liveJoinAvChatRoom.getFanclub_card());
                                mLiveMessageSignBean.setFanclub_level(liveJoinAvChatRoom.getFanclub_level());
                                mLiveMessageSignBean.setFanclub_status(liveJoinAvChatRoom.getFanclub_status());
                                live_user_role = joinMsg.getLive_user_role();
                                is_admin = liveJoinAvChatRoom.getIs_admin();
                                name_color = joinMsg.getName_color();
                                backgroundColor = joinMsg.getBackground_color();
                                livePoster = liveJoinAvChatRoom.getAnchor_info().getLive_poster();
                                liveTitle = liveJoinAvChatRoom.getAnchor_info().getLive_title();
                                mLayoutTopToolBar.updateDurationTime(liveJoinAvChatRoom.getAnchor_info().getLive_time());
                                if ("1".equals(liveJoinAvChatRoom.getIs_interaction())) { //当前房间为互动房 开始倒计时
                                    interactLimitTime = liveJoinAvChatRoom.getInteraction_time();
                                    interactTip = liveJoinAvChatRoom.getInteraction_tips();
                                    startInteractTime();
                                }
                                if (liveJoinAvChatRoom.getPkInfo().getIs_pk() == 1) {
                                    showPkView(liveJoinAvChatRoom.getPkInfo());
                                }
                                taskSchedule = liveJoinAvChatRoom.getComplete_schedule();
                                updateTaskScheduleView();
                                if (!TextUtils.isEmpty(liveJoinAvChatRoom.getManager_link_list())) {
                                    refreshLinkManager(liveJoinAvChatRoom.getManager_link_list());
                                }
                                getLiveRedEnvelopes();
                            }
                        } else if (msgType.equals(PROMPT_MSG)) {//公告栏(text/nickName)
                            CustomMessage timCustomRoom = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
                            String nickname = !TextUtils.isEmpty(event.getMessage().getNickName()) ? event.getMessage().getNickName() : event.getMessage().getSender();
                            String selfUserId = V2TIMManager.getInstance().getLoginUser();
                            if (!jsonObject.isNull("touid")) {
                                String touid = jsonObject.optString("touid");
                                if (TextUtils.isEmpty(touid) || selfUserId.equals(touid)) { //自身显示
                                    updateIMMessageList(IMUtils.handlePrompt(timCustomRoom, event.getMessage()));
                                }
                            }
                        } else if (msgType.equals(CAVEAT)) {//警告
                            TimCustomMessage timCustomMessage = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), TimCustomMessage.class);
                            EventBus.getDefault().post(timCustomMessage);
                        } else if (msgType.equals(WATCH_ROOM)) {
                            Log.d("logUtil", event.getMessage().getGroupID() + " -- " + event.getCloudCustomData());
                            LiveWatchAvChatRoom watchAvChatRoom = GsonUtil.GsonToBean(event.getCloudCustomData(), LiveWatchAvChatRoom.class);
                            updateImWatchData(watchAvChatRoom.getWatchsum());
                            updateTopAudienceInfoFromPlatform(watchAvChatRoom.getWatchuser(), watchAvChatRoom.getTop_gift_uid());
                        } else if (msgType.equals(CHANGE_ROOM)) {
                            Log.e(TAG, event.getCloudCustomData());
                            LiveChangeInfoChatRoom liveChangeInfoChatRoom = GsonUtil.GsonToBean(event.getCloudCustomData(), LiveChangeInfoChatRoom.class);
                            switch (liveChangeInfoChatRoom.getActionType()) {
                                case "1":
                                    mLayoutTopToolBar.updateCurrentBeansCount(liveChangeInfoChatRoom.getBeans_current_count());
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
//                                case "5":
//                                    if (TextUtils.isEmpty(liveChangeInfoChatRoom.getLink_mic_num()) || "0".equals(liveChangeInfoChatRoom.getLink_mic_num())) {
//                                        tvLinkMic.setVisibility(View.GONE);
//                                    } else {
//                                        tvLinkMic.setVisibility(View.VISIBLE);
//                                        tvLinkMic.setText(liveChangeInfoChatRoom.getLink_mic_num() + "人连麦中");
//                                    }
//                                    break;
                                case "6":
                                    if (!jsonObject.isNull("touid")) {
                                        String touid = jsonObject.optString("touid");
                                        if (TextUtils.isEmpty(touid) || mSelfUserId.equals(touid)) { //自身显示
                                            audienceLevel = liveChangeInfoChatRoom.getUser_level();
                                            anchorLevel = liveChangeInfoChatRoom.getAnchor_level();
                                            mLiveMessageSignBean.setAnchor_level(liveChangeInfoChatRoom.getAnchor_level());
                                            mLiveMessageSignBean.setUser_level(liveChangeInfoChatRoom.getUser_level());
                                        }
                                    }
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
                                case "10":
                                    if (!jsonObject.isNull("touid")) {
                                        String touid = jsonObject.optString("touid");
                                        if (TextUtils.isEmpty(touid) || mSelfUserId.equals(touid)) { //自身显示
                                            mLiveMessageSignBean.setAnchor_level(liveChangeInfoChatRoom.getAnchor_level());
                                            mLiveMessageSignBean.setUser_level(liveChangeInfoChatRoom.getUser_level());
                                            mLiveMessageSignBean.setFanclub_card(liveChangeInfoChatRoom.getFanclub_card());
                                            mLiveMessageSignBean.setFanclub_level(liveChangeInfoChatRoom.getFanclub_level());
                                            mLiveMessageSignBean.setFanclub_status(liveChangeInfoChatRoom.getFanclub_status());
                                        }
                                    }
                                    break;
                            }
                        } else if (msgType.equals(ROOM_STATE_PK)) {
                            PkChatRoomBean pkChatRoomBean = GsonUtil.GsonToBean(event.getCloudCustomData(), PkChatRoomBean.class);
                            if ("0".equals(pkChatRoomBean.getActionType())) {
                                if (pkChatRoomBean.getIs_reset().equals("1")) {
                                    mPkViewLayout.clearPkStatus();
                                    mPkViewLayout.initPkData(pkChatRoomBean.getPkInfo());
                                } else {
                                    mPkViewLayout.initPkData(pkChatRoomBean.getPkInfo());
                                }
                            }
                        } else if (msgType.equals(LIVE_MANAGER_CHANGE)) {
//                            Log.d("aha",event.getCloudCustomData());
                            LiveManagerChangeBean liveManagerChangeBean = GsonUtil.GsonToBean(event.getCloudCustomData(), LiveManagerChangeBean.class);
                            if (liveManagerChangeBean != null) {
                                if ("0".equals(liveManagerChangeBean.getActionType())) {
                                    String managerLinkStr = liveManagerChangeBean.getManager_link_list();
                                    refreshLinkManager(managerLinkStr);
                                }
                            }

                        } else if (msgType.equals(LIVE_RED_ENVELOPES_CHANGE)) {
                            String actionType = jsonObject.optString("actionType");
                            if ("0".equals(actionType)) {
                                getLiveRedEnvelopes();
                                String showType = jsonObject.optString("showType");
                                if (!"1".equals(showType)) {
                                    CustomMessage customMessage = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
                                    String nickname = !TextUtils.isEmpty(event.getMessage().getNickName()) ? event.getMessage().getNickName() : event.getMessage().getSender();
                                    String sendId = !TextUtils.isEmpty(event.getMessage().getSender()) ? event.getMessage().getSender() : event.getMessage().getSender();
                                    updateIMMessageList(handleCustomTextMsg(nickname, sendId, customMessage));
                                }
                            }
                        } else if (msgType.equals(LIVE_ANIMATION_MSG)) {
                            Log.v("LIVE_ANIMATION_MSG", liveMessageCustomDataBean.getMessage() + "");
                            String svgaUrL = jsonObject.optString("gift_svgaurl");
                            Log.v("LIVE_ANIMATION_MSG_URL", svgaUrL + "");
                            if (!TextUtils.isEmpty(svgaUrL)) {
                                mGiftAnimatorLayout.showAnmation(svgaUrL);
                            }
                        } else {
                            String showType = jsonObject.optString("showType");
                            if (!"1".equals(showType)) {
                                CustomMessage customMessage = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
                                String nickname = !TextUtils.isEmpty(event.getMessage().getNickName()) ? event.getMessage().getNickName() : event.getMessage().getSender();
                                String sendId = !TextUtils.isEmpty(event.getMessage().getSender()) ? event.getMessage().getSender() : event.getMessage().getSender();
                                updateIMMessageList(handleCustomTextMsg(nickname, sendId, customMessage));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, liveMessageCustomDataBean.getMessage());
                CustomMessage customMessage = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
                String nickname = !TextUtils.isEmpty(event.getMessage().getNickName()) ? event.getMessage().getNickName() : event.getMessage().getSender();
                String sendId = !TextUtils.isEmpty(event.getMessage().getSender()) ? event.getMessage().getSender() : event.getMessage().getSender();
                updateIMMessageList(handleCustomTextMsg(nickname, sendId, customMessage));
            }
        } else if (liveMessageCustomDataBean.getCommand().

                equals(String.valueOf(Constants.IMCMD_ANNOUNCEMENT))) {
            LiveAnnouncementBean.MessageBean messageBean = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), LiveAnnouncementBean.MessageBean.class);
            mLayoutTopToolBar.showLiveAnnouncement(messageBean);
        } else if (liveMessageCustomDataBean.getCommand().

                equals(String.valueOf(Constants.IMCMD_DANMU_NEW))) {
            //Log.d("danmaku",liveMessageCustomDataBean.getMessage());
            CustomMessage customMessage = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
            String nickname = !TextUtils.isEmpty(event.getMessage().getNickName()) ? event.getMessage().getNickName() : event.getMessage().getSender();
            String sendId = !TextUtils.isEmpty(event.getMessage().getSender()) ? event.getMessage().getSender() : event.getMessage().getSender();
            updateIMMessageList(handleCustomTextMsg(nickname, sendId, customMessage));
        }

    }

    //关闭窗口
    public void finishActivity() {
        //防止窗口泄露事件
        if (isActivityFinish(getActivity())) {
            return;
        } else {
            if (getActivity() != null) getActivity().finish();
        }
    }

    NormalTipDialog normalTipDialog;

    void showAnchorInviteLinkPop(final String inviteId) {
        //final NormalTipDialog normalTipDialog = new NormalTipDialog();
        if (normalTipDialog == null) {
            normalTipDialog = new NormalTipDialog();
        } else {
            if (normalTipDialog.isShowing()) {
                return;
            }
        }
        normalTipDialog.setMessage("主播邀请你连麦");
        normalTipDialog.setNegativeStr("拒绝");
        normalTipDialog.setPositiveStr("同意");
        normalTipDialog.setNegativeClickListener(new NormalTipDialog.NegativeClickListener() {
            @Override
            public void onClick() {
                ToastUtil.toastShortMessage("拒绝");
                // mLiveRoom.responseJoinAnchor(mAnchorId, false, "观众拒绝了你的连麦邀请");
                String data = IMProtocol.getJoinRspJsonStr(TUIKitLive.getLoginUserInfo().getNickName() + " 拒绝了你的连麦邀请");
                V2TIMManager.getSignalingManager().reject(inviteId, data, null);
                normalTipDialog.dismiss();
            }
        });
        normalTipDialog.setPositiveClickListener(new NormalTipDialog.PositiveClickListener() {
            @Override
            public void onClick() {
                ToastUtil.toastShortMessage("同意");
                startLinkMic("auto");
                normalTipDialog.dismiss();
            }
        });
        normalTipDialog.show(getActivity().getSupportFragmentManager(), "showAnchorInviteLinkPop");
    }

    private void startLinkMic(final String reason) {
        if (("1").equals(isNoTalking)) {//已禁言
            ToastUtil.toastShortMessage(getString(R.string.banned));
            return;
        }
        mButtonLink.setEnabled(false);
        mButtonLink.setImageResource(R.drawable.live_linkmic_off);
        mStatusTipsView.setText(R.string.live_wait_anchor_accept);
        mStatusTipsView.setVisibility(View.VISIBLE);
        mLiveRoom.requestJoinAnchor(reason, REQUEST_LINK_MIC_TIME_OUT, new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (getActivity() == null) {
                    TUILiveLog.d(TAG, "getActivity is null");
                    Toast.makeText(getActivity(), "getActivity is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                mStatusTipsView.setText("");
                mStatusTipsView.setVisibility(View.GONE);
                if (code == 0) {
                    //joinPusher();
                    //joinManagerPusher();
                    if (reason.equals("manager")) {
                        reportLinkManager();
                    } else {
                        joinPusher();
                    }
                    return;
                }
                if (code == -1) {
                    // 拒绝请求
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                } else if (code == -2) {
                    // 主播超时未响应
                    Toast.makeText(getActivity(), getString(R.string.live_request_time_out), Toast.LENGTH_SHORT).show();
                } else {
                    //出现错误
                    Toast.makeText(getActivity(), getString(R.string.live_error_request_link_mic, msg), Toast.LENGTH_SHORT).show();
                }
                linkState = 1;
                mButtonLink.setEnabled(true);
                //                hideNoticeToast();
                mIsBeingLinkMic = false;
                mButtonLink.setImageResource(R.drawable.live_linkmic_on);
            }
        });
    }

    /**
     * 显示确认消息
     *
     * @param msg    消息内容
     * @param （必须退出） false提示消息（可选择是否退出）
     */
    public void showFloatPermissionDialog(String msg) {
        final ExitConfirmDialog dialogFragment = new ExitConfirmDialog();
        dialogFragment.setCancelable(false);
        dialogFragment.setMessage(msg);

        if (dialogFragment.isAdded()) {
            dialogFragment.dismiss();
            return;
        }

        dialogFragment.setPositiveClickListener(new ExitConfirmDialog.PositiveClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();
                showFloatView();
            }
        });

        dialogFragment.setNegativeClickListener(new ExitConfirmDialog.NegativeClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();
            }
        });
        dialogFragment.show(getFragmentManager(), "showInfoDialog");
    }

    public void showUserCard(String uid) {
        if (!TextUtils.isEmpty(uid)) {
            TimCustomMessage timRoomMessageBean = new TimCustomMessage();
            timRoomMessageBean.setUid(uid);
            timRoomMessageBean.setRoomId(TextUtils.isEmpty(String.valueOf(mRoomId)) ? String.valueOf(mRoomId) : "");
            timRoomMessageBean.setIs_group_admin(!TextUtils.isEmpty(is_group_admin) ? is_group_admin : "");
            timRoomMessageBean.setIs_admin(!TextUtils.isEmpty(is_admin) ? is_admin : "");
            if (!TextUtils.isEmpty(mAnchorId)) {
                timRoomMessageBean.setAnchor_uid(!TextUtils.isEmpty(mAnchorId) ? mAnchorId : "");
            }
            String data = GsonUtil.GsonString(timRoomMessageBean);
            sendLiveEvent(LiveEventConstant.CLICK_LIVE_USER, data);
        }
    }

    int linkState = 1;
    boolean isOpenCamera = true;

    void showLinkMicUserPop() {
        LinkMicStateBean linkMicStateBean = new LinkMicStateBean();
        linkMicStateBean.setAnchorId(mAnchorId);
        linkMicStateBean.setCameraOpen(isOpenCamera);
        linkMicStateBean.setLinkState(linkState);
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LINK_MIC_USER, "", GsonUtil.getInstance().toJson(linkMicStateBean)));
    }

    public void doCamera(boolean isOpen) {
        if (isOpen) {
            if (linkState == 3) {
                openLocalVideo();
            } else {
                isOpenCamera = true;
            }
        } else {
            if (linkState == 3) {
                closeLocalVideo();
            } else {
                isOpenCamera = false;
            }
        }
    }

    public void doLinkAnchor(int type) {
        switch (type) {
            case 1: //发起连麦申请
                if ("1".equals(is_admin) || "1".equals(is_group_admin)) {
                    showLinkTypePop();
                } else {
                    tryToStartLink();
                }
                break;
            case 2: //取消连麦申请
                cancelLink();
                break;
            case 3: //关闭连麦
                stopLinkMic();
                break;
        }
    }

    void tryToStartLink() {
        if (getActivity() == null) {
            TUILiveLog.d(TAG, "getActivity is null");
            return;
        }
        if (!mIsBeingLinkMic) {
            long curTime = System.currentTimeMillis();
            if (curTime < mLastLinkMicTime + LINK_MIC_INTERVAL) {
                Toast.makeText(getActivity(), R.string.live_tips_rest, Toast.LENGTH_SHORT).show();
            } else {
                mLastLinkMicTime = curTime;
                requestPermissions(PermissionUtils.getLivePermissions(), new OnPermissionGrandCallback() {
                    @Override
                    public void onAllPermissionsGrand() {
                        startLinkMic();
                    }
                });
            }
        } else {
            stopLinkMic();
        }
    }

    void cancelLink() {
        linkState = 1;
        mLiveRoom.cancelRequestJoinAnchor("取消了连麦请求", new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                ToastUtil.toastShortMessage("取消连麦申请");
            }
        });
    }

    void tryToCloseLinkPop() {
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.HIDE_LINK_MIC_USER, "", ""));
    }

    boolean isInteractPause = false;
    Handler handler;
    Runnable runnable;
    int interactTime = 0;
    int interactLimitTime = 60;
    String interactTip = "";

    public void startInteractTime() {
        handler = new Handler(Looper.myLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!isInteractPause) {
                    handler.postDelayed(this, 1000);
                    interactTime++;
                    if (interactTime >= interactLimitTime) {
                        showInteractTipPop();
                    }
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void pauseInteractTime() {
        isInteractPause = true;
        if (liveInteractTipDialog != null && liveInteractTipDialog.isShowing()) {
            liveInteractTipDialog.dismiss();
        }
    }

    public void resumeInteractTime() {
        isInteractPause = false;
        interactTime = 0;
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 1000);
        }
    }

    public void stopInteractTime() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    LiveInteractTipDialog liveInteractTipDialog;

    void showInteractTipPop() {
        if (Constents.isShowAudienceFloatWindow) {
            return;
        }
        pauseInteractTime();
        // ToastUtil.toastLongMessage("该房间为互动房间，请保持与主播互动哦");
        //resumeInteractTime();
        liveInteractTipDialog = new LiveInteractTipDialog();
        liveInteractTipDialog.setMessage(interactTip);
        liveInteractTipDialog.setNegativeStr("退出");
        liveInteractTipDialog.setPositiveStr("发送");
        liveInteractTipDialog.show(getFragmentManager(), "interactDialog");
        liveInteractTipDialog.setNegativeClickListener(new LiveInteractTipDialog.NegativeClickListener() {
            @Override
            public void onClick() {
                liveInteractTipDialog.dismiss();
                exitRoom();
                finishActivity();
            }
        });
//        liveInteractTipDialog.setPositiveClickListener(new LiveInteractTipDialog.PositiveClickListener() {
//            @Override
//            public void onClick(String msg) {
//                sendTextMessage(msg);
//                liveInteractTipDialog.dismiss();
//            }
//        });
    }


//    void sendTextMessage(final String msg) {
//        //点击发送按钮
//        V2TIMManager.getInstance().getUsersInfo(Arrays.asList(V2TIMManager.getInstance().getLoginUser()), new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
//            @Override
//            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
//                resumeInteractTime();
//                V2TIMUserFullInfo v2TIMUserFullInfo = v2TIMUserFullInfos.get(0);
//                final ChatEntity entity = new ChatEntity();
//                String anchorUserName = !TextUtils.isEmpty(v2TIMUserFullInfo.getNickName()) ? v2TIMUserFullInfo.getNickName() : "";
//                entity.setSenderName(anchorUserName);
//                entity.setContent(msg);
//                entity.setLevel(-1);
//                entity.setType(Constants.TEXT_TYPE);
//                entity.setUid(V2TIMManager.getInstance().getLoginUser());
//                //增加场控/vip标识
//                String admin = !TextUtils.isEmpty(is_group_admin) ? is_group_admin : "0";
//                entity.setIs_group_admin(admin);
//                entity.setLive_group_role(IMUtils.getGroupRole(admin, mAnchorId));
//                entity.setLive_user_role(live_user_role);
//                entity.setLive_user_role(String.valueOf(TUIKitLive.getLoginUserInfo().getRole()));
//                entity.setWealth_val(wealth_val);
//                entity.setWealth_val_switch(wealth_val_switch);
//                entity.setName_color(name_color);
//                entity.setBackground_color(backgroundColor);
//                entity.setUser_level(audienceLevel);
//                entity.setAnchor_level(anchorLevel);
//                String anchorUid = "";
//                if (mAnchorInfo != null) {
//                    anchorUid = !TextUtils.isEmpty(mAnchorInfo.userId) ? mAnchorInfo.userId : mAnchorId;
//                }
//                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.REPORT_LIVE_MESSAGE,
//                        "", GsonUtil.getInstance().toJson(new ReportLiveMessageBean(mAnchorId, String.valueOf(mRoomId), msg))));
//                //userData里边的字段
//                if (!TextUtils.isEmpty(isNoTalking)) { //从服务器获取 禁言状态
//                    if (("1").equals(isNoTalking)) {//已禁言
//                        ToastUtil.toastShortMessage(getString(R.string.banned));
//                    } else if (("0").equals(isNoTalking)) {//未禁言
//                        IMUtils.sendText(mLiveRoom, entity, new IMUtils.OnTextSendListener() {
//                            @Override
//                            public void sendSuc() {
//                                updateIMMessageList(entity);
//                                resumeInteractTime();
//                            }
//                        });
//                    }
//                } else {
//                    //腾讯SDK监听
//                    getStatus(String.valueOf(mRoomId), mSelfUserId, anchorUid, msg, admin);
//                }
//            }
//
//            @Override
//            public void onError(int code, String desc) {
//
//            }
//        });
//    }

    boolean isVideoMute = false;
    boolean isAudioMute = false;

    void closeLocalVideo() {
        Glide.with(getActivity()).asBitmap().load(TUIKitLive.getLoginUserInfo().getFaceUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                TRTCCloud.sharedInstance(getActivity()).setVideoMuteImage(resource, 5);
                if (mSelfImageView != null) {
                    mSelfImageView.setImageBitmap(resource);
                    mSelfImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    if (mSelfVideoView != null) {
                        if (mSelfImageView.getParent() != null) {
                            ((ViewGroup) mSelfImageView.getParent()).removeView(mSelfImageView);
                        }
                        mSelfVideoView.addView(mSelfImageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                }
            }
        });
        TRTCCloud.sharedInstance(getActivity()).muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG, true);
        isOpenCamera = false;
        isVideoMute = true;
    }

    void openLocalVideo() {
        TRTCCloud.sharedInstance(getActivity()).muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG, false);
        if (mSelfVideoView != null) {
            mSelfVideoView.removeView(mSelfImageView);
        }
        isOpenCamera = true;
        isVideoMute = false;
    }


    public void doCameraFront() {
        openLocalVideo();
        //如果是后置 转换摄像头
        if (!TRTCCloud.sharedInstance(getActivity()).getDeviceManager().isFrontCamera()) {
            isFrontCamera = true;
            mLiveRoom.switchCamera();
        } else {

        }
        if (isFrontCamera) {
            mLiveRoom.setMirror(true); //前置改为镜像
        } else {
            mLiveRoom.setMirror(false); //后置改为非镜像
        }
    }

    public void doCameraBack() {
        openLocalVideo();
        //如果是前置 转换摄像头
        if (TRTCCloud.sharedInstance(getActivity()).getDeviceManager().isFrontCamera()) {
            isFrontCamera = false;
            mLiveRoom.switchCamera();
        } else {

        }
        if (isFrontCamera) {
            mLiveRoom.setMirror(true); //前置改为镜像
        } else {
            mLiveRoom.setMirror(false); //后置改为非镜像
        }
    }

    public void doCameraClose() {
        closeLocalVideo();
    }

    public void doAudioOpen() {
        TRTCCloud.sharedInstance(getActivity()).muteLocalAudio(false);
        //ToastUtil.toastShortMessage("开启麦克风");
        isAudioMute = false;
    }

    public void doAudioClose() {
        TRTCCloud.sharedInstance(getActivity()).muteLocalAudio(true);
        //ToastUtil.toastShortMessage("关闭麦克风");
        isAudioMute = true;
    }

    public void doAudioSetting() {
        if (mAnchorAudioPanel != null) {
            mAnchorAudioPanel.show();
        }
    }

    public void doVideoBeauty() {
        if (tiPanelLayout != null) {
            tiPanelLayout.showBeautyPanel();
        }
    }

    void getHistoryMessage() {
        V2TIMManager.getMessageManager().getGroupHistoryMessageList(String.valueOf(mRoomId), 20, null, new V2TIMValueCallback<List<V2TIMMessage>>() {
            @Override
            public void onSuccess(List<V2TIMMessage> v2TIMMessages) {
                Log.d("slide", "" + v2TIMMessages.size());
            }

            @Override
            public void onError(int i, String s) {
                Log.d("slide11", "" + s);
            }
        });
    }

    public void doActivityRestart() {
        // if (FloatWindowLayout.getInstance().is)
        Log.d("float", "onRestart");
        if (Constents.isShowAudienceFloatWindow) {
            Log.d("float", "小窗模式回来");
            View mLiveView = Constents.liveView;
            if (mLiveView != null) {
                Log.d("float", "移除小窗 回显到直播页");
                ((ViewGroup) mLiveView.getParent()).removeView(mLiveView);
                mLayoutVideoManager.addView(mLiveView);
                Constents.liveView = null;
            }
            Log.d("float", "销毁小窗");
            FloatWindowLayout.getInstance().closeFloatWindowWithoutDestory();
            FloatWindowLayout.getInstance().mWindowMode = Constants.WINDOW_MODE_FULL;
            if (mIsBeingLinkMic) {
                mLayoutVideoManager.updateVideoLayoutByWindowStatus();
            }
        }
    }

    void showBuyTicketView() {
        cdbTicket.cancle();
        cdbTicket.setVisibility(View.GONE);
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_TICKET_BUY, "", mAnchorId));
    }

    boolean isTicket = false;
    boolean isBuyTicket = false;

    public void doTicketBuySuc() {
        isBuyTicket = true;
        cdbTicket.setVisibility(View.GONE);
    }

    public void closeRoom() {
        exitRoom();
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    void getTicketState() {
        if (getActivity() != null && getActivity().getIntent() != null) {
            int limitTime = getActivity().getIntent().getIntExtra("ticketTryTime", 0);
            if (limitTime != 0) { //当前房间为门票房
                isTicket = true;
                cdbTicket.setVisibility(View.VISIBLE);
                cdbTicket.setTotalTime(limitTime * 1000);
                cdbTicket.setDelayTime(500);
                cdbTicket.startTimer();
            } else {
                cdbTicket.setVisibility(View.GONE);
            }
        } else {
            cdbTicket.setVisibility(View.GONE);
        }
    }

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

    void showPkView(PkInfoBean pkInfoBean) {
        mPkViewLayout.initPkData(pkInfoBean);
    }

    void showPkTopAudienceView(String uid, String otherId, boolean isUs) {
        PkTopAudienceMessageBean pkBean = new PkTopAudienceMessageBean(uid, otherId, isUs);
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_PK_TOP_AUDIENCE, "", GsonUtil.getInstance().toJson(pkBean)));
    }

    void showLinkTypePop() {
        ChooseLinkTypePanel chooseLinkTypePanel = new ChooseLinkTypePanel(getActivity());
        chooseLinkTypePanel.setPkStopListener(new ChooseLinkTypePanel.OnChooseLinkTypeListener() {
            @Override
            public void onChooseManagerLink() {
                startLinkMic("manager");
            }

            @Override
            public void onChooseUserLink() {
                tryToStartLink();
            }
        });
        chooseLinkTypePanel.show();
    }

    void refreshLinkManager(String linkStr) {
        if (TextUtils.isEmpty(linkStr)) {
            mLiveManagerLayout.refreshManager(new ArrayList<String>());
        } else {
            String[] split = linkStr.split(",");
            mLiveManagerLayout.refreshManager(Arrays.asList(split));
        }
    }


    //场控发言加入房间
    void checkIsManagerEnter(final String uid) {
        String url = "/Api/Live/userIsAdminLink";
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("anchor_uid", mAnchorId);
        HttpPostRequest.getInstance().post(url, map, new TUILiveRequestCallback() {
            @Override
            public void onError(int code, String desc) {
                mLayoutVideoManager.startAnchorVideo(uid, false, null);
            }

            @Override
            public void onSuccess(Object o) {

            }
        });
    }


    void reportLinkManager() {
        String url = "/Api/Live/adminLinkMicReport";
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid", mAnchorId);
        HttpPostRequest.getInstance().post(url, map, new TUILiveRequestCallback() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(Object o) {
                joinManagerPusher();
            }
        });
    }

    void showAnnouncementTipPop(final LiveAnnouncementBean.MessageBean messageBean) {
        String title = "是否前往 " + messageBean.getReceive_nickname() + " 的直播间?";
        LiveNormalTipPanel liveNormalTipPanel = new LiveNormalTipPanel(mContext, title);
        liveNormalTipPanel.show();
        liveNormalTipPanel.setOnNormalTipListener(new LiveNormalTipPanel.OnNormalTipListener() {
            @Override
            public void doNormalLeftClick() {

            }

            @Override
            public void doNormalRightClick() {
                NormalEventBean normalEventBean = new NormalEventBean();
                normalEventBean.setEventData1(messageBean.getReceive_uid());
                normalEventBean.setEventData2(messageBean.getRoom_id());
                LiveMethodEvent liveMethodEvent = new LiveMethodEvent(LiveEventConstant.GO_TO_OTHER_ANCHOR, "1", GsonUtil.getInstance().toJson(normalEventBean));
                EventBus.getDefault().post(liveMethodEvent);
            }
        });
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
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_RED_ENVELOPES, GsonUtil.GsonString(mLayoutTopToolBar.getRedList().get(index)), String.valueOf(mRoomId)));
        //EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_RED_ENVELOPES,String.valueOf(index),String.valueOf(mRoomId)));
    }

    void showRedEnvelopesResult(int index) {
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_RED_ENVELOPES_2, GsonUtil.GsonString(mLayoutTopToolBar.getRedList().get(index)), String.valueOf(mRoomId)));
        //EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_RED_ENVELOPES_2,String.valueOf(index),String.valueOf(mRoomId)));
    }

    public void showLiveRedEnvelopesSign(String url, int num) {
        mLayoutTopToolBar.showLiveRedEnvelopesView(url, num);
    }

    public void showLiveRedEnvelopesSign(LinkedList<LiveRedEnvelopesBean.DataBean> redList) {
        // mLayoutTopToolBar.showLiveRedEnvelopesView(redList);
    }

    public void hideLiveRedEnvelopesSign() {
        mLayoutTopToolBar.hideLiveRedEnvelopesView();
    }

    void getLiveRedEnvelopes() {
        // EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.GET_LIVE_RED_ENVELOPES_LIST,"",String.valueOf(mRoomId)));
        getRedBagList();
    }

    public View getRootView() {
        return mRootView;
    }

    public void showLotteryDrawLayout() {
        sendLiveEvent(LiveEventConstant.SHOW_LIVE_LOTTERY_DRAW, "");
    }

    public void getRedBagList() {
        String url = "/Api/Ping/getLiveRedBagListNew";
        Map<String, String> map = new HashMap<>();
        map.put("room_id", String.valueOf(mRoomId));
        HttpPostRequest.getInstance().post(url, map, new TUILiveRequestCallback() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(Object o) {
                Log.d("haha", o.toString());
                LiveRedEnvelopesBean liveRedEnvelopesBean = GsonUtil.GsonToBean(o.toString(), LiveRedEnvelopesBean.class);
                if (liveRedEnvelopesBean != null) {
                    List<LiveRedEnvelopesBean.DataBean> tempList = liveRedEnvelopesBean.getData();
                    mLayoutTopToolBar.showLiveRedEnvelopesView(tempList);
                }
            }
        });
    }

    public void changeOrientation() {
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void getLiveHistoryMessage() {
        String url = "Api/ChatRoom/chatHistoryAndroid";
        Map<String, String> map = new HashMap<>();
        map.put("roomId", String.valueOf(mRoomId));
        map.put("anchor_uid", mAnchorId);
        HttpPostRequest.getInstance().post(url, map, new TUILiveRequestCallback() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(final Object o) {
                Log.d("okhttp", o.toString());
                mLayoutBottomToolBar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LiveHistoryMessageBean liveHistoryMessageBean = GsonUtil.GsonToBean(o.toString(), LiveHistoryMessageBean.class);
                        if (liveHistoryMessageBean != null && liveHistoryMessageBean.getData() != null) {
                            if (liveHistoryMessageBean.getData().getList() != null && liveHistoryMessageBean.getData().getList().size() > 0) {
                                for (ChatEntity entity : liveHistoryMessageBean.getData().getList()) {
                                    updateIMMessageList(entity);
                                }
                            }
                        }
                    }
                },2000);
            }
        });
    }

}
