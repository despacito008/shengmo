package com.aiwujie.shengmo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.PayResultData;
import com.aiwujie.shengmo.eventbus.BuyVipSucces;
import com.aiwujie.shengmo.http.HttpUrl;
import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;

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

public class AliPayMentTaskManager {
    private Activity context;
    private String httpurl;
    private String payType;
    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResultData payResult = new PayResultData((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    LogUtil.d(resultInfo);
                    LogUtil.d(resultStatus);
                    LogUtil.d(payResult.getMemo());
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtil.show(context.getApplicationContext(),"支付成功");
                        EventBus.getDefault().post(new BuyVipSucces());
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.show(context.getApplicationContext(),"支付失败");

                        
                    }
                    break;
                }

            }
        };
    };

    public AliPayMentTaskManager(Activity context) {
        this.context = context;
    }


    public AliPayMentTaskManager(Activity context,String httpurl, String payJson) {
        this.context = context;
        this.httpurl = httpurl;
        new PayMentTask().execute(new PayRequest(payJson));
    }

    public AliPayMentTaskManager(Activity context,String httpurl, String payJson,String  payType) {
        this.context = context;
        this.httpurl = httpurl;
        this.payType = payType;
        new PayMentTask().execute(new PayRequest(payJson));
    }

    private class PayMentTask extends AsyncTask<PayRequest, Void, String>{

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(PayRequest... params) {
            PayRequest payrequest = params[0];
            String data = null;
            String json=payrequest.payObjectStr;
            LogUtil.d(httpurl);
            LogUtil.d(json);
            try {
                if (!TextUtil.isEmpty(payType)){
                    data = postJson(httpurl, json,"");
                }else {
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
            Log.v("onPostExecute",data);
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    PayTask alipay = new PayTask(context);
                    Map<String, String> result = alipay.payV2(data, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            Thread payThread = new Thread(payRunnable);
            payThread.start();


            LogUtil.d(data);
//            Log.i("AlipayRespone", "onPostExecute: "+data);
        }
    }


    private  String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(HttpUrl.NetPic()+url)
                .addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", ""))
                .post(body).build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    private String postJson(String url, String json,String payType) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(type, json);
        com.alibaba.fastjson.JSONObject jo = JSON.parseObject(json);
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("token",jo.getString("token"));
        builder.add("bag_id",jo.getString("bag_id"));
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

    public void payByAli(String data) {
        LogUtil.d("支付宝支付 " + data);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(context);
                Map<String, String> result = alipay.payV2(data, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
