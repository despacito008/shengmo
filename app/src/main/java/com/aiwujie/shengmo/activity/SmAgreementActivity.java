package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;

public class SmAgreementActivity extends AppCompatActivity {

    @BindView(R.id.mRegistEmail_return)
    ImageView mRegistEmailReturn;
    @BindView(R.id.item_title_name)
    TextView itemTitleName;
    @BindView(R.id.sm_agreement_webview)
    WebView smAgreementWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sm_agreement);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
        //设置WebView属性，能够执行Javascript脚本
        smAgreementWebview.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
//        smAgreementWebview.loadUrl("http://59.110.28.150:888/Home/info/regagreement");
        smAgreementWebview.loadUrl(NetPic()+"Home/Info/regagreement");
        //设置Web视图
        smAgreementWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        smAgreementWebview.setWebChromeClient(new WebChromeClient());
    }

    @OnClick(R.id.mRegistEmail_return)
    public void onClick() {
        finish();
    }
}
