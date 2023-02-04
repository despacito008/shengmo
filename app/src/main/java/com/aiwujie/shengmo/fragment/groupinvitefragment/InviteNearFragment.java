package com.aiwujie.shengmo.fragment.groupinvitefragment;

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
import com.aiwujie.shengmo.adapter.InviteNewNearAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.HomeNewListData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.InviteMemberClickUtils;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
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
 * Created by 290243232 on 2017/6/8.
 */

public class InviteNearFragment extends Fragment implements AdapterView.OnItemClickListener ,PullToRefreshBase.OnRefreshListener2{
    @BindView(R.id.mFragment_invite_near_listview)
    PullToRefreshListView mFragmentInviteNearListview;
    private int page=0;
    Handler handler=new Handler();
    private TimeSecondUtils refresh;
    private List<HomeNewListData.DataBean> datas = new ArrayList<>();
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
    private InviteNewNearAdapter listviewAdapter;
    public static String nearUidstr="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.fragment_invite_near, null);
        ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }
    private void setData() {
        mFragmentInviteNearListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentInviteNearListview.setFocusable(false);
        mFragmentInviteNearListview.getRefreshableView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mFragmentInviteNearListview.getRefreshableView().getCheckedItemPositions();
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentInviteNearListview.setRefreshing();
            }
        },100);

    }

    private void setListener() {
        mFragmentInviteNearListview.setOnRefreshListener(this);
        mFragmentInviteNearListview.setOnItemClickListener(this);
        mFragmentInviteNearListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if(IsListviewSlideBottom.isListViewReachBottomEdge(absListView)){
                            if(isReresh) {
                                page = page + 1;
                                getUserList();
                            }
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
        });
    }
    private void getUserList() {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("layout", "1");
        map.put("type", "1");
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("onlinestate", "");
        map.put("realname", "");
        map.put("age", "");
        map.put("sex", "");
        map.put("sexual", "");
        map.put("role", "");
        map.put("culture", "");
        map.put("monthly", "");
        map.put("loginid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.UserListNewth, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            HomeNewListData listData = new Gson().fromJson(response, HomeNewListData.class);
                            if (listData.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                                    isReresh=false;
                                }
                            } else {
                                isReresh=true;
                                if (page == 0) {
                                    int retcode = listData.getRetcode();
                                    datas.addAll(listData.getData());
                                    try {
                                        listviewAdapter = new InviteNewNearAdapter(getActivity(), datas, retcode);
                                        mFragmentInviteNearListview.setAdapter(listviewAdapter);
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    datas.addAll(listData.getData());
                                    listviewAdapter.notifyDataSetChanged();
                                }
                            }

                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        mFragmentInviteNearListview.onRefreshComplete();
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
        nearUidstr= InviteMemberClickUtils.click(getActivity(),mFragmentInviteNearListview,datas,position);
        listviewAdapter.notifyDataSetChanged();
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

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        datas.clear();
        if (listviewAdapter != null) {
            listviewAdapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(getActivity(), mFragmentInviteNearListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserList();
            }
        }, 300);
    }
    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }
}
