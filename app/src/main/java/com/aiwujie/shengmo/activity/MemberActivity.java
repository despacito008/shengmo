package com.aiwujie.shengmo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.MemberListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.MemberData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.util.IntentKey;
import com.aiwujie.shengmo.net.HttpCodeListener;
import com.aiwujie.shengmo.net.HttpHelper;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.tim.helper.MessageSendHelper;
import com.aiwujie.shengmo.utils.FinishActivityManager;
import com.aiwujie.shengmo.utils.SafeCheckUtil;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hb.dialog.myDialog.MyAlertInputDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kotlin.Result;

public class MemberActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2, AdapterView.OnItemLongClickListener {

    @BindView(R.id.mMember_return)
    ImageView mMemberReturn;
    @BindView(R.id.mMember_title_name)
    TextView mMemberTitleName;
    @BindView(R.id.mMember_listview)
    PullToRefreshListView mMemberListview;
    @BindView(R.id.mMember_et_search)
    EditText mMemberEtSearch2;
    @BindView(R.id.et_layout_normal_search_text)
    EditText etLayoutNormalSearchText;
    @BindView(R.id.tv_layout_normal_search_search)
    TextView tvLayoutNormalSearchSearch;
    @BindView(R.id.ll_normal_search)
    LinearLayout llNormalSearch;
    @BindView(R.id.layout_normal_empty)
    LinearLayout layoutNormalEmpty;
    private int page = 0;
    private int searchpage = 0;
    Handler handler = new Handler();
    private String gid;

    //判断操作权限
    private String state;
    private String memberFlag;

    private String searchContent;
    List<MemberData.DataBean> members = new ArrayList<>();
    private List<MemberData.DataBean> searchmembers = new ArrayList<>();
    private MemberListviewAdapter listviewAdapter;
    private MemberListviewAdapter searchlistviewAdapter;
    private String ugid;
    private boolean isSearch;
    //区分是否第一次加载，来判断edittext监听
    private int oncemore = 0;
    //禁言的状态 0为正常 1为禁言
    private String gagstate;
    private String uid;
    private String cardname;
    private Unbinder unbinder;
    private String leaderLet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        unbinder = ButterKnife.bind(this);
        // X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        gid = getIntent().getStringExtra(IntentKey.GROUP_ID);
        state = getIntent().getStringExtra(IntentKey.STATE);
        memberFlag = getIntent().getStringExtra(IntentKey.FLAG);
       // leaderLet = getIntent().getStringExtra("leaderLet");
        setData();
        setListener();
    }

    @OnClick(R.id.mMember_return)
    public void onClick() {
        finish();
    }

    private void setData() {
        if (memberFlag.equals("2")) {
            mMemberTitleName.setText("设置管理员");
        }
        mMemberListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mMemberListview.setFocusable(false);
        //实现自动刷新
        mMemberListview.mHeaderLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int width = mMemberListview.mHeaderLayout.getHeight();
                if (width > 0) {
                    mMemberListview.setRefreshing();
                    mMemberListview.mHeaderLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
    }

    private void setListener() {
        etLayoutNormalSearchText.addTextChangedListener(watcher);
        mMemberListview.setOnRefreshListener(this);
        mMemberListview.setOnItemClickListener(this);
        mMemberListview.getRefreshableView().setOnItemLongClickListener(this);
        mMemberListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
                //visibleItemCount：当前能看见的列表项个数（小半个也算）
                //totalItemCount：列表项共数
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0 && visibleItemCount != totalItemCount) {
                    if (!mMemberListview.isShownHeader()) {
                        if (!isSearch) {
                            page = page + 1;
                            if (hasMore) {
                                getMembers();
                            }
                        } else {
                            searchpage = searchpage + 1;
                            if (hasMore) {
                                getMemberSearch();
                            }
                        }
                    }
                }
            }
        });
    }

    boolean isLoading = false;
    private void getMembers() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid", gid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", page + "");
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetGroupMember, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                isLoading = false;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("memeberactivity", "run: " + response);
                        try {
                            oncemore = 1;
                            etLayoutNormalSearchText.addTextChangedListener(watcher);
                            MemberData data = new Gson().fromJson(response, MemberData.class);
                            if (data.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    ToastUtil.show(MemberActivity.this.getApplicationContext(), "没有更多");
                                    hasMore = false;
                                } else {
                                    layoutNormalEmpty.setVisibility(View.VISIBLE);
                                }
                            } else {
                                hasMore = true;
                                layoutNormalEmpty.setVisibility(View.GONE);
                                if (page == 0) {
                                    members.clear();
                                    int retcode = data.getRetcode();
                                    members.addAll(data.getData());
                                    try {
                                        listviewAdapter = new MemberListviewAdapter(MemberActivity.this, members, retcode);
                                        mMemberListview.setAdapter(listviewAdapter);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    members.addAll(data.getData());
                                    listviewAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            ToastUtil.show(getApplicationContext(), "出错啦...");
                        }
                        mMemberListview.onRefreshComplete();
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {
                isLoading = false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PesonInfoActivity.class);
        if (members.size() != 0) {
            intent.putExtra("uid", members.get(position - 1).getUid());
        } else {
            intent.putExtra("uid", searchmembers.get(position - 1).getUid());
        }
        startActivity(intent);

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        searchpage = 0;
        oncemore = 0;
        members.clear();
        searchmembers.clear();
        etLayoutNormalSearchText.setText("");
        if (listviewAdapter != null) {
            listviewAdapter.notifyDataSetChanged();
        }
        if (searchlistviewAdapter != null) {
            searchlistviewAdapter.notifyDataSetChanged();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMembers();
            }
        }, 300);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        if (!state.equals("-1") && !state.equals("0") && !state.equals("1")) {
//                Log.i("memberactivity", "onItemLongClick: " + Integer.parseInt(state) + "," + Integer.parseInt(members.get(position).getState()));
            if (members.size() != 0) {
                if (Integer.parseInt(state) > Integer.parseInt(members.get(position - 1).getState())) {
                    ugid = members.get(position - 1).getUgid();
                    uid = members.get(position - 1).getUid();
                    cardname = members.get(position - 1).getCardname();
                    if (memberFlag.equals("1")) {
                        gagstate = (members.get(position - 1).getGagstate());
                        deleteAndBanMember(position - 1);
                    } else {
                        operationMember(position - 1);
                    }
                }
            } else {
                if (Integer.parseInt(state) > Integer.parseInt(searchmembers.get(position - 1).getState())) {
                    ugid = searchmembers.get(position - 1).getUgid();
                    uid = searchmembers.get(position - 1).getUid();
                    cardname = searchmembers.get(position - 1).getCardname();
                    if (memberFlag.equals("1")) {
                        gagstate = searchmembers.get(position - 1).getGagstate();
                        deleteAndBanMember(position - 1);
                    } else {
                        operationMember(position - 1);
                    }
                }
            }
        }

        return true;
    }

    private void deleteAndBanMember(final int pos) {
        String showText = null;
        if (gagstate.equals("0")) {
            showText = "禁言该成员";
        } else {
            showText = "解除禁言";
        }
        new AlertView(null, null, "取消", null,
                new String[]{"设置群名片", showText, "删除群成员"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                switch (position) {
                    case 0:
                        geteditGroupCardName(pos);
                        break;
                    case 1:
                        if (gagstate.equals("0")) {
                            showMuteGroupMemberPop(pos);
                            //禁言某人
                           // banSomeOne(pos);
                        } else {
                            //解除禁言
                            removeSomeOne(pos);
                        }
                        break;
                    case 2:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MemberActivity.this);
                        builder.setMessage("是否删除群成员?").setPositiveButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //踢出某人
                                outGroup(pos);
                            }
                        }).create().show();
                        break;
                }
            }
        }).show();
    }

    private void removeSomeOne(final int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("ugid", ugid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.RmoveGagSomeOne, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("isBanle", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    if (members.size() != 0) {
                                        members.get(pos).setGagstate("0");
                                        listviewAdapter.notifyDataSetChanged();
                                    } else {
                                        searchmembers.get(pos).setGagstate("0");
                                        searchlistviewAdapter.notifyDataSetChanged();
                                    }
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                case 4006:
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

    void showMuteGroupMemberPop(final int pos) {
        final String hour6 = "6小时";
        final String hour12 = "12小时";
        final String day01 = "1天";
        final String day03 = "3天";
        final String day07 = "1周";
        //final String day__ = "永久";
        AlertView alertView = new AlertView(null, null, "取消", null,
                new String[]{hour6,hour12,day01,day03,day07},
                this, AlertView.Style.ActionSheet, null);
        alertView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                switch (data) {
                    case hour6:
                        banSomeOne(6 * 3600,pos);
                        break;
                    case hour12:
                        banSomeOne(12 * 3600,pos);
                        break;
                    case day01:
                        banSomeOne(24 * 3600,pos);
                        break;
                    case day03:
                        banSomeOne(3 * 24 * 3600,pos);
                        break;
                    case day07:
                        banSomeOne(7 * 24 * 3600 ,pos);
                        break;

                }
            }
        });
        alertView.show();
    }

    private void banSomeOne(long time,final int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("ugid", ugid);
        map.put("blockingalong",String.valueOf(time));
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.BanSomeOne, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("isBanle", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    if (members.size() != 0) {
                                        members.get(pos).setGagstate("1");
                                        listviewAdapter.notifyDataSetChanged();
                                    } else {
                                        searchmembers.get(pos).setGagstate("1");
                                        searchlistviewAdapter.notifyDataSetChanged();
                                    }
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    break;
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                case 4006:
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

    private void operationMember(final int pos) {
        new AlertView(null, null, "取消", null,
                new String[]{"设置管理员", "取消管理员"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position, String data) {
                switch (position) {
                    case 0:
                        setManager(pos);
                        break;
                    case 1:
                        cancelManager(pos);
                        break;
                    case 2:
//                        new AlertDialog.Builder(MemberActivity.this).setMessage("是否设置该成员为新的群主？")
//                                .setPositiveButton("否", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                }).setNegativeButton("是", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                setGroupOwner(pos);
//                            }
//                        }).create().show();

                        break;
                }
            }
        }).show();
    }

    private void setGroupOwner(int index) {
        HttpHelper.getInstance().changeGroupAdmin(gid, members.get(index).getUid(), new HttpCodeListener() {
            @Override
            public void onSuccess(String data) {
                if (SafeCheckUtil.isActivityFinish(MemberActivity.this)) {
                    return;
                }
                try {
                    JSONObject obj = new JSONObject(data);
                    ToastUtil.show(MemberActivity.this, obj.getString("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFail(int code, String msg) {
                if (SafeCheckUtil.isActivityFinish(MemberActivity.this)) {
                    return;
                }
                ToastUtil.show(MemberActivity.this,msg);
            }
        });
    }

    private void setManager(final int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("ugid", ugid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.SetManager, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    if (members.size() != 0) {
                                        members.get(pos).setState("2");
                                        listviewAdapter.notifyDataSetChanged();
                                    } else {
                                        searchmembers.get(pos).setState("2");
                                        searchlistviewAdapter.notifyDataSetChanged();
                                    }
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    EventBus.getDefault().post("operationsuccess");
                                    break;
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                case 4006:
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

    private void cancelManager(final int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("ugid", ugid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.CancelManager, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    if (members.size() != 0) {
                                        members.get(pos).setState("1");
                                        listviewAdapter.notifyDataSetChanged();
                                    } else {
                                        searchmembers.get(pos).setState("1");
                                        searchlistviewAdapter.notifyDataSetChanged();
                                    }
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    EventBus.getDefault().post("operationsuccess");
                                    break;
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                case 4006:
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

    private void outGroup(final int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("ugid", ugid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.TiGroup, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("outgroup", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(response);
                            switch (obj.getInt("retcode")) {
                                case 2000:
                                    if (members.size() != 0) {
                                        members.remove(pos);
                                        listviewAdapter.notifyDataSetChanged();
                                    } else {
                                        searchmembers.remove(pos);
                                        searchlistviewAdapter.notifyDataSetChanged();
                                    }
                                    ToastUtil.show(getApplicationContext(), obj.getString("msg"));
                                    EventBus.getDefault().post("operationsuccess");
                                    break;
                                case 4000:
                                case 4001:
                                case 4002:
                                case 4003:
                                case 4004:
                                case 4005:
                                case 4006:
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

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i("memberedittext", "afterTextChanged: " + s.toString());
            searchContent = s.toString();
            searchpage = 0;
            if (!searchContent.equals("")) {
                if (listviewAdapter != null) {
                    members.clear();
                    listviewAdapter.notifyDataSetChanged();
                }
                getMemberSearch();
                isSearch = true;
            } else {
                members.clear();
                searchmembers.clear();
                page = 0;
                if (oncemore != 0) {
                    getMembers();
                }
                isSearch = false;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    boolean hasMore = true;
    private void getMemberSearch() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid", gid);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", searchpage + "");
        map.put("search", searchContent);
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetGroupMember, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("memeberactivity", "run: " + response);
                        MemberData data = new Gson().fromJson(response, MemberData.class);
                        if (data.getData().size() == 0) {
                            if (searchpage != 0) {
                                searchpage = searchpage - 1;
                                ToastUtil.show(MemberActivity.this.getApplicationContext(), "没有更多");
                                hasMore = false;
                            } else {
                                layoutNormalEmpty.setVisibility(View.VISIBLE);
                                if (searchlistviewAdapter != null) {
                                    searchmembers.clear();
                                    searchlistviewAdapter.notifyDataSetChanged();
                                }
                            }

                        } else {
                            hasMore = true;
                            layoutNormalEmpty.setVisibility(View.GONE);
                            if (searchpage == 0) {
                                searchmembers.clear();
                                int retcode = data.getRetcode();
                                searchmembers.addAll(data.getData());
                                try {
                                    searchlistviewAdapter = new MemberListviewAdapter(MemberActivity.this, searchmembers, retcode);
                                    mMemberListview.setAdapter(searchlistviewAdapter);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                searchmembers.addAll(data.getData());
                                searchlistviewAdapter.notifyDataSetChanged();
                            }
                        }
                        mMemberListview.onRefreshComplete();
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    private void geteditGroupCardName(final int pos) {

        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(MemberActivity.this).builder()
                .setTitle("设置群名片")
                .setEditText(cardname);
        myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = myAlertInputDialog.getResult().length();
                if (length > 10) {
                    ToastUtil.show(MemberActivity.this, "群名片限十字以内！");
                } else {
                    //getaddfriendgroup(myAlertInputDialog.getResult());
                    geteditGroupCardName(myAlertInputDialog.getResult(), pos);
                    myAlertInputDialog.dismiss();
                }
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAlertInputDialog.dismiss();
            }
        });
        myAlertInputDialog.show();
    }

    private void geteditGroupCardName(final String cardname, final int pos) {
        Map<String, String> map = new HashMap<>();
        map.put("gid", gid);
        map.put("uid", uid);
        map.put("cardname", cardname);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.editGroupCardName, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i("groupintroissucc", "onSuccess: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2000:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (members.size() != 0) {
                                        members.get(pos).setCardname(cardname);
                                        listviewAdapter.notifyDataSetChanged();
                                    } else {
                                        members.get(pos).setCardname(cardname);
                                        searchlistviewAdapter.notifyDataSetChanged();
                                    }
                                }
                            });

                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}
