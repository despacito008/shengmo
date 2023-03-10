package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.audio.TXAudioEffectManager;
import com.tencent.liteav.beauty.TXBeautyManager;
import com.tencent.liteav.trtc.impl.TRTCCloudImpl;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoom;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDelegate;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.av.liveplayer.TXLivePlayerRoom;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.av.trtc.ITXTRTCLiveRoomDelegate;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.av.trtc.TXTRTCLiveRoom;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.av.trtc.TXTRTCMixUser;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.base.TRTCLogger;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.base.TXCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.base.TXRoomInfo;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.base.TXRoomInfoListCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.base.TXUserInfo;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.base.TXUserListCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.room.ITXRoomServiceDelegate;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.room.impl.TXRoomService;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef.ROOM_STATUS_NONE;
import static com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef.ROOM_STATUS_PK;


public class TRTCLiveRoomImpl extends TRTCLiveRoom implements ITXTRTCLiveRoomDelegate, ITXRoomServiceDelegate {
    private static final int CODE_SUCCESS   = 0;
    private static final int CODE_ERROR     = -1;
    public static final  int PK_ANCHOR_NUMS = 2;

    private static final class Role {
        static final int UNKNOWN       = 0;
        static final int TRTC_ANCHOR   = 1;
        static final int TRTC_AUDIENCE = 2;
        static final int CDN_AUDIENCE  = 3;
    }

    private static final class TXCallbackHolder implements TXCallback {
        private WeakReference<TRTCLiveRoomImpl> wefImpl;

        private TRTCLiveRoomCallback.ActionCallback realCallback;

        TXCallbackHolder(TRTCLiveRoomImpl impl) {
            wefImpl = new WeakReference<>(impl);
        }

        public void setRealCallback(TRTCLiveRoomCallback.ActionCallback callback) {
            realCallback = callback;
        }

        @Override
        public void onCallback(final int code, final String msg) {
            TRTCLiveRoomImpl impl = wefImpl.get();
            if (impl != null) {
                impl.runOnDelegateThread(new Runnable() {
                    @Override
                    public void run() {
                        if (realCallback != null) {
                            realCallback.onCallback(code, msg);
                        }
                    }
                });
            }
        }
    }

    private static final String               TAG = "TRTCLiveRoom";
    private static       TRTCLiveRoomImpl     sInstance;
    private              TRTCLiveRoomDelegate mDelegate;
    // ????????????????????????????????????????????????????????????????????????
    private              Handler              mMainHandler;
    // ??????????????????????????????
    private              Handler              mDelegateHandler;

    // SDK AppId
    private int                                mSDKAppId;
    // ??????ID RoomService ??? TRTC ??????????????? ID
    private String                             mRoomId;
    // ??????ID
    private String                             mUserId;
    // ????????????
    private String                             mUserSign;
    // ??????
    private TRTCLiveRoomDef.TRTCLiveRoomConfig mRoomConfig;
    // ???????????????????????????cdn
    private boolean                            mUseCDNFirst;
    // CDN???????????????
    private String                             mCDNDomain;
    // ?????????????????????
    private int                                mRoomLiveStatus = ROOM_STATUS_NONE;
    // ?????????????????????
    private TRTCLiveRoomDef.TRTCLiveRoomInfo   mLiveRoomInfo;
    // ???????????????Tuikit??????
    private boolean                            mIsAttachTuikit;

    // ????????????
    private Set<String> mAnchorList;
    // ????????????????????????
    private Set<String> mAudienceList;

    // ??????????????? user id ???????????? View ?????? CDN ????????? TRTC ????????????
    private Map<String, TXCloudVideoView> mPlayViewMap;

    // ???????????????????????????????????????
    // ????????????
    private int mCurrentRole;
    // ????????????
    private int mTargetRole;
    // ??????????????? - ??????????????????????????? CDN ???????????? TRTC ??????
    private int mOriginalRole;

    // ??????????????????
    private TXCallbackHolder mJoinAnchorCallbackHolder;
    // ??????????????????
    private TXCallbackHolder mRequestPKHolder;

    public static synchronized TRTCLiveRoom sharedInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TRTCLiveRoomImpl(context.getApplicationContext());
        }
        return sInstance;
    }

    public static synchronized void destroySharedInstance() {
        if (sInstance != null) {
            sInstance.destroy();
            sInstance = null;
        }
    }

    private TRTCLiveRoomImpl(Context context) {
        mCurrentRole = Role.CDN_AUDIENCE;
        mOriginalRole = Role.CDN_AUDIENCE;
        mTargetRole = Role.CDN_AUDIENCE;
        mMainHandler = new Handler(Looper.getMainLooper());
        mDelegateHandler = new Handler(Looper.getMainLooper());
        mLiveRoomInfo = new TRTCLiveRoomDef.TRTCLiveRoomInfo();
        mSDKAppId = 0;
        mRoomId = "";
        mUserId = "";
        mUserSign = "";
        TXLivePlayerRoom.getInstance().init(context);
        TXTRTCLiveRoom.getInstance().init(context);
        TXTRTCLiveRoom.getInstance().setDelegate(this);
        TXRoomService.getInstance().init(context);
        TXRoomService.getInstance().setDelegate(this);
        mPlayViewMap = new HashMap<>();
        mAnchorList = new HashSet<>();
        mAudienceList = new HashSet<>();
        //        mThrowVideoAvailableAnchorList = new HashSet<>();
        mJoinAnchorCallbackHolder = new TXCallbackHolder(this);
        mRequestPKHolder = new TXCallbackHolder(this);
    }

    private void destroy() {
        TXRoomService.getInstance().destroy();
        TRTCCloudImpl.destroySharedInstance();
    }

    private void runOnMainThread(Runnable runnable) {
        Handler handler = mMainHandler;
        if (handler != null) {
            if (handler.getLooper() == Looper.myLooper()) {
                runnable.run();
            } else {
                handler.post(runnable);
            }
        } else {
            runnable.run();
        }
    }

    private void runOnDelegateThread(Runnable runnable) {
        Handler handler = mDelegateHandler;
        if (handler != null) {
            if (handler.getLooper() == Looper.myLooper()) {
                runnable.run();
            } else {
                handler.post(runnable);
            }
        } else {
            runnable.run();
        }
    }

    @Override
    public void setDelegate(final TRTCLiveRoomDelegate delegate) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.setDelegate(delegate);
                mDelegate = delegate;
            }
        });
    }

    @Override
    public void setDelegateHandler(final Handler handler) {
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                if (handler != null) {
                    mDelegateHandler = handler;
                }
            }
        });
    }

    /**
     * ???????????????
     * ???????????????check(params)
     * |- false: ????????????
     * |- true: ?????????????????????: TXRoomService.login
     * |- callback ?????????????????????
     *
     * @param sdkAppId
     * @param userId
     * @param userSig
     * @param config
     * @param callback
     */
    @Override
    public void login(final int sdkAppId, final String userId, final String userSig, final TRTCLiveRoomDef.TRTCLiveRoomConfig config, final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "start login, sdkAppId:" + sdkAppId + " userId:" + userId + " config:" + config + " sign is empty:" + TextUtils.isEmpty(userSig));
                if (sdkAppId == 0 || TextUtils.isEmpty(userId) || TextUtils.isEmpty(userSig) || config == null) {
                    TRTCLogger.e(TAG, "start login fail. params invalid.");
                    if (callback != null) {
                        callback.onCallback(-1, "???????????????????????????");
                    }
                    return;
                }
                mSDKAppId = sdkAppId;
                mUserId = userId;
                mUserSign = userSig;
                mRoomConfig = config;
                mIsAttachTuikit = config.isAttachTuikit;
                TRTCLogger.i(TAG, "start login room service");
                TXRoomService.getInstance().login(sdkAppId, userId, userSig, mIsAttachTuikit, new TXCallback() {
                    @Override
                    public void onCallback(final int code, final String msg) {
                        TRTCLogger.i(TAG, "login room service finish, code:" + code + " msg:" + msg);
                        runOnDelegateThread(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onCallback(code, msg);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * ???????????????
     * ????????????????????????TXRoomService.logout
     * |- ?????????????????????
     *
     * @param callback
     */
    @Override
    public void logout(final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "start logout");
                mSDKAppId = 0;
                mUserId = "";
                mUserSign = "";
                TRTCLogger.i(TAG, "start logout room service");
                TXRoomService.getInstance().logout(new TXCallback() {
                    @Override
                    public void onCallback(final int code, final String msg) {
                        TRTCLogger.i(TAG, "logout room service finish, code:" + code + " msg:" + msg);
                        runOnDelegateThread(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onCallback(code, msg);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * ????????????????????????????????????
     * ???????????????????????????????????????TXRoomService.setSelfProfile
     * |- ?????????????????????
     *
     * @param userName
     * @param avatarURL
     * @param callback
     */
    @Override
    public void setSelfProfile(final String userName, final String avatarURL, final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "set profile, user name:" + userName + " avatar url:" + avatarURL);
                TXRoomService.getInstance().setSelfProfile(userName, avatarURL, new TXCallback() {
                    @Override
                    public void onCallback(final int code, final String msg) {
                        TRTCLogger.i(TAG, "set profile finish, code:" + code + " msg:" + msg);
                        runOnDelegateThread(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onCallback(code, msg);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * ?????????????????????
     * 1. ?????? TRTC ?????????this.enterTRTCRoomInner
     * |- check(params)
     * |- false: ??????????????????
     * |- true:
     * |- TXTRTCRoom.enterRoom
     * |- false: ??????????????????
     * |- true:
     * |- ????????????????????? Role.TRTC_ANCHOR ??????????????????????????????
     * |- ??????????????????
     * 2. ?????????????????????????????? IM ????????? TXRoomService.createRoom
     * |- ???????????????
     * <p>
     * ?????????
     * 1. ??????????????????????????????????????????IM??????????????????TRTC?????????
     * 2. ?????????????????????????????? RoomInfo?????????????????????????????????????????????RoomInfoChange
     *
     * @param roomId
     * @param roomParam
     * @param callback
     */
    @Override
    public void createRoom(final int roomId, final TRTCLiveRoomDef.TRTCCreateRoomParam roomParam, final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "create room, room id:" + roomId + " info:" + roomParam);
                if (roomId == 0) {
                    TRTCLogger.e(TAG, "create room fail. params invalid");
                    return;
                }

                mRoomId = String.valueOf(roomId);

                // ????????????
                mCurrentRole = Role.UNKNOWN;
                mTargetRole = Role.UNKNOWN;
                mOriginalRole = Role.TRTC_ANCHOR;
                mRoomLiveStatus = ROOM_STATUS_NONE;

                mAnchorList.clear();
                mAudienceList.clear();

                mTargetRole = Role.TRTC_ANCHOR;
                final String roomName  = (roomParam == null ? "" : roomParam.roomName);
                final String roomCover = (roomParam == null ? "" : roomParam.coverUrl);
                // ????????????
                TXRoomService.getInstance().createRoom(mRoomId, roomName, roomCover, new TXCallback() {
                    @Override
                    public void onCallback(final int code, final String msg) {
                        TRTCLogger.i(TAG, "create room in service, code:" + code + " msg:" + msg);
                        if (code == 0) {
                            enterTRTCRoomInner(mRoomId, mUserId, mUserSign, TRTCCloudDef.TRTCRoleAnchor, new TRTCLiveRoomCallback.ActionCallback() {
                                @Override
                                public void onCallback(final int code, final String msg) {
                                    if (code == 0) {
                                        runOnMainThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mAnchorList.add(mUserId);
                                                mCurrentRole = Role.TRTC_ANCHOR;
                                            }
                                        });
                                    }
                                    runOnDelegateThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (callback != null) {
                                                callback.onCallback(code, msg);
                                            }
                                        }
                                    });
                                }
                            });
                            return;
                        } else {
                            runOnDelegateThread(new Runnable() {
                                @Override
                                public void run() {
                                    TRTCLiveRoomDelegate delegate = mDelegate;
                                    if (delegate != null) {
                                        delegate.onError(code, msg);
                                    }
                                }
                            });
                        }
                        runOnDelegateThread(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onCallback(code, msg);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * ?????????????????????
     * 1. ?????? TRTC ?????????TXTRTCLiveRoom.destroyRoom
     * |- ??????????????? onError???TRTC ???????????????????????????????????????????????????????????????????????????
     * <p>
     * 2. ????????????????????????TXRoomService.destroyRoom
     * |- ?????????????????????
     * <p>
     * ?????????
     * 1. destroyRoom ???????????????????????????????????????????????????????????????????????????????????????
     *
     * @param callback
     */
    @Override
    public void destroyRoom(final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "start destroy room.");
                quitRoomPK(null);

                // TRTC ???????????????????????????????????????????????????
                TRTCLogger.i(TAG, "start exit trtc room.");
                TXTRTCLiveRoom.getInstance().exitRoom(new TXCallback() {
                    @Override
                    public void onCallback(final int code, final String msg) {
                        TRTCLogger.i(TAG, "exit trtc room finish, code:" + code + " msg:" + msg);
                        if (code != 0) {
                            runOnDelegateThread(new Runnable() {
                                @Override
                                public void run() {
                                    TRTCLiveRoomDelegate delegate = mDelegate;
                                    if (delegate != null) {
                                        delegate.onError(code, msg);
                                    }
                                }
                            });
                        }
                    }
                });

                TRTCLogger.i(TAG, "start destroy room service.");
                TXRoomService.getInstance().destroyRoom(new TXCallback() {
                    @Override
                    public void onCallback(final int code, final String msg) {
                        TRTCLogger.i(TAG, "destroy room finish, code:" + code + " msg:" + msg);
                        runOnDelegateThread(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onCallback(code, msg);
                                }
                            }
                        });
                    }
                });
                mPlayViewMap.clear();

                // ????????????
                mCurrentRole = Role.UNKNOWN;
                mTargetRole = Role.UNKNOWN;
                mOriginalRole = Role.UNKNOWN;
                mRoomLiveStatus = ROOM_STATUS_NONE;

                mAnchorList.clear();
                mRoomId = "";

                mJoinAnchorCallbackHolder.setRealCallback(null);
                mRequestPKHolder.setRealCallback(null);
            }
        });
    }

    /**
     * ???????????????
     * 1. ??????????????? TRTC ?????? CDN???check(useCDNFirst)
     * |- true: ???????????? TRTC
     * |- false: ????????? TRTC
     * |- ????????????
     * 2. ????????????????????????TXRoomService.enterRoom
     * |- ??????
     * |- check(useCDNFirst)
     * |- true: ????????????
     * |- false: ???
     * <p>
     * ?????????
     * 1. ??????????????? TRTC??????????????????TRTC ????????? IM ???????????????????????? TRTC ????????????
     * 2. CDN??????????????? TRTC ???????????????????????? IM ?????????
     * 3. ?????? IM ????????????????????????????????? roomInfo ??? ?????????????????????????????? ?????????????????? ??? ??????????????????CDN??????????????????
     *
     * @param roomId
     * @param callback
     */
    @Override
    public void enterRoom(final int roomId, final boolean useCDNFirst, final String cdnURL, final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                // ????????????
                mCurrentRole = Role.UNKNOWN;
                mTargetRole = Role.UNKNOWN;
                mOriginalRole = Role.UNKNOWN;
                mRoomLiveStatus = ROOM_STATUS_NONE;

                mAnchorList.clear();
                mRoomId = String.valueOf(roomId);
                mUseCDNFirst = useCDNFirst;
                mCDNDomain = cdnURL;
                TRTCLiveRoomDef.TRTCLiveRoomConfig config      = mRoomConfig;

                if (useCDNFirst) {
                    mOriginalRole = Role.CDN_AUDIENCE;
                } else {
                    mOriginalRole = Role.TRTC_AUDIENCE;
                }

                // ???????????? CDN ???????????? room service ?????? callbak???????????? TRTC
                TRTCLogger.i(TAG, "start enter room, room id:" + roomId + " use cdn:" + useCDNFirst);
                final boolean finalUseCDNFirst = useCDNFirst;

                if (!finalUseCDNFirst) {
                    TRTCLogger.i(TAG, "start enter trtc room.");
                    mTargetRole = Role.TRTC_AUDIENCE;
                    enterTRTCRoomInner(mRoomId, mUserId, mUserSign, TRTCCloudDef.TRTCRoleAudience, new TRTCLiveRoomCallback.ActionCallback() {
                        @Override
                        public void onCallback(final int code, final String msg) {
                            TRTCLogger.i(TAG, "trtc enter room finish, room id:" + roomId + " code:" + code + " msg:" + msg);
                            runOnMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (code == 0) {
                                        mCurrentRole = Role.TRTC_AUDIENCE;
                                    }
                                }
                            });
                            runOnDelegateThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (callback != null) {
                                        callback.onCallback(code, msg);
                                    }
                                }
                            });
                        }
                    });
                }
                TXRoomService.getInstance().enterRoom(mRoomId, new TXCallback() {
                    @Override
                    public void onCallback(final int code, final String msg) {
                        TRTCLogger.i(TAG, "enter room service finish, room id:" + roomId + " code:" + code + " msg:" + msg);
                        runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                if (finalUseCDNFirst && callback != null) {
                                    // ????????????CDN?????????????????? RoomService ?????????
                                    runOnDelegateThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onCallback(code, msg);
                                        }
                                    });
                                }
                                if (code != 0) {
                                    runOnDelegateThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TRTCLiveRoomDelegate delegate = mDelegate;
                                            if (delegate != null) {
                                                delegate.onError(code, msg);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * ????????????
     * <p>
     * 1. ???????????????????????????????????????????????????????????????????????????????????????????????????
     * 2. ??????????????????????????? TRTC ???????????????????????????TXTRTCLiveRoom.destroyRoom
     * |- ????????????????????????
     * <p>
     * 3. ????????????????????? CDN ???????????????????????????????????? TXLivePlayerRoom.stopAllPlay
     * <p>
     * 4. ????????????????????? TXRoomService.destroyRoom
     * |- ????????????
     *
     * @param callback
     */
    @Override
    public void exitRoom(final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "start exit room.");
                // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                if (mCurrentRole == Role.TRTC_ANCHOR) {
                    stopPublish(null);
                }
                mRoomId = "";
                TXTRTCLiveRoom.getInstance().exitRoom(new TXCallback() {
                    @Override
                    public void onCallback(final int code, final String msg) {
                        if (code != 0) {
                            runOnDelegateThread(new Runnable() {
                                @Override
                                public void run() {
                                    TRTCLiveRoomDelegate delegate = mDelegate;
                                    if (delegate != null) {
                                        delegate.onError(code, msg);
                                    }
                                }
                            });
                        }
                    }
                });
                TRTCLogger.i(TAG, "start stop all live player.");
                TXLivePlayerRoom.getInstance().stopAllPlay();
                TRTCLogger.i(TAG, "start exit room service.");
                TXRoomService.getInstance().exitRoom(new TXCallback() {
                    @Override
                    public void onCallback(final int code, final String msg) {
                        TRTCLogger.i(TAG, "exit room finish, code:" + code + " msg:" + msg);
                        runOnDelegateThread(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onCallback(code, msg);
                                }
                            }
                        });
                    }
                });
                mPlayViewMap.clear();
                mAnchorList.clear();
                mTargetRole = Role.UNKNOWN;
                mOriginalRole = Role.UNKNOWN;
                mCurrentRole = Role.UNKNOWN;
                mJoinAnchorCallbackHolder.setRealCallback(null);
                mRequestPKHolder.setRealCallback(null);
            }
        });
    }

    /**
     * ??????????????????
     * ??????IM???????????????????????????????????????????????????id?????????
     */
    @Override
    public void getRoomInfos(final List<Integer> roomIdList, final TRTCLiveRoomCallback.RoomInfoCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                final List<TRTCLiveRoomDef.TRTCLiveRoomInfo> trtcLiveRoomInfoList = new ArrayList<>();
                TRTCLogger.i(TAG, "start getRoomInfos: " + roomIdList);
                List<String> strings = new ArrayList<>();
                for (Integer id : roomIdList) {
                    strings.add(String.valueOf(id));
                }
                TXRoomService.getInstance().getRoomInfos(strings, new TXRoomInfoListCallback() {
                    @Override
                    public void onCallback(int code, String msg, List<TXRoomInfo> list) {
                        if (code == 0) {
                            for (TXRoomInfo info : list) {
                                TRTCLogger.i(TAG, info.toString());
                                // ????????????????????????????????????ownerId?????????
                                if (TextUtils.isEmpty(info.ownerId)) {
                                    continue;
                                }
                                TRTCLiveRoomDef.TRTCLiveRoomInfo liveRoomInfo = new TRTCLiveRoomDef.TRTCLiveRoomInfo();
                                int                              translateRoomId;
                                try {
                                    translateRoomId = Integer.valueOf(info.roomId);
                                } catch (NumberFormatException e) {
                                    continue;
                                }
                                liveRoomInfo.roomId = translateRoomId;
                                liveRoomInfo.memberCount = info.memberCount;
                                liveRoomInfo.roomName = info.roomName;
                                liveRoomInfo.ownerId = info.ownerId;
                                liveRoomInfo.coverUrl = info.coverUrl;
                                liveRoomInfo.streamUrl = info.streamUrl;
                                liveRoomInfo.ownerName = info.ownerName;
                                trtcLiveRoomInfoList.add(liveRoomInfo);
                            }
                        }
                        if (callback != null) {
                            callback.onCallback(code, msg, trtcLiveRoomInfoList);
                        }
                    }
                });
            }
        });
    }

    /**
     * ??????????????????
     * 1. TRTC????????????????????????/????????????????????????????????????????????????AnchorList
     * 2. ????????????????????????????????????????????????AnchorList???IM????????????
     * 3. ????????????
     *
     * @param callback ????????????????????????
     */
    @Override
    public void getAnchorList(final TRTCLiveRoomCallback.UserListCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                List<String> anchorList = new ArrayList<>(mAnchorList);
                if (anchorList.size() > 0) {
                    TRTCLogger.i(TAG, "start getAnchorList");
                    TXRoomService.getInstance().getUserInfo(anchorList, new TXUserListCallback() {
                        @Override
                        public void onCallback(int code, String msg, List<TXUserInfo> list) {
                            if (code == 0) {
                                List<TRTCLiveRoomDef.TRTCLiveUserInfo> trtcLiveUserInfoList = new ArrayList<>();
                                for (TXUserInfo info : list) {
                                    TRTCLogger.i(TAG, info.toString());
                                    TRTCLiveRoomDef.TRTCLiveUserInfo userInfo = new TRTCLiveRoomDef.TRTCLiveUserInfo();
                                    userInfo.userId = info.userId;
                                    userInfo.userName = info.userName;
                                    userInfo.avatarUrl = info.avatarURL;
                                    trtcLiveUserInfoList.add(userInfo);
                                }
                                callback.onCallback(code, msg, trtcLiveUserInfoList);
                            } else {
                                callback.onCallback(code, msg, new ArrayList<TRTCLiveRoomDef.TRTCLiveUserInfo>());
                            }
                            TRTCLogger.i(TAG, "onCallback: " + code + " " + msg);
                        }
                    });
                } else {
                    if (callback != null) {
                        callback.onCallback(0, "??????????????????", new ArrayList<TRTCLiveRoomDef.TRTCLiveUserInfo>());
                    }
                }
            }
        });
    }

    /**
     * ??????????????????
     * 1. TRTC????????????????????????/??????????????????????????????????????????????????????AnchorList
     * 2. ???IM?????????????????????????????????????????????????????????
     * 3. ????????????
     *
     * @param callback ????????????????????????
     */
    @Override
    public void getAudienceList(final TRTCLiveRoomCallback.UserListCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TXRoomService.getInstance().getAudienceList(new TXUserListCallback() {
                    @Override
                    public void onCallback(final int code, final String msg, final List<TXUserInfo> list) {
                        TRTCLogger.i(TAG, "get audience list finish, code:" + code + " msg:" + msg + " list:" + (list != null ? list.size() : 0));
                        runOnDelegateThread(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    List<TRTCLiveRoomDef.TRTCLiveUserInfo> userList = new ArrayList<>();
                                    if (list != null) {
                                        for (TXUserInfo info : list) {
                                            // ???????????????
                                            if (mAnchorList.contains(info.userId)) {
                                                continue;
                                            }
                                            TRTCLiveRoomDef.TRTCLiveUserInfo trtcUserInfo = new TRTCLiveRoomDef.TRTCLiveUserInfo();
                                            trtcUserInfo.userId = info.userId;
                                            trtcUserInfo.avatarUrl = info.avatarURL;
                                            trtcUserInfo.userName = info.userName;
                                            userList.add(trtcUserInfo);
                                            TRTCLogger.i(TAG, "info:" + info);
                                        }
                                    }
                                    callback.onCallback(code, msg, userList);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * ????????? TRTC ???????????????
     *
     * @param isFront  YES?????????????????????NO?????????????????????
     * @param view     ???????????????????????????
     * @param callback ????????????
     */
    @Override
    public void startCameraPreview(final boolean isFront, final TXCloudVideoView view, final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "start camera preview???");
                TXTRTCLiveRoom.getInstance().startCameraPreview(isFront, view, new TXCallback() {
                    @Override
                    public void onCallback(final int code, final String msg) {
                        TRTCLogger.i(TAG, "start camera preview finish, code:" + code + " msg:" + msg);
                        runOnDelegateThread(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onCallback(code, msg);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void stopCameraPreview() {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "stop camera preview.");
                TXTRTCLiveRoom.getInstance().stopCameraPreview();
            }
        });
    }

    /**
     * ???????????????
     * <p>
     * 1. ??????????????????????????? TRTC ?????????this.isTRTCMode
     * |- true: ??????????????????
     * |            |- ????????????
     * |- false:
     * |- ?????? TRTC ??????
     * |- false???????????????
     * |- true???????????????
     * |- ????????????
     * <p>
     * 2. ?????? stream id ??????????????????TXRoomService.updateStreamId
     * |- ??????????????????
     *
     * @param streamId
     * @param callback
     */
    @Override
    public void startPublish(final String streamId, final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                String tempStreamId = streamId;
                if (TextUtils.isEmpty(tempStreamId)) {
                    tempStreamId = mSDKAppId + "_" + mRoomId + "_" + mUserId + "_main";
                    //tempStreamId = mSDKAppId + "_" + mRoomId + "_" + 707134 + "_main";
                }
                final String finalStreamId = tempStreamId;

                if (!isTRTCMode()) {
                    if (TextUtils.isEmpty(mRoomId)) {
                        TRTCLogger.e(TAG, "start publish error, room id is empty.");
                        if (callback != null) {
                            runOnDelegateThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onCallback(-1, "????????????, room id ??????");
                                }
                            });
                        }
                        return;
                    }
                    mTargetRole = Role.TRTC_ANCHOR;
                    // ???????????????????????? TRTC ???
                    TRTCLogger.i(TAG, "enter trtc room before start publish.");
                    enterTRTCRoomInner(mRoomId, mUserId, mUserSign, TRTCCloudDef.TRTCRoleAudience, new TRTCLiveRoomCallback.ActionCallback() {
                        @Override
                        public void onCallback(final int code, final String msg) {
                            TRTCLogger.i(TAG, "enter trtc room finish, code:" + code + " msg:" + msg);
                            mCurrentRole = Role.TRTC_ANCHOR;
                            if (code == 0) {
                                // ?????? TRTC ????????????????????????
                                startPublishInner(finalStreamId, callback);
                                if (mOriginalRole == Role.CDN_AUDIENCE) {
                                    changeToTRTCPlay();
                                }
                            } else {
                                runOnDelegateThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onCallback(code, msg);
                                    }
                                });
                            }
                        }
                    });
                } else {
                    startPublishInner(finalStreamId, new TRTCLiveRoomCallback.ActionCallback() {
                        @Override
                        public void onCallback(final int code, final String msg) {
                            runOnDelegateThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onCallback(code, msg);
                                }
                            });
                        }
                    });
                }
                TRTCLogger.i(TAG, "update room service stream id:" + finalStreamId);
                TXRoomService.getInstance().updateStreamId(finalStreamId, new TXCallback() {
                    @Override
                    public void onCallback(final int code, final String msg) {
                        TRTCLogger.i(TAG, "room service start publish, code:" + code + " msg:" + msg);
                        if (code != 0) {
                            runOnDelegateThread(new Runnable() {
                                @Override
                                public void run() {
                                    TRTCLiveRoomDelegate delegate = mDelegate;
                                    if (delegate != null) {
                                        delegate.onError(code, msg);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    /**
     * ????????????
     * 1. ?????? TRTC ?????????TXTRTCLiveRoom.stopPublish???????????????????????????trtc?????????????????????trtc???????????????????????????(???????????????)
     * |- check(mOriginalRole == Role.CDN_AUDIENCE)
     * |- true:
     * |    |- 1. ?????? TRTC ??????
     * |    |- 2. ????????? CDN ??????
     * |- false:
     * 2. ?????? stream id ??????????????????TXRoomService.updateStreamId
     * |- ??????????????????
     * 3. check(mOriginalRole == ??????) ????????????
     *
     * @param callback
     */
    @Override
    public void stopPublish(final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "stop publish");
                TXTRTCLiveRoom.getInstance().stopPublish(new TXCallback() {
                    @Override
                    public void onCallback(final int code, final String msg) {
                        TRTCLogger.i(TAG, "stop publish finish, code:" + code + " msg:" + msg);
                        if (mOriginalRole == Role.CDN_AUDIENCE) {
                            mTargetRole = Role.CDN_AUDIENCE;

                            TRTCLogger.i(TAG, "start exit trtc room.");
                            TXTRTCLiveRoom.getInstance().exitRoom(new TXCallback() {
                                @Override
                                public void onCallback(int code, String msg) {
                                    TRTCLogger.i(TAG, "exit trtc room finish, code:" + code + " msg:" + msg);
                                    runOnMainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mCurrentRole = Role.CDN_AUDIENCE;
                                            changeToCDNPlay();
                                        }
                                    });
                                }
                            });
                        } else {
                            runOnDelegateThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (callback != null) {
                                        callback.onCallback(code, msg);
                                    }
                                }
                            });
                        }
                    }
                });

                TRTCLogger.i(TAG, "start update stream id");
                TXRoomService.getInstance().updateStreamId("", new TXCallback() {
                    @Override
                    public void onCallback(final int code, final String msg) {
                        TRTCLogger.i(TAG, "room service update stream id finish, code:" + code + " msg:" + msg);
                        if (code != 0) {
                            runOnDelegateThread(new Runnable() {
                                @Override
                                public void run() {
                                    TRTCLiveRoomDelegate delegate = mDelegate;
                                    if (delegate != null) {
                                        delegate.onError(code, msg);
                                    }
                                }
                            });
                        }
                    }
                });
                if (mOriginalRole == Role.TRTC_AUDIENCE || mOriginalRole == Role.CDN_AUDIENCE) {
                    TXRoomService.getInstance().quitLinkMic();
                }
            }
        });
    }

    @Override
    public void startPlay(final String userId, final TXCloudVideoView view, final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mPlayViewMap.put(userId, view);
                if (isTRTCMode()) {
                    TXTRTCLiveRoom.getInstance().startPlay(userId, view, new TXCallback() {
                        @Override
                        public void onCallback(final int code, final String msg) {
                            TRTCLogger.i(TAG, "start trtc play finish, code:" + code + " msg:" + msg);
                            runOnDelegateThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (callback != null) {
                                        callback.onCallback(code, msg);
                                    }
                                }
                            });
                        }
                    });
                } else {
                    String playURL = getPlayURL(userId);
                    if (!TextUtils.isEmpty(playURL)) {
                        TRTCLogger.i(TAG, "start cdn play, url:" + playURL);
                        TXLivePlayerRoom.getInstance().startPlay(playURL, view, new TXCallback() {
                            @Override
                            public void onCallback(final int code, final String msg) {
                                TRTCLogger.i(TAG, "start cdn play finish, code:" + code + " msg:" + msg);
                                runOnDelegateThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (callback != null) {
                                            callback.onCallback(code, msg);
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        TRTCLogger.e(TAG, "start cdn play error, can't find stream id by user id:" + userId);
                        runOnDelegateThread(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onCallback(-1, "??????CDN????????????????????????????????????ID");
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void changeToCDNPlay() {
        TRTCLogger.i(TAG, "switch trtc to cdn play");
        // ??????????????????????????????????????????View?????????
        // TRTC ????????? CDN ??????

        // 1. ???????????? TRTC ??????
        TXTRTCLiveRoom.getInstance().stopAllPlay();
        // 2. ??????????????? userId
        final String ownerId = TXRoomService.getInstance().getOwnerUserId();
        if (!TextUtils.isEmpty(ownerId)) {
            // 3. ???????????????????????????????????????????????????
            Set<String> leaveAnchorSet = new HashSet<>();

            Iterator<String> anchorIterator = mAnchorList.iterator();
            while (anchorIterator.hasNext()) {
                String userId = anchorIterator.next();
                if (!TextUtils.isEmpty(userId) && !userId.equals(ownerId)) {
                    // ?????????????????????????????????
                    leaveAnchorSet.add(userId);
                    anchorIterator.remove();
                    // ????????? View
                    mPlayViewMap.remove(userId);
                }
            }

            TRTCLiveRoomDelegate delegate = mDelegate;
            if (delegate != null) {
                for (String userId : leaveAnchorSet) {
                    delegate.onAnchorExit(userId);
                }
            }

            final String ownerPlayURL = getPlayURL(ownerId);
            if (!TextUtils.isEmpty(ownerPlayURL)) {
                TXCloudVideoView view = mPlayViewMap.get(ownerId);
                // 4. ?????? CDN ??????
                TXLivePlayerRoom.getInstance().startPlay(ownerPlayURL, view, null);
            } else {
                TRTCLogger.e(TAG, "change to play cdn fail, can't get owner play url, owner id:" + ownerId);
            }
        } else {
            TRTCLogger.e(TAG, "change to play cdn fail, can't get owner user id.");
        }
    }


    private void changeToTRTCPlay() {
        TRTCLogger.i(TAG, "switch cdn to trtc play");
        // ??????????????? CDN ???????????????????????????????????????????????? TRTC
        TXLivePlayerRoom.getInstance().stopAllPlay();
        for (String userId : mAnchorList) {
            TXTRTCLiveRoom.getInstance().startPlay(userId, mPlayViewMap.get(userId), null);
        }
    }

    /**
     * ????????????
     * <p>
     * 1. ?????? View ????????????????????? CDN ??? TRTC ??????????????????????????????TRTC ??????????????????????????????????????????
     * <p>
     * 2. ?????????????????? TRTC ???????????????check(isTRTCMode)
     * |- true: ???????????? TRTC ?????????TXTRTCLiveRoom.startPlay
     * |- false???
     * |- ?????? user id ????????????????????? stream id: TXRoomService.exchangeStreamId
     * |- ?????? stream id ?????? config.domain ???????????? url
     * |- ??????????????????????????????TXLivePlayerRoom.startPlay
     *
     * @param userId
     * @param view
     * @param callback
     */
    @Override
    public void startPlay(final String userId, final String flvUrl, final TXCloudVideoView view, final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                    if (!TextUtils.isEmpty(flvUrl)) {
                        TRTCLogger.i(TAG, "start cdn play, url:" + flvUrl);
                        TXLivePlayerRoom.getInstance().startPlay(flvUrl, view, new TXCallback() {
                            @Override
                            public void onCallback(final int code, final String msg) {
                                TRTCLogger.i(TAG, "start cdn play finish, code:" + code + " msg:" + msg);
                                runOnDelegateThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (callback != null) {
                                            callback.onCallback(code, msg);
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        TRTCLogger.e(TAG, "start cdn play error, can't find stream id by user id:" + userId);
                        runOnDelegateThread(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onCallback(-1, "??????CDN????????????????????????????????????ID");
                                }
                            }
                        });
                    }

            }
        });
    }

    /**
     * ???????????????
     * 1. ???????????????????????? View
     * 2. ?????????????????? TRTC ???????????????check(isTRTCMode)
     * |- true: ??????????????????
     * |- false:
     * |- ?????? user id ????????????????????? stream id: TXRoomService.exchangeStreamId
     * |- ?????? stream id ?????? config.domain ???????????? url
     * |- ????????????????????????????????????TXLivePlayerRoom.stopPlay
     *
     * @param userId
     * @param callback
     */
    @Override
    public void stopPlay(final String userId, final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mPlayViewMap.remove(userId);
                if (isTRTCMode()) {
                    TXTRTCLiveRoom.getInstance().stopPlay(userId, new TXCallback() {
                        @Override
                        public void onCallback(final int code, final String msg) {
                            TRTCLogger.i(TAG, "stop trtc play finish, code:" + code + " msg:" + msg);
                            runOnDelegateThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (callback != null) {
                                        callback.onCallback(code, msg);
                                    }
                                }
                            });
                        }
                    });
                } else {
                    String playURL = getPlayURL(userId);
                    if (!TextUtils.isEmpty(playURL)) {
                        TRTCLogger.i(TAG, "stop play, url:" + playURL);
                        TXLivePlayerRoom.getInstance().stopPlay(playURL, new TXCallback() {
                            @Override
                            public void onCallback(final int code, final String msg) {
                                TRTCLogger.i(TAG, "stop cdn play finish, code:" + code + " msg:" + msg);
                                runOnDelegateThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (callback != null) {
                                            callback.onCallback(code, msg);
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        TRTCLogger.e(TAG, "stop cdn play error, can't find stream id by user id:" + userId);
                        runOnDelegateThread(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onCallback(-1, "??????????????????????????????????????????ID");
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * ???????????????
     * 1. ????????????????????????????????????check(isLogin)
     * |- true: ?????????????????????TXRoomService.requestJoinAnchor
     * |                                           |- ????????????
     * |- false: ????????????
     * ?????????????????????????????????15s??????????????????????????????-2
     *
     * @param reason
     * @param timeout
     * @param callback
     */
    @Override
    public void requestJoinAnchor(final String reason, final int timeout, final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (mRoomLiveStatus == ROOM_STATUS_PK) {
                    //??????PK???
                    runOnDelegateThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onCallback(CODE_ERROR, "??????PK???");
                            }
                        }
                    });
                    return;
                }
                if (TXRoomService.getInstance().isLogin()) {
                    TRTCLogger.i(TAG, "start join anchor.");
                    mJoinAnchorCallbackHolder.setRealCallback(callback);
                    TXRoomService.getInstance().requestJoinAnchor(reason, timeout, mJoinAnchorCallbackHolder);
                } else {
                    TRTCLogger.e(TAG, "request join anchor fail, not login yet.");
                    runOnDelegateThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onCallback(CODE_ERROR, "?????????????????????IM?????????");
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void cancelRequestJoinAnchor(final String reason, TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "start cancel request anchor.");
                TXRoomService.getInstance().cancelRequestJoinAnchor(reason, new TXCallback() {
                    @Override
                    public void onCallback(int code, String msg) {

                    }
                });
            }
        });
    }

    /**
     * ??????????????????
     * 1. ????????????????????????????????????check(isLogin)
     * |- true: ?????????????????????????????????TXRoomService.responseJoinAnchor
     * |- false: ??????
     * ????????????????????????????????????10s???????????????????????? responseJoinAnchor????????????????????????
     * ????????????????????? ?????????3s ??????????????????????????????????????????
     *
     * @param userId
     * @param agree
     * @param reason
     */
    @Override
    public void responseJoinAnchor(final String userId, final boolean agree, final String reason) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (TXRoomService.getInstance().isLogin()) {
                    TRTCLogger.i(TAG, "response join anchor.");
                    TXRoomService.getInstance().responseJoinAnchor(userId, agree, reason);
                } else {
                    TRTCLogger.e(TAG, "response join anchor fail. not login yet.");
                }
            }
        });
    }

    /**
     * ?????????????????????
     * 1. ????????????????????????????????????check(isLogin)
     * |- true: ???????????????????????????????????????TXRoomService.kickJoinAnchor
     * |                                                   |- ????????????
     * |- false: ????????????
     * ????????????????????????stopPublish????????????
     *
     * @param userId
     * @param callback
     */
    @Override
    public void kickoutJoinAnchor(final String userId, final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (TXRoomService.getInstance().isLogin()) {
                    TRTCLogger.i(TAG, "kick out join anchor.");
                    TXRoomService.getInstance().kickoutJoinAnchor(userId, new TXCallback() {
                        @Override
                        public void onCallback(final int code, final String msg) {
                            TRTCLogger.i(TAG, "kick out finish, code:" + code + " msg:" + msg);
                            runOnDelegateThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (callback != null) {
                                        callback.onCallback(code, msg);
                                    }
                                }
                            });
                        }
                    });
                } else {
                    TRTCLogger.e(TAG, "kick out fail. not login yet.");
                    runOnDelegateThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onCallback(CODE_ERROR, "???????????????IM?????????");
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * ?????? PK ??????
     * <p>
     * 1. ????????????????????????????????????check(isLogin)
     * |- true: ?????????????????????????????? PK ?????????TXRoomService.requestRoomPK
     * |                                                       |- ????????????
     * |- false: ??????????????????
     * ????????????PK?????????15s??????????????????????????????-2
     *
     * @param roomId
     * @param userId
     * @param callback
     */
    @Override
    public void requestRoomPK(int roomId, String userId, int timeout, final TRTCLiveRoomCallback.ActionCallback callback) {
        if (TXRoomService.getInstance().isLogin()) {
            TRTCLogger.i(TAG, "request room pk.");
            mRequestPKHolder.setRealCallback(callback);
            TXRoomService.getInstance().requestRoomPK(String.valueOf(roomId), userId, timeout, mRequestPKHolder);
        } else {
            TRTCLogger.e(TAG, "request room pk fail. not login yet.");
            runOnDelegateThread(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onCallback(CODE_ERROR, "??????PK?????????IM?????????");
                    }
                }
            });
        }
    }

    @Override
    public void cancelRequestRoomPK(final int roomId, final String userId, TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "start cancel request pk.");
                TXRoomService.getInstance().cancelRequestRoomPK(String.valueOf(roomId), userId, new TXCallback() {
                    @Override
                    public void onCallback(int code, String msg) {

                    }
                });
            }
        });
    }

    /**
     * ?????? PK ??????
     * 1. ????????????????????????????????????check(isLogin)
     * |- true: ???????????????????????? PK ?????????TXRoomService.responseRoomPK
     * |- false: ??????
     * ???????????????PK???????????????10s???????????????????????? responseRoomPK????????????????????????
     * ????????????PK??? ?????????3s ??????????????????????????????????????????
     *
     * @param userId
     * @param agree
     * @param reason
     */
    @Override
    public void responseRoomPK(final String userId, final boolean agree, final String reason) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (TXRoomService.getInstance().isLogin()) {
                    TRTCLogger.i(TAG, "response pk.");
                    TXRoomService.getInstance().responseRoomPK(userId, agree, reason);
                } else {
                    TRTCLogger.e(TAG, "response pk fail. not login yet.");
                }
            }
        });
    }

    /**
     * ?????? PK
     * 1. ????????????????????????????????????check(isLogin)
     * |- true: ?????????????????????????????? PK ?????????TXRoomService.quitRoom
     * |- false: ????????????
     *
     * @param callback
     */
    @Override
    public void quitRoomPK(final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                // ?????????????????????
                if (TXRoomService.getInstance().isLogin()) {
                    TRTCLogger.i(TAG, "quit pk.");
                    TXRoomService.getInstance().quitRoomPK(new TXCallback() {
                        @Override
                        public void onCallback(final int code, final String msg) {
                            TRTCLogger.i(TAG, "quit pk finish, code:" + code + " msg:" + msg);
                            runOnDelegateThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (callback != null) {
                                        callback.onCallback(code, msg);
                                    }
                                }
                            });
                        }
                    });
                } else {
                    TRTCLogger.i(TAG, "quit pk fail.not login yet.");
                    runOnDelegateThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onCallback(CODE_ERROR, "??????PK?????????IM?????????");
                            }
                        }
                    });
                }
                //?????????
                TXTRTCLiveRoom.getInstance().stopPK();
            }
        });
    }

    /**
     * ???????????????
     * <p>
     * ???????????? TRTC ?????????TXTRTCLiveRoom.switchCamera
     */
    @Override
    public void switchCamera() {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "switch camera.");
                TXTRTCLiveRoom.getInstance().switchCamera();
            }
        });
    }

    /**
     * ????????????
     * <p>
     * ???????????? TRTC ?????????TXTRTCLiveRoom.setMirror
     */
    @Override
    public void setMirror(final boolean isMirror) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "set mirror.");
                TXTRTCLiveRoom.getInstance().setMirror(isMirror);
            }
        });
    }

    /**
     * ????????????
     * <p>
     * ???????????? TRTC ?????????TXTRTCLiveRoom.muteLocalAudio
     *
     * @param mute
     */
    @Override
    public void muteLocalAudio(final boolean mute) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "mute local audio, mute:" + mute);
                TXTRTCLiveRoom.getInstance().muteLocalAudio(mute);
            }
        });
    }

    /**
     * ????????????
     * 1. ?????????????????? TRTC ???????????????check(isTRTCMode)
     * |- true: ???????????????TXTRTCLiveRoom.muteRemoteAudio
     * |- false:
     * |- ?????? user id ????????????????????? stream id: TXRoomService.exchangeStreamId
     * |- ?????? stream id ?????? config.domain ???????????? url
     * |- ????????????????????????????????????TXLivePlayerRoom.muteRemoteAudio
     *
     * @param userId
     * @param mute
     */
    @Override
    public void muteRemoteAudio(final String userId, final boolean mute) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (isTRTCMode()) {
                    TRTCLogger.i(TAG, "mute trtc audio, user id:" + userId);
                    TXTRTCLiveRoom.getInstance().muteRemoteAudio(userId, mute);
                } else {
                    TRTCLogger.i(TAG, "mute cnd audio, user id:" + userId);
                    String playURL = getPlayURL(userId);
                    if (!TextUtils.isEmpty(playURL)) {
                        // ??? CDN
                        TRTCLogger.i(TAG, "mute cdn audio success, url:" + playURL);
                        TXLivePlayerRoom.getInstance().muteRemoteAudio(playURL, mute);
                    } else {
                        TRTCLogger.e(TAG, "mute cdn remote audio fail, exchange stream id fail. user id:" + userId);
                    }
                }
            }
        });
    }

    /**
     * ??????????????????
     * 1. ?????????????????? TRTC ???????????????check(isTRTCMode)
     * |- true??????????????????TXTRTCLiveRoom.muteAllRemoteAudio
     * |- false??????????????????TXLivePlayerRoom.muteAllRemoteAudio
     *
     * @param mute
     */
    @Override
    public void muteAllRemoteAudio(final boolean mute) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (isTRTCMode()) {
                    TRTCLogger.i(TAG, "mute all trtc remote audio success, mute:" + mute);
                    TXTRTCLiveRoom.getInstance().muteAllRemoteAudio(mute);
                } else {
                    TRTCLogger.i(TAG, "mute all cdn audio success, mute:" + mute);
                    TXLivePlayerRoom.getInstance().muteAllRemoteAudio(mute);
                }
            }
        });
    }

    /**
     * ??????????????????
     * 1. ???????????????????????????????????????TXRoomService.sendRoomTextMsg
     *
     * @param msg
     * @param callback
     */
    @Override
    public void sendRoomTextMsg(final String msg, final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TXRoomService.getInstance().sendRoomTextMsg(msg, new TXCallback() {
                    @Override
                    public void onCallback(int code, String msg) {
                        if (callback != null) {
                            callback.onCallback(code, msg);
                        }
                    }
                });
            }
        });
    }

    /**
     * ?????????????????????
     * 1. ??????????????????????????????????????????TXRoomService.sendRoomCustomMsg
     *
     * @param cmd
     * @param message
     * @param callback
     */
    @Override
    public void sendRoomCustomMsg(final String cmd, final String message, final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TXRoomService.getInstance().sendRoomCustomMsg(cmd, message, new TXCallback() {
                    @Override
                    public void onCallback(int code, String msg) {
                        if (callback != null) {
                            callback.onCallback(code, msg);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void showVideoDebugLog(final boolean isShow) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TXLivePlayerRoom.getInstance().showVideoDebugLog(isShow);
                TXTRTCLiveRoom.getInstance().showVideoDebugLog(isShow);
            }
        });
    }

    @Override
    public TXAudioEffectManager getAudioEffectManager() {
        return TXTRTCLiveRoom.getInstance().getAudioEffectManager();
    }

    @Override
    public void setAudioQuality(final int quality) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TXTRTCLiveRoom.getInstance().setAudioQuality(quality);
            }
        });
    }

    @Override
    public TXBeautyManager getBeautyManager() {
        return TXTRTCLiveRoom.getInstance().getTXBeautyManager();
    }

    private void enterTRTCRoomInner(final String roomId, final String userId, final String userSign, final int role, final TRTCLiveRoomCallback.ActionCallback callback) {
        // ?????? TRTC ??????
        TRTCLogger.i(TAG, "enter trtc room.");
        mTargetRole = Role.TRTC_ANCHOR;
        TXTRTCLiveRoom.getInstance().enterRoom(mSDKAppId, roomId, userId, userSign, role, new TXCallback() {
            @Override
            public void onCallback(final int code, final String msg) {
                TRTCLogger.i(TAG, "enter trtc room finish, code:" + code + " msg:" + msg);
                runOnDelegateThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onCallback(code, msg);
                        }
                    }
                });
            }
        });
    }

    private void startPublishInner(String streamId, final TRTCLiveRoomCallback.ActionCallback callback) {
        TRTCLogger.i(TAG, "start publish stream id:" + streamId);
        // ????????????????????????????????????
        TXTRTCLiveRoom.getInstance().startPublish(streamId, new TXCallback() {
            @Override
            public void onCallback(final int code, final String msg) {
                TRTCLogger.i(TAG, "start publish stream finish, code:" + code + " msg:" + msg);
                runOnDelegateThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onCallback(code, msg);
                        }
                    }
                });
            }
        });
    }

    private boolean isTRTCMode() {
        return mCurrentRole == Role.TRTC_ANCHOR || mCurrentRole == Role.TRTC_AUDIENCE || mTargetRole == Role.TRTC_ANCHOR || mTargetRole == Role.TRTC_AUDIENCE;
    }

    private void updateMixConfig() {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TRTCLogger.i(TAG, "start mix stream:" + mAnchorList.size() + " status:" + mRoomLiveStatus);
                if (TXRoomService.getInstance().isOwner()) {
                    if (mAnchorList.size() > 0) {
                        // ??????????????????????????????????????????????????????
                        List<TXTRTCMixUser> needToMixUserList = new ArrayList<>();
                        boolean             isPKing           = TXRoomService.getInstance().isPKing();
                        if (isPKing) {
                            // ????????? PK ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                            if (mAnchorList.size() == PK_ANCHOR_NUMS) {
                                String userId = TXRoomService.getInstance().getPKUserId();
                                String roomId = TXRoomService.getInstance().getPKRoomId();
                                if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(userId)) {
                                    TXTRTCMixUser user = new TXTRTCMixUser();
                                    user.userId = userId;
                                    user.roomId = roomId;
                                    needToMixUserList.add(user);
                                } else {
                                    TRTCLogger.e(TAG, "set pk mix config fail, pk user id:" + userId + " pk room id:" + roomId);
                                }
                            } else {
                                TRTCLogger.e(TAG, "set pk mix config fail, available uer size:s" + mAnchorList.size());
                            }
                        } else {
                            // ?????? PK ???????????????????????????????????????
                            for (String userId : mAnchorList) {
                                //??????????????????
                                if (userId.equals(mUserId)) {
                                    continue;
                                }
                                TXTRTCMixUser user = new TXTRTCMixUser();
                                user.roomId = null;
                                user.userId = userId;
                                needToMixUserList.add(user);
                            }
                        }
                        if (needToMixUserList.size() > 0) {
                            // ?????????????????? 0???????????????
                            TXTRTCLiveRoom.getInstance().setMixConfig(needToMixUserList, isPKing);
                        } else {
                            // ????????????????????????????????????
                            TXTRTCLiveRoom.getInstance().setMixConfig(null, false);
                        }
                    } else {
                        // ?????????????????????????????????
                        TXTRTCLiveRoom.getInstance().setMixConfig(null, false);
                    }
                }
            }
        });
    }

    private String getPlayURL(String userId) {
        String streamId = TXRoomService.getInstance().exchangeStreamId(userId);
        return generateCdnPlayURL(mCDNDomain, streamId);
    }

    private String generateCdnPlayURL(String cdnDomain, String streamId) {
        if (TextUtils.isEmpty(cdnDomain)) {
            return "";
        } else if (cdnDomain.startsWith("http") && cdnDomain.endsWith("flv")) {
            return cdnDomain;
        } else if (TextUtils.isEmpty(streamId)) {
            return "";
        }
        String prefix = "http://";
        if (cdnDomain.startsWith("https://")) {
            prefix = "https://";
            cdnDomain = cdnDomain.replace("https://", "");
        } else {
            cdnDomain = cdnDomain.replace("http://", "");
        }
        if (cdnDomain.startsWith("/") && cdnDomain.length() > 1) {
            cdnDomain = cdnDomain.substring(1);
        }
        String[] domainElements = cdnDomain.split("/");
        StringBuilder path = new StringBuilder("/");
        if (domainElements.length > 2) {
            for (int i = 1; i < domainElements.length; i++) {
                path.append(domainElements[i]).append("/");
            }
        } else {
            path.append("live/");
        }
        cdnDomain = domainElements[0];
        return String.format("%s%s%s%s.flv", prefix, cdnDomain, path.toString(), streamId);
    }

    @Override
    public void onRoomInfoChange(final TXRoomInfo roomInfo) {
        TRTCLogger.i(TAG, "onRoomInfoChange:" + roomInfo);
        //???????????????????????????????????????
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(roomInfo.roomId)) {
                    return;
                }
                mLiveRoomInfo.ownerId = roomInfo.ownerId;
                mLiveRoomInfo.coverUrl = roomInfo.coverUrl;
                mLiveRoomInfo.roomId = Integer.parseInt(roomInfo.roomId);
                mLiveRoomInfo.roomName = roomInfo.roomName;
                mLiveRoomInfo.ownerName = roomInfo.ownerName;
                mLiveRoomInfo.streamUrl = roomInfo.streamUrl;
                mLiveRoomInfo.roomStatus = roomInfo.roomStatus;
                mLiveRoomInfo.memberCount = roomInfo.memberCount;

                mRoomLiveStatus = roomInfo.roomStatus;
                updateMixConfig();
                runOnDelegateThread(new Runnable() {
                    @Override
                    public void run() {
                        TRTCLiveRoomDelegate delegate = mDelegate;
                        if (delegate != null) {
                            delegate.onRoomInfoChange(mLiveRoomInfo);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRoomDestroy(final String roomId) {
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                TRTCLiveRoomDelegate delegate = mDelegate;
                if (delegate != null) {
                    delegate.onRoomDestroy(roomId);
                }
            }
        });
    }

    @Override
    public void onTRTCAnchorEnter(final String userId) {
        TRTCLogger.i(TAG, "onTRTCAnchorEnter:" + userId);
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                TXRoomService.getInstance().handleAnchorEnter(userId);
                if (mAnchorList.add(userId)) {
                    handleAnchorEnter(userId);
                } else {
                    //?????????????????????
                    TRTCLogger.e(TAG, "trtc anchor enter, but already exit:" + userId);
                }
            }
        });
    }

    private void handleAnchorEnter(final String userId) {
        updateMixConfig();
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                TRTCLiveRoomDelegate delegate = mDelegate;
                if (delegate != null) {
                    delegate.onAnchorEnter(userId);
                }
            }
        });
    }

    @Override
    public void onTRTCAnchorExit(final String userId) {
        TRTCLogger.i(TAG, "onTRTCAnchorExit:" + userId);
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                //??????????????????????????????????????????PK/???????????????? ??????????????????????????????~
                TXRoomService.getInstance().handleAnchorExit(userId);
                if (mAnchorList.contains(userId)) {
                    mAnchorList.remove(userId);
                    updateMixConfig();
                    if (TXRoomService.getInstance().isOwner()
                            && mAnchorList.size() == 1) {
                        //????????????????????????????????????TXRoom
                        TXRoomService.getInstance().resetRoomStatus();
                    }
                    runOnDelegateThread(new Runnable() {
                        @Override
                        public void run() {
                            TRTCLiveRoomDelegate delegate = mDelegate;
                            if (delegate != null) {
                                delegate.onAnchorExit(userId);
                            }
                        }
                    });
                } else {
                    TRTCLogger.e(TAG, "trtc anchor exit, but never throw yet, maybe something error.");
                }
            }
        });
    }

    @Override
    public void onTRTCStreamAvailable(final String userId) {
    }

    @Override
    public void onTRTCStreamUnavailable(final String userId) {
    }

    @Override
    public void onRoomAnchorEnter(final String userId) {
    }

    @Override
    public void onRoomAnchorExit(final String userId) {
    }

    @Override
    public void onRoomStreamAvailable(final String userId) {
        TRTCLogger.i(TAG, "onRoomStreamAvailable:" + userId);
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (isTRTCMode()) {
                    return;
                }
                if (!mAnchorList.contains(userId)) {
                    mAnchorList.add(userId);
                    runOnDelegateThread(new Runnable() {
                        @Override
                        public void run() {
                            TRTCLiveRoomDelegate delegate = mDelegate;
                            if (delegate != null) {
                                delegate.onAnchorEnter(userId);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRoomStreamUnavailable(final String userId) {
        TRTCLogger.i(TAG, "onRoomStreamUnavailable:" + userId);
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (isTRTCMode()) {
                    return;
                }
                if (mAnchorList.contains(userId)) {
                    mAnchorList.remove(userId);
                    runOnDelegateThread(new Runnable() {
                        @Override
                        public void run() {
                            TRTCLiveRoomDelegate delegate = mDelegate;
                            if (delegate != null) {
                                delegate.onAnchorExit(userId);
                            }
                        }
                    });
                } else {
                    TRTCLogger.e(TAG, "room anchor exit, but never throw yet, maybe something error.");
                }
            }
        });
    }

    @Override
    public void onRoomAudienceEnter(final TXUserInfo userInfo) {
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                if (mAudienceList.contains(userInfo.userId)) {
                    return;
                }
                TRTCLiveRoomDelegate delegate = mDelegate;
                if (delegate != null) {
                    mAudienceList.add(userInfo.userId);
                    TRTCLiveRoomDef.TRTCLiveUserInfo info = new TRTCLiveRoomDef.TRTCLiveUserInfo();
                    info.userId = userInfo.userId;
                    info.avatarUrl = userInfo.avatarURL;
                    info.userName = userInfo.userName;
                    delegate.onAudienceEnter(info);
                }
            }
        });
    }

    @Override
    public void onRoomAudienceExit(final TXUserInfo userInfo) {
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                TRTCLiveRoomDelegate delegate = mDelegate;
                if (delegate != null) {
                    mAudienceList.remove(userInfo.userId);
                    TRTCLiveRoomDef.TRTCLiveUserInfo info = new TRTCLiveRoomDef.TRTCLiveUserInfo();
                    info.userId = userInfo.userId;
                    info.avatarUrl = userInfo.avatarURL;
                    info.userName = userInfo.userName;
                    delegate.onAudienceExit(info);
                }
            }
        });
    }

    @Override
    public void onRoomRequestJoinAnchor(final TXUserInfo userInfo, final String reason) {
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                TRTCLiveRoomDelegate delegate = mDelegate;
                if (delegate != null) {
                    TRTCLiveRoomDef.TRTCLiveUserInfo info = new TRTCLiveRoomDef.TRTCLiveUserInfo();
                    info.userId = userInfo.userId;
                    info.userName = userInfo.userName;
                    info.avatarUrl = userInfo.avatarURL;
                    delegate.onRequestJoinAnchor(info, reason);
                }
            }
        });
    }

    @Override
    public void onRoomKickoutJoinAnchor() {
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                stopPublish(null);
                TRTCLiveRoomDelegate delegate = mDelegate;
                if (delegate != null) {
                    delegate.onKickoutJoinAnchor();
                }
            }
        });
    }

    @Override
    public void onRoomRequestRoomPK(final TXUserInfo userInfo) {
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                TRTCLiveRoomDelegate delegate = mDelegate;
                if (delegate != null) {
                    TRTCLiveRoomDef.TRTCLiveUserInfo info = new TRTCLiveRoomDef.TRTCLiveUserInfo();
                    info.userId = userInfo.userId;
                    info.userName = userInfo.userName;
                    info.avatarUrl = userInfo.avatarURL;
                    delegate.onRequestRoomPK(info);
                }
            }
        });
    }

    @Override
    public void onRoomResponseRoomPK(final String roomId, final String streamId, final TXUserInfo userInfo) {
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mRequestPKHolder.setRealCallback(null);
                TRTCLogger.i(TAG, "recv pk repsonse, room id:" + roomId + " stream id:" + streamId + " info:" + userInfo.toString());
                // do pk
                // ???????????? PK ??????????????????????????? PK
                if (mCurrentRole == Role.TRTC_ANCHOR || mTargetRole == Role.TRTC_ANCHOR) {
                    TXTRTCLiveRoom.getInstance().startPK(roomId, userInfo.userId, new TXCallback() {
                        @Override
                        public void onCallback(final int code, final String msg) {
                            TRTCLogger.i(TAG, "start pk, code:" + code + " msg:" + msg);
                            if (code != 0) {
                                runOnDelegateThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TRTCLiveRoomDelegate delegate = mDelegate;
                                        if (delegate != null) {
                                            delegate.onError(code, msg);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onAnchorCancelRequestRoomPK(final String userId) {
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                TRTCLiveRoomDelegate delegate = mDelegate;
                if (delegate != null) {
                    delegate.onAnchorCancelRequestRoomPK(userId);
                }
            }
        });
    }

    @Override
    public void onAudienceRequestJoinAnchorTimeout(final String userId) {
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                TRTCLiveRoomDelegate delegate = mDelegate;
                if (delegate != null) {
                    delegate.onAudienceRequestJoinAnchorTimeout(userId);
                }
            }
        });
    }

    @Override
    public void onAnchorRequestRoomPKTimeout(final String userId) {
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                TRTCLiveRoomDelegate delegate = mDelegate;
                if (delegate != null) {
                    delegate.onAnchorRequestRoomPKTimeout(userId);
                }
            }
        });
    }

    @Override
    public void onAudienceCancelRequestJoinAnchor(final String userId) {
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                TRTCLiveRoomDelegate delegate = mDelegate;
                if (delegate != null) {
                    delegate.onAudienceCancelRequestJoinAnchor(userId);
                }
            }
        });
    }

    @Override
    public void onRoomQuitRoomPk() {
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                TRTCLiveRoomDelegate delegate = mDelegate;
                if (delegate != null) {
                    delegate.onQuitRoomPK();
                }
            }
        });
    }

    @Override
    public void onRoomRecvRoomTextMsg(final String roomId, final String message, final TXUserInfo userInfo) {
        V2TIMManager.getGroupManager().getGroupsInfo(Arrays.asList(roomId), new V2TIMValueCallback<List<V2TIMGroupInfoResult>>() {
            @Override
            public void onSuccess(List<V2TIMGroupInfoResult> v2TIMGroupInfoResults) {
                if(v2TIMGroupInfoResults == null || v2TIMGroupInfoResults.size() == 0){
                    return;
                }
                if (v2TIMGroupInfoResults.get(0).getGroupInfo().getGroupType().equals("AVChatRoom")) {
                    runOnDelegateThread(new Runnable() {
                        @Override
                        public void run() {
                            TRTCLiveRoomDelegate delegate = mDelegate;
                            if (delegate != null) {
                                TRTCLiveRoomDef.TRTCLiveUserInfo info = new TRTCLiveRoomDef.TRTCLiveUserInfo();
                                info.userId = userInfo.userId;
                                info.userName = userInfo.userName;
                                info.avatarUrl = userInfo.avatarURL;
                                delegate.onRecvRoomTextMsg(message, info,roomId);
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(int code, String desc) {

            }
        });
    }

    @Override
    public void onRoomRecvRoomCustomMsg(final String groupId, final String cmd, final String message, final TXUserInfo userInfo) {
        //?????????????????????????????????????????????
        if (!mRoomId.equals(groupId)) {
            return;
        }
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                TRTCLiveRoomDelegate delegate = mDelegate;
                if (delegate != null) {
                    TRTCLiveRoomDef.TRTCLiveUserInfo info = new TRTCLiveRoomDef.TRTCLiveUserInfo();
                    info.userId = userInfo.userId;
                    info.userName = userInfo.userName;
                    info.avatarUrl = userInfo.avatarURL;
                    delegate.onRecvRoomCustomMsg(groupId,cmd, message, info);
                }
            }
        });
    }

    @Override
    public void followAnchor(final String userId, final TRTCLiveRoomCallback.ActionCallback callback) {
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                TXRoomService.getInstance().followAnchor(userId, callback);
            }
        });
    }

    @Override
    public void checkFollowAnchorState(final String userId, final TRTCLiveRoomCallback.RoomFollowStateCallback callback) {
        runOnDelegateThread(new Runnable() {
            @Override
            public void run() {
                TXRoomService.getInstance().checkFollowState(userId, callback);
            }
        });
    }
}
