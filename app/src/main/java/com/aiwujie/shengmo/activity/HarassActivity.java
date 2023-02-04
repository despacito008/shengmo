package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.HarassData;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.bean.VipSecretSitData;
import com.aiwujie.shengmo.customview.BindSvipDialog;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//消息设置页面
public class HarassActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    public static final String TAG = "HarassActivity";

    @BindView(R.id.mAlert_Setting_return)
    ImageView mAlertSettingReturn;
    @BindView(R.id.mAlert_Setting_all)
    RadioButton mAlert_Setting_ckRing;
    @BindView(R.id.mAlert_Setting_vip)
    RadioButton mAlert_Setting_vip;
    @BindView(R.id.mAlert_Setting_group)
    RadioGroup mAlertSettingRg;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harass);
        ButterKnife.bind(this);

        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        mAlertSettingRg.setOnCheckedChangeListener(this);

        getSecreSit();
    }

    private void getSecreSit() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetVipSecretSit, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            VipSecretSitData vipSecretSitData = new Gson().fromJson(response, VipSecretSitData.class);
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    String char_rule = vipSecretSitData.getData().getChar_rule();
                                    if (char_rule.equals("1")) {  //1是被限制
                                        mAlert_Setting_vip.setChecked(true);
                                    } else {
                                        mAlert_Setting_ckRing.setChecked(true);
                                    }
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.mAlert_Setting_all:
                setSecreSit("0");
                mAlert_Setting_vip.setChecked(false);//vip取消选中
                break;
            case R.id.mAlert_Setting_vip:
                setSecreSit("1");
                mAlert_Setting_ckRing.setChecked(false);
                break;

        }
    }

    private void setSecreSit(String s) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("char_rule", s);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SetVipSecretSit, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HarassData harassData = new Gson().fromJson(response, HarassData.class);
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    //2000表示设置成功了
//                                    if (char_rule.equals("1")) {  //1是被限制
//                                        mAlert_Setting_vip.setChecked(true);
//                                    } else {
//                                        mAlert_Setting_ckRing.setChecked(true);
//                                    }
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
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
    protected void onResume() {
        super.onResume();
        getSecreSit();
    }

    @OnClick(R.id.mAlert_Setting_return)
    public void onClick() {
        finish();
    }


    public void isSVIP() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetUserPowerInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, TAG);
                try {

                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            VipAndVolunteerData data = new Gson().fromJson(response, VipAndVolunteerData.class);
                            String svip = data.getData().getSvip();
                            PrintLogUtils.log(svip, TAG);
                            if (svip.equals("0")) {
                                mAlert_Setting_vip.setChecked(false);
                                mAlert_Setting_vip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        BindSvipDialog.bindAlertDialog(HarassActivity.this, "功能限SVIP可用");
                                        mAlert_Setting_vip.setChecked(false);
                                    }
                                });
                            } else {
                                mAlert_Setting_vip.setChecked(true);
                            }
                            break;
                        case 50001:
                        case 50002:
                            EventBus.getDefault().post(new TokenFailureEvent());
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
}