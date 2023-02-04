package com.aiwujie.shengmo.fragment.homefragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.BannerWebActivity;
import com.aiwujie.shengmo.activity.DynamicDetailActivity;
import com.aiwujie.shengmo.activity.GroupSquareActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.RichesListActivity;
import com.aiwujie.shengmo.activity.SearchActivity;
import com.aiwujie.shengmo.activity.TopicDetailActivity;
import com.aiwujie.shengmo.activity.ranking.RewardRankingActivity;
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.HomeGridviewAdapter;
import com.aiwujie.shengmo.adapter.HomeListviewAdapter;
import com.aiwujie.shengmo.adapter.HomeUserListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.BannerNewData;
import com.aiwujie.shengmo.bean.FilterData;
import com.aiwujie.shengmo.bean.HomeGridviewData;
import com.aiwujie.shengmo.bean.HomeNewListData;
import com.aiwujie.shengmo.bean.ScenesRoomInfoBean;
import com.aiwujie.shengmo.bean.SearchUserData;
import com.aiwujie.shengmo.eventbus.ChangeLayoutEvent;
import com.aiwujie.shengmo.eventbus.ClickHomeEvent;
import com.aiwujie.shengmo.eventbus.MainPageTurnEvent;
import com.aiwujie.shengmo.eventbus.NearTopEvent;
import com.aiwujie.shengmo.eventbus.RecommendEvent;
import com.aiwujie.shengmo.eventbus.SharedprefrenceEvent;
import com.aiwujie.shengmo.fragment.warningfragment.PunishmentFragment;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.adapter.RecommendLivingAdapter;
import com.aiwujie.shengmo.kt.ui.activity.MapActivity;
import com.aiwujie.shengmo.kt.ui.activity.statistical.WarningRankListActivity;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.qnlive.utils.QNRoomManager;
import com.aiwujie.shengmo.timlive.net.RoomManager;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.AutoLoadListener;
import com.aiwujie.shengmo.utils.BannerUtils;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.LogUtil;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.android.percent.support.PercentFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 290243232 on 2017/3/22.
 */

public class FragmentNearRecommend extends Fragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2, OnBannerListener {

    @BindView(R.id.mFragment_near_recommend_listview)
    PullToRefreshListView mFragmentNearRecommendListview;
    @BindView(R.id.mFragment_near_recommend_gridview)
    PullToRefreshGridView mFragmentNearRecommendGridview;
    @BindView(R.id.mFragment_near_recommend_top)
    ImageView mFragmentNearRecommendTop;

    private String modle = "";
    private String onlinestate = "";
    private String realname = "";
    private String age = "";
    private String sex = "";
    private String sexual = "";
    private String role = "";
    private String culture = "";
    private String monthly = "";
    private String upxzya = "";
    private List<HomeGridviewData.DataBean> gridviewDatas = new ArrayList<>();
    private List<HomeNewListData.DataBean> listviewDatas = new ArrayList<>();
    private HomeGridviewAdapter grideAdapter;
    private HomeUserListviewAdapter listviewAdapter;
    private int gridviewpage = 0;
    private int listviewpage = 0;
    private String type = "0";
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
    Handler handler = new Handler();
    private AutoLoadListener autoLoadListener;
    private TimeSecondUtils refresh;
    private View gridHeaderView;
    private View listHeaderView;
    private TextView tvRich;
    private TextView tvBeauty;
    private TextView tvFans;

    private TextView tvRich_list;
    private TextView tvBeauty_list;
    private TextView tvFans_list;
    private int clickCount = 0;
    private List<String> bannerTitle;
    private List<String> bannerPath;
    private List<String> bannerUrl;
    private List<String> linkType;
    private List<String> linkId;
    private boolean bannerCloseFlag = false;
    /*   private EditText etSearch;
       private TextView tvSearch;*/
    private InputMethodManager imm;
    private LinearLayout llHeaderLiving;
    private RecyclerView rvHeaderLiving;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_near_recommend, null);
        ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    //
    private void getSharedprefrence() {
        onlinestate = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterLine", "");
        realname = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterAuthen", "");
        age = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterUpAge", "");
        sex = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterSex", "");
        sexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterQx", "");
        role = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterRole", "");
        culture = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterCulture", "");
        monthly = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterUpMoney", "");
        upxzya = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterUpxzya", "");
    }

    private void setData() {
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mFragmentNearRecommendListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentNearRecommendGridview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        modle = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "modle", "0");
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (modle.equals("1")) {
                    mFragmentNearRecommendGridview.setVisibility(View.VISIBLE);
                    mFragmentNearRecommendListview.setVisibility(View.GONE);
                    mFragmentNearRecommendGridview.setRefreshing();
                } else {
                    mFragmentNearRecommendGridview.setVisibility(View.GONE);
                    mFragmentNearRecommendListview.setVisibility(View.VISIBLE);
                    mFragmentNearRecommendListview.setRefreshing();
                }
            }
        }, 100);

    }

    private void setListener() {
        mFragmentNearRecommendListview.setOnRefreshListener(this);
        mFragmentNearRecommendGridview.setOnRefreshListener(this);
        mFragmentNearRecommendGridview.getRefreshableView().setOnItemClickListener(this);
        mFragmentNearRecommendListview.getRefreshableView().setOnItemClickListener(this);
        mFragmentNearRecommendListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            listviewpage = listviewpage + 1;
                            getUserList();
                        }
                        break;
                }

                if (mFragmentNearRecommendListview.getRefreshableView().getFirstVisiblePosition() == 0) {
                    mFragmentNearRecommendTop.setVisibility(View.GONE);
                    mFragmentNearRecommendTop.setAnimation(AnimationUtil.ViewToGone());
                } else {
                    if (mFragmentNearRecommendTop.getVisibility() == View.GONE) {
                        mFragmentNearRecommendTop.setVisibility(View.VISIBLE);
                        mFragmentNearRecommendTop.setAnimation(AnimationUtil.ViewToVisible());
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });

        //添加自动读页的事件
        autoLoadListener = new AutoLoadListener(callBack);
        mFragmentNearRecommendGridview.setOnScrollListener(autoLoadListener);
    }

    private void getBanner(final Banner mNearBanner, final FrameLayout mNearBannerFramlayout) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "1");
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetSlideMore, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
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
                                    mNearBannerFramlayout.setVisibility(View.VISIBLE);
                                    //设置图片集合
                                    mNearBanner.setImages(bannerPath);
                                    mNearBanner.start();
                                    break;
                                case 4000:
                                    mNearBannerFramlayout.setVisibility(View.GONE);
                                    bannerCloseFlag = true;
                                    break;
                            }
                            if (mFragmentNearRecommendGridview.getVisibility() == View.VISIBLE) {
                                if (grideAdapter != null) {
                                    grideAdapter.notifyDataSetChanged();
                                }

                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void getUserList() {
        if (SafeCheckUtil.isActivityFinish(getActivity())) {
            return;
        }
        modle = (String) SharedPreferencesUtils.getParam(MyApp.getInstance(), "modle", "0");
        Map<String, String> map = new HashMap<>();
        if (modle.equals("1")) {
            map.put("page", gridviewpage + "");
            map.put("layout", "0");
        } else {
            map.put("page", listviewpage + "");
            //列表模式
            map.put("layout", "1");
        }
        map.put("type", type);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("onlinestate", onlinestate);
        map.put("realname", realname);
        map.put("age", age);
        map.put("sex", sex);
        map.put("sexual", sexual);
        map.put("role", role);
        map.put("culture", culture);
        map.put("monthly", monthly);
        map.put("want", upxzya);
        map.put("loginid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.UserListNewth, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                try {
                    Log.i("FragmentNear", "onSuccess02: " + response);
                    if (modle.equals("1")) {
                        HomeGridviewData gridData = new Gson().fromJson(response, HomeGridviewData.class);
                        mFragmentNearRecommendGridview.setVisibility(View.VISIBLE);
                        mFragmentNearRecommendListview.setVisibility(View.GONE);
                        if (grideAdapter == null) {
                            int retcode = gridData.getRetcode();
                            gridviewDatas.addAll(gridData.getData());
                            gridHeaderView = View.inflate(getActivity(), R.layout.item_home_header, null);
                            mFragmentNearRecommendGridview.getRefreshableView().addHeaderView(gridHeaderView);
                            headerViewListener(gridHeaderView, 0);
                            grideAdapter = new HomeGridviewAdapter(getActivity(), gridviewDatas, retcode);
                            mFragmentNearRecommendGridview.setAdapter(grideAdapter);
                        } else {
                            if (gridData.getData().size() == 0) {
                                if (gridviewpage != 0) {
                                    gridviewpage = gridviewpage - 1;
                                    autoLoadListener.setPage(gridviewpage);
                                    ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                                }
                            } else {

                                gridviewDatas.addAll(gridData.getData());
                                grideAdapter.notifyDataSetChanged();
                            }

                        }
                        //头部点击事件
                        if (type.equals("5")) {
                            tvRich.setSelected(true);
                            tvBeauty.setSelected(false);
                            tvFans.setSelected(false);
                        } else if (type.equals("6")) {
                            tvRich.setSelected(false);
                            tvBeauty.setSelected(true);
                            tvFans.setSelected(false);
                        } else if (type.equals("2")) {
                            tvRich.setSelected(false);
                            tvBeauty.setSelected(false);
                            tvFans.setSelected(true);
                        }

                    } else {
                        HomeNewListData listData = new Gson().fromJson(response, HomeNewListData.class);
                        mFragmentNearRecommendGridview.setVisibility(View.GONE);
                        mFragmentNearRecommendListview.setVisibility(View.VISIBLE);

                        if (listviewAdapter == null) {
                            listHeaderView = View.inflate(getContext(), R.layout.item_home_header, null);
                            mFragmentNearRecommendListview.getRefreshableView().addHeaderView(listHeaderView);
                            headerViewListener(listHeaderView, 1);
                            int retcode = listData.getRetcode();
                            listviewDatas.addAll(listData.getData());
                            listviewAdapter = new HomeUserListviewAdapter(getActivity(), listviewDatas, retcode, 0);
                            mFragmentNearRecommendListview.setAdapter(listviewAdapter);

                        } else {
                            if (listData.getData().size() == 0) {
                                if (listviewpage != 0) {
                                    listviewpage = listviewpage - 1;
                                    ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                                }
                            } else {
                                listviewDatas.addAll(listData.getData());
                                listviewAdapter.notifyDataSetChanged();
                            }
                        }

                        //头部点击事件
                        if (type.equals("5")) {
                            tvRich_list.setSelected(true);
                            tvBeauty_list.setSelected(false);
                            tvFans_list.setSelected(false);
                        } else if (type.equals("6")) {
                            tvRich_list.setSelected(false);
                            tvBeauty_list.setSelected(true);
                            tvFans_list.setSelected(false);
                        } else if (type.equals("2")) {
                            tvRich_list.setSelected(false);
                            tvBeauty_list.setSelected(false);
                            tvFans_list.setSelected(true);
                        }

                    }

                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (modle.equals("1")) {
                    mFragmentNearRecommendGridview.onRefreshComplete();
                } else {
                    mFragmentNearRecommendListview.onRefreshComplete();
                }
                if (refresh != null) {
                    refresh.cancel();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    /**
     * @param view
     * @param state 0表示是grid 1表示是列表
     */
    private void headerViewListener(View view, int state) {
        bannerTitle = new ArrayList<>();
        bannerPath = new ArrayList<>();
        bannerUrl = new ArrayList<>();
        linkType = new ArrayList<>();
        linkId = new ArrayList<>();
        final AutoLinearLayout richLL = (AutoLinearLayout) view.findViewById(R.id.mHome_header_ll01);
        final AutoLinearLayout beautyLL = (AutoLinearLayout) view.findViewById(R.id.mHome_header_ll02);
        final AutoLinearLayout fansLL = (AutoLinearLayout) view.findViewById(R.id.mHome_header_ll03);
        final AutoLinearLayout fansJS = (AutoLinearLayout) view.findViewById(R.id.mHome_header_ll04);

        final TextView tvHeaderRecommend = view.findViewById(R.id.tv_home_header_recommend);
        final TextView tvHeaderWealth = view.findViewById(R.id.tv_home_header_wealth);
        final TextView tvHeaderCharm = view.findViewById(R.id.tv_home_header_charm);
        final TextView tvHeaderFans = view.findViewById(R.id.tv_home_header_fans);

        View viewGroup = view.findViewById(R.id.view_group);
        View viewMap = view.findViewById(R.id.view_map);
        View viewVideo = view.findViewById(R.id.view_video);
        View viewtopics = view.findViewById(R.id.view_topics);
        View viewExceptionalList = view.findViewById(R.id.view_exceptional_list);
        View viewPunishment = view.findViewById(R.id.view_punishment_list);
        final EditText etSearch = view.findViewById(R.id.et_search);
        final TextView tvSearch = view.findViewById(R.id.tv_search);
        ImageView ivExplain = view.findViewById(R.id.iv_explain);
        TextView tvNearRule = view.findViewById(R.id.tv_near_rule);
        if (state == 0) {
            tvRich = (TextView) view.findViewById(R.id.mHome_header_tv01);
            tvBeauty = (TextView) view.findViewById(R.id.mHome_header_tv02);
            tvFans = (TextView) view.findViewById(R.id.mHome_header_tv03);
        } else {
            tvRich_list = (TextView) view.findViewById(R.id.mHome_header_tv01);
            tvBeauty_list = (TextView) view.findViewById(R.id.mHome_header_tv02);
            tvFans_list = (TextView) view.findViewById(R.id.mHome_header_tv03);
        }

        //Banner mNearBanner = (Banner) view.findViewById(R.id.mNear_banner);
        //ImageView mNearBannerClose = (ImageView) view.findViewById(R.id.mNear_banner_close);
       // final FrameLayout mNearBannerFramlayout = (FrameLayout) view.findViewById(R.id.mNear_banner_framlayout);
        //广告轮播
//        BannerUtils.setBannerView(mNearBanner);
//        mNearBanner.setOnBannerListener(this);
//        //bannerCloseFlag为true的时候不重新获取广告
//        if (!bannerCloseFlag) {
//            getBanner(mNearBanner, mNearBannerFramlayout);
//        }
//
//        mNearBannerClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mNearBannerFramlayout.setVisibility(View.GONE);
//                if (grideAdapter != null) {
//                    grideAdapter.notifyDataSetChanged();
//                }
//                //是否关闭了，true为是
//                bannerCloseFlag = true;
//            }
//        });
        richLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridviewpage = 0;
                gridviewDatas.clear();
                listviewpage = 0;
                listviewDatas.clear();
                type = "5";
                EventBus.getDefault().post(new ClickHomeEvent());
                clickCount = 1;
                getUserList();
            }
        });
        beautyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridviewpage = 0;
                gridviewDatas.clear();
                listviewpage = 0;
                listviewDatas.clear();
                type = "6";
                EventBus.getDefault().post(new ClickHomeEvent());
                clickCount = 2;
                getUserList();
            }
        });
        fansLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridviewpage = 0;
                gridviewDatas.clear();
                listviewpage = 0;
                listviewDatas.clear();
                type = "2";
                EventBus.getDefault().post(new ClickHomeEvent());
                clickCount = 3;
                getUserList();
            }
        });

        tvHeaderRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridviewpage = 0;
                gridviewDatas.clear();
                listviewpage = 0;
                listviewDatas.clear();
                if (grideAdapter != null) {
                    grideAdapter.notifyDataSetChanged();
                }
                if (listviewAdapter != null) {
                    listviewAdapter.notifyDataSetChanged();
                }
                type = "0";
                clickCount = 0;
                getUserList();

                tvHeaderRecommend.setTextColor(getResources().getColor(R.color.white));
                tvHeaderWealth.setTextColor(getResources().getColor(R.color.titleBlack));
                tvHeaderCharm.setTextColor(getResources().getColor(R.color.titleBlack));
                tvHeaderFans.setTextColor(getResources().getColor(R.color.titleBlack));
                tvHeaderRecommend.setBackgroundResource(R.drawable.bg_round_purple_home);
                tvHeaderWealth.setBackgroundResource(R.drawable.bg_round_gray_home);
                tvHeaderCharm.setBackgroundResource(R.drawable.bg_round_gray_home);
                tvHeaderFans.setBackgroundResource(R.drawable.bg_round_gray_home);
            }
        });

        tvHeaderWealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridviewpage = 0;
                gridviewDatas.clear();
                listviewpage = 0;
                listviewDatas.clear();
                type = "5";
                EventBus.getDefault().post(new ClickHomeEvent());
                clickCount = 1;
                getUserList();

                tvHeaderRecommend.setTextColor(getResources().getColor(R.color.titleBlack));
                tvHeaderWealth.setTextColor(getResources().getColor(R.color.white));
                tvHeaderCharm.setTextColor(getResources().getColor(R.color.titleBlack));
                tvHeaderFans.setTextColor(getResources().getColor(R.color.titleBlack));
                tvHeaderRecommend.setBackgroundResource(R.drawable.bg_round_gray_home);
                tvHeaderWealth.setBackgroundResource(R.drawable.bg_round_purple_home);
                tvHeaderCharm.setBackgroundResource(R.drawable.bg_round_gray_home);
                tvHeaderFans.setBackgroundResource(R.drawable.bg_round_gray_home);
            }
        });

        tvHeaderCharm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridviewpage = 0;
                gridviewDatas.clear();
                listviewpage = 0;
                listviewDatas.clear();
                type = "6";
                EventBus.getDefault().post(new ClickHomeEvent());
                clickCount = 2;
                getUserList();

                tvHeaderRecommend.setTextColor(getResources().getColor(R.color.titleBlack));
                tvHeaderWealth.setTextColor(getResources().getColor(R.color.titleBlack));
                tvHeaderCharm.setTextColor(getResources().getColor(R.color.white));
                tvHeaderFans.setTextColor(getResources().getColor(R.color.titleBlack));
                tvHeaderRecommend.setBackgroundResource(R.drawable.bg_round_gray_home);
                tvHeaderWealth.setBackgroundResource(R.drawable.bg_round_gray_home);
                tvHeaderCharm.setBackgroundResource(R.drawable.bg_round_purple_home);
                tvHeaderFans.setBackgroundResource(R.drawable.bg_round_gray_home);
            }
        });

        tvHeaderFans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridviewpage = 0;
                gridviewDatas.clear();
                listviewpage = 0;
                listviewDatas.clear();
                type = "2";
                EventBus.getDefault().post(new ClickHomeEvent());
                clickCount = 3;
                getUserList();

                tvHeaderRecommend.setTextColor(getResources().getColor(R.color.titleBlack));
                tvHeaderWealth.setTextColor(getResources().getColor(R.color.titleBlack));
                tvHeaderCharm.setTextColor(getResources().getColor(R.color.titleBlack));
                tvHeaderFans.setTextColor(getResources().getColor(R.color.white));
                tvHeaderRecommend.setBackgroundResource(R.drawable.bg_round_gray_home);
                tvHeaderWealth.setBackgroundResource(R.drawable.bg_round_gray_home);
                tvHeaderCharm.setBackgroundResource(R.drawable.bg_round_gray_home);
                tvHeaderFans.setBackgroundResource(R.drawable.bg_round_purple_home);
            }
        });

        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupSquareActivity.class);
                startActivity(intent);
            }
        });

        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MapActivity.class);
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("mapflag", "1");
                startActivity(intent);
            }
        });
        viewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MainPageTurnEvent(1, 1));

            }
        });

        viewtopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MainPageTurnEvent(1, 3));

            }
        });
        viewExceptionalList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent  intent = new Intent(getActivity(), RichesListActivity.class);
                Intent intent = new Intent(getActivity(), RewardRankingActivity.class);
                startActivity(intent);
            }
        });

        viewPunishment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), WarningRankListActivity.class);
                Intent intent = new Intent(getActivity(), WarningRankListActivity.class);
                startActivity(intent);
            }
        });

        fansJS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WarningRankListActivity.class);
                startActivity(intent);
            }
        });

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etSearch.getText().toString().equals("")) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.putExtra("search", etSearch.getText().toString().trim());
                    startActivity(intent);
                    etSearch.setText("");
                } else {
                    ToastUtil.show(getActivity().getApplicationContext(), "请输入搜索内容...");
                }
            }
        });
        tvNearRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BannerWebActivity.class);
                intent.putExtra("path", HttpUrl.NetPic() + "Home/Info/Shengmosimu/id/2");
                intent.putExtra("title", "圣魔");
                startActivity(intent);
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!etSearch.getText().toString().equals("")) {
                        // 隐藏软键盘
                        if (imm.isActive()) {
                            imm.hideSoftInputFromWindow(
                                    v.getApplicationWindowToken(), 0);
                        }
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        intent.putExtra("search", etSearch.getText().toString().trim());
                        startActivity(intent);
                        etSearch.setText("");
                    } else {
                        ToastUtil.show(getActivity().getApplicationContext(), "请输入搜索内容...");
                    }
                    return true;
                }
                return false;
            }
        });

        llHeaderLiving = view.findViewById(R.id.ll_home_living);
        final LinearLayout llHeaderLivingInfo = view.findViewById(R.id.ll_home_living_info);
        rvHeaderLiving = view.findViewById(R.id.rv_home_living);
        llHeaderLiving.setVisibility(View.GONE);
        getLivingData();

        llHeaderLivingInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MainPageTurnEvent(3, 0));
            }
        });
    }

    void getLivingData() {
        if (llHeaderLiving == null) {
            return;
        }
        HttpHelper.getInstance().getRecommendLiveList(new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                llHeaderLiving.setVisibility(View.VISIBLE);
                final SearchUserData livingUser = GsonUtil.GsonToBean(data, SearchUserData.class);
                LogUtil.d("living size = " + livingUser.getData().size());
                if (livingUser.getData().size() == 0) {
                    llHeaderLiving.setVisibility(View.GONE);
                    return;
                }
                RecommendLivingAdapter liveUserAdapter = new RecommendLivingAdapter(getActivity(), livingUser.getData());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rvHeaderLiving.setAdapter(liveUserAdapter);
                rvHeaderLiving.setLayoutManager(linearLayoutManager);
                liveUserAdapter.setOnSimpleItemListener(new OnSimpleItemListener() {
                    @Override
                    public void onItemListener(int position) {
                        //List<ScenesRoomInfoBean> roomInfoBeanList = new ArrayList<>();
                        //roomInfoBeanList.add(livingUser.getData().get(position));
                        //RoomManager.enterRoom(getActivity(),roomInfoBeanList,0,"");

//                       RoomManager.enterRoom(getActivity(),livingUser.getData().get(position));
                        RoomManager.enterLiveRoom(getActivity(),
                                livingUser.getData().get(position).getUid(),
                                livingUser.getData().get(position).getRoom_id());
//                        QNRoomManager.getInstance().gotoLiveRoom(getActivity(),
//                                livingUser.getData().get(position).getUid(),
//                                livingUser.getData().get(position).getRoom_id());
                    }
                });
            }

            @Override
            public void onFail(int code, String msg) {
                llHeaderLiving.setVisibility(View.GONE);
            }
        });
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
            EventBus.getDefault().register(this);
            getSharedprefrence();
            setData();
            setListener();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(FilterData data) {
        listviewpage = 0;
        gridviewpage = 0;
        onlinestate = data.getOnlinestate();
        realname = data.getRealname();
        age = data.getAge();
        sex = data.getSex();
        sexual = data.getSexual();
        role = data.getRole();
        culture = data.getCulture();
        monthly = data.getIncome();
        upxzya = data.getUpxzya();
        gridviewDatas.clear();
        listviewDatas.clear();
        if (grideAdapter != null) {
            grideAdapter.notifyDataSetChanged();
        }
        if (listviewAdapter != null) {
            listviewAdapter.notifyDataSetChanged();
        }
        getUserList();
//        mNearPullRefreshScrollview.setRefreshing();
    }

    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessagee(SharedprefrenceEvent event) {
        listviewpage = 0;
        gridviewpage = 0;
        onlinestate = event.getOnlinestate();
        realname = event.getRealname();
        age = event.getAge();
        sex = event.getSex();
        sexual = event.getSexual();
        role = event.getRole();
        culture = event.getCulture();
        monthly = event.getMonthly();
        upxzya = event.getUpxzya();
        gridviewDatas.clear();
        listviewDatas.clear();
        getUserList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(final ChangeLayoutEvent event) {
        mFragmentNearRecommendTop.setVisibility(View.GONE);
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (event.getFalg() == 0) {
                    gridviewpage = 0;
                    gridviewDatas.clear();
                } else {
                    listviewpage = 0;
                    listviewDatas.clear();
                }
                getUserList();
            }
        }.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(RecommendEvent event) {

        if (modle.equals("1")) {
            tvRich.setSelected(false);
            tvBeauty.setSelected(false);
            tvFans.setSelected(false);
        } else {
            tvRich_list.setSelected(false);
            tvBeauty_list.setSelected(false);
            tvFans_list.setSelected(false);

        }


        if (!type.equals("0")) {
            gridviewpage = 0;
            gridviewDatas.clear();
            listviewpage = 0;
            listviewDatas.clear();
            if (grideAdapter != null) {
                grideAdapter.notifyDataSetChanged();
            }
            if (listviewAdapter != null) {
                listviewAdapter.notifyDataSetChanged();
            }
            type = "0";
            clickCount = 0;
            getUserList();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(NearTopEvent event) {
        if (event.getFlag() == 0) {
            if (mFragmentNearRecommendTop.getVisibility() == View.VISIBLE) {
                mFragmentNearRecommendTop.setVisibility(View.GONE);
                mFragmentNearRecommendTop.setAnimation(AnimationUtil.ViewToGone());
            }
        } else {
            if (mFragmentNearRecommendTop.getVisibility() == View.GONE) {
                mFragmentNearRecommendTop.setVisibility(View.VISIBLE);
                mFragmentNearRecommendTop.setAnimation(AnimationUtil.ViewToVisible());
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        if (parent == mFragmentNearRecommendGridview.getRefreshableView()) {
            if (position > 2) {
//
//                intent = new Intent(getActivity(), UserInfoActivity.class);
//                intent.putExtra("uid", gridviewDatas.get(position - 3).getUid());
//                startActivity(intent);
                position = position - 3;
                intent = new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra("uid", gridviewDatas.get(position).getUid());
                startActivity(intent);
//                if (!TextUtil.isEmpty(gridviewDatas.get(position).getAnchor_room_id()) && !"0".equals(gridviewDatas.get(position).getAnchor_room_id())) {
//                    if ("1".equals(gridviewDatas.get(position).getAnchor_is_live())) {
//                        RoomManager.enterRoom(getActivity(), gridviewDatas.get(position).getUid(), Integer.parseInt(gridviewDatas.get(position).getAnchor_room_id()));
//                    } else {
//                        intent = new Intent(getActivity(),UserInfoActivity.class);
//                        intent.putExtra("uid", gridviewDatas.get(position).getUid());
//                        startActivity(intent);
//                    }
//                } else {
//                    intent = new Intent(getActivity(),UserInfoActivity.class);
//                    intent.putExtra("uid", gridviewDatas.get(position).getUid());
//                    startActivity(intent);
//                }
            }
        } else {
//            intent = new Intent(getActivity(), UserInfoActivity.class);
//            intent.putExtra("uid", listviewDatas.get(position - 2).getUid());
//            startActivity(intent);
            position = position - 2;
            intent = new Intent(getActivity(), UserInfoActivity.class);
            intent.putExtra("uid", listviewDatas.get(position).getUid());
            startActivity(intent);
//            if (!TextUtil.isEmpty(listviewDatas.get(position).getAnchor_room_id()) && !"0".equals(listviewDatas.get(position).getAnchor_room_id())) {
//                if ("1".equals(listviewDatas.get(position).getAnchor_is_live())) {
//                    RoomManager.enterRoom(getActivity(), listviewDatas.get(position).getUid(), Integer.parseInt(listviewDatas.get(position).getAnchor_room_id()));
//                } else {
//                    intent = new Intent(getActivity(),UserInfoActivity.class);
//                    intent.putExtra("uid", listviewDatas.get(position).getUid());
//                    startActivity(intent);
//                }
//            } else {
//                intent = new Intent(getActivity(),UserInfoActivity.class);
//                intent.putExtra("uid", listviewDatas.get(position).getUid());
//                startActivity(intent);
//            }
        }

    }

    AutoLoadListener.AutoLoadCallBack callBack = new AutoLoadListener.AutoLoadCallBack() {

        @Override
        public void execute(int pages) {
            gridviewpage = pages;
            getUserList();
        }

    };

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        UpLocationUtils.LogintimeAndLocation();
        getLivingData();
        if (modle.equals("1")) {
            gridviewpage = 0;
            autoLoadListener.setPage(0);
            gridviewDatas.clear();
            if (grideAdapter != null) {
                grideAdapter.notifyDataSetChanged();
            }
            refresh = new TimeSecondUtils(getActivity(), mFragmentNearRecommendGridview);
        } else {
            listviewpage = 0;
            listviewDatas.clear();
            if (listviewAdapter != null) {
                listviewAdapter.notifyDataSetChanged();
            }
            refresh = new TimeSecondUtils(getActivity(), mFragmentNearRecommendListview);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserList();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick(R.id.mFragment_near_recommend_top)
    public void onViewClicked() {
        if (modle.equals("0")) {
            mFragmentNearRecommendListview.getRefreshableView().setSelection(0);
        } else {
            mFragmentNearRecommendGridview.getRefreshableView().setSelection(0);
        }
        mFragmentNearRecommendTop.setVisibility(View.GONE);
    }

    @Override
    public void OnBannerClick(int position) {
        Intent intent = null;
        if (linkType.get(position).equals("0")) {
            intent = new Intent(getActivity(), BannerWebActivity.class);
            intent.putExtra("path", bannerUrl.get(position));
            intent.putExtra("title", bannerTitle.get(position));
        } else if (linkType.get(position).equals("1")) {
            intent = new Intent(getActivity(), TopicDetailActivity.class);
            intent.putExtra("tid", linkId.get(position));
            intent.putExtra("topictitle", bannerTitle.get(position));
        } else if (linkType.get(position).equals("2")) {
            intent = new Intent(getActivity(), DynamicDetailActivity.class);
            intent.putExtra("uid", MyApp.uid);
            intent.putExtra("did", linkId.get(position));
            intent.putExtra("pos", 1);
            intent.putExtra("showwhat", 1);
        } else {
            intent = new Intent(getActivity(), PesonInfoActivity.class);
            intent.putExtra("uid", linkId.get(position));
        }
        startActivity(intent);
    }

}
