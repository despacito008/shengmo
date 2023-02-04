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

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.DynamicListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.eventbus.DynamicCommentEvent;
import com.aiwujie.shengmo.eventbus.DynamicEvent;
import com.aiwujie.shengmo.eventbus.DynamicRewardEvent;
import com.aiwujie.shengmo.eventbus.SendDynamicSuccessEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.ListviewOntouchListener;
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
 * Created by 290243232 on 2017/1/12.
 */
public class FragmentDynamicMy extends Fragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {
    @BindView(R.id.mFragment_dynamic_my_listview)
    PullToRefreshListView mFragmentDynamicMyListview;
    @BindView(R.id.mFragment_dynamic_my_top)
    ImageView mFragmentDynamicMyTop;
    private int page = 0;
    Handler handler = new Handler();
    List<DynamicListData.DataBean> dynamics = new ArrayList<>();
    private DynamicListviewAdapter dynamicAdapter;
    private boolean eventBoolean = true;
    String lastid = "";

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.item_fragment_dynamic_my, null);
        ButterKnife.bind(this, view);
        if (eventBoolean) {
            EventBus.getDefault().register(this);
            eventBoolean = false;
        }
        isPrepared = true;
        lazyLoad();
//        setData();
//        setListener();
        return view;
    }

    private void setData() {
        mFragmentDynamicMyListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //实现自动刷新
        //viewpager滑动用这个
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentDynamicMyListview.setRefreshing();
            }
        }, 100);
        //viewpager不滑动用这个
//        mFragmentDynamicMyListview.mHeaderLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                int width = mFragmentDynamicMyListview.mHeaderLayout.getHeight();
//                if (width > 0) {
//                    mFragmentDynamicMyListview.setRefreshing();
//                    mFragmentDynamicMyListview.mHeaderLayout.getViewTreeObserver().removeOnPreDrawListener(this);
//                }
//                return true;
//            }
//        });
    }

    private void setListener() {
        mFragmentDynamicMyListview.setOnRefreshListener(this);
//        mFragmentDynamicMyListview.setFocusable(false);
        mFragmentDynamicMyListview.setOnItemClickListener(this);
        mFragmentDynamicMyListview.getRefreshableView().setOnTouchListener(new ListviewOntouchListener(0));
        mFragmentDynamicMyListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isReresh) {
                                page = page + 1;
                                lastid = dynamics.get(0).getDid();
                                getDynamicList();
                            }
                        }  else {
                            int first = absListView.getFirstVisiblePosition();
                            int last = absListView.getLastVisiblePosition();
                            for (int j = last; j >= first; j--) {
                                if(j > 0 && dynamics.size() > j - 1 && !TextUtil.isEmpty(dynamics.get(j - 1).getPlayUrl().trim())) {
                                    dynamicAdapter.tryToPlayVideo(absListView,j - first,dynamics.get(j - 1).getPlayUrl().trim());
                                    return;
                                }
                            }
                        }
                        break;
                }
                if (mFragmentDynamicMyListview.getRefreshableView().getFirstVisiblePosition() == 0) {
                    mFragmentDynamicMyTop.setVisibility(View.GONE);
                    mFragmentDynamicMyTop.setAnimation(AnimationUtil.ViewToGone());
                } else {
                    if (mFragmentDynamicMyTop.getVisibility() == View.GONE) {
                        mFragmentDynamicMyTop.setVisibility(View.VISIBLE);
                        mFragmentDynamicMyTop.setAnimation(AnimationUtil.ViewToVisible());
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });
//        mFragmentDynamicMyListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                //firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
//                //visibleItemCount：当前能看见的列表项个数（小半个也算）
//                //totalItemCount：列表项共数
//                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0 && visibleItemCount != totalItemCount) {
//                    if (!mFragmentDynamicMyListview.isShownHeader()) {
//                        page = page + 1;
//                        getDynamicList();
//                    }
//                }
//                if(mFragmentDynamicMyListview.getRefreshableView().getFirstVisiblePosition() == 0){
//                    mFragmentDynamicMyTop.setVisibility(View.GONE);
//                    mFragmentDynamicMyTop.setAnimation(AnimationUtil.ViewToGone());
//                }else{
//                    if(mFragmentDynamicMyTop.getVisibility()==View.GONE) {
//                        mFragmentDynamicMyTop.setVisibility(View.VISIBLE);
//                        mFragmentDynamicMyTop.setAnimation(AnimationUtil.ViewToVisible());
//                    }
//                }
//            }
//        });

    }


    private void getDynamicList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("type", "3");
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
                                    dynamics.addAll(listData.getData());
                                    int retcode = listData.getRetcode();
                                    try {
                                        dynamicAdapter = new DynamicListviewAdapter(getActivity(), dynamics, retcode,"3");
                                        mFragmentDynamicMyListview.setAdapter(dynamicAdapter);
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
                        mFragmentDynamicMyListview.onRefreshComplete();
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
        page = 0;
        dynamics.clear();
        if (dynamicAdapter != null) {
            dynamicAdapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(getActivity(), mFragmentDynamicMyListview);
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
            dynamicAdapter.notifyDataSetChanged();
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

//    /**
//     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
//     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
//     *
//     * @param hidden hidden True if the fragment is now hidden, false if it is not
//     *               visible.
//     */
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            isVisible = true;
//            onVisible();
//        } else {
//            isVisible = false;
//            onInvisible();
//        }
//    }

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
            Log.i("haomany", "lazyLoad: " + "one");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @OnClick(R.id.mFragment_dynamic_my_top)
    public void onClick() {
        mFragmentDynamicMyListview.getRefreshableView().setSelection(0);
        mFragmentDynamicMyTop.setVisibility(View.GONE);
    }

}
