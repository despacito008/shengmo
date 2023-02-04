package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 290243232 on 2017/5/16.
 */

public class JudgeVersionUtils {
    private static String downurl;
    public static int retcode=0;
    public static void judgeVersion(final Handler handler, final Context context, final boolean isToast) {
        try {
            String SN=GetDeviceIdUtils.getSN(context);
            Map<String, String> map = new HashMap<>();
            map.put("version", "V" +VersionUtils.getVersion(context));
            map.put("uid", MyApp.uid);
            map.put("device_tag","android");
            map.put("device_token",SN);
            IRequestManager manager = RequestFactory.getRequestManager();
            manager.post(HttpUrl.JudgeVersionNew, map, new IRequestCallback() {
                @Override
                public void onSuccess(final String response) {
                    Log.i("judgeVersion", "onSuccess: " + response);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject obj = new JSONObject(response);
                                retcode=obj.getInt("retcode");
                                switch (retcode) {
                                    case 3001:
                                        JSONObject obj1 = obj.getJSONObject("data");
                                        downurl = obj1.getString("url");
                                        String versionMsg = obj.getString("msg");
//                                        necessity（0：有关闭按钮，1：没有关闭按钮）
                                        String necessity=obj1.getString("necessity");
                                        downDialog(versionMsg,context,necessity);
                                        break;
                                    case 3000:
                                        if(isToast){
                                            ToastUtil.show(context.getApplicationContext(),obj.getString("msg"));
                                        }
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void downDialog(String versionMsg, final Context context,String necessity) {
        View view = View.inflate(context, R.layout.custom_dialog_two_layout, null);
        final android.app.AlertDialog startDialog = new android.app.AlertDialog.Builder(context,R.style.MyDialog).create();
        startDialog.setView(view);
        startDialog.show();
//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Window window = startDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
//        lp.width = dm.widthPixels * 85 / 100;//宽高可设置具体大小
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;//宽高可设置具体大小
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        startDialog.getWindow().setAttributes(lp);
        TextView tvContent = (TextView) view.findViewById(R.id.version_dialog_tv_msg);
        Button tvCancel = (Button) view.findViewById(R.id.version_dialog_cancel);
        Button tvUp = (Button) view.findViewById(R.id.version_dialog_commit);
        tvContent.setText(versionMsg);
        if (necessity.equals("0")) {
            tvCancel.setVisibility(View.VISIBLE);
            startDialog.setCanceledOnTouchOutside(true);// 设置点击屏幕Dialog消失
            startDialog.setCancelable(true);
        } else {
            tvCancel.setVisibility(View.GONE);
            startDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
            startDialog.setCancelable(false);
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog.dismiss();
            }
        });
        tvUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog.dismiss();
                new DownLoadManager(context, downurl).downLoadThread();
            }
        });
    }
}
