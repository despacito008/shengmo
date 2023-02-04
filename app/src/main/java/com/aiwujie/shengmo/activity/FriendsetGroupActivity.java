package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.FriendGroupListBean;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.PrintLogUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.google.gson.Gson;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FriendsetGroupActivity extends AppCompatActivity {

    private TagFlowLayout tagFlowLayout;
    List<String> mVals = new ArrayList<>();
    Handler handler = new Handler();
    private TagAdapter<String> tagAdapter;
    String astr = "";
    private String fuid;
    private List<FriendGroupListBean.DataBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendset_group);
        StatusBarUtil.showLightStatusBar(this);
        Intent intent = getIntent();
        fuid = intent.getStringExtra("fuid");

        final LayoutInflater mInflater = LayoutInflater.from(this);
        tagFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        tagAdapter = new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv_liu_item,
                        tagFlowLayout, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                getdelfgusers(data.get(position).getId(), fuid);
            }

            @Override
            public boolean setSelected(int position, String s) {
                return super.setSelected(position, s);
            }
        };

        TextView mStamp_zhangdan = (TextView) findViewById(R.id.mStamp_zhangdan);
        mStamp_zhangdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(FriendsetGroupActivity.this).builder()
                        .setTitle("新建分组")
                        .setEditText("");
                myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!myAlertInputDialog.getResult().equals("")) {
                            int length = myAlertInputDialog.getResult().length();
                            if (length > 4) {
                                ToastUtil.show(FriendsetGroupActivity.this, "分组名称限四字以内！");
                            } else {
                                getaddfriendgroup(myAlertInputDialog.getResult());
                                myAlertInputDialog.dismiss();
                            }

                        } else {
                            ToastUtil.show(FriendsetGroupActivity.this, "分组名称不能为空");
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
        });

        tagFlowLayout.setAdapter(tagAdapter);
        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                astr = "";
                Iterator<Integer> it = selectPosSet.iterator();
                while (it.hasNext()) {
                    Integer str = it.next();
                    String s = data.get(str).getId();
                    astr += s + ",";
                }
                if (selectPosSet.size() > 0) {
                    astr = astr.substring(0, astr.length() - 1);
                }
            }
        });

        ImageView mStamp_return = (ImageView) findViewById(R.id.mStamp_return);
        mStamp_return.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getsetfgsusers();
            }
        });
        getfriendgrouplist();

    }


    //查询分组
    public void getfriendgrouplist() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", fuid);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.friendgrouplist, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                PrintLogUtils.log(response, "--");
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Gson gson = new Gson();
                                    FriendGroupListBean friendGroupListBean = gson.fromJson(response, FriendGroupListBean.class);
                                    data = friendGroupListBean.getData();
                                    Set<Integer> strings = new HashSet<>();
                                    mVals.clear();
                                    for (int i = 0; i < data.size(); i++) {
                                        mVals.add(data.get(i).getFgname());
                                        if (data.get(i).getIs_select().equals("1")) {
                                            strings.add(i);
                                        }
                                    }
                                    tagAdapter.notifyDataChanged();
                                    tagAdapter.setSelectedList(strings);

                                }
                            });
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        getsetfgsusers();
    }

    //添加一个好友到多个分组
    public void getsetfgsusers() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fuid", fuid);
        map.put("fgid", astr);
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.post(HttpUrl.setfgsusers, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                PrintLogUtils.log(response, "--");
                try {
                    JSONObject object = new JSONObject(response);
                    switch (object.getInt("retcode")) {
                        case 2000:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post("gerenzhuyefenzushuaxin");
                                }
                            });

                            break;
                        default:
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    setResult(200);
                    finish();
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                finish();
            }
        });
    }

    //将好友移除分组
    public void getdelfgusers(String fgid, String fuid) {
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
                    switch (object.getInt("retcode")) {
                        case 2000:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post("gerenzhuyefenzushuaxin");
                                }
                            });
                            break;
                        default:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        ToastUtil.show(getApplicationContext(), "" + object.getString("msg"));
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

    //新建分组
    public void getaddfriendgroup(String fgname) {
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
                    switch (object.getInt("retcode")) {
                        case 2000:
                            getfriendgrouplist();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        EventBus.getDefault().post("fenzuliebiaoshuaxin");
                                        ToastUtil.show(FriendsetGroupActivity.this, "" + object.getString("msg"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            break;
                        default:
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        ToastUtil.show(FriendsetGroupActivity.this, "" + object.getString("msg"));
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
