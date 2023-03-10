package com.aiwujie.shengmo.fragment.warningfragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.EditPunishmentActivity;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.activity.SendReasonActivity;
import com.aiwujie.shengmo.activity.newui.PunishmentDetailActivity;
import com.aiwujie.shengmo.adapter.PunishmentAdapter;
import com.aiwujie.shengmo.adapter.WarningAdapter;
import com.aiwujie.shengmo.bean.PunishmentBean;
import com.aiwujie.shengmo.bean.WarningListData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.normaldeal.PunishmentEditActivity;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.GsonUtil;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.OnSimpleItemListener;
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

/**
 * Created by 290243232 on 2017/11/17.
 */

public class PunishmentFragment extends Fragment implements  PullToRefreshBase.OnRefreshListener2 {
    @BindView(R.id.mWarning_listview)
    PullToRefreshListView mWarningListview;
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
    private Handler handler = new Handler();
    private int page = 0;
    private TimeSecondUtils refresh;
    private List<PunishmentBean.DataBean> punishments = new ArrayList<>();
    private PunishmentAdapter punishAdapter;

    public static PunishmentFragment newInstance() {

        Bundle args = new Bundle();

        PunishmentFragment fragment = new PunishmentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.fragment_punishment_publicity, null);
        ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }


    private void setData() {
        mWarningListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //??????????????????
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWarningListview.setRefreshing();
            }
        }, 100);
    }

    private void setListener() {
        mWarningListview.setOnRefreshListener(this);
        //mWarningListview.setOnItemClickListener(this);
        mWarningListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isReresh) {
                                page = page + 1;
                                getWarnings(false);
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

    private void getWarnings(final boolean needRefresh) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetPunishmentList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                final PunishmentBean punishmentBean = GsonUtil.GsonToBean(response.toString(), PunishmentBean.class);
                if (punishmentBean.getRetcode() == 2000) {
                    final List<PunishmentBean.DataBean> punishmentList = punishmentBean.getData();
                    if (punishmentList == null || punishmentList.size() == 0) {
                        if (page != 0) {
                            page = page - 1;
                            isReresh = false;
                            ToastUtil.show(getActivity().getApplicationContext(), "????????????");
                        }
                    } else {
                        isReresh = true;
                        if (page == 0 && !needRefresh) {
                            punishments.addAll(punishmentList);
                            try {
                                punishAdapter = new PunishmentAdapter(getActivity(), punishments);
                                mWarningListview.setAdapter(punishAdapter);
                                punishAdapter.setOnPunishmentListener(new PunishmentAdapter.OnPunishmentListener() {
                                    @Override
                                    public void onItemEdit(int position) {
                                        PunishmentBean.DataBean punishmentInfo = punishments.get(position);
                                        Intent intent = new Intent(getActivity(), EditPunishmentActivity.class);
                                        //Intent intent = new Intent(getActivity(), PunishmentEditActivity.class);
                                        intent.putExtra("info", punishmentInfo);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onItemClick(int position) {
                                        //Intent intent = new Intent(getActivity(), PesonInfoActivity.class);
                                        Intent intent = new Intent(getActivity(), PunishmentDetailActivity.class);
                                        PunishmentBean.DataBean punishBean = punishments.get(position);
                                        intent.putExtra("punish_data",  GsonUtil.GsonString(punishBean));
                                        startActivity(intent);
                                    }
                                });
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        } else {
                            punishments.addAll(punishmentList);
                            punishAdapter.notifyDataSetChanged();
                        }
                    }
                    mWarningListview.onRefreshComplete();
                    if (refresh != null) {
                        refresh.cancel();
                    }
                } else {
                    ToastUtil.show(getActivity(),punishmentBean.getMsg());
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
     * ?????????????????????Fragment??????,????????? onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (isPrepared && isVisible && isFirstLoad) {
            isFirstLoad = false;
            setData();
            setListener();
        }
    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PesonInfoActivity.class);
        intent.putExtra("uid", punishments.get(position - 1).getUid());
        startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        punishments.clear();
        if (punishAdapter != null) {
            punishAdapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(getActivity(), mWarningListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getWarnings(false);
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    public void refreshData() {
        page = 0;
        punishments.clear();
        getWarnings(true);
    }
}
