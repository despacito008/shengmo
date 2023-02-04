package com.aiwujie.shengmo.timlive.ui;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.MainActivity;
import com.aiwujie.shengmo.activity.MyPurseActivity;
import com.aiwujie.shengmo.activity.ReportActivity;
import com.aiwujie.shengmo.activity.ranking.LiveRankingActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BeanIcon;
import com.aiwujie.shengmo.bean.LiveAnchorStateBean;
import com.aiwujie.shengmo.kt.bean.NormalShareBean;
import com.aiwujie.shengmo.kt.ui.activity.HomePageActivity;
import com.aiwujie.shengmo.kt.ui.activity.tabtopbar.RechargeGiftActivity;
import com.aiwujie.shengmo.kt.ui.view.LiveBlindBoxRulePop;
import com.aiwujie.shengmo.kt.ui.view.NormalSharePop;
import com.aiwujie.shengmo.kt.ui.view.RechargeBeanPop;
import com.aiwujie.shengmo.kt.ui.view.SignAnchorPop;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.timlive.kt.ui.view.LiveLotteryDrawPop;
import com.aiwujie.shengmo.timlive.view.LiveRedEnvelopesPop;
import com.aiwujie.shengmo.timlive.view.SendLiveRedEnvelopesPop;
import com.aiwujie.shengmo.utils.AliPayMentTaskManager;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveRedEnvelopesBean;
import com.tencent.qcloud.tim.tuikit.live.bean.NormalEventBean;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.customview.SharedPop;
import com.tencent.qcloud.tim.tuikit.live.bean.OtherLiveRoomEvent;
import com.aiwujie.shengmo.eventbus.StopLiveEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.bean.NormalMenuItem;
import com.aiwujie.shengmo.kt.listener.OnSimplePopListener;
import com.aiwujie.shengmo.kt.ui.activity.LiveAdminOperateActivity;
import com.aiwujie.shengmo.kt.ui.activity.MyLiveLevelActivity;
import com.aiwujie.shengmo.kt.ui.view.LiveAuTaskRankPop;
import com.aiwujie.shengmo.kt.ui.view.LiveAudienceTaskPop;
import com.aiwujie.shengmo.kt.ui.view.LiveBuyTicketPop;
import com.aiwujie.shengmo.kt.ui.view.LiveInfoPop;
import com.aiwujie.shengmo.kt.ui.view.LiveLinkMicUserPop;
import com.aiwujie.shengmo.kt.ui.view.LiveOnlineUserPop;
import com.aiwujie.shengmo.kt.ui.view.LivePkTopAudiencePop;
import com.aiwujie.shengmo.kt.ui.view.NormalMenuPopup;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.HttpResult;
import com.aiwujie.shengmo.net.LiveHttpHelper;
import com.aiwujie.shengmo.net.OkHttpRequestManager;
import com.aiwujie.shengmo.tim.bean.LiveAnchorMessageBean;
import com.aiwujie.shengmo.tim.helper.MessageHelper;
import com.aiwujie.shengmo.tim.helper.MessageSendHelper;
import com.aiwujie.shengmo.timlive.helper.LiveHttpRequest;
import com.aiwujie.shengmo.timlive.adapter.LiveSwitchAdapter;
import com.aiwujie.shengmo.timlive.net.RoomManager;
import com.aiwujie.shengmo.timlive.view.LiveAnchorSettingPop;
import com.aiwujie.shengmo.timlive.view.LiveRoomHeadPop;
import com.aiwujie.shengmo.timlive.view.LiveSlideRecyclerView;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.PhotoRemoteUtil;
import com.aiwujie.shengmo.utils.PhotoUploadTask;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.NormalTipsPop;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.liteav.custom.Constents;
import com.tencent.liteav.custom.FloatVideoWindowService2;
import com.tencent.qcloud.tim.tuikit.live.bean.LinkMicStateBean;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveSettingStateBean;
import com.tencent.qcloud.tim.tuikit.live.bean.PkTopAudienceMessageBean;
import com.tencent.qcloud.tim.tuikit.live.bean.ShareAnchorBean;
import com.tencent.qcloud.tim.tuikit.live.bean.TimCustomMessage;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.AppLiveDialogEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.AppLiveGiftChangedEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.ReportLiveMessageBean;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.LiveRoomAudienceFragment;
import com.tencent.qcloud.tim.tuikit.live.utils.PermissionUtils;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.generator.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.EasyPermissions;
import razerdp.basepopup.BasePopupWindow;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;
import static com.aiwujie.shengmo.timlive.view.LiveRoomHeadPop.TAG_AUDIENCE;

/**
 * 观众端上下划动页面
 */
public class LiveRoomSwitchActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private Context mContext;
    private LiveSlideRecyclerView mRvLiveSlide;
    private LiveSwitchAdapter adapter;
    private List<ScenesRoomInfoBean> list = new ArrayList<>();
    private String tag; //页面标签
    private List<ScenesRoomInfoBean> roomInfos;
    private int currentPostion;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_room_audience);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
        } else {
        }
        OkHttpRequestManager.getInstance().setTag(this.getLocalClassName());
        ImmersionBar.with(this).init();
        Intent intent = getIntent();
        tag = intent.getStringExtra(RoomManager.ROOM_CLICK_FROM);
        roomInfos = (List<ScenesRoomInfoBean>) intent.getSerializableExtra(RoomManager.ROOM_INFO);
        currentPostion = intent.getIntExtra(RoomManager.ROOM_ITEM,0);
        initView();
        EventBus.getDefault().register(this);
        mContext = this;
        //LogUtil.d("进入直播间 啦啦啦");
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        if (!getIntent().getBooleanExtra("canRecord",false)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        mRvLiveSlide = (LiveSlideRecyclerView) findViewById(R.id.rvLive);
        if (roomInfos != null) {
            list.clear();
            list.addAll(roomInfos);
        }
        LogUtil.d("list size = " + list.size());
        adapter = new LiveSwitchAdapter(this, list,getSupportFragmentManager(),currentPostion);
        mRvLiveSlide.setAdapter(adapter);
        mRvLiveSlide.scrollToPosition(currentPostion);
        //左滑事件
        mRvLiveSlide.setOnLiveSlideLeftFillingListener(new LiveSlideRecyclerView.OnLiveSlideLeftFillingListener() {
            @Override
            public void onLiveSlideLeftFilling() {
//                if(adapter != null && adapter.getLiveRoomAudienceFragment() != null){
//                    //adapter.getLiveRoomAudienceFragment().exitRoom();
//                    adapter.getLiveRoomAudienceFragment().finishActivity();
//                    adapter.onBackPressed(currentPostion);
//                }
            }
        });
        //点击事件
        mRvLiveSlide.setOnItemClickListener(new LiveSlideRecyclerView.OnItemClickListener() {
            @Override
            public void onClick() {
                if (adapter != null && adapter.getLiveRoomAudienceFragment() != null) {
                    adapter.getLiveRoomAudienceFragment().clickLikeFrequencyControl();
                }
            }
        });

        //用户切换了直播间
        adapter.setOnLiveRoomChangedListener(new LiveSwitchAdapter.OnLiveRoomChangedListener() {
            @Override
            public void onLiveRoomChanged(int position) {
                currentPostion = position;
                if (mTimer == null) {
                    startHeaderReport();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpRequestManager.getInstance().cancelTag(this.getLocalClassName());
        EventBus.getDefault().unregister(this);
        LogUtil.d("退出直播间 啦啦啦");
        stopHeaderReport();
        unregisterReceiver(mHomeKeyEventReceiver);
        if (Constents.isShowAudienceFloatWindow) {
            FloatWindowLayout.getInstance().closeFloatWindow();
        }
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        if(adapter != null){
            adapter.onBackPressed(currentPostion);
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showTip(TimCustomMessage timCustomMessage) {
        showCaveat(timCustomMessage);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showExitDialog(AppLiveDialogEvent appLiveDialogEvent) { showExitDialog(appLiveDialogEvent.getContent()); }

    //设置警告框
    private void showCaveat(TimCustomMessage timRoomMessageBean) {
        final NormalTipsPop normalTipsPop = new NormalTipsPop.Builder(LiveRoomSwitchActivity.this)
                .setTitle("提醒")
                .setInfo(timRoomMessageBean.getContent())
                .setCancelStr("")
                .setConfirmStr("好的")
                .build();
        normalTipsPop.setOutSideTouchable(false);
        normalTipsPop.update();
        normalTipsPop.setOnPopClickListener(new NormalTipsPop.OnPopClickListener() {
            @Override
            public void cancelClick() {
                normalTipsPop.dismiss();
            }

            @Override
            public void confirmClick() {
                normalTipsPop.dismiss();
            }
        });
        normalTipsPop.showPopupWindow();
        if(timRoomMessageBean.costomMassageType.equals("closeAvChatRoom")){
            normalTipsPop.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    finish();
                }
            });
        }
    }

    //设置主播关闭房间
    private void showExitDialog(String content) {
        final NormalTipsPop normalTipsPop = new NormalTipsPop.Builder(LiveRoomSwitchActivity.this)
                .setTitle("直播结束啦！")
                .setInfo(content)
                .setCancelStr("")
                .setConfirmStr("好的")
                .build();
        normalTipsPop.setOutSideTouchable(false);
        normalTipsPop.update();
        normalTipsPop.setOnPopClickListener(new NormalTipsPop.OnPopClickListener() {
            @Override
            public void cancelClick() {
                normalTipsPop.dismiss();
            }

            @Override
            public void confirmClick() {
                normalTipsPop.dismiss();
                closeLiveRoom();
//                if (adapter.getLiveRoomAudienceFragment() != null) {
//                    adapter.getLiveRoomAudienceFragment().exitRoom();
//                    adapter.getLiveRoomAudienceFragment().finishActivity();
//                }
            }
        });
        normalTipsPop.showPopupWindow();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveLiveEvent(LiveMethodEvent event) {
        switch (event.getType()) {
            case "OnGiftPanel":
                //Intent intent = new Intent(LiveRoomSwitchActivity.this, MyPurseActivity.class);
                //startActivity(intent);
                //RechargeGiftActivity.Companion.start(LiveRoomSwitchActivity.this);
                showRechargePop();
                break;
            case "onGiftItemClick":
                String json = event.getData();
                GiftInfo giftInfo = (GiftInfo) GsonUtil.GsonToBean(json,GiftInfo.class);
                sendGift(giftInfo);
                break;
            case LiveEventConstant.CLICK_LIVE_USER:
                final String data = event.getData();
                final TimCustomMessage timRoomMessageBean = GsonUtil.GsonToBean(data, TimCustomMessage.class);
                showUserCardPop(timRoomMessageBean);
//                final LiveRoomHeadPop headPop = new LiveRoomHeadPop(LiveRoomSwitchActivity.this,timRoomMessageBean,TAG_AUDIENCE);
//                headPop.setOnPopOperateListener(new LiveRoomHeadPop.OnPopOperateListener() {
//                    @Override
//                    public void onFollow(TextView tvAnchorFollow, int follow_state, String uid) {
//                        LiveHttpRequest.followAnchor(tvAnchorFollow,mContext,uid,follow_state);
//                    }
//
//                    @Override
//                    public void onAt(String uid,String name) {
//                        headPop.dismiss();
//                        String  sender = !TextUtil.isEmpty(name) ? name : uid;
//                        String at = "@" + sender;
//                        adapter.onAt(at);
////                        LiveHttpHelper.getInstance().getAnchorInfo(timRoomMessageBean.uid, new HttpListener() {
////                            @Override
////                            public void onSuccess(String data) {
////                                LiveRoomInfo liveRoomInfo = GsonUtil.GsonToBean(data,LiveRoomInfo.class);
////                                String  sender = !TextUtil.isEmpty(liveRoomInfo.getData().getNickname()) ? liveRoomInfo.getData().getNickname() : liveRoomInfo.getData().getUid();
////                                String at = "@" + sender;
////                                adapter.onAt(at);
////                            }
////
////                            @Override
////                            public void onFail(String msg) {
////                                LogUtil.d(msg);
////                            }
////                        });
//                    }
//
//                    @Override
//                    public void onReport(String mySelfId) {
//                        Intent intent = new Intent(LiveRoomSwitchActivity.this, ReportActivity.class);
//                        intent.putExtra("uid", mySelfId);
//                        intent.putExtra("source_type",1);
//                        LiveRoomSwitchActivity.this.startActivity(intent);
//                    }
//                });
//                headPop.showPopupWindow();
                break;
            case "tanmuOpenSendTxt":
                String txt = event.getData();
                TimCustomMessage dammuBean = GsonUtil.GsonToBean(txt, TimCustomMessage.class);
                sendDanmu(dammuBean);
                break;
            case LiveEventConstant.SHOW_ONLINE_USER:
                showLiveUserPop(event.getData());
                break;
            case LiveEventConstant.GO_TO_OTHER_ANCHOR:
//                Intent intent2 = new Intent(this, LiveRoomSwitchActivity.class);
//                NormalEventBean normalEventBean = GsonUtil.GsonToBean(event.getData(), NormalEventBean.class);
//                ScenesRoomInfoBean scenesRoomInfoBean = new ScenesRoomInfoBean();
//                scenesRoomInfoBean.setUid(normalEventBean.getEventData1());
//                scenesRoomInfoBean.setRoom_id(Integer.parseInt(normalEventBean.getEventData2()));
//                intent2.putExtra(RoomManager.ROOM_INFO, (Serializable) Arrays.asList(scenesRoomInfoBean));
//                intent2.putExtra(RoomManager.ROOM_ITEM,0);
//                intent2.putExtra(RoomManager.ROOM_CLICK_FROM,"");
//                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent2);
//                finish();
                NormalEventBean normalEventBean = GsonUtil.GsonToBean(event.getData(), NormalEventBean.class);
                EventBus.getDefault().post(new OtherLiveRoomEvent(normalEventBean.getEventData1(),normalEventBean.getEventData2()));
                mRvLiveSlide.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Intent intent2 = new Intent(LiveRoomSwitchActivity.this, MainActivity.class);
                        Intent intent2 = new Intent(LiveRoomSwitchActivity.this, HomePageActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                },300);
                break;
            case LiveEventConstant.SHARE_LIVE:
                showSharePop(event.getData());
                break;
            case LiveEventConstant.SHOW_GIFT_PANEL:
                showGiftPanel();
                break;
            case LiveEventConstant.SHOW_LINK_MIC_USER:
                showLinkMicUserPop(event.getData());
                break;
            case LiveEventConstant.SHOW_ANCHOR_MANAGER_CARD:
                getAnchorStateBeforeShow(event.getData());
                break;
            case LiveEventConstant.HIDE_LINK_MIC_USER:
                hideLinkMicUserPop();
                break;
            case LiveEventConstant.REPORT_LIVE_MESSAGE:
                reportChatMessage(event.getData());
                break;
            case LiveEventConstant.GET_BEAUTY_PERMISSION:
                reportBeautyPermission();
                break;
            case LiveEventConstant.SHOW_LIVE_ANCHOR_SETTING:
                showLiveAnchorSettingPop(event.getData());
                break;
            case LiveEventConstant.SHOW_LIVE_TASK:
                showLiveTaskPop();
                break;
            case LiveEventConstant.SHOW_LIVE_TOP_RANK:
                Intent intent3 = new Intent(LiveRoomSwitchActivity.this, LiveRankingActivity.class);
                startActivity(intent3);
                break;
            case LiveEventConstant.AUDIENCE_START_FLOAT:
                //startFloat();
                break;
            case LiveEventConstant.SHOW_TICKET_BUY:
                showBuyTicketPop(event.getData());
                break;
            case LiveEventConstant.SHOW_PK_TOP_AUDIENCE:
                showPkTopAudienceTop(event.getData());
                break;
            case LiveEventConstant.SHOW_MY_LIVE_LEVEL_PAGE:
                gotoLevelPage(event.getType());
                break;
            case LiveEventConstant.DISABLE_SCREENSHOT:
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                break;
            case LiveEventConstant.SHOW_SEND_LIVE_RED_ENVELOPES:
                showSendRedEnvelopesPop(event.getData());
                break;
            case LiveEventConstant.SHOW_LIVE_RED_ENVELOPES:
//                int index = 0;
//                if (!TextUtil.isEmpty(event.getTag())) {
//                    index = Integer.parseInt(event.getTag());
//                }
//                showRedEnvelopesPop(event.getData(),index,false);
                if (!TextUtil.isEmpty(event.getTag())) {
                    LiveRedEnvelopesBean.DataBean dataBean1 = GsonUtil.GsonToBean(event.getTag(), LiveRedEnvelopesBean.DataBean.class);
                    showRedEnvelopesPop(event.getData(),dataBean1,false);
                }
                break;
            case LiveEventConstant.SHOW_LIVE_RED_ENVELOPES_2:
//                int index2 = 0;
//                if (!TextUtil.isEmpty(event.getTag())) {
//                    index2 = Integer.parseInt(event.getTag());
//                }
//                showRedEnvelopesPop(event.getData(),index2,true);
                if (!TextUtil.isEmpty(event.getTag())) {
                    LiveRedEnvelopesBean.DataBean dataBean2 = GsonUtil.GsonToBean(event.getTag(), LiveRedEnvelopesBean.DataBean.class);
                    showRedEnvelopesPop(event.getData(),dataBean2,true);
                }
                break;
            case LiveEventConstant.GET_LIVE_RED_ENVELOPES_LIST:
                getLiveRedBagList(event.getData());
                break;
            case LiveEventConstant.SHOW_LIVE_LOTTERY_DRAW:
                showLotteryDrawLayout();
                break;
            case LiveEventConstant.SHOW_LIVE_BLIND_RULE:
                LiveBlindBoxRulePop pop =new LiveBlindBoxRulePop(this);
                pop.show();
                break;
        }
    }
    //发送弹幕接口
    private void sendDanmu(final TimCustomMessage dammuBean) {
        if(dammuBean == null) return;
        LiveHttpHelper.getInstance().sendBarrage(dammuBean.anchor_uid, dammuBean.content, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                HttpResult httpResult = GsonUtil.GsonToBean(data, HttpResult.class);
                if(httpResult.getRetcode() == 2001){
                    ToastUtil.show(mContext,httpResult.getMsg());
                } else if (httpResult.getRetcode() == 2000){
                    ToastUtil.show(mContext,httpResult.getMsg());
                }
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d(msg);
                ToastUtil.show(mContext,msg);
            }
        });
    }

    //送礼物调用接口
    private void sendGift(final GiftInfo giftInfo) {
        LiveHttpHelper.getInstance().sendGift(giftInfo.sendUser, giftInfo.giftId, String.valueOf(giftInfo.count), String.valueOf(giftInfo.presentType),new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                HttpResult httpResult = GsonUtil.GsonToBean(data, HttpResult.class);
                switch (httpResult.getRetcode()){
                    case 2000:
                        //更新免费礼物的数量
                        if(giftInfo.presentType == 2){
                            int changeNum = Integer.valueOf(giftInfo.num) - giftInfo.count;
                            giftInfo.num = String.valueOf(changeNum);
                            EventBus.getDefault().post(new AppLiveGiftChangedEvent(changeNum,giftInfo));
                        }
                        break;
                    case 2001:
                        ToastUtil.show(mContext,httpResult.getMsg());
                        break;
                }
            }

            @Override
            public void onFail(int code, String msg) {
                LogUtil.d(msg);
                ToastUtil.show(mContext,msg);
            }
        });
    }

    void showLiveUserPop(String uid) {
        LiveOnlineUserPop liveOnlineUserPop = new LiveOnlineUserPop(LiveRoomSwitchActivity.this,uid,0);
        liveOnlineUserPop.showPopupWindow();
        liveOnlineUserPop.setOnLiveUserListener(new LiveOnlineUserPop.OnLiveUserListener() {
            @Override
            public void onLiveUserKitOut(@NotNull TimCustomMessage tcm) {
                showKickTipDialog(tcm.anchor_uid,tcm.uid);
                //kickOutMicUser(tcm.getAnchor_uid(),tcm.getUid());
            }

            @Override
            public void onLiveUserLink(@NotNull TimCustomMessage tcm) {
                ToastUtil.show("onLiveUserLink");
            }

            @Override
            public void onLiveUserClick(@NotNull final TimCustomMessage tcm) {
                showUserCardPop(tcm);
            }
        });
    }
    LiveLinkMicUserPop liveLinkMicUserPop;
    void showLinkMicUserPop(String linkStateJson) {
        LinkMicStateBean linkMicStateBean = GsonUtil.GsonToBean(linkStateJson,LinkMicStateBean.class);
        liveLinkMicUserPop = new LiveLinkMicUserPop(LiveRoomSwitchActivity.this,linkMicStateBean);
        liveLinkMicUserPop.showPopupWindow();
        liveLinkMicUserPop.setOnLiveUserListener(new LiveLinkMicUserPop.OnLiveUserListener() {
            @Override
            public void onLiveUserKickMic(@NotNull TimCustomMessage tcm) {
                //kickOutMicUser(tcm.anchor_uid,tcm.uid);
                showKickTipDialog(tcm.anchor_uid,tcm.uid);
            }

            @Override
            public void onLiveUserClick(@NotNull TimCustomMessage tcm) {
                showUserCardPop(tcm);
            }

            @Override
            public void onLiveUserDoCamera(boolean isOpen) {
                if (getAudienceFragment() != null) {
                    getAudienceFragment().doCamera(isOpen);
                }
            }

            @Override
            public void onLiveUserLink(int type) {
                if (getAudienceFragment() != null) {
                    getAudienceFragment().doLinkAnchor(type);
                }
            }

        });
    }

    void hideLinkMicUserPop() {
        if (liveLinkMicUserPop != null && liveLinkMicUserPop.isShowing()) {
            liveLinkMicUserPop.dismiss();
        }
    }

    Timer mTimer;
    public void startHeaderReport() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
               // Log.d("logutil","开始轮询" + currentPostion);
                HttpHelper.getInstance().reportHeartBeat(list.get(currentPostion).getUid());
            }
        },100,20000);
    }

    public void stopHeaderReport() {
        //Log.d("logutil","结束轮询");
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    public void showSharePop(String data) {
        ShareAnchorBean shareAnchorBean = GsonUtil.GsonToBean(data,ShareAnchorBean.class);
        String uid = shareAnchorBean.getAnchorId();
        String nickname = shareAnchorBean.getLiveTitle();
        String pic = shareAnchorBean.getLivePoster();
        NormalShareBean normalShareBean = new NormalShareBean(4,uid,
                "直播推荐",
                nickname,
                nickname+"正在精彩直播中，快来圣魔观看吧~",
                HttpUrl.SMaddress,
                pic);
        NormalSharePop normalSharePop = new NormalSharePop(LiveRoomSwitchActivity.this,normalShareBean,true);
        normalSharePop.showPopupWindow();


//       // SharedPop sharedPop = new SharedPop(this, HttpUrl.NetPic() + HttpUrl.ShareUserDetail + uid, "正在直播", nickname + "正在圣魔精彩直播中", pic, 0, 3, "", "", "", uid);
//        SharedPop sharedPop = new SharedPop(this, uid,nickname,pic);
//        sharedPop.showAtLocation(mRvLiveSlide, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        final WindowManager.LayoutParams[] params = {getWindow().getAttributes()};
//        //当弹出Popupwindow时，背景变半透明
//        params[0].alpha = 0.7f;
//        getWindow().setAttributes(params[0]);
//        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
//        sharedPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                params[0] = getWindow().getAttributes();
//                params[0].alpha = 1f;
//                getWindow().setAttributes(params[0]);
//            }
//        });
    }



    public void shareLive(String anchorId,String poster,String title,boolean isGroup,String chatId) {
        LiveAnchorMessageBean liveAnchorMessageBean = new LiveAnchorMessageBean();
        LiveAnchorMessageBean.ContentDictBean contentDictBean = new LiveAnchorMessageBean.ContentDictBean();
        contentDictBean.setAnchorId(anchorId);
        contentDictBean.setLiveTitle(title);
        contentDictBean.setLivePoster(poster);
        contentDictBean.setRoomId("");
        liveAnchorMessageBean.setContentDict(contentDictBean);
        MessageInfo customMessage = MessageHelper.buildCustomMessage(GsonUtil.getInstance().toJson(liveAnchorMessageBean));
        MessageSendHelper.getInstance().sendNormalOutMessage(customMessage, isGroup, chatId, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                //ToastUtil.show();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }

    void showGiftPanel() {
        String token = SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", "");
        adapter.getLiveRoomAudienceFragment().showGiftPanel(LiveHttpHelper.getInstance().liveGiftList(HttpUrl.liveGiftList),token);

    }

    void showUserCardPop(TimCustomMessage tcm) {
        final LiveRoomHeadPop headPop = new LiveRoomHeadPop(LiveRoomSwitchActivity.this,tcm,TAG_AUDIENCE);
        headPop.setOnPopOperateListener(new LiveRoomHeadPop.OnPopOperateListener() {

            @Override
            public void onFollow(TextView tvAnchorFollow, int follow_state, String uid) {
                LiveHttpRequest.followAnchor(tvAnchorFollow,mContext,uid,follow_state);
            }

            @Override
            public void onAt(String uid,String name) {
                headPop.dismiss();
                String  sender = !TextUtil.isEmpty(name)? name : uid;
                String at = "@" + sender;
                adapter.onAt(at);
            }

            @Override
            public void onReport(String mySelfId) {
                Intent intent = new Intent(LiveRoomSwitchActivity.this, ReportActivity.class);
                intent.putExtra("uid", mySelfId);
                intent.putExtra("source_type",1);
                LiveRoomSwitchActivity.this.startActivity(intent);
            }
        });
        headPop.showPopupWindow();
    }

    void getAnchorStateBeforeShow(final String anchorId) {
        HttpHelper.getInstance().getLiveAnchorState(anchorId, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                LiveAnchorStateBean liveAnchorStateBean = GsonUtil.GsonToBean(data, LiveAnchorStateBean.class);
                showLiveAdminManagerPop(anchorId,liveAnchorStateBean.getData());
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }
    NormalMenuPopup normalMenuPopup;
    void showLiveAdminManagerPop(final String uid, final LiveAnchorStateBean.DataBean stateBean) {
        final List<NormalMenuItem> normalMenuItemList = new ArrayList<>();
        if ("1".equals(stateBean.getRecommend_status())) {
            normalMenuItemList.add(new NormalMenuItem(0, "取消推荐"));
        } else {
            normalMenuItemList.add(new NormalMenuItem(0, "推荐主播"));
        }
        if ("2".equals(stateBean.getAnchor_status())) {
            normalMenuItemList.add(new NormalMenuItem(0,"取消签约"));
        } else {
            normalMenuItemList.add(new NormalMenuItem(0,"签约主播"));
        }
        normalMenuItemList.add(new NormalMenuItem(0,"资料修改"));
        normalMenuItemList.add(new NormalMenuItem(0,"删除直播封面"));
        normalMenuItemList.add(new NormalMenuItem(0,"删除直播标题"));
        normalMenuItemList.add(new NormalMenuItem(0,"警告主播"));
        normalMenuItemList.add(new NormalMenuItem(0,"强制下播"));
        if ("1".equals(stateBean.getProhibition_status())) {
            normalMenuItemList.add(new NormalMenuItem(0, "解禁主播"));
        } else {
            normalMenuItemList.add(new NormalMenuItem(0, "封禁主播"));
        }


        normalMenuPopup = new NormalMenuPopup(LiveRoomSwitchActivity.this,normalMenuItemList);
        normalMenuPopup.showPopupWindow();
        normalMenuPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                switch (position) {
                    case 0:
                        recommendOrCancelAnchor(uid);
                        break;
                    case 1:
                        if (!"2".equals(stateBean.getAnchor_status())) {
                            showSignPop(uid);
                        } else {
                            signAnchor(uid,!"2".equals(stateBean.getAnchor_status()));
                        }
                        break;
                    case 2:
                        showLiveCardPop(uid);
                        break;
                    case 3:
                        deleteLiveInfo(uid,"2");
                        break;
                    case 4:
                        deleteLiveInfo(uid,"1");
                        break;
                    case 5:
                        Intent intent = new Intent(LiveRoomSwitchActivity.this, LiveAdminOperateActivity.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                        break;
                    case 6:
                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(LiveRoomSwitchActivity.this);
                        builder.setMessage("是否强制主播下播?")
                                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                kickOutLiver(uid);
                            }
                        }).create().show();
                        break;
                    case 7:
                        if (normalMenuItemList.get(4).getContent().equals("解禁主播")) {
                            relieveBanLiver(uid);
                        } else {
                            Intent intent2 = new Intent(LiveRoomSwitchActivity.this, LiveAdminOperateActivity.class);
                            intent2.putExtra("uid", uid);
                            intent2.putExtra("type", 2);
                            startActivity(intent2);
                        }

                        break;
                }
            }
        });
    }


    void deleteLiveInfo(String  uid,String type){
        HttpHelper.getInstance().deleteLiveTitleOrCover(uid, type, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                ToastUtil.show(msg);
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(msg);
            }
        });
    }

    //推荐 取消推荐
    void recommendOrCancelAnchor(String uid) {
        HttpHelper.getInstance().recommendOrCancel(uid, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(LiveRoomSwitchActivity.this,"操作成功");
                if (normalMenuPopup != null) {
                    normalMenuPopup.dismiss();
                }
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(LiveRoomSwitchActivity.this,msg);
            }
        });
    }


    LiveInfoPop liveInfoPop;
    void showLiveCardPop(String uid) {
        liveInfoPop = new LiveInfoPop(this,uid);
        liveInfoPop.showPopupWindow();
        liveInfoPop.setOnSimpleListener(new OnSimplePopListener() {
            @Override
            public void doSimplePop() {
                showChoosePhoto();
            }
        });
    }

    void showChoosePhoto() {
        final List<NormalMenuItem> normalMenuItemList = new ArrayList<>();
        normalMenuItemList.add(new NormalMenuItem(0,"从相册选择"));
        final NormalMenuPopup menuPopup = new NormalMenuPopup(LiveRoomSwitchActivity.this,normalMenuItemList);
        menuPopup.showPopupWindow();
        menuPopup.setOnSimpleItemListener(new OnSimpleItemListener() {
            @Override
            public void onItemListener(int position) {
                requestPhotoPermission();
                menuPopup.dismiss();
            }
        });
    }

    void requestPhotoPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片", 1000, perms);
        } else {
            choosePhotoBySelector();
        }
    }

    void choosePhotoBySelector() {
        ImageSelector.builder()
                .useCamera(false)
                .setSingle(false)
                .setMaxSelectCount(1)
                .canPreview(true)
                .setCrop(true)
                .setCropRatio(1f)
                .start(LiveRoomSwitchActivity.this,10000);
    }
    int picFlag;
    void uploadImage(String path) {
        picFlag = 1;
        Bitmap loadbitmap = BitmapFactory.decodeFile(path, getBitmapOption(2));
        Bitmap rotaBitmap = PhotoRemoteUtil.rotaingImageView(PhotoRemoteUtil.getBitmapDegree(path), loadbitmap);
        ByteArrayInputStream is = new ByteArrayInputStream(Bitmap2Bytes(rotaBitmap));
        PhotoUploadTask put = new PhotoUploadTask(
                NetPic() + "Api/Api/filePhoto"//  "http://59.110.28.150:888/Api/Api/filePhoto"
                , is,
                this, new MyHandler(LiveRoomSwitchActivity.this));
        put.start();
    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    static class MyHandler extends Handler {
        WeakReference<LiveRoomSwitchActivity> weakReference;
        public MyHandler(LiveRoomSwitchActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LiveRoomSwitchActivity activity = weakReference.get();
            if (msg.what == 152) {
                String s = (String) msg.obj;
                BeanIcon beanicon = new Gson().fromJson(s, BeanIcon.class);
                String imgpre = SharedPreferencesUtils.geParam(activity, "image_host", "");
                String imgUrl = imgpre + beanicon.getData();
                if (activity.liveInfoPop != null) {
                    activity.liveInfoPop.refreshImageUrl(imgUrl);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10000) {
            if (data != null) {
                ToastUtil.show(LiveRoomSwitchActivity.this,"图片上传中");
                //获取选择器返回的数据
                ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
                for (int i = 0; i < images.size(); i++) {
                    LogUtil.d(images.get(i));
                    uploadImage(images.get(i));
                }
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == 1000) {
            if (!perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return;
            }
            choosePhotoBySelector();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (perms.contains(Manifest.permission.CAMERA)) {
            ToastUtil.show(getApplicationContext(), "授权失败,请开启相机权限");
            return;
        }
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(getApplicationContext(), "授权失败,请开启读写权限");
            return;
        }
    }

    //强制主播下播
    void kickOutLiver(String uid) {
        HttpHelper.getInstance().adminOperateLive(4, uid, "你已被官方强制下播", 0, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(LiveRoomSwitchActivity.this, "强制下播成功");
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(LiveRoomSwitchActivity.this, msg);
            }
        });
    }

    //解禁主播
    private void relieveBanLiver(String uid) {
        HttpHelper.getInstance().adminOperateLive(3, uid, "", 0, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(LiveRoomSwitchActivity.this, "解禁成功");
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(LiveRoomSwitchActivity.this, msg);
            }
        });
    }

    private void showSignPop(String uid) {
        SignAnchorPop signAnchorPop = new SignAnchorPop(LiveRoomSwitchActivity.this,uid);
        signAnchorPop.showPopupWindow();
        signAnchorPop.setOnSignAnchorListener(new SignAnchorPop.OnSignAnchorListener() {
            @Override
            public void doSignAnchor() {
                signAnchor(uid,true);
            }
        });
    }


    //签约/取消签约
    private void signAnchor(String uid, final boolean isSign) {
        HttpHelper.getInstance().adminOperateLive(isSign ? 5 : 6, uid, "", 0, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                ToastUtil.show(LiveRoomSwitchActivity.this, isSign ? "签约成功":"取消签约");
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(LiveRoomSwitchActivity.this, msg);
            }
        });
    }

    LiveRoomAudienceFragment getAudienceFragment() {
        if (adapter != null && adapter.getLiveRoomAudienceFragment() != null) {
            return adapter.getLiveRoomAudienceFragment();
        } else {
            return null;
        }
    }

    public void reportChatMessage(String liveMessageData) {
        ReportLiveMessageBean reportLiveMessageBean = GsonUtil.GsonToBean(liveMessageData,ReportLiveMessageBean.class);
        if (reportLiveMessageBean != null) {
            HttpHelper.getInstance().reportLiveMessage(reportLiveMessageBean.getAnchorId(),
                    reportLiveMessageBean.getRoomId(), reportLiveMessageBean.getContent(), new HttpCodeListener() {
                        @Override
                        public void onSuccess(String data) {

                        }

                        @Override
                        public void onFail(int code, String msg) {

                        }
                    });
        }
    }

    void reportBeautyPermission() {
        HttpHelper.getInstance().reportBeautyPermission(new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(LiveRoomSwitchActivity.this,msg);
            }
        });
    }

    void showLiveAnchorSettingPop(String liveSettingStateJson) {
        LiveSettingStateBean liveSettingStateBean = GsonUtil.GsonToBean(liveSettingStateJson, LiveSettingStateBean.class);
        LiveAnchorSettingPop liveAnchorSettingPop = new LiveAnchorSettingPop(LiveRoomSwitchActivity.this,false, liveSettingStateBean);
        liveAnchorSettingPop.showPopupWindow();
        liveAnchorSettingPop.setOnLiveSettingListener(new LiveAnchorSettingPop.OnLiveSettingListener() {
            @Override
            public void onChooseCameraFront() {
                if ( getAudienceFragment() != null ) {
                     getAudienceFragment().doCameraFront();
                }
            }

            @Override
            public void onChooseCameraBack() {
                if ( getAudienceFragment() != null ) {
                    getAudienceFragment().doCameraBack();
                }
            }

            @Override
            public void onChooseCameraClose() {
                if ( getAudienceFragment() != null ) {
                    getAudienceFragment().doCameraClose();
                }
            }

            @Override
            public void onChooseAudioOpen() {
                if ( getAudienceFragment() != null ) {
                    getAudienceFragment().doAudioOpen();
                }
            }

            @Override
            public void onChooseAudioClose() {
                if ( getAudienceFragment() != null ) {
                    getAudienceFragment().doAudioClose();
                }
            }

            @Override
            public void onChooseAudioSetting() {
                if ( getAudienceFragment() != null ) {
                    getAudienceFragment().doAudioSetting();
                }
            }

            @Override
            public void onChooseBeauty() {
                if ( getAudienceFragment() != null ) {
                    getAudienceFragment().doVideoBeauty();
                }
            }

            @Override
            public void onChooseManagerSpeak() {

            }
        });
    }

    void showLiveTaskPop() {
        LiveAudienceTaskPop liveAudienceTaskPop = new LiveAudienceTaskPop(LiveRoomSwitchActivity.this,MyApp.uid);
        liveAudienceTaskPop.showPopupWindow();
        liveAudienceTaskPop.setOnLiveTaskPopListener(new LiveAudienceTaskPop.OnLiveTaskPopListener() {
            @Override
            public void doTaskRank() {
                showTaskRankPop();
            }
        });
    }

    void showTaskRankPop() {
        LiveAuTaskRankPop liveAuTaskRankPop = new LiveAuTaskRankPop(LiveRoomSwitchActivity.this,MyApp.uid);
        liveAuTaskRankPop.showPopupWindow();
    }


    private boolean isStartService = false;
    private ServiceConnection mVideoServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获取服务的操作对象
            FloatVideoWindowService2.MyBinder binder = (FloatVideoWindowService2.MyBinder) service;
            binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void startFloat() {
        if (!PermissionUtils.requestFloatPermission(this, 24)) {
            ToastUtil.show(LiveRoomSwitchActivity.this, "最小化需要开启悬浮窗权限");
            //com.tencent.qcloud.tim.uikit.utils.ToastUtil.toastShortMessage("最小化需要开启悬浮窗权限");
            return;
        }
        isStartService = true;
        //Constents.mAnchorTextureView = mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getVideoView().getGLSurfaceView();
        //Constents.mAnchorViewView = mLayoutTuiLiverRomAnchor.getLiveRomAnchor().getVideoView();
        Intent intent = new Intent(this, FloatVideoWindowService2.class);//开启服务显示悬浮框
        bindService(intent, mVideoServiceConnection, Context.BIND_AUTO_CREATE);
        moveTaskToBack(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("float","audienceActivity onNewIntent");
        //onRestart 有时候不走  逻辑放入onNewIntent中
        if (getAudienceFragment() != null) {
            Log.d("float","audienceFragment onRestart");
            getAudienceFragment().doActivityRestart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("float","audienceActivity onRestart");
//        if (getAudienceFragment() != null) {
//            Log.d("float","audienceFragment onRestart");
//            getAudienceFragment().doActivityRestart();
//        }
    }

        /**
     * 判断主播是否开启了悬浮窗权限
     * 如果开启了 直接跳转页面 并且开启悬浮窗
     * 未开启 显示开启提示框 不跳转页面
     *
     * @param //intent
     */
    @Override
    public void startActivity(Intent intent) {
        if (intent.getData() == null || !intent.getData().toString().contains("package")) {
            if (intent.getExtras() != null) {
                if (intent.getExtras().containsKey("_mmessage_appPackage")) { //微信分享
                    super.startActivity(intent);
                    return;
                }
                if (intent.getExtras().containsKey("url") && intent.getExtras().get("url").toString().contains("alipay")) { //支付宝支付
                    //LogUtil.d("支付宝支付");
                    super.startActivity(intent);
                    return;
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //6.0版本
                super.startActivity(intent);
                if (getAudienceFragment() != null) {
                    getAudienceFragment().onBackPressed();
                }
            } else {
                super.startActivity(intent);
            }

        } else {
            super.startActivity(intent);
        }
    }




    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        private String SYSTEM_REASON = "reason";
        private String SYSTEM_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    if (!Constents.isShowAnchorFloatWindow) {
                        onBackPressed();
                    }
                }
            }
        }
    };

    //最小化窗口时 退出app 结束直播
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void StopLive(StopLiveEvent event) {
        closeLiveRoom();
    }

    void closeLiveRoom() {
        if (getAudienceFragment() != null) {
            getAudienceFragment().exitRoom();
            getAudienceFragment().finishActivity();
        }
    }

    void showBuyTicketPop(String anchorId) {
        final LiveBuyTicketPop liveBuyTicketPop = new LiveBuyTicketPop(LiveRoomSwitchActivity.this,anchorId);
        liveBuyTicketPop.showPopupWindow();
        liveBuyTicketPop.setOnTicketPopListener(new LiveBuyTicketPop.OnTicketPopListener() {
            @Override
            public void doPopBuySuc() {
                getAudienceFragment().doTicketBuySuc();
                ToastUtil.show(LiveRoomSwitchActivity.this,"购买成功");
                liveBuyTicketPop.dismiss();
            }

            @Override
            public void doPopBuyFail(@Nullable String msg) {
                ToastUtil.show(LiveRoomSwitchActivity.this,msg);
                liveBuyTicketPop.dismiss();
            }

            @Override
            public void doPopDismiss() {
                getAudienceFragment().closeRoom();
            }
        });
    }

    void showPkTopAudienceTop(String topData) {
        PkTopAudienceMessageBean pkBean = GsonUtil.GsonToBean(topData, PkTopAudienceMessageBean.class);
        LivePkTopAudiencePop livePkTopAudiencePop = new LivePkTopAudiencePop(LiveRoomSwitchActivity.this,pkBean.getAnchorId(),pkBean.getOtherId(),pkBean.isUs());
        livePkTopAudiencePop.showPopupWindow();
    }

    void gotoLevelPage(String type) {
        Intent intent = new Intent(LiveRoomSwitchActivity.this, MyLiveLevelActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
        //showSendRedEnvelopesPop();
       // showRedEnvelopesPop();
    }

    void showSendRedEnvelopesPop(String roomId) {
        SendLiveRedEnvelopesPop sendLiveRedEnvelopesPop = new SendLiveRedEnvelopesPop(LiveRoomSwitchActivity.this,roomId);
        sendLiveRedEnvelopesPop.showPopupWindow();
        //sendLiveRedEnvelopesPop.showPopupWindow(getAudienceFragment().getRootView());
       // sendLiveRedEnvelopesPop.getPopupWindow().showAtLocation(getAudienceFragment().getRootView(), Gravity.CENTER, 0, 0);
    }

    void showRedEnvelopesPop(String roomId,int index,boolean isReceive) {
        //LiveRedEnvelopesPop redEnvelopesDialog = new LiveRedEnvelopesPop(LiveRoomSwitchActivity.this);
        //redEnvelopesDialog.showPopupWindow();
        //getLiveRedBagList(roomId);
        LiveRedEnvelopesPop redEnvelopesPop = new LiveRedEnvelopesPop(LiveRoomSwitchActivity.this,redEnvelopesList.get(index),isReceive);
        redEnvelopesPop.showPopupWindow();
        redEnvelopesPop.setOnLiveRedEnvelopes(new LiveRedEnvelopesPop.OnLiveRedEnvelopesListener() {
            @Override
            public void doLiveUserClick(String uid) {
                if (getAudienceFragment() != null) {
                    getAudienceFragment().showUserCard(uid);
                }
            }

            @Override
            public void refreshRedEnvelopes() {
                getLiveRedBagList(roomId);
            }
        });
    }

    LinkedList<LiveRedEnvelopesBean.DataBean> redEnvelopesList;
    void getLiveRedBagList(String roomId) {
        if (redEnvelopesList == null) {
            redEnvelopesList = new LinkedList<>();
        }
        HttpHelper.getInstance().getLiveRedBagList(roomId, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                LiveRedEnvelopesBean liveRedEnvelopesBean = GsonUtil.GsonToBean(data, LiveRedEnvelopesBean.class);
                if (liveRedEnvelopesBean!= null) {
                    List<LiveRedEnvelopesBean.DataBean> tempList = liveRedEnvelopesBean.getData();
                    redEnvelopesList.clear();
                    redEnvelopesList.addAll(tempList);
                    refreshLiveRedEnvelop();
                }
            }

            @Override
            public void onFail(int code, String msg) {
                if (getAudienceFragment() != null) {
                    refreshLiveRedEnvelop();
                }
            }
        });
    }

    public void refreshLiveRedEnvelop() {
        if (redEnvelopesList == null) {
            showOrHideRedEnvelopesView(false);
            return;
        }
        if (redEnvelopesList.isEmpty()) {
            showOrHideRedEnvelopesView(false);
        } else {
            showOrHideRedEnvelopesView(true);
        }
    }

    public void showOrHideRedEnvelopesView(boolean isShow) {
        if (getAudienceFragment() != null) {
            if (isShow) {
                getAudienceFragment().showLiveRedEnvelopesSign(redEnvelopesList.getFirst().getHead_pic(),redEnvelopesList.size());
               // getAudienceFragment().showLiveRedEnvelopesSign(redEnvelopesList);
            } else {
                getAudienceFragment().hideLiveRedEnvelopesSign();
            }
        }
    }

    public void showLotteryDrawLayout() {
        LiveLotteryDrawPop liveLotteryDrawPop = new LiveLotteryDrawPop(this);
        liveLotteryDrawPop.showPopupWindow();
    }

    void showRedEnvelopesPop(String roomId, LiveRedEnvelopesBean.DataBean redEnvelopesBean, boolean isReceive) {
        LiveRedEnvelopesPop redEnvelopesPop = new LiveRedEnvelopesPop(LiveRoomSwitchActivity.this,redEnvelopesBean,isReceive);
        redEnvelopesPop.showPopupWindow();
        redEnvelopesPop.setOnLiveRedEnvelopes(new LiveRedEnvelopesPop.OnLiveRedEnvelopesListener() {
            @Override
            public void doLiveUserClick(String uid) {
                if (getAudienceFragment() != null) {
                    getAudienceFragment().showUserCard(uid);
                }
            }

            @Override
            public void refreshRedEnvelopes() {
                //getLiveRedBagList(roomId);
                getAudienceFragment().getRedBagList();
            }
        });
    }

    public void showRechargePop() {
        RechargeBeanPop rechargeBeanPop = new RechargeBeanPop(LiveRoomSwitchActivity.this);
        rechargeBeanPop.showPopupWindow();
        rechargeBeanPop.setPayListener(new RechargeBeanPop.PayListener() {
            @Override
            public void doAliPay(@NotNull String payJson) {
                new AliPayMentTaskManager(LiveRoomSwitchActivity.this, HttpUrl.ALIPAYCzcharge, payJson);
            }
        });
    }

    private void showKickTipDialog(String anchorId,String uid) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("当前用户正在连麦/发言，是否关闭ta的连麦/发言？")
                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                kickOutMicUser(anchorId,uid);
            }
        }).create().show();
    }

    private void kickOutMicUser(String anchorId,String uid) {
        HttpHelper.getInstance().kickMicUser(anchorId, uid, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                ToastUtil.show(msg);
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(msg);
            }
        });
    }
}