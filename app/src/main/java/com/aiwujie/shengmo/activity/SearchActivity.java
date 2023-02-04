package com.aiwujie.shengmo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.HomeListviewAdapter;
import com.aiwujie.shengmo.adapter.HomeUserListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.HomeNewListData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity implements TextView.OnEditorActionListener, PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.mSearch_return)
    ImageView mSearchReturn;
    @BindView(R.id.mSearch_iv_search)
    ImageView mSearchIvSearch;
    @BindView(R.id.mSearch_et_search)
    EditText mSearchEtSearch;
    @BindView(R.id.mSearch_et_llSearch)
    PercentLinearLayout mSearchEtLlSearch;
    @BindView(R.id.mSearch_listview)
    PullToRefreshListView mSearchListview;
    @BindView(R.id.iv_layout_normal_empty)
    ImageView ivLayoutNormalEmpty;
    @BindView(R.id.tv_layout_normal_empty)
    TextView tvLayoutNormalEmpty;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    private int page = 0;
    private List<HomeNewListData.DataBean> searchDatas = new ArrayList<>();
    private HomeUserListviewAdapter adapter;
    private InputMethodManager imm;
    Handler handler = new Handler();
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mSearchEtSearch.setText(getIntent().getStringExtra("search"));
        mSearchIvSearch.setVisibility(View.GONE);
        mSearchEtLlSearch.setVisibility(View.VISIBLE);
        mSearchEtSearch.setFocusable(true);
        mSearchEtSearch.requestFocus();
        setListener();
    }

    private void setListener() {
        mSearchListview.setOnItemClickListener(this);
        mSearchListview.setOnRefreshListener(this);
        mSearchEtSearch.setOnEditorActionListener(this);
        mSearchListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mSearchListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isReresh) {
                                page = page + 1;
                                searchUser();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!TextUtil.isEmpty(mSearchEtSearch.getText().toString())) {
                    mSearchListview.setRefreshing();
                }
            }
        }, 100);
    }

    @OnClick({R.id.mSearch_return, R.id.mSearch_iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mSearch_return:
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                finish();
                break;
            case R.id.mSearch_iv_search:
                mSearchIvSearch.setVisibility(View.GONE);
                mSearchEtLlSearch.setVisibility(View.VISIBLE);
                mSearchEtSearch.setFocusable(true);
                mSearchEtSearch.requestFocus();
                imm.showSoftInput(mSearchEtSearch, InputMethodManager.RESULT_SHOWN);
                break;
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
            mSearchListview.setRefreshing();
            return true;
        }
        return false;
    }

    private void searchUser() {
        Map<String, String> map = new HashMap<>();
        map.put("search", mSearchEtSearch.getText().toString().trim());
        map.put("page", page + "");
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.SearchUserNewth, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("searchData", "onSuccess: " + response);
                if (SafeCheckUtil.isActivityFinish(SearchActivity.this)) {
                    return;
                }
                try {
                    HomeNewListData data = new Gson().fromJson(response, HomeNewListData.class);
                    if (data == null || data.getData() == null) {
                        ToastUtil.show(getApplicationContext(), "没有更多");
                        return;
                    }
                    if (data.getData().size() == 0) {
                        if (page != 0) {
                            page = page - 1;
                            isReresh = false;
                            ToastUtil.show(getApplicationContext(), "没有更多");
                        } else {
                            layoutNormalEmpty.setVisibility(View.VISIBLE);
                        }
                    } else {
                        layoutNormalEmpty.setVisibility(View.GONE);
                        isReresh = true;
                        if (page == 0) {
                            int retcode = data.getRetcode();
                            searchDatas.addAll(data.getData());
                            try {
                                adapter = new HomeUserListviewAdapter(SearchActivity.this, searchDatas, retcode, 0);
                                mSearchListview.setAdapter(adapter);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        } else {
                            searchDatas.addAll(data.getData());
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                mSearchListview.onRefreshComplete();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        searchDatas.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchUser();
            }
        }, 100);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchUser();
            }
        }, 500);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PesonInfoActivity.class);
        intent.putExtra("uid", searchDatas.get(position - 1).getUid());
        startActivity(intent);
//        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }

}
