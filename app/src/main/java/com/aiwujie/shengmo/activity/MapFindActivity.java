package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.HomeListviewAdapter;
import com.aiwujie.shengmo.adapter.HomeUserListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.FilterData;
import com.aiwujie.shengmo.bean.HomeNewListData;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.eventbus.HomeFilterStateEvent;
import com.aiwujie.shengmo.eventbus.SharedprefrenceEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AutoLoadListener;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

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

public class MapFindActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener, View.OnClickListener {

    @BindView(R.id.mMap_find_return)
    ImageView mMapFindReturn;
    @BindView(R.id.mMap_find_Gridview)
    PullToRefreshGridView mMapFindGridview;
    @BindView(R.id.mMap_find_addrdetail)
    TextView mMapFindAddrdetail;
    private int page = 0;
    //    private List<HomeNewListData.DataBean> mapDatas = new ArrayList<>();
//    private PopupWindow mPopWindow;

    //////////////
    private List<HomeNewListData.DataBean> mapDatas = new ArrayList<>();
    private String lat;
    private String lng;
    private HomeUserListviewAdapter grideAdapter;
    Handler handler = new Handler();
    private String addrdetail;
    private AutoLoadListener autoLoadListener;
    @BindView(R.id.mNear_Sx)
    ImageView mNearSx;
    /**
     * 判断是否是初始化Fragment
     */
    private boolean hasStarted = false;
    private PopupWindow mPopWindow;
    private String onlinestate = "";
    private String realname = "";
    private String age = "";
    private String sex = "";
    private String sexual = "";
    private String role = "";
    private String culture = "";
    private String monthly = "";
    private List<Fragment> fragments;
    private List<TextView> titles;
    private List<ImageView> tvs;
    HomeNewListData gridData;
    String isvip="";
    private String upxzya="";


//    @BindView(R.id.mNear_findPeople)
//    ImageView mNearFindPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_find);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        setData();
        mnearSxState();
        isSVIP();
    }

    private void setData() {
        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lng = intent.getStringExtra("lng");
        addrdetail = intent.getStringExtra("addrdetail");
        onlinestate = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterLine", "");
        realname = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "ckAuthen", "");
        age = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterUpAge", "");
        sex = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterSex", "");
        sexual = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterQx", "");
        role = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterRole", "");
        culture = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterCulture", "");
        monthly = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterUpMoney", "");
        mMapFindAddrdetail.setText(addrdetail);
        mMapFindGridview.setFocusable(false);
        mMapFindGridview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mMapFindGridview.setOnRefreshListener(this);
        mMapFindGridview.setOnItemClickListener(this);
        //实现自动刷新
        mMapFindGridview.mHeaderLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int width = mMapFindGridview.mHeaderLayout.getHeight();
                if (width > 0) {
                    mMapFindGridview.setRefreshing();
                    mMapFindGridview.mHeaderLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
        //添加自动读页的事件
        autoLoadListener = new AutoLoadListener(callBack);
        mMapFindGridview.setOnScrollListener(autoLoadListener);

    }

    @OnClick(R.id.mMap_find_return)
    public void onClick() {
        finish();
    }

    private void getMapPeople() {
        Map<String, String> map = new HashMap<>();
        map.put("lat", lat);
        map.put("lng", lng);
        map.put("page", page + "");
        map.put("layout", "1");
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.SearchByMapNew, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("mapfindactivity", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            gridData = new Gson().fromJson(response, HomeNewListData.class);
//                            HomeNewListData gridData = new Gson().fromJson(response, HomeNewListData.class);
                            if (gridData.getData().size() == 0) {
                                page = page - 1;
                                autoLoadListener.setPage(page);
                                ToastUtil.show(getApplicationContext(), "没有更多");
                            } else {
                                if (page == 0) {
                                    int retcode = gridData.getRetcode();
                                    mapDatas.addAll(gridData.getData());
                                    try {
//                                        grideAdapter = new HomeGridviewAdapter(MapFindActivity.this, mapDatas, retcode);
                                        grideAdapter = new HomeUserListviewAdapter(MapFindActivity.this, mapDatas, retcode, 0);
                                        mMapFindGridview.setAdapter(grideAdapter);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    mapDatas.addAll(gridData.getData());
                                    grideAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        mMapFindGridview.onRefreshComplete();
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        autoLoadListener.setPage(0);
        mapDatas.clear();
        onlinestate = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterLine", "");
        realname = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "ckAuthen", "");
        age = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterUpAge", "");
        sex = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterSex", "");
        sexual = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterQx", "");
        role = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterRole", "");
        culture = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterCulture", "");
        monthly = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterUpMoney", "");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserList(sex, "", "1", false);
//                getMapPeople();
            }
        }, 500);

    }


    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserList(sex, "", "1", true);//                getMapPeople();
            }
        }, 500);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        if ("1".equals(mapDatas.get(position).getLocation_switch())||"1".equals(mapDatas.get(position).getLocation_city_switch())){
                Intent intent = new Intent(this, PesonInfoActivity.class);
                intent.putExtra("uid", mapDatas.get(position).getUid());
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

        }else {
            Intent intent = new Intent(this, PesonInfoActivity.class);
            intent.putExtra("uid", mapDatas.get(position).getUid());
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        }

    }

    AutoLoadListener.AutoLoadCallBack callBack = new AutoLoadListener.AutoLoadCallBack() {

        public void execute(int pages) {
                page = page+1;
            //getMapPeople();
            getUserList(sex, "", "1", true);
        }

    };

    //点击漏斗
    private void showOption() {
        View contentView = LayoutInflater.from(MapFindActivity.this).inflate(R.layout.item_home_pop, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setFocusable(true);
        backgroundAlpha(0.5f);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOnDismissListener(new poponDismissListener());
        mPopWindow.showAsDropDown(mNearSx);
        mPopWindow.setAnimationStyle(R.style.AnimationPreview);
        LinearLayout llSx = (LinearLayout) contentView.findViewById(R.id.item_home_pop_llSx);
        LinearLayout llSexual = (LinearLayout) contentView.findViewById(R.id.item_home_pop_llSexual);
        TextView tvSexual = (TextView) contentView.findViewById(R.id.item_home_pop_tvSexual);
        TextView llNan = (TextView) contentView.findViewById(R.id.item_home_pop_llNan);
        TextView llNv = (TextView) contentView.findViewById(R.id.item_home_pop_llNv);
        TextView llNao = (TextView) contentView.findViewById(R.id.item_home_pop_llCdts);
        TextView llAll = (TextView) contentView.findViewById(R.id.item_home_pop_llAll);
        TextView tvSx = (TextView) contentView.findViewById(R.id.item_home_pop_isOpen);
        TextView item_home_pop_tvAdvancedScreening = contentView.findViewById(R.id.item_home_pop_tvAdvancedScreening);


        if ("1".equals(SharedPreferencesUtils.getParam(getApplicationContext(), "filterZong", ""))) {
            tvSx.setText("已开启");
//            item_home_pop_tvAdvancedScreening.setTextColor(Color.parseColor("#b73acb"));//设置自定义高级筛选为紫色色
        } else {
            tvSx.setText("未开启");
//            item_home_pop_tvAdvancedScreening.setTextColor(Color.parseColor("#646464"));
        }
        onlinestate = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterLine", "");
        realname = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "ckAuthen", "");
        age = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterUpAge", "");
        sex = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterSex", "");
        sexual = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterQx", "");
        role = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterRole", "");
        culture = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterCulture", "");
        monthly = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterUpMoney", "");
        upxzya = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "filterUpxzya", "");
        if ( TextUtil.isEmpty(onlinestate) &&  TextUtil.isEmpty(realname) &&  TextUtil.isEmpty(age) && TextUtil.isEmpty(sex)  && TextUtil.isEmpty(sexual) && TextUtil.isEmpty(role) && TextUtil.isEmpty(culture) && TextUtil.isEmpty(monthly) && TextUtil.isEmpty(upxzya)) {
            llAll.setTextColor(Color.parseColor("#b73acb"));

        }
        if (TextUtil.isEmpty(onlinestate) && TextUtil.isEmpty(realname)  && TextUtil.isEmpty(age) && "1".equals(sex) &&TextUtil.isEmpty(sexual) && TextUtil.isEmpty(role) && TextUtil.isEmpty(culture) && TextUtil.isEmpty(monthly)&& TextUtil.isEmpty(upxzya)) {
            llNan.setTextColor(Color.parseColor("#b73acb"));
        }
        if (TextUtil.isEmpty(onlinestate) && TextUtil.isEmpty(realname) && TextUtil.isEmpty(age)&&  "2".equals(sex)&& TextUtil.isEmpty(sexual) && TextUtil.isEmpty(role) && TextUtil.isEmpty(culture) && TextUtil.isEmpty(monthly)&& TextUtil.isEmpty(upxzya)) {
            llNv.setTextColor(Color.parseColor("#b73acb"));
        }
        if (TextUtil.isEmpty(onlinestate) &&TextUtil.isEmpty(realname) && TextUtil.isEmpty(age) && "3".equals(sex)&& TextUtil.isEmpty(sexual)&& TextUtil.isEmpty(role) && TextUtil.isEmpty(culture)&& TextUtil.isEmpty(monthly)&& TextUtil.isEmpty(upxzya)) {
            llNao.setTextColor(Color.parseColor("#b73acb"));
        }
        //筛选取向选中
        if ("1".equals(SharedPreferencesUtils.getParam(getApplicationContext(), "nearSexual", ""))) {
            tvSexual.setTextColor(Color.parseColor("#b73acb"));
        }
        llSx.setOnClickListener(this);
        llNan.setOnClickListener(this);
        llNv.setOnClickListener(this);
        llAll.setOnClickListener(this);
        llNao.setOnClickListener(this);
        llSexual.setOnClickListener(this);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    @OnClick({R.id.mNear_Sx})
//R.id.mNear_findPeople,  R.id.mNear_title_near,R.id.mNear_title_hot, R.id.mNear_title_recommend,
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
//            case R.id.mNear_findPeople:
////                intent = new Intent(getActivity(), SeeActivity.class);
////                startActivity(intent);
//                if (SharedPreferencesUtils.getParam(getApplicationContext(), "modle", "0").equals("0")) {
//                    SharedPreferencesUtils.setParam(getApplicationContext(), "modle", "1");
////                    mNearFindPeople.setImageResource(R.mipmap.liebiao);
//                    EventBus.getDefault().post(new ChangeLayoutEvent(1));
//                } else {
//                    SharedPreferencesUtils.setParam(getApplicationContext(), "modle", "0");
////                    mNearFindPeople.setImageResource(R.mipmap.gongge);
//                    EventBus.getDefault().post(new ChangeLayoutEvent(0));
//                }
//                break;
//            case R.id.mNear_title_near:
//                Selected(0);
//                EventBus.getDefault().post(new RecommendEvent());
//                break;
//            case R.id.mNear_title_hot:
//                Selected(1);
//                break;
//            case R.id.mNear_title_recommend:
//                Selected(2);
//                break;
//            case R.id.mNear_title_new:
//                Selected(3);
//                mFragmentNearNewCount.setVisibility(View.GONE);
//                //清除首页附近红点
//                EventBus.getDefault().post(new ClearRedPointEvent("clearNearRedPoint", 0));
//                break;
            case R.id.mNear_Sx:
                showOption();//展示筛选框
                break;
            case R.id.item_home_pop_llNan:
                sex = "1";
                mNearSx.setImageResource(R.mipmap.shuaixuanlv);
//                mNearSx.setImageResource(R.mipmap.shaixuan_zise);
                view.setSelected(true);
                simpleSp(sex, "", "1", false);
                break;
            case R.id.item_home_pop_llNv:
                sex = "2";
                mNearSx.setImageResource(R.mipmap.shuaixuanlv);
//                mNearSx.setImageResource(R.mipmap.shaixuan_zise);
                view.setSelected(true);
                simpleSp(sex, "", "1", false);
                break;
            case R.id.item_home_pop_llCdts:
                sex = "3";
                mNearSx.setImageResource(R.mipmap.shuaixuanlv);
//                mNearSx.setImageResource(R.mipmap.shaixuan_zise);
                view.setSelected(true);
                simpleSp(sex, "", "1", false);
                break;
            case R.id.item_home_pop_llAll:
                sex = "";
                mNearSx.setImageResource(R.mipmap.popshaixuan);
                view.setSelected(false);
                simpleSp(sex, "", "", false);
                break;
            case R.id.item_home_pop_llSexual:
                //根据自己性取向筛选
                String ownerSex = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "mysex", "");
                String ownerSexual = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "mysexual", "");
                if (!ownerSexual.equals("") && !ownerSex.equals("")) {
                    simpleSp(ownerSex, ownerSexual, "1", true);
                    view.setSelected(true);
                    mNearSx.setImageResource(R.mipmap.shuaixuanlv);
//                    mNearSx.setImageResource(R.mipmap.shaixuan_zise);
                    SharedPreferencesUtils.setParam(getApplicationContext(), "nearSexual", "1");
                } else {
                    ToastUtil.show(this.getApplicationContext(), "请您重新登录,才可以使用此功能...");
                }
                break;
            case R.id.item_home_pop_llSx:
                intent = new Intent(MapFindActivity.this, HomeFilterActivity.class);
                startActivity(intent);
                mPopWindow.dismiss();
//                mNearSx.setImageResource(R.mipmap.shaixuan_zise);

                break;
        }
    }

    private void simpleSp(String sex, String sexual, String switchZ, boolean isSxOwner) {
        onlinestate = "";
        realname = "";
        age = "";
        role = "";
        culture = "";
        monthly = "";
        upxzya="";
        SharedPreferencesUtils.setParam(this.getApplicationContext(), "filterZong", switchZ);
        SharedPreferencesUtils.setParam(this.getApplicationContext(), "filterLine", onlinestate);
        SharedPreferencesUtils.setParam(this.getApplicationContext(), "ckAuthen", realname);
        SharedPreferencesUtils.setParam(this.getApplicationContext(), "filterUpAge", age);
        SharedPreferencesUtils.setParam(this.getApplicationContext(), "filterRole", role);
        SharedPreferencesUtils.setParam(this.getApplicationContext(), "filterCulture", culture);
        SharedPreferencesUtils.setParam(this.getApplicationContext(), "filterUpMoney", monthly);
        SharedPreferencesUtils.setParam(this.getApplicationContext(), "nearSexual", "0");
        SharedPreferencesUtils.setParam(this.getApplicationContext(), "filterUpxzya", upxzya);
        SharedPreferencesUtils.setParam(this.getApplicationContext(), "filterxzya", upxzya);
        if (isSxOwner) {
            SharedPreferencesUtils.setParam(this.getApplicationContext(), "filterSex", sexual);
            SharedPreferencesUtils.setParam(this.getApplicationContext(), "filterQx", sex);
            EventBus.getDefault().post(new SharedprefrenceEvent(onlinestate, realname, age, sexual, sex, role, culture, monthly,upxzya));
        } else {
            SharedPreferencesUtils.setParam(this.getApplicationContext(), "filterSex", sex);
            SharedPreferencesUtils.setParam(this.getApplicationContext(), "filterQx", sexual);
            EventBus.getDefault().post(new SharedprefrenceEvent(onlinestate, realname, age, sex, sexual, role, culture, monthly,upxzya));
        }
        if(mPopWindow!=null){
            mPopWindow.dismiss();
        }

        getUserList(this.sex, sexual, "1", false);
    }

    private void getUserList(String sex, String sexual, String switchZ, boolean isSxOwner) {

        if(isSxOwner==false){
            mapDatas.clear();
        }
        Map<String, String> map = new HashMap<>();
        map.put("page", page+"");
        map.put("layout", "1");
        map.put("type", "1");
        map.put("lat", lat);
        map.put("lng", lng);
        map.put("onlinestate", onlinestate);
        map.put("realname", realname);
        map.put("age", age);
        map.put("want", upxzya);
        map.put("sex", this.sex);
        map.put("sexual", this.sexual);

        map.put("role", role);
        map.put("culture", culture);
        map.put("monthly", monthly);
        map.put("loginid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.UserListNewth, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("mapfindactivity", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (gridData != null) {
                                gridData.getData().clear();
                            }
                           // Log.e("-------", "run: "+response );
                            gridData = new Gson().fromJson(response, HomeNewListData.class);
//                            HomeNewListData gridData = new Gson().fromJson(response, HomeNewListData.class);
                            if (gridData.getData().size() == 0) {
                                page = page - 1;
                                autoLoadListener.setPage(page);
                                ToastUtil.show(getApplicationContext(), "没有更多");
                            } else {
                                if (page == 0) {
                                    int retcode = gridData.getRetcode();
                                    mapDatas.clear();
                                    mapDatas.addAll(gridData.getData());
                                    try {
//                                        grideAdapter = new HomeGridviewAdapter(MapFindActivity.this, mapDatas, retcode);
                                        grideAdapter = new HomeUserListviewAdapter(MapFindActivity.this, mapDatas, retcode, 0);
                                        mMapFindGridview.setAdapter(grideAdapter);
                                        grideAdapter.notifyDataSetChanged();
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    mapDatas.addAll(gridData.getData());
                                    grideAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        mMapFindGridview.onRefreshComplete();
                    }
                });


//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.i("FragmentNear", "onSuccess03: " + response);
//                        try {
//                            if (modle.equals("1")) {
//                                HomeGridviewData gridData = new Gson().fromJson(response, HomeGridviewData.class);
//                                mFragmentNearNearGridview.setVisibility(View.VISIBLE);
//                                mFragmentNearNearListview.setVisibility(View.GONE);
//                                if (gridData.getData().size() == 0) {
//                                    if (gridviewpage != 0) {
//                                        gridviewpage = gridviewpage - 1;
//                                        autoLoadListener.setPage(gridviewpage);
//                                        ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
//                                    }
//                                } else {
//                                    if (gridviewpage == 0) {
//                                        int retcode = gridData.getRetcode();
//                                        gridviewDatas.addAll(gridData.getData());
//                                        try {
//                                            grideAdapter = new HomeGridviewAdapter(getActivity(), gridviewDatas, retcode);
//                                            mFragmentNearNearGridview.setAdapter(grideAdapter);
//                                        } catch (NullPointerException e) {
//                                            e.printStackTrace();
//                                        }
//                                    } else {
//                                        gridviewDatas.addAll(gridData.getData());
//                                        grideAdapter.notifyDataSetChanged();
//                                    }
//                                }
//                            } else {
//                                HomeNewListData listData = new Gson().fromJson(response, HomeNewListData.class);
//                                mFragmentNearNearGridview.setVisibility(View.GONE);
//                                mFragmentNearNearListview.setVisibility(View.VISIBLE);
//                                if (listData.getData().size() == 0) {
//                                    if (listviewpage != 0) {
//                                        listviewpage = listviewpage - 1;
//                                        ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
//                                    }
//                                } else {
//                                    if (listviewpage == 0) {
//                                        int retcode = listData.getRetcode();
//                                        listviewDatas.addAll(listData.getData());
//                                        try {
//                                            listviewAdapter = new HomeListviewAdapter(getActivity(), listviewDatas, retcode, 0);
//                                            mFragmentNearNearListview.setAdapter(listviewAdapter);
//                                        } catch (NullPointerException e) {
//                                            e.printStackTrace();
//                                        }
//                                    } else {
//                                        listviewDatas.addAll(listData.getData());
//                                        listviewAdapter.notifyDataSetChanged();
//                                    }
//                                }
//                            }
//                        } catch (JsonSyntaxException e) {
//                            e.printStackTrace();
//                        }
//                        if (modle.equals("1")) {
//                            mFragmentNearNearGridview.onRefreshComplete();
//                        } else {
//                            mFragmentNearNearListview.onRefreshComplete();
//                        }
//                        if (refresh != null) {
//                            refresh.cancel();
//                        }
//                    }
//                });
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(HomeFilterStateEvent event) {
        //右上角筛选图标状态
        mnearSxState();
    }

    private void mnearSxState() {
        if (SharedPreferencesUtils.getParam(this.getApplicationContext(), "filterZong", "").equals("1")) {
//            mNearSx.setImageResource(R.mipmap.shaixuan_zise);//有颜色
            mNearSx.setImageResource(R.mipmap.shuaixuanlv);
        } else {
//            mNearSx.setImageResource(R.mipmap.popshaixuan);//没颜色

            mNearSx.setImageResource(R.mipmap.popshaixuan);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(FilterData data) {

        upxzya = data.getUpxzya();
        onlinestate = data.getOnlinestate();
        realname = data.getRealname();
        age = data.getAge();
        sex = data.getSex();
        sexual = data.getSexual();
        role = data.getRole();
        culture = data.getCulture();
        monthly = data.getIncome();
        mapDatas.clear();
        if (grideAdapter != null) {
            grideAdapter.notifyDataSetChanged();
        }
        page = 0;
        autoLoadListener.setPage(0);
        mapDatas.clear();
        getUserList(sex, "", "1", false);

//        mNearPullRefreshScrollview.setRefreshing();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessagee(SharedprefrenceEvent event) {

        onlinestate = event.getOnlinestate();
        realname = event.getRealname();
        age = event.getAge();
        sex = event.getSex();
        sexual = event.getSexual();
        role = event.getRole();
        culture = event.getCulture();
        monthly = event.getMonthly();
        mapDatas.clear();
        if (grideAdapter != null) {
            grideAdapter.notifyDataSetChanged();
        }
        page = 0;
        autoLoadListener.setPage(0);
        mapDatas.clear();
        getUserList(sex, "", "1", false);
    }

    public void isSVIP() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.GetUserPowerInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
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
