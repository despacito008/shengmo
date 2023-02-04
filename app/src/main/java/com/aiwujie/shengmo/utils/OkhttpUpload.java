package com.aiwujie.shengmo.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 290243232 on 2017/4/11.
 */

public class OkhttpUpload {
    IOkhttpUploadCallback iOkhttpUploadCallback;

    public OkhttpUpload(IOkhttpUploadCallback iOkhttpUploadCallback) {
        this.iOkhttpUploadCallback = iOkhttpUploadCallback;
    }

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    okhttp3.Request request = chain.request().newBuilder()
                            .build();
                    return chain.proceed(request);
                }
            }).readTimeout(15, TimeUnit.SECONDS)// 设置读取超时时间
            .writeTimeout(15, TimeUnit.SECONDS)// 设置写的超时时间
            .connectTimeout(15, TimeUnit.SECONDS)// 设置连接超时时间
            .build();

    // 上传图片公有方法
    public  void uploadImgAndParameter(Map<String, Object> map,
                                                    String url) {

        // mImgUrls为存放图片的url集合
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() != null) {
                    if (entry.getValue() instanceof File) {
                        File f = (File) entry.getValue();
                        builder.addFormDataPart(entry.getKey(), f.getName(),
                                RequestBody.create(MEDIA_TYPE_PNG, f));
                    } else {
                        builder.addFormDataPart(entry.getKey(), entry
                                .getValue().toString());
                    }
                }
            }
        }
        // 创建RequestBody
        RequestBody body = builder.build();
        final okhttp3.Request request = new okhttp3.Request.Builder().url(url)// 地址
                .post(body)// 添加请求体
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onResponse(Call call, final Response response)
                    throws IOException {
//                final String data = response.body().string();
//                Log.i("okhttpuplode", "上传照片成功-->" + data);
                iOkhttpUploadCallback.UpSuccessCall(response);
                call.cancel();// 上传成功取消请求释放内存
            }

            @Override
            public void onFailure(Call call, final IOException e) {
//                Log.i("okhttpuplode", "上传失败-->" + e.getMessage());
                iOkhttpUploadCallback.UpFailCall();
                call.cancel();// 上传失败取消请求释放内存
            }

        });
    }
}
