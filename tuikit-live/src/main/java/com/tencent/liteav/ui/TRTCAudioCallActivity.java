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
import com.opensource.svgaplayer.utils.log.LogUtils;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.SelectContactActivity;
import com.tencent.liteav.custom.Constents;
import com.tencent.liteav.custom.FloatVideoWindowService;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.model.ITRTCAVCall;
import com.tencent.liteav.model.IntentParams;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.liteav.model.TRTCAVCallListener;
import com.tencent.liteav.renderer.TXCGLSurfaceView;
import com.tencent.liteav.ui.audiolayout.TRTCAudioLayout;
import com.tencent.liteav.ui.audiolayout.TRTCAudioLayoutManager;
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
public class TRTCAudioCallActivity extends AppCompatActivity {
    private static final String TAG = TRTCAudioCallActivity.class.getName();

    public static final int TYPE_BEING_CALLED = 1;
    public static final int TYPE_BEING_CALLED_FROM_NOTIFICATION = 3;
    public static final int TYPE_CALL = 2;

    public static final String PARAM_GROUP_ID = "group_id";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_USER = "user_model";
    public static final String PARAM_BEINGCALL_USER = "beingcall_user_model";
    public static final String PARAM_OTHER_INVITING_USER = "other_inviting_user_model";
    private static final int MAX_SHOW_INVITING_USER = 2;

    public static final String PARAM_LINK_TYPE = "link_type"; //???????????????????????????

    private static final int RADIUS = 3;
    private static final int REQUEST_PERMISSION_CODE = 99;

    /**
     * ??????????????????
     */
    private ImageView mMuteImg;
    private LinearLayout mMuteLl;
    private ImageView mHangupImg;
    private LinearLayout mHangupLl;
    private ImageView mHandsfreeImg;
    private LinearLayout mHandsfreeLl;
    private ImageView mDialingImg;
    private LinearLayout mDialingLl;
    private TRTCAudioLayoutManager mLayoutManagerTrtc;
    private Group mInvitingGroup;
    private LinearLayout mImgContainerLl;
    private TextView mTimeTv;
    private Runnable mTimeRunnable;
    private int mTimeCount;
    private Handler mTimeHandler;
    private HandlerThread mTimeHandlerThread;

    /**
     * ????????????????????????
     */
    private UserModel mSelfModel;
    private List<UserModel> mCallUserModelList = new ArrayList<>(); // ?????????
    private Map<String, UserModel> mCallUserModelMap = new HashMap<>();
    private UserModel mSponsorUserModel; // ?????????
    private List<UserModel> mOtherInvitingUserModelList;
    private int mCallType;
    private ITRTCAVCall mITRTCAVCall;
    private String mGroupId;
    private boolean isHandsFree = true;
    private boolean isMuteMic = false;
    private Vibrator mVibrator;
    private Ringtone mRingtone;

    private ImageView ivLinkSign;

    /**
     * ???????????????
     */
    private TRTCAVCallListener mTRTCAudioCallListener = new TRTCAVCallListener() {
        @Override
        public void onError(int code, String msg) {
            //??????????????????????????????????????????
            ToastUtil.toastLongMessage(getString(R.string.error) + "[" + code + "]:" + msg);
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
                    TRTCAudioLayout layout = mLayoutManagerTrtc.findAudioCallLayout(userId);
                    if (layout != null) {
                        layout.stopLoading();
                    } else {
                        // ???????????????????????????????????????, ???????????????????????????????????????
                        ProfileManager.getInstance().getUserInfoByUserId(userId, new ProfileManager.GetUserInfoCallback() {
                            @Override
                            public void onSuccess(UserModel model) {
                                mCallUserModelList.add(model);
                                mCallUserModelMap.put(model.userId, model);
                                addUserToManager(model);
                            }

                            @Override
                            public void onFailed(int code, String msg) {
                                // ????????????????????????????????????????????????
                                ToastUtil.toastLongMessage(getString(R.string.get_user_info_tips_before) + userId + getString(R.string.get_user_info_tips_after));
                                UserModel model = new UserModel();
                                model.userId = userId;
                                model.phone = "";
                                model.userName = userId;
                                model.userAvatar = "";
                                mCallUserModelList.add(model);
                                mCallUserModelMap.put(model.userId, model);
                                addUserToManager(model);
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onUserLeave(final String userId) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //1. ??????????????????
                    mLayoutManagerTrtc.recyclerAudioCallLayout(userId);
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
                        mLayoutManagerTrtc.recyclerAudioCallLayout(userId);
                        //2. ????????????model
                        UserModel userModel = mCallUserModelMap.remove(userId);
                        if (userModel != null) {
                            mCallUserModelList.remove(userModel);
                            ToastUtil.toastLongMessage(userModel.userName + getString(R.string.reject_call));
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
                        mLayoutManagerTrtc.recyclerAudioCallLayout(userId);
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
                mLayoutManagerTrtc.recyclerAudioCallLayout(userId);
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
            //mITRTCAVCall.hangup();
            finishActivity();
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {

        }

        @Override
        public void onUserVideoAvailable(String userId, boolean isVideoAvailable) {

        }

        @Override
        public void onUserVoiceVolume(Map<String, Integer> volumeMap) {
            for (Map.Entry<String, Integer> entry : volumeMap.entrySet()) {
                String userId = entry.getKey();
                TRTCAudioLayout layout = mLayoutManagerTrtc.findAudioCallLayout(userId);
                if (layout != null) {
                    layout.setAudioVolume(entry.getValue());
                }
            }
        }
    };

    /**
     * ???????????????????????????
     *
     * @param context
     * @param models
     */
    public static void startCallSomeone(Context context, List<UserModel> models) {
        TUILiveLog.i(TAG, "startCallSomeone");
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCAudioCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
        starter.putExtra(PARAM_USER, new IntentParams(models));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).isLink) {
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
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCAudioCallActivity.class);
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
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCAudioCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        starter.putExtra(PARAM_BEINGCALL_USER, beingCallUserModel);
        starter.putExtra(PARAM_OTHER_INVITING_USER, new IntentParams(otherInvitingUserModel));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    /**
     * ??????????????????
     *
     * @param context
     * @param beingCallUserModel
     */
    public static void startBeingCall2(Context context, UserModel beingCallUserModel, List<UserModel> otherInvitingUserModel) {
        TUILiveLog.i(TAG, "startBeingCall");
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCAudioCallActivity.class);
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
        if (!PermissionUtils.checkPermission(this, Manifest.permission.RECORD_AUDIO)) {
            PermissionUtils.requestPermission(this, Manifest.permission.RECORD_AUDIO, REQUEST_PERMISSION_CODE);
            ToastUtil.toastLongMessage(this.getString(R.string.audio_permission_error));
            finish();
            return;
        }

        mCallType = getIntent().getIntExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        TUILiveLog.i(TAG, "mCallType: " + mCallType);
        if (mCallType == TYPE_BEING_CALLED && ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(this)).isWaitingLastActivityFinished()) {
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
        setContentView(R.layout.audiocall_activity_call_main);

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mRingtone = RingtoneManager.getRingtone(this,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        initView();
        initData();
        initListener();
        registerReceiver(mHomeKeyEventReceiver,new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    private void finishActivity() {
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(this)).setWaitingLastActivityFinished(true);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        TUILiveLog.i(TAG, "onResume");
    }


    @Override
    public void onPause() {
        super.onPause();
        TUILiveLog.i(TAG, "onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        TUILiveLog.i(TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        TUILiveLog.i(TAG, "onStop");
    }

    @Override
    public void onBackPressed() {
       startFloat();
    }

    @Override
    protected void onDestroy() {
        TUILiveLog.i(TAG, "onDestroy");
        super.onDestroy();
        if (mVibrator != null) {
            mVibrator.cancel();
        }
        if (mRingtone != null) {
            mRingtone.stop();
        }
        if (mITRTCAVCall != null) {
            mITRTCAVCall.removeListener(mTRTCAudioCallListener);
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
        mITRTCAVCall.addListener(mTRTCAudioCallListener);
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
            mLinkUserId = mSponsorUserModel.userId;
            Log.d("callcall","link1 id = " + mLinkUserId);
            showWaitingResponseView();
            mVibrator.vibrate(new long[]{0, 1000, 1000}, 0);
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
                if (mCallUserModelList.size() > 0) {
                    mLinkUserId = mCallUserModelList.get(0).userId;
                }
            }
        }
    }

    private void startInviting() {
        List<String> list = new ArrayList<>();
        for (UserModel userModel : mCallUserModelList) {
            list.add(userModel.userId);
        }
        mITRTCAVCall.groupCall(list, ITRTCAVCall.TYPE_AUDIO_CALL, mGroupId);
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
        mLayoutManagerTrtc = (TRTCAudioLayoutManager) findViewById(R.id.trtc_layout_manager);
        mInvitingGroup = (Group) findViewById(R.id.group_inviting);
        mImgContainerLl = (LinearLayout) findViewById(R.id.ll_img_container);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
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

    private String mLinkUserId = "";

    //????????????????????????
    public void uploadFeeLinkHeartBeat() {
        String userId = mSelfModel.userId;
        String url = "/api/live/reportAnchorChat";
        Map<String, String> map = new HashMap<>();
        map.put("anchor_uid", mLinkUserId);
        map.put("type", "1");
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

            }
        });
    }

    public void uploadAnchorHeartBeat() {
        String url = "/api/live/reportAnchorChatFromLive";
        Map<String, String> map = new HashMap<>();
        map.put("uid", mLinkUserId);
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

            }
        });
    }

    //????????????????????????
    public void uploadLinkHeartBeat() {
        String url = "/api/Power/updateChatDuration";
        Map<String, String> map = new HashMap<>();
        map.put("type", "1");
        map.put("touid", mLinkUserId);
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
                FreeData data = new Gson().fromJson((String) o, FreeData.class);
                Log.v("????????????????????????", data.getData().getMin() + "");
            }
        });
    }


    /**
     * ??????????????????
     */
    public void showWaitingResponseView() {
        //1. ?????????????????????
        TRTCAudioLayout layout = mLayoutManagerTrtc.allocAudioCallLayout(mSponsorUserModel.userId);
        layout.setUserId(mSponsorUserModel.userName);
        GlideEngine.loadImage(layout.getImageView(), mSponsorUserModel.userAvatar, R.drawable.live_default_head_img, RADIUS);
        updateUserView(mSponsorUserModel, layout);
        //2. ????????????????????????
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
                //1.?????????????????????
                mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
                addUserToManager(mSelfModel);
                //2.????????????
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
        addUserToManager(mSelfModel);
        //2. ?????????????????????
        for (UserModel userModel : mCallUserModelList) {
            TRTCAudioLayout layout = addUserToManager(userModel);
            layout.startLoading();
        }
        //3. ???????????????
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
        //4. ??????????????????????????????
        hideOtherInvitingUserView();
    }

    /**
     * ????????????????????????
     */
    public void showCallingView() {
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
                    }
                    if (isFeeLink()) { //????????????
                        if (mTimeCount % 60 == 5) {
                            Log.d("callcall","link2 id = " + mLinkUserId);
                            if (mCallType != TYPE_BEING_CALLED) { //???????????????
                                if (!TextUtils.isEmpty(mLinkUserId)) {
                                    uploadFeeLinkHeartBeat();
                                }
                            } else {
                                if (!TextUtils.isEmpty(mLinkUserId)) {
                                    uploadAnchorHeartBeat();
                                }
                            }
                        }
                    } else {
                        if (mTimeCount % 60 == 5) { //???????????????
                            if (mCallType != TYPE_BEING_CALLED) {
                                if (!TextUtils.isEmpty(mLinkUserId)) {
                                    uploadLinkHeartBeat();
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
        if (mOtherInvitingUserModelList == null || mOtherInvitingUserModelList.isEmpty()) {
            return;
        }
        mInvitingGroup.setVisibility(View.VISIBLE);
        int squareWidth = getResources().getDimensionPixelOffset(R.dimen.contact_avatar_width);
        int leftMargin = getResources().getDimensionPixelOffset(R.dimen.small_image_left_margin);
        for (int index = 0; index < mOtherInvitingUserModelList.size() && index < MAX_SHOW_INVITING_USER; index++) {
            UserModel userModel = mOtherInvitingUserModelList.get(index);
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(squareWidth, squareWidth);
            if (index != 0) {
                layoutParams.leftMargin = leftMargin;
            }
            imageView.setLayoutParams(layoutParams);
            GlideEngine.loadImage(imageView, userModel.userAvatar, R.drawable.live_default_head_img, SelectContactActivity.RADIUS);
            updateUserView(userModel, imageView);
            mImgContainerLl.addView(imageView);
        }
    }

    private void hideOtherInvitingUserView() {
        mInvitingGroup.setVisibility(View.GONE);
    }

    private String userHeadUrl = "";

    private TRTCAudioLayout addUserToManager(final UserModel userModel) {
        final TRTCAudioLayout layout = mLayoutManagerTrtc.allocAudioCallLayout(userModel.userId);
        layout.setUserId(userModel.userName);
        // Log.d("faceUrl2 = ",userModel.userAvatar);
        if (!userModel.userId.equals(mSelfModel.userId)) {
            userHeadUrl = userModel.userAvatar;
        }
        GlideEngine.loadImage(layout.getImageView(), userModel.userAvatar, R.drawable.live_default_head_img, RADIUS);
        updateUserView(userModel, layout);
        return layout;
    }

    private void updateUserView(final UserModel userModel, final Object layout) {
        if (!TextUtils.isEmpty(userModel.userName) && !TextUtils.isEmpty(userModel.userAvatar)) {
            return;
        }
        ArrayList<String> users = new ArrayList<>();
        users.add(userModel.userId);
        V2TIMManager.getInstance().getUsersInfo(users, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onError(int code, String desc) {
                TUILiveLog.w(TAG, "getUsersInfo code:" + "|desc:" + desc);
            }

            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                if (v2TIMUserFullInfos == null || v2TIMUserFullInfos.size() != 1) {
                    TUILiveLog.w(TAG, "getUsersInfo v2TIMUserFullInfos error");
                    return;
                }
                if (TextUtils.isEmpty(userModel.userName)) {
                    userModel.userName = v2TIMUserFullInfos.get(0).getNickName();
                    if (layout instanceof TRTCAudioLayout) {
                        ((TRTCAudioLayout) layout).setUserId(v2TIMUserFullInfos.get(0).getNickName());
                    }
                }
                userModel.userAvatar = v2TIMUserFullInfos.get(0).getFaceUrl();
                Log.d("faceUrl = ", userModel.userAvatar);
                if (layout instanceof TRTCAudioLayout) {
                    GlideEngine.loadImage(((TRTCAudioLayout) layout).getImageView(), userModel.userAvatar, R.drawable.live_default_head_img, RADIUS);
                } else if (layout instanceof ImageView) {
                    GlideEngine.loadImage((ImageView) layout, userModel.userAvatar, R.drawable.live_default_head_img, RADIUS);
                }
            }
        });
    }

    private boolean isStartService = false;
    private ServiceConnection mVideoServiceConnection = new ServiceConnection() {
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
        if (!PermissionUtils.requestFloatPermission(TRTCAudioCallActivity.this, 24)) {
            ToastUtil.toastShortMessage("????????????????????????????????????");
            return;
        }
        String lastBigUserId = mLayoutManagerTrtc.getLastTRTCLayoutEntity();
        isStartService = true;
        Constents.mAudioViewLayout = mLayoutManagerTrtc;
        Intent intent = new Intent(this, FloatVideoWindowService.class);//???????????????????????????
        intent.putExtra("userId", lastBigUserId);
        intent.putExtra("userUrl", userHeadUrl);
        intent.putExtra("type", 2);
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
//        TXCloudVideoView txCloudVideoView = mLayoutManagerTrtc.findCloudViewView(userId).getVideoView();
//        if (txCloudVideoView == null) {
//            txCloudVideoView = mLayoutManagerTrtc.allocCloudVideoView(userId).getVideoView();
//        }
//        if (Constents.currentUserID.equals(userId)) {
//            TXCGLSurfaceView mTXCGLSurfaceView = txCloudVideoView.getGLSurfaceView();
//            if (mTXCGLSurfaceView != null && mTXCGLSurfaceView.getParent() != null) {
//                ((ViewGroup) mTXCGLSurfaceView.getParent()).removeView(mTXCGLSurfaceView);
//                txCloudVideoView.addVideoView(mTXCGLSurfaceView);
//            }
//        } else {
//            TextureView mTextureView = txCloudVideoView.getVideoView();
//            if (mTextureView != null && mTextureView.getParent() != null) {
//                ((ViewGroup) mTextureView.getParent()).removeView(mTextureView);
//                txCloudVideoView.addVideoView(mTextureView);
//            }
//        }
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
                    if (!PermissionUtils.requestFloatPermission(TRTCAudioCallActivity.this,24)) {
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
