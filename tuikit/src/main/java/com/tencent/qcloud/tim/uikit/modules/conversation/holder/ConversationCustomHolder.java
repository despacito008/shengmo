package com.tencent.qcloud.tim.uikit.modules.conversation.holder;

import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.base.TUIConversationControllerListener;
import com.tencent.qcloud.tim.uikit.base.TUIKitListenerManager;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationIconView;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.DraftInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.utils.DateTimeUtil;

import java.util.Date;

/**
 * 自定义会话Holder
 */
public class ConversationCustomHolder extends ConversationBaseHolder {

    protected LinearLayout leftItemLayout;
    protected TextView titleText;
    protected TextView messageText;
    protected TextView timelineText;
    protected TextView unreadText;
    protected ConversationIconView conversationIconView;


    public ConversationCustomHolder(View itemView) {
        super(itemView);
        leftItemLayout = rootView.findViewById(R.id.item_left);
        conversationIconView = rootView.findViewById(R.id.conversation_icon);
        titleText = rootView.findViewById(R.id.conversation_title);
        messageText = rootView.findViewById(R.id.conversation_last_msg);
        timelineText = rootView.findViewById(R.id.conversation_time);
        unreadText = rootView.findViewById(R.id.conversation_unread);
    }

    @Override
    public void layoutViews(ConversationInfo conversation, int position) {
        if (conversation.isTop()) {
            leftItemLayout.setBackgroundColor(rootView.getResources().getColor(R.color.conversation_top_color));
        } else {
            leftItemLayout.setBackgroundColor(Color.WHITE);
        }
        conversationIconView.setConversation(conversation);

        titleText.setText(conversation.getTitle());
        messageText.setText("");
        timelineText.setText("");

//        DraftInfo draftInfo = conversation.getDraft();
//        if (draftInfo != null && !TextUtils.isEmpty(draftInfo.getDraftText())) {
//            messageText.setText(draftInfo.getDraftText());
//            timelineText.setText(DateTimeUtil.getTimeFormatText(new Date(draftInfo.getDraftTime() * 1000)));
//        } else {
//            MessageInfo lastMsg = conversation.getLastMessage();
//            if (lastMsg != null) {
//                timelineText.setText(DateTimeUtil.getTimeFormatText(new Date(lastMsg.getMsgTime() * 1000)));
//            }
//        }

        if (conversation.getUnRead() > 0) {
            unreadText.setVisibility(View.VISIBLE);
            if (conversation.getUnRead() > 99) {
                unreadText.setText("99+");
            } else {
                unreadText.setText("" + conversation.getUnRead());
            }
        } else {
            unreadText.setVisibility(View.GONE);
        }

        if (mAdapter.getItemDateTextSize() != 0) {
            timelineText.setTextSize(mAdapter.getItemDateTextSize());
        }
        if (mAdapter.getItemBottomTextSize() != 0) {
            messageText.setTextSize(mAdapter.getItemBottomTextSize());
        }
        if (mAdapter.getItemTopTextSize() != 0) {
            titleText.setTextSize(mAdapter.getItemTopTextSize());
        }

    }

}
