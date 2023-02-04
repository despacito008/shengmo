package com.aiwujie.shengmo.fragment.topic;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.newui.TopicDynamicActivity;
import com.aiwujie.shengmo.adapter.DynamicRecyclerAdapter;
import com.aiwujie.shengmo.base.CommonLazyLoadFragment;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.kt.util.OnDynamicItemScrollVisibleListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.view.gloading.Gloading;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.aiwujie.shengmo.customview.CustomViewPage.SCROLL_STATE_IDLE;

public class TopicDynamicFragment extends CommonLazyLoadFragment {
    @BindView(R.id.rv_fragment_topic)
    RecyclerView rvFragmentTopic;
    Unbinder unbinder;
    String type;
    String topicId;
    String lastId = "";
    int page = 0;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    private List<DynamicListData.DataBean> dynamicList;
    private DynamicRecyclerAdapter dynamicRecyclerAdapter;
    private String sex = "";
    private String sexual = "";

    private boolean hasMore = true;
    private boolean isLoading = false;

    public static TopicDynamicFragment newInstance(String type, String topicId) {
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("topicId", topicId);
        TopicDynamicFragment fragment = new TopicDynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    Gloading.Holder gLoadHolder;

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        gLoadHolder = Gloading.getDefault().wrap(rvFragmentTopic);
    }

    @Override
    protected void initData() {
        dynamicList = new ArrayList<>();
        Bundle args = getArguments();
        if (args != null) {
            type = args.getString("type");
            topicId = args.getString("topicId");
        }
        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(topicId)) {
            sex = (String) SharedPreferencesUtils.getParam(getActivity(), "dynamicSex", "");
            sexual = (String) SharedPreferencesUtils.getParam(getActivity(), "dynamicSexual", "");
            gLoadHolder.showLoading();
            getTopicDynamic();
        }
        setListener();
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    protected int providelayoutId() {
        return R.layout.app_fragment_topic;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    void getTopicDynamic() {
        isLoading = true;
        HttpHelper.getInstance().getTopicDynamicList(type, sex, sexual, topicId, lastId, page, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                gLoadHolder.showLoadSuccess();
                hideEmptyView();
                isLoading = false;
                hasMore = true;
                DynamicListData dynamicData = GsonUtil.GsonToBean(data, DynamicListData.class);
                if (dynamicData == null && dynamicData.getData() == null) {
                    return;
                }
                List<DynamicListData.DataBean> tempDynamicList = dynamicData.getData();
                if (page == 0) {
                    dynamicList.clear();
                    dynamicList.addAll(tempDynamicList);
                    lastId = dynamicData.getData().get(0).getDid();
                    dynamicRecyclerAdapter = new DynamicRecyclerAdapter(getActivity(), dynamicList, dynamicData.getRetcode(), type, topicId);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    rvFragmentTopic.setAdapter(dynamicRecyclerAdapter);
                    rvFragmentTopic.setLayoutManager(linearLayoutManager);
                } else {
                    int temp = dynamicList.size();
                    dynamicList.addAll(tempDynamicList);
                    dynamicRecyclerAdapter.notifyItemRangeInserted(temp, tempDynamicList.size());
                }
            }

            @Override
            public void onFail(String msg) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                if (page == 0) {
                    gLoadHolder.showEmpty();
                } else {
                    gLoadHolder.showLoadSuccess();
                }
                isLoading = false;
                hasMore = false;
            }
        });
    }

    public void refreshData(String sex, String sexual) {
        page = 0;
        this.sex = sex;
        this.sexual = sexual;
        if (lastId == null || type == null) {
            return;
        }
        getTopicDynamic();
    }

    public void refreshData() {
        page = 0;
        getTopicDynamic();
    }

    void setListener() {
        rvFragmentTopic.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) {
                    if (hasMore && !isLoading) {
                        LogUtil.d("加载下一页");
                        page++;
                        getTopicDynamic();
                    }
                }
                if (recyclerView.computeVerticalScrollOffset() > 1000) {
                    ((TopicDynamicActivity) getActivity()).showTopIcon();
                } else {
                    ((TopicDynamicActivity) getActivity()).hideTopIcon();
                }
                if (dy > 20) {
                    ((TopicDynamicActivity) getActivity()).hideBottomBar();
                } else if (dy < -20) {
                    ((TopicDynamicActivity) getActivity()).showBottomBar();
                }
            }
        });

        rvFragmentTopic.addOnScrollListener(new OnDynamicItemScrollVisibleListener(dynamicList,0) {
            @Override
            public void onItemScrollVisible(int index) {
                dynamicRecyclerAdapter.tryToPlayVideo(rvFragmentTopic, index, dynamicList.get(index).getPlayUrl());
            }
        });
    }

    public void gotoTop() {
        rvFragmentTopic.scrollToPosition(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    public void hideEmptyView() {
        if (layoutNormalEmpty.getVisibility() == View.VISIBLE) {
            layoutNormalEmpty.setVisibility(View.GONE);
        }
    }

    public void showEmptyView() {
        layoutNormalEmpty.setVisibility(View.VISIBLE);
    }
}
