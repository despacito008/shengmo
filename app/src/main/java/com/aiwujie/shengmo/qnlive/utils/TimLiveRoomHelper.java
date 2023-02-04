package com.aiwujie.shengmo.qnlive.utils;


import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.qnlive.bean.TimSignalMessageBean;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.tencent.imsdk.v2.V2TIMGroupListener;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSignalingListener;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.room.impl.IMProtocol;

import java.util.List;


public class TimLiveRoomHelper {

    private static TimLiveRoomHelper sInstance;

    private LiveRoomGroupListener mGroupListener;
    private LiveRoomSignalingListener mSignalingListener;

    public static synchronized TimLiveRoomHelper getInstance() {
        if (sInstance == null) {
            sInstance = new TimLiveRoomHelper();
        }
        return sInstance;
    }

    private TimLiveRoomHelper() {
        if (mGroupListener == null) {
            mGroupListener = new LiveRoomGroupListener();
            mSignalingListener = new LiveRoomSignalingListener();
            V2TIMManager.getInstance().addGroupListener(mGroupListener);
            V2TIMManager.getSignalingManager().addSignalingListener(mSignalingListener);
        }
    }

    class LiveRoomGroupListener extends V2TIMGroupListener {
        @Override
        public void onMemberEnter(String groupID, List<V2TIMGroupMemberInfo> memberList) {
            super.onMemberEnter(groupID, memberList);
            if (memberList.size() < 1) {
                return;
            }
            if (onTimLiveListener != null) {
                V2TIMGroupMemberInfo member = memberList.get(0);
                onTimLiveListener.onMemberEnter(groupID,member.getUserID());
            }
        }

        @Override
        public void onMemberLeave(String groupID, V2TIMGroupMemberInfo member) {
            super.onMemberLeave(groupID, member);
            if (onTimLiveListener != null) {
                onTimLiveListener.onMemberLeave(groupID,member.getUserID());
            }
        }

        @Override
        public void onGroupDismissed(String groupID, V2TIMGroupMemberInfo opUser) {
            super.onGroupDismissed(groupID, opUser);
            if (onTimLiveListener != null) {
                onTimLiveListener.onGroupDismiss(groupID);
            }
        }

        @Override
        public void onMemberKicked(String groupID, V2TIMGroupMemberInfo opUser, List<V2TIMGroupMemberInfo> memberList) {
            super.onMemberKicked(groupID, opUser, memberList);
        }
    }

    public interface OnTimLiveListener {
        void onMemberEnter(String groupId,String uid);
        void onMemberLeave(String groupId,String uid);
        void onGroupDismiss(String groupId);
    }

    OnTimLiveListener onTimLiveListener;

    String mRoomId;

    public void initRoom(String roomId) {
        mRoomId = roomId;
    }

    private class LiveRoomSignalingListener extends V2TIMSignalingListener {
        @Override
        public void onReceiveNewInvitation(java.lang.String inviteID, java.lang.String inviter, java.lang.String groupID, java.util.List<java.lang.String> inviteeList, java.lang.String data) {
//            if (!groupID.equals(mRoomId)) {
//                return;
//            }
            if (!inviteeList.contains(MyApp.uid)) {
                return;
            }
            TimSignalMessageBean timSignalMessageBean = GsonUtil.GsonToBean(data, TimSignalMessageBean.class);
            if (timSignalMessageBean == null) {
                if (onTimSignalingListener != null) {
                    onTimSignalingListener.onReceiveLinkRequest(inviteID,inviter,timSignalMessageBean.getReason());
                }
                return;
            }
            if (timSignalMessageBean.getAction() == IMProtocol.Define.CODE_REQUEST_JOIN_ANCHOR) {
                if (onTimSignalingListener != null) {
                    onTimSignalingListener.onReceiveLinkRequest(inviteID,inviter,timSignalMessageBean.getReason());
                }
            } else if (timSignalMessageBean.getAction() == IMProtocol.Define.CODE_REQUEST_ROOM_PK) {
                if (onTimSignalingListener != null) {
                    onTimSignalingListener.onReceivePkRequest(inviteID,inviter,timSignalMessageBean.getReason());
                }
            }
        }
        @Override
        public void onInviteeAccepted(java.lang.String inviteID, java.lang.String invitee, java.lang.String data) {
            TimSignalMessageBean timSignalMessageBean = GsonUtil.GsonToBean(data, TimSignalMessageBean.class);
            if (timSignalMessageBean == null) {
                if (onTimSignalingListener != null) {
                    onTimSignalingListener.onAcceptLinkRequest(inviteID,invitee,data);
                }
                return;
            }
            if (timSignalMessageBean.getAction() == IMProtocol.Define.CODE_RESPONSE_JOIN_ANCHOR) {
                if (onTimSignalingListener != null) {
                    onTimSignalingListener.onAcceptLinkRequest(inviteID,invitee,timSignalMessageBean.getReason());
                }
            } else if (timSignalMessageBean.getAction() == IMProtocol.Define.CODE_RESPONSE_PK) {
                if (onTimSignalingListener != null) {
                    onTimSignalingListener.onAcceptPkRequest(inviteID,invitee,timSignalMessageBean.getReason());
                }
            }

        }
        @Override
        public void onInviteeRejected(java.lang.String inviteID, java.lang.String invitee, java.lang.String data) {
            TimSignalMessageBean timSignalMessageBean = GsonUtil.GsonToBean(data, TimSignalMessageBean.class);
            if (timSignalMessageBean == null) {
                if (onTimSignalingListener != null) {
                    onTimSignalingListener.onRejectLinkRequest(inviteID,invitee,data);
                }
                return;
            }
            if (timSignalMessageBean.getAction() == IMProtocol.Define.CODE_RESPONSE_JOIN_ANCHOR) {
                if (onTimSignalingListener != null) {
                    onTimSignalingListener.onRejectLinkRequest(inviteID,invitee,timSignalMessageBean.getReason());
                }
            } else if (timSignalMessageBean.getAction() == IMProtocol.Define.CODE_RESPONSE_PK) {
                if (onTimSignalingListener != null) {
                    onTimSignalingListener.onRejectPkRequest(inviteID,invitee,timSignalMessageBean.getReason());
                }
            }

        }
        @Override
        public void onInvitationCancelled(java.lang.String inviteID, java.lang.String inviter, java.lang.String data) {
            TimSignalMessageBean timSignalMessageBean = GsonUtil.GsonToBean(data, TimSignalMessageBean.class);
            if (timSignalMessageBean == null) {
                if (onTimSignalingListener != null) {
                    onTimSignalingListener.onCancelLinkRequest(inviteID,inviter,data);
                }
                return;
            }
            if (timSignalMessageBean.getAction() == IMProtocol.Define.CODE_CANCEL_REQUEST_JOIN_ANCHOR) {
                if (onTimSignalingListener != null) {
                    onTimSignalingListener.onCancelLinkRequest(inviteID,inviter,timSignalMessageBean.getReason());
                }
            } else if (timSignalMessageBean.getAction() == IMProtocol.Define.CODE_CANCEL_REQUEST_ROOM_PK) {
                if (onTimSignalingListener != null) {
                    onTimSignalingListener.onCancelPkRequest(inviteID,inviter,timSignalMessageBean.getReason());
                }
            }
        }
        @Override
        public void onInvitationTimeout(java.lang.String inviteID, java.util.List<java.lang.String> inviteeList) {
            if (onTimSignalingListener != null) {
                if (inviteeList != null && inviteeList.size() > 0) {
                    onTimSignalingListener.onTimeoutLinkRequest(inviteID, inviteeList.get(0), "");
                } else {
                    onTimSignalingListener.onTimeoutLinkRequest(inviteID, "", "");
                }
            }
        }
    }

    public interface OnTimSignalingListener {
        void onReceiveLinkRequest(String inviteId,String userId,String reason);
        void onCancelLinkRequest(String inviteId,String userId,String reason);
        void onAcceptLinkRequest(String inviteId,String userId,String reason);
        void onRejectLinkRequest(String inviteId,String userId,String reason);
        void onTimeoutLinkRequest(String inviteId,String userId,String reason);
        void onReceivePkRequest(String inviteId,String userId,String reason);
        void onCancelPkRequest(String inviteId,String userId,String reason);
        void onAcceptPkRequest(String inviteId,String userId,String reason);
        void onRejectPkRequest(String inviteId,String userId,String reason);
        void onTimeoutPkRequest(String inviteId,String userId,String reason);
    }

    OnTimSignalingListener onTimSignalingListener;

    public void initListener(OnTimLiveListener onTimLiveListener,OnTimSignalingListener onTimSignalingListener) {
        this.onTimLiveListener = onTimLiveListener;
        this.onTimSignalingListener = onTimSignalingListener;
    }

    public void unitListener() {
        this.onTimLiveListener = null;
        this.onTimSignalingListener = null;
    }
}
