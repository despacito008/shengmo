package com.aiwujie.shengmo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BannerWebActivity extends AppCompatActivity {

    @BindView(R.id.banner_webview)
    WebView bannerWebview;
    @BindView(R.id.mBannerWeb_return)
    ImageView mBannerWebReturn;
    @BindView(R.id.mBannerWeb_title_name)
    TextView mBannerWebTitleName;
    //当bannerFlag等于0的时候，是欢迎页后跳转到广告标识
    private int bannerFlag;

    public static void start(Context context,String path,String title) {
        Intent intent = new Intent(context,BannerWebActivity.class);
        intent.putExtra(IntentKey.PATH,path + "?uid=" + MyApp.uid);
        intent.putExtra(IntentKey.TITLE,title);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_web);
        ButterKnife.bind(this);
       // X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        bannerFlag=getIntent().getIntExtra("bannerflag",-1);
        String url = getIntent().getStringExtra(IntentKey.PATH);

        String title = getIntent().getStringExtra(IntentKey.TITLE);
        bannerWebview.loadUrl(url);
        mBannerWebTitleName.setText(title);
        WebSettings settings = bannerWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);//设定支持缩放
        settings.setUseWideViewPort(true);//设定支持viewport
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
//        settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        bannerWebview.setWebChromeClient(new WebChromeClient());
        bannerWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url == null) return false;
                try {
                    if(url.startsWith("weixin://") || url.startsWith("alipays://") ||
                            url.startsWith("mailto://") || url.startsWith("tel://")
                        //其他自定义的scheme
                            ) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return false;
                }

                //处理http和https开头的url
                view.loadUrl(url);
                return true;
            }


        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        UpLocationUtils.LogintimeAndLocation();
    }

    @OnClick(R.id.mBannerWeb_return)
    public void onViewClicked() {
        if(bannerFlag==-1) {
            finish();
        }else{
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(bannerFlag==-1) {
                finish();
            }else{
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
        return true;
    }
}
