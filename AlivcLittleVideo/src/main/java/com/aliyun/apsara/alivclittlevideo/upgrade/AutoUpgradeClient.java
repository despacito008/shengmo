package com.aliyun.apsara.alivclittlevideo.upgrade;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.aliyun.qupaiokhttp.FileDownloadCallback;
import com.aliyun.qupaiokhttp.HttpRequest;
import com.aliyun.qupaiokhttp.StringHttpRequestCallback;
import com.aliyun.svideo.editor.util.FixedToastUtils;
import com.aliyun.svideo.common.widget.CustomProgressDialog;
import com.google.gson.Gson;

import java.io.File;

/**
 * @author cross_ly
 * @date 2018/11/07
 * <p>描述:自动升级相关 -> littlevideo模块
 * **更新策略为强制更新**
 */
public class AutoUpgradeClient {

    /**
     * 请求读写权限的request code
     */
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 10001;
    private static String TAG = AutoUpgradeClient.class.getName();
    /**
     * oss-upgrade文件的URL
     * cdn加速域名
     */
    private static final String UPGRADE_JSON_BASE_URL = "https://vod-download.cn-shanghai.aliyuncs.com";

    public static final String LITTLEVIDEO_JSON = "/apsaravideo-upgrade/android/littlevideo/oss_upgrade_littlevideo.json";//littlevideo
    /**
     * 临时apk文件的文件名头
     */
    private static String sOutputBasePath = Environment.getExternalStorageDirectory() + File.separator + "aliyunVideoLittlevideo";

    private static Context sContext;

    /**
     * 检查升级
     */
    public static void checkUpgrade(final Context context, String jsonName) {
        sContext = context;
        String url = UPGRADE_JSON_BASE_URL + jsonName;
        HttpRequest.get(url, null, new StringHttpRequestCallback() {
            @Override
            protected void onSuccess(String s) {
                super.onSuccess(s);
                Log.i(TAG, "自动升级，servers request success");
                try {
                    UpgradeBean upgradeBean = new Gson().fromJson(s, UpgradeBean.class);
                    int versionCode = getVersionCode(context);
                    Log.i(TAG, "当前版本code = " + versionCode + " ,最新版本code = " + upgradeBean.getVersionCode());
                    if (upgradeBean.getVersionCode() > versionCode) {
                        //提示有升级
                        showHintDialog(upgradeBean);
                    } else {
                        release();
                    }

                } catch (Exception e) {
                    Log.e(TAG, "自动升级，json解析失败");
                    e.printStackTrace();
                    release();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Log.e(TAG, "自动升级，servers request failure + \n + " + msg);
                release();
            }
        });
    }


    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                                          context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 提示有更新
     * @param upgradeBean UpgradeBean
     */
    private static void showHintDialog(final UpgradeBean upgradeBean) {
        if (sContext == null) {
            return;
        }
        Dialog dialog = new AlertDialog.Builder(sContext)
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startDownload(upgradeBean);
            }
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .setMessage(upgradeBean.getDescribe())
        .setTitle("发现新版本，请升级")
        .setCancelable(false)
        .create();
        dialog.show();
    }

    /**
     * 开始下载apk
     * @param upgradeBean UpgradeBean
     */
    private static void startDownload(UpgradeBean upgradeBean) {
        if (sContext == null) {
            return;
        }
        //android 23 权限适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查权限
            if (ContextCompat.checkSelfPermission(sContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //进入到这里代表没有权限.
                ActivityCompat.requestPermissions((Activity) sContext, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                Log.e(TAG, "自动升级 Failure : Permission Not WRITE_EXTERNAL_STORAGE  ");
                return;
            }
        }
        Log.i(TAG, "自动升级,----------------- start ----------------");
        final String outPath = sOutputBasePath + upgradeBean.getVersionCode() + ".apk";
        File file = new File(outPath);

        final CustomProgressDialog progressDialog = new CustomProgressDialog(sContext);
        progressDialog.setMaxProgress(100);
        progressDialog.setMessage(upgradeBean.getDescribe());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("升级中...");
        progressDialog.show();
        HttpRequest.download(upgradeBean.getUrl(), file, new FileDownloadCallback() {
            @Override
            public void onProgress(int progress, long networkSpeed) {
                super.onProgress(progress, networkSpeed);
                progressDialog.setProgress(progress);
                Log.d(TAG, "progress : " + progress);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                Log.e(TAG, "自动升级 : download apk onFailure");
                if (sContext != null) {
                    FixedToastUtils.show(sContext, "下载失败，请检查网络情况重新下载");
                }
                progressDialog.dismiss();
                release();
            }

            @Override
            public void onDone() {
                super.onDone();
                Log.e(TAG, "onDone: ");
                progressDialog.dismiss();
                installApk(outPath);
            }
        });
    }

    /**
     * 安装应用
     */
    private static void installApk(final String outPath) {

        if (sContext != null) {
            //安装应用
            File apkFile = new File(outPath);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            Log.e(TAG, "installApk: ");
            sContext.startActivity(install);
            //跳转安装后，kill进程，取消安装不能继续使用
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * 释放context对象
     */
    private static void release() {
        sContext = null;
    }

}
