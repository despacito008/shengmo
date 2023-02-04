package com.aiwujie.shengmo.net;

import org.json.JSONException;

public interface HttpCodeListener {
    void onSuccess(String data) throws JSONException;
    void onFail(int code,String msg);
}
