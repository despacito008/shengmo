package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomBannerActivity extends AppCompatActivity {

    @BindView(R.id.mWelcom_banner_iv)
    ImageView mWelcomBannerIv;
    @BindView(R.id.mWelcom_banner_count_down)
    TextView mWelcomBannerCountDown;
    private int time = 5;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (time <= 0) {
                        mWelcomBannerCountDown.setText("0s 跳过");
                        Intent intent = new Intent(WelcomBannerActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        mWelcomBannerCountDown.setText(time-- + "s 跳过");
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
            }
        }
    };
    private String path;
    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom_banner);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        Intent intent=getIntent();
        path=intent.getStringExtra("path");
        url=intent.getStringExtra("url");
        title=intent.getStringExtra("title");
        Glide.with(this).load(path).into(mWelcomBannerIv);
        handler.sendEmptyMessage(0);
    }

    @OnClick({R.id.mWelcom_banner_iv, R.id.mWelcom_banner_count_down})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mWelcom_banner_iv:
                handler.removeMessages(0);
                intent=new Intent(this,BannerWebActivity.class);
                intent.putExtra("bannerflag",0);
                intent.putExtra("title",title);
                intent.putExtra("path",url);
                startActivity(intent);
                finish();
                break;
            case R.id.mWelcom_banner_count_down:
                handler.removeMessages(0);
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
