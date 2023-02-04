package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.AtMemberListviewAdapter;
import com.aiwujie.shengmo.adapter.MemberListviewAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.AtGroupMemberData;
import com.aiwujie.shengmo.bean.MemberData;
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
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageManager;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AtGroupMemberActivity extends AppCompatActivity implements AdapterView.OnItemClickListener , PullToRefreshBase.OnRefreshListener2{

    @BindView(R.id.mAtGroup_member_return)
    ImageView mAtGroupMemberReturn;
    @BindView(R.id.mAtGroup_member_search)
    EditText mAtGroupMemberSearch;
    @BindView(R.id.mAtGroup_member_listview)
    PullToRefreshListView mAtGroupMemberListview;
    @BindView(R.id.activity_at_group_member)
    PercentLinearLayout activityAtGroupMember;
    private String groupId;
    private int page = 0;
    private int searchpage = 0;
    Handler handler = new Handler();
    private boolean isSearch;
    //区分是否第一次加载，来判断edittext监听
    private int oncemore=0;

    List<AtGroupMemberData.DataBean> members = new ArrayList<>();
    private List<AtGroupMemberData.DataBean> searchmembers=new ArrayList<>();
    private AtMemberListviewAdapter listviewAdapter;
    private AtMemberListviewAdapter searchlistviewAdapter;
    private String searchContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_group_member);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
        groupId = getIntent().getStringExtra("groupId");
        setListener();
    }

    private void setListener() {
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);

        mAtGroupMemberListview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mAtGroupMemberListview.setFocusable(false);
        //实现自动刷新
        mAtGroupMemberListview.mHeaderLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int width = mAtGroupMemberListview.mHeaderLayout.getHeight();
                if (width > 0) {
                    mAtGroupMemberListview.setRefreshing();
                    mAtGroupMemberListview.mHeaderLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });
        mAtGroupMemberSearch.addTextChangedListener(watcher);
        mAtGroupMemberListview.setOnRefreshListener(this);
        mAtGroupMemberListview.setOnItemClickListener(this);
        mAtGroupMemberListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
                //visibleItemCount：当前能看见的列表项个数（小半个也算）
                //totalItemCount：列表项共数
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0 && visibleItemCount != totalItemCount) {
                    if (!mAtGroupMemberListview.isShownHeader()) {
                        if(!isSearch) {
                            page = page + 1;
                            getMembers();
                        }else{
                            searchpage=searchpage+1;
                            getMemberSearch();
                        }
                    }
                }
            }
        });
    }

    @OnClick(R.id.mAtGroup_member_return)
    public void onClick() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String uid=null;
        String name=null;
        String headpic=null;
        if (members.size() != 0) {
            uid=members.get(position - 1).getUid();
            String nickname = members.get(position - 1).getNickname();
            String cardname = members.get(position - 1).getCardname();
            if (!"".equals(cardname)&&null!=cardname){
                name=cardname;
            }else {
                name=nickname;
            }
            headpic=members.get(position - 1).getHead_pic();
        }else{
            uid=searchmembers.get(position - 1).getUid();
            String nickname = searchmembers.get(position - 1).getNickname();
            String cardname = searchmembers.get(position - 1).getCardname();
            if (!"".equals(cardname)&&null!=cardname){
                name=cardname;
            }else {
                name=nickname;
            }
            headpic=searchmembers.get(position - 1).getHead_pic();
        }
        //RongMentionManager.getInstance().mentionMember(new UserInfo(uid,name, Uri.parse(headpic)));
        Intent intent = new Intent();
        intent.putExtra("name",name);
        intent.putExtra("id",String.valueOf(uid));
        setResult(200,intent);
        finish();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        page = 0;
        searchpage=0;
        members.clear();
        searchmembers.clear();
        mAtGroupMemberSearch.setText("");
        if (listviewAdapter != null) {
            listviewAdapter.notifyDataSetChanged();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMembers();
            }
        }, 500);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }
    private void getMembers() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid", groupId);
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        map.put("page", page + "");
        map.put("login_uid", MyApp.uid);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.GetGroupMember, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("memeberactivity", "run: " + response);
                        try {
                            oncemore = 1;
                            AtGroupMemberData data = new Gson().fromJson(response, AtGroupMemberData.class);
                            if (data.getData().size() == 0) {
                                if (page != 0) {
                                    page = page - 1;
                                    ToastUtil.show(AtGroupMemberActivity.this.getApplicationContext(), "没有更多");
                                }
                                if (listviewAdapter != null) {
                                    listviewAdapter.notifyDataSetChanged();
                                }
                            } else {
                                if (page == 0) {
                                    members.addAll(data.getData());
                                    try {
                                        listviewAdapter = new AtMemberListviewAdapter(AtGroupMemberActivity.this, members);
                                        mAtGroupMemberListview.setAdapter(listviewAdapter);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    members.addAll(data.getData());
                                    listviewAdapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonSyntaxException e){
                            e.printStackTrace();
                        }
                        mAtGroupMemberListview.onRefreshComplete();
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

//    private void getMemberSearch() {
//        Map<String, String> map = new HashMap<>();
//        map.put("uid", MyApp.uid);
//        map.put("gid", gid);
//        map.put("lat", MyApp.lat);
//        map.put("lng", MyApp.lng);
//        map.put("page", searchpage + "");
//        map.put("search", searchContent);
//        map.put("login_uid", MyApp.uid);
//        IRequestManager manager = RequestFactory.getRequestManager();
//        manager.post(HttpUrl.GetGroupMember, map, new IRequestCallback() {
//            @Override
//            public void onSuccess(final String response) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.i("memeberactivity", "run: " + response);
//                        MemberData data = new Gson().fromJson(response, MemberData.class);
//                        if (data.getData().size() == 0) {
//                            if (searchpage != 0) {
//                                searchpage = searchpage - 1;
//                                ToastUtil.show(MemberActivity.this.getApplicationContext(), "没有更多");
//                            } else {
//                                if (searchlistviewAdapter != null) {
//                                    searchmembers.clear();
//                                    searchlistviewAdapter.notifyDataSetChanged();
//                                }
//                            }
//
//                        } else {
//                            if (searchpage == 0) {
//                                searchmembers.clear();
//                                int retcode = data.getRetcode();
//                                searchmembers.addAll(data.getData());
//                                try {
//                                    searchlistviewAdapter = new MemberListviewAdapter(MemberActivity.this, searchmembers, retcode);
//                                    mMemberListview.setAdapter(searchlistviewAdapter);
//                                } catch (NullPointerException e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                searchmembers.addAll(data.getData());
//                                searchlistviewAdapter.notifyDataSetChanged();
//                            }
//                        }
//                        mMemberListview.onRefreshComplete();
//                    }
//                });
//
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//            }
//        });
//    }
    private void getMemberSearch() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid", groupId);
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
                        AtGroupMemberData data = new Gson().fromJson(response, AtGroupMemberData.class);
                        if (data.getData().size() == 0) {
                            if (searchpage != 0) {
                                searchpage = searchpage - 1;
                                ToastUtil.show(AtGroupMemberActivity.this.getApplicationContext(), "没有更多");
                            }else{
                                if(searchlistviewAdapter!=null) {
                                    searchmembers.clear();
                                    searchlistviewAdapter.notifyDataSetChanged();
                                }
                            }

                        } else {
                            if (searchpage == 0) {
                                searchmembers.clear();
                                searchmembers.addAll(data.getData());
                                try {
                                    searchlistviewAdapter = new AtMemberListviewAdapter(AtGroupMemberActivity.this, searchmembers);
                                    mAtGroupMemberListview.setAdapter(searchlistviewAdapter);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                searchmembers.addAll(data.getData());
                                searchlistviewAdapter.notifyDataSetChanged();
                            }
                        }
                        mAtGroupMemberListview.onRefreshComplete();
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
            Log.i("memberedittext", "afterTextChanged: "+s.toString());
            searchContent=s.toString();
            searchpage=0;
            if(!searchContent.equals("")) {
                if(listviewAdapter!=null){
                    members.clear();
                    listviewAdapter.notifyDataSetChanged();
                }
                getMemberSearch();
                isSearch=true;
            }else{
                members.clear();
                searchmembers.clear();
                page=0;
                if(oncemore!=0) {
                    getMembers();
                }
                isSearch=false;
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
}
