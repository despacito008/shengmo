package com.aiwujie.shengmo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.aiwujie.shengmo.activity.SearchfriendsActivity;
import com.aiwujie.shengmo.adapter.FenzuListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.FenzuListData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
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
public class FragmentFriendTopic extends Fragment implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener, TextView.OnEditorActionListener {
    @BindView(R.id.mFragment_friend_listview)
    PullToRefreshListView mFragmentFriendListview;
    @BindView(R.id.mGroupSearchKeyWord_et_sou)
    TextView mGroupSearchKeyWord_et_sou;
    @BindView(R.id.iv_layout_normal_empty)
    ImageView ivLayoutNormalEmpty;
    @BindView(R.id.tv_layout_normal_empty)
    TextView tvLayoutNormalEmpty;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    private String type = "2";
    private int page = 0;
    private ArrayList<FenzuListData.DataBean> datas = new ArrayList<>();
    Handler handler = new Handler();
    private String uid;
    private FenzuListviewAdapter adapter;
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
    private int fgid;
    private InputMethodManager imm;

    private Unbinder unbinder;

    public FragmentFriendTopic() {

    }

    public static FragmentFriendTopic newInstance(int fgid) {
        FragmentFriendTopic newFragment = new FragmentFriendTopic();
        Bundle bundle = new Bundle();
        bundle.putInt("fgid", fgid);
        newFragment.setArguments(bundle);
        return newFragment;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_friend, null);
        unbinder = ButterKnife.bind(this, view);
//        EventBus.getDefault().register(this);
//        setData();
//        setListener();
//        uid = getActivity().getIntent().getStringExtra("uid");
        isPrepared = true;
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //mGroupSearchKeyWord_et_sou.setOnEditorActionListener(this);
        mGroupSearchKeyWord_et_sou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchfriendsActivity.class);
                startActivity(intent);
            }
        });
        lazyLoad();
        return view;
    }

    private void setData() {
        mFragmentFriendListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentFriendListview.setFocusable(false);
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentFriendListview.setRefreshing();
            }
        }, 100);
    }

    private void setListener() {
        mFragmentFriendListview.setOnRefreshListener(this);
        mFragmentFriendListview.setOnItemClickListener(this);
        mFragmentFriendListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isCanLoad) {
                                isCanLoad = false;
                                if (isReresh) {
                                    page = page + 1;
                                    getFriendList();
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

    private void getFriendList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fgid", fgid + "");
        map.put("page", page + "");
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.getgfuserslist, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fragmentfriend", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FenzuListData data = new Gson().fromJson(response, FenzuListData.class);
                            if (data.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    isReresh = false;
                                } else {
                                    layoutNormalEmpty.setVisibility(View.VISIBLE);
                                }
                                //ToastUtil.show(getActivity().getApplicationContext(), "没有更多");
                            } else {
                                isReresh = true;
                                layoutNormalEmpty.setVisibility(View.GONE);
                                if (page == 0) {
                                    datas.addAll(data.getData());
                                    int retcode = data.getRetcode();
                                    adapter = new FenzuListviewAdapter(getActivity(), datas, retcode, 0);
                                    mFragmentFriendListview.setAdapter(adapter);
                                } else {
                                    datas.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        isCanLoad = true;
                        mFragmentFriendListview.onRefreshComplete();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), PesonInfoActivity.class);
        intent.putExtra("uid", datas.get(position - 1).getUid());
        startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        name = "";
        datas.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(getActivity(), mFragmentFriendListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFriendList();
            }
        }, 100);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFriendList();
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
            Bundle args = getArguments();
            if (args != null) {
                fgid = args.getInt("fgid");
            }
            setData();
            setListener();
            uid = getActivity().getIntent().getStringExtra("uid");
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
            name = mGroupSearchKeyWord_et_sou.getText().toString().trim();
            getFriendList();
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
//            getFriendList();
//        }
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
}
