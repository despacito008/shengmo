package com.tencent.qcloud.tim.uikit.modules.conversation;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMGroupAtInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.DraftInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.interfaces.ILoadConversationCallback;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.modules.message.MessageRevokedManager;
import com.tencent.qcloud.tim.uikit.utils.DateTimeUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import java.io.File;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConversationManagerKit implements MessageRevokedManager.MessageRevokeHandler {

    private final static String TAG = ConversationManagerKit.class.getSimpleName();
    private final static String SP_IMAGE = "_conversation_group_face";
    private final static int GET_CONVERSATION_COUNT = 100;

    private static ConversationManagerKit instance = new ConversationManagerKit();

    private ConversationProvider mProvider;
    private List<MessageUnreadWatcher> mUnreadWatchers = new ArrayList<>();
    private int mUnreadTotal;


    private ConversationManagerKit() {
        init();
    }

    public static ConversationManagerKit getInstance() {
        return instance;
    }

    private void init() {
        TUIKitLog.i(TAG, "init");
        MessageRevokedManager.getInstance().addHandler(this);
    }

    List<V2TIMConversation> c2cV2TIMConversationList = new ArrayList<>();

    /**
     * ??????????????????
     *
     * @param nextSeq  ????????????????????????????????????????????? 0???????????????????????????????????????????????????????????? nextSeq
     * @param callBack ?????????????????????
     */
    public void loadConversation(long nextSeq, final ILoadConversationCallback callBack) {
        TUIKitLog.i(TAG, "loadConversation callBack:" + callBack);
        //mProvider???????????????null,???????????????????????????????????????????????????????????????
        if (mProvider == null) {
            mProvider = new ConversationProvider();
        }

        V2TIMManager.getConversationManager().getConversationList(nextSeq, GET_CONVERSATION_COUNT, new V2TIMValueCallback<V2TIMConversationResult>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.v(TAG, "loadConversation getConversationList error, code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                List<V2TIMConversation> v2TIMConversationList = v2TIMConversationResult.getConversationList();

//                for (V2TIMConversation v2TIMConversation : v2TIMConversationList) {
//                    if (V2TIMConversation.V2TIM_C2C == v2TIMConversation.getType()) {
//                        c2cV2TIMConversationList.add(v2TIMConversation);
//                    }
//                }
//                onRefreshConversation(c2cV2TIMConversationList);
                onRefreshConversation(v2TIMConversationList);

                boolean isFinished = v2TIMConversationResult.isFinished();
                long nextSeq = v2TIMConversationResult.getNextSeq();
                if (callBack != null) {
                    callBack.onSuccess(mProvider, isFinished, nextSeq);
                }

                // ????????????????????????
                V2TIMManager.getConversationManager().getTotalUnreadMessageCount(new V2TIMValueCallback<Long>() {
                    @Override
                    public void onSuccess(Long aLong) {
                        mUnreadTotal = aLong.intValue();
                        updateUnreadTotal(mUnreadTotal);
                    }

                    @Override
                    public void onError(int code, String desc) {

                    }
                });
            }
        });
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param v2TIMConversationList ???????????????????????????
     */
    public void onRefreshConversation(List<V2TIMConversation> v2TIMConversationList) {
        TUIKitLog.v(TAG, "onRefreshConversation conversations:" + v2TIMConversationList);
        if (mProvider == null) {
            return;
        }
        ArrayList<ConversationInfo> infos = new ArrayList<>();
        for (int i = 0; i < v2TIMConversationList.size(); i++) {
            V2TIMConversation v2TIMConversation = v2TIMConversationList.get(i);
            TUIKitLog.v(TAG, "refreshConversation v2TIMConversation " + v2TIMConversation.toString());
            ConversationInfo conversationInfo = TIMConversation2ConversationInfo(v2TIMConversation);
            //6.1 update
            if (conversationInfo != null && !V2TIMManager.GROUP_TYPE_AVCHATROOM.equals(v2TIMConversation.getGroupType())) {
                infos.add(conversationInfo);
            }

//            if (conversationInfo != null && !V2TIMManager.GROUP_TYPE_AVCHATROOM.equals(v2TIMConversation.getGroupType())) {
//                if (V2TIMConversation.V2TIM_C2C == v2TIMConversation.getType()) {
//                    infos.add(conversationInfo);
//                }
//            }

        }
        if (infos.size() == 0) {
            return;
        }
        List<ConversationInfo> dataSource = mProvider.getDataSource();
        ArrayList exists = new ArrayList();
        for (int j = 0; j < infos.size(); j++) {
            ConversationInfo update = infos.get(j);
            for (int i = 0; i < dataSource.size(); i++) {
                ConversationInfo cacheInfo = dataSource.get(i);
                //???????????????????????????????????????????????????????????????????????????????????????????????????????????????id?????????id??????
                if (cacheInfo.getId().equals(update.getId()) && cacheInfo.isGroup() == update.isGroup()) {
                    dataSource.remove(i);
                    dataSource.add(i, update);
                    exists.add(update);
                    //infos.remove(j);
                    break;
                }
            }
        }
        infos.removeAll(exists);
        if (infos.size() > 0) {
            dataSource.addAll(infos);
        }
//        Collections.sort(dataSource);
//        ArrayList<ConversationInfo> conversationInfos = new ArrayList<>();
//        conversationInfos.addAll(dataSource);
//        mProvider.setDataSource(conversationInfos);
        mProvider.setDataSource(sortConversations(dataSource));


    }

    public void updateTotalUnreadMessageCount(long totalUnreadCount) {
        mUnreadTotal = (int) totalUnreadCount;
        updateUnreadTotal(mUnreadTotal);
    }

    /**
     * TIMConversation?????????ConversationInfo
     *
     * @param conversation
     * @return
     */
    private ConversationInfo TIMConversation2ConversationInfo(final V2TIMConversation conversation) {
        if (conversation == null) {
            return null;
        }
        TUIKitLog.i(TAG, "TIMConversation2ConversationInfo id:" + conversation.getConversationID()
                + "|name:" + conversation.getShowName()
                + "|unreadNum:" + conversation.getUnreadCount());
        final ConversationInfo info = new ConversationInfo();
        int type = conversation.getType();
        if (type != V2TIMConversation.V2TIM_C2C && type != V2TIMConversation.V2TIM_GROUP) {
            return null;
        }

        boolean isGroup = type == V2TIMConversation.V2TIM_GROUP;

        String draftText = conversation.getDraftText();
        if (!TextUtils.isEmpty(draftText)) {
            DraftInfo draftInfo = new DraftInfo();
            draftInfo.setDraftText(draftText);
            draftInfo.setDraftTime(conversation.getDraftTimestamp());
            info.setDraft(draftInfo);
        }
        V2TIMMessage message = conversation.getLastMessage();
        if (message == null) {
            long time = DateTimeUtil.getStringToDate("0001-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
            info.setLastMessageTime(time);
        } else {
            info.setLastMessageTime(message.getTimestamp());
        }
        MessageInfo messageInfo = MessageInfoUtil.TIMMessage2MessageInfo(message);
        if (messageInfo != null) {
            info.setLastMessage(messageInfo);
        }

        int atInfoType = getAtInfoType(conversation);
        switch (atInfoType) {
            case V2TIMGroupAtInfo.TIM_AT_ME:
                info.setAtInfoText(TUIKit.getAppContext().getString(R.string.ui_at_me));
                break;
            case V2TIMGroupAtInfo.TIM_AT_ALL:
                info.setAtInfoText(TUIKit.getAppContext().getString(R.string.ui_at_all));
                break;
            case V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME:
                info.setAtInfoText(TUIKit.getAppContext().getString(R.string.ui_at_all_me));
                break;
            default:
                info.setAtInfoText("");
                break;

        }

        info.setTitle(conversation.getShowName());
        if (isGroup) {
            fillConversationUrlForGroup(conversation, info);
        } else {
            List<Object> faceList = new ArrayList<>();
            if (TextUtils.isEmpty(conversation.getFaceUrl())) {
                faceList.add(R.drawable.default_head);
            } else {
                faceList.add(conversation.getFaceUrl());
            }
            info.setIconUrlList(faceList);
        }
        if (isGroup) {
            info.setId(conversation.getGroupID());
            info.setGroupType(conversation.getGroupType());
        } else {
            info.setId(conversation.getUserID());
        }

        info.setShowDisturbIcon(conversation.getRecvOpt() == V2TIMMessage.V2TIM_NOT_RECEIVE_MESSAGE ? true : false);
        info.setConversationId(conversation.getConversationID());
        info.setGroup(isGroup);
        // AVChatRoom ?????????????????????
        if (!V2TIMManager.GROUP_TYPE_AVCHATROOM.equals(conversation.getGroupType())) {
            info.setUnRead(conversation.getUnreadCount());
        }
        info.setTop(conversation.isPinned());
        return info;
    }

    private int getAtInfoType(V2TIMConversation conversation) {
        int atInfoType = 0;
        boolean atMe = false;
        boolean atAll = false;

        List<V2TIMGroupAtInfo> atInfoList = conversation.getGroupAtInfoList();

        if (atInfoList == null || atInfoList.isEmpty()) {
            return V2TIMGroupAtInfo.TIM_AT_UNKNOWN;
        }

        for (V2TIMGroupAtInfo atInfo : atInfoList) {
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ME) {
                atMe = true;
                continue;
            }
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ALL) {
                atAll = true;
                continue;
            }
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME) {
                atMe = true;
                atAll = true;
                continue;
            }
        }

        if (atAll && atMe) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME;
        } else if (atAll) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ALL;
        } else if (atMe) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ME;
        } else {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_UNKNOWN;
        }

        return atInfoType;
    }

    private void fillConversationUrlForGroup(final V2TIMConversation conversation, final ConversationInfo info) {
        if (TextUtils.isEmpty(conversation.getFaceUrl())) {
            final String savedIcon = getGroupConversationAvatar(conversation.getConversationID());
            if (TextUtils.isEmpty(savedIcon)) {
                fillFaceUrlList(conversation.getGroupID(), info);
            } else {
                List<Object> list = new ArrayList<>();
                list.add(savedIcon);
                info.setIconUrlList(list);
            }
        } else {
            List<Object> list = new ArrayList<>();
            list.add(conversation.getFaceUrl());
            info.setIconUrlList(list);
        }
    }

    private void fillFaceUrlList(final String groupID, final ConversationInfo info) {
        V2TIMManager.getGroupManager().getGroupMemberList(groupID, V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_FILTER_ALL, 0, new V2TIMValueCallback<V2TIMGroupMemberInfoResult>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "getGroupMemberList failed! groupID:" + groupID + "|code:" + code + "|desc: " + desc);
            }

            @Override
            public void onSuccess(V2TIMGroupMemberInfoResult v2TIMGroupMemberInfoResult) {
                List<V2TIMGroupMemberFullInfo> v2TIMGroupMemberFullInfoList = v2TIMGroupMemberInfoResult.getMemberInfoList();
                int faceSize = v2TIMGroupMemberFullInfoList.size() > 9 ? 9 : v2TIMGroupMemberFullInfoList.size();
                List<Object> urlList = new ArrayList<>();
                for (int i = 0; i < faceSize; i++) {
                    V2TIMGroupMemberFullInfo v2TIMGroupMemberFullInfo = v2TIMGroupMemberFullInfoList.get(i);
                    if (TextUtils.isEmpty(v2TIMGroupMemberFullInfo.getFaceUrl())) {
                        urlList.add(R.drawable.default_head);
                    } else {
                        urlList.add(v2TIMGroupMemberFullInfo.getFaceUrl());
                    }
                }
                info.setIconUrlList(urlList);
                mProvider.updateAdapter(info.getConversationId());
            }
        });
    }

    /**
     * ?????????????????????
     *
     * @param conversation
     */
    public void setConversationTop(final ConversationInfo conversation, final IUIKitCallBack callBack) {
        TUIKitLog.i(TAG, "setConversationTop" + "|conversation:" + conversation);
        final boolean setTop = !conversation.isTop();

        V2TIMManager.getConversationManager().pinConversation(conversation.getConversationId(), setTop, new V2TIMCallback() {
            @Override
            public void onSuccess() {
                conversation.setTop(setTop);
                mProvider.setDataSource(sortConversations(mProvider.getDataSource()));
            }

            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "setConversationTop code:" + code + "|desc:" + desc);
                if (callBack != null) {
                    callBack.onError("setConversationTop", code, desc);
                }
            }
        });
    }

    /**
     * ??????????????????
     *
     * @param id    ??????ID
     * @param isTop ????????????
     */
    public void setConversationTop(String id, final boolean isTop, final IUIKitCallBack callBack) {
        TUIKitLog.i(TAG, "setConversationTop id:" + id + "|isTop:" + isTop);
        ConversationInfo conversation = null;
        List<ConversationInfo> conversationInfos = mProvider.getDataSource();
        for (int i = 0; i < conversationInfos.size(); i++) {
            ConversationInfo info = conversationInfos.get(i);
            if (info.getId().equals(id)) {
                conversation = info;
                break;
            }
        }
        if (conversation == null) {
            return;
        }
        final String conversationID = conversation.getConversationId();
        V2TIMManager.getConversationManager().pinConversation(conversation.getConversationId(), isTop, new V2TIMCallback() {
            @Override
            public void onSuccess() {
                List<ConversationInfo> conversationInfoList = mProvider.getDataSource();
                for (int i = 0; i < conversationInfoList.size(); i++) {
                    ConversationInfo info = conversationInfoList.get(i);
                    if (info.getId().equals(conversationID)) {
                        info.setTop(isTop);
                        break;
                    }
                }
                mProvider.setDataSource(sortConversations(mProvider.getDataSource()));
            }

            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "setConversationTop code:" + code + "|desc:" + desc);
                if (callBack != null) {
                    callBack.onError("setConversationTop", code, desc);
                }
            }
        });
    }

    /**
     * ??????????????????????????????????????????imsdk?????????
     *
     * @param index        ????????????????????????
     * @param conversation ????????????
     */
    public void deleteConversation(int index, ConversationInfo conversation) {
        TUIKitLog.i(TAG, "deleteConversation index:" + index + "|conversation:" + conversation);
        V2TIMManager.getConversationManager().deleteConversation(conversation.getConversationId(), new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "deleteConversation error:" + code + ", desc:" + desc);
            }

            @Override
            public void onSuccess() {
                TUIKitLog.i(TAG, "deleteConversation success");
            }
        });
        if (mProvider != null) {
//            if (index != -1 && mProvider.getDataSource().size() > index) {
//                mProvider.deleteConversation(index);
//            }

            if (!TextUtils.isEmpty(conversation.getConversationId())){
                mProvider.deleteConversation(conversation.getConversationId());
            }

        }
    }

    /**
     * ????????????
     *
     * @param index        ????????????????????????
     * @param conversation ????????????
     */
    public void clearConversationMessage(int index, ConversationInfo conversation) {
        if (conversation == null || TextUtils.isEmpty(conversation.getConversationId())) {
            TUIKitLog.e(TAG, "clearConversationMessage error: invalid conversation");
        }

        TUIKitLog.i(TAG, "clearConversationMessage index:" + index + "|conversation:" + conversation);
        if (conversation.isGroup()) {
            V2TIMManager.getMessageManager().clearGroupHistoryMessage(conversation.getId(), new V2TIMCallback() {
                @Override
                public void onError(int code, String desc) {
                    TUIKitLog.e(TAG, "clearConversationMessage error:" + code + ", desc:" + desc);
                }

                @Override
                public void onSuccess() {
                    TUIKitLog.i(TAG, "clearConversationMessage success");
                }
            });
        } else {
            V2TIMManager.getMessageManager().clearC2CHistoryMessage(conversation.getId(), new V2TIMCallback() {
                @Override
                public void onError(int code, String desc) {
                    TUIKitLog.e(TAG, "clearConversationMessage error:" + code + ", desc:" + desc);
                }

                @Override
                public void onSuccess() {
                    TUIKitLog.i(TAG, "clearConversationMessage success");
                }
            });
        }
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param id C2C???????????? userID???Group?????? ID
     */
    public void deleteConversation(String id, boolean isGroup) {
        TUIKitLog.i(TAG, "deleteConversation id:" + id + "|isGroup:" + isGroup);
        String conversationID = "";
        if (mProvider != null) {
            List<ConversationInfo> conversationInfoList = mProvider.getDataSource();
            for (ConversationInfo conversationInfo : conversationInfoList) {
                if (isGroup == conversationInfo.isGroup() && conversationInfo.getId().equals(id)) {
                    conversationID = conversationInfo.getConversationId();
                    break;
                }
            }
            if (!TextUtils.isEmpty(conversationID)) {
                mProvider.deleteConversation(conversationID);
            }
        }
        if (!TextUtils.isEmpty(conversationID)) {
            V2TIMManager.getConversationManager().deleteConversation(conversationID, new V2TIMCallback() {
                @Override
                public void onError(int code, String desc) {
                    TUIKitLog.i(TAG, "deleteConversation error:" + code + ", desc:" + desc);
                }

                @Override
                public void onSuccess() {
                    TUIKitLog.i(TAG, "deleteConversation success");
                }
            });
        }
    }

    /**
     * ????????????
     *
     * @param conversationInfo
     * @return
     */
    public boolean addConversation(ConversationInfo conversationInfo) {
        List<ConversationInfo> conversationInfos = new ArrayList<>();
        conversationInfos.add(conversationInfo);
        return mProvider.addConversations(conversationInfos);
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param sources
     * @return
     */
    private List<ConversationInfo> sortConversations(List<ConversationInfo> sources) {
        ArrayList<ConversationInfo> conversationInfos = new ArrayList<>();
        List<ConversationInfo> normalConversations = new ArrayList<>();
        List<ConversationInfo> topConversations = new ArrayList<>();

        for (int i = 0; i <= sources.size() - 1; i++) {
            ConversationInfo conversation = sources.get(i);
            if (conversation.isTop()) {
                topConversations.add(conversation);
            } else {
                normalConversations.add(conversation);
            }
        }

        if (topConversations != null && topConversations.size() > 1) {
            Collections.sort(topConversations); // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        }
        conversationInfos.addAll(topConversations);
        if (normalConversations != null && normalConversations.size() > 1) {
            Collections.sort(normalConversations); // ?????????????????????????????????????????????????????????????????????
        }
        conversationInfos.addAll(normalConversations);
        return conversationInfos;
    }

    /**
     * ????????????????????????
     *
     * @param unreadTotal
     */
    public void updateUnreadTotal(int unreadTotal) {
        TUIKitLog.i(TAG, "updateUnreadTotal:" + unreadTotal);
        mUnreadTotal = unreadTotal;
        for (int i = 0; i < mUnreadWatchers.size(); i++) {
            mUnreadWatchers.get(i).updateUnread(mUnreadTotal);
        }
    }

    /**
     * ???????????????????????????
     *
     * @return ????????????
     */
    public int getUnreadTotal() {
        return mUnreadTotal;
    }

    public boolean isTopConversation(String conversationID) {
        List<ConversationInfo> conversationInfos = mProvider.getDataSource();
        for (int i = 0; i < conversationInfos.size(); i++) {
            ConversationInfo info = conversationInfos.get(i);
            if (info.getId().equals(conversationID)) {
                return info.isTop();
            }
        }
        return false;
    }

    /**
     * ??????????????????
     *
     * @param msgID
     */
    @Override
    public void handleRevoke(String msgID) {
        TUIKitLog.i(TAG, "handleInvoke msgID:" + msgID);
        if (mProvider != null) {
            loadConversation(0, null);
        }
    }


    /**
     * ???????????????????????????
     *
     * @param messageUnreadWatcher
     */
    public void addUnreadWatcher(MessageUnreadWatcher messageUnreadWatcher) {
        TUIKitLog.i(TAG, "addUnreadWatcher:" + messageUnreadWatcher);
        if (!mUnreadWatchers.contains(messageUnreadWatcher)) {
            mUnreadWatchers.add(messageUnreadWatcher);
            messageUnreadWatcher.updateUnread(mUnreadTotal);
        }
    }

    /**
     * ???????????????????????????
     *
     * @param messageUnreadWatcher
     */
    public void removeUnreadWatcher(MessageUnreadWatcher messageUnreadWatcher) {
        TUIKitLog.i(TAG, "removeUnreadWatcher:" + messageUnreadWatcher);
        if (messageUnreadWatcher == null) {
            mUnreadWatchers.clear();
        } else {
            mUnreadWatchers.remove(messageUnreadWatcher);
        }
    }

    /**
     * ???UI????????????????????????????????????
     */
    public void destroyConversation() {
        TUIKitLog.i(TAG, "destroyConversation");
        if (mProvider != null) {
            mProvider.clear();
        }
        if (mUnreadWatchers != null) {
            mUnreadWatchers.clear();
        }
    }

    public String getGroupConversationAvatar(String groupId) {
        SharedPreferences sp = TUIKit.getAppContext().getSharedPreferences(
                TUIKitConfigs.getConfigs().getGeneralConfig().getSDKAppId() + SP_IMAGE, Context.MODE_PRIVATE);
        final String savedIcon = sp.getString(groupId, "");
        if (!TextUtils.isEmpty(savedIcon) && new File(savedIcon).isFile() && new File(savedIcon).exists()) {
            return savedIcon;
        }
        return "";
    }

    public void setGroupConversationAvatar(String groupId, String url) {
        SharedPreferences sp = TUIKit.getAppContext().getSharedPreferences(
                TUIKitConfigs.getConfigs().getGeneralConfig().getSDKAppId() + SP_IMAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(groupId, url);
        editor.apply();
    }

    /**
     * ?????????????????????????????????
     */
    public interface MessageUnreadWatcher {
        void updateUnread(int count);
    }

    /**
     * ?????????????????????????????????lastMessage????????????
     */
    public void onReceiptUpdateConversation(List<V2TIMMessageReceipt> receiptList) {
        ArrayList<String> conversationIDList = new ArrayList<>();

        for (int i = 0; i < receiptList.size(); i++) {
            String userID = receiptList.get(i).getUserID();
            conversationIDList.add(String.format("c2c_%s", userID));
        }

        V2TIMManager.getConversationManager().getConversationList(conversationIDList, new V2TIMValueCallback<List<V2TIMConversation>>() {
            @Override
            public void onSuccess(List<V2TIMConversation> v2TIMConversations) {
                onRefreshConversation(v2TIMConversations);
            }

            @Override
            public void onError(int i, String s) {
                TUIKitLog.v(TAG, "receipt update conversation - getConversationList error, code = " + i + ", desc = " + s);
            }
        });
    }

//    void destroyConversationList() {
//        mProvider = null;
//    }


    public ConversationProvider getProvider() {
        return  mProvider;
    }

}