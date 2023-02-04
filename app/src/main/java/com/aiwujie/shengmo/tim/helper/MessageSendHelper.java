package com.aiwujie.shengmo.tim.helper;

import android.content.Intent;

import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.tim.bean.FlashMessageBean;
import com.aiwujie.shengmo.tim.bean.GiftMessageBean;
import com.aiwujie.shengmo.tim.bean.LocationMessageBean;
import com.aiwujie.shengmo.tim.bean.RedEnvelopesMessageBean;
import com.aiwujie.shengmo.tim.bean.RefreshMessageBean;
import com.aiwujie.shengmo.tim.bean.SystemTipsMessageBean;
import com.aiwujie.shengmo.tim.chat.TempActivity;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aliyun.common.utils.L;
import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMOfflinePushInfo;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageBean;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageContainerBean;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import org.greenrobot.eventbus.EventBus;


public class MessageSendHelper {
    public static final String TAG = "logUtil";
    /**
     * 私有化构造方法
     */
    private MessageSendHelper() {
    }

    /**
     * 静态内部类做单例
     */
    private static class SingletonHolder {
        private static final MessageSendHelper messageSendHelper = new MessageSendHelper();
    }

    /**
     * 获取单例方法
     *
     * @return
     */
    public static final MessageSendHelper getInstance() {
        return MessageSendHelper.SingletonHolder.messageSendHelper;
    }


    public void sendRedEnvelopsMessage(String orderid, String message, final IUIKitCallBack callBack) {
        RedEnvelopesMessageBean envelopesMessageBean = new RedEnvelopesMessageBean();
        RedEnvelopesMessageBean.ContentDictBean contentDictBean = new RedEnvelopesMessageBean.ContentDictBean();
        contentDictBean.setIsopen("0");
        contentDictBean.setOrderid(orderid);
        contentDictBean.setMessage(message);
        envelopesMessageBean.setContentDict(contentDictBean);
        String data = GsonUtil.getInstance().toJson(envelopesMessageBean);
        //MessageInfo messageInfo = MessageHelper.buildRedEnvelopesMessage(data);
        MessageInfo messageInfo = MessageInfoUtil.buildCustomMessage(GsonUtil.getInstance().toJson(envelopesMessageBean));
        ChatManagerHelper.getInstance().getChatMangerKit().sendMessage(messageInfo, false, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                callBack.onSuccess(data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                callBack.onError(module,errCode,errMsg);
            }
        });
    }

    public void sendGiftMessage(String imageName,String number,String orderId) {
        GiftMessageBean giftMessageBean = new GiftMessageBean();
        GiftMessageBean.ContentDictBean contentDictBean = new GiftMessageBean.ContentDictBean();
        contentDictBean.setNumber(number);
        contentDictBean.setGiftText(imageName + " X " + number);
        contentDictBean.setImageName(imageName);
        contentDictBean.setOrderid(orderId);
        giftMessageBean.setContentDict(contentDictBean);
        //MessageInfo messageInfo = MessageHelper.buildGiftMessage(GsonUtil.getInstance().toJson(giftMessageBean));
        MessageInfo messageInfo =  MessageInfoUtil.buildCustomMessage(GsonUtil.getInstance().toJson(giftMessageBean));
        ChatManagerHelper.getInstance().getChatLayout().sendMessage(messageInfo,false);
    }

    public void sendGiftMessage(GiftMessageBean.ContentDictBean contentDictBean) {
        GiftMessageBean giftMessageBean = new GiftMessageBean();
        giftMessageBean.setContentDict(contentDictBean);
        //MessageInfo messageInfo = MessageHelper.buildGiftMessage(GsonUtil.getInstance().toJson(giftMessageBean));
        MessageInfo messageInfo =  MessageInfoUtil.buildCustomMessage(GsonUtil.getInstance().toJson(giftMessageBean));
        ChatManagerHelper.getInstance().getChatLayout().sendMessage(messageInfo,false);
    }

    public void sendTipMessage(String self, String other) {
        SystemTipsMessageBean systemTipsMessageBean = new SystemTipsMessageBean();
        SystemTipsMessageBean.ContentDictBean contentDict = new SystemTipsMessageBean.ContentDictBean();
        contentDict.setSelfShowString(self);
        contentDict.setOtherShowString(other);
        systemTipsMessageBean.setContentDict(contentDict);
        String tips = GsonUtil.getInstance().toJson(systemTipsMessageBean);
        MessageInfo customMessage = MessageInfoUtil.buildCustomMessage(tips);
        ChatManagerHelper.getInstance().getChatMangerKit().sendMessage(customMessage, false, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });

    }

    public void sendFlashMessage(String url) {
        FlashMessageBean flashMessageBean = new FlashMessageBean();
        FlashMessageBean.ContentDictBean contentDictBean = new FlashMessageBean.ContentDictBean();
        contentDictBean.setImageUrl(url);
        flashMessageBean.setContentDict(contentDictBean);
        MessageInfo messageInfo =MessageInfoUtil.buildCustomMessage(GsonUtil.getInstance().toJson(flashMessageBean));
        ChatManagerHelper.getInstance().getChatMangerKit().sendMessage(messageInfo, false, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
               // MyApp.getInstance().startActivity(new Intent(MyApp.instance(), TempActivity.class));
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }

    public void sendLocationMessage(String lat,String lng,String address,final IUIKitCallBack callBack ) {
        LocationMessageBean locationMessageBean = new LocationMessageBean();
        LocationMessageBean.ContentDictBean contentDictBean = new LocationMessageBean.ContentDictBean();
        contentDictBean.setLatitude(lat);
        contentDictBean.setLongitude(lng);
        contentDictBean.setAddressName(address);
        locationMessageBean.setContentDict(contentDictBean);
        MessageInfo customMessage = MessageInfoUtil.buildCustomMessage(GsonUtil.getInstance().toJson(locationMessageBean));
        //MessageInfo messageInfo = MessageHelper.buildCustomMessage(GsonUtil.getInstance().toJson(locationMessageBean));
        ChatManagerHelper.getInstance().getChatMangerKit().sendMessage(customMessage, false, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                callBack.onSuccess(data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                callBack.onError(module,errCode,errMsg);
            }
        });
    }

    public void insertGroupMessage(String groupId,String msg) {
        SystemTipsMessageBean systemTipsMessageBean = new SystemTipsMessageBean();
        SystemTipsMessageBean.ContentDictBean contentDict = new SystemTipsMessageBean.ContentDictBean();
        contentDict.setSelfShowString(msg);
        contentDict.setOtherShowString(msg);
        contentDict.setContTitle(msg);
        systemTipsMessageBean.setContentDict(contentDict);
        String tips = GsonUtil.getInstance().toJson(systemTipsMessageBean);
        V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createCustomMessage(tips.getBytes());
        V2TIMManager.getMessageManager().insertGroupMessageToLocalStorage(v2TIMMessage,groupId,MyApp.uid, new V2TIMValueCallback<V2TIMMessage>() {
            @Override
            public void onError(int code, String desc) {
                LogUtil.d("插入失败 " + desc);
            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
                LogUtil.d("插入成功 ");
                EventBus.getDefault().post(new RefreshMessageBean());
                //MyApp.getInstance().startActivity(new Intent(MyApp.instance(), TempActivity.class));
            }
        });
    }


    public void insertC2CMessage(String userId,String msg) {
        SystemTipsMessageBean systemTipsMessageBean = new SystemTipsMessageBean();
        SystemTipsMessageBean.ContentDictBean contentDict = new SystemTipsMessageBean.ContentDictBean();
        contentDict.setSelfShowString(msg);
        contentDict.setOtherShowString(msg);
        systemTipsMessageBean.setContentDict(contentDict);
        String tips = GsonUtil.getInstance().toJson(systemTipsMessageBean);
        V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createCustomMessage(tips.getBytes());
        V2TIMManager.getMessageManager().insertC2CMessageToLocalStorage(v2TIMMessage, userId,v2TIMMessage.getSender() , new V2TIMValueCallback<V2TIMMessage>() {
            @Override
            public void onError(int code, String desc) {
                LogUtil.d("插入失败 " + desc);
            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
                LogUtil.d("插入成功 ");

            }
        });
    }

    /**
     * 非聊天页面 - 发送自定义消息
     * @param message
     * @param isGroup
     * @param id
     * @param callBack
     */
    public void sendNormalOutMessage(final MessageInfo message, boolean isGroup,String id, final IUIKitCallBack callBack) {
//        if (!safetyCall()) {
//            TUIKitLog.w(TAG, "sendMessage unSafetyCall");
//            return;
//        }
        if (message == null || message.getStatus() == MessageInfo.MSG_STATUS_SENDING) {
            return;
        }
        message.setSelf(true);
        message.setRead(true);
        //assembleGroupMessage(message);

        OfflineMessageContainerBean containerBean = new OfflineMessageContainerBean();
        OfflineMessageBean entity = new OfflineMessageBean();
        entity.content = message.getExtra().toString();
        entity.sender = message.getFromUser();
        entity.nickname = TUIKitConfigs.getConfigs().getGeneralConfig().getUserNickname();
        entity.faceUrl = TUIKitConfigs.getConfigs().getGeneralConfig().getUserFaceUrl();
        containerBean.entity = entity;

        String userID = "";
        String groupID = "";
        //boolean isGroup = false;
        if (isGroup) {
            groupID = id;
            isGroup = true;
            entity.chatType = V2TIMConversation.V2TIM_GROUP;
            entity.sender = groupID;
        } else {
            userID = id;
        }

        V2TIMOfflinePushInfo v2TIMOfflinePushInfo = new V2TIMOfflinePushInfo();
        v2TIMOfflinePushInfo.setExt(new Gson().toJson(containerBean).getBytes());
        // OPPO必须设置ChannelID才可以收到推送消息，这个channelID需要和控制台一致
        v2TIMOfflinePushInfo.setAndroidOPPOChannelID("tuikit");

        V2TIMMessage v2TIMMessage = message.getTimMessage();
        String msgID = V2TIMManager.getMessageManager().sendMessage(v2TIMMessage, isGroup ? null : userID, isGroup ? groupID : null,
                V2TIMMessage.V2TIM_PRIORITY_DEFAULT, false, v2TIMOfflinePushInfo, new V2TIMSendCallback<V2TIMMessage>() {
                    @Override
                    public void onProgress(int progress) {

                    }

                    @Override
                    public void onError(int code, String desc) {
                        TUIKitLog.v(TAG, "sendMessage fail:" + code + "=" + desc);
//                        if (!safetyCall()) {
//                            TUIKitLog.w(TAG, "sendMessage unSafetyCall");
//                            return;
//                        }
                        if (callBack != null) {
                            callBack.onError(TAG, code, desc);
                        }
                        message.setStatus(MessageInfo.MSG_STATUS_SEND_FAIL);
                        //mCurrentProvider.updateMessageInfo(message);
                    }

                    @Override
                    public void onSuccess(V2TIMMessage v2TIMMessage) {
                        TUIKitLog.v(TAG, "sendMessage onSuccess:" + v2TIMMessage.getMsgID());
//                        if (!safetyCall()) {
//                            TUIKitLog.w(TAG, "sendMessage unSafetyCall");
//                            return;
//                        }
                        if (callBack != null) {
                            callBack.onSuccess(v2TIMMessage);
                        }
                        message.setStatus(MessageInfo.MSG_STATUS_SEND_SUCCESS);
                        message.setMsgTime(v2TIMMessage.getTimestamp());
                        EventBus.getDefault().post(new RefreshMessageBean());
                        //mCurrentProvider.updateMessageInfo(message);
                    }
                });

        //消息先展示，通过状态来确认发送是否成功
        TUIKitLog.i(TAG, "sendMessage msgID:" + msgID);
        message.setId(msgID);
        if (message.getMsgType() < MessageInfo.MSG_TYPE_TIPS) {
            message.setStatus(MessageInfo.MSG_STATUS_SENDING);
//            if (retry) {
//                mCurrentProvider.resendMessageInfo(message);
//            } else {
//                mCurrentProvider.addMessageInfo(message);
//            }
        }
    }

}