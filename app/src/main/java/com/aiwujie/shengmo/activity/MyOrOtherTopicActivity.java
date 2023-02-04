package com.aiwujie.shengmo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.TopicAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.ListTopicData;
import com.aiwujie.shengmo.eventbus.TopicViewIsVisibleEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.ListviewOntouchListener;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
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
import butterknife.OnClick;

public class MyOrOtherTopicActivity extends AppCompatActivity implements AdapterView.OnItemClickListener ,PullToRefreshBase.OnRefreshListener2 {

    @BindView(R.id.mMyOrOtherTopic_return)
    ImageView mMyOrOtherTopicReturn;
    @BindView(R.id.mMyOrOtherTopic_title)
    TextView mMyOrOtherTopicTitle;
    @BindView(R.id.mFragment_topic_listview)
    PullToRefreshListView mFragmentTopicListview;
    /*@BindView(R.id.mMyOrOtherTopic_tabs)
    TabLayout mMyOrOtherTopicTabs;
    @BindView(R.id.mMyOrOtherTopic_viewpager)
    ViewPager mMyOrOtherTopicViewpager;*/
   /* @BindView(R.id.mMyOrOtherTopic_sendTopic)
    ImageView mMyOrOtherTopicSendTopic;*/
    private int topicflag;
    Handler handler = new Handler();
    int page = 0;
    int topicType=9;
    int topicState=0;
    private ListTopicData listData;
    private TopicAdapter topicAdapter;
    List<ListTopicData.DataBean> topicDatas=new ArrayList<>();
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;
    private boolean isCanLoad=true;
    private TimeSecondUtils refresh;
    private String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_or_other_topic);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        userid = getIntent().getStringExtra("uid");
        if (userid.equals(MyApp.uid)) {
            mMyOrOtherTopicTitle.setText("参与话题");
        } else {
            mMyOrOtherTopicTitle.setText("Ta的话题");
        }
        //setData();
        topicflag= getIntent().getIntExtra("topicflag",-1);
        setData();
        setListener();
    }

    private void setData() {
        mFragmentTopicListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFragmentTopicListview.setRefreshing();
            }
        }, 100);
    }

    private void setListener() {
        mFragmentTopicListview.getRefreshableView().setOnTouchListener(new ListviewOntouchListener(0));
        mFragmentTopicListview.setOnRefreshListener(this);
        mFragmentTopicListview.setOnItemClickListener(this);
        mFragmentTopicListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isCanLoad) {
                                isCanLoad = false;
                                if (isReresh) {
                                    page = page + 1;
                                    getTopicList();
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

    private void getTopicList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", userid);
        map.put("page", page + "");
        if(topicType==0){
            map.put("type","1");
        }else if(topicType==8){
            map.put("type","2");
        }else if(topicType==9){
            map.put("type","3");
        }else{
            map.put("type","0");
            map.put("pid",topicState+"");
        }
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetTopicList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("fragmenttopic", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            listData = new Gson().fromJson(response, ListTopicData.class);
                            if (listData.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    isReresh=false;
                                    ToastUtil.show(getApplicationContext(), "没有更多");
                                }
                            } else {
                                isReresh=true;
                                if (page == 0) {
                                    topicDatas.addAll(listData.getData());
                                    try {
                                        topicAdapter = new TopicAdapter(MyOrOtherTopicActivity.this, topicDatas);
                                        mFragmentTopicListview.setAdapter(topicAdapter);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    topicDatas.addAll(listData.getData());
                                    topicAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        isCanLoad=true;
                        mFragmentTopicListview.onRefreshComplete();
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

/*    private void setData() {
        //mMyOrOtherTopicSendTopic.animate().alpha(1).setStartDelay(500).start();
        //添加页卡标题
        mTitleList.add("发布的话题");
        mTitleList.add("参与的话题");
        //添加页卡视图
        mViewList.add(FragmentTopic.newInstance(0, 8));
        mViewList.add(FragmentTopic.newInstance(0, 9));
        mMyOrOtherTopicTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mMyOrOtherTopicTabs.addTab(mMyOrOtherTopicTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mMyOrOtherTopicTabs.addTab(mMyOrOtherTopicTabs.newTab().setText(mTitleList.get(1)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(), mTitleList, mViewList);
        mMyOrOtherTopicViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mMyOrOtherTopicTabs.setupWithViewPager(mMyOrOtherTopicViewpager);//将TabLayout和ViewPager关联起来。
        //修改下划线的长度
        mMyOrOtherTopicTabs.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineWidthUtils.setIndicator(mMyOrOtherTopicTabs, 50, 50);
            }
        });
    }*/

    @OnClick({R.id.mMyOrOtherTopic_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mMyOrOtherTopic_return:
                finish();
                break;
         /*   case R.id.mMyOrOtherTopic_sendTopic:
                Intent intent = new Intent(this, SendTopicActivity.class);
                startActivity(intent);
                break;*/
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(TopicViewIsVisibleEvent event) {
        switch (event.getIsVisible()) {
            case 0:
              /*  if (mMyOrOtherTopicSendTopic.getVisibility() == View.VISIBLE) {
                    mMyOrOtherTopicSendTopic.setVisibility(View.GONE);
                    mMyOrOtherTopicSendTopic.setAnimation(AnimationUtil.moveToViewBottom());
                }*/
                break;
            case 1:
              /*  if (mMyOrOtherTopicSendTopic.getVisibility() == View.GONE) {
                    mMyOrOtherTopicSendTopic.setVisibility(View.VISIBLE);
                    mMyOrOtherTopicSendTopic.setAnimation(AnimationUtil.moveToViewLocation());
                }*/
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(topicflag==-1) {
            Intent intent = new Intent(this, TopicDetailActivity.class);
            intent.putExtra("tid",topicDatas.get(position-1).getTid());
            intent.putExtra("topictitle",topicDatas.get(position-1).getTitle());
            intent.putExtra("topicState",topicState);
            startActivity(intent);
        }else{
            if(topicDatas.get(position-1).getIs_admin().equals("0")) {
                Intent intent = new Intent();
                intent.putExtra("topicid", topicDatas.get(position - 1).getTid());
                intent.putExtra("topictitle", topicDatas.get(position - 1).getTitle());
                intent.putExtra("topicState",topicState);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }else{
                ToastUtil.show(this.getApplicationContext(),"官方话题不允许参与哦~");
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        topicDatas.clear();
        if(topicAdapter!=null){
            topicAdapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(this, mFragmentTopicListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getTopicList();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }
}
