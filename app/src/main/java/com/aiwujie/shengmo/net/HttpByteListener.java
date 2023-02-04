package com.aiwujie.shengmo.net;

public interface HttpByteListener {
    void onSuccess(String s,byte[] data);
    void onFail(String msg);
}
