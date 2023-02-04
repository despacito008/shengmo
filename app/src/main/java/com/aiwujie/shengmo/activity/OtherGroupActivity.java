package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.GroupListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GroupData;
import com.aiwujie.shengmo.customview.MyListView;
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
import com.zhy.android.percent.support.PercentLinearLayout;

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

public class OtherGroupActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {

    @BindView(R.id.mOtherGroup_return)
    ImageView mOtherGroupReturn;
    @BindView(R.id.tv_qunzuweihu)
    TextView tv_qunzuweihu;
    @BindView(R.id.mOtherGroup_listview)
    PullToRefreshListView mOtherGroupListview;
//    @BindView(R.id.mOhterGroup_bottom_tv)
//    TextView mOhterGroupBottomTv;
//    @BindView(R.id.mOhterGroup_bottom_ll)
//    AutoLinearLayout mOhterGroupBottomLl;
    private int page = 0;
    Handler handler = new Handler();
    List<GroupData.DataBean> groups = new ArrayList<>();
    private GroupListviewAdapter groupAdapter;
    private String otherUid;
    private MyListView listView;
    private View ll01;
    private View ll02;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;
    private GroupData createListData;
    private TimeSecondUtils refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_group);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        EventBus.getDefault().register(this);
        otherUid = getIntent().getStringExtra("otherUid");
        setData();
        setListener();
        tv_qunzuweihu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(OtherGroupActivity.this, VipWebActivity.class);
                intent.putExtra("title", "圣魔");
                intent.putExtra("path", HttpUrl.NetPic()+"Home/Info/news/id/31");
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.mOtherGroup_return)
    public void onClick() {
        finish();
    }

    private void setData() {
        View view = View.inflate(getApplicationContext(), R.layout.item_other_group_header, null);
        listView = (MyListView) view.findViewById(R.id.item_other_group_header_listview);
        ll01 =  view.findViewById(R.id.item_other_group_header_ll01);
        ll02 =  view.findViewById(R.id.item_other_group_header_ll02);
        getOtherCreateGroupList(listView);
        mOtherGroupListview.getRefreshableView().addHeaderView(view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OtherGroupActivity.this, GroupInfoActivity.class);
                intent.putExtra("groupId", createListData.getData().get(position).getGid());
                startActivity(intent);
            }
        });
        mOtherGroupListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mOtherGroupListview.setFocusable(false);
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mOtherGroupListview.setRefreshing();
            }
        }, 100);
    }

    private void setListener() {
        mOtherGroupListview.setOnRefreshListener(this);
        mOtherGroupListview.setOnItemClickListener(this);
        mOtherGroupListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isReresh) {
                                page = page + 1;
                                getGroupList();
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

    private void getGroupList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", otherUid);
        map.put("type", "2");
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetGroupListFilter, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fragmentgroupmy", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        GroupData listData = new Gson().fromJson(response, GroupData.class);
                        if (listData.getData().size() == 0) {
                            if (page != 0) {
                                isReresh = false;
                                page = page - 1;
                                ToastUtil.show(OtherGroupActivity.this, "没有更多");
                            } else {
                                ll02.setVisibility(View.GONE);
                                groups.addAll(listData.getData());
                                int retcode = listData.getRetcode();
                                groupAdapter = new GroupListviewAdapter(OtherGroupActivity.this, groups, retcode);
                                mOtherGroupListview.setAdapter(groupAdapter);
                            }
                        } else {
                            isReresh = true;
                            ll02.setVisibility(View.VISIBLE);
                            if (page == 0) {
                                groups.addAll(listData.getData());
                                int retcode = listData.getRetcode();
                                groupAdapter = new GroupListviewAdapter(OtherGroupActivity.this, groups, retcode);
                                mOtherGroupListview.setAdapter(groupAdapter);
                            } else {
                                groups.addAll(listData.getData());
                                groupAdapter.notifyDataSetChanged();
                            }
                        }
                        mOtherGroupListview.onRefreshComplete();
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

    private void getOtherCreateGroupList(final ListView listview) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", otherUid);
        map.put("type", "3");
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetGroupListFilter, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            createListData = new Gson().fromJson(response, GroupData.class);
                            if (createListData.getData().size() == 0) {
                                ll01.setVisibility(View.GONE);
                                listview.setVisibility(View.GONE);
                            } else {
                                GroupListviewAdapter groupAdapter = null;
                                ll01.setVisibility(View.VISIBLE);
                                listview.setVisibility(View.VISIBLE);
                                int retcode = createListData.getRetcode();
                                try {
                                    if (groupAdapter == null) {
                                        groupAdapter = new GroupListviewAdapter(OtherGroupActivity.this, createListData.getData(), retcode);
                                        listview.setAdapter(groupAdapter);
                                    } else {
                                        groupAdapter.notifyDataSetChanged();
                                    }
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JsonSyntaxException e) {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, GroupInfoActivity.class);
        intent.putExtra("groupId", groups.get(position - 2).getGid());
        startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        groups.clear();
//        String realname = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "realname", "0");
//        String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
//        if (realname.equals("0") && vip.equals("0")) {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mOtherGroupListview.onRefreshComplete();
//                }
//            }, 200);
//            mOhterGroupBottomLl.setVisibility(View.VISIBLE);
//            TextMoreClickUtils clickUtils = new TextMoreClickUtils(OtherGroupActivity.this, otherUid, handler);
//            mOhterGroupBottomTv.setText(clickUtils.addClickablePart("互为好友、认证用户、VIP会员", "TA的群组限", "可见"), TextView.BufferType.SPANNABLE);
//            //Android4.0以上默认是淡绿色，低版本的是黄色。解决方法就是通过重新设置文字背景为透明色
//            mOhterGroupBottomTv.setHighlightColor(getResources().getColor(android.R.color.transparent));
//            mOhterGroupBottomTv.setMovementMethod(LinkMovementMethod.getInstance());
//        } else {
            if (groupAdapter != null) {
                groupAdapter.notifyDataSetChanged();
            }
            refresh = new TimeSecondUtils(this, mOtherGroupListview);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getGroupList();
                }
            }, 300);
//        }

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        getGroupList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(String message) {
        if (message.equals("refreshgroup")) {
            page = 0;
            groups.clear();
            getOtherCreateGroupList(listView);
            getGroupList();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
