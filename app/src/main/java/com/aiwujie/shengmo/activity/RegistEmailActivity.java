package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GetDeviceIdUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.emulatorcheck.EmulatorCheckCallback;
import com.aiwujie.shengmo.utils.emulatorcheck.EmulatorCheckUtil;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistEmailActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.mRegistEmail_return)
    ImageView mRegistEmailReturn;
    @BindView(R.id.item_title_name)
    TextView itemTitleName;
    @BindView(R.id.mRegistEmail_email)
    EditText mRegistEmailEmail;
    @BindView(R.id.mRegistEmail_password)
    EditText mRegistEmailPassword;
    @BindView(R.id.mRegistEmail_checkbox)
    CheckBox mRegistEmailCheckbox;
    @BindView(R.id.mRegistEmail_xieyi)
    TextView mRegistEmailXieyi;
    @BindView(R.id.mRegistEmail_btn_regist)
    Button mRegistEmailBtnRegist;
    @BindView(R.id.mRegistEmail_vercode)
    EditText mRegistEmailVercode;
    @BindView(R.id.mRegistEmail_sendVercode)
    Button mRegistEmailSendVercode;
    @BindView(R.id.mRegist_invite_code)
    EditText mRegistInviteCode;
    private String email;
    private String code;
    private String password;
    private JSONObject basicObj;
    private String tempEmail;
    private String tempPassword;
    int time = 60;
    private String IMEI;
    private String SN;
    private String isEmulator = "0";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (time <= 0) {
                        mRegistEmailSendVercode.setText("点击发送验证码");
                        mRegistEmailSendVercode.setBackgroundResource(R.drawable.item_login_btn2);
                        mRegistEmailSendVercode.setTextColor(Color.parseColor("#FFFFFF"));
                        mRegistEmailSendVercode.setEnabled(true);
                        time = 60;
                    } else {
                        mRegistEmailSendVercode.setText("(" + time-- + "秒后重新获取)");
                        mRegistEmailSendVercode.setBackgroundResource(R.drawable.item_login_hui_btn);
                        mRegistEmailSendVercode.setTextColor(Color.parseColor("#DDDDDD"));
                        mRegistEmailSendVercode.setEnabled(false);
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_email);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        setView();
        setListener();
        getEmulatorInfo();
        //mRegistInviteCode.setVisibility(View.GONE);
//        //获取设备号
//        IMEI= GetDeviceIdUtils.getIMEI(this);
        SN = GetDeviceIdUtils.getSN(this);



    }

    void getEmulatorInfo() {
        isEmulator = EmulatorCheckUtil.getSingleInstance().readSysProperty(RegistEmailActivity.this, new EmulatorCheckCallback() {
            @Override
            public void findEmulator(String emulatorInfo) {

            }
        }) ? "1" : "0";

        // boolean isAopt = GetDeviceIdUtils.isAdopt(LoginActivity.this);
    }

    private void setView() {
        mRegistEmailXieyi.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        if (mRegistEmailCheckbox.isChecked()) {
//            mRegistEmailBtnRegist.setBackgroundResource(R.drawable.item_login_btn2);
//            mRegistEmailBtnRegist.setTextColor(Color.parseColor("#FFFFFF"));
//        } else {
//            mRegistEmailBtnRegist.setBackgroundResource(R.drawable.item_login_hui_btn);
//            mRegistEmailBtnRegist.setTextColor(Color.parseColor("#b7b7b7"));
//        }
    }

    private void setListener() {
        //mRegistEmailCheckbox.setOnCheckedChangeListener(this);
        mRegistEmailEmail.addTextChangedListener(new TextWatcher() {
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
        mRegistEmailVercode.addTextChangedListener(new TextWatcher() {
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
        mRegistEmailPassword.addTextChangedListener(new TextWatcher() {
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
    }

    private String mEmail,mCode,mPassword;
    private void checkInfo() {
        if (TextUtil.isEmpty(mEmail) || TextUtil.isEmpty(mCode) || TextUtil.isEmpty(mPassword)) {
            mRegistEmailBtnRegist.setBackgroundResource(R.drawable.item_login_hui_btn);
            mRegistEmailBtnRegist.setTextColor(Color.parseColor("#b7b7b7"));
        } else {
            mRegistEmailBtnRegist.setBackgroundResource(R.drawable.bg_round_purple_home);
            mRegistEmailBtnRegist.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    @OnClick({R.id.mRegistEmail_return, R.id.mRegistEmail_xieyi, R.id.mRegistEmail_btn_regist, R.id.mRegistEmail_sendVercode})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mRegistEmail_return:
                finish();
                break;
            case R.id.mRegistEmail_sendVercode:
                //发送验证码
                sendVercode();
                break;
            case R.id.mRegistEmail_xieyi:
                intent = new Intent(this, SmAgreementActivity.class);
                startActivity(intent);
                break;
            case R.id.mRegistEmail_btn_regist:
                if (mRegistEmailCheckbox.isChecked()) {
                    email = mRegistEmailEmail.getText().toString().trim();
                    code = mRegistEmailVercode.getText().toString().trim();
                    password = mRegistEmailPassword.getText().toString().trim();
                    if (TextUtil.isEmpty(email) || TextUtil.isEmpty(password) || TextUtil.isEmpty(code)) {
                        ToastUtil.show(getApplicationContext(), "请填写完整注册信息...");
                    } else {
                        //验证
                        if (!email.equals(tempEmail) || !password.equals(tempPassword) || TextUtil.isEmpty(code)) {
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
        }
    }

    private void chargeFrist() {
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("code", code);
        map.put("password", password);
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
                                    Intent intent = new Intent(RegistEmailActivity.this, RegistOnePageActivity.class);
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
                                    tempEmail = mRegistEmailEmail.getText().toString().trim();
                                    tempPassword = mRegistEmailPassword.getText().toString().trim();
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

    private void sendVercode() {
        Map<String, String> map = new HashMap<>();
        map.put("email", mRegistEmailEmail.getText().toString().trim());
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SendMailCode_reg, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("registemail", "onSuccess: " + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getString("retcode")) {
                                case "4000":
                                case "4001":
                                case "4002":
                                case "4006":
                                case "4007":
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
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
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
//            mRegistBtnRegist.setEnabled(true);
            mRegistEmailBtnRegist.setBackgroundResource(R.drawable.item_login_btn2);
            mRegistEmailBtnRegist.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
//            mRegistBtnRegist.setEnabled(false);
            mRegistEmailBtnRegist.setBackgroundResource(R.drawable.item_login_hui_btn);
            mRegistEmailBtnRegist.setTextColor(Color.parseColor("#b7b7b7"));
        }
    }

    private void basicJson() {
        basicObj = new JSONObject();
        try {
            basicObj.put("email", email);
            basicObj.put("code", code);
            basicObj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void checkInviteCode() {
        HttpHelper.getInstance().checkInviteCode(mRegistInviteCode.getText().toString(), new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                chargeFrist();
            }

            @Override
            public void onFail(int code, String msg) {
                ToastUtil.show(RegistEmailActivity.this,msg);
            }
        });
    }
}