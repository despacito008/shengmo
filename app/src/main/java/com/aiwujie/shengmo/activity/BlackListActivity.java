package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.BlackListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BlackListData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BlackListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {

    @BindView(R.id.mMyBlackList_return)
    ImageView mMyBlackListReturn;
    @BindView(R.id.mMyBlackList_ll)
    PercentRelativeLayout mMyBlackListLl;
    @BindView(R.id.mMyBlackList_none)
    LinearLayout mMyBlackListNone;
    @BindView(R.id.mMyBlackList_listview)
    PullToRefreshListView mMyBlackListListview;
    private int page = 0;
    Handler handler = new Handler();
    List<BlackListData.DataBean> blackList = new ArrayList<>();
    private BlackListviewAdapter listviewAdapter;
    private TimeSecondUtils refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setData();
        setListener();
    }

    private void setData() {
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        mMyBlackListListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mMyBlackListListview.setFocusable(false);
        //实现自动刷新
        mMyBlackListListview.mHeaderLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int width = mMyBlackListListview.mHeaderLayout.getHeight();
                if (width > 0) {
                    mMyBlackListListview.setRefreshing();
                    mMyBlackListListview.mHeaderLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
    }

    private void setListener() {
        mMyBlackListListview.setOnRefreshListener(this);
        mMyBlackListListview.setOnItemClickListener(this);
        mMyBlackListListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
                //visibleItemCount：当前能看见的列表项个数（小半个也算）
                //totalItemCount：列表项共数
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0&&visibleItemCount!=totalItemCount) {
                    if(!mMyBlackListListview.isShownHeader()) {
                        page = page + 1;
                        getBlackList();
                    }
                }
            }
        });
    }

    @OnClick(R.id.mMyBlackList_return)
    public void onClick() {
        finish();
    }

    private void getBlackList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetBlackList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BlackListData data = new Gson().fromJson(response, BlackListData.class);
                            if (data.getData().size() == 0) {
                                if (page == 0) {
                                    mMyBlackListNone.setVisibility(View.VISIBLE);
                                }
                                if (page != 0) {
                                    page = page - 1;
                                    ToastUtil.show(BlackListActivity.this.getApplicationContext(), "没有更多");
                                }
                                if (listviewAdapter != null) {
                                    listviewAdapter.notifyDataSetChanged();
                                }
                            } else {
                                if (page == 0) {
                                    blackList.addAll(data.getData());
                                    try {
                                        listviewAdapter = new BlackListviewAdapter(BlackListActivity.this, blackList);
                                        mMyBlackListListview.setAdapter(listviewAdapter);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    blackList.addAll(data.getData());
                                    listviewAdapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        mMyBlackListListview.onRefreshComplete();
                        refresh.cancel();
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PesonInfoActivity.class);
        intent.putExtra("uid", blackList.get(position-1).getUid());
        startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        blackList.clear();
        if(listviewAdapter!=null){
            listviewAdapter.notifyDataSetChanged();
        }
        refresh=new TimeSecondUtils(this,mMyBlackListListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBlackList();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBlackList();
            }
        }, 500);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(String message) {
        if (message.equals("blackSuccess")) {
            blackList.clear();
            page = 0;
            getBlackList();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpLocationUtils.LogintimeAndLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
