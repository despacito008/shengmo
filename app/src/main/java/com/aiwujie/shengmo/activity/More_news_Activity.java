package com.aiwujie.shengmo.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.bean.CommonWealthNewsData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.recycleradapter.MoreproblemsAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class More_news_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<CommonWealthNewsData.DataBean> dataBeanList = new ArrayList<>();
    Handler handler = new Handler();
    private MoreproblemsAdapter moreproblemsAdapter;
    int page = 0;
    private SmartRefreshLayout smartRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_news_);

        findViewById(R.id.mStamp_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        moreproblemsAdapter = new MoreproblemsAdapter(this, dataBeanList);
        recyclerView.setAdapter(moreproblemsAdapter);
        getQuestions();
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page=0;
                dataBeanList.clear();
                getQuestions();

            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                getQuestions();
            }
        });

    }

    private void getQuestions() {
        IRequestManager manager = RequestFactory.getRequestManager();
        Map<String, String> map = new HashMap<>();
        map.put("page",page+"");
        manager.post(HttpUrl.getBasicNewsList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    switch (object.getInt("retcode")) {
                                        case 2000:
                                            CommonWealthNewsData commonWealthNewsData = new Gson().fromJson(response, CommonWealthNewsData.class);
                                            dataBeanList.addAll(commonWealthNewsData.getData());
                                            moreproblemsAdapter.notifyDataSetChanged();
                                            smartRefreshLayout.finishRefresh();
                                            smartRefreshLayout.finishLoadMore();
                                            break;
                                    }
                                    break;
                                case 4000:
                                    smartRefreshLayout.finishRefresh();
                                    smartRefreshLayout.finishLoadMore();
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
