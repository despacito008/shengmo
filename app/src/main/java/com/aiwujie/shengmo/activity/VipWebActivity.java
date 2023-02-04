package com.aiwujie.shengmo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VipWebActivity extends AppCompatActivity {

    @BindView(R.id.vip_web_return)
    ImageView vipWebReturn;
    @BindView(R.id.item_title_name)
    TextView itemTitleName;
    @BindView(R.id.vip_web_webview)
    WebView vipWebWebview;

    public static void start(Context context,String title, String path) {
        Intent intent = new Intent(context,VipWebActivity.class);
        intent.putExtra(IntentKey.TITLE,title);
        intent.putExtra(IntentKey.PATH,path);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_web);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
        String url = getIntent().getStringExtra("path");
        String title = getIntent().getStringExtra("title");
        itemTitleName.setText(title);
        vipWebWebview.loadUrl(url);
        vipWebWebview.getSettings().setJavaScriptEnabled(true);
        vipWebWebview.setWebChromeClient(new WebChromeClient());
        vipWebWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @OnClick(R.id.vip_web_return)
    public void onClick() {
        finish();
    }
}
