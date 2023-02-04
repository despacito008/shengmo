package com.aiwujie.shengmo.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.activity.PesonInfoActivity;
import com.aiwujie.shengmo.adapter.GzFsHyListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GzFsHyListviewData;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 290243232 on 2017/1/7.
 */
public class FragmentFollow1 extends Fragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2, TextView.OnEditorActionListener {
    @BindView(R.id.mFragment_follow_listview)
    PullToRefreshListView mFragmentFollowListview;
    @BindView(R.id.iv_layout_normal_empty)
    ImageView ivLayoutNormalEmpty;
    @BindView(R.id.tv_layout_normal_empty)
    TextView tvLayoutNormalEmpty;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    private String type = "0";
    private int page = 0;
    private ArrayList<GzFsHyListviewData.DataBean> datas = new ArrayList<>();
    Handler handler = new Handler();
    private String uid;
    private GzFsHyListviewAdapter adapter;
    private int who;
    //定位权限
    private boolean permissons;
    private TimeSecondUtils refresh;
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
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;
    private boolean isCanLoad = true;
    String name = "";
    private InputMethodManager imm;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_follow1, null);
        unbinder = ButterKnife.bind(this, view);
        isPrepared = true;
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //mGroupSearchKeyWord_et_sou.setOnEditorActionListener(this);
        lazyLoad();

        return view;
    }

    private void setData() {
        ivLayoutNormalEmpty.setImageResource(R.drawable.ic_empty_fans);
        tvLayoutNormalEmpty.setText("还没有关注，赶快去关注一波吧~");
        mFragmentFollowListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentFollowListview.setFocusable(false);
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentFollowListview.setRefreshing();
            }
        }, 100);
        String permisson = "android.permission.ACCESS_COARSE_LOCATION";
        permissons = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), permisson) == PackageManager.PERMISSION_DENIED;
    }

    private void setListener() {
        mFragmentFollowListview.setOnRefreshListener(this);
        mFragmentFollowListview.setOnItemClickListener(this);
        mFragmentFollowListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isCanLoad) {
                                isCanLoad = false;
                                if (isReresh) {
                                    page = page + 1;
                                    getFollewingList();
                                }
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

    private void getFollewingList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        map.put("page", page + "");
        map.put("type", type);
        map.put("name", name);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetFollewingList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                if (SafeCheckUtil.isActivityFinish(getActivity())) {
                    return;
                }

                try {
                    GzFsHyListviewData data = new Gson().fromJson(response, GzFsHyListviewData.class);
                    if (data.getRetcode() == 4002) {
                       // ToastUtil.show(getActivity().getApplicationContext(), "该用户拒绝查看此内容");
                        tvLayoutNormalEmpty.setText("该用户拒绝查看此内容");
                    }
                    if (data.getData().size() == 0) {
                        if (permissons) {
                           // ToastUtil.show(getActivity().getApplicationContext(), "该用户拒绝查看此内容");
                        } else {
                            if (page != 0) {
                                page = page - 1;
                                isReresh = false;
                                ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                            } else {
                                layoutNormalEmpty.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        isReresh = true;
                        if (page == 0) {
                            datas.addAll(data.getData());
                            try {
                                adapter = new GzFsHyListviewAdapter(getActivity(), datas, who, type);
                                mFragmentFollowListview.setAdapter(adapter);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        } else {
                            datas.addAll(data.getData());
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                isCanLoad = true;
                mFragmentFollowListview.onRefreshComplete();
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
        Intent intent = new Intent(getActivity(), PesonInfoActivity.class);
        intent.putExtra("uid", datas.get(position - 1).getUid());
        startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        datas.clear();
        name = "";
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(getActivity(), mFragmentFollowListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFollewingList();
            }
        }, 100);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFollewingList();
            }
        }, 500);
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
            setData();
            setListener();
            uid = getActivity().getIntent().getStringExtra("uid");
            who = getActivity().getIntent().getIntExtra("who", -1);

        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            /*隐藏软键盘*/
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(
                        v.getApplicationWindowToken(), 0);
            }

            datas.clear();
            page = 0;
            getFollewingList();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void helloEventBus(String message){
//        if(message.equals("refresh")){
//            getFollewingList();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
}
