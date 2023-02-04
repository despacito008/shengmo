package com.aiwujie.shengmo.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.AllCommentAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.AllCommentData;
import com.aiwujie.shengmo.bean.UnreadMessageData;
import com.aiwujie.shengmo.eventbus.ClearDynamicNewCount;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.aiwujie.shengmo.view.OperateCommentPopup;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.android.percent.support.PercentLinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DynamicMessageActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener{

    @BindView(R.id.mDynamicMessage_return)
    ImageView mDynamicMessageReturn;
    Handler handler = new Handler();
    @BindView(R.id.mDynamic_message_listview)
    PullToRefreshListView mDynamicMessageListview;
    TextView mDynamicMessageZancount;
    PercentLinearLayout mDynamicMessageLlZan;
    TextView mDynamicMessageDscount;
    PercentLinearLayout mDynamicMessageLlDs;
    TextView mDynamicMessageAtcount;
    PercentLinearLayout mDynamicMessageLlAt;
    View viewZan,viewReword,viewRecommend,viewAt;
    private List<AllCommentData.DataBean> comments = new ArrayList<>();
    private AllCommentAdapter commentAdapter;
    int page = 0;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh=true;
    private AllCommentData allCommentData;
    private UnreadMessageData unreadMessageData;
    private int atcount;
    private PercentLinearLayout mDynamic_message_ll_at_tuiding;
    private TextView mDynamic_tuiding_atcount;
    private TextView mDynamic_message_tvHeadview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_message);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        setData();
        setListener();
    }

    private void setData() {
        View view = View.inflate(this, R.layout.item_dynamic_messageheadview, null);
        mDynamicMessageZancount= (TextView) view.findViewById(R.id.mDynamic_message_zancount);
        mDynamic_tuiding_atcount = view.findViewById(R.id.mDynamic_tuiding_atcount);
        //mDynamicMessageLlZan= (PercentLinearLayout) view.findViewById(R.id.mDynamic_message_ll_zan);
        mDynamicMessageDscount= (TextView) view.findViewById(R.id.mDynamic_message_dscount);
        //mDynamicMessageLlDs= (PercentLinearLayout) view.findViewById(R.id.mDynamic_message_ll_ds);
        mDynamicMessageAtcount= (TextView) view.findViewById(R.id.mDynamic_message_atcount);
        //mDynamicMessageLlAt= (PercentLinearLayout) view.findViewById(R.id.mDynamic_message_ll_at_mined);
        //mDynamic_message_ll_at_tuiding = view.findViewById(R.id.mDynamic_message_ll_at_tuiding);
        mDynamic_message_tvHeadview2 = view.findViewById(R.id.mDynamic_message_tvHeadview2);
        mDynamicMessageListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mDynamicMessageListview.getRefreshableView().addHeaderView(view);
        mDynamicMessageListview.setOnRefreshListener(this);
        mDynamicMessageListview.setOnItemClickListener(this);

        commentAdapter = new AllCommentAdapter(DynamicMessageActivity.this, comments);
        commentAdapter.setOnCommentListener(new AllCommentAdapter.OnItemCommentListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(DynamicMessageActivity.this, DynamicDetailActivity.class);
                intent.putExtra("uid", comments.get(position).getDuid());
                intent.putExtra("did", comments.get(position).getDid());
                intent.putExtra("pos", position);
                intent.putExtra("showwhat", 1);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position) {

            }

            @Override
            public void onItemApply(int position) {

            }
        });
        mDynamicMessageListview.setAdapter(commentAdapter);

        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDynamicMessageListview.setRefreshing();
            }
        }, 100);
        mDynamicMessageListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if(IsListviewSlideBottom.isListViewReachBottomEdge(absListView)){
                            if(isReresh) {
                                page = page + 1;
                                getAllComment();
                            }
                        }
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {}
        });

        viewZan = view.findViewById(R.id.view_message_thumb_bg);
        viewReword = view.findViewById(R.id.view_message_reword_bg);
        viewRecommend = view.findViewById(R.id.view_message_recommend_bg);
        viewAt = view.findViewById(R.id.view_message_alite_bg);
    }
    private void setListener() {
        viewRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DynamicMessageActivity.this, AllTuidingActivity.class);
                startActivity(intent);
            }
        });
        viewZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DynamicMessageActivity.this, AllZanActivity.class);
                startActivity(intent);
            }
        });
        viewReword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DynamicMessageActivity.this, AllRewardActivity.class);
                startActivity(intent);
            }
        });
        viewAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DynamicMessageActivity.this,AtMinedActivity.class);
                startActivity(intent);
            }
        });
    }
    
    private void getAllComment() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetCommentedList, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            allCommentData = new Gson().fromJson(response, AllCommentData.class);
                            if (allCommentData.getData().size() == 0) {
                                if (page != 0) {
                                    isReresh=false;
                                    page = page - 1;
                                    ToastUtil.show(getApplicationContext(), "没有更多");
                                }else{
                                    comments.addAll(allCommentData.getData());
                                   commentAdapter.notifyDataSetChanged();
                                }

                            } else {
                                isReresh=true;
                                if (page == 0) {
                                    comments.addAll(allCommentData.getData());
                                    try {
                                        commentAdapter.notifyDataSetChanged();
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    comments.addAll(allCommentData.getData());
                                    commentAdapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        mDynamicMessageListview.onRefreshComplete();
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
    private void getAtNum(){
        String atjson= (String) SharedPreferencesUtils.getParam(getApplicationContext(),"atjson","");

            if(!atjson.equals("")) {
                atcount=atjson.split("@-@").length;
            }else{
                atcount=0;
                mDynamicMessageAtcount.setTextColor(0xff999999);
            }
            mDynamicMessageAtcount.setVisibility(View.VISIBLE);
            mDynamicMessageAtcount.setText("刚刚有" + atcount + "个人@过我");

    }
    private void getUnreadNum() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("type", "0");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetUnreadNum, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            unreadMessageData = new Gson().fromJson(response, UnreadMessageData.class);

                                mDynamicMessageZancount.setVisibility(View.VISIBLE);
                                if ( unreadMessageData.getData().getLaudnum().equals("0")){
                                    mDynamicMessageZancount.setTextColor(0xff999999);
                                }
                                mDynamicMessageZancount.setText("刚刚有" + unreadMessageData.getData().getLaudnum() + "个人赞过我");
                            if ( unreadMessageData.getData().getTopnum().equals("0")){
                                mDynamic_tuiding_atcount.setTextColor(0xff999999);
                            }
                            mDynamic_tuiding_atcount.setText("刚刚有" + unreadMessageData.getData().getTopnum() + "个人推顶过我");
                            if ( unreadMessageData.getData().getRewardnum().equals("0")){
                                mDynamicMessageDscount.setTextColor(0xff999999);
                            }
                                mDynamicMessageDscount.setVisibility(View.VISIBLE);
                                mDynamicMessageDscount.setText("刚刚有" + unreadMessageData.getData().getRewardnum() + "个人打赏过我");

                            if ( unreadMessageData.getData().getComnum().equals("0")){
                                mDynamic_message_tvHeadview2.setTextColor(0xff999999);
                            }
                            mDynamic_message_tvHeadview2.setVisibility(View.VISIBLE);
                            mDynamic_message_tvHeadview2.setText("刚刚有" + unreadMessageData.getData().getComnum() + "条评论");

                        }catch (JsonSyntaxException e){
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

    private void clearUnreadNum() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.ClearUnreadNumAll, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("clearpinglun", "onSuccess: " + response);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }

    @OnClick({R.id.mDynamicMessage_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mDynamicMessage_return:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUnreadNum();
        getAtNum();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UpLocationUtils.LogintimeAndLocation();
       // clearUnreadNum();
        SharedPreferencesUtils.setParam(getApplicationContext(),"dongtaixiaoxirednum",0);
        SharedPreferencesUtils.setParam(getApplicationContext(),"atjson","");
        EventBus.getDefault().post(new ClearDynamicNewCount());
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        comments.clear();
        if (commentAdapter != null) {
            commentAdapter.notifyDataSetChanged();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllComment();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllComment();
            }
        }, 300);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position < 2 ) {
            return;
        }
        Intent intent = new Intent(this, DynamicDetailActivity.class);
        intent.putExtra("uid", comments.get(position - 2).getDuid());
        intent.putExtra("did", comments.get(position - 2).getDid());
        intent.putExtra("pos", position - 2);
        intent.putExtra("showwhat", 1);
        startActivity(intent);
    }

}
