package com.aiwujie.shengmo.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.eventbus.MyPurseBindEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用商店评分工具类
 * Created by 290243232 on 2017/6/30.
 */

public class GoToAppStoreUtils {

    public static void goToMarket(Context context, String packageName, Handler hanler) {
        if(isAppInstalled(context,"com.tencent.android.qqdownloader")) {
            Uri uri = Uri.parse("market://details?id=" + packageName);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                goToMarket.setPackage("com.tencent.android.qqdownloader");
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//               goToMarket.setClassName("com.tencent.android.qqdownloader", "com.tencent.pangu.link.LinkProxyActivity");
                context.startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
            hanler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //获取评论app以后的邮票
                    getCMAppStamp();
                }
            },5000);

        }else{
            ToastUtil.show(context.getApplicationContext(),"请您安装应用宝...");
        }
    }

    private static void getCMAppStamp() {
        Map<String,String> map=new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetCMAppStamp, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("fragmentStamp", "onSuccess: "+response);
                EventBus.getDefault().post(new MyPurseBindEvent());
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    /*
* check the app is installed
*/
    public static boolean isAppInstalled(Context context,String packagename)
    {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        }catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo ==null){
            //System.out.println("没有安装");
            return false;
        }else{
            //System.out.println("已经安装");
            return true;
        }
    }
}
