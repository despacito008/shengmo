package com.aiwujie.shengmo.activity;

import android.content.Intent;
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
import com.aiwujie.shengmo.bean.SetMarkNameData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditBeizhuActivity extends AppCompatActivity  {


    @BindView(R.id.mEdit_name_return)
    ImageView mEditNameReturn;
    @BindView(R.id.mEdit_name_Tijiao)
    TextView mEditNameTijiao;
    @BindView(R.id.mEdit_name_edittext)
    EditText mEditNameEdittext;
    @BindView(R.id.mEdit_name_count)
    TextView mEditNameCount;
    private String fuid;
    private String etContent;
    Handler handler = new Handler();
    private CharSequence temp="";//监听前的文本

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_beizhu);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        Intent intent = getIntent();
        Intent intent1 = getIntent();
        fuid = intent1.getStringExtra("fuid");
        mEditNameEdittext.setText(intent.getStringExtra("name"));
        mEditNameEdittext.addTextChangedListener(new EditTitleChangedListener());
        mEditNameEdittext.setSelection(mEditNameEdittext.length());
    }

    @OnClick({R.id.mEdit_name_return, R.id.mEdit_name_Tijiao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mEdit_name_return:
                finish();
                break;
            case R.id.mEdit_name_Tijiao:
                etContent = mEditNameEdittext.getText().toString().trim();
                if (temp.length() > 10) {
                    ToastUtil.show(getApplicationContext(), "字数已经达到了限制！");
                }else {
                    setmarkname();
                }
                break;
        }
    }

    class EditTitleChangedListener implements TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mEditNameCount.setText("(" + s.length() + "/10)");
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() > 10) {
                ToastUtil.show(getApplicationContext(), "字数已经达到了限制！");
            }
        }
    }

    public void setmarkname(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", fuid);
        map.put("markname", etContent);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.markName, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {
                    final SetMarkNameData data = new Gson().fromJson(response, SetMarkNameData.class);
                    JSONObject object = new JSONObject(response);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            ToastUtil.show(EditBeizhuActivity.this,""+data.getMsg());
                        }
                    });
                    setResult(200);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    ;
}
