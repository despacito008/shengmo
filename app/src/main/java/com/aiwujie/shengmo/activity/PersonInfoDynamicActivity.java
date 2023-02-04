package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.DynamicPersonMsgListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.DynamicAndTopicCountData;
import com.aiwujie.shengmo.bean.DynamicListData;
import com.aiwujie.shengmo.eventbus.CallBackEvent;
import com.aiwujie.shengmo.eventbus.DynamicTopEvent;
import com.aiwujie.shengmo.eventbus.FollowEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.android.percent.support.PercentLinearLayout;
import com.zhy.autolayout.AutoLinearLayout;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonInfoDynamicActivity extends AppCompatActivity implements View.OnTouchListener, PullToRefreshBase.OnRefreshListener2, View.OnClickListener {

    @BindView(R.id.mPersonInfoDynamic_return)
    ImageView mPersonInfoDynamicReturn;
    @BindView(R.id.mPersonInfoDynamic_title_name)
    TextView mPersonInfoDynamicTitleName;
    @BindView(R.id.mPersonInfoDynamic_listview)
    PullToRefreshListView mPersonInfoDynamicListview;
    private int page = 0;
    List<DynamicListData.DataBean> dynamics = new ArrayList<>();
    private DynamicPersonMsgListviewAdapter dynamicAdapter;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;
    private Handler handler = new Handler();
    private int friendRetcode;
    private String uid;
    private TimeSecondUtils refresh;
    String lastid = "";

    TextView mPersonInfoDynamicCount;
    TextView mPersonInfoRecommendCount;
    TextView mPersonInfoCommentCount;
    TextView mPersonInfoLaudCount;
    TextView mPersonInfoSendTvTopic;
    PercentLinearLayout mPersonInfoLlSendTopic;
    TextView mPersonInfoJoinTvTopic;
    PercentLinearLayout mPersonInfoLlJoinTopic;
    private TextView mPersonInfoDynamicLine;
    private String colors[] = {"#ff0000", "#b73acb", "#0000ff", "#18a153", "#f39700", "#ff00ff", "#00a0e9"};
    private String colorss[] = {"#18a153", "#f39700", "#ff00ff", "#00a0e9", "#ff0000", "#b73acb", "#0000ff"};
    private PercentLinearLayout mPersonInfoLlDynamic;
    private AutoLinearLayout mPersonInfoDynamicBottomLl;
    private TextView mPersonInfoDynamicBottomTv;
    private TextView mPersonInfodynamicheadertopcard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info_dynamic);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        EventBus.getDefault().register(this);
        setData();
        setListener();
    }

    private void setData() {
        uid = getIntent().getStringExtra("uid");
        mPersonInfoDynamicTitleName.setText(getIntent().getStringExtra("nickname"));
        View view = View.inflate(this, R.layout.item_person_info_dynamic_header, null);
        mPersonInfodynamicheadertopcard = view.findViewById(R.id.mPersonInfo_dynamic_header_topcard);
        mPersonInfoDynamicCount = (TextView) view.findViewById(R.id.mPersonInfo_dynamic_header_dynamicCount);
        mPersonInfoRecommendCount = (TextView) view.findViewById(R.id.mPersonInfo_dynamic_header_recommendCount);
        mPersonInfoCommentCount = (TextView) view.findViewById(R.id.mPersonInfo_dynamic_header_commentCount);
        mPersonInfoLaudCount = (TextView) view.findViewById(R.id.mPersonInfo_dynamic_header_laudCount);
//        mPersonInfoRewardCount = (TextView) view.findViewById(R.id.mPersonInfo_dynamic_header_rewardCount);
        mPersonInfoSendTvTopic = (TextView) view.findViewById(R.id.mPersonInfo_dynamic_sendTvTopic);
        mPersonInfoLlSendTopic = (PercentLinearLayout) view.findViewById(R.id.mPersonInfo_dynamic_ll_sendTopic);
        mPersonInfoJoinTvTopic = (TextView) view.findViewById(R.id.mPersonInfo_dynamic_joinTvTopic);
        mPersonInfoLlJoinTopic = (PercentLinearLayout) view.findViewById(R.id.mPersonInfo_dynamic_ll_joinTopic);
//        mPersonInfoDynamicLineOne = (TextView) view.findViewById(R.id.mPersonInfo_dynamic_line01);
        mPersonInfoDynamicLine = (TextView) view.findViewById(R.id.mPersonInfo_dynamic_line);
        mPersonInfoLlDynamic = (PercentLinearLayout) view.findViewById(R.id.mPersonInfo_dynamic_header_ll);
        mPersonInfoDynamicBottomLl = (AutoLinearLayout) view.findViewById(R.id.mPersonInfo_dynamic_bottom_ll);
        mPersonInfoDynamicBottomTv = (TextView) view.findViewById(R.id.mPersonInfo_dynamic_bottom_tv);
        mPersonInfoLlSendTopic.setOnClickListener(this);
        mPersonInfoLlJoinTopic.setOnClickListener(this);
        mPersonInfoDynamicListview.getRefreshableView().addHeaderView(view);
        //动态和话题统计的接口
        getDynamicAndTopicCount();
        //是否是朋友
        isFriend();
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPersonInfoDynamicListview.setRefreshing();
            }
        }, 100);
    }


    private void getDynamicAndTopicCount() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetDynamicAndTopicCount, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("getDynamicAndTopicCount", "onSuccess: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DynamicAndTopicCountData data = new Gson().fromJson(response, DynamicAndTopicCountData.class);
                            if (data.getData().getDynamiccount().getDynamicnum() != 0) {
                                //显示
                                mPersonInfoLlDynamic.setVisibility(View.VISIBLE);
                                mPersonInfoLlDynamic.animate().alpha(1).setStartDelay(150).start();
                                mPersonInfodynamicheadertopcard.setText(data.getData().getDynamiccount().getTopnum()+"");
                                mPersonInfoDynamicCount.setText(data.getData().getDynamiccount().getDynamicnum() + "");
                                mPersonInfoRecommendCount.setText(data.getData().getDynamiccount().getRecommend() + "");
                                mPersonInfoCommentCount.setText(data.getData().getDynamiccount().getComnum() + "");
                                mPersonInfoLaudCount.setText(data.getData().getDynamiccount().getLaudnum() + "");
//                                mPersonInfoRewardCount.setText(data.getData().getDynamiccount().getRewardnum() + "");
                            } else {
                                //隐藏
                                mPersonInfoLlDynamic.setVisibility(View.GONE);
                            }
                            if (data.getData().getC_topic().size() != 0) {
                                mPersonInfoLlSendTopic.setVisibility(View.GONE);
                                SpannableStringBuilder builders = new SpannableStringBuilder();
                                for (int i = 0; i < data.getData().getC_topic().size(); i++) {
                                    ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor(colors[i]));
                                    SpannableStringBuilder builder = new SpannableStringBuilder("#" + data.getData().getC_topic().get(i).getTitle() + "#");
                                    builder.setSpan(purSpan, 0, data.getData().getC_topic().get(i).getTitle().length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    builders = builders.append(builder);
                                }
                                mPersonInfoSendTvTopic.setText(builders);
                            } else {
                                mPersonInfoLlSendTopic.setVisibility(View.GONE);
                            }
                            if (data.getData().getJ_topic().size() != 0) {
                                mPersonInfoLlJoinTopic.setVisibility(View.VISIBLE);
                                SpannableStringBuilder builders = new SpannableStringBuilder();
                                for (int i = 0; i < data.getData().getJ_topic().size(); i++) {
                                    ForegroundColorSpan purSpan = new ForegroundColorSpan(Color.parseColor(colorss[i]));
                                    SpannableStringBuilder builder = new SpannableStringBuilder("#" + data.getData().getJ_topic().get(i).getTitle() + "#");
                                    builder.setSpan(purSpan, 0, data.getData().getJ_topic().get(i).getTitle().length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    builders.append(builder);
                                }
                                mPersonInfoJoinTvTopic.setText(builders);
                            } else {
                                mPersonInfoLlJoinTopic.setVisibility(View.GONE);
                            }

                            if (data.getData().getC_topic().size() == 0 && data.getData().getJ_topic().size() == 0) {
                                mPersonInfoDynamicLine.setVisibility(View.GONE);
                            }

                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setListener() {
        mPersonInfoDynamicListview.setOnRefreshListener(this);
        mPersonInfoDynamicListview.setOnTouchListener(this);
        mPersonInfoDynamicListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isReresh) {
                                page = page + 1;
                                lastid = dynamics.get(0).getDid();
                                getDynamicList();
                            }
                        }  else {
                            int first = absListView.getFirstVisiblePosition();
                            int last = absListView.getLastVisiblePosition();
                            for (int j = last; j >= first; j--) {
                                if(j > 1 && dynamics.size() > j - 2 && !TextUtil.isEmpty(dynamics.get(j - 2).getPlayUrl().trim())) {
                                    dynamicAdapter.tryToPlayVideo(absListView,j - first,dynamics.get(j - 2).getPlayUrl().trim());
                                    return;
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

    @OnClick(R.id.mPersonInfoDynamic_return)
    public void onViewClicked() {
        finish();
    }

    private void getDynamicList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("type", "3");
        map.put("page", page + "");
        map.put("loginuid", MyApp.uid);
        map.put("lastid", lastid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetDynamicListNewFive, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("fragmentDynamichot", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
                            if (uid.equals(MyApp.uid)) {
                                setAdapter(response);
                            } else {
                                if (vip.equals("1")) {
                                    setAdapter(response);
                                } else {
                                    if (friendRetcode == 2001) {
                                        setAdapter(response);
                                    } else {
                                        setAdapter(response);//所有人可见

//                                        isReresh = false;
//                                        dynamicAdapter = new DynamicPersonMsgListviewAdapter(PersonInfoDynamicActivity.this, dynamics, 0);
//                                        mPersonInfoDynamicListview.setAdapter(dynamicAdapter);
//                                        mPersonInfoDynamicBottomLl.setVisibility(View.VISIBLE);
//                                        mPersonInfoDynamicBottomTv.setMovementMethod(LinkMovementMethod.getInstance());
//                                        TextMoreClickUtils clickUtils=new TextMoreClickUtils(PersonInfoDynamicActivity.this,uid,handler);
//                                        mPersonInfoDynamicBottomTv.setText(clickUtils.addClickablePart("互为好友、VIP会员","TA的动态限","可见"), TextView.BufferType.SPANNABLE);
                                    }
                                }
                            }
                            mPersonInfoDynamicListview.onRefreshComplete();
                            if (refresh != null) {
                                refresh.cancel();
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    //展示数据
    private void setAdapter(String response) {
        DynamicListData listData = new Gson().fromJson(response, DynamicListData.class);
        if (listData != null) {
            if (listData.getData().size() == 0) {
                if (page != 0) {
                    page = page - 1;
                    ToastUtil.show(getApplicationContext(), "没有更多");
                    isReresh = false;
                } else {
                    dynamicAdapter = new DynamicPersonMsgListviewAdapter(PersonInfoDynamicActivity.this, dynamics, 0,"3");
                    mPersonInfoDynamicListview.setAdapter(dynamicAdapter);
                }
            } else {
                isReresh = true;
                if (page == 0) {
                    dynamics.addAll(listData.getData());
                    int retcode = listData.getRetcode();
                    try {
                        dynamicAdapter = new DynamicPersonMsgListviewAdapter(PersonInfoDynamicActivity.this, dynamics, retcode,"3");
                        mPersonInfoDynamicListview.setAdapter(dynamicAdapter);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    dynamics.addAll(listData.getData());
                    dynamicAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void isFriend() {
        Map<String, String> map = new HashMap<>();
        map.put("login_uid", MyApp.uid);
        map.put("uid", uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetAchievePower, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    friendRetcode = object.getInt("retcode");
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
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        dynamics.clear();
        if (dynamicAdapter != null) {
            dynamicAdapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(this, mPersonInfoDynamicListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastid = "";
                getDynamicList();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    //置顶动态刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(DynamicTopEvent event) {
        page = 0;
        dynamics.clear();
        lastid = "";
        getDynamicList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void backEventBus(CallBackEvent event){
        page = 0;
        dynamics.clear();
        if (dynamicAdapter != null) {
            dynamicAdapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(this, mPersonInfoDynamicListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastid = "";
                getDynamicList();
            }
        }, 300);
    }

    //互为好友更新列表数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(FollowEvent event) {
        if (event.getFollowState().equals("3")) {
            mPersonInfoDynamicBottomLl.setVisibility(View.GONE);
            //是否是朋友
            isFriend();
            //关注以后动态
            lastid = "";
            getDynamicList();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mPersonInfo_dynamic_ll_sendTopic:
            case R.id.mPersonInfo_dynamic_ll_joinTopic:
                Intent intent = new Intent(this, MyOrOtherTopicActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
