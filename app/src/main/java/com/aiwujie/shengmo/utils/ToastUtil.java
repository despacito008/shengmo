package com.aiwujie.shengmo.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.aiwujie.shengmo.application.MyApp;

/**
 * Created by 290243232 on 2016/12/16.
 */
public class ToastUtil {
    public static void show(Context context,String msg){
        Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
    public static void showLong(Context context,String msg){
        Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }
    public static void show(String msg){
        Toast.makeText(MyApp.instance(),msg,Toast.LENGTH_SHORT).show();
    }
    public static void showLong(String msg){
        Toast.makeText(MyApp.instance(),msg,Toast.LENGTH_LONG).show();
    }
}

