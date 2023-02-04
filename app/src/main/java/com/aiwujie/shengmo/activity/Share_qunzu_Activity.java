package com.aiwujie.shengmo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.GroupListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.GroupData;
import com.aiwujie.shengmo.customview.MyListView;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.android.percent.support.PercentLinearLayout;

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

public class Share_qunzu_Activity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener{

    @BindView(R.id.mAt_fans_cancel)
    TextView mAt_fans_cancel;
    @BindView(R.id.mOtherGroup_listview)
    PullToRefreshListView mOtherGroupListview;

    private int page = 0;
    Handler handler = new Handler();
    List<GroupData.DataBean> groups = new ArrayList<>();
    private GroupListviewAdapter groupAdapter;
    private MyListView listView;
    private View ll01;
    private View ll02;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;
    private TimeSecondUtils refresh;
    private int leixing;
    private String did;
    private String content;
    private String pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_qunzu_);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        leixing = intent.getIntExtra("leixing", 0);
        did = intent.getStringExtra("id");
        content = intent.getStringExtra("content");
        pic = intent.getStringExtra("pic");
        setData();
        setListener();
    }

    @OnClick(R.id.mAt_fans_cancel)
    public void onClick() {
        finish();
    }

    private void setData() {
        View view = View.inflate(getApplicationContext(), R.layout.item_other_group_header, null);
        listView = (MyListView) view.findViewById(R.id.item_other_group_header_listview);
        ll01 =  view.findViewById(R.id.item_other_group_header_ll01);
        ll02 =  view.findViewById(R.id.item_other_group_header_ll02);
        mOtherGroupListview.getRefreshableView().addHeaderView(view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        mOtherGroupListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mOtherGroupListview.setFocusable(false);
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mOtherGroupListview.setRefreshing();
            }
        }, 100);
    }

    private void setListener() {
        mOtherGroupListview.setOnRefreshListener(this);
        mOtherGroupListview.setOnItemClickListener(this);
        mOtherGroupListview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isReresh) {
                                page = page + 1;
                                getGroupList();
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

    private void getGroupList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("type", "2");
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", page + "");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetGroupListFilter, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fragmentgroupmy", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        GroupData listData = new Gson().fromJson(response, GroupData.class);
                        if (listData.getData().size() == 0) {
                            if (page != 0) {
                                isReresh = false;
                                page = page - 1;
                                ToastUtil.show(Share_qunzu_Activity.this, "没有更多");
                            } else {
                                ll02.setVisibility(View.GONE);
                            }
                        } else {
                            isReresh = true;
                            ll02.setVisibility(View.GONE);
                            if (page == 0) {
                                groups.addAll(listData.getData());
                                int retcode = listData.getRetcode();
                                groupAdapter = new GroupListviewAdapter(Share_qunzu_Activity.this, groups, retcode);
                                mOtherGroupListview.setAdapter(groupAdapter);
                            } else {
                                groups.addAll(listData.getData());
                                groupAdapter.notifyDataSetChanged();
                            }
                        }
                        mOtherGroupListview.onRefreshComplete();
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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("提示");
        if (leixing==1){
            builder1.setMessage("分享动态给 "+groups.get(position-2).getGroupname());
        }else if (leixing==2){
            builder1.setMessage("分享同好给 "+groups.get(position-2).getGroupname());
        }

        builder1.setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (leixing==1){
                    sendPhone(content, groups.get(position-2).getGid(),did,pic);
                }else if (leixing==2){
                    sendPhone2(content, groups.get(position-2).getGid(),did,pic);
                }


            }
        }).create().show();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        groups.clear();
//        String realname = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "realname", "0");
//        String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
//        if (realname.equals("0") && vip.equals("0")) {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mOtherGroupListview.onRefreshComplete();
//                }
//            }, 200);
//            mOhterGroupBottomLl.setVisibility(View.VISIBLE);
//            TextMoreClickUtils clickUtils = new TextMoreClickUtils(OtherGroupActivity.this, otherUid, handler);
//            mOhterGroupBottomTv.setText(clickUtils.addClickablePart("互为好友、认证用户、VIP会员", "TA的群组限", "可见"), TextView.BufferType.SPANNABLE);
//            //Android4.0以上默认是淡绿色，低版本的是黄色。解决方法就是通过重新设置文字背景为透明色
//            mOhterGroupBottomTv.setHighlightColor(getResources().getColor(android.R.color.transparent));
//            mOhterGroupBottomTv.setMovementMethod(LinkMovementMethod.getInstance());
//        } else {
        if (groupAdapter != null) {
            groupAdapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(this, mOtherGroupListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getGroupList();
            }
        }, 300);
//        }

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        page = page + 1;
        getGroupList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(String message) {
        if (message.equals("refreshgroup")) {
            page = 0;
            groups.clear();
            getGroupList();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    public void sendPhone(String message1,String chatUserid,String did,String pic){

//        final Message_Sharedynamic info =new Message_Sharedynamic();
//        info.setContent(message1);
//        info.setNewid(did);
//        info.setIcon(pic);
        //chatUserid 是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
//        Message message = Message.obtain(chatUserid, Conversation.ConversationType.GROUP,info);
//        message.setExtra("0");
//        RongIM.getInstance().sendMessage(message, info.toString(), null, new IRongCallback.ISendMessageCallback() {
//            @Override //表示消息添加到本地数据库
//            public void onAttached(Message message) {
//
//            }
//            @Override//消息发送成功
//            public void onSuccess(Message message) {
//                finish();
//            }
//            @Override //消息发送失败
//            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//
//            }
//        });

    }

    public void sendPhone2(String message1,String chatUserid,String did,String pic){

//        final Message_Sharperson info =new Message_Sharperson();
//        info.setContent(message1);
//        info.setNewid(did);
//        info.setIcon(pic);
        //chatUserid 是接收消息方的id   Conversation.ConversationType 是消息会话的类型在这里表示的是私聊
//        Message message = Message.obtain(chatUserid, Conversation.ConversationType.GROUP,info);
//        message.setExtra("0");
//        RongIM.getInstance().sendMessage(message, info.toString(), null, new IRongCallback.ISendMessageCallback() {
//            @Override //表示消息添加到本地数据库
//            public void onAttached(Message message) {
//
//            }
//            @Override//消息发送成功
//            public void onSuccess(Message message) {
//                finish();
//            }
//            @Override //消息发送失败
//            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//
//            }
//        });

    }

}
