package com.aiwujie.shengmo.fragment.dynamicfragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.DynamicListviewAdapter2;
import com.aiwujie.shengmo.adapter.DynamicRecyclerAdapter;
import com.aiwujie.shengmo.adapter.DynamicRvAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.eventbus.ClearRedPointEvent;
import com.aiwujie.shengmo.eventbus.DynamicCommentEvent;
import com.aiwujie.shengmo.eventbus.DynamicEvent;
import com.aiwujie.shengmo.eventbus.DynamicMarke;
import com.aiwujie.shengmo.eventbus.DynamicRewardEvent;
import com.aiwujie.shengmo.eventbus.DynamicSxEvent;
import com.aiwujie.shengmo.eventbus.ViewIsVisibleEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.ListviewOntouchListener;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.TextMoreClickUtils;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.autolayout.AutoLinearLayout;

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

import static com.aiwujie.shengmo.customview.CustomViewPage.SCROLL_STATE_IDLE;

/**
 * Created by 290243232 on 2017/1/12.    动态_广场
 */
public class FragmentDynamicHot extends Fragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.mFragment_dynamic_near_listview)
    PullToRefreshListView mFragmentDynamicNearListview;
    @BindView(R.id.mFragment_dynamic_near_tv)
    TextView mFragmentDynamicNearTv;
    @BindView(R.id.mFragment_dynamic_near_ll_lock)
    AutoLinearLayout mFragmentDynamicNearLlLock;
    @BindView(R.id.mFragment_dynamic_near_top)
    ImageView mFragmentDynamicNearTop;
    @BindView(R.id.rv_dynamic_hot)
    RecyclerView rvDynamicHot;
    @BindView(R.id.srl_dynamic_hot)
    SmartRefreshLayout srlDynamicHot;
    private int page = 0;
    Handler handler = new Handler();
    List<DynamicListData.DataBean> dynamics = new ArrayList<>();
    private DynamicListviewAdapter2 dynamicAdapter;
    private boolean eventBoolean = true;
    /**
     * 是否可见状态
     */
    private boolean isVisible;

    String lastid = "";
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
    private int loadcount = 0;
    private TimeSecondUtils refresh;
    private String sex = "";
    private String sexual = "";
    private boolean isCanLoad = true;
    private String myadmin = "0";
    //    private View tvHeaderView;
//    private TextView tvHeaderViewTv;
    private DynamicRecyclerAdapter dynamicRecyclerAdapter;
    //private DynamicRvAdapter dynamicRecyclerAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.item_fragment_dynamic_near, null);
        ButterKnife.bind(this, view);
//        if (eventBoolean) {
//            EventBus.getDefault().register(this);
//            eventBoolean = false;
//        }
        isPrepared = true;
        lazyLoad();
        isSVIP();
        initRecyclerView();
//        setData();
//        setListener();
        return view;
    }

    private void setData() {
//        tvHeaderView=View.inflate(getActivity(),R.layout.item_dynamic_tv_header,null);
//        tvHeaderViewTv=(TextView)tvHeaderView.findViewById(R.id.item_dynamic_tv_header_tv);
//        tvHeaderViewTv.setMovementMethod(LinkMovementMethod.getInstance());
//        TextMoreClickUtils clickUtils=new TextMoreClickUtils(getActivity(),"0",handler);
//        tvHeaderViewTv.setText(clickUtils.addClickablePart("VIP会员","仅显示","发布的动态"), TextView.BufferType.SPANNABLE);
//        mFragmentDynamicNearListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//        mFragmentDynamicNearListview.getRefreshableView().addHeaderView(tvHeaderView);
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentDynamicNearListview.setRefreshing();
            }
        }, 100);
    }

    private void setListener() {
        mFragmentDynamicNearListview.setOnRefreshListener(this);
//        mFragmentDynamicNearListview.setFocusable(false);
        mFragmentDynamicNearListview.setOnItemClickListener(this);
        mFragmentDynamicNearListview.getRefreshableView().setOnTouchListener(new ListviewOntouchListener(0));
        mFragmentDynamicNearListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
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
                        } else {
                            int first = absListView.getFirstVisiblePosition();
                            int last = absListView.getLastVisiblePosition();
                            //Log.d("xmf",first + " -------- " + last);
                            // Log.d("xmf",dynamics.get(last).getPlayUrl().trim());
                            for (int j = last; j >= first; j--) {
                                if (j > 0 && dynamics.size() > j - 1 && !TextUtil.isEmpty(dynamics.get(j - 1).getPlayUrl().trim())) {
//                                    Log.d("xmf",(j - first) + " ------  " + (absListView.getChildAt(j - first)==null));
                                    // ImageView videoCoverImg = absListView.getChildAt(j - first).findViewById(R.id.video_cover_img);
                                    //  TextureView textureView = absListView.getChildAt(j - first).findViewById(R.id.video_cover_texture);
                                    // TextView tvContent = absListView.getChildAt(j - first).findViewById(R.id.item_dynamic_listview_intro);
//                                    Log.d("xmf",(j - first) + " videoCoverImg  " + (videoCoverImg==null));
                                    // Log.d("xmf",tvContent.getText().toString());
                                    dynamicAdapter.tryToPlayVideo(absListView, j - first, dynamics.get(j - 1).getPlayUrl().trim());
                                    return;
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
                if (mFragmentDynamicNearListview.getRefreshableView().getFirstVisiblePosition() == 0) {
                    mFragmentDynamicNearTop.setVisibility(View.GONE);
                    mFragmentDynamicNearTop.setAnimation(AnimationUtil.ViewToGone());
                } else {
                    if (mFragmentDynamicNearTop.getVisibility() == View.GONE) {
                        mFragmentDynamicNearTop.setVisibility(View.VISIBLE);
                        mFragmentDynamicNearTop.setAnimation(AnimationUtil.ViewToVisible());
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
        map.put("type", "1");
        map.put("page", page + "");
        map.put("loginuid", MyApp.uid);
        map.put("sex", sex);
        map.put("sexual", sexual);
        if (dynamics != null && dynamics.size() > 0) {
            map.put("lastid", dynamics.get(0).getDid());
        }
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetDynamicListNewFive, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                try {
                    DynamicListData listData = new Gson().fromJson(response, DynamicListData.class);
                    if(page == 0) {
                        srlDynamicHot.finishRefresh();
                    } else {
                        srlDynamicHot.finishLoadMore();
                    }
                    if (listData.getData().size() == 0) {
                        if (page != 0) {
                            page = page - 1;
                            isReresh = false;
                        }
                        ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                    } else {
                        isReresh = true;
                        if (page == 0) {
                            dynamics.clear();
                            dynamics.addAll(listData.getData());
                            int retcode = listData.getRetcode();
                            try {
                                //dynamicAdapter = new DynamicListviewAdapter2(getActivity(), dynamics, retcode, myadmin, "1");
                                //mFragmentDynamicNearListview.setAdapter(dynamicAdapter);
                                dynamicRecyclerAdapter = new DynamicRecyclerAdapter(getActivity(), dynamics,retcode,"1");
                                //dynamicRecyclerAdapter = new DynamicRvAdapter(getActivity(), dynamics,retcode,"1");
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                                HeaderViewAdapter headerViewAdapter = new HeaderViewAdapter(dynamicRecyclerAdapter);
                                headerViewAdapter.addHeaderView(getHeaderView());
                                rvDynamicHot.setAdapter(headerViewAdapter);
                                rvDynamicHot.setLayoutManager(linearLayoutManager);

                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        } else {
//                            dynamics.addAll(listData.getData());
//                            dynamicAdapter.notifyDataSetChanged();
                            int temp = dynamics.size();
                            dynamics.addAll(listData.getData());
                            dynamicRecyclerAdapter.notifyItemRangeInserted(temp, listData.getData().size());
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                isCanLoad = true;
                mFragmentDynamicNearListview.onRefreshComplete();
                if (refresh != null) {
                    refresh.cancel();
                }
                EventBus.getDefault().post(new ClearRedPointEvent("ClearDynamicRedPoint", 4));
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        dynamics.clear();
        if (dynamicAdapter != null) {
            dynamicAdapter.notifyDataSetChanged();
        }
        String realname = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "realname", "0");
        String vip = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "vip", "0");
//        if (vip.equals("0")) {//非会员不能看页面
        if (vip.equals("0qq")) {//暂时不让进入该逻辑，目的是让vip 值为0即非会员时能看页面，不进入该逻辑
//            mFragmentDynamicNearListview.getRefreshableView().removeFooterView(tvHeaderView);
            mFragmentDynamicNearListview.onRefreshComplete();
            mFragmentDynamicNearLlLock.setVisibility(View.VISIBLE);
            mFragmentDynamicNearTv.setMovementMethod(LinkMovementMethod.getInstance());
            TextMoreClickUtils clickUtils = new TextMoreClickUtils(getActivity(), "0", handler);
            mFragmentDynamicNearTv.setText(clickUtils.addClickablePart("VIP会员", "限", "可见"), TextView.BufferType.SPANNABLE);
        } else {

            mFragmentDynamicNearLlLock.setVisibility(View.GONE);
            refresh = new TimeSecondUtils(getActivity(), mFragmentDynamicNearListview);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lastid = "";
                    getDynamicList();
                }
            }, 300);
            loadcount++;
            if (loadcount != 1) {
                EventBus.getDefault().post(new ClearRedPointEvent("ClearDynamicRedPoint", 4));
            }
        }
    }


    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastid = dynamics.get(0).getDid();
                getDynamicList();
            }
        }, 300);
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
    public void markeEventBus(DynamicMarke event) {
        if (dynamics.size() != 0) {
            int position = event.getPosition();
            dynamics.get(position).setAuditMark("" + event.getZancount());
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
           // setListener();
            srlDynamicHot.autoRefresh();
        }
    }

    private void getDynamicSx() {
        sex = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dynamicSex", "");
        sexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dynamicSexual", "");
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
        lastid = "";
        getDynamicList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @OnClick(R.id.mFragment_dynamic_near_top)
    public void onClick() {
        //mFragmentDynamicNearListview.getRefreshableView().setSelection(0);
        //mFragmentDynamicNearTop.setVisibility(View.GONE);
        //rvDynamicHot.smoothScrollToPosition(0);
        //mFragmentDynamicFollowTop.setVisibility(View.GONE);
        rvDynamicHot.scrollToPosition(0);
        EventBus.getDefault().post(new ViewIsVisibleEvent(1));
        LinearLayoutManager mLayoutManager =  (LinearLayoutManager) rvDynamicHot.getLayoutManager();
        mLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    public void isSVIP() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetUserPowerInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {

                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            VipAndVolunteerData data = new Gson().fromJson(response, VipAndVolunteerData.class);
                            myadmin = data.getData().getIs_admin();
                            SharedPreferencesUtils.setParam(getActivity(),"admin",myadmin);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    void initRecyclerView() {

        srlDynamicHot.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                getDynamicList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 0;
                getDynamicList();
            }
        });

        rvDynamicHot.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Log.d("xmf","12345");

                //if (newState == SCROLL_STATE_IDLE || newState == SCROLL_STATE_DRAGGING) {
                if (newState == SCROLL_STATE_IDLE) {
                    // DES: 找出当前可视Item位置
                    RecyclerView.LayoutManager layoutManager = rvDynamicHot.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        int mFirstVisiblePosition = linearManager.findFirstVisibleItemPosition();
                        int mLastVisiblePosition = linearManager.findLastVisibleItemPosition();
                        //Log.d("xmf", mFirstVisiblePosition + " --- " + mLastVisiblePosition);
                        if (linearManager.findFirstVisibleItemPosition() == 0) {
                            mFragmentDynamicNearTop.setVisibility(View.GONE);
                            mFragmentDynamicNearTop.setAnimation(AnimationUtil.ViewToGone());
                        } else {
                            if (mFragmentDynamicNearTop.getVisibility() == View.GONE) {
                                mFragmentDynamicNearTop.setVisibility(View.VISIBLE);
                                mFragmentDynamicNearTop.setAnimation(AnimationUtil.ViewToVisible());
                            }
                        }

                        for (int i = mLastVisiblePosition; i >= mFirstVisiblePosition; i--) {
                            int trueIndex = i - 1;//去掉头布局的index
                            if (trueIndex > 0 && dynamics.get(trueIndex).getPlayUrl().length() > 0) {
                                dynamicRecyclerAdapter.tryToPlayVideo(rvDynamicHot, i, dynamics.get(trueIndex).getPlayUrl());
                                return;
                            }
                        }
                    }

                }



            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 20) {
                    ((FragmentDynamicPlaza)getParentFragment()).hideTab();
                } else if (dy < -20){
                    ((FragmentDynamicPlaza)getParentFragment()).showTab();
                }
            }
        });
        rvDynamicHot.setOnTouchListener(new ListviewOntouchListener(0));

        rvDynamicHot.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                int index = rvDynamicHot.getChildAdapterPosition(view);
                dynamicRecyclerAdapter.resetState(rvDynamicHot,index);
            }
        });
    }

    public View getHeaderView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_fragment_hot,null);
        return view;
    }



}

