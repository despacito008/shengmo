package com.aiwujie.shengmo.net;

import java.util.Map;

/**
 * Created by lvzhiwei on 2016/12/11.
 */

public interface IRequestManager {

    void get(String url, IRequestCallback requestCallback);

    void post(String url, Map<String, String> map, IRequestCallback requestCallback);

    void post(String url, String json, IRequestCallback requestCallback);

    void put(String url, Map<String, String> map, IRequestCallback requestCallback);

    void delete(String url, Map<String, String> map, IRequestCallback requestCallback);

}
