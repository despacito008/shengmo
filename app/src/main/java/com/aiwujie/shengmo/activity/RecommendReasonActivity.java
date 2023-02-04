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
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecommendReasonActivity extends AppCompatActivity {

    @BindView(R.id.mRecommendReason_return)
    ImageView mRecommendReasonReturn;
    @BindView(R.id.mRecommendReason_confirm)
    TextView mRecommendReasonConfirm;
    @BindView(R.id.mRecommendReason_edittext)
    EditText mRecommendReasonEdittext;
    @BindView(R.id.mRecommendReason_count)
    TextView mRecommendReasonCount;
    String uid;
    String did;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_reason);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        uid = getIntent().getStringExtra("uid");
        did = getIntent().getStringExtra("did");
        mRecommendReasonEdittext.addTextChangedListener(new EditTitleChangedListener());
    }

    @OnClick({R.id.mRecommendReason_return, R.id.mRecommendReason_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mRecommendReason_return:
                finish();
                break;
            case R.id.mRecommendReason_confirm:
                mRecommendReasonConfirm.setClickable(false);
                recommendDynamic();
                break;
        }
    }

    private void recommendDynamic() {
        Map<String,String> map=new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("did",did);
        map.put("reason",mRecommendReasonEdittext.getText().toString());
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.RecommendDynamic, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("recommendDynamic", "onSuccess: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object=new JSONObject(response);
                            switch (object.getInt("retcode")){
                                case 2000:
                                    ToastUtil.show(getApplicationContext(),"推荐成功，请等待审核~");
                                    finish();
                                    break;
                                case 3001:
                                    ToastUtil.show(getApplicationContext(),"您已经推荐过了~");
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
            mRecommendReasonCount.setText("(" + s.length() + "/256)");
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() == 256) {
                ToastUtil.show(getApplicationContext(), "字数已经达到了限制！");
            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        UpLocationUtils.LogintimeAndLocation();
    }
}
