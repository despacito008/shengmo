package com.aiwujie.shengmo.net;

import android.icu.text.UFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import com.aiwujie.shengmo.BuildConfig;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.VersionUtils;
import com.alibaba.fastjson.JSONObject;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.aiwujie.shengmo.http.HttpUrl.NetPic;


/**
 * Created by lvzhiwei on 2016/12/11.
 */

public class OkHttpRequestManager implements IRequestManager {

    public static final MediaType TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient okHttpClient;


    public static OkHttpRequestManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final OkHttpRequestManager INSTANCE = new OkHttpRequestManager();
    }

    public OkHttpClient getClient() {
        return okHttpClient;
    }

    public OkHttpRequestManager() {
        if (VersionUtils.isApkInDebug(MyApp.getInstance())) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            // 包含header、body数据
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder()
                    //.proxy(Proxy.NO_PROXY)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            HttpUrl.Builder builder = request.url().newBuilder()
                                    .addQueryParameter("device", Build.MODEL);
                            request = request.newBuilder().url(builder.build()).addHeader("ox", "android")
                                    .addHeader("version", BuildConfig.VERSION_NAME).build();
                            return chain.proceed(request);
                        }
                    })
                    .addInterceptor(loggingInterceptor)
                    .build();
        } else {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    //.proxy(Proxy.NO_PROXY)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            HttpUrl.Builder builder = request.url().newBuilder()
                                    .addQueryParameter("device", Build.MODEL);
                            request = request.newBuilder().url(builder.build()).addHeader("ox", "android")
                                    .addHeader("version", BuildConfig.VERSION_NAME).build();
                            return chain.proceed(request);
                        }
                    })
                    .build();
        }

    }

    public OkHttpClient getHttpClient() {
        return okHttpClient;
    }

    @Override
    public void get(String url, IRequestCallback requestCallback) {
        Request request = new Request.Builder()
                .url(url.startsWith("https://") ? url : com.aiwujie.shengmo.http.HttpUrl.NetPic() + url)
                .addHeader("ox", "android")
                .addHeader("version", BuildConfig.VERSION_NAME)
                .addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", ""))
                .get()
                .build();
        if (url.equals(com.aiwujie.shengmo.http.HttpUrl.GetPicSession)) {
            addCallBack2(requestCallback, request);
        } else {
            addCallBack(requestCallback, request);
        }
    }

    @Override
    public void post(String url, Map<String, String> map, IRequestCallback requestCallback) {
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

        if (TextUtil.isEmpty(tag)) {
           // LogUtil.d("发起无tag网络请求 tag = " + tag);
            Request request = new Request.Builder()
                    .url(url.startsWith("https://") ? url : url.equals(com.aiwujie.shengmo.http.HttpUrl.GetBaseUrl) ? ("http://cs.aiwujie.net/" + url) : (com.aiwujie.shengmo.http.HttpUrl.NetPic() + url))
                    .addHeader("ox", "android")
                    .addHeader("version", BuildConfig.VERSION_NAME)
                    .addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", ""))
                    .addHeader("appname", "shengmo")
                    .post(body)
                    .build();
            addCallBack(requestCallback, request);
        } else {
           // LogUtil.d("发起网络请求 tag = " + tag);
            Request request = new Request.Builder()
                    .tag(tag)
                    .url(url.startsWith("https://") ? url : url.equals(com.aiwujie.shengmo.http.HttpUrl.GetBaseUrl) ? ("http://cs.aiwujie.net/" + url) : (com.aiwujie.shengmo.http.HttpUrl.NetPic() + url))
                    .addHeader("ox", "android")
                    .addHeader("version", BuildConfig.VERSION_NAME)
                    .addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", ""))
                    .addHeader("appname", "shengmo")
                    .post(body)
                    .build();
            addCallBack(requestCallback, request);
        }


    }

    @Override
    public void post(String url, String requestBodyJson, IRequestCallback requestCallback) {
        //获取数据的进程中设置一下进程的优先级 ...
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        //Json类型
        RequestBody body = RequestBody.create(TYPE_JSON, requestBodyJson);

        if (TextUtil.isEmpty(tag)) {
            //LogUtil.d("发起无tag网络请求 tag = " + tag);
            Request request = new Request.Builder()
                    .url(url.startsWith("https://") ? url : url.equals(com.aiwujie.shengmo.http.HttpUrl.GetBaseUrl) ? ("http://cs.aiwujie.net/" + url) : (com.aiwujie.shengmo.http.HttpUrl.NetPic() + url))
                    .addHeader("ox", "android")
                    .addHeader("version", BuildConfig.VERSION_NAME)
                    .addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", ""))
                    .addHeader("appname", "shengmo")
                    .post(body)
                    .build();
            addCallBack2(requestCallback, request);
        } else {
            //LogUtil.d("发起网络请求 tag = " + tag);
            Request request = new Request.Builder()
                    .tag(tag)
                    .url(url.startsWith("https://") ? url : url.equals(com.aiwujie.shengmo.http.HttpUrl.GetBaseUrl) ? ("http://cs.aiwujie.net/" + url) : (com.aiwujie.shengmo.http.HttpUrl.NetPic() + url))
                    .addHeader("ox", "android")
                    .addHeader("version", BuildConfig.VERSION_NAME)
                    .addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", ""))
                    .addHeader("appname", "shengmo")
                    .post(body)
                    .build();
            addCallBack2(requestCallback, request);
        }

    }

    @Override
    public void put(String url, Map<String, String> map, IRequestCallback requestCallback) {
//        RequestBody body = RequestBody.create(TYPE_JSON, requestBodyJson);
        FormBody.Builder builder = new FormBody.Builder();
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            builder.add(entry.getKey(), entry.getValue());
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(url.startsWith("https://") ? url : com.aiwujie.shengmo.http.HttpUrl.NetPic() + url)
                .addHeader("ox", "android")
                .addHeader("version", BuildConfig.VERSION_NAME)
                .addHeader("token", SharedPreferencesUtils.geParam(MyApp.getInstance(), "url_token", ""))
                .addHeader("appname", "shengmo")
                .put(body)
                .build();
        addCallBack(requestCallback, request);
    }

    @Override
    public void delete(String url, Map<String, String> map, IRequestCallback requestCallback) {
//        RequestBody body = RequestBody.create(TYPE_JSON, requestBodyJson);
        FormBody.Builder builder = new FormBody.Builder();
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            builder.add(entry.getKey(), entry.getValue());
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(com.aiwujie.shengmo.http.HttpUrl.NetPic() + url)
                .delete(body)
                .build();
        addCallBack(requestCallback, request);
    }

    private void addCallBack(final IRequestCallback requestCallback, final Request request) {

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (!"Canceled".equals(e.getMessage())) { //忽略网络请求取消的异常
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            requestCallback.onFailure(e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                if (response.isSuccessful()) {
                    try {
                        final String json = response.body().string();
                        if (TextUtil.isEmpty(json)) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    requestCallback.onFailure(new Error());
                                }
                            });
                        }
                        if (json.contains("callback")) {
                            //requestCallback.onSuccess(json);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    requestCallback.onSuccess(json);
                                }
                            });
                        } else {
                            if(!GsonUtil.isJson(json)) return;
                            JSONObject jsonObject = JSONObject.parseObject(json);
                            if (jsonObject == null) {
                                return;
                            }
                            Integer retcode = jsonObject.getInteger("retcode");
                            if (retcode == null) {
                                //requestCallback.onSuccess(json);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        requestCallback.onSuccess(json);
                                    }
                                });
                            } else {
                                switch (retcode) {
                                    case 50001:
                                    case 50002:
                                        LogUtil.d(request.body());
                                        EventBus.getDefault().post(new TokenFailureEvent());
                                        break;
                                    default:
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                requestCallback.onSuccess(json);
                                            }
                                        });
                                        // requestCallback.onSuccess(json);
                                        break;

                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //requestCallback.onFailure(new IOException(response.message() + ",url=" + call.request().url().toString()));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            requestCallback.onFailure(new IOException(response.message() + ",url=" + call.request().url().toString()));
                        }
                    });
                }
            }
        });


//                }.start();
//            }

    }

    private void addCallBack2(final IRequestCallback requestCallback, final Request request) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestCallback.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                if (response.isSuccessful()) {
                    try {
                        final String json = response.body().string();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                requestCallback.onSuccess(json);
                            }
                        });
                    } catch (final IOException e) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                requestCallback.onFailure(e);
                            }
                        });
                    }
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            requestCallback.onFailure(new IOException());
                        }
                    });
                }
            }
        });
    }

    public static Handler mHandler = new Handler(Looper.getMainLooper());

    public static Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }

    String tag = "";
    public void setTag(String tag) {
        this.tag = tag;
        LogUtil.d("okhttp设置tag" + tag);
    }

    public void cancelTag(Object tag) {
        LogUtil.d("okhttp取消tag" + tag);
        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
            LogUtil.d("排队的tag" + call.request().tag());
            if (tag.equals(call.request().tag())) {
                LogUtil.d("取消排队的网络请求");
                call.cancel();
            }
        }
        for (Call call : okHttpClient.dispatcher().runningCalls()) {
            LogUtil.d("进行中的tag" + call.request().tag());
            if (tag.equals(call.request().tag())) {
                LogUtil.d("取消进行中的网络请求");
                call.cancel();
            }
        }
    }

    public void upload(String filePath,final IRequestCallback requestCallback) {
        File file = new File(filePath);
        //String boundary = "----WebKitFormBoundaryzrfXlGEP1Y8Qmzxf";
        // 请求体
//        RequestBody body = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart(
//                        "file",
//                        file.getName(),
//                        MultipartBody.create(MediaType.parse("image/jpeg"), file)
//                ).build();
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "image" + MyApp.uid + SystemClock.currentThreadTimeMillis() + ".jpg", image)
                .build();

        String url = NetPic() + "Api/Api/filePhoto";
        // Post 请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // 执行异步请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestCallback.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                // 上传完毕
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestCallback.onSuccess(result);
                    }
                });
            }
        });
    }

    public void uploadWithWaterMark(String filePath,final IRequestCallback requestCallback) {
        File file = new File(filePath);
        String boundary = "----WebKitFormBoundaryzrfXlGEP1Y8Qmzxf";
        // 请求体
        RequestBody body = new MultipartBody.Builder(boundary)
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "file",
                        file.getName(),
                        MultipartBody.create(MediaType.parse("multipart/form-data"), file)
                ).build();
        String url = NetPic() + "Api/Api/dynamicPicUpload";
        // Post 请求
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // 执行异步请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestCallback.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                // 上传完毕
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestCallback.onSuccess(result);
                    }
                });
            }
        });
    }


}
