package com.aiwujie.shengmo.activity.binding;

import android.content.Intent;
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
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChargeEmailCodeActivity extends AppCompatActivity {

    @BindView(R.id.mChargeEmailCode_return)
    ImageView mChargeEmailCodeReturn;
    @BindView(R.id.mChargeEmailCode_oldemail)
    TextView mChargeEmailCodeOldemail;
    @BindView(R.id.mChargeEmailCode_vercode)
    EditText mChargeEmailCodeVercode;
    @BindView(R.id.mChargeEmailCode_btnConfirm)
    Button mChargeEmailCodeBtnConfirm;
    @BindView(R.id.mChargeEmailCode_reVercode)
    Button mChargeEmailCodeReVercode;
    private String emails;
    private String realemails;
    int time=60;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if (time<=0){
                        mChargeEmailCodeReVercode.setText("点击发送验证码");
                        mChargeEmailCodeReVercode.setBackgroundResource(R.drawable.item_login_btn2);
                        mChargeEmailCodeReVercode.setTextColor(Color.parseColor("#FFFFFF"));
                        mChargeEmailCodeReVercode.setEnabled(true);
                        time=60;
                    }else{
                        mChargeEmailCodeReVercode.setText("("+time--+"秒后重新获取)");
                        mChargeEmailCodeReVercode.setBackgroundResource(R.drawable.item_login_hui_btn);
                        mChargeEmailCodeReVercode.setTextColor(Color.parseColor("#DDDDDD"));
                        mChargeEmailCodeReVercode.setEnabled(false);
                        handler.sendEmptyMessageDelayed(0,1000);
                    }
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_email_code);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        emails = getIntent().getStringExtra("emails");
        realemails=getIntent().getStringExtra("realemails");
        mChargeEmailCodeOldemail.setText("您的邮箱："+realemails);
        handler.sendEmptyMessage(0);
    }

    @OnClick({R.id.mChargeEmailCode_return, R.id.mChargeEmailCode_btnConfirm, R.id.mChargeEmailCode_reVercode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mChargeEmailCode_return:
                finish();
                break;
            case R.id.mChargeEmailCode_btnConfirm:
                if(!mChargeEmailCodeVercode.getText().toString().equals("")) {
                    chargeMCodeByBinding();
                }else{
                    ToastUtil.show(getApplicationContext(),"请输入验证码...");
                }
                break;
            case R.id.mChargeEmailCode_reVercode:
                sendVerCodeChange();
                break;
        }
    }
    private void chargeMCodeByBinding() {
        Map<String,String> map=new HashMap<>();
        map.put("uid",MyApp.uid);
        map.put("code",mChargeEmailCodeVercode.getText().toString().trim());
        map.put("type","1");
        IRequestManager manager=RequestFactory.getRequestManager();
        manager.post(HttpUrl.ChargeMCodeByBinding, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("chargeMCodeByBinding", "onSuccess: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj=new JSONObject(response);
                            switch (obj.getInt("retcode")){
                                case 2000:
                                    Intent intent=new Intent(ChargeEmailCodeActivity.this,BindingEmailActivity.class);
                                    intent.putExtra("neworchange","change");
                                    startActivity(intent);
                                    finish();
                                    break;
                                case 4000:
                                case 4001:
                                case 4004:
                                    ToastUtil.show(getApplicationContext(),obj.getString("msg"));
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
    private void sendVerCodeChange() {
        Map<String,String> map=new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.SendEmailCode_change, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("sendVerCode_change", "onSuccess: "+response);
                try {
                    JSONObject obj=new JSONObject(response);
                    switch (obj.getInt("retcode")){
                        case 4000:
                        case 4002:
                        case 4003:
                            ToastUtil.show(getApplicationContext(),obj.getString("msg"));
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
}
