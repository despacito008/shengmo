package com.aiwujie.shengmo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.eventbus.BuyVipSucces;
import com.aiwujie.shengmo.eventbus.WxPaySucBean;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.tencent.liteav.custom.Constents;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    //    private String APP_ID = "wxa3e35bff43fdc8b1";
    private String APP_ID = "wx0392b14b6a6f023c";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        //调用微信支付时 观众端直播会被系统销毁 这里清理一下id
        FloatWindowLayout.getInstance().clearAnchorFloat();
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        Log.d(TAG, " errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                ToastUtil.show(getApplicationContext(), "支付成功");
                EventBus.getDefault().post(new BuyVipSucces());
                EventBus.getDefault().post(new WxPaySucBean());
            } else if (resp.errCode == -2) {
                ToastUtil.show(getApplicationContext(), "取消支付");
            } else {
                ToastUtil.show(getApplicationContext(), "支付失败");
            }
        }
        finish();
    }
}