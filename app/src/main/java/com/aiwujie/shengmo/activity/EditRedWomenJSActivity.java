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
import com.aiwujie.shengmo.eventbus.RedWomenJsEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
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

public class EditRedWomenJSActivity extends AppCompatActivity {

    @BindView(R.id.mEditRedWomenJs_return)
    ImageView mEditRedWomenJsReturn;
    @BindView(R.id.mEditRedWomenJs_Tijiao)
    TextView mEditRedWomenJsTijiao;
    @BindView(R.id.mEditRedWomenJs_edittext)
    EditText mEditRedWomenJsEdittext;
    private String position;
    private String redId;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_red_women_js);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        position = getIntent().getStringExtra("position");
        mEditRedWomenJsEdittext.setText(getIntent().getStringExtra("content"));
        mEditRedWomenJsEdittext.setSelection(getIntent().getStringExtra("content").length());
        redId = getIntent().getStringExtra("redid");
    }

    @OnClick({R.id.mEditRedWomenJs_return, R.id.mEditRedWomenJs_Tijiao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mEditRedWomenJs_return:
                finish();
                break;
            case R.id.mEditRedWomenJs_Tijiao:
                mEditRedWomenJsTijiao.setEnabled(false);
                editRemarks(mEditRedWomenJsEdittext.getText().toString());
                break;
        }
    }

    private void editRemarks(final String remarks) {
        Map<String, String> map = new HashMap<>();
        map.put("login_uid", MyApp.uid);
        map.put("id", redId);
        map.put("remarks", remarks);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.EditRemarks, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    EventBus.getDefault().post(new RedWomenJsEvent(Integer.parseInt(position), remarks));
                                    finish();
                                    break;
                                case 4001:
                                case 4002:
                                    ToastUtil.show(getApplicationContext(), object.getString("msg"));
                                    break;
                            }
                            mEditRedWomenJsTijiao.setEnabled(true);
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
