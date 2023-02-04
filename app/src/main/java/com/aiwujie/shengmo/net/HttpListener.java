package com.aiwujie.shengmo.net;

public interface HttpListener {
    void onSuccess(String data);
    void onFail(String msg);
}
