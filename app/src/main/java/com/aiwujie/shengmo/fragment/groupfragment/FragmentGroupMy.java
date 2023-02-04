package com.aiwujie.shengmo.fragment.groupfragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.GroupInfoActivity;
import com.aiwujie.shengmo.activity.VipWebActivity;
import com.aiwujie.shengmo.adapter.GroupListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GroupData;
import com.aiwujie.shengmo.customview.MyListView;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
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

/**
 * Created by 290243232 on 2017/1/15.
 */
public class FragmentGroupMy extends Fragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {

    @BindView(R.id.mFragment_group_my_listview)
    PullToRefreshListView mFragmentGroupMyListview;
    @BindView(R.id.tv_qunweihu)
    TextView tv_qunweihu;
    private int page = 0;
    Handler handler = new Handler();
    List<GroupData.DataBean> groups = new ArrayList<>();
    private GroupListviewAdapter groupAdapter;
    private TimeSecondUtils refresh;
    /**
     * 是否可见状态
     */
    private boolean isVisible;
    /**
     * 标志位，View已经初始化完成。
     * 2016/04/29
     * 用isAdded()属性代替
     * 2016/05/03
     * isPrepared还是准一些,isAdded有可能出现onCreateView没走完但是isAdded了
     */
    private boolean isPrepared;
    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh=true;
    private View ll01;
    private View ll02;
    private MyListView listView;
    private GroupData createListData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_group_my, null);
        ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
//        setData();
//        setListener();
        tv_qunweihu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent  intent = new Intent(getActivity(), VipWebActivity.class);
                intent.putExtra("title", "圣魔");
                intent.putExtra("path", HttpUrl.NetPic()+"/Home/Info/news/id/31");
                startActivity(intent);
            }
        });

        return view;
    }

    private void setData() {
        View view=View.inflate(getActivity().getApplicationContext(),R.layout.item_group_my_header,null);
        listView= (MyListView) view.findViewById(R.id.item_group_my_header_listview);
        ll01=  view.findViewById(R.id.item_group_my_header_ll01);
        ll02=  view.findViewById(R.id.item_group_my_header_ll02);
        getMyCreateGroupList(listView);
        mFragmentGroupMyListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentGroupMyListview.getRefreshableView().addHeaderView(view);
        mFragmentGroupMyListview.setFocusable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
                intent.putExtra("groupId", createListData.getData().get(position).getGid());
                startActivity(intent);
            }
        });
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentGroupMyListview.setRefreshing();
            }
        },100);
    }

    private void getMyCreateGroupList(final ListView listview) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("type", "3");
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetGroupListFilter, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                PrintLogUtils.log(response,"fragmentgroupmycreate");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            createListData = new Gson().fromJson(response, GroupData.class);
                            int retcode = createListData.getRetcode();
                            GroupListviewAdapter groupAdapter=null;
                            if (createListData.getData().size() == 0) {
                                ll01.setVisibility(View.GONE);
                                listview.setVisibility(View.GONE);
                            } else {
                                ll01.setVisibility(View.VISIBLE);
                                listview.setVisibility(View.VISIBLE);
                            }
                            try{
                                if(groupAdapter==null) {
                                    groupAdapter = new GroupListviewAdapter(getActivity(), createListData.getData(), retcode);
                                    listview.setAdapter(groupAdapter);
                                }else{
                                    groupAdapter.notifyDataSetChanged();
                                }
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
                        }catch (JsonSyntaxException e){
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

    private void setListener() {
        mFragmentGroupMyListview.setOnRefreshListener(this);
        mFragmentGroupMyListview.setOnItemClickListener(this);
        mFragmentGroupMyListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if(IsListviewSlideBottom.isListViewReachBottomEdge(absListView)){
                            if(isReresh) {
                                page = page + 1;
                                getGroupList();
                            }
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
        });
    }

    private void getGroupList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
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
                        try{
                            GroupData listData = new Gson().fromJson(response, GroupData.class);
                            if (listData.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    isReresh=false;
                                    ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                                }else{
                                    ll02.setVisibility(View.GONE);
                                }

                            } else {
                                isReresh=true;
                                ll02.setVisibility(View.VISIBLE);
                                if (page == 0) {
                                    groups.addAll(listData.getData());
                                    int retcode = listData.getRetcode();
                                    try{
                                        groupAdapter = new GroupListviewAdapter(getActivity(), groups, retcode);
                                        mFragmentGroupMyListview.setAdapter(groupAdapter);
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    groups.addAll(listData.getData());
                                    groupAdapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        mFragmentGroupMyListview.onRefreshComplete();
                        if(refresh!=null) {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position==0||position==1){
            return;
        }
        Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
        intent.putExtra("groupId", groups.get(position-2).getGid());
        startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        groups.clear();
        if(groupAdapter!=null){
            groupAdapter.notifyDataSetChanged();
        }
        refresh=new TimeSecondUtils(getActivity(),mFragmentGroupMyListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getGroupList();
                getMyCreateGroupList(listView);
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getGroupList();
            }
        }, 500);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(String message) {
        if (message.equals("refreshgroup")) {
            page = 0;
            groups.clear();
            getGroupList();
            getMyCreateGroupList(listView);
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (isPrepared && isVisible && isFirstLoad) {
            EventBus.getDefault().register(this);
            isFirstLoad = false;
            setData();
            setListener();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
