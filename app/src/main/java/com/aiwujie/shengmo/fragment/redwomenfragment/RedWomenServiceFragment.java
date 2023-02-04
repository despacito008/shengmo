package com.aiwujie.shengmo.fragment.redwomenfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.http.HttpUrl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/8/11.
 */

public class RedWomenServiceFragment extends Fragment {

    @BindView(R.id.mRedwomen_service_webview)
    WebView mRedwomenServiceWebview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_redwomen_service, null);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void setData() {
        mRedwomenServiceWebview.setFocusable(false);
        WebSettings settings = mRedwomenServiceWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);//设定支持缩放
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);//设定支持viewport
        mRedwomenServiceWebview.loadUrl(HttpUrl.NetPic()+HttpUrl.HongniangHtml);
        mRedwomenServiceWebview.setWebChromeClient(new WebChromeClient());
        mRedwomenServiceWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }



}
