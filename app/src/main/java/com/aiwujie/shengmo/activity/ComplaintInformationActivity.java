package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.ComplaintInformationAdapter;
import com.aiwujie.shengmo.bean.ComplaintInformatoinBean;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplaintInformationActivity extends AppCompatActivity {

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView rlvComplaintInformation;
    private List<ComplaintInformatoinBean.ComplaintInformatoin> list= new ArrayList<>();
    private int page = 0;
    private String type;
    private String uid;
    private ComplaintInformationAdapter complaintInformationAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_complaint_information);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        uid = intent.getStringExtra("uid");
        String title_str = intent.getStringExtra("user_name");
        StatusBarUtil.showLightStatusBar(this);
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

                page++;
                getRecordList(page, type, uid);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 0;
                getRecordList(page, type, uid);
            }
        });
        rlvComplaintInformation = (RecyclerView) findViewById(R.id.rlv_complaint_information);
        TextView titleName = (TextView) findViewById(R.id.title_name);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleName.setText(title_str);

        if (!TextUtil.isEmpty(type)) {
            getRecordList(page, type, uid);
        }
    }


    private void getRecordList(final int page, final String type, String uid) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("page", page + "");
        map.put("type", type);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.RecordList, map, new IRequestCallback() {


            @Override
            public void onSuccess(final String response) {

                ComplaintInformatoinBean complaintInformatoinBean = new Gson().fromJson(response, ComplaintInformatoinBean.class);
                switch (complaintInformatoinBean.getRetcode()) {
                    case 2000:
                        if (page == 0) {

                            list.clear();
                            list.addAll(complaintInformatoinBean.getData());
                            if (complaintInformationAdapter == null) {
                                complaintInformationAdapter = new ComplaintInformationAdapter(list, type, ComplaintInformationActivity.this);
                                rlvComplaintInformation.setLayoutManager(new LinearLayoutManager(ComplaintInformationActivity.this));
                                DividerItemDecoration itemDecoration = new DividerItemDecoration(ComplaintInformationActivity.this, OrientationHelper.VERTICAL);
                                itemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(ComplaintInformationActivity.this, R.color.colorGray)));
                                rlvComplaintInformation.addItemDecoration(itemDecoration);
                                rlvComplaintInformation.setAdapter(complaintInformationAdapter);
                            } else {
                                complaintInformationAdapter.notifyDataSetChanged();
                            }
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.resetNoMoreData();
                        } else {

                            smartRefreshLayout.finishLoadMore();
                            list.addAll(complaintInformatoinBean.getData());
                            complaintInformationAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 3000:
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                        break;
                    case 4001:
                        ToastUtil.show(getApplicationContext(), complaintInformatoinBean.getMsg());
                        break;
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        page = 0;
        getRecordList(page, type, uid);

    }
}
