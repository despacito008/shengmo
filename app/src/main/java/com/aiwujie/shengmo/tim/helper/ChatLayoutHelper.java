package com.aiwujie.shengmo.tim.helper;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.MainActivity;
import com.aiwujie.shengmo.activity.SendredbaoActivity;
import com.aiwujie.shengmo.activity.SendredbaopersonActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.CallStateBean;
import com.aiwujie.shengmo.bean.ConversationUserInfoBean;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.customview.BindGuanzhuDialog;
import com.aiwujie.shengmo.customview.DashangDialogNew2;
import com.aiwujie.shengmo.customview.DashangDialogNew3;
import com.aiwujie.shengmo.customview.DashangDialogNew4;
import com.aiwujie.shengmo.customview.VipDialog;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.C2CSendRedEnvelopesActivity;
import com.aiwujie.shengmo.kt.ui.activity.GroupSendRedEnvelopesActivity;
import com.aiwujie.shengmo.kt.ui.view.GiftPanelPop;
import com.aiwujie.shengmo.kt.ui.view.GiftPanelPop2;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.tim.chat.ChatActivity;
import com.aiwujie.shengmo.tim.location.SelectLocationActivity;
import com.aiwujie.shengmo.tim.utils.CustomMessageType;
import com.aiwujie.shengmo.utils.DensityUtil;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;


import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMCustomElem;
import com.tencent.imsdk.v2.V2TIMGroupManager;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.liteav.ui.TRTCAudioCallActivity;
import com.tencent.liteav.ui.TRTCVideoCallActivity;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.helper.TUIKitLiveChatController;
import com.tencent.qcloud.tim.tuikit.live.utils.TUILiveLog;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.TUIChatControllerListener;
import com.tencent.qcloud.tim.uikit.base.TUIKitListenerManager;
import com.tencent.qcloud.tim.uikit.component.NoticeLayout;
import com.tencent.qcloud.tim.uikit.component.face.CustomFace;
import com.tencent.qcloud.tim.uikit.component.face.CustomFaceGroup;
import com.tencent.qcloud.tim.uikit.component.face.Emoji;
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.GroupChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.chat.base.InputMoreActionUnit;
import com.tencent.qcloud.tim.uikit.modules.chat.base.InputMoreActionUnit.OnActionClickListener;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayoutUI;

import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.IOnCustomMessageDrawListener;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.GroupPrincipal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;


public class ChatLayoutHelper {

    private static final String TAG = ChatLayoutHelper.class.getSimpleName();

    private Context mContext;
    private ChatInfo mChatInfo;
    private ChatLayout mChatLayout;
    //List<RedEnvelopesBean.DataBean> mRedEnvelopesList;
    private String mUserInfo;

    public ChatLayoutHelper(Context context, ChatInfo chatInfo, String userInfo) {
        mContext = context;
        mChatInfo = chatInfo;
        mUserInfo = userInfo;
        //mRedEnvelopesList = redEnvelopesList;
    }

    public void customizeChatLayout(final ChatLayout layout) {
        this.mChatLayout = layout;
        //CustomAVCallUIController.getInstance().setUISender(layout);

//        //====== NoticeLayout使用范例 ======//
//        NoticeLayout noticeLayout = layout.getNoticeLayout();
//        noticeLayout.alwaysShow(true);
//        noticeLayout.getContent().setText("现在插播一条广告");
//        noticeLayout.getContentExtra().setText("参看有奖");
//        noticeLayout.setOnNoticeClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.toastShortMessage("赏白银五千两");
//            }
//        });

//
        //====== MessageLayout使用范例 ======//
        MessageLayout messageLayout = layout.getMessageLayout();
        //messageLayout.setBackgroundColor(mContext.getResources().getColor(R.color.chatWrite));

//        ////// 设置聊天背景 //////
//        messageLayout.setBackground(new ColorDrawable(0xFFEFE5D4));
//        ////// 设置头像 //////
//        // 设置默认头像，默认与朋友与自己的头像相同
//        messageLayout.setAvatar(R.drawable.ic_more_file);
//        // 设置头像圆角
        messageLayout.setAvatarRadius(50);
//        // 设置头像大小
//        messageLayout.setAvatarSize(new int[]{48, 48});
//
//        ////// 设置昵称样式（对方与自己的样式保持一致）//////
//        messageLayout.setNameFontSize(12);
//        messageLayout.setNameFontColor(0xFF8B5A2B);
//
//        ////// 设置气泡 ///////
//        // 设置自己聊天气泡的背景
//        messageLayout.setRightBubble(new ColorDrawable(0xFFCCE4FC));
//        // 设置朋友聊天气泡的背景
//        messageLayout.setLeftBubble(new ColorDrawable(0xFFE4E7EB));
//
//        ////// 设置聊天内容 //////
//        // 设置聊天内容字体字体大小，朋友和自己用一种字体大小
//        messageLayout.setChatContextFontSize(15);
//        // 设置自己聊天内容字体颜色
//        messageLayout.setRightChatContentFontColor(0xFFA9A9A9);
//        // 设置朋友聊天内容字体颜色
//        messageLayout.setLeftChatContentFontColor(0xFFA020F0);
//
//        ////// 设置聊天时间 //////
//        // 设置聊天时间线的背景
//        messageLayout.setChatTimeBubble(new ColorDrawable(0xFFE4E7EB));
//        // 设置聊天时间的字体大小
//        messageLayout.setChatTimeFontSize(12);
//        // 设置聊天时间的字体颜色
//        messageLayout.setChatTimeFontColor(0xFF7E848C);
//
//        ////// 设置聊天的提示信息 //////
//        // 设置提示的背景
//        messageLayout.setTipsMessageBubble(new ColorDrawable(0xFFE4E7EB));
//        // 设置提示的字体大小
//        messageLayout.setTipsMessageFontSize(12);
//        // 设置提示的字体颜色
//        messageLayout.setTipsMessageFontColor(0xFF7E848C);
//
        // 设置自定义的消息渲染时的回调
       // messageLayout.setOnCustomMessageDrawListener(new CustomMessageDraw());
        //V2TIMManager.getMessageManager().getGroupHistoryMessageList();

        messageLayout.setBackgroundColor(mContext.getResources().getColor(R.color.minGray));

        messageLayout.setRightBubble(mContext.getResources().getDrawable(R.drawable.shape_tim_text_message_self_bg));
        messageLayout.setLeftBubble(mContext.getResources().getDrawable(R.drawable.shape_tim_text_message_other_bg));
        messageLayout.setRightChatContentFontColor(mContext.getResources().getColor(R.color.white));
        messageLayout.setLeftChatContentFontColor(mContext.getResources().getColor(R.color.black));

        //mChatLayout.getTitleBar().setBackgroundColor(mContext.getResources().getColor(R.color.chatWrite));
//
//        // 新增一个PopMenuAction
//        PopMenuAction action = new PopMenuAction();
//        action.setActionName("test");
//        action.setActionClickListener(new PopActionClickListener() {
//            @Override
//            public void onActionClick(int position, Object data) {
//                ToastUtil.toastShortMessage("新增一个pop action");
//            }
//        });
//        messageLayout.addPopAction(action);
//
//        final MessageLayout.OnItemClickListener l = messageLayout.getOnItemClickListener();
//        messageLayout.setOnItemClickListener(new MessageLayout.OnItemClickListener() {
//            @Override
//            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
//                l.onMessageLongClick(view, position, messageInfo);
//                ToastUtil.toastShortMessage("demo中自定义长按item");
//            }
//
//            @Override
//            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
//                l.onUserIconClick(view, position, messageInfo);
//                ToastUtil.toastShortMessage("demo中自定义点击头像");
//            }
//        });


        //====== InputLayout使用范例 ======//
        InputLayout inputLayout = layout.getInputLayout();

        //inputLayout.setVisibility(View.GONE);

//        // TODO 隐藏音频输入的入口，可以打开下面代码测试
//        inputLayout.disableAudioInput(true);
//        // TODO 隐藏表情输入的入口，可以打开下面代码测试
//        inputLayout.disableEmojiInput(true);
//        // TODO 隐藏更多功能的入口，可以打开下面代码测试
//        inputLayout.disableMoreInput(true);
//        // TODO 可以用自定义的事件来替换更多功能的入口，可以打开下面代码测试
//        inputLayout.replaceMoreInput(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.toastShortMessage("自定义的更多功能按钮事件");
//                MessageInfo info = MessageInfoUtil.buildTextMessage("自定义的消息");
//                layout.sendMessage(info, false);
//            }
//        });
//        // TODO 可以用自定义的fragment来替换更多功能，可以打开下面代码测试
//        inputLayout.replaceMoreInput(new CustomInputFragment().setChatLayout(layout));
//
//        // TODO 可以disable更多面板上的各个功能，可以打开下面代码测试

        //inputLayout.disableCaptureAction(true);
        //inputLayout.disableSendFileAction(true);
        //inputLayout.disableSendPhotoAction(true);
        //inputLayout.disableSendPhotoAction(true);
        //inputLayout.disableVideoRecordAction(true);


        //关闭群直播
        //TUIKitConfigs.getConfigs().setEnableGroupLiveEntry(false);
        // TODO 可以自己增加一些功能，可以打开下面代码测试
        // 这里增加一个视频通话
//        InputMoreActionUnit videoCall = new InputMoreActionUnit();
//        videoCall.setIconResId(com.tencent.qcloud.tim.uikit.R.drawable.ic_more_video);
//        videoCall.setTitleId(R.string.video_call);
//        videoCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CustomAVCallUIController.getInstance().createVideoCallRequest();
//            }
//        });
//        inputLayout.addAction(videoCall);

        // 增加一个欢迎提示富文本
//        InputMoreActionUnit unit = new InputMoreActionUnit();
//        unit.setIconResId(R.drawable.edittextbackground);
//        unit.setTitleId(R.string.test_custom_action);
//        unit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Gson gson = new Gson();
//                CustomMessage customMessage = new CustomMessage();
//                String data = gson.toJson(customMessage);
//                MessageInfo info = MessageInfoUtil.buildCustomMessage(data);
//                layout.sendMessage(info, false);
//            }
//        });
        //inputLayout.addAction(unit);
        ChatManagerHelper.getInstance().init(mContext, layout);

        addGiftAction(layout);
        addRedBaoAction(layout);
        addOneTimePicAction(inputLayout);
        addLocationPicAction(layout);
       // mChatLayout.getTitleBar().getMiddleTitle().setText(mChatInfo.getChatName());
        if (mChatInfo.getType() == V2TIMConversation.V2TIM_C2C) {
//            inputLayout.enableAudioCall();
//            inputLayout.enableVideoCall();
            for(TUIChatControllerListener chatListener : TUIKitListenerManager.getInstance().getTUIChatListeners()) {
                if (chatListener instanceof TUIKitLiveChatController) {
                    ((TUIKitLiveChatController)chatListener).setEnableVideoCall(true);
                    ((TUIKitLiveChatController)chatListener).setEnableAudioCall(true);
                }
            }
            showIdentity();
            if (mUserInfo != null) {
                ConversationUserInfoBean conversationUserInfoBean = GsonUtil.GsonToBean(mUserInfo, ConversationUserInfoBean.class);
                if (conversationUserInfoBean.getData().getInfo().getIs_likeliar().equals("1")) {
                    showSuspiciousNotice();
                } else if (conversationUserInfoBean.getData().getFiliation() == 2) {
                    showFollowNotice();
                }
            }
            layout.getInputLayout().setPrepareListener(new InputLayoutUI.OnActionPrepareListener() {
                @Override
                public void OnActionPrepare() {
                    List<InputMoreActionUnit> action = layout.getInputLayout().getAction();

                    for (InputMoreActionUnit actionUnit : action) {
                        if (actionUnit.getTitleId() == R.string.video_call) {
                            actionUnit.setOnClickListener(actionUnit.new OnActionClickListener() {
                                @Override
                                public void onClick() {
                                    //ToastUtil.show(mContext,"ha ha");
                                    //layout.getInputLayout().startVideoCall();
                                    getStateBeforeCall(layout, 1);
                                }
                            });
                        }

                        if (actionUnit.getTitleId() == R.string.audio_call) {
                            actionUnit.setOnClickListener(actionUnit.new OnActionClickListener() {
                                @Override
                                public void onClick() {
                                    //ToastUtil.show(mContext,"ha ha ha");
                                    //layout.getInputLayout().startAudioCall();
                                    getStateBeforeCall(layout, 2);
                                }
                            });
                        }
                    }
                }
            });

        } else {
            ChatManagerHelper.getInstance().getChatMangerKit().getCurrentChatInfo().getChatName();
            for(TUIChatControllerListener chatListener : TUIKitListenerManager.getInstance().getTUIChatListeners()) {
                if (chatListener instanceof TUIKitLiveChatController) {
                    ((TUIKitLiveChatController)chatListener).setEnableVideoCall(false);
                    ((TUIKitLiveChatController)chatListener).setEnableAudioCall(false);
                    ((TUIKitLiveChatController)chatListener).setEnableGroupLiveEntry(false);
                }
            }
        }

        //取到草稿
        if (mChatInfo.getDraft() != null) {
            String draftText = mChatInfo.getDraft().getDraftText();
            mChatLayout.getInputLayout().getInputText().setText(draftText);
            mChatLayout.getInputLayout().getInputText().setSelection(mChatLayout.getInputLayout().getInputText().getText().length());
        }



        // mChatLayout.getTitleBar().setLeftIcon(R.mipmap.return_back);


    }


    public class CustomMessageDraw implements IOnCustomMessageDrawListener {

        /**
         * 自定义消息渲染时，会调用该方法，本方法实现了自定义消息的创建，以及交互逻辑
         *
         * @param parent 自定义消息显示的父View，需要把创建的自定义消息view添加到parent里
         * @param info   消息的具体信息
         */
        @Override
        public void onDraw(ICustomMessageViewGroup parent, MessageInfo info,int positioin) {
            // LogUtil.d("value = " + info.getTIMMessage().getC());
            // 获取到自定义消息的json数据
            if (info.getTimMessage().getElemType() != V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM) {
                return;
            }

            V2TIMCustomElem elem = info.getTimMessage().getCustomElem();
            LogUtil.d(new String(elem.getData()));
            try {
                JSONObject jsonObject = new JSONObject(new String(elem.getData()));
                if (jsonObject.has("costomMassageType")) {
                    String type = jsonObject.optString("costomMassageType");
                    //customerDraw(type, new String(elem.getData()), parent, info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 自定义的json数据，需要解析成bean实例
//            CustomMessage data = null;
//            try {
//                data = new Gson().fromJson(new String(elem.getData()), CustomMessage.class);
//            } catch (Exception e) {
//                DemoLog.e(TAG, "invalid json: " + new String(elem.getData()) + " " + e.getMessage());
//            }
//            if (data == null) {
//                DemoLog.e(TAG, "No Custom Data: " + new String(elem.getData()));
//            } else if (data.version == JSON_VERSION_1_HELLOTIM) {
//                CustomHelloTIMUIController.onDraw(parent, data);
//            } else if (data.version == JSON_VERSION_3_ANDROID_IOS_TRTC) {
//               // CustomAVCallUIController.getInstance().onDraw(parent, data);
//            } else if (data.version == MessageHelper.MSG_TYPE_GIFT) {
//                // CustomAVCallUIController.getInstance().onDraw(parent, data);
//                GiftUIController.onDraw(parent,data);
//            } else if (data.version == MessageHelper.MSG_TYPE_RED_ENVELOPES) {
//                // CustomAVCallUIController.getInstance().onDraw(parent, data);
//                RedEnvelopesUIController.onDraw(parent,data);
//            } else {
//                DemoLog.e(TAG, "unsupported version: " + data.version);
//
//            }
        }
    }

    void addGiftAction(final ChatLayout layout) {
        InputMoreActionUnit unit = new InputMoreActionUnit();
        unit.setIconResId(R.drawable.txt_liwu);
        unit.setTitleId(R.string.im_action_gift);
       // unit.setAction("");
        unit.setOnClickListener(unit.new OnActionClickListener() {
            @Override
            public void onClick() {
                if (mChatInfo.getType() == V2TIMConversation.V2TIM_GROUP) {
                    if (mChatInfo.getId().startsWith("top")) {
                        ToastUtil.show("高端会话暂不支持此功能");
                        return;
                    }
                    final GiftPanelPop2 giftPanelPop = new GiftPanelPop2(mContext,5,mChatInfo.getId());
                    giftPanelPop.showPopupWindow();
                    giftPanelPop.setOnGiftSendSucListener(new GiftPanelPop2.OnGiftSendSucListener() {
                        @Override
                        public void onGiftSendSuc(@NotNull String orderId, int giftReward) {
                            //ToastUtil.show(mContext,"赠送成功");
                            giftPanelPop.dismiss();
                        }
                    });

                } else if (mChatInfo.getType() == V2TIMConversation.V2TIM_C2C) {
                    //new DashangDialogNew2(mContext, mChatInfo.getId(), layout.getChatManager());
                    final GiftPanelPop2 giftPanelPop = new GiftPanelPop2(mContext,4,mChatInfo.getId());
                    giftPanelPop.showPopupWindow();
                    giftPanelPop.setOnGiftSendSucListener(new GiftPanelPop2.OnGiftSendSucListener() {
                        @Override
                        public void onGiftSendSuc(@NotNull String orderId, int giftReward) {
                            //ToastUtil.show(mContext,"赠送成功");
                            giftPanelPop.dismiss();
                        }
                    });
                } else if (mChatInfo.getType() == V2TIMConversation.CONVERSATION_TYPE_INVALID) {
                  //  new DashangDialogNew4(mContext, mChatInfo.getId());
                }
            }
        });

        layout.getInputLayout().addAction(unit);
    }

    void addRedBaoAction(final ChatLayout layout) {
        InputMoreActionUnit unit = new InputMoreActionUnit();
        unit.setIconResId(R.drawable.ic_more_red_bag);
        unit.setTitleId(R.string.im_action_red_bao);
       // unit.setAction("");

        unit.setOnClickListener(unit.new OnActionClickListener() {
            @Override
            public void onClick() {
                if (mChatInfo.getType() == V2TIMConversation.V2TIM_GROUP) {
                    if (mChatInfo.getId().startsWith("top")) {
                        ToastUtil.show("高端会话暂不支持此功能");
                        return;
                    }
                    Intent intent = new Intent(MyApp.getInstance(), GroupSendRedEnvelopesActivity.class);
                    intent.putExtra("targetid", mChatInfo.getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApp.getInstance().startActivity(intent);
                } else if (mChatInfo.getType() == V2TIMConversation.V2TIM_C2C) {
                    //Intent intent = new Intent(MyApp.getInstance(), SendredbaopersonActivity.class);
                    Intent intent = new Intent(MyApp.getInstance(), C2CSendRedEnvelopesActivity.class);
                    intent.putExtra("targetid", mChatInfo.getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApp.getInstance().startActivity(intent);
                }
            }
        });

        layout.getInputLayout().addAction(unit);
        //addFaceBag();
    }

    void addOneTimePicAction(InputLayout inputLayout) {
        InputMoreActionUnit unit = new InputMoreActionUnit();
        unit.setIconResId(R.drawable.ic_more_flash);
        unit.setTitleId(R.string.im_action_one_time_pic);
        //unit.setAction("");
        unit.setOnClickListener(unit.new OnActionClickListener() {
            @Override
            public void onClick() {
                //getMyOwnInfo();
                showSelectPic();
            }
        });
        inputLayout.addAction(unit);
    }

    void addLocationPicAction(final ChatLayout layout) {
        InputMoreActionUnit unit = new InputMoreActionUnit();
        unit.setIconResId(R.drawable.ic_more_location);
        unit.setTitleId(R.string.im_action_location);
        //unit.setAction("");
        unit.setOnClickListener(unit.new OnActionClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(MyApp.getInstance(), SelectLocationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.getInstance().startActivity(intent);
            }
        });
        layout.getInputLayout().addAction(unit);
    }

//    void customerDraw(String type, String data, ICustomMessageViewGroup parent, MessageInfo info) {
//        switch (type) {
//            case CustomMessageType.TYPE_RED_ENVELOPES:
//                RedEnvelopesUIController.onDraw(parent, data, info, mContext, mChatInfo);
//                break;
//            case CustomMessageType.TYPE_SYSTEM_TIPS:
//                SystemTipsUIController.onDraw(parent, data, info);
//                break;
//            case CustomMessageType.TYPE_GIFT:
//                GiftUIController.onDraw(parent, data, mContext, mChatInfo, info);
//                break;
//            case CustomMessageType.TYPE_FLASH:
//                FlashUIController.onDraw(parent, data, mContext,info);
//                break;
//            case CustomMessageType.TYPE_LOCATION:
//                LocationUIController.onDraw(parent, data,mContext,info);
//                break;
//            case CustomMessageType.TYPE_DYNAMIC:
//                DynamicUIController.onDraw(parent, data, mContext);
//                break;
//            case CustomMessageType.TYPE_GROUP_INVITE:
//                GroupInviteUIController.onDraw(parent, data, mContext);
//                break;
//        }
//    }


    private void showSelectPic() {
        new AlertView(null, null, "取消", null,
                new String[]{"拍照", "从相册中选择"}, mContext, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                switch (position) {
                    case 0:
                        ((ChatActivity) mContext).takePhone();
                        break;
                    case 1:
                        ((ChatActivity) mContext).choosePhone();
                        break;
                }
            }
        }).show();
    }

    private void showIdentity() {

        V2TIMManager.getInstance().getUsersInfo(Arrays.asList(mChatInfo.getId()), new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                //titleText.setText(v2TIMUserFullInfos.get(0).getRole() + "---");
                if (v2TIMUserFullInfos == null || v2TIMUserFullInfos.size() == 0) {
                    return;
                }
                int role = v2TIMUserFullInfos.get(0).getRole();
                switch (role) {
                    case 2:
                    case 3:
                    case 4:
                        showIdentityImg(1);
                        break;
                    case 5:
                        break;
                    case 6:
                        showIdentityImg(2);
                        break;
                    case 7:
                        showIdentityImg(3);
                        break;
                    case 8:
                        showIdentityImg(4);
                        break;
                    case 9:
                        showIdentityImg(5);
                        break;
                    default:
                       // ivItemCommentDetailSign.setVisibility(View.INVISIBLE);
                        //identityView.setVisibility(View.INVISIBLE);
                        break;
                }
                String nickName = v2TIMUserFullInfos.get(0).getNickName();

                Map<String, byte[]> customInfo = v2TIMUserFullInfos.get(0).getCustomInfo();
                byte[] high_status = customInfo.get("IsTop");
                String mHighStatus="";
                if (high_status != null) {
                    mHighStatus = new String(high_status);
                }
                byte[] highNum = customInfo.get("TopNum");
                String targetHighNum = "";
                if (highNum != null) {
                    targetHighNum = new String(highNum);
                }
                if ("1".equals(mHighStatus)){
                    mChatLayout.getTitleBar().getMiddleTitle().setText(targetHighNum);
                }else {
                    if (!TextUtil.isEmpty(nickName)) {
                        mChatLayout.getTitleBar().getMiddleTitle().setText(nickName);
                    }
                }

            }
        });




//        ConversationUserInfoBean conversationUserInfoBean = GsonUtil.GsonToBean(mUserInfo, ConversationUserInfoBean.class);
//        if (conversationUserInfoBean == null) {
//            return;
//        }
//        //  判断显示标识，官方管理>年svip>svip>年vip>vip
//        //ivItemCommentDetailSign.setVisibility(View.VISIBLE);
//        if (conversationUserInfoBean.getData().getInfo().getIs_admin().equals("1")) {
//            // Glide.with(context).load(R.mipmap.guanfangbiaozhi).into(ivItemCommentDetailSign);
//            //mChatLayout.getTitleBar().getMiddleTitle().setTextColor(mContext.getResources().getColor(R.color.black));
//            showIdentityImg(1);
//        } else if (conversationUserInfoBean.getData().getInfo().getSvipannual().equals("1")) {
//            //Glide.with(context).load(R.mipmap.svipnian).into(ivItemCommentDetailSign);
//            //mChatLayout.getTitleBar().getMiddleTitle().setTextColor(mContext.getResources().getColor(R.color.red));
//            showIdentityImg(2);
//        } else if (conversationUserInfoBean.getData().getInfo().getSvip().equals("1")) {
//            //Glide.with(context).load(R.mipmap.svip).into(ivItemCommentDetailSign);
//            //mChatLayout.getTitleBar().getMiddleTitle().setTextColor(mContext.getResources().getColor(R.color.red2));
//            showIdentityImg(3);
//        } else if (conversationUserInfoBean.getData().getInfo().getVipannual().equals("1")) {
//            //Glide.with(context).load(R.mipmap.vipnian).into(ivItemCommentDetailSign);
//            //mChatLayout.getTitleBar().getMiddleTitle().setTextColor(mContext.getResources().getColor(R.color.green));
//            showIdentityImg(4);
//        } else if (conversationUserInfoBean.getData().getInfo().getVip().equals("1")) {
//            //Glide.with(context).load(R.mipmap.vip).into(ivItemCommentDetailSign);
//            //mChatLayout.getTitleBar().getMiddleTitle().setTextColor(mContext.getResources().getColor(R.color.blue_light));
//            showIdentityImg(5);
//        } else {
//            //ivItemCommentDetailSign.setVisibility(View.INVISIBLE);
//        }

        //if(conversationUserInfoBean.getData().getInfo().get)

    }

    private void showFollowNotice() {
        if (getTipShowState()) {
            return;
        }
        final NoticeLayout noticeLayout = mChatLayout.getNoticeLayout();
        noticeLayout.removeAllViews();
        View view = LayoutInflater.from(mContext).inflate(R.layout.notice_layout_add_friend, noticeLayout, false);
        noticeLayout.addView(view);
        noticeLayout.setVisibility(View.VISIBLE);
        TextView tvFollow = view.findViewById(R.id.mConversation_follow);
        ImageView ivClose = view.findViewById(R.id.mConversation_follow_close);
        tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signFollowTips();
                noticeLayout.setVisibility(View.GONE);
            }
        });

    }

    private void showSuspiciousNotice() {
        final NoticeLayout noticeLayout = mChatLayout.getNoticeLayout();
        noticeLayout.removeAllViews();
        View view = LayoutInflater.from(mContext).inflate(R.layout.notice_layout_suspicious, noticeLayout, false);
        noticeLayout.addView(view);
        noticeLayout.setVisibility(View.VISIBLE);
    }
    //标记关注tips
    void signFollowTips() {
        try {
            List<String> redEnvelopesCacheList = SharedPreferencesUtils.getDataList("follow_tip_" + MyApp.uid);
            if (redEnvelopesCacheList == null) {
                redEnvelopesCacheList = new ArrayList<>();
            }
            redEnvelopesCacheList.add(mChatInfo.getId());
            SharedPreferencesUtils.addDataList("follow_tip_" + MyApp.uid, redEnvelopesCacheList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean getTipShowState() {
        try {
            List<String> redEnvelopesCacheList = SharedPreferencesUtils.getDataList("follow_tip_" + MyApp.uid);
            if (redEnvelopesCacheList == null) {
                return false;
            } else {
                return redEnvelopesCacheList.contains(mChatInfo.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void follow() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", mChatInfo.getId());
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.FollowOneBox, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            ToastUtil.show(mContext, "关注成功");
                            mChatLayout.getNoticeLayout().setVisibility(View.GONE);
                            //mConversationLlFollow.setVisibility(View.GONE);
//                                    String followFlag = obj.getString("data");
//                                    if (followFlag.equals("1")) {
//                                        EventBus.getDefault().post(new FollowEvent("1", "3"));
//                                    } else {
//                                        EventBus.getDefault().post(new FollowEvent("2", "1"));
//                                    }
                            //ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                            break;
                        case 4002:
                        case 8881:
                        case 8882:
                        case 4787:
                            BindGuanzhuDialog.bindAlertDialog(mContext, obj.getString("msg"));
                            break;
                        case 5000:
                            ToastUtil.show(MyApp.getInstance(), obj.getString("msg") + "");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


    /**
     * 缩放图片
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public Bitmap getBitmap(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = (float) newWidth / width;
        float scaleHeight = (float) newHeight / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    public void showIdentityImg(int type) {
        Bitmap bmp = null;
        switch (type) {
            case 1:
                bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.user_manager);
                break;
            case 2:
                bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.user_svip_year);
                break;
            case 3:
                bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.user_svip);
                break;
            case 4:
                bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.user_vip_year);
                break;
            case 5:
                bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.user_vip);
                break;

        }
        if (bmp == null) {
            return;
        }
        int iconWidth = DensityUtil.dip2px(mContext,50);
        Bitmap suitbmp = getBitmap(bmp, iconWidth, iconWidth);
        Drawable suitDrawable = new BitmapDrawable(suitbmp);
        mChatLayout.getTitleBar().getMiddleTitle().setCompoundDrawablesWithIntrinsicBounds(suitDrawable, null, null, null);
        mChatLayout.getTitleBar().getMiddleTitle().setCompoundDrawablePadding(15);
    }

    CustomFaceGroup faceGroup = new CustomFaceGroup();

    void addFaceBag() {
        String[] emojiFilters = mContext.getResources().getStringArray(com.tencent.qcloud.tim.uikit.R.array.emoji_filter_key);
        String[] emojiFilters_values = mContext.getResources().getStringArray(com.tencent.qcloud.tim.uikit.R.array.emoji_filter_value);
        CustomFaceConfig config = TUIKit.getConfigs().getCustomFaceConfig();

        faceGroup.setPageRowCount(2);
        faceGroup.setPageColumnCount(2);
        faceGroup.setFaceIconName("test");
        faceGroup.setFaceIconPath("emoji/" + emojiFilters[3] + "@2x.png");

        for (int i = 0; i < emojiFilters.length; i++) {
            //loadAssetBitmap(emojiFilters[i], "emoji/" + emojiFilters[i] + "@2x.png", true);
            CustomFace customFace = new CustomFace();
            customFace.setFaceName(emojiFilters_values[i]);
            customFace.setAssetPath("emoji/" + emojiFilters[i] + "@2x.png");
            customFace.setFaceWidth(drawableWidth);
            customFace.setFaceHeight(drawableWidth);
            faceGroup.addCustomFace(customFace);
        }
        config.addFaceGroup(faceGroup);
    }

    private static final int drawableWidth = ScreenUtil.getPxByDp(32);
    ArrayList<Emoji> emojiList = new ArrayList<>();

    private Emoji loadAssetBitmap(String filter, String assetPath, boolean isEmoji) {
        InputStream is = null;
        try {
            Emoji emoji = new Emoji();
            CustomFace customFace = new CustomFace();
            Resources resources = mContext.getResources();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDensity = DisplayMetrics.DENSITY_XXHIGH;
            options.inScreenDensity = resources.getDisplayMetrics().densityDpi;
            options.inTargetDensity = resources.getDisplayMetrics().densityDpi;
            mContext.getAssets().list("");
            is = mContext.getAssets().open(assetPath);
            Bitmap bitmap = BitmapFactory.decodeStream(is, new Rect(0, 0, drawableWidth, drawableWidth), options);
            if (bitmap != null) {
                // drawableCache.put(filter, bitmap);
                emoji.setIcon(bitmap);
                emoji.setFilter(filter);

                if (isEmoji) {

                    emojiList.add(emoji);
                }

            }
            return emoji;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    void getStateBeforeCall(final ChatLayout layout, final int type) {
//        HttpHelper.getInstance().getCallState(mChatInfo.getId(),new HttpListener() {
        HttpHelper.getInstance().getCallStateNew(mChatInfo.getId(),new HttpListener() {
            @Override
            public void onSuccess(String data) {
                LogUtil.d(data);
                CallStateBean callStateBean = GsonUtil.GsonToBean(data, CallStateBean.class);
                if (callStateBean != null && callStateBean.getData().getState() == 1) {
                    ProfileManager.getInstance().getUserInfoByUserId(mChatInfo.getId(), new ProfileManager.GetUserInfoCallback() {
                                @Override
                                public void onSuccess(UserModel model) {
                                    List<UserModel> list = new ArrayList<>();
                                    list.add(model);
                                    ((TRTCAVCallImpl)TRTCAVCallImpl.sharedInstance(mContext)).setLink(false);
                                    if (type == 1) {
                                        TRTCVideoCallActivity.startCallSomeone(TUIKitLive.getAppContext(), list);
                                    } else {
                                        TRTCAudioCallActivity.startCallSomeone(TUIKitLive.getAppContext(), list);
                                    }
                                }

                                @Override
                                public void onFailed(int code, String msg) {
                                    TUILiveLog.e(TAG, "onInputMoreActionClick error " + msg + " code : " + code);
                                }
                            });
//                    if (type == 1) {
//                       // layout.getInputLayout().startVideoCall();
//                    } else {
//                       // layout.getInputLayout().startAudioCall();
//                    }
                } else {
                    ToastUtil.show(mContext, callStateBean.getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(mContext, msg);
            }
        });
    }

    void getMyOwnInfo() {
        HttpHelper.getInstance().getMyOwnInfo(new HttpListener() {
            @Override
            public void onSuccess(String data) {
                VipAndVolunteerData vipAndVolunteerData = GsonUtil.GsonToBean(data, VipAndVolunteerData.class);
                String vip = vipAndVolunteerData.getData().getVip();
                String svip = vipAndVolunteerData.getData().getSvip();
                if ("1".equals(vip)) {
                    showSelectPic();
                } else {
                    new VipDialog(mContext,"VIP可以使用闪照功能");
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });


    }
}
