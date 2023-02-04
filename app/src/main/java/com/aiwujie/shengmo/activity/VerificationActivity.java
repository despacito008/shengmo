package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerificationActivity extends AppCompatActivity {

    @BindView(R.id.mVerification_return)
    ImageView mVerificationReturn;
    @BindView(R.id.mVerification_send)
    TextView mVerificationSend;
    @BindView(R.id.mVerification_edittext)
    EditText mVerificationEdittext;
    @BindView(R.id.mVerification_count)
    TextView mVerificationCount;
    private String gid;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        gid=getIntent().getStringExtra(IntentKey.GROUP_ID);
        mVerificationEdittext.addTextChangedListener(new EditTitleChangedListener());
    }

    @OnClick({R.id.mVerification_return, R.id.mVerification_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mVerification_return:
                finish();
                break;
            case R.id.mVerification_send:
                mVerificationSend.setEnabled(false);
                joinGroup();
                break;
        }
    }
    private void joinGroup() {
        Map<String,String> map=new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid",gid);
        map.put("msg",mVerificationEdittext.getText().toString().trim());
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.JoinGroup, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                        try {
                            JSONObject obj=new JSONObject(response);
                            switch (obj.getInt("retcode")){
                                case 2000:
                                    ToastUtil.show(getApplicationContext(),obj.getString("msg"));
                                    EventBus.getDefault().post("joinsuceess");
                                    finish();
                                    break;
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                    ToastUtil.show(getApplicationContext(),obj.getString("msg"));
                                    break;
                            }
                            mVerificationSend.setEnabled(true);
                        } catch (JSONException e) {
                            mVerificationSend.setEnabled(true);
                            e.printStackTrace();
                        }

            }

            @Override
            public void onFailure(Throwable throwable) {
                ToastUtil.show(getApplicationContext(),"网络错误，请稍后再试");
                mVerificationSend.setEnabled(true);
            }
        });
    }
    class EditTitleChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mVerificationCount.setText(s.length()+"/90");
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length()==90) {
                ToastUtil.show(getApplicationContext(),"字数已经达到了限制！");
            }
        }
    };
}
