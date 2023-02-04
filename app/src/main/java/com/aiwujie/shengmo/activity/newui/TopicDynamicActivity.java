package com.aiwujie.shengmo.activity.newui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.BannerWebActivity;
import com.aiwujie.shengmo.activity.GroupInfoActivity;
import com.aiwujie.shengmo.activity.SendDynamicActivity;
import com.aiwujie.shengmo.activity.TopicDetailActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.TopicPushTopRecyclerAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.TopicDetailData;
import com.aiwujie.shengmo.bean.TopicTopListBean;
import com.aiwujie.shengmo.customview.VipDialog;
import com.aiwujie.shengmo.eventbus.TopicRefreshEvent;
import com.aiwujie.shengmo.eventbus.ViewIsVisibleEvent;
import com.aiwujie.shengmo.fragment.topic.TopicDynamicFragment;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.GroupDetailActivity;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.OkHttpRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.ImageLoader;
import com.aiwujie.shengmo.utils.OnSimpleItemViewListener;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.DisallowViewPager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tillusory.tiui.adapter.TiDesCategoryViewHolder;

public class TopicDynamicActivity extends AppCompatActivity {

    @BindView(R.id.iv_normal_title_back)
    ImageView ivNormalTitleBack;
    @BindView(R.id.tv_normal_title_title)
    TextView tvNormalTitleTitle;
    @BindView(R.id.iv_normal_title_more)
    ImageView ivNormalTitleMore;
    @BindView(R.id.iv_activity_topic_bg)
    ImageView ivActivityTopicBg;
    @BindView(R.id.iv_activity_topic_icon)
    ImageView ivActivityTopicIcon;
    @BindView(R.id.tv_activity_topic)
    TextView tvActivityTopic;
    @BindView(R.id.tab_layout_activity_topic)
    TabLayout tabLayoutActivityTopic;
    @BindView(R.id.app_bar_topic)
    AppBarLayout appBarTopic;
    @BindView(R.id.view_pager_activity_topic)
    DisallowViewPager viewPagerActivityTopic;
    @BindView(R.id.crl_activity_topic)
    CoordinatorLayout crlActivityTopic;
    @BindView(R.id.srl_activity_topic)
    SmartRefreshLayout srlActivityTopic;
    @BindView(R.id.tv_activity_topic_title)
    TextView tvActivityTopicTitle;
    @BindView(R.id.tv_activity_topic_visit)
    TextView tvActivityTopicVisit;
    @BindView(R.id.tv_activity_topic_dynamic)
    TextView tvActivityTopicDynamic;
    @BindView(R.id.tv_activity_topic_join)
    TextView tvActivityTopicJoin;
    @BindView(R.id.iv_activity_topic_follow)
    ImageView ivActivityTopicFollow;
    @BindView(R.id.iv_activity_topic_join)
    ImageView ivActivityTopicJoin;
    @BindView(R.id.ll_activity_topic_bottom)
    LinearLayout llActivityTopicBottom;
    @BindView(R.id.iv_activity_topic_go_top)
    ImageView ivActivityTopicGoTop;
    @BindView(R.id.rv_activity_topic_top)
    RecyclerView rvActivityTopicTop;
    @BindView(R.id.view_activity_topic_top)
    View viewActivityTopicTop;

    private String topicId;
    private String topictitle;
    private int topicState;
    private List<String> titles;
    private TopicDynamicFragment newTopicFragment;
    private TopicDynamicFragment hotTopicFragment;
    private boolean isFollowTopic;

    public static void start(Context context, String tid,String title) {
        Intent intent = new Intent(context,TopicDynamicActivity.class);
        intent.putExtra("tid",tid);
        intent.putExtra("topictitle",title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_topic);
        OkHttpRequestManager.getInstance().setTag(this.getLocalClassName());
        StatusBarUtil.showLightStatusBar(this);
        ButterKnife.bind(this);
        topicId = getIntent().getStringExtra("tid");
        topictitle = getIntent().getStringExtra("topictitle");
        topicState = getIntent().getIntExtra("topicState", -1);
        getTopicDetail();
        getTopicTopList();
        initViewPager();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpRequestManager.getInstance().cancelTag(this.getLocalClassName());
    }

    void initViewPager() {
        titles = Arrays.asList("最新", "最热");
        final List<Fragment> fragmentList = new ArrayList<>();
        newTopicFragment = TopicDynamicFragment.newInstance("1", topicId);
        hotTopicFragment = TopicDynamicFragment.newInstance("4", topicId);
        fragmentList.add(newTopicFragment);
        fragmentList.add(hotTopicFragment);
        FragmentStatePagerAdapter fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

        };
        viewPagerActivityTopic.setAdapter(fragmentStatePagerAdapter);
        tabLayoutActivityTopic.setupWithViewPager(viewPagerActivityTopic);
        for (int i = 0; i < tabLayoutActivityTopic.getTabCount(); i++) {
            tabLayoutActivityTopic.getTabAt(i).setCustomView(getTabView(i));
        }
        tabLayoutActivityTopic.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                View view = tabView.findViewById(R.id.tab_item_textview);
                View bottomView = tabView.findViewById(R.id.tab_item_bottom);
                if (null != view && view instanceof TextView) {
                    ((TextView) view).setTextSize(16);
                    ((TextView) view).setTextColor(ContextCompat.getColor(TopicDynamicActivity.this, R.color.titleBlack));
                    ((TextView) view).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    bottomView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                View view = tabView.findViewById(R.id.tab_item_textview);
                View bottomView = tabView.findViewById(R.id.tab_item_bottom);
                if (null != view && view instanceof TextView) {
                    ((TextView) view).setTextSize(14);
                    ((TextView) view).setTextColor(ContextCompat.getColor(TopicDynamicActivity.this, R.color.normalGray));
                    ((TextView) view).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    bottomView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPagerActivityTopic.setCurrentItem(0);
        View tabView = tabLayoutActivityTopic.getTabAt(0).getCustomView();
        View view = tabView.findViewById(R.id.tab_item_textview);
        View bottomView = tabView.findViewById(R.id.tab_item_bottom);
        if (null != view && view instanceof TextView) {
            ((TextView) view).setTextSize(16);
            ((TextView) view).setTextColor(ContextCompat.getColor(TopicDynamicActivity.this, R.color.titleBlack));
            ((TextView) view).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            bottomView.setVisibility(View.VISIBLE);
        }
    }

    void setListener() {
        tvNormalTitleTitle.setText("话题");
        ivNormalTitleMore.setImageResource(R.drawable.shuaixuan);
        ivNormalTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivNormalTitleMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOption();
            }
        });
        srlActivityTopic.setEnableLoadMore(false);
        srlActivityTopic.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (viewPagerActivityTopic.getCurrentItem() == 0) {
                    newTopicFragment.refreshData();
                } else {
                    hotTopicFragment.refreshData();
                }
                getTopicTopList();
                srlActivityTopic.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srlActivityTopic.finishRefresh();
                    }
                }, 1000);
            }
        });

        ivActivityTopicJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dynamicState == 0) {
                    ToastUtil.show(toastStr);
                } else if (dynamicState == 1) {
                    Intent intent = new Intent(TopicDynamicActivity.this, SendDynamicActivity.class);
                    intent.putExtra("topicid", topicId);
                    intent.putExtra("topictitle", topictitle);
                    intent.putExtra("topicState", topicState);
                    startActivity(intent);
                } else {
                    ToastUtil.show("未获取到权限，请稍后重试");
                }
            }
        });

        ivActivityTopicFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollowTopic) {
                    cancelFollowTopic();
                } else {
                    followTopic();
                }
            }
        });

        ivActivityTopicGoTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPagerActivityTopic.getCurrentItem() == 0) {
                    newTopicFragment.gotoTop();
                } else {
                    hotTopicFragment.gotoTop();
                }
            }
        });


        crlActivityTopic.setOnTouchListener(new View.OnTouchListener() {
            int mx, my;
            int lastx, lasty;

            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                //获取坐标点：
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                if (x > 600) {
                    return TopicDynamicActivity.super.onTouchEvent(ev);
                }
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int deletx = x - mx;
                        int delety = y - my;
                        if (Math.abs(deletx) > Math.abs(delety)) {
//                            if (onLeftFillingListener != null) {
//                                onLeftFillingListener.onLeftFilling();
//                            }
                            finish();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                //这里尤其重要，解决了拦截MOVE事件却没有拦截DOWN事件没有坐标的问题
                lastx = x;
                lasty = y;
                mx = x;
                my = y;
                return false;
            }
        });

        viewPagerActivityTopic.setOnLeftFillingListener(new DisallowViewPager.OnLeftFillingListener() {
            @Override
            public void onLeftFilling() {
                if (viewPagerActivityTopic.getCurrentItem() == 0) {
                    finish();
                }
            }
        });
    }

    void getTopicDetail() {
        HttpHelper.getInstance().getTopicDetail(topicId, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                TopicDetailData topicDetailData = GsonUtil.GsonToBean(data, TopicDetailData.class);
                if (topicDetailData == null) {
                    return;
                }
                TopicDetailData.DataBean topicDetail = topicDetailData.getData();
                String pic = topicDetail.getPic();
                if (!TopicDynamicActivity.this.isFinishing()) {
                    ImageLoader.loadRoundImage(TopicDynamicActivity.this, pic, ivActivityTopicIcon, -1);
                    ImageLoader.loadImage(TopicDynamicActivity.this, pic, ivActivityTopicBg);
                }
                tvActivityTopic.setText(topicDetail.getIntroduce());
                tvActivityTopicVisit.setText(topicDetail.getReadtimes() + " 来访");
                tvActivityTopicJoin.setText(topicDetail.getPartaketimes() + " 参与");
                tvActivityTopicDynamic.setText(topicDetail.getDynamicnum() + " 动态");
                tvActivityTopicTitle.setText("#" + topicDetail.getTitle() + "#");
                isFollowTopic = "1".equals(topicDetail.getIs_follow_topic());
                if (isFollowTopic) {
                    ivActivityTopicFollow.setImageResource(R.mipmap.quxiaoguanzhu);
                } else {
                    ivActivityTopicFollow.setImageResource(R.mipmap.guanzhuhuati);
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    void getTopicTopList() {
        HttpHelper.getInstance().getTopicTopList(topicId, new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(TopicDynamicActivity.this)) {
                    return;
                }
                TopicTopListBean topicTopListBean = GsonUtil.GsonToBean(data, TopicTopListBean.class);
                final List<TopicTopListBean.DataBean> topList = topicTopListBean.getData();
                if (topList == null || topList.size() == 0) {
                    rvActivityTopicTop.setVisibility(View.GONE);
                    viewActivityTopicTop.setVisibility(View.GONE);
                    return;
                } else {
                    rvActivityTopicTop.setVisibility(View.VISIBLE);
                    viewActivityTopicTop.setVisibility(View.VISIBLE);
                }
                TopicPushTopRecyclerAdapter topicAdapter = new TopicPushTopRecyclerAdapter(TopicDynamicActivity.this, topList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TopicDynamicActivity.this);
                rvActivityTopicTop.setLayoutManager(linearLayoutManager);
                rvActivityTopicTop.setAdapter(topicAdapter);
                topicAdapter.setOnSimpleItemViewListener(new OnSimpleItemViewListener() {
                    @Override
                    public void onItemListener(View view) {
                        int index = rvActivityTopicTop.getChildAdapterPosition(view);
                        String id = topList.get(index).getLink_id();
                        Intent intent;
                        switch (topList.get(index).getType()) {
                            case "1":
                                intent = new Intent(TopicDynamicActivity.this, BannerWebActivity.class);
                                intent.putExtra("path", topList.get(index).getUrl());
                                intent.putExtra("title", "圣魔");
                                startActivity(intent);
                                break;
                            case "2":
                                intent = new Intent(TopicDynamicActivity.this, NewDynamicDetailActivity.class);
                                intent.putExtra("did", id);
                                startActivity(intent);
                                break;
                            case "3":
                                intent = new Intent(TopicDynamicActivity.this, UserInfoActivity.class);
                                intent.putExtra("uid", id);
                                startActivity(intent);
                                break;
                            case "4":
//                                intent = new Intent(TopicDynamicActivity.this, GroupInfoActivity.class);
//                                intent.putExtra("groupId", id);
//                                startActivity(intent);
                                GroupDetailActivity.Companion.start(MyApp.getInstance(),
                                        id, 0,false);
                                break;
                            case "5":
                                intent = new Intent(TopicDynamicActivity.this, TopicDetailActivity.class);
                                intent.putExtra("tid", id);
                                intent.putExtra("topictitle", "topictitle");
                                intent.putExtra("topicState", "topictitle");
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                break;
                        }
                    }
                });
            }

            @Override
            public void onFail(int code, String msg) {

            }
        });
    }

    View getTabView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_tab_dynamic_detail, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_item_textview);
        textView.setText(titles.get(index));
        return view;
    }

    PopupWindow mPopWindow;
    private String dynamicSex;
    private String dynamicSexual;
    //vip  1.为vip 0.不是vip
    private String vipflag;
    private String sex = "";
    private String sexual = "";

    private void showOption() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_dynamic_pop, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setFocusable(true);
        backgroundAlpha(0.5f);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        mPopWindow.showAsDropDown(ivNormalTitleMore);
        mPopWindow.setAnimationStyle(R.style.AnimationPreview);
        TextView llNan = (TextView) contentView.findViewById(R.id.item_dynamic_pop_llNan);
        TextView llNv = (TextView) contentView.findViewById(R.id.item_dynamic_pop_llNv);
        TextView llNao = (TextView) contentView.findViewById(R.id.item_dynamic_pop_llCdts);
        TextView llAll = (TextView) contentView.findViewById(R.id.item_dynamic_pop_llAll);
        TextView tvSexual = (TextView) contentView.findViewById(R.id.item_dynamic_pop_tvSexual);
        PercentLinearLayout llSexual = (PercentLinearLayout) contentView.findViewById(R.id.item_dynamic_pop_llSexual);
        sex = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "dynamicSex", "");
        sexual = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "dynamicSexual", "");
        dynamicSexual = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "dynamicSxSexual", "");
        vipflag = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
        if (sex.equals("") && sexual.equals("")) {
            llAll.setTextColor(Color.parseColor("#b73acb"));
        }
        if (sex.equals("1") && sexual.equals("")) {
            llNan.setTextColor(Color.parseColor("#b73acb"));
        }
        if (sex.equals("2") && sexual.equals("")) {
            llNv.setTextColor(Color.parseColor("#b73acb"));
        }
        if (sex.equals("3") && sexual.equals("")) {
            llNao.setTextColor(Color.parseColor("#b73acb"));
        }
        if (dynamicSexual.equals("1")) {
            tvSexual.setTextColor(Color.parseColor("#b73acb"));
        }
        llNan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vipflag.equals("1")) {
                    dynamicSex = "1";
                    dynamicSexual = "";
                    dynamicSx(dynamicSex, dynamicSexual, true);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSxSexual", "0");
                    //设置沙漏为紫色
//                    mFragmentDynamicSx.setImageResource(R.mipmap.shaixuan_zise);
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
            }
        });
        llNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vipflag.equals("1")) {
                    dynamicSex = "2";
                    dynamicSexual = "";
                    dynamicSx(dynamicSex, dynamicSexual, true);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSxSexual", "0");
                    //设置沙漏为紫色
//                    mFragmentDynamicSx.setImageResource(R.mipmap.shaixuan_zise);
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
            }
        });
        llAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vipflag.equals("1")) {
                    dynamicSex = "";
                    dynamicSexual = "";
                    dynamicSx(dynamicSex, dynamicSexual, false);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSxSexual", "0");
                    ivNormalTitleMore.setImageResource(R.mipmap.popshaixuan);
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
            }
        });
        llNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vipflag.equals("1")) {
                    dynamicSex = "3";
                    dynamicSexual = "";
                    dynamicSx(dynamicSex, dynamicSexual, true);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSxSexual", "0");
                    //设置沙漏为紫色
//                    mFragmentDynamicSx.setImageResource(R.mipmap.shaixuan_zise);
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
            }
        });
        llSexual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vipflag.equals("1")) {
                    dynamicSex = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "mydynamicSex", "");
                    dynamicSexual = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "mydynamicSexual", "");
                    if (!dynamicSex.equals("") && !dynamicSexual.equals("")) {
                        SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSxSexual", "1");
                        dynamicSx(dynamicSex, dynamicSexual, true);
                        //设置沙漏为紫色
//                        mFragmentDynamicSx.setImageResource(R.mipmap.shaixuan_zise);
                    } else {
                        ToastUtil.show("请您重新登录,才可以使用此功能...");
                    }
                } else {
                    mPopWindow.dismiss();
                    vipDialog("会员才能使用筛选功能哦~");
                }
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    public void vipDialog(String toastStr) {
        new VipDialog(this, toastStr);
    }

    private void dynamicSx(String dynamicSex, String dynamicSexual, boolean isOpen) {
        if (isOpen) {
            SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSwitch", "1");
            ivNormalTitleMore.setImageResource(R.mipmap.shuaixuanlv);
        } else {
            SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSwitch", "0");
            ivNormalTitleMore.setImageResource(R.mipmap.popshaixuan);
        }
        SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSex", dynamicSex);
        SharedPreferencesUtils.setParam(getApplicationContext(), "dynamicSexual", dynamicSexual);
        //EventBus.getDefault().post(new DynamicSxEvent(dynamicSex, dynamicSexual));
        newTopicFragment.refreshData(dynamicSex, dynamicSexual);
        hotTopicFragment.refreshData(dynamicSex, dynamicSexual);
        mPopWindow.dismiss();
    }

    public void showBottomBar() {
        if (llActivityTopicBottom.getVisibility() == View.GONE) {
            llActivityTopicBottom.setVisibility(View.VISIBLE);
            llActivityTopicBottom.setAnimation(AnimationUtil.moveToViewLocation());
            EventBus.getDefault().post(new ViewIsVisibleEvent(1));
        }
    }

    public void hideBottomBar() {
        if (llActivityTopicBottom.getVisibility() == View.VISIBLE) {
            llActivityTopicBottom.setVisibility(View.GONE);
            llActivityTopicBottom.setAnimation(AnimationUtil.moveToViewBottom());
        }
    }

    public void showTopIcon() {
        if (ivActivityTopicGoTop.getVisibility() == View.GONE) {
            ivActivityTopicGoTop.setVisibility(View.VISIBLE);
        }
    }

    public void hideTopIcon() {
        if (ivActivityTopicGoTop.getVisibility() == View.VISIBLE) {
            ivActivityTopicGoTop.setVisibility(View.GONE);
        }
    }

    public void followTopic() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("tid", topicId);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.FollowTopic, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                try {
                    JSONObject json = new JSONObject(response);
                    int dynamicRetcode = json.getInt("retcode");
                    switch (dynamicRetcode) {
                        case 2000:
                            ToastUtil.show( "关注成功");
                            ivActivityTopicFollow.setImageResource(R.mipmap.quxiaoguanzhu);
                            isFollowTopic = true;
                            EventBus.getDefault().post(new TopicRefreshEvent());
                            break;
                        default:
                            ToastUtil.show(json.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void cancelFollowTopic() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("tid", topicId);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.CancleFollowTopic, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    int dynamicRetcode = json.getInt("retcode");
                    switch (dynamicRetcode) {
                        case 2000:
                            ToastUtil.show( "取消关注");
                            ivActivityTopicFollow.setImageResource(R.mipmap.guanzhuhuati);
                            isFollowTopic = false;
                            break;
                        default:
                            ToastUtil.show( json.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getJudgeDynamic();
    }

    int dynamicState = -1;
    String toastStr = "";

    private void getJudgeDynamic() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.JudgeDynamicNewrd, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    int dynamicRetcode = json.getInt("retcode");
                    switch (dynamicRetcode) {
                        case 2000:
                            dynamicState = 1;
                            break;
                        case 3001:
                        case 4003:
                        case 4004:
                        case 5000:
                            dynamicState = 0;
                            toastStr = json.getString("msg");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}
