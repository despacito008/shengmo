package com.aiwujie.shengmo.fragment.dynamicfragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.DynamicListviewAdapter;
import com.aiwujie.shengmo.adapter.DynamicRecyclerAdapter;
import com.aiwujie.shengmo.adapter.DynamicRvAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.eventbus.ClearRedPointEvent;
import com.aiwujie.shengmo.eventbus.DynamicCommentEvent;
import com.aiwujie.shengmo.eventbus.DynamicEvent;
import com.aiwujie.shengmo.eventbus.DynamicRewardEvent;
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
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.scwang.smartrefresh.header.DeliveryHeader;
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
 * 动态-关注
 * Created by 290243232 on 2017/1/12.
 */
public class FragmentDynamicFollow extends Fragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.mFragment_dynamic_follow_listview)
    PullToRefreshListView mFragmentDynamicFollowListview;
    @BindView(R.id.mFragment_dynamic_follow_tv)
    TextView mFragmentDynamicFollowTv;
    @BindView(R.id.mFragment_dynamic_follow_ll_lock)
    AutoLinearLayout mFragmentDynamicFollowLlLock;
    @BindView(R.id.mFragment_dynamic_follow_top)
    ImageView mFragmentDynamicFollowTop;
    @BindView(R.id.rv_dynamic_follow)
    RecyclerView rvDynamicFollow;
    @BindView(R.id.srl_dynamic_follow)
    SmartRefreshLayout srlDynamicFollow;
    //    @BindView(R.id.mFragment_dynamic_follow_listview)
//    MyListView mFragmentDynamicFollowListview;
//    @BindView(R.id.mFragment_dynamic_follow_scrollview)
//    PullToRefreshScrollView mFragmentDynamicFollowScrollview;
    private int page = 0;
    Handler handler = new Handler();
    List<DynamicListData.DataBean> dynamics = new ArrayList<>();
    private DynamicListviewAdapter dynamicAdapter;
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
    private TimeSecondUtils refresh;
    private boolean isCanLoad = true;
    private DynamicRecyclerAdapter dynamicRecyclerAdapter;
    //private DynamicRvAdapter dynamicRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.item_fragment_dynamic_follow, null);
        ButterKnife.bind(this, view);
        if (eventBoolean) {
            EventBus.getDefault().register(this);
            eventBoolean = false;
        }
        isPrepared = true;
        lazyLoad();
        initRecyclerView();
//        setData();
//        setListener();
        return view;
    }

    private void setData() {
        mFragmentDynamicFollowListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentDynamicFollowListview.setRefreshing();
            }
        }, 100);
    }

    private void setListener() {
        mFragmentDynamicFollowListview.setOnRefreshListener(this);
        mFragmentDynamicFollowListview.setOnItemClickListener(this);
        mFragmentDynamicFollowListview.getRefreshableView().setOnTouchListener(new ListviewOntouchListener(0));
//        mFragmentDynamicFollowListview.setFocusable(false);
        mFragmentDynamicFollowListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
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
                }
//                if (mFragmentDynamicFollowListview.getRefreshableView().getFirstVisiblePosition() == 0) {
//                    mFragmentDynamicFollowTop.setVisibility(View.GONE);
//                    mFragmentDynamicFollowTop.setAnimation(AnimationUtil.ViewToGone());
//                } else {
//                    if (mFragmentDynamicFollowTop.getVisibility() == View.GONE) {
//                        mFragmentDynamicFollowTop.setVisibility(View.VISIBLE);
//                        mFragmentDynamicFollowTop.setAnimation(AnimationUtil.ViewToVisible());
//                    }
//                }
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
        map.put("type", "2");
        map.put("page", page + "");
        map.put("loginuid", MyApp.uid);
        map.put("lastid", lastid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetDynamicListNewFive, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fragmentDynamichot", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(response);
                            int retcode = object.getInt("retcode");
                            if (retcode == 2000 || retcode == 2001) {
                                if(page == 0) {
                                    srlDynamicFollow.finishRefresh();
                                } else {
                                    srlDynamicFollow.finishLoadMore();
                                }
                                final DynamicListData listData = new Gson().fromJson(response, DynamicListData.class);
                                if (listData.getData().size() == 0) {
                                    if (page != 0) {
                                        page = page - 1;
                                        isReresh = false;
                                    }
                                    ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                                } else {
                                    isReresh = true;
                                    if (page == 0) {
//                                        if(TextUtil.isEmpty(SharedPreferencesUtils.getParam(getActivity(),"tempData","").toString())){
//                                            SharedPreferencesUtils.setParam(getActivity(),"tempData",response);
//                                        }
                                        dynamics.clear();
                                        dynamics.addAll(listData.getData());
                                        try {
//                                            dynamicAdapter = new DynamicListviewAdapter(getActivity(), dynamics, retcode, "2");
//                                            mFragmentDynamicFollowListview.setAdapter(dynamicAdapter);
                                            dynamicRecyclerAdapter = new DynamicRecyclerAdapter(getActivity(), dynamics,retcode,"2");
                                            //dynamicRecyclerAdapter = new DynamicRvAdapter(getActivity(), dynamics,retcode,"2");
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//                                            HeaderViewAdapter headerViewAdapter = new HeaderViewAdapter(dynamicRecyclerAdapter);
//                                            headerViewAdapter.addHeaderView(getHeaderView());
                                            rvDynamicFollow.setAdapter(dynamicRecyclerAdapter);
                                            rvDynamicFollow.setLayoutManager(linearLayoutManager);

                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        //dynamics.addAll(listData.getData());
                                        //dynamicAdapter.notifyDataSetChanged();
                                        int temp = dynamics.size();
                                        dynamics.addAll(listData.getData());
                                        dynamicRecyclerAdapter.notifyItemRangeInserted(temp, listData.getData().size());
                                    }
                                }
                            } else if (retcode == 4343 || retcode == 4344) {
                                mFragmentDynamicFollowLlLock.setVisibility(View.VISIBLE);
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        isCanLoad = true;
                        mFragmentDynamicFollowListview.onRefreshComplete();
                        if (refresh != null) {
                            refresh.cancel();
                        }
                        EventBus.getDefault().post(new ClearRedPointEvent("clearGroupRedPoint", 6));
                    }
                });
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
        refresh = new TimeSecondUtils(getActivity(), mFragmentDynamicFollowListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastid = "";
                getDynamicList();
            }
        }, 300);

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
        }, 500);
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
            setData();
            setListener();
            srlDynamicFollow.autoRefresh(100);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @OnClick(R.id.mFragment_dynamic_follow_top)
    public void onClick() {
        //mFragmentDynamicFollowListview.getRefreshableView().setSelection(0);
        //rvDynamicFollow.smoothScrollToPosition(0);
        mFragmentDynamicFollowTop.setVisibility(View.GONE);
        rvDynamicFollow.scrollToPosition(0);
        LinearLayoutManager mLayoutManager =  (LinearLayoutManager) rvDynamicFollow.getLayoutManager();
        mLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // null.unbind();
    }

    void initRecyclerView() {

        srlDynamicFollow.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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

        rvDynamicFollow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //if (newState == SCROLL_STATE_IDLE || newState == SCROLL_STATE_DRAGGING) {
                if (newState == SCROLL_STATE_IDLE) {
                    // DES: 找出当前可视Item位置
                    RecyclerView.LayoutManager layoutManager = rvDynamicFollow.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        int mFirstVisiblePosition = linearManager.findFirstVisibleItemPosition();
                        int mLastVisiblePosition = linearManager.findLastVisibleItemPosition();
                        //Log.d("xmf", mFirstVisiblePosition + " --- " + mLastVisiblePosition);

                        if (linearManager.findFirstVisibleItemPosition() == 0) {
                            mFragmentDynamicFollowTop.setVisibility(View.GONE);
                            mFragmentDynamicFollowTop.setAnimation(AnimationUtil.ViewToGone());
                        } else {
                            if (mFragmentDynamicFollowTop.getVisibility() == View.GONE) {
                                mFragmentDynamicFollowTop.setVisibility(View.VISIBLE);
                                mFragmentDynamicFollowTop.setAnimation(AnimationUtil.ViewToVisible());
                            }
                        }

                        for (int i = mLastVisiblePosition; i >= mFirstVisiblePosition; i--) {
                            if ( i < 0) {
                                return;
                            }
                            if (dynamics.get(i).getPlayUrl().length() > 0) {
                                dynamicRecyclerAdapter.tryToPlayVideo(rvDynamicFollow, i, dynamics.get(i).getPlayUrl());
                                return;
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.d("xmf","---"+dy);
                if (dy < -20) { // 当前处于上滑状态
                    EventBus.getDefault().post(new ViewIsVisibleEvent(1));
                } else if (dy > 20) { // 当前处于下滑状态
                    EventBus.getDefault().post(new ViewIsVisibleEvent(0));
                }

                if (dy > 20) {
                    //((FragmentDynamicPlaza) getParentFragment()).hideTab();
                    showOrHideTopArrow(false);
                } else if (dy < -20) {
                   // ((FragmentDynamicPlaza) getParentFragment()).showTab();
                    if (rvDynamicFollow.computeVerticalScrollOffset() > 500) {
                        showOrHideTopArrow(true);
                    }
                }
            }
        });

        rvDynamicFollow.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                int index = rvDynamicFollow.getChildAdapterPosition(view);
                //Log.d("xmf",index + " 被回收了");
            }
        });
    }

    public View getHeaderView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_fragment_hot,null);
        return view;
    }

    void showOrHideTopArrow(boolean isShow) {
        if (isShow) {
            if (mFragmentDynamicFollowTop.getVisibility() == View.GONE) {
                mFragmentDynamicFollowTop.setVisibility(View.VISIBLE);
                mFragmentDynamicFollowTop.setAnimation(AnimationUtil.moveToViewLocation());
            }
        } else {
            if (mFragmentDynamicFollowTop.getVisibility() == View.VISIBLE) {
                mFragmentDynamicFollowTop.setVisibility(View.GONE);
                mFragmentDynamicFollowTop.setAnimation(AnimationUtil.moveToViewBottom());
            }
        }
    }
}
