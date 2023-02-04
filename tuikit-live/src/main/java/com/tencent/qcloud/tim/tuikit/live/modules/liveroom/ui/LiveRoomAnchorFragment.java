package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui;

import android.animation.ObjectAnimator;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Group;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.support.annotation.NonNull;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.audiosettingkit.AudioEffectPanel;
import com.tencent.liteav.custom.Constents;
import com.tencent.liteav.demo.beauty.model.BeautyInfo;
import com.tencent.liteav.demo.beauty.utils.BeautyUtils;
import com.tencent.liteav.demo.beauty.view.BeautyPanel;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.renderer.TXCGLSurfaceView;
import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.impl.V2TXLivePusherImpl;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.base.BaseFragment;
import com.tencent.qcloud.tim.tuikit.live.base.Config;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.base.HttpPostRequest;
import com.tencent.qcloud.tim.tuikit.live.base.TUILiveRequestCallback;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveAnnouncementBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveChangeInfoChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveCreateAvChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveJoinAvChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveMessageBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveMessageCustomDataBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveRedEnvelopesBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveRoomInfo;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveSettingStateBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveUserData;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveWatchAvChatRoom;
import com.tencent.qcloud.tim.tuikit.live.bean.MassageType;
import com.tencent.qcloud.tim.tuikit.live.bean.PkChatRoomBean;
import com.tencent.qcloud.tim.tuikit.live.bean.PkTopAudienceMessageBean;
import com.tencent.qcloud.tim.tuikit.live.bean.ShareAnchorBean;
import com.tencent.qcloud.tim.tuikit.live.bean.TimCustomMessage;
import com.tencent.qcloud.tim.tuikit.live.component.bottombar.BottomToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.component.common.CircleImageView;
import com.tencent.qcloud.tim.tuikit.live.component.common.SelectMemberView;
import com.tencent.qcloud.tim.tuikit.live.component.countdown.CountDownTimerView;
import com.tencent.qcloud.tim.tuikit.live.component.countdown.ICountDownTimerView;
import com.tencent.qcloud.tim.tuikit.live.component.danmaku.DanmakuManager;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout2;
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
import com.tencent.qcloud.tim.tuikit.live.component.link.LiveManagerSpeakListPanel;
import com.tencent.qcloud.tim.tuikit.live.component.link.adapter.SpeakManagerAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.manager.LiveManagerLayout;
import com.tencent.qcloud.tim.tuikit.live.component.manager.LiveManagerTipPanel;
import com.tencent.qcloud.tim.tuikit.live.component.message.ChatEntity;
import com.tencent.qcloud.tim.tuikit.live.component.message.ChatLayout;
import com.tencent.qcloud.tim.tuikit.live.component.other.LiveNormalTipPanel;
import com.tencent.qcloud.tim.tuikit.live.component.pk.PkMuteOtherPanel;
import com.tencent.qcloud.tim.tuikit.live.component.pk.PkStopTipPanel;
import com.tencent.qcloud.tim.tuikit.live.component.pk.PkViewLayout;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.TopToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.helper.UserIdentityUtils;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.TUILiveRoomAnchorLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.TUILiveRoomAnchorLayout.TUILiveRoomAnchorLayoutDelegate;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.AppLiveGiftChangedEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.CustomMessage;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.GiftMessage;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.ReportLiveMessageBean;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoom;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDelegate;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.TRTCLiveRoomImpl;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.av.trtc.ITRTCTXLiveRoom;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.av.trtc.TXTRTCLiveRoom;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.base.TXCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.base.TXUserInfo;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.base.TXUserListCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.room.impl.TXRoomService;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.ExitConfirmDialog;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.FinishDetailDialog;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.LinkMicListDialog;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.LiveVideoView;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.VideoViewController;
import com.tencent.qcloud.tim.tuikit.live.utils.AnimationUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil;
import com.tencent.qcloud.tim.tuikit.live.utils.IMUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.PermissionUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.TUILiveLog;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;
import com.tencent.qcloud.tim.uikit.utils.NetWorkUtils;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.TiRotation;
import cn.tillusory.tiui.TiPanelLayout;
import cn.tillusory.tiui.custom.TiSharePreferences;
import master.flame.danmaku.controller.IDanmakuView;

import static com.tencent.qcloud.tim.tuikit.live.base.Constants.MAX_LINK_MIC_SIZE;
import static com.tencent.qcloud.tim.tuikit.live.base.Constants.REQUEST_PK_TIME_OUT;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.CAVEAT;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.CHANGE_ROOM;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.CREATE_ROOM;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.JOIN_ROOM_MSG;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.LIVE_ANIMATION_MSG;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.LIVE_RED_ENVELOPES_CHANGE;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.PROMPT_MSG;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.RETRY_INVITE_PK;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.ROOM_STATE_PK;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.USER_DATA;
import static com.tencent.qcloud.tim.tuikit.live.bean.MassageType.WATCH_ROOM;
import static com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.FinishDetailDialog.ANCHOR_LOCAL_BEANS;
import static com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.FinishDetailDialog.LIVE_TOTAL_TIME;
import static com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.FinishDetailDialog.TOTAL_AUDIENCE_COUNT;
import static com.tencent.qcloud.tim.tuikit.live.utils.IMUtils.handleCustomTextMsg;
import static com.tencent.trtc.TRTCCloudDef.TRTC_GSENSOR_MODE_DISABLE;
import static com.tencent.trtc.TRTCCloudDef.TRTC_GSENSOR_MODE_UIAUTOLAYOUT;
import static com.tencent.trtc.TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG;
import static com.tencent.trtc.TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SMALL;

public class LiveRoomAnchorFragment extends BaseFragment implements TopToolBarLayout.TopToolBarDelegate {
    private static final String TAG = LiveRoomAnchorFragment.class.getName();
    private LiveRoomInfo liveRoomInfo;//进入直播间主播信息
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    private ConstraintLayout mRootView;
    private BeautyPanel mLayoutBeautyPanel;
    private TXCloudVideoView mVideoViewAnchor;
    //private ImageView mImagePkLayer;
    private AudioEffectPanel mAnchorAudioPanel;
    private String mPushAddress;
    private String data;
    private String giftUrl;
    private V2TXLivePusherImpl mLivePusher;
    private boolean mFrontCamera = true;
    private String is_group_admin; //备注：是否场控1是0不是

    public void setIs_group_admin(String is_group_admin) {
        this.is_group_admin = is_group_admin;
    }

    public LiveRoomPreviewLayout getLayoutPreview() {
        return mLayoutPreview;
    }

    private LiveRoomPreviewLayout mLayoutPreview;
    private VideoViewController mTUIVideoViewController;
    private AlertDialog mErrorDialog;
    private TopToolBarLayout mLayoutTopToolBar;
    private BottomToolBarLayout mBottomToolBarLayout;
    private ChatLayout mChatLayout;
    private IDanmakuView mDanmakuView;                   // 负责弹幕的显示
    private DanmakuManager mDanmakuManager;               // 弹幕的管理类
    private TRTCLiveRoom mLiveRoom;
    private TXCloudVideoView mVideoViewPKAnchor;
    private RelativeLayout mLayoutPKContainer;
    private SelectMemberView mSelectMemberView;
    private TextView mBottomStopPkBtn;
    private Group mGroupAfterLive;
    private Group mGroupButtomView;
    private Group mGroupPkView;
    private HeartLayout mHeartLayout;
    private GiftAnimatorLayout mGiftAnimatorLayout;   //礼物动画和礼物弹幕的显示
    private TextView mStatusTipsView;         //状态提示view
    private CircleImageView mButtonInvitationPk;     //邀请pk按钮
    private LinkMicListDialog mLinkMicListDialog;
    private AlertDialog mRoomPKRequestDialog;
    private ImageView mImageRedDot;
    private CountDownTimerView mCountDownTimerView;  //倒计时view
    private LiveRoomPreviewLayout.PreviewOnClickCover mPreviewOnClickCover;//预览页上传头像
    private LiveRoomPreviewLayout.PreviewOnLoadCover mPreviewOnLoadCover;
    private TUILiveRoomAnchorLayoutDelegate mLiveRoomAnchorLayoutDelegate;
    private GiftInfoDataHandler mGiftInfoDataHandler;

    private TRTCLiveRoomDef.TRTCLiveRoomInfo mRoomInfo = new TRTCLiveRoomDef.TRTCLiveRoomInfo();
    private TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo = new TRTCLiveRoomDef.LiveAnchorInfo();
    private List<String> mAnchorUserIdList = new ArrayList<>();

    private Runnable mGetAudienceRunnable;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private long mTotalMemberCount = 0;   // 总进房观众数量
    private long mCurrentMemberCount = 0;   // 当前观众数量
    private long mHeartCount = 0;   // 点赞数量
    private long mLocalBeans = 0;   // 本场收益
    private long mStartTime = 0;   // 主播开始直播时间
    private String mOwnerUserId;
    private int mRoomId;
    private String mRoomName;
    private boolean mIsEnterRoom;
    private int mCurrentStatus = TRTCLiveRoomDef.ROOM_STATUS_NONE;
    private boolean mIsPkStatus;           //当前正在pk
    private boolean mIsLinkMicStatus;      //当前正在连麦
    private TextView mPkTimer, mPkMeNo, mPkotherNo; //PK
    private LinearLayout mLLPkLayer, mLLPkLayerContest; //显示PK血条
    private RelativeLayout mRlPkTimeDisplayContainer; //显示PK倒计时
    private ImageView mIvPkContestVictor, mIvPkContestPing, mIvPkContestFail; //显示PK胜负
    private PkViewLayout mPkViewLayout;
    private boolean mIsPushing = false;
    private String wealth_val; //财富值
    private String wealth_val_switch; //是否显示财富值
    private String name_color;
    private String backgroupColor;//背景颜色
    private String liveTitle;
    private String livePoster;
    private String anchorLevel;
    private String audienceLevel;

    private LiveManagerLayout mLiveManagerLayout;

    private TRTCLiveRoomDelegate mTRTCLiveRoomDelegate = new TRTCLiveRoomDelegate() {
        @Override
        public void onError(int code, String message) {
            TUILiveLog.e(TAG, "onError: " + code + " " + message);
            if (mLiveRoomAnchorLayoutDelegate != null) {
                mLiveRoomAnchorLayoutDelegate.onError(mRoomInfo, code, message);
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
            TUILiveLog.d(TAG, "onRoomInfoChange: " + roomInfo);
            int oldStatus = mCurrentStatus;
            mCurrentStatus = roomInfo.roomStatus;
            setAnchorViewFull(mCurrentStatus != TRTCLiveRoomDef.ROOM_STATUS_PK);
            TUILiveLog.d(TAG, "onRoomInfoChange: " + mCurrentStatus);
            if (oldStatus == TRTCLiveRoomDef.ROOM_STATUS_PK
                    && mCurrentStatus != TRTCLiveRoomDef.ROOM_STATUS_PK) {
                // 上一个状态是PK，需要将界面中的元素恢复
                // mBottomStopPkBtn.setVisibility(View.GONE);
                // mBottomStopPkBtn.setText(R.string.live_wait_link);
                //mGroupPkView.setVisibility(View.GONE);
                mPkViewLayout.hidePkView();
                judgeRedDotShow();
                LiveVideoView videoView = mTUIVideoViewController.getPKUserView();
                mVideoViewPKAnchor = videoView.getPlayerVideo();
                if (mLayoutPKContainer.getChildCount() != 0) {
                    mLayoutPKContainer.removeView(mVideoViewPKAnchor);
                    videoView.addView(mVideoViewPKAnchor);
                    mTUIVideoViewController.clearPKView();
                    mVideoViewPKAnchor = null;
                }
                //mImagePkLayer.setVisibility(View.GONE);
                //mButtonInvitationPk.setEnabled(true);
                mIsPkStatus = false;
                if (isVideoMute) {
                    suitCover(livePoster);
                }
            } else if (oldStatus == TRTCLiveRoomDef.ROOM_STATUS_LINK_MIC
                    && mCurrentStatus != TRTCLiveRoomDef.ROOM_STATUS_LINK_MIC) {
                mIsLinkMicStatus = false;
            } else if (mCurrentStatus == TRTCLiveRoomDef.ROOM_STATUS_PK) {
                // 本次状态是PK，需要将一个PK的view挪到右上角
                //mImagePkLayer.setVisibility(View.VISIBLE);
                mIsPkStatus = true;
                // mBottomStopPkBtn.setVisibility(View.VISIBLE);
                //mBottomStopPkBtn.setText(R.string.live_stop_pk);
                mPkViewLayout.showPkView();
                judgeRedDotShow();
                LiveVideoView videoView = mTUIVideoViewController.getPKUserView();
                videoView.showKickoutBtn(false);
                mVideoViewPKAnchor = videoView.getPlayerVideo();
                videoView.removeView(mVideoViewPKAnchor);
                mLayoutPKContainer.addView(mVideoViewPKAnchor);
                //mButtonInvitationPk.setEnabled(false);
                //reportStartPk(pkInviteId);
                if (isVideoMute) {
                    suitCover2(livePoster);
                }
            }
        }

        @Override
        public void onRoomDestroy(String roomId) {

        }

        @Override
        public void onAnchorEnter(final String userId) {
            if (getContext() == null) {
                TUILiveLog.d(TAG, "getContext is null!");
                return;
            }

            if (TXTRTCLiveRoom.getInstance().getLiveManagerUserList().contains(userId)) {
                mLiveManagerLayout.addManager(userId);
                reportManagerSpeak();
//                if (userId.equals("407950")) {
//                    adminCloseBeauty();
//                }
                return;
            } else {
                mAnchorUserIdList.add(userId);
            }

            final LiveVideoView view = mTUIVideoViewController.applyVideoView(userId);
            if (view == null) {
                Toast.makeText(getContext(), R.string.live_warning_link_user_max_limit, Toast.LENGTH_SHORT).show();
                return;
            }
            if (mCurrentStatus != TRTCLiveRoomDef.ROOM_STATUS_PK) {
                view.startLoading();
            }
            mLiveRoom.startPlay(userId, view.getPlayerVideo(), new TRTCLiveRoomCallback.ActionCallback() {
                @Override
                public void onCallback(int code, String msg) {
                    if (code == 0) {
                        Log.d(TAG, userId + "");
                        if (mCurrentStatus != TRTCLiveRoomDef.ROOM_STATUS_PK) {
                            view.stopLoading(true);
                        }
                    }
                }
            });
            view.setOnLiveViewClickListener(new LiveVideoView.OnLiveViewClickListener() {
                @Override
                public void onLiveViewClick(String uid) {
                    showAudienceCard(uid);
                }
            });
            updateLinkMicUserList();
        }

        @Override
        public void onAnchorExit(String userId) {
            if (TXTRTCLiveRoom.getInstance().getLiveManagerUserList().contains(userId)) {
                TXTRTCLiveRoom.getInstance().removeManagerUser(userId);
                reportManagerSpeak();
            }
            mLiveManagerLayout.removeManager(userId);
            mLiveRoom.stopPlay(userId, null);
            mTUIVideoViewController.recycleVideoView(userId);
            mAnchorUserIdList.remove(userId);
            updateLinkMicUserList();
        }

        @Override
        public void onAudienceEnter(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
            mTotalMemberCount++;
            ChatEntity entity = new ChatEntity();
            entity.setSenderName(getString(R.string.live_notification));
            if (TextUtils.isEmpty(userInfo.userName)) {
                entity.setContent(getString(R.string.live_user_join_live, userInfo.userId));
            } else {
                entity.setContent(getString(R.string.live_user_join_live, userInfo.userName));
            }
            entity.setType(Constants.MEMBER_ENTER);
            //updateIMMessageList(entity);
            //addAudienceListLayout(userInfo);

        }

        @Override
        public void onAudienceExit(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
            ChatEntity entity = new ChatEntity();
            entity.setSenderName(getString(R.string.live_notification));
            if (TextUtils.isEmpty(userInfo.userName)) {
                entity.setContent(getString(R.string.live_user_quit_live, userInfo.userId));
            } else {
                entity.setContent(getString(R.string.live_user_quit_live, userInfo.userName));
            }
            entity.setType(Constants.MEMBER_EXIT);
            //updateIMMessageList(entity);
            removeAudienceListLayout(userInfo);
            mLinkMicListDialog.removeMemberEntity(userInfo.userId);
            judgeRedDotShow();
        }

        @Override
        public void onRequestJoinAnchor(final TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, String reason) {

            //观众端同意主播的连麦邀请
            if (!TextUtils.isEmpty(reason) && reason.contains("auto")) {
                autoAgreeLinkRequest(userInfo.userId);
                return;
            }

            //场控发言
            if (!TextUtils.isEmpty(reason) && reason.contains("manager")) {
                TXTRTCLiveRoom.getInstance().addLiveManagerUser(userInfo.userId);
                autoAgreeLinkRequest(userInfo.userId);
                return;
            }

            if (getContext() == null) {
                TUILiveLog.d(TAG, "getContext is null!");
                return;
            }
            if (mAnchorUserIdList != null && mAnchorUserIdList.size() >= getMaxLinkNum()) {
                mLiveRoom.responseJoinAnchor(userInfo.userId, false, getString(R.string.live_warning_link_user_max_limit));
                return;
            }
            Log.d("logutil", userInfo.toString());
            LinkMicListDialog.MemberEntity memberEntity = new LinkMicListDialog.MemberEntity();
            memberEntity.userId = userInfo.userId;
            memberEntity.userAvatar = userInfo.avatarUrl;
            memberEntity.userName = userInfo.userName;
            mLinkMicListDialog.addMemberEntity(memberEntity);
            judgeRedDotShow();
            Toast.makeText(getContext(), getContext().getString(R.string.live_request_link_mic, userInfo.userName), Toast.LENGTH_SHORT).show();
            if (mBottomStopPkBtn.getVisibility() == View.GONE) { //当有用户发起连麦时 显示连麦列表按钮
                mBottomStopPkBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAudienceRequestJoinAnchorTimeout(String userId) {
            mLinkMicListDialog.removeMemberEntity(userId);
            judgeRedDotShow();

//            if (inviteUserList.contains(userId)) {
//                ToastUtil.toastShortMessage("超时" + userId);
//            }
        }

        @Override
        public void onAudienceCancelRequestJoinAnchor(String userId) {
            mLinkMicListDialog.removeMemberEntity(userId);
            judgeRedDotShow();
        }

        @Override
        public void onKickoutJoinAnchor() {

        }

        @Override
        public void onRequestRoomPK(final TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
//            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
//                    .setCancelable(true)
//                    .setTitle(R.string.live_tips)
//                    .setMessage(getString(R.string.live_request_pk, userInfo.userName))
//                    .setPositiveButton(R.string.live_accept, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (getContext() == null) {
//                                TUILiveLog.d(TAG, "getContext is null!");
//                                return;
//                            }
//
//                            if (mIsLinkMicStatus) {
//                                Toast.makeText(getContext(), R.string.live_link_mic_status, Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            dialog.dismiss();
//                            mLiveRoom.responseRoomPK(userInfo.userId, true, "");
//                            mGroupPkView.setVisibility(View.VISIBLE);
//                            mIsPkStatus = true;
//                        }
//                    })
//                    .setNegativeButton(R.string.live_refuse, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            mLiveRoom.responseRoomPK(userInfo.userId, false, getString(R.string.live_anchor_refuse_pk_request));
//                            mGroupPkView.setVisibility(View.GONE);
//                        }
//                    });
//
//            mMainHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (mRoomPKRequestDialog != null) {
//                        mRoomPKRequestDialog.dismiss();
//                    }
//                    mRoomPKRequestDialog = builder.create();
//                    mRoomPKRequestDialog.setCancelable(false);
//                    mRoomPKRequestDialog.setCanceledOnTouchOutside(false);
//                    mRoomPKRequestDialog.show();
//                }
//            });
            if (isAutoRefusePk) {
                doPkRefuse();
            } else {
                pkInviteId = userInfo.userId;
                showPkInvitedView(userInfo.userId);
            }
        }

        @Override
        public void onAnchorCancelRequestRoomPK(String userId) {
            if (mRoomPKRequestDialog != null) {
                mRoomPKRequestDialog.dismiss();
            }
        }

        @Override
        public void onAnchorRequestRoomPKTimeout(String userId) {
            if (mRoomPKRequestDialog != null) {
                mRoomPKRequestDialog.dismiss();
            }
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
            switch (type) {
                case Constants.IMCMD_PRAISE:
                    mHeartCount++;
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

    String pkInviteId = "";

    public void setLiveRoomAnchorLayoutDelegate(TUILiveRoomAnchorLayoutDelegate liveRoomAnchorLayoutDelegate) {
        mLiveRoomAnchorLayoutDelegate = liveRoomAnchorLayoutDelegate;
    }

    public void stopLive() {
        if (mIsEnterRoom) {
            destroyRoom();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsEnterRoom = false;
        mLiveRoom = TRTCLiveRoom.sharedInstance(getContext());
        mLiveRoom.setDelegate(mTRTCLiveRoomDelegate);
        mDanmakuManager = new DanmakuManager(getContext());
        mOwnerUserId = V2TIMManager.getInstance().getLoginUser();
        //updateAnchorInfo();

        // 如果当前观众页悬浮窗，关闭并退房
        if (FloatWindowLayout.getInstance().mWindowMode == Constants.WINDOW_MODE_FLOAT) {
            FloatWindowLayout.getInstance().closeFloatWindow();
            mLiveRoom.exitRoom(null);
        }
        EventBus.getDefault().register(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        preExitRoom();
//        if (mIsEnterRoom) {
//            startFloat();
//        } else {
//            finishRoom();
//        }
    }

    public void startFloat() {
//        mVideoViewAnchor.getGLSurfaceView();
//        mVideoViewAnchor.getHWVideoView();
//        mVideoViewAnchor.getVideoView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //6.0版本
            if (!Settings.canDrawOverlays(getActivity())) {
                showFloatPermissionDialog("主播在直播中去其他页面，需要开启悬浮窗权限，是否开启？");
            } else {
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.ANCHOR_START_FLOAT, "", ""));
            }
        } else {
            preExitRoom();
        }
    }

    private void preExitRoom() {
        if (mIsEnterRoom) {
            showExitInfoDialog(getContext().getString(R.string.live_warning_anchor_exit_room), false);
        } else {
            finishRoom();
        }
    }

    protected void updateLiveWindowMode(int mode) {
        if (FloatWindowLayout.getInstance().mWindowMode == mode) return;

        if (mode == Constants.WINDOW_MODE_FLOAT) {
            if (getActivity() == null) {
                return;
            }
            FloatWindowLayout.IntentParams intentParams = new FloatWindowLayout.IntentParams(getActivity().getClass(), mRoomId, mOwnerUserId, false, "mCdnUrl");
            boolean result = FloatWindowLayout.getInstance().showFloatWindow(mRootView, intentParams);
            if (!result) {
                //exitRoom();
                return;
            }
            FloatWindowLayout.getInstance().setOnTouchFloatWindowListener(new FloatWindowLayout.OnTouchFloatWindowListener() {
                @Override
                public void onTouchUpFloatWindow() {
//                    if(mOnTouchFloatWindowListener != null){
//                        mOnTouchFloatWindowListener.onTouchUpFloatWindow();
//                    }
                }
            });
            FloatWindowLayout.getInstance().mWindowMode = mode;
            // mLayoutVideoManager.updateVideoLayoutByWindowStatus();
            FloatWindowLayout.getInstance().setFloatWindowLayoutDelegate(new FloatWindowLayout.FloatWindowLayoutDelegate() {
                @Override
                public void onClose() {
                    //exitRoom();
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
            });
        } else if (mode == Constants.WINDOW_MODE_FULL) {
            FloatWindowLayout.getInstance().closeFloatWindow();
            FloatWindowLayout.getInstance().mWindowMode = mode;
            // isFloatRecovery = true;
        }
    }

    View mAnchorLiveRoomView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAnchorLiveRoomView = inflater.inflate(R.layout.live_fragment_live_room_anchor, container, false);
        initPusher();
        initView(mAnchorLiveRoomView);
        //initData();
        return mAnchorLiveRoomView;
    }

    /**
     * 初始化 SDK 推流器
     */
    private void initPusher() {
        mLivePusher = new V2TXLivePusherImpl(getActivity(), V2TXLiveDef.V2TXLiveMode.TXLiveMode_RTC);

        // 设置默认美颜参数， 美颜样式为光滑，美颜等级 5，美白等级 3，红润等级 2
//        mLivePusher.getBeautyManager().setBeautyStyle(TXLiveConstants.BEAUTY_STYLE_SMOOTH);
//        mLivePusher.getBeautyManager().setBeautyLevel(5);
//        mLivePusher.getBeautyManager().setWhitenessLevel(5);
//        mLivePusher.getBeautyManager().setRuddyLevel(5);


        TRTCCloud mTRTCCloud = TRTCCloud.sharedInstance(getActivity());
        // mTRTCCloud.setGSensorMode(TRTC_GSENSOR_MODE_UIAUTOLAYOUT);
        mTRTCCloud.setGSensorMode(TRTC_GSENSOR_MODE_DISABLE);

        //mLivePusher.setWatermark(BitmapFactory.decodeResource(getResources(),R.drawable.ic_hangup),0.5f,0.5f,100);

        //关闭腾讯自己的美颜
        //  mTRTCCloud.enableCustomVideoCapture(false);

        //  TiSDKManager.getInstance().setWatermark();

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
        initTiPanel();

    }

    TiPanelLayout tiPanelLayout;

    private void initTiPanel() {
        TRTCCloud.sharedInstance(getActivity()).setLocalVideoProcessListener(TRTCCloudDef.TRTC_VIDEO_PIXEL_FORMAT_NV21,
                TRTCCloudDef.TRTC_VIDEO_BUFFER_TYPE_BYTE_ARRAY,
                new TRTCCloudListener.TRTCVideoFrameListener() {
                    @Override
                    public void onGLContextCreated() {
//                    // 预销毁ＳＤＫ
//                        TiSDKManager.getInstance().destroy();
//                    // 载入缓存参数
//                        TiSharePreferences.getInstance().init(getActivity(),
//                                TiSDKManager.getInstance());
//                        TiSharePreferences.getInstance().initCacheValue();
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
                                mFrontCamera
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

    private void initView(View view) {
        mRootView = view.findViewById(R.id.root);
        initBeauty();
        initPKView(view);
        mVideoViewAnchor = view.findViewById(R.id.video_view_anchor);
        //mImagePkLayer = view.findViewById(R.id.iv_pk_layer);
        mAnchorAudioPanel = new AudioEffectPanel(getContext());
        mLayoutPreview = view.findViewById(R.id.layout_preview);
        mLayoutPKContainer = view.findViewById(R.id.layout_pk_container);
        //  mLayoutTopToolBar = view.findViewById(R.id.layout_top_toolbar);
        mLayoutTopToolBar = view.findViewById(R.id.layout_top_toolbar);
        mChatLayout = view.findViewById(R.id.layout_chat);
        mDanmakuView = view.findViewById(R.id.view_danmaku);
        mGiftAnimatorLayout = view.findViewById(R.id.lottie_animator_layout);
        mDanmakuManager.setDanmakuView(mDanmakuView);
        mGroupAfterLive = view.findViewById(R.id.group_after_live);
        mGroupButtomView = view.findViewById(R.id.group_bottom_view);
        mHeartLayout = view.findViewById(R.id.heart_layout);
        mStatusTipsView = view.findViewById(R.id.state_tips);
        tiPanelLayout = view.findViewById(R.id.ti_panel);
        tiPanelLayout.init(TiSDKManager.getInstance());
        tiPanelLayout.setOnPanelItemClickListener(new TiPanelLayout.OnPanelItemClickListener() {
            @Override
            public void onBeautyItemClick() {
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.GET_BEAUTY_PERMISSION, "", ""));
            }
        });
        initTopToolBar(view);
        initBottomToolBar();
        initBottomMessageLayout(view);
        mAnchorAudioPanel.setAudioEffectManager(mLiveRoom.getAudioEffectManager());
        mAnchorAudioPanel.initPanelDefaultBackground();
        List<LiveVideoView> tuiVideoViewList = new ArrayList<>();
        tuiVideoViewList.add((LiveVideoView) view.findViewById(R.id.video_view_link_mic_1));
        tuiVideoViewList.add((LiveVideoView) view.findViewById(R.id.video_view_link_mic_2));
        tuiVideoViewList.add((LiveVideoView) view.findViewById(R.id.video_view_link_mic_3));
        mTUIVideoViewController = new VideoViewController(tuiVideoViewList, new LiveVideoView.OnRoomViewListener() {
            @Override
            public void onKickUser(String userId) {
                if (userId != null) {
                    mLiveRoom.kickoutJoinAnchor(userId, null);
                }
            }
        });

        mBottomStopPkBtn = view.findViewById(R.id.btn_bottom_stop_pk);
        mImageRedDot = view.findViewById(R.id.img_badge);
        mBottomStopPkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentStatus == TRTCLiveRoomDef.ROOM_STATUS_PK) {
                    showStopPkTips();
                } else {
                    //排麦需求
                    mLinkMicListDialog.show();
                }
            }
        });

        //PK展示界面
        mSelectMemberView = new SelectMemberView(getContext());
        mSelectMemberView.setOnSelectedCallback(new SelectMemberView.onSelectedCallback() {
            @Override
            public void onSelected(int seatIndex, final SelectMemberView.MemberEntity memberEntity) {
                mLiveRoom.requestRoomPK(memberEntity.roomId, memberEntity.userId, REQUEST_PK_TIME_OUT, new TRTCLiveRoomCallback.ActionCallback() {
                    @Override
                    public void onCallback(int code, String msg) {
                        if (getContext() == null) {
                            TUILiveLog.d(TAG, "getContext is null!");
                            return;
                        }

                        mStatusTipsView.setText("");
                        mStatusTipsView.setVisibility(View.GONE);
                        if (code == 0) {
                            // 主播已经接受
                            Toast.makeText(getContext(), R.string.live_anchor_accept_pk, Toast.LENGTH_SHORT).show();
                            // mGroupPkView.setVisibility(View.VISIBLE);
                            mPkViewLayout.showPkView();
                            //mBottomStopPkBtn.setVisibility(View.VISIBLE);
                            mPkAnchorId = memberEntity.userId;
                        } else if (code == -2) {
                            Toast.makeText(getContext(), R.string.live_anchor_accept_pk_timeout, Toast.LENGTH_SHORT).show();
                            //mGroupPkView.setVisibility(View.GONE);
                            mPkViewLayout.hidePkView();
                            // mBottomStopPkBtn.setVisibility(View.GONE);
                        } else {
                            // 主播拒绝
                            Toast.makeText(getContext(), R.string.live_anchor_reject_pk, Toast.LENGTH_SHORT).show();
                            //mGroupPkView.setVisibility(View.GONE);
                            mPkViewLayout.hidePkView();
                            // mBottomStopPkBtn.setVisibility(View.GONE);
                        }
                        mSelectMemberView.dismiss();
                    }
                });
                mSelectMemberView.dismiss();
                mStatusTipsView.setText(R.string.live_wait_anchor_accept);
                mStatusTipsView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
                mSelectMemberView.dismiss();
            }
        });

        mLinkMicListDialog = new LinkMicListDialog(getContext());
        mLinkMicListDialog.setTitle(getContext().getString(R.string.live_audience_link_mic));
        mLinkMicListDialog.setOnSelectedCallback(new LinkMicListDialog.onSelectedCallback() {
            @Override
            public void onItemAgree(LinkMicListDialog.MemberEntity memberEntity) {
                if (getContext() == null) {
                    TUILiveLog.d(TAG, "getContext is null!");
                    return;
                }

                if (mIsPkStatus) {
                    Toast.makeText(getContext(), R.string.live_pk_status, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mAnchorUserIdList != null && mAnchorUserIdList.size() >= getMaxLinkNum()) {
                    Toast.makeText(getContext(), R.string.live_warning_link_user_max_limit, Toast.LENGTH_SHORT).show();
                    return;
                }
                mLiveRoom.responseJoinAnchor(memberEntity.userId, true, getString(R.string.live_anchor_accept));
                mLinkMicListDialog.removeMemberEntity(memberEntity.userId);
                mLinkMicListDialog.dismiss();
                judgeRedDotShow();
                mIsLinkMicStatus = true;
            }

            @Override
            public void onItemReject(LinkMicListDialog.MemberEntity memberEntity) {
                mLiveRoom.responseJoinAnchor(memberEntity.userId, false, getString(R.string.live_anchor_reject));
                mLinkMicListDialog.removeMemberEntity(memberEntity.userId);
                mLinkMicListDialog.dismiss();
                judgeRedDotShow();
            }

            @Override
            public void onCancel() {
                mLinkMicListDialog.dismiss();
            }

            @Override
            public void onLinkMicEmpty() {
                if (mBottomStopPkBtn.getVisibility() == View.VISIBLE) {//当没有用户发起连麦时 隐藏连麦列表按钮
                    if (mCurrentStatus != TRTCLiveRoomDef.ROOM_STATUS_PK) {
                        mBottomStopPkBtn.setVisibility(View.GONE);
                    }
                }

            }

        });
        mLayoutPreview.setmPreviewOnLoadCover(new LiveRoomPreviewLayout.PreviewOnLoadCover() {
            @Override
            public void onLoadCover(ImageView view) {
                if (mPreviewOnLoadCover != null) {
                    mPreviewOnLoadCover.onLoadCover(view);
                }
            }

            @Override
            public void onLoadLiveTitle(String liveTitle, String is_interact) {
                if (mPreviewOnLoadCover != null) {
                    mPreviewOnLoadCover.onLoadLiveTitle(liveTitle, is_interact);
                }
            }
        });
        mLayoutPreview.setPreviewOnClickCover(new LiveRoomPreviewLayout.PreviewOnClickCover() {
            @Override
            public void onClickCover(ImageView view) {
                if (mPreviewOnClickCover != null) {
                    mPreviewOnClickCover.onClickCover(view);
                }
            }
        });
        mLayoutPreview.setPreviewCallback(new LiveRoomPreviewLayout.PreviewCallback() {
            @Override
            public void onClose() {
                // 点击预览页的关闭按钮
                finishRoom();
            }

            @Override
            public void onBeautyPanel() {
                //mLayoutBeautyPanel.show();
//                if (mLayoutBeautyPanel != null) {
//                    mLayoutBeautyPanel.show();
//                }
                if (tiPanelLayout != null) {
                    tiPanelLayout.showBeautyPanel();
                }
            }

            @Override
            public void onSwitchCamera() {
                //mLiveRoom.switchCamera();
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_PREVIEW_SETTING, "",
                        GsonUtil.getInstance().toJson(new LiveSettingStateBean(isAudioMute, isVideoMute, mLivePusher.getDeviceManager().isFrontCamera()))));

            }

            @Override
            public void onStartLive(final String roomName, final String coverUrl, final int audioQualityType) {
                requestPermissions(PermissionUtils.getLivePermissions(), new OnPermissionGrandCallback() {
                    @Override
                    public void onAllPermissionsGrand() {
                        startPreview();
                        startLiveCountDown(roomName, coverUrl, audioQualityType);
                    }
                });
            }
        });
        mLayoutBeautyPanel.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mIsEnterRoom) {
                    mGroupButtomView.setVisibility(View.VISIBLE);
                } else {
                    mLayoutPreview.setBottomViewVisibility(View.VISIBLE);
                }
            }
        });
        mLayoutBeautyPanel.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
               /*if (mIsEnterRoom) {
                    mGroupButtomView.setVisibility(View.GONE);
                } else {
                    mLayoutPreview.setBottomViewVisibility(View.GONE);
                }*/
            }
        });
        requestPermissions(PermissionUtils.getLivePermissions(), new OnPermissionGrandCallback() {
            @Override
            public void onAllPermissionsGrand() {
                startPreview();
            }
        });
        mCountDownTimerView = view.findViewById(R.id.countdown_timer_view);
        initListener();

        mLiveManagerLayout = view.findViewById(R.id.layout_live_manager);
        mLiveManagerLayout.setOnLiveManagerListener(new LiveManagerLayout.OnLiveManagerListener() {
            @Override
            public void doManagerClick(String uid) {
                showManagerTipPop(uid);
            }
        });
    }


    Map<String, Boolean> managerMuteMap;

    void showManagerTipPop(String uid) {
        boolean isMangerMute = false;
        if (managerMuteMap == null) {
            managerMuteMap = new HashMap<>();
        } else {
            if (managerMuteMap.containsKey(uid)) {
                isMangerMute = managerMuteMap.get(uid);
            }
        }

        final LiveManagerTipPanel liveManagerTipPanel = new LiveManagerTipPanel(getActivity(), uid, isMangerMute);
        liveManagerTipPanel.setCanceledOnTouchOutside(true);
        liveManagerTipPanel.setTipListener(new LiveManagerTipPanel.OnManagerTipListener() {
            @Override
            public void onManagerMute(String uid, boolean isMute) {
                mLiveRoom.muteRemoteAudio(uid, isMute);
                managerMuteMap.put(uid, isMute);
                if (isMute) {
                    ToastUtil.toastShortMessage("静音场控成功");
                } else {
                    ToastUtil.toastShortMessage("取消静音场控");
                }
                liveManagerTipPanel.dismiss();
            }

            @Override
            public void onManagerKick(String uid) {
                mLiveRoom.kickoutJoinAnchor(uid, new TRTCLiveRoomCallback.ActionCallback() {
                    @Override
                    public void onCallback(int code, String msg) {
                        ToastUtil.toastShortMessage("下麦成功");
                        liveManagerTipPanel.dismiss();
                    }
                });

            }
        });
        liveManagerTipPanel.show();
    }

    private void initTopToolBar(View view) {
        view.findViewById(R.id.ll_live_more).setVisibility(View.GONE);
        LinearLayout ll_gift = view.findViewById(R.id.ll_alert_gift_list);
        ll_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLiveOperateClickListener != null) {
                    String anchor_id = "";
                    if (liveRoomInfo != null) {
                        anchor_id = !TextUtils.isEmpty(liveRoomInfo.getData().getUid()) ? liveRoomInfo.getData().getUid() : "";
                    }
                    onLiveOperateClickListener.onLiveStarNumber(v, anchor_id);
                }
            }
        });
        mLayoutTopToolBar.setTopToolBarDelegate(this);
        TextView tvLiveContestNo = mLayoutTopToolBar.findViewById(R.id.tv_live_contest_number);
        tvLiveContestNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLiveOperateClickListener != null) {
                    String anchor_id = "";
                    if (liveRoomInfo != null) {
                        anchor_id = !TextUtils.isEmpty(liveRoomInfo.getData().getUid()) ? liveRoomInfo.getData().getUid() : "";
                    }
                    onLiveOperateClickListener.onClickContestNo(v, anchor_id);
                }
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

    private void startLiveCountDown(final String roomName, final String coverUrl, final int audioQualityType) {
        mLayoutPreview.setVisibility(View.GONE);
        mCountDownTimerView.countDownAnimation(CountDownTimerView.DEFAULT_COUNTDOWN_NUMBER);
        mCountDownTimerView.setOnCountDownListener(new ICountDownTimerView.ICountDownListener() {
            @Override
            public void onCountDownComplete() {
                startLive(roomName, coverUrl, audioQualityType);
            }
        });
    }

    private void startLive(final String roomName, final String coverUrl, final int audioQualityType) {
        // 开始创建房间
        TRTCLiveRoomDef.TRTCCreateRoomParam roomParam = new TRTCLiveRoomDef.TRTCCreateRoomParam();
        roomParam.roomName = roomName;
        //roomParam.coverUrl = mAnchorInfo.avatarUrl;
        roomParam.coverUrl = TextUtils.isEmpty(coverUrl) || coverUrl.contains("null") ? mAnchorInfo.avatarUrl : coverUrl;
        TXTRTCLiveRoom.getInstance().setIsRecord(mLayoutPreview.getRecord() == 1);
        mLiveRoom.createRoom(mRoomId, roomParam, new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (getContext() == null) {
                    TUILiveLog.d(TAG, "getContext is null!");
                    return;
                }

                //Toast.makeText(getContext(), R.string.live_create_room_success, Toast.LENGTH_SHORT).show();
                if (code == 0) {
                    mIsEnterRoom = true;
                    onCreateRoomSuccess(audioQualityType);
                    if (mLiveRoomAnchorLayoutDelegate != null) {
                        mRoomInfo.roomName = roomName;
                        mRoomInfo.roomId = mRoomId;
                        mRoomInfo.ownerId = mOwnerUserId;
                        mRoomInfo.ownerName = TUIKitLive.getLoginUserInfo().getNickName();
                        mLiveRoomAnchorLayoutDelegate.onRoomCreate(mRoomInfo);
                    }
                } else {
                    Toast.makeText(getContext(), R.string.live_create_room_fail, Toast.LENGTH_SHORT).show();
                    showErrorAndQuit(code, msg);
                }
            }
        });
    }

    public void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mRoomId = bundle.getInt("room_id");
            mPushAddress = bundle.getString("push_address");
            data = bundle.getString("liveRoomInfo");
            giftUrl = bundle.getString("gift_url");
            if (!TextUtils.isEmpty(data)) {
                liveRoomInfo = (LiveRoomInfo) GsonUtil.getInstance().fromJson(data, LiveRoomInfo.class);
                mRoomId = Integer.parseInt(liveRoomInfo.getData().getRoom_id());
                updateTopToolBarFromPlatform(liveRoomInfo);
                updateTopAudienceInfo();
            }
        }
    }

    private void initBeauty() {
        mLayoutBeautyPanel = new BeautyPanel(getContext());
//        String beautyJson = BeautyUtils.readAssetsFile("live_beauty_sm_data.json");
//        BeautyInfo beautyInfo = BeautyUtils.createBeautyInfo(beautyJson);
//        mLayoutBeautyPanel.setBeautyInfo(beautyInfo);
//        mLayoutBeautyPanel.setBeautyManager(mLivePusher.getBeautyManager());
//        mLayoutBeautyPanel.setCurrentFilterIndex(1);
//        mLayoutBeautyPanel.setCurrentBeautyIndex(0);
    }

    //PK视图，group需要setVisibility
    private void initPKView(View view) {
        // mGroupPkView = view.findViewById(R.id.group_pk_view);
//        mLLPkLayer = view.findViewById(R.id.ll_pk_layer_contest);
//        mLLPkLayerContest = view.findViewById(R.id.ll_pk_layer_contest);
//        mRlPkTimeDisplayContainer = view.findViewById(R.id.rl_pk_time_display_container);
//        mIvPkContestVictor = view.findViewById(R.id.live_circle_contest_victor);
//        mIvPkContestPing = view.findViewById(R.id.live_circle_contest_ping);
//        mIvPkContestFail = view.findViewById(R.id.live_circle_contest_fail);
//        mPkTimer = view.findViewById(R.id.ic_live_pk_rect_timer);
//        mPkMeNo = view.findViewById(R.id.tv_live_pk_me_no);
//        mPkotherNo = view.findViewById(R.id.tv_live_pk_other_no);
        mPkViewLayout = view.findViewById(R.id.layout_pk_view);
        mPkViewLayout.setIsAnchor(true);
        mPkViewLayout.setOnPkViewListener(new PkViewLayout.OnPkViewListener() {
            @Override
            public void doPkAudienceClick(String uid, String otherId, boolean isUs) {
                showPkTopAudienceView(uid, otherId, isUs);
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
                if (isMute) {
                    showPkMutePop(uid, name);
                } else {
                    TRTCCloud.sharedInstance(getActivity()).muteRemoteAudio(uid, false);
                    TXTRTCLiveRoom.getInstance().doMuteRemoteAnchor(uid, false);
                    mPkViewLayout.refreshMuteStatus(false);
                }
            }
        });
    }

    private void initBottomToolBar() {
        View view = mAnchorLiveRoomView;
        mBottomToolBarLayout = view.findViewById(R.id.layout_bottom_toolbar);
        mBottomToolBarLayout.setOnTextSendListener(new InputTextMsgDialog.OnTextSendDelegate() {
            @Override
            public void onTextSend(String msg, boolean tanmuOpen) {
                //发送按钮点击
                final ChatEntity entity = new ChatEntity();
                String anchorUserName = !TextUtils.isEmpty(liveRoomInfo.getData().getNickname()) ? liveRoomInfo.getData().getNickname() : "";
                entity.setSenderName(anchorUserName);
                entity.setContent(msg);
                entity.setType(Constants.TEXT_TYPE);
                entity.setLevel(-1);
                String anchorUid = !TextUtils.isEmpty(liveRoomInfo.getData().getUid()) ? liveRoomInfo.getData().getUid() : ProfileManager.getInstance().getUserModel().userId;
                entity.setUid(anchorUid);
                //增加场控/vip标识
                String admin = !TextUtils.isEmpty(is_group_admin) ? is_group_admin : "0";
                entity.setIs_group_admin(admin);
                entity.setLive_group_role(IMUtils.getGroupRole(admin, anchorUid));
                entity.setLive_user_role(String.valueOf(TUIKitLive.getLoginUserInfo().getRole()));
                entity.setWealth_val(wealth_val);
                entity.setWealth_val_switch(wealth_val_switch);
                entity.setAnchor_level(anchorLevel);
                entity.setUser_level(audienceLevel);
                entity.setName_color(name_color);
                entity.setBackground_color(backgroupColor);
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.REPORT_LIVE_MESSAGE,
                        "", GsonUtil.getInstance().toJson(new ReportLiveMessageBean(mOwnerUserId, String.valueOf(mRoomId), msg))));


                if (tanmuOpen) { //发弹幕
                    TimCustomMessage timRoomMessageBean = new TimCustomMessage();
                    if (mAnchorInfo != null) {
                        timRoomMessageBean.setAnchor_uid(anchorUid);
                    }
                    timRoomMessageBean.setContent(msg);
                    String data = GsonUtil.GsonString(timRoomMessageBean);
                    sendLiveEvent("tanmuOpenSendTxt", data);
                } else { //发文字消息
                    if (mAnchorInfo != null) {
                        IMUtils.sendText(mLiveRoom, entity, new IMUtils.OnTextSendListener() {
                            @Override
                            public void sendSuc() {
                                //发送消息成功之后 再插入本地数据
                                updateIMMessageList(entity);
                            }
                        });
                    }
                }
            }
        });

        // 初始化PK按钮
        mButtonInvitationPk = new CircleImageView(getContext());
        mButtonInvitationPk.setImageResource(R.drawable.live_pk_btn_icon);
        mButtonInvitationPk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsPkStatus) {
                    showStopPkTips();
                } else {
                    requestPermissions(PermissionUtils.getLivePermissions(), new OnPermissionGrandCallback() {
                        @Override
                        public void onAllPermissionsGrand() {
                            showPKView();
                        }
                    });
                }
            }
        });
        mButtonInvitationPk.setVisibility(Config.getPKButtonStatus() ? View.VISIBLE : View.GONE);

        // 初始化音乐按钮
        CircleImageView btnMusic = new CircleImageView(getContext());
        btnMusic.setImageResource(R.drawable.live_ic_music);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnchorAudioPanel != null) {
                    mAnchorAudioPanel.show();
                }
            }
        });

        // 初始化分享按钮
        CircleImageView buttonShare = new CircleImageView(getContext());
        buttonShare.setId(mRoomId);
        buttonShare.setImageResource(R.drawable.ic_live_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSharePanel();

            }
        });

        // 初始化消息按钮
        CircleImageView buttonMessage = new CircleImageView(getContext());
        buttonMessage.setId(mRoomId);
        buttonMessage.setImageResource(R.drawable.live_message_btn_icon);
        buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // showSharePanel();
                showConversationCard();
            }
        });


        // 初始化美颜按钮
        CircleImageView buttonBeauty = new CircleImageView(getContext());
        buttonBeauty.setImageResource(R.drawable.live_ic_beauty);
        buttonBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mLayoutBeautyPanel != null) {
//                    mLayoutBeautyPanel.show();
//                }
                if (tiPanelLayout != null) {
                    tiPanelLayout.showBeautyPanel();
                }
            }
        });

        // 初始化切换摄像头按钮
        CircleImageView buttonSwitchCam = new CircleImageView(getContext());
        buttonSwitchCam.setImageResource(R.drawable.live_ic_switch_camera_on);
        buttonSwitchCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mLiveRoom.switchCamera();
                mFrontCamera = !mFrontCamera;
                mLivePusher.getDeviceManager().switchCamera(mFrontCamera);
                if (mLivePusher.getDeviceManager().isFrontCamera()) {
                    mLiveRoom.setMirror(true); //前置改为镜像
                } else {
                    mLiveRoom.setMirror(false); //后置改为非镜像
                }
            }
        });

        // 初始化礼物按钮
        CircleImageView buttonGift = new CircleImageView(getContext());
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

        //初始化设置按钮
        CircleImageView buttonSetting = new CircleImageView(getContext());
        buttonSetting.setId(mRoomId);
        buttonSetting.setImageResource(R.drawable.live_setting_btn_icon);
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_ANCHOR_SETTING, "",
                        GsonUtil.getInstance().toJson(new LiveSettingStateBean(isAudioMute, isVideoMute, mFrontCamera))));
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


        //屏幕直播
        CircleImageView buttonScreen = new CircleImageView(TUIKitLive.getAppContext());
        buttonScreen.setId(mRoomId);
        buttonScreen.setImageResource(R.drawable.ic_live_room_screen_capture);
        //buttonScreen.setCircleBackgroundColor(getActivity().getResources().getColor(R.color.read_dot_bg));
        buttonScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mtScreenCaptureMode) {
                    stopScreenCapture();
                } else {
                    tryToScreenCapture();
                }
            }
        });

        // 初始化退出房间按钮
        /*CircleImageView buttonExitRoom = new CircleImageView(getContext());
        buttonExitRoom.setImageResource(R.drawable.live_exit_room);
        buttonExitRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preExitRoom();
            }
        });*/
        //mBottomToolBarLayout.setRightButtonsLayout(Arrays.asList(mButtonInvitationPk, btnMusic, buttonBeauty, buttonSwitchCam, buttonExitRoom));
        //mBottomToolBarLayout.setRightButtonsLayout(Arrays.asList(buttonShare,buttonMessage,buttonGift,buttonSetting));

        List<View> buttonList = new ArrayList<>();
        buttonList.add(buttonShare);
        buttonList.add(mButtonInvitationPk);
        buttonList.add(buttonMessage);
        buttonList.add(buttonLottery);
        buttonList.add(buttonGift);
        if (!mtScreenCaptureMode) {
            buttonList.add(buttonSetting);
        }
        buttonList.add(buttonScreen);
        mBottomToolBarLayout.setRightButtonsLayout(buttonList);
        mLayoutTopToolBar.findViewById(R.id.ll_audiences_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preExitRoom();
            }
        });
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

    private void showPKView() {
        if (mCurrentStatus == TRTCLiveRoomDef.ROOM_STATUS_PK) {
            return;
        }
        if (mLiveRoomAnchorLayoutDelegate != null) {
            mSelectMemberView.show();
            mSelectMemberView.refreshView();
            mLiveRoomAnchorLayoutDelegate.getRoomPKList(new TUILiveRoomAnchorLayout.OnRoomListCallback() {
                @Override
                public void onSuccess(List<String> roomIdList) {
                    List<Integer> roomList = new ArrayList<>();
                    for (String id : roomIdList) {
                        try {
                            roomList.add(Integer.parseInt(id));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mLiveRoom.getRoomInfos(roomList, new TRTCLiveRoomCallback.RoomInfoCallback() {
                        @Override
                        public void onCallback(int code, String msg, List<TRTCLiveRoomDef.TRTCLiveRoomInfo> list) {
                            if (code == 0) {
                                List<SelectMemberView.MemberEntity> memberEntityList = new ArrayList<>();
                                for (TRTCLiveRoomDef.TRTCLiveRoomInfo info : list) {
                                    //过滤哪些没有 userId 的房间（主播不在线）
                                    if (info.roomId == mRoomId || TextUtils.isEmpty(info.ownerId)) {
                                        continue;
                                    }
                                    SelectMemberView.MemberEntity memberEntity = new SelectMemberView.MemberEntity();
                                    memberEntity.userId = info.ownerId;
                                    memberEntity.userAvatar = info.coverUrl;
                                    memberEntity.userName = info.ownerName;
                                    memberEntity.roomId = info.roomId;
                                    memberEntityList.add(memberEntity);
                                }
                                if (memberEntityList.size() == 0) {
                                    mSelectMemberView.setTitle("暂无可PK主播");
                                } else {
                                    mSelectMemberView.setTitle("PK列表");
                                }
                                mSelectMemberView.setList(memberEntityList);
                            } else {
                                mSelectMemberView.setTitle("暂无可PK主播");
                            }
                        }
                    });
                }

                @Override
                public void onFailed() {

                }
            });
        }
    }

    public void updateIMMessageList(ChatEntity entity) {
        mChatLayout.addMessageToList(entity);
    }

    /**
     * 处理点赞消息显示
     */
    public void handlePraiseMsg(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        ChatEntity entity = new ChatEntity();

        entity.setSenderName(getString(R.string.live_notification));
        if (TextUtils.isEmpty(userInfo.userName)) {
            entity.setContent(getString(R.string.live_user_click_like, userInfo.userId));
        } else {
            entity.setContent(getString(R.string.live_user_click_like, userInfo.userName));
        }
        if (mHeartLayout != null) {
            mHeartLayout.addFavor();
        }
        entity.setType(Constants.MEMBER_ENTER);
        updateIMMessageList(entity);
    }

    /**
     * 处理弹幕消息的显示
     */
    public void handleDanmuMsg(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, String text) {
        //updateIMMessageList(IMUtils.handleTextMsg(userInfo, text));
        if (mDanmakuManager != null) {
            //这里暂时没有头像，所以用默认的一个头像链接代替
            mDanmakuManager.addDanmu(userInfo.avatarUrl, userInfo.userName, text);
        }
    }


    /**
     * 处理礼物弹幕消息-调用平台接口
     */
    private void handleGiftMsg(final TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, final String message) {
        String type = GsonUtil.getType(message);
        if (type.equals("Number") || type.equals("String")) {
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
//                if (tvCoin != null) {
//                    if (V2TIMManager.getInstance().getLoginUser().equals(giftMessage.extra.uid)) {
//                        tvCoin.setText(extraInfo.rich_beans);
//                        if (giftPanelView != null && giftPanelView.isShowing()) {
//                            giftPanelView.refreshUserLevelProgress();
//                        }
//                    }
//                }
            }
        }
    }


    protected void startPreview() {
        // 打开本地预览，传入预览的 View
        mVideoViewAnchor.setVisibility(View.VISIBLE);
        mLiveRoom.startCameraPreview(true, mVideoViewAnchor, null);
    }

    public void finishRoom() {
        mLayoutBeautyPanel.clear();
        mLiveRoom.stopCameraPreview();
        if (mLiveRoomAnchorLayoutDelegate != null) {
            mLiveRoomAnchorLayoutDelegate.onClose();
        }
    }

    public void destroyRoom() {
        mIsEnterRoom = false;
        mLayoutBeautyPanel.clear();
        mLiveRoom.stopCameraPreview();
        mLiveRoom.destroyRoom(new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code == 0) {
                    TUILiveLog.d(TAG, "destroy room ");
                } else {
                    TUILiveLog.d(TAG, "destroy room failed:" + msg);
                }
            }
        });
        mLiveRoom.setDelegate(null);
        if (mLiveRoomAnchorLayoutDelegate != null) {
            mLiveRoomAnchorLayoutDelegate.onRoomDestroy(mRoomInfo);
        }

        // 退房后，清理AudioPanel的相关设置
        if (mAnchorAudioPanel != null) {
            mAnchorAudioPanel.unInit();
        }
    }

    private void onCreateRoomSuccess(int audioQualityType) {
        // 创建房间成功
        mGroupAfterLive.setVisibility(View.VISIBLE);
        mGroupButtomView.setVisibility(View.VISIBLE);
        //updateTopToolBar();
        mLiveRoom.setAudioQuality(audioQualityType);
        //mLivePusher = new V2TXLivePusherImpl(getActivity(), V2TXLiveDef.V2TXLiveMode.TXLiveMode_RTMP);
        //mLivePusher.startMicrophone();
        //给消息列表推送消息通知
        if (onLiveCreateRoomCallBack != null) {
            onLiveCreateRoomCallBack.createRoomSuccess();
        }
        updateAnchorInfo();
        //mLiveRoom.setMirror(true);
        // LiveMessageSpan.view2Bitmap()
        // View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_audience_watermark,mRootView);
        //关闭水印
        //  TRTCCloud.sharedInstance(getActivity()).setWatermark(null,TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,0.05F,0.1F,(0.1f));

        // 创建房间成功，开始推流
        mLiveRoom.startPublish("", new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (getContext() == null) {
                    TUILiveLog.d(TAG, "getContext is null!");
                    return;
                }

                if (code == 0) {
                    mStartTime = System.currentTimeMillis();
                    //设置屏幕持续点亮
                    UIUtil.setGoToSleep(getActivity());
                    TUILiveLog.d(TAG, "start live success");
                    // closeLocalVideo();
                    if (isVideoMute) {
                        muteLocalVideo();
                    } else {
                        initVideoMirror();
                    }
                    // TRTCCloud.sharedInstance(getActivity()).getDeviceManager().isFrontCamera();
                    EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.TEST_NET_SPEED, "", ""));
                } else {
                    Toast.makeText(getContext(), "start publish failed:" + msg, Toast.LENGTH_SHORT).show();
                    TUILiveLog.e(TAG, "start live failed:" + msg);
                }


                // TRTCCloud.sharedInstance(getActivity()).setWatermark(BitmapFactory.decodeResource(getResources(),R.drawable.ic_input_face_normal),TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,0.5F,0.5F,0.5f);
            }
        });
    }

    private void updateTopToolBarFromPlatform(LiveRoomInfo liveRoomInfo) {
        if (liveRoomInfo == null) {
            return;
        }
        if (liveRoomInfo.getData().getUid().equals(mOwnerUserId)) { //表明当前是主播
            mAnchorInfo.userId = liveRoomInfo.getData().getUid();
            mAnchorInfo.userName = liveRoomInfo.getData().getNickname();
            mAnchorInfo.avatarUrl = liveRoomInfo.getData().getHead_pic();
            mAnchorInfo.sex = liveRoomInfo.getData().getSex();
            mAnchorInfo.type = liveRoomInfo.getData().getRole();
            mAnchorInfo.age = liveRoomInfo.getData().getAge();
            mAnchorInfo.watchNo = liveRoomInfo.getData().getWatchsum();
            mLayoutTopToolBar.setHasFollowed(true); //主播自己不能关注自己，隐藏关注视图
            TUILiveLog.d(TAG, "updateAnchorInfo mAnchorInfo: " + mAnchorInfo.toString());
            saveRecordState("1".equals(liveRoomInfo.getData().getIs_record()));
        }
        mLayoutTopToolBar.setAnchorInfo(mAnchorInfo); //传递主播信息
        mLayoutTopToolBar.updateCurrentBeansCount(liveRoomInfo.getData().getBeans_current_count());
        if (!liveRoomInfo.getData().getNickname().equals(liveRoomInfo.getData().getLive_title())) {
            mLayoutTopToolBar.updateLiveTitle(liveRoomInfo.getData().getLive_title());
        }
    }

    private void updateAnchorInfo() {
        TXRoomService.getInstance().getUserInfo(Arrays.asList(mOwnerUserId), new TXUserListCallback() {
            @Override
            public void onCallback(int code, String msg, List<TXUserInfo> list) {
                if (code == 0) {
                    for (TXUserInfo info : list) {
                        if (info.userId.equals(mOwnerUserId)) {
                            mAnchorInfo.userId = info.userId;
                            mAnchorInfo.userName = info.userName;
                            mAnchorInfo.avatarUrl = info.avatarURL;
                            mLayoutTopToolBar.setAnchorInfo(mAnchorInfo);
                            mLayoutTopToolBar.setHasFollowed(true);
                            TUILiveLog.d(TAG, "updateAnchorInfo mAnchorInfo: " + mAnchorInfo.toString());
                        }
                    }
                } else {
                    TUILiveLog.e(TAG, "code: " + code + " msg: " + msg);
                }
            }
        });
    }

    /**
     * 显示直播结果的弹窗，如：观看数量、点赞数量、直播时长数
     */
    protected void showPublishFinishDetailsDialog() {
        //确认则显示观看detail
        String roomId = mRoomId == 0 ? liveRoomInfo.getData().getRoom_id() : mRoomId + "";
        FinishDetailDialog dialog = new FinishDetailDialog(onLiveOperateClickListener, roomId, (1 == mLayoutPreview.getRecord()));
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
            dialog.show(getFragmentManager().beginTransaction(), "");
        }
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
            dialogFragment.show(getFragmentManager(), "ExitConfirmDialog");
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
        dialogFragment.show(getFragmentManager(), "ExitConfirmDialog");
    }

    /**
     * 显示错误并且退出直播的弹窗
     *
     * @param errorCode
     * @param errorMsg
     */
    protected void showErrorAndQuit(int errorCode, String errorMsg) {
        if (mErrorDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.TUILiveDialogTheme)
                    .setTitle(R.string.live_error)
                    .setMessage(errorMsg)
                    .setNegativeButton(R.string.live_get_it, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mErrorDialog.dismiss();
                            destroyRoom();
                            finishRoom();
                        }
                    });
            mErrorDialog = builder.create();
        }
        if (mErrorDialog.isShowing()) {
            mErrorDialog.dismiss();
        }
        mErrorDialog.show();
    }

    private void setAnchorViewFull(boolean isFull) {
        if (isFull) {
            ConstraintSet set = new ConstraintSet();
            set.clone(mRootView);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            set.applyTo(mRootView);
        } else {
            ConstraintSet set = new ConstraintSet();
            set.clone(mRootView);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.TOP, R.id.layout_pk_container, ConstraintSet.TOP);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.BOTTOM, R.id.layout_pk_container, ConstraintSet.BOTTOM);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.END, R.id.gl_vertical, ConstraintSet.END);
            set.applyTo(mRootView);
        }
    }

    private void updateTopAudienceInfo() {
        mGetAudienceRunnable = new Runnable() {
            @Override
            public void run() {
                mLiveRoom.getAudienceList(new TRTCLiveRoomCallback.UserListCallback() {
                    @Override
                    public void onCallback(int code, String msg, List<TRTCLiveRoomDef.TRTCLiveUserInfo> list) {
                        if (code == 0) {
                            //addAudienceListLayout(list);
                        } else {
                            mHandler.postDelayed(mGetAudienceRunnable, 2000);
                        }
                    }
                });
            }
        };
        // 为了防止进房后立即获取列表不全，所以增加了一个延迟
        mHandler.postDelayed(mGetAudienceRunnable, 2000);
    }

    public void updateTopAudienceInfoFromPlatform(List<String> mAnchorUserIdList, List<String> mTopIdList) {
        if (mAnchorUserIdList != null && mAnchorUserIdList.size() > 0) {
            addAudienceListLayout(mAnchorUserIdList);
            setAudienceTopListLayout(mTopIdList);
        } else {
            mHandler.postDelayed(mGetAudienceRunnable, 2000);
        }
    }

    private void addAudienceListLayout(List<String> list) {
        mLayoutTopToolBar.addAudienceListUser(list);
    }

    private void setAudienceTopListLayout(List<String> list) {
        mLayoutTopToolBar.setAudienceTopListUser(list);
    }

    private void addAudienceListLayout(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        mLayoutTopToolBar.addAudienceListUser(userInfo);
    }

    private void removeAudienceListLayout(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        mLayoutTopToolBar.removeAudienceUser(userInfo);
    }

    private void judgeRedDotShow() {
        if (mCurrentStatus == TRTCLiveRoomDef.ROOM_STATUS_PK) {
            mImageRedDot.setVisibility(View.GONE);
        } else {
            if (mLinkMicListDialog.getList().size() > 0) {
                mImageRedDot.setVisibility(View.VISIBLE);
            } else {
                mImageRedDot.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mGetAudienceRunnable);
        mAnchorAudioPanel = null;
        stopPush();
        mVideoViewAnchor.onDestroy();
        EventBus.getDefault().unregister(this);
        mLayoutTopToolBar.removeCountDown();//停止倒计时
        if (mtScreenCaptureMode) {
            stopScreenCapture();
        }
    }

    public TXCloudVideoView getVideoView() {
        return mVideoViewAnchor;
    }

    public void setPreviewOnClickCover(LiveRoomPreviewLayout.PreviewOnClickCover previewOnClickCover) {
        mPreviewOnClickCover = previewOnClickCover;
    }

    public void setPreviewOnLoadCover(LiveRoomPreviewLayout.PreviewOnLoadCover previewOnLoadCover) {
        mPreviewOnLoadCover = previewOnLoadCover;
    }

    @Override
    public void onClickAnchorAvatar() {
        if (onLiveOperateClickListener != null) {
            onLiveOperateClickListener.onClickAnchorAvatar(mOwnerUserId, mOwnerUserId);
        }
    }

    @Override
    public void onClickFollow(TRTCLiveRoomDef.LiveAnchorInfo liveAnchorInfo) {

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
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_ONLINE_USER, "", V2TIMManager.getInstance().getLoginUser()));
    }

    @Override
    public void onClickTopRank() {
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_TOP_RANK, "", ""));
    }

    @Override
    public void onClickClose() {

    }

    public void onAt(String at) {
        if (!TextUtils.isEmpty(at) && mBottomToolBarLayout != null) {
            mBottomToolBarLayout.onAt(at);
        }
    }

    public void setOnLiveOperateClickListener(LiveRoomAudienceFragment.OnLiveOperateClickListener onLiveOperateClickListener) {
        this.onLiveOperateClickListener = onLiveOperateClickListener;
    }

    public LiveRoomAudienceFragment.OnLiveOperateClickListener onLiveOperateClickListener;

    private void stopPush() {
        if (!mIsPushing) {
            return;
        }
        // 停止本地预览
        mLivePusher.stopCamera();
        // 移除监听
        mLivePusher.setObserver(null);
        // 停止推流
        mLivePusher.stopPush();
        mIsPushing = false;
    }

    public void setOnLiveCreateRoomCallBack(OnLiveCreateRoomCallBack onLiveCreateRoomCallBack) {
        this.onLiveCreateRoomCallBack = onLiveCreateRoomCallBack;
    }

    private OnLiveCreateRoomCallBack onLiveCreateRoomCallBack;

    public interface OnLiveCreateRoomCallBack {
        void createRoomSuccess();
    }


    void initListener() {
        mChatLayout.setOnChatLayoutItemClickListener(new ChatLayout.OnChatLayoutItemClickListener() {
            @Override
            public void onChatUserItemClick(String uid) {
                showAudienceCard(uid);
                //ToastUtil.toastShortMessage("uid = " + uid);
//                if (!TextUtils.isEmpty(uid)) {
//                    String roomId = mRoomId == 0 ? liveRoomInfo.getData().getRoom_id() : mRoomId + "";
//                    TimCustomMessage timRoomMessageBean = new TimCustomMessage();
//                    timRoomMessageBean.setUid(uid);
//                    timRoomMessageBean.setRoomId(roomId);
//                    timRoomMessageBean.setIs_group_admin(!TextUtils.isEmpty(is_group_admin) ? is_group_admin : "");
//                    if (liveRoomInfo != null) {
//                        timRoomMessageBean.setAnchor_uid(!TextUtils.isEmpty(liveRoomInfo.getData().getUid()) ? liveRoomInfo.getData().getUid() : "");
//                    }
//                    String data = GsonUtil.GsonString(timRoomMessageBean);
//                    sendLiveEvent(LiveEventConstant.CLICK_LIVE_USER, data);
//                }
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
                            //setIsNoTalking(liveUserData.getSetNotalking());
                        } else if (liveUserData.getActionType().equals("3")) {
                            //mLayoutTopToolBar.updateCurrentBeansCount(liveUserData.getRich_beans());
                            if (tvCoin != null) {
                                tvCoin.setText(liveUserData.getRich_beans());
                            }
                        }
                    } else if (msgType.equals(MassageType.CLOSE_ROOM_MSG)) {

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
                        if (msgType.equals(USER_DATA)) {//更新用户信息
                            TimCustomMessage timCustomMessage = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), TimCustomMessage.class);
                            ChatEntity chatEntity = IMUtils.handleUserData(timCustomMessage);
                            updateIMMessageList(chatEntity);
                            setIs_group_admin(timCustomMessage.is_group_admin);
                        } else if (msgType.equals(JOIN_ROOM_MSG)) {//加入直播间(text/nickName/senderUserID)
                            CustomMessage joinMsg = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
                            LiveJoinAvChatRoom liveJoinAvChatRoom = GsonUtil.GsonToBean(event.getCloudCustomData(), LiveJoinAvChatRoom.class);
                            String nickname = !TextUtils.isEmpty(event.getMessage().getNickName()) ? event.getMessage().getNickName() : event.getMessage().getSender();
                            String sendId = !TextUtils.isEmpty(event.getMessage().getSender()) ? event.getMessage().getSender() : event.getMessage().getSender();
                            //updateIMMessageListStartAnim(IMUtils.joinRoom(joinMsg,nickname,sendId));
                            if (!"1".equals(liveJoinAvChatRoom.getShowType())) {
                                showJoinInfo(IMUtils.joinRoom(joinMsg,liveJoinAvChatRoom, nickname, sendId));
                            }
                            if (event.getMessage().getSender().equals(ProfileManager.getInstance().getUserModel().userId)) {
                                setIs_group_admin(liveJoinAvChatRoom.getIs_group_admin());
                                wealth_val = joinMsg.getWealth_val();
                                wealth_val_switch = joinMsg.getWealth_val_switch();
                                name_color = joinMsg.getName_color();
                                backgroupColor = joinMsg.getBackground_color();
                            }
                            //setIs_group_admin(liveJoinAvChatRoom.getIs_group_admin());
                        } else if (msgType.equals(PROMPT_MSG)) {//公告栏(text/nickName)
                            CustomMessage timCustomRoom = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
                            String nickname = !TextUtils.isEmpty(event.getMessage().getNickName()) ? event.getMessage().getNickName() : event.getMessage().getSender();
                            String selfUserId = ProfileManager.getInstance().getUserModel().userId;
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
                            LiveWatchAvChatRoom watchAvChatRoom = GsonUtil.GsonToBean(event.getCloudCustomData(), LiveWatchAvChatRoom.class);
                            mLayoutTopToolBar.updateAudienceNumber(watchAvChatRoom.getWatchsum());
                            updateTopAudienceInfoFromPlatform(watchAvChatRoom.getWatchuser(), watchAvChatRoom.getTop_gift_uid());
                            mTotalMemberCount = watchAvChatRoom.getWatchsum();
                        } else if (msgType.equals(CHANGE_ROOM)) {
                            Log.e(TAG, event.getCloudCustomData());
                            LiveChangeInfoChatRoom liveChangeInfoChatRoom = GsonUtil.GsonToBean(event.getCloudCustomData(), LiveChangeInfoChatRoom.class);
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
                        } else if (msgType.equals(CREATE_ROOM)) {
                            Log.e(TAG, event.getCloudCustomData());
                            LiveCreateAvChatRoom liveCreateAvChatRoom = GsonUtil.GsonToBean(event.getCloudCustomData(), LiveCreateAvChatRoom.class);
                            mLayoutTopToolBar.updateContestNo(liveCreateAvChatRoom.getAnchor_info().getHour_ranking());
                            mLayoutTopToolBar.updateDurationTime(liveCreateAvChatRoom.getTime());
                            wealth_val = liveCreateAvChatRoom.getWealth_val();
                            wealth_val_switch = liveCreateAvChatRoom.getWealth_val_switch();
                            name_color = liveCreateAvChatRoom.getName_color();
                            backgroupColor = liveCreateAvChatRoom.getBackground_color();
                            setIs_group_admin(liveCreateAvChatRoom.getIs_group_admin());
                            liveTitle = liveCreateAvChatRoom.getAnchor_info().getLive_title();
                            livePoster = liveCreateAvChatRoom.getAnchor_info().getLive_poster();
                            anchorLevel = liveCreateAvChatRoom.getAnchor_level();
                            audienceLevel = liveCreateAvChatRoom.getUser_level();
                            //initLeaveBitmap();
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
                        } else if (msgType.equals(RETRY_INVITE_PK)) { //pk再来一局
                            String actionType = jsonObject.optString("actionType");
                            if ("0".equals(actionType)) {
                                showPkAgainTip();
                            } else if ("1".equals(actionType)) {
                                ToastUtil.toastShortMessage("对方拒绝再次pk");
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
                            if (!TextUtils.isEmpty(svgaUrL)){
                                mGiftAnimatorLayout.showAnmation(svgaUrL);
                            }
                        } else if (msgType.equals(MassageType.KICK_MIC_USER)) {
                            String micUid = jsonObject.optString("mic_uid");
                            if (!TextUtils.isEmpty(micUid)) {
                                mLiveRoom.kickoutJoinAnchor(micUid,null);
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
        } else if (liveMessageCustomDataBean.getCommand().equals(String.valueOf(Constants.IMCMD_ANNOUNCEMENT))) {
            LiveAnnouncementBean.MessageBean messageBean = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), LiveAnnouncementBean.MessageBean.class);
            mLayoutTopToolBar.showLiveAnnouncement(messageBean);
        } else if (liveMessageCustomDataBean.getCommand().equals(String.valueOf(Constants.IMCMD_DANMU_NEW))) {
            //Log.d("danmaku",liveMessageCustomDataBean.getMessage());
            CustomMessage customMessage = GsonUtil.GsonToBean(liveMessageCustomDataBean.getMessage(), CustomMessage.class);
            String nickname = !TextUtils.isEmpty(event.getMessage().getNickName()) ? event.getMessage().getNickName() : event.getMessage().getSender();
            String sendId = !TextUtils.isEmpty(event.getMessage().getSender()) ? event.getMessage().getSender() : event.getMessage().getSender();
            updateIMMessageList(handleCustomTextMsg(nickname, sendId, customMessage));
        }
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

    private void showJoinInfo(final ChatEntity entity) {

        updateIMMessageList(entity);

        if (true) {
            return;
        }


        Log.d("logutil", entity.getSenderName() + " " + entity.getLive_group_role() + " " + entity.getLive_user_role());
        llItemJoinMessage.setVisibility(View.VISIBLE);
        String joinStr = entity.getSenderName() + " " + entity.getContent();

        SpannableString spanString = new SpannableString(joinStr);
        // 根据名称计算颜色
        spanString.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.live_color_send_name5)),
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
            UserIdentityUtils.showUserIdentity(getActivity(), Integer.valueOf(entity.getLive_user_role()), userIdentity);
        } else {
            userIdentity.setVisibility(View.GONE);
        }

        llItemJoinMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(entity.getUid())) {
                    TimCustomMessage timRoomMessageBean = new TimCustomMessage();
                    timRoomMessageBean.setUid(entity.getUid());
                    timRoomMessageBean.setRoomId(TextUtils.isEmpty(String.valueOf(mRoomId)) ? String.valueOf(mRoomId) : "");
                    timRoomMessageBean.setIs_group_admin(!TextUtils.isEmpty(is_group_admin) ? is_group_admin : "");
                    if (liveRoomInfo != null) {
                        timRoomMessageBean.setAnchor_uid(!TextUtils.isEmpty(liveRoomInfo.getData().getUid()) ? liveRoomInfo.getData().getUid() : "");
                    }
                    String data = GsonUtil.GsonString(timRoomMessageBean);
                    sendLiveEvent("onChatLayoutItemClick", data);
                }
            }
        });
        ObjectAnimator amin1 = AnimationUtils.createFadesInFromLtoR(
                llItemJoinMessage, -llItemJoinMessage.getWidth(), 0, 400, new OvershootInterpolator());
        amin1.start();
    }


    //自动同意连麦
    void autoAgreeLinkRequest(String userId) {
        if (getContext() == null) {
            TUILiveLog.d(TAG, "getContext is null!");
            Toast.makeText(getContext(), "getContext is null!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mIsPkStatus) {
            Toast.makeText(getContext(), R.string.live_pk_status, Toast.LENGTH_SHORT).show();
            return;
        }
        if (mAnchorUserIdList != null && mAnchorUserIdList.size() >= getMaxLinkNum()) {
            Toast.makeText(getContext(), R.string.live_warning_link_user_max_limit, Toast.LENGTH_SHORT).show();
            inviteUserList.remove(userId);
            mLiveRoom.responseJoinAnchor(userId, false, "当前连麦数量已达最大限制");
            return;
        }
        mLiveRoom.responseJoinAnchor(userId, true, getString(R.string.live_anchor_accept));
        mLinkMicListDialog.removeMemberEntity(userId);
        mLinkMicListDialog.dismiss();
        judgeRedDotShow();
        mIsLinkMicStatus = true;
        inviteUserList.remove(userId);
    }

    ArrayList<String> inviteUserList = new ArrayList<>();

    //主播主动邀请用户连麦
    public void inviteLinkUser(final String uid) {
        //当前连麦用户已经到最大连接数
        if (mAnchorUserIdList != null && mAnchorUserIdList.size() >= getMaxLinkNum()) {
            Toast.makeText(getContext(), R.string.live_warning_link_user_max_limit, Toast.LENGTH_SHORT).show();
            return;
        }
        if (mAnchorUserIdList.contains(uid)) {
            ToastUtil.toastShortMessage("该用户已经上麦");
            return;
        }
        ToastUtil.toastShortMessage("已发送邀请");
//        if (inviteUserList.contains(uid)) {
//            return;
//        }
        inviteUserList.add(uid);
        TXRoomService.getInstance().requestJoinAudience("", uid, 30, new TXCallback() {
            @Override
            public void onCallback(int code, String msg) {
                ToastUtil.toastShortMessage(code + "-----" + msg);
                if (code == -1) { //用户拒绝连麦
                    inviteUserList.remove(uid);
                    try {
                        JSONObject msgJson = new JSONObject(msg);
                        if (msgJson.has("reason")) {
                            ToastUtil.toastShortMessage(msgJson.optString("reason"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void showAudienceCard(String uid) {
        if (!TextUtils.isEmpty(uid)) {
            String roomId = mRoomId == 0 ? liveRoomInfo.getData().getRoom_id() : mRoomId + "";
            TimCustomMessage timRoomMessageBean = new TimCustomMessage();
            timRoomMessageBean.setUid(uid);
            timRoomMessageBean.setRoomId(roomId);
            timRoomMessageBean.setIs_group_admin(!TextUtils.isEmpty(is_group_admin) ? is_group_admin : "");
            if (liveRoomInfo != null) {
                timRoomMessageBean.setAnchor_uid(!TextUtils.isEmpty(liveRoomInfo.getData().getUid()) ? liveRoomInfo.getData().getUid() : "");
            }
            String data = GsonUtil.GsonString(timRoomMessageBean);
            sendLiveEvent(LiveEventConstant.CLICK_LIVE_USER, data);
        }
    }

    public void onActivityRestart() {
        TXCGLSurfaceView mTXCGLSurfaceView = Constents.mAnchorTextureView;
        if (mTXCGLSurfaceView != null) {
            ((ViewGroup) mTXCGLSurfaceView.getParent()).removeView(mTXCGLSurfaceView);
            mVideoViewAnchor.addVideoView(mTXCGLSurfaceView);
            Constents.mAnchorTextureView = null;
        }
    }

    GiftPanelViewImp giftPanelView;
    private LinearLayout llAlertGiftList;
    private GiftAdapter mGiftAdapter;

    //展示礼物面板
    public void showGiftPanel(String url, String token) {
        giftPanelView = new GiftPanelViewImp(getContext(), url, token, mOwnerUserId, String.valueOf(mRoomId), true);
        ((GiftPanelViewImp) giftPanelView).setOnGiftPanelOperateListener(new GiftPanelViewImp.OnGiftPanelOperateListener() {
            @Override
            public void OnGiftPanel() {
                sendLiveEvent("OnGiftPanel", "");
            }

            @Override
            public void OnGiftLevelClick() {
                sendLiveEvent(LiveEventConstant.SHOW_MY_LIVE_LEVEL_PAGE, "1");
            }
        });
        ((GiftPanelViewImp) giftPanelView).setOnCoinNumberChangeListener(new GiftPanelViewImp.OnCoinNumberChangeListener() {
            @Override
            public void onCoinNumberChanged(TextView giftCoin) {
                tvCoin = giftCoin;
            }
        });
        GiftInfoDataHandler mGiftInfoDataHandler = new GiftInfoDataHandler();
        mGiftAdapter = new DefaultGiftAdapterImp();
        giftPanelView.init(mGiftInfoDataHandler, mGiftAdapter);
        giftPanelView.setGiftPanelDelegate(new GiftPanelDelegate() {
            @Override
            public void onGiftItemClick(GiftInfo giftInfo) {
                //sendGift(giftInfo);
                if (TextUtils.isEmpty(mOwnerUserId) || giftInfo == null) return;
                giftInfo.sendUser = mOwnerUserId;
                sendLiveEvent(LiveEventConstant.SEND_GIFT, GsonUtil.GsonString(giftInfo));
            }

            @Override
            public void onChargeClick() {

            }
        });
        giftPanelView.show();
    }


    LiveGiftPanel liveGiftPanel;

    public void showGiftPanel() {
        liveGiftPanel = new LiveGiftPanel(getActivity(), String.valueOf(mRoomId), mOwnerUserId, true);
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


    private TextView tvCoin;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveGiftChangedItemNotifyEvent(AppLiveGiftChangedEvent event) {
        ((GiftPanelViewImp) giftPanelView).notifyGiftController(event.changedNo, event.changedInfo);
    }

    public void showSharePanel() {
        mAnchorInfo = mLayoutTopToolBar.getAnchorInfo();
        if (mAnchorInfo != null) {
            ShareAnchorBean shareAnchorBean = new ShareAnchorBean();
            shareAnchorBean.setRoomId(String.valueOf(mRoomId));
            shareAnchorBean.setLiveTitle(TextUtils.isEmpty(liveTitle) ? mAnchorInfo.userName : liveTitle);
            shareAnchorBean.setLivePoster(TextUtils.isEmpty(livePoster) ? mAnchorInfo.avatarUrl : livePoster);
            shareAnchorBean.setAnchorId(mOwnerUserId);
            EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHARE_LIVE, "", GsonUtil.getInstance().toJson(shareAnchorBean)));
        }
    }

    public void showConversationCard() {
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_CONVERSATION, "", ""));
    }

    void updateLinkMicUserList() {
        StringBuilder linkMicUserList = new StringBuilder("");
        for (int i = 0; i < mAnchorUserIdList.size(); i++) {
            linkMicUserList.append(mAnchorUserIdList.get(i));
            linkMicUserList.append(",");
        }
        String linkMicStr = linkMicUserList.toString().endsWith(",") ? linkMicUserList.substring(0, linkMicUserList.length() - 1) : linkMicUserList.toString();
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.UPDATE_LINK_MIC, "", linkMicStr));
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
                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.ANCHOR_START_FLOAT, "", ""));
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

    boolean isVideoMute = false;
    boolean isAudioMute = false;
    ImageView mSelfImageView;

    void closeLocalVideo() {
        isVideoMute = true;
        mSelfImageView = new ImageView(getActivity());
        if (TextUtils.isEmpty(livePoster)) {
            livePoster = TUIKitLive.getLoginUserInfo().getFaceUrl();
        }
        if (mIsPkStatus) {
            suitCoverByView2(livePoster);
        } else {
            suitCoverByView(livePoster);
        }
        TRTCCloud.sharedInstance(getActivity()).muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG, true);
    }

    void openLocalView() {
        isVideoMute = false;
        TRTCCloud.sharedInstance(getActivity()).muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG, false);
        if (mVideoViewAnchor != null && mSelfImageView != null) {
            mVideoViewAnchor.removeView(mSelfImageView);
        }
    }

    public void doCameraFront() {
        openLocalView();
        if (!mFrontCamera) {
            mFrontCamera = true;
            mLiveRoom.switchCamera();
        }
        initVideoMirror();
    }

    public void doCameraBack() {
        openLocalView();

        if (mFrontCamera) {
            mFrontCamera = false;
            mLiveRoom.switchCamera();
        }
        initVideoMirror();
//        if (mLivePusher.getDeviceManager().isFrontCamera()) {
//            mLiveRoom.switchCamera();
//        }
//        if (TRTCCloud.sharedInstance(getActivity()).getDeviceManager().isFrontCamera()) {
//            mFrontCamera = !mFrontCamera;
//            TRTCCloud.sharedInstance(getActivity()).getDeviceManager().switchCamera(mFrontCamera);
//        } else {
//
//        }
//
//        if (TRTCCloud.sharedInstance(getActivity()).getDeviceManager().isFrontCamera()) {
//            mLiveRoom.setMirror(true); //前置改为镜像
//        } else {
//            mLiveRoom.setMirror(false); //后置改为非镜像
//        }
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
        //TRTCCloud.sharedInstance(getActivity()).setLocalRenderParams(trtcRenderParams);

        //TRTCCloud.sharedInstance(getActivity()).setRemoteRenderParams();

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
//        if (mLayoutBeautyPanel != null) {
//            mLayoutBeautyPanel.show();
//        }
    }

    public void doPreViewCamera(boolean isOpenVideo, boolean isFront) {
        isVideoMute = !isOpenVideo;
        if (isOpenVideo) {
            mFrontCamera = isFront;
            if (mFrontCamera) {
                mLayoutPreview.updateCameraType(1);
            } else {
                mLayoutPreview.updateCameraType(2);
            }
            //当前摄像头状态和选择的不一致时 切换摄像头
            if (mFrontCamera != TRTCCloud.sharedInstance(getActivity()).getDeviceManager().isFrontCamera()) {
                mLiveRoom.switchCamera();
            }

            hideCover();
        } else {
            mLayoutPreview.updateCameraType(3);
            showCover();
        }
    }

    void showCover() {
        mSelfImageView = new ImageView(getActivity());
        if (TextUtils.isEmpty(livePoster)) {
            livePoster = TUIKitLive.getLoginUserInfo().getFaceUrl();
        }

        suitCoverByView(livePoster);

//        Glide.with(getActivity()).asBitmap().load(livePoster).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                TRTCCloud.sharedInstance(getActivity()).setVideoMuteImage(resource, 5);
//                if (mSelfImageView != null) {
//                    mSelfImageView.setImageBitmap(resource);
//                    mSelfImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    if (mVideoViewAnchor != null) {
//                        mVideoViewAnchor.removeView(mSelfImageView);
//                        mVideoViewAnchor.addView(mSelfImageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                    }
//                }
//            }
//        });
    }

    void hideCover() {
        if (mVideoViewAnchor != null && mSelfImageView != null) {
            mVideoViewAnchor.removeView(mSelfImageView);
        }
    }

    void muteLocalVideo() {

        TRTCCloud.sharedInstance(getActivity()).muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG, true);
    }

    void initVideoMirror() {
        if (mFrontCamera) {
            mLiveRoom.setMirror(true); //前置改为镜像
        } else {
            mLiveRoom.setMirror(false); //后置改为非镜像
        }
    }

    public void changeCoverImg(String imgUrl) {
        livePoster = imgUrl;
        if (isVideoMute) {
            if (mIsPkStatus) {
                suitCover2(livePoster);
            } else {
                suitCover(livePoster);
            }
        }
    }

    public void refreshLiveInfo(String title, String imgUrl) {
        liveTitle = title;
        livePoster = imgUrl;
        changeCoverImg(livePoster);
    }

    public void showPkInvitedView(String uid) {
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_PK_INVITED_POP, "", uid));
        mSelectMemberView.dismiss();
    }

    public void doPkRefuse() {
        mLiveRoom.responseRoomPK(pkInviteId, false, TUIKitLive.getLoginUserInfo().getNickName() + "拒绝了你的pk邀请");
        //mGroupPkView.setVisibility(View.GONE);
    }

    public void doPkAccept(String uid) {
        if (getContext() == null) {
            TUILiveLog.d(TAG, "getContext is null!");
            return;
        }
        if (mIsLinkMicStatus) {
            Toast.makeText(getContext(), R.string.live_link_mic_status, Toast.LENGTH_SHORT).show();
            return;
        }
        mLiveRoom.responseRoomPK(pkInviteId, true, "");
        reportStartPk(pkInviteId);
        mPkAnchorId = uid;
        //mGroupPkView.setVisibility(View.VISIBLE);
        // mPkViewLayout.showPkView();
        //mBottomStopPkBtn.setVisibility(View.VISIBLE);
        mIsPkStatus = true;
    }

    boolean isAutoRefusePk = false;

    public void doPkAutoRefuse(boolean isAutoRefuse) {
        this.isAutoRefusePk = isAutoRefuse;
    }

    //开始pk 并且上报
    void reportStartPk(String uid) {
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.START_PK, "", uid));
    }

    //完成pk 并且上报
    void reportCompletePk(String uid) {
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.COMPLETE_PK, "", uid));
    }

    //停止pk 并且上报 开始连线
    public void reportStopPk(String uid) {
        //EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.STOP_PK, "", uid));
        //mPkViewLayout.clearPkStatus();
        mPkViewLayout.showLinkView();
    }

    //停止pk 并且上报
    public void reportStopPkByQuit(String uid) {
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.STOP_PK, "", uid));
        mLiveRoom.quitRoomPK(null);
    }

    void showPkTopAudienceView(String uid, String otherId, boolean isUs) {
        PkTopAudienceMessageBean pkBean = new PkTopAudienceMessageBean(uid, otherId, isUs);
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_PK_TOP_AUDIENCE, "", GsonUtil.getInstance().toJson(pkBean)));
    }

    void suitCover(String url) {
        final View coverView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_live_cover, null, false);
        final ImageView ivCover = coverView.findViewById(R.id.iv_live_cover);
        Glide.with(getActivity())
                .load(url)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        ivCover.setImageDrawable(resource);
                        Bitmap coverBitmap = createBitmap3(coverView,
                                getActivity().getWindowManager().getDefaultDisplay().getWidth(),
                                getActivity().getWindowManager().getDefaultDisplay().getHeight());
                        TRTCCloud.sharedInstance(getActivity()).muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG, false);
                        TRTCCloud.sharedInstance(getActivity()).setVideoMuteImage(coverBitmap, 5);
                        TRTCCloud.sharedInstance(getActivity()).muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG, true);
                        if (mSelfImageView != null) {
                            mSelfImageView.setImageBitmap(coverBitmap);
                            mSelfImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                    }
                });
    }


    void suitCover2(String url) {
        Glide.with(getActivity())
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        TRTCCloud.sharedInstance(getActivity()).muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG, false);
                        TRTCCloud.sharedInstance(getActivity()).setVideoMuteImage(resource, 5);
                        TRTCCloud.sharedInstance(getActivity()).muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG, true);
                        if (mSelfImageView != null) {
                            mSelfImageView.setImageBitmap(resource);
                            mSelfImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
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

    void suitCoverByView(String livePoster) {
        final View coverView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_live_cover, null, false);
        final ImageView ivCover = coverView.findViewById(R.id.iv_live_cover);
        Glide.with(getActivity())
                .load(livePoster)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        ivCover.setImageDrawable(resource);
                        Bitmap coverBitmap = createBitmap3(coverView,
                                getActivity().getWindowManager().getDefaultDisplay().getWidth(),
                                getActivity().getWindowManager().getDefaultDisplay().getHeight());
                        TRTCCloud.sharedInstance(getActivity()).setVideoMuteImage(coverBitmap, 5);
                        if (mSelfImageView != null) {
                            mSelfImageView.setImageBitmap(coverBitmap);
                            mSelfImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            if (mVideoViewAnchor != null) {
                                mVideoViewAnchor.removeView(mSelfImageView);
                                mVideoViewAnchor.addView(mSelfImageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            }
                        }
                    }
                });
    }

    void suitCoverByView2(String livePoster) {
        Glide.with(getActivity())
                .asBitmap()
                .load(livePoster)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        TRTCCloud.sharedInstance(getActivity()).setVideoMuteImage(resource, 5);
                        if (mSelfImageView != null) {
                            mSelfImageView.setImageBitmap(resource);
                            mSelfImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            if (mVideoViewAnchor != null) {
                                mVideoViewAnchor.removeView(mSelfImageView);
                                mVideoViewAnchor.addView(mSelfImageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            }
                        }
                    }
                });

    }

    void showPkMutePop(final String uid, final String name) {
        PkMuteOtherPanel pkMuteOtherPanel = new PkMuteOtherPanel(getActivity(), name);
        pkMuteOtherPanel.setPkMuteListener(new PkMuteOtherPanel.OnPkMuteListener() {
            @Override
            public void onPkMute() {
                TRTCCloud.sharedInstance(getActivity()).muteRemoteAudio(uid, true);
                TXTRTCLiveRoom.getInstance().doMuteRemoteAnchor(uid, true);
                mPkViewLayout.refreshMuteStatus(true);
            }
        });
        pkMuteOtherPanel.show();
    }

    //获取当前房间最大连麦数
    int getMaxLinkNum() {
        return MAX_LINK_MIC_SIZE + TXTRTCLiveRoom.getInstance().getLiveManagerUserList().size();
    }

    public void doLiveManagerSpeak() {
        final LiveManagerSpeakListPanel liveManagerSpeakListPanel = new LiveManagerSpeakListPanel(getActivity(), TXTRTCLiveRoom.getInstance().getLiveManagerUserList());
        liveManagerSpeakListPanel.setOnItemSpeakManagerListener(new SpeakManagerAdapter.OnItemSpeakManagerListener() {
            @Override
            public void doItemClick(String uid) {
                liveManagerSpeakListPanel.dismiss();
                showAudienceCard(uid);
            }

            @Override
            public void doItemClose(String uid) {
                liveManagerSpeakListPanel.dismiss();
                mLiveRoom.kickoutJoinAnchor(uid, new TRTCLiveRoomCallback.ActionCallback() {
                    @Override
                    public void onCallback(int code, String msg) {

                    }
                });
            }
        });
        liveManagerSpeakListPanel.show();
    }

    public void showStopPkTips() {
        PkStopTipPanel pkStopTipPanel = new PkStopTipPanel(getActivity(), "");
        pkStopTipPanel.setCanceledOnTouchOutside(true);
        pkStopTipPanel.setPkStopListener(new PkStopTipPanel.OnPkStopListener() {
            @Override
            public void onPkQuit() {
                reportStopPkByQuit("");
            }

            @Override
            public void onPkAgain() {
                if (mPkViewLayout.isLinkStatus()) { //连线状态
                    retryPk();
                } else {
                    ToastUtil.toastShortMessage("pk完成之后，才能再次发起Pk");
                }
            }

        });
        pkStopTipPanel.show();
    }

    private boolean isRecord = false;

    public void saveRecordState(boolean isRecord) {
        this.isRecord = isRecord;
    }

    String mPkAnchorId;

    public void retryPk() {
        String url = "/Api/Pk/invitePk";
        Map<String, String> map = new HashMap<>();
        map.put("invite_anchor_uid", mPkAnchorId);
        HttpPostRequest.getInstance().post(url, map, new TUILiveRequestCallback() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.toastShortMessage(desc);
            }

            @Override
            public void onSuccess(Object o) {
                ToastUtil.toastShortMessage("邀请成功");
            }
        });
    }

    public void showPkAgainTip() {
        new AlertDialog.Builder(getActivity())
                .setTitle("主播邀请你再次pk")
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ToastUtil.toastShortMessage("已同意");
                        responseAnchorPk(1);
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.toastShortMessage("已拒绝");
                        responseAnchorPk(2);
                    }
                })
                .show();
    }

    void responseAnchorPk(int type) {
        String url = "/Api/Pk/pkAgreeOrRefuse";
        Map<String, String> map = new HashMap<>();
        map.put("type", String.valueOf(type));
        map.put("invite_anchor_uid", mPkAnchorId);
        HttpPostRequest.getInstance().post(url, map, new TUILiveRequestCallback() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(Object o) {

            }
        });
    }

    //上报场控发言
    void reportManagerSpeak() {
        List<String> liveManagerUserList = TXTRTCLiveRoom.getInstance().getLiveManagerUserList();
        StringBuilder linkMicManagerStr = new StringBuilder("");
        for (String uid : liveManagerUserList) {
            linkMicManagerStr.append(uid);
            linkMicManagerStr.append(",");
        }
        String managerMicStr = linkMicManagerStr.toString().endsWith(",") ? linkMicManagerStr.substring(0, linkMicManagerStr.length() - 1) : linkMicManagerStr.toString();
        String url = "/Api/Live/saveAdminLinkMicList";
        Map<String, String> map = new HashMap<>();
        map.put("uids", managerMicStr);
        HttpPostRequest.getInstance().post(url, map, new TUILiveRequestCallback() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(Object o) {

            }
        });
    }

    void showCloseLiveNoticePop() {
        String title = "是否隐藏直播间公告?";
        LiveNormalTipPanel liveNormalTipPanel = new LiveNormalTipPanel(getActivity(), title);
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

    void adminCloseBeauty() {
        TiSDKManager.getInstance().setBeautyEnable(false);
        mLayoutTopToolBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                TiSDKManager.getInstance().setBeautyEnable(true);
            }
        }, 5000);
    }

    void showRedEnvelopes(int index) {
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_RED_ENVELOPES, GsonUtil.GsonString(mLayoutTopToolBar.getRedList().get(index)), String.valueOf(mRoomId)));
        //EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_RED_ENVELOPES, String.valueOf(index), String.valueOf(mRoomId)));
    }

    void showRedEnvelopesResult(int index) {
        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_RED_ENVELOPES_2, GsonUtil.GsonString(mLayoutTopToolBar.getRedList().get(index)), String.valueOf(mRoomId)));
        //EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_LIVE_RED_ENVELOPES_2, String.valueOf(index), String.valueOf(mRoomId)));
    }

    public void showLiveRedEnvelopesSign(String url, int num) {
        mLayoutTopToolBar.showLiveRedEnvelopesView(url, num);
    }

    public void hideLiveRedEnvelopesSign() {
        mLayoutTopToolBar.hideLiveRedEnvelopesView();
    }

    void getLiveRedEnvelopes() {
        // EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.GET_LIVE_RED_ENVELOPES_LIST, "", String.valueOf(mRoomId)));
        getRedBagList();
    }

    Bitmap leaveBitmap;

    void initLeaveBitmap() {
        Glide.with(getActivity())
                .asBitmap()
                .load("http://image.aiwujie.com.cn/live_bg.png")
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        leaveBitmap = resource;
                    }
                });
    }

    public void doAnchorLeave() {
        Glide.with(getActivity().getApplicationContext())
                .asBitmap()
                .load("http://image.aiwujie.com.cn/live_bg.png")
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        TRTCCloud.sharedInstance(getActivity()).muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG, false);
                        TRTCCloud.sharedInstance(getActivity()).setVideoMuteImage(leaveBitmap, 5);
                        TRTCCloud.sharedInstance(getActivity()).muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG, true);
                    }
                });
    }

    public void doAnchorComeBack() {
        if (isVideoMute) {
            changeCoverImg(livePoster);
        } else {
            openLocalView();
        }
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


    public void showLotteryDrawLayout() {
        sendLiveEvent(LiveEventConstant.SHOW_LIVE_LOTTERY_DRAW, "");
        //changeOrientation();
    }

    public void changeOrientation() {
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }


    TXCallback mCaptureCallBack;
    boolean mtScreenCaptureMode = false;

    public void tryToScreenCapture() {
        if (mCaptureCallBack == null) {
            mCaptureCallBack = new TXCallback() {
                @Override
                public void onCallback(int code, String msg) {
                    Log.d("logutil",code + " " + msg);
                    if (code == 1000) {
                        showCaptureView();
                        //mtScreenCaptureMode = true;
                    } else if (code < 0) {
                        mtScreenCaptureMode = false;
                        ToastUtil.toastShortMessage("开启录屏直播失败，用户未授权");
                        TRTCCloud.sharedInstance(getActivity()).stopScreenCapture();
                        startPreview();
                    }
                }
            };
            TXTRTCLiveRoom.getInstance().setScreenCaptureCallback(mCaptureCallBack);
        }
        //关闭美颜
        //TiSDKManager.getInstance().setBeautyEnable(false);
        closeBeauty();
        doCameraFront();
        mLayoutPreview.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLiveRoom.stopCameraPreview();
            }
        },1000);
        mLayoutPreview.postDelayed(new Runnable() {
            @Override
            public void run() {
                TRTCCloudDef.TRTCVideoEncParam encParams = new TRTCCloudDef.TRTCVideoEncParam();
                encParams.videoResolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_1280_720;
                encParams.videoFps = 20;
                encParams.videoBitrate = 2000;
                encParams.videoResolutionMode = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_MODE_PORTRAIT;
                TRTCCloud.sharedInstance(getActivity()).startScreenCapture(TRTC_VIDEO_STREAM_TYPE_BIG, encParams, null);
                //TRTCCloud.sharedInstance(getActivity()).startScreenCapture(TRTC_VIDEO_STREAM_TYPE_SMALL, encParams, null);
            }
        }, 1000);
    }

    public void showCaptureView() {
        mtScreenCaptureMode = true;
        FloatWindowLayout2.getInstance().showFloatWindow(null, null);
        mLayoutPreview.postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().moveTaskToBack(true);
            }
        }, 1000);
        sendLiveEvent(LiveEventConstant.START_SCREEN_CAPTURE, "");
        initBottomToolBar();

    }

    public void stopScreenCapture() {
        mtScreenCaptureMode = false;
        FloatWindowLayout2.getInstance().closeFloatWindow();
        TRTCCloud.sharedInstance(getActivity()).stopScreenCapture();

        sendLiveEvent(LiveEventConstant.STOP_SCREEN_CAPTURE, "");
        initBottomToolBar();

       // initTiPanel();
        //开启美颜
        openBeauty();
        startPreview();

    }

    void closeBeauty() {
        //TiSDKManager.getInstance().setBeautyEnable(false);
        //TiSDKManager.getInstance().setFaceTrimEnable(false);
        TiSDKManager.getInstance().renderEnable(false);
    }

    void openBeauty() {
       // TiSDKManager.getInstance().setBeautyEnable(true);
       // TiSDKManager.getInstance().setFaceTrimEnable(true);
        TiSDKManager.getInstance().renderEnable(true);
    }

}
