package com.aiwujie.shengmo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.FollowMsgAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.FollowMsgData;
import com.aiwujie.shengmo.customview.PercentLinearLayout;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FollowMsgActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.mFollowMsg_return)
    ImageView mFollowMsgReturn;
    @BindView(R.id.mFollowMsg_clear)
    TextView mFollowMsgClear;
    @BindView(R.id.mFollowMsg_listview)
    PullToRefreshListView mFollowMsgListview;
    @BindView(R.id.activity_follow_msg)
    PercentLinearLayout activityFollowMsg;
    private int page = 0;
    Handler handler = new Handler();
    List<FollowMsgData.DataBean> followMsgs=new ArrayList<>();
    private FollowMsgAdapter listviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_msg);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        setData();
        setListener();
    }

    private void setData() {
        mFollowMsgListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mFollowMsgListview.setFocusable(false);
        //实现自动刷新
        mFollowMsgListview.mHeaderLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int width = mFollowMsgListview.mHeaderLayout.getHeight();
                if (width > 0) {
                    mFollowMsgListview.setRefreshing();
                    mFollowMsgListview.mHeaderLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
    }

    private void setListener() {
        mFollowMsgListview.setOnRefreshListener(this);
        mFollowMsgListview.setOnItemClickListener(this);
//        mFollowMsgListview.setOnItemClickListener(this);
        mFollowMsgListview.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));
        mFollowMsgListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
                //visibleItemCount：当前能看见的列表项个数（小半个也算）
                //totalItemCount：列表项共数
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0&&visibleItemCount!=totalItemCount) {
                    if(!mFollowMsgListview.isShownHeader()) {
                        page = page + 1;
                        getFollowList();
                    }
                }
            }
        });
    }

    private void getFollowList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetFollowMessage, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("followmsgs", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FollowMsgData data = new Gson().fromJson(response, FollowMsgData.class);
                            if (data.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    ToastUtil.show(FollowMsgActivity.this.getApplicationContext(), "没有更多");
                                }
                            } else {
                                if (page == 0) {
                                    followMsgs.addAll(data.getData());
                                    listviewAdapter = new FollowMsgAdapter(FollowMsgActivity.this, followMsgs);
                                    mFollowMsgListview.setAdapter(listviewAdapter);
                                } else {
                                    followMsgs.addAll(data.getData());
                                    listviewAdapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        mFollowMsgListview.onRefreshComplete();
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @OnClick({R.id.mFollowMsg_return, R.id.mFollowMsg_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mFollowMsg_return:
                finish();
                break;
            case R.id.mFollowMsg_clear:
                if(followMsgs.size()!=0) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("确认清空吗?")
                            .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            clearMsg();
                        }
                    }).create().show();
                }
                break;
        }
    }

    private void clearMsg() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.DelFollowMessage, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    page = 0;
                                    followMsgs.clear();
                                    if(listviewAdapter!=null){
                                        listviewAdapter.notifyDataSetChanged();
                                    }
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 3000:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                            }
                        } catch (JSONException e) {
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
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        followMsgs.clear();
        if(listviewAdapter!=null){
            listviewAdapter.notifyDataSetChanged();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFollowList();
            }
        },300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PesonInfoActivity.class);
        intent.putExtra("uid", followMsgs.get(position-1).getUid());
        startActivity(intent);
    }
}
