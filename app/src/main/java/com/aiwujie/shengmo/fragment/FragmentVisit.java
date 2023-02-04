package com.aiwujie.shengmo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.adapter.HomeNewListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.HomeListviewData;
import com.aiwujie.shengmo.customview.VipDialog;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
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

/**
 * Created by 290243232 on 2016/12/29.
 */
public class FragmentVisit extends Fragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {

    @BindView(R.id.mFragment_visit_listview)
    PullToRefreshListView mFragmentVisitListview;
    private int page = 0;
    Handler handler = new Handler();
    private List<HomeListviewData.DataBean> dataList = new ArrayList<>();
    private HomeNewListviewAdapter listviewAdapter;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_visit, null);
        ButterKnife.bind(this, view);
//        setData();
//        setListener();
        isPrepared = true;
        lazyLoad();
        return view;
    }
    private void setData() {
        mFragmentVisitListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentVisitListview.setFocusable(false);
        String isvip = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "vip", "");
        if ("1".equals(isvip)) {
            //实现自动刷新
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFragmentVisitListview.setRefreshing();
                }
            }, 100);
        } else {
            new VipDialog(getActivity(),"会员才能查看看过我的人哦~");
        }
    }

    private void setListener() {
        mFragmentVisitListview.setOnRefreshListener(this);
        mFragmentVisitListview.setOnItemClickListener(this);
        mFragmentVisitListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if(IsListviewSlideBottom.isListViewReachBottomEdge(absListView)){
                            if(isReresh) {
                                page = page + 1;
                                getVisit();
                            }
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
        });
    }
    private void getVisit(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("type", "0");
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.ReadList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            HomeListviewData listData = new Gson().fromJson(response, HomeListviewData.class);
                            if (listData.getData().size() == 0) {
                                if(page!=0) {
                                    page = page - 1;
                                    isReresh=false;
                                }
                                ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                            } else {
                                isReresh=true;
                                if (page == 0) {
                                    dataList.addAll(listData.getData());
                                    int retcode = listData.getRetcode();
                                    try {
                                        listviewAdapter = new HomeNewListviewAdapter(getContext(), dataList, retcode, "0");
                                        mFragmentVisitListview.setAdapter(listviewAdapter);
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    dataList.addAll(listData.getData());
                                    listviewAdapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        mFragmentVisitListview.onRefreshComplete();
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
        Intent intent = new Intent(getActivity(), PesonInfoActivity.class);
        intent.putExtra("uid", dataList.get(position-1).getUid());
        startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        dataList.clear();
        if(listviewAdapter!=null){
            listviewAdapter.notifyDataSetChanged();
        }
        String isvip = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "vip", "");
        if ("1".equals(isvip)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh=new TimeSecondUtils(getActivity(),mFragmentVisitListview);
                    getVisit();
                }
            }, 300);
        }else {
            mFragmentVisitListview.onRefreshComplete();
            new VipDialog(getActivity(),"会员才能查看看过我的人哦~");
        }

    }


    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

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
            isFirstLoad = false;
            setData();
            setListener();
        }
    }

}
