package com.aiwujie.shengmo.fragment.dynamic;

import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.BannerWebActivity;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.TopicDetailActivity;
import com.aiwujie.shengmo.activity.ranking.ActiveAndPopularityRankingActivity;
import com.aiwujie.shengmo.activity.ranking.ActiveRankingActivity;
import com.aiwujie.shengmo.activity.ranking.PopularityRankingActivity;
import com.aiwujie.shengmo.activity.ranking.RewardRankingActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.DynamicRecyclerAdapter;
import com.aiwujie.shengmo.adapter.RecyclerViewDynamicAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BannerNewData;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.bean.TopicHeaderData;
import com.aiwujie.shengmo.eventbus.DynamicSxEvent;
import com.aiwujie.shengmo.fragment.dynamicfragment.FragmentDynamicNew;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.util.OnRefreshStateListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.BannerUtils;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.AutoPollRecyclerView;
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.android.percent.support.PercentFrameLayout;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.DTDHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.aiwujie.shengmo.customview.CustomViewPage.SCROLL_STATE_IDLE;

public class RecommendDynamicFragment extends Fragment {

    @BindView(R.id.rv_fragment_dynamic_recommend)
    RecyclerView rvFragmentDynamicRecommend;
    @BindView(R.id.srl_fragment_dynamic_recommend)
    SmartRefreshLayout srlFragmentDynamicRecommend;
    @BindView(R.id.iv_fragment_dynamic_recommend)
    ImageView ivFragmentDynamicRecommend;
    Unbinder unbinder;
    List<DynamicListData.DataBean> dynamicList;
    private String sex = "";
    private String sexual = "";
    private String lastId = "";
    int page = 0;
    private DynamicRecyclerAdapter dynamicRecyclerAdapter;
    private View headerView;

    public static RecommendDynamicFragment newInstance() {
        Bundle args = new Bundle();
        RecommendDynamicFragment fragment = new RecommendDynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_dynamic_recommend, container, false);
        unbinder = ButterKnife.bind(this, view);
        dynamicList = new ArrayList<>();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListener();
        initHeaderView();
        getDynamicSx();
        getBanner();
        getTopicHeader();
        EventBus.getDefault().register(this);
        srlFragmentDynamicRecommend.autoRefresh(100);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    public void setListener() {
        srlFragmentDynamicRecommend.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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

        srlFragmentDynamicRecommend.setOnMultiPurposeListener(new OnRefreshStateListener() {
            @Override
            public void doPullStart() {
                ((FragmentDynamicNew)getParentFragment()).hideTab();
            }

            @Override
            public void doPullEnd() {
                ((FragmentDynamicNew)getParentFragment()).showTab();
            }
        });

        rvFragmentDynamicRecommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 20) {
                    ((FragmentDynamicNew)getParentFragment()).hideTab();
                    showOrHideTopArrow(false);
                } else if (dy < -20){
                    ((FragmentDynamicNew)getParentFragment()).showTab();
                    if (rvFragmentDynamicRecommend.computeVerticalScrollOffset() > 500) {
                        showOrHideTopArrow(true);
                    }
                }
//                if (rvFragmentDynamicRecommend.computeVerticalScrollOffset() < 500) {
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
                    if (rvFragmentDynamicRecommend == null)  {
                        return;
                    }
                    RecyclerView.LayoutManager layoutManager = rvFragmentDynamicRecommend.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        int mFirstVisiblePosition = linearManager.findFirstVisibleItemPosition();
                        int mLastVisiblePosition = linearManager.findLastVisibleItemPosition();
                        for (int i = mLastVisiblePosition; i >= mFirstVisiblePosition; i--) {
                            int trueIndex = i - 1;//去掉头布局的index
                            if (trueIndex >= 0 && dynamicList.get(trueIndex).getPlayUrl().length() > 0) {
                                dynamicRecyclerAdapter.tryToPlayVideo(rvFragmentDynamicRecommend, i, dynamicList.get(trueIndex).getPlayUrl());
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
                rvFragmentDynamicRecommend.scrollToPosition(0);
            }
        });
    }

    private void getDynamicSx() {
        sex = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dynamicSex", "");
        sexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dynamicSexual", "");
    }

    public void getDynamicList() {
        HttpHelper.getInstance().getDynamicList("0", sex, sexual, "",lastId, page, new HttpListener() {
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
                    srlFragmentDynamicRecommend.finishRefresh();
                    dynamicList.clear();
                    dynamicList.addAll(tempDynamicList);
                    lastId = dynamicData.getData().get(0).getDid();
                    dynamicRecyclerAdapter = new DynamicRecyclerAdapter(getActivity(), dynamicList, dynamicData.getRetcode(), "0");
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    HeaderViewAdapter headerViewAdapter = new HeaderViewAdapter(dynamicRecyclerAdapter);
                    headerViewAdapter.addHeaderView(headerView);
                    rvFragmentDynamicRecommend.setAdapter(headerViewAdapter);
                    rvFragmentDynamicRecommend.setLayoutManager(linearLayoutManager);
                } else {
                    srlFragmentDynamicRecommend.finishLoadMore();
                    int temp = dynamicList.size();
                    dynamicList.addAll(tempDynamicList);
                    if (dynamicRecyclerAdapter != null) {
                        dynamicRecyclerAdapter.notifyItemRangeInserted(temp, tempDynamicList.size());
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                ToastUtil.show(getActivity(), msg);
                if (page == 0) {
                    srlFragmentDynamicRecommend.finishRefresh();
                } else {
                    srlFragmentDynamicRecommend.finishLoadMore();
                }
            }
        });
    }

    public void initHeaderView() {
        headerView = View.inflate(getActivity(), R.layout.item_dynamic_header, null);
        LinearLayout ll01 = (LinearLayout) headerView.findViewById(R.id.mDynamic_header_ll01);
        LinearLayout ll02 = (LinearLayout) headerView.findViewById(R.id.mDynamic_header_ll02);
        LinearLayout ll03 = (LinearLayout) headerView.findViewById(R.id.mDynamic_header_ll03);
        LinearLayout ll05 = (LinearLayout) headerView.findViewById(R.id.mDynamic_header_ll05);
        topicRecyclerview =  headerView.findViewById(R.id.mDynamic_recyclerview);
        ll01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RewardRankingActivity.class);
                startActivity(intent);
            }
        });
        ll02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PopularityRankingActivity.class);
                startActivity(intent);
            }
        });
        ll03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), ActiveRankingActivity.class);
                Intent intent = new Intent(getActivity(), ActiveAndPopularityRankingActivity.class);
                startActivity(intent);
            }
        });
        ll05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private List<String> bannerTitle = new ArrayList<>();
    private List<String> bannerPath = new ArrayList<>();
    private List<String> bannerUrl = new ArrayList<>();
    private List<String> linkType = new ArrayList<>();
    private List<String> linkId = new ArrayList<>();
    private AutoPollRecyclerView topicRecyclerview;

    private void getBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "2");
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetSlideMore, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
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
                            break;
                        case 4000:
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                final TopicHeaderData data = new Gson().fromJson(response, TopicHeaderData.class);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                topicRecyclerview.setLayoutManager(linearLayoutManager);
                if (data.getRetcode() == 2000) {
                    RecyclerViewDynamicAdapter recyclerAdapter = new RecyclerViewDynamicAdapter(data.getData(), getActivity());
                    recyclerAdapter.setHeaderView(View.inflate(getActivity(), R.layout.item_dynamic_recycler_header, null));
                    topicRecyclerview.setAdapter(recyclerAdapter);
                    if (data.getData().size() >= 4) {
                        topicRecyclerview.start();
                    }
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
                    topicRecyclerview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }


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



}
