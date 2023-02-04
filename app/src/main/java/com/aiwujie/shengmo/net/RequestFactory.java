package com.aiwujie.shengmo.net;


public class RequestFactory {

    public static IRequestManager getRequestManager(){
        return OkHttpRequestManager.getInstance();
    }

}
