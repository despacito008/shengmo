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
    //注册成功之后的APPKEY
    public static final String SINA_APPKEY = "1601897348";
    //注册成功之后的REDIRECT_URL
    public String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public String SINA_SCOPE = "all";
    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
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
                                        mSettingLlHarass.setVisibility(View.GONE);//不可见
                                    }
                                });
//                                mAlert_Setting_vip.setChecked(false);
//                                mAlert_Setting_vip.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        BindSvipDialog.bindAlertDialog(HarassActivity.this, "功能限SVIP可用");
//                                        mAlert_Setting_vip.setChecked(false);
//                                    }
//                                });
                            } else {
//                                mAlert_Setting_vip.setChecked(true);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSettingLlHarass.setVisibility(View.VISIBLE);//可见
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
                    ToastUtil.show(SettingActivity.this, "绑定手机号或者邮箱之后，才可以设置密码");
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
                //切换账号 关闭浮窗
                FloatWindowLayout.getInstance().closeFloatWindow();
                intent = new Intent(this, SwitchActivity.class);
                startActivity(intent);
                break;
//            case R.id.mSetting_ll_kefu:
//                //首先需要构造使用客服者的用户信息
//                CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
//                CSCustomServiceInfo csInfo = csBuilder.nickName("圣魔客服").build();
//                RongIM.getInstance().startCustomerServiceChat(this, HttpUrl.SMKF, "在线客服", csInfo);
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
                            jiechuStr = "解除微信绑定";
                            break;
                        case "2":
                            jiechuStr = "解除QQ绑定";
                            break;
                        case "3":
                            jiechuStr = "解除微博绑定";
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
        new AlertView(null, null, "取消", null,
                new String[]{"绑定微信", "绑定QQ", "绑定微博"},
                this, AlertView.Style.ActionSheet, this).show();
    }

    private void unbind(String jcstr) {
        new AlertView(null, null, "取消", null,
                new String[]{jcstr},
                this, AlertView.Style.ActionSheet, this).show();
    }

    //  绑定监听
    @Override
    public void onItemClick(Object o, int position, String data) {
        if (operationflag == 0) {
            switch (position) {
                case 0:
                    IWXAPI mApi = WXAPIFactory.createWXAPI(this, WXEntryActivity.APP_ID, false);
                    mApi.registerApp(WXEntryActivity.APP_ID);
                    if (mApi != null && mApi.isWXAppInstalled()) {
                        ToastUtil.show(getApplicationContext(), "请稍后...");
                        SendAuth.Req req = new SendAuth.Req();
                        req.scope = "snsapi_userinfo";
                        req.state = "wechat_sdk_demo_test";
                        mApi.sendReq(req);
                    } else {
                        ToastUtil.show(getApplicationContext(), "您未安装微信");
                    }
                    break;
                case 1:
                    //QQ登录初始化
                    isInstallQq = AppIsInstallUtils.isQQClientAvailable(this);
                    if (isInstallQq == true) {
                        ToastUtil.show(getApplicationContext(), "请稍后...");
                        mTencent = Tencent.createInstance(QQAPP_ID, getApplicationContext());
                        if (!mTencent.isSessionValid()) {
                            mTencent.login(SettingActivity.this, "all", new BaseUiListener());
                        } else {
                            // 注销登录
                            mTencent.logout(getApplicationContext());
                        }
                    } else {
                        ToastUtil.show(getApplicationContext(), "您未安装QQ");
                    }
                    break;
                case 2:
                    //  微博登录初始化
                    ToastUtil.show(getApplicationContext(), "请稍后...");
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
                            alertView = new AlertView("解除账号绑定", "解除账号绑定后，你将不能再通过该绑定账号进行登录", "取消", new String[]{"解除绑定"}, null, SettingActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Object o, int position, String data) {
//                                    Log.i("settingposition", "onItemClick: " + position);
                                    switch (position) {
                                        case 0:
                                            //解绑第三方
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
                                mSettingTvMobile.setText("未绑定");
                                mSettingTvMobile.setTextColor(Color.RED);
                            }

                            if (!TextUtil.isEmpty(datas.getEmail())) {
                                realemails = datas.getEmail();
                                emails = datas.getEmail().replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1******$3$4");
                                mSettingTvEmail.setText(emails);
                                mSettingTvEmail.setTextColor(Color.GRAY);
                            } else {
                                mSettingTvEmail.setText("未绑定");
                                mSettingTvEmail.setTextColor(Color.RED);
                            }
                            openid = datas.getOpenid();
                            channel = datas.getChannel();
                            if (!openid.equals("")) {
                                switch (channel) {
                                    case "1":
                                        mSettingTvSanfang.setText("已绑定微信");
                                        break;
                                    case "2":
                                        mSettingTvSanfang.setText("已绑定QQ");
                                        break;
                                    case "3":
                                        mSettingTvSanfang.setText("已绑定微博");
                                        break;
                                }
                                mSettingTvSanfang.setTextColor(Color.GRAY);
                            } else {
                                mSettingTvSanfang.setText("未绑定");
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
        builder.setMessage("确认退出吗?")
                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 关闭浮窗
                FloatWindowLayout.getInstance().closeFloatWindow();

                SharedPreferencesUtils.setParam(getApplicationContext(), "uid", "");
                SharedPreferencesUtils.setParam(getApplicationContext(), "modle", "");
                SharedPreferencesUtils.setParam(getApplicationContext(), "token", "");
                SharedPreferencesUtils.setParam(getApplicationContext(), "url_token", "");
                //清除所有首页附近筛选
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
                //清除动态性取向
//                SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSex", "");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSexual", "");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSwitch", "0");
//                SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSxSexual", "0");
                //清除群组筛选
                SharedPreferencesUtils.setParam(getApplicationContext(), "groupSwitch", "0");
                SharedPreferencesUtils.setParam(getApplicationContext(), "groupFlag", "0");
                //清除VIP状态
                SharedPreferencesUtils.setParam(getApplicationContext(), "vip", "");
                SharedPreferencesUtils.setParam(getApplicationContext(), "vip", "0");
                SharedPreferencesUtils.setParam(getApplicationContext(), "volunteer", "0");
                SharedPreferencesUtils.setParam(getApplicationContext(), "svip", "0");
                SharedPreferencesUtils.setParam(getApplicationContext(), "admin", "0");
                SharedPreferencesUtils.setParam(getApplicationContext(), "realname", "0");
                SharedPreferencesUtils.setParam(getApplicationContext(), "match_state", "0");
                //清除访问数字
                SharedPreferencesUtils.setParam(getApplicationContext(), "visitcount", 0);
                //清除关注
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
                //清空会话列表
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

    //接收微信的openid
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessagee(OpenidEvent event) {
        String wxopenid = event.getOpenid();
        bindingOther(wxopenid, "1");

    }

    /**
     * @param openid  三方的openid
     * @param channel 1.微信 2.QQ 3.微博
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


    //QQ回调
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
            if (mAccessToken.isSessionValid()) {//授权成功
                //获取用户具体信息
                getUserInfo();
            } else {
                /**
                 *  以下几种情况，您会收到 Code：
                 * 1. 当您未在平台上注册应用程序的包名与签名时；
                 * 2. 当您注册的应用程序包名与签名不正确时；
                 * 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                 */
                String code = bundle.getString("code");//直接从bundle里边获取
                if (!TextUtils.isEmpty(code)) {
                    Toast.makeText(SettingActivity.this, "签名不正确", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(SettingActivity.this, "授权异常", Toast.LENGTH_SHORT).show();
            mSsoHandler = null;
        }

        @Override
        public void onCancel() {
            Toast.makeText(SettingActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
            mSsoHandler = null;
        }

    }

    /* * 获取用户个人信息
     */
    private void getUserInfo() {
        //获取用户信息接口
        mUsersAPI = new UsersAPI(SettingActivity.this, SINA_APPKEY, mAccessToken);
        //调用接口
        long uid = Long.parseLong(mAccessToken.getUid());
        mUsersAPI.show(uid, mListener);//将uid传递到listener中，通过uid在listener回调中接收到该用户的json格式的个人信息
    }

    /**
     * 实现异步请求接口回调，并在回调中直接解析User信息
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                //解析response
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
         *如果运行测试的时候，登录的账号不是注册应用的账号，那么需要去：
         *开放平台-》管理中心-》应用信息-》测试信息-》添加测试账号（填写用户昵称）！
         * 否则便会抛出以下异常
         */
        @Override
        public void onWeiboException(WeiboException e) {
            e.printStackTrace();
            Toast.makeText(SettingActivity.this, "获取用户个人信息 出现异常", Toast.LENGTH_SHORT).show();
        }
    };


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mTencent != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
        }
        //SSO 授权回调
        //重要：发起sso登录的activity必须重写onActivtyResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取绑定状态
        getBindingState();
        //获取消息设置
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
                                    if (char_rule.equals("1")) {  //1是被限制
                                        mSettingLlHarassTv.setText("好友/邮票/SVIP");
                                    } else {
                                        mSettingLlHarassTv.setText("所有人");
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
                    ToastUtil.show(SettingActivity.this, "未获取到最新版本");
                    return;
                }
                AppUpdateBean.DataBean appData = appUpdateBean.getData();
                try {
                    String currentVersion = VersionUtils.getVersion(getApplicationContext());
                    if (currentVersion.equals(appData.getUser_version())) {
                        ToastUtil.show(SettingActivity.this, "当前已经是最新版本");
                    } else {
                        if (TextUtil.isEmpty(appData.getDown_url())) {
                            ToastUtil.show(SettingActivity.this, "未获取到最新的安装包");
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
        new AlertDialog.Builder(SettingActivity.this).setMessage("发现了新的版本，是否更新？")
                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("是", new DialogInterface.OnClickListener() {
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
            EasyPermissions.requestPermissions(this, "下载最新版本需要使用以下权限:\n\n1.手机读取权限\n\n2.手机下载权限", 101, perms);
        }
    }

    public void startDownLoad() {
        AppUpdateUtil.getInstance(SettingActivity.this).startDownLoad(apkUrl);
        ToastUtil.show(SettingActivity.this, "开始进行后台下载任务，请查看通知栏的下载进度");
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
            ToastUtil.show(getApplicationContext(), "授权失败,请开启读取权限");
            return;
        }
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ToastUtil.show(getApplicationContext(), "授权失败,请开启读写权限");
            return;
        }
    }


//    private NotificationCompat.Builder builder;
//    private NotificationManager notificationManager;
//
//    /*
//     * 方法名：initialNotification()
//     * 功    能：初始化通知管理器,创建Notification
//     * 参    数：无
//     * 返回值：无
//     */
//    private void initialNotification() {
//        //Notification跳转页面
//        Intent notificationIntent = new Intent(this, SettingActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
//        //初始化通知管理器
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        //8.0及以上需要设置好“channelId”（没有特殊要求、唯一即可）、“channelName”（用户看得到的信息）、“importance”（重要等级）这三个重要参数，然后创建到NotificationManager。
//        String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
//        String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
//            if (notificationManager != null) {
//                notificationManager.createNotificationChannel(channel);
//            }
//        }
//
//        //创建Notification
//        builder = new NotificationCompat.Builder(SettingActivity.this);
//        builder.setContentTitle("正在更新...") //设置通知标题
//                .setContentIntent(contentIntent)
//                .setSmallIcon(R.mipmap.smallapplogo)//设置通知的小图标(有些手机设置Icon图标不管用，默认图标就是Manifest.xml里的图标)
//                .setLargeIcon(BitmapFactory.decodeResource(SettingActivity.this.getResources(), R.mipmap.applogo)) //设置通知的大图标
//                .setDefaults(Notification.DEFAULT_LIGHTS) //设置通知的提醒方式： 呼吸灯
//                .setPriority(Notification.PRIORITY_MAX) //设置通知的优先级：最大
//                .setAutoCancel(false)//设置通知被点击一次是否自动取消
//                .setContentText("下载进度:0%")
//                .setChannelId(PUSH_CHANNEL_ID)
//                .setProgress(100, 0, false);//进度最大100，默认是从0开始
//
//        //构建通知对象
//        Notification notification = builder.build();
//        notificationManager.notify(1, notification);
//
////        问题来了,默认情况下,点击了通知栏,会弹出一个新的activity实例，也就是说会重复打开同一个Activity。
////        解决办法是在Manifest.xml里面把此activity的android:launchMode设为singleTop就搞定了。
//
//        ToastUtil.show(SettingActivity.this,"正在后台下载最新版本");
//    }
//
//    /*
//     * 方法名： download()
//     * 功    能：下载apk，保存到本地，安装apk
//     * 参    数：无
//     * 返回值：无
//     */
//    private void download() {
//        if (TextUtil.isEmpty(apkUrl)) {
//            ToastUtil.show(SettingActivity.this, "下载路径不能为空");
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
//                Log.e("TAG-失败", e.toString());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplication(), "网络请求失败！", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws FileNotFoundException {
//                Log.e("TAG-下载成功", response.code() + "---" + response.body().toString());
//
//                //设置apk存储路径和名称
//                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/shengmo.apk");
//
//                //保存文件到本地
//                localStorage(response, file);
//            }
//        });
//    }
//
//    /*
//     * 方法名：localStorage(final Response response, final File file)
//     * 功    能：保存文件到本地
//     * 参    数：Response response, File file
//     * 返回值：无
//     */
//    private void localStorage(final Response response, final File file) throws FileNotFoundException {
//        //拿到字节流
//        InputStream is = response.body().byteStream();
//        int len = 0;
//        final FileOutputStream fos = new FileOutputStream(file);
//        byte[] buf = new byte[2048];
//        try {
//            while ((len = is.read(buf)) != -1) {
//                fos.write(buf, 0, len);
//                //Log.e("TAG每次写入到文件大小", "onResponse: "+len);
//                Log.e("TAG保存到文件进度：", file.length() + "/" + response.body().contentLength());
//
//                //notification进度条和显示内容不断变化，并刷新。
//                builder.setProgress(100, (int) (file.length() * 100 / response.body().contentLength()), false);
//                builder.setContentText("下载进度:" + (int) (file.length() * 100 / response.body().contentLength()) + "%");
//                Notification notification = builder.getNotification();
//                notificationManager.notify(1, notification);
//            }
//            fos.flush();
//            fos.close();
//            is.close();
//
//            //下载完成，点击通知，安装
//            installingAPK(file);
//
//        } catch (IOException e) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplication(), "下载失败！", Toast.LENGTH_SHORT).show();
//                }
//            });
//            e.printStackTrace();
//        }
//    }
//
//    /*
//     * 方法名：installingAPK(File file)
//     * 功    能：下载完成，点击通知，安装apk,适配安卓6.0,7.0,8.0
//     * 参    数：File file
//     * 返回值：无
//     */
//    private void installingAPK(final File file) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        //安卓7.0以上需要在在Manifest.xml里的application里，设置provider路径
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
//        //下载完成后，设置notification为点击一次就关闭，并设置完成标题内容。并设置跳转到安装页面。
//        builder.setContentTitle("下载完成")
//                .setContentText("点击安装")
//                .setAutoCancel(true)//设置通知被点击一次是否自动取消
//                .setContentIntent(contentIntent);
//
//        Notification notification = builder.getNotification();
//        notificationManager.notify(1, notification);
//    }
//
//    private void installApk(File file) {
//        //新下载apk文件存储地址
//        File apkFile = file;
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
//        startActivity(intent);
//        notificationManager.cancel(1);//取消通知
//
//    }

}
