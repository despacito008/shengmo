package com.aiwujie.shengmo.utils;

import okhttp3.Response;

/**
 * Created by 290243232 on 2017/4/11.
 */

public interface IOkhttpUploadCallback {
    void UpSuccessCall(Response response);
    void UpFailCall();
}
