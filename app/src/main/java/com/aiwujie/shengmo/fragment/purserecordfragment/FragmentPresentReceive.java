package com.aiwujie.shengmo.fragment.purserecordfragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.PresentReceiveAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.PresentReceiveData;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/1/11.
 */
public class FragmentPresentReceive extends Fragment implements PullToRefreshBase.OnRefreshListener2 {

    @BindView(R.id.mFragment_withdrawals_listview)
    PullToRefreshListView mFragmentWithdrawalsListview;
    @BindView(R.id.mWithdrawals_noRecorder)
    LinearLayout mWithdrawalsNoRecorder;
    private int page = 0;
    List<PresentReceiveData.DataBean> bills = new ArrayList<>();
    Handler handler = new Handler();
    private PresentReceiveAdapter listviewAdapter;
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
    private boolean isReresh = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_withdrawals, null);
        ButterKnife.bind(this, view);
//        setData();
//        setListener();
        isPrepared = true;
        lazyLoad();
        return view;
    }

    private void setData() {
        mFragmentWithdrawalsListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentWithdrawalsListview.setRefreshing();
            }
        }, 100);
        mFragmentWithdrawalsListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isReresh) {
                                page = page + 1;
                                getBills();
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

    private void getBills() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetReceivePresent, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PresentReceiveData listData = new Gson().fromJson(response, PresentReceiveData.class);
                            if (listData.getData().size() == 0) {
                                if (page == 0) {
                                    mWithdrawalsNoRecorder.setVisibility(View.VISIBLE);
                                }
                                if (page != 0) {
                                    isReresh = false;
                                    page = page - 1;
                                    ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                                }
                            } else {
                                isReresh = true;
                                if (page == 0) {
                                    bills.addAll(listData.getData());
                                    try {
                                        listviewAdapter = new PresentReceiveAdapter(getActivity(), bills);
                                        mFragmentWithdrawalsListview.setAdapter(listviewAdapter);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    bills.addAll(listData.getData());
                                    listviewAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        mFragmentWithdrawalsListview.onRefreshComplete();
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

    private void setListener() {
        mFragmentWithdrawalsListview.setOnRefreshListener(this);
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        bills.clear();
        if (listviewAdapter != null) {
            listviewAdapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(getActivity(), mFragmentWithdrawalsListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBills();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        if (listviewAdapter != null) {
            listviewAdapter.notifyDataSetChanged();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBills();
            }
        }, 500);
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
