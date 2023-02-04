package com.aiwujie.shengmo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.binding.BindingMobileActivity;
import com.aiwujie.shengmo.activity.newui.VipMemberCenterActivity;
import com.aiwujie.shengmo.adapter.GroupMemberAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GroupInfoData;
import com.aiwujie.shengmo.bean.GroupInfoMemberData;
import com.aiwujie.shengmo.customview.MyGridview;
import com.aiwujie.shengmo.customview.SharedPop;
import com.aiwujie.shengmo.eventbus.FinishConversationEvent;
import com.aiwujie.shengmo.eventbus.FinishGroupInfoEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.GroupDetailActivity;
import com.aiwujie.shengmo.kt.ui.activity.GroupMemberActivity;
import com.aiwujie.shengmo.kt.ui.activity.HomePageActivity;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.tim.chat.ChatActivity;
import com.aiwujie.shengmo.tim.utils.Constants;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.listener.NormalContentListener;
import com.aiwujie.shengmo.view.GroupClaimPop;
import com.aiwujie.shengmo.view.NormalTipsPop;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

//import com.tencent.imsdk.TIMConversationType;

public class GroupInfoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, OnItemClickListener, V2TIMCallback {

    @BindView(R.id.mGroupInfo_return)
    ImageView mGroupInfoReturn;
    @BindView(R.id.mGroupInfo_groupname)
    TextView mGroupInfoGroupname;
    @BindView(R.id.mGroupInfo_groupid)
    TextView mGroupInfoGroupid;
    @BindView(R.id.mGroupInfo_right)
    ImageView mGroupInfoRight;
    @BindView(R.id.mGroupInfo_right2)
    ImageView mGroupInfoRight2;
    @BindView(R.id.mGroupInfo_ll_editInfo)
    LinearLayout mGroupInfoLlEditInfo;
    @BindView(R.id.mGroupInfo_ll_qingli)
    LinearLayout mGroupInfo_ll_qingli;
    @BindView(R.id.mGroupInfo_peoplecount)
    TextView mGroupInfoPeoplecount;
    @BindView(R.id.mGroupInfo_gridview)
    MyGridview mGroupInfoGridview;
    @BindView(R.id.mGroupInfo_addr)
    TextView mGroupInfoAddr;
    @BindView(R.id.mGroupInfo_ck)
    CheckBox mGroupInfoCk;
    @BindView(R.id.mGroupInfo_clearChat)
    LinearLayout mGroupInfoClearChat;
    @BindView(R.id.mGroupInfo_setManager)
    LinearLayout mGroupInfoSetManager;
    @BindView(R.id.mGroupInfo_ll_shengji)
    LinearLayout mGroupInfoLlShengji;
    @BindView(R.id.mGroupInfo_intro)
    TextView mGroupInfoIntro;
    @BindView(R.id.mGroupInfo_btn01)
    Button mGroupInfoBtn01;
    @BindView(R.id.mGroupInfo_btn02)
    Button mGroupInfoBtn02;
    @BindView(R.id.mGroupInfo_ll_miandarao)
    LinearLayout mGroupInfoLlMiandarao;
    @BindView(R.id.mGroupInfo_allMember)
    LinearLayout mGroupInfoAllMember;
    @BindView(R.id.mGroupInfo_managerCount)
    TextView mGroupInfoManagerCount;
    @BindView(R.id.group_cardname)
    TextView group_cardname;
    @BindView(R.id.mGroupInfo_ll_intro)
    LinearLayout mGroupInfoLlIntro;
    @BindView(R.id.mGroupInfo_groupIcon)
    ImageView mGroupInfoGroupIcon;
    @BindView(R.id.mGroupInfo_metionAll)
    LinearLayout mGroupInfoMetionAll;
    @BindView(R.id.mGroupInfo_ll_card)
    LinearLayout mGroupInfo_ll_card;
    @BindView(R.id.mGroupInfo_vip)
    ImageView mGroupInfoVip;
    @BindView(R.id.mGroupInfo_sandian)
    ImageView mGroupInfoSandian;
    @BindView(R.id.mGroupInfo_layout)
    RelativeLayout mGroupInfoLayout;
    @BindView(R.id.cb_pass_request)
    CheckBox cbPassRequest;
    @BindView(R.id.mGroupInfo_ll_passRequest)
    LinearLayout mGroupInfoLlPassRequest;
    @BindView(R.id.mGroupInfo_create_time)
    TextView mGroupInfoCreateTime;
    @BindView(R.id.mGroupInfo_cn_mute_all)
    CheckBox mGroupInfoCnMuteAll;
    @BindView(R.id.mGroupInfo_ll_mute_all)
    LinearLayout mGroupInfoLlMuteAll;
    @BindView(R.id.mGroupInfo_voice_ck)
    CheckBox mGroupInfoVoiceCk;
    @BindView(R.id.mGroupInfo_ll_voice)
    LinearLayout mGroupInfoLlVoice;
    @BindView(R.id.ll_group_info_strive)
    LinearLayout llGroupInfoStrive;
    @BindView(R.id.tv_claim_rule)
    TextView tvClaimRule;
    @BindView(R.id.iv_group_info_address)
    ImageView ivGroupInfoAddress;
    private String groupId;
    private int mConversationType;
    Handler handler = new Handler();
    private String groupIcon;
    private String name;
    private String userPower = "";
    private String memberCount;
    private String memberZong;
    private GroupInfoData data;
    private String managerCount;
    private String managerZong;
    //群主id
    private String groupLeader;
    private List<GroupInfoMemberData.DataBean> groupMemberDatas;
    private int inviteState;
    private String cardname;
    private Intent intent;
    private String autoCheck = "0";

    private String province, city, lat, lng;
    private String groupLeaderLet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        ButterKnife.bind(this);
        intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        GroupDetailActivity.Companion.start(MyApp.getInstance(), groupId, 0,false);
        if (true) {
            finish();
            return;
        }
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        EventBus.getDefault().register(this);

        inviteState = intent.getIntExtra("inviteState", -5);
        boolean isChat = intent.getBooleanExtra("isChat", true);
        if (!isChat) {
            mGroupInfoBtn02.setVisibility(View.GONE);
        }
        //消息免打扰
        //getStateListener();
        setListener();
        getGroupinfo();

    }

    private void getStateListener() {

        V2TIMManager.getGroupManager().getGroupsInfo(Arrays.asList(groupId), new V2TIMValueCallback<List<V2TIMGroupInfoResult>>() {
            @Override
            public void onSuccess(List<V2TIMGroupInfoResult> v2TIMGroupInfoResults) {
                for (V2TIMGroupInfoResult v2TIMGroupInfo : v2TIMGroupInfoResults) {
                    if (v2TIMGroupInfo.getGroupInfo().getGroupID().equals(groupId)) {
                        boolean muteState = v2TIMGroupInfo.getGroupInfo().isAllMuted();
                        mGroupInfoCnMuteAll.setChecked(muteState);
                        switch (v2TIMGroupInfo.getGroupInfo().getRecvOpt()) {
                            case V2TIMMessage.V2TIM_NOT_RECEIVE_MESSAGE: //消息免打扰
//                                mGroupInfoCk.setChecked(true);
//                                mGroupInfoVoiceCk.setChecked(false);
//                                mGroupInfoLlVoice.setVisibility(View.GONE);
                                mGroupInfoCk.setChecked(false);
                                mGroupInfoVoiceCk.setChecked(false);
                                mGroupInfoLlVoice.setVisibility(View.VISIBLE);
                                break;
                            case V2TIMMessage.V2TIM_RECEIVE_MESSAGE: //开启群声音
                                mGroupInfoCk.setChecked(false);
                                mGroupInfoVoiceCk.setChecked(true);
                                mGroupInfoLlVoice.setVisibility(View.VISIBLE);
                                break;
                            case V2TIMMessage.V2TIM_RECEIVE_NOT_NOTIFY_MESSAGE: //关闭群声音
                                mGroupInfoCk.setChecked(false);
                                mGroupInfoVoiceCk.setChecked(false);
                                mGroupInfoLlVoice.setVisibility(View.VISIBLE);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onError(int code, String desc) {

            }
        });

//        V2TIMManager.getGroupManager().getJoinedGroupList(new V2TIMValueCallback<List<V2TIMGroupInfo>>() {
//            @Override
//            public void onError(int code, String desc) {
//
//            }
////            /**
////             * ## 在线正常接收消息，离线时会进行厂商离线推送
////             */
////            public static final int V2TIM_GROUP_RECEIVE_MESSAGE = 0;
////            /**
////             * ## 不会接收到群消息
////             */
////            public static final int V2TIM_GROUP_NOT_RECEIVE_MESSAGE = 1;
////            /**
////             * ## 在线正常接收消息，离线不会有推送通知
////             */
////            public static final int V2TIM_GROUP_RECEIVE_NOT_NOTIFY_MESSAGE = 2;
//            @Override
//            public void onSuccess(List<V2TIMGroupInfo> v2TIMGroupInfos) {
//                for (V2TIMGroupInfo v2TIMGroupInfo : v2TIMGroupInfos) {
//                    if (groupId.equals(v2TIMGroupInfo.getGroupID())) {
//                        switch (v2TIMGroupInfo.getRecvOpt()) {
//                            case 1:
//                            case 2:
//                                mGroupInfoCk.setChecked(false);
//                                break;
//                            case 0:
//                                mGroupInfoCk.setChecked(true);
//                                break;
//
//                            default:
//                                break;
//
//                        }
//
//                    }
//                }
//
//            }
//        });
    }

    private void setListener() {
        mGroupInfoCk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGroupInfoCk.isChecked()) {
                    V2TIMManager.getMessageManager().setGroupReceiveMessageOpt(groupId, V2TIMMessage.V2TIM_NOT_RECEIVE_MESSAGE, null);
                    mGroupInfoLlVoice.setVisibility(View.GONE);
                    mGroupInfoVoiceCk.setChecked(false);
                } else {
                    mGroupInfoLlVoice.setVisibility(View.VISIBLE);
                    V2TIMManager.getMessageManager().setGroupReceiveMessageOpt(groupId, V2TIMMessage.V2TIM_RECEIVE_NOT_NOTIFY_MESSAGE, null);
                }
            }
        });
        mGroupInfoVoiceCk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGroupInfoVoiceCk.isChecked()) {
                    V2TIMManager.getMessageManager().setGroupReceiveMessageOpt(groupId, V2TIMMessage.V2TIM_RECEIVE_MESSAGE, null);
                } else {
                    V2TIMManager.getMessageManager().setGroupReceiveMessageOpt(groupId, V2TIMMessage.V2TIM_RECEIVE_NOT_NOTIFY_MESSAGE, null);
                }
            }
        });
        mGroupInfoGridview.setOnItemClickListener(this);
//        cbPassRequest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                changeGroupCheckState(isChecked);
//            }
//        });
        cbPassRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGroupCheckState(cbPassRequest.isChecked());
            }
        });
        mGroupInfoCnMuteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muteGroup(mGroupInfoCnMuteAll.isChecked());
            }
        });
//        mGroupInfoCnMuteAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                muteGroup(isChecked);
//            }
//        });

        llGroupInfoStrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClaimState();
            }
        });

        tvClaimRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GroupInfoActivity.this, BannerWebActivity.class);
                intent1.putExtra("path", NetPic() + "Home/Info/Shengmosimu/id/20");
                intent1.putExtra("title", "圣魔");
                startActivity(intent1);
            }
        });

        mGroupInfoAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("2".equals(userPower) || "3".equals(userPower)) {
                    Intent intent = new Intent(GroupInfoActivity.this, ChangeGroupLocationActivity.class);
                    intent.putExtra("province", province);
                    intent.putExtra("city", city);
                    intent.putExtra("groupId",groupId);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lng", lng);
                    startActivityForResult(intent,1000);
                }
            }
        });


    }

    void getClaimState() {

        HttpHelper.getInstance().getClaimState(groupId, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                showClaimPop();
            }

            @Override
            public void onFail(int code, String msg) {
                if (code == 4002) {
                    showVipPop(msg);
                } else if (code == 4003) {
                    showBindPhonePop(msg);
                } else {
                    ToastUtil.show(GroupInfoActivity.this, msg);
                }
            }
        });
    }

    void showVipPop(String msg) {
        final NormalTipsPop normalTipsPop = new NormalTipsPop
                .Builder(GroupInfoActivity.this)
                .setTitle("权限不足")
                .setInfo(msg)
                .setCancelStr("取消")
                .setConfirmStr("去开通")
                .build();
        normalTipsPop.showPopupWindow();
        normalTipsPop.setOnPopClickListener(new NormalTipsPop.OnPopClickListener() {
            @Override
            public void cancelClick() {
                normalTipsPop.dismiss();
            }

            @Override
            public void confirmClick() {
                normalTipsPop.dismiss();
                Intent intent = new Intent(GroupInfoActivity.this, VipMemberCenterActivity.class);
                String headpic = (String) SharedPreferencesUtils.getParam(GroupInfoActivity.this, "headurl", "");
                intent.putExtra("headpic", headpic);
                intent.putExtra("uid", MyApp.uid);
                startActivity(intent);
            }
        });
    }

    void showBindPhonePop(String msg) {
        final NormalTipsPop normalTipsPop = new NormalTipsPop
                .Builder(GroupInfoActivity.this)
                .setTitle("绑定手机号")
                .setInfo(msg)
                .setCancelStr("取消")
                .setConfirmStr("去绑定")
                .build();
        normalTipsPop.showPopupWindow();
        normalTipsPop.setOnPopClickListener(new NormalTipsPop.OnPopClickListener() {
            @Override
            public void cancelClick() {
                normalTipsPop.dismiss();
            }

            @Override
            public void confirmClick() {
                normalTipsPop.dismiss();
                Intent intent = new Intent(GroupInfoActivity.this, BindingMobileActivity.class);
                intent.putExtra("neworchange", "new");
                startActivity(intent);
            }
        });
    }

    void showClaimPop() {
        final GroupClaimPop groupClaimPop = new GroupClaimPop(GroupInfoActivity.this);
        groupClaimPop.showPopupWindow();
        groupClaimPop.setNormalContentListener(new NormalContentListener() {
            @Override
            public void onNormalContent(String content) {
                addClaim(groupClaimPop, content);
            }
        });
    }

    void addClaim(final GroupClaimPop groupClaimPop, String reason) {
        HttpHelper.getInstance().addClaimGroup(groupId, reason, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(GroupInfoActivity.this)) {
                    return;
                }
                try {
                    JSONObject obj = new JSONObject(data);
                    ToastUtil.show(GroupInfoActivity.this, obj.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                groupClaimPop.dismiss();
            }

            @Override
            public void onFail(String msg) {
                if (SafeCheckUtil.isActivityFinish(GroupInfoActivity.this)) {
                    return;
                }
                ToastUtil.show(GroupInfoActivity.this, msg);
            }
        });
    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if (isChecked) {
//            //V2TIMManager.getGroupManager().setReceiveMessageOpt(groupId, V2TIMGroupInfo.V2TIM_GROUP_RECEIVE_MESSAGE, this);
//            //V2TIMManager.getMessageManager().setGroupReceiveMessageOpt(groupId, V2TIMMessage.V2TIM_RECEIVE_MESSAGE, null);
//        } else {
//            //V2TIMManager.getGroupManager().setReceiveMessageOpt(groupId, V2TIMGroupInfo.V2TIM_GROUP_RECEIVE_NOT_NOTIFY_MESSAGE, this);
//            //V2TIMManager.getMessageManager().setGroupReceiveMessageOpt(groupId, V2TIMMessage.V2TIM_RECEIVE_NOT_NOTIFY_MESSAGE, null);
//            //V2TIMManager.getMessageManager().setGroupReceiveMessageOpt(groupId, V2TIMMessage.V2TIM_NOT_RECEIVE_MESSAGE, null);//消息免打扰
//        }
//    }

    private void getGroupMember() {
        Map<String, String> map = new HashMap<>();
        map.put("gid", groupId);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("pagetype", "1");
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetGroupMember, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        GroupMemberAdapter adapter;
                        groupMemberDatas = new ArrayList<>();
                        try {
                            GroupInfoMemberData groupInfoMemberData = new Gson().fromJson(response, GroupInfoMemberData.class);
                            if (userPower.equals("-1") || userPower.equals("0")) {
                                groupMemberDatas.clear();
                                groupMemberDatas.addAll(groupInfoMemberData.getData());
                                adapter = new GroupMemberAdapter(GroupInfoActivity.this, groupInfoMemberData.getData(), groupId, userPower);
                            } else {
                                GroupInfoMemberData.DataBean groupinfoData = new GroupInfoMemberData.DataBean();
                                groupMemberDatas.addAll(groupInfoMemberData.getData());
                                if (groupMemberDatas.size() == 6) {
                                    groupMemberDatas.remove(5);
                                }
                                groupMemberDatas.add(groupinfoData);
                                adapter = new GroupMemberAdapter(GroupInfoActivity.this, groupMemberDatas, groupId, userPower,groupLeaderLet);
                            }
                            mGroupInfoGridview.setAdapter(adapter);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void getGroupinfo() {
        Map<String, String> map = new HashMap<>();
        map.put("gid", groupId);
        map.put("uid", MyApp.uid);
        final IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetGroupinfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("groupInfo", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            data = new Gson().fromJson(response, GroupInfoData.class);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        if (data == null || data.getData() == null) {
                            return;
                        }
                        cardname = data.getData().getCardname();
                        if (!TextUtil.isEmpty(cardname)) {
                            group_cardname.setText(cardname);
                        }
                        if ("1".equals(data.getData().getIs_claim())) {
                            llGroupInfoStrive.setVisibility(View.VISIBLE);
                            //tvClaimRule.setVisibility(View.VISIBLE);
                        } else {
                            llGroupInfoStrive.setVisibility(View.GONE);
                            //tvClaimRule.setVisibility(View.GONE);
                        }
                        groupLeader = data.getData().getUid();
                        userPower = data.getData().getUserpower();
                        name = data.getData().getGroupname();
                        mGroupInfoGroupname.setText(name);
                        String groupNum = data.getData().getGroup_num();
                        if (groupNum.length() <= 5) {
                            mGroupInfoVip.setVisibility(View.VISIBLE);
                        }
                        groupLeaderLet = data.getData().getGroup_leader_let();
                        mGroupInfoGroupid.setText("群号: " + groupNum);
                        mGroupInfoCreateTime.setText("创建：" + data.getData().getCreate_time());
                        memberCount = data.getData().getMember();
                        memberZong = data.getData().getMax_member();
                        mGroupInfoPeoplecount.setText("(" + memberCount + "/" + memberZong + ")");
                        mGroupInfoIntro.setText(data.getData().getIntroduce());
                        province = data.getData().getProvince();
                        city = data.getData().getCity();
                        lat = data.getData().getLat();
                        lng = data.getData().getLng();
                        mGroupInfoAddr.setText(data.getData().getProvince() + " " + data.getData().getCity());
                        mGroupInfoLlMuteAll.setVisibility(View.GONE);
                        mGroupInfoLlVoice.setVisibility(View.GONE);
                        ivGroupInfoAddress.setVisibility(View.GONE);
                        mGroupInfoSetManager.setVisibility(View.GONE);
                        mGroupInfoClearChat.setVisibility(View.GONE);
                        switch (userPower) {
                            case "-1":
                                if (inviteState == -5 || inviteState == 0) {
                                    mGroupInfoLlMiandarao.setVisibility(View.GONE);
                                    mGroupInfoLlPassRequest.setVisibility(View.GONE);
                                    mGroupInfoClearChat.setVisibility(View.GONE);
                                    mGroupInfoBtn01.setVisibility(View.GONE);
                                    mGroupInfoSetManager.setVisibility(View.GONE);
                                    mGroupInfoLlShengji.setVisibility(View.GONE);
                                    mGroupInfoBtn02.setText("申请入群");
                                    mGroupInfoLlEditInfo.setEnabled(false);
                                    mGroupInfoRight.setVisibility(View.GONE);
                                    mGroupInfoRight.setVisibility(View.GONE);
                                    mGroupInfoRight2.setVisibility(View.GONE);
                                    mGroupInfo_ll_card.setVisibility(View.GONE);
                                    //退群了 但是还是从群聊天里面进来
//                                    boolean isChat = intent.getBooleanExtra("isChat", true);
//                                    if (!isChat) {
//                                        mGroupInfoBtn02.setVisibility(View.VISIBLE);
//                                    }
                                } else if (inviteState == 1) {
                                    mGroupInfoLlMiandarao.setVisibility(View.GONE);
                                    mGroupInfoLlPassRequest.setVisibility(View.GONE);
                                    mGroupInfoClearChat.setVisibility(View.GONE);
                                    mGroupInfoBtn01.setVisibility(View.GONE);
                                    mGroupInfoSetManager.setVisibility(View.GONE);
                                    mGroupInfoLlShengji.setVisibility(View.GONE);
                                    mGroupInfoBtn02.setText("同意加入群");
                                    mGroupInfoLlEditInfo.setEnabled(false);
                                    mGroupInfoRight.setVisibility(View.GONE);
                                    mGroupInfoRight.setVisibility(View.GONE);
                                    mGroupInfoRight2.setVisibility(View.GONE);
                                }
                                break;
                            case "0":
                                mGroupInfoLlMiandarao.setVisibility(View.GONE);
                                mGroupInfoLlPassRequest.setVisibility(View.GONE);
                                mGroupInfoClearChat.setVisibility(View.GONE);
                                mGroupInfoBtn01.setVisibility(View.GONE);
                                mGroupInfoSetManager.setVisibility(View.GONE);
                                mGroupInfoLlShengji.setVisibility(View.GONE);
                                mGroupInfoBtn02.setText("已申请入群");
                                mGroupInfoLlEditInfo.setEnabled(false);
                                mGroupInfoRight.setVisibility(View.GONE);
                                mGroupInfoRight.setVisibility(View.GONE);
                                mGroupInfoRight2.setVisibility(View.GONE);
                                mGroupInfo_ll_card.setVisibility(View.GONE);
                                break;
                            case "1":
                                //群成员
                                getStateListener();
                                mGroupInfoLlVoice.setVisibility(View.VISIBLE);
                                // mGroupInfoLlMiandarao.setVisibility(View.VISIBLE);
                                mGroupInfoLlPassRequest.setVisibility(View.GONE);
                                mGroupInfoClearChat.setVisibility(View.VISIBLE);
                                mGroupInfoBtn01.setVisibility(View.VISIBLE);
                                mGroupInfoBtn01.setText("退出群组");
                                mGroupInfoLlEditInfo.setEnabled(false);
                                mGroupInfoRight.setVisibility(View.GONE);
                                mGroupInfoRight2.setVisibility(View.GONE);
                                break;
                            case "2":
                                //管理员
                                ivGroupInfoAddress.setVisibility(View.VISIBLE);
                                getStateListener();
                                mGroupInfoLlVoice.setVisibility(View.VISIBLE);
                                mGroupInfo_ll_qingli.setVisibility(View.VISIBLE);
                                mGroupInfoMetionAll.setVisibility(View.VISIBLE);
                                //mGroupInfoLlMuteAll.setVisibility(View.VISIBLE);
                                //mGroupInfoLlMiandarao.setVisibility(View.VISIBLE);
                                mGroupInfoLlPassRequest.setVisibility(View.VISIBLE);
                                mGroupInfoClearChat.setVisibility(View.VISIBLE);
                                mGroupInfoBtn01.setVisibility(View.VISIBLE);
                                mGroupInfoBtn01.setText("退出群组");
//                                mGroupInfoLlShengji.setVisibility(View.VISIBLE);
                                mGroupInfoRight.setVisibility(View.VISIBLE);
                                mGroupInfoRight2.setVisibility(View.VISIBLE);
                                managerCount = data.getData().getManager();
                                managerZong = data.getData().getManagerStr();
                                mGroupInfoManagerCount.setText(managerCount + managerZong);
                                //修改群介绍
                                editGroupIntro();
                                break;
                            case "3":
                                //群主
                                ivGroupInfoAddress.setVisibility(View.VISIBLE);
                                getStateListener();
                                mGroupInfoLlVoice.setVisibility(View.VISIBLE);
                                mGroupInfo_ll_qingli.setVisibility(View.VISIBLE);
                                //mGroupInfoLlMuteAll.setVisibility(View.VISIBLE);
                                mGroupInfoMetionAll.setVisibility(View.VISIBLE);
                                //mGroupInfoLlMiandarao.setVisibility(View.VISIBLE);
                                mGroupInfoLlPassRequest.setVisibility(View.VISIBLE);
                                mGroupInfoClearChat.setVisibility(View.VISIBLE);
                                mGroupInfoBtn01.setVisibility(View.VISIBLE);
                                mGroupInfoSetManager.setVisibility(View.VISIBLE);
                                //mGroupInfoLlShengji.setVisibility(View.VISIBLE);
                                mGroupInfoRight.setVisibility(View.VISIBLE);
                                mGroupInfoRight2.setVisibility(View.VISIBLE);
                                managerCount = data.getData().getManager();
                                managerZong = data.getData().getManagerStr();
                                mGroupInfoManagerCount.setText(managerCount + managerZong);
                                //修改群介绍
                                editGroupIntro();
                                break;
                        }
                        groupIcon = data.getData().getGroup_pic();
                        try {
                            if (groupIcon.equals(NetPic())) {//"http://59.110.28.150:888/"
                                mGroupInfoGroupIcon.setImageResource(R.mipmap.qunmorentouxiang);
                            } else {
                                GlideImgManager.glideLoader(GroupInfoActivity.this, groupIcon, R.mipmap.qunmorentouxiang, R.mipmap.qunmorentouxiang, mGroupInfoGroupIcon, 0);
                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        autoCheck = data.getData().getIs_auto_check();
                        if ("0".equals(data.getData().getIs_auto_check())) {
                            cbPassRequest.setChecked(false);
                        } else {
                            cbPassRequest.setChecked(true);
                        }

                    }
                });
                //获取群信息上的群组成员
                getGroupMember();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void editGroupIntro() {
        mGroupInfoLlIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupInfoActivity.this, GroupIntroActivity.class);
                intent.putExtra("groupintro", mGroupInfoIntro.getText().toString());
                intent.putExtra("gid", groupId);
                startActivityForResult(intent, 312);
            }
        });
    }

    @OnClick({R.id.mGroupInfo_ll_card, R.id.mGroupInfo_ll_qingli, R.id.mGroupInfo_return, R.id.mGroupInfo_ll_editInfo, R.id.mGroupInfo_clearChat, R.id.mGroupInfo_setManager, R.id.mGroupInfo_ll_shengji, R.id.mGroupInfo_btn01, R.id.mGroupInfo_btn02, R.id.mGroupInfo_allMember, R.id.mGroupInfo_metionAll, R.id.mGroupInfo_groupIcon, R.id.mGroupInfo_sandian})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mGroupInfo_return:
                finish();
                break;
            case R.id.mGroupInfo_ll_editInfo:
                intent = new Intent(this, EditGroupActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("groupId", groupId);
                intent.putExtra("groupicon", groupIcon);
                startActivity(intent);
                break;
            case R.id.mGroupInfo_ll_qingli:
//                android.app.AlertDialog.Builder builder12 = new android.app.AlertDialog.Builder(this);
//                builder12.setMessage("您确定清理7天未登录的用户吗").setPositiveButton("取消", null).setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        sendqingli();
//                    }
//                }).create().show();
                new AlertView(null, null, "取消", null,
                        new String[]{"清理7天未登录", "清理15天未登录", "清理31天未登录"},
                        this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position, String data) {
                        switch (position) {
                            case 0:
                                sendqingli("7");
                                break;
                            case 1:
                                sendqingli("15");
                                break;
                            case 2:
                                sendqingli("31");
                                break;
                        }
                    }
                }).show();

                break;
            case R.id.mGroupInfo_allMember:
                if (data == null || data.getData() == null) {
                    return;
                }
                //intent = new Intent(this, MemberActivity.class);
                intent = new Intent(this, GroupMemberActivity.class);
                intent.putExtra("memberFlag", "1");
                intent.putExtra("gid", groupId);
                intent.putExtra("state", data.getData().getUserpower());
                intent.putExtra("leaderLet",data.getData().getGroup_leader_let());
                startActivityForResult(intent,1001);
                break;
            case R.id.mGroupInfo_clearChat:
                //清空群聊天记录
                clearGroupChat();
                break;
            case R.id.mGroupInfo_setManager:
                //intent = new Intent(this, MemberActivity.class);
                intent = new Intent(this, GroupMemberActivity.class);
                intent.putExtra("memberFlag", "2");
                intent.putExtra("gid", groupId);
                intent.putExtra("state", data.getData().getUserpower());
                startActivityForResult(intent,1001);
                break;
            case R.id.mGroupInfo_ll_shengji:
                intent = new Intent(this, UpLevelActivity.class);
                intent.putExtra("groupIcon", groupIcon);
                startActivity(intent);
                break;
            case R.id.mGroupInfo_btn01:
                switch (userPower) {
                    case "1":
                    case "2":
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("确认退出吗?")
                                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exitGroup();
                            }
                        }).create().show();
                        break;
                    case "3":
                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                        builder1.setMessage("确认解散吗?")
                                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delGroup();
                            }
                        }).create().show();
                        break;
                }
                break;
            case R.id.mGroupInfo_btn02:
                Log.i("GroupLogLog", "onClick1: " + userPower);
                switch (userPower) {
                    case "-1":
                    case "0":
                        Log.i("GroupLogLog", "onClick2: " + userPower + "," + inviteState);
                        if (inviteState == -5 || inviteState == 0) {
                            if (Integer.parseInt(memberCount) == Integer.parseInt(memberZong)) {
                                ToastUtil.show(getApplicationContext(), "您加入的群已满");
                            } else {
                                sendapplyfor();
                            }
                        } else if (inviteState == 1) {
                            mGroupInfoBtn02.setClickable(false);
                            //同意加入群
                            agreeJoinGroup();
                        }
                        break;
                    case "1":
                    case "2":
                    case "3":
                        //是否禁言  2为禁言  1解除禁言
                        String isSpeak = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "nospeak", "1");
                        if (isSpeak.equals("0")) {
                            ToastUtil.show(getApplicationContext(), "您因违规被系统禁用聊天功能，如有疑问请与客服联系！");
                        } else {
                            ChatInfo chatInfo = new ChatInfo();
                            chatInfo.setType(V2TIMConversation.V2TIM_GROUP);
                            chatInfo.setId(groupId);
                            chatInfo.setChatName(name);
                            intent = new Intent(MyApp.instance(), ChatActivity.class);
                            intent.putExtra(Constants.CHAT_INFO, chatInfo);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            MyApp.instance().startActivity(intent);
                        }
                        break;
                }
                break;

            case R.id.mGroupInfo_metionAll:
                intent = new Intent(this, SendMetionAllMsgActivity.class);
                intent.putExtra("mTargetId", groupId);
                startActivity(intent);
                break;
            case R.id.mGroupInfo_groupIcon:
                ArrayList<String> ivlist = new ArrayList<>();
                ivlist.add(groupIcon);
                intent = new Intent(this, ZoomActivity.class);
                intent.putExtra("pics", ivlist);
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            case R.id.mGroupInfo_sandian:
                if (userPower.equals("3")) {
                    new AlertView(null, null, "取消", null,
                            new String[]{"分享"},
                            this, AlertView.Style.ActionSheet, this).show();
                } else {
                    new AlertView(null, null, "取消", null,
                            new String[]{"分享", "举报"},
                            this, AlertView.Style.ActionSheet, this).show();
                }
                break;
            case R.id.mGroupInfo_ll_card:
                geteditGroupCardName();
                break;
        }
    }

    private void agreeJoinGroup() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid", groupId);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.AgreeIntoGroupOne, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("agreejoingroup", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    finish();
                                    break;
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                case 4006:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                            }
                            mGroupInfoBtn02.setClickable(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void clearGroupChat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("确认清空记录吗？").setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                V2TIMManager.getMessageManager().clearGroupHistoryMessage(groupId, new V2TIMCallback() {
                    @Override
                    public void onSuccess() {
                        dialog.dismiss();
                        ToastUtil.show(getApplicationContext(), "清理成功");
                        EventBus.getDefault().post(new FinishConversationEvent());
                        finish();
                    }

                    @Override
                    public void onError(int code, String desc) {
                        dialog.dismiss();
                        ToastUtil.show(getApplicationContext(), "网络异常,请稍后重试");
                    }
                });
//                V2TIMManager.getConversationManager().deleteConversation("group_" + groupId, new V2TIMCallback() {
//                    @Override
//                    public void onError(int code, String desc) {
//                        dialog.dismiss();
//                        ToastUtil.show(getApplicationContext(), "网络异常,请稍后重试");
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        dialog.dismiss();
//                        ToastUtil.show(getApplicationContext(), "清理成功");
//                        EventBus.getDefault().post(new FinishConversationEvent());
//                        finish();
//                    }
//                });

            }
        }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();

    }

    private void delGroup() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid", groupId);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.DelGroup, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4005:
                                case 4006:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 2000:
                                    finish();
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    EventBus.getDefault().post("refreshgroup");
                                    break;
                                default:
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void exitGroup() {
        HttpHelper.getInstance().applyQuitGroup(groupId, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                exitGroupWithTim();
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(GroupInfoActivity.this, msg);
            }
        });
    }


    private void exitGroupWithTim() {

        V2TIMManager.getInstance().quitGroup(groupId, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.show(getApplicationContext(), desc);
            }

            @Override
            public void onSuccess() {
                ToastUtil.show(getApplicationContext(), "成功退出群组");
                V2TIMManager.getMessageManager().clearGroupHistoryMessage(groupId, null);
                //Intent intent = new Intent(GroupInfoActivity.this, MainActivity.class);
                Intent intent = new Intent(GroupInfoActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(String message) {
        if (message.equals("editgroupsuccess")) {
            getGroupinfo();
        }
        if (message.equals("joinsuceess")) {
            getGroupinfo();
        }
        if (message.equals("operationsuccess")) {
            getGroupMember();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(FinishGroupInfoEvent event) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 312 && resultCode == RESULT_OK) {
            try {
                mGroupInfoIntro.setText(data.getStringExtra("groupintro"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 1000 && resultCode == RESULT_OK) {
            getGroupinfo();
        } else if (requestCode == 1001 && resultCode == RESULT_OK) {
            getGroupinfo();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PesonInfoActivity.class);
        intent.putExtra("uid", groupMemberDatas.get(position).getUid());
        startActivity(intent);
    }

    @Override
    public void onItemClick(Object o, int position, String data) {
        switch (position) {
            case 0:
                showShareWay();
                break;
            case 1:
                Intent intent = new Intent(this, ReportActivity.class);
                intent.putExtra("uid", groupLeader);
                startActivity(intent);
                break;
        }
    }

    private void showShareWay() {
        SharedPop sharedPop = new SharedPop(this, NetPic() + HttpUrl.ShareGroupDetail + groupId, "来自圣魔的群组", "邀请你加入群组：" + data.getData().getGroupname() + "。快快加入吧~", groupIcon, 0, 3, "", "", "", "");
        sharedPop.showAtLocation(mGroupInfoLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final WindowManager.LayoutParams[] params = {getWindow().getAttributes()};
        //当弹出Popupwindow时，背景变半透明
        params[0].alpha = 0.7f;
        getWindow().setAttributes(params[0]);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        sharedPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params[0] = getWindow().getAttributes();
                params[0].alpha = 1f;
                getWindow().setAttributes(params[0]);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void sendapplyfor() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.getGirlState, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                PrintLogUtils.log(response, "--");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    Intent intent = new Intent(GroupInfoActivity.this, VerificationActivity.class);
                                    intent.putExtra("gid", groupId);
                                    startActivity(intent);
                                    break;
                                default:
                                    ToastUtil.show(GroupInfoActivity.this, object.getString("msg") + "");
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


    public void sendqingli(String day) {
//        if (true) {
//            ToastUtil.show(GroupInfoActivity.this,"day = " + day);
//            return;
//        }
        Map<String, String> map = new HashMap<>();
        map.put("gid", groupId);
        map.put("day",day);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.shotOffmore, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                PrintLogUtils.log(response, "--");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                default:
                                    ToastUtil.show(GroupInfoActivity.this, object.getString("msg") + "");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


    private void geteditGroupCardName() {

        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(GroupInfoActivity.this).builder()
                .setTitle("设置群名片")
                .setEditText("");
        myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = myAlertInputDialog.getResult().length();
                if (length > 10) {
                    ToastUtil.show(GroupInfoActivity.this, "群名片限十字以内！");
                } else {
                    //getaddfriendgroup(myAlertInputDialog.getResult());
                    geteditGroupCardName(myAlertInputDialog.getResult());
                    myAlertInputDialog.dismiss();
                }
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAlertInputDialog.dismiss();
            }
        });
        myAlertInputDialog.show();
    }

    private void geteditGroupCardName(final String cardname) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", groupId);
        map.put("uid", MyApp.uid);
        map.put("cardname", cardname);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.editGroupCardName, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("groupintroissucc", "onSuccess: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    group_cardname.setText(cardname);
                                }
                            });

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

    @Override
    public void onError(int code, String desc) {

    }

    @Override
    public void onSuccess() {

    }

    public void changeGroupCheckState(final boolean isOpen) {
        HttpHelper.getInstance().setGroupAutoCheck(groupId, isOpen ? "1" : "0", new HttpListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(String msg) {
                if (SafeCheckUtil.isActivityFinish(GroupInfoActivity.this)) {
                    return;
                }
                ToastUtil.show(GroupInfoActivity.this, msg);
                cbPassRequest.setChecked(!cbPassRequest.isChecked());
            }
        });
    }

    public void muteGroup(final boolean isBan) {
        if (true) {
            return;
        }
        HttpHelper.getInstance().banGroup(groupId, isBan ? 1 : 0, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(GroupInfoActivity.this)) {
                    return;
                }
                ToastUtil.show(GroupInfoActivity.this, isBan ? "开启禁言" : "取消禁言");
            }

            @Override
            public void onFail(String msg) {
                if (SafeCheckUtil.isActivityFinish(GroupInfoActivity.this)) {
                    return;
                }
                ToastUtil.show(GroupInfoActivity.this, msg);
                mGroupInfoCnMuteAll.setChecked(!mGroupInfoCnMuteAll.isChecked());
            }
        });
    }



}
