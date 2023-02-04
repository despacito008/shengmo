package com.aiwujie.shengmo.tim.helper.chatcontroller;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.tim.helper.GiftUIController;
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
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageCustomHolder;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GiftChatController implements TUIChatControllerListener {

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

            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMsgType(GiftMessageInfo.MSG_TYPE_GIFT);
            messageInfo.setExtra("[礼物]");
            MessageInfoUtil.setMessageInfoCommonAttributes(messageInfo, timMessage);
            return messageInfo;
        }
        return null;
    }

    @Override
    public IBaseViewHolder createCommonViewHolder(ViewGroup parent, int viewType) {
        if (viewType != GiftMessageInfo.MSG_TYPE_GIFT) {
            return null;
        }
        if (parent == null) {
            return null;
        }
        LayoutInflater inflater = LayoutInflater.from(TUIKit.getAppContext());
        View contentView = inflater.inflate(R.layout.message_adapter_item_content, parent, false);
        return new GiftViewHolder(contentView);

    }

    @Override
    public boolean bindCommonViewHolder(IBaseViewHolder baseViewHolder, IBaseInfo baseInfo, int position) {
        if (baseViewHolder instanceof ICustomMessageViewGroup && baseInfo instanceof GiftChatController) {
            ICustomMessageViewGroup customHolder = (ICustomMessageViewGroup) baseViewHolder;
            MessageInfo msg = (MessageInfo) baseInfo;
            new CustomMessageDraw().onDraw(customHolder, msg, position);
            return true;
        }
        return false;
    }

    static class GiftViewHolder extends MessageCustomHolder {

        public GiftViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class GiftMessageInfo extends MessageInfo {
        public static final int MSG_TYPE_GIFT = 100010;
    }

    public static class GiftConversationController implements TUIConversationControllerListener {

        @Override
        public CharSequence getConversationDisplayString(IBaseInfo baseInfo) {
            if (baseInfo instanceof GiftChatController.GiftMessageInfo) {
                return "[礼物]";
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
            if (info.getTimMessage().getElemType() != V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM) {
                return;
            }
            V2TIMCustomElem elem = info.getTimMessage().getCustomElem();
            try {
                JSONObject jsonObject = new JSONObject(new String(elem.getData()));
                if (jsonObject.has("costomMassageType")) {
                    String type = jsonObject.optString("costomMassageType");
                    GiftUIController.onDraw(parent,new String(elem.getData()), info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
