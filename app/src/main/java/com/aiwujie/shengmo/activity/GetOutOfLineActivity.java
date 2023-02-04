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
import com.aiwujie.shengmo.adapter.GetOutOfLineAdapter;
import com.aiwujie.shengmo.bean.PunishmentBean;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetOutOfLineActivity extends AppCompatActivity {

    @BindView(R.id.tv_get_out_of_line_empty)
    TextView tvGetOutOfLineEmpty;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView rlvGetOutOfLine;
    private List<PunishmentBean.DataBean> list = new ArrayList<>();
    private int page = 0;
    private String type;
    private String uid;
    private GetOutOfLineAdapter getOutOfLineAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_out_of_line);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        uid = intent.getStringExtra("uid");
        String title_str = intent.getStringExtra("user_name");
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

                page++;
                getBlockingList(page, type, uid);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 0;
                getBlockingList(page, type, uid);
            }
        });
        rlvGetOutOfLine = (RecyclerView) findViewById(R.id.rlv_get_out_of_line);
        TextView titleName = (TextView) findViewById(R.id.title_name);
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleName.setText(title_str);
        if (!TextUtil.isEmpty(type)) {
            getBlockingList(page, type, uid);
        }
    }


    private void getBlockingList(final int page, String type, String uid) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("page", page + "");
        map.put("type", type);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.BlockingList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                PunishmentBean getOutOfLineData = new Gson().fromJson(response, PunishmentBean.class);
                switch (getOutOfLineData.getRetcode()) {
                    case 2000:
                        if (page == 0) {
                            list.clear();
                            list.addAll(getOutOfLineData.getData());
                            if (getOutOfLineAdapter == null) {
                                getOutOfLineAdapter = new GetOutOfLineAdapter(list, GetOutOfLineActivity.this);
                                rlvGetOutOfLine.setLayoutManager(new LinearLayoutManager(GetOutOfLineActivity.this));
                                DividerItemDecoration itemDecoration = new DividerItemDecoration(GetOutOfLineActivity.this, OrientationHelper.VERTICAL);
                                itemDecoration.setDrawable(new ColorDrawable(ContextCompat.getColor(GetOutOfLineActivity.this, R.color.colorGray)));
                                rlvGetOutOfLine.addItemDecoration(itemDecoration);
                                rlvGetOutOfLine.setAdapter(getOutOfLineAdapter);
                            } else {
                                getOutOfLineAdapter.notifyDataSetChanged();
                            }
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.resetNoMoreData();
                        } else {

                            smartRefreshLayout.finishLoadMore();
                            list.addAll(getOutOfLineData.getData());
                            getOutOfLineAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 3000:
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                        if (page == 0) {
                            tvGetOutOfLineEmpty.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 4001:
                        ToastUtil.show(getApplicationContext(), getOutOfLineData.getMsg());
                        break;
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                if (page == 0) {
                    tvGetOutOfLineEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6) {
            page = 0;
            getBlockingList(page, type, uid);
        }

    }
}
