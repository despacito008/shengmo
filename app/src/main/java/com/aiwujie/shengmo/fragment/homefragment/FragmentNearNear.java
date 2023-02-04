package com.aiwujie.shengmo.fragment.homefragment;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.AppOpsManagerCompat;
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
import com.aiwujie.shengmo.activity.user.UserInfoActivity;
import com.aiwujie.shengmo.adapter.HomeGridviewAdapter;
import com.aiwujie.shengmo.adapter.HomeListviewAdapter;
import com.aiwujie.shengmo.adapter.HomeUserListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.FilterData;
import com.aiwujie.shengmo.bean.HomeGridviewData;
import com.aiwujie.shengmo.bean.HomeNewListData;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.eventbus.ChangeLayoutEvent;
import com.aiwujie.shengmo.eventbus.NearTopEvent;
import com.aiwujie.shengmo.eventbus.SharedprefrenceEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.timlive.net.RoomManager;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.AutoLoadListener;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.PermissionHelper;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
import java.util.jar.Attributes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by 290243232 on 2017/3/22.
 */

public class FragmentNearNear extends Fragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {

    @BindView(R.id.mFragment_near_near_listview)
    PullToRefreshListView mFragmentNearNearListview;
    @BindView(R.id.mFragment_near_near_gridview)
    PullToRefreshGridView mFragmentNearNearGridview;
    @BindView(R.id.mFragment_near_near_top)
    ImageView mFragmentNearNearTop;
    String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private String modle = "";
    private String onlinestate = "";
    private String realname = "";
    private String age = "";
    private String sex = "";
    private String sexual = "";
    private String role = "";
    private String culture = "";
    private String monthly = "";
    private List<HomeGridviewData.DataBean> gridviewDatas = new ArrayList<>();
    private List<HomeNewListData.DataBean> listviewDatas = new ArrayList<>();
    private HomeGridviewAdapter grideAdapter;
    private HomeUserListviewAdapter listviewAdapter;
    private int gridviewpage = 0;
    private int listviewpage = 0;
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
    String isvip = "";
    private String upxzya = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.item_fragment_near_near, null);
        ButterKnife.bind(this, view);
//        getSharedprefrence();
//        isPrepared = true;
        isPrepared = true;
        lazyLoad();
        isSVIP();
//        setData();
//        setListener();
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
        //upxzya = getSharePreferenceData("filterUpxzya");
        upxzya = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "filterUpxzya", "");
//        EventBus.getDefault().post(new SharedprefrenceEvent(onlinestate, realname, age, sex, sexual, role, culture, monthly));
    }

    String getSharePreferenceData(String key) {
        return (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), key,"");
    }

    private void setData() {
        mFragmentNearNearListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentNearNearGridview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        modle = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "modle", "0");
        //mFragmentNearNearGridview.postDelayed()
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (modle.equals("1")) {
                    mFragmentNearNearGridview.setVisibility(View.VISIBLE);
                    mFragmentNearNearListview.setVisibility(View.GONE);
                    mFragmentNearNearGridview.setRefreshing();
                } else {
                    mFragmentNearNearListview.setRefreshing();
                    mFragmentNearNearGridview.setVisibility(View.GONE);
                    mFragmentNearNearListview.setVisibility(View.VISIBLE);
                }
            }
        }, 100);
    }

    private void setListener() {
        mFragmentNearNearListview.setOnRefreshListener(this);
        mFragmentNearNearGridview.setOnRefreshListener(this);
        mFragmentNearNearGridview.getRefreshableView().setOnItemClickListener(this);
        mFragmentNearNearListview.getRefreshableView().setOnItemClickListener(this);
        mFragmentNearNearListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
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
                if (mFragmentNearNearListview.getRefreshableView().getFirstVisiblePosition() == 0) {
                    mFragmentNearNearTop.setVisibility(View.GONE);
                    mFragmentNearNearTop.setAnimation(AnimationUtil.ViewToGone());
                } else {
                    if (mFragmentNearNearTop.getVisibility() == View.GONE) {
                        mFragmentNearNearTop.setVisibility(View.VISIBLE);
                        mFragmentNearNearTop.setAnimation(AnimationUtil.ViewToVisible());
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });

        //添加自动读页的事件
        autoLoadListener = new AutoLoadListener(callBack);
        mFragmentNearNearGridview.setOnScrollListener(autoLoadListener);
    }

    private void getUserList() {
        if (SafeCheckUtil.isActivityFinish(getActivity())) {
            return;
        }
        modle = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "modle", "0");
        Map<String, String> map = new HashMap<>();
        if (modle.equals("1")) {
            map.put("page", gridviewpage + "");
            map.put("layout", "0");
        } else {
            map.put("page", listviewpage + "");
            map.put("layout", "1");
        }
        map.put("type", "1");
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
                Log.i("FragmentNear", "onSuccess03: " + response);
                try {
                    if (modle.equals("1")) {
                        HomeGridviewData gridData = new Gson().fromJson(response, HomeGridviewData.class);
                        mFragmentNearNearGridview.setVisibility(View.VISIBLE);
                        mFragmentNearNearListview.setVisibility(View.GONE);
                        if (gridData.getData().size() == 0) {
                            if (gridviewpage != 0) {
                                gridviewpage = gridviewpage - 1;
                                autoLoadListener.setPage(gridviewpage);
                                ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                            }
                        } else {
                            if (gridviewpage == 0) {
                                int retcode = gridData.getRetcode();
                                gridviewDatas.addAll(gridData.getData());
                                try {
                                    grideAdapter = new HomeGridviewAdapter(getActivity(), gridviewDatas, retcode);

                                    //检测用户是否将当前应用的定位权限拒绝
                                    int checkResult = PermissionHelper.checkOp(getContext(), 2, AppOpsManager.OPSTR_FINE_LOCATION);//其中2代表AppOpsManager.OP_GPS，如果要判断悬浮框权限，第二个参数需换成24即AppOpsManager。OP_SYSTEM_ALERT_WINDOW及，第三个参数需要换成AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW
                                    int checkResult2 = PermissionHelper.checkOp(getContext(), 1, AppOpsManager.OPSTR_FINE_LOCATION);
                                    if (AppOpsManagerCompat.MODE_IGNORED == checkResult || AppOpsManagerCompat.MODE_IGNORED == checkResult2) {
                                        PermissionHelper permissionHelper = new PermissionHelper(getContext());
                                        permissionHelper.showLocIgnoredDialog(getContext());
                                    } else {
                                        if (!EasyPermissions.hasPermissions(getContext(), perms)) {//检查是否获取该权限
                                            //第二个参数是被拒绝后再次申请该权限的解释
                                            //第三个参数是请求码
                                            //第四个参数是要申请的权限
                                            EasyPermissions.requestPermissions(getActivity(), "需要开启位置权限，以便正常使用全部功能（如您想隐藏位置，可在APP我的-设置-隐私中关闭位置）", 0, perms);
                                        } else {
                                            mFragmentNearNearGridview.setAdapter(grideAdapter);
                                        }
                                    }

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
                        mFragmentNearNearGridview.setVisibility(View.GONE);
                        mFragmentNearNearListview.setVisibility(View.VISIBLE);
                        if (listData.getData().size() == 0) {
                            if (listviewpage != 0) {
                                listviewpage = listviewpage - 1;
                                ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                            }
                        } else {
                            if (listviewpage == 0) {
                                int retcode = listData.getRetcode();
                                listviewDatas.addAll(listData.getData());
                                try {
                                    listviewAdapter = new HomeUserListviewAdapter(getActivity(), listviewDatas, retcode, 0);
                                    //检测用户是否将当前应用的定位权限拒绝
                                    int checkResult = PermissionHelper.checkOp(getContext(), 2, AppOpsManager.OPSTR_FINE_LOCATION);//其中2代表AppOpsManager.OP_GPS，如果要判断悬浮框权限，第二个参数需换成24即AppOpsManager。OP_SYSTEM_ALERT_WINDOW及，第三个参数需要换成AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW
                                    int checkResult2 = PermissionHelper.checkOp(getContext(), 1, AppOpsManager.OPSTR_FINE_LOCATION);
                                    if (AppOpsManagerCompat.MODE_IGNORED == checkResult || AppOpsManagerCompat.MODE_IGNORED == checkResult2) {
                                        PermissionHelper permissionHelper = new PermissionHelper(getContext());
                                        permissionHelper.showLocIgnoredDialog(getContext());
                                    } else {
                                       // PermissionHelper permissionHelper = new PermissionHelper(getContext());
                                      //  permissionHelper.showLocIgnoredDialog(getContext());
                                        if (!EasyPermissions.hasPermissions(getContext(), perms)) {//检查是否获取该权限
                                            //第二个参数是被拒绝后再次申请该权限的解释
                                            //第三个参数是请求码
                                            //第四个参数是要申请的权限
                                            EasyPermissions.requestPermissions(getActivity(), "需要开启位置权限，以便正常使用全部功能（如您想隐藏位置，可在APP我的-设置-隐私中关闭位置）", 0, perms);
                                        } else {
                                            mFragmentNearNearListview.setAdapter(listviewAdapter);
                                        }
                                    }

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
                    mFragmentNearNearGridview.onRefreshComplete();
                } else {
                    mFragmentNearNearListview.onRefreshComplete();
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
        upxzya = data.getUpxzya();
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
        mFragmentNearNearTop.setVisibility(View.GONE);
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
            if (mFragmentNearNearTop.getVisibility() == View.VISIBLE) {
                mFragmentNearNearTop.setVisibility(View.GONE);
                mFragmentNearNearTop.setAnimation(AnimationUtil.ViewToGone());
            }
        } else {
            if (mFragmentNearNearTop.getVisibility() == View.GONE) {
                mFragmentNearNearTop.setVisibility(View.VISIBLE);
                mFragmentNearNearTop.setAnimation(AnimationUtil.ViewToVisible());
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        if (parent == mFragmentNearNearGridview.getRefreshableView()) {
//            if (gridviewDatas.get(position).getLocation_switch().equals("1") || gridviewDatas.get(position).getLocation_city_switch().equals("1")) {
//                intent = new Intent(getActivity(), PesonInfoActivity.class);
//                intent.putExtra("uid", gridviewDatas.get(position).getUid());
//                startActivity(intent);
//
//            } else {
//                intent = new Intent(getActivity(), PesonInfoActivity.class);
//                intent.putExtra("uid", gridviewDatas.get(position).getUid());
//                startActivity(intent);
//            }

            if (!TextUtil.isEmpty(gridviewDatas.get(position).getAnchor_room_id()) && !"0".equals(gridviewDatas.get(position).getAnchor_room_id())) {
                if ("1".equals(gridviewDatas.get(position).getAnchor_is_live())) {
                    RoomManager.enterRoom(getActivity(),gridviewDatas.get(position).getUid(), Integer.parseInt(gridviewDatas.get(position).getAnchor_room_id()));
                } else {
                    intent = new Intent(getActivity(),UserInfoActivity.class);
                    intent.putExtra("uid", gridviewDatas.get(position).getUid());
                    startActivity(intent);
                }
            } else {
                intent = new Intent(getActivity(),UserInfoActivity.class);
                intent.putExtra("uid", gridviewDatas.get(position).getUid());
                startActivity(intent);
            }

        } else {
            position = position - 1;
            if (!TextUtil.isEmpty(listviewDatas.get(position).getAnchor_room_id()) && !"0".equals(listviewDatas.get(position).getAnchor_room_id())) {
                if ("1".equals(listviewDatas.get(position).getAnchor_is_live())) {
                    RoomManager.enterRoom(getActivity(), listviewDatas.get(position).getUid(), Integer.parseInt(listviewDatas.get(position).getAnchor_room_id()));
                } else {
                    intent = new Intent(getActivity(),UserInfoActivity.class);
                    intent.putExtra("uid", listviewDatas.get(position).getUid());
                    startActivity(intent);
                }
            } else {
                intent = new Intent(getActivity(),UserInfoActivity.class);
                intent.putExtra("uid", listviewDatas.get(position).getUid());
                startActivity(intent);
            }

//            if (listviewDatas.get(position - 1).getLocation_switch().equals("1") || listviewDatas.get(position - 1).getLocation_city_switch().equals("1")) {
//                intent = new Intent(getActivity(), PesonInfoActivity.class);
//                intent.putExtra("uid", listviewDatas.get(position - 1).getUid());
//                startActivity(intent);
//
//            } else {
//                intent = new Intent(getActivity(), PesonInfoActivity.class);
//                intent.putExtra("uid", listviewDatas.get(position - 1).getUid());
//                startActivity(intent);
//            }

        }

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
            refresh = new TimeSecondUtils(getActivity(), mFragmentNearNearGridview);
        } else {
            listviewpage = 0;
            listviewDatas.clear();
            if (listviewAdapter != null) {
                listviewAdapter.notifyDataSetChanged();
            }
            refresh = new TimeSecondUtils(getActivity(), mFragmentNearNearListview);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserList();
            }
        }, 300);
        UpLocationUtils.LogintimeAndLocation();

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick(R.id.mFragment_near_near_top)
    public void onViewClicked() {
        if (modle.equals("0")) {
            mFragmentNearNearListview.getRefreshableView().setSelection(0);
        } else {
            mFragmentNearNearGridview.getRefreshableView().setSelection(0);
        }
        mFragmentNearNearTop.setVisibility(View.GONE);
    }

    public void isSVIP() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetUserPowerInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                PrintLogUtils.log(response, "--");
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            VipAndVolunteerData data = new Gson().fromJson(response, VipAndVolunteerData.class);
                            String svip = data.getData().getSvip();
                            isvip = data.getData().getVip();
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
   
