package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.Ls_nameViewAdapter;
import com.aiwujie.shengmo.bean.Ls_nameBean;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class history_nameActivity extends AppCompatActivity {

    private Ls_nameViewAdapter ls_nameViewAdapter;
    List<Ls_nameBean.DataBean> dataBeanList = new ArrayList<>();
    Handler handler = new Handler();
    private ListView recyclerView;
    private String uid;
    private TextView wu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_name);


        wu = (TextView) findViewById(R.id.wu);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        recyclerView = (ListView) findViewById(R.id.recycler);
        getll_name();

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void getll_name(){
        Map<String, String> map = new HashMap<>();
        map.put("uid",uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.ls_name, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Ls_nameBean data = new Gson().fromJson(response, Ls_nameBean.class);
                dataBeanList =   data.getData();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(dataBeanList.size()==0){
                            wu.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        ls_nameViewAdapter = new Ls_nameViewAdapter(dataBeanList, history_nameActivity.this);
                        recyclerView.setAdapter(ls_nameViewAdapter);
                        ls_nameViewAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }




}
