package com.aiwujie.shengmo.activity;


import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.FriendGroupListBean;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.recycleradapter.LabellistAdapter;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemStateChangedListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class LabelDefaultsActivity extends AppCompatActivity {

    private SwipeMenuRecyclerView recycler_label;
    List<FriendGroupListBean.DataBean> list = new ArrayList<>();
    private LabellistAdapter labellistAdapter;
    private LinearLayout ll_addlable;
    private ImageView mStamp_return;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_defaults);


        ll_addlable = (LinearLayout) findViewById(R.id.ll_addlable);
        mStamp_return = (ImageView) findViewById(R.id.mStamp_return);
        recycler_label = (SwipeMenuRecyclerView) findViewById(R.id.recycler_label);
        setSideslipMenu();
        getfriendgrouplist();
        recycler_label.setLayoutManager(new LinearLayoutManager(this));
        labellistAdapter = new LabellistAdapter(this, list);
        recycler_label.setAdapter(labellistAdapter);
        helper.attachToRecyclerView(recycler_label);
        recycler_label.setOnItemStateChangedListener(new OnItemStateChangedListener() {
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

            }
        });


        ll_addlable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(LabelDefaultsActivity.this).builder()
                        .setTitle("????????????")
                        .setEditText("");
                myAlertInputDialog.setPositiveButton("??????", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!myAlertInputDialog.getResult().equals("")){
                            int length = myAlertInputDialog.getResult().length();
                            if (length>4){
                                ToastUtil.show(LabelDefaultsActivity.this,"??????????????????????????????");
                            }else {
                                getaddfriendgroup(myAlertInputDialog.getResult());
                                myAlertInputDialog.dismiss();
                            }

                        }else {
                            ToastUtil.show(LabelDefaultsActivity.this,"????????????????????????");
                        }

                    }
                }).setNegativeButton("??????", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        myAlertInputDialog.dismiss();
                    }
                });
                myAlertInputDialog.show();
            }
        });
        mStamp_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    //???RecycleView??????????????????
    ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //????????????????????? ??????int???????????????????????????
            int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;//??????
            int swipeFlags = ItemTouchHelper.RIGHT;//????????????
            return makeMovementFlags(dragFlags,0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //????????????
            Collections.swap(list,viewHolder.getAdapterPosition(),target.getAdapterPosition());
            labellistAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //????????????
            list.remove(viewHolder.getAdapterPosition());
            labellistAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            String aaa = "";
            for (int i = 0; i < list.size(); i++) {
                aaa+=list.get(i).getId()+",";
            }
            if (list.size()>0){
                aaa = aaa.substring(0, aaa.length() - 1);
                getfriendgroupsort(aaa);
            }
        }

        @Override
        public boolean isLongPressDragEnabled() {
            //???????????????
            return true;
        }
    });



    // 3. ??????????????????
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int i = dp2px(75);
            SwipeMenuItem deleteItem = new SwipeMenuItem(LabelDefaultsActivity.this)
                    .setBackgroundColor(getResources().getColor(R.color.red2)) // ????????????
                    .setText("??????") // ?????????
                    .setTextColor(Color.WHITE) // ???????????????
                    .setTextSize(16) // ???????????????
                    .setWidth(i) // ???
                    .setHeight(MATCH_PARENT); //??????MATCH_PARENT??????Item???????????????????????? ?????????????????????
            swipeRightMenu.addMenuItem(deleteItem);// ???????????????????????????????????????
        }
    };

    public static int dp2px(float dpValue){
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    // 4. ?????????????????????????????????
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            // ??????????????????????????????????????????????????????Item???????????????????????????
            menuBridge.closeMenu();
            //???menuBridge???????????????????????????????????????item???position (menuBridge.getAdapterPosition())
            int adapterPosition = menuBridge.getAdapterPosition();
            getdelfriendgroup(list.get(adapterPosition).getId());
            list.remove(adapterPosition);
            labellistAdapter.notifyDataSetChanged();
        }
    };

    // 2. ??????item????????????
    private void setSideslipMenu() {
        // ?????????????????????
        recycler_label .setSwipeMenuCreator(swipeMenuCreator);
        // ????????????Item????????????
        recycler_label .setSwipeMenuItemClickListener(mMenuItemClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getfriendgrouplist();
    }

    //????????????
    public void getaddfriendgroup(String fgname){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fgname", fgname);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.addfriendgroup, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {
                    final JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")){
                        case 2000:
                            getfriendgrouplist();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        EventBus.getDefault().post("fenzuliebiaoshuaxin");
                                        ToastUtil.show(LabelDefaultsActivity.this,""+object.getString("msg"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            break;
                        case 50001:
                        case 50002:
                            EventBus.getDefault().post(new TokenFailureEvent());
                            break;
                        default:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        ToastUtil.show(LabelDefaultsActivity.this,""+object.getString("msg"));
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

    //????????????
    public void getfriendgrouplist(){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.friendgrouplist, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                PrintLogUtils.log(response, "--");
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")){
                        case 2000:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    list.clear();
                                    Gson gson = new Gson();
                                    FriendGroupListBean friendGroupListBean = gson.fromJson(response, FriendGroupListBean.class);
                                    List<FriendGroupListBean.DataBean> data = friendGroupListBean.getData();
                                        list.addAll(data);
                                    labellistAdapter.notifyDataSetChanged();
                                }
                            });
                            break;
                        case 50001:
                        case 50002:
                            EventBus.getDefault().post(new TokenFailureEvent());
                            break;
                        default:
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

    }

    //????????????
    public void getfriendgroupsort(String sort){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("sort", sort);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.friendgroupsort, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {
                    final JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")){
                        case 2000:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //EventBus.getDefault().post("fenzuliebiaoshuaxin");
                                }
                            });
                            break;
                        case 50001:
                        case 50002:
                            EventBus.getDefault().post(new TokenFailureEvent());
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

    //????????????
    public void getdelfriendgroup(String sort){
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fgid", sort);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.delfriendgroup, map, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                PrintLogUtils.log(response, "--");
                try {
                    final JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")){
                        case 2000:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        EventBus.getDefault().post("fenzuliebiaoshuaxin");
                                        ToastUtil.show(getApplicationContext(),""+object.getString("msg"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            break;
                        case 50001:
                        case 50002:
                            EventBus.getDefault().post(new TokenFailureEvent());
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

}
