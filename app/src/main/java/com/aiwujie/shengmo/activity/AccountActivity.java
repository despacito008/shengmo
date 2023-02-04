package com.aiwujie.shengmo.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.AccountDeviceData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.recycleradapter.AccountDeviceAdapter;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * @author：zq 2021/4/12 14:57
 * 邮箱：80776234@qq.com
 */
public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rlvAccountDevice;
    private String uid;
    AccountDeviceAdapter accountDeviceAdapter;
    private TextView titleName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        StatusBarUtil.showLightStatusBar(this);
        initView();
        getData();

    }

    private void initView() {
        findViewById(R.id.tv_back).setOnClickListener(this);
        rlvAccountDevice = (RecyclerView) findViewById(R.id.rlv_account_device);
        titleName = (TextView) findViewById(R.id.title_name);
    }

    private void getData() {

        uid = getIntent().getStringExtra("uid");
        String title_str = getIntent().getStringExtra("user_name");
        titleName.setText(title_str);
        getRecordList(uid);
    }


    private void getRecordList(final String uid) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Userdetail, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                AccountDeviceData accountDeviceData = new Gson().fromJson(response, AccountDeviceData.class);
                switch (accountDeviceData.getRetcode()) {
                    case 2000:
                        accountDeviceAdapter = new AccountDeviceAdapter(getApplicationContext(), accountDeviceData.getData(),uid);
                        rlvAccountDevice.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rlvAccountDevice.setAdapter(accountDeviceAdapter);
                        break;
                    case 3000:
                        break;
                    case 4001:
                        ToastUtil.show(getApplicationContext(), accountDeviceData.getMsg());
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            default:
                break;


        }
    }
}
