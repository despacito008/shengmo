package com.aiwujie.shengmo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.eventbus.SessionEvent;
import com.aiwujie.shengmo.eventbus.VercodeEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.JudgmentFormatUtils;
import com.aiwujie.shengmo.utils.OkHttpSession;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RetrievePswActivity extends AppCompatActivity {

    @BindView(R.id.mRetrievePsw_return)
    ImageView mRetrievePswReturn;
    @BindView(R.id.item_title_name)
    TextView itemTitleName;
    @BindView(R.id.mRetrievePsw_username)
    EditText mRetrievePswUsername;
    @BindView(R.id.mRetrievePsw_vercode)
    EditText mRetrievePswVercode;
    @BindView(R.id.mRetrievePsw_sendVercode)
    Button mRetrievePswSendVercode;
    @BindView(R.id.mRetrievePsw_password)
    EditText mRetrievePswRepassword;
    @BindView(R.id.mRetrievePsw_btn_confirm)
    Button mRetrievePswBtnConfirm;
    @BindView(R.id.mRetrievePsw_etPicVercode)
    EditText mRetrievePswEtPicVercode;
    @BindView(R.id.mRetrievePsw_picVercode)
    ImageView mRetrievePswPicVercode;
    private OkHttpSession okHttpSession;
    private String JSESSIONID;
    int time = 60;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (time <= 0) {
                        mRetrievePswSendVercode.setText("点击发送验证码");
                        mRetrievePswSendVercode.setBackgroundResource(R.drawable.item_login_btn2);
                        mRetrievePswSendVercode.setTextColor(Color.parseColor("#FFFFFF"));
                        mRetrievePswSendVercode.setEnabled(true);
                        time = 60;
                    } else {
                        mRetrievePswSendVercode.setText("(" + time-- + "秒后重新获取)");
                        mRetrievePswSendVercode.setBackgroundResource(R.drawable.item_login_hui_btn);
                        mRetrievePswSendVercode.setTextColor(Color.parseColor("#DDDDDD"));
                        mRetrievePswSendVercode.setEnabled(false);
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_psw);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        EventBus.getDefault().register(this);
        okHttpSession = new OkHttpSession();
        okHttpSession.ChangeVercodeImage(HttpUrl.NetPic()+HttpUrl.GetPicSession);
    }

    @OnClick({R.id.mRetrievePsw_return, R.id.mRetrievePsw_sendVercode, R.id.mRetrievePsw_btn_confirm,R.id.mRetrievePsw_picVercode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mRetrievePsw_return:
                finish();
                break;
            case R.id.mRetrievePsw_sendVercode:
                String username = mRetrievePswUsername.getText().toString().trim();
                if (!TextUtil.isEmpty(username)) {
                    if (true) {
                        if (JudgmentFormatUtils.isMobile(username)) {
                            if (TextUtil.isEmpty(mRetrievePswEtPicVercode.getText().toString().trim())){
                                ToastUtil.show(getApplicationContext(),"请输入图形验证码...");
                                return;
                            }
                            okHttpSession.VercodeServer(username,mRetrievePswEtPicVercode.getText().toString(),JSESSIONID,HttpUrl.NetPic()+HttpUrl.ForPswVercode);
                        } else {
                            sendEmailVercode();
                        }
                    } else {
                        ToastUtil.show(getApplicationContext(), "用户名格式不正确...");
                    }
                } else {
                    ToastUtil.show(getApplicationContext(), "请输入用户名...");
                }
                break;
            case R.id.mRetrievePsw_btn_confirm:
                if (!TextUtil.isEmpty(mRetrievePswUsername.getText().toString().trim())&&
                        !TextUtil.isEmpty(mRetrievePswEtPicVercode.getText().toString().trim())&&
                        !TextUtil.isEmpty(mRetrievePswVercode.getText().toString().trim())&&
                        !TextUtil.isEmpty(mRetrievePswRepassword.getText().toString().trim())) {
                    confirm();
                } else {
                    ToastUtil.show(getApplicationContext(), "请输入完整信息...");
                }
                break;
            case R.id.mRetrievePsw_picVercode:
                okHttpSession.ChangeVercodeImage(HttpUrl.NetPic()+HttpUrl.GetPicSession);
                break;
        }
    }

    private void confirm() {
        ;
        if ( Pattern.matches("\\s+", mRetrievePswRepassword.getText().toString().trim())){
            ToastUtil.show(getApplicationContext(),"密码中不能包含空格");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("mobile", mRetrievePswUsername.getText().toString().trim());
        map.put("password", mRetrievePswRepassword.getText().toString().trim());
        map.put("code", mRetrievePswVercode.getText().toString().trim());
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.ForPsw, map, new IRequestCallback() {
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
                                case 3000:
                                case 3001:
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                case 4006:
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


    private void sendEmailVercode() {
        Map<String, String> map = new HashMap<>();
        map.put("email", mRetrievePswUsername.getText().toString());
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SendMailCode_forget, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getString("retcode")) {
                                case "4000":
                                case "4002":
                                case "4006":
                                case "4007":
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case "2000":
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    handler.sendEmptyMessage(0);
                                    break;
                            }
                        } catch (Exception e) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(SessionEvent sessionEvent) {
        JSESSIONID= sessionEvent.getSessionId();
        Bitmap bitmap = BitmapFactory.decodeByteArray(sessionEvent.getPicByte(), 0, sessionEvent.getPicByte().length);
        mRetrievePswPicVercode.setImageBitmap(bitmap);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(VercodeEvent vercodeEvent) {
        try {
            JSONObject obj = new JSONObject(vercodeEvent.getResponse());
            switch (obj.getString("retcode")) {
                case "4000":
                case "4001":
                case "4002":
                case "4006":
                case "4007":
                case "4008":
                case "3000":
                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                    okHttpSession.ChangeVercodeImage(HttpUrl.NetPic()+HttpUrl.GetPicSession);
                    break;
                case "2000":
                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                    handler.sendEmptyMessage(0);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
