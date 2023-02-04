package com.aiwujie.shengmo.fragment.rank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.ranking.RewardRankingActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.RewardRankRecyclerAdapter;
import com.aiwujie.shengmo.base.CommonLazyLoadFragment;
import com.aiwujie.shengmo.bean.RankBerewardData;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.view.ZoomInScrollView;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kotlin.collections.ArraysKt;

public class RewardRankingWealthFragment extends CommonLazyLoadFragment {
    @BindView(R.id.cl_fragment_reward_ranking)
    ConstraintLayout clFragmentRewardRanking;
    @BindView(R.id.rv_fragment_reward_ranking)
    RecyclerView rvFragmentRewardRanking;
    @BindView(R.id.zoom_scroll_reward_ranking)
    ZoomInScrollView zoomScrollRewardRanking;
    Unbinder unbinder;
    @BindView(R.id.view_fragment_reward_ranking_wealth_light)
    View viewFragmentRewardRankingWealthLight;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_one)
    ImageView ivFragmentRewardRankingWealthTopOne;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_two)
    ImageView ivFragmentRewardRankingWealthTopTwo;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_three)
    ImageView ivFragmentRewardRankingWealthTopThree;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_one)
    TextView tvFragmentRewardRankingWealthTopOne;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_two)
    TextView tvFragmentRewardRankingWealthTopTwo;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_three)
    TextView tvFragmentRewardRankingWealthTopThree;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_one_sex)
    ImageView ivFragmentRewardRankingWealthTopOneSex;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_one_age)
    TextView tvFragmentRewardRankingWealthTopOneAge;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_one_sex)
    TextView tvFragmentRewardRankingWealthTopOneSex;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_one_richCount)
    TextView tvFragmentRewardRankingWealthTopOneRichCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_one_richCount)
    LinearLayout llFragmentRewardRankingWealthTopOneRichCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_one_info)
    LinearLayout llFragmentRewardRankingWealthTopOneInfo;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_one_send)
    LinearLayout llFragmentRewardRankingWealthTopOneSend;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_one_favour)
    ImageView ivFragmentRewardRankingWealthTopOneFavour;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_one_favour)
    LinearLayout llFragmentRewardRankingWealthTopOneFavour;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_two_sex)
    ImageView ivFragmentRewardRankingWealthTopTwoSex;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_two_age)
    TextView tvFragmentRewardRankingWealthTopTwoAge;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_two_sex)
    TextView tvFragmentRewardRankingWealthTopTwoSex;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_two_richCount)
    TextView tvFragmentRewardRankingWealthTopTwoRichCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_two_richCount)
    LinearLayout llFragmentRewardRankingWealthTopTwoRichCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_two_info)
    LinearLayout llFragmentRewardRankingWealthTopTwoInfo;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_two_send)
    LinearLayout llFragmentRewardRankingWealthTopTwoSend;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_two_favour)
    ImageView ivFragmentRewardRankingWealthTopTwoFavour;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_two_favour)
    LinearLayout llFragmentRewardRankingWealthTopTwoFavour;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_three_sex)
    ImageView ivFragmentRewardRankingWealthTopThreeSex;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_three_age)
    TextView tvFragmentRewardRankingWealthTopThreeAge;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_three_sex)
    TextView tvFragmentRewardRankingWealthTopThreeSex;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_three_richCount)
    TextView tvFragmentRewardRankingWealthTopThreeRichCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_three_richCount)
    LinearLayout llFragmentRewardRankingWealthTopThreeRichCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_three_info)
    LinearLayout llFragmentRewardRankingWealthTopThreeInfo;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_three_send)
    LinearLayout llFragmentRewardRankingWealthTopThreeSend;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_three_favour)
    ImageView ivFragmentRewardRankingWealthTopThreeFavour;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_three_favour)
    LinearLayout llFragmentRewardRankingWealthTopThreeFavour;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_one_sendCount)
    TextView tvFragmentRewardRankingWealthTopOneSendCount;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_two_sendCount)
    TextView tvFragmentRewardRankingWealthTopTwoSendCount;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_three_sendCount)
    TextView tvFragmentRewardRankingWealthTopThreeSendCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_one_sex)
    LinearLayout llFragmentRewardRankingWealthTopOneSex;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_two_sex)
    LinearLayout llFragmentRewardRankingWealthTopTwoSex;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_three_sex)
    LinearLayout llFragmentRewardRankingWealthTopThreeSex;
    @BindView(R.id.view_fragment_reward_ranking_wealth_top_one_bg)
    View viewFragmentRewardRankingWealthTopOneBg;
    @BindView(R.id.view_fragment_reward_ranking_wealth_top_two_bg)
    View viewFragmentRewardRankingWealthTopTwoBg;
    @BindView(R.id.view_fragment_reward_ranking_wealth_top_three_bg)
    View viewFragmentRewardRankingWealthTopThreeBg;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_one_crown)
    ImageView ivFragmentRewardRankingWealthTopOneCrown;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_two_crown)
    ImageView ivFragmentRewardRankingWealthTopTwoCrown;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_three_crown)
    ImageView ivFragmentRewardRankingWealthTopThreeCrown;
    @BindView(R.id.group_reward_ranking_top_one)
    Group groupRewardRankingTopOne;
    @BindView(R.id.group_reward_ranking_top_two)
    Group groupRewardRankingTopTwo;
    @BindView(R.id.group_reward_ranking_top_three)
    Group groupRewardRankingTopThree;
    @BindView(R.id.group_reward_ranking_top_one_info)
    Group groupRewardRankingTopOneInfo;
    @BindView(R.id.group_reward_ranking_top_two_info)
    Group groupRewardRankingTopTwoInfo;
    @BindView(R.id.group_reward_ranking_top_three_info)
    Group groupRewardRankingTopThreeInfo;


    private String type;
    private int page;
    private RewardRankRecyclerAdapter rewardRankRecyclerAdapter;
    private List<RankBerewardData.DataBean> rankList;
    private boolean hasMore = true;
    private boolean isLoading = false;

    public static RewardRankingWealthFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        RewardRankingWealthFragment fragment = new RewardRankingWealthFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.app_fragment_reward_ranking_wealth, container, false);
//
//
//        return view;
//    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        setListener();

    }

    @Override
    protected int providelayoutId() {
        return R.layout.app_fragment_reward_ranking_wealth;
    }

    @Override
    protected void initData() {
        getData();
    }

    @Override
    protected boolean setIsRealTimeRefresh() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    void setListener() {
        zoomScrollRewardRanking.setOnScrollListener(new ZoomInScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > 200) {
                    ((RewardRankingActivity) getActivity()).hideRankTab();
                } else {
                    ((RewardRankingActivity) getActivity()).showRankTab();
                }
                if (!isLoading && hasMore && rewardRankRecyclerAdapter != null) {
                    LogUtil.d("加载下一页");
                    page++;
                    getWealthRanking();
                }
            }

            @Override
            public void onZoom(float zoomValue) {
                viewFragmentRewardRankingWealthLight.setRotation(zoomValue);
            }
        });
    }

    void getData() {
        Bundle args = getArguments();
        if (args != null) {
            type = args.getString("type");
        }
        if (!TextUtils.isEmpty(type)) {
            getWealthRanking();
        }
    }

    void getWealthRanking() {
        isLoading = true;
        HttpHelper.getInstance().getRewardWealthRankingList(type, page, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                isLoading = false;
                RankBerewardData rankBerewardData = GsonUtil.GsonToBean(data, RankBerewardData.class);
                List<RankBerewardData.DataBean> rankRewardList = rankBerewardData.getData();
                if(page == 0) {
                    initTop3View(rankRewardList);
                    if (rankRewardList.size() > 3) {
                        initRankingRecyclerView(rankRewardList);
                    }
                } else {
                    if(rankRewardList != null && rankRewardList.size() > 0) {
                        int temp = rankList.size();
                        rankList.addAll(rankRewardList);
                        rewardRankRecyclerAdapter.notifyItemRangeInserted(temp,rankRewardList.size());
                    } else {
                        hasMore = false;
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                isLoading = false;
                if(page != 0) {
                    hasMore = false;
                } else {
                    initTop3View(new ArrayList<RankBerewardData.DataBean>());
                }
            }
        });
    }

    void initTop3View(List<RankBerewardData.DataBean> rankRewardList) {
        groupRewardRankingTopOne.setVisibility(View.GONE);
        groupRewardRankingTopTwo.setVisibility(View.GONE);
        groupRewardRankingTopThree.setVisibility(View.GONE);
        groupRewardRankingTopOneInfo.setVisibility(View.GONE);
        groupRewardRankingTopTwoInfo.setVisibility(View.GONE);
        groupRewardRankingTopThreeInfo.setVisibility(View.GONE);

        if (rankRewardList.size() >= 1) {
            groupRewardRankingTopOne.setVisibility(View.VISIBLE);
            RankBerewardData.DataBean rankBean = rankRewardList.get(0);
            GlideImgManager.glideLoader(getActivity(), rankBean.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivFragmentRewardRankingWealthTopOne, 0);
            tvFragmentRewardRankingWealthTopOne.setText(rankBean.getNickname());
            tvFragmentRewardRankingWealthTopOneSendCount.setText("送出" + rankBean.getAllamount());
            tvFragmentRewardRankingWealthTopOneRichCount.setText(rankBean.getWealth_val());
            tvFragmentRewardRankingWealthTopOneAge.setText(rankBean.getAge());
            if (rankBean.getRewardeduserinfo() != null && !TextUtil.isEmpty(rankBean.getRewardeduserinfo().getHead_pic())) {
                groupRewardRankingTopOneInfo.setVisibility(View.VISIBLE);
                GlideImgManager.glideLoader(getActivity(), rankBean.getRewardeduserinfo().getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivFragmentRewardRankingWealthTopOneFavour, 0);
            } else {
                llFragmentRewardRankingWealthTopOneInfo.setVisibility(View.VISIBLE);
            }
            suitRole(rankBean, tvFragmentRewardRankingWealthTopOneSex);
            suitSex(rankBean, llFragmentRewardRankingWealthTopOneSex, ivFragmentRewardRankingWealthTopOneSex);
        }
        if (rankRewardList.size() >= 2) {
            groupRewardRankingTopTwo.setVisibility(View.VISIBLE);
            RankBerewardData.DataBean rankBean = rankRewardList.get(1);
            GlideImgManager.glideLoader(getActivity(), rankBean.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivFragmentRewardRankingWealthTopTwo, 0);
            tvFragmentRewardRankingWealthTopTwo.setText(rankBean.getNickname());
            tvFragmentRewardRankingWealthTopTwoSendCount.setText("送出" + rankBean.getAllamount());
            tvFragmentRewardRankingWealthTopTwoRichCount.setText(rankBean.getWealth_val());
            tvFragmentRewardRankingWealthTopTwoAge.setText(rankBean.getAge());
            if (rankBean.getRewardeduserinfo() != null && !TextUtil.isEmpty(rankBean.getRewardeduserinfo().getHead_pic())) {
                groupRewardRankingTopTwoInfo.setVisibility(View.VISIBLE);
                GlideImgManager.glideLoader(getActivity(), rankBean.getRewardeduserinfo().getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivFragmentRewardRankingWealthTopTwoFavour, 0);
            } else {
                llFragmentRewardRankingWealthTopTwoInfo.setVisibility(View.VISIBLE);
            }
            suitRole(rankBean, tvFragmentRewardRankingWealthTopTwoSex);
            suitSex(rankBean, llFragmentRewardRankingWealthTopTwoSex, ivFragmentRewardRankingWealthTopTwoSex);
        }
        if (rankRewardList.size() >= 3) {
            groupRewardRankingTopThree.setVisibility(View.VISIBLE);
            RankBerewardData.DataBean rankBean = rankRewardList.get(2);
            GlideImgManager.glideLoader(getActivity(), rankBean.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivFragmentRewardRankingWealthTopThree, 0);
            tvFragmentRewardRankingWealthTopThree.setText(rankBean.getNickname());
            tvFragmentRewardRankingWealthTopThreeSendCount.setText("送出" + rankBean.getAllamount());
            tvFragmentRewardRankingWealthTopThreeRichCount.setText(rankBean.getWealth_val());
            tvFragmentRewardRankingWealthTopThreeAge.setText(rankBean.getAge());
            if (rankBean.getRewardeduserinfo() != null && !TextUtil.isEmpty(rankBean.getRewardeduserinfo().getHead_pic())) {
                groupRewardRankingTopThreeInfo.setVisibility(View.VISIBLE);
                GlideImgManager.glideLoader(getActivity(), rankBean.getRewardeduserinfo().getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivFragmentRewardRankingWealthTopThreeFavour, 0);
            } else {
                llFragmentRewardRankingWealthTopThreeInfo.setVisibility(View.VISIBLE);
            }
            suitRole(rankBean, tvFragmentRewardRankingWealthTopThreeSex);
            suitSex(rankBean, llFragmentRewardRankingWealthTopThreeSex, ivFragmentRewardRankingWealthTopThreeSex);
        }
        initListener(rankRewardList);
    }

    void initRankingRecyclerView(List<RankBerewardData.DataBean> rankRewardList) {
        rankList = rankRewardList.subList(3, rankRewardList.size());
        rewardRankRecyclerAdapter = new RewardRankRecyclerAdapter(getActivity(), rankList, 1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvFragmentRewardRanking.setAdapter(rewardRankRecyclerAdapter);
        rvFragmentRewardRanking.setLayoutManager(linearLayoutManager);
        rvFragmentRewardRanking.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });
        //解决数据加载不完的问题
        rvFragmentRewardRanking.setNestedScrollingEnabled(false);
        rvFragmentRewardRanking.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        rvFragmentRewardRanking.setFocusable(false);
    }

    void suitRole(RankBerewardData.DataBean data, TextView tvRole) {
        if (data.getRole().equals("S")) {
            tvRole.setBackgroundResource(R.drawable.item_sex_nan_bg);
            tvRole.setText("斯");
        } else if (data.getRole().equals("M")) {
            tvRole.setBackgroundResource(R.drawable.item_sex_nv_bg);
            tvRole.setText("慕");
        } else if (data.getRole().equals("SM")) {
            tvRole.setBackgroundResource(R.drawable.item_sex_san_bg);
            tvRole.setText("双");
        } else if (data.getRole().equals("~")) {
            tvRole.setBackgroundResource(R.drawable.item_sex_lang_bg);
            tvRole.setText("~");
        }
    }

    void suitSex(RankBerewardData.DataBean data, LinearLayout llSex, ImageView ivSex) {
        if (data.getSex().equals("1")) {
            llSex.setBackgroundResource(R.drawable.item_sex_nan_bg);
            ivSex.setImageResource(R.mipmap.nan);
        } else if (data.getSex().equals("2")) {
            llSex.setBackgroundResource(R.drawable.item_sex_nv_bg);
            ivSex.setImageResource(R.mipmap.nv);
        } else if (data.getSex().equals("3")) {
            llSex.setBackgroundResource(R.drawable.item_sex_san_bg);
            ivSex.setImageResource(R.mipmap.san);
        }
    }

    void initListener(final List<RankBerewardData.DataBean> rankRewardList) {
        ivFragmentRewardRankingWealthTopOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUserInfoActivity(rankRewardList.get(0).getUid());
            }
        });
        ivFragmentRewardRankingWealthTopTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUserInfoActivity(rankRewardList.get(1).getUid());
            }
        });
        ivFragmentRewardRankingWealthTopThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUserInfoActivity(rankRewardList.get(2).getUid());
            }
        });
        llFragmentRewardRankingWealthTopOneFavour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUserInfoActivity(rankRewardList.get(0).getRewardeduserinfo().getFuid());
            }
        });
        llFragmentRewardRankingWealthTopTwoFavour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUserInfoActivity(rankRewardList.get(1).getRewardeduserinfo().getFuid());
            }
        });
        llFragmentRewardRankingWealthTopThreeFavour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUserInfoActivity(rankRewardList.get(2).getRewardeduserinfo().getFuid());
            }
        });
    }

    void gotoUserInfoActivity(String uid) {
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        intent.putExtra("uid",uid);
        startActivity(intent);
    }
}
