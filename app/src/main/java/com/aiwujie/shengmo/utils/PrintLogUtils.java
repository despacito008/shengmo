package com.aiwujie.shengmo.utils;

import android.util.Log;

/**
 * Created by 290243232 on 2017/4/19.
 */

public class PrintLogUtils {
    public static void log(String response,String tag){
        if(response.length() > 4000) {
            for(int i=0;i<response.length();i+=4000){
                if(i+4000<response.length())
                    Log.i(tag+i,response.substring(i, i+4000));
                else
                    Log.i(tag+i,response.substring(i, response.length()));
            }
        } else
            Log.i("resinfo",response);
    }
}
