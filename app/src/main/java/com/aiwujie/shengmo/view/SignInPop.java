package com.aiwujie.shengmo.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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

import razerdp.basepopup.BasePopupWindow;

public class SignInPop extends BasePopupWindow {
    Context context;
    TextView tvSignConfirm, tvSignDays;
    ImageView ivSignClose;
    List<View> signIvList;
    private int signtimes = -1;

    public SignInPop(Context context) {
        super(context);
        this.context = context;
        setPopupGravity(Gravity.CENTER);
        initViews();
        initListener();
        getSignState();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.app_pop_sign_in);
    }

    void initViews() {
        tvSignConfirm = findViewById(R.id.tv_sign_in_sign);
        tvSignDays = findViewById(R.id.tv_sign_in_day);
        ivSignClose = findViewById(R.id.iv_sign_in_close);
        signIvList = new ArrayList<>();
        signIvList.add(findViewById(R.id.iv_day_one));
        signIvList.add(findViewById(R.id.iv_day_two));
        signIvList.add(findViewById(R.id.iv_day_three));
        signIvList.add(findViewById(R.id.iv_day_four));
        signIvList.add(findViewById(R.id.iv_day_five));
        signIvList.add(findViewById(R.id.iv_day_six));
        signIvList.add(findViewById(R.id.iv_day_seven));
    }

    void getSignState() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();

        manager.post(HttpUrl.GetSignTimesInWeeks, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    int signRetcode = obj.getInt("retcode");


                    //Type type =  new TypeToken<NormalResultBean<AttendanceStateBean>>(){}.getType();
                    //NormalResultBean<AttendanceStateBean> data = GsonUtil.getInstance().fromJson(response, type);

                    switch (signRetcode) {
                        case 2001:
                            tvSignConfirm.setText("关闭");
                            JSONObject obj1 = obj.getJSONObject("data");
                            signtimes = obj1.getInt("signtimes");
                            //tvSignDays.setText("已签到" + signtimes + "天");
                            String content = "已签到 " + signtimes + " 天";
                            TextViewUtil.setSpannedSizeBoldText(tvSignDays,content,4,4+String.valueOf(signtimes).length(),17);
                            for (int i = 0; i < signtimes; i++) {
                                signIvList.get(i).setVisibility(View.VISIBLE);
                            }
                            break;
                        case 2002:
                            tvSignConfirm.setText("签到领取");
                            JSONObject obj2 = obj.getJSONObject("data");
                            signtimes = obj2.getInt("signtimes");
                            tvSignDays.setText("已签到" + signtimes + "天");
                            if (signtimes != 0) {
                                for (int i = 0; i < signtimes; i++) {
                                    signIvList.get(i).setVisibility(View.VISIBLE);
                                }
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    void initListener() {
        tvSignConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvSignConfirm.getText().toString().equals("签到领取")) {
                    tvSignConfirm.setClickable(false);
                    switch (signtimes) {
                        case 0:
                        case 1:
                        case 2:
                            signOnDay(signtimes, HttpUrl.SignOnDay1stTo3rd);
                            break;
                        case 3:
                            signOnDay4th(HttpUrl.SignOnDay4th);
                            break;
                        case 4:
                            signOnDay(signtimes, HttpUrl.SignOnDay5th);
                            break;
                        case 5:
                            signOnDay(signtimes, HttpUrl.SignOnDay6th);
                            break;
                        case 6:
                            signOnDay7th(HttpUrl.SignOnDay7th);
                            break;
                        case -1:
                            ToastUtil.show(context,"正在获取签到状态，请稍等");
                            break;
                    }
                } else if (tvSignConfirm.getText().toString().equals("关闭")) {
//                    Intent intent = new Intent(context, SignGiftActivity.class);
//                    intent.putExtra("giftFlag", 2006);
//                    context.startActivity(intent);
                    dismiss();
                }
                tvSignConfirm.setText("关闭");
            }
        });

        ivSignClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dismiss();
            }
        });
    }

    private void signOnDay7th(String httpurl) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(httpurl, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    int retcode = object.getInt("retcode");
                    switch (retcode) {
                        case 2004:
                        case 2005:
                        case 2006:
                            for (int i = 0; i < signtimes + 1; i++) {
                                signIvList.get(i).setVisibility(View.VISIBLE);
                            }
                            String content = "已签到 " + (signtimes + 1) + " 天";
                            TextViewUtil.setSpannedSizeBoldText(tvSignDays,content,4,4+String.valueOf(signtimes).length(),17);
                            Intent intent = new Intent(context, SignGiftActivity.class);
                            intent.putExtra("giftFlag", retcode);
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(0, 0);
                            break;
                        case 3000:
                            ToastUtil.show(context.getApplicationContext(), object.getString("msg"));
                            break;
                    }
                    tvSignConfirm.setClickable(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                try {
                    JSONObject object = new JSONObject(response);
                    int retcode = object.getInt("retcode");
                    switch (retcode) {
                        case 2001:
                        case 2002:
                        case 2003:
                            for (int i = 0; i < signtimes + 1; i++) {
                                signIvList.get(i).setVisibility(View.VISIBLE);
                            }
                            String content = "已签到 " + (signtimes + 1) + " 天";
                            TextViewUtil.setSpannedSizeBoldText(tvSignDays,content,4,4+String.valueOf(signtimes).length(),17);
                            Intent intent = new Intent(context, SignGiftActivity.class);
                            intent.putExtra("giftFlag", retcode);
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(0, 0);
                            break;
                        case 3000:
                            ToastUtil.show(context.getApplicationContext(), object.getString("msg"));
                            break;
                    }
                    tvSignConfirm.setClickable(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            for (int i = 0; i < signtimess + 1; i++) {
                                signIvList.get(i).setVisibility(View.VISIBLE);
                            }
                            String content = "已签到 " + (signtimes + 1) + " 天";
                            TextViewUtil.setSpannedSizeBoldText(tvSignDays,content,4,4+String.valueOf(signtimes).length(),17);
                            Intent intent = new Intent(context, SignGiftActivity.class);
                            intent.putExtra("giftFlag", signtimess);
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(0, 0);
                            break;
                        case 3000:
                            ToastUtil.show(context.getApplicationContext(), obj.getString("msg"));
                            break;
                    }
                    tvSignConfirm.setClickable(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}
