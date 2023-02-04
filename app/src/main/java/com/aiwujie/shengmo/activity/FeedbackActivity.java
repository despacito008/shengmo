package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
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

public class FeedbackActivity extends AppCompatActivity {

    @BindView(R.id.mFeedback_return)
    ImageView mFeedbackReturn;
    @BindView(R.id.mFeedback_Tijiao)
    TextView mFeedbackTijiao;
    @BindView(R.id.mFeedback_content)
    EditText mFeedbackContent;
    @BindView(R.id.mFeedback_count)
    TextView mFeedbackCount;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        mFeedbackContent.addTextChangedListener(new EditTitleChangedListener());
    }

    @OnClick({R.id.mFeedback_return, R.id.mFeedback_Tijiao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mFeedback_return:
                finish();
                break;
            case R.id.mFeedback_Tijiao:
                suggest();
                break;
        }
    }

    private void suggest() {
        Map<String,String> map=new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("suggest",mFeedbackContent.getText().toString().trim());
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.Suggest, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("suggest", "onSuccess: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj=new JSONObject(response);
                            switch (obj.getInt("retcode")){
                                case 2000:
                                    ToastUtil.show(getApplicationContext(),obj.getString("msg"));
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

    class EditTitleChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mFeedbackCount.setText("(" + s.length() + "/256)");
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() == 256) {
                ToastUtil.show(getApplicationContext(), "字数已经达到了限制！");
            }
        }
    };
}
