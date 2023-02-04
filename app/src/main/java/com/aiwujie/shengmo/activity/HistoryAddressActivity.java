package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.BaseDataList;
import com.aiwujie.shengmo.bean.LocationInfo;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.recycleradapter.HistoryAddressAdapter;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * @author：zq 2021/4/12 14:57
 * 邮箱：80776234@qq.com
 * 历史地址
 */
public class HistoryAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rlvHistoryAddress;
    private String uid;
    private HistoryAddressAdapter historyAddressAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_addresss_activity);
        initView();
        getData();
    }

    private void initView() {
        findViewById(R.id.tv_back).setOnClickListener(this);
        rlvHistoryAddress = (RecyclerView) findViewById(R.id.rlv_history_address);
    }

    private void getData() {

        uid = getIntent().getStringExtra("uid");
        getLocationList(uid);
    }


    private void getLocationList(String uid) {
        Map<String, String> map = new HashMap<>(1);
        map.put("uid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.Userposition, map, new IRequestCallback() {


            @Override
            public void onSuccess(final String response) {

                BaseDataList<LocationInfo> locationInfo = new Gson().fromJson(response, new TypeToken<BaseDataList<LocationInfo>>() {}.getType());

                switch (locationInfo.getRetcode()) {
                    case 2000:
                        historyAddressAdapter = new HistoryAddressAdapter(getApplicationContext(), locationInfo.getData());
                        rlvHistoryAddress.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rlvHistoryAddress.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
                        rlvHistoryAddress.setAdapter(historyAddressAdapter);
                        break;
                    case 3000:
                        break;
                    case 4001:
                        ToastUtil.show(getApplicationContext(), locationInfo.getMsg());
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
