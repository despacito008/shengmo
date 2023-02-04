package com.aiwujie.shengmo.fragment.rank;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.ranking.ActiveAndPopularityRankingActivity;
import com.aiwujie.shengmo.activity.ranking.ActiveRankingActivity;
import com.aiwujie.shengmo.activity.ranking.PopularityRankingActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.PopularityRankRecyclerAdapter;
import com.aiwujie.shengmo.base.CommonLazyLoadFragment;
import com.aiwujie.shengmo.bean.RankPopularityData;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.GlideImgManager;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.view.ZoomInScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ActiveRankingFragment extends CommonLazyLoadFragment {


    @BindView(R.id.view_fragment_reward_ranking_wealth_light)
    View viewFragmentRewardRankingWealthLight;
    @BindView(R.id.view_fragment_reward_ranking_wealth_top_one_bg)
    View viewFragmentRewardRankingWealthTopOneBg;
    @BindView(R.id.view_fragment_reward_ranking_wealth_top_two_bg)
    View viewFragmentRewardRankingWealthTopTwoBg;
    @BindView(R.id.view_fragment_reward_ranking_wealth_top_three_bg)
    View viewFragmentRewardRankingWealthTopThreeBg;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_one)
    ImageView ivFragmentRewardRankingWealthTopOne;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_two)
    ImageView ivFragmentRewardRankingWealthTopTwo;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_three)
    ImageView ivFragmentRewardRankingWealthTopThree;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_one_crown)
    ImageView ivFragmentRewardRankingWealthTopOneCrown;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_two_crown)
    ImageView ivFragmentRewardRankingWealthTopTwoCrown;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_three_crown)
    ImageView ivFragmentRewardRankingWealthTopThreeCrown;
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
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_one_sex)
    LinearLayout llFragmentRewardRankingWealthTopOneSex;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_one_sex)
    TextView tvFragmentRewardRankingWealthTopOneSex;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_one_richCount)
    TextView tvFragmentRewardRankingWealthTopOneRichCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_one_richCount)
    LinearLayout llFragmentRewardRankingWealthTopOneRichCount;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_one_charmCount)
    TextView tvFragmentRewardRankingWealthTopOneCharmCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_one_charmCount)
    LinearLayout llFragmentRewardRankingWealthTopOneCharmCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_one_info)
    LinearLayout llFragmentRewardRankingWealthTopOneInfo;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_two_sex)
    ImageView ivFragmentRewardRankingWealthTopTwoSex;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_two_age)
    TextView tvFragmentRewardRankingWealthTopTwoAge;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_two_sex)
    LinearLayout llFragmentRewardRankingWealthTopTwoSex;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_two_sex)
    TextView tvFragmentRewardRankingWealthTopTwoSex;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_two_richCount)
    TextView tvFragmentRewardRankingWealthTopTwoRichCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_two_richCount)
    LinearLayout llFragmentRewardRankingWealthTopTwoRichCount;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_two_charmCount)
    TextView tvFragmentRewardRankingWealthTopTwoCharmCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_two_charmCount)
    LinearLayout llFragmentRewardRankingWealthTopTwoCharmCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_two_info)
    LinearLayout llFragmentRewardRankingWealthTopTwoInfo;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_three_richCount)
    TextView tvFragmentRewardRankingWealthTopThreeRichCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_three_richCount)
    LinearLayout llFragmentRewardRankingWealthTopThreeRichCount;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_three_charmCount)
    TextView tvFragmentRewardRankingWealthTopThreeCharmCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_three_charmCount)
    LinearLayout llFragmentRewardRankingWealthTopThreeCharmCount;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_three_info)
    LinearLayout llFragmentRewardRankingWealthTopThreeInfo;
    @BindView(R.id.group_reward_ranking_top_one)
    Group groupRewardRankingTopOne;
    @BindView(R.id.group_reward_ranking_top_two)
    Group groupRewardRankingTopTwo;
    @BindView(R.id.group_reward_ranking_top_three)
    Group groupRewardRankingTopThree;
    @BindView(R.id.cl_fragment_reward_ranking)
    ConstraintLayout clFragmentRewardRanking;
    @BindView(R.id.rv_fragment_reward_ranking)
    RecyclerView rvFragmentRewardRanking;
    @BindView(R.id.zoom_scroll_reward_ranking)
    ZoomInScrollView zoomScrollRewardRanking;
    @BindView(R.id.iv_fragment_reward_ranking_wealth_top_three_sex)
    ImageView ivFragmentRewardRankingWealthTopThreeSex;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_three_age)
    TextView tvFragmentRewardRankingWealthTopThreeAge;
    @BindView(R.id.ll_fragment_reward_ranking_wealth_top_three_sex)
    LinearLayout llFragmentRewardRankingWealthTopThreeSex;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_three_sex)
    TextView tvFragmentRewardRankingWealthTopThreeSex;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_one_num)
    TextView tvFragmentRewardRankingWealthTopOneNum;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_two_num)
    TextView tvFragmentRewardRankingWealthTopTwoNum;
    @BindView(R.id.tv_fragment_reward_ranking_wealth_top_three_num)
    TextView tvFragmentRewardRankingWealthTopThreeNum;
    private Unbinder unbinder;
    private String type;
    private String popType;
    private int page;
    private List<RankPopularityData.DataBean> rankList;
    private PopularityRankRecyclerAdapter popularityRankRecyclerAdapter;
    private boolean hasMore = true;
    private boolean isLoading = false;


    public static ActiveRankingFragment newInstance(String popType, String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("popType", popType);
        ActiveRankingFragment fragment = new ActiveRankingFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.app_fragment_popularity_ranking, container, false);
//
//        //getData();
//        return view;
//    }

    @Override
    protected void initView(View mRootView) {
        unbinder = ButterKnife.bind(this, mRootView);
        setListener();
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
    protected int providelayoutId() {
        return R.layout.app_fragment_active_ranking;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null) {
            unbinder.unbind();
        }
    }

    void setListener() {
        zoomScrollRewardRanking.setOnScrollListener(new ZoomInScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > 200) {
                    ((ActiveAndPopularityRankingActivity) getActivity()).hideRankTab();
                } else {
                    ((ActiveAndPopularityRankingActivity) getActivity()).showRankTab();
                }

                if (scrollY == (zoomScrollRewardRanking.getChildAt(0).getMeasuredHeight() - zoomScrollRewardRanking.getMeasuredHeight())) {
                    // 底部
                    //LogUtil.d("到底了");
                    if (!isLoading && hasMore && popularityRankRecyclerAdapter != null) {
                        LogUtil.d("加载下一页");
                        page++;
                        getCharmRanking();
                    }
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
            popType = args.getString("popType");
        }
        if (!TextUtils.isEmpty(type)) {
            getCharmRanking();
        }
    }

    void getCharmRanking() {
        isLoading = true;
        HttpHelper.getInstance().getActiveRankingList(popType, type, page, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                isLoading = false;
                RankPopularityData rankPopularityData = GsonUtil.GsonToBean(data, RankPopularityData.class);
                List<RankPopularityData.DataBean> rankPopularityList = rankPopularityData.getData();

                if (page == 0) {
                    initTop3View(rankPopularityList);
                    if (rankPopularityList.size() > 3) {
                        initRankingRecyclerView(rankPopularityList);
                    }
                } else {
                    if (rankPopularityList != null && rankPopularityList.size() > 0) {
                        int temp = rankList.size();
                        rankList.addAll(rankPopularityList);
                        //rewardRankRecyclerAdapter.notifyDataSetChanged();
                        ((SimpleItemAnimator) rvFragmentRewardRanking.getItemAnimator()).setSupportsChangeAnimations(false);
                        popularityRankRecyclerAdapter.notifyItemRangeChanged(temp - 1, rankList.size());
                    } else {
                        hasMore = false;
                    }
                }
            }

            @Override
            public void onFail(String msg) {
                isLoading = false;
                if (page != 0) {
                    hasMore = false;
                } else {
                    initTop3View(new ArrayList<RankPopularityData.DataBean>());
                }
            }
        });
    }

    void initTop3View(List<RankPopularityData.DataBean> rankPopularityList) {
        if(groupRewardRankingTopOne == null) {
            return;
        }
        groupRewardRankingTopOne.setVisibility(View.GONE);
        groupRewardRankingTopTwo.setVisibility(View.GONE);
        groupRewardRankingTopThree.setVisibility(View.GONE);
        if (rankPopularityList.size() >= 1) {
            groupRewardRankingTopOne.setVisibility(View.VISIBLE);
            RankPopularityData.DataBean rankBean = rankPopularityList.get(0);
            GlideImgManager.glideLoader(getActivity(), rankBean.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivFragmentRewardRankingWealthTopOne, 0);
            tvFragmentRewardRankingWealthTopOne.setText(rankBean.getNickname());
            tvFragmentRewardRankingWealthTopOneRichCount.setText(rankBean.getWealth_val());
            tvFragmentRewardRankingWealthTopOneCharmCount.setText(rankBean.getCharm_val());
            tvFragmentRewardRankingWealthTopOneAge.setText(rankBean.getAge());
            tvFragmentRewardRankingWealthTopOneNum.setText(rankBean.getAllnum());
            suitRole(rankBean, tvFragmentRewardRankingWealthTopOneSex);
            suitSex(rankBean, llFragmentRewardRankingWealthTopOneSex, ivFragmentRewardRankingWealthTopOneSex);
        }
        if (rankPopularityList.size() >= 2) {
            groupRewardRankingTopTwo.setVisibility(View.VISIBLE);
            RankPopularityData.DataBean rankBean = rankPopularityList.get(1);
            GlideImgManager.glideLoader(getActivity(), rankBean.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivFragmentRewardRankingWealthTopTwo, 0);
            tvFragmentRewardRankingWealthTopTwo.setText(rankBean.getNickname());
            tvFragmentRewardRankingWealthTopTwoRichCount.setText(rankBean.getWealth_val());
            tvFragmentRewardRankingWealthTopTwoCharmCount.setText(rankBean.getCharm_val());
            tvFragmentRewardRankingWealthTopTwoAge.setText(rankBean.getAge());
            tvFragmentRewardRankingWealthTopTwoNum.setText(rankBean.getAllnum());
            suitRole(rankBean, tvFragmentRewardRankingWealthTopTwoSex);
            suitSex(rankBean, llFragmentRewardRankingWealthTopTwoSex, ivFragmentRewardRankingWealthTopTwoSex);
        }
        if (rankPopularityList.size() >= 3) {
            groupRewardRankingTopThree.setVisibility(View.VISIBLE);
            RankPopularityData.DataBean rankBean = rankPopularityList.get(2);
            GlideImgManager.glideLoader(getActivity(), rankBean.getHead_pic(), R.mipmap.morentouxiang, R.mipmap.morentouxiang, ivFragmentRewardRankingWealthTopThree, 0);
            tvFragmentRewardRankingWealthTopThree.setText(rankBean.getNickname());
            tvFragmentRewardRankingWealthTopThreeRichCount.setText(rankBean.getWealth_val());
            tvFragmentRewardRankingWealthTopThreeCharmCount.setText(rankBean.getCharm_val());
            tvFragmentRewardRankingWealthTopThreeAge.setText(rankBean.getAge());
            tvFragmentRewardRankingWealthTopThreeNum.setText(rankBean.getAllnum());
            suitRole(rankBean, tvFragmentRewardRankingWealthTopThreeSex);
            suitSex(rankBean, llFragmentRewardRankingWealthTopThreeSex, ivFragmentRewardRankingWealthTopThreeSex);
        }
        initListener(rankPopularityList);
    }

    void initRankingRecyclerView(List<RankPopularityData.DataBean> rankRewardList) {
        if(rvFragmentRewardRanking == null) {
            return;
        }
        rankList = rankRewardList.subList(3, rankRewardList.size());
        popularityRankRecyclerAdapter = new PopularityRankRecyclerAdapter(getActivity(), rankList,2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvFragmentRewardRanking.setAdapter(popularityRankRecyclerAdapter);
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

    void suitRole(RankPopularityData.DataBean data, TextView tvRole) {
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

    void suitSex(RankPopularityData.DataBean data, LinearLayout llSex, ImageView ivSex) {
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

    void initListener(final List<RankPopularityData.DataBean> rankPopularityList) {

        ivFragmentRewardRankingWealthTopOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUserInfoActivity(rankPopularityList.get(0).getUid());
            }
        });
        ivFragmentRewardRankingWealthTopTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUserInfoActivity(rankPopularityList.get(1).getUid());
            }
        });
        ivFragmentRewardRankingWealthTopThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUserInfoActivity(rankPopularityList.get(2).getUid());
            }
        });

    }

    void gotoUserInfoActivity(String uid) {
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

}
