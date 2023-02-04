package com.aiwujie.shengmo.fragment.dynamicfragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.DynamicHotCardListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.eventbus.DynamicCommentEvent;
import com.aiwujie.shengmo.eventbus.DynamicEvent;
import com.aiwujie.shengmo.eventbus.DynamicRewardEvent;
import com.aiwujie.shengmo.eventbus.DynamicSxEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.ListviewOntouchListener;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.TextMoreClickUtils;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.autolayout.AutoLinearLayout;

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

/**
 * Created by 290243232 on 2017/7/28.
 */

public class FragmentDynamicHotCard extends Fragment implements PullToRefreshBase.OnRefreshListener2 {

    @BindView(R.id.mFragment_dynamic_hotCard_listview)
    PullToRefreshListView mFragmentDynamicHotCardListview;
    @BindView(R.id.mFragment_dynamic_hotCard_tv)
    TextView mFragmentDynamicHotCardTv;
    @BindView(R.id.mFragment_dynamic_hotCard_ll_lock)
    AutoLinearLayout mFragmentDynamicHotCardLlLock;
    @BindView(R.id.mFragment_dynamic_hotCard_top)
    ImageView mFragmentDynamicHotCardTop;
    private int page = 0;
    Handler handler = new Handler();
    List<DynamicListData.DataBean> dynamics = new ArrayList<>();
    private DynamicHotCardListviewAdapter dynamicAdapter;
    private boolean eventBoolean = true;
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
    private TimeSecondUtils refresh;
    private String sex = "";
    private String sexual = "";
    private String firsttime = "0";
    private View tvHeaderView;
    private TextView tvHeaderViewTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.item_fragment_dynamic_hotcard, null);
        isPrepared = true;
        lazyLoad();
        ButterKnife.bind(this, view);
        return view;
    }

    private void setData() {
        mFragmentDynamicHotCardListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        tvHeaderView = View.inflate(getActivity(), R.layout.item_dynamic_tv_header, null);
        tvHeaderViewTv = (TextView) tvHeaderView.findViewById(R.id.item_dynamic_tv_header_tv);
        tvHeaderViewTv.setMovementMethod(LinkMovementMethod.getInstance());
        TextMoreClickUtils clickUtils = new TextMoreClickUtils(getActivity(), "0", handler);
        tvHeaderViewTv.setText(clickUtils.addClickablePart("认证用户、VIP会员", "仅显示", "发布的动态"), TextView.BufferType.SPANNABLE);
        mFragmentDynamicHotCardListview.getRefreshableView().addHeaderView(tvHeaderView);
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentDynamicHotCardListview.setRefreshing();
            }
        }, 100);
    }

    private void setListener() {
        mFragmentDynamicHotCardListview.setOnRefreshListener(this);
        mFragmentDynamicHotCardListview.getRefreshableView().setOnTouchListener(new ListviewOntouchListener(0));
        mFragmentDynamicHotCardListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isReresh) {
                                page = page + 1;
                                getDynamicList();
                            }
                        }
                        break;
                }
                if (mFragmentDynamicHotCardListview.getRefreshableView().getFirstVisiblePosition() == 0) {
                    mFragmentDynamicHotCardTop.setVisibility(View.GONE);
                    mFragmentDynamicHotCardTop.setAnimation(AnimationUtil.ViewToGone());
                } else {
                    if (mFragmentDynamicHotCardTop.getVisibility() == View.GONE) {
                        mFragmentDynamicHotCardTop.setVisibility(View.VISIBLE);
                        mFragmentDynamicHotCardTop.setAnimation(AnimationUtil.ViewToVisible());
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });

    }

    private void getDynamicList() {
        Map<String, String> map = new HashMap<>();
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", page + "");
        map.put("loginuid", MyApp.uid);
        map.put("sex", sex);
        map.put("sexual", sexual);
        map.put("commenttime", firsttime);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetHotDynamicListed, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("GetHotDynamicList", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DynamicListData listData = new Gson().fromJson(response, DynamicListData.class);
                            if (listData.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    isReresh = false;
                                    ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                                }
                            } else {
                                isReresh = true;
                                if (page == 0) {
                                    dynamics.addAll(listData.getData());
                                    int retcode = listData.getRetcode();
                                    try {
                                        dynamicAdapter = new DynamicHotCardListviewAdapter(getActivity(), dynamics, retcode);
                                        mFragmentDynamicHotCardListview.setAdapter(dynamicAdapter);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    dynamics.addAll(listData.getData());
                                    dynamicAdapter.notifyDataSetChanged();
                                }
                            }
                            firsttime = dynamics.get(0).getCommenttime();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        mFragmentDynamicHotCardListview.onRefreshComplete();
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


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//        String realname = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "realname", "0");
        Toast.makeText(getActivity(), "1111111111111111111111", Toast.LENGTH_SHORT).show();
        String vip = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "vip", "0");
        if (vip.equals("0")) {
            mFragmentDynamicHotCardListview.removeView(tvHeaderView);
            mFragmentDynamicHotCardListview.onRefreshComplete();
            mFragmentDynamicHotCardLlLock.setVisibility(View.VISIBLE);
            mFragmentDynamicHotCardTv.setMovementMethod(LinkMovementMethod.getInstance());
            TextMoreClickUtils clickUtils = new TextMoreClickUtils(getActivity(), "0", handler);
            mFragmentDynamicHotCardTv.setText(clickUtils.addClickablePart("VIP会员", "限", "可见"), TextView.BufferType.SPANNABLE);
        } else {
            page = 0;
            firsttime = "0";
            dynamics.clear();

            if (dynamicAdapter != null) {
                dynamicAdapter.notifyDataSetChanged();
            }
            refresh = new TimeSecondUtils(getActivity(), mFragmentDynamicHotCardListview);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDynamicList();
                }
            }, 300);
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(DynamicEvent event) {
        if (dynamics.size() != 0) {
            dynamics.get(event.getPosition()).setLaudstate(event.getLaudstate() + "");
            dynamics.get(event.getPosition()).setLaudnum(event.getZancount() + "");
//            dynamicAdapter.notifyDataSetChanged();
            dynamicAdapter.notify(dynamics,event.getPosition());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void secondEventBus(DynamicCommentEvent event) {
        if (dynamics.size() != 0) {
            dynamics.get(event.getPosition()).setComnum(event.getCommentcount() + "");
            dynamicAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void rewardEventBus(DynamicRewardEvent event) {
        if (dynamics.size() != 0) {
            dynamics.get(event.getPosition()).setRewardnum(event.getRewardcount() + "");
            dynamicAdapter.notifyDataSetChanged();
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
            isFirstLoad = false;
            if (eventBoolean) {
                EventBus.getDefault().register(this);
                eventBoolean = false;
            }
            getDynamicSx();
            setData();
            setListener();
        }
    }

    private void getDynamicSx() {
        sex = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dynamicSex", "");
        sexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dynamicSexual", "");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(DynamicSxEvent data) {
        page = 0;
        firsttime = "0";
        sex = data.getSex();
        sexual = data.getSexual();
        dynamics.clear();
        if (dynamicAdapter != null) {
            dynamicAdapter.notifyDataSetChanged();
        }
        Log.i("fragmentDynamichot", "onMessage: " + sex + "," + sexual);
        getDynamicList();
    }

    @OnClick(R.id.mFragment_dynamic_hotCard_top)
    public void onViewClicked() {
        mFragmentDynamicHotCardListview.getRefreshableView().setSelection(0);
        mFragmentDynamicHotCardTop.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
