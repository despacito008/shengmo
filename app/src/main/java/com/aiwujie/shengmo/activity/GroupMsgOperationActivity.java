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
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.GroupOperationAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GroupOperationData;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.aiwujie.shengmo.view.RefuseJoinGroupPop;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.gyf.immersionbar.ImmersionBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GroupMsgOperationActivity extends AppCompatActivity implements  PullToRefreshBase.OnRefreshListener2, View.OnClickListener {

    @BindView(R.id.mGroupOperation_return)
    ImageView mGroupOperationReturn;
    //    @BindView(R.id.mGroupOperation_listview)
//    MyListView mGroupOperationListview;
//    @BindView(R.id.mGroupOperation_scrollview)
//    PullToRefreshScrollView mGroupOperationScrollview;
    @BindView(R.id.mGroupOperation_clear)
    TextView mGroupOperationClear;
    @BindView(R.id.mGroupOperation_listview)
    PullToRefreshListView mGroupOperationListview;
    private String targetId;
    private int page = 0;
    Handler handler = new Handler();
    List<GroupOperationData.DataBean> operations = new ArrayList<>();
    private GroupOperationAdapter listviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_msg_operation);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this,R.id.rl_group_msg_operation_title_bar);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        targetId = getIntent().getStringExtra("targetId");
        setData();
        setListener();
    }

    private void setData() {
        mGroupOperationListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mGroupOperationListview.setFocusable(false);
//        ILoadingLayout endLabels = mGroupOperationListview.getLoadingLayoutProxy(
//                false, true);
//        endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
//        endLabels.setRefreshingLabel("正在加载");// 刷新时
//        endLabels.setReleaseLabel("松手加载");// 下来达到一定距离时，显示的提示
        //实现自动刷新
        mGroupOperationListview.mHeaderLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int width = mGroupOperationListview.mHeaderLayout.getHeight();
                if (width > 0) {
                    mGroupOperationListview.setRefreshing();
                    mGroupOperationListview.mHeaderLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
    }

    private void setListener() {
        mGroupOperationListview.setOnRefreshListener(this);
//        mGroupOperationListview.setOnItemClickListener(this);
        mGroupOperationListview.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));
        mGroupOperationReturn.setOnClickListener(this);
        mGroupOperationClear.setOnClickListener(this);
        mGroupOperationListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
                //visibleItemCount：当前能看见的列表项个数（小半个也算）
                //totalItemCount：列表项共数
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0&&visibleItemCount!=totalItemCount) {
                    if(!mGroupOperationListview.isShownHeader()) {
                        page = page + 1;
                        getGroupOperationList();
                    }
                }
            }
        });
    }

    private void getGroupOperationList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetGroupMsg, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("groupmsgoperation", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            GroupOperationData data = new Gson().fromJson(response, GroupOperationData.class);
                            if (data.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    ToastUtil.show(GroupMsgOperationActivity.this.getApplicationContext(), "没有更多");
                                }
                                if (listviewAdapter != null) {
                                    listviewAdapter.notifyDataSetChanged();
                                }
                            } else {
                                if (page == 0) {
                                    operations.addAll(data.getData());
                                    listviewAdapter = new GroupOperationAdapter(GroupMsgOperationActivity.this, operations);
                                    mGroupOperationListview.setAdapter(listviewAdapter);
                                } else {
                                    operations.addAll(data.getData());
                                    listviewAdapter.notifyDataSetChanged();
                                }
                            }
                            if (listviewAdapter != null) {
                                listviewAdapter.setOnConfirmClick(GroupMsgOperationActivity.this);
                                listviewAdapter.setOnRefuseClick(GroupMsgOperationActivity.this);
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        mGroupOperationListview.onRefreshComplete();
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (!operations.get(position).getOther_uid().equals("0")) {
//            Intent intent = new Intent(this, PesonInfoActivity.class);
//            intent.putExtra("uid", operations.get(position-1).getOther_uid());
//            startActivity(intent);
//        }
//    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        operations.clear();
        if(listviewAdapter!=null){
            listviewAdapter.notifyDataSetChanged();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getGroupOperationList();
            }
        },300);

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_group_operation_confirm:
                int pos = (int) v.getTag();
                v.setEnabled(false);
                confirmJoin(pos, (TextView) v);
                break;
            case R.id.item_group_operation_refuse:
                int pos2 = (int) v.getTag();
                v.setEnabled(false);
                //refuseJoin(pos2, (TextView) v);
                showReasonPage(pos2,(TextView) v);
                break;
            case R.id.mGroupOperation_return:
                finish();
                break;
            case R.id.mGroupOperation_clear:
                if(operations.size()!=0) {
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
        manager.post(HttpUrl.DelGroupMsg, map, new IRequestCallback() {
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
                                    operations.clear();
                                    if(listviewAdapter!=null){
                                        listviewAdapter.notifyDataSetChanged();
                                    }
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4001:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
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

    private void confirmJoin(final int pos, final TextView view) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("ugid", operations.get(pos).getUgid());
        map.put("mid", operations.get(pos).getMid());
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.AgreeOneInto, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    JSONObject data = obj.getJSONObject("data");
                                    String nickname = data.getString("nickname");
                                    operations.get(pos).setState("2");
                                    operations.get(pos).setNickname(nickname+"");
                                    listviewAdapter.notifyDataSetChanged();
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                case 4006:
                                case 4007:
                                case 4008:
                                case 4009:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                            view.setEnabled(true);
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

    private void showReasonPage(final int index, final TextView view) {
        final RefuseJoinGroupPop refuseJoinGroupPop = new RefuseJoinGroupPop(GroupMsgOperationActivity.this,operations.get(index).getContent());
        refuseJoinGroupPop.showPopupWindow();
        refuseJoinGroupPop.setOnPopRefuseListener(new RefuseJoinGroupPop.OnPopRefuseListener() {
            @Override
            public void onPopRefuse(String reason) {
                refuseJoin(index,reason,view);
            }
        });
    }

    private void refuseJoin(final int pos, String reason,final TextView view) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("ugid", operations.get(pos).getUgid());
        map.put("mid", operations.get(pos).getMid());
        map.put("reason",reason);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.RefuseOneInto, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    JSONObject data = obj.getJSONObject("data");
                                    String nickname = data.getString("nickname");
                                    operations.get(pos).setNickname(nickname+"");
                                    operations.get(pos).setState("3");
                                    listviewAdapter.notifyDataSetChanged();
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                case 4006:
                                case 4007:
                                case 4008:
                                case 4009:
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                            }
                            view.setEnabled(true);
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
}
