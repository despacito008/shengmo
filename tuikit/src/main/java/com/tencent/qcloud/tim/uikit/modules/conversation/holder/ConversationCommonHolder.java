package com.tencent.qcloud.tim.uikit.modules.conversation.holder;

import android.graphics.Color;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.TUIKitListenerManager;
import com.tencent.qcloud.tim.uikit.base.TUIConversationControllerListener;
import com.tencent.qcloud.tim.uikit.component.face.FaceManager;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationIconView;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.DraftInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.utils.DateTimeUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitFileProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConversationCommonHolder extends ConversationBaseHolder {

    public ConversationIconView conversationIconView;
    protected LinearLayout leftItemLayout;
    protected TextView titleText;
    protected TextView messageText;
    protected TextView timelineText;
    protected TextView unreadText;
    protected TextView atInfoText;
    protected ImageView disturbView;
    protected TextView readDoneText;
    protected ImageView identityView, liveStateView, ivGroupSign;
    private String mHighStatus = "";
    private String targetHighStatus = "";
    private String targetHighNum = "";

    public ConversationCommonHolder(View itemView) {
        super(itemView);
        leftItemLayout = rootView.findViewById(R.id.item_left);
        conversationIconView = rootView.findViewById(R.id.conversation_icon);
        titleText = rootView.findViewById(R.id.conversation_title);
        messageText = rootView.findViewById(R.id.conversation_last_msg);
        timelineText = rootView.findViewById(R.id.conversation_time);
        unreadText = rootView.findViewById(R.id.conversation_unread);
        atInfoText = rootView.findViewById(R.id.conversation_at_msg);
        disturbView = rootView.findViewById(R.id.not_disturb);
        identityView = rootView.findViewById(R.id.conversation_identity);
        liveStateView = rootView.findViewById(R.id.conversation_live_state);
        readDoneText = rootView.findViewById(R.id.conversation_read_done);
        ivGroupSign = rootView.findViewById(R.id.conversation_group_sign);
//        getUserHighInfo();
    }

    public void getUserHighInfo() {
        Log.v("getUserHighInfo", V2TIMManager.getInstance().getLoginUser());
        V2TIMManager.getInstance().getUsersInfo(Arrays.asList(V2TIMManager.getInstance().getLoginUser()), new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                Map<String, byte[]> customInfo = v2TIMUserFullInfos.get(0).getCustomInfo();
                byte[] high_status = customInfo.get("IsTop");
                // Log.d("logUtil","liveRole == null" + (live_role == null));
                if (high_status != null) {
                    mHighStatus = new String(high_status);
                    Log.d("logUtil", "id = " + v2TIMUserFullInfos.get(0).getUserID() + ",role = " + mHighStatus);
                }
            }
        });
    }

    public void layoutViews(final ConversationInfo conversation, int position) {
        MessageInfo lastMsg = conversation.getLastMessage();
        if (lastMsg != null && lastMsg.getStatus() == MessageInfo.MSG_STATUS_REVOKE) {
            if (lastMsg.isSelf()) {
                lastMsg.setExtra(TUIKit.getAppContext().getString(R.string.revoke_tips_you));
            } else if (lastMsg.isGroup()) {
                String message = TUIKitConstants.covert2HTMLString(
                        TextUtils.isEmpty(lastMsg.getGroupNameCard())
                                ? lastMsg.getFromUser()
                                : lastMsg.getGroupNameCard());
                lastMsg.setExtra(message + TUIKit.getAppContext().getString(R.string.revoke_tips));
            } else {
                lastMsg.setExtra(TUIKit.getAppContext().getString(R.string.revoke_tips_other));
            }
        }

        if (conversation.isTop()) {
            leftItemLayout.setBackgroundColor(rootView.getResources().getColor(R.color.conversation_top_color));
        } else {
            leftItemLayout.setBackgroundColor(Color.WHITE);
        }




        messageText.setText("");
        timelineText.setText("");
        titleText.setTextColor(Color.parseColor("#181818"));
        //        identityView.setVisibility(View.INVISIBLE);
        if (conversation.isGroup()) {  //群组
            titleText.setText(conversation.getTitle());
            ivGroupSign.setVisibility(View.VISIBLE);
            liveStateView.setVisibility(View.INVISIBLE);
            identityView.setVisibility(View.INVISIBLE);
            if (conversation.getId().startsWith("fs")) {
                titleText.setTextColor(Color.parseColor("#ff0000"));
                titleText.setText(conversation.getTitle() + "粉丝团");
            }
            V2TIMManager.getGroupManager().getGroupsInfo(Arrays.asList(conversation.getId()), new V2TIMValueCallback<List<V2TIMGroupInfoResult>>() {
                @Override
                public void onSuccess(List<V2TIMGroupInfoResult> v2TIMGroupInfoResults) {
                    if (v2TIMGroupInfoResults.size() == 0) {
                        return;
                    }
                    Map<String, byte[]> customInfo = v2TIMGroupInfoResults.get(0).getGroupInfo().getCustomInfo();
                    byte[] group_roles = customInfo.get("Group_Role");
                    if (group_roles != null) {
                        String role = new String(group_roles);
                        if (role.equals("2")) {
                            identityView.setVisibility(View.VISIBLE);
                            identityView.setImageResource(R.drawable.user_manager);
                        } else {
                            identityView.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                @Override
                public void onError(int code, String desc) {

                }
            });
        } else {  //单聊
            ivGroupSign.setVisibility(View.GONE);
            identityView.setVisibility(View.INVISIBLE);
            liveStateView.setVisibility(View.INVISIBLE);
            titleText.setText(conversation.getTitle());
            V2TIMManager.getInstance().getUsersInfo(Arrays.asList(conversation.getId()), new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
                @Override
                public void onError(int code, String desc) {

                }

                @Override
                public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                    //titleText.setText(v2TIMUserFullInfos.get(0).getRole() + "---");

                    int role = v2TIMUserFullInfos.get(0).getRole();

                    Map<String, byte[]> customInfo = v2TIMUserFullInfos.get(0).getCustomInfo();
                    byte[] live_role = customInfo.get("LiveRole");
                    byte[] high_status = customInfo.get("IsTop");
                    byte[] highNum = customInfo.get("TopNum");


                    switch (role) {
                        case 2:
                        case 3:
                        case 4:
                            Glide.with(TUIKit.getAppContext()).load(R.drawable.user_manager).into(identityView);
                            identityView.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            break;
                        case 6:
                            Glide.with(TUIKit.getAppContext()).load(R.drawable.user_svip_year).into(identityView);
                            identityView.setVisibility(View.VISIBLE);
                            break;
                        case 7:
                            Glide.with(TUIKit.getAppContext()).load(R.drawable.user_svip).into(identityView);
                            identityView.setVisibility(View.VISIBLE);
                            break;
                        case 8:
                            Glide.with(TUIKit.getAppContext()).load(R.drawable.user_vip_year).into(identityView);
                            identityView.setVisibility(View.VISIBLE);
                            break;
                        case 9:
                            Glide.with(TUIKit.getAppContext()).load(R.drawable.user_vip).into(identityView);
                            identityView.setVisibility(View.VISIBLE);
                            break;
                        default:
                            identityView.setVisibility(View.INVISIBLE);
                            break;
                    }

                    if (live_role != null) {
                        String liveRole = new String(live_role);
                        switch (liveRole) {
                            case "":
                            case "0":
                                liveStateView.setVisibility(View.INVISIBLE);
                                break;
                            case "1":
                                liveStateView.setVisibility(View.VISIBLE);
                                Glide.with(TUIKit.getAppContext()).load(R.drawable.ic_user_liver).into(liveStateView);
                                break;
                            case "2":
                                liveStateView.setVisibility(View.VISIBLE);
                                Glide.with(TUIKit.getAppContext()).load(R.drawable.ic_user_living).into(liveStateView);
                                break;
                            default:
                                liveStateView.setVisibility(View.INVISIBLE);
                                break;
                        }
                    } else {
                        liveStateView.setVisibility(View.INVISIBLE);
                    }


                }
            });
        }
        DraftInfo draftInfo = conversation.getDraft();
        if (draftInfo != null && !TextUtils.isEmpty(draftInfo.getDraftText())) {
            messageText.setText(draftInfo.getDraftText());
            timelineText.setText(DateTimeUtil.getTimeFormatText(new Date(draftInfo.getDraftTime() * 1000)));
        } else {
            if (lastMsg != null) {
                // 如果最后一条消息是自定义消息，由消息创建者决定显示什么字符
                if (lastMsg.getMsgType() > MessageInfo.MSG_STATUS_REVOKE) {
                    for (TUIConversationControllerListener conversationListener : TUIKitListenerManager.getInstance().getTUIConversationListeners()) {
                        CharSequence displayStr = conversationListener.getConversationDisplayString(lastMsg);
                        if (displayStr != null) {
                            messageText.setText(displayStr);
                            messageText.setTextColor(rootView.getResources().getColor(R.color.list_bottom_text_bg));
                            break;
                        }
                    }
                } else {
                    if (lastMsg.getExtra() != null) {
                        String result = emojiJudge(lastMsg.getExtra().toString());
                        messageText.setText(Html.fromHtml(result));
                        messageText.setTextColor(rootView.getResources().getColor(R.color.list_bottom_text_bg));
                    }
                }
                timelineText.setText(DateTimeUtil.getTimeFormatText(new Date(lastMsg.getMsgTime() * 1000)));
            }
        }

        if (conversation.getUnRead() > 0 && !conversation.isShowDisturbIcon()) {
            unreadText.setVisibility(View.VISIBLE);
            if (conversation.getUnRead() > 99999) {
                unreadText.setText("99999+");
            } else {
                unreadText.setText("" + conversation.getUnRead());
            }
        } else {
            unreadText.setVisibility(View.GONE);
        }

        if (draftInfo != null && !TextUtils.isEmpty(draftInfo.getDraftText())) {
            atInfoText.setVisibility(View.VISIBLE);
            atInfoText.setText(R.string.drafts);
            atInfoText.setTextColor(Color.RED);
        } else {
            if (conversation.getAtInfoText().isEmpty()) {
                atInfoText.setVisibility(View.GONE);
            } else {
                atInfoText.setVisibility(View.VISIBLE);
                atInfoText.setText(conversation.getAtInfoText());
                atInfoText.setTextColor(Color.RED);
            }
        }

        conversationIconView.setRadius(mAdapter.getItemAvatarRadius());
        if (mAdapter.getItemDateTextSize() != 0) {
            timelineText.setTextSize(mAdapter.getItemDateTextSize());
        }
        if (mAdapter.getItemBottomTextSize() != 0) {
            messageText.setTextSize(mAdapter.getItemBottomTextSize());
        }
        if (mAdapter.getItemTopTextSize() != 0) {
            titleText.setTextSize(mAdapter.getItemTopTextSize());
        }
        if (!mAdapter.hasItemUnreadDot()) {
            unreadText.setVisibility(View.GONE);
        }

        if (conversation.getIconUrlList() != null) {
            conversationIconView.setConversation(conversation);
        }

        if (conversation.isShowDisturbIcon()) {
            disturbView.setVisibility(View.VISIBLE);
        } else {
            disturbView.setVisibility(View.GONE);
        }

        if (!conversation.isGroup() && lastMsg != null) {
            if (conversation.getUnRead() == 0 && lastMsg.isPeerRead()) {
                unreadText.setVisibility(View.GONE);
                readDoneText.setVisibility(View.VISIBLE);
            } else {
                readDoneText.setVisibility(View.GONE);
            }
        } else {
            readDoneText.setVisibility(View.GONE);
        }


//        if (lastMsg != null && TextUtils.isEmpty(lastMsg.getTimMessage().getCloudCustomData())) {
//            identityView.setVisibility(View.VISIBLE);
//        } else {
//            identityView.setVisibility(View.INVISIBLE);
//        }

        //// 由子类设置指定消息类型的views
        layoutVariableViews(conversation, position);
    }


    private static String emojiJudge(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }

        String[] emojiList = FaceManager.getEmojiFilters();
        if (emojiList == null || emojiList.length == 0) {
            return text;
        }

        SpannableStringBuilder sb = new SpannableStringBuilder(text);
        String regex = "\\[(\\S+?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        ArrayList<EmojiData> emojiDataArrayList = new ArrayList<>();
        //遍历找到匹配字符并存储
        int lastMentionIndex = -1;
        while (m.find()) {
            String emojiName = m.group();
            int start;
            if (lastMentionIndex != -1) {
                start = text.indexOf(emojiName, lastMentionIndex);
            } else {
                start = text.indexOf(emojiName);
            }
            int end = start + emojiName.length();
            lastMentionIndex = end;

            int index = findeEmoji(emojiName);
            String[] emojiListValues = FaceManager.getEmojiFiltersValues();
            if (index != -1 && emojiListValues != null && emojiListValues.length >= index) {
                emojiName = emojiListValues[index];
            }


            EmojiData emojiData = new EmojiData();
            emojiData.setStart(start);
            emojiData.setEnd(end);
            emojiData.setEmojiText(emojiName);

            emojiDataArrayList.add(emojiData);
        }

        //倒叙替换
        if (emojiDataArrayList.isEmpty()) {
            return text;
        }
        for (int i = emojiDataArrayList.size() - 1; i >= 0; i--) {
            EmojiData emojiData = emojiDataArrayList.get(i);
            String emojiName = emojiData.getEmojiText();
            int start = emojiData.getStart();
            int end = emojiData.getEnd();

            if (!TextUtils.isEmpty(emojiName) && start != -1 && end != -1) {
                sb.replace(start, end, emojiName);
            }
        }
        return sb.toString();
    }

    private static int findeEmoji(String text) {
        int result = -1;
        if (TextUtils.isEmpty(text)) {
            return result;
        }

        String[] emojiList = FaceManager.getEmojiFilters();
        if (emojiList == null || emojiList.length == 0) {
            return result;
        }

        for (int i = 0; i < emojiList.length; i++) {
            if (text.equals(emojiList[i])) {
                result = i;
                break;
            }
        }

        return result;
    }

    public void layoutVariableViews(ConversationInfo conversationInfo, int position) {

    }

    private static class EmojiData {
        private int start;
        private int end;
        private String emojiText;

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public String getEmojiText() {
            return emojiText;
        }

        public void setEmojiText(String emojiText) {
            this.emojiText = emojiText;
        }
    }
}
