package com.aiwujie.shengmo.utils;

import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.SessionEvent;
import com.aiwujie.shengmo.eventbus.SessionEvent2;
import com.aiwujie.shengmo.eventbus.VercodeEvent;
import com.aiwujie.shengmo.http.HttpUrl;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 290243232 on 2018/3/13.
 */

public class OkHttpSession {

    private OkHttpClient okHttpClient;

    public OkHttpSession() {
        okHttpClient=new OkHttpClient();
    }

    //发送请求获取验证码照片
    public void ChangeVercodeImage(String httpUrl) {
        Request request = new Request.Builder()
                .url(httpUrl)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] byte_image =  response.body().bytes();
                //获取session的操作，session放在cookie头，且取出后含有“；”，取出后为下面的 s （也就是jsesseionid）
                Headers headers = response.headers();
//                Log.i("info_headers", "header " + headers);
                List<String> cookies = headers.values("Set-Cookie");
//                Log.i("info_cookies", "onResponse: " + cookies);
                String session = cookies.get(0);
                String s = session.substring(0, session.indexOf(";"));
                EventBus.getDefault().post(new SessionEvent(s,byte_image));
            }
        });
    }

    //发送请求获取验证码照片
    public void ChangeVercodeImage2(String httpUrl) {
        Request request = new Request.Builder()
                .url(httpUrl)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] byte_image =  response.body().bytes();
                //获取session的操作，session放在cookie头，且取出后含有“；”，取出后为下面的 s （也就是jsesseionid）
                Headers headers = response.headers();
//                Log.i("info_headers", "header " + headers);
                List<String> cookies = headers.values("Set-Cookie");
//                Log.i("info_cookies", "onResponse: " + cookies);
                String session = cookies.get(0);
                String s = session.substring(0, session.indexOf(";"));
                EventBus.getDefault().post(new SessionEvent2(s,byte_image));
            }
        });
    }

    //验证
    public void VercodeServer(String mobile,String code,String JSESSIONID,String httpurl) {
        FormBody body;
        if(httpurl.equals(HttpUrl.NetPic()+HttpUrl.SendVerCode_change)) {
            body = new FormBody.Builder()
                    .add("verify", code)
                    .add("uid", mobile)
                    .build();
        }else {
            body = new FormBody.Builder()
                    .add("verify", code)
                    .add("mobile", mobile)
                    .build();
        }
        final Request request = new Request.Builder()
                .addHeader("cookie",JSESSIONID)
                .addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", ""))
                .url(httpurl)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String responses=response.body().string();
                    EventBus.getDefault().post(new VercodeEvent(responses));
                } else {
                    LogUtil.d(request.body());
                }
            }
        });
    }

}
