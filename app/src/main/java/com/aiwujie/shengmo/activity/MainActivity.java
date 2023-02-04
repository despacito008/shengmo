package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.BuildConfig;
import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.binding.BindingMobileActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.AllUserStates;
import com.aiwujie.shengmo.bean.LargeSvgaBean;
import com.aiwujie.shengmo.bean.LargeSvgaDataBean;
import com.aiwujie.shengmo.bean.NoticeUnreadMessageBean;
import com.aiwujie.shengmo.bean.RedPointData;
import com.aiwujie.shengmo.bean.TimUserSignBean;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.customview.MyViewpager;
import com.aiwujie.shengmo.eventbus.BigHornEvent;
import com.aiwujie.shengmo.eventbus.ClearRedPointEvent;
import com.aiwujie.shengmo.eventbus.DynamicPageTurnEvent;
import com.aiwujie.shengmo.eventbus.JumpDynamicEvent;
import com.aiwujie.shengmo.eventbus.MainPageTurnEvent;
import com.aiwujie.shengmo.eventbus.MessageEvent;
import com.aiwujie.shengmo.eventbus.RedPointEvent;
import com.aiwujie.shengmo.eventbus.StopLiveEvent;
import com.aiwujie.shengmo.eventbus.TIMLoginEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.eventbus.VisitEvent;
import com.aiwujie.shengmo.fragment.HomeMyFragment;
import com.aiwujie.shengmo.fragment.mainfragment.FragmentNear;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.bean.AttendanceStateBean;
import com.aiwujie.shengmo.kt.bean.NormalResultBean;
import com.aiwujie.shengmo.kt.ui.fragment.tabtopbar.HomePageAroundFragment;
import com.aiwujie.shengmo.kt.ui.fragment.tabtopbar.HomePageDynamicFragment;
import com.aiwujie.shengmo.kt.ui.fragment.tabtopbar.HomePageLiveFragment;
import com.aiwujie.shengmo.kt.ui.fragment.tabtopbar.HomePageMessageFragment;
import com.aiwujie.shengmo.kt.ui.view.DailyAttendancePop;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.kt.util.NormalUtilKt;
import com.aiwujie.shengmo.kt.util.SpKey;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.OkHttpRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.service.AliveService;
import com.aiwujie.shengmo.service.FloatingService;
import com.aiwujie.shengmo.service.PlayAliveService;
import com.aiwujie.shengmo.timlive.net.RoomManager;
import com.aiwujie.shengmo.timlive.ui.LiveRoomAnchorActivity;
import com.aiwujie.shengmo.timlive.ui.LiveRoomSwitchActivity;
import com.aiwujie.shengmo.utils.DateUtils;
import com.aiwujie.shengmo.utils.FinishActivityManager;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LocationListener;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.aiwujie.shengmo.view.NormalTipsPop;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.opensource.svgaplayer.SVGAParser;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.custom.Constents;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.qcloud.tim.tuikit.live.base.HttpPostRequest;
import com.tencent.qcloud.tim.tuikit.live.bean.OtherLiveRoomEvent;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveEventConstant;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.bean.LiveMethodEvent;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, EasyPermissions.PermissionCallbacks, ConversationManagerKit.MessageUnreadWatcher {

    @BindView(R.id.mMain_viewpager)
    MyViewpager mMainViewpager;
    @BindView(R.id.mMmain_rb01)
    RadioButton mMmainRb01;
    @BindView(R.id.mMmain_rb02)
    RadioButton mMmainRb02;
    @BindView(R.id.mMmain_rb03)
    RadioButton mMmainRb03;
    @BindView(R.id.mMmain_rb04)
    RadioButton mMmainRb04;
    @BindView(R.id.mMmain_rb05)
    RadioButton mMmainRb05;
    @BindView(R.id.mMain_rg)
    RadioGroup mMainRg;
    @BindView(R.id.mMain_NearRedPoint)
    TextView mMainNearRedPoint;
    @BindView(R.id.mMain_DiscoveryRedPoint)
    TextView mMainDiscoveryRedPoint;
    @BindView(R.id.mMain_MyRedPoint)
    TextView mMainMyRedPoint;
    @BindView(R.id.mMain_DynamicRedPoint)
    TextView mMainDynamicRedPoint;
    @BindView(R.id.mMain_ll)
    LinearLayout mMainLl;
    @BindView(R.id.mMain_messageCount)
    TextView mMainMessageCount;
    @BindView(R.id.mMain_lltabs)
    FrameLayout mMainLltabs;

    private boolean isQuit;
    private List<Fragment> fragments;
    private List<RadioButton> rabuttons;
    private Handler handler = new Handler();
    private boolean isFirst = true;
    private FragmentManager manager;
    private WeakReference<AMapLocationClient> weakMLocationClient;
    private WeakReference<AMapLocationListener> weakLocationListener;
    private Unbinder mUnbinder;
    private int signRetcode;
    private MyViewpagerAdapter myViewpagerAdapter;
    private Intent chatroomintent;
    private int allNum;
    private long onBackPressedTime;
    private long lastTime, currentTime;

    //HomeMessageFragment messageFragment = new HomeMessageFragment();
    HomePageMessageFragment homePageMessageFragment;
    HomePageAroundFragment homePageAroundFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getMyOwnInfo();
        setData();
        setListener();
        ImmersionBar.with(this)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();
        LogUtil.d("首页 onCreate");

        //启动服务
        startAliveService();
//        startPlayMusicAliveService();
        chatroomintent = new Intent(MainActivity.this, FloatingService.class);

        //LogUtil.d(GetDeviceIdUtils.isAdopt(MainActivity.this));

        FinishActivityManager.getManager().addActivity(this);
        getChatState();
        //isVisibleHornRedPoint();
        //startCountDown(12000);
        //LogUtil.d("mac = " + GetDeviceIdUtils.getMacAddress());
        //LogUtil.d("SN = " + GetDeviceIdUtils.getSN(MainActivity.this));
        getCacheUrlList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateLocation();
    }

    private void getRedDutNum() {
        int visicount = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "visitcount", 0);
        if (visicount != 0) {
            mMainMyRedPoint.setVisibility(View.VISIBLE);
        }
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetRedDutNum, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                LogUtil.d(response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final RedPointData data = new Gson().fromJson(response, RedPointData.class);
                            if (data.getData().getNewRegerNum() != 0) {
//                                mMainNearRedPoint.setVisibility(View.VISIBLE);        // 隐藏掉身边按钮 红点显示
                                EventBus.getDefault().post(new RedPointEvent(data.getData().getNewRegerNum(), 1));
                            }
//                        if (data.getData().getVisitNum() != 0) {
//                            mMainMyRedPoint.setVisibility(View.VISIBLE);
//                            EventBus.getDefault().post(new RedPointEvent(data.getData().getVisitNum(), 2));
//                        }
                            if (data.getData().getGroupNum() != 0) {
                                mMainDiscoveryRedPoint.setVisibility(View.VISIBLE);
                                SharedPreferencesUtils.setParam(getApplicationContext(), "tempGroupCount", data.getData().getGroupNum());
                                EventBus.getDefault().post(new RedPointEvent(data.getData().getGroupNum(), 3));
                            }
                            if (data.getData().getDynamic() != 0) {
                                //mMainDynamicRedPoint.setVisibility(View.VISIBLE);
                                EventBus.getDefault().post(new RedPointEvent(data.getData().getDynamic(), 4));
                            }
                            if (data.getData().getFollowDyNum() != 0) {
                                EventBus.getDefault().post(new RedPointEvent(data.getData().getFollowDyNum(), 5));
                            }
                            SharedPreferencesUtils.setParam(getApplicationContext(), "tuijianrednum", data.getData().getDyRecommendNum());
                            if (data.getData().getDyRecommendNum() != 0) {
                                mMainDynamicRedPoint.setVisibility(View.VISIBLE);
                                //int tuijianrednum = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "tuijianrednum", 0);
                                //tuijianrednum+=data.getData().getDyRecommendNum();
                                EventBus.getDefault().post(new RedPointEvent(data.getData().getDyRecommendNum(), 6));
                            }
                            SharedPreferencesUtils.setParam(getApplicationContext(), "tuidingrednum", data.getData().getDyTopNum());
                            if (data.getData().getDyTopNum() != 0) {
                                //int tuidingrednum = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "tuidingrednum", 0);
                                ///tuidingrednum+=data.getData().getDyTopNum();
                                mMainDynamicRedPoint.setVisibility(View.VISIBLE);
                                EventBus.getDefault().post(new RedPointEvent(data.getData().getDyTopNum(), 7));

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMainDynamicRedPoint.setText(data.getData().getDyTopNum() + "");
                                    }
                                });
                            } else {
                                mMainDynamicRedPoint.setVisibility(View.GONE);
                            }

//                            if (data.getData().getAllNum() != 0) {
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        allNum = data.getData().getAllNum();
//                                        mMainDynamicRedPoint.setText(allNum + "");
//                                    }
//                                });
//                            } else {
//                                mMainDynamicRedPoint.setVisibility(View.GONE);
//                            }

                            if (data.getData().getAlldynum_nolaud() != 0) {
                                EventBus.getDefault().post(new MessageEvent(data.getData().getAlldynum_nolaud(),1));
//                                if (messageFragment != null) {
//                                    messageFragment.refreshNoticeMessage(data.getData().getAlldynum_nolaud());
//                                }
                                if (homePageMessageFragment != null) {
                                    homePageMessageFragment.showCommentRedDot(data.getData().getAlldynum_nolaud());
                                }
                            }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(String event) {

        if (event.equals("hongdiantuijian")) {
            int tuijianrednum = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "tuijianrednum", 0);
            allNum -= tuijianrednum;
            mMainDynamicRedPoint.setText(allNum + "");
            if (allNum <= 0) {
                mMainDynamicRedPoint.setVisibility(View.GONE);
            }
            SharedPreferencesUtils.setParam(getApplicationContext(), "tuijianrednum", 0);

        }
        if (event.equals("hongdiantuiding")) {
            int tuidingrednum = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "tuidingrednum", 0);
            allNum -= tuidingrednum;
            mMainDynamicRedPoint.setText(allNum + "");
            if (allNum <= 0) {
                mMainDynamicRedPoint.setVisibility(View.GONE);
            }
            SharedPreferencesUtils.setParam(getApplicationContext(), "tuidingrednum", 0);
        }


        if (event.equals("gezhonghongdianshuaxin")) {
            getRedDutNum();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(TokenFailureEvent event) {
        MyApp.clearSharedPreference();
        // RongIM.getInstance().logout();
        ConversationManagerKit.getInstance().destroyConversation();
        TUIKit.logout(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
        ToastUtil.show(MainActivity.this, "您的账号登录已过期");
        Intent intent = new Intent(MyApp.getInstance(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void isOpenLocationPermission() {
        //所要申请的权限
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "如果您需要定位请开启此权限...", 0, perms);
        }
    }

    private void isOpensystemalertwindow() {
        //所要申请的权限
        String[] perms = {Manifest.permission.SYSTEM_ALERT_WINDOW};
        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "如果您需要通知请开启此权限...", 0, perms);
        }
    }

    private void isOpenWriteStrogePermission() {
        //所要申请的权限
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "如果您需要升级版本请开启此权限...", 1, perms);
        }
    }

    private void setData() {
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        if (SharedPreferencesUtils.getParam(getApplicationContext(), "modle", "").equals("")) {
            SharedPreferencesUtils.setParam(getApplicationContext(), "modle", "0");
        }
        mMainViewpager.setOffscreenPageLimit(5);
        mMainViewpager.setScrollble(false);
        rabuttons = new ArrayList<>();
        rabuttons.add(mMmainRb02);
        rabuttons.add(mMmainRb04);
        rabuttons.add(mMmainRb03);
        rabuttons.add(mMmainRb01);
        rabuttons.add(mMmainRb05);
        TextPaint tp = mMmainRb02.getPaint();
        tp.setFakeBoldText(true);
        fragments = new ArrayList<>();
        //fragments.add(new FragmentNear());
        fragments.add(homePageAroundFragment);
        //fragments.add(new FragmentDynamic());
        fragments.add(new HomePageDynamicFragment());
        //fragments.add(messageFragment);
        homePageMessageFragment = new HomePageMessageFragment();
        fragments.add(homePageMessageFragment);
        //fragments.add(new HomeLiveFragment());
        fragments.add(new HomePageLiveFragment());

        fragments.add(new HomeMyFragment());
        myViewpagerAdapter = new MyViewpagerAdapter(getSupportFragmentManager());
        mMainViewpager.setAdapter(myViewpagerAdapter);
        mMainViewpager.setCurrentItem(0, false);
        manager = getSupportFragmentManager();

        mMainLltabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void setListener() {
        mMainRg.setOnCheckedChangeListener(this);
        FloatWindowLayout.getInstance().setOnHeartBeatReport(new FloatWindowLayout.OnHeartBeatReport() {
            @Override
            public void reportHeart(String anchorId) {
                HttpHelper.getInstance().reportHeartBeat(anchorId);
            }
        });
    }


    class MyViewpagerAdapter extends FragmentPagerAdapter {

        public MyViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < rabuttons.size(); i++) {
            RadioButton radioButton = rabuttons.get(i);

            TextPaint tp = radioButton.getPaint();
            tp.setFakeBoldText(false);
        }
        switch (checkedId) {
            case R.id.mMmain_rb01:
                mMainViewpager.setCurrentItem(3);
                TextPaint tp01 = mMmainRb01.getPaint();
                tp01.setFakeBoldText(true);
                break;
            case R.id.mMmain_rb02:
                checkedItem(0);
                break;
            case R.id.mMmain_rb03:
                checkedItem(2);
                break;
            case R.id.mMmain_rb04:
                checkedItem(1);
                //判断是否显示大喇叭上的红点
                isVisibleHornRedPoint();
                break;
            case R.id.mMmain_rb05:
                checkedItem(4);
                //loginTencentIM();
                break;
            default:
                break;
        }
    }

    /**
     * @param position 当前item
     */
    public void checkedItem(int position) {
        mMainViewpager.setCurrentItem(position);
        TextPaint tp02 = rabuttons.get(position).getPaint();
        tp02.setFakeBoldText(true);
    }


    private void isVisibleHornRedPoint() {
        String labaState = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "labaState", "0");
        // if (labaState.equals("0")) {
        //获取大喇叭状态
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetBigPresentNum, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
//                    Log.i("huoqudalaba", "onSuccess: "+response);

                List<String> strings = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    strings.add("" + response);
                }

                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 3001:
                            JSONObject data = obj.getJSONObject("data");
                            String giftnum = data.getString("giftnum");
                            String topcardnum = data.getString("topcardnum");
                            String vipnum = data.getString("vipnum");
                            String redBagNum = data.getString("redbagnum");
                            int allnum = data.getInt("allnum");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "giftnum", giftnum);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "topcardnum", topcardnum);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "vipnum", vipnum);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "redbagnum", redBagNum);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "labaallnum", allnum);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "labaState", "1");
                            EventBus.getDefault().post(new BigHornEvent(1));
                            break;
                        case 3002:
                            SharedPreferencesUtils.setParam(getApplicationContext(), "giftnum", "0");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "topcardnum", "0");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "vipnum", "0");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "redbagnum", "0");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "labaallnum", 0);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "labaState", "0");
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
        //}
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long timeSpan = System.currentTimeMillis() - onBackPressedTime;
            onBackPressedTime = System.currentTimeMillis();
            if (timeSpan > 2000) {
                ToastUtil.show(getApplicationContext(), "再按一次退出程序");
            } else {
                if (Constents.isShowAudienceFloatWindow) {
                    //如果悬浮窗 退出直播间
                    EventBus.getDefault().post(new StopLiveEvent());
                    mMainViewpager.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TUIKit.logout(new IUIKitCallBack() {
                                @Override
                                public void onSuccess(Object data) {

                                }

                                @Override
                                public void onError(String module, int errCode, String errMsg) {

                                }
                            });
                            stopAliveService();
                            System.exit(0);
                            Process.killProcess(Process.myPid());
                            finish();
                        }
                    },1000);
                } else {
                    TUIKit.logout(new IUIKitCallBack() {
                        @Override
                        public void onSuccess(Object data) {

                        }

                        @Override
                        public void onError(String module, int errCode, String errMsg) {

                        }
                    });
                    stopAliveService();
                    System.exit(0);
                    Process.killProcess(Process.myPid());
                    finish();
                }
            }

        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //下面两个方法是实现EasyPermissions的EasyPermissions.PermissionCallbacks接口
    //分别返回授权成功和失败的权限
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        ToastUtil.show(getApplicationContext(), "授权成功");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this,"哈哈").build().show();
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(ClearRedPointEvent event) {
        switch (event.getFlag()) {
            case 0:
                mMainNearRedPoint.setVisibility(View.GONE);
                break;
            case 2:
                mMainDiscoveryRedPoint.setVisibility(View.GONE);
                break;
            case 4:
            case 8:
                //mMainDynamicRedPoint.setVisibility(View.GONE);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatroom(String chatroom) {
        if (chatroom.equals("chatroomxiaochuangguan")) {
            boolean floatingService = isServiceExisted(MainActivity.this, "com.aiwujie.shengmo.service.FloatingService");
            if (floatingService) {
                //  quit();
                stopService(chatroomintent);
            }
        }
        if (chatroom.equals("chatroomxiaochuangguankai")) {

            SharedPreferencesUtils.setParam(getApplicationContext(), "chatroombutteryhatch", "1");
            if (checkAlertWindowsPermission(this)) {
                boolean floatingService = isServiceExisted(MainActivity.this, "com.aiwujie.shengmo.service.FloatingService");
                if (!floatingService) {
                    startService(chatroomintent);
                }
            } else {
                // quit();
                SharedPreferencesUtils.setParam(getApplicationContext(), "chatroombutteryhatch", "0");
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("聊吧最小化需要设置允许“显示悬浮窗”").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(getAppDetailSettingIntent());
                    }
                }).create().show();
            }
        }
        if (chatroom.equals("chatroomdakai")) {
            chatroomkaigetPermission();
        }
    }

    int messageNum, noticeNum;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(MessageEvent event) {
        if (event.getType() == 0) {
            messageNum = event.getMsgCount();
        } else {
            noticeNum = event.getMsgCount();
        }
        LogUtil.i("eventType = " + event.getType() + ",messageNum = " + messageNum + ",noticeNum = " + noticeNum);
        int count = messageNum + noticeNum;
        if (count <= 0) {
            mMainMessageCount.setVisibility(View.GONE);
        } else {
            mMainMessageCount.setVisibility(View.VISIBLE);
            if (count > 9999) {
                mMainMessageCount.setText("9999+");
            } else {
                mMainMessageCount.setText(String.valueOf(count));
            }
        }
//        ShortcutBadger.applyCount(getApplicationContext(), count);
//        if (count <= 0) {
//            mMainMessageCount.setVisibility(View.GONE);
//        } else {
//            mMainMessageCount.setVisibility(View.VISIBLE);
//            if (count > 999) {
//                mMainMessageCount.setText(count + "");
//            } else {
//                mMainMessageCount.setText(count + "");
//            }
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(VisitEvent event) {
        int visitcount = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "visitcount", 0);
        if (visitcount != 0) {
            mMainMyRedPoint.setVisibility(View.VISIBLE);
        } else {
            mMainMyRedPoint.setVisibility(View.GONE);
        }
    }

    private void updateLocation() {
        //初始化LocationClient对象
        AMapLocationClient mLocationClient = new AMapLocationClient(getApplicationContext());
        weakMLocationClient = new WeakReference<>(mLocationClient);
        mLocationClient = null;
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        LocationListener locationListener = new LocationListener(getApplicationContext(), weakMLocationClient);
        weakLocationListener = new WeakReference<>(locationListener);
        weakMLocationClient.get().setLocationListener(weakLocationListener.get());
        locationListener = null;
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setOnceLocation(true);
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        weakMLocationClient.get().setLocationOption(mLocationOption);
        weakMLocationClient.get().startLocation();
        mMainViewpager.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Log.i("locationchange", "onLocationChanged:upload "+MyApp.lat+","+MyApp.lng);
                updateLoginTimeAndLocation();
            }
        },5000);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(JumpDynamicEvent event) {
        mMainViewpager.setCurrentItem(3);
        mMmainRb04.setChecked(true);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MainPageTurnEvent event) {
        mMainViewpager.setCurrentItem(event.getPosition());
        rabuttons.get(event.getPosition()).setChecked(true);
        if (event.getPosition() == 1) {
            EventBus.getDefault().post(new DynamicPageTurnEvent(event.getType()));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(LiveMethodEvent event) {
        switch (event.getType()) {
            case LiveEventConstant.ANCHOR_RESUME_LIVING:
                Intent intent = new Intent(this, LiveRoomAnchorActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case LiveEventConstant.AUDIENCE_RESUME_LIVING:
//                Intent intent1 = new Intent(this, LiveRoomSwitchActivity.class);
//                intent1.setFlags(FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent1);
                if (!FloatWindowLayout.getInstance().getFloatState()) {
                    FloatWindowLayout.getInstance().closeFloatWindow();
//                    QNRoomManager.getInstance().gotoLiveRoom(this,
//                            FloatWindowLayout.getInstance().getAnchorId(),FloatWindowLayout.getInstance().getRoomId());
//
                    RoomManager.enterLiveRoom(this,
                            FloatWindowLayout.getInstance().getAnchorId(),
                            FloatWindowLayout.getInstance().getRoomId());
                } else {
                   // Intent intent1 = new Intent(this, QNLiveRoomAudienceActivity.class);
                    Intent intent1 = new Intent(this, LiveRoomSwitchActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                }
                break;
        }
    }

    private void startPlayMusicAliveService() {
        Intent intent = new Intent(this, PlayAliveService.class);
        startService(intent);
    }

    private void startAliveService() {
        Intent intent = new Intent(this, AliveService.class);
        startService(intent);
    }


    private void stopAliveService() {
        Intent intent = new Intent(this, AliveService.class);
        stopService(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d("main onResume");
        OkHttpRequestManager.getInstance().setTag(this.getLocalClassName());
       // MyApp.uid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "uid", "");
        if (isFirst) {
            isFirst = false;
            //各种红点和数字
            //getRedDutNum();
            getNoticeUnreadMessage();
            //判断是否开启定位权限
            isOpenLocationPermission();
            //判断是否开启通知权限
            isOpensystemalertwindow();
            //判断是否开启存储权限
            isOpenWriteStrogePermission();
            //检测是否有新版本
            //JudgeVersionUtils.judgeVersion(handler, this, false);
            LoginTencentAV();
            loginTencentIM();
            //登录时间
            UpLocationUtils.LogintimeAndLocation();
            //updateLoginTimeAndLocation();
            lastTime = SystemClock.currentThreadTimeMillis();
            getSignTimesInWeeks();//先获取一下接口判断是否签到   再跳弹窗  主要是因为首页签到与我的页面签到复用
        }
       // VipAndVolunteerUtils.isVip(this, handler);
    }

    private void OnHighResultIntent() {
        for (int i = 0; i < rabuttons.size(); i++) {
            RadioButton radioButton = rabuttons.get(i);

            TextPaint tp = radioButton.getPaint();
            tp.setFakeBoldText(false);
        }
        rabuttons.get(0).setChecked(true);
        checkedItem(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        setIntent(intent);
        if (intent != null){
            LogUtil.d("OnNewIntent", "onNewIntent");
           if ("High".equals(intent.getStringExtra(IntentKey.FLAG))){
                OnHighResultIntent();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkLiveRoomIntent();
        //getRedDutNum();
        //更新位置
        //updateLocation();
        //登录时间
        //UpLocationUtils.LogintimeAndLocation();
        //updateLoginTimeAndLocation();
        //isVisibleHornRedPoint();
        checkSignState();
    }

    void updateLoginTimeAndLocation() {
        HttpHelper.getInstance().updateLocationAndLastLoginTime();
    }



    void checkSignState() {
        currentTime = SystemClock.currentThreadTimeMillis();
        if (DateUtils.isSameData(String.valueOf(lastTime), String.valueOf(currentTime))) { //同一天
            LogUtil.d("是同一天");
        } else { //不是同一天
            lastTime = SystemClock.currentThreadTimeMillis();
            getSignTimesInWeeks();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TUIKit.logout(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });

//        stopPlayAliveService();
        stopAliveService();
        EventBus.getDefault().unregister(this);
        SharedPreferencesUtils.setParam(getApplicationContext(), "chatroombutteryhatch", "0");
        OkHttpRequestManager.getInstance().cancelTag(this.getLocalClassName());
        FinishActivityManager.getManager().finishActivity();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        @SuppressLint("RestrictedApi")
        Fragment fragment = manager.getFragments().get(3);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    //判断 签到没签到
    private void getSignTimesInWeeks() {
        HttpHelper.getInstance().getSignState(new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                Type type = new TypeToken<NormalResultBean<AttendanceStateBean>>(){}.getType();
                NormalResultBean<AttendanceStateBean> stateBean = GsonUtil.getInstance().fromJson(data, type);
                if (stateBean != null && stateBean.getData() != null) {
                    if ("0".equals(stateBean.getData().getSignStatus())) {
                        DailyAttendancePop dailyAttendancePop = new DailyAttendancePop(MainActivity.this);
                        dailyAttendancePop.showPopupWindow();
                    }
                }
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }


    /**
     * 判断 悬浮窗口权限是否打开
     *
     * @param context
     * @return true 允许  false禁止
     */
    public boolean checkAlertWindowsPermission(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = 24;
            arrayOfObject1[1] = Binder.getCallingUid();
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1));
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     *
     * @return
     */
    private Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return localIntent;
    }

    private void chatroomkaigetPermission() {
        String[] perms = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "需要以下权限:\n\n1.录音相机权限", 1, perms);
        } else {
         /*   Intent intent = new Intent(this, ChatRoomActivity.class);
            startActivity(intent);
            EventBus.getDefault().post("chatroomxiaochuangguan");
            SharedPreferencesUtils.setParam(getApplicationContext(), "chatroombutteryhatch", "0");*/
        }
    }

    public static boolean isServiceExisted(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;
            if (serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    private void LoginTencentAV() {

    }

    public void loginTencentIM() {
        if (V2TIMManager.getInstance().getLoginStatus() == V2TIMManager.V2TIM_STATUS_LOGOUT) {
            TUIKit.login(MyApp.uid, MyApp.token, new IUIKitCallBack() {
                @Override
                public void onSuccess(Object data) {
                    LogUtil.d("im登录成功");
                    initTIMUserInfo();
                    EventBus.getDefault().postSticky(new TIMLoginEvent(true));
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    LogUtil.d("im登录失败 " + errMsg);
                    getTimUserSign();
                }
            });

        }
        ConversationManagerKit.getInstance().addUnreadWatcher(this);
        //初始化一下 live包网络请求 需要的url和token
        HttpPostRequest.getInstance().initUrlAndToken(HttpUrl.NetPic(),SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", ""), BuildConfig.VERSION_NAME);
    }

    void getTimUserSign() {
        HttpHelper.getInstance().getTimUserSign(new HttpListener() {
            @Override
            public void onSuccess(String data) {
                LogUtil.d(data);
                TimUserSignBean timUserSignBean = GsonUtil.GsonToBean(data, TimUserSignBean.class);
                if (timUserSignBean != null && timUserSignBean.getData() != null) {
                    String uid = timUserSignBean.getData().getUid();
                    String sign = timUserSignBean.getData().getT_sign();
                    if (!TextUtil.isEmpty(sign)) {
                        TUIKit.login(uid, sign, new IUIKitCallBack() {
                            @Override
                            public void onSuccess(Object data) {
                                //LogUtil.d("im登录成功");
                                initTIMUserInfo();
                                EventBus.getDefault().post(new TIMLoginEvent(true));
                            }

                            @Override
                            public void onError(String module, int errCode, String errMsg) {
                                ToastUtil.show("连接聊天服务器失败,请重新登录");
                                SharedPreferencesUtils.setParam(getApplicationContext(), "t_sign", "yes");
                                EventBus.getDefault().post(new TokenFailureEvent());
                                finish();
                            }
                        });
                    } else {
                        ToastUtil.show("连接聊天服务器失败,请重新登录");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "t_sign", "yes");
                        EventBus.getDefault().post(new TokenFailureEvent());
                        finish();
                    }
                }
            }

            @Override
            public void onFail(String msg) {
            }
        });
    }

    @Override
    public void updateUnread(int count) {
        EventBus.getDefault().post(new MessageEvent(count));
    }

    void initTIMUserInfo() {
        V2TIMManager.getInstance().getUsersInfo(Arrays.asList(MyApp.uid), new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                UserModel userModel = ProfileManager.getInstance().getUserModel();
                userModel.userAvatar = v2TIMUserFullInfos.get(0).getFaceUrl();
                userModel.userName = v2TIMUserFullInfos.get(0).getNickName();
                userModel.userId = v2TIMUserFullInfos.get(0).getUserID();
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
                String admin = vipAndVolunteerData.getData().getIs_admin();
                MyApp.isAdmin = admin;
                SharedPreferencesUtils.setParam(MainActivity.this, "vip", vip);
                SharedPreferencesUtils.setParam(MainActivity.this, "svip", svip);
                SharedPreferencesUtils.setParam(MainActivity.this, "admin", admin);
                if (getIntent().getIntExtra("gotoByRegister", -1) == -1) {
                    if (!"1".equals(vip)) {
                        MyApp.clearScreen();
                    }
                } else {
                    getNewUserInviteState();
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    void getChatState() {
        HttpHelper.getInstance().getBanUserStatus(MyApp.uid, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(MainActivity.this)) {
                    return;
                }
                AllUserStates alluserStates = GsonUtil.GsonToBean(data, AllUserStates.class);
                AllUserStates.DataBean userBanStatusBean = alluserStates.getData();
                //禁言状态
                SharedPreferencesUtils.setParam(getApplicationContext(), "nospeak", userBanStatusBean.getChatstatus());
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    public void getNewUserInviteState() {
        HttpHelper.getInstance().getNewUserInviteState(new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFail(int code, final String msg) {
                if (code == 4001) {
                    mMainLl.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showInviteTipsPop(msg);
                        }
                    }, 10000);
                }
            }
        });
    }

    void showInviteTipsPop(String msg) {
        final NormalTipsPop normalTipsPop = new NormalTipsPop.Builder(MainActivity.this)
                .setTitle("领取SVIP")
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
                Intent intent = new Intent(MainActivity.this, BindingMobileActivity.class);
                intent.putExtra("neworchange", "new");
                startActivity(intent);
            }
        });
        normalTipsPop.setOutSideDismiss(false);
    }

    String other_uid;
    String other_roomId;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receivedOtherLiveRoomRequest(OtherLiveRoomEvent event) {
        other_uid = event.getUid();
        other_roomId = event.getRoomId();
    }

    void checkLiveRoomIntent() {
        if (!TextUtil.isEmpty(other_uid) && !TextUtil.isEmpty(other_roomId)) {
            RoomManager.enterLiveRoom(MainActivity.this,other_uid,Integer.parseInt(other_roomId));
            other_uid = "";
            other_roomId = "";
        }
    }

    void getCacheUrlList() {
        String svgaUrl = NormalUtilKt.getSpValue(SpKey.SVGA_KEY,"");
        if (TextUtil.isEmpty(svgaUrl)) {
            svgaUrl = "";
        }
        String finalSvgaUrl = svgaUrl;
        HttpHelper.getInstance().getLargeGift(new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                List<String> tempList = new LinkedList<>();
                LargeSvgaBean largeSvgaBean = GsonUtil.GsonToBean(data, LargeSvgaBean.class);
                if (largeSvgaBean.getData().size() > 0) {
                    for (LargeSvgaDataBean svgaDataBean: largeSvgaBean.getData()){
                        if (!finalSvgaUrl.contains(svgaDataBean.getGift_svgaurl())) {
                            tempList.add(svgaDataBean.getGift_svgaurl());
                        }
                    }
                    cacheSVGAImage(tempList);
                }
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    void cacheSVGAImage(List<String> urlList) {
        List<String> sucList = new LinkedList<>();
        for (int i = 0; i < urlList.size(); i++) {
            try{
                int finalI = i;
                SVGAParser.Companion.shareParser().getFileDownloader().resume(new URL(urlList.get(finalI)), inputStream -> {
                    sucList.add(urlList.get(finalI));
                    if (sucList.size() == urlList.size()) {
                        doCacheComplete(sucList);
                    }
                    return null;
                }, e -> {
                    sucList.add("");
                    if (sucList.size() == urlList.size()) {
                        doCacheComplete(sucList);
                    }
                    return null;
                });
            } catch (Exception e) {
                sucList.add("");
                if (sucList.size() == urlList.size()) {
                    doCacheComplete(sucList);
                }
            }
        }
    }

    void doCacheComplete(List<String> urlList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String url : urlList) {
            if (!TextUtil.isEmpty(url)) {
                stringBuilder.append(url).append(",");
            }
        }
        NormalUtilKt.saveSpValue(SpKey.SVGA_KEY,stringBuilder.toString());
    }

    void getNoticeUnreadMessage() {
        HttpHelper.getInstance().getNoticeUnreadMessage(new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                NoticeUnreadMessageBean noticeUnreadMessageBean = GsonUtil.GsonToBean(data, NoticeUnreadMessageBean.class);
                if (noticeUnreadMessageBean != null) {
                    NoticeUnreadMessageBean.DataBean.OtherBean otherNoticeBean = noticeUnreadMessageBean.getData().getOther();
                    if (otherNoticeBean.getNewRegerNum() != 0) {
                        EventBus.getDefault().post(new RedPointEvent(otherNoticeBean.getNewRegerNum(), 1));
                    }
                    if (otherNoticeBean.getGroupNum() != 0) {
                        mMainDiscoveryRedPoint.setVisibility(View.VISIBLE);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "tempGroupCount", otherNoticeBean.getGroupNum());
                        EventBus.getDefault().post(new RedPointEvent(otherNoticeBean.getGroupNum(), 3));
                    }
                    if (otherNoticeBean.getDynamic() != 0) {
                        EventBus.getDefault().post(new RedPointEvent(otherNoticeBean.getDynamic(), 4));
                    }
                    if (otherNoticeBean.getFollowDyNum() != 0) {
                        EventBus.getDefault().post(new RedPointEvent(otherNoticeBean.getFollowDyNum(), 5));
                    }
                    SharedPreferencesUtils.setParam(getApplicationContext(), "tuijianrednum", otherNoticeBean.getDyRecommendNum());
                    if (otherNoticeBean.getDyRecommendNum() != 0) {
                        EventBus.getDefault().postSticky(new RedPointEvent(otherNoticeBean.getDyRecommendNum(), 6));
                    }
                   // SharedPreferencesUtils.setParam(getApplicationContext(), "tuidingrednum", otherNoticeBean.getDyTopNum());
                    NormalUtilKt.saveSpValue("tuidingrednum",otherNoticeBean.getDyTopNum());
                    if (otherNoticeBean.getDyTopNum() != 0) {
                        mMainDynamicRedPoint.setVisibility(View.VISIBLE);
                        EventBus.getDefault().postSticky(new RedPointEvent(otherNoticeBean.getDyTopNum(), 7));
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mMainDynamicRedPoint.setText(otherNoticeBean.getDyTopNum() + "");
                            }
                        });
                    } else {
                        mMainDynamicRedPoint.setVisibility(View.GONE);
                    }



                    if (otherNoticeBean.getAlldynum_nolaud() != 0) {
                        EventBus.getDefault().post(new MessageEvent(otherNoticeBean.getAlldynum_nolaud(),1));
//                        if (messageFragment != null) {
//                            messageFragment.refreshNoticeMessage(otherNoticeBean.getAlldynum_nolaud());
//                        }
                        if (homePageMessageFragment != null) {
                            homePageMessageFragment.showCommentRedDot(otherNoticeBean.getAlldynum_nolaud());
                        }
                    }
                    NoticeUnreadMessageBean.DataBean.BigHornBean bigHornNoticeBean = noticeUnreadMessageBean.getData().getBigHorn();
                    String giftnum = String.valueOf(bigHornNoticeBean.getGiftnum());
                    String topcardnum = String.valueOf(bigHornNoticeBean.getTopcardnum());
                    String vipnum = String.valueOf(bigHornNoticeBean.getVipnum());
                    String redBagNum = String.valueOf(bigHornNoticeBean.getRedbagnum());
                    int allnum = bigHornNoticeBean.getAllnum();
                    SharedPreferencesUtils.setParam(getApplicationContext(), "giftnum", giftnum);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "topcardnum", topcardnum);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "vipnum", vipnum);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "redbagnum", redBagNum);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "labaallnum", allnum);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "labaState", "1");
                    EventBus.getDefault().post(new BigHornEvent(1));
                }
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }


}
