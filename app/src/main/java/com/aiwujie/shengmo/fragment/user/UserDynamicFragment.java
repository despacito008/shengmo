package com.aiwujie.shengmo.fragment.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.MyOrOtherTopicActivity;
import com.aiwujie.shengmo.activity.VipCenterActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.DynamicRecyclerAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicAndTopicCountData;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.bean.UserInfoBean;
import com.aiwujie.shengmo.eventbus.UserDynamicRefreshBean;
import com.aiwujie.shengmo.fragment.dynamicfragment.FragmentDynamicNew;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.normallist.UserJoinTopicActivity;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.aiwujie.shengmo.customview.CustomViewPage.SCROLL_STATE_IDLE;

public class UserDynamicFragment extends Fragment {
    @BindView(R.id.rv_user_info_dynamic)
    RecyclerView rvUserInfoDynamic;
    Unbinder unbinder;
    int page = 0;
    DynamicRecyclerAdapter dynamicRecyclerAdapter;
    List<DynamicListData.DataBean> dynamicList = new ArrayList<>();
    boolean hasMore = true;
    boolean isLoading = false;
    @BindView(R.id.tv_user_info_dynamic_permission)
    TextView tvUserInfoDynamicPermission;
    @BindView(R.id.tv_user_info_dynamic_num)
    TextView tvUserInfoDynamicNum;
    @BindView(R.id.tv_user_info_dynamic_title)
    TextView tvUserInfoDynamicTitle;
    @BindView(R.id.tv_user_info_dynamic_info)
    TextView tvUserInfoDynamicInfo;
    @BindView(R.id.ll_user_info_dynamic)
    NestedScrollView llUserInfoDynamic;
    @BindView(R.id.tv_user_info_add_friend)
    TextView tvUserInfoAddFriend;
    @BindView(R.id.tv_user_info_buy_vip)
    TextView tvUserInfoBuyVip;
    @BindView(R.id.cl_user_info_dynamic_btn)
    ConstraintLayout clUserInfoDynamicBtn;
    TextView tvUserDynamicDynamic;
    TextView tvUserDynamicRecommend;
    TextView tvUserDynamicComment;
    TextView tvUserDynamicThumbUp;
    TextView tvUserDynamicPush;
    LinearLayout llFragmentUserDynamicNum;
    TextView tvFragmentUserDynamicJoinTopic;
    LinearLayout llFragmentUserDynamicJoinTopic;
    LinearLayout llFragmentUserDynamic;
    @BindView(R.id.iv_user_info_dynamic_empty)
    ImageView ivUserInfoDynamicEmpty;
    @BindView(R.id.iv_user_dynamic_top)
    ImageView ivUserDynamicTop;
    private String uid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info_dynamic, container, false);
        unbinder = ButterKnife.bind(this, view);
        uid = getActivity().getIntent().getStringExtra("uid");
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // setListener();
    }

    private void getDynamicList() {
        isLoading = true;
        //String uid = getActivity().getIntent().getStringExtra("uid");
        String lastid = "";
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
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
                LogUtil.d(response);
                isLoading = false;
                DynamicListData listData = GsonUtil.GsonToBean(response, DynamicListData.class);
                List<DynamicListData.DataBean> data = listData.getData();
                int retcode = listData.getRetcode();
                if (retcode == 4001) {
                    hasMore = false;
                }
                if (rvUserInfoDynamic == null) {
                    return;
                }
                if (page == 0) {
                    dynamicList.clear();
                    dynamicList.addAll(data);
                    dynamicRecyclerAdapter = new DynamicRecyclerAdapter(getActivity(),UserDynamicFragment.this, dynamicList, retcode, "3");
                    HeaderViewAdapter headerViewAdapter = new HeaderViewAdapter(dynamicRecyclerAdapter);
                    headerViewAdapter.addHeaderView(getHeaderView());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rvUserInfoDynamic.setAdapter(headerViewAdapter);
                    rvUserInfoDynamic.setLayoutManager(layoutManager);
                } else {
                    int temp = dynamicList.size();
                    dynamicList.addAll(data);
                    dynamicRecyclerAdapter.notifyItemRangeInserted(temp, data.size());
                }

                rvUserInfoDynamic.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) {
                            if (hasMore && !isLoading) {
                                LogUtil.d("加载下一页");
                                page++;
                                getDynamicList();
                            }
                        }
                        if (Math.abs(dy) > 20) {
                            if (dy > 0) {
                                showOrHideTopArrow(false);
                                ((UserInfoActivity)getActivity()).showOrHideTopBottomBar(false);
                            } else {
                                ((UserInfoActivity)getActivity()).showOrHideTopBottomBar(true);
                                if (rvUserInfoDynamic.computeVerticalScrollOffset() > 500) {
                                    showOrHideTopArrow(true);
                                }
                            }
                        }
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (rvUserInfoDynamic == null) {
                            return;
                        }
                        if (newState == SCROLL_STATE_IDLE) {
                            RecyclerView.LayoutManager layoutManager = rvUserInfoDynamic.getLayoutManager();
                            if (layoutManager instanceof LinearLayoutManager) {
                                LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                                int mFirstVisiblePosition = linearManager.findFirstVisibleItemPosition();
                                int mLastVisiblePosition = linearManager.findLastVisibleItemPosition();
                                for (int i = mLastVisiblePosition; i >= mFirstVisiblePosition; i--) {
                                    int trueIndex = i - 1;//去掉头布局的index
                                    if (trueIndex >= 0 && dynamicList.get(trueIndex).getPlayUrl().length() > 0) {
                                        dynamicRecyclerAdapter.tryToPlayVideo(rvUserInfoDynamic, i, dynamicList.get(trueIndex).getPlayUrl());
                                        return;
                                    }
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                isLoading = false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void showData(final UserInfoBean.DataBean userInfo) {
        if (clUserInfoDynamicBtn == null) {
            return;
        }
        String vip = (String) SharedPreferencesUtils.getParam(getActivity(), "vip", "0");
        String svip = (String) SharedPreferencesUtils.getParam(getActivity(), "svip", "0");
        if ("0".equals(userInfo.getDynamic_num())) {
            llUserInfoDynamic.setVisibility(View.VISIBLE);
            tvUserInfoDynamicTitle.setText("暂无动态");
            tvUserInfoDynamicInfo.setText("");
            tvUserInfoDynamicNum.setText(userInfo.getComment_num());
            clUserInfoDynamicBtn.setVisibility(View.GONE);
            return;
        }
        clUserInfoDynamicBtn.setVisibility(View.VISIBLE);
        if ("0".equals(userInfo.getDynamic_rule())) { //公开
            llUserInfoDynamic.setVisibility(View.GONE);
            tvUserInfoDynamicTitle.setText("(所有人可见)");
            tvUserInfoDynamicNum.setText(userInfo.getComment_num());
            getDynamicList();
            // getDynamicAndTopicCount();
        } else { //仅好友 会员可见
            tvUserInfoDynamicTitle.setText("(好友/会员可见)");
            tvUserInfoDynamicNum.setText(userInfo.getComment_num());
            if (MyApp.uid.equals(userInfo.getUid()) || "1".equals(vip) || "1".equals(svip) || 3 == userInfo.getFollow_state()) {
                llUserInfoDynamic.setVisibility(View.GONE);
                getDynamicList();
                // getDynamicAndTopicCount();
            } else {
                llUserInfoDynamic.setVisibility(View.VISIBLE);
                tvUserInfoDynamicTitle.setText("动态不可见");
                //ivUserInfoDynamicEmpty.setVisibility(View.VISIBLE);
                SpannableStringBuilder builder = new SpannableStringBuilder("该用户已将发布的动态设为好友/会员可见");
                ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#db57f3"));
                builder.setSpan(purSpan, 12, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvUserInfoDynamicInfo.setText(builder);
                int followState = userInfo.getFollow_state();
                if (followState == 0) {
                    tvUserInfoAddFriend.setText("加好友");
                } else if (followState == 1) {
                    tvUserInfoAddFriend.setText("已关注");
                } else if (followState == 2) {
                    tvUserInfoAddFriend.setText("关注");
                } else if (followState == 4) {
                    tvUserInfoAddFriend.setText("被关注");
                }
            }
            tvUserInfoAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int followState = userInfo.getFollow_state();
                    if (followState == 0) {
                        ((UserInfoActivity) getActivity()).follow();
                    } else if (followState == 1) {
                        ((UserInfoActivity) getActivity()).overfollow();
                    } else if (followState == 2) {
                        ((UserInfoActivity) getActivity()).follow();
                    } else if (followState == 4) {
                        ((UserInfoActivity) getActivity()).follow();
                    }
                }
            });

            tvUserInfoBuyVip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String headpic = (String) SharedPreferencesUtils.getParam(getActivity(), "headurl", "");
                    Intent intent = new Intent(getActivity(), VipCenterActivity.class);
                    intent.putExtra("uid", MyApp.uid);
                    intent.putExtra("headpic", headpic);
                    startActivity(intent);
                }
            });
        }
    }

    private void getDynamicAndTopicCount() {
        //String uid = getActivity().getIntent().getStringExtra("uid");
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetDynamicAndTopicCount, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                if (llFragmentUserDynamicJoinTopic == null) {
                    return;
                }
                try {
                    DynamicAndTopicCountData data = new Gson().fromJson(response, DynamicAndTopicCountData.class);
                    if (data != null) {
                        if (data.getData().getJ_topic().size() != 0) {
                            llFragmentUserDynamicJoinTopic.setVisibility(View.VISIBLE);
                            SpannableStringBuilder builders = new SpannableStringBuilder();
                            for (int i = 0; i < data.getData().getJ_topic().size(); i++) {
                                ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor("#db57f3"));
                                SpannableStringBuilder builder = new SpannableStringBuilder("#" + data.getData().getJ_topic().get(i).getTitle() + "# ");
                                builder.setSpan(purSpan, 0, data.getData().getJ_topic().get(i).getTitle().length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                builders.append(builder);
                            }
                            tvFragmentUserDynamicJoinTopic.setText(builders);
                        } else {
                            llFragmentUserDynamicJoinTopic.setVisibility(View.GONE);
                        }
                    }
                    tvUserDynamicDynamic.setText(String.valueOf(data.getData().getDynamiccount().getDynamicnum()));
                    tvUserDynamicRecommend.setText(String.valueOf(data.getData().getDynamiccount().getRecommend()));
                    tvUserDynamicComment.setText(String.valueOf(data.getData().getDynamiccount().getComnum()));
                    tvUserDynamicThumbUp.setText(String.valueOf(data.getData().getDynamiccount().getLaudnum()));
                    tvUserDynamicPush.setText(String.valueOf(data.getData().getDynamiccount().getTopnum()));
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    void setListener() {
        llFragmentUserDynamicJoinTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserJoinTopicActivity.Companion.start(getActivity(),uid);
            }
        });

        ivUserDynamicTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvUserInfoDynamic.scrollToPosition(0);
            }
        });
    }

    public void refreshData() {
        page = 0;
        getDynamicList();
    }

    public View getHeaderView() {
        View headerView = View.inflate(getActivity(), R.layout.layout_header_user_dynamic, null);
        llFragmentUserDynamicJoinTopic = headerView.findViewById(R.id.ll_fragment_user_dynamic_join_topic);
        tvFragmentUserDynamicJoinTopic = headerView.findViewById(R.id.tv_fragment_user_dynamic_join_topic);
        tvUserDynamicDynamic = headerView.findViewById(R.id.tv_user_dynamic_dynamic);
        tvUserDynamicRecommend = headerView.findViewById(R.id.tv_user_dynamic_recommend);
        tvUserDynamicComment = headerView.findViewById(R.id.tv_user_dynamic_comment);
        tvUserDynamicThumbUp = headerView.findViewById(R.id.tv_user_dynamic_thumb_up);
        tvUserDynamicPush = headerView.findViewById(R.id.tv_user_dynamic_push);
        setListener();
        getDynamicAndTopicCount();
        return headerView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(UserDynamicRefreshBean event) {
        refreshData();
    }

    void showOrHideTopArrow(boolean isShow) {
        if (isShow) {
            if (ivUserDynamicTop.getVisibility() == View.GONE) {
                ivUserDynamicTop.setVisibility(View.VISIBLE);
                ivUserDynamicTop.setAnimation(AnimationUtil.moveToViewLocation());
            }
        } else {
            if (ivUserDynamicTop.getVisibility() == View.VISIBLE) {
                ivUserDynamicTop.setVisibility(View.GONE);
                ivUserDynamicTop.setAnimation(AnimationUtil.moveToViewBottom());
            }
        }
    }

}
