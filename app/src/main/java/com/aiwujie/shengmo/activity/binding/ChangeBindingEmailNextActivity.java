package com.aiwujie.shengmo.activity.binding;

import android.content.Intent;
import android.os.Bundle;
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

public class ChangeBindingEmailNextActivity extends AppCompatActivity {

    @BindView(R.id.mChange_Binding_email_next_return)
    ImageView mChangeBindingEmailNextReturn;
    @BindView(R.id.mChange_Binding_email_next_username)
    EditText mChangeBindingEmailNextUsername;
    @BindView(R.id.mChange_Binding_next_btn)
    Button mChangeBindingNextBtn;
    private String emails;
    private String realemails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_binding_email_next);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        emails = getIntent().getStringExtra("emails");
        realemails=getIntent().getStringExtra("realemails");
    }

    @OnClick({R.id.mChange_Binding_email_next_return, R.id.mChange_Binding_next_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mChange_Binding_email_next_return:
                finish();
                break;
            case R.id.mChange_Binding_next_btn:
                if(realemails.equals(mChangeBindingEmailNextUsername.getText().toString().trim())){
                    sendVerCodeChange();
                    Intent intent=new Intent(this,ChargeEmailCodeActivity.class);
                    intent.putExtra("realemails",realemails);
                    startActivity(intent);
                    finish();
                }else{
                    ToastUtil.show(getApplicationContext(),"您输入的邮箱不匹配...");
                }
                break;
        }
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
