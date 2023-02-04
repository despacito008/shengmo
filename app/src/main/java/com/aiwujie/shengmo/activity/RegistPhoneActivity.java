package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.SessionEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.eventbus.VercodeEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GetDeviceIdUtils;
import com.aiwujie.shengmo.utils.OkHttpSession;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.emulatorcheck.EmulatorCheckCallback;
import com.aiwujie.shengmo.utils.emulatorcheck.EmulatorCheckUtil;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistPhoneActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.mRegist_return)
    ImageView mRegistReturn;
    @BindView(R.id.item_title_name)
    TextView itemTitleName;
    @BindView(R.id.mRegist_username)
    EditText mRegistUsername;
    @BindView(R.id.mRegist_vercode)
    EditText mRegistVercode;
    @BindView(R.id.mRegist_sendVercode)
    Button mRegistSendVercode;
    @BindView(R.id.mRegist_password)
    EditText mRegistPassword;
    @BindView(R.id.mRegist_checkbox)
    CheckBox mRegistCheckbox;
    @BindView(R.id.mRegist_btn_regist)
    Button mRegistBtnRegist;
    @BindView(R.id.mRegist_xieyi)
    TextView mRegistXieyi;
    @BindView(R.id.mRegist_etPicVercode)
    EditText mRegistEtPicVercode;
    @BindView(R.id.mRegist_picVercode)
    ImageView mRegistPicVercode;
    @BindView(R.id.mRegist_invite_code)
    EditText mRegistInviteCode;
    //    @BindView(R.id.mRegist_email)
//    TextView mRegistEmail;
    private JSONObject basicObj;
    private String phone;
    private String password;
    private String vercode;
    int time = 60;
    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<RegistPhoneActivity> activityWeakReference;

        public MyHandler(RegistPhoneActivity registPhoneActivity) {
            activityWeakReference = new WeakReference<>(registPhoneActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RegistPhoneActivity registPhoneActivity = activityWeakReference.get();
            switch (msg.what) {
                case 0:
                    if (registPhoneActivity.time <= 0) {
                        registPhoneActivity.mRegistSendVercode.setText("点击发送验证码");
                        registPhoneActivity.mRegistSendVercode.setBackgroundResource(R.drawable.item_login_btn2);
                        registPhoneActivity.mRegistSendVercode.setTextColor(Color.parseColor("#FFFFFF"));
                        registPhoneActivity.mRegistSendVercode.setEnabled(true);
                        registPhoneActivity.time = 60;
                    } else {
                        registPhoneActivity.mRegistSendVercode.setText("(" + registPhoneActivity.time-- + "秒后重新获取)");
                        registPhoneActivity.mRegistSendVercode.setBackgroundResource(R.drawable.item_login_hui_btn);
                        registPhoneActivity.mRegistSendVercode.setTextColor(Color.parseColor("#DDDDDD"));
                        registPhoneActivity.mRegistSendVercode.setEnabled(false);
                        registPhoneActivity.handler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
            }
        }
    }

    private String tempPhone;
    private String tempPassword;
    private String tempVercode;
    private String IMEI;
    private String SN;
    private OkHttpSession okHttpSession;
    private String JSESSIONID;
    private String piccode;
    private String isEmulator = "0";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_phone);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        EventBus.getDefault().register(this);
        setView();
        setListener();
        getEmulatorInfo();
       // mRegistInviteCode.setVisibility(View.GONE);
        //获取设备号
//        IMEI= GetDeviceIdUtils.getIMEI(this);
    }

    void getEmulatorInfo() {
        isEmulator = EmulatorCheckUtil.getSingleInstance().readSysProperty(RegistPhoneActivity.this, new EmulatorCheckCallback() {
            @Override
            public void findEmulator(String emulatorInfo) {

            }
        }) ? "1" : "0";
    }

    private void setView() {
        SN = GetDeviceIdUtils.getSN(this);
        okHttpSession = new OkHttpSession();
        okHttpSession.ChangeVercodeImage(HttpUrl.NetPic() + HttpUrl.GetPicSession);
        mRegistXieyi.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        if (mRegistCheckbox.isChecked()) {
//            mRegistBtnRegist.setBackgroundResource(R.drawable.item_login_btn2);
//            mRegistBtnRegist.setTextColor(Color.parseColor("#FFFFFF"));
//        } else {
//            mRegistBtnRegist.setBackgroundResource(R.drawable.item_login_hui_btn);
//            mRegistBtnRegist.setTextColor(Color.parseColor("#b7b7b7"));
//        }
    }

    private void setListener() {
        mRegistCheckbox.setOnCheckedChangeListener(this);
        mRegistUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mEmail = s.toString();
                checkInfo();
            }
        });
        mRegistEtPicVercode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPCode = s.toString();
                checkInfo();
            }
        });
        mRegistPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPassword = s.toString();
                checkInfo();
            }
        });
        mRegistVercode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mCode = s.toString();
                checkInfo();
            }
        });
    }

    private String mEmail,mCode,mPCode,mPassword;
    private void checkInfo() {
        if (TextUtil.isEmpty(mEmail) || TextUtil.isEmpty(mCode) || TextUtil.isEmpty(mPassword) || TextUtil.isEmpty(mPCode)) {
            mRegistBtnRegist.setBackgroundResource(R.drawable.item_login_hui_btn);
            mRegistBtnRegist.setTextColor(Color.parseColor("#b7b7b7"));
        } else {
            mRegistBtnRegist.setBackgroundResource(R.drawable.bg_round_purple_home);
            mRegistBtnRegist.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    @OnClick({R.id.mRegist_return, R.id.mRegist_sendVercode, R.id.mRegist_btn_regist, R.id.mRegist_xieyi, R.id.mRegist_picVercode})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mRegist_return:
                finish();
                break;
            case R.id.mRegist_sendVercode:
                //发送验证码
                if (TextUtil.isEmpty(mRegistUsername.getText().toString().trim()) || TextUtil.isEmpty(mRegistEtPicVercode.getText().toString().trim())) {
                    ToastUtil.show(getApplicationContext(), "请输入手机号和图形验证码...");
                    return;
                }
                okHttpSession.VercodeServer(mRegistUsername.getText().toString(), mRegistEtPicVercode.getText().toString(), JSESSIONID, HttpUrl.NetPic() + HttpUrl.SendVercode);
                break;
            case R.id.mRegist_btn_regist:
                if (mRegistCheckbox.isChecked()) {
                    phone = mRegistUsername.getText().toString().trim();
                    password = mRegistPassword.getText().toString().trim();
                    vercode = mRegistVercode.getText().toString().trim();
                    piccode = mRegistEtPicVercode.getText().toString().trim();
                    if (TextUtil.isEmpty(phone) || TextUtil.isEmpty(password) || TextUtil.isEmpty(vercode) || TextUtil.isEmpty(piccode)) {
                        ToastUtil.show(getApplicationContext(), "请填写完整注册信息...");
                    } else {
                        //验证
                        if (!phone.equals(tempPhone) || !password.equals(tempPassword) || !vercode.equals(tempVercode)) {
                            if (TextUtil.isEmpty(mRegistInviteCode.getText().toString())) {
                                chargeFrist();
                            } else {
                                checkInviteCode();
                            }
                        } else {
                            ToastUtil.show(getApplicationContext(), "请您修改注册信息后再提交");
                        }
                    }
                } else {
                    ToastUtil.show(getApplicationContext(), "请同意圣魔用户协议");
                }
                break;
            case R.id.mRegist_xieyi:
                intent = new Intent(this, SmAgreementActivity.class);
                startActivity(intent);
                break;
            case R.id.mRegist_picVercode:
                okHttpSession.ChangeVercodeImage(HttpUrl.NetPic() + HttpUrl.GetPicSession);
                break;
        }
    }

    private void chargeFrist() {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("password", password);
        map.put("code", vercode);
        map.put("device_token", SN);
        map.put("is_simulator", isEmulator);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.ChargeFristNew, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    basicJson();
                                    Intent intent = new Intent(RegistPhoneActivity.this, RegistOnePageActivity.class);
                                    intent.putExtra("basicObj", basicObj.toString());
                                    intent.putExtra("loginmode", 0);
                                    intent.putExtra("invite_code",mRegistInviteCode.getText().toString());
                                    startActivity(intent);
                                    break;
                                case 4010:
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                case 4006:
                                case 4007:
                                case 4008:
                                case 3000:
                                case 3001:
                                case 3002:
                                case 3003:
                                case 3004:
                                case 4011:
                                case 5000:
                                    tempPhone = mRegistUsername.getText().toString().trim();
                                    tempPassword = mRegistPassword.getText().toString().trim();
                                    tempVercode = mRegistVercode.getText().toString().trim();
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
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


    private void basicJson() {
        basicObj = new JSONObject();
        try {
            basicObj.put("mobile", phone);
            basicObj.put("password", password);
            basicObj.put("code", vercode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //判断勾选点击协议
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if (isChecked) {
////            mRegistBtnRegist.setEnabled(true);
//            mRegistBtnRegist.setBackgroundResource(R.drawable.item_login_btn2);
//            mRegistBtnRegist.setTextColor(Color.parseColor("#FFFFFF"));
//        } else {
////            mRegistBtnRegist.setEnabled(false);
//            mRegistBtnRegist.setBackgroundResource(R.drawable.item_login_hui_btn);
//            mRegistBtnRegist.setTextColor(Color.parseColor("#b7b7b7"));
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(SessionEvent sessionEvent) {
        JSESSIONID = sessionEvent.getSessionId();
        Bitmap bitmap = BitmapFactory.decodeByteArray(sessionEvent.getPicByte(), 0, sessionEvent.getPicByte().length);
        mRegistPicVercode.setImageBitmap(bitmap);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(VercodeEvent vercodeEvent) {
        try {
            JSONObject obj = new JSONObject(vercodeEvent.getResponse());
            switch (obj.getString("retcode")) {
                case "4000":
                case "4001":
                case "4002":
                case "4006":
                case "4007":
                case "3000":
                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                    okHttpSession.ChangeVercodeImage(HttpUrl.NetPic() + HttpUrl.GetPicSession);
                    break;
                case "2000":
                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                    handler.sendEmptyMessage(0);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    void checkInviteCode() {
        HttpHelper.getInstance().checkInviteCode(mRegistInviteCode.getText().toString(), new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                chargeFrist();
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(RegistPhoneActivity.this,msg);
            }
        });
    }
}
