package com.aliyun.apsara.alivclittlevideo.net;

import com.aliyun.apsara.alivclittlevideo.net.data.LittleHttpResponse;
import com.aliyun.svideo.common.utils.ThreadUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http Engine
 *
 * @author xlx
 */
public class HttpEngine {


    private ConcurrentHashMap<String, Call> map = new ConcurrentHashMap<>();
    private OkHttpClient okHttpClient;

    private static final int NET_CONNECT_TIME_OUT = 10;
    private static final int NET_READ_TIME_OUT = 10;

    public HttpEngine() {
        okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(NET_CONNECT_TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(NET_READ_TIME_OUT, TimeUnit.SECONDS)
        .build();

    }

    public <T extends LittleHttpResponse> void request(Request request, final Class<T> responseClass,
            final HttpResponseResultCallback<T> callback) {
        Call call = okHttpClient.newCall(request);

        map.put(request.url().toString(), call);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                callbackPost(new Runnable() {

                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onResponse(false, "request failure, error message : " + e.getMessage(), null);
                        }
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();

                try {
                    final T resp = gson.fromJson(result, responseClass);
                    if (resp == null) {
                        return;
                    }

                    String message = resp.message;

                    if (resp.result) {
                        message += (" [code=" + resp.code + "] ");
                    }

                    if (callback != null) {
                        final String finalErrorMessage = message;
                        callbackPost(new Runnable() {
                            @Override
                            public void run() {
                                callback.onResponse(resp.result, finalErrorMessage, resp);
                            }
                        });
                    }
                } catch (Exception e) {
                    onFailure(call, new IOException(e.getMessage()));
                }

            }
        });
    }

    private void callbackPost(Runnable runnable) {
        ThreadUtils.runOnUiThread(runnable);
    }

    public void cancel(String url) {
        if (map != null && map.containsKey(url)) {
            Call remove = map.remove(url);
            remove.cancel();
        }
    }

    public interface HttpResponseResultCallback<T> {
        void onResponse(boolean result, String message, T data);

    }
}