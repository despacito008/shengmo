package com.aiwujie.shengmo.fragment.homefragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.adapter.HomeGridviewAdapter;
import com.aiwujie.shengmo.adapter.HomeListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.FilterData;
import com.aiwujie.shengmo.bean.HomeGridviewData;
import com.aiwujie.shengmo.bean.HomeNewListData;
import com.aiwujie.shengmo.eventbus.ChangeLayoutEvent;
import com.aiwujie.shengmo.eventbus.ClearRedPointEvent;
import com.aiwujie.shengmo.eventbus.NearTopEvent;
import com.aiwujie.shengmo.eventbus.SharedprefrenceEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.AutoLoadListener;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class FragmentNearNew extends Fragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {
    @BindView(R.id.mFragment_near_new_listview)
    PullToRefreshListView mFragmentNearNewListview;
    @BindView(R.id.mFragment_near_new_gridview)
    PullToRefreshGridView mFragmentNearNewGridview;
    @BindView(R.id.mFragment_near_new_top)
    ImageView mFragmentNearNewTop;

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
    private HomeListviewAdapter listviewAdapter;
    private int gridviewpage = 0;
    private int listviewpage = 0;
    /**
     * ??????????????????
     */
    private boolean isVisible;
    /**
     * ????????????View????????????????????????
     * 2016/04/29
     * ???isAdded()????????????
     * 2016/05/03
     * isPrepared???????????????,isAdded???????????????onCreateView???????????????isAdded???
     */
    private boolean isPrepared;
    /**
     * ?????????????????????
     */
    private boolean isFirstLoad = true;
    Handler handler = new Handler();
    private AutoLoadListener autoLoadListener;
    private TimeSecondUtils refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.item_fragment_near_new, null);
        ButterKnife.bind(this, view);
//        EventBus.getDefault().register(this);
//        getSharedprefrence();
        isPrepared = true;
        lazyLoad();
//        setData();
//        setListener();
//        getUserList();
        return view;
    }

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
//        EventBus.getDefault().post(new SharedprefrenceEvent(onlinestate, realname, age, sex, sexual, role, culture, monthly));
    }

    private void setData() {
        mFragmentNearNewListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentNearNewGridview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        modle = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "modle", "0");
        //??????????????????
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (modle.equals("1")) {
                    mFragmentNearNewGridview.setVisibility(View.VISIBLE);
                    mFragmentNearNewListview.setVisibility(View.GONE);
                    mFragmentNearNewGridview.setRefreshing();
                } else {
                    mFragmentNearNewListview.setRefreshing();
                    mFragmentNearNewGridview.setVisibility(View.GONE);
                    mFragmentNearNewListview.setVisibility(View.VISIBLE);
                }
            }
        }, 100);
    }

    private void setListener() {
        mFragmentNearNewListview.setOnRefreshListener(this);
        mFragmentNearNewGridview.setOnRefreshListener(this);
        mFragmentNearNewGridview.getRefreshableView().setOnItemClickListener(this);
        mFragmentNearNewListview.getRefreshableView().setOnItemClickListener(this);
        mFragmentNearNewListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
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

                if (mFragmentNearNewListview.getRefreshableView().getFirstVisiblePosition() == 0) {
                    mFragmentNearNewTop.setVisibility(View.GONE);
                    mFragmentNearNewTop.setAnimation(AnimationUtil.ViewToGone());
                } else {
                    if (mFragmentNearNewTop.getVisibility() == View.GONE) {
                        mFragmentNearNewTop.setVisibility(View.VISIBLE);
                        mFragmentNearNewTop.setAnimation(AnimationUtil.ViewToVisible());
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });

        //???????????????????????????
        autoLoadListener = new AutoLoadListener(callBack);
        mFragmentNearNewGridview.setOnScrollListener(autoLoadListener);
    }

    private void getUserList() {
        modle = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "modle", "0");
        Map<String, String> map = new HashMap<>();
        if (modle.equals("1")) {
            map.put("page", gridviewpage + "");
            map.put("layout", "0");
        } else {
            map.put("page", listviewpage + "");
            map.put("layout", "1");
        }

        map.put("type", "3");
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("onlinestate", onlinestate);
        map.put("realname", realname);
        map.put("age", age);
        map.put("sex", sex);
        map.put("sexual", sexual);
        map.put("role", role);
        map.put("want", upxzya);
        map.put("culture", culture);
        map.put("monthly", monthly);
        map.put("loginid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.UserListNewth, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("FragmentNear", "onSuccess04: " + response);
                        try {
                            if (modle.equals("1")) {
                                HomeGridviewData gridData = new Gson().fromJson(response, HomeGridviewData.class);
                                mFragmentNearNewGridview.setVisibility(View.VISIBLE);
                                mFragmentNearNewListview.setVisibility(View.GONE);
                                if (gridData.getData().size() == 0) {
                                    if (gridviewpage != 0) {
                                        gridviewpage = gridviewpage - 1;
                                        autoLoadListener.setPage(gridviewpage);
                                        ToastUtil.show(getActivity().getApplicationContext(), "????????????");
                                    }
                                } else {
                                    if (gridviewpage == 0) {
                                        int retcode = gridData.getRetcode();
                                        gridviewDatas.addAll(gridData.getData());
                                        try {
                                            grideAdapter = new HomeGridviewAdapter(getActivity(), gridviewDatas, retcode);
                                            mFragmentNearNewGridview.setAdapter(grideAdapter);
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        gridviewDatas.addAll(gridData.getData());
                                        grideAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                HomeNewListData listData = new Gson().fromJson(response, HomeNewListData.class);
                                mFragmentNearNewGridview.setVisibility(View.GONE);
                                mFragmentNearNewListview.setVisibility(View.VISIBLE);
                                if (listData.getData().size() == 0) {
                                    if (listviewpage != 0) {
                                        listviewpage = listviewpage - 1;
                                        ToastUtil.show(getActivity().getApplicationContext(), "????????????");
                                    }
                                } else {
                                    if (listviewpage == 0) {
                                        int retcode = listData.getRetcode();
                                        listviewDatas.addAll(listData.getData());
                                        try {
                                            listviewAdapter = new HomeListviewAdapter(getActivity(), listviewDatas, retcode, 1);
                                            mFragmentNearNewListview.setAdapter(listviewAdapter);
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        listviewDatas.addAll(listData.getData());
                                        listviewAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        if (modle.equals("1")) {
                            mFragmentNearNewGridview.onRefreshComplete();
                        } else {
                            mFragmentNearNewListview.onRefreshComplete();
                        }
                        if (refresh != null) {
                            refresh.cancel();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
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
     * ?????????????????????Fragment??????,????????? onCreateView
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
        upxzya=data.getUpxzya();
        onlinestate = data.getOnlinestate();
        realname = data.getRealname();
        age = data.getAge();
        sex = data.getSex();
        sexual = data.getSexual();
        role = data.getRole();
        culture = data.getCulture();
        monthly = data.getIncome();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessagee(SharedprefrenceEvent event) {
        listviewpage = 0;
        gridviewpage = 0;
        upxzya = event.getUpxzya();
        onlinestate = event.getOnlinestate();
        realname = event.getRealname();
        age = event.getAge();
        sex = event.getSex();
        sexual = event.getSexual();
        role = event.getRole();
        culture = event.getCulture();
        monthly = event.getMonthly();
        gridviewDatas.clear();
        listviewDatas.clear();
        getUserList();
//        mNearPullRefreshScrollview.setRefreshing();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(final ChangeLayoutEvent event) {
        mFragmentNearNewTop.setVisibility(View.GONE);
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
    public void onMessages(NearTopEvent event) {
        if (event.getFlag() == 0) {
            if (mFragmentNearNewTop.getVisibility() == View.VISIBLE) {
                mFragmentNearNewTop.setVisibility(View.GONE);
                mFragmentNearNewTop.setAnimation(AnimationUtil.ViewToGone());
            }
        } else {
            if (mFragmentNearNewTop.getVisibility() == View.GONE) {
                mFragmentNearNewTop.setVisibility(View.VISIBLE);
                mFragmentNearNewTop.setAnimation(AnimationUtil.ViewToVisible());
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        if (parent == mFragmentNearNewGridview.getRefreshableView()) {
            intent = new Intent(getActivity(), PesonInfoActivity.class);
            intent.putExtra("uid", gridviewDatas.get(position).getUid());

        } else {
            intent = new Intent(getActivity(), PesonInfoActivity.class);
            intent.putExtra("uid", listviewDatas.get(position - 1).getUid());
        }
        startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }

    AutoLoadListener.AutoLoadCallBack callBack = new AutoLoadListener.AutoLoadCallBack() {

        public void execute(int pages) {
            gridviewpage = pages;
            getUserList();
        }

    };

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        UpLocationUtils.LogintimeAndLocation();

        if (modle.equals("1")) {
            gridviewpage = 0;
            autoLoadListener.setPage(0);
            gridviewDatas.clear();
            refresh = new TimeSecondUtils(getActivity(), mFragmentNearNewGridview);
        } else {
            listviewpage = 0;
            listviewDatas.clear();
            if (listviewAdapter != null) {
                listviewAdapter.notifyDataSetChanged();
            }
            refresh = new TimeSecondUtils(getActivity(), mFragmentNearNewListview);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserList();
            }
        }, 300);
        //????????????????????????
        EventBus.getDefault().post(new ClearRedPointEvent("clearNearRedPoint", 0));
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick(R.id.mFragment_near_new_top)
    public void onViewClicked() {
        if (modle.equals("0")) {
            mFragmentNearNewListview.getRefreshableView().setSelection(0);
        } else {
            mFragmentNearNewGridview.getRefreshableView().setSelection(0);
        }
        mFragmentNearNewTop.setVisibility(View.GONE);
    }

}
