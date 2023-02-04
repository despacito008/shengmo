package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.util.NormalConstant;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
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

public class EditIntroduceActivity extends AppCompatActivity {

    @BindView(R.id.mEdit_introduce_return)
    ImageView mEditIntroduceReturn;
    @BindView(R.id.mEdit_introduce_Tijiao)
    TextView mEditIntroduceTijiao;
    @BindView(R.id.mEdit_introduce_edittext)
    EditText mEditIntroduceEdittext;
    @BindView(R.id.mEdit_introduce_count)
    TextView mEditIntroduceCount;
    @BindView(R.id.mEdit_introduce_guifan)
    TextView mEditIntroduceGuifan;
    int xianzhi = 256;
    private CharSequence temp = "";//监听前的文本

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_introduce);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        Intent intent = getIntent();
        mEditIntroduceEdittext.setText(intent.getStringExtra("introduce"));
        mEditIntroduceEdittext.addTextChangedListener(new EditTitleChangedListener());
        mEditIntroduceEdittext.setSelection(mEditIntroduceEdittext.getText().length());
        isSVIP();
    }

    @OnClick({R.id.mEdit_introduce_return, R.id.mEdit_introduce_Tijiao,R.id.mEdit_introduce_guifan})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mEdit_introduce_return:
                finish();
                break;
            case R.id.mEdit_introduce_Tijiao:
                if (temp.length() > xianzhi) {
                    ToastUtil.show(getApplicationContext(), "字数已经达到了限制！");
                    return;
                }
                String introduce = mEditIntroduceEdittext.getText().toString();
                intent = new Intent();
                intent.putExtra("introduce", introduce);
                setResult(NormalConstant.RESULT_CODE_OK, intent);
                finish();
                break;
            case R.id.mEdit_introduce_guifan:
                intent = new Intent(this, VipWebActivity.class);
                intent.putExtra("title", "图文规范");
                intent.putExtra("path",HttpUrl.NetPic()+  HttpUrl.PicTextHtml);
                startActivity(intent);
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
            mEditIntroduceCount.setText("(" + s.length() + "/"+xianzhi);
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() > xianzhi) {
                ToastUtil.show(getApplicationContext(), "字数已经达到了限制！");
            }
        }
    }

    public void isSVIP() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetUserPowerInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {

                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            VipAndVolunteerData data = new Gson().fromJson(response, VipAndVolunteerData.class);
                            String svip = data.getData().getSvip();
                            PrintLogUtils.log(svip, "--");
                            if (svip.equals("0")) {

                            }else {
                             xianzhi=500;
                            }
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
