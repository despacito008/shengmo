package com.tencent.liteav.ui;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.tencent.liteav.custom.Constents;
import com.tencent.liteav.custom.FloatVideoWindowService;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.model.ITRTCAVCall;
import com.tencent.liteav.model.IntentParams;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.liteav.model.TRTCAVCallListener;
import com.tencent.liteav.renderer.TXCGLSurfaceView;
import com.tencent.liteav.ui.videolayout.TRTCVideoLayout;
import com.tencent.liteav.ui.videolayout.TRTCVideoLayoutManager;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.base.HttpPostRequest;
import com.tencent.qcloud.tim.tuikit.live.base.TUILiveRequestCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.FreeData;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;
import com.tencent.qcloud.tim.tuikit.live.utils.PermissionUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.TUILiveLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ???????????????????????????????????????????????????????????????????????????????????????????????????
 *
 * @author guanyifeng
 */
public class TRTCVideoCallActivity extends AppCompatActivity {

    private static final String TAG = TRTCVideoCallActivity.class.getSimpleName();

    public static final int TYPE_BEING_CALLED = 1;
    public static final int TYPE_CALL         = 2;

    public static final String PARAM_GROUP_ID = "group_id";
    public static final  String PARAM_TYPE                = "type";
    public static final  String PARAM_USER                = "user_model";
    public static final  String PARAM_BEINGCALL_USER      = "beingcall_user_model";
    public static final  String PARAM_OTHER_INVITING_USER = "other_inviting_user_model";

    public static final String PARAM_LINK_TYPE = "link_type"; //???????????????????????????

    private static final int    MAX_SHOW_INVITING_USER    = 4;

    private static final int RADIUS = 3;
    private static final int REQUEST_PERMISSION_CODE = 99;
    /**
     * ??????????????????
     */
    private ImageView              mMuteImg;
    private LinearLayout           mMuteLl;
    private ImageView              mHangupImg;
    private LinearLayout           mHangupLl;
    private ImageView              mHandsfreeImg;
    private LinearLayout           mHandsfreeLl;
    private ImageView              mDialingImg;
    private LinearLayout           mDialingLl;
    private TRTCVideoLayoutManager mLayoutManagerTrtc;
    private Group mInvitingGroup;
    private LinearLayout           mImgContainerLl;
    private TextView               mTimeTv;
    private Runnable               mTimeRunnable;
    private int                    mTimeCount;
    private Handler                mTimeHandler;
    private HandlerThread          mTimeHandlerThread;
    //private TRTCv

    /**
     * ????????????????????????
     */
    private UserModel              mSelfModel;
    private List<UserModel>        mCallUserModelList = new ArrayList<>(); // ?????????
    private Map<String, UserModel> mCallUserModelMap  = new HashMap<>();
    private UserModel              mSponsorUserModel; // ?????????
    private List<UserModel>        mOtherInvitingUserModelList;
    private int                    mCallType;
    private ITRTCAVCall            mITRTCAVCall;
    private boolean                isHandsFree        = true;
    private boolean                isMuteMic          = false;
    private String mGroupId;

    private Vibrator mVibrator;
    private Ringtone mRingtone;

    private ImageView ivLinkSign;

    /**
     * ???????????????
     */
    private TRTCAVCallListener mTRTCAVCallListener = new TRTCAVCallListener() {
        @Override
        public void onError(int code, String msg) {
            //??????????????????????????????????????????
            ToastUtil.toastLongMessage(getString(R.string.send_error) + "[" + code + "]:" + msg);
            finishActivity();
        }

        @Override
        public void onInvited(String sponsor, List<String> userIdList, boolean isFromGroup, int callType) {
        }

        @Override
        public void onGroupCallInviteeListUpdate(List<String> userIdList) {
        }

        @Override
        public void onUserEnter(final String userId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showCallingView();
                    //1.?????????????????????????????????????????????
                    UserModel model = new UserModel();
                    model.userId = userId;
                    model.phone = "";
                    model.userName = userId;
                    model.userAvatar = "";
                    mCallUserModelList.add(model);
                    mCallUserModelMap.put(model.userId, model);
                    TRTCVideoLayout videoLayout = addUserToManager(model);
                    if (videoLayout == null) {
                        return;
                    }
                    videoLayout.setVideoAvailable(false);
                    //2. ???????????????????????????????????????????????????????????????????????????
                    ProfileManager.getInstance().getUserInfoByUserId(userId, new ProfileManager.GetUserInfoCallback() {
                        @Override
                        public void onSuccess(UserModel model) {
                            UserModel oldModel = mCallUserModelMap.get(model.userId);
                            if (oldModel != null) {
                                oldModel.userName = model.userName;
                                oldModel.userAvatar = model.userAvatar;
                                oldModel.phone = model.phone;
                                TRTCVideoLayout videoLayout = mLayoutManagerTrtc.findCloudViewView(model.userId);
                                if (videoLayout != null) {
                                    GlideEngine.loadImage(videoLayout.getHeadImg(), oldModel.userAvatar, R.drawable.live_default_head_img, RADIUS);
                                    videoLayout.getUserNameTv().setText(oldModel.userName);
                                }
                            }
                        }

                        @Override
                        public void onFailed(int code, String msg) {
                            ToastUtil.toastLongMessage(getString(R.string.get_user_info_tips_before) + userId + getString(R.string.get_user_info_tips_after));
                        }
                    });
                }
            });
        }

        @Override
        public void onUserLeave(final String userId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //1. ??????????????????
                    mLayoutManagerTrtc.recyclerCloudViewView(userId);
                    //2. ????????????model
                    UserModel userModel = mCallUserModelMap.remove(userId);
                    if (userModel != null) {
                        mCallUserModelList.remove(userModel);
                    }
                }
            });
        }

        @Override
        public void onReject(final String userId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mCallUserModelMap.containsKey(userId)) {
                        // ??????????????????
                        //1. ??????????????????
                        mLayoutManagerTrtc.recyclerCloudViewView(userId);
                        //2. ????????????model
                        UserModel userModel = mCallUserModelMap.remove(userId);
                        if (userModel != null) {
                            mCallUserModelList.remove(userModel);
                            ToastUtil.toastLongMessage(userModel.userName + getString(R.string.reject_calls));
                        }
                    }
                }
            });
        }

        @Override
        public void onNoResp(final String userId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mCallUserModelMap.containsKey(userId)) {
                        // ?????????????????????
                        //1. ??????????????????
                        mLayoutManagerTrtc.recyclerCloudViewView(userId);
                        //2. ????????????model
                        UserModel userModel = mCallUserModelMap.remove(userId);
                        if (userModel != null) {
                            mCallUserModelList.remove(userModel);
                            ToastUtil.toastLongMessage(userModel.userName + getString(R.string.no_response));
                        }
                    }
                }
            });
        }

        @Override
        public void onLineBusy(String userId) {
            if (mCallUserModelMap.containsKey(userId)) {
                // ?????????????????????
                //1. ??????????????????
                mLayoutManagerTrtc.recyclerCloudViewView(userId);
                //2. ????????????model
                UserModel userModel = mCallUserModelMap.remove(userId);
                if (userModel != null) {
                    mCallUserModelList.remove(userModel);
                    ToastUtil.toastLongMessage(userModel.userName + getString(R.string.line_busy));
                }
            }
        }

        @Override
        public void onCallingCancel() {
            if (mSponsorUserModel != null) {
                ToastUtil.toastLongMessage(mSponsorUserModel.userName + getString(R.string.cancle_calling));
            }
            finishActivity();
        }

        @Override
        public void onCallingTimeout() {
            if (mSponsorUserModel != null) {
                ToastUtil.toastLongMessage(mSponsorUserModel.userName + getString(R.string.call_time_out));
            }
            finishActivity();
        }

        @Override
        public void onCallEnd() {
            finishActivity();
        }

        @Override
        public void onUserVideoAvailable(final String userId, final boolean isVideoAvailable) {
            //???????????????????????????
            TRTCVideoLayout layout = mLayoutManagerTrtc.findCloudViewView(userId);
            if (layout != null) {
                layout.setVideoAvailable(isVideoAvailable);
                if (isVideoAvailable) {
                    mITRTCAVCall.startRemoteView(userId, layout.getVideoView());
                } else {
                    mITRTCAVCall.stopRemoteView(userId);
                }
            } else {

            }
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {

        }

        @Override
        public void onUserVoiceVolume(Map<String, Integer> volumeMap) {
            for (Map.Entry<String, Integer> entry : volumeMap.entrySet()) {
                String          userId = entry.getKey();
                TRTCVideoLayout layout = mLayoutManagerTrtc.findCloudViewView(userId);
                if (layout != null) {
                    layout.setAudioVolumeProgress(entry.getValue());
                }
            }
        }
    };
    private ImageView             mSponsorAvatarImg;
    private TextView              mSponsorUserNameTv;
    private Group                 mSponsorGroup;


    /**
     * ???????????????????????????
     *
     * @param context
     * @param models
     */
    public static void startCallSomeone(Context context, List<UserModel> models) {
        TUILiveLog.i(TAG, "startCallSomeone");
        ((TRTCAVCallImpl)TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCVideoCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
        starter.putExtra(PARAM_USER, new IntentParams(models));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (((TRTCAVCallImpl)TRTCAVCallImpl.sharedInstance(context)).isLink) {
            starter.putExtra(PARAM_LINK_TYPE, "1");
        }
        context.startActivity(starter);
    }
    /**
     * ???????????????????????????
     *
     * @param context
     * @param models
     */
    public static void startCallSomePeople(Context context, List<UserModel> models, String groupId) {
        TUILiveLog.i(TAG, "startCallSomePeople");
        ((TRTCAVCallImpl)TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCVideoCallActivity.class);
        starter.putExtra(PARAM_GROUP_ID, groupId);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
        starter.putExtra(PARAM_USER, new IntentParams(models));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    /**
     * ??????????????????
     *
     * @param context
     * @param beingCallUserModel
     */
    public static void startBeingCall(Context context, UserModel beingCallUserModel, List<UserModel> otherInvitingUserModel) {
        TUILiveLog.i(TAG, "startBeingCall");
        ((TRTCAVCallImpl)TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCVideoCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        starter.putExtra(PARAM_BEINGCALL_USER, beingCallUserModel);
        starter.putExtra(PARAM_OTHER_INVITING_USER, new IntentParams(otherInvitingUserModel));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    /**
     * ?????????????????? - ????????????
     *
     * @param context
     * @param beingCallUserModel
     */
    public static void startBeingCall2(Context context, UserModel beingCallUserModel, List<UserModel> otherInvitingUserModel) {
        TUILiveLog.i(TAG, "startBeingCall");
        ((TRTCAVCallImpl)TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCVideoCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        starter.putExtra(PARAM_BEINGCALL_USER, beingCallUserModel);
        starter.putExtra(PARAM_OTHER_INVITING_USER, new IntentParams(otherInvitingUserModel));
        starter.putExtra(PARAM_LINK_TYPE, "1");
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TUILiveLog.i(TAG, "onCreate");
        if (!(PermissionUtils.checkPermission(this, Manifest.permission.CAMERA)
                && PermissionUtils.checkPermission(this, Manifest.permission.RECORD_AUDIO))) {
            PermissionUtils.checkAndRequestMorePermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
            ToastUtil.toastLongMessage(this.getString(R.string.live_permission_denied));
            finish();
            return;
        }

        mCallType = getIntent().getIntExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        TUILiveLog.i(TAG, "mCallType: " + mCallType);
        if (mCallType == TYPE_BEING_CALLED && ((TRTCAVCallImpl)TRTCAVCallImpl.sharedInstance(this)).isWaitingLastActivityFinished()) {
            // ??????????????????????????????Activity????????????bug???????????????????????????????????????????????????????????????????????????????????????????????????????????????
            // ????????????????????????????????????Activity??????finish?????????????????????????????????Activity???????????????????????????Activity?????????????????????
            TUILiveLog.w(TAG, "ignore activity launch");
            finishActivity();
            return;
        }

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancelAll();
        }

        // ?????????????????????????????????????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.videocall_activity_online_call);

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mRingtone = RingtoneManager.getRingtone(this,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        initView();
        initData();
        initListener();
        registerReceiver(mHomeKeyEventReceiver,new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    private void finishActivity() {
        ((TRTCAVCallImpl)TRTCAVCallImpl.sharedInstance(this)).setWaitingLastActivityFinished(true);
        finish();
    }

    @Override
    public void onBackPressed() {
        startFloat();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVibrator != null) {
            mVibrator.cancel();
        }
        if (mRingtone != null) {
            mRingtone.stop();
        }
        if (mITRTCAVCall != null) {
            mITRTCAVCall.closeCamera();
            mITRTCAVCall.removeListener(mTRTCAVCallListener);
        }
        stopTimeCount();
        if (mTimeHandlerThread != null) {
            mTimeHandlerThread.quit();
        }
        unregisterReceiver(mHomeKeyEventReceiver);
    }

    private void initListener() {
        mMuteLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMuteMic = !isMuteMic;
                mITRTCAVCall.setMicMute(isMuteMic);
                mMuteImg.setActivated(isMuteMic);
                ToastUtil.toastLongMessage(isMuteMic ? getString(R.string.open_silent) : getString(R.string.close_silent));
            }
        });
        mHandsfreeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHandsFree = !isHandsFree;
                mITRTCAVCall.setHandsFree(isHandsFree);
                mHandsfreeImg.setActivated(isHandsFree);
                ToastUtil.toastLongMessage(isHandsFree ? getString(R.string.use_speakers) : getString(R.string.use_handset));
            }
        });
        mMuteImg.setActivated(isMuteMic);
        mHandsfreeImg.setActivated(isHandsFree);
        findViewById(R.id.iv_call_small).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFloat();
            }
        });
    }

    private void initData() {
        // ?????????????????????
        mITRTCAVCall = TRTCAVCallImpl.sharedInstance(this);
        mITRTCAVCall.addListener(mTRTCAVCallListener);
        mTimeHandlerThread = new HandlerThread("time-count-thread");
        mTimeHandlerThread.start();
        mTimeHandler = new Handler(mTimeHandlerThread.getLooper());
        // ?????????????????????????????????
        Intent intent = getIntent();
        //???????????????
        mSelfModel = ProfileManager.getInstance().getUserModel();
        mCallType = intent.getIntExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        mGroupId = intent.getStringExtra(PARAM_GROUP_ID);
        if (mCallType == TYPE_BEING_CALLED) {
            // ????????????
            mSponsorUserModel = (UserModel) intent.getSerializableExtra(PARAM_BEINGCALL_USER);
            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_OTHER_INVITING_USER);
            if (params != null) {
                mOtherInvitingUserModelList = params.mUserModels;
            }
            showWaitingResponseView();
            mVibrator.vibrate(new long[] { 0, 1000, 1000 }, 0);
            mRingtone.play();
        } else {
            // ?????????
            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_USER);
            if (params != null) {
                mCallUserModelList = params.mUserModels;
                for (UserModel userModel : mCallUserModelList) {
                    mCallUserModelMap.put(userModel.userId, userModel);
                }
                startInviting();
                showInvitingView();
                if (isFeeLink()) {
                    ivLinkSign.setVisibility(View.VISIBLE);
                }
                if (mCallUserModelList.size() > 0) {
                    mLinkUserId = mCallUserModelList.get(0).userId;
                }
            }
        }

    }

    private String mLinkUserId = "";
    private void startInviting() {
        List<String> list = new ArrayList<>();
        for (UserModel userModel : mCallUserModelList) {
            list.add(userModel.userId);
        }
        mITRTCAVCall.groupCall(list, ITRTCAVCall.TYPE_VIDEO_CALL, mGroupId);
    }

    private void initView() {
        mMuteImg = (ImageView) findViewById(R.id.img_mute);
        mMuteLl = (LinearLayout) findViewById(R.id.ll_mute);
        mHangupImg = (ImageView) findViewById(R.id.img_hangup);
        mHangupLl = (LinearLayout) findViewById(R.id.ll_hangup);
        mHandsfreeImg = (ImageView) findViewById(R.id.img_handsfree);
        mHandsfreeLl = (LinearLayout) findViewById(R.id.ll_handsfree);
        mDialingImg = (ImageView) findViewById(R.id.img_dialing);
        mDialingLl = (LinearLayout) findViewById(R.id.ll_dialing);
        mLayoutManagerTrtc = (TRTCVideoLayoutManager) findViewById(R.id.trtc_layout_manager);
        mInvitingGroup = (Group) findViewById(R.id.group_inviting);
        mImgContainerLl = (LinearLayout) findViewById(R.id.ll_img_container);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
        mSponsorAvatarImg = (ImageView) findViewById(R.id.img_sponsor_avatar);
        mSponsorUserNameTv = (TextView) findViewById(R.id.tv_sponsor_user_name);
        mSponsorGroup = (Group) findViewById(R.id.group_sponsor);
        ivLinkSign = findViewById(R.id.iv_link_sign);
        if (isFeeLink()) {
            ivLinkSign.setVisibility(View.VISIBLE);
        } else {
            ivLinkSign.setVisibility(View.GONE);
        }
    }

    //?????????????????????
    boolean isFeeLink() {
        return "1".equals(getIntent().getStringExtra(PARAM_LINK_TYPE));
    }

    //????????????????????????
    public void uploadFeeLinkHeartBeat() {
        String userId = mSelfModel.userId;
        String url = "/api/live/reportAnchorChat";
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid",mLinkUserId);
        map.put("type","2");
        HttpPostRequest.getInstance().post(url, map, new TUILiveRequestCallback() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.toastLongMessage(desc );
                if (code == 4000) {
                    mITRTCAVCall.hangup();
                    finishActivity();
                }
            }

            @Override
            public void onSuccess(Object o) {

            }
        });
    }

    //????????????????????????
    public void uploadLinkHeartBeat() {
        String url = "/api/Power/updateChatDuration";
        Map<String, String> map = new HashMap<>();
        map.put("type","2");
        map.put("touid",mLinkUserId);
        HttpPostRequest.getInstance().post(url, map, new TUILiveRequestCallback() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.toastLongMessage(desc);
                if (code == 4000) {
                    mITRTCAVCall.hangup();
                    finishActivity();
                }
            }

            @Override
            public void onSuccess(Object o) {
                FreeData data = new Gson().fromJson((String) o,FreeData.class);
                if (data .getRetcode() == 2000){
                    Log.v("????????????????????????",data.getData().getMin()+"");
                }

            }
        });
    }

    //????????????????????????
    private void uploadFeeLinkComplete() {

    }


    /**
     * ??????????????????
     */
    public void showWaitingResponseView() {
        //1. ?????????????????????
        mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
        TRTCVideoLayout videoLayout = addUserToManager(mSelfModel);
        if (videoLayout == null) {
            return;
        }
        videoLayout.setVideoAvailable(true);
        mITRTCAVCall.openCamera(true, videoLayout.getVideoView());

        //2. ??????????????????????????????
        mSponsorGroup.setVisibility(View.VISIBLE);
        GlideEngine.loadImage(mSponsorAvatarImg, mSponsorUserModel.userAvatar, R.drawable.live_default_head_img, RADIUS);
        mSponsorUserNameTv.setText(mSponsorUserModel.userName);

        //3. ????????????????????????
        mHangupLl.setVisibility(View.VISIBLE);
        mDialingLl.setVisibility(View.VISIBLE);
        mHandsfreeLl.setVisibility(View.GONE);
        mMuteLl.setVisibility(View.GONE);
        //3. ???????????????listener
        mHangupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVibrator.cancel();
                mRingtone.stop();
                mITRTCAVCall.reject();
                finishActivity();
            }
        });
        mDialingLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVibrator.cancel();
                mRingtone.stop();
                mITRTCAVCall.accept();
                showCallingView();
            }
        });
        //4. ????????????????????????
        showOtherInvitingUserView();
    }

    /**
     * ??????????????????
     */
    public void showInvitingView() {
        //1. ?????????????????????
        mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
        TRTCVideoLayout videoLayout = addUserToManager(mSelfModel);
        if (videoLayout == null) {
            return;
        }
        videoLayout.setVideoAvailable(true);
        mITRTCAVCall.openCamera(true, videoLayout.getVideoView());
        //        for (UserModel userModel : mCallUserModelList) {
        //            TRTCVideoLayout layout = addUserToManager(userModel);
        //            layout.getShadeImg().setVisibility(View.VISIBLE);
        //        }
        //2. ???????????????
        mHangupLl.setVisibility(View.VISIBLE);
        mHangupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITRTCAVCall.hangup();
                finishActivity();
            }
        });
        mDialingLl.setVisibility(View.GONE);
        mHandsfreeLl.setVisibility(View.GONE);
        mMuteLl.setVisibility(View.GONE);
        //3. ??????????????????????????????
        hideOtherInvitingUserView();
        //4. sponsor???????????????
        mSponsorGroup.setVisibility(View.GONE);
    }

    /**
     * ????????????????????????
     */
    public void showCallingView() {
        //1. ????????????
        mSponsorGroup.setVisibility(View.GONE);
        //2. ???????????????
        mHangupLl.setVisibility(View.VISIBLE);
        mDialingLl.setVisibility(View.GONE);
        mHandsfreeLl.setVisibility(View.VISIBLE);
        mMuteLl.setVisibility(View.VISIBLE);

        mHangupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITRTCAVCall.hangup();
                finishActivity();
            }
        });
        showTimeCount();
        hideOtherInvitingUserView();
    }

    private void showTimeCount() {
        if (mTimeRunnable != null) {
            return;
        }
        mTimeCount = 0;
        mTimeTv.setText(getShowTime(mTimeCount));
        if (mTimeRunnable == null) {
            mTimeRunnable = new Runnable() {
                @Override
                public void run() {
                    mTimeCount++;
                    if (mTimeTv != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTimeTv.setText(getShowTime(mTimeCount));
                            }
                        });
                        if (isFeeLink()){
                            if (mTimeCount%60 == 5 ) {
                                if (mCallType != TYPE_BEING_CALLED) {
                                    if (!TextUtils.isEmpty(mLinkUserId)) {
                                        uploadFeeLinkHeartBeat();
                                    }
                                }
                            }
                        }else  {
                            if (mTimeCount%60 == 5 ) {
                                if (mCallType != TYPE_BEING_CALLED) {
                                    if (!TextUtils.isEmpty(mLinkUserId)) {
                                        uploadLinkHeartBeat();
                                    }
                                }
                            }
                        }

                    }
                    mTimeHandler.postDelayed(mTimeRunnable, 1000);
                }
            };
        }
        mTimeHandler.postDelayed(mTimeRunnable, 1000);
    }

    private void stopTimeCount() {
        if (mTimeHandler != null) {
            mTimeHandler.removeCallbacks(mTimeRunnable);
        }
        mTimeRunnable = null;
    }

    private String getShowTime(int count) {
        return String.format("%02d:%02d", count / 60, count % 60);
    }

    private void showOtherInvitingUserView() {
        if (mOtherInvitingUserModelList == null || mOtherInvitingUserModelList.size() == 0) {
            return;
        }
        mInvitingGroup.setVisibility(View.VISIBLE);
        int squareWidth = getResources().getDimensionPixelOffset(R.dimen.small_image_size);
        int leftMargin  = getResources().getDimensionPixelOffset(R.dimen.small_image_left_margin);
        for (int index = 0; index < mOtherInvitingUserModelList.size() && index < MAX_SHOW_INVITING_USER; index++) {
            UserModel                 userModel    = mOtherInvitingUserModelList.get(index);
            ImageView                 imageView    = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(squareWidth, squareWidth);
            if (index != 0) {
                layoutParams.leftMargin = leftMargin;
            }
            imageView.setLayoutParams(layoutParams);
            GlideEngine.loadImage(imageView, userModel.userAvatar, R.drawable.live_default_head_img, RADIUS);

            mImgContainerLl.addView(imageView);
        }
    }

    private void hideOtherInvitingUserView() {
        mInvitingGroup.setVisibility(View.GONE);
    }

    private TRTCVideoLayout addUserToManager(UserModel userModel) {
        TRTCVideoLayout layout = mLayoutManagerTrtc.allocCloudVideoView(userModel.userId);
        if (layout == null) {
            return null;
        }
        layout.getUserNameTv().setText(userModel.userName);
        if (!TextUtils.isEmpty(userModel.userAvatar)) {
            GlideEngine.loadImage(layout.getHeadImg(), userModel.userAvatar, R.drawable.live_default_head_img, RADIUS);
        }
        return layout;
    }

    private boolean isStartService = false;
    private ServiceConnection mVideoServiceConnection  =  new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // ???????????????????????????
            FloatVideoWindowService.MyBinder binder = (FloatVideoWindowService.MyBinder) service;
            binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    public void startFloat() {
        if (!PermissionUtils.requestFloatPermission(TRTCVideoCallActivity.this,24)) {
            ToastUtil.toastShortMessage("????????????????????????????????????");
            return;
        }
        String lastBigUserId = mLayoutManagerTrtc.getLastTRTCLayoutEntity();
        isStartService = true;
        Constents.mVideoViewLayout = mLayoutManagerTrtc;
        Intent intent = new Intent(this, FloatVideoWindowService.class);//???????????????????????????
        intent.putExtra("userId", lastBigUserId);
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

        String userId = mLayoutManagerTrtc.getLastTRTCLayoutEntity();
        TXCloudVideoView txCloudVideoView = mLayoutManagerTrtc.findCloudViewView(userId).getVideoView();
        if (txCloudVideoView == null) {
            txCloudVideoView = mLayoutManagerTrtc.allocCloudVideoView(userId).getVideoView();
        }
        if (Constents.currentUserID.equals(userId)) {
            TXCGLSurfaceView mTXCGLSurfaceView = txCloudVideoView.getGLSurfaceView();
            if (mTXCGLSurfaceView != null && mTXCGLSurfaceView.getParent() != null) {
                ((ViewGroup) mTXCGLSurfaceView.getParent()).removeView(mTXCGLSurfaceView);
                txCloudVideoView.addVideoView(mTXCGLSurfaceView);
            }
        } else {
            TextureView mTextureView = txCloudVideoView.getVideoView();
            if (mTextureView != null && mTextureView.getParent() != null) {
                ((ViewGroup) mTextureView.getParent()).removeView(mTextureView);
                txCloudVideoView.addVideoView(mTextureView);
            }
        }
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
                    if (!PermissionUtils.requestFloatPermission(TRTCVideoCallActivity.this,24)) {
                        ToastUtil.toastShortMessage("????????????????????????????????????");
                        mITRTCAVCall.hangup();
                        finishActivity();
                    } else {
                        startFloat();
                    }
                }
            }
        }
    };


}
