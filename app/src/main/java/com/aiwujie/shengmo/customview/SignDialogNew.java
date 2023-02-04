package com.aiwujie.shengmo.customview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.SignGiftActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.TextViewUtil;
import com.aiwujie.shengmo.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by mac on 16/9/23.
 */
public class SignDialogNew extends AlertDialog implements View.OnClickListener {
    //    private final EditText et;
    private AlertDialog.Builder alertDialog;
    private Context context;
    private Handler handler = new Handler();

    private List<ImageView> gous;
    private TextView sign;
    private TextView signDays;
    private int signRetcode;
    private int signtimess;
    private AlertDialog signDialog;

    public SignDialogNew(final Context context) {
        super(context);
        this.context = context;

        //判断是否签到  再下一步操作
        //获取签到状态和记录
        getSignTimesInWeeks();
        //初始化数据源
        initDatas(context);
        //获取签到状态和记录
//        getSignTimesInWeeks();
    }


    /**
     * 初始化数据源
     */
    private void initDatas(final Context context) {
        View view = View.inflate(context, R.layout.item_sign_dialog, null);
        signDays = (TextView) view.findViewById(R.id.sign_dialog_signDay);
        ImageView gou01 = (ImageView) view.findViewById(R.id.item_sign_dialog_presentGou01);
        ImageView gou02 = (ImageView) view.findViewById(R.id.item_sign_dialog_presentGou02);
        ImageView gou03 = (ImageView) view.findViewById(R.id.item_sign_dialog_presentGou03);
        ImageView gou04 = (ImageView) view.findViewById(R.id.item_sign_dialog_presentGou04);
        ImageView gou05 = (ImageView) view.findViewById(R.id.item_sign_dialog_presentGou05);
        ImageView gou06 = (ImageView) view.findViewById(R.id.item_sign_dialog_presentGou06);
        ImageView gou07 = (ImageView) view.findViewById(R.id.item_sign_dialog_presentGou07);
        sign = (TextView) view.findViewById(R.id.sign_dialog_sign);
        sign.setOnClickListener(this);
        gous = new ArrayList<>();
        gous.add(gou01);
        gous.add(gou02);
        gous.add(gou03);
        gous.add(gou04);
        gous.add(gou05);
        gous.add(gou06);
        gous.add(gou07);
        alertDialog = new Builder(context, R.style.MyDialog);
        alertDialog.setCancelable(false);
        signDialog = alertDialog.create();
        signDialog.setView(view);
        signDialog.show();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Window window = signDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = dm.widthPixels * 90 / 100;//宽高可设置具体大小
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        signDialog.getWindow().setAttributes(lp);
        signDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog消失

        //此句是不允许背景变灰色
        signDialog.getWindow().setDimAmount(0);
    }

    private void getSignTimesInWeeks() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();

        manager.post(HttpUrl.GetSignTimesInWeeks, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("signdialognew", "run: " + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            signRetcode = obj.getInt("retcode");
                            switch (signRetcode) {
                                case 2001:
                                    sign.setText("关闭");
//                                    sign.setSelected(true);
                                    JSONObject obj1=obj.getJSONObject("data");
                                    int signtimes = obj1.getInt("signtimes");
                                    signDays.setText("已签到"+signtimes+"天");
                                    for (int i=0;i<signtimes;i++){
                                        gous.get(i).setVisibility(View.VISIBLE);
                                    }

//                                    signDialog.dismiss();
//
//                                    ToastUtil.show(getContext(), "已签到");
                                    break;
                                case 2002:
                                    sign.setText("签到领取");
                                    sign.setSelected(false);
                                    JSONObject obj2 = obj.getJSONObject("data");
                                    signtimess = obj2.getInt("signtimes");
                                    signDays.setText("已签到" + signtimess + "天");
                                    if (signtimess != 0) {
                                        for (int i = 0; i < signtimess; i++) {
                                            gous.get(i).setVisibility(View.VISIBLE);
                                        }
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
    }


    @Override
    public void onClick(View v) {
        if (sign.getText().toString().equals("签到领取")) {
            sign.setClickable(false);
            switch (signtimess) {
                case 0:
                case 1:
                case 2:
                    signOnDay(signtimess, HttpUrl.SignOnDay1stTo3rd);
                    break;
                case 3:
                    signOnDay4th(HttpUrl.SignOnDay4th);
                    break;
                case 4:
                    signOnDay(signtimess, HttpUrl.SignOnDay5th);
                    break;
                case 5:
                    signOnDay(signtimess, HttpUrl.SignOnDay6th);
                    break;
                case 6:
                    signOnDay7th(HttpUrl.SignOnDay7th);
                    break;
            }
        } else if (sign.getText().toString().equals("关闭")) {
            signDialog.dismiss();
        }
        sign.setText("关闭");
    }

    private void signOnDay7th(String httpurl) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(httpurl, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            int retcode = object.getInt("retcode");
                            switch (retcode) {
                                case 2004:
                                case 2005:
                                case 2006:
                                    for (int i = 0; i < signtimess + 1; i++) {
                                        gous.get(i).setVisibility(View.VISIBLE);
                                    }
                                    signDays.setText("已签到" + (signtimess + 1) + "天");

//                                    sign.setSelected(true);
//                                    signRetcode=2001;
                                    Intent intent = new Intent(context, SignGiftActivity.class);
                                    intent.putExtra("giftFlag", retcode);
                                    context.startActivity(intent);
                                    ((Activity) context).overridePendingTransition(0, 0);
                                    break;
                                case 3000:
                                    ToastUtil.show(context.getApplicationContext(), object.getString("msg"));
                                    break;
                            }
                            sign.setClickable(true);
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
    }

    private void signOnDay4th(String httpurl) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(httpurl, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            int retcode = object.getInt("retcode");
                            switch (retcode) {
                                case 2001:
                                case 2002:
                                case 2003:
                                    for (int i = 0; i < signtimess + 1; i++) {
                                        gous.get(i).setVisibility(View.VISIBLE);
                                    }
                                    signDays.setText("已签到" + (signtimess + 1) + "天");
                                    Intent intent = new Intent(context, SignGiftActivity.class);
                                    intent.putExtra("giftFlag", retcode);
                                    context.startActivity(intent);
                                    ((Activity) context).overridePendingTransition(0, 0);
                                    break;
                                case 3000:
                                    ToastUtil.show(context.getApplicationContext(), object.getString("msg"));
                                    break;
                            }
                            sign.setClickable(true);
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
    }

    private void signOnDay(final int signtimess, final String httpurl) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        if (signtimess == 0 || signtimess == 1 || signtimess == 2) {
            map.put("type", signtimess + "");
        }
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(httpurl, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("signresponse", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    for (int i = 0; i < signtimess + 1; i++) {
                                        gous.get(i).setVisibility(View.VISIBLE);
                                    }
                                    signDays.setText("已签到" + (signtimess + 1) + "天");

//                                    sign.setSelected(true);
//                                    signRetcode=2001;
                                    Intent intent = new Intent(context, SignGiftActivity.class);
                                    intent.putExtra("giftFlag", signtimess);
                                    context.startActivity(intent);
                                    ((Activity) context).overridePendingTransition(0, 0);
                                    break;
                                case 3000:
                                    ToastUtil.show(context.getApplicationContext(), obj.getString("msg"));
                                    break;
                            }
                            sign.setClickable(true);
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
    }

}

