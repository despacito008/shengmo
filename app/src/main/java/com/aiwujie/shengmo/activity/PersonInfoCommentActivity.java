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
import com.aiwujie.shengmo.activity.newui.NewDynamicDetailActivity;
import com.aiwujie.shengmo.adapter.AllCommentAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.AllCommentData;
import com.aiwujie.shengmo.customview.PercentLinearLayout;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.HttpListener;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.IsListviewSlideBottom;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.TimeSecondUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.aiwujie.shengmo.view.OperateCommentPopup;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonInfoCommentActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemClickListener {

    @BindView(R.id.mPersonInfoComment_return)
    ImageView mPersonInfoCommentReturn;
    @BindView(R.id.mPersonInfoComment_title_name)
    TextView mPersonInfoCommentTitleName;
    @BindView(R.id.mPersonInfoComment_listview)
    PullToRefreshListView mPersonInfoCommentListview;
    @BindView(R.id.activity_person_info_comment)
    PercentLinearLayout activityPersonInfoComment;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;
    private String uid;
    private AutoLinearLayout mPersonInfoCommentBottomLl;
    private TextView mPersonInfoCommentBottomTv;
    private Handler handler =new Handler();
    private int friendRetcode;
    private int page=0;
    private TimeSecondUtils refresh;
    private AllCommentAdapter commentAdapter;
    private List<AllCommentData.DataBean> comments = new ArrayList<>();
    private int currentIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info_comment);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
        setData();
        setListener();
    }

    private void setData() {
        uid = getIntent().getStringExtra("uid");
        mPersonInfoCommentTitleName.setText(getIntent().getStringExtra("nickname"));
        View view = View.inflate(this, R.layout.item_person_info_comment_header, null);
        mPersonInfoCommentBottomLl=(AutoLinearLayout)view.findViewById(R.id.mPersonInfo_comment_bottom_ll);
        mPersonInfoCommentBottomTv=(TextView)view.findViewById(R.id.mPersonInfo_comment_bottom_tv);
        mPersonInfoCommentListview.getRefreshableView().addHeaderView(view);
        //是否是朋友
//        isFriend();
        //实现自动刷新
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPersonInfoCommentListview.setRefreshing();
            }
        }, 100);
    }

    private void setListener() {
        mPersonInfoCommentListview.setOnRefreshListener(this);
        mPersonInfoCommentListview.setOnItemClickListener(this);
        mPersonInfoCommentListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case SCROLL_STATE_IDLE:
                        if (IsListviewSlideBottom.isListViewReachBottomEdge(absListView)) {
                            if (isReresh) {
                                page = page + 1;
                                getCommentList();
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

    private void getCommentList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("login_uid", MyApp.uid);
        map.put("page",page+"");
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetCommentedListOnUserInfo, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("personinfocomment", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            String vip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vip", "0");
//                            if (uid.equals(MyApp.uid)) {
                                setAdapter(response);
//                            } else {
//                                if ( vip.equals("1")) {
//                                    setAdapter(response);
//                                } else {
//                                    if (friendRetcode == 2001) {
//                                        setAdapter(response);
//                                    } else {
//                                        isReresh = false;
//                                        commentAdapter = new AllCommentAdapter(PersonInfoCommentActivity.this, comments);
//                                        mPersonInfoCommentListview.setAdapter(commentAdapter);
//                                        mPersonInfoCommentBottomLl.setVisibility(View.VISIBLE);
//                                        mPersonInfoCommentBottomTv.setMovementMethod(LinkMovementMethod.getInstance());
//                                        TextMoreClickUtils clickUtils=new TextMoreClickUtils(PersonInfoCommentActivity.this,uid,handler);
//                                        mPersonInfoCommentBottomTv.setText(clickUtils.addClickablePart("互为好友、VIP会员","TA的评论限","可见"), TextView.BufferType.SPANNABLE);
//                                    }
//                                }
//                            }
                            mPersonInfoCommentListview.onRefreshComplete();
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
        AllCommentData listData = new Gson().fromJson(response, AllCommentData.class);
        if (listData != null) {
            if (listData.getData().size() == 0) {
                if (page != 0) {
                    page = page - 1;
                    ToastUtil.show(getApplicationContext(), "没有更多");
                    isReresh = false;
                } else {
                    commentAdapter = new AllCommentAdapter(this, comments);
                    mPersonInfoCommentListview.setAdapter(commentAdapter);
                }
            } else {
                isReresh = true;
                if (page == 0) {
                    comments.addAll(listData.getData());
                    try {
                        commentAdapter = new AllCommentAdapter(this, comments);
                        mPersonInfoCommentListview.setAdapter(commentAdapter);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    comments.addAll(listData.getData());
                    commentAdapter.notifyDataSetChanged();
                }
            }
        }
        commentAdapter.setOnCommentListener(new AllCommentAdapter.OnItemCommentListener() {
            @Override
            public void onItemClick(int position) {
                //Intent intent = new Intent(PersonInfoCommentActivity.this, DynamicDetailActivity.class);
                Intent intent = new Intent(PersonInfoCommentActivity.this, NewDynamicDetailActivity.class);
                intent.putExtra("uid", comments.get(position).getDuid());
                intent.putExtra("did", comments.get(position).getDid());
                intent.putExtra("pos", position);
                intent.putExtra("showwhat", 1);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(final int position) {
                String admin = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "admin", "0");
                final OperateCommentPopup operateCommentPopup;
                if("1".equals(admin) || MyApp.uid.equals(comments.get(position).getDuid())) {
                    operateCommentPopup = new OperateCommentPopup(PersonInfoCommentActivity.this,0);
                } else if (MyApp.uid.equals(comments.get(position).getUid())) {
                    operateCommentPopup = new OperateCommentPopup(PersonInfoCommentActivity.this,1);
                } else {
                    operateCommentPopup = new OperateCommentPopup(PersonInfoCommentActivity.this,2);
                }
                operateCommentPopup.showPopupWindow();
                operateCommentPopup.setOnCommentOperateListener(new OperateCommentPopup.OnCommentOperateListener() {
                    @Override
                    public void onCommentCopy() {
                        //获取剪贴板管理器：
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        // 创建普通字符型ClipData
                        ClipData mClipData = ClipData.newPlainText("Label", comments.get(position).getContent());
                        // 将ClipData内容放到系统剪贴板里。
                        cm.setPrimaryClip(mClipData);
                        ToastUtil.show(PersonInfoCommentActivity.this, "已复制到剪贴板");
                    }

                    @Override
                    public void onCommentReport() {
                        if(comments.get(position).getReportState() == 0) {
                            Intent intent = new Intent(PersonInfoCommentActivity.this, OtherReasonActivity.class);
                            intent.putExtra("uid", MyApp.uid);
                            intent.putExtra("did", comments.get(position).getDid());
                            intent.putExtra("cmid", comments.get(position).getCmid());
                            currentIndex = position;
                            startActivityForResult(intent,101);
                            operateCommentPopup.dismiss();
                        } else {
                            ToastUtil.show(PersonInfoCommentActivity.this,"该条评论已经举报过");
                        }
                    }

                    @Override
                    public void onCommentDelete() {
                        operateCommentPopup.dismiss();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(PersonInfoCommentActivity.this);
                        builder.setMessage("确认删除吗?")
                                .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                HttpHelper.getInstance().deleteComment(comments.get(position).getDid(), comments.get(position).getCmid(), new HttpListener() {
                                    @Override
                                    public void onSuccess(String data) {
                                        ToastUtil.show(PersonInfoCommentActivity.this, "删除成功");
                                        comments.remove(position);
                                        commentAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFail(String msg) {
                                        ToastUtil.show(PersonInfoCommentActivity.this, msg);
                                    }
                                });
                            }
                        }).create().show();
                    }
                });
            }

            @Override
            public void onItemApply(int position) {

            }
        });
    }


    private void isFriend() {
        Map<String, String> map = new HashMap<>();
        map.put("login_uid", MyApp.uid);
        map.put("uid",uid);
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

    @OnClick(R.id.mPersonInfoComment_return)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        comments.clear();
        if (commentAdapter != null) {
            commentAdapter.notifyDataSetChanged();
        }
        refresh = new TimeSecondUtils(this, mPersonInfoCommentListview);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getCommentList();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DynamicDetailActivity.class);
        intent.putExtra("uid", comments.get(position -2).getDuid());
        intent.putExtra("did", comments.get(position -2).getDid());
        intent.putExtra("pos", position -2);
        intent.putExtra("showwhat", 1);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == 200 && currentIndex != -1) {
            comments.get(currentIndex).setReportState(1);
            commentAdapter.notifyDataSetChanged();
        }
    }
}
