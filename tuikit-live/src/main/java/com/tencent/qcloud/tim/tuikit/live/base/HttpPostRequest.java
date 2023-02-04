package com.tencent.qcloud.tim.tuikit.live.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;

import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tencent.qcloud.tim.tuikit.live.utils.GsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpPostRequest {
    private HttpPostRequest() {
    }

    public static HttpPostRequest getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final HttpPostRequest INSTANCE = new HttpPostRequest();
    }

    private OkHttpClient okHttpClient;

    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            HttpUrl.Builder builder = request.url().newBuilder()
                                    .addQueryParameter("device", Build.MODEL);
                            request = request.newBuilder().url(builder.build()).addHeader("ox", "android")
                                    .addHeader("version", version).build();
                            return chain.proceed(request);
                        }
                    })
                    .build();
        }
        return okHttpClient;
    }

    public static Handler mHandler = new Handler();

    public static Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }

    public static final MediaType TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    String baseUrl = "";
    String token = "";
    String version = "";

    public void initUrlAndToken(String baseUrl,String token,String version) {
        this.baseUrl = baseUrl;
        this.token = token;
        this.version = version;
    }

    public void post(String url, Map<String, String> map, final TUILiveRequestCallback requestCallBack) {
        //获取数据的进程中设置一下进程的优先级 ...
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        //Json类型
//        RequestBody body = RequestBody.create(TYPE_JSON, requestBodyJson);

        //String类型
        Set<Map.Entry<String, String>> entries = map.entrySet();
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : entries) {
            //Log.i("FragmentNearOkHttp", "key= " + entry.getKey() + " and value= " + entry.getValue());
            if (entry.getValue() != null) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody body = builder.build();

        // LogUtil.d("发起无tag网络请求 tag = " + tag);
        final Request request = new Request.Builder()
                .url(baseUrl + url)
                .addHeader("ox", "android")
                .addHeader("version", version)
                .addHeader("token", token)
                .addHeader("appname", "shengmo")
                .post(body)
                .build();

        getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestCallBack.onError(1000,e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        final String json = response.body().string();
                        if (TextUtils.isEmpty(json)) {
                            requestCallBack.onError(1000,"e.getMessage()");
                        }
                        if (json.contains("callback")) {
                            //requestCallback.onSuccess(json);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    requestCallBack.onSuccess(json);
                                }
                            });
                        } else {
                            if(!GsonUtil.isJson(json)) return;
                            try {
                                final JSONObject jsonObject = new JSONObject(json);
                                if (jsonObject.has("retcode")) {
                                    final int code = jsonObject.getInt("retcode");
                                    switch (code) {
                                        case 2000:
                                            mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    requestCallBack.onSuccess(json);
                                                }
                                            });
                                            break;
                                        default:
                                            mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (TextUtils.isEmpty(jsonObject.optString("msg"))) {
                                                        requestCallBack.onError(code,json);
                                                    } else {
                                                        requestCallBack.onError(code,jsonObject.optString("msg"));
                                                    }
                                                }
                                            });
                                            break;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            requestCallBack.onError(1000, String.valueOf(new IOException(response.message() + ",url=" + call.request().url().toString())));
                        }
                    });
                }
            }
        });

    }

}
