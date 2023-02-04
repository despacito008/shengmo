package com.aiwujie.shengmo.activity.binding;

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

public class BindingEmailActivity extends AppCompatActivity {

    @BindView(R.id.mBinding_mobile_return)
    ImageView mBindingMobileReturn;
    @BindView(R.id.mBinding_email_btn)
    Button mBindingEmailBtn;
    @BindView(R.id.mBinding_email_username)
    EditText mBindingEmailUsername;
    @BindView(R.id.mBinding_email_vercode)
    EditText mBindingEmailVercode;
    @BindView(R.id.mBinding_email_sendVercode)
    Button mBindingEmailSendVercode;
    private String changeornew;
    int time=60;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if (time<=0){
                        mBindingEmailSendVercode.setText("点击发送验证码");
                        mBindingEmailSendVercode.setBackgroundResource(R.drawable.item_login_btn2);
                        mBindingEmailSendVercode.setTextColor(Color.parseColor("#FFFFFF"));
                        mBindingEmailSendVercode.setEnabled(true);
                        time=60;
                    }else{
                        mBindingEmailSendVercode.setText("("+time--+"秒后重新获取)");
                        mBindingEmailSendVercode.setBackgroundResource(R.drawable.item_login_hui_btn);
                        mBindingEmailSendVercode.setTextColor(Color.parseColor("#DDDDDD"));
                        mBindingEmailSendVercode.setEnabled(false);
                        handler.sendEmptyMessageDelayed(0,1000);
                    }
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_email);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        changeornew = getIntent().getStringExtra("neworchange");
    }

    @OnClick({R.id.mBinding_mobile_return, R.id.mBinding_email_btn,R.id.mBinding_email_sendVercode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mBinding_mobile_return:
                finish();
                break;
            case R.id.mBinding_email_sendVercode:
                sendVercode();
                break;
            case R.id.mBinding_email_btn:
                if(!mBindingEmailVercode.getText().toString().equals("")||!mBindingEmailUsername.getText().toString().equals("")) {
                    bindingEmail();
                }else{
                    ToastUtil.show(getApplicationContext(),"请输入完整信息...");
                }
                break;
        }
    }

    private void bindingEmail() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("email", mBindingEmailUsername.getText().toString().trim());
        map.put("code", mBindingEmailVercode.getText().toString().trim());
        if (changeornew.equals("change")) {
            map.put("change", "1");
        }
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.BindingEmail, map, new IRequestCallback() {
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
                                    finish();
                                    break;
                                case 4001:
                                case 4002:
                                case 4003:
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
        Map<String,String> map=new HashMap<>();
        map.put("email",mBindingEmailUsername.getText().toString().trim());
        IRequestManager manager=RequestFactory.getRequestManager();
        manager.post(HttpUrl.SendMailCode_reg, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("registemail", "onSuccess: "+response);
                        try{
                            JSONObject obj =new JSONObject(response);
                            switch (obj.getString("retcode")){
                                case "4000":
                                case "4001":
                                case "4002":
                                case "4006":
                                case "4007":
                                    ToastUtil.show(getApplicationContext(),obj.getString("msg"));
                                    break;
                                case "2000":
                                    ToastUtil.show(getApplicationContext(),obj.getString("msg"));
                                    handler.sendEmptyMessage(0);
                                    break;
                            }
                        }catch (Exception e){
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
}
