package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.DynamicListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectDynamicActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2{

    @BindView(R.id.mCollectDynamic_return)
    ImageView mCollectDynamicReturn;
    @BindView(R.id.mCollectDynamic_listview)
    PullToRefreshListView mCollectDynamicListview;
    private Handler handler=new Handler();
    private int page=0;
    List<DynamicListData.DataBean> dynamics = new ArrayList<>();
    private DynamicListviewAdapter dynamicAdapter;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh=true;
    private TimeSecondUtils refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_dynamic);
        ButterKnife.bind(this);
       // X_SystemBarUI.initSystemBar(this, R.color.white);
        StatusBarUtil.showLightStatusBar(this);
        setData();
        setListener();
    }

    private void setListener() {
        mCollectDynamicListview.setOnRefreshListener(this);
        mCollectDynamicListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if(IsListviewSlideBottom.isListViewReachBottomEdge(absListView)){
                            if(isReresh) {
                                page = page + 1;
                                getCollectDynamic();
                            }
                        }  else {
                            int first = absListView.getFirstVisiblePosition();
                            int last = absListView.getLastVisiblePosition();
                            for (int j = last; j >= first; j--) {
                                if(j > 0 && dynamics.size() > j - 1 && !TextUtil.isEmpty(dynamics.get(j - 1).getPlayUrl().trim())) {
                                    dynamicAdapter.tryToPlayVideo(absListView,j - first,dynamics.get(j - 1).getPlayUrl().trim());
                                    return;
                                }
                            }
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
        });
    }



    private void getCollectDynamic() {
        Map<String, String> map = new HashMap<>();
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", page + "");
        map.put("loginuid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetCollectDynamicList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("collectdynamic", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final DynamicListData listData = new Gson().fromJson(response, DynamicListData.class);
                            if (listData.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    isReresh=false;
                                }
                                ToastUtil.show(getApplicationContext(), "没有更多");
                                Log.i("fragmentDynamichot", "run: " + "1");
                            } else {
                                isReresh=true;
                                if (page == 0) {
                                    dynamics.addAll(listData.getData());
                                    int retcode = listData.getRetcode();
                                    try {
                                        dynamicAdapter = new DynamicListviewAdapter(CollectDynamicActivity.this, dynamics, retcode,"");
                                        mCollectDynamicListview.setAdapter(dynamicAdapter);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    dynamics.addAll(listData.getData());
                                    dynamicAdapter.notifyDataSetChanged();
                                    Log.i("fragmentDynamichot", "run: " + "3");
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        mCollectDynamicListview.onRefreshComplete();
                        if (refresh != null) {
                            refresh.cancel();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setData() {
        mCollectDynamicListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCollectDynamicListview.setRefreshing();
            }
        },100);
    }

    @OnClick(R.id.mCollectDynamic_return)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        dynamics.clear();
        if (dynamicAdapter != null) {
            dynamicAdapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(this, mCollectDynamicListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getCollectDynamic();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }
}
