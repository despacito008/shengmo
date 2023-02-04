package com.aiwujie.shengmo.activity.binding;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.MyPurseBindEvent;
import com.aiwujie.shengmo.eventbus.SessionEvent;
import com.aiwujie.shengmo.eventbus.VercodeEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.view.GraphicsVerifyPop;
import com.aiwujie.shengmo.kt.ui.view.GraphicsVerifyView;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.OkHttpSession;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindingMobileActivity extends AppCompatActivity {

    public static final String TAG = "BindingMobileActivity";

    @BindView(R.id.mBinding_mobile_return)
    ImageView mBindingMobileReturn;
    @BindView(R.id.mBinding_mobile_username)
    EditText mBindingMobileUsername;
    @BindView(R.id.mBinding_mobile_vercode)
    EditText mBindingMobileVercode;
    @BindView(R.id.mBinding_mobile_sendVercode)
    Button mBindingMobileSendVercode;
    @BindView(R.id.mBinding_mobile_btn)
    Button mBindingMobileBtn;
    @BindView(R.id.mBinding_etPicVercode)
    EditText mBindingEtPicVercode;
    @BindView(R.id.mBinding_picVercode)
    ImageView mBindingPicVercode;
    private String changeornew;
    int time = 60;
    private String JSESSIONID;
    private OkHttpSession okHttpSession;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (time <= 0) {
                        mBindingMobileSendVercode.setText("点击发送验证码");
                        mBindingMobileSendVercode.setBackgroundResource(R.drawable.item_login_btn2);
                        mBindingMobileSendVercode.setTextColor(Color.parseColor("#FFFFFF"));
                        mBindingMobileSendVercode.setEnabled(true);
                        time = 60;
                    } else {
                        mBindingMobileSendVercode.setText("(" + time-- + "秒后重新获取)");
                        mBindingMobileSendVercode.setBackgroundResource(R.drawable.item_login_hui_btn);
                        mBindingMobileSendVercode.setTextColor(Color.parseColor("#DDDDDD"));
                        mBindingMobileSendVercode.setEnabled(false);
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_mobile);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        changeornew = getIntent().getStringExtra("neworchange");
        okHttpSession = new OkHttpSession();
        okHttpSession.ChangeVercodeImage(HttpUrl.NetPic()+HttpUrl.GetPicSession);
        EventBus.getDefault().register(this);

//        GraphicsVerifyPop graphicsVerifyView = new GraphicsVerifyPop(BindingMobileActivity.this);
//        graphicsVerifyView.showPopupWindow();
    }

    @OnClick({R.id.mBinding_mobile_return, R.id.mBinding_mobile_sendVercode, R.id.mBinding_mobile_btn, R.id.mBinding_picVercode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mBinding_mobile_return:
                finish();
                break;
            case R.id.mBinding_mobile_sendVercode:
                //发送验证码
                //sendVercode();
                //发送验证码
                if (TextUtil.isEmpty(mBindingMobileUsername.getText().toString().trim()) || TextUtil.isEmpty(mBindingEtPicVercode.getText().toString().trim())) {
                    ToastUtil.show(getApplicationContext(), "请输入手机号和图形验证码...");
                    return;
                }
                okHttpSession.VercodeServer(mBindingMobileUsername.getText().toString(), mBindingEtPicVercode.getText().toString(), JSESSIONID, HttpUrl.NetPic()+HttpUrl.SendVercode);
                break;
            case R.id.mBinding_picVercode:
                okHttpSession.ChangeVercodeImage(HttpUrl.NetPic()+HttpUrl.GetPicSession);
                break;
            case R.id.mBinding_mobile_btn:
                if (!mBindingMobileVercode.getText().toString().equals("") || !mBindingMobileUsername.getText().toString().equals("")) {
                    bindingMobile();
                } else {
                    ToastUtil.show(getApplicationContext(), "请输入完整信息...");
                }
                break;
        }
    }

    private void bindingMobile() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("mobile", mBindingMobileUsername.getText().toString().trim());
        map.put("code", mBindingMobileVercode.getText().toString().trim());
        if (changeornew.equals("change")) {
            map.put("change", "1");
        }
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.BindingMobile, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onSuccess obj = " + obj);
                            Log.d(TAG, "onSuccess response = " + response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    EventBus.getDefault().post(new MyPurseBindEvent());
                                    finish();
                                    break;
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                case 4006:
                                case 4007:
                                case 4008:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    finish();
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
                Log.d(TAG, "onFailure throwable = " + throwable);

            }
        });
    }

    private void sendVercode() {
        Map<String, String> map = new HashMap<>();
        String NumPhone = mBindingMobileUsername.getText().toString().trim();
        map.put("mobile", NumPhone);
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SendVercode, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d(TAG, "onSuccess obj = " + obj);
                            Log.d(TAG, "onSuccess response = " + response);
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

                Log.d(TAG, "onFailure throwable = " + throwable);

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SetPicEventBus(SessionEvent sessionEvent) {
        JSESSIONID = sessionEvent.getSessionId();
        Bitmap bitmap = BitmapFactory.decodeByteArray(sessionEvent.getPicByte(), 0, sessionEvent.getPicByte().length);
        mBindingPicVercode.setImageBitmap(bitmap);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SetPicEventBus(VercodeEvent vercodeEvent) {
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
                    okHttpSession.ChangeVercodeImage(HttpUrl.NetPic()+HttpUrl.GetPicSession);
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
}
