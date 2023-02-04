package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentsPermissionsActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    @BindView(R.id.mdreviewpermissions_Setting_all)
    RadioButton mdreviewpermissions_Setting_all;
    @BindView(R.id.mdreviewpermissions_Setting_vip)
    RadioButton mdreviewpermissions_Setting_vip;
    @BindView(R.id.mdreviewpermissions_Setting_group)
    RadioGroup mdreviewpermissions_Setting_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_permissions);
        ButterKnife.bind(this);

        setData();
        setListener();

    }
    @OnClick(R.id.mEdit_name_return)
    public void onClick() {
        finish();
    }

    private void setListener() {
        mdreviewpermissions_Setting_group.setOnCheckedChangeListener(this);
    }
    private void setData() {   //后台获取数据  判断设置选项   本页面销毁的时候再上传后台选择情况
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        int alertflag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "commentsdiction_flag", -1);
        switch (alertflag) {
            case 0://全部
                mdreviewpermissions_Setting_all.setChecked(true);
//                SharedPreferencesUtils.setParam(getApplicationContext(), "commentsdiction_flag", 0);
                break;
            case 1://限制
                mdreviewpermissions_Setting_vip.setChecked(true);
//                SharedPreferencesUtils.setParam(getApplicationContext(), "commentsdiction_flag", 1);
                break;

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.mdreviewpermissions_Setting_all:
                SharedPreferencesUtils.setParam(getApplicationContext(), "commentsdiction_flag", 0);
//                removeNotification();
                break;
            case R.id.mdreviewpermissions_Setting_vip:
                SharedPreferencesUtils.setParam(getApplicationContext(), "commentsdiction_flag", 1);
//                removeNotification();
                break;

        }
    }
}
