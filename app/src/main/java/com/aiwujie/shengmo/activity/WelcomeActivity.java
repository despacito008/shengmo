package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BaseUrlBean;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.util.SpKey;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GetDeviceIdUtils;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.VersionUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.aiwujie.shengmo.utils.emulatorcheck.EmulatorCheckCallback;
import com.aiwujie.shengmo.utils.emulatorcheck.EmulatorCheckUtil;
import com.aiwujie.shengmo.view.VerifyAccountPop;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXVodPlayer;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {
    @BindView(R.id.mWelcom_helloIv)
    ImageView mWelcomHelloIv;
    //判断是否有广告
    private boolean isHaveBanner = false;
    private String picPath;
    private String url;
    private String title;
    private String gusture;
    Handler handler = new MyHandler(this);
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    if (gusture.equals("0") || gusture.equals("")) {
//                        Intent intent;
////                        Log.i("GetSlideStartup", "handleMessage: "+isHaveBanner);
//                        if (isHaveBanner) {
//                            intent = new Intent(WelcomeActivity.this, WelcomBannerActivity.class);
//                            intent.putExtra("path", picPath);
//                            intent.putExtra("url", url);
//                            intent.putExtra("title", title);
//                        } else {
//                            intent = new Intent(WelcomeActivity.this, LoginActivity.class);
//                        }
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Intent intent = new Intent(WelcomeActivity.this, Lock9ViewActivity.class);
//                        intent.putExtra("gustureFlag", "input");
//                        startActivity(intent);
//                        finish();
//                    }
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };

    private static class MyHandler extends Handler {
        private WeakReference<WelcomeActivity> activityWeakReference;

        public MyHandler(WelcomeActivity welcomeActivity) {
            activityWeakReference = new WeakReference<>(welcomeActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WelcomeActivity activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {
                case 0:
                    if (activity.gusture.equals("0") || activity.gusture.equals("")) {
                        Intent intent;
                        if (activity.isHaveBanner) {
                            intent = new Intent(activity, WelcomBannerActivity.class);
                            intent.putExtra("path", activity.picPath);
                            intent.putExtra("url", activity.url);
                            intent.putExtra("title", activity.title);
                        } else {
                            intent = new Intent(activity, LoginActivity.class);
                        }
                        activity.startActivity(intent);
                        activity.finish();
                    } else {
                        Intent intent = new Intent(activity, Lock9ViewActivity.class);
                        intent.putExtra("gustureFlag", "input");
                        activity.startActivity(intent);
                        activity.finish();
                    }
                    break;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        ImmersionBar.with(this).transparentBar().init();
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        gusture = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "gusture", "");
        if (EmulatorCheckUtil.getSingleInstance().readSysProperty(WelcomeActivity.this, new EmulatorCheckCallback() {
            @Override
            public void findEmulator(String emulatorInfo) {
               // LogUtil.d("模拟器吗？ " + emulatorInfo.toString());
            }
        })) {
           // LogUtil.d("模拟器设备登录");
        }
        if (GetDeviceIdUtils.isAdopt(WelcomeActivity.this)) {
           // LogUtil.d("模拟器设备登录");
           // finish();
        }
        //启动页后是否有广告
       //getSlideStartup();
//        if (VersionUtils.isApkInDebug(MyApp.getInstance())) {
//            //getSlideStartup();
//            getBaseUrl();
//        } else {
//            getBaseUrl();
//        }
        //    getBaseUrl();
        getPreUrl();
    }

    private void getSlideStartup() {
        Map<String, String> map = new HashMap<>();
        map.put("state", "5");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetSlideStartup, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            JSONObject object = obj.getJSONObject("data");
                            isHaveBanner = true;
                            picPath = object.getString("path");
                            url = object.getString("url");
                            title = object.getString("title");
                            break;
                        case 4000:
                            isHaveBanner = false;
                            break;
                        case 50001:
                        case 50002:
                            EventBus.getDefault().post(new TokenFailureEvent());
                            break;
                    }
                    handler.sendEmptyMessageDelayed(0, 2000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("failure",throwable.getMessage());
            }
        });
    }

    private void getPreUrl() {
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.get(HttpUrl.basePreUrl, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                if (TextUtil.isEmpty(response)) {
                    getBaseUrl();
                    return;
                }
                BaseUrlBean baseUrlBean = GsonUtil.GsonToBean(response,BaseUrlBean.class);
                if (baseUrlBean == null || TextUtil.isEmpty(baseUrlBean.getApi_host())) {
                    getBaseUrl();
                    return;
                }
                String baseUrl = baseUrlBean.getApi_host();
                String imageUrl = baseUrlBean.getImage_host();
                String testUrl = baseUrlBean.getTest_api_host();
                if(!TextUtil.isEmpty(imageUrl) && !imageUrl.equals(SharedPreferencesUtils.getParam(WelcomeActivity.this, SpKey.IMAGE_HOST,""))) {
                    SharedPreferencesUtils.setParam(WelcomeActivity.this, SpKey.IMAGE_HOST,imageUrl);
                }

                String pullHost = baseUrlBean.getPull_host();
                if (!TextUtil.isEmpty(pullHost) && !pullHost.equals(SharedPreferencesUtils.getParam(WelcomeActivity.this,SpKey.PULL_HOST,""))) {
                    SharedPreferencesUtils.setParam(WelcomeActivity.this,SpKey.PULL_HOST,pullHost);
                }

               // if (VersionUtils.isApkInDebug(getApplicationContext()) && false) {
                if (VersionUtils.isApkInDebug(getApplicationContext()) ) {
                    if(!TextUtil.isEmpty(testUrl) && !testUrl.equals(SharedPreferencesUtils.getParam(WelcomeActivity.this,SpKey.API_HOST,""))) {
                        SharedPreferencesUtils.setParam(WelcomeActivity.this,SpKey.API_HOST,testUrl);
                    }
                    HttpUrl.baseUrl = testUrl;
                } else {
                    if(!TextUtil.isEmpty(baseUrl) && !baseUrl.equals(SharedPreferencesUtils.getParam(WelcomeActivity.this,SpKey.API_HOST,""))) {
                        SharedPreferencesUtils.setParam(WelcomeActivity.this,SpKey.API_HOST,baseUrl);
                    }
                    HttpUrl.baseUrl = baseUrl;
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSlideStartup();
                    }
                },100);
            }

            @Override
            public void onFailure(Throwable throwable) {
                getBaseUrl();
            }
        });
    }



    private void getBaseUrl() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "5");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetBaseUrl, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            JSONObject data = obj.getJSONObject("data");
                            String baseUrl = data.getString(SpKey.API_HOST);
                            String imageUrl = data.getString(SpKey.IMAGE_HOST);
                            if(!TextUtil.isEmpty(imageUrl) && !imageUrl.equals(SharedPreferencesUtils.getParam(WelcomeActivity.this,"image_host",""))) {
                                SharedPreferencesUtils.setParam(WelcomeActivity.this,SpKey.IMAGE_HOST,imageUrl);
                            }
                            if(!TextUtil.isEmpty(baseUrl) && !baseUrl.equals(SharedPreferencesUtils.getParam(WelcomeActivity.this,"api_host",""))) {
                                SharedPreferencesUtils.setParam(WelcomeActivity.this,SpKey.API_HOST,baseUrl);
                            }
                            HttpUrl.baseUrl = baseUrl;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getSlideStartup();
                                }
                            },100);
                            break;
                        default:
                            getSlideStartup();
                            break;
                    }
                } catch (JSONException e) {
                    getSlideStartup();
                    e.printStackTrace();
                    getBaseUrl();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                //getBaseUrl();
              //  ToastUtil.show(WelcomeActivity.this,throwable.getMessage());
                HttpUrl.baseUrl = SharedPreferencesUtils.geParam(WelcomeActivity.this,"api_host","");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSlideStartup();
                    }
                },100);
            }
        });
    }

    void initH5Parameter() {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            //获取h5页面传递的参数
            String routeId = uri.getQueryParameter("pid");
            //根据参数做跳转处理
        }
    }
}
