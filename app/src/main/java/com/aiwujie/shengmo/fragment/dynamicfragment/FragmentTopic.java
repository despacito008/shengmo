package com.aiwujie.shengmo.fragment.dynamicfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.TopicDetailActivity;
import com.aiwujie.shengmo.adapter.TopicAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.ListTopicData;
import com.aiwujie.shengmo.eventbus.DynamicSxEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.ListviewOntouchListener;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
import butterknife.Unbinder;

/**
 * Created by 290243232 on 2017/7/18.
 */

public class FragmentTopic extends Fragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {
    @BindView(R.id.mFragment_topic_listview)
    PullToRefreshListView mFragmentTopicListview;
    @BindView(R.id.mFragment_dynamic_hot_top)
    ImageView mFragmentDynamicHotTop;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    @BindView(R.id.iv_layout_normal_empty)
    ImageView ivLayoutNormalEmpty;
    @BindView(R.id.tv_layout_normal_empty)
    TextView tvLayoutNormalEmpty;
    private int topicState;
    private int topicType;
    private Handler handler = new Handler();
    List<ListTopicData.DataBean> topicDatas = new ArrayList<>();
    int page = 0;
    private String sex = "";
    private String sexual = "";
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
    private TimeSecondUtils refresh;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;
    private TopicAdapter topicAdapter;
    private int topicflag;
    private ListTopicData listData;
    private boolean isCanLoad = true;
    private Unbinder unbinder;

    public FragmentTopic() {

    }

    public static FragmentTopic newInstance(int topicState, int topicType) {
        FragmentTopic newFragment = new FragmentTopic();
        Bundle bundle = new Bundle();
        bundle.putInt("topicState", topicState);
        bundle.putInt("topicType", topicType);
        newFragment.setArguments(bundle);
        return newFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.item_fragment_topic, null);
        unbinder = ButterKnife.bind(this, view);
//        userid=getActivity().getIntent().getStringExtra("uid");
        topicflag = getActivity().getIntent().getIntExtra("topicflag", -1);
        EventBus.getDefault().register(this);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    private void setData() {
        if (mFragmentTopicListview == null) {
            return;
        }
        mFragmentTopicListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentTopicListview.setRefreshing();
            }
        }, 100);

        layoutNormalEmpty.setBackgroundResource(R.color.white);
        tvLayoutNormalEmpty.setText("暂无话题");
    }

    private void setListener() {
        if (mFragmentTopicListview == null || mFragmentTopicListview.getRefreshableView() == null) {
            return;
        }
        mFragmentTopicListview.getRefreshableView().setOnTouchListener(new ListviewOntouchListener(0));
        mFragmentTopicListview.setOnRefreshListener(this);
        mFragmentTopicListview.setOnItemClickListener(this);
        mFragmentTopicListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isCanLoad) {
                                isCanLoad = false;
                                if (isReresh) {
                                    page = page + 1;
                                    if (topicType == 0)
                                        getFollowTopicList();
                                    else
                                        getTopicList();
                                }
                            }
                        }
                        break;
                }
                if (mFragmentTopicListview.getRefreshableView().getFirstVisiblePosition() == 0) {
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

    @OnClick(R.id.mFragment_dynamic_hot_top)
    public void onClick() {
//        EventBus.getDefault().post("tuitop");
        mFragmentTopicListview.getRefreshableView().setSelection(0);
        mFragmentDynamicHotTop.setVisibility(View.GONE);
    }

    private void getTopicList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", page + "");
//        if (topicType == 1) {
//            map.put("type", "1");
//        } else
        if (topicType == 8) {
            map.put("type", "2");
        } else if (topicType == 9) {
            map.put("type", "3");
        } else {
            map.put("type", "0");
            map.put("pid", topicState + "");
        }
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetTopicList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("fragmenttopic", "onSuccess: " + response);
                if (mFragmentTopicListview == null) {
                    return;
                }
                try {
                    listData = new Gson().fromJson(response, ListTopicData.class);
                    if (listData.getData().size() == 0) {
                        if (page != 0) {
                            page = page - 1;
                            isReresh = false;
                            ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                        }
                    } else {
                        isReresh = true;
                        if (page == 0) {
                            topicDatas.addAll(listData.getData());
                            try {
                                topicAdapter = new TopicAdapter(getActivity(), topicDatas);
                                mFragmentTopicListview.setAdapter(topicAdapter);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        } else {
                            topicDatas.addAll(listData.getData());
                            topicAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                isCanLoad = true;
                mFragmentTopicListview.onRefreshComplete();
                if (refresh != null) {
                    refresh.cancel();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void getFollowTopicList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetFollowTopicList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("fragmenttopic", "onSuccess: " + response);
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                try {
                    listData = new Gson().fromJson(response, ListTopicData.class);
                    if (listData.getData().size() == 0) {
                        if (page != 0) {
                            page = page - 1;
                            isReresh = false;
                            ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                        } else {
                            layoutNormalEmpty.setVisibility(View.VISIBLE);

                        }
                    } else {
                        isReresh = true;
                        if (page == 0) {
                            topicDatas.addAll(listData.getData());
                            try {
                                topicAdapter = new TopicAdapter(getActivity(), topicDatas);
                                mFragmentTopicListview.setAdapter(topicAdapter);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        } else {
                            topicDatas.addAll(listData.getData());
                            topicAdapter.notifyDataSetChanged();
                        }
                        if (layoutNormalEmpty.getVisibility() == View.VISIBLE) {
                            layoutNormalEmpty.setVisibility(View.GONE);
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                isCanLoad = true;
                mFragmentTopicListview.onRefreshComplete();
                if (refresh != null) {
                    refresh.cancel();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                layoutNormalEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (topicflag == -1) {
            Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
            intent.putExtra("tid", topicDatas.get(position - 1).getTid());
            intent.putExtra("topictitle", topicDatas.get(position - 1).getTitle());
            intent.putExtra("topicState", topicState);
            startActivity(intent);
        } else {
            if (topicDatas.get(position - 1).getIs_admin().equals("0")) {
                Intent intent = new Intent();
                intent.putExtra("topicid", topicDatas.get(position - 1).getTid());
                intent.putExtra("topictitle", topicDatas.get(position - 1).getTitle());
                intent.putExtra("topicState", topicState);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            } else {
                ToastUtil.show(getActivity().getApplicationContext(), "官方话题不允许参与哦~");
            }
        }
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        topicDatas.clear();
        if (topicAdapter != null) {
            topicAdapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(getActivity(), mFragmentTopicListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (topicType == 0)
                    getFollowTopicList();
                else
                    getTopicList();
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
            Bundle args = getArguments();
            if (args != null) {
                topicState = args.getInt("topicState");
                topicType = args.getInt("topicType");
            }

            setData();
            setListener();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(DynamicSxEvent data) {
//        page=0;
//        sex = data.getSex();
//        sexual = data.getSexual();
//        topicDatas.clear();
//        if (topicAdapter != null) {
//            topicAdapter.notifyDataSetChanged();
//        }
////        lastid = "";
//        getTopicList();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}