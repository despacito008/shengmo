package com.aiwujie.shengmo.tim.helper;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.tim.bean.LiveAnchorMessageBean;
import com.aiwujie.shengmo.tim.utils.CustomMessageType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sina.weibo.sdk.utils.LogUtil;
import com.tencent.imsdk.v2.V2TIMCustomElem;
import com.tencent.imsdk.v2.V2TIMMessage;

import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IBaseAction;
import com.tencent.qcloud.tim.uikit.base.IBaseInfo;
import com.tencent.qcloud.tim.uikit.base.IBaseViewHolder;
import com.tencent.qcloud.tim.uikit.base.TUIChatControllerListener;
import com.tencent.qcloud.tim.uikit.base.TUIConversationControllerListener;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.IOnCustomMessageDrawListener;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageBaseHolder;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageCustomHolder;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HelloChatController implements TUIChatControllerListener {
    private static final String TAG = HelloChatController.class.getSimpleName();

    @Override
    public List<IBaseAction> onRegisterMoreActions() {
        return null;
    }

    @Override
    public IBaseInfo createCommonInfoFromTimMessage(V2TIMMessage timMessage) {
        if (timMessage.getElemType() == V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM) {
            V2TIMCustomElem customElem = timMessage.getCustomElem();
            if (customElem == null || customElem.getData() == null) {
                return null;
            }
            try {
                JSONObject jsonObject = new JSONObject(new String(customElem.getData()));
                if (jsonObject.has("costomMassageType")) {
                    String type = jsonObject.optString("costomMassageType");
                    return createCustomMessage(type,timMessage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            CustomHelloMessage helloMessage = null;
//            try {
//                helloMessage = new Gson().fromJson(new String(customElem.getData()), CustomHelloMessage.class);
//            } catch (Exception e) {
//                LogUtil.w(TAG, "invalid json: " + new String(customElem.getData()) + " " + e.getMessage());
//            }
//            if (helloMessage != null && TextUtils.equals(helloMessage.businessID, TUIKitConstants.BUSINESS_ID_CUSTOM_HELLO)) {
//                MessageInfo messageInfo = new HelloMessageInfo();
//                messageInfo.setMsgType(HelloMessageInfo.MSG_TYPE_HELLO);
//                MessageInfoUtil.setMessageInfoCommonAttributes(messageInfo, timMessage);
//                Context context = TUIKit.getAppContext();
//                if (context != null) {
//                    messageInfo.setExtra(context.getString(R.string.custom_msg));
//                }
//                return messageInfo;
//            }
        }
//        else if (timMessage.getElemType() == V2TIMMessage.V2TIM_ELEM_TYPE_TEXT) {
//            String customElem = timMessage.getCloudCustomData();
//            if (TextUtil.isEmpty(customElem)) {
//                return null;
//            }
//            try {
//                JSONObject jsonObject = new JSONObject(customElem);
//                if (jsonObject.has("costomMassageType")) {
//                    String type = jsonObject.optString("costomMassageType");
//                    return createCustomMessage(type,timMessage);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        return null;
    }

    @Override
    public IBaseViewHolder createCommonViewHolder(ViewGroup parent, int viewType) {
        if (viewType != HelloMessageInfo.MSG_TYPE_HELLO) {
            return null;
        }
        if (parent == null) {
            return null;
        }
        LayoutInflater inflater = LayoutInflater.from(TUIKit.getAppContext());
        View contentView = inflater.inflate(R.layout.message_adapter_item_content, parent, false);
        return new HelloViewHolder(contentView);
    }

    @Override
    public boolean bindCommonViewHolder(IBaseViewHolder baseViewHolder, IBaseInfo baseInfo, int position) {
        if (baseViewHolder instanceof ICustomMessageViewGroup && baseInfo instanceof MessageInfo) {
            ICustomMessageViewGroup customHolder = (ICustomMessageViewGroup) baseViewHolder;
            MessageInfo msg = (MessageInfo) baseInfo;
            new CustomMessageDraw().onDraw(customHolder, msg, position);
            return true;
        }
        return false;
    }

    static class HelloMessageInfo extends MessageInfo {
        public static final int MSG_TYPE_HELLO = 100002;
    }

    static class LocationMessageInfo extends MessageInfo {
        public static final int MSG_TYPE_LOCATION = 100003;
    }

    static class HelloViewHolder extends MessageCustomHolder {

        public HelloViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class HelloConversationController implements TUIConversationControllerListener {

        @Override
        public CharSequence getConversationDisplayString(IBaseInfo baseInfo) {
            if (baseInfo instanceof HelloChatController.HelloMessageInfo) {
                //return "[自定义消息]";
                return String.valueOf((((HelloMessageInfo) baseInfo).getExtra()));
                //return MyApp.instance().getString(R.string.welcome_tip);
            }
            return null;
        }
    }

    public static class CustomMessageDraw implements IOnCustomMessageDrawListener {

        /**
         * 自定义消息渲染时，会调用该方法，本方法实现了自定义消息的创建，以及交互逻辑
         *
         * @param parent 自定义消息显示的父View，需要把创建的自定义消息view添加到parent里
         * @param info   消息的具体信息
         */
        @Override
        public void onDraw(ICustomMessageViewGroup parent, MessageInfo info, int position) {
            // 获取到自定义消息的json数据
            if (info.getTimMessage().getElemType() != V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM  && TextUtil.isEmpty(info.getTimMessage().getCloudCustomData())) {
                return;
            }
            if (info.getTimMessage().getElemType() == V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM) { //普通自定义消息
                V2TIMCustomElem elem = info.getTimMessage().getCustomElem();
                try {
                    JSONObject jsonObject = new JSONObject(new String(elem.getData()));
                    if (jsonObject.has("costomMassageType")) {
                        String type = jsonObject.optString("costomMassageType");
                        customerDraw(type, new String(elem.getData()), parent, info, position);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            else if (!TextUtil.isEmpty(info.getTimMessage().getCloudCustomData())) { //带CloudCustomData的文字消息
//                String customData = info.getTimMessage().getCloudCustomData();
//                try {
//                    JSONObject jsonObject = new JSONObject(customData);
//                    if (jsonObject.has("costomMassageType")) {
//                        String type = jsonObject.optString("costomMassageType");
//                        customerDraw(type, customData, parent, info, position);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    static void customerDraw(String type, String data, ICustomMessageViewGroup parent, MessageInfo info,int position) {
        switch (type) {
            case CustomMessageType.TYPE_RED_ENVELOPES:
                RedEnvelopesUIController.onDraw(parent, data, info);
                break;
            case CustomMessageType.TYPE_SYSTEM_TIPS:
                SystemTipsUIController.onDraw(parent, data, info);
                break;
            case CustomMessageType.TYPE_GIFT:
                GiftUIController.onDraw(parent, data, info);
                break;
            case CustomMessageType.TYPE_FLASH:
                FlashUIController.onDraw(parent, data,info,position);
                break;
            case CustomMessageType.TYPE_LOCATION:
                LocationUIController.onDraw(parent, data,info,position);
                break;
            case CustomMessageType.TYPE_DYNAMIC:
                DynamicUIController.onDraw(parent, data,info,position);
                break;
            case CustomMessageType.TYPE_GROUP_INVITE:
                GroupInviteUIController.onDraw(parent, data,info,position);
                break;
            case CustomMessageType.TYPE_LIVE_ANCHOR:
                LiveAnchorUIController.onDraw(parent, data,info,position);
                break;
            case CustomMessageType.TYPE_SYSTEM_RICH_TEXT:
                SystemRichTextUIController.onDraw(parent,data,info);
                break;
        }
    }

    static MessageInfo createCustomMessage(String type, V2TIMMessage timMessage) {
        MessageInfo messageInfo = new HelloMessageInfo();
        messageInfo.setMsgType(HelloMessageInfo.MSG_TYPE_HELLO);
        MessageInfoUtil.setMessageInfoCommonAttributes(messageInfo, timMessage);
        switch (type) {
            case CustomMessageType.TYPE_RED_ENVELOPES:
                messageInfo.setExtra("[红包]");
                break;
            case CustomMessageType.TYPE_SYSTEM_TIPS:
                messageInfo.setExtra("[提示]");
                break;
            case CustomMessageType.TYPE_GIFT:
                messageInfo.setExtra("[礼物]");
                break;
            case CustomMessageType.TYPE_FLASH:
                messageInfo.setExtra("[闪图]");
                break;
            case CustomMessageType.TYPE_LOCATION:
                messageInfo.setExtra("[位置]");
                break;
            case CustomMessageType.TYPE_DYNAMIC:
                messageInfo.setExtra("[推荐]");
                break;
            case CustomMessageType.TYPE_GROUP_INVITE:
                messageInfo.setExtra("[群组邀请]");
                break;
            case CustomMessageType.TYPE_LIVE_ANCHOR:
                messageInfo.setExtra("[直播推荐]");
                break;
            case CustomMessageType.TYPE_SYSTEM_RICH_TEXT:
                messageInfo.setExtra("[通知消息]");
                break;
            default:
                messageInfo.setExtra("[自定义消息]");
                break;
        }
        return messageInfo;
    }
}

