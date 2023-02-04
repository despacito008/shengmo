package com.aiwujie.shengmo.fragment.noticefragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.VipListAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.NoticeVipData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/6/22.
 */

public class NoticeVipFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2{
    @BindView(R.id.mFragment_notice_listview)
    PullToRefreshListView mFragmentNoticeListview;
    /**
     * 是否可见状态
     */
    private boolean isVisible;
    /**
     * 标志位，View已经初始化完成。
     * 用isAdded()属性代替
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
   
    Handler handler=new Handler();
    private int page=0;
    private TimeSecondUtils refresh;
    List<NoticeVipData.DataBean> noticePresentDatas=new ArrayList<>();
    private VipListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad=true;
        View view = inflater.inflate(R.layout.item_fragment_notice, null);
        ButterKnife.bind(this, view);

        TextView viewById = view.findViewById(R.id.aa);
        viewById.setText("送SVIP会员可上大喇叭");
        isPrepared = true;
        lazyLoad();
        return view;
    }
    
    private void setData() {
        mFragmentNoticeListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentNoticeListview.setRefreshing();
            }
        },100);
    }
    private void setListener() {
        mFragmentNoticeListview.setOnRefreshListener(this);
        mFragmentNoticeListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if(IsListviewSlideBottom.isListViewReachBottomEdge(absListView)){
                            if(isReresh) {
                                page = page + 1;
                                getNoticePresent();
                            }
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
        });
    }

    private void getNoticePresent() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "1");
        map.put("page", page + "");
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetPresentMsg, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("noticepresent", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            NoticeVipData listData = new Gson().fromJson(response, NoticeVipData.class);
                            if (listData.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    isReresh=false;
                                    ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                                }

                            } else {
                                isReresh=true;
                                if (page == 0) {
                                    noticePresentDatas.addAll(listData.getData());
                                    try {
                                        listAdapter = new VipListAdapter(getActivity(), noticePresentDatas);
                                        mFragmentNoticeListview.setAdapter(listAdapter);
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    noticePresentDatas.addAll(listData.getData());
                                    listAdapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        mFragmentNoticeListview.onRefreshComplete();
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
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page=0;
        EventBus.getDefault().post("zengsonghuiyuanredmei");
        noticePresentDatas.clear();
        if(listAdapter!=null){
            listAdapter.notifyDataSetChanged();
        }
        refresh=new TimeSecondUtils(getActivity(),mFragmentNoticeListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getNoticePresent();
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
