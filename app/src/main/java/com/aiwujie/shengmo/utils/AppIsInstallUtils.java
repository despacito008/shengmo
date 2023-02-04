package com.aiwujie.shengmo.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.aiwujie.shengmo.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;

/**
 * Created by mac on 16/10/13.
 */
public class AppIsInstallUtils {
    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isWeChatAvailable(Context context) {
        IWXAPI wxApi = WXAPIFactory.createWXAPI(context, WXEntryActivity.APP_ID);
        boolean bIsWXAppInstalledAndSupported = wxApi .isWXAppInstalled() && wxApi.isWXAppSupportAPI();
        if (!bIsWXAppInstalledAndSupported) {
            final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equals("com.tencent.mm")) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }
    public static boolean isWeiBoAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.sina.weibo")) {
                    return true;
                }
            }
        }
        return false;
    }
}
