package com.aiwujie.shengmo.activity.newui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.InviteRewardRecyclerAdapter;
import com.aiwujie.shengmo.base.BaseNetActivity;
import com.aiwujie.shengmo.bean.InviteRewardBean;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvitationRewardDetailActivity extends BaseNetActivity {
    @BindView(R.id.iv_normal_title_back)
    ImageView ivNormalTitleBack;
    @BindView(R.id.tv_normal_title_title)
    TextView tvNormalTitleTitle;
    @BindView(R.id.iv_normal_title_more)
    ImageView ivNormalTitleMore;
    @BindView(R.id.tv_normal_title_more)
    TextView tvNormalTitleMore;
    List<InviteRewardBean.DataBean> inviteRewardList;
    int page = 0;
    @BindView(R.id.rv_invite_reward)
    RecyclerView rvInviteReward;
    @BindView(R.id.iv_layout_normal_empty)
    ImageView ivLayoutNormalEmpty;
    @BindView(R.id.tv_layout_normal_empty)
    TextView tvLayoutNormalEmpty;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    private InviteRewardRecyclerAdapter rewardRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_reward_detail);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
        ivNormalTitleMore.setVisibility(View.INVISIBLE);
        tvNormalTitleTitle.setText("奖励明细");
        ivNormalTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getRewordDetail();
        inviteRewardList = new ArrayList<>();
    }

    boolean hasMore = true;
    boolean isLoading = false;

    void getRewordDetail() {
        isLoading = true;
        HttpHelper.getInstance().getUserInviteRewardState(page, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(InvitationRewardDetailActivity.this)) {
                    return;
                }
                isLoading = false;
                InviteRewardBean inviteRewardBean = GsonUtil.GsonToBean(data, InviteRewardBean.class);
                List<InviteRewardBean.DataBean> tempList = inviteRewardBean.getData();
                if (page == 0) {
                    inviteRewardList.clear();
                    inviteRewardList.addAll(tempList);
                    rewardRecyclerAdapter = new InviteRewardRecyclerAdapter(InvitationRewardDetailActivity.this, inviteRewardList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InvitationRewardDetailActivity.this);
                    rvInviteReward.setAdapter(rewardRecyclerAdapter);
                    rvInviteReward.setLayoutManager(linearLayoutManager);
                    rvInviteReward.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) {
                                if (hasMore && !isLoading) {
                                    LogUtil.d("加载下一页");
                                    page++;
                                    getRewordDetail();
                                }
                            }
                        }
                    });
                } else {
                    int temp = inviteRewardList.size();
                    inviteRewardList.addAll(tempList);
                    rewardRecyclerAdapter.notifyItemRangeInserted(temp, tempList.size());
                }
            }

            @Override
            public void onFail(int code, String msg) {
                isLoading = false;
                if (page == 0) {
                    if (layoutNormalEmpty != null) {
                        layoutNormalEmpty.setVisibility(View.VISIBLE);
                    }
                } else {
                    hasMore = false;
                }
            }
        });
    }
}
