package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import com.aiwujie.shengmo.activity.PhotoRzActivity;
import com.aiwujie.shengmo.activity.VipCenterActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.customview.BindGuanzhuDialog;
import com.aiwujie.shengmo.eventbus.FollowEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 290243232 on 2017/8/31.
 */

public class TextMoreClickUtils {
    private Context context;
    private String uid;
    private Handler handler;
    public TextMoreClickUtils(Context context, String uid, Handler handler) {
        this.context = context;
        this.uid=uid;
        this.handler=handler;
    }

    /**
     * @param str
     * @return
     */
    public  SpannableStringBuilder addClickablePart(String str,String headStr,String endStr) {
        SpannableString spanStr = new SpannableString(headStr);
        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
        ssb.append(str) ;

        String[] likeUsers = str.split("、");

        if (likeUsers.length > 0) {
            // 最后一个
            for (int i = 0; i < likeUsers.length; i++) {
                final String name = likeUsers[i];
                final int start = str.indexOf(name) + spanStr.length();
                ssb.setSpan(new ClickableSpan() {

                    @Override
                    public void onClick(View widget) {
                        Intent intent;
                        if(name.equals("认证用户")){
                            intent = new Intent(context, PhotoRzActivity.class);
                            context.startActivity(intent);
                        }else if((name.equals("VIP会员"))){
                            String headpic= (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), "headurl", "");
                            intent = new Intent(context, VipCenterActivity.class);
                            intent.putExtra("headpic", headpic);
                            intent.putExtra("uid", MyApp.uid);
                            context.startActivity(intent);
                        }else if(name.equals("互为好友")){
                            follow();
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                         ds.setColor(Color.parseColor("#b73acb")); // 设置文本颜色
                        // 去掉下划线
                        ds.setUnderlineText(false);
                    }

                }, start, start + name.length(), 0);
            }
        }
        return ssb.append(endStr);
    }

    private void follow() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.FollowOneBox, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("personmsgfollow", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    String followFlag = obj.getString("data");
                                    if (followFlag.equals("1")) {
                                        EventBus.getDefault().post(new FollowEvent("1","3"));
                                    } else {
                                        EventBus.getDefault().post(new FollowEvent("2","1"));
                                    }
                                    ToastUtil.showLong(context.getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4002:
                                case 4787:
                                    ToastUtil.show(context.getApplicationContext(), "您已经关注了对方~");
                                    break;
                                case 8881:
                                case 8882:
                                    BindGuanzhuDialog.bindAlertDialog(context,obj.getString("msg"));
                                    break;
                                case 5000:
                                    ToastUtil.show(context, obj.getString("msg")+"");
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
}
