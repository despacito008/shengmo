package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LiveRuleSettingActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.mdynamicdiction_Setting_all)
    RadioButton mdynamicdiction_Setting_all;
    @BindView(R.id.mdynamicdiction_Setting_vip)
    RadioButton mdynamicdiction_Setting_vip;
    @BindView(R.id.mdynamicdiction_Setting_group)
    RadioGroup mdynamicdiction_Setting_group;
    @BindView(R.id.tv_diction_title)
    TextView tvDictionTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamicdiction);
        ButterKnife.bind(this);

        setData();
        setListener();
    }

    @OnClick(R.id.mEdit_name_return)
    public void onClick() {
        finish();
    }

    private void setListener() {
        mdynamicdiction_Setting_group.setOnCheckedChangeListener(this);
    }

    private void setData() {   //后台获取数据  判断设置选项   本页面销毁的时候再上传后台选择情况
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        tvDictionTitle.setText("直播记录查看权限");
        int alertflag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "livediction_flag", -1);
        switch (alertflag) {
            case 0://全部
                mdynamicdiction_Setting_all.setChecked(true);
                break;
            case 1://限制
                mdynamicdiction_Setting_vip.setChecked(true);
                break;

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.mdynamicdiction_Setting_all:
                SharedPreferencesUtils.setParam(getApplicationContext(), "livediction_flag", 0);
//                removeNotification();
                break;
            case R.id.mdynamicdiction_Setting_vip:
                SharedPreferencesUtils.setParam(getApplicationContext(), "livediction_flag", 1);
//                removeNotification();
                break;

        }
    }
}
