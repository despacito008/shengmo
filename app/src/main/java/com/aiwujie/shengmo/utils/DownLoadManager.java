package com.aiwujie.shengmo.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiwujie.shengmo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 290243232 on 2017/3/11.
 */

public class DownLoadManager {
    private Context mContext;
    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/shengmoapk/";
    private static final String saveFileName = savePath + "shengmo.apk";
    /* 进度条与通知ui刷新的handler和msg常量 */
    private int progress;
    private ProgressBar progressBar;
    private String downurl;
    private boolean interceptFlag = true;
    private TextView tvProgress;
    private AlertDialog dialog;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x123:
                    progressBar.setProgress(progress);
                    tvProgress.setText(progress+"%");
                    break;
                case 0x124: // 安装apk
                    File apkfile = new File(saveFileName);
                    if (!apkfile.exists()) {
                        return;
                    }
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                            "application/vnd.android.package-archive");
                    mContext.startActivity(i);
                    android.os.Process.killProcess(android.os.Process.myPid());
//                    pdu.dismiss();
                    break;
                default:
                    break;
            }
        };
    };

    public DownLoadManager(Context context, String downurl) {
        this.mContext = context;
        this.downurl=downurl;
    }

    public void showDownDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.MyDialog);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View layout = inflater.inflate(R.layout.custom_download_layout, null);//获取自定义布局
        builder.setView(layout);
        progressBar= (ProgressBar) layout.findViewById(R.id.custom_download_dialog_progressBar);
        tvProgress= (TextView) layout.findViewById(R.id.custom_download_dialog_tv);
//        builder.setTitle("正在下载,请不要做任何操作...").setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                interceptFlag=false;
//                dialog.dismiss();
//            }
//        });
        dialog=builder.create();
        dialog.show();
    }

    public void downLoadThread() {
        showDownDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    // 返回的安装包url
                    String apkUrl = downurl;
                    URL url = new URL(apkUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    is = conn.getInputStream();

                    File file = new File(savePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    String apkFile = saveFileName;
                    File ApkFile = new File(apkFile);
                    fos = new FileOutputStream(ApkFile);

                    int count = 0;
                    byte buf[] = new byte[1024];
                    while (interceptFlag) {// 点击取消就停止下载.
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(0x123);
                        if (numread <= 0) {
                            // 下载完成通知安装
                            mHandler.sendEmptyMessage(0x124);
                            dialog.dismiss();
                            break;
                        }
                        fos.write(buf, 0, numread);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null)
                            fos.close();
                        if (is != null)
                            is.close();
                    } catch (Exception e) {

                    } finally {
                        fos = null;
                        is = null;
                    }
                }

            }
        }).start();
    }
}
