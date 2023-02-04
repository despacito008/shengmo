package com.aiwujie.shengmo.fragment.dynamicfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.DynamicVideoAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.bean.VipAndVolunteerData;
import com.aiwujie.shengmo.decoration.SpaceItemDecoration;
import com.aiwujie.shengmo.eventbus.DianZanEvent;
import com.aiwujie.shengmo.eventbus.DynamicSxEvent;
import com.aiwujie.shengmo.eventbus.FollowEvent;
import com.aiwujie.shengmo.eventbus.ViewIsVisibleEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.ListviewOntouchListener;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.videoplay.VideoActivity;
import com.aiwujie.shengmo.view.headerviewadapter.adapter.HeaderViewAdapter;
import com.aiwujie.shengmo.view.headerviewadapter.layoutmanager.HeaderViewGridLayoutManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiwujie.shengmo.customview.CustomViewPage.SCROLL_STATE_IDLE;


/**
 * Created by 290243232 on 2017/1/12.    动态_视频
 */
public class FragmentDynamicVideo extends Fragment implements AdapterView.OnItemClickListener {


    @BindView(R.id.mFragment_dynamic_near_tv)
    TextView mFragmentDynamicNearTv;
    @BindView(R.id.mFragment_dynamic_near_ll_lock)
    AutoLinearLayout mFragmentDynamicNearLlLock;
    @BindView(R.id.mFragment_dynamic_near_top)
    ImageView mFragmentDynamicNearTop;
    @BindView(R.id.rv_dynamic_hot)
    RecyclerView rvDynamicHot;
    @BindView(R.id.srl_dynamic_hot)
    SmartRefreshLayout srlDynamicHot;
    private int page = 0;
    List<DynamicListData.DataBean> dynamics = new ArrayList<>();
    DynamicVideoAdapter dynamicVideoAdapter;
    private boolean eventBoolean = true;
    /**
     * 是否可见状态
     */
    private boolean isVisible;

    String lastid = "";
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
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;
    private int loadcount = 0;
    private TimeSecondUtils refresh;
    private String sex = "";
    private String sexual = "";
    private boolean isCanLoad = true;
    private String myadmin = "0";
    String mode = "0";
    public static FragmentDynamicVideo newInstance(String mode) {
        Bundle args = new Bundle();
        args.putString("mode",mode);
        FragmentDynamicVideo fragment = new FragmentDynamicVideo();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.fragment_dynamic_video, null);
        ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
        isSVIP();
        initRecyclerView();
        mode = getArguments().getString("mode");
        return view;
    }

    private void setData() {

    }

    private void setListener() {


    }


    private void getDynamicList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("type", "7");
        map.put("page", page + "");
        map.put("loginuid", MyApp.uid);
        map.put("sex", sex);
        map.put("sexual", sexual);
        map.put("lastid", lastid);
        map.put("mode",mode);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.DynamicShortVideoList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {

                try {
                    DynamicListData listData = new Gson().fromJson(response, DynamicListData.class);
                    if (page == 0) {
                        srlDynamicHot.finishRefresh();
                    } else {
                        srlDynamicHot.finishLoadMore();
                    }
                    if (listData.getData().size() == 0) {
                        if (page != 0) {
                            page = page - 1;
                            isReresh = false;
                        }
                        ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                    } else {
                        isReresh = true;
                        if (page == 0) {
                            dynamics.clear();
                            dynamics.addAll(listData.getData());
                            if (dynamicVideoAdapter==null){
                                try {
                                    dynamicVideoAdapter = new DynamicVideoAdapter(getActivity(), dynamics, "7");
                                    dynamicVideoAdapter.setOnItemClickListener(new DynamicVideoAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            position = position - 2;
                                            Intent intent = new Intent(getContext(), VideoActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("videoList", (Serializable) dynamics);
                                            bundle.putString("type", "7");
                                            bundle.putString("page", page + "");
                                            bundle.putString("currentPosition", position + "");
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    });
                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                                    SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(getContext(), 10,true);
                                    rvDynamicHot.addItemDecoration(spaceItemDecoration);
//                                    rvDynamicHot.setAdapter(dynamicVideoAdapter);
                                    rvDynamicHot.setLayoutManager(gridLayoutManager);
                                    HeaderViewAdapter headerViewAdapter = new HeaderViewAdapter(dynamicVideoAdapter);
                                    headerViewAdapter.addHeaderView(getHeaderView());
                                    headerViewAdapter.addHeaderView(getHeaderView());
                                    rvDynamicHot.setAdapter(headerViewAdapter);
                                    //rvDynamicHot.setLayoutManager(new HeaderViewGridLayoutManager(getActivity(),2,headerViewAdapter));

                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }else {

                                dynamicVideoAdapter.notifyDataSetChanged();
                            }
                        } else {

                            int temp = dynamics.size();
                            dynamics.addAll(listData.getData());
                            dynamicVideoAdapter.notifyItemRangeInserted(temp, listData.getData().size());
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                isCanLoad = true;

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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
            if (eventBoolean) {
                EventBus.getDefault().register(this);
                eventBoolean = false;
            }
            getDynamicSx();
            setData();
            setListener();
            srlDynamicHot.autoRefresh();
        }
    }

    private void getDynamicSx() {
        sex = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dynamicSex", "");
        sexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "dynamicSexual", "");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(DynamicSxEvent data) {
        page = 0;
        sex = data.getSex();
        sexual = data.getSexual();
        dynamics.clear();
        if (dynamicVideoAdapter != null) {
            dynamicVideoAdapter.notifyDataSetChanged();
        }
        lastid = "";
        getDynamicList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @OnClick(R.id.mFragment_dynamic_near_top)
    public void onClick() {
        rvDynamicHot.scrollToPosition(0);
        EventBus.getDefault().post(new ViewIsVisibleEvent(1));
        mFragmentDynamicNearTop.setVisibility(View.GONE);
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
                            myadmin = data.getData().getIs_admin();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    void initRecyclerView() {

        srlDynamicHot.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                if (dynamics.size() != 0) {
                    lastid = dynamics.get(0).getDid();
                }
                getDynamicList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 0;
                getDynamicList();
            }
        });

        srlDynamicHot.setOnMultiPurposeListener(new OnMultiPurposeListener() {

            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                if (isDragging) {
                    ((FragmentDynamicPlaza) getParentFragment()).hideTab();
                }
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int extendHeight) {

            }


            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int extendHeight) {

            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
                ((FragmentDynamicPlaza) getParentFragment()).showTab();
            }

            @Override
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {

            }


            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int extendHeight) {

            }


            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int extendHeight) {

            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {

            }

            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

            }

            @Override
            public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

            }
        });

        rvDynamicHot.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Log.d("xmf","12345");

                //if (newState == SCROLL_STATE_IDLE || newState == SCROLL_STATE_DRAGGING) {
                if (newState == SCROLL_STATE_IDLE) {
                    // DES: 找出当前可视Item位置
                    RecyclerView.LayoutManager layoutManager = rvDynamicHot.getLayoutManager();
                    if (layoutManager instanceof GridLayoutManager) {

                        if (((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition() == 0) {
                                showOrHideTopArrow(false);
                        } else {
                                showOrHideTopArrow(true);
                        }
                    }

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    ((FragmentDynamicPlaza)getParentFragment()).hideTab();
                } else {
                    ((FragmentDynamicPlaza)getParentFragment()).showTab();
                }
            }
        });
        rvDynamicHot.setOnTouchListener(new ListviewOntouchListener(0));
    }

    /**
     * 关注
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(FollowEvent event) {
            if (dynamics.size()>= event.getPosition()){
                dynamics.get(event.getPosition()).setFollow_state(event.getFollowStateNew());
                if (dynamicVideoAdapter!=null){
                    dynamicVideoAdapter.notifyItemChanged(event.getPosition());
                }
            }
    }

    /**
     * 点赞event
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void dianZanEventBus(DianZanEvent event){
        if (dynamics.size()>= event.getPosition()){
            dynamics.get(event.getPosition()).setLaudstate("1");
            dynamics.get(event.getPosition()).setLaudnum((Integer.parseInt(dynamics.get(event.getPosition()).getLaudnum())+1)+"");
            if (dynamicVideoAdapter!=null){
                dynamicVideoAdapter.notifyItemChanged(event.getPosition());
            }
        }
    }

    public View getHeaderView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header_fragment_hot,null);
        return view;
    }

    void showOrHideTopArrow(boolean isShow) {
        if (isShow) {
            if (mFragmentDynamicNearTop.getVisibility() == View.GONE) {
                mFragmentDynamicNearTop.setVisibility(View.VISIBLE);
                mFragmentDynamicNearTop.setAnimation(AnimationUtil.moveToViewLocation());
            }
        } else {
            if (mFragmentDynamicNearTop.getVisibility() == View.VISIBLE) {
                mFragmentDynamicNearTop.setVisibility(View.GONE);
                mFragmentDynamicNearTop.setAnimation(AnimationUtil.moveToViewBottom());
            }
        }
    }

    public void refreshMode(String mode) {
        this.mode = mode;
         page = 0;
         getDynamicList();
    }

}

