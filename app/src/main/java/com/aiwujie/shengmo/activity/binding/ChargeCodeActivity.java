package com.aiwujie.shengmo.activity.binding;

import android.content.Intent;
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
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.SessionEvent;
import com.aiwujie.shengmo.eventbus.VercodeEvent;
import com.aiwujie.shengmo.http.HttpUrl;
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

public class ChargeCodeActivity extends AppCompatActivity {

    @BindView(R.id.mChargeCode_return)
    ImageView mChargeCodeReturn;
    @BindView(R.id.mChargeCode_oldmobile)
    TextView mChargeCodeOldmobile;
    @BindView(R.id.mChargeCode_vercode)
    EditText mChargeCodeVercode;
    @BindView(R.id.mChargeCode_btnConfirm)
    Button mChargeCodeBtnConfirm;
    @BindView(R.id.mChargeCode_reVercode)
    Button mChargeCodeReVercode;
    @BindView(R.id.mChargeCode_etPicVercode)
    EditText mChargeCodeEtPicVercode;
    @BindView(R.id.mChargeCode_picVercode)
    ImageView mChargeCodePicVercode;
    private String mobile;
    private String realmobile;
    int time = 60;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (time <= 0) {
                        mChargeCodeReVercode.setText("点击发送验证码");
                        mChargeCodeReVercode.setBackgroundResource(R.drawable.item_login_btn2);
                        mChargeCodeReVercode.setTextColor(Color.parseColor("#FFFFFF"));
                        mChargeCodeReVercode.setEnabled(true);
                        time = 60;
                    } else {
                        mChargeCodeReVercode.setText("(" + time-- + "秒后重新获取)");
                        mChargeCodeReVercode.setBackgroundResource(R.drawable.item_login_hui_btn);
                        mChargeCodeReVercode.setTextColor(Color.parseColor("#DDDDDD"));
                        mChargeCodeReVercode.setEnabled(false);
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
            }

        }
    };
    private OkHttpSession okHttpSession;
    private String JSESSIONID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_code);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        EventBus.getDefault().register(this);
        mobile = getIntent().getStringExtra("mobile");
        realmobile = getIntent().getStringExtra("realmobile");
        mChargeCodeOldmobile.setText("您的原手机：" + mobile);
        okHttpSession = new OkHttpSession();
        okHttpSession.ChangeVercodeImage(HttpUrl.NetPic()+HttpUrl.GetPicSession);
    }

    @OnClick({R.id.mChargeCode_return, R.id.mChargeCode_btnConfirm, R.id.mChargeCode_reVercode,R.id.mChargeCode_picVercode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mChargeCode_return:
                finish();
                break;
            case R.id.mChargeCode_btnConfirm:
                if (!TextUtil.isEmpty(mChargeCodeVercode.getText().toString())&&!TextUtil.isEmpty(mChargeCodeEtPicVercode.getText().toString())) {
                    chargeMCodeByBinding();
                } else {
                    ToastUtil.show(getApplicationContext(), "请输入验证码...");
                }
                break;
            case R.id.mChargeCode_reVercode:
                if (!TextUtil.isEmpty(mChargeCodeEtPicVercode.getText().toString())) {
                    okHttpSession.VercodeServer(MyApp.uid,mChargeCodeEtPicVercode.getText().toString(),JSESSIONID,HttpUrl.NetPic()+HttpUrl.SendVerCode_change);
                } else {
                    ToastUtil.show(getApplicationContext(), "请输入图形验证码...");
                }
                break;
            case R.id.mChargeCode_picVercode:
                okHttpSession.ChangeVercodeImage(HttpUrl.NetPic()+HttpUrl.GetPicSession);
                break;
        }
    }


    private void chargeMCodeByBinding() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("code", mChargeCodeVercode.getText().toString().trim());
        map.put("type", "0");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.ChargeMCodeByBinding, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("chargeMCodeByBinding", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    Intent intent = new Intent(ChargeCodeActivity.this, BindingMobileActivity.class);
                                    intent.putExtra("neworchange", "change");
                                    startActivity(intent);
                                    finish();
                                    break;
                                case 4001:
                                case 4004:
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(SessionEvent sessionEvent) {
        JSESSIONID= sessionEvent.getSessionId();
        Bitmap bitmap = BitmapFactory.decodeByteArray(sessionEvent.getPicByte(), 0, sessionEvent.getPicByte().length);
        mChargeCodePicVercode.setImageBitmap(bitmap);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(VercodeEvent vercodeEvent) {
        try {
            JSONObject obj = new JSONObject(vercodeEvent.getResponse());
            switch (obj.getString("retcode")) {
                case "4000":
                case "4002":
                case "4008":
                case "3000":
                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                    okHttpSession.ChangeVercodeImage(HttpUrl.NetPic()+HttpUrl.GetPicSession);
                    break;
                case "2000":
                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                    handler.sendEmptyMessage(0);
                    break;
                default:
                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
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
