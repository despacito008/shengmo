package com.aiwujie.shengmo.tim.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.GroupInfoActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.HighUserBean;
import com.aiwujie.shengmo.bean.HighUserModel;
import com.aiwujie.shengmo.bean.RedEnvelopesBean;
import com.aiwujie.shengmo.kt.ui.activity.AtGroupMemberActivity;
import com.aiwujie.shengmo.kt.ui.activity.GroupDetailActivity;
import com.aiwujie.shengmo.kt.ui.activity.HighEndUserActivity;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.tim.bean.DraftMessageBean;
import com.aiwujie.shengmo.tim.bean.RedEnvelopesMessageBean;
import com.aiwujie.shengmo.tim.bean.RefreshMessageBean;
import com.aiwujie.shengmo.tim.helper.ChatContextUtil;
import com.aiwujie.shengmo.tim.helper.ChatLayoutHelper;
import com.aiwujie.shengmo.tim.helper.ChatManagerHelper;
import com.aiwujie.shengmo.tim.utils.Constants;
import com.aiwujie.shengmo.tim.utils.DemoLog;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.MediaMessageCheckPopup;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.imsdk.conversation.ConversationListener;
import com.tencent.imsdk.group.GroupMemberInfo;
import com.tencent.imsdk.message.DraftMessage;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageListGetOption;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.AudioPlayer;
import com.tencent.qcloud.tim.uikit.component.NoticeLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.C2CChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.AbsChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.modules.forward.ForwardSelectActivity;
import com.tencent.qcloud.tim.uikit.modules.forward.base.ConversationBean;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;
import com.tencent.qcloud.tim.uikit.utils.NetWorkUtils;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;


public class ChatFragment extends BaseFragment {

    private View mBaseView;
    private ChatLayout mChatLayout;
    private TitleBarLayout mTitleBar;
    private LinearLayout llUnreadTip;
    private TextView mTvUnreadTip;
    private ChatInfo mChatInfo;
    private List<RedEnvelopesBean.DataBean> redEnvelopesList;

    private List<MessageInfo> mForwardSelectMsgInfos = null;
    private int mForwardMode;
    private long aLong;
    private String mHighStatus = "";
    private String TopNum = "";
    private String isHighGroup = "0";   //   判断高端群组  1 是 其他不是
    public static final int CODE_IMAGE_SELECT = 10005;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.chat_fragment, container, false);
        return mBaseView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    int atState = 0;

    private void initView() {
        //从布局文件中获取聊天面板组件
        mChatLayout = mBaseView.findViewById(R.id.chat_layout);
        //单聊组件的默认UI和交互初始化
        mChatLayout.initDefault();

        /*
         * 需要聊天的基本信息
         */
        mChatLayout.setChatInfo(mChatInfo);
        if (mChatInfo.getId().startsWith("top")) {
            isHighGroup = "1";
        }

        //获取单聊面板的标题栏
        mTitleBar = mChatLayout.getTitleBar();
        //getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ImmersionBar.with(this)
                .statusBarColor(R.color.chatWrite)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .keyboardEnable(true)
                .titleBarMarginTop(mTitleBar)
                .init();

        mChatLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.chatWrite));
        mTitleBar.setBackgroundColor(getActivity().getResources().getColor(R.color.chatWrite));
        mChatLayout.getMessageLayout().setBackgroundColor(getActivity().getResources().getColor(R.color.chatWrite));

        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
        {
            mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
        }

        if (mChatInfo.getType() == V2TIMConversation.V2TIM_C2C) {

            mTitleBar.setRightIcon(R.drawable.ic_chat_c2c);
            mTitleBar.setOnRightClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MyApp.getInstance(), PesonInfoActivity.class);
                    intent.putExtra("uid", mChatInfo.getId());
                    startActivity(intent);

                }
            });


            if (Integer.parseInt(mChatInfo.getId()) <= 10 || mChatInfo.equals("697955")) { //官方号
                mChatLayout.getInputLayout().setVisibility(View.GONE);
                mChatLayout.getTitleBar().getRightGroup().setVisibility(View.GONE);
            } else {
                mChatLayout.getInputLayout().setVisibility(View.VISIBLE);
                mChatLayout.getTitleBar().getRightGroup().setVisibility(View.VISIBLE);
            }

        } else if (mChatInfo.getType() == V2TIMConversation.V2TIM_GROUP) {
            if ("1".equals(isHighGroup)) {
                mTitleBar.getRightGroup().setVisibility(View.INVISIBLE);
            } else {
                mTitleBar.getRightGroup().setVisibility(View.VISIBLE);
            }
            mTitleBar.setRightIcon(R.drawable.ic_chat_group);
            mTitleBar.setOnRightClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
//                    intent.putExtra("isChat", false);
//                    intent.putExtra("groupId", mChatInfo.getId());
//                    startActivity(intent);
                    GroupDetailActivity.Companion.start(MyApp.getInstance(),
                            mChatInfo.getId(), 0, true);
                }
            });


            //给群组和管理员 添加撤回他人消息的功能  黑v也可以
            V2TIMManager.getGroupManager().getGroupMembersInfo(mChatInfo.getId(), Arrays.asList(MyApp.uid), new V2TIMValueCallback<List<V2TIMGroupMemberFullInfo>>() {
                @Override
                public void onSuccess(List<V2TIMGroupMemberFullInfo> v2TIMGroupMemberFullInfos) {
                    for (V2TIMGroupMemberFullInfo v2TIMGroupMemberFullInfo : v2TIMGroupMemberFullInfos) {

                        if (v2TIMGroupMemberFullInfo.getUserID().equals(MyApp.uid)) {
                            int role = v2TIMGroupMemberFullInfo.getRole();
                            if (role == GroupMemberInfo.MEMBER_ROLE_ADMINISTRATOR || role == GroupMemberInfo.MEMBER_ROLE_OWNER || "1".equals(MyApp.isAdmin)) {
                                mChatLayout.getMessageLayout().setOnManagePopActionClickListener(new MessageLayout.OnManagePopActionClickListener() {
                                    @Override
                                    public void onRevokeMessageClick(int position, MessageInfo msg) {
                                        revokeMessage(mChatInfo.getId(), msg.getTimMessage().getSeq());
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void onError(int code, String desc) {

                }
            });
        }

        mChatLayout.setForwardSelectActivityListener(new AbsChatLayout.onForwardSelectActivityListener() {
            @Override
            public void onStartForwardSelectActivity(int mode, List<MessageInfo> msgIds) {
                mForwardMode = mode;
                mForwardSelectMsgInfos = msgIds;
                Intent intent = new Intent(getActivity(), ForwardSelectActivity.class);
                intent.putExtra(ForwardSelectActivity.FORWARD_MODE, mode);
                startActivityForResult(intent, TUIKitConstants.FORWARD_SELECT_ACTIVTY_CODE);
            }
        });


        mChatLayout.getMessageLayout().setOnItemClickListener(new MessageLayout.OnItemLongClickListener() {
            @Override
            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
                //因为adapter中第一条为加载条目，位置需减1
                mChatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
            }

            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
                if (null == messageInfo) {
                    return;
                }
                if (!mChatInfo.getId().startsWith("top")){
                    if (Integer.parseInt(mChatInfo.getId()) <= 10 || mChatInfo.equals("697955")) { //官方号
                        return;
                    }
                }

                if (!messageInfo.isSelf()) {
                    if ("1".equals(isHighGroup)) {
                        startHighPage(messageInfo.getFromUser());
                    } else {
                        Intent intent = new Intent(getActivity(), PesonInfoActivity.class);
                        intent.putExtra("uid", messageInfo.getFromUser());
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(getActivity(), PesonInfoActivity.class);
                    intent.putExtra("uid", messageInfo.getFromUser());
                    startActivity(intent);
                }

            }

            @Override
            public void onUserIconLongClick(View view, int position, MessageInfo messageInfo) {
                if (messageInfo.isGroup()) { //只有群聊天 才有长按@
                    if ("1".equals(isHighGroup)) {
                        return;
                    }
                    atState = 1;
                    mChatLayout.getInputLayout().getInputText().setText(mChatLayout.getInputLayout().getInputText().getText() + "@");
                    mChatLayout.getInputLayout().updateInputText(TextUtil.isEmpty(messageInfo.getGroupNameCard()) ? messageInfo.getTimMessage().getNickName() : messageInfo.getGroupNameCard(), messageInfo.getFromUser());
                    mChatLayout.getInputLayout().getInputText().setText(mChatLayout.getInputLayout().getInputText().getText() + " ");
                    mChatLayout.getInputLayout().getInputText().setSelection(mChatLayout.getInputLayout().getInputText().getText().length());
                }
            }
        });
        mChatLayout.getInputLayout().getInputText().setMaxLines(4);
        mChatLayout.getInputLayout().setStartActivityListener(new InputLayout.OnStartActivityListener() {
            @Override
            public void onStartGroupMemberSelectActivity() {
                if (atState == 1) {
                    atState = 0;
                    return;
                }
                if ("1".equals(isHighGroup)) {
                    return;
                }
//                Intent intent = new Intent(getActivity(), AtGroupMemberActivity.class);
                Intent intent = new Intent(getActivity(), AtGroupMemberActivity.class);
                intent.putExtra("groupId", mChatInfo.getId());
                startActivityForResult(intent, 100);
            }

        });
        mChatLayout.getInputLayout().setMediaCheckListener(new InputLayout.OnMediaCheckListener() {
            @Override
            public void onImageCheck(final MessageInfo info) {
                //mChatLayout.sendMessage(info,false);
                Uri uri = Uri.parse(info.getDataPath());
                MediaMessageCheckPopup mediaMessageCheckPopup = new MediaMessageCheckPopup(getActivity(), uri, 1);
                mediaMessageCheckPopup.showPopupWindow();
                mediaMessageCheckPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
                    @Override
                    public void onItemListener(int position) {
                        mChatLayout.sendMessage(info, false);
                    }
                });
            }

            @Override
            public void onVideoCheck(final MessageInfo info) {
                // mChatLayout.sendMessage(info,false);
                Uri uri = Uri.parse(info.getDataPath());
                MediaMessageCheckPopup mediaMessageCheckPopup = new MediaMessageCheckPopup(getActivity(), uri, 2);
                mediaMessageCheckPopup.showPopupWindow();
                mediaMessageCheckPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
                    @Override
                    public void onItemListener(int position) {
                        mChatLayout.sendMessage(info, false);
                    }
                });
            }
        });


        mTvUnreadTip = mBaseView.findViewById(R.id.tv_chat_unread);
        llUnreadTip = mBaseView.findViewById(R.id.ll_message_unread_tip);

        mChatLayout.getInputLayout().setOnMediaOpenListener(new InputLayout.OnMediaOpenListener() {
            @Override
            public void onMediaOpen() {
                ImageSelector.builder()
                        .useCamera(false)
                        .setSingle(false)
                        .setMaxSelectCount(9)
                        .canPreview(true)
                        .start(ChatFragment.this, CODE_IMAGE_SELECT);
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        // initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        AudioPlayer.getInstance().stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatLayout != null) {
            mChatLayout.exitChat();
        }
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
                Map<String, byte[]> customInfo = v2TIMUserFullInfos.get(0).getCustomInfo();
                byte[] high_status = customInfo.get("IsTop");
                byte[] topNum = customInfo.get("TopId");

                if (high_status != null) {
                    mHighStatus = new String(high_status);
                }
                if (topNum != null) {
                    TopNum = new String(topNum);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            String name = data.getStringExtra("name");
            String id = data.getStringExtra("id");
//            String atText = mChatLayout.getInputLayout().getInputText().getText().toString()  + name;
//            mChatLayout.getInputLayout().getInputText().setText(atText);
            mChatLayout.getInputLayout().updateInputText(name, id);
            mChatLayout.getInputLayout().getInputText().setText(mChatLayout.getInputLayout().getInputText().getText() + " ");
            mChatLayout.getInputLayout().getInputText().setSelection(mChatLayout.getInputLayout().getInputText().getText().length());
        } else if (requestCode == TUIKitConstants.FORWARD_SELECT_ACTIVTY_CODE && resultCode == TUIKitConstants.FORWARD_SELECT_ACTIVTY_CODE) {
            if (data != null) {
                if (mForwardSelectMsgInfos == null || mForwardSelectMsgInfos.isEmpty()) {
                    return;
                }

                ArrayList<ConversationBean> conversationBeans = data.getParcelableArrayListExtra(TUIKitConstants.FORWARD_SELECT_CONVERSATION_KEY);
                if (conversationBeans == null || conversationBeans.isEmpty()) {
                    return;
                }

                for (int i = 0; i < conversationBeans.size(); i++) {//遍历发送对象会话
                    boolean isGroup = conversationBeans.get(i).getIsGroup() == 1;
                    String id = conversationBeans.get(i).getConversationID();
                    String title = "";
                    if (mChatInfo.getType() == V2TIMConversation.V2TIM_GROUP) {
                        title = mChatInfo.getId() + getString(R.string.forward_chats);
                    } else {
                        title = V2TIMManager.getInstance().getLoginUser() + getString(R.string.and_text) + mChatInfo.getId() + getString(R.string.forward_chats_c2c);
                    }

                    boolean selfConversation = false;
                    if (id != null && id.equals(mChatInfo.getId())) {
                        selfConversation = true;
                    }

                    ChatManagerKit chatManagerKit = mChatLayout.getChatManager();
                    chatManagerKit.forwardMessage(mForwardSelectMsgInfos, isGroup, id, title, mForwardMode, selfConversation, false, new IUIKitCallBack() {
                        @Override
                        public void onSuccess(Object data) {
                            // DemoLog.v(TAG, "sendMessage onSuccess:");
                            ToastUtil.show(getActivity(), "转发成功");
                        }

                        @Override
                        public void onError(String module, int errCode, String errMsg) {
                            // DemoLog.v(TAG, "sendMessage fail:" + errCode + "=" + errMsg);
                            ToastUtil.show(getActivity(), "转发失败 ：" + errCode + errMsg);
                        }
                    });
                }
            }
        } else if (requestCode == CODE_IMAGE_SELECT && data != null) {
            //获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            for (int i = 0; i < images.size(); i++) {
                LogUtil.d(images.get(i));
                sendMessageByPath(images.get(i));
            }
            mChatLayout.getInputLayout().hideSoftInput();
        }

    }

    void initData() {
        Bundle bundle = getArguments();
        mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
        String userInfo = bundle.getString("userInfo");
        if (mChatInfo == null) {
            return;
        }
        showIdentity();
        initView();
        // TODO 通过api设置ChatLayout各种属性的样例
        ChatLayoutHelper helper = new ChatLayoutHelper(getActivity(), mChatInfo, userInfo);
        //ChatContextUtil.setActivity(getActivity());
        helper.customizeChatLayout(mChatLayout);
        aLong = bundle.getLong(Constants.UNREAD_SEQ, -1);
        final int unreadNum = bundle.getInt(Constants.UNREAD_NUM, -1);
        if (aLong != -1) {
            showUnreadTip();
            mTvUnreadTip.setText(unreadNum + "条新消息");
        } else {
            llUnreadTip.setVisibility(View.GONE);
        }

        llUnreadTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideUnreadTip();
                loadUnReadMessage();
            }
        });

        //滑动到未读消息时 取消未读消息提示
        mChatLayout.getMessageLayout().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = mChatLayout.getMessageLayout().getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        int mLastVisiblePosition = linearManager.findLastVisibleItemPosition();
                        if (mLastVisiblePosition >= unreadNum) {
                            hideUnreadTip();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mChatLayout != null && mChatLayout.getInputLayout() != null) {
            mChatLayout.getInputLayout().setDraft();
        }
        //List<DraftMessageBean> dataList = SharedPreferencesUtils.getDataList("daft_" + MyApp.uid);
        // DraftMessage
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    //主动刷新列表
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshConversation(RefreshMessageBean bean) {
        // initView();
    }

    void revokeMessage(String gid, long seq) {
        HttpHelper.getInstance().revokeGroupMessage(gid, seq, new HttpListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    void startHighPage(String gid) {
        HttpHelper.getInstance().getHighIdByUid(gid, new HttpCodeMsgListener() {

            @Override
            public void onSuccess(String data, String msg) {
                HighUserModel model = GsonUtil.GsonToBean(data, HighUserModel.class);
                if (model.data != null) {
                    if (!TextUtils.isEmpty(model.data.getTop_id())) {
                        Intent intent = new Intent(getActivity(), HighEndUserActivity.class);
                        intent.putExtra(IntentKey.UID, model.data.getTop_id());
                        startActivity(intent);
                    } else {
                        ToastUtil.show(getActivity(), "用户id为空");
                    }
                }

            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(getActivity(), msg);
            }
        });
    }

    void loadUnReadMessage() {
        V2TIMMessageListGetOption optionForward = new V2TIMMessageListGetOption();
        optionForward.setCount(5);
        optionForward.setGetType(V2TIMMessageListGetOption.V2TIM_GET_CLOUD_NEWER_MSG);
        //optionForward.setGetType(V2TIMMessageListGetOption.V2TIM_GET_LOCAL_NEWER_MSG);
        optionForward.setLastMsgSeq(aLong);
        if (mChatInfo.getType() == V2TIMConversation.V2TIM_C2C) {
            optionForward.setUserID(mChatInfo.getId());
        } else {
            optionForward.setGroupID(mChatInfo.getId());
        }
        V2TIMManager.getMessageManager().getHistoryMessageList(optionForward, new V2TIMValueCallback<List<V2TIMMessage>>() {
            @Override
            public void onSuccess(List<V2TIMMessage> v2TIMMessages) {
                if (v2TIMMessages != null && v2TIMMessages.size() > 0) {
                    V2TIMMessage v2TIMMessage = v2TIMMessages.get(0);
                    mChatInfo.setLocateTimMessage(v2TIMMessage);
                    mChatLayout.setChatInfo(mChatInfo);
                    hideUnreadTip();
                    resetRightIcon();
                }
            }

            @Override
            public void onError(int code, String desc) {

            }
        });

    }

    void showUnreadTip() {
        if (llUnreadTip.getVisibility() != View.VISIBLE) {
            llUnreadTip.setVisibility(View.VISIBLE);
            TranslateAnimation translateAnimation = AnimationUtil.horizontalRightShow();
            llUnreadTip.setAnimation(translateAnimation);
        }
    }

    void hideUnreadTip() {
        if (llUnreadTip.getVisibility() != View.GONE) {
            llUnreadTip.setAnimation(AnimationUtil.horizontalRightHide());
            llUnreadTip.setVisibility(View.GONE);
        }
    }

    void sendMessageByPath(String data) {
        String uri = data.toString();
        if (TextUtils.isEmpty(uri)) {
            // TUIKitLog.e(TAG, "uri is empty");
            return;
        }

        String videoPath = FileUtil.getPathFromUri(Uri.parse(data));
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(videoPath);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        if (mimeType != null && mimeType.contains("video")) {
            MessageInfo info = MessageInfoUtil.buildImageMessage(Uri.parse(data), true);
            mChatLayout.sendMessage(info, false);
        } else {
            MessageInfo info = MessageInfoUtil.buildImageMessage2(Uri.parse(data), true);
            mChatLayout.sendMessage(info, false);
        }
    }

    //解决点击跳转到未读消息时，群组右侧头像无法跳转到群组信息页面的问题
    void resetRightIcon() {
        if (mChatInfo.getType() == V2TIMConversation.V2TIM_GROUP) {
            mTitleBar.setRightIcon(R.drawable.ic_chat_group);
            mTitleBar.setOnRightClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
//                    intent.putExtra("isChat", false);
//                    intent.putExtra("groupId", mChatInfo.getId());
//                    startActivity(intent);
                    GroupDetailActivity.Companion.start(MyApp.getInstance(),
                            mChatInfo.getId(), 0, true);
                }
            });
        }
    }
}
