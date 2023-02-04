package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.AllAtAdapter;
import com.aiwujie.shengmo.adapter.AtMineAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.AtUserData;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.widget.LinearLayout.SHOW_DIVIDER_NONE;

public class AtMinedActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.mAt_mined_return)
    ImageView mAtMinedReturn;
    @BindView(R.id.mAt_mined_listview)
    PullToRefreshListView mAtMinedListview;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    // List<AtData> atDatas=new ArrayList<>();
    private AtMineAdapter atMineAdapter;
    Handler handler = new Handler();
    int page = 0;
    private boolean isReresh;
    AllAtAdapter atAdapter;
    private List<AtUserData.DataBean> rewards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_mined);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
//        String atjson= (String) SharedPreferencesUtils.getParam(getApplicationContext(),"atjson","");
//        if(!atjson.equals("")){
//            if(atjson.contains("@-@")) {
//                String[] atArray = atjson.split("@-@");
//                for (int i = atArray.length-1; i >=0 ; i--) {
//                    atDatas.add(new Gson().fromJson(atArray[i], AtData.class));
//                }
//            }else{
//                atDatas.add(new Gson().fromJson(atjson,AtData.class));
//            }
//            atMineAdapter=new AtMineAdapter(this,atDatas);
//            mAtMinedListview.setAdapter(atMineAdapter);
//        }else{
//            ToastUtil.show(getApplicationContext(),"没有人@你哦");
//        }
        getAtData();
        mAtMinedListview.setShowDividers(SHOW_DIVIDER_NONE);
        mAtMinedListview.setOnItemClickListener(this);
        setListener();
    }

    @OnClick(R.id.mAt_mined_return)
    public void onClick() {
        finish();
    }

    public void getAtData() {
        HttpHelper.getInstance().getAtUsData(page, new HttpListener() {
            @Override
            public void onSuccess(String data) {
                try {
                    AtUserData listData = new Gson().fromJson(data, AtUserData.class);
                    if (listData.getData().size() == 0) {
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
                            rewards.addAll(listData.getData());
                            atAdapter = new AllAtAdapter(AtMinedActivity.this, rewards);
                            mAtMinedListview.setAdapter(atAdapter);
                        } else {
                            rewards.addAll(listData.getData());
                            atAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                mAtMinedListview.onRefreshComplete();
            }

            @Override
            public void onFail(String msg) {
                mAtMinedListview.onRefreshComplete();
                if (page == 0) {
                    layoutNormalEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void setListener() {
        mAtMinedListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                rewards.clear();
                if (atAdapter != null) {
                    atAdapter.notifyDataSetChanged();
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAtData();
                    }
                }, 300);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = page + 1;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAtData();
                    }
                }, 500);
            }
        });
        mAtMinedListview.setOnItemClickListener(this);
        mAtMinedListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
                //visibleItemCount：当前能看见的列表项个数（小半个也算）
                //totalItemCount：列表项共数
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0 && visibleItemCount != totalItemCount) {
                    if (!mAtMinedListview.isShownHeader()) {
                        page = page + 1;
                        getAtData();
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DynamicDetailActivity.class);
        intent.putExtra("uid", MyApp.uid);
        intent.putExtra("did", rewards.get(position - 1).getDid());
        intent.putExtra("pos", position - 1);
        intent.putExtra("showwhat", 1);
        startActivity(intent);
//        Intent intent = new Intent(this, DynamicDetailActivity.class);
//        intent.putExtra("uid",rewards.get(position).getUid());
//        intent.putExtra("did", rewards.get(position).getDid());
//        intent.putExtra("pos", position);
//        intent.putExtra("showwhat", 1);
//        startActivity(intent);
//        rewards.remove(position);
//        atMineAdapter.notifyDataSetChanged();
//        String atjson="";
//        if(rewards.size()!=0) {
//            if(rewards.size()==1){
//                atjson=new Gson().toJson(rewards.get(0));
//            }else {
//                for (int i = 0; i < rewards.size(); i++) {
//                    atjson += new Gson().toJson(rewards.get(i)) + "@-@";
//                }
//                atjson = atjson.substring(0, atjson.length() - 3);
//            }
//            SharedPreferencesUtils.setParam(getApplicationContext(),"atjson",atjson);
//        }else{
//            SharedPreferencesUtils.setParam(getApplicationContext(),"atjson","");
//        }
//        EventBus.getDefault().post(new AtSomeOneEvent(1));
    }

}
