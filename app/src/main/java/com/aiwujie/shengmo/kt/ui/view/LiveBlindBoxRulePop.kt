package com.aiwujie.shengmo.kt.ui.view

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import com.aiwujie.shengmo.R
import com.aiwujie.shengmo.http.HttpUrl
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil

class LiveBlindBoxRulePop: Dialog {

    lateinit var  webView: WebView
    lateinit var  ivBack: ImageView

    constructor(context: Context):super(context,R.style.dialog){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_pop_blindbox_rule)
        initParams()
        initView()

    }

    fun  initView(){
        webView = findViewById(R.id.webview)!!
        ivBack = findViewById(R.id.iv_back)!!

        ivBack.setOnClickListener {
            dismiss()
        }

        initWebView()

        webView.loadUrl( HttpUrl.NetPic()+"Home/Gift/blindboxRule")
    }

    private fun initWebView() {
        val ws = webView.settings
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.loadWithOverviewMode = false
        // 保存表单数据
        ws.saveFormData = true
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true)
        ws.builtInZoomControls = true
        ws.displayZoomControls = false
        // 启动应用缓存
        ws.setAppCacheEnabled(true)
        // 设置缓存模式
        ws.cacheMode = WebSettings.LOAD_DEFAULT
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.useWideViewPort = true
        // 缩放比例 1
        webView.setInitialScale(1);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.javaScriptEnabled = true
        //  页面加载好以后，再放开图片
        ws.blockNetworkImage = false
        // 使用localStorage则必须打开
        ws.domStorageEnabled = true

        // WebView是否支持多个窗口。
        ws.setSupportMultipleWindows(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用) */
        //ws.setTextZoom(80);
        webView.webChromeClient =WebChromeClient();
        webView.webViewClient = WebViewClient()
    }

    private fun initParams() {
        val dialogWindow = window
        dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        dialogWindow.setGravity(Gravity.BOTTOM)
        val screenW = UIUtil.getScreenWidth(context)
        val screenH = UIUtil.getScreenHeight(context)
        dialogWindow.setLayout(screenW, screenH)
        val lp = dialogWindow.attributes
        lp.x = 0
        lp.y = 0
        dialogWindow.attributes = lp
    }

}