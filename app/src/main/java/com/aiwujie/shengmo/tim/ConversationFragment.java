package com.aiwujie.shengmo.tim;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.MbmsStreamingSession;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiwujie.shengmo.Manifest;
import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.DynamicMessageActivity;
import com.aiwujie.shengmo.activity.FollowMsgActivity;
import com.aiwujie.shengmo.activity.GroupInfoActivity;
import com.aiwujie.shengmo.activity.GroupIntroActivity;
import com.aiwujie.shengmo.activity.GroupMsgOperationActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.CallUserBean;
import com.aiwujie.shengmo.bean.ConversationCallStatusBean;
import com.aiwujie.shengmo.bean.DialogStampData;
import com.aiwujie.shengmo.customview.StampDialogNew;
import com.aiwujie.shengmo.eventbus.ClearConversationEvent;
import com.aiwujie.shengmo.eventbus.TIMLoginEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.adapter.ConversationCallAdapter;
import com.aiwujie.shengmo.kt.ui.activity.ConversationCallListActivity;
import com.aiwujie.shengmo.kt.ui.activity.GroupDetailActivity;
import com.aiwujie.shengmo.kt.ui.activity.GroupMsgOperateActivity;
import com.aiwujie.shengmo.kt.ui.activity.normallist.FollowMessageActivity;
import com.aiwujie.shengmo.kt.ui.fragment.HomePageConversationFragment;
import com.aiwujie.shengmo.kt.ui.fragment.HomePageConversationTabFragment;
import com.aiwujie.shengmo.kt.ui.fragment.HomePageHighConversationFragment;
import com.aiwujie.shengmo.kt.ui.view.ConversationCallBuyPop;
import com.aiwujie.shengmo.kt.ui.view.ConversationCallUsePop;
import com.aiwujie.shengmo.kt.util.livedata.UnFlowLiveData;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.tim.chat.ChatActivity;
import com.aiwujie.shengmo.tim.helper.ConversationLayoutHelper;
import com.aiwujie.shengmo.tim.utils.Constants;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.NetWorkUtils;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationManager;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.action.PopActionClickListener;
import com.tencent.qcloud.tim.uikit.component.action.PopDialogAdapter;
import com.tencent.qcloud.tim.uikit.component.action.PopMenuAction;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListAdapter;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.modules.search.SearchMainActivity;
import com.tencent.qcloud.tim.uikit.utils.PopWindowUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConversationFragment extends Fragment {

    private View mBaseView;
    private ConversationLayout mConversationLayout;
    private ListView mConversationPopList;
    private PopDialogAdapter mConversationPopAdapter;
    private PopupWindow mConversationPopWindow;
    private List<PopMenuAction> mConversationPopActions = new ArrayList<>();
    private Menu mMenu;
    private SmartRefreshLayout mSmartRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.conversation_fragment, container, false);
        EventBus.getDefault().register(this);
        //initView();
        return mBaseView;
    }

    private void initView() {
        // 从布局文件中获取会话列表面板
        mConversationLayout = mBaseView.findViewById(R.id.conversation_layout);
        // mMenu = new Menu(getActivity(), (TitleBarLayout) mConversationLayout.getTitleBar(), Menu.MENU_TYPE_CONVERSATION);
        LogUtil.d("获取会话列表");
        // 会话列表面板的默认UI和交互初始化
        mConversationLayout.initDefault();
        //mConversationLayout.initDefault(true);
        // 通过API设置ConversataonLayout各种属性的样例，开发者可以打开注释，体验效果
        ConversationLayoutHelper.customizeConversation(mConversationLayout, getActivity());
        mConversationLayout.getConversationList().setOnMessageLoadListener(new ConversationListLayout.OnMessageLoadListener() {
            @Override
            public void doMessageLoad(int size) {

            }

            @Override
            public void doMessageUnread(int unread) {
                ((HomePageConversationTabFragment)((HomePageConversationFragment)getParentFragment()).getParentFragment()).refreshNormalUnreadNum(unread);
            }
        });
        mConversationLayout.getConversationList().setOnItemClickListener(new ConversationListLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ConversationInfo conversationInfo) {
                //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
                if (position == 0) {
                    //search
                    Intent intent = new Intent(getContext(), SearchMainActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return;
                }

                String mTargetId = conversationInfo.getId();
                if (mTargetId.equals("4")) {
                    // Intent intent1 = new Intent(getActivity(), GroupMsgOperationActivity.class);
                    Intent intent1 = new Intent(getActivity(), GroupMsgOperateActivity.class);
                    intent1.putExtra("targetId", mTargetId);
                    startActivity(intent1);
                    V2TIMManager.getMessageManager().markC2CMessageAsRead(mTargetId, null);
                    return;
                }
                if (mTargetId.equals("9")) {
                    //Intent intent2 = new Intent(getActivity(), FollowMsgActivity.class);
                    Intent intent2 = new Intent(getActivity(), FollowMessageActivity.class);
                    startActivity(intent2);
                    V2TIMManager.getMessageManager().markC2CMessageAsRead(mTargetId, null);
                    return;
                }

//                if (mTargetId.equals("3")) {
////                    Intent intent3 = new Intent(getActivity(), DynamicMessageActivity.class);
////                    startActivity(intent3);
//                    ((HomeMessageFragment) getParentFragment()).gotoNoticePage();
//                    V2TIMManager.getMessageManager().markC2CMessageAsRead(mTargetId, null);
//                    return;
//                }

                String isSpeak = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "nospeak", "1");
                if (isSpeak.equals("0") &&  !conversationInfo.isGroup() &&  Integer.parseInt(mTargetId) > 20) {
                    ToastUtil.show(getActivity().getApplicationContext(), "您因违规被系统禁用聊天功能，如有疑问请与客服联系！");
                    return;
                } else {
                    if (conversationInfo.isGroup()) {
                        startChatGroupActivity(conversationInfo);
                    } else {
                        isOpenChat(conversationInfo);
                    }
                }
            }

        });
        mConversationLayout.getConversationList().setOnItemLongClickListener(new ConversationListLayout.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(View view, int position, ConversationInfo conversationInfo) {
                if (position == 0) {
                    Intent intent = new Intent(getContext(), SearchMainActivity.class);
                    startActivity(intent);
                } else {
                    startPopShow(view, position - ConversationListAdapter.HEADER_COUNT, conversationInfo);
                }
            }
        });
        mConversationLayout.getConversationList().setOnItemIconClickListener(new ConversationListLayout.onItemIconClickListener() {
            @Override
            public void onItemIconClick(View view, int position, ConversationInfo conversationInfo) {
                // ToastUtil.show(getActivity(),position + " item 被点击");
                //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
                String mTargetId = conversationInfo.getId();
                if (mTargetId.equals("4")) {
                    //Intent intent1 = new Intent(getActivity(), GroupMsgOperationActivity.class);
                    Intent intent1 = new Intent(getActivity(), GroupMsgOperateActivity.class);
                    intent1.putExtra("targetId", mTargetId);
                    startActivity(intent1);
                    V2TIMManager.getMessageManager().markC2CMessageAsRead(mTargetId, null);
                    return;
                }
                if (mTargetId.equals("9")) {
                    Intent intent2 = new Intent(getActivity(), FollowMsgActivity.class);
                    startActivity(intent2);
                    V2TIMManager.getMessageManager().markC2CMessageAsRead(mTargetId, null);
                    return;
                }


                if (!conversationInfo.isGroup() && Integer.parseInt(mTargetId) <= 10) {
                    return;
                }

                if (conversationInfo.isGroup()) {
                    goToGroupInfoActivity(conversationInfo);
                } else {
                    goToUserInfoActivity(conversationInfo);
                }

//                if (mTargetId.equals("3")) {
////                    Intent intent3 = new Intent(getActivity(), DynamicMessageActivity.class);
////                    startActivity(intent3);
//                    ((HomeMessageFragment) getParentFragment()).gotoNoticePage();
//                    V2TIMManager.getMessageManager().markC2CMessageAsRead(mTargetId, null);
//                    return;
//                }
//
//                if (mTargetId.equals("1") || mTargetId.equals("2") || mTargetId.equals("5")) {
//                    return;
//                }
//
//                String isSpeak = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "nospeak", "1");
//                if (isSpeak.equals("0") && Integer.parseInt(mTargetId) > 20) {
//                    ToastUtil.show(getActivity().getApplicationContext(), "您因违规被系统禁用聊天功能，如有疑问请与客服联系！");
//                    return;
//                } else {
//
//                }
            }
        });
        initTitleAction();
        initPopMenuAction();
        mSmartRefreshLayout = mBaseView.findViewById(R.id.smart_refresh_conversation);
        mSmartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshCallList();
            }
        });
        initHeaderView();
        getCallUserList();

    }

    private void initTitleAction() {
        mConversationLayout.getTitleBar().setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mMenu.isShowing()) {
//                    mMenu.hide();
//                } else {
//                    mMenu.show();
//                }
            }
        });
    }

    private void initPopMenuAction() {

        // 设置长按conversation显示PopAction
        List<PopMenuAction> conversationPopActions = new ArrayList<PopMenuAction>();
        PopMenuAction action = new PopMenuAction();
        action.setActionName(getString(R.string.chat_top));
        action.setActionClickListener(new PopActionClickListener() {
            @Override
            public void onActionClick(int position, Object data) {
                mConversationLayout.setConversationTop((ConversationInfo) data, new IUIKitCallBack() {
                    @Override
                    public void onSuccess(Object data) {

                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {

                    }
                });
            }
        });
        conversationPopActions.add(action);
        action = new PopMenuAction();
        action.setActionClickListener(new PopActionClickListener() {
            @Override
            public void onActionClick(final int position, final Object data) {
                new AlertDialog.Builder(getActivity()).setMessage("删除聊天后，该条会话记录将无法恢复，是否删除？")
                        .setPositiveButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.d(position);
                        mConversationLayout.deleteConversation(position, (ConversationInfo) data);
                    }
                }).create().show();

            }
        });
        action.setActionName(getString(R.string.chat_delete));
        conversationPopActions.add(action);
        mConversationPopActions.clear();
        mConversationPopActions.addAll(conversationPopActions);
    }

    /**
     * 长按会话item弹框
     *
     * @param index            会话序列号
     * @param conversationInfo 会话数据对象
     * @param locationX        长按时X坐标
     * @param locationY        长按时Y坐标
     */
    private void showItemPopMenu(final int index, final ConversationInfo conversationInfo, float locationX, float locationY) {
        if (mConversationPopActions == null || mConversationPopActions.size() == 0)
            return;
        View itemPop = LayoutInflater.from(getActivity()).inflate(R.layout.pop_menu_layout, null);
        mConversationPopList = itemPop.findViewById(R.id.pop_menu_list);
        mConversationPopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopMenuAction action = mConversationPopActions.get(position);
                if (action.getActionClickListener() != null) {
                    action.getActionClickListener().onActionClick(index, conversationInfo);
                }
                mConversationPopWindow.dismiss();
            }
        });

        for (int i = 0; i < mConversationPopActions.size(); i++) {
            PopMenuAction action = mConversationPopActions.get(i);
            if (conversationInfo.isTop()) {
                if (action.getActionName().equals(getString(R.string.chat_top))) {
                    action.setActionName(getString(R.string.quit_chat_top));
                }
            } else {
                if (action.getActionName().equals(getString(R.string.quit_chat_top))) {
                    action.setActionName(getResources().getString(R.string.chat_top));
                }

            }
        }
        mConversationPopAdapter = new PopDialogAdapter();
        mConversationPopList.setAdapter(mConversationPopAdapter);
        mConversationPopAdapter.setDataSource(mConversationPopActions);
        mConversationPopWindow = PopWindowUtil.popupWindow(itemPop, mBaseView, (int) locationX, (int) locationY);
        mBaseView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mConversationPopWindow.dismiss();
            }
        }, 10000); // 10s后无操作自动消失
    }

    private void startPopShow(View view, int position, ConversationInfo info) {
        showItemPopMenu(position, info, view.getX(), view.getY() + view.getHeight() / 2);
    }


    private void startChatC2CActivity(ConversationInfo conversationInfo, String userData) {

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(conversationInfo.isGroup() ? V2TIMConversation.V2TIM_GROUP : V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(conversationInfo.getId());
        chatInfo.setChatName(conversationInfo.getTitle());

        chatInfo.setDraft(conversationInfo.getDraft());
        Intent intent = new Intent(MyApp.instance(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.putExtra("userInfo", userData);
//        if (conversationInfo.getLastMessage() != null) {
//            long seq = conversationInfo.getLastMessage().getTimMessage().getSeq();
//            int unRead = conversationInfo.getUnRead();
//            if (unRead > 10) {
//                unRead = unRead > 999 ? 999 : unRead;
//                long unReadSeq = seq - unRead;
//                intent.putExtra(Constants.UNREAD_NUM, unRead);
//                intent.putExtra(Constants.UNREAD_SEQ, unReadSeq);
//            }
//        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApp.instance().startActivity(intent);
    }

    private void startChatGroupActivity(ConversationInfo conversationInfo) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(conversationInfo.isGroup() ? V2TIMConversation.V2TIM_GROUP : V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(conversationInfo.getId());
        chatInfo.setChatName(conversationInfo.getTitle());
        chatInfo.setDraft(conversationInfo.getDraft());
        Intent intent = new Intent(MyApp.instance(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (conversationInfo.getLastMessage() != null) {
            long seq = conversationInfo.getLastMessage().getTimMessage().getSeq();
            int unRead = conversationInfo.getUnRead();
            if (unRead > 10) {
                unRead = Math.min(unRead, 99999);
                long unReadSeq = seq - unRead + 1;
                intent.putExtra(Constants.UNREAD_NUM, unRead);
                intent.putExtra(Constants.UNREAD_SEQ, unReadSeq);
            }
        }
        MyApp.instance().startActivity(intent);
    }

    private void goToGroupInfoActivity(ConversationInfo conversationInfo) {
        String groupId = conversationInfo.getId();
        GroupDetailActivity.Companion.start(MyApp.getInstance(), groupId, 0, false);
    }

    private void goToUserInfoActivity(ConversationInfo conversationInfo) {
        String uid = conversationInfo.getId();
        UserInfoActivity.start(getActivity(), uid);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(ClearConversationEvent event) {
        if (mConversationLayout != null) {
            ConversationLayoutHelper.clearConversation(mConversationLayout);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onIMLoginSuc(TIMLoginEvent timLoginEvent) {
        LogUtil.d("im连接腾讯服务器成功，开始拉取会话列表");
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void isOpenChat(final ConversationInfo conversationInfo) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("otheruid", conversationInfo.getId());
        map.put("is_chat_list", "1");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetOpenChatRestrictAndInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("isopenchat", "onSuccess: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    int chatRetcode = object.getInt("retcode");
                    switch (chatRetcode) {
                        //以使用过邮票可以打开会话
                        case 2000:
                            //互相关注打开会话
                        case 2001:
                            JSONObject obj = object.getJSONObject("data");
                            JSONObject obj1 = obj.getJSONObject("info");
                            startChatC2CActivity(conversationInfo, response);
                            break;
                        case 3001:
                            DialogStampData data = new Gson().fromJson(response, DialogStampData.class);
                            DialogStampData.DataBean datas = data.getData();
                            new StampDialogNew(getActivity(), conversationInfo.getId(), "nickname", datas.getWallet_stamp(), datas.getBasicstampX() + "", datas.getBasicstampY() + "", datas.getBasicstampZ() + "", datas.getSex());
                            break;
                        case 4005:
                        default:
                            ToastUtil.show(getActivity(), object.getString("msg"));
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                refreshCallList();
            }
        });
    }

    public void showMenu() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        ConversationLayoutHelper.showOption(getActivity(), mConversationLayout);
    }

    public void setPaddingTop(int top) {
//        mConversationLayout.setPad(top);
    }

    RecyclerView rvCall;
    private UnFlowLiveData<String> callStatus;

    public void initHeaderView() {
        callStatus = new UnFlowLiveData<String>();
        callStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (adapter != null && s != null && s.length() == 1) {
                    try {
                        adapter.refreshCallStatus(Integer.parseInt(s), mCallTime);
                    } catch (Exception e) {
                    }
                }
            }
        });
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.app_layout_conversation_header, mConversationLayout, false);
        rvCall = headerView.findViewById(R.id.rv_conversation_call);
        LinearLayout llTitle = headerView.findViewById(R.id.ll_conversation_call_title);
        llTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConversationCallListActivity.Companion.start(getActivity());
            }
        });
        mConversationLayout.initHeaderView(headerView);
    }

    ConversationCallAdapter adapter;

    void getCallUserList() {
        HttpHelper.getInstance().getCallList(0, "1", new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                mSmartRefreshLayout.finishRefresh();
                CallUserBean callUserBean = GsonUtil.GsonToBean(data, CallUserBean.class);
                ArrayList<CallUserBean.DataBean> callUserList = new ArrayList<>();
                callUserList.add(new CallUserBean.DataBean());
                if (callUserBean != null && callUserBean.getData() != null) {
                    List<CallUserBean.DataBean> tempList = callUserBean.getData();
                    callUserList.addAll(tempList);
                }
                adapter = new ConversationCallAdapter(getActivity(), callUserList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rvCall.setAdapter(adapter);
                rvCall.setLayoutManager(layoutManager);
                adapter.setItemListener(new OnSimpleItemListener() {
                    @Override
                    public void onItemListener(int position) {
                        if (position == 0) {
                            requestLocation();
                        } else {
                            UserInfoActivity.start(getActivity(), callUserList.get(position).getUid());
                        }
                    }
                });
                getCallStatus(false);
            }

            @Override
            public void onFail(int code, String msg) {
                mSmartRefreshLayout.finishRefresh();
                ArrayList<CallUserBean.DataBean> callUserList = new ArrayList<>();
                callUserList.add(new CallUserBean.DataBean());
                ConversationCallAdapter adapter = new ConversationCallAdapter(getActivity(), callUserList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rvCall.setAdapter(adapter);
                rvCall.setLayoutManager(layoutManager);
                adapter.setItemListener(new OnSimpleItemListener() {
                    @Override
                    public void onItemListener(int position) {
                        if (position == 0) {
                            //getCallStatus();
                            requestLocation();
                        } else {
                            UserInfoActivity.start(getActivity(), callUserList.get(position).getUid());
                        }
                    }
                });
                getCallStatus(false);
            }
        });
    }

    void requestLocation() {
        String[] perms = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
        XXPermissions.with(this)
                .permission(perms)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            getCallStatus(true);
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        ToastUtil.show("使用呼唤前，请收拾您的定位功能");
                    }
                });
    }

    private String mCallTime = "";

    void getCallStatus(boolean needToast) {
        HttpHelper.getInstance().getConversationCallStatus(new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                ConversationCallStatusBean conversationCallStatusBean = GsonUtil.GsonToBean(data, ConversationCallStatusBean.class);
                if (conversationCallStatusBean != null && conversationCallStatusBean.getData() != null) {
                    if (needToast) {
                        if (!"0".equals(conversationCallStatusBean.getData().getStatus())) {
                            ToastUtil.show(conversationCallStatusBean.getData().getToast_tip());
                        } else {
                            if (!"0".equals(conversationCallStatusBean.getData().getCall_times())) {
                                showUseCallPop();
                            } else {
                                showBuyCallPop();
                            }
                        }
                    }
                    mCallTime = conversationCallStatusBean.getData().getRemaining_time();
                    callStatus.postValue(conversationCallStatusBean.getData().getStatus());
                }
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(msg);
            }
        });
    }

    void showBuyCallPop() {
        ConversationCallBuyPop callBuyPop = new ConversationCallBuyPop(getActivity());
        callBuyPop.showPopupWindow();
        callBuyPop.setOnBuyCallListener(new ConversationCallBuyPop.OnBuyCallListener() {
            @Override
            public void doBuyCallSuc() {
                showUseCallPop();
            }
        });
    }

    void showUseCallPop() {
        ConversationCallUsePop useCallPop = new ConversationCallUsePop(getActivity());
        useCallPop.showPopupWindow();
        useCallPop.setOnCallPopListener(new ConversationCallUsePop.OnCallPopListener() {
            @Override
            public void doUseCall() {
                getCallUserList();
            }
        });
    }

    int refreshTime = 0;

    public void refreshCallList() {
        refreshTime++;
        if (refreshTime > 1) {
            getCallUserList();
        }
    }
}
