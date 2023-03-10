package com.aiwujie.shengmo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.GzFsHyListviewAdapter;
import com.aiwujie.shengmo.adapter.SouHyListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GzFsHyListviewData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchfriendsActivity extends AppCompatActivity implements TextView.OnEditorActionListener,AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2{

    private ImageView mSee_return;
    private EditText mGroupSearchKeyWord_et_sou;
    private PullToRefreshListView mFragmentFollowListview;
    private String type = "1";
    private int page = 0;
    private ArrayList<GzFsHyListviewData.DataBean> datas = new ArrayList<>();
    Handler handler = new Handler();
    private String uid;
    //private SouHyListviewAdapter adapter;
    private GzFsHyListviewAdapter adapter;
    private int who=0;
    //????????????
    private boolean permissons;
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
    private boolean isReresh=true;
    private boolean isCanLoad=true;
    String name="";
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchfriends);
        StatusBarUtil.showLightStatusBar(this);
        isPrepared = true;
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        type = intent.getStringExtra("type");
        mSee_return = (ImageView) findViewById(R.id.mSee_return);
        mGroupSearchKeyWord_et_sou = (EditText) findViewById(R.id.mGroupSearchKeyWord_et_sou);
        mGroupSearchKeyWord_et_sou.setOnEditorActionListener(this);
        mGroupSearchKeyWord_et_sou.setText(name+"");
        mFragmentFollowListview = (PullToRefreshListView) findViewById(R.id.mFragment_follow_listview);
        setData();
        setListener();

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*???????????????*/
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(
                        v.getApplicationWindowToken(), 0);
            }

            datas.clear();
            page=0;
            name=mGroupSearchKeyWord_et_sou.getText().toString().trim();
            getFollewingList();
            return true;
        }
        return false;
    }

    private void setData() {
        mFragmentFollowListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFragmentFollowListview.setFocusable(false);
        //??????????????????
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentFollowListview.setRefreshing();
            }
        },100);
        String permisson = "android.permission.ACCESS_COARSE_LOCATION";
        permissons = ContextCompat.checkSelfPermission(getApplicationContext(), permisson) == PackageManager.PERMISSION_DENIED;
    }

    private void setListener() {
        mFragmentFollowListview.setOnRefreshListener(this);
        mFragmentFollowListview.setOnItemClickListener(this);
        mFragmentFollowListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if(IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
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
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
        });
    }

    private void getFollewingList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("login_uid", MyApp.uid);
        map.put("page", page + "");
        map.put("type", type);
        map.put("name", name);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetFollewingList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fragmentfollow", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            GzFsHyListviewData data = new Gson().fromJson(response, GzFsHyListviewData.class);
                            if(data.getRetcode()==4002){
                                ToastUtil.show(getApplicationContext(), "??????????????????????????????");
                            }
                            if (data.getData().size() == 0) {
                                if (permissons) {
                                    ToastUtil.show(getApplicationContext(), "??????????????????????????????");
                                } else {
                                    if(page!=0) {
                                        page = page - 1;
                                        isReresh=false;
                                        ToastUtil.show(getApplicationContext(), "????????????");
                                    }
                                }
                            } else {
                                isReresh=true;
                                if (page == 0) {
                                    datas.addAll(data.getData());
                                    try {
                                        adapter = new GzFsHyListviewAdapter(SearchfriendsActivity.this, datas, who, type,name);
                                        mFragmentFollowListview.setAdapter(adapter);
                                    }catch (NullPointerException e){
                                        e.printStackTrace();
                                    }
                                } else {
                                    datas.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        isCanLoad=true;
                        mFragmentFollowListview.onRefreshComplete();
                        if(refresh!=null) {
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
        Intent intent = new Intent(this, PesonInfoActivity.class);
        intent.putExtra("uid", datas.get(position-1).getUid());
        startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        datas.clear();
        //name="";
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        refresh=new TimeSecondUtils(this,mFragmentFollowListview);
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
}
