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
import com.aiwujie.shengmo.adapter.InviteFollowAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GzFsHyListviewData;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/6/8.
 */

public class InviteFollowFragment extends Fragment implements AdapterView.OnItemClickListener ,PullToRefreshBase.OnRefreshListener2{
    @BindView(R.id.mFragment_invite_follow_listview)
    PullToRefreshListView mFragmentInviteFollowListview;
    private int page = 0;
    private ArrayList<GzFsHyListviewData.DataBean> datas = new ArrayList<>();
    Handler handler = new Handler();
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
    private InviteFollowAdapter adapter;
    public static String followUidstr="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad=true;
        View view = inflater.inflate(R.layout.fragment_invite_follow, null);
        ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }
    private void setData() {
        mFragmentInviteFollowListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentInviteFollowListview.setFocusable(false);
        mFragmentInviteFollowListview.getRefreshableView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mFragmentInviteFollowListview.getRefreshableView().getCheckedItemPositions();
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentInviteFollowListview.setRefreshing();
            }
        },100);
    }

    private void setListener() {
        adapter = new InviteFollowAdapter(getActivity(), datas);
        mFragmentInviteFollowListview.setAdapter(adapter);
        mFragmentInviteFollowListview.setOnRefreshListener(this);
        mFragmentInviteFollowListview.setOnItemClickListener(this);
        mFragmentInviteFollowListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if(IsListviewSlideBottom.isListViewReachBottomEdge(absListView)){
                            if(isReresh) {
                                page = page + 1;
                                getFollewingList();
                            }
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
        });
    }
    private void getFollewingList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("login_uid", MyApp.uid);
        map.put("page", page + "");
        map.put("type", "0");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetFollewingList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            GzFsHyListviewData dataList = new Gson().fromJson(response, GzFsHyListviewData.class);
                            if (dataList.getData().size() == 0) {
                                if(page!=0) {
                                    page = page - 1;
                                    isReresh=false;
                                    ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                                }
                            } else {
                                isReresh=true;
                                    datas.addAll(dataList.getData());
                                    adapter.notifyDataSetChanged();
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        mFragmentInviteFollowListview.onRefreshComplete();
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
        followUidstr=InviteMemberClickUtils.clicks(getActivity(),mFragmentInviteFollowListview,datas,position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        datas.clear();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        refresh=new TimeSecondUtils(getActivity(),mFragmentInviteFollowListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFollewingList();
            }
        }, 300);
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
