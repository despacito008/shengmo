package com.aiwujie.shengmo.fragment.dynamic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.DynamicRecyclerAdapter;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.eventbus.DynamicSxEvent;
import com.aiwujie.shengmo.fragment.dynamicfragment.FragmentDynamicNew;
import com.aiwujie.shengmo.fragment.dynamicfragment.FragmentDynamicPlaza;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.aiwujie.shengmo.customview.CustomViewPage.SCROLL_STATE_IDLE;

public class SquareDynamicFragment extends Fragment {

    @BindView(R.id.rv_fragment_dynamic_push_top)
    RecyclerView rvFragmentDynamicPushTop;
    @BindView(R.id.srl_fragment_dynamic_push_top)
    SmartRefreshLayout srlFragmentDynamicPushTop;
    @BindView(R.id.iv_fragment_dynamic_recommend)
    ImageView ivFragmentDynamicRecommend;
    Unbinder unbinder;
    int page = 0;
    String lastId = "";
    private List<DynamicListData.DataBean> dynamicList;
    private DynamicRecyclerAdapter dynamicRecyclerAdapter;

    public static SquareDynamicFragment newInstance() {
        Bundle args = new Bundle();
        SquareDynamicFragment fragment = new SquareDynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_dynamic_push_top, container, false);
        unbinder = ButterKnife.bind(this, view);
        dynamicList = new ArrayList<>();
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListener();
        sex = SharedPreferencesUtils.geParam(getActivity(), "dynamicSex", "");
        sexual = SharedPreferencesUtils.geParam(getActivity(), "dynamicSexual", "");
        srlFragmentDynamicPushTop.autoRefresh(100);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    public void setListener() {
        srlFragmentDynamicPushTop.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                getDynamicList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 0;
                lastId = "";
                getDynamicList();
            }
        });
        srlFragmentDynamicPushTop.setOnMultiPurposeListener(new OnMultiPurposeListener() {

            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                if (isDragging) {
                    ((FragmentDynamicPlaza) getParentFragment()).hideTab();
                }
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int extendHeight) {

            }



            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int extendHeight) {

            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
                ((FragmentDynamicPlaza) getParentFragment()).showTab();
            }

            @Override
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {

            }


            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int extendHeight) {

            }



            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int extendHeight) {

            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {

            }

            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

            }

            @Override
            public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

            }
        });
        rvFragmentDynamicPushTop.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 20) {
                    ((FragmentDynamicPlaza) getParentFragment()).hideTab();
                    showOrHideTopArrow(false);
                } else if (dy < -20) {
                    ((FragmentDynamicPlaza) getParentFragment()).showTab();
                    if (rvFragmentDynamicPushTop.computeVerticalScrollOffset() > 500) {
                        showOrHideTopArrow(true);
                    }
                }
//                if (rvFragmentDynamicPushTop.computeVerticalScrollOffset() < 500) {
//                    if (ivFragmentDynamicRecommend.getVisibility() == View.VISIBLE) {
//                        ivFragmentDynamicRecommend.setVisibility(View.GONE);
//                    }
//                } else {
//                    if (ivFragmentDynamicRecommend.getVisibility() == View.GONE) {
//                        ivFragmentDynamicRecommend.setVisibility(View.VISIBLE);
//                    }
//                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = rvFragmentDynamicPushTop.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        int mFirstVisiblePosition = linearManager.findFirstVisibleItemPosition();
                        int mLastVisiblePosition = linearManager.findLastVisibleItemPosition();
                        for (int i = mLastVisiblePosition; i >= mFirstVisiblePosition; i--) {
                            int trueIndex = i - 1;//去掉头布局的index
                            if (trueIndex >= 0 && dynamicList.get(trueIndex).getPlayUrl().length() > 0) {
                                dynamicRecyclerAdapter.tryToPlayVideo(rvFragmentDynamicPushTop, i, dynamicList.get(trueIndex).getPlayUrl());
                                return;
                            }
                        }
                    }
                }
            }
        });

        ivFragmentDynamicRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvFragmentDynamicPushTop.scrollToPosition(0);
            }
        });
    }

    void getDynamicList() {
        HttpHelper.getInstance().getDynamicList("1", sex, sexual, "", lastId, page, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                DynamicListData dynamicData = GsonUtil.GsonToBean(data, DynamicListData.class);
                if (dynamicData == null && dynamicData.getData() == null) {
                    return;
                }
                List<DynamicListData.DataBean> tempDynamicList = dynamicData.getData();
                if (page == 0) {
                    srlFragmentDynamicPushTop.finishRefresh();
                    dynamicList.clear();
                    dynamicList.addAll(tempDynamicList);
                    lastId = dynamicData.getData().get(0).getDid();
                    dynamicRecyclerAdapter = new DynamicRecyclerAdapter(getActivity(), dynamicList, dynamicData.getRetcode(), "1");
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    HeaderViewAdapter headerViewAdapter = new HeaderViewAdapter(dynamicRecyclerAdapter);
                    headerViewAdapter.addHeaderView(getHeaderView());
                    rvFragmentDynamicPushTop.setAdapter(headerViewAdapter);
                    rvFragmentDynamicPushTop.setLayoutManager(linearLayoutManager);
                } else {
                    srlFragmentDynamicPushTop.finishLoadMore();
                    int temp = dynamicList.size();
                    dynamicList.addAll(tempDynamicList);
                    dynamicRecyclerAdapter.notifyItemRangeInserted(temp, tempDynamicList.size());
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    public View getHeaderView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_fragment_hot, null);
        return view;
    }


    String sex = "";
    String sexual = "";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(DynamicSxEvent data) {
        page = 0;
        sex = data.getSex();
        sexual = data.getSexual();
        lastId = "";
        getDynamicList();
    }
    void showOrHideTopArrow(boolean isShow) {
        if (isShow) {
            if (ivFragmentDynamicRecommend.getVisibility() == View.GONE) {
                ivFragmentDynamicRecommend.setVisibility(View.VISIBLE);
                ivFragmentDynamicRecommend.setAnimation(AnimationUtil.moveToViewLocation());
            }
        } else {
            if (ivFragmentDynamicRecommend.getVisibility() == View.VISIBLE) {
                ivFragmentDynamicRecommend.setVisibility(View.GONE);
                ivFragmentDynamicRecommend.setAnimation(AnimationUtil.moveToViewBottom());
            }
        }
    }

//    boolean isFirstLoad = true;
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && isFirstLoad) {
//            srlFragmentDynamicPushTop.autoRefresh(100);
//            isFirstLoad = false;
//        }
//    }
}
