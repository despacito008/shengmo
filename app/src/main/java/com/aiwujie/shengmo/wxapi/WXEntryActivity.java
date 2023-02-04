package com.aiwujie.shengmo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.AddUserActivity;
import com.aiwujie.shengmo.activity.MainActivity;
import com.aiwujie.shengmo.activity.RegistOnePageActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.SwitchMarkBean;
import com.aiwujie.shengmo.bean.ThirdLoginResultBean;
import com.aiwujie.shengmo.dao.DaoSession;
import com.aiwujie.shengmo.dao.SwitchMarkBeanDao;
import com.aiwujie.shengmo.eventbus.OpenidEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.HomePageActivity;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GetDeviceIdUtils;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.ShareSuccessUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.SystemUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.VersionUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    public static final String APP_ID = "wx0392b14b6a6f023c";
    public static final String APP_SECRET = "e9d7a5a8e1f15528a99ab7e51a7193e2";
    public IWXAPI mApi;
    Handler handler = new Handler();
    private String SN;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        SN = GetDeviceIdUtils.getSN(this);
        mApi = WXAPIFactory.createWXAPI(this, APP_ID, false);
        mApi.registerApp(APP_ID);
        mApi.handleIntent(getIntent(), this);
    }

    /**
     * 获取openid accessToken值用于后期操作
     *
     * @param code 请求码
     */
    private void getAccess_token(final String code) {
        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + APP_ID
                + "&secret="
                + APP_SECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.get(path, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (null != jsonObject) {
                                String openid = jsonObject.getString("openid").toString().trim();
                                String access_token = jsonObject.getString("access_token").toString().trim();
                                if (MyApp.wxloginFlag == 1) {
                                    getUserMesg(access_token, openid);
                                } else {
                                    //微信绑定
                                    EventBus.getDefault().post(new OpenidEvent(openid));
                                }
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

    /**
     * 获取微信的个人信息
     *
     * @param access_token
     * @param openid
     */
    private void getUserMesg(final String access_token, final String openid) {
        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.get(path, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (null != jsonObject) {
                                String nickname = jsonObject.getString("nickname");
                                int sex = Integer.parseInt(jsonObject.get("sex").toString());
                                String headimgurl = jsonObject.getString("headimgurl");
                                String unionid = jsonObject.getString("unionid");
                                wxLogin(openid, nickname, sex, headimgurl, access_token, unionid);
//                                Log.e(TAG, "getUserMesg 拿到了用户Wx基本信息.. nickname:" + nickname+","+sex+","+headimgurl);
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

    private void wxLogin(final String openid, final String nickname, int sex, final String headimgurl, String access_token, String unionid) {
//        Map<String,String> map=new HashMap<>();
//        map.put("unionid",unionid);
//        map.put("openid",openid);
//        map.put("channel","1");
//        map.put("device_token",SN);
//        map.put("new_device_brand", SystemUtil.getDeviceBrand()+"-"+SystemUtil.getSystemModel());
//        map.put("new_device_version", SystemUtil.getSystemVersion());
//        try {
//            map.put("new_device_appversion", VersionUtils.getVersion(getApplicationContext()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        IRequestManager manager= RequestFactory.getRequestManager();
//        manager.post(HttpUrl.ChargeOpenid, map, new IRequestCallback() {
//            @Override
//            public void onSuccess(final String response) {
////                Log.i("WXloginMsg", "onSuccess: "+response);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intent;
//                        try {
//                            JSONObject obj=new JSONObject(response);
//                            switch (obj.getInt("retcode")){
//                                case 2000:
//                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
//                                    JSONObject obj1 = obj.getJSONObject("data");
//                                    MyApp.uid = obj1.getString("uid");
//                                    MyApp.token = obj1.getString("t_sign");
//                                    String sex=obj1.getString("sex");
//                                    String sexual=obj1.getString("sexual");
//                                    String head_pic=obj1.getString("head_pic");
//
//                                    String url_token = obj1.getString("token");
//                                    //禁言状态
//                                    SharedPreferencesUtils.setParam(getApplicationContext(),"nospeak",obj1.getString("chatstatus"));
//                                    //筛选
//                                    SharedPreferencesUtils.setParam(getApplicationContext(),"mysex",sex);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(),"mysexual",sexual);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(),"mydynamicSex",sexual);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(),"mydynamicSexual",sex);
//                                    /*群组筛选*/
//                                    SharedPreferencesUtils.setParam(getApplicationContext(),"mygroupSex",sex);
//                                    SharedPreferencesUtils.setParam(getApplicationContext(),"mygroupSexual",sexual);
//
//                                    SharedPreferencesUtils.setParam(WXEntryActivity.this, "uid", MyApp.uid);
//                                    SharedPreferencesUtils.setParam(WXEntryActivity.this, "token", MyApp.token);
//                                    SharedPreferencesUtils.setParam(WXEntryActivity.this, "url_token", url_token);
//                                    SharedPreferencesUtils.setParam(WXEntryActivity.this,"username","");
//                                    gotoMainActivity();
//                                    DaoSession daoSession = MyApp.getSwitchMarkBeanDao();
//                                    SwitchMarkBeanDao switchMarkBeanDao = daoSession.getSwitchMarkBeanDao();
//                                    List<SwitchMarkBean> switchMarkBeans = switchMarkBeanDao.loadAll();
//                                    if(switchMarkBeans.size()>=5){
//                                        switchMarkBeanDao.delete(switchMarkBeans.get(0));
//                                    }
//                                    SwitchMarkBean switchMarkBean = new SwitchMarkBean();
//                                    switchMarkBean.setUser_id("微信登录");
//                                    switchMarkBean.setUid(Long.valueOf(MyApp.uid));
//                                    switchMarkBean.setHeadimage(head_pic);
//                                    switchMarkBean.setUser_name(nickname);
//                                    switchMarkBean.setT_sign(MyApp.token);
//                                    switchMarkBean.setUrl_token(url_token);
//                                    switchMarkBean.setSex(sex);
//                                    switchMarkBean.setSexual(sexual);
//                                    switchMarkBeanDao.insertOrReplace(switchMarkBean);
//                                    MyApp.wxloginFlag=0;
//                                    break;
//                                case 4000:
//                                    intent = new Intent(WXEntryActivity.this,RegistOnePageActivity.class);
//                                    intent.putExtra("openid",openid);
//                                    intent.putExtra("nickname",nickname);
//                                    intent.putExtra("headurl",headimgurl);
//                                    intent.putExtra("loginmode",1);
//                                    intent.putExtra("channel","1");
//                                    startActivity(intent);
//                                    break;
//                                case 4001:
//                                case 4003:
//                                case 4004:
//                                case 5000:
//                                    ToastUtil.show(getApplicationContext(),obj.getString("msg"));
//                                    break;
//                                case 50001:
//                                case 50002:
//                                    EventBus.getDefault().post(new TokenFailureEvent());
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


        HttpHelper.getInstance().thirdPartyLogin(1, unionid, openid, new HttpCodeMsgListener() {
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

                    SharedPreferencesUtils.setParam(WXEntryActivity.this, "uid", MyApp.uid);
                    SharedPreferencesUtils.setParam(WXEntryActivity.this, "token", MyApp.token);
                    SharedPreferencesUtils.setParam(WXEntryActivity.this, "url_token", url_token);
                    SharedPreferencesUtils.setParam(WXEntryActivity.this, "username", "");
                    gotoMainActivity();
                    DaoSession daoSession = MyApp.getSwitchMarkBeanDao();
                    SwitchMarkBeanDao switchMarkBeanDao = daoSession.getSwitchMarkBeanDao();
                    List<SwitchMarkBean> switchMarkBeans = switchMarkBeanDao.loadAll();
                    if (switchMarkBeans.size() >= 5) {
                        switchMarkBeanDao.delete(switchMarkBeans.get(0));
                    }
                    SwitchMarkBean switchMarkBean = new SwitchMarkBean();
                    switchMarkBean.setUser_id("微信登录");
                    switchMarkBean.setUid(Long.valueOf(MyApp.uid));
                    switchMarkBean.setHeadimage(head_pic);
                    switchMarkBean.setUser_name(nickname);
                    switchMarkBean.setT_sign(MyApp.token);
                    switchMarkBean.setUrl_token(url_token);
                    switchMarkBean.setSex(sex);
                    switchMarkBean.setSexual(sexual);
                    switchMarkBeanDao.insertOrReplace(switchMarkBean);
                    MyApp.wxloginFlag = 0;
                }
            }

            @Override
            public void onFail(int code, String msg) {
                if (code == 4000) {
                    Intent intent = new Intent(WXEntryActivity.this, RegistOnePageActivity.class);
                    intent.putExtra("openid", openid);
                    intent.putExtra("nickname", nickname);
                    intent.putExtra("headurl", headimgurl);
                    intent.putExtra("loginmode", 1);
                    intent.putExtra("channel", "1");
                    startActivity(intent);
                } else {
                    ToastUtil.show(msg);
                }
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        mApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {

        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {//登录
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //发送成功
                    SendAuth.Resp sendResp = (SendAuth.Resp) baseResp;
                    if (sendResp != null) {
                        String code = sendResp.code;
                        getAccess_token(code);
                    }
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    //发送取消
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    //发送被拒绝
                    break;
                default:
                    //发送返回
                    break;
            }
        } else if (baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {//分享
            if (baseResp.errCode == 0) {
                ShareSuccessUtils.Shared(handler);
            }
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApi = null;
    }

    void gotoMainActivity() {
        //清空会话列表
        ConversationManagerKit.getInstance().destroyConversation();
        TUIKit.logout(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                //Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                Intent intent = new Intent(WXEntryActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                //Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                Intent intent = new Intent(WXEntryActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
//
    }
}
