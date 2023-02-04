package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.av.trtc;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.liteav.audio.TXAudioEffectManager;
import com.tencent.liteav.beauty.TXBeautyManager;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.base.TRTCLogger;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.base.TXCallback;
import com.tencent.qcloud.tim.uikit.utils.NetWorkUtils;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tencent.trtc.TRTCCloudDef.TRTC_TranscodingConfigMode_Template_PresetLayout;
import static com.tencent.trtc.TRTCCloudDef.TRTC_VIDEO_MIRROR_TYPE_DISABLE;
import static com.tencent.trtc.TRTCCloudDef.TRTC_VIDEO_MIRROR_TYPE_ENABLE;
import static com.tencent.trtc.TRTCCloudDef.TRTC_VIDEO_ROTATION_0;
import static com.tencent.trtc.TRTCCloudDef.TRTC_VIDEO_ROTATION_90;
import static com.tencent.trtc.TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG;

public class TXTRTCLiveRoom extends TRTCCloudListener implements ITRTCTXLiveRoom {
    private static final String TAG = "TXTRTCLiveRoom";
    private static final long PLAY_TIME_OUT = 5000;

    private static TXTRTCLiveRoom sInstance;

    private TRTCCloud mTRTCCloud;
    private TXBeautyManager mTXBeautyManager;
    // 一开始进房的角色
    private int mOriginRole;
    private TXCallback mEnterRoomCallback;
    private TXCallback mExitRoomCallback;
    private TXCallback mPKCallback;
    private boolean mIsInRoom;
    private ITXTRTCLiveRoomDelegate mDelegate;
    private String mUserId;
    private String mRoomId;
    private TRTCCloudDef.TRTCParams mTRTCParams;
    private Map<String, TXCallback> mPlayCallbackMap;
    private Map<String, Runnable> mPlayTimeoutRunnable;
    private Handler mMainHandler;

    private TXCallback mScreenCaptureCallback;

    public static synchronized TXTRTCLiveRoom getInstance() {
        if (sInstance == null) {
            sInstance = new TXTRTCLiveRoom();
        }
        return sInstance;
    }

    @Override
    public void init(Context context) {
        TRTCLogger.i(TAG, "init context:" + context);
        mTRTCCloud = TRTCCloud.sharedInstance(context);
        mTXBeautyManager = mTRTCCloud.getBeautyManager();
        mPlayCallbackMap = new HashMap<>();
        mPlayTimeoutRunnable = new HashMap<>();
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void setDelegate(ITXTRTCLiveRoomDelegate delegate) {
        TRTCLogger.i(TAG, "init delegate:" + delegate);
        mDelegate = delegate;
    }

    @Override
    public void enterRoom(int sdkAppId, String roomId, String userId, String userSign, int role, TXCallback callback) {
        if (sdkAppId == 0 || TextUtils.isEmpty(roomId) || TextUtils.isEmpty(userId) || TextUtils.isEmpty(userSign)) {
            // 参数非法，可能执行了退房，或者登出
            TRTCLogger.e(TAG, "enter trtc room fail. params invalid. room id:" + roomId + " user id:" + userId + " sign is empty:" + TextUtils.isEmpty(userSign));
            if (callback != null) {
                callback.onCallback(-1, "enter trtc room fail. params invalid. room id:" + roomId + " user id:" + userId + " sign is empty:" + TextUtils.isEmpty(userSign));
            }
            return;
        }
        mUserId = userId;
        mRoomId = roomId;
        mOriginRole = role;
        TRTCLogger.i(TAG, "enter room, app id:" + sdkAppId + " room id:" + roomId + " user id:" + userId + " sign:" + TextUtils.isEmpty(userId) + " role:" + role);
        mEnterRoomCallback = callback;
        mTRTCParams = new TRTCCloudDef.TRTCParams();
        mTRTCParams.sdkAppId = sdkAppId;
        mTRTCParams.userId = userId;
        mTRTCParams.userSig = userSign;
        mTRTCParams.role = role;
        if (mIsRecord) {
            Log.d("record","userDefineRecordId = " + roomId + "_" + userId);
            mTRTCParams.userDefineRecordId = sdkAppId + "_" +  roomId + "_" + userId + "_main";
        }
        // 字符串房间号逻辑
        mTRTCParams.roomId = Integer.valueOf(roomId);
        internalEnterRoom();
    }

    private void internalEnterRoom() {
        // 进房前设置一下监听，不然可能会被其他信息打断
        if (mTRTCParams == null) {
            return;
        }
        mTRTCCloud.setListener(this);
        mTRTCCloud.enterRoom(mTRTCParams, TRTCCloudDef.TRTC_APP_SCENE_LIVE);

        //弱网情况下 流畅优先
        //TRTCCloudDef.TRTCNetworkQosParam param = new TRTCCloudDef.TRTCNetworkQosParam();
       // param.preference = TRTCCloudDef.TRTC_VIDEO_QOS_PREFERENCE_SMOOTH;
       // param.controlMode = TRTCCloudDef.VIDEO_QOS_CONTROL_SERVER;
        //mTRTCCloud.setNetworkQosParam(param);
    }

    @Override
    public void onFirstVideoFrame(String userId, int streamType, int width, int height) {
        TRTCLogger.i(TAG, "onFirstVideoFrame:" + userId);
        if (userId == null) {
            // userId 为 null，代表开始渲染本地采集的摄像头画面
        } else {
            stopTimeoutRunnable(userId);
            TXCallback callback = mPlayCallbackMap.remove(userId);
            if (callback != null) {
                callback.onCallback(0, userId + "播放成功");
            }
        }
    }

    @Override
    public void exitRoom(TXCallback callback) {
        TRTCLogger.i(TAG, "exit room.");
        mUserId = null;
        mTRTCParams = null;
        mExitRoomCallback = callback;
        mPlayCallbackMap.clear();
        mPlayTimeoutRunnable.clear();
        mMainHandler.removeCallbacksAndMessages(null);
        mTRTCCloud.exitRoom();
        mEnterRoomCallback = null;
    }

    @Override
    public void startPublish(String streamId, TXCallback callback) {
        TRTCLogger.i(TAG, "start publish. stream id:" + streamId);
        if (!isEnterRoom()) {
            // 还没进房，先进入房间，再推流
            TRTCLogger.i(TAG, "not enter room yet, enter trtc room.");
            internalEnterRoom();
        }
        // 如果是观众，那么则切换到主播
        if (mOriginRole == TRTCCloudDef.TRTCRoleAudience) {
            mTRTCCloud.switchRole(TRTCCloudDef.TRTCRoleAnchor);
            // 观众切换到主播是小主播，小主播设置一下分辨率
            TRTCCloudDef.TRTCVideoEncParam param = new TRTCCloudDef.TRTCVideoEncParam();
            param.videoResolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_320_240;
            param.videoBitrate = 375;
            param.videoFps = 15;
            param.videoResolutionMode = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_MODE_PORTRAIT;
            mTRTCCloud.setVideoEncoderParam(param);


        } else if (mOriginRole == TRTCCloudDef.TRTCRoleAnchor) {
            // 大主播的时候切换分辨率
            TRTCCloudDef.TRTCVideoEncParam param = new TRTCCloudDef.TRTCVideoEncParam();
            param.videoResolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_960_540;
            param.videoBitrate = 850;
            param.videoFps = 15;
            param.videoResolutionMode = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_MODE_PORTRAIT;
            mTRTCCloud.setVideoEncoderParam(param);

        }

        mTRTCCloud.startPublishing(streamId, TRTC_VIDEO_STREAM_TYPE_BIG);
        mTRTCCloud.startLocalAudio();
        if (callback != null) {
            callback.onCallback(0, "start publish success.");
        }
    }

    @Override
    public void stopPublish(TXCallback callback) {
        TRTCLogger.i(TAG, "stop publish.");
        mTRTCCloud.stopPublishing();
        mTRTCCloud.stopLocalAudio();
        if (mOriginRole == TRTCCloudDef.TRTCRoleAudience) {
            mTRTCCloud.switchRole(TRTCCloudDef.TRTCRoleAudience);
        } else if (mOriginRole == TRTCCloudDef.TRTCRoleAnchor) {
            mTRTCCloud.exitRoom();
        }
        if (callback != null) {// TODO:
            callback.onCallback(0, "stop publish success.");
        }
    }

    @Override
    public void startCameraPreview(boolean isFront, TXCloudVideoView view, TXCallback callback) {
        TRTCLogger.i(TAG, "start camera preview.");
        mTRTCCloud.startLocalPreview(isFront, view);
        if (callback != null) {
            callback.onCallback(0, "success");
        }
    }

    @Override
    public void stopCameraPreview() {
        TRTCLogger.i(TAG, "stop camera preview.");
        mTRTCCloud.stopLocalPreview();
    }

    @Override
    public void switchCamera() {
        mTRTCCloud.switchCamera();
    }

    @Override
    public void setMirror(boolean isMirror) {
        TRTCLogger.i(TAG, "mirror:" + isMirror);
        if (isMirror) {
            mTRTCCloud.setVideoEncoderMirror(isMirror);
//            TRTCCloudDef.TRTCRenderParams tarams = new TRTCCloudDef.TRTCRenderParams();
//            tarams.mirrorType = TRTC_VIDEO_MIRROR_TYPE_ENABLE;
//            mTRTCCloud.setLocalRenderParams(tarams);
            //mTRTCCloud.setLocalViewMirror(TRTCCloudDef.TRTC_VIDEO_MIRROR_TYPE_ENABLE);
        } else {
            mTRTCCloud.setVideoEncoderMirror(isMirror);
            //mTRTCCloud.setLocalViewMirror(TRTCCloudDef.TRTC_VIDEO_MIRROR_TYPE_DISABLE);
//            TRTCCloudDef.TRTCRenderParams tarams = new TRTCCloudDef.TRTCRenderParams();
//            tarams.mirrorType = TRTC_VIDEO_MIRROR_TYPE_DISABLE;
//            mTRTCCloud.setLocalRenderParams(tarams);
        }
    }

    @Override
    public void muteLocalAudio(boolean mute) {
        TRTCLogger.i(TAG, "mute local audio, mute:" + mute);
        mTRTCCloud.muteLocalAudio(mute);
    }

    @Override
    public void muteRemoteAudio(String userId, boolean mute) {
        TRTCLogger.i(TAG, "mute remote audio, user id:" + userId + " mute:" + mute);
        mTRTCCloud.muteRemoteAudio(userId, mute);
    }

    @Override
    public void muteAllRemoteAudio(boolean mute) {
        TRTCLogger.i(TAG, "mute all remote audio, mute:" + mute);
        mTRTCCloud.muteAllRemoteAudio(mute);
    }

    @Override
    public boolean isEnterRoom() {
        return mIsInRoom;
    }

    @Override
    public void setMixConfig(List<TXTRTCMixUser> list, boolean isPK) {
        if (list == null) {
            mTRTCCloud.setMixTranscodingConfig(null);
        } else if (isPK) {
            int videoWidth = 540;
            int videoHeight = 960;

            TRTCCloudDef.TRTCTranscodingConfig config = new TRTCCloudDef.TRTCTranscodingConfig();
            config.videoWidth = videoWidth;
            config.videoHeight = videoHeight;
            config.videoGOP = 1;
            config.videoFramerate = 15;
            config.videoBitrate = 1000;
            config.audioSampleRate = 48000;
            config.audioBitrate = 64;
            config.audioChannels = 1;

            config.mode = TRTC_TranscodingConfigMode_Template_PresetLayout;

            TRTCCloudDef.TRTCMixUser mixUser = new TRTCCloudDef.TRTCMixUser();
            mixUser.userId = "$PLACE_HOLDER_LOCAL_MAIN$";
            mixUser.roomId = null;
            mixUser.zOrder = 1;
            mixUser.x = 0;
            mixUser.y = 120;
            mixUser.width = 270;
            mixUser.height = 360;


            config.mixUsers = new ArrayList<>();
            config.mixUsers.add(mixUser);

            mixUser = new TRTCCloudDef.TRTCMixUser();
            mixUser.userId = "$PLACE_HOLDER_REMOTE$";
           // mixUser.userId = list.get(0).userId;
            mixUser.roomId = list.get(0).roomId;
            mixUser.zOrder = 1;
            mixUser.x = 270;
            mixUser.y = 120;
            mixUser.width = 270;
            mixUser.height = 360;
            config.mixUsers.add(mixUser);
            config.backgroundImage = "8900";
            saveMixConfig(config); //保存混流配置
            mTRTCCloud.setMixTranscodingConfig(config);

        } else {
            // 背景大画面宽高
            int videoWidth = 720;
            int videoHeight = 1280;

            // 小画面宽高
            int subWidth = 180;
            int subHeight = 320;

            int offsetX = 20;
            int offsetY = 50;

            int bitrate = 200;

            int resolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_960_540;
            switch (resolution) {
                case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_160_160: {
                    videoWidth = 160;
                    videoHeight = 160;
                    subWidth = 32;
                    subHeight = 48;
                    offsetY = 10;
                    bitrate = 200;
                    break;
                }
                case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_320_180: {
                    videoWidth = 192;
                    videoHeight = 336;
                    subWidth = 54;
                    subHeight = 96;
                    offsetY = 30;
                    bitrate = 400;
                    break;
                }
                case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_320_240: {
                    videoWidth = 240;
                    videoHeight = 320;
                    subWidth = 54;
                    subHeight = 96;
                    offsetY = 30;
                    bitrate = 400;
                    break;
                }
                case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_480_480: {
                    videoWidth = 480;
                    videoHeight = 480;
                    subWidth = 72;
                    subHeight = 128;
                    bitrate = 600;
                    break;
                }
                case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_360: {
                    videoWidth = 368;
                    videoHeight = 640;
                    subWidth = 90;
                    subHeight = 160;
                    bitrate = 800;
                    break;
                }
                case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_480: {
                    videoWidth = 480;
                    videoHeight = 640;
                    subWidth = 90;
                    subHeight = 160;
                    bitrate = 800;
                    break;
                }
                case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_960_540: {
                    videoWidth = 544;
                    videoHeight = 960;
                    offsetX = 40;
                    offsetY = 70;
                    subWidth = 180;
                    subHeight = 240;
                    //subWidth = 1;
                    //subHeight = 1;
                    bitrate = 1000;
                    break;
                }
                case TRTCCloudDef.TRTC_VIDEO_RESOLUTION_1280_720: {
                    videoWidth = 720;
                    videoHeight = 1280;
                    subWidth = 192;
                    subHeight = 336;
                    bitrate = 1500;
                    break;
                }
            }

            TRTCCloudDef.TRTCTranscodingConfig config = new TRTCCloudDef.TRTCTranscodingConfig();
            config.videoWidth = videoWidth;
            config.videoHeight = videoHeight;
            config.videoGOP = 1;
            config.videoFramerate = 15;
            config.videoBitrate = bitrate;
            config.audioSampleRate = 48000;
            config.audioBitrate = 64;
            config.audioChannels = 2;

            // 设置混流后主播的画面位置
            TRTCCloudDef.TRTCMixUser mixUser = new TRTCCloudDef.TRTCMixUser();
            mixUser.userId = mUserId; // 以主播uid为broadcaster为例
            mixUser.roomId = mRoomId;
            mixUser.zOrder = 1;
            mixUser.x = 0;
            mixUser.y = 0;
            mixUser.width = videoWidth;
            mixUser.height = videoHeight;

            config.mixUsers = new ArrayList<>();
            config.mixUsers.add(mixUser);
            // 设置混流后各个小画面的位置
            int index = 0;
            //场控混流相关
            int _index = 0;
            int managerWidth = 1;
            for (TXTRTCMixUser txtrtcMixUser : list) {
                TRTCCloudDef.TRTCMixUser _mixUser = new TRTCCloudDef.TRTCMixUser();
                _mixUser.userId = txtrtcMixUser.userId;
                _mixUser.roomId = txtrtcMixUser.roomId == null ? mRoomId : txtrtcMixUser.roomId;
                _mixUser.streamType = TRTC_VIDEO_STREAM_TYPE_BIG;
                _mixUser.zOrder = 2 + index;
                if (liveManagerUserList.contains(_mixUser.userId)) { //场控发言列表
                    _mixUser.x = videoWidth - offsetX - (_index * (managerWidth + offsetX) );
                    _mixUser.y = videoHeight - offsetY - 50;
                    _mixUser.width = managerWidth;
                    _mixUser.height = managerWidth;
                    _mixUser.inputType = 3;
                    _index++;
                } else { //连麦列表
                    if (index < 3) {
                        // 前三个小画面靠右从下往上铺
                        _mixUser.x = videoWidth - offsetX - subWidth;
                        _mixUser.y = videoHeight - offsetY - index * subHeight - subHeight - 10;
                        _mixUser.width = subWidth;
                        _mixUser.height = subHeight;
                    } else if (index < 6) {
                        // 后三个小画面靠左从下往上铺
                        _mixUser.x = offsetX;
                        _mixUser.y = videoHeight - offsetY - (index - 3) * subHeight - subHeight;
                        _mixUser.width = subWidth;
                        _mixUser.height = subHeight;
                    } else {
                        // 最多只叠加六个小画面
                    }
                    ++index;
                }
                config.mixUsers.add(_mixUser);
            }
            saveMixConfig(config); //保存混流配置
            mTRTCCloud.setMixTranscodingConfig(config);
        }
    }

    @Override
    public void showVideoDebugLog(boolean isShow) {
        if (isShow) {
            mTRTCCloud.showDebugView(2);
        } else {
            mTRTCCloud.showDebugView(0);
        }
    }

    @Override
    public void startPK(String roomId, String userId, TXCallback callback) {
        TRTCLogger.i(TAG, "start pk, room id:" + roomId + " user id:" + userId);
        mPKCallback = callback;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomId", Integer.valueOf(roomId));
            jsonObject.put("userId", userId);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        mTRTCCloud.ConnectOtherRoom(jsonObject.toString());
    }

    @Override
    public void stopPK() {
        TRTCLogger.i(TAG, "stop pk.");
        mTRTCCloud.DisconnectOtherRoom();
    }


    @Override
    public void startPlay(final String userId, TXCloudVideoView view, TXCallback callback) {
        TRTCLogger.i(TAG, "start play user id:" + userId + " view:" + view);
        mPlayCallbackMap.put(userId, callback);
        mTRTCCloud.startRemoteView(userId, view);
        //停掉上一次超时
        stopTimeoutRunnable(userId);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TRTCLogger.e(TAG, "start play timeout:" + userId);
                TXCallback callback = mPlayCallbackMap.remove(userId);
                if (callback != null) {
                    callback.onCallback(-1, "play " + userId + " timeout.");
                }
            }
        };
        mPlayTimeoutRunnable.put(userId, runnable);
        mMainHandler.postDelayed(runnable, PLAY_TIME_OUT);

    }

    private void stopTimeoutRunnable(String userId) {
        if (mPlayTimeoutRunnable == null) {
            return;
        }
        Runnable runnable = mPlayTimeoutRunnable.get(userId);
        mMainHandler.removeCallbacks(runnable);
    }

    @Override
    public void stopPlay(String userId, TXCallback callback) {
        TRTCLogger.i(TAG, "stop play user id:" + userId);
        mPlayCallbackMap.remove(userId);
        stopTimeoutRunnable(userId);
        mTRTCCloud.stopRemoteView(userId);
        if (callback != null) {
            callback.onCallback(0, "stop play success.");
        }
    }

    @Override
    public void stopAllPlay() {
        TRTCLogger.i(TAG, "stop all play");
        mTRTCCloud.stopAllRemoteView();
    }


    @Override
    public void onEnterRoom(long l) {
        TRTCLogger.i(TAG, "on enter room, result:" + l);
        if (mEnterRoomCallback != null) {
            if (l > 0) {
                mIsInRoom = true;
                mEnterRoomCallback.onCallback(0, "enter room success.");
                mEnterRoomCallback = null;
            } else {
                mIsInRoom = false;
                mEnterRoomCallback.onCallback((int) l, "enter room fail");
                mEnterRoomCallback = null;
            }
        }
    }

    @Override
    public void onExitRoom(int i) {
        TRTCLogger.i(TAG, "on exit room.");
        if (mExitRoomCallback != null) {
            mIsInRoom = false;
            mExitRoomCallback.onCallback(0, "exit room success.");
            mExitRoomCallback = null;
        }
    }

    @Override
    public void onConnectOtherRoom(String s, int i, String s1) {
        TRTCLogger.i(TAG, "on connect other room, code:" + i + " msg:" + s1);
        if (mPKCallback != null) {
            if (i == 0) {
                mPKCallback.onCallback(0, "connect other room success.");
            } else {
                mPKCallback.onCallback(i, "connect other room fail. msg:" + s1);
            }
        }
    }

    @Override
    public void onRemoteUserEnterRoom(String userId) {
        TRTCLogger.i(TAG, "on user enter, user id:" + userId);
        if (mDelegate != null) {
            mDelegate.onTRTCAnchorEnter(userId);
        }
    }

    @Override
    public void onRemoteUserLeaveRoom(String userId, int i) {
        TRTCLogger.i(TAG, "on user exit, user id:" + userId);
        if (mDelegate != null) {
            mDelegate.onTRTCAnchorExit(userId);
        }
    }

    @Override
    public void onUserVideoAvailable(String userId, boolean available) {
        TRTCLogger.i(TAG, "on user available, user id:" + userId + " available:" + available);
        if (mDelegate != null) {
            if (available) {
                mDelegate.onTRTCStreamAvailable(userId);
            } else {
                mDelegate.onTRTCStreamUnavailable(userId);
            }
        }
    }

    @Override
    public void onSetMixTranscodingConfig(int i, String s) {
        super.onSetMixTranscodingConfig(i, s);
        TRTCLogger.i(TAG, "on set mix transcoding, code:" + i + " msg:" + s);
    }

    @Override
    public TXBeautyManager getTXBeautyManager() {
        return mTXBeautyManager;
    }

    @Override
    public TXAudioEffectManager getAudioEffectManager() {
        return mTRTCCloud.getAudioEffectManager();
    }

    @Override
    public void setAudioQuality(int quality) {
        mTRTCCloud.setAudioQuality(quality);
    }

//    public void muteVideo() {
//        mTRTCCloud.muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG,true);
//    }
//
//    public void muteVideoWithBitmap(Bitmap bitmap) {
//        mTRTCCloud.setVideoMuteImage(bitmap,5);
//        mTRTCCloud.muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG,true);
//    }
//
//    public void openVideo() {
//        mTRTCCloud.muteLocalVideo(TRTC_VIDEO_STREAM_TYPE_BIG,false);
//    }


    @Override
    public void onSpeedTest(TRTCCloudDef.TRTCSpeedTestResult currentResult, int finishedCount, int totalCount) {
        super.onSpeedTest(currentResult, finishedCount, totalCount);
        Log.d("speed", "网络质量评分 : " + currentResult.quality);
        Log.d("speed", "上行丢包率 : " + currentResult.upLostRate);
        Log.d("speed", "下行丢包率 : " + currentResult.downLostRate);
        Log.d("speed", "网络延时 : " + currentResult.rtt);
    }

    @Override
    public void onSpeedTestResult(TRTCCloudDef.TRTCSpeedTestResult result) {
        super.onSpeedTestResult(result);
        Log.d("speed", "网络质量评分 : " + result.quality);
        Log.d("speed", "上行丢包率 : " + result.upLostRate);
        Log.d("speed", "下行丢包率 : " + result.downLostRate);
        Log.d("speed", "网络延时 : " + result.rtt);
    }

    List<String> liveManagerUserList = new ArrayList<>();

    public void addLiveManagerUser(String userId) {
        if (!liveManagerUserList.contains(userId)) {
            liveManagerUserList.add(userId);
        }
    }

    public void removeManagerUser(String userId) {
        if (liveManagerUserList.contains(userId)) {
            liveManagerUserList.remove(userId);
        }
    }

    public void clearManagerUser() {
        liveManagerUserList.clear();
    }

    public List<String> getLiveManagerUserList() {
        return liveManagerUserList;
    }

    boolean mIsRecord = false;
    public void setIsRecord(boolean isRecord) {
        mIsRecord = isRecord;
    }

    TRTCCloudDef.TRTCTranscodingConfig mixConfig;
    void saveMixConfig(TRTCCloudDef.TRTCTranscodingConfig config) {
        mixConfig = config;
    }
    //关闭某用户的声音
    public void doMuteRemoteUser(String userId,boolean isMute) {
        if (mixConfig == null || mixConfig.mixUsers==null) {
            return;
        }
        for (TRTCCloudDef.TRTCMixUser user: mixConfig.mixUsers) {
            if (user.userId.equals(userId)) {
                if (isMute) {
                    user.inputType = 2;
                } else {
                    user.inputType = 1;
                }
                mTRTCCloud.setMixTranscodingConfig(mixConfig);
            }
        }
    }

    //关闭Pk主播的声音
    public void doMuteRemoteAnchor(String userId,boolean isMute) {
        if (mixConfig == null || mixConfig.mixUsers==null) {
            return;
        }
        for (TRTCCloudDef.TRTCMixUser user: mixConfig.mixUsers) {
            if (user.userId.equals("$PLACE_HOLDER_REMOTE$") ) {
                if (isMute) {
                    user.inputType = 2;
                } else {
                    user.inputType = 1;
                }
                mTRTCCloud.setMixTranscodingConfig(mixConfig);
            }
        }
    }

    @Override
    public void onScreenCaptureStarted() {
        super.onScreenCaptureStarted();
        if (mScreenCaptureCallback != null) {
            mScreenCaptureCallback.onCallback(1000,"开始录屏");
        }
    }

    @Override
    public void onScreenCaptureStopped(int reason) {
        super.onScreenCaptureStopped(reason);
        if (mScreenCaptureCallback != null) {
            mScreenCaptureCallback.onCallback(1001,"停止录屏");
        }
    }

    @Override
    public void onScreenCapturePaused() {
        super.onScreenCapturePaused();
        Log.d("logutil","screen pause");
    }

    @Override
    public void onError(int errCode, String errMsg, Bundle extraInfo) {
        super.onError(errCode, errMsg, extraInfo);
        //Log.d("trtc error","trtc error " + errCode + "  " + errMsg);
        Log.d("logutil","on error" + errCode + " ---- " + errMsg);
        if (errCode == -1308 || errCode == -1309) {
            if (mScreenCaptureCallback != null) {
                mScreenCaptureCallback.onCallback(errCode,errMsg);
            }
        }
    }

    @Override
    public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {
        super.onWarning(warningCode, warningMsg, extraInfo);
        Log.d("logutil","on warning" + warningCode + " ---- " + warningMsg);
    }

    public void setScreenCaptureCallback(TXCallback callback) {
        mScreenCaptureCallback = callback;
    }






}
