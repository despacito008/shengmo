package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.HomeNewListData;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.recycleradapter.LabellistpeoAdapter;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class GrouplistActivity extends AppCompatActivity {

    Handler handler = new Handler();
    private String fgid;
    private EditText et_name;
    private LinearLayout ll_addlable;
    private SwipeMenuRecyclerView recycler_label;
    List<HomeNewListData.DataBean> entityList = new ArrayList<>();
    int page=0;
    private LabellistpeoAdapter labellistpeoAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    public  static String yiyouhaoyou="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouplist);
        StatusBarUtil.showLightStatusBar(this);
        EventBus.getDefault().register(this);
        recycler_label = (SwipeMenuRecyclerView) findViewById(R.id.recycler_label);
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        setSideslipMenu();
        recycler_label.setLayoutManager(new LinearLayoutManager(this));
        labellistpeoAdapter = new LabellistpeoAdapter(this, entityList);
        recycler_label.setAdapter(labellistpeoAdapter);
        et_name = (EditText) findViewById(R.id.edit_name);
        ll_addlable = (LinearLayout) findViewById(R.id.ll_addlable);
        ImageView mStamp_return = (ImageView) findViewById(R.id.mStamp_return);
        mStamp_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_addlable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GrouplistActivity.this,GrouptoaddActivity.class);
                intent.putExtra("fgid",fgid);
                startActivity(intent);
            }
        });

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page=0;
                entityList.clear();
                getFriendList();

            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                getFriendList();
            }
        });

        findViewById(R.id.mStamp_zhangdan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_name.getText().toString().trim().equals("")){
                    int length = et_name.getText().toString().trim().length();
                    if (length>4){
                        ToastUtil.show(GrouplistActivity.this,"分组名称限四字以内！");
                    }else {
                        getfriendgrouplist(fgid, et_name.getText().toString().trim());
                    }

                }

            }
        });

        Intent intent = getIntent();
        String groupname = intent.getStringExtra("groupname");
        fgid = intent.getStringExtra("fgid");
        et_name.setText(groupname+"");
        et_name.setSelection(groupname.length());//将光标移至文字末尾

        getFriendList();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getshuaxin(String str){
        if (str.equals("fenzuhaoyouliebiaoshuaxin")){
            page=0;
            entityList.clear();
            getFriendList();
        }
    }

    public static int dp2px(float dpValue){
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    // 2. 设置item侧滑菜单
    private void setSideslipMenu() {
        // 设置菜单创建器
        recycler_label .setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听
        recycler_label .setSwipeMenuItemClickListener(mMenuItemClickListener);
    }


    // 3. 创建侧滑菜单
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int i = dp2px(60);
            SwipeMenuItem deleteItem = new SwipeMenuItem(GrouplistActivity.this)
                    .setBackgroundColor(getResources().getColor(R.color.red2)) // 背景颜色
                    .setText("删除") // 文字。
                    .setTextColor(Color.WHITE) // 文字颜色。
                    .setTextSize(16) // 文字大小。
                    .setWidth(i) // 宽
                    .setHeight(MATCH_PARENT); //高（MATCH_PARENT意为Item多高侧滑菜单多高 （推荐使用））
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
        }
    };

    // 4. 创建侧滑菜单的点击事件
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            menuBridge.closeMenu();
            //在menuBridge中我们可以得到侧滑的这一项item的position (menuBridge.getAdapterPosition())
            int adapterPosition = menuBridge.getAdapterPosition();
            getdelfgusers(fgid,entityList.get(adapterPosition).getUid());
            Message message = new Message();
            message.arg2=15;
            message.obj=fgid;
            EventBus.getDefault().post(message);
            entityList.remove(adapterPosition);
            labellistpeoAdapter.notifyDataSetChanged();
        }
    };


    //修改分组名称
    public void getfriendgrouplist(String fgid,String fgname){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fgid", fgid);
        map.put("fgname", fgname);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.updfriendgroup, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                PrintLogUtils.log(response, "--");
                try {
                    final JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")){
                        case 2000:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post("fenzuliebiaoshuaxin");
                                    finish();
                                }
                            });
                            break;
                        default:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        ToastUtil.show(getApplicationContext(),""+object.getString("msg"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
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

    private void getFriendList() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fgid", fgid+"");
        map.put("page", page + "");
        map.put("lat", MyApp.lat);
        map.put("lng", MyApp.lng);
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.getgfuserslist, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("fragmentfriend", "onSuccess: " + response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            HomeNewListData data = new Gson().fromJson(response, HomeNewListData.class);
                            List<HomeNewListData.DataBean> data1 = data.getData();
                            entityList.addAll(data1);
                            labellistpeoAdapter.notifyDataSetChanged();
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.finishLoadMore();
                            yiyouhaoyou="";
                            for (int i = 0; i < entityList.size(); i++) {
                                yiyouhaoyou+=entityList.get(i).getUid()+",";
                            }
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

    //将好友移除分组
    public void getdelfgusers(String fgid,String fuid){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fgid", fgid);
        map.put("fuid", fuid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.delfgusers, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                PrintLogUtils.log(response, "--");
                try {
                    final JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")){
                        case 2000:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                            break;
                        default:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        ToastUtil.show(getApplicationContext(),""+object.getString("msg"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        yiyouhaoyou="";
        EventBus.getDefault().unregister(this);
    }
}
