package com.aiwujie.shengmo.utils;

import android.os.Handler;
import android.util.Log;

import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.MyPurseBindEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.HttpCodeMsgListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 290243232 on 2017/7/4.
 */

public class ShareSuccessUtils {

    public static void Shared(final Handler handler){
//        Map<String,String> map=new HashMap<>();
//        map.put("uid", MyApp.uid);
//        IRequestManager manager= RequestFactory.getRequestManager();
//        manager.post(HttpUrl.GetShareStamp, map, new IRequestCallback() {
//            @Override
//            public void onSuccess(final String response) {
//                Log.i("sharesuccess", "onSuccess: "+response);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject object=new JSONObject(response);
//                            switch (object.getInt("retcode")){
//                                case 2000:
//                                    EventBus.getDefault().post(new MyPurseBindEvent());
//                                    break;
//                            }
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//
//            }
//        });

        HttpHelper.getInstance().getStampByShare(new HttpCodeMsgListener() {
            @Override
            public void onSuccess(String data, String msg) {
                EventBus.getDefault().post(new MyPurseBindEvent());
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }
}
