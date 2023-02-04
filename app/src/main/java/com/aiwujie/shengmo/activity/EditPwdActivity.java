package com.aiwujie.shengmo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.SessionEvent2;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.VerifyAccountPop;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.jvm.Synchronized;

public class EditPwdActivity extends AppCompatActivity {

    @BindView(R.id.mEditpwd_return)
    ImageView mEditpwdReturn;
    @BindView(R.id.mEditpwd_oldpwd)
    EditText mEditpwdOldpwd;
    @BindView(R.id.mEditpwd_onewpwd)
    EditText mEditpwdOnewpwd;
    @BindView(R.id.mEditpwd_confirm)
    Button mEditpwdConfirm;
    Handler handler = new Handler();
    @BindView(R.id.tv_pwd_title)
    TextView tvPwdTitle;
    private String phone, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pwd);
        ButterKnife.bind(this);
        // X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        if (!TextUtil.isEmpty(getIntent().getStringExtra("passwordStatus")) && "0".equals(getIntent().getStringExtra("passwordStatus"))) {
            tvPwdTitle.setText("设置密码");
            mEditpwdOldpwd.setHint("请输入密码");
            mEditpwdOnewpwd.setHint("请再次确认密码");
        } else {
            tvPwdTitle.setText("修改密码");
            mEditpwdOldpwd.setHint("请输入原密码");
            mEditpwdOnewpwd.setHint("请输入新密码");
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.mEditpwd_return, R.id.mEditpwd_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mEditpwd_return:
                finish();
                break;
            case R.id.mEditpwd_confirm:
                //editPwd();
                // showVerifyPop();
                showVerifyTypePop();
                break;
        }
    }

    void showVerifyTypePop() {
        if (!TextUtil.isEmpty(phone) && !TextUtil.isEmpty(email)) {
            new AlertView("请选择验证方式", null, "取消", null,
                    new String[]{"手机号", "邮箱"},
                    this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position, String data) {
                    if (position == 0) {
                        showVerifyPop(phone);
                    } else {
                        showVerifyPop(email);
                    }
                }
            }).show();
        } else if (!TextUtil.isEmpty(phone)) {
            showVerifyPop(phone);
        } else if (!TextUtil.isEmpty(email)) {
            showVerifyPop(email);
        }
    }

    VerifyAccountPop verifyAccountPop;

    void showVerifyPop(String account) {
        verifyAccountPop = new VerifyAccountPop(EditPwdActivity.this, account, "安全验证", "为了保障您的账户安全，需要进行验证");
        verifyAccountPop.showPopupWindow();
        verifyAccountPop.setOnVerifyCodeCheckListener(new VerifyAccountPop.OnVerifyCodeCheckListener() {
            @Override
            public void OnVerifyCodeCheck(String code) {
                editPwd(code);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(SessionEvent2 sessionEvent) {
        String session = sessionEvent.getSessionId();
        Bitmap bitmap = BitmapFactory.decodeByteArray(sessionEvent.getPicByte(), 0, sessionEvent.getPicByte().length);
        if (verifyAccountPop != null) {
            verifyAccountPop.setSession(session);
            verifyAccountPop.showImg(bitmap);
        }
    }

    private void editPwd(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("oldpwd", mEditpwdOldpwd.getText().toString().trim());
        map.put("newpwd", mEditpwdOnewpwd.getText().toString().trim());
        if (!TextUtil.isEmpty(code)) {
            map.put("code", code);
        }
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.EditPwd, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                if (SafeCheckUtil.isActivityFinish(EditPwdActivity.this)) {
                    return;
                }
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
                        case 4999:
                            ToastUtil.show(getApplicationContext(), obj.getString("msg"));
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
