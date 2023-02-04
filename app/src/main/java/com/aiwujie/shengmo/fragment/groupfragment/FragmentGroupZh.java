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
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.GroupInfoActivity;
import com.aiwujie.shengmo.adapter.GroupListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GroupData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 290243232 on 2017/1/18.
 */
public class FragmentGroupZh extends Fragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {

    @BindView(R.id.mFragment_group_zh_listview)
    PullToRefreshListView mFragmentGroupZhListview;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    private String search;
    private int page = 0;
    Handler handler = new Handler();
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;
    List<GroupData.DataBean> groups = new ArrayList<>();
    private GroupListviewAdapter groupAdapter;
    private TimeSecondUtils refresh;
    private Unbinder unbind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_group_zh, null);
        unbind = ButterKnife.bind(this, view);
        search = getActivity().getIntent().getStringExtra("search");
        setData();
        setListener();
        return view;
    }

    private void setData() {
        mFragmentGroupZhListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentGroupZhListview.setFocusable(false);
        //实现自动刷新
        mFragmentGroupZhListview.mHeaderLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int width = mFragmentGroupZhListview.mHeaderLayout.getHeight();
                if (width > 0) {
                    mFragmentGroupZhListview.setRefreshing();
                    mFragmentGroupZhListview.mHeaderLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
    }

    private void setListener() {
        mFragmentGroupZhListview.setOnRefreshListener(this);
        mFragmentGroupZhListview.setOnItemClickListener(this);
        mFragmentGroupZhListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
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
        map.put("search", search);
        map.put("type", "1");
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SearchGroup, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fragmentsearchgroup", "onSuccess: " + response);
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }
                try {
                    GroupData listData = new Gson().fromJson(response, GroupData.class);
                    if (listData.getData().size() == 0) {
                        if (page != 0) {
                            page = page - 1;
                            isReresh = false;
                            ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                        } else {
                            layoutNormalEmpty.setVisibility(View.VISIBLE);
                        }
                    } else {
                        layoutNormalEmpty.setVisibility(View.GONE);
                        isReresh = true;
                        if (page == 0) {
                            groups.addAll(listData.getData());
                            int retcode = listData.getRetcode();
                            try {
                                groupAdapter = new GroupListviewAdapter(getActivity(), groups, retcode);
                                mFragmentGroupZhListview.setAdapter(groupAdapter);
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
                mFragmentGroupZhListview.onRefreshComplete();
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
        refresh = new TimeSecondUtils(getActivity(), mFragmentGroupZhListview);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }
}
