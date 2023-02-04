package com.aiwujie.shengmo.fragment.dynamicfragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.ActiveActivity;
import com.aiwujie.shengmo.activity.BannerWebActivity;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.DynamicMessageActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.PopularityActivity;
import com.aiwujie.shengmo.activity.RichesListActivity;
import com.aiwujie.shengmo.activity.TopicDetailActivity;
import com.aiwujie.shengmo.activity.ranking.ActiveRankingActivity;
import com.aiwujie.shengmo.activity.ranking.PopularityRankingActivity;
import com.aiwujie.shengmo.activity.ranking.RewardRankingActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.DynamicTopListviewAdapter;
import com.aiwujie.shengmo.adapter.RecyclerViewDynamicAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BannerNewData;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.bean.TopicHeaderData;
import com.aiwujie.shengmo.eventbus.ClearDynamicNewCount;
import com.aiwujie.shengmo.eventbus.ClearRedPointEvent;
import com.aiwujie.shengmo.eventbus.DynamicCommentEvent;
import com.aiwujie.shengmo.eventbus.DynamicEvent;
import com.aiwujie.shengmo.eventbus.DynamicMessageEvent;
import com.aiwujie.shengmo.eventbus.DynamicRewardEvent;
import com.aiwujie.shengmo.eventbus.DynamicSxEvent;
import com.aiwujie.shengmo.eventbus.SendDynamicSuccessEvent;
import com.aiwujie.shengmo.eventbus.ViewIsVisibleEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.BannerUtils;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.ListviewOntouchListener;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.AutoPollRecyclerView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.android.percent.support.PercentFrameLayout;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

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
public class FragmentDynamicNew_tuijian extends Fragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener, View.OnClickListener, OnBannerListener {
    @BindView(R.id.mFragment_dynamic_hot_listview)
    PullToRefreshListView mFragmentDynamicHotListview;
    Banner mDynamicBanner;
    ImageView mDynamicBannerClose;
    PercentFrameLayout mdynamicBannerFramlayout;
    @BindView(R.id.mFragment_dynamic_hot_top)
    ImageView mFragmentDynamicHotTop;
    private int page = 0;
    Handler handler = new Handler();
    List<DynamicListData.DataBean> dynamics = new ArrayList<>();
    private DynamicTopListviewAdapter dynamicAdapter;
    private String sex="";
    private String sexual="";
    private boolean eventBoolean = true;
    String type = "0";
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
    private TextView tvRedPoint;
    private AutoPollRecyclerView recyclerview;
    private List<String> bannerTitle = new ArrayList<>();
    private List<String> bannerPath = new ArrayList<>();
    private List<String> bannerUrl = new ArrayList<>();
    private List<String> linkType = new ArrayList<>();
    private List<String> linkId = new ArrayList<>();
    private boolean isCanLoad = true;
    //private TextView tvHeaderSvip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.item_fragment_dynamic_hot_tuijian, null);
        ButterKnife.bind(this, view);
        //EventBus.getDefault().register(this);
        isPrepared = true;
        lazyLoad();
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


    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        View view = View.inflate(getActivity(), R.layout.item_dynamic_header, null);
//        PercentLinearLayout ll01 = (PercentLinearLayout) view.findViewById(R.id.mDynamic_header_ll01);
//        PercentLinearLayout ll02 = (PercentLinearLayout) view.findViewById(R.id.mDynamic_header_ll02);
//        PercentLinearLayout ll03 = (PercentLinearLayout) view.findViewById(R.id.mDynamic_header_ll03);
//        PercentLinearLayout ll05 = (PercentLinearLayout) view.findViewById(R.id.mDynamic_header_ll05);
//        mdynamicBannerFramlayout = (PercentFrameLayout) view.findViewById(R.id.mdynamic_banner_framlayout);
//        mDynamicBanner = (Banner) view.findViewById(R.id.mDynamic_banner);
//        mDynamicBannerClose = (ImageView) view.findViewById(R.id.mDynamic_banner_close);
        tvRedPoint =  view.findViewById(R.id.mDynamic_header_redpoint);
        int dongtaixiaoxirednum = (int) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dongtaixiaoxirednum", 0);
        if (dongtaixiaoxirednum!=0){
            tvRedPoint.setText(dongtaixiaoxirednum+"");
            tvRedPoint.setVisibility(View.VISIBLE);
        }else {
            tvRedPoint.setVisibility(View.GONE);
        }

        //广告轮播
        BannerUtils.setBannerView(mDynamicBanner);
        mDynamicBanner.setOnBannerListener(this);
//        ll01.setOnClickListener(this);
//        ll02.setOnClickListener(this);
//        ll03.setOnClickListener(this);
////        ll04.setOnClickListener(this);
//        ll05.setOnClickListener(this);
        //tvHeaderSvip.setOnClickListener(this);
        mDynamicBannerClose.setOnClickListener(this);
        recyclerview =  view.findViewById(R.id.mDynamic_recyclerview);

        mFragmentDynamicHotListview.getRefreshableView().addHeaderView(view);
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
                                    if (dynamics!=null&&dynamics.size()>0){
                                        lastid = dynamics.get(0).getDid();
                                    }

                                    getDynamicList();

                                }
                            }
                        }else {
                            int first = absListView.getFirstVisiblePosition();
                            int last = absListView.getLastVisiblePosition();
                            for (int j = last; j > first; j--) {
                                if(j > 1 && dynamics.size() > j - 2 && !TextUtil.isEmpty(dynamics.get(j-2).getPlayUrl().trim())) {
                                    dynamicAdapter.tryToPlayVideo(absListView,j - first,dynamics.get(j - 2).getPlayUrl().trim());
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
        map.put("sex",sex);
        map.put("sexual",sexual);
        map.put("lastid", lastid);
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
                                        dynamicAdapter = new DynamicTopListviewAdapter(getActivity(), dynamics, retcode,type);
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

    private void getTopicHeader() {
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.get(HttpUrl.GetTopicDyHead, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("dynamictopictitle", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final TopicHeaderData data = new Gson().fromJson(response, TopicHeaderData.class);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerview.setLayoutManager(linearLayoutManager);
                        if (data.getRetcode() == 2000) {
                            RecyclerViewDynamicAdapter recyclerAdapter = new RecyclerViewDynamicAdapter(data.getData(), getActivity());
                           recyclerAdapter.setHeaderView(View.inflate(getActivity(), R.layout.item_dynamic_recycler_header, null));
                            recyclerview.setAdapter(recyclerAdapter);
                            if(data.getData().size()>=4)
                                recyclerview.start();
                            recyclerAdapter.setOnItemClickLitener(new RecyclerViewDynamicAdapter.OnItemClickLitener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
                                    intent.putExtra("tid", data.getData().get(position).getTid());
                                    intent.putExtra("topictitle", data.getData().get(position).getTitle());
                                    startActivity(intent);
                                }
                            });
                        } else {
                            recyclerview.setVisibility(View.GONE);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void getBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "2");
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetSlideMore, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("guanggaozeng", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            switch (object.getInt("retcode")) {
                                case 2000:
                                    BannerNewData data = new Gson().fromJson(response, BannerNewData.class);
                                    for (int i = 0; i < data.getData().size(); i++) {
                                        bannerTitle.add(data.getData().get(i).getTitle());
                                        bannerPath.add(data.getData().get(i).getPath());
                                        bannerUrl.add(data.getData().get(i).getUrl());
                                        linkType.add(data.getData().get(i).getLink_type());
                                        linkId.add(data.getData().get(i).getLink_id());
                                    }
                                    mdynamicBannerFramlayout.setVisibility(View.VISIBLE);
                                    //设置图片集合
                                    mDynamicBanner.setImages(bannerPath);
                                    mDynamicBanner.start();
                                    break;
                                case 4000:
                                    mdynamicBannerFramlayout.setVisibility(View.GONE);
                                    break;
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
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
        //刷新话题标题
        getTopicHeader();
        EventBus.getDefault().post("hongdiantuijian");

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(SendDynamicSuccessEvent event) {
        if (event.getSuccess() == 0) {
            page = 0;
            dynamics.clear();
            lastid = "";
            getDynamicList();
            EventBus.getDefault().post("hongdiantuijian");
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
            getBanner();
        }
    }


    private void getDynamicSx() {
        sex= (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(),"dynamicSex","0");
        sexual= (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(),"dynamicSexual","0");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(DynamicSxEvent data) {
        page=0;
        sex = data.getSex();
        sexual = data.getSexual();
        dynamics.clear();
        if (dynamicAdapter != null) {
            dynamicAdapter.notifyDataSetChanged();
        }
        Log.i("fragmentDynamichot", "onMessage: "+sex+","+sexual);
        lastid = "";
        getDynamicList();
        EventBus.getDefault().post("hongdiantuijian");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(DynamicMessageEvent event) {
        if (event.getIsShow() == 0) {
            //红点隐藏
            tvRedPoint.setVisibility(View.GONE);
        } else if (event.getIsShow() == 1) {
            tvRedPoint.setText(event.getIsShowcount()+"");
            tvRedPoint.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(ClearDynamicNewCount event) {
        tvRedPoint.setVisibility(View.GONE);
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
        Intent intent;
        switch (v.getId()) {
            case R.id.mDynamic_header_ll01:
                //intent = new Intent(getActivity(), RichesListActivity.class);
                intent = new Intent(getActivity(), RewardRankingActivity.class);
                startActivity(intent);
                break;
            case R.id.mDynamic_header_ll02:
                //intent = new Intent(getActivity(), PopularityActivity.class);
                intent = new Intent(getActivity(), PopularityRankingActivity.class);
                startActivity(intent);
                break;
            case R.id.mDynamic_header_ll03:
                //intent = new Intent(getActivity(), ActiveActivity.class);
                intent = new Intent(getActivity(), ActiveRankingActivity.class);
                startActivity(intent);
                break;
//            case R.id.mDynamic_header_ll04:
//                intent = new Intent(getActivity(), TopicActivity.class);
////                intent.putExtra("uid", MyApp.uid);
//                startActivity(intent);
//                break;
            case R.id.mDynamic_header_ll05:
                intent = new Intent(getActivity(), DynamicMessageActivity.class);
                startActivity(intent);
                break;
//            case R.id.mDynamic_banner_close:
//                mdynamicBannerFramlayout.setVisibility(View.GONE);
//                break;
/*            case R.id.mDynamic_header_tv_svip:
                intent = new Intent(getActivity(), VipWebActivity.class);
                intent.putExtra("title", "Samer");
//                intent.putExtra("path", "http://hao.shengmo.org:888/Home/Info/news/id/5");
                intent.putExtra("path", HttpUrl.NetPic()+ "Home/Info/news/id/5");
                startActivity(intent);
                break;*/
        }
    }

    @Override
    public void OnBannerClick(int position) {
        Intent intent = null;
        if (linkType.get(position).equals("0")) {
            intent = new Intent(getActivity(), BannerWebActivity.class);
            intent.putExtra("path", bannerUrl.get(position)+"?uid="+MyApp.uid);
            intent.putExtra("title", bannerTitle.get(position));
        } else if (linkType.get(position).equals("1")){
            intent = new Intent(getActivity(), TopicDetailActivity.class);
            intent.putExtra("tid", linkId.get(position));
            intent.putExtra("topictitle", bannerTitle.get(position));
        } else if (linkType.get(position).equals("2")){
            intent = new Intent(getActivity(), DynamicDetailActivity.class);
            intent.putExtra("uid", MyApp.uid);
            intent.putExtra("did", linkId.get(position));
            intent.putExtra("pos", 1);
            intent.putExtra("showwhat", 1);
        }else {
            //intent = new Intent(getActivity(), PesonInfoActivity.class);
            intent = new Intent(getActivity(), UserInfoActivity.class);
            intent.putExtra("uid", linkId.get(position));
        }
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



}
