package com.aiwujie.shengmo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;

public class HistnameseeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histnamesee);

        int nickname_rule_flag = (int) SharedPreferencesUtils.getParam(getApplicationContext(), "nickname_rule_flag", -1);

        RadioButton  mAlbum_Setting_all = (RadioButton) findViewById(R.id.mAlbum_Setting_all);
        RadioButton  mAlbum_Setting_vip = (RadioButton) findViewById(R.id.mAlbum_Setting_vip);

        if (nickname_rule_flag == 1){
            mAlbum_Setting_vip.setChecked(true);
        }
        if (nickname_rule_flag ==0 ){
            mAlbum_Setting_all.setChecked(true);
        }

        mAlbum_Setting_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.setParam(getApplicationContext(), "nickname_rule_flag", 0);
            }
        });

        mAlbum_Setting_vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.setParam(getApplicationContext(), "nickname_rule_flag", 1);
            }
        });
    }
}
