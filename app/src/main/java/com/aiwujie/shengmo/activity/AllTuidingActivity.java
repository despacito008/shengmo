package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.TopcardCommentAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicDetailBean;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.widget.LinearLayout.SHOW_DIVIDER_NONE;

public class AllTuidingActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.mAllZan_return)
    ImageView mAllZanReturn;
    Handler handler = new Handler();
    int page = 0;
    @BindView(R.id.mAllZan_listview)
    PullToRefreshListView mAllZanListview;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    private List<DynamicDetailBean.DataBean> zans = new ArrayList<>();
    private TopcardCommentAdapter zanAdapter;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tuiding);
        ButterKnife.bind(this);
        setData();
        setListener();
    }

    @OnClick(R.id.mAllZan_return)
    public void onClick() {
        finish();
    }

    private void setData() {
        StatusBarUtil.showLightStatusBar(this);
        mAllZanListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mAllZanListview.setFocusable(false);
        mAllZanListview.setShowDividers(SHOW_DIVIDER_NONE);
    }

    private void setListener() {
        mAllZanListview.setOnRefreshListener(this);
        mAllZanListview.setOnItemClickListener(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAllZanListview.setRefreshing();
            }
        }, 100);
        mAllZanListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isReresh) {
                                page = page + 1;
                                getAllZan();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });
    }

    private void getAllZan() {
        Map<String, String> map = new HashMap<>();
        map.put("fuid", MyApp.uid);
        map.put("page", page + "");
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.getTopcardUsedRs, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DynamicDetailBean listData = new Gson().fromJson(response, DynamicDetailBean.class);
                            if (listData.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    isReresh = false;
                                    ToastUtil.show(getApplicationContext(), "没有更多");
                                } else {
                                    layoutNormalEmpty.setVisibility(View.VISIBLE);
                                }
                            } else {
                                layoutNormalEmpty.setVisibility(View.GONE);
                                isReresh = true;
                                if (page == 0) {
                                    zans.addAll(listData.getData());
                                    zanAdapter = new TopcardCommentAdapter(AllTuidingActivity.this, zans);
                                    mAllZanListview.setAdapter(zanAdapter);
                                } else {
                                    zans.addAll(listData.getData());
                                    zanAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        mAllZanListview.onRefreshComplete();
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (page == 0) {
                    layoutNormalEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        zans.clear();
        if (zanAdapter != null) {
            zanAdapter.notifyDataSetChanged();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllZan();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllZan();
            }
        }, 500);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DynamicDetailActivity.class);
        intent.putExtra("uid", MyApp.uid);
        intent.putExtra("did", zans.get(position - 1).getDid());
        intent.putExtra("pos", position - 1);
        intent.putExtra("showwhat", 1);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpLocationUtils.LogintimeAndLocation();
    }
}
