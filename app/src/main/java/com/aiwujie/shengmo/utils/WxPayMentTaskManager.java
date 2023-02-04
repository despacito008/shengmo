package com.aiwujie.shengmo.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.WeChatOrderBean;
import com.aiwujie.shengmo.http.HttpUrl;
import com.alibaba.fastjson.JSON;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 290243232 on 2018/2/23.
 */

public class WxPayMentTaskManager {
    private String httpurl;
    private String APP_ID = "wx0392b14b6a6f023c";
    private IWXAPI api;
    private String payType;

    public WxPayMentTaskManager(Activity context, String httpurl, String payJson) {
        this.httpurl = httpurl;
        if (AppIsInstallUtils.isWeChatAvailable(context)) {
            api = WXAPIFactory.createWXAPI(context, APP_ID);
            new PayMentTask().execute(new PayRequest(payJson));
        } else {
            ToastUtil.show(context.getApplicationContext(), "您未安装微信");
        }
    }

    public WxPayMentTaskManager(Activity context, String httpurl, String payJson, String payType) {
        this.httpurl = httpurl;
        this.payType = payType;
        if (AppIsInstallUtils.isWeChatAvailable(context)) {
            api = WXAPIFactory.createWXAPI(context, APP_ID);
            new PayMentTask().execute(new PayRequest(payJson));
        } else {
            ToastUtil.show(context.getApplicationContext(), "您未安装微信");
        }
    }

    public WxPayMentTaskManager(Context context) {
        api = WXAPIFactory.createWXAPI(context, APP_ID);
    }


    private class PayMentTask extends AsyncTask<PayRequest, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(PayRequest... params) {
            PayRequest payrequest = params[0];
            String data = null;
            String json = payrequest.payObjectStr;
            try {
                if (!TextUtil.isEmpty(payType)) {
                    data = postJson(httpurl, json, "");
                } else {
                    data = postJson(httpurl, json);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }


        @Override
        protected void onPostExecute(final String data) {
            if (null == data) {
                return;
            }
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject json = new JSONObject(data);
                        PayReq req = new PayReq();
                        req.appId = json.getString("appid");
                        req.partnerId = json.getString("partnerid");
                        req.prepayId = json.getString("prepayid");
                        req.nonceStr = json.getString("noncestr");
                        req.timeStamp = json.getString("timestamp");
                        req.packageValue = json.getString("package");
                        req.sign = json.getString("sign");
                        req.extData = "app data"; // optional
                        Log.d("jim", "send return :" + api.sendReq(req));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread payThread = new Thread(payRunnable);
            payThread.start();
            Log.i("WxpayRespone", "onPostExecute: " + data);
        }
    }

    private String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(HttpUrl.NetPic() + url)
                .addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", ""))
                .post(body).build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    private String postJson(String url, String json, String payType) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(type, json);
        com.alibaba.fastjson.JSONObject jo = JSON.parseObject(json);
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("token", jo.getString("token"));
        builder.add("bag_id", jo.getString("bag_id"));
        FormBody body = builder.build();
        Request request = new Request.Builder().url(HttpUrl.NetPic() + url)
                .addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", ""))
                .post(body).build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    class PayRequest {
        String payObjectStr;

        public PayRequest(String payObjectStr) {
            this.payObjectStr = payObjectStr;
        }

    }

    public void payByWeChat(WeChatOrderBean orderBean) {
        LogUtil.d("微信支付");
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayReq req = new PayReq();
                req.appId = orderBean.getAppid();
                req.partnerId = orderBean.getPartnerid();
                req.prepayId = orderBean.getPrepayid();
                req.nonceStr = orderBean.getNoncestr();
                req.timeStamp = orderBean.getTimestamp();
                req.packageValue = orderBean.getPackageX();
                req.sign = orderBean.getSign();
                req.extData = "app data"; // optional
                api.sendReq(req);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

}
