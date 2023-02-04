package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.RedWomenJyEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
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

public class EditRedWomenJYActivity extends AppCompatActivity {
    @BindView(R.id.mEditRedWomenJy_return)
    ImageView mEditRedWomenJyReturn;
    @BindView(R.id.mEditRedWomenJy_Tijiao)
    TextView mEditRedWomenJyTijiao;
    @BindView(R.id.mEditRedWomenJy_edittext)
    EditText mEditRedWomenJyEdittext;
    private String uid;
    private Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_red_women_jy);
        StatusBarUtil.showLightStatusBar(this);
        ButterKnife.bind(this);
        uid=getIntent().getStringExtra("uid");
        mEditRedWomenJyEdittext.setText(getIntent().getStringExtra("hnjy"));
        mEditRedWomenJyEdittext.setSelection(getIntent().getStringExtra("hnjy").length());
    }

    @OnClick({R.id.mEditRedWomenJy_return, R.id.mEditRedWomenJy_Tijiao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mEditRedWomenJy_return:
                finish();
                break;
            case R.id.mEditRedWomenJy_Tijiao:
                mEditRedWomenJyTijiao.setEnabled(false);
                editMatchMarkerIntroduce(mEditRedWomenJyEdittext.getText().toString());
                break;
        }
    }

    private void editMatchMarkerIntroduce(final String content) {
        Map<String,String> map=new HashMap<>();
        map.put("login_uid", MyApp.uid);
        map.put("uid",uid);
        map.put("match_makerintroduce",content);
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.EditMatchMarkerIntroduce, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj=new JSONObject(response);
                            switch (obj.getInt("retcode")){
                                case 2000:
                                    EventBus.getDefault().post(new RedWomenJyEvent(content));
                                    finish();
                                    break;
                                case 4001:
                                case 4002:
                                    ToastUtil.show(getApplicationContext(),obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                            mEditRedWomenJyTijiao.setEnabled(true);
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
}
