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
//设置  隐私页面 相册查看权限页面

public class AlbumJurisdictionActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.mAlbum_Setting_all)
    RadioButton mAlbum_Setting_all;
    @BindView(R.id.mAlbum_Setting_vip)
    RadioButton mAlbum_Setting_vip;
    @BindView(R.id.mAlbum_Setting_group)
    RadioGroup mAlbum_Setting_group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_jurisdiction);
        ButterKnife.bind(this);
        setData();
        setListener();
    }

    private void setData() {   //后台获取数据  判断设置选项   本页面销毁的时候再上传后台选择情况
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        int alertflag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "albumjurisdiction_flag", -1);
        switch (alertflag) {
            case 0://全部
                mAlbum_Setting_all.setChecked(true);
                break;
            case 1://限制
                mAlbum_Setting_vip.setChecked(true);
                break;

        }
    }

    private void setListener() {
        mAlbum_Setting_group.setOnCheckedChangeListener(this);
    }

    @OnClick(R.id.mEdit_name_return)
    public void onClick() {
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.mAlbum_Setting_all:
                SharedPreferencesUtils.setParam(getApplicationContext(), "albumjurisdiction_flag", 0);
//                removeNotification();
                break;
            case R.id.mAlbum_Setting_vip:
                SharedPreferencesUtils.setParam(getApplicationContext(), "albumjurisdiction_flag", 1);
//                removeNotification();
                break;
//            case R.id.mAlert_Setting_ckQuiet:
//                SharedPreferencesUtils.setParam(getApplicationContext(), "alertflag", 2);
////                notificationAlert();
//                break;
        }
    }
}
