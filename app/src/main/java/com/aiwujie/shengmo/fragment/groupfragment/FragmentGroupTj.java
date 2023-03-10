package com.aiwujie.shengmo.fragment.groupfragment;

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
import android.widget.LinearLayout;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.GroupInfoActivity;
import com.aiwujie.shengmo.adapter.GroupListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GroupData;
import com.aiwujie.shengmo.eventbus.GroupSxEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
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
import butterknife.Unbinder;

/**
 * Created by 290243232 on 2017/1/15.
 */
public class FragmentGroupTj extends Fragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {
    @BindView(R.id.mFragment_group_tuijian_listview)
    PullToRefreshListView mFragmentGroupTuijianListview;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    private int page = 0;
    Handler handler = new Handler();
    List<GroupData.DataBean> groups = new ArrayList<>();
    private GroupListviewAdapter groupAdapter;
    private TimeSecondUtils refresh;
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
    /**
     * ????????????????????????
     */
    private boolean isReresh = true;
    private String whatSexual;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_group_tuijian, null);
        unbinder = ButterKnife.bind(this, view);
//        EventBus.getDefault().register(this);
//        setData();
//        setListener();
        isPrepared = true;
        lazyLoad();
        return view;
    }

    private void setData() {
        mFragmentGroupTuijianListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentGroupTuijianListview.setFocusable(false);
        //??????????????????
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentGroupTuijianListview.setRefreshing();
            }
        }, 100);
    }

    private void setListener() {
        mFragmentGroupTuijianListview.setOnRefreshListener(this);
        mFragmentGroupTuijianListview.setOnItemClickListener(this);
        mFragmentGroupTuijianListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isReresh) {
                                page = page + 1;
                                getGroupList();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });
    }

    private void getGroupList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("type", "0");
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", page + "");
        map.put("filterabnormal", whatSexual);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetGroupListFilter, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                Log.i("fragmentfroup", "onSuccess: " + response);
                try {
                    GroupData listData = new Gson().fromJson(response, GroupData.class);
                    if (listData.getData().size() == 0) {
                        if (page != 0) {
                            page = page - 1;
                            isReresh = false;
                            ToastUtil.show(getActivity().getApplicationContext(), "????????????");
                        } else {
                            layoutNormalEmpty.setVisibility(View.VISIBLE);
                        }
                    } else {
                        isReresh = true;
                        layoutNormalEmpty.setVisibility(View.GONE);
                        if (page == 0) {
                            groups.addAll(listData.getData());
                            int retcode = listData.getRetcode();
                            try {
                                groupAdapter = new GroupListviewAdapter(getActivity(), groups, retcode);
                                mFragmentGroupTuijianListview.setAdapter(groupAdapter);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        } else {
                            groups.addAll(listData.getData());
                            groupAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                mFragmentGroupTuijianListview.onRefreshComplete();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
        intent.putExtra("groupId", groups.get(position - 1).getGid());
        startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        groups.clear();
        if (groupAdapter != null) {
            groupAdapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(getActivity(), mFragmentGroupTuijianListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getGroupList();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getGroupList();
            }
        }, 500);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(String message) {
        if (message.equals("refreshgroup")) {
            page = 0;
            groups.clear();
            getGroupList();
        }
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
            EventBus.getDefault().register(this);
            isFirstLoad = false;
            getGroupSx();
            setData();
            setListener();
        }
    }

    private void getGroupSx() {
        whatSexual = (String) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "groupFlag", "");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(GroupSxEvent data) {
        page = 0;
        whatSexual = data.getSexFlag();
        groups.clear();
        if (groupAdapter != null) {
            groupAdapter.notifyDataSetChanged();
        }
        getGroupList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
