package com.aiwujie.shengmo.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.view.Display;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.LoginActivity;
import com.aiwujie.shengmo.bean.TimAccountMessageBean;
import com.aiwujie.shengmo.bean.TimDynamicMessageBean;
import com.aiwujie.shengmo.dao.DaoMaster;
import com.aiwujie.shengmo.dao.DaoSession;
import com.aiwujie.shengmo.dao.SwitchMarkBeanDao;
import com.aiwujie.shengmo.eventbus.StopLiveEvent;
import com.aiwujie.shengmo.kt.util.GlobalExceptionHandler;
import com.aiwujie.shengmo.qnlive.utils.Utils;
import com.aiwujie.shengmo.tim.helper.HelloChatController;
import com.aiwujie.shengmo.utils.FixedGlideImageLoader;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LocationListener;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.gloading.Gloading;
import com.aiwujie.shengmo.view.gloading.GlobalAdapter;
import com.aiwujie.shengmo.view.smart_refresh.MyRefreshLottieHeader;
import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.aliyun.svideo.downloader.DownloaderManager;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lzy.ninegrid.NineGridView;
import com.opensource.svgaplayer.SVGACache;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.utils.log.SVGALogger;
import com.qiniu.droid.rtc.QNRTCEnv;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.imsdk.message.Message;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.tuikit.live.bean.LiveMessageBean;
import com.tencent.qcloud.tim.tuikit.live.bean.TimCustomMessage;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.base.TUIKitListenerManager;
import com.tencent.qcloud.tim.uikit.component.face.CustomFace;
import com.tencent.qcloud.tim.uikit.component.face.CustomFaceGroup;
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig;
import com.tencent.qcloud.tim.uikit.config.GeneralConfig;
import com.tencent.rtmp.TXLiveBase;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.photopicker.imageloader.BGAImage;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.tillusory.sdk.TiSDK;



/**
 * Created by 290243232 on 2016/12/15.
 */
public class MyApp extends MultiDexApplication {
    public static int TEST_SDK_APP_ID = 1400542455; //?????????
    public static int LIVE_SDK_APP_ID = 1400506958; //?????????
    public static Context instance;
    public static String lat = "";
    public static String lng = "";
    public static String city = "";
    public static String address = "";
    public static String province = "";
    public static String uid = "";
    public static String isAdmin = "";
    public static String token = "";
    public static String baseUrl = "";
    //?????????????????? //0 ??????,1 ??????
    public static int wxloginFlag = 0;
    public static Uri resultUri = null;
    public static String pullAddress;
    public static String selectItemNum;
    public static String anchor_id;
    private String miAppId = "2882303761517557180";
    private String miAppKey = "5741755732180";
    private Handler handler = new Handler();
    public static final String QQAPP_ID = "1105968084";
    public static final String SINA_APPKEY = "2769337208";
    private WeakReference<AMapLocationClient> weakMLocationClient;
    private WeakReference<AMapLocationListener> weakLocationListener;
    public static final String UMENGAPPKEY = "57c9318667e58e97c6000fe4";
    private static DaoSession daoSession;
    private int qiantaishu = 0;
    private int diyici = 0;
    /*String licenceUrl = "https://license.vod2.myqcloud.com/license/v2/1259146267_1/v_cube.license";
    String licenseKey = "0f00d17780cddfb48a7d2c26ac5ec176";*/
//    private int qiantaishu = 0;
//    private int diyici = 0;
    // String licenceUrl = "http://license.vod2.myqcloud.com/license/v1/c026ba1053f80616d2058955034e8e8b/TXLiveSDK.licence";
    // String licenseKey = "d24435f17987e1c1fbb0809fd2522815";

    public static List<Class> mActivityList = new ArrayList<>();
    private Vibrator vibrator;
    private SoundPool soundPool;
    private int msgId;
    private int soundId;
    private IMEventListener imEventListener;


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        uid = SharedPreferencesUtils.geParam(MyApp.getInstance(), "uid", "");
        QupaiHttpFinal.getInstance().initOkHttpFinal();
        DownloaderManager.getInstance().init(this);

        //??????
        initQiNiuLive();

        //??????bugly
        //CrashReport.initCrashReport(getApplicationContext(), "b10f5bd8bc", false);
        Bugly.init(getApplicationContext(), "b10f5bd8bc", false);
        GlobalExceptionHandler.Companion.getInstance().initExceptionHandler(getApplicationContext());
        //leakcanary??????????????????
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        Fresco.initialize(this);
        LeakCanary.install(this);
        //Glide??????????????????????????????:Error ???You must not call setTag() on a view Glide is targeting??? when use Glide
        ViewTarget.setTagId(R.id.glide_tag);
        //NineGridview????????????
        NineGridView.setImageLoader(new GlideImageLoader());
        //????????????
        updateLocation();
        //????????????

//        RongIM.getInstance().setReadReceiptConversationTypeList(types);
//        RongIM.getInstance().setMessageAttachedUserInfo(false);
//        RongIM.registerMessageType(Message_GeRen_LiWu.class);
//        RongIM.registerMessageTemplate(new Message_GeRen_LiWuProvider());
//        RongIM.registerMessageType(Message_Group_LiWu.class);
//        RongIM.registerMessageTemplate(new Message_Group_LiWuProvider());
//        RongIM.registerMessageType(Message_Red_bao.class);
//        RongIM.registerMessageTemplate(new Message_Red_baoProvider());
//        RongIM.registerMessageType(Message_Shan.class);
//        RongIM.registerMessageTemplate(new Message_Shan_Provider());
//        RongIM.registerMessageType(Message_Sharedynamic.class);
//        RongIM.registerMessageTemplate(new Message_Sharedynamic_Provider());
//        RongIM.registerMessageType(Message_Sharperson.class);
//        RongIM.registerMessageTemplate(new Message_Sharperson_Provider());
//        RongIM.registerMessageType(Message_chatroom.class);
//        RongIM.registerMessageType(Message_chatroom_Gift.class);

        //??????
       /* if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            *//**
         * IMKit SDK??????????????? ?????????
         *//*
            RongIM.init(this);
            Conversation.ConversationType[] types = new Conversation.ConversationType[]{
                    Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.DISCUSSION
            };
            RongIM.getInstance().setReadReceiptConversationTypeList(types);
            RongIM.getInstance().setMessageAttachedUserInfo(false);
            RongIM.registerMessageType(Message_GeRen_LiWu.class);
            RongIM.registerMessageTemplate(new Message_GeRen_LiWuProvider());
            RongIM.registerMessageType(Message_Group_LiWu.class);
            RongIM.registerMessageTemplate(new Message_Group_LiWuProvider());
            RongIM.registerMessageType(Message_Red_bao.class);
            RongIM.registerMessageTemplate(new Message_Red_baoProvider());
            RongIM.registerMessageType(Message_Shan.class);
            RongIM.registerMessageTemplate(new Message_Shan_Provider());
            RongIM.registerMessageType(Message_Sharedynamic.class);
            RongIM.registerMessageTemplate(new Message_Sharedynamic_Provider());
            RongIM.registerMessageType(Message_Sharperson.class);
            RongIM.registerMessageTemplate(new Message_Sharperson_Provider());
            RongIM.registerMessageType(Message_chatroom.class);
            RongIM.registerMessageType(Message_chatroom_Gift.class);
        }*/
        //RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
        //??????
        /*List<IExtensionModule> moduleList =RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule= null;
        if(moduleList!=null){
            for (IExtensionModule module: moduleList){
                if(module instanceof DefaultExtensionModule){
                    defaultModule=module;
                    break;
                }
            }
            if(defaultModule!=null){
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
            }
        }*/

        //LiveKit.init(this, "6tnym1brns117");
     /*   RongIM.getInstance().enableNewComingMessageIcon(true);//?????????????????????
        RongIM.getInstance().enableUnreadMessageIcon(true);//????????????????????????*/
        //??????????????????
/*        RongIM.setConnectionStatusListener(new MyConnectionStatusListener());
        Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));
        //??????????????????????????????????????????
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
//                TextMessage msge= (TextMessage) message.getContent();
//                Log.i("conversationtype", "onReceived: " + message.getConversationType() + "," + message.getTargetId());
                if (message.getTargetId().equals("3")) {
                    EventBus.getDefault().post(new ReceiveDynamicMsgEvent(0));
                    EventBus.getDefault().post("gezhonghongdianshuaxin");
                }
                if (message.getConversationType()== Conversation.ConversationType.GROUP){
                    if (message.getContent() instanceof CommandMessage &&((CommandMessage) message.getContent()).getName().equals("cardname")){
                            String senderUserId = message.getSenderUserId();
                            String targetId = message.getTargetId();
                        RongCloudEvent.findGroupUserinfoById2(targetId,senderUserId);
                    }
                }
                //???????????????????????????
                if (message.getObjectName().equals("ec:phoneinfo")){
                    getxiaohuitiao(message.getTargetId());
                }
                if (message.getTargetId().equals("5")) {
                    //????????????????????????
                    int visitcount = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "visitcount", 0) + 1;
                    SharedPreferencesUtils.setParam(getApplicationContext(), "visitcount", visitcount);
                    EventBus.getDefault().post(new VisitEvent(visitcount));
                }
                if (message.getTargetId().equals("6")) {
                    final CommandMessage msg = (CommandMessage) message.getContent();
                    //????????????
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showLong(getApplicationContext(), msg.getData());
                            clearSharedPreference();
                            RongIM.getInstance().logout();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    });
                }
                if (message.getTargetId().equals("7")) {
                    //@?????????json
                    String saveAtJson;
                    String newAtJson = "";
                    String atJson;
                    try {
                        //@?????????json
                        atJson = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "atjson", "");
                        CommandMessage msg = (CommandMessage) message.getContent();
                        newAtJson += msg.getData() + "@-@";
                        if (!atJson.equals("")) {
                            saveAtJson = atJson + "@-@" + newAtJson;
                        } else {
                            saveAtJson = newAtJson;
                        }
                        SharedPreferencesUtils.setParam(getApplicationContext(), "atjson", saveAtJson.substring(0, saveAtJson.length() - 3));
                        EventBus.getDefault().post(new AtSomeOneEvent(1));
                        EventBus.getDefault().post(new ReceiveDynamicMsgEvent(0));
                        EventBus.getDefault().post("gezhonghongdianshuaxin");
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
                //??????
                if (message.getTargetId().equals("8")) {
                    CommandMessage msg = (CommandMessage) message.getContent();
                    if (msg.getName().equals("resumeuser")) {
                        SharedPreferencesUtils.setParam(getApplicationContext(), "nospeak", "1");
                    } else {
                        SharedPreferencesUtils.setParam(getApplicationContext(), "nospeak", "0");
                    }
                }
                int alertflag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "alertflag", 0);
                //alertflag -1.????????? 0.?????? 1.??????
                if (alertflag == -1 || alertflag == 0) {
                    return false;
                }
//                if(alertflag==0){
//                    MediaPlayerManager mediaPlayer=MediaPlayerManager.getInstance(getApplicationContext(),R.raw.dog);
//                    mediaPlayer.start();
//                    return true;
//                }
                if (alertflag == 1) {
                    if (message.getConversationType() == Conversation.ConversationType.GROUP || message.getConversationType() == Conversation.ConversationType.PRIVATE || message.getConversationType() == Conversation.ConversationType.SYSTEM) {
                        long[] pattern = {100, 300, 100, 300};
                        VibratorUtil.Vibrate(getApplicationContext(), pattern, false);
//                        VibratorUtil.Vibrate(getApplicationContext(), 200);

                        return true;
                    }
                }
                return false;
            }
        });*/
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            if (bundle.getString("ru.futurobot.glidedownloadintercenptor.MyGlideModule") == null) {
                installDownloadInterceptor();
            }
        } catch (PackageManager.NameNotFoundException e) {
            installDownloadInterceptor();
        }

        initWithUmeng();
        //???????????????


        BGASwipeBackHelper.init(this, null);
        //BGASwipeBackManager.getInstance().init(this);
        //??????7.0????????????
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }


        daoSession = DaoMaster.newDevSession(this, SwitchMarkBeanDao.TABLENAME);
        //getappqianhoutai();

        //????????????
        TXLiveBase.setConsoleEnabled(true);
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion(V2TIMManager.getInstance().getVersion());
        CrashReport.initCrashReport(getApplicationContext(), strategy);

        //TXLiveBase.getInstance().setLicence(instance, licenceUrl, licenseKey);
        closeAndroidPDialog();

       // TiSDK.initSDK("54f9781174bf43dcaf17892c75148ab2",getApplicationContext());

        //TiSDK.init("54f9781174bf43dcaf17892c75148ab2", this);
       TiSDK.init("54f9781174bf43dcaf17892c75148ab2", getApplicationContext());

       initGLoading();
       initSVGA();
    }

    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initTencentIM();
        suitImageLoader();
    }

    public static void clearSharedPreference() {
        LogUtil.d("??????token");
        SharedPreferencesUtils.setParam(getInstance(), "uid", "");
        SharedPreferencesUtils.setParam(getInstance(), "modle", "");
        SharedPreferencesUtils.setParam(getInstance(), "token", "");
        //??????????????????????????????
        SharedPreferencesUtils.setParam(getInstance(), "filterZong", "0");
        SharedPreferencesUtils.setParam(getInstance(), "filterShowAge", "");
        SharedPreferencesUtils.setParam(getInstance(), "filterCulture", "");
        SharedPreferencesUtils.setParam(getInstance(), "filterShowMoney", "");
        SharedPreferencesUtils.setParam(getInstance(), "filterSex", "");
        SharedPreferencesUtils.setParam(getInstance(), "filterQx", "");
        SharedPreferencesUtils.setParam(getInstance(), "filterRole", "");
        SharedPreferencesUtils.setParam(getInstance(), "filterLine", "");
        SharedPreferencesUtils.setParam(getInstance(), "filterUpAge", "");
        SharedPreferencesUtils.setParam(getInstance(), "ckvip", "0");
        SharedPreferencesUtils.setParam(getInstance(), "filterUpCulture", "");
        SharedPreferencesUtils.setParam(getInstance(), "filterUpMoney", "");
        SharedPreferencesUtils.setParam(getInstance(), "filterAuthen", "");
        //?????????????????????
        SharedPreferencesUtils.setParam(getInstance(), "dynamicSex", "");
        SharedPreferencesUtils.setParam(getInstance(), "dynamicSexual", "");
        SharedPreferencesUtils.setParam(getInstance(), "dynamicSwitch", "0");
        SharedPreferencesUtils.setParam(getInstance(), "dynamicSxSexual", "0");
        //??????????????????
        SharedPreferencesUtils.setParam(getInstance(), "groupSwitch", "0");
        SharedPreferencesUtils.setParam(getInstance(), "groupFlag", "0");
        //??????VIP??????
        SharedPreferencesUtils.setParam(getInstance(), "vip", "0");
        SharedPreferencesUtils.setParam(getInstance(), "volunteer", "0");
        SharedPreferencesUtils.setParam(getInstance(), "svip", "0");
        SharedPreferencesUtils.setParam(getInstance(), "admin", "0");
        SharedPreferencesUtils.setParam(getInstance(), "realname", "0");
        SharedPreferencesUtils.setParam(getInstance(), "match_state", "0");
        //??????????????????
        SharedPreferencesUtils.setParam(getInstance(), "visitcount", 0);

        SharedPreferencesUtils.clearParam(MyApp.getInstance(), "url_token");

        SharedPreferencesUtils.clearParam(getInstance(), "follow_tip" + MyApp.uid);
    }

    public static void clearScreen() {
        //??????????????????????????????
//        SharedPreferencesUtils.setParam(getInstance(), "filterZong", "0");
//        SharedPreferencesUtils.setParam(getInstance(), "filterShowAge", "");
//        SharedPreferencesUtils.setParam(getInstance(), "filterCulture", "");
//        SharedPreferencesUtils.setParam(getInstance(), "filterShowMoney", "");
//        SharedPreferencesUtils.setParam(getInstance(), "filterSex", "");
//        SharedPreferencesUtils.setParam(getInstance(), "filterQx", "");
//        SharedPreferencesUtils.setParam(getInstance(), "filterRole", "");
//        SharedPreferencesUtils.setParam(getInstance(), "filterLine", "");
//        SharedPreferencesUtils.setParam(getInstance(), "filterUpAge", "");
//        SharedPreferencesUtils.setParam(getInstance(), "ckvip", "0");
//        SharedPreferencesUtils.setParam(getInstance(), "filterUpCulture", "");
//        SharedPreferencesUtils.setParam(getInstance(), "filterUpMoney", "");
//        SharedPreferencesUtils.setParam(getInstance(), "filterAuthen", "");
        //?????????????????????
        SharedPreferencesUtils.setParam(getInstance(), "dynamicSex", "");
        SharedPreferencesUtils.setParam(getInstance(), "dynamicSexual", "");
        SharedPreferencesUtils.setParam(getInstance(), "dynamicSwitch", "0");
        SharedPreferencesUtils.setParam(getInstance(), "dynamicSxSexual", "0");
    }

    private void updateLocation() {
        //?????????LocationClient??????
        AMapLocationClient mLocationClient = new AMapLocationClient(getApplicationContext());

        weakMLocationClient = new WeakReference<AMapLocationClient>(mLocationClient);
        mLocationClient = null;
        //?????????AMapLocationClientOption??????
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        AMapLocationListener locationListener = new LocationListener(getApplicationContext(), weakMLocationClient);
        weakLocationListener = new WeakReference<AMapLocationListener>(locationListener);
        weakMLocationClient.get().setLocationListener(weakLocationListener.get());
        locationListener = null;
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setOnceLocation(true);
        //??????setOnceLocationLatest(boolean b)?????????true??????????????????SDK???????????????3s?????????????????????????????????????????????????????????true???setOnceLocation(boolean b)????????????????????????true???????????????????????????false???
        mLocationOption.setOnceLocationLatest(true);
        weakMLocationClient.get().setLocationOption(mLocationOption);
        weakMLocationClient.get().startLocation();
    }

    private void installDownloadInterceptor() {
//        Glide.get(this).getRegistry().replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(GlideProgressListener.getGlideOkHttpClient()));
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }


    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * Glide ??????
     */
    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, final ImageView imageView, String url) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.default_error);
            requestOptions.error(R.mipmap.default_error);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);


            Glide.with(context)
                    .load(url)
                    .override(700)
                    .apply(requestOptions)
                    .into(imageView);

            //requestOptions.transform(new MultiTransformation(new CenterCrop(),new RoundedCorners(20)));
            //requestOptions.transform(new GlideRoundTransform(context,10));

//            Glide.with(context)
//                    .asBitmap()
//                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .format(DecodeFormat.PREFER_ARGB_8888)//????????????????????????
//                    .load(url)
//                    .apply(requestOptions)
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                            int height = resource.getHeight();
//                        int width = resource.getWidth();
//                        if (height > 5000) {
//                            int newHeight = 5000;
//                            int newWidth = (int) (newHeight * (width * 1f / height));
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                                resource.setWidth(newWidth);
//                                resource.setHeight(newHeight);
//                            }
//                        }
//                        imageView.setImageBitmap(resource);
//                        }
//                    });

        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }


    /**
     * Glide ??????
     */
    private class NormalGlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.default_error);
            requestOptions.error(R.mipmap.default_error);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(20)));
            //requestOptions.transform(new GlideRoundTransform(context,10));

            Glide.with(context).load(url)
                    .apply(requestOptions)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

    public static Context getInstance() {
        return instance;
    }

    public static Context instance() {
        return instance;
    }

    private void initWithUmeng() {
        //final String market = PackerNg.getMarket(this);
        //MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, UMENGAPPKEY, market, MobclickAgent.EScenarioType.E_UM_NORMAL, true));
    }

    public static DaoSession getSwitchMarkBeanDao() {
        return daoSession;
    }

//    public void getxiaohuitiao(String targetId) {
//        MessageContent content = InformationNotificationMessage.obtain(
//                "Ta??????????????????"
//        );
////        RongIM.getInstance().insertIncomingMessage(
////                Conversation.ConversationType.PRIVATE, targetId, MyApp.uid,
////                new Message.ReceivedStatus(1),
////                content,
////                new RongIMClient.ResultCallback<Message>() {
////                    @Override
////                    public void onSuccess(Message message) {
////
////                    }
////
////                    @Override
////                    public void onError(RongIMClient.ErrorCode errorCode) {
////
////                    }
////                }
////        );
//    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void getappqianhoutai() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                qiantaishu++;
                if (diyici == 0) {
                    SharedPreferencesUtils.setParam(getApplicationContext(), "chatroombutteryhatch", "0");
                    SharedPreferencesUtils.setParam(getApplicationContext(), "tuijianrednum", 0);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "tuidingrednum", 0);
                }
                diyici++;
                String chatroombutteryhatch = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "chatroombutteryhatch", "0");
                if (chatroombutteryhatch.equals("1")) {
                    EventBus.getDefault().post("chatroomxiaochuangguankai");
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                qiantaishu--;
                if (qiantaishu == 0) {
                    EventBus.getDefault().post("chatroomxiaochuangguan");
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


    void initTencentIM() {
        // ?????? Config??????????????????
//        TUIKitConfigs configs = TUIKit.getConfigs();
//        configs.setSdkConfig(new V2TIMSDKConfig());
//        configs.setCustomFaceConfig(new CustomFaceConfig());
//        configs.setGeneralConfig(new GeneralConfig());
//        TUIKit.init(this, 1400499650, configs);

        GeneralConfig config = new GeneralConfig();
        config.setLogLevel(1);
        // ???????????????????????????view????????????
        config.setShowRead(true);
        config.setAppCacheDir(MyApp.instance().getFilesDir().getPath());
        if (new File(Environment.getExternalStorageDirectory() + "/111222333").exists()) {
            config.setTestEnv(true);
        }
        //CustomFaceConfig customFaceConfig = new CustomFaceConfig();
        TUIKit.getConfigs().setGeneralConfig(config);
        // TUIKit.getConfigs().setCustomFaceConfig(customFaceConfig);

        imEventListener = new IMEventListener() {
            @Override
            public void onForceOffline() {
                super.onForceOffline();
                ToastUtil.show(MyApp.getInstance(), "??????im - ????????????");
                logOutBySystem(true);
            }

            @Override
            public void onUserSigExpired() {
                super.onUserSigExpired();
                ToastUtil.show(MyApp.getInstance(), "??????im - ??????????????????");
                logOutBySystem(false);
            }

            @Override
            public void onConnected() {
                super.onConnected();
                //ToastUtil.show(MyApp.getInstance(),"??????im - ????????????");

            }

            @Override
            public void onDisconnected(int code, String desc) {
                super.onDisconnected(code, desc);
                ToastUtil.show(MyApp.getInstance(), "??????im - ????????????");

                LogUtil.d(code + desc);
                //logOutBySystem(false);
            }

            @Override
            public void onWifiNeedAuth(String name) {
                super.onWifiNeedAuth(name);
                ToastUtil.show(MyApp.getInstance(), "??????im - wifi????????????");
            }

            @Override
            public void onRefreshConversation(List<V2TIMConversation> conversations) {
                super.onRefreshConversation(conversations);
            }

            @Override
            public void onNewMessage(final V2TIMMessage v2TIMMessage) {
                super.onNewMessage(v2TIMMessage);

                if (!TextUtil.isEmpty(v2TIMMessage.getUserID())) {  //???????????????????????????
                    //??????
                    if (!"1".equals(v2TIMMessage.getUserID())) {
                        chooseSoundBeforeRing();
                    } else {
//                        if (!TextUtil.isEmpty(v2TIMMessage.getMessage().getCloudCustomString())) {
//                            TimCustomMessage timRoomMessageBean = GsonUtil.GsonToBean(v2TIMMessage.getMessage().getCloudCustomString(), TimCustomMessage.class);
//                            if(timRoomMessageBean == null) return;
//                            if(timRoomMessageBean.costomMassageType.equals("caveatAvChatRoom")){
//                                EventBus.getDefault().post(timRoomMessageBean);
//                            } else if(timRoomMessageBean.costomMassageType.equals("liveCaveat")){
//                                EventBus.getDefault().post(timRoomMessageBean);
//                            }
//                        } else

                        if (!TextUtil.isEmpty(v2TIMMessage.getCloudCustomData())) {
                            Message message = new Message();
                            message.getCloudCustomString();
                            TimCustomMessage timRoomMessageBean = GsonUtil.GsonToBean(v2TIMMessage.getCloudCustomData(), TimCustomMessage.class);
                            if (timRoomMessageBean != null) {
                                if (timRoomMessageBean.costomMassageType.equals("caveatAvChatRoom")) {
                                    EventBus.getDefault().post(timRoomMessageBean);
                                } else if (timRoomMessageBean.costomMassageType.equals("liveCaveat")) {
                                    EventBus.getDefault().post(timRoomMessageBean);
                                }
                            }
                            TimAccountMessageBean timAccountMessageBean = GsonUtil.GsonToBean(v2TIMMessage.getCloudCustomData(), TimAccountMessageBean.class);
                            if (timAccountMessageBean != null) {
                                if ("AvChatRoom".equals(timAccountMessageBean.getCloudCustomType())) {
                                    if (v2TIMMessage.getCustomElem() != null) {
                                        LiveMessageBean liveMessageBean = new LiveMessageBean();
                                        liveMessageBean.setMessage(v2TIMMessage);
                                        liveMessageBean.setCustomElemData("");
                                        liveMessageBean.setCloudCustomData(v2TIMMessage.getCloudCustomData());
                                        EventBus.getDefault().post(liveMessageBean);
                                    }
                                } else {
                                    if (timAccountMessageBean.getCostomMassageType().equals(TimAccountMessageBean.MESSAGE_BAN_ACCOUNT)) {
                                        logOutBySystem(false);
                                    } else if (timAccountMessageBean.getCostomMassageType().equals(TimAccountMessageBean.MESSAGE_BAN_CHAT)) {
                                        SharedPreferencesUtils.setParam(getApplicationContext(), "nospeak", "0");
                                    } else if (timAccountMessageBean.getCostomMassageType().equals(TimAccountMessageBean.MESSAGE_RESUME_CHAT)) {
                                        SharedPreferencesUtils.setParam(getApplicationContext(), "nospeak", "1");
                                    } else if (timAccountMessageBean.getCostomMassageType().equals(TimAccountMessageBean.MESSAGE_VIP_EXPIRE)) {
                                        clearScreen();
                                    } else {
                                        TimDynamicMessageBean timDynamicMessageBean = GsonUtil.GsonToBean(v2TIMMessage.getCloudCustomData(), TimDynamicMessageBean.class);
                                        if (timDynamicMessageBean != null) {
                                            EventBus.getDefault().post(timDynamicMessageBean);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //todo ????????????
                    if (true) { }
                    if (!TextUtil.isEmpty(v2TIMMessage.getCloudCustomData())) {
                        TimAccountMessageBean timAccountMessageBean = GsonUtil.GsonToBean(v2TIMMessage.getCloudCustomData(), TimAccountMessageBean.class);
                        if (timAccountMessageBean != null) {
                            if ("AVChatRoom".equals(timAccountMessageBean.getCloudCustomType())) {
                                LiveMessageBean liveMessageBean = new LiveMessageBean();
                                liveMessageBean.setMessage(v2TIMMessage);
                                liveMessageBean.setCustomElemData("");
                                liveMessageBean.setCloudCustomData(v2TIMMessage.getCloudCustomData());
                                EventBus.getDefault().post(liveMessageBean);
                            }
                        }
                    }
                } else {
                    LogUtil.d("??????" + v2TIMMessage.getSender() + "--" + v2TIMMessage.getNickName());
                    if (v2TIMMessage.getTextElem() != null) {
                        LogUtil.d("??????2" + v2TIMMessage.getTextElem().getText());
                    }
                    //??????
                    setGroupRingWay(v2TIMMessage.getGroupID());
                    V2TIMManager.getGroupManager().getGroupsInfo(Arrays.asList(v2TIMMessage.getGroupID()), new V2TIMValueCallback<List<V2TIMGroupInfoResult>>() {
                        @Override
                        public void onSuccess(List<V2TIMGroupInfoResult> v2TIMGroupInfoResults) {
                            if (v2TIMGroupInfoResults == null || v2TIMGroupInfoResults.size() == 0) {
                                return;
                            }
                            LogUtil.d(v2TIMGroupInfoResults.get(0).getGroupInfo().getGroupType());
                            if (v2TIMGroupInfoResults.get(0).getGroupInfo().getGroupType().equals("AVChatRoom")) {
                                //????????????????????????
                                if (v2TIMMessage.getCustomElem() != null) {
                                    LiveMessageBean liveMessageBean = new LiveMessageBean();
                                    liveMessageBean.setMessage(v2TIMMessage);
                                    liveMessageBean.setCustomElemData(new String(v2TIMMessage.getCustomElem().getData()));
                                    liveMessageBean.setCloudCustomData(v2TIMMessage.getCloudCustomData());
                                    EventBus.getDefault().post(liveMessageBean);
                                }


                                if (!TextUtils.isEmpty(v2TIMMessage.getCloudCustomData())) {
                                    /*Log.i("onNewMessage???onSuccess",v2TIMMessage.getMessage().getCloudCustomString());
                                    TimCustomRoom timRoomMessageBean = GsonUtil.GsonToBean(v2TIMMessage.getMessage().getCloudCustomString(), TimCustomRoom.class);
                                    if(timRoomMessageBean != null && !TextUtil.isEmpty(timRoomMessageBean.costomMassageType)){
                                        if(timRoomMessageBean.costomMassageType.equals("closeAvChatRoom")){
                                            EventBus.getDefault().post(timRoomMessageBean);
                                        } else if(timRoomMessageBean.costomMassageType.equals("joinAvChatRoom")){
                                            EventBus.getDefault().post(timRoomMessageBean);
                                        } else if(timRoomMessageBean.costomMassageType.equals("promptContent")){
                                            return;
                                        }else if(timRoomMessageBean.costomMassageType.equals("watchAvChatRoom")){
                                            TimCustomRoom.WatchAvChatRoom watchAvChatRoom = GsonUtil.GsonToBean(v2TIMMessage.getMessage().getCloudCustomString(), TimCustomRoom.WatchAvChatRoom.class);
                                            TimCustomRoom tb = new TimCustomRoom();
                                            tb.setWatchAvChatRoom(watchAvChatRoom);
                                            EventBus.getDefault().post(tb);
                                        } else if(timRoomMessageBean.costomMassageType.equals("userData")){
                                            //timRoomMessageBean.setName(TextUtil.isEmpty(v2TIMMessage.getNickName()) ? v2TIMMessage.getNickName() : "????????????");
                                            EventBus.getDefault().post(timRoomMessageBean);
                                        }
                                    }*/
                                }
                            }
                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    });
                }
            }
        };
        TUIKit.addIMEventListener(imEventListener);



        
        /**
         * TUIKit??????????????????
         *
         * @param context  ?????????????????????????????????????????????ApplicationContext
         * @param sdkAppID ???????????????????????????????????????sdkAppID
         * @param configs  TUIKit?????????????????????????????????????????????????????????????????????API??????
         */
        TUIKit.init(this, TEST_SDK_APP_ID, TUIKit.getConfigs());
//        TUIKit.init(this, LIVE_SDK_APP_ID, TUIKit.getConfigs());

        //TUIKit.init(this, GenerateTestUserSig.SDKAPPID, new ConfigHelper().getConfigs());
        vibrator = (Vibrator) getBaseContext().getSystemService(VIBRATOR_SERVICE);
        registerCustomListeners();
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //??????????????????????????????,
            builder.setMaxStreams(1);
            //AudioAttributes??????????????????????????????????????????
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //?????????????????????????????????
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            //????????????AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 0);
        }

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                setRingWay();
            }
        });

//        int soundFlag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "soundFlag", -1);
////
////        switch (soundFlag) {
////            case -1:
////            case  0:
////                soundId = soundPool.load(getBaseContext(), R.raw.sound_wechat, 1);
////                 break;
////            case  1:
////                soundId = soundPool.load(getBaseContext(), R.raw.dog, 1);
////                break;
////            case  2:
////                soundId = soundPool.load(getBaseContext(), R.raw.sound_cat, 1);
////                break;
////            case  3:
////                soundId = soundPool.load(getBaseContext(), R.raw.sound_water, 1);
////                break;
////        }

    }

    private static void registerCustomListeners() {
        TUIKitListenerManager.getInstance().addChatListener(new HelloChatController());
        TUIKitListenerManager.getInstance().addConversationListener(new HelloChatController.HelloConversationController());
//        TUIKitListenerManager.getInstance().addChatListener(new GiftChatController());
//        TUIKitListenerManager.getInstance().addConversationListener(new GiftChatController.GiftConversationController());
    }

    /**
     * ???????????????
     *
     * @param groupID
     */
    private void setGroupRingWay(final String groupID) {
        V2TIMManager.getGroupManager().getJoinedGroupList(new V2TIMValueCallback<List<V2TIMGroupInfo>>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(List<V2TIMGroupInfo> v2TIMGroupInfos) {
                for (V2TIMGroupInfo v2TIMGroupInfo :
                        v2TIMGroupInfos) {
                    if (v2TIMGroupInfo.getGroupID().equals(groupID)) {
                        switch (v2TIMGroupInfo.getRecvOpt()) {
                            //case V2TIMGroupInfo.V2TIM_GROUP_RECEIVE_MESSAGE :
                            case V2TIMMessage.V2TIM_RECEIVE_MESSAGE:
                                //setRingWay();
                                chooseSoundBeforeRing();
                                break;
                            default:
                                break;

                        }

                    }
                }

            }
        });

        // addFaceBag();
    }

    int lastSoundId = -2;

    public void chooseSoundBeforeRing() {
        //soundPool.release();
        int soundFlag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "soundFlag", -1);
        if (soundFlag != lastSoundId) { //??????????????????
            switch (soundFlag) {
                case -1:
                case 0:
                    soundId = soundPool.load(getBaseContext(), R.raw.sound_wechat, 1);
                    //setRingWay();
                    break;
                case 1:
                    soundId = soundPool.load(getBaseContext(), R.raw.sound_water, 1);
                    //setRingWay();
                    break;
                case 2:
                    soundId = soundPool.load(getBaseContext(), R.raw.dog, 1);
                    //setRingWay();
                    break;
                case 3:
                    soundId = soundPool.load(getBaseContext(), R.raw.sound_cat, 1);
                    //setRingWay();
                    break;
                case 4:
                    soundId = soundPool.load(getBaseContext(), R.raw.sound_whip, 1);
                    //setRingWay();
                    break;
            }
            lastSoundId = soundId;
        } else {
            setRingWay();
        }

    }


    @SuppressLint("MissingPermission")
    public void setRingWay() {

        int alertflag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "alertflag", 0);
        //alertflag -1.????????? 0.?????? 1.??????
        switch (alertflag) {
            case -1:
                break;
            case 0:
                LogUtil.d("????????????");
                soundPool.play(soundId, 1, 1, 1, 0, 1);
                break;
            case 1:
                vibrator.vibrate(new long[]{500, 100, 500, 100}, -1);
                break;
            default:
                break;
        }
    }

    void suitImageLoader() {
        BGAImage.setImageLoader(new FixedGlideImageLoader());
    }


    void addFaceBag() {
        CustomFaceGroup faceGroup = new CustomFaceGroup();
        final int drawableWidth = 80;
        String[] emojiFilters = this.getResources().getStringArray(com.tencent.qcloud.tim.uikit.R.array.emoji_filter_key);
        String[] emojiFilters_values = this.getResources().getStringArray(com.tencent.qcloud.tim.uikit.R.array.emoji_filter_value);
        CustomFaceConfig config = TUIKit.getConfigs().getCustomFaceConfig();

        faceGroup.setPageRowCount(7);
        faceGroup.setPageColumnCount(7);
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

    /**
     * @param isTi ?????????
     */
    public void logOutBySystem(boolean isTi) {
        clearSharedPreference();
        if (isTi) {
            SharedPreferencesUtils.setParam(getApplicationContext(), "isTi", "1");
        }
        TUIKit.logout(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
        //?????????????????? ???????????????
        EventBus.getDefault().post(new StopLiveEvent());
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        TUIKit.removeIMEventListener(imEventListener);
    }

    void initQiNiuLive() {
        QNRTCEnv.init(getApplicationContext());
        QNRTCEnv.setDnsManager(Utils.getDefaultDnsManager(getApplicationContext()));
        QNRTCEnv.setLogFileEnabled(true);
    }

    static {
        //???????????????Header?????????
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//????????????????????????
                // return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("????????? %s"));//???????????????Header???????????? ???????????????Header
                return new MyRefreshLottieHeader(context);//.setTimeFormat(new DynamicTimeFormat("????????? %s"));//???????????????Header???????????? ???????????????Header

            }
        });

        //???????????????Footer?????????
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                //???????????????Footer???????????? BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    void initGLoading() {
        Gloading.initDefault(new GlobalAdapter());
    }

    void initSVGA() {
        SVGAParser.Companion.shareParser().init(this);
        SVGALogger.INSTANCE.setLogEnabled(true);
        try {
            File cacheDir = new File(MyApp.getInstance().getCacheDir(),"http");
            HttpResponseCache.install(cacheDir,1024 * 1024 * 128);
            SVGACache.INSTANCE.onCreate(this,SVGACache.Type.FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void doer() {

        DisplayManager displayManager = (DisplayManager)getInstance().getSystemService(Context.DISPLAY_SERVICE);
        DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {

            @Override
            public void onDisplayAdded(int displayId) {

            }

            @Override
            public void onDisplayRemoved(int displayId) {

            }

            @Override
            public void onDisplayChanged(int displayId) {
                //Display display = displayManager.getDisplay(displayId);
                LogUtil.d("display changed" + displayId);
            }
        };
        displayManager.registerDisplayListener(mDisplayListener,new Handler());
    }
}