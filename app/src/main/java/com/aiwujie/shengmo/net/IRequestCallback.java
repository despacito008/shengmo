package com.aiwujie.shengmo.net;

/**
 * Created by lvzhiwei on 2016/12/11.
 */

public interface IRequestCallback {

    void onSuccess(String response);

    void onFailure(Throwable throwable);

}
