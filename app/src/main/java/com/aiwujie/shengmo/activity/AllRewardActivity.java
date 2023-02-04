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
import com.aiwujie.shengmo.adapter.AllDsAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.AllDsData;
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

public class AllRewardActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.mAllDs_return)
    ImageView mAllDsReturn;

    Handler handler = new Handler();
    int page = 0;
    @BindView(R.id.mAllDs_listview)
    PullToRefreshListView mAllDsListview;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    private List<AllDsData.DataBean> rewards = new ArrayList<>();
    private AllDsAdapter dsAdapter;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reward);
        ButterKnife.bind(this);
        setData();
        setListener();
    }

    @OnClick(R.id.mAllDs_return)
    public void onClick() {
        finish();
    }

    private void setData() {
        StatusBarUtil.showLightStatusBar(this);
        mAllDsListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mAllDsListview.setFocusable(false);
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAllDsListview.setRefreshing();
            }
        }, 100);
        mAllDsListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isReresh) {
                                page = page + 1;
                                getAllRewards();
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

    private void setListener() {
        mAllDsListview.setOnRefreshListener(this);
        mAllDsListview.setOnItemClickListener(this);
        mAllDsListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
                //visibleItemCount：当前能看见的列表项个数（小半个也算）
                //totalItemCount：列表项共数
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0 && visibleItemCount != totalItemCount) {
                    if (!mAllDsListview.isShownHeader()) {
                        page = page + 1;
                        getAllRewards();
                    }
                }
            }
        });
    }

    private void getAllRewards() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetRewardedList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            AllDsData listData = new Gson().fromJson(response, AllDsData.class);
                            if (listData.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    isReresh = false;
                                    ToastUtil.show(getApplicationContext(), "没有更多");
                                } else {
                                    layoutNormalEmpty.setVisibility(View.VISIBLE);
                                }
                            } else {
                                isReresh = true;
                                layoutNormalEmpty.setVisibility(View.GONE);
                                if (page == 0) {
                                    rewards.addAll(listData.getData());
                                    dsAdapter = new AllDsAdapter(AllRewardActivity.this, rewards);
                                    mAllDsListview.setAdapter(dsAdapter);
                                } else {
                                    rewards.addAll(listData.getData());
                                    dsAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        mAllDsListview.onRefreshComplete();
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
        rewards.clear();
        if (dsAdapter != null) {
            dsAdapter.notifyDataSetChanged();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllRewards();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllRewards();
            }
        }, 500);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpLocationUtils.LogintimeAndLocation();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DynamicDetailActivity.class);
        intent.putExtra("uid", MyApp.uid);
        intent.putExtra("did", rewards.get(position - 1).getDid());
        intent.putExtra("pos", position - 1);
        intent.putExtra("showwhat", 1);
        startActivity(intent);
    }
}
