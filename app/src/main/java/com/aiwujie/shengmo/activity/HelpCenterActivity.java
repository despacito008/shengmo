package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.zhy.android.percent.support.PercentLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpCenterActivity extends AppCompatActivity {

    @BindView(R.id.mHelp_center_return)
    ImageView mHelpCenterReturn;
    @BindView(R.id.mHelp_center_help)
    PercentLinearLayout mHelpCenterHelp;
    @BindView(R.id.mHelp_center_agreement)
    PercentLinearLayout mHelpCenterAgreement;
    @BindView(R.id.mHelp_center_guifan)
    PercentLinearLayout mHelpCenterGuifan;
    @BindView(R.id.mHelp_center_secret)
    PercentLinearLayout mHelpCenterSecret;
    @BindView(R.id.mHelp_center_contact)
    PercentLinearLayout mHelpCenterContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        StatusBarUtil.showLightStatusBar(this);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.mHelp_center_return, R.id.mHelp_center_help, R.id.mHelp_center_agreement, R.id.mHelp_center_guifan, R.id.mHelp_center_secret, R.id.mHelp_center_contact})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mHelp_center_return:
                finish();
                break;
            case R.id.mHelp_center_help:
                intent = new Intent(this, VipWebActivity.class);
                intent.putExtra("title", "帮助");
                intent.putExtra("path", HttpUrl.NetPic()+ HttpUrl.Help);
                startActivity(intent);
                break;
            case R.id.mHelp_center_agreement:
                intent=new Intent(this,SmAgreementActivity.class);
                startActivity(intent);
                break;
            case R.id.mHelp_center_guifan:
                intent = new Intent(this, VipWebActivity.class);
                intent.putExtra("title", "图文规范");
                intent.putExtra("path",HttpUrl.NetPic()+ HttpUrl.PicTextHtml);
                startActivity(intent);
                break;
            case R.id.mHelp_center_secret:
                intent = new Intent(this, VipWebActivity.class);
                intent.putExtra("title", "隐私协议");
                intent.putExtra("path", HttpUrl.NetPic()+ HttpUrl.SecretHtml);
                startActivity(intent);
                break;
            case R.id.mHelp_center_contact:
                intent = new Intent(this, ContactUsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
