package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.zhy.android.percent.support.PercentLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscoveryAboutSMActivity extends AppCompatActivity {

    @BindView(R.id.mDiscoveryAboutSm_return)
    ImageView mDiscoveryAboutSmReturn;
    @BindView(R.id.mDiscoveryAboutSm_ll01)
    PercentLinearLayout mDiscoveryAboutSmLl01;
//    @BindView(R.id.mDiscoveryAboutSm_ll02)
//    PercentLinearLayout mDiscoveryAboutSmLl02;
    @BindView(R.id.mDiscoveryAboutSm_ll03)
    PercentLinearLayout mDiscoveryAboutSmLl03;
//    @BindView(R.id.mDiscoveryAboutSm_ll07)
//    PercentLinearLayout mDiscoveryAboutSmLl07;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_about_sm);
        ButterKnife.bind(this);

    }
// R.id.mDiscoveryAboutSm_ll02, ,  R.id.mDiscoveryAboutSm_ll07
    @OnClick({R.id.mDiscoveryAboutSm_return, R.id.mDiscoveryAboutSm_ll01,R.id.mDiscoveryAboutSm_ll03})
    public void onViewClicked(View view) {
        String state;
        String titlename;
        Intent intent;
        switch (view.getId()) {
            case R.id.mDiscoveryAboutSm_return:
                finish();
                break;
            case R.id.mDiscoveryAboutSm_ll01:
                state = "2";
                titlename = "关于圣魔";
                intent = new Intent(this, WebDiscoveryActivity.class);
                intent.putExtra("titlename", titlename);
                intent.putExtra("state", state);
                startActivity(intent);
                break;
//            case R.id.mDiscoveryAboutSm_ll02:
//                state = "3";
//                titlename = "圣魔文化节";
//                intent = new Intent(this, WebDiscoveryActivity.class);
//                intent.putExtra("titlename", titlename);
//                intent.putExtra("state", state);
//                startActivity(intent);
//                break;
            case R.id.mDiscoveryAboutSm_ll03:
                state = "1";
                titlename = "活动★俱乐部";
                intent = new Intent(this, WebDiscoveryActivity.class);
                intent.putExtra("titlename", titlename);
                intent.putExtra("state", state);
                startActivity(intent);
                break;
//            case R.id.mDiscoveryAboutSm_ll07:
//                state = "4";
//                titlename = "创始人";
//                intent = new Intent(this, WebDiscoveryActivity.class);
//                intent.putExtra("titlename", titlename);
//                intent.putExtra("state", state);
//                startActivity(intent);
//                break;
        }
    }
}
