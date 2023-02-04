package com.aiwujie.shengmo.fragment.dynamicfragment;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.DynamicTopListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.eventbus.ClearRedPointEvent;
import com.aiwujie.shengmo.eventbus.DynamicCommentEvent;
import com.aiwujie.shengmo.eventbus.DynamicEvent;
import com.aiwujie.shengmo.eventbus.DynamicRewardEvent;
import com.aiwujie.shengmo.eventbus.DynamicSxEvent;
import com.aiwujie.shengmo.eventbus.SendDynamicSuccessEvent;
import com.aiwujie.shengmo.eventbus.ViewIsVisibleEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.ListviewOntouchListener;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.feezu.liuli.timeselector.Utils.TextUtil;
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
 * Created by 290243232 on 2017/1/12.  推荐
 */
public class FragmentDynamicNew_tuiding extends Fragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener, View.OnClickListener {
    @BindView(R.id.mFragment_dynamic_hot_listview)
    PullToRefreshListView mFragmentDynamicHotListview;
    @BindView(R.id.mFragment_dynamic_hot_top)
    ImageView mFragmentDynamicHotTop;
    @BindView(R.id.pai_ll)
    LinearLayout pai_ll;
    @BindView(R.id.pai_tv)
    TextView pai_tv;
    private int page = 0;
    Handler handler = new Handler();
    List<DynamicListData.DataBean> dynamics = new ArrayList<>();
    private DynamicTopListviewAdapter dynamicAdapter;
    private String sex = "";
    private String sexual = "";
    private boolean eventBoolean = true;
    String type = "5";
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

    String lastid = "";


    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;
    private TimeSecondUtils refresh;
    private boolean isCanLoad = true;
    //private TextView tvHeaderSvip;
   // String order = "time";
    String order = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.item_fragment_dynamic_hot_tuiding, null);
        ButterKnife.bind(this, view);
        //EventBus.getDefault().register(this);
        isPrepared = true;
        lazyLoad();
        order = SharedPreferencesUtils.geParam(getActivity(),SharedPreferencesUtils.HOT_SORT,"time");
//        if (order.equals("time")) {
//            pai_tv.setText("时间排序");
//        } else {
//            pai_tv.setText("推顶数量");
//        }
//        pai_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                page = 0;
//                dynamics.clear();
//                if (dynamicAdapter != null) {
//                    dynamicAdapter.notifyDataSetChanged();
//                }
//
//                if (order.equals("time")) {
//                    pai_tv.setText("推顶数量");
//                    order = "tnum";
//                    getDynamicList();
//                } else {
//                    pai_tv.setText("时间排序");
//                    order = "time";
//                    getDynamicList();
//                }
//                SharedPreferencesUtils.setParam(getActivity(),SharedPreferencesUtils.HOT_SORT,order);
//            }
//        });

//        setData();
//        setListener();
        return view;
    }

    private void setData() {
        mFragmentDynamicHotListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentDynamicHotListview.setRefreshing();
            }
        }, 100);
    }


    private void setListener() {


        mFragmentDynamicHotListview.setOnRefreshListener(this);
        mFragmentDynamicHotListview.setOnItemClickListener(this);
        mFragmentDynamicHotListview.getRefreshableView().setOnTouchListener(new ListviewOntouchListener(0));
        mFragmentDynamicHotListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isCanLoad) {
                                isCanLoad = false;
                                if (isReresh) {
                                    page = page + 1;
                                    lastid = dynamics.get(0).getDid();
                                    getDynamicList();

                                }
                            }
                        }else {
                            int first = absListView.getFirstVisiblePosition();
                            int last = absListView.getLastVisiblePosition();
                            for (int j = last; j > first; j--) {
                                if(j > 0 && dynamics.size() > j - 1 && !TextUtil.isEmpty(dynamics.get(j-1).getPlayUrl().trim())) {
                                    dynamicAdapter.tryToPlayVideo(absListView,j - first,dynamics.get(j - 1).getPlayUrl().trim());
                                    return;
                                }
                            }
                        }
                        break;
                }
                if (mFragmentDynamicHotListview.getRefreshableView().getFirstVisiblePosition() == 0) {
                    mFragmentDynamicHotTop.setVisibility(View.GONE);
                    mFragmentDynamicHotTop.setAnimation(AnimationUtil.ViewToGone());
                } else {
                    if (mFragmentDynamicHotTop.getVisibility() == View.GONE) {
                        mFragmentDynamicHotTop.setVisibility(View.VISIBLE);
                        mFragmentDynamicHotTop.setAnimation(AnimationUtil.ViewToVisible());
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
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("type", type);
        map.put("page", page + "");
        map.put("loginuid", MyApp.uid);
        map.put("sex", sex);
        map.put("sexual", sexual);
        map.put("lastid", lastid);
        map.put("order", order);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetDynamicListNewFive, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final DynamicListData listData = new Gson().fromJson(response, DynamicListData.class);
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
                                        dynamicAdapter = new DynamicTopListviewAdapter(getActivity(), dynamics, retcode,type,order);
                                        mFragmentDynamicHotListview.setAdapter(dynamicAdapter);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    dynamics.addAll(listData.getData());
                                    dynamicAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        isCanLoad = true;
                        mFragmentDynamicHotListview.onRefreshComplete();
                        if (refresh != null) {
                            refresh.cancel();
                        }
                        EventBus.getDefault().post(new ClearRedPointEvent("ClearDynamicRedPoint", 8));
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
        page = 0;
        dynamics.clear();
        if (dynamicAdapter != null) {
            dynamicAdapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(getActivity(), mFragmentDynamicHotListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastid = "";
                getDynamicList();
            }
        }, 300);

        EventBus.getDefault().post("hongdiantuiding");
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
            dynamicAdapter.notify(dynamics, event.getPosition());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(SendDynamicSuccessEvent event) {
        if (event.getSuccess() == 0) {
            page = 0;
            dynamics.clear();
            lastid = "";
            getDynamicList();

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
        //sex= (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(),"dynamicSex","0");
        //sexual= (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(),"dynamicSexual","0");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(DynamicSxEvent data) {
        page = 0;
        sex = data.getSex();
        sexual = data.getSexual();
        dynamics.clear();
        if (dynamicAdapter != null) {
            dynamicAdapter.notifyDataSetChanged();
        }
        Log.i("fragmentDynamichot", "onMessage: " + sex + "," + sexual);
        lastid = "";
        getDynamicList();
        EventBus.getDefault().post("hongdiantuiding");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @OnClick(R.id.mFragment_dynamic_hot_top)
    public void onClick() {
        EventBus.getDefault().post("tuitop");
        mFragmentDynamicHotListview.getRefreshableView().setSelection(0);
        EventBus.getDefault().post(new ViewIsVisibleEvent(1));
        mFragmentDynamicHotTop.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void changeOrder(int type) {

        page = 0;
        dynamics.clear();
        if (dynamicAdapter != null) {
            dynamicAdapter.notifyDataSetChanged();
        }

        if (type == 1) {
            pai_tv.setText("推顶数量");
            order = "tnum";
            getDynamicList();
        } else {
            pai_tv.setText("时间排序");
            order = "time";
            getDynamicList();
        }

        SharedPreferencesUtils.setParam(getActivity(),SharedPreferencesUtils.HOT_SORT,order);
    }



}
