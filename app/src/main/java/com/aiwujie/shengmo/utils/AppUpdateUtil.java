package com.aiwujie.shengmo.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.MainActivity;
import com.aiwujie.shengmo.activity.SettingActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.kt.ui.activity.HomePageActivity;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AppUpdateUtil {
    Context context;
    private static final AppUpdateUtil INSTANCE = new AppUpdateUtil();
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;

    private static class LazyHolder {
        public static AppUpdateUtil getThis(Context context) {
            INSTANCE.context = context;
            return INSTANCE;
        }
    }

    public static AppUpdateUtil getInstance(Context context) {
        return LazyHolder.getThis(context);
    }

    private AppUpdateUtil(){}

    public void startDownLoad(String apkUrl) {
        initialNotification();
        download(apkUrl);
    }

    /*
     * 方法名：initialNotification()
     * 功    能：初始化通知管理器,创建Notification
     * 参    数：无
     * 返回值：无
     */
    private void initialNotification() {
        //Notification跳转页面
        //Intent notificationIntent = new Intent(context, MainActivity.class);
        Intent notificationIntent = new Intent(context, HomePageActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        //初始化通知管理器
        notificationManager = (NotificationManager) MyApp.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);

        //8.0及以上需要设置好“channelId”（没有特殊要求、唯一即可）、“channelName”（用户看得到的信息）、“importance”（重要等级）这三个重要参数，然后创建到NotificationManager。
        String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
        String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            channel.setSound(null,null);
        }

        //创建Notification
        builder = new NotificationCompat.Builder( MyApp.getInstance());
        builder.setContentTitle("正在更新...") //设置通知标题
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.smallapplogo)//设置通知的小图标(有些手机设置Icon图标不管用，默认图标就是Manifest.xml里的图标)
                .setLargeIcon(BitmapFactory.decodeResource(MyApp.getInstance().getResources(), R.mipmap.applogo)) //设置通知的大图标
                //.setDefaults(Notification.DEFAULT_LIGHTS) //设置通知的提醒方式： 呼吸灯
                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE) //发起Notification后，铃声和震动均只执行一次
                .setPriority(Notification.PRIORITY_MAX) //设置通知的优先级：最大
                .setAutoCancel(false)//设置通知被点击一次是否自动取消
                .setContentText("下载进度:0%")
                .setChannelId(PUSH_CHANNEL_ID)
                .setOnlyAlertOnce(true)
                .setProgress(100, 0, false)//进度最大100，默认是从0开始
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS);

        //构建通知对象
        Notification notification = builder.build();
        notificationManager.notify(1, notification);

//        问题来了,默认情况下,点击了通知栏,会弹出一个新的activity实例，也就是说会重复打开同一个Activity。
//        解决办法是在Manifest.xml里面把此activity的android:launchMode设为singleTop就搞定了。

        //ToastUtil.show(SettingActivity.this,"正在后台下载最新版本");
    }

    /*
     * 方法名： download()
     * 功    能：下载apk，保存到本地，安装apk
     * 参    数：无
     * 返回值：无
     */
    private void download(String apkUrl) {
        if (TextUtil.isEmpty(apkUrl)) {
            //ToastUtil.show(SettingActivity.this, "下载路径不能为空");
            return;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //.url("https://downpack.baidu.com/appsearch_AndroidPhone_v7.9.3(1.0.64.143)_1012271b.apk")
                .url(apkUrl)
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG-失败", e.toString());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplication(), "网络请求失败！", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws FileNotFoundException {
                Log.e("TAG-下载成功", response.code() + "---" + response.body().toString());

                //设置apk存储路径和名称
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/shengmo.apk");

                //保存文件到本地
                localStorage(response, file);
            }
        });
    }

    /*
     * 方法名：localStorage(final Response response, final File file)
     * 功    能：保存文件到本地
     * 参    数：Response response, File file
     * 返回值：无
     */
    private void localStorage(final Response response, final File file) throws FileNotFoundException {
        //拿到字节流
        InputStream is = response.body().byteStream();
        int len = 0;
        final FileOutputStream fos = new FileOutputStream(file);
        byte[] buf = new byte[8 * 1024];
        try {
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                //Log.e("TAG每次写入到文件大小", "onResponse: "+len);
                Log.e("TAG保存到文件进度：", file.length() + "/" + response.body().contentLength());

                //notification进度条和显示内容不断变化，并刷新。
                builder.setProgress(100, (int) (file.length() * 100 / response.body().contentLength()), false);
                builder.setContentText("下载进度:" + (int) (file.length() * 100 / response.body().contentLength()) + "%");
                Notification notification = builder.getNotification();
                notificationManager.notify(1, notification);
            }
            fos.flush();
            fos.close();
            is.close();

            //下载完成，点击通知，安装
            installingAPK(file);

        } catch (IOException e) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplication(), "下载失败！", Toast.LENGTH_SHORT).show();
//                }
//            });
            e.printStackTrace();
        }
    }

    /*
     * 方法名：installingAPK(File file)
     * 功    能：下载完成，点击通知，安装apk,适配安卓6.0,7.0,8.0
     * 参    数：File file
     * 返回值：无
     */
    private void installingAPK(final File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //安卓7.0以上需要在在Manifest.xml里的application里，设置provider路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.aiwujie.shengmo.fileProvider", new File(file.getPath()));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //下载完成后，设置notification为点击一次就关闭，并设置完成标题内容。并设置跳转到安装页面。
        builder.setContentTitle("下载完成")
                .setContentText("点击安装")
                .setAutoCancel(true)//设置通知被点击一次是否自动取消
                .setContentIntent(contentIntent);

        Notification notification = builder.getNotification();
        notificationManager.notify(1, notification);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApp.getInstance().startActivity(intent);
    }

//    private void installApk(File file) {
//        //新下载apk文件存储地址
//        File apkFile = file;
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
//        startActivity(intent);
//        notificationManager.cancel(1);//取消通知
//
//    }
}
