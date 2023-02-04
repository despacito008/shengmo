package com.aiwujie.shengmo.net;

public interface HttpCodeMsgListener {
    void onSuccess(String data,String msg);
    void onFail(int code,String msg);
}
