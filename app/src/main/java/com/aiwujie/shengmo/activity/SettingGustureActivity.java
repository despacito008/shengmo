package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingGustureActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.mGusture_return)
    ImageView mGustureReturn;
    @BindView(R.id.mGusture_isopen)
    CheckBox mGustureIsopen;
    @BindView(R.id.mGusture_reset_password)
    PercentLinearLayout mGustureResetPassword;
    @BindView(R.id.activity_setting_gusture)
    PercentLinearLayout activitySettingGusture;
    @BindView(R.id.mGusture_set_password)
    TextView mGustureSetPassword;
    private String gusture;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_gusture);
        ButterKnife.bind(this);
       // X_SystemBarUI.initSystemBar(this, R.color.white);
        StatusBarUtil.showLightStatusBar(this);
        EventBus.getDefault().register(this);
        setData();
        setListener();
    }

    private void setData() {
        gusture = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "gusture", "");
        password = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "gusturePsw", "");
        if (!password.equals("")) {
            mGustureSetPassword.setText("重置手势密码");
        }else{
            mGustureSetPassword.setText("设置手势密码");
        }
        if (gusture.equals("") || gusture.equals("0")) {
            SharedPreferencesUtils.setParam(getApplicationContext(), "gusture", "0");
            mGustureIsopen.setChecked(false);
        } else {
            mGustureIsopen.setChecked(true);
        }
    }

    private void setListener() {
        mGustureIsopen.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.mGusture_return, R.id.mGusture_reset_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mGusture_return:
                finish();
                break;
            case R.id.mGusture_reset_password:
                Intent intent = new Intent(this, Lock9ViewActivity.class);
                intent.putExtra("gustureFlag","set");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if(password.equals("")){
                mGustureIsopen.setChecked(false);
                SharedPreferencesUtils.setParam(getApplicationContext(), "gusture", "0");
                ToastUtil.show(getApplicationContext(),"请您先设置手势密码");
            }else {
                SharedPreferencesUtils.setParam(getApplicationContext(), "gusture", "1");
            }
        } else {
            SharedPreferencesUtils.setParam(getApplicationContext(), "gusture", "0");
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(String message){
        if(message.equals("setGustureSuccess")){
            setData();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
