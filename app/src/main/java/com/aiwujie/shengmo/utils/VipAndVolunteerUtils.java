package com.aiwujie.shengmo.utils;

import android.content.Context;
import android.os.Handler;

import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 290243232 on 2017/3/22.
 */

public class VipAndVolunteerUtils {
    public static void isVip(final Context context, final Handler handler) {
        Map<String, String> map = new HashMap<>();
        //map.put("uid", MyApp.uid);
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetUserPowerInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    VipAndVolunteerData data = new Gson().fromJson(response, VipAndVolunteerData.class);
                                    if (data.getData().getVip().equals("0")) {
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "vip", "0");
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "ckvip", "0");
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "filterCulture", "");
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "filterShowMoney", "");
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "filterAuthen", "");
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "filterUpCulture", "");
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "filterUpMoney", "");
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "filterShowAge", "");
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "filterUpAge", "");
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "filterLine", "");
                                    } else {
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "vip", "1");
                                    }
                                    SharedPreferencesUtils.setParam(context.getApplicationContext(), "bk_vip_role", data.getData().getBk_vip_role());
                                    SharedPreferencesUtils.setParam(context.getApplicationContext(), "bl_vip_role",  data.getData().getBl_vip_role());


                                    if (data.getData().getIs_volunteer().equals("0")) {
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "volunteer", "0");
                                    } else {
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "volunteer", "1");
                                    }
                                    if (data.getData().getSvip().equals("0")) {
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "svip", "0");
                                    } else {
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "svip", "1");
                                    }
                                    if (data.getData().getIs_admin().equals("0")) {
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "admin", "0");
                                    } else {
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "admin", "1");
                                    }
                                    if (data.getData().getRealname().equals("0")) {
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "realname", "0");
                                    } else {
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "realname", "1");
                                    }
                                    if ("0".equals(data.getData().getBkvip())) {
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "bkvip", "0");
                                    } else {
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "bkvip", "1");
                                    }
                                    if ("0".equals(data.getData().getBlvip())) {
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "blvip", "0");
                                    } else {
                                        SharedPreferencesUtils.setParam(context.getApplicationContext(), "blvip", "1");
                                    }
                                    break;
                                case 4000:
                                    MyApp.uid = (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), "uid", "");
                                    isVip(context, handler);
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
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
