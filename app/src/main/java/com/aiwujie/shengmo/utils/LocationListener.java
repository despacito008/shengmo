package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.util.Log;

import com.aiwujie.shengmo.application.MyApp;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

import java.lang.ref.WeakReference;

/**
 * Created by 290243232 on 2018/2/6.
 */

public class LocationListener implements AMapLocationListener {
    private Context context;
    private WeakReference<AMapLocationClient> weakMLocationClient;

    public LocationListener(Context context, WeakReference<AMapLocationClient> weakMLocationClient) {
        this.context = context;
        this.weakMLocationClient = weakMLocationClient;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                int isOpenLocation = (int) SharedPreferencesUtils.getParam(context, "isOpenLocation", 1);
                if (isOpenLocation == 0) {
                 /*   MyApp.lat = "";
                    MyApp.lng = "";
                    MyApp.city = "";
                    MyApp.address = "";
                    MyApp.province = "";*/

                    //获取纬度
                    MyApp.lat = String.valueOf(aMapLocation.getLatitude());
                    //获取经度
                    MyApp.lng = String.valueOf(aMapLocation.getLongitude());
                    LogUtil.d("获取到经纬度1 " + MyApp.lat + "  "+ MyApp.lng);
                    //地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    MyApp.address = aMapLocation.getAddress();
                    //城市信息
                    MyApp.city = aMapLocation.getCity();
                    //省信息
                    MyApp.province = aMapLocation.getProvince();
                } else {
                    MyApp.lat = String.valueOf(aMapLocation.getLatitude());//获取纬度
                    MyApp.lng = String.valueOf(aMapLocation.getLongitude());//获取经度
                    LogUtil.d("获取到经纬度2 " + MyApp.lat + "  "+ MyApp.lng);
                    MyApp.address = aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    MyApp.city = aMapLocation.getCity();//城市信息
                    MyApp.province = aMapLocation.getProvince();//省信息
//                    Log.i("myapphashcode", "onLocationChanged: "+MyApp.lat.hashCode());
                }
//                if (onLocationComplete != null) {
//                    onLocationComplete.onComplete();
//                }
              //  UpLocationUtils.LogintimeAndLocation();
                weakMLocationClient.get().onDestroy();
                //Log.i("locationchange", "get location suc: "+aMapLocation.getLatitude()+","+aMapLocation.getLongitude());
               // Log.i("locationchange", "onLocationChanged: "+MyApp.lat+","+MyApp.lng);
            }
        } else {
            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
            Log.e("AmapError", "location Error, ErrCode:"
                    + aMapLocation.getErrorCode() + ", errInfo:"
                    + aMapLocation.getErrorInfo());
        }
    }

//    public interface OnLocationComplete {
//        void onComplete();
//    }
//    OnLocationComplete onLocationComplete;
//    public void setOnLocationComplete(OnLocationComplete onLocationComplete) {
//        this.onLocationComplete = onLocationComplete;
//    }
}
