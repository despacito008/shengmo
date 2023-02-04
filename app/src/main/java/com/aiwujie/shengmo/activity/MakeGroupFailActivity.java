package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.binding.BindingMobileActivity;
import com.aiwujie.shengmo.activity.binding.ChangeBindingMobileActivity;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BindingData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MakeGroupFailActivity extends AppCompatActivity {
    Handler handler = new Handler();
    @BindView(R.id.mMakeGroupFail_return)
    ImageView mMakeGroupFailReturn;
    @BindView(R.id.mMake_group_bindingPhone)
    PercentLinearLayout mMakeGroupBindingPhone;
    @BindView(R.id.mMake_group_openVip)
    PercentLinearLayout mMakeGroupOpenVip;
    @BindView(R.id.mMake_group_openYearVip)
    PercentLinearLayout mMakeGroupOpenYearVip;
    @BindView(R.id.mMake_group_netRule)
    PercentLinearLayout mMakeGroupNetRule;
    @BindView(R.id.mMake_group_openSvip)
    PercentLinearLayout mMakeGroupOpenSvip;
    @BindView(R.id.mMake_group_openYearSvip)
    PercentLinearLayout mMakeGroupOpenYearSvip;
    private String realmobile = "";
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_group_fail);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        getBindingState();
    }


    @OnClick({R.id.mMakeGroupFail_return, R.id.mMake_group_openVip, R.id.mMake_group_openYearVip, R.id.mMake_group_openSvip, R.id.mMake_group_openYearSvip, R.id.mMake_group_bindingPhone, R.id.mMake_group_netRule})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mMakeGroupFail_return:
                finish();
                break;
            case R.id.mMake_group_bindingPhone:
                if (realmobile.equals("")) {
                    intent = new Intent(this, BindingMobileActivity.class);
                    intent.putExtra("neworchange", "new");
                    startActivity(intent);
                } else {
                    intent = new Intent(this, ChangeBindingMobileActivity.class);
                    intent.putExtra("mobile", mobile);
                    intent.putExtra("realmobile", realmobile);
                    startActivity(intent);
                }
                break;
            case R.id.mMake_group_openVip:
            case R.id.mMake_group_openYearVip:
                intent = new Intent(this, VipCenterActivity.class);
                intent.putExtra("headpic", (String) SharedPreferencesUtils.getParam(getApplicationContext(), "headurl", ""));
                intent.putExtra("uid", MyApp.uid);
                startActivity(intent);
                break;
            case R.id.mMake_group_openSvip:
            case R.id.mMake_group_openYearSvip:
                intent = new Intent(this, VipCenterActivity.class);
                intent.putExtra("headpic", (String) SharedPreferencesUtils.getParam(getApplicationContext(), "headurl", ""));
                intent.putExtra("uid", MyApp.uid);
                startActivity(intent);
                break;
            case R.id.mMake_group_netRule:
                intent = new Intent(this, BannerWebActivity.class);
//                intent.putExtra("path", "http://hao.shengmo.org:888/Home/Info/Shengmosimu/id/1");
                intent.putExtra("path", HttpUrl.NetPic()+"Home/Info/Shengmosimu/id/1");
                intent.putExtra("title", "圣魔");
                startActivity(intent);
                break;
        }
    }

    private void getBindingState() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetBindingState, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    BindingData data = new Gson().fromJson(response, BindingData.class);
                    BindingData.DataBean datas = data.getData();
                    if (!datas.getMobile().equals("")) {
                        realmobile = datas.getMobile();
                        mobile = datas.getMobile().substring(0, 3) + "****" + datas.getMobile().substring(7, datas.getMobile().length());
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}
