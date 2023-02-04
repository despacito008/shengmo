package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.LoginResultBean;
import com.aiwujie.shengmo.bean.SwitchMarkBean;
import com.aiwujie.shengmo.bean.ThirdLoginResultBean;
import com.aiwujie.shengmo.dao.DaoSession;
import com.aiwujie.shengmo.dao.SwitchMarkBeanDao;
import com.aiwujie.shengmo.eventbus.SessionEvent;
import com.aiwujie.shengmo.eventbus.SessionEvent2;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.HomePageActivity;
import com.aiwujie.shengmo.net.HttpByteListener;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AppIsInstallUtils;
import com.aiwujie.shengmo.utils.GetDeviceIdUtils;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.SystemUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.VersionUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.aiwujie.shengmo.utils.emulatorcheck.EmulatorCheckCallback;
import com.aiwujie.shengmo.utils.emulatorcheck.EmulatorCheckUtil;
import com.aiwujie.shengmo.view.VerifyAccountPop;
import com.aiwujie.shengmo.wxapi.WXEntryActivity;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.tencent.bugly.beta.Beta;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.liteav.custom.Constents;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.item_title_name)
    TextView itemTitleName;
    //    @BindView(R.id.mLogin_regist)
//    TextView mLoginRegist;
    @BindView(R.id.mLogin_username)
    EditText mLoginUsername;
    @BindView(R.id.mLogin_password)
    EditText mLoginPassword;
    @BindView(R.id.mLogin_forPsw)
    TextView mLoginForPsw;
    @BindView(R.id.mLogin_btn_login)
    Button mLoginBtnLogin;
    @BindView(R.id.mLogin_iv_qq)
    ImageView mLoginIvQq;
    @BindView(R.id.mLogin_iv_wx)
    ImageView mLoginIvWx;
    @BindView(R.id.mLogin_iv_wb)
    ImageView mLoginIvWb;
    @BindView(R.id.mLogin_help)
    TextView mLoginHelp;
    @BindView(R.id.mLogin_phoneRegist)
    TextView mLoginPhoneRegist;
    @BindView(R.id.mLogin_emailRegist)
    TextView mLoginEmailRegist;
    @BindView(R.id.mLogin_clearUsername)
    ImageView mLoginClearUsername;
    @BindView(R.id.login_ll)
    PercentLinearLayout login_ll;
    @BindView(R.id.kuai_ll)
    PercentLinearLayout kuai_ll;
    private JSONObject loginObj;
    private JSONObject basicObj;
    private JSONObject locationObj;
    private JSONObject deviceObj;
    private IWXAPI mApi;
    private Tencent mTencent;
    private boolean isInstallQq;
    private String openidString;
    private String expiresString;
    private String tokenString;
    private String nicknameString;
    private String headurlString;
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
    public String SN;
    private String isEmulator = "0";
    private String emulatorInfoStr = "";
    private String user_id;
    private String password;
    private VerifyAccountPop verifyAccountPop;
    MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<LoginActivity> activityWeakReference;

        public MyHandler(LoginActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
            //Build.getSerial()
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoginActivity loginActivity = activityWeakReference.get();
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                try {
                    loginActivity.nicknameString = response.getString("nickname");
                    loginActivity.headurlString = response.getString("figureurl_qq_2");
                    //qq登录
                    loginActivity.QqGologinfirst(loginActivity.openidString, loginActivity.nicknameString, loginActivity.headurlString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.show(e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        EventBus.getDefault().register(this);
        if (Constents.isShowAudienceFloatWindow) {
            FloatWindowLayout.getInstance().closeFloatWindow();
        }
        setData();
        // getthirdParty();
        getEmulatorInfo();
    }

    void getEmulatorInfo() {
        isEmulator = EmulatorCheckUtil.getSingleInstance().readSysProperty(LoginActivity.this, new EmulatorCheckCallback() {
            @Override
            public void findEmulator(String emulatorInfo) {
                emulatorInfoStr = emulatorInfo;
                //LogUtil.d("info = " + emulatorInfoStr);
            }
        }) ? "1" : "0";
        //LogUtil.d("isEmulator = " + isEmulator);

        // boolean isAopt = GetDeviceIdUtils.isAdopt(LoginActivity.this);
    }

    private void setData() {
        SN = GetDeviceIdUtils.getSN(this);
        mLoginPhoneRegist.setText(Html.fromHtml(mLoginPhoneRegist.getText() + ""));
        String uid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "uid", "");
        String token = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "token", "");
        String isTi = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "isTi", "");
        String username = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "username", "");

        if (!username.equals("")) {
            mLoginUsername.setText(username);
            mLoginUsername.setSelection(mLoginUsername.getText().length());
        }
        if (isTi.equals("1")) {
            ToastUtil.show(getApplicationContext(), "您的聊天服务器已断开，请重新登录");
            SharedPreferencesUtils.setParam(getApplicationContext(), "isTi", "0");
        }
        if (!uid.equals("")) {
            MyApp.uid = uid;
            MyApp.token = token;
            //Intent intent = new Intent(this, MainActivity.class);
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
            finish();
        } else {
            loginObj = new JSONObject();
            basicObj = new JSONObject();
            locationObj = new JSONObject();
            deviceObj = new JSONObject();
        }
        mApi = WXAPIFactory.createWXAPI(this, WXEntryActivity.APP_ID, false);
        mApi.registerApp(WXEntryActivity.APP_ID);
        //QQ登录初始化
        isInstallQq = AppIsInstallUtils.isQQClientAvailable(this);
        //微博登录初始化
        mAuthInfo = new AuthInfo(LoginActivity.this, MyApp.SINA_APPKEY, SINA_REDIRECT_URL, SINA_SCOPE);
    }

    @OnClick({R.id.mLogin_clearUsername, R.id.mLogin_phoneRegist, R.id.mLogin_emailRegist, R.id.mLogin_forPsw, R.id.mLogin_btn_login, R.id.mLogin_iv_qq, R.id.mLogin_iv_wx, R.id.mLogin_iv_wb, R.id.mLogin_help})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mLogin_forPsw:
                intent = new Intent(this, RetrievePswActivity.class);
                startActivity(intent);
                break;
            case R.id.mLogin_btn_login:
                mLoginBtnLogin.setEnabled(false);
                ToastUtil.show(getApplicationContext(), "登录中,请稍后...");
                login();
                break;
            case R.id.mLogin_iv_qq:
                isInstallQq = true;
                if (isInstallQq == true) {
                    ToastUtil.show(getApplicationContext(), "QQ登录中,请稍后...");
                    mTencent = Tencent.createInstance(MyApp.QQAPP_ID, getApplicationContext());
                    QQlogin();
                } else {
                    ToastUtil.show(getApplicationContext(), "您未安装QQ");
                }
                break;
            case R.id.mLogin_iv_wx:
                MyApp.wxloginFlag = 1;
                if (mApi != null && mApi.isWXAppInstalled()) {
                    ToastUtil.show(getApplicationContext(), "微信登录中,请稍后...");
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_demo_test";
                    mApi.sendReq(req);
                } else {
                    ToastUtil.show(getApplicationContext(), "您未安装微信");
                }
                break;
            case R.id.mLogin_iv_wb:
                ToastUtil.show(getApplicationContext(), "微博登录中,请稍后...");
                mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
                mSsoHandler.authorize(new AuthListener());
                break;
            case R.id.mLogin_help:
                intent = new Intent(this, HelpCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.mLogin_phoneRegist:
                intent = new Intent(this, RegistPhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.mLogin_emailRegist:
                intent = new Intent(this, RegistEmailActivity.class);
                startActivity(intent);
                break;
            case R.id.mLogin_clearUsername:
                if (!mLoginUsername.getText().equals("")) {
                    mLoginUsername.setText("");
                }
                break;
        }
    }

    //微博回调

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle bundle) {

            mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
            if (mAccessToken.isSessionValid()) {//授权成功
                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                //Access_Token
//                String access_token = mAccessToken.getToken();
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
                    Toast.makeText(LoginActivity.this, "签名不正确", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this, "授权异常", Toast.LENGTH_SHORT).show();
            mSsoHandler = null;
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
            mSsoHandler = null;
        }

    }

    /* * 获取用户个人信息
     */
    private void getUserInfo() {
        //获取用户信息接口
        mUsersAPI = new UsersAPI(LoginActivity.this, MyApp.SINA_APPKEY, mAccessToken);
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
//                Log.i("weibomessage", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String wbid = obj.getString("idstr");
                    String wbname = obj.getString("name");// 姓名
                    String wbheadpic = obj.getString("profile_image_url");// 头像
                    WbLogin(wbid, wbname, wbheadpic);
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
            Toast.makeText(LoginActivity.this, "获取用户个人信息 出现异常", Toast.LENGTH_SHORT).show();
        }
    };

    private void WbLogin(final String wbid, final String wbname, final String wbheadpic) {
//        Map<String, String> map = new HashMap<>();
//        map.put("openid", wbid);
//        map.put("channel", "3");
//        map.put("device_token", SN);
//        map.put("new_device_brand", SystemUtil.getDeviceBrand() + "-" + SystemUtil.getSystemModel());
//        map.put("new_device_version", SystemUtil.getSystemVersion());
//        try {
//            map.put("new_device_appversion", VersionUtils.getVersion(getApplicationContext()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        IRequestManager manager = RequestFactory.getRequestManager();
//        manager.post(HttpUrl.ChargeOpenid, map, new IRequestCallback() {
//            @Override
//            public void onSuccess(final String response) {
//                Log.i("WbloginMsg", "onSuccess: " + response);
//
//                        try {
//                            Intent intent;
//                            JSONObject obj = new JSONObject(response);
//                            switch (obj.getInt("retcode")) {
//                                case 2000:
//                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
//                                    JSONObject obj1 = obj.getJSONObject("data");
//                                    MyApp.uid = obj1.getString("uid");
//                                    MyApp.token = obj1.getString("t_sign");
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "t_sign", MyApp.token);
//                                    String url_token = obj1.getString("token");
//                                    String sex = obj1.getString("sex");
//                                    String sexual = obj1.getString("sexual");
//                                    String head_pic = obj1.getString("head_pic");
//                                    String nickname = obj1.getString("nickname");
//                                    //禁言状态
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "nospeak", obj1.getString("chatstatus"));
//                                    //筛选
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mysex", sex);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mysexual", sexual);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSex", sexual);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSexual", sex);
//                                    /*群组筛选*/
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSex", sex);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSexual", sexual);
//
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "uid", MyApp.uid);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "token", MyApp.token);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "url_token", url_token);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "username", "");
//                                    //intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    intent = new Intent(LoginActivity.this, HomePageActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(intent);
//                                    DaoSession daoSession = MyApp.getSwitchMarkBeanDao();
//                                    SwitchMarkBeanDao switchMarkBeanDao = daoSession.getSwitchMarkBeanDao();
//                                    List<SwitchMarkBean> switchMarkBeans = switchMarkBeanDao.loadAll();
//                                    if (switchMarkBeans.size() >= 5) {
//                                        switchMarkBeanDao.delete(switchMarkBeans.get(0));
//                                    }
//                                    SwitchMarkBean switchMarkBean = new SwitchMarkBean();
//                                    switchMarkBean.setUser_id("微博登录");
//                                    switchMarkBean.setUid(Long.valueOf(MyApp.uid));
//                                    switchMarkBean.setHeadimage(head_pic);
//                                    switchMarkBean.setUser_name(nickname);
//                                    switchMarkBean.setT_sign(MyApp.token);
//                                    switchMarkBean.setUrl_token(url_token);
//                                    switchMarkBean.setSex(sex);
//                                    switchMarkBean.setSexual(sexual);
//                                    switchMarkBeanDao.insertOrReplace(switchMarkBean);
//                                    break;
//                                case 4000:
//                                    intent = new Intent(LoginActivity.this, RegistOnePageActivity.class);
//                                    intent.putExtra("openid", wbid);
//                                    intent.putExtra("nickname", wbname);
//                                    intent.putExtra("headurl", wbheadpic);
//                                    intent.putExtra("loginmode", 1);
//                                    intent.putExtra("channel", "3");
//                                    startActivity(intent);
//                                    break;
//                                case 4001:
//                                case 4004:
//                                case 5000:
//                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
//                                    break;
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//
//            }
//        });

        HttpHelper.getInstance().thirdPartyLogin(3, "", wbid, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                ThirdLoginResultBean thirdLoginResultBean = GsonUtil.GsonToBean(data, ThirdLoginResultBean.class);
                if (thirdLoginResultBean != null && thirdLoginResultBean.getData() != null) {
                    ThirdLoginResultBean.DataBean resultBean = thirdLoginResultBean.getData();
                    ToastUtil.show("登录成功");
                    MyApp.uid = resultBean.getUid();
                    MyApp.token = resultBean.getT_sign();
                    String sex = resultBean.getSex();
                    String sexual = resultBean.getSexual();
                    String head_pic = resultBean.getHead_pic();
                    String nickname = resultBean.getNickname();
                    String url_token = resultBean.getToken();
                    //禁言状态
                    SharedPreferencesUtils.setParam(getApplicationContext(), "nospeak", resultBean.getChatstatus());
                    //筛选
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mysex", sex);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mysexual", sexual);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSex", sexual);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSexual", sex);
                    /*群组筛选*/
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSex", sex);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSexual", sexual);

                    SharedPreferencesUtils.setParam(LoginActivity.this, "uid", MyApp.uid);
                    SharedPreferencesUtils.setParam(LoginActivity.this, "token", MyApp.token);
                    SharedPreferencesUtils.setParam(LoginActivity.this, "url_token", url_token);
                    SharedPreferencesUtils.setParam(LoginActivity.this, "username", "");
                    gotoMainActivity();
                    DaoSession daoSession = MyApp.getSwitchMarkBeanDao();
                    SwitchMarkBeanDao switchMarkBeanDao = daoSession.getSwitchMarkBeanDao();
                    List<SwitchMarkBean> switchMarkBeans = switchMarkBeanDao.loadAll();
                    if (switchMarkBeans.size() >= 5) {
                        switchMarkBeanDao.delete(switchMarkBeans.get(0));
                    }
                    SwitchMarkBean switchMarkBean = new SwitchMarkBean();
                    switchMarkBean.setUser_id("微博登录");
                    switchMarkBean.setUid(Long.valueOf(MyApp.uid));
                    switchMarkBean.setHeadimage(head_pic);
                    switchMarkBean.setUser_name(nickname);
                    switchMarkBean.setT_sign(MyApp.token);
                    switchMarkBean.setUrl_token(url_token);
                    switchMarkBean.setSex(sex);
                    switchMarkBean.setSexual(sexual);
                    switchMarkBeanDao.insertOrReplace(switchMarkBean);
                }
            }

            @Override
            public void onFail(int code, String msg) {
                if (code == 4000) {
                    Intent intent = new Intent(LoginActivity.this, RegistOnePageActivity.class);
                    intent.putExtra("openid", wbid);
                    intent.putExtra("nickname", wbname);
                    intent.putExtra("headurl", wbheadpic);
                    intent.putExtra("loginmode", 1);
                    intent.putExtra("channel", "3");
                    startActivity(intent);
                } else {
                    ToastUtil.show(msg);
                }
            }
        });
    }

    private void QQlogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(LoginActivity.this, "all", new BaseUiListener());
        } else {
            // 注销登录
            mTencent.logout(getApplicationContext());
        }
    }

    //QQ回调
    private class BaseUiListener implements IUiListener {

        protected void doComplete(JSONObject values) {
//            Log.i("qqlogindocomplete", "doComplete: " + values);

        }

        @Override
        public void onComplete(Object o) {
            doComplete((JSONObject) o);
            Log.i("qqlogindata", "onComplete: " + o.toString());
            try {
                openidString = ((JSONObject) o).getString(Constants.PARAM_OPEN_ID);
                expiresString = ((JSONObject) o).getString(Constants.PARAM_EXPIRES_IN);
                tokenString = ((JSONObject) o).getString(Constants.PARAM_ACCESS_TOKEN);
                mTencent.setOpenId(openidString);
                mTencent.setAccessToken(tokenString, expiresString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(getApplicationContext(), qqToken);
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = o;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onError(UiError uiError) {
                    ToastUtil.show(uiError.errorMessage);
                }

                @Override
                public void onCancel() {
                }
            });
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

    private void QqGologinfirst(final String openidString, final String nicknameString, final String headurlString) {
        String urll = "https://graph.qq.com/oauth2.0/me?access_token=" + tokenString + "&unionid=1";
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.get(urll, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                ToastUtil.show("qq授权成功，登录中");
                PrintLogUtils.log(response, "--");
                // TODO 获取unionid方法有待改善
                String[] split = response.split(":");
                String s = split[split.length - 1];
                split = s.split("\"");
                String unionid = split[1];
                Log.d("--", "getQQUnionId_unionid: " + unionid);
                QqGologin(openidString, nicknameString, headurlString, unionid);
            }

            @Override
            public void onFailure(Throwable throwable) {
                ToastUtil.show(LoginActivity.this, throwable.getMessage());
            }
        });

    }

    private void QqGologin(final String openidString, final String nicknameString, final String headurlString, String unionid) {
//        Map<String, String> map = new HashMap<>();
//        map.put("unionid", unionid);
//        map.put("openid", openidString);
//        map.put("channel", "2");
//        map.put("device_token", SN);
//        map.put("new_device_brand", SystemUtil.getDeviceBrand() + "-" + SystemUtil.getSystemModel());
//        map.put("new_device_version", SystemUtil.getSystemVersion());
//        try {
//            map.put("new_device_appversion", VersionUtils.getVersion(getApplicationContext()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        IRequestManager manager = RequestFactory.getRequestManager();
//        manager.post(HttpUrl.ChargeOpenid, map, new IRequestCallback() {
//            @Override
//            public void onSuccess(final String response) {
//                Log.i("QQloginMsg", "onSuccess: " + response);
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Intent intent;
//                            JSONObject obj = new JSONObject(response);
//                            switch (obj.getInt("retcode")) {
//                                case 2000:
//                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
//                                    JSONObject obj1 = obj.getJSONObject("data");
//                                    MyApp.uid = obj1.getString("uid");
//                                    MyApp.token = obj1.getString("t_sign");
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "t_sign", MyApp.token);
//                                    String url_token = obj1.getString("token");
//                                    String sex = obj1.getString("sex");
//                                    String sexual = obj1.getString("sexual");
//                                    String head_pic = obj1.getString("head_pic");
//                                    String nickname = obj1.getString("nickname");
//                                    //禁言状态
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "nospeak", obj1.getString("chatstatus"));
//                                    //筛选
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mysex", sex);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mysexual", sexual);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSex", sexual);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSexual", sex);
//                                    /*群组筛选*/
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSex", sex);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSexual", sexual);
//
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "uid", MyApp.uid);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "token", MyApp.token);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "url_token", url_token);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(), "username", "");
//                                    //intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    intent = new Intent(LoginActivity.this, HomePageActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(intent);
//                                    DaoSession daoSession = MyApp.getSwitchMarkBeanDao();
//                                    SwitchMarkBeanDao switchMarkBeanDao = daoSession.getSwitchMarkBeanDao();
//                                    List<SwitchMarkBean> switchMarkBeans = switchMarkBeanDao.loadAll();
//                                    if (switchMarkBeans.size() >= 5) {
//                                        switchMarkBeanDao.delete(switchMarkBeans.get(0));
//                                    }
//                                    SwitchMarkBean switchMarkBean = new SwitchMarkBean();
//                                    switchMarkBean.setUser_id("QQ登录");
//                                    switchMarkBean.setUid(Long.valueOf(MyApp.uid));
//                                    switchMarkBean.setHeadimage(head_pic);
//                                    switchMarkBean.setUser_name(nickname);
//                                    switchMarkBean.setT_sign(MyApp.token);
//                                    switchMarkBean.setUrl_token(url_token);
//                                    switchMarkBean.setSex(sex);
//                                    switchMarkBean.setSexual(sexual);
//                                    switchMarkBeanDao.insertOrReplace(switchMarkBean);
//
//                                    break;
//                                case 4000:
//                                    intent = new Intent(LoginActivity.this, RegistOnePageActivity.class);
//                                    intent.putExtra("openid", openidString);
//                                    intent.putExtra("nickname", nicknameString);
//                                    intent.putExtra("headurl", headurlString);
//                                    intent.putExtra("loginmode", 1);
//                                    intent.putExtra("channel", "2");
//                                    startActivity(intent);
//                                    break;
//                                case 4001:
//                                case 4004:
//                                case 5000:
//                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
//                                    break;
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//
//            }
//        });


        HttpHelper.getInstance().thirdPartyLogin(2, unionid, openidString, new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                ThirdLoginResultBean thirdLoginResultBean = GsonUtil.GsonToBean(data, ThirdLoginResultBean.class);
                if (thirdLoginResultBean != null && thirdLoginResultBean.getData() != null) {
                    ThirdLoginResultBean.DataBean resultBean = thirdLoginResultBean.getData();
                    ToastUtil.show("登录成功");
                    MyApp.uid = resultBean.getUid();
                    MyApp.token = resultBean.getT_sign();
                    String sex = resultBean.getSex();
                    String sexual = resultBean.getSexual();
                    String head_pic = resultBean.getHead_pic();
                    String nickname = resultBean.getNickname();
                    String url_token = resultBean.getToken();
                    //禁言状态
                    SharedPreferencesUtils.setParam(getApplicationContext(), "nospeak", resultBean.getChatstatus());
                    //筛选
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mysex", sex);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mysexual", sexual);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSex", sexual);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSexual", sex);
                    /*群组筛选*/
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSex", sex);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSexual", sexual);

                    SharedPreferencesUtils.setParam(LoginActivity.this, "uid", MyApp.uid);
                    SharedPreferencesUtils.setParam(LoginActivity.this, "token", MyApp.token);
                    SharedPreferencesUtils.setParam(LoginActivity.this, "url_token", url_token);
                    SharedPreferencesUtils.setParam(LoginActivity.this, "username", "");
                    gotoMainActivity();
                    DaoSession daoSession = MyApp.getSwitchMarkBeanDao();
                    SwitchMarkBeanDao switchMarkBeanDao = daoSession.getSwitchMarkBeanDao();
                    List<SwitchMarkBean> switchMarkBeans = switchMarkBeanDao.loadAll();
                    if (switchMarkBeans.size() >= 5) {
                        switchMarkBeanDao.delete(switchMarkBeans.get(0));
                    }
                    SwitchMarkBean switchMarkBean = new SwitchMarkBean();
                    switchMarkBean.setUser_id("QQ登录");
                    switchMarkBean.setUid(Long.valueOf(MyApp.uid));
                    switchMarkBean.setHeadimage(head_pic);
                    switchMarkBean.setUser_name(nickname);
                    switchMarkBean.setT_sign(MyApp.token);
                    switchMarkBean.setUrl_token(url_token);
                    switchMarkBean.setSex(sex);
                    switchMarkBean.setSexual(sexual);
                    switchMarkBeanDao.insertOrReplace(switchMarkBean);
                }
            }

            @Override
            public void onFail(int code, String msg) {
                if (code == 4000) {
                    Intent intent = new Intent(LoginActivity.this, RegistOnePageActivity.class);
                    intent.putExtra("openid", openidString);
                    intent.putExtra("nickname", nicknameString);
                    intent.putExtra("headurl", headurlString);
                    intent.putExtra("loginmode", 1);
                    intent.putExtra("channel", "2");
                    startActivity(intent);
                } else {
                    ToastUtil.show(msg);
                }
            }
        });

    }

    private void login() {
        try {
            loginObj = new JSONObject();
            user_id = mLoginUsername.getText().toString().trim();
            password = mLoginPassword.getText().toString().trim();
            basicObj.put("user_name", mLoginUsername.getText().toString().trim());
            basicObj.put("password", mLoginPassword.getText().toString().trim());
            locationObj.put("city", MyApp.city);
            locationObj.put("lat", MyApp.lat);
            locationObj.put("lng", MyApp.lng);
            locationObj.put("addr", MyApp.address);
            deviceObj.put("new_device_brand", SystemUtil.getDeviceBrand() + "-" + SystemUtil.getSystemModel());
            deviceObj.put("new_device_version", SystemUtil.getSystemVersion());
            try {
                deviceObj.put("new_device_appversion", VersionUtils.getVersion(getApplicationContext()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            deviceObj.put("device_token", SN);
//            deviceObj.put("device_token","malajisi");
            loginObj.put("basic", basicObj);
            loginObj.put("location", locationObj);
            loginObj.put("device_info", deviceObj);
            loginObj.put("is_simulator", isEmulator);
            if ("1".equals(isEmulator)) {
                if (!TextUtil.isEmpty(emulatorInfoStr)) {
                    loginObj.put("emulator_info", emulatorInfoStr);
                }
            }
            if (!TextUtil.isEmpty(verifyCode)) {
                loginObj.put("code", verifyCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // new LoginTask().execute(new LoginRequest(loginObj.toString()));

        HttpHelper.getInstance().login(loginObj.toString(), new HttpCodeListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                mLoginBtnLogin.setEnabled(true);
                if (verifyAccountPop != null) {
                    verifyAccountPop.dismiss();
                }

                LoginResultBean loginResultBean = GsonUtil.GsonToBean(data, LoginResultBean.class);

                if (loginResultBean != null && loginResultBean.getData() != null) {
                    LoginResultBean.DataBean dataBean = loginResultBean.getData();
                    MyApp.uid = dataBean.getUid();
                    //MyApp.token = obj1.getString("r_token");
                    MyApp.token = dataBean.getT_sign();
                    SharedPreferencesUtils.setParam(getApplicationContext(), "t_sign", MyApp.token);
                    String sex = dataBean.getSex();
                    String sexual = dataBean.getSexual();
                    String match_photo = dataBean.getMatch_photo();
                    String nickname = dataBean.getNickname();
                    String url_token = dataBean.getToken();
                    //禁言状态
                    SharedPreferencesUtils.setParam(getApplicationContext(), "nospeak", dataBean.getChatstatus());
                    //筛选
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mysex", sex);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mysexual", sexual);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSex", sexual);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSexual", sex);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSex", sex);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSexual", sexual);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "uid", MyApp.uid);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "token", MyApp.token);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "url_token", url_token);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "username", mLoginUsername.getText().toString().trim());
                    //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    //账号存储到数据库
                    DaoSession daoSession = MyApp.getSwitchMarkBeanDao();
                    SwitchMarkBeanDao switchMarkBeanDao = daoSession.getSwitchMarkBeanDao();
                    List<SwitchMarkBean> switchMarkBeans = switchMarkBeanDao.loadAll();
                    if (switchMarkBeans.size() >= 5) {
                        switchMarkBeanDao.delete(switchMarkBeans.get(0));
                    }
                    SwitchMarkBean switchMarkBean = new SwitchMarkBean();
                    switchMarkBean.setUser_id(user_id);
                    switchMarkBean.setPassword(password);
                    switchMarkBean.setUid(Long.valueOf(MyApp.uid));
                    switchMarkBean.setHeadimage(match_photo);
                    switchMarkBean.setUser_name(nickname);
                    switchMarkBean.setT_sign(MyApp.token);
                    switchMarkBean.setUrl_token(url_token);
                    switchMarkBean.setSex(sex);
                    switchMarkBean.setSexual(sexual);
                    switchMarkBeanDao.insertOrReplace(switchMarkBean);
                    finish();
                } else {
                    ToastUtil.show("登录异常,请稍后再试，或者联系客服");
                }
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(msg);
                mLoginBtnLogin.setEnabled(true);
                if (code == 2002) {
                    showVerPop();
                }
            }
        });
    }

    class LoginTask extends AsyncTask<LoginRequest, Void, String> {

        @Override
        protected String doInBackground(LoginRequest... params) {
            LoginRequest paymentRequest = params[0];
            String data = null;
            String json = paymentRequest.loginObjectStr;
            LogUtil.d(json);
            try {
                data = postJson(HttpUrl.Login, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
            verifyCode = "";
            if (null == data) {
                return;
            }
            try {
                JSONObject obj = new JSONObject(data);
                Log.d("okhttp", data);
                switch (obj.getInt("retcode")) {
                    case 4000:
                    case 4001:
                    case 4002:
                    case 4003:
                    case 4004:
                    case 5000:
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                        break;
                    case 2002:
                        //ToastUtil.show(getApplicationContext(), "需要验证");
                        showVerPop();
                        break;
                    case 2000:
                        if (verifyAccountPop != null) {
                            verifyAccountPop.dismiss();
                        }
                        ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                        JSONObject obj1 = obj.getJSONObject("data");
                        MyApp.uid = obj1.getString("uid");
                        //MyApp.token = obj1.getString("r_token");
                        MyApp.token = obj1.getString("t_sign");
                        SharedPreferencesUtils.setParam(getApplicationContext(), "t_sign", MyApp.token);
                        String sex = obj1.getString("sex");
                        String sexual = obj1.getString("sexual");
                        String match_photo = obj1.getString("match_photo");
                        String nickname = obj1.getString("nickname");
                        String url_token = obj1.getString("token");
                        //禁言状态
                        SharedPreferencesUtils.setParam(getApplicationContext(), "nospeak", obj1.getString("chatstatus"));
                        //筛选
                        SharedPreferencesUtils.setParam(getApplicationContext(), "mysex", sex);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "mysexual", sexual);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSex", sexual);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSexual", sex);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSex", sex);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSexual", sexual);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "uid", MyApp.uid);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "token", MyApp.token);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "url_token", url_token);
                        SharedPreferencesUtils.setParam(getApplicationContext(), "username", mLoginUsername.getText().toString().trim());
                        //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        //账号存储到数据库
                        DaoSession daoSession = MyApp.getSwitchMarkBeanDao();
                        SwitchMarkBeanDao switchMarkBeanDao = daoSession.getSwitchMarkBeanDao();
                        List<SwitchMarkBean> switchMarkBeans = switchMarkBeanDao.loadAll();
                        if (switchMarkBeans.size() >= 5) {
                            switchMarkBeanDao.delete(switchMarkBeans.get(0));
                        }
                        SwitchMarkBean switchMarkBean = new SwitchMarkBean();
                        switchMarkBean.setUser_id(user_id);
                        switchMarkBean.setPassword(password);
                        switchMarkBean.setUid(Long.valueOf(MyApp.uid));
                        switchMarkBean.setHeadimage(match_photo);
                        switchMarkBean.setUser_name(nickname);
                        switchMarkBean.setT_sign(MyApp.token);
                        switchMarkBean.setUrl_token(url_token);
                        switchMarkBean.setSex(sex);
                        switchMarkBean.setSexual(sexual);
                        switchMarkBeanDao.insertOrReplace(switchMarkBean);
                        finish();
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mLoginBtnLogin.setEnabled(true);
        }
    }

    private String session = "";

    private String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(HttpUrl.NetPic() + url)
                .addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", "")).post(body).build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    class LoginRequest {
        String loginObjectStr;

        public LoginRequest(String loginObjectStr) {
            this.loginObjectStr = loginObjectStr;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mTencent != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
        }
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, new BaseUiListener());
            }
        }
        //SSO 授权回调
        //重要：发起sso登录的activity必须重写onActivtyResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止微信登录内存泄露
        mApi.detach();
        if (mTencent != null) {
            mTencent.releaseResource();
        }
        mTencent = null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //getthirdParty();
    }

    public void getthirdParty() {
        Map<String, String> map = new HashMap<>();
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.thirdParty, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            JSONObject data = obj.getJSONObject("data");
                            String state = data.getString("state");
                            if ("1".equals(state)) {
                                kuai_ll.setVisibility(View.VISIBLE);
                                login_ll.setVisibility(View.VISIBLE);
                            } else {
                                kuai_ll.setVisibility(View.GONE);
                                login_ll.setVisibility(View.GONE);
                            }
                            break;
                        case 50001:
                        case 50002:
                            EventBus.getDefault().post(new TokenFailureEvent());
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private String verifyCode = "";

    void showVerPop() {
        verifyAccountPop = new VerifyAccountPop(LoginActivity.this, mLoginUsername.getText().toString().trim());
        verifyAccountPop.showPopupWindow();
        verifyAccountPop.setOnVerifyCodeCheckListener(new VerifyAccountPop.OnVerifyCodeCheckListener() {
            @Override
            public void OnVerifyCodeCheck(String code) {
                if (TextUtil.isEmpty(code)) {
                    return;
                }
                verifyCode = code;
                login();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(SessionEvent2 sessionEvent) {
        session = sessionEvent.getSessionId();
        Bitmap bitmap = BitmapFactory.decodeByteArray(sessionEvent.getPicByte(), 0, sessionEvent.getPicByte().length);
        if (verifyAccountPop != null) {
            verifyAccountPop.setSession(session);
            verifyAccountPop.showImg(bitmap);
        }
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void checkLiveRoom() {

    }
}
