package com.aiwujie.shengmo.timlive.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.kt.bean.NormalMenuItem;
import com.aiwujie.shengmo.kt.ui.view.LiveKickOutUserPop;
import com.aiwujie.shengmo.kt.ui.view.LiveManageUserPop;
import com.aiwujie.shengmo.kt.ui.view.LiveMuteUserPop;
import com.aiwujie.shengmo.kt.ui.view.NormalMenuPopup;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.timlive.bean.ChatRoom;
import com.aiwujie.shengmo.timlive.bean.ManagerList;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.bigkoo.alertview.AlertView;
import com.bumptech.glide.Glide;
import com.tencent.liteav.audiosettingkit.CircleImageView;
import com.tencent.qcloud.tim.tuikit.live.bean.TimCustomMessage;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.CardInfo;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import razerdp.basepopup.BasePopupWindow;

import static com.tencent.liteav.demo.beauty.utils.ResourceUtils.getString;

import com.bigkoo.alertview.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * ??????????????????
 */
public class LiveRoomHeadPop extends BasePopupWindow implements View.OnClickListener {
    //??????????????????????????????tag??????
    public static final String TAG_ANCHOR = "anchor";   //??????
    public static final String TAG_AUDIENCE = "audience";   //??????
    public static final String TAG_ADMIN = "admin";     //???V
    public static final String TAG_MANAGER = "manager";     //??????
    private TimCustomMessage tb;
    private TextView tvAnchorName, tvAge, tvRole, tvWealth, tvCharm, tvReport, tvIsSpeaking, tvAnchorLoc, tvAnchorMyself, tvAnchorAnte, tvAnchorFollow, tvAnchorManager;
    private View viewLineone, viewLineTwo, tvAnchorManagerLine;
    private LinearLayout llLayoutInfoWealth, llLayoutInfoCharm, llLayoutInfoSexAndAge, llBindIcCardInfo; //?????????????????????
    private RadioButton mRBSetControl, mRBCurrentNoTalking, mRBForeverTalking; //????????????/????????????/????????????
    private CircleImageView roomCover;
    private ImageView ivItemIdentity;
    private ImageView ivSex;
    private Context context;
    private String tag = ""; //????????????
    private String mySelfId;

    public OnPopOperateListener onPopOperateListener;

    public void setOnPopOperateListener(OnPopOperateListener onPopOperateListener) {
        this.onPopOperateListener = onPopOperateListener;
    }

    /*********************?????????1????????? 2???????????? 3???????????? 4?????????****************************/
    private int followStatus = 2; //????????????????????????????????????
    private ManagerType managerType = ManagerType.CONTROLTRUE;
    private NoSpeakType noSpeakType = NoSpeakType.MESSAGE_NORMAL_CHAT;

    private enum NoSpeakType {//?????????/?????????
        MESSAGE_BAN_CHAT, MESSAGE_NORMAL_CHAT
    }

    private enum ManagerType {//????????????/????????????
        CONTROLTRUE, CONTROLFALSE;
    }

    CardInfo.DataBean cardInfo;


    /**
     * ???????????????
     *
     * @param context
     * @param tb
     * @param tag
     */
    public LiveRoomHeadPop(Context context, TimCustomMessage tb, String tag) {
        super(context);
        this.tb = tb;
        this.context = context;
        this.tag = tag;
        this.mySelfId = tb.getAnchor_uid();
        initView();
        if (tb == null) return;
        getCardInfo();
        if (tag.equals(TAG_AUDIENCE)) {
            if ("1".equals(tb.is_admin)) {
                this.tag = TAG_ADMIN;
            } else if ("1".equals(tb.is_group_admin)) {
                this.tag = TAG_MANAGER;
            }
        }

    }

    //?????????????????????
    private void getCardInfo() {
        LiveHttpHelper.getInstance().getCardInfo(tb.uid, tb.anchor_uid, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                LogUtil.d(data);
                Activity activity = (Activity)context;
                if (SafeCheckUtil.isActivityFinish(activity)) {
                    return;
                }
                CardInfo cardInfoBean = GsonUtil.GsonToBean(data, CardInfo.class);
                cardInfo = cardInfoBean.getData();
                setCardInfo(cardInfoBean);
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d(msg);
            }
        });
    }

    //????????????????????????
    private void initView() {
        tvAnchorName = findViewById(R.id.tv_anchor_name);
        tvAge = findViewById(R.id.tv_layout_user_normal_info_age);
        tvRole = findViewById(R.id.tv_layout_user_normal_info_role);
        ivSex = findViewById(R.id.iv_layout_user_normal_info_sex);
        tvWealth = findViewById(R.id.tv_layout_user_normal_info_wealth);
        tvCharm = findViewById(R.id.tv_layout_user_normal_info_charm);

        tvReport = findViewById(R.id.report_user);
        tvIsSpeaking = findViewById(R.id.is_speaking);
        tvAnchorLoc = findViewById(R.id.tv_anchor_loc);
        tvAnchorMyself = findViewById(R.id.tv_anchor_myself);
        tvAnchorAnte = findViewById(R.id.tv_anchor_ante);
        tvAnchorFollow = findViewById(R.id.tv_anchor_follow);
        roomCover = findViewById(R.id.img_live_room_cover);
        viewLineone = findViewById(R.id.view_line_one);
        viewLineTwo = findViewById(R.id.view_line_two);
        tvAnchorManager = findViewById(R.id.tv_anchor_manager);
        tvAnchorManagerLine = findViewById(R.id.view_line_three);

        ivItemIdentity = findViewById(R.id.item_identity_icon);

        llBindIcCardInfo = findViewById(R.id.ll_bind_ic_card_info);
        llLayoutInfoSexAndAge = findViewById(R.id.ll_layout_user_normal_info_sex_age);
        llLayoutInfoWealth = findViewById(R.id.ll_layout_user_normal_info_wealth);
        llLayoutInfoCharm = findViewById(R.id.ll_layout_user_normal_info_charm);
        tvAnchorMyself.setOnClickListener(this);
        tvAnchorFollow.setOnClickListener(this);
    }

    //??????????????????
    private void setCardInfo(final CardInfo cardInfo) {
        if (cardInfo == null) return;
        //?????????????????????
        followStatus = cardInfo.getData().getFollow_state();
        initFollowStatus(followStatus);
        //??????????????????????????????
        // initReportOrTalking(cardInfo.getData().getUid(),tb.getIs_group_admin(),cardInfo.getData().getSetNotalking());
        initReportOrTalking(cardInfo.getData());
        //?????????????????????
        initCenterText(tag, cardInfo, tvIsSpeaking);
        //??????????????????
        initSex(cardInfo.getData().getSex());
        //?????????????????????
        initAnchorInfo(cardInfo.getData());
    }

    private void initFollowStatus(int followStatus) {
        //?????????Number  ????????????  ????????????????????? 1????????? 2????????? 3???????????? 4?????????
        if (followStatus == 1 || followStatus == 3) { //????????????
            tvAnchorFollow.setText(getString(R.string.live_has_followed));
        } else if (followStatus == 2 || followStatus == 4) {//?????????
            tvAnchorFollow.setText(getString(R.string.live_follow));
        }
    }

    /**
     * ???????????????????????????
     *
     * @param data
     */
    private void initAnchorInfo(CardInfo.DataBean data) {
        tvAnchorName.setText(!TextUtil.isEmpty(data.getNickname()) ? data.getNickname() : data.getUid());
        Glide.with(context).load(data.getHead_pic()).into(roomCover);
        ivItemIdentity.setVisibility(View.VISIBLE);
        if ("1".equals(data.getIs_admin())) {
            ivItemIdentity.setImageResource(R.drawable.user_manager);
        } else if ("1".equals(data.getSvipannual())) {
            ivItemIdentity.setImageResource(R.drawable.user_svip_year);
        } else if ("1".equals(data.getSvip())) {
            ivItemIdentity.setImageResource(R.drawable.user_svip);
        } else if ("1".equals(data.getVipannual())) {
            ivItemIdentity.setImageResource(R.drawable.user_vip_year);
        } else if ("1".equals(data.getVip())) {
            ivItemIdentity.setImageResource(R.drawable.user_vip);
        } else {
            ivItemIdentity.setImageResource(R.drawable.user_normal);
        }

        roomCover.setOnClickListener(this);
        // tvAnchorLoc.setText(MyApp.province + "\t" + MyApp.city);
        tvAnchorLoc.setText(data.getCity());

        if (!TextUtil.isEmpty(data.getAge())) {
            tvAge.setText(data.getAge());
        } else {
            llLayoutInfoSexAndAge.setVisibility(View.GONE);
        }

        if (!TextUtil.isEmpty(data.getRole())) {
            //tvRole.setText(data.getRole());
            if ("S".equals(data.getRole())) {
                tvRole.setText("???");
                tvRole.setBackgroundResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.bg_user_info_sex_boy);
            } else if ("M".equals(data.getRole())) {
                tvRole.setText("???");
                tvRole.setBackgroundResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.bg_user_info_sex_girl);
            } else if ("SM".equals(data.getRole())) {
                tvRole.setText("???");
                tvRole.setBackgroundResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.bg_user_info_sex_cdts);
            } else {
                tvRole.setBackgroundResource(com.tencent.qcloud.tim.tuikit.live.R.drawable.bg_user_info_sex_other);
                tvRole.setText(data.getRole());
            }

        } else {
            tvRole.setVisibility(View.GONE);
        }

        if (!TextUtil.isEmpty(data.getWealth_val_new()) && !"0".equals(data.getWealth_val_new())) {
            tvWealth.setText(data.getWealth_val_new());
        } else {
            llLayoutInfoWealth.setVisibility(View.GONE);
        }

        if (!TextUtil.isEmpty(data.getCharm_val_new()) && !"0".equals(data.getCharm_val_new())) {
            tvCharm.setText(data.getCharm_val_new());
        } else {
            llLayoutInfoCharm.setVisibility(View.GONE);
        }
        displayView(data);
    }

    //??????view??????
    private void displayView(CardInfo.DataBean data) {
        //???????????????????????????'????????????' ??????
        if (data.getUid().equals(MyApp.uid)) {
            //tvAnchorAnte.setVisibility(View.GONE);
            //viewLineone.setVisibility(View.GONE);
            //viewLineTwo.setVisibility(View.GONE);
            //tvAnchorFollow.setVisibility(View.GONE);
            llBindIcCardInfo.setVisibility(View.GONE);
        }
    }

    /**
     * ???????????????????????????
     *
     * @param
     */
    private void initReportOrTalking(final CardInfo.DataBean cardInfo) {
        tvReport.setOnClickListener(new View.OnClickListener() {
            String reportId;

            @Override
            public void onClick(View v) {
                if (onPopOperateListener != null) {
                    reportId = !TextUtil.isEmpty(cardInfo.getUid()) ? cardInfo.getUid() : "";
                    onPopOperateListener.onReport(reportId);
                }
            }
        });


        if (cardInfo.getUid().equals(MyApp.uid)) { //??????????????????
            watchSelf();
            tvReport.setVisibility(View.GONE);
            tvIsSpeaking.setVisibility(View.GONE);
        } else if ("3".equals(cardInfo.getGroup_role())) { //??????????????????
            watchAnchor();
            if (tag.equals(TAG_ADMIN)) {
                tvIsSpeaking.setVisibility(View.VISIBLE);
                tvReport.setVisibility(View.VISIBLE);
                tvIsSpeaking.setText("????????????");
                tvIsSpeaking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.SHOW_ANCHOR_MANAGER_CARD, "", tb.uid));
                    }
                });
            } else {
                tvIsSpeaking.setVisibility(View.GONE);
                tvReport.setVisibility(View.VISIBLE);
            }

        } else {
            if ("1".equals(tb.getIs_group_admin())) {  //???????????????
                managerWatchElse();
//                tvIsSpeaking.setVisibility(View.VISIBLE);
//                tvReport.setVisibility(View.VISIBLE);
//                tvIsSpeaking.setOnClickListener(this);
//                if (cardInfo.getSetNotalking().equals("0")) { //?????????
//                    tvIsSpeaking.setText(context.getString(R.string.local_no_talking));
//                } else {//????????????/????????????
//                    tvIsSpeaking.setText(context.getString(R.string.unbind_no_talking));
//                }
            } else { //??????????????????????????????
                if (mySelfId.equals(MyApp.uid)) {
                    if ("1".equals(cardInfo.getIs_online())) {
                        tvIsSpeaking.setVisibility(View.VISIBLE);
                        tvReport.setVisibility(View.VISIBLE);
                        tvIsSpeaking.setText("????????????");
                        tvIsSpeaking.setBackgroundResource(R.drawable.live_top_coins);
                        tvIsSpeaking.setTextColor(context.getResources().getColor(R.color.white));
                        tvIsSpeaking.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //????????????
                                EventBus.getDefault().post(new LiveMethodEvent(LiveEventConstant.link_LIVE_USER, "", tb.uid));
                            }
                        });
                    } else {
                        tvIsSpeaking.setVisibility(View.GONE);
                        tvReport.setVisibility(View.VISIBLE);
                    }
                } else {
                    watchNormal();
                    tvIsSpeaking.setVisibility(View.GONE);
                    tvReport.setVisibility(View.VISIBLE);
                }
            }
        }


//        if(!cardInfo.getUid().equals(MyApp.uid)){  //????????????
//            tvReport.setGravity(Gravity.RIGHT);
//            if (cardInfo.getGroup_role().equals("2")) { //???????????????
//
//
//            } else {
//                if(tb.getIs_group_admin().equals("1")){ //??????????????????
//
//                } else{
//                    tvIsSpeaking.setVisibility(View.GONE);
//                    tvReport.setVisibility(View.VISIBLE);
//                    tvReport.setOnClickListener(new View.OnClickListener() {
//                        String reportId;
//                        @Override
//                        public void onClick(View v) {
//                            if(onPopOperateListener != null){
//                                reportId = !TextUtil.isEmpty(cardInfo.getUid()) ? cardInfo.getUid() : "";
//                                onPopOperateListener.onReport(reportId);
//                            }
//                        }
//                    });
//                }
//            }
//        }else{
//            tvReport.setVisibility(View.GONE);
//        }
    }


    /**
     * ???????????????????????????
     *
     * @param uid
     */
    private void initReportOrTalking(final String uid, final String isAdamin, final String isNoTalking) {
        if (!uid.equals(MyApp.uid)) {
            tvReport.setGravity(Gravity.RIGHT);
            if (isAdamin.equals("1")) { //????????????
                tvIsSpeaking.setVisibility(View.VISIBLE);
                tvReport.setVisibility(View.VISIBLE);
                tvIsSpeaking.setOnClickListener(this);
                if (isNoTalking.equals("0")) { //?????????
                    tvIsSpeaking.setText(context.getString(R.string.local_no_talking));
                } else {//????????????/????????????
                    tvIsSpeaking.setText(context.getString(R.string.unbind_no_talking));
                }
            } else {
                tvIsSpeaking.setVisibility(View.GONE);
                tvReport.setVisibility(View.VISIBLE);
                tvReport.setOnClickListener(new View.OnClickListener() {
                    String reportId;

                    @Override
                    public void onClick(View v) {
                        if (onPopOperateListener != null) {
                            reportId = !TextUtil.isEmpty(uid) ? uid : "";
                            onPopOperateListener.onReport(reportId);
                        }
                    }
                });
            }
        } else {
            tvReport.setVisibility(View.GONE);
        }
    }

    /**
     * ?????????????????????
     *
     * @param sexType
     */
    private void initSex(String sexType) {
        if (!TextUtil.isEmpty(sexType)) {
            if (sexType.equals("1")) {
                ivSex.setBackgroundResource(R.drawable.nan);
                llLayoutInfoSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_boy);
            } else if ("2".equals(sexType)) {
                ivSex.setBackgroundResource(R.drawable.nv);
                llLayoutInfoSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_girl);
            } else if ("3".equals(sexType)) {
                ivSex.setBackgroundResource(R.drawable.san);
                llLayoutInfoSexAndAge.setBackgroundResource(R.drawable.bg_user_info_sex_cdts);
            }
        } else {
            ivSex.setVisibility(View.GONE);
        }
    }

    /**
     * ??????????????????@?????????
     *
     * @param action
     */
    private void initCenterText(String action, final CardInfo cardInfo, final TextView tvIsSpeaking) {
        tvAnchorAnte.setText(context.getResources().getString(R.string.tv_anchor_ante));
        tvAnchorAnte.setOnClickListener(this);
        //???????????????????????????/????????????@???
        if (action.equals(TAG_ANCHOR) || action.equals(TAG_ADMIN) || action.equals(TAG_MANAGER)) {//anchor
            if (cardInfo.getData().getUid().equals(tb.anchor_uid)) { //??????????????? ?????????
                tvAnchorManager.setVisibility(View.GONE);
                tvAnchorManagerLine.setVisibility(View.GONE);
            } else { //????????????
                tvAnchorManager.setVisibility(View.VISIBLE);
                tvAnchorManagerLine.setVisibility(View.VISIBLE);
                ManagerPop managerPop = initPop(cardInfo, tvIsSpeaking);
                managerPop.setOnStartLoadListener(new ManagerPop.OnStartLoadListener() {
                    @Override
                    public void onStartLoad(View mMenuView) {
                        mRBSetControl = mMenuView.findViewById(R.id.rb_set_control);
                        mRBCurrentNoTalking = mMenuView.findViewById(R.id.rb_current_no_speak);
                        mRBForeverTalking = mMenuView.findViewById(R.id.rb_no_speak_forever);
                        setManagerType(cardInfo, mRBSetControl);
                        setNoTalking(cardInfo, mRBCurrentNoTalking, mRBForeverTalking);
                    }
                });
                tvAnchorManager.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        showManageView();
                    }
                });
            }

        } else { //audience
            tvAnchorManager.setVisibility(View.GONE);
            tvAnchorManagerLine.setVisibility(View.GONE);
        }
    }

    //??????????????????
    private void onMenuListStatus() {
        if (noSpeakType == NoSpeakType.MESSAGE_NORMAL_CHAT) { //??????????????????
            mRBCurrentNoTalking.setText(context.getString(R.string.local_no_talking));
            mRBForeverTalking.setVisibility(View.VISIBLE);
        } else if (noSpeakType == NoSpeakType.MESSAGE_BAN_CHAT) {//????????????/??????????????????
            mRBCurrentNoTalking.setText(context.getString(R.string.unbind_no_talking));
            mRBForeverTalking.setVisibility(View.GONE);
        }

        if (managerType == ManagerType.CONTROLTRUE) {
            mRBSetControl.setText(context.getString(R.string.bind_manager));
        } else {
            mRBSetControl.setText(context.getString(R.string.unbind_manager));
        }
    }

    /**
     * ??????????????????
     *
     * @return
     */
    private ManagerPop initPop(final CardInfo cardInfo, TextView tvIsSpeaking) {
        LiveRoomHeadPop.this.setBackgroundColor(R.color.recycler_swipe_color_text_gray);
        final ManagerPop managerPop = new ManagerPop(context, cardInfo, tvIsSpeaking);
        //??????PopupWindow??????????????????????????????
        managerPop.setWidth(UIUtil.getScreenWidth(context) - 20);
        managerPop.setHeight(WRAP_CONTENT);
        managerPop.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));
        managerPop.setOutsideTouchable(true);
        return managerPop;
    }

    /**
     * ????????????????????????
     *
     * @param cardInfo
     * @param rb1
     */
    public void setManagerType(CardInfo cardInfo, RadioButton rb1) {
        if (cardInfo.getData().getIs_admin().equals("2")) { //????????????
            rb1.setText(context.getString(R.string.bind_manager));
            //tvIsSpeaking.setVisibility(View.VISIBLE);
            managerType = ManagerType.CONTROLFALSE;
        } else {
            rb1.setText(context.getString(R.string.unbind_manager));
            //tvIsSpeaking.setVisibility(View.GONE);
            managerType = ManagerType.CONTROLTRUE;
        }
    }

    /**
     * ??????????????????
     *
     * @param cardInfo
     * @param rb3
     * @param rb4
     */
    public void setNoTalking(CardInfo cardInfo, RadioButton rb3, RadioButton rb4) {
        if (cardInfo.getData().getSetNotalking().equals("0")) { //?????????
            rb3.setText(context.getString(R.string.local_no_talking));
            rb4.setVisibility(View.VISIBLE);
            noSpeakType = NoSpeakType.MESSAGE_NORMAL_CHAT;
        } else {//????????????/????????????
            rb4.setText(context.getString(R.string.unbind_no_talking));
            rb4.setVisibility(View.GONE);
            noSpeakType = NoSpeakType.MESSAGE_BAN_CHAT;
        }
    }

    /**
     * ????????????????????????
     *
     * @param managerPop
     * @param radioGroup
     * @param checkedId  1??????????????? 2???????????? 3????????????
     *                   1???????????? 2????????????
     */
    private void onCheckeded(ManagerPop managerPop, RadioGroup radioGroup, int checkedId, String uid) {
        if (TextUtils.isEmpty(uid)) return;
        RadioButton rb = managerPop.getmMenuView().findViewById(radioGroup.getCheckedRadioButtonId());
        switch (checkedId) {
            case R.id.rb_set_control:
                if (managerType == ManagerType.CONTROLTRUE) { //????????????
                    setManager("1", uid, rb);
                } else {//????????????
                    setManager("2", uid, rb);
                }
                break;
            case R.id.rb_control_list:
                managerList(rb);
                break;
            case R.id.rb_current_no_speak:
                if (noSpeakType == NoSpeakType.MESSAGE_NORMAL_CHAT) {//???????????????
                    noSpeakList("1", uid, mySelfId, null);
                } else {
                    noSpeakList("3", uid, mySelfId, null);//??????????????????
                }
                break;
            case R.id.rb_no_speak_forever:
                if (noSpeakType == NoSpeakType.MESSAGE_NORMAL_CHAT) {//???????????????
                    noSpeakList("2", uid, mySelfId, null);
                } else {
                    noSpeakList("3", uid, mySelfId, null);//??????????????????
                }
                break;
        }
    }

    //????????????
    private void noSpeakList(String type, String touid, String anchor_uid, final TextView tvNoSpeaking) {
        LiveHttpHelper.getInstance().setNoTalking(type, touid, anchor_uid, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                LogUtil.d(data);
                try {
                    JSONObject obj = new JSONObject(data);
                    ToastUtil.show(context, obj.getString("msg"));

                    if (tvNoSpeaking != null) {
                        String tag = tvIsSpeaking.getText().toString().trim();
                        if (tag.equals(getString(R.string.local_no_talking))) {
                            tvIsSpeaking.setText(getString(R.string.forever_no_talking));
                        } else if (tag.equals(getString(R.string.unbind_no_talking))) {
                            tvIsSpeaking.setText(getString(R.string.local_no_talking));
                        }
                    }

                    if (noSpeakType == NoSpeakType.MESSAGE_BAN_CHAT) { //??????????????????
                        mRBCurrentNoTalking.setText(context.getString(R.string.local_no_talking));
                        mRBForeverTalking.setVisibility(View.VISIBLE);
                        noSpeakType = NoSpeakType.MESSAGE_NORMAL_CHAT;
                    } else {//????????????/??????????????????
                        mRBCurrentNoTalking.setText(context.getString(R.string.forever_no_talking));
                        mRBForeverTalking.setVisibility(View.GONE);
                        noSpeakType = NoSpeakType.MESSAGE_BAN_CHAT;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String msg) {
                if (getContext() == null) return;
                ToastUtil.show(getContext(), msg);
                LogUtil.d("onFail: " + msg);
            }
        });
    }

    //????????????
    private void managerList(final RadioButton rb) {
        LiveHttpHelper.getInstance().managerList("", new HttpListener() {
            @Override
            public void onSuccess(String data) {
                LogUtil.d(data);
                ManagerList managerList = GsonUtil.GsonToBean(data, ManagerList.class);
                if (managerList.getRetcode() == 2000 && managerList.getData().size() > 0) {
                    LiveRoomHeadPop.this.setBackgroundColor(R.color.recycler_swipe_color_text_gray);
                    int offY = 245;
                    final ManagerListPop managerListPop = new ManagerListPop(context, managerList);
                    managerListPop.setOnControlListener(new ManagerListPop.onControlListener() {
                        @Override
                        public void onControlSuccess(ManagerList.DataBean control) {
                            if (rb == null) return;
                            managerType = ManagerType.CONTROLTRUE;
                            rb.setText(context.getString(R.string.bind_manager));
                        }

                        @Override
                        public void onControlFailed(ManagerList.DataBean control) {
                            if (rb == null) return;
                            managerType = ManagerType.CONTROLFALSE;
                            rb.setText(context.getString(R.string.unbind_manager));
                        }
                    });
                    //??????PopupWindow??????????????????????????????
                    managerListPop.setWidth(UIUtil.getScreenWidth(context) - 20);
                    managerListPop.setHeight(WRAP_CONTENT);
                    managerListPop.setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));
                    if (!UIUtil.hasNavBar(context)) {
                        offY = offY - UIUtil.getNavigationBarHeight(context);
                    }
                    managerListPop.showAtLocation(managerListPop.getmMenuView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, offY);
                    managerListPop.setOutsideTouchable(true);
                } else {
                    ToastUtil.show(context, "??????????????????");
                }
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d(msg);
            }
        });
    }

    //??????/????????????
    public void setManager(final String type, String mySelfId, final RadioButton rb) {
        LiveHttpHelper.getInstance().setChatRoomAdmin(type, mySelfId, tb.anchor_uid, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                LogUtil.d(data);
                ChatRoom chatRoom = GsonUtil.GsonToBean(data, ChatRoom.class);
                ToastUtil.show(context, chatRoom.getMsg());
                setStatus(rb, managerType);
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d("onFail: " + msg);
                ToastUtil.show(context, msg);
                setStatus(rb, managerType);
            }
        });
    }

    //??????????????????
    private void setStatus(RadioButton rb, ManagerType type) {
        if (rb == null) return;
        if (type == ManagerType.CONTROLTRUE) {//????????????
            managerType = ManagerType.CONTROLFALSE;
            rb.setText("????????????");
        } else {//2-????????????
            managerType = ManagerType.CONTROLTRUE;
            rb.setText("????????????");
        }
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.live_room_click_anchor_dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_anchor_myself:
            case R.id.img_live_room_cover:
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("uid", tb.getUid());
                context.startActivity(intent);
                break;
            case R.id.tv_anchor_ante:
                if (onPopOperateListener != null) {
                    onPopOperateListener.onAt(cardInfo.getUid(), cardInfo.getNickname());
                }
                break;
            case R.id.tv_anchor_follow:
                if (onPopOperateListener != null) {
                    onPopOperateListener.onFollow(tvAnchorFollow, followStatus, tb.getUid());
                }
                break;
            case R.id.is_speaking:
                String tag = tvIsSpeaking.getText().toString().trim();
                if (tag.equals(getString(R.string.local_no_talking))) {
                    noSpeakList("1", tb.getUid(), mySelfId, tvIsSpeaking);//????????????
                } else if (tag.equals(getString(R.string.unbind_no_talking))) {
                    noSpeakList("3", tb.getUid(), mySelfId, tvIsSpeaking);//????????????
                }
                break;
        }
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0f, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0f, 1f, 300);
    }

    public interface OnPopOperateListener {
        void onFollow(TextView tvAnchorFollow, int follow_state, String uid);

        void onAt(String uid, String name);

        void onReport(String mySelfId);
    }

    void watchAnchor() { //??????????????????

    }

    void watchSelf() { //??????????????????

    }

    void watchNormal() { //???????????????

    }

    void managerWatchElse() { //???????????????

    }

    OnItemClickListener alertItemListener;
    OnSimpleItemListener onSimpleItemListener;

    void initAlertListener() {
//        alertItemListener = new OnItemClickListener() {
//            @Override
//            public void onItemClick(Object o, int position, String data) {
//                switch (data) {
//                    case "????????????":
//                        setLiveManager(true);
//                        break;
//                    case "????????????":
//                        setLiveManager(false);
//                        break;
//                    case "????????????":
//                        setMute(1);
//                        break;
//                    case "????????????":
//                        setMute(2);
//                        break;
//                    case "????????????":
//                        setMute(3);
//                        break;
//                    case "????????????":
//                        break;
//                }
//            }

//        };
        onSimpleItemListener = new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                switch (normalMenuItemList.get(position).getContent()) {
                    case "????????????":
                        setLiveManager(true);
                        break;
                    case "????????????":
                        setLiveManager(false);
                        break;
                    case "????????????":
                        setMute(1);
                        break;
                    case "????????????":
                        setMute(2);
                        break;
                    case "????????????":
                        setMute(3);
                        break;
                    case "????????????":

                        break;
                    case "????????????":
                        break;
                }
            }
        };
    }

    AlertView alertView;
    NormalMenuPopup normalMenuPopup;
    List<NormalMenuItem> normalMenuItemList;

    void showManageView() {

//        if (alertItemListener == null) {
//            initAlertListener();
//        }

        boolean isGroupAdmin = "2".equals(cardInfo.getGroup_role());
        boolean isMute = "1".equals(cardInfo.getIs_mute());

        String groupStr = "";
        String groupListStr = "????????????";
        if (isGroupAdmin) {
            groupStr = "????????????";
        } else {
            groupStr = "????????????";
        }

//        if (isMute) {
//            alertView = new AlertView(null, null, "??????", null,
//                    new String[]{groupStr, groupListStr, "????????????"},
//                    context, AlertView.Style.ActionSheet, alertItemListener);
//        } else {
//            alertView = new AlertView(null, null, "??????", null,
//                    new String[]{groupStr, groupListStr, "????????????", "????????????"},
//                    context, AlertView.Style.ActionSheet, alertItemListener);
//        }
//        alertView.show();
        normalMenuItemList = new ArrayList<>();
        if (MyApp.isAdmin.equals("1") || MyApp.uid.equals(tb.anchor_uid)) { //???v???????????????????????????
            normalMenuItemList.add(new NormalMenuItem(0, groupStr));
            normalMenuItemList.add(new NormalMenuItem(0, groupListStr));
        }
        if (isMute) {
            normalMenuItemList.add(new NormalMenuItem(0, "????????????"));
        } else {
            normalMenuItemList.add(new NormalMenuItem(0, "???????????????"));
//            normalMenuItemList.add(new NormalMenuItem(0, "????????????"));
//            normalMenuItemList.add(new NormalMenuItem(0, "????????????"));
        }

        normalMenuItemList.add(new NormalMenuItem(0, "????????????"));

        if ("1".equals(cardInfo.getIs_kick())) {
            normalMenuItemList.add(new NormalMenuItem(0, "????????????"));
        } else {
            normalMenuItemList.add(new NormalMenuItem(0, "???????????????"));
        }
        normalMenuItemList.add(new NormalMenuItem(0, "????????????"));

        normalMenuPopup = new NormalMenuPopup(context, normalMenuItemList);
        normalMenuPopup.showPopupWindow();
        normalMenuPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                switch (normalMenuItemList.get(position).getContent()) {
                    case "????????????":
                        setLiveManager(true);
                        break;
                    case "????????????":
                        setLiveManager(false);
                        break;
                    case "????????????":
                        setMute(1);
                        break;
                    case "????????????":
                        setMute(2);
                        break;
                    case "????????????":
                       // setMute(3);
                        muteUser(0);
                        break;
                    case "????????????":
                        showManagePop();
                        break;
                    case "????????????":
                        showMutePop();
                        break;
                    case "???????????????":
                        showSetTimePop(2);
                        break;
                    case "????????????":
                        resetKickOut();
                        break;
                    case "????????????":
                        showKickOutListPop();
                        break;
                    case "???????????????":
                        showSetTimePop(1);
                        break;
                }
            }
        });
    }

    void setLiveManager(final boolean setManager) {
        LiveHttpHelper.getInstance().setChatRoomAdmin(setManager ? "1" : "2", tb.uid, tb.anchor_uid, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (setManager) {
                    ToastUtil.show(context, "????????????");
                    cardInfo.setGroup_role("2");
                } else {
                    ToastUtil.show(context, "????????????");
                    cardInfo.setGroup_role("1");
                }
                //alertView.dismiss();
                normalMenuPopup.dismiss();
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(context, msg);
            }
        });
    }

    void setMute(final int muteState) {
        LiveHttpHelper.getInstance().setNoTalking(String.valueOf(muteState), tb.uid, tb.anchor_uid, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                switch (muteState) {
                    case 1: //????????????
                        ToastUtil.show(context, "????????????");
                        cardInfo.setSetNotalking("1");
                        break;
                    case 2: //????????????
                        ToastUtil.show(context, "????????????");
                        cardInfo.setSetNotalking("1");
                        break;
                    case 3: //????????????
                        ToastUtil.show(context, "????????????");
                        cardInfo.setSetNotalking("0");
                        break;
                }
                // alertView.dismiss();
                normalMenuPopup.dismiss();
            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(context, msg);
            }
        });
    }

    void showManagePop() {
        normalMenuPopup.dismiss();
        LiveManageUserPop manageUserPop = new LiveManageUserPop(context, MyApp.uid, tb.anchor_uid);
        manageUserPop.showPopupWindow();
        manageUserPop.setOnLiveUserListener(new LiveManageUserPop.OnLiveUserListener() {
            @Override
            public void onLiveUserRefresh() {
                refreshData();
            }

            @Override
            public void onLiveUserClick(@NotNull TimCustomMessage tcm) {

            }
        });
    }

    void showMutePop() {
        normalMenuPopup.dismiss();
        LiveMuteUserPop muteUserPop = new LiveMuteUserPop(context, tb.anchor_uid);
        muteUserPop.showPopupWindow();
        muteUserPop.setMuteListener(new LiveMuteUserPop.OnLiveMuteListener() {
            @Override
            public void doCancelMute(@NotNull String uid) {
                if (uid.equals(tb.uid)) {
                    refreshData();
                }
            }
        });
    }

    public void refreshData() {
        getCardInfo();
    }

    void showKickOutPop() {
        List<NormalMenuItem> normalMenuItemList = new ArrayList<>();
        normalMenuItemList.add(new NormalMenuItem(0, "??????"));
        normalMenuItemList.add(new NormalMenuItem(0, "??????"));
        normalMenuItemList.add(new NormalMenuItem(0, "??????"));
        normalMenuItemList.add(new NormalMenuItem(0, "??????"));
        normalMenuItemList.add(new NormalMenuItem(0, "??????"));
        normalMenuItemList.add(new NormalMenuItem(0, "??????"));
        NormalMenuPopup kickOutPop = new NormalMenuPopup(context, normalMenuItemList);
        kickOutPop.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                switch (position) {
                    case 0:
                        kickOutUser(1);
                        break;
                    case 1:
                        kickOutUser(3);
                        break;
                    case 2:
                        kickOutUser(7);
                        break;
                    case 3:
                        kickOutUser(14);
                        break;
                    case 4:
                        kickOutUser(30);
                        break;
                    case 5:
                        kickOutUser(99);
                        break;
                }
                kickOutPop.dismiss();
            }
        });
        kickOutPop.showPopupWindow();
    }

    void showSetTimePop(int type) {
        List<NormalMenuItem> normalMenuItemList = new ArrayList<>();
        normalMenuItemList.add(new NormalMenuItem(0, "??????"));
        normalMenuItemList.add(new NormalMenuItem(0, "??????"));
        normalMenuItemList.add(new NormalMenuItem(0, "??????"));
        normalMenuItemList.add(new NormalMenuItem(0, "??????"));
        normalMenuItemList.add(new NormalMenuItem(0, "??????"));
        normalMenuItemList.add(new NormalMenuItem(0, "??????"));
        normalMenuItemList.add(new NormalMenuItem(0, "??????"));
        NormalMenuPopup kickOutPop = new NormalMenuPopup(context, normalMenuItemList);
        kickOutPop.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                switch (position) {
                    case 0:
                        doLiveOperate(type,-1);
                        break;
                    case 1:
                        doLiveOperate(type,1);
                        break;
                    case 2:
                        doLiveOperate(type,3);
                        break;
                    case 3:
                        doLiveOperate(type,7);
                        break;
                    case 4:
                        doLiveOperate(type,14);
                        break;
                    case 5:
                        doLiveOperate(type,30);
                        break;
                    case 6:
                        doLiveOperate(type,99);
                        break;
                }
                kickOutPop.dismiss();
            }
        });
        kickOutPop.showPopupWindow();
    }

    void doLiveOperate(int type, int time) {
        if (type == 1) {
            muteUser(time);
        } else if (type == 2) {
            kickOutUser(time);
        }
    }

    void kickOutUser(int day) {
        HttpHelper.getInstance().kickUser(tb.uid, tb.anchor_uid, String.valueOf(day), new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                if (day == 0) {
                    ToastUtil.show(context, "????????????");
                    cardInfo.setIs_kick("0");
                } else {
                    ToastUtil.show(context, "????????????");
                    cardInfo.setIs_kick("1");
                }
                normalMenuPopup.dismiss();
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(context, msg);
            }
        });
    }

    void muteUser(int day) {
        HttpHelper.getInstance().muteUser(tb.uid, tb.anchor_uid, String.valueOf(day), new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                if (day == 0) {
                    ToastUtil.show(context, "????????????");
                    cardInfo.setIs_mute("0");
                } else {
                    ToastUtil.show(context, "????????????");
                    cardInfo.setIs_mute("1");
                }
                normalMenuPopup.dismiss();
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(context, msg);
            }
        });
    }

    void showKickOutListPop() {
        LiveKickOutUserPop liveKickOutUserPop = new LiveKickOutUserPop(context, tb.uid, tb.anchor_uid);
        liveKickOutUserPop.showPopupWindow();
    }

    void showMuteListPop() {

    }


    void resetKickOut() {
        kickOutUser(0);
    }


}
