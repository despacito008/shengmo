package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebDiscoveryActivity extends AppCompatActivity {

    @BindView(R.id.mWebDiscovery_return)
    ImageView mWebDiscoveryReturn;
    @BindView(R.id.mWebDiscovery_name)
    TextView mWebDiscoveryName;
    @BindView(R.id.mWebDiscovery_webview)
    WebView mWebDiscoveryWebview;
    private String html5url;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    mWebDiscoveryWebview.loadUrl(html5url);
                    mWebDiscoveryWebview.setWebChromeClient(new WebChromeClient());
                    mWebDiscoveryWebview.setWebViewClient(new WebViewClient(){
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_discovery);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        String title = getIntent().getStringExtra("titlename");
        String state = getIntent().getStringExtra("state");
        mWebDiscoveryName.setText(title);
        WebSettings settings=mWebDiscoveryWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);//设定支持缩放
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);//设定支持viewport
//        settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);// 默认缩放模式
        getFindUrl(state);
    }

    private void getFindUrl(String state) {
        Map<String,String> map=new HashMap<>();
        map.put("type",state);
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetFindUrl, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
//                Log.i("getFindUrl", "onSuccess: "+response);
                try {
                    JSONObject obj=new JSONObject(response);
                    switch (obj.getInt("retcode")){
                        case 2000:
                            JSONObject dataobj=obj.getJSONObject("data");
                            html5url=dataobj.getString("url");
                            handler.sendEmptyMessage(1);
                            break;
                        case 4001:
                            ToastUtil.show(getApplicationContext(),obj.getString("msg"));
                            break;
                        case 50001:
                        case 50002:
                            EventBus.getDefault().post(new TokenFailureEvent());
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @OnClick(R.id.mWebDiscovery_return)
    public void onClick() {
        finish();
    }
}
