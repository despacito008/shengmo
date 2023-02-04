package com.aiwujie.shengmo.fragment.dynamic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.DynamicRewardAdapter;
import com.aiwujie.shengmo.adapter.RewardCommentAdapter;
import com.aiwujie.shengmo.bean.RewardData;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.NetWorkUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DynamicDetailRewordFragment extends Fragment {
    int page = 0;
    String did = "";
    @BindView(R.id.iv_layout_normal_empty)
    ImageView ivLayoutNormalEmpty;
    @BindView(R.id.tv_layout_normal_empty)
    TextView tvLayoutNormalEmpty;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    @BindView(R.id.nested_scroll_fragment_dynamic_detail_reward_empty)
    NestedScrollView nestedScrollFragmentDynamicDetailRewardEmpty;
    @BindView(R.id.rv_fragment_dynamic_detail_reward)
    RecyclerView rvFragmentDynamicDetailReward;
    Unbinder unbinder;
    private boolean hasMore = true;
    private boolean isLoading = false;
    private DynamicRewardAdapter rewardCommentAdapter;
    private List<RewardData.DataBean> rewardList;

    public static DynamicDetailRewordFragment newInstance() {
        Bundle args = new Bundle();
        DynamicDetailRewordFragment fragment = new DynamicDetailRewordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_dynamic_detail_reward, container, false);
        did = getActivity().getIntent().getStringExtra("did");
        unbinder = ButterKnife.bind(this, view);
        did = getActivity().getIntent().getStringExtra("did");
        getRewordList();
        return view;
    }

    void getRewordList() {
        isLoading = true;
        HttpHelper.getInstance().getDynamicReward(did, page, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                if (rvFragmentDynamicDetailReward == null) {
                    return;
                }
                isLoading = false;
                RewardData rewardData = new Gson().fromJson(data, RewardData.class);
                if(rewardData != null && rewardData.getData() != null) {
                    List<RewardData.DataBean> tempData = rewardData.getData();
                    if(tempData.size() > 0) {
                        if (nestedScrollFragmentDynamicDetailRewardEmpty != null) {
                            nestedScrollFragmentDynamicDetailRewardEmpty.setVisibility(View.GONE);
                        }
                        if (page == 0) {
                            rewardList = new ArrayList<>();
                            rewardList.addAll(tempData);
                            rewardCommentAdapter = new DynamicRewardAdapter(getActivity(),rewardList,DynamicDetailRewordFragment.this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            rvFragmentDynamicDetailReward.setAdapter(rewardCommentAdapter);
                            rvFragmentDynamicDetailReward.setLayoutManager(linearLayoutManager);
                        } else {
                            int temp = rewardList.size();
                            rewardList.addAll(tempData);
                            rewardCommentAdapter.notifyItemRangeInserted(temp,tempData.size());
                        }
                        rvFragmentDynamicDetailReward.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) {
                                    if (hasMore && !isLoading) {
                                        LogUtil.d("加载下一页");
                                        page++;
                                        getRewordList();
                                    }
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                isLoading = false;
                if(page == 0) {
                    if (nestedScrollFragmentDynamicDetailRewardEmpty != null) {
                        nestedScrollFragmentDynamicDetailRewardEmpty.setVisibility(View.VISIBLE);
                    }
                } else {
                    hasMore = false;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void refreshData() {
        page = 0;
        getRewordList();
    }
}
