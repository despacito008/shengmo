package com.aiwujie.shengmo.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.binding.BindingEmailActivity;
import com.aiwujie.shengmo.activity.binding.BindingMobileActivity;
import com.aiwujie.shengmo.activity.binding.ChangeBindingEmailActivity;
import com.aiwujie.shengmo.activity.binding.ChangeBindingMobileActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.AppUpdateBean;
import com.aiwujie.shengmo.bean.BindingData;
import com.aiwujie.shengmo.bean.SwitchMarkBean;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.bean.VipSecretSitData;
import com.aiwujie.shengmo.dao.DaoSession;
import com.aiwujie.shengmo.dao.SwitchMarkBeanDao;
import com.aiwujie.shengmo.eventbus.OpenidEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.EditPasswordActivity;
import com.aiwujie.shengmo.kt.ui.activity.FeedBackActivity;
import com.aiwujie.shengmo.kt.ui.activity.HomePageActivity;
import com.aiwujie.shengmo.kt.ui.activity.LiveChatSettingActivity;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AppIsInstallUtils;
import com.aiwujie.shengmo.utils.AppUpdateUtil;
import com.aiwujie.shengmo.utils.FinishActivityManager;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.VersionUtils;
import com.aiwujie.shengmo.wxapi.WXEntryActivity;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class SettingActivity extends AppCompatActivity implements OnItemClickListener, EasyPermissions.PermissionCallbacks {
    Handler handler = new Handler();
    @BindView(R.id.mSetting_return)
    ImageView mSettingReturn;
    @BindView(R.id.mSetting_name)
    TextView mSettingName;
    @BindView(R.id.mSetting_tv_mobile)
    TextView mSettingTvMobile;
    @BindView(R.id.mSetting_ll_Banding_phone)
    LinearLayout mSettingLlBandingPhone;
    @BindView(R.id.mSetting_tv_email)
    TextView mSettingTvEmail;
    @BindView(R.id.mSetting_ll_Banding_email)
    LinearLayout mSettingLlBandingEmail;
    @BindView(R.id.mSetting_tv_sanfang)
    TextView mSettingTvSanfang;
    @BindView(R.id.mSetting_ll_Banding_other)
    LinearLayout mSettingLlBandingOther;
    @BindView(R.id.mSetting_ll_Password)
    LinearLayout mSettingLlPassword;
    @BindView(R.id.mSetting_ll_Guesture)
    LinearLayout mSettingLlGuesture;
    @BindView(R.id.mSetting_ll_Alert)
    LinearLayout mSettingLlAlert;
    @BindView(R.id.et_set_yinsi)
    TextView etSetYinsi;
    @BindView(R.id.mSetting_ll_secret)
    LinearLayout mSettingLlSecret;
    @BindView(R.id.et_set_xiaoxiset)
    TextView etSetXiaoxiset;
    @BindView(R.id.mSetting_ll_Harass_tv)
    TextView mSettingLlHarassTv;
    @BindView(R.id.mSetting_ll_Harass)
    LinearLayout mSettingLlHarass;
    @BindView(R.id.mSetting_ll_one_to_one)
    LinearLayout mSettingLlOneToOne;
    @BindView(R.id.mSetting_ll_common)
    LinearLayout mSettingLlCommon;
    @BindView(R.id.mSetting_ll_Opinion)
    LinearLayout mSettingLlOpinion;
    @BindView(R.id.mSetting_ll_helpCenter)
    LinearLayout mSettingLlHelpCenter;
    @BindView(R.id.mSetting_ll_version_new)
    TextView mSettingLlVersionNew;
    @BindView(R.id.mSetting_tv_version)
    TextView mSettingTvVersion;
    @BindView(R.id.mSetting_ll_version)
    LinearLayout mSettingLlVersion;
    @BindView(R.id.mSetting_btn_qie)
    Button mSettingBtnQie;
    @BindView(R.id.mSetting_btn_exit)
    Button mSettingBtnExit;
    @BindView(R.id.mSetting_ll_Banding_ali)
    LinearLayout mSettingLLAli;
    private String mobile = "";
    private String realmobile;
    private String emails = "";
    private String realemails = "";
    private String openid;
    private String channel;
    private int operationflag;
    private boolean isInstallQq;
    private Tencent mTencent;
    public static final String QQAPP_ID = "1109831381";
    //?????????????????????APPKEY
    public static final String SINA_APPKEY = "1601897348";
    //?????????????????????REDIRECT_URL
    public String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public String SINA_SCOPE = "all";
    /**
     * ?????????SsoHandler ?????? SDK ?????? SSO ?????????
     */
    private SsoHandler mSsoHandler;
    private AuthInfo mAuthInfo;
    private Oauth2AccessToken mAccessToken;
    private UsersAPI mUsersAPI;

    private String phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        // X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        // ((RoundRelativeLayout)findViewById(R.id.round_test)).setRadius(30);

        try {
            mSettingTvVersion.setText("V" + VersionUtils.getVersion(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        isSVIP();

        mSettingLLAli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingActivity.this, SetBankcardActivity.class);
                intent.putExtra("operation","add");
                startActivity(intent);
            }
        });
    }

    public void isSVIP() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetUserPowerInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
//                PrintLogUtils.log(response, TAG);
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            VipAndVolunteerData data = new Gson().fromJson(response, VipAndVolunteerData.class);
                            String svip = data.getData().getSvip();
//                            PrintLogUtils.log(svip, TAG);
                            if (svip.equals("0")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSettingLlHarass.setVisibility(View.GONE);//?????????
                                    }
                                });
//                                mAlert_Setting_vip.setChecked(false);
//                                mAlert_Setting_vip.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        BindSvipDialog.bindAlertDialog(HarassActivity.this, "?????????SVIP??????");
//                                        mAlert_Setting_vip.setChecked(false);
//                                    }
//                                });
                            } else {
//                                mAlert_Setting_vip.setChecked(true);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSettingLlHarass.setVisibility(View.VISIBLE);//??????
                                    }
                                });
                            }
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

    @OnClick({R.id.mSetting_btn_qie, R.id.mSetting_return, R.id.mSetting_ll_Password, R.id.mSetting_ll_secret, R.id.mSetting_ll_common, R.id.mSetting_ll_Opinion, R.id.mSetting_btn_exit, R.id.mSetting_ll_helpCenter, R.id.mSetting_ll_Guesture, R.id.mSetting_ll_Alert, R.id.mSetting_ll_version, R.id.mSetting_ll_Banding_phone,
            R.id.mSetting_ll_Banding_email, R.id.mSetting_ll_Banding_other, R.id.mSetting_ll_Harass,R.id.mSetting_ll_one_to_one})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mSetting_return:
                finish();
                break;
            case R.id.mSetting_ll_Guesture:
                intent = new Intent(this, SettingGustureActivity.class);
                startActivity(intent);
                break;
            case R.id.mSetting_ll_Password:
                if (TextUtil.isEmpty(phone) && TextUtil.isEmpty(email)) {
                    ToastUtil.show(SettingActivity.this, "?????????????????????????????????????????????????????????");
                } else {
//                    intent = new Intent(this, EditPwdActivity.class);
                    intent = new Intent(this, EditPasswordActivity.class);
                    intent.putExtra("passwordStatus", passwordStatus);
                    if (!TextUtil.isEmpty(phone)) {
                        intent.putExtra("phone", phone);
                    }
                    if (!TextUtil.isEmpty(email)) {
                        intent.putExtra("email", email);
                    }
                    startActivity(intent);
                }
                break;
            case R.id.mSetting_ll_secret:
                intent = new Intent(this, SecretActivity.class);
                startActivity(intent);
                break;
            case R.id.mSetting_ll_common:
                intent = new Intent(this, CommonActivity.class);
                startActivity(intent);
                break;
            case R.id.mSetting_ll_Opinion:
//                intent = new Intent(this, FeedbackActivity.class);
                intent = new Intent(this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.mSetting_ll_helpCenter:
                intent = new Intent(this, HelpCenterActivity.class);
                startActivity(intent);
                //Beta.checkUpgrade();
                break;
            case R.id.mSetting_btn_qie:
                //???????????? ????????????
                FloatWindowLayout.getInstance().closeFloatWindow();
                intent = new Intent(this, SwitchActivity.class);
                startActivity(intent);
                break;
//            case R.id.mSetting_ll_kefu:
//                //????????????????????????????????????????????????
//                CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
//                CSCustomServiceInfo csInfo = csBuilder.nickName("????????????").build();
//                RongIM.getInstance().startCustomerServiceChat(this, HttpUrl.SMKF, "????????????", csInfo);
//                break;
            case R.id.mSetting_btn_exit:
                isExit();
                break;
            case R.id.mSetting_ll_Alert:
                intent = new Intent(this, AlertSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.mSetting_ll_version:
//                JudgeVersionUtils.judgeVersion(handler, this, true);
//                initialNotification();
//                download("");
                getUpdate();
                break;
            case R.id.mSetting_ll_Banding_phone:
                if (mobile.equals("")) {
                    intent = new Intent(this, BindingMobileActivity.class);
                    intent.putExtra("neworchange", "new");
                    startActivity(intent);
                } else {
                    intent = new Intent(this, ChangeBindingMobileActivity.class);
                    intent.putExtra("mobile", mobile);
                    intent.putExtra("realmobile", realmobile);
                    startActivity(intent);
                }
                break;
            case R.id.mSetting_ll_Banding_email:
                if (emails.equals("")) {
                    intent = new Intent(this, BindingEmailActivity.class);
                    intent.putExtra("neworchange", "new");
                    startActivity(intent);
                } else {
                    intent = new Intent(this, ChangeBindingEmailActivity.class);
                    intent.putExtra("emails", emails);
                    intent.putExtra("realemails", realemails);
                    startActivity(intent);
                }
                break;
            case R.id.mSetting_ll_Banding_other:
                if (openid.equals("")) {
                    operationflag = 0;
                    bind();
                } else {
                    String jiechuStr = "";
                    switch (channel) {
                        case "1":
                            jiechuStr = "??????????????????";
                            break;
                        case "2":
                            jiechuStr = "??????QQ??????";
                            break;
                        case "3":
                            jiechuStr = "??????????????????";
                            break;
                    }
                    unbind(jiechuStr);
                    operationflag = 1;
                }
                break;

            case R.id.mSetting_ll_Harass:
                intent = new Intent(this, HarassActivity.class);
                startActivity(intent);
                break;
            case R.id.mSetting_ll_one_to_one:
                intent = new Intent(SettingActivity.this, LiveChatSettingActivity.class);
                startActivity(intent);
                break;
        }
    }


    private void bind() {
        new AlertView(null, null, "??????", null,
                new String[]{"????????????", "??????QQ", "????????????"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    private void unbind(String jcstr) {
        new AlertView(null, null, "??????", null,
                new String[]{jcstr},
                this, AlertView.Style.ActionSheet, this).show();
    }

    //  ????????????
    @Override
    public void onItemClick(Object o, int position, String data) {
        if (operationflag == 0) {
            switch (position) {
                case 0:
                    IWXAPI mApi = WXAPIFactory.createWXAPI(this, WXEntryActivity.APP_ID, false);
                    mApi.registerApp(WXEntryActivity.APP_ID);
                    if (mApi != null && mApi.isWXAppInstalled()) {
                        ToastUtil.show(getApplicationContext(), "?????????...");
                        SendAuth.Req req = new SendAuth.Req();
                        req.scope = "snsapi_userinfo";
                        req.state = "wechat_sdk_demo_test";
                        mApi.sendReq(req);
                    } else {
                        ToastUtil.show(getApplicationContext(), "??????????????????");
                    }
                    break;
                case 1:
                    //QQ???????????????
                    isInstallQq = AppIsInstallUtils.isQQClientAvailable(this);
                    if (isInstallQq == true) {
                        ToastUtil.show(getApplicationContext(), "?????????...");
                        mTencent = Tencent.createInstance(QQAPP_ID, getApplicationContext());
                        if (!mTencent.isSessionValid()) {
                            mTencent.login(SettingActivity.this, "all", new BaseUiListener());
                        } else {
                            // ????????????
                            mTencent.logout(getApplicationContext());
                        }
                    } else {
                        ToastUtil.show(getApplicationContext(), "????????????QQ");
                    }
                    break;
                case 2:
                    //  ?????????????????????
                    ToastUtil.show(getApplicationContext(), "?????????...");
                    mAuthInfo = new AuthInfo(SettingActivity.this, SINA_APPKEY, SINA_REDIRECT_URL, SINA_SCOPE);
                    mSsoHandler = new SsoHandler(SettingActivity.this, mAuthInfo);
                    mSsoHandler.authorize(new AuthListener());
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    handler.postDelayed(new Runnable() {
                        public AlertView alertView;

                        @Override
                        public void run() {
                            alertView = new AlertView("??????????????????", "????????????????????????????????????????????????????????????????????????", "??????", new String[]{"????????????"}, null, SettingActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Object o, int position, String data) {
//                                    Log.i("settingposition", "onItemClick: " + position);
                                    switch (position) {
                                        case 0:
                                            //???????????????
                                            unBindOhter();
                                            break;
                                    }
                                }
                            }).setCancelable(true);
                            alertView.show();
                        }
                    }, 500);
                    break;
            }

        }
    }

    private void unBindOhter() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.RemoveOther, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    getBindingState();
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;
                                case 3000:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
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

    private String passwordStatus;

    private void getBindingState() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetBindingState, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("settingbinding", "run: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BindingData data = new Gson().fromJson(response, BindingData.class);
                            BindingData.DataBean datas = data.getData();
                            passwordStatus = data.getData().getPassword_status();
                            phone = data.getData().getMobile();
                            email = data.getData().getEmail();
                            if (!TextUtil.isEmpty(datas.getMobile())) {
                                realmobile = datas.getMobile();
                                mobile = datas.getMobile().substring(0, 3) + "****" + datas.getMobile().substring(7, datas.getMobile().length());
                                mSettingTvMobile.setText(mobile);
                                mSettingTvMobile.setTextColor(Color.GRAY);
                            } else {
                                mSettingTvMobile.setText("?????????");
                                mSettingTvMobile.setTextColor(Color.RED);
                            }

                            if (!TextUtil.isEmpty(datas.getEmail())) {
                                realemails = datas.getEmail();
                                emails = datas.getEmail().replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1******$3$4");
                                mSettingTvEmail.setText(emails);
                                mSettingTvEmail.setTextColor(Color.GRAY);
                            } else {
                                mSettingTvEmail.setText("?????????");
                                mSettingTvEmail.setTextColor(Color.RED);
                            }
                            openid = datas.getOpenid();
                            channel = datas.getChannel();
                            if (!openid.equals("")) {
                                switch (channel) {
                                    case "1":
                                        mSettingTvSanfang.setText("???????????????");
                                        break;
                                    case "2":
                                        mSettingTvSanfang.setText("?????????QQ");
                                        break;
                                    case "3":
                                        mSettingTvSanfang.setText("???????????????");
                                        break;
                                }
                                mSettingTvSanfang.setTextColor(Color.GRAY);
                            } else {
                                mSettingTvSanfang.setText("?????????");
                                mSettingTvSanfang.setTextColor(Color.RED);
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

    private void isExit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("????????????????")
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ????????????
                FloatWindowLayout.getInstance().closeFloatWindow();

                SharedPreferencesUtils.setParam(getApplicationContext(), "uid", "");
                SharedPreferencesUtils.setParam(getApplicationContext(), "modle", "");
                SharedPreferencesUtils.setParam(getApplicationContext(), "token", "");
                SharedPreferencesUtils.setParam(getApplicationContext(), "url_token", "");
                //??????????????????????????????
//                SharedPreferencesUtils.setParam(getApplicationContext(), "filterZong", "0");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "filterShowAge", "");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "filterCulture", "");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "filterShowMoney", "");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "filterSex", "");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "filterQx", "");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "filterRole", "");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "filterLine", "");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpAge", "");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "ckvip", "0");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpCulture", "");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "filterUpMoney", "");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "filterAuthen", "");
                //?????????????????????
//                SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSex", "");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSexual", "");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSwitch", "0");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSxSexual", "0");
                //??????????????????
                SharedPreferencesUtils.setParam(getApplicationContext(), "groupSwitch", "0");
                SharedPreferencesUtils.setParam(getApplicationContext(), "groupFlag", "0");
                //??????VIP??????
                SharedPreferencesUtils.setParam(getApplicationContext(), "vip", "");
                SharedPreferencesUtils.setParam(getApplicationContext(), "vip", "0");
                SharedPreferencesUtils.setParam(getApplicationContext(), "volunteer", "0");
                SharedPreferencesUtils.setParam(getApplicationContext(), "svip", "0");
                SharedPreferencesUtils.setParam(getApplicationContext(), "admin", "0");
                SharedPreferencesUtils.setParam(getApplicationContext(), "realname", "0");
                SharedPreferencesUtils.setParam(getApplicationContext(), "match_state", "0");
                //??????????????????
                SharedPreferencesUtils.setParam(getApplicationContext(), "visitcount", 0);
                //????????????
                SharedPreferencesUtils.clearParam(getApplicationContext(), "follow_tip" + MyApp.uid);


                //  RongIM.getInstance().logout();
//                RongIMClient.getInstance().logout();

//                TUIKit.logout(new IUIKitCallBack() {
//                    @Override
//                    public void onSuccess(Object data) {
//
//                    }
//
//                    @Override
//                    public void onError(String module, int errCode, String errMsg) {
//
//                    }
//                });
                DaoSession daoSession = MyApp.getSwitchMarkBeanDao();
                SwitchMarkBeanDao switchMarkBeanDao = daoSession.getSwitchMarkBeanDao();
                List<SwitchMarkBean> switchMarkBeans = switchMarkBeanDao.loadAll();
                for (int i = 0; i < switchMarkBeans.size(); i++) {
                    SwitchMarkBean switchMarkBean = switchMarkBeans.get(i);
                    if (String.valueOf(switchMarkBean.getUid()).equals(MyApp.uid)) {
                        switchMarkBeanDao.delete(switchMarkBean);
                    }
                }
                //??????????????????
                ConversationManagerKit.getInstance().destroyConversation();
                TUIKit.logout(new IUIKitCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        logOut();
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        logOut();
                    }

                    void logOut() {
                        //FinishActivityManager.getManager().finishActivity(MainActivity.class);
                        FinishActivityManager.getManager().finishActivity(HomePageActivity.class);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

            }
        }).create().show();
    }

    //???????????????openid
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessagee(OpenidEvent event) {
        String wxopenid = event.getOpenid();
        bindingOther(wxopenid, "1");

    }

    /**
     * @param openid  ?????????openid
     * @param channel 1.?????? 2.QQ 3.??????
     */
    private void bindingOther(String openid, String channel) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("openid", openid);
        map.put("channel", channel);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.BindingOther, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
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


    //QQ??????
    private class BaseUiListener implements IUiListener {

        protected void doComplete(JSONObject values) {
//            Log.i("qqlogindocomplete", "doComplete: " + values);
        }

        @Override
        public void onComplete(Object o) {
            doComplete((JSONObject) o);
            try {
                String qqopenid = ((JSONObject) o).getString("openid");
//                Log.i("qqopenid", "onComplete: " + qqopenid);
                bindingOther(qqopenid, "2");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError e) {
            Log.i("QQerror", "onError:" + "code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {
            mTencent = null;
        }
    }

    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle bundle) {

            mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
            if (mAccessToken.isSessionValid()) {//????????????
                //????????????????????????
                getUserInfo();
            } else {
                /**
                 *  ????????????????????????????????? Code???
                 * 1. ???????????????????????????????????????????????????????????????
                 * 2. ?????????????????????????????????????????????????????????
                 * 3. ??????????????????????????????????????????????????????????????????????????????????????????????????????
                 */
                String code = bundle.getString("code");//?????????bundle????????????
                if (!TextUtils.isEmpty(code)) {
                    Toast.makeText(SettingActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(SettingActivity.this, "????????????", Toast.LENGTH_SHORT).show();
            mSsoHandler = null;
        }

        @Override
        public void onCancel() {
            Toast.makeText(SettingActivity.this, "????????????", Toast.LENGTH_SHORT).show();
            mSsoHandler = null;
        }

    }

    /* * ????????????????????????
     */
    private void getUserInfo() {
        //????????????????????????
        mUsersAPI = new UsersAPI(SettingActivity.this, SINA_APPKEY, mAccessToken);
        //????????????
        long uid = Long.parseLong(mAccessToken.getUid());
        mUsersAPI.show(uid, mListener);//???uid?????????listener????????????uid???listener??????????????????????????????json?????????????????????
    }

    /**
     * ????????????????????????????????????????????????????????????User??????
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                //??????response
                Log.i("weibomessage", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String wbid = obj.getString("idstr");
                    Log.i("wbopenid", "onComplete: " + wbid);
                    bindingOther(wbid, "3");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         *?????????????????????????????????????????????????????????????????????????????????????????????
         *????????????-???????????????-???????????????-???????????????-????????????????????????????????????????????????
         * ??????????????????????????????
         */
        @Override
        public void onWeiboException(WeiboException e) {
            e.printStackTrace();
            Toast.makeText(SettingActivity.this, "???????????????????????? ????????????", Toast.LENGTH_SHORT).show();
        }
    };


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mTencent != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
        }
        //SSO ????????????
        //???????????????sso?????????activity????????????onActivtyResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //??????????????????
        getBindingState();
        //??????????????????
        getSecreSit();
    }

    private void getSecreSit() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetVipSecretSit, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            VipSecretSitData vipSecretSitData = new Gson().fromJson(response, VipSecretSitData.class);
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    String char_rule = vipSecretSitData.getData().getChar_rule();
                                    if (char_rule.equals("1")) {  //1????????????
                                        mSettingLlHarassTv.setText("??????/??????/SVIP");
                                    } else {
                                        mSettingLlHarassTv.setText("?????????");
                                    }
                                    break;
                            }


                        } catch (Exception e) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTencent = null;
        mSsoHandler = null;
        mAuthInfo = null;
        EventBus.getDefault().unregister(this);
    }

    String apkUrl = "";

    public void getUpdate() {
        HttpHelper.getInstance().getAppUpdate(new HttpListener() {
            @Override
            public void onSuccess(String data) {
                AppUpdateBean appUpdateBean = GsonUtil.GsonToBean(data, AppUpdateBean.class);
                if (appUpdateBean == null || appUpdateBean.getData() == null) {
                    ToastUtil.show(SettingActivity.this, "????????????????????????");
                    return;
                }
                AppUpdateBean.DataBean appData = appUpdateBean.getData();
                try {
                    String currentVersion = VersionUtils.getVersion(getApplicationContext());
                    if (currentVersion.equals(appData.getUser_version())) {
                        ToastUtil.show(SettingActivity.this, "???????????????????????????");
                    } else {
                        if (TextUtil.isEmpty(appData.getDown_url())) {
                            ToastUtil.show(SettingActivity.this, "??????????????????????????????");
                            return;
                        }
                        apkUrl = appData.getDown_url();
                        showUpdateDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String msg) {
                ToastUtil.show(SettingActivity.this, msg);
            }
        });
    }

    void showUpdateDialog() {
        new AlertDialog.Builder(SettingActivity.this).setMessage("???????????????????????????????????????")
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tryToDownload();
            }
        }).create().show();
    }

    public void tryToDownload() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            startDownLoad();
        } else {
            EasyPermissions.requestPermissions(this, "??????????????????????????????????????????:\n\n1.??????????????????\n\n2.??????????????????", 101, perms);
        }
    }

    public void startDownLoad() {
        AppUpdateUtil.getInstance(SettingActivity.this).startDownLoad(apkUrl);
        ToastUtil.show(SettingActivity.this, "??????????????????????????????????????????????????????????????????");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == 101) {
            if (!perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                return;
            }
            if (!perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return;
            }
            startDownLoad();
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ToastUtil.show(getApplicationContext(), "????????????,?????????????????????");
            return;
        }
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(getApplicationContext(), "????????????,?????????????????????");
            return;
        }
    }


//    private NotificationCompat.Builder builder;
//    private NotificationManager notificationManager;
//
//    /*
//     * ????????????initialNotification()
//     * ???    ??????????????????????????????,??????Notification
//     * ???    ?????????
//     * ???????????????
//     */
//    private void initialNotification() {
//        //Notification????????????
//        Intent notificationIntent = new Intent(this, SettingActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
//        //????????????????????????
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        //8.0???????????????????????????channelId????????????????????????????????????????????????channelName???????????????????????????????????????importance????????????????????????????????????????????????????????????NotificationManager???
//        String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
//        String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
//            if (notificationManager != null) {
//                notificationManager.createNotificationChannel(channel);
//            }
//        }
//
//        //??????Notification
//        builder = new NotificationCompat.Builder(SettingActivity.this);
//        builder.setContentTitle("????????????...") //??????????????????
//                .setContentIntent(contentIntent)
//                .setSmallIcon(R.mipmap.smallapplogo)//????????????????????????(??????????????????Icon????????????????????????????????????Manifest.xml????????????)
//                .setLargeIcon(BitmapFactory.decodeResource(SettingActivity.this.getResources(), R.mipmap.applogo)) //????????????????????????
//                .setDefaults(Notification.DEFAULT_LIGHTS) //?????????????????????????????? ?????????
//                .setPriority(Notification.PRIORITY_MAX) //?????????????????????????????????
//                .setAutoCancel(false)//?????????????????????????????????????????????
//                .setContentText("????????????:0%")
//                .setChannelId(PUSH_CHANNEL_ID)
//                .setProgress(100, 0, false);//????????????100???????????????0??????
//
//        //??????????????????
//        Notification notification = builder.build();
//        notificationManager.notify(1, notification);
//
////        ????????????,???????????????,??????????????????,?????????????????????activity?????????????????????????????????????????????Activity???
////        ??????????????????Manifest.xml????????????activity???android:launchMode??????singleTop???????????????
//
//        ToastUtil.show(SettingActivity.this,"??????????????????????????????");
//    }
//
//    /*
//     * ???????????? download()
//     * ???    ????????????apk???????????????????????????apk
//     * ???    ?????????
//     * ???????????????
//     */
//    private void download() {
//        if (TextUtil.isEmpty(apkUrl)) {
//            ToastUtil.show(SettingActivity.this, "????????????????????????");
//            return;
//        }
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                //.url("https://downpack.baidu.com/appsearch_AndroidPhone_v7.9.3(1.0.64.143)_1012271b.apk")
//                .url(apkUrl)
//                .build();
//        Call call = client.newCall(request);
//
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("TAG-??????", e.toString());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplication(), "?????????????????????", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws FileNotFoundException {
//                Log.e("TAG-????????????", response.code() + "---" + response.body().toString());
//
//                //??????apk?????????????????????
//                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/shengmo.apk");
//
//                //?????????????????????
//                localStorage(response, file);
//            }
//        });
//    }
//
//    /*
//     * ????????????localStorage(final Response response, final File file)
//     * ???    ???????????????????????????
//     * ???    ??????Response response, File file
//     * ???????????????
//     */
//    private void localStorage(final Response response, final File file) throws FileNotFoundException {
//        //???????????????
//        InputStream is = response.body().byteStream();
//        int len = 0;
//        final FileOutputStream fos = new FileOutputStream(file);
//        byte[] buf = new byte[2048];
//        try {
//            while ((len = is.read(buf)) != -1) {
//                fos.write(buf, 0, len);
//                //Log.e("TAG???????????????????????????", "onResponse: "+len);
//                Log.e("TAG????????????????????????", file.length() + "/" + response.body().contentLength());
//
//                //notification???????????????????????????????????????????????????
//                builder.setProgress(100, (int) (file.length() * 100 / response.body().contentLength()), false);
//                builder.setContentText("????????????:" + (int) (file.length() * 100 / response.body().contentLength()) + "%");
//                Notification notification = builder.getNotification();
//                notificationManager.notify(1, notification);
//            }
//            fos.flush();
//            fos.close();
//            is.close();
//
//            //????????????????????????????????????
//            installingAPK(file);
//
//        } catch (IOException e) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplication(), "???????????????", Toast.LENGTH_SHORT).show();
//                }
//            });
//            e.printStackTrace();
//        }
//    }
//
//    /*
//     * ????????????installingAPK(File file)
//     * ???    ??????????????????????????????????????????apk,????????????6.0,7.0,8.0
//     * ???    ??????File file
//     * ???????????????
//     */
//    private void installingAPK(final File file) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        //??????7.0??????????????????Manifest.xml??????application????????????provider??????
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri contentUri = FileProvider.getUriForFile(this, "com.aiwujie.shengmo.fileProvider", new File(file.getPath()));
//            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//        } else {
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        }
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        //????????????????????????notification??????????????????????????????????????????????????????????????????????????????????????????
//        builder.setContentTitle("????????????")
//                .setContentText("????????????")
//                .setAutoCancel(true)//?????????????????????????????????????????????
//                .setContentIntent(contentIntent);
//
//        Notification notification = builder.getNotification();
//        notificationManager.notify(1, notification);
//    }
//
//    private void installApk(File file) {
//        //?????????apk??????????????????
//        File apkFile = file;
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
//        startActivity(intent);
//        notificationManager.cancel(1);//????????????
//
//    }

}
