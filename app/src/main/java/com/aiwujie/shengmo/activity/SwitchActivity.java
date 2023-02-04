package com.aiwujie.shengmo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.SwitchUserAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.bean.SwitchMarkBean;
import com.aiwujie.shengmo.bean.Three_loginBean;
import com.aiwujie.shengmo.dao.DaoSession;
import com.aiwujie.shengmo.dao.SwitchMarkBeanDao;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.kt.ui.activity.HomePageActivity;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.tim.chat.ChatActivity;
import com.aiwujie.shengmo.tim.helper.ChatManagerHelper;
import com.aiwujie.shengmo.utils.FilterGroupUtils;
import com.aiwujie.shengmo.utils.GetDeviceIdUtils;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.view.VerifyAccountPop;
import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SwitchActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView addhao;
    private ListView listView;
    private List<SwitchMarkBean> switchMarkBeans;
    private SwitchUserAdapter switchUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        StatusBarUtil.showLightStatusBar(this);
        addhao = (TextView) findViewById(R.id.addhao);
        addhao.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.lvya);
        ImageView mAlert_Setting_return = (ImageView) findViewById(R.id.mAlert_Setting_return);
        mAlert_Setting_return.setOnClickListener(this);
        DaoSession daoSession = MyApp.getSwitchMarkBeanDao();
        SwitchMarkBeanDao switchMarkBeanDao = daoSession.getSwitchMarkBeanDao();
        switchMarkBeans = switchMarkBeanDao.loadAll();
        if (switchMarkBeans.size() >= 5 && !"1".equals(MyApp.isAdmin)) {
            addhao.setVisibility(View.GONE);
        }
        switchUserAdapter = new SwitchUserAdapter(this, switchMarkBeans);
        listView.setAdapter(switchUserAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                if (String.valueOf(switchMarkBeans.get(position).getUid()).equals(MyApp.uid)) {
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(SwitchActivity.this);
                builder.setTitle("删除账号");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DaoSession daoSession = MyApp.getSwitchMarkBeanDao();
                        SwitchMarkBeanDao switchMarkBeanDao = daoSession.getSwitchMarkBeanDao();
                        switchMarkBeanDao.delete(switchMarkBeans.get(position));
                        switchMarkBeans.remove(position);
                        switchUserAdapter = new SwitchUserAdapter(SwitchActivity.this, switchMarkBeans);
                        listView.setAdapter(switchUserAdapter);
                        refreshCurrentUserIndex();
                    }
                });
                builder.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (String.valueOf(switchMarkBeans.get(position).getUid()).equals(MyApp.uid)) {
                    return;
                }
                SwitchMarkBean switchMarkBean = switchMarkBeans.get(position);
                String swiuid = String.valueOf(switchMarkBean.getUid());
                cancelLaud(position, swiuid);
                //RongCloudEvent.connect(MyApp.token, SwitchActivity.this);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        DaoSession daoSession = MyApp.getSwitchMarkBeanDao();
        SwitchMarkBeanDao switchMarkBeanDao = daoSession.getSwitchMarkBeanDao();
        switchMarkBeans = switchMarkBeanDao.loadAll();
        switchUserAdapter = new SwitchUserAdapter(SwitchActivity.this, switchMarkBeans);
        listView.setAdapter(switchUserAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addhao:
                Intent intent = new Intent(this, AddUserActivity.class);
                startActivity(intent);
                break;
            case R.id.mAlert_Setting_return:
                finish();
                break;
        }
    }

    private void cancelLaud(final int pos, String swiuid) {
        Map<String, String> map = new HashMap<>();
        map.put("uid", swiuid);
        map.put("device_token", GetDeviceIdUtils.getSN(this));
        IRequestManager manager = RequestFactory.getRequestManager();
        manager.post(HttpUrl.login_third, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
                Log.i("cancelDynamic", "onSuccess: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    switch (obj.getInt("retcode")) {
                        case 2001:
                            break;
                        case 2000:
                            SwitchMarkBean switchMarkBean = switchMarkBeans.get(pos);
                            MyApp.uid = String.valueOf(switchMarkBean.getUid());
                            MyApp.token = switchMarkBean.getT_sign();
                            SharedPreferencesUtils.setParam(getApplicationContext(),"t_sign", MyApp.token);
                            String sex = switchMarkBean.getSex();
                            String sexual = switchMarkBean.getSexual();
                            //禁言状态
                            //SharedPreferencesUtils.setParam(getApplicationContext(),"nospeak",obj1.getString("chatstatus"));
                            //筛选
                            SharedPreferencesUtils.setParam(getApplicationContext(), "mysex", sex);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "mysexual", sexual);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSex", sexual);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSexual", sex);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSex", sex);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSexual", sexual);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "uid", MyApp.uid);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "token", MyApp.token);
                            if (!TextUtil.isEmpty(switchMarkBean.getUser_id())) {
                                SharedPreferencesUtils.setParam(getApplicationContext(), "username", switchMarkBean.getUser_id());
                                SharedPreferencesUtils.setParam(getApplicationContext(), "url_token", switchMarkBean.getUrl_token());
                            }
                            //更新性取向
                            String whatSexual = FilterGroupUtils.isWhatSexual(sex, sexual);
                            SharedPreferencesUtils.setParam(getApplicationContext(), "groupFlag", whatSexual);
                            SharedPreferencesUtils.setParam(SwitchActivity.this,"current_user_index",pos);
                            // switchUserAdapter.notifyDataSetChanged();
                            // RongIM.getInstance().logout();
                            //清空会话列表
                            ConversationManagerKit.getInstance().destroyConversation();
                            TUIKit.logout(new IUIKitCallBack() {
                                @Override
                                public void onSuccess(Object data) {
                                    //Intent intent = new Intent(SwitchActivity.this, MainActivity.class);
                                    Intent intent = new Intent(SwitchActivity.this, HomePageActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

                                @Override
                                public void onError(String module, int errCode, String errMsg) {
                                    //Intent intent = new Intent(SwitchActivity.this, MainActivity.class);
                                    Intent intent = new Intent(SwitchActivity.this, HomePageActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });
//
//                                    Gson gson = new Gson();
//                                    Three_loginBean three_loginBean = gson.fromJson(response, Three_loginBean.class);
//                                    Three_loginBean.DataBean data = three_loginBean.getData();
//
//                                    DaoSession daoSession = MyApp.getSwitchMarkBeanDao();
//                                    SwitchMarkBeanDao switchMarkBeanDao = daoSession.getSwitchMarkBeanDao();
//                                    List<SwitchMarkBean> switchMarkBeans = switchMarkBeanDao.loadAll();
//                                    SwitchMarkBean switchMarkBean1 = switchMarkBeans.get(pos);
//                                    switchMarkBean1.setUid(Long.valueOf(MyApp.uid));
//                                    switchMarkBean1.setHeadimage(data.getMatch_photo());
//                                    switchMarkBean1.setUser_name(data.getNickname());
//                                    MyApp.token = data.getR_token();
//                                    switchMarkBean1.setR_token(MyApp.token);
//                                    switchMarkBean1.setSex(data.getSex());
//                                    switchMarkBean1.setSexual(data.getSexual());
//                                    switchMarkBeanDao.insertOrReplace(switchMarkBean1);


                            break;
                        case 4002:
                            ToastUtil.show(SwitchActivity.this, "账号状态变更，请重新登录");
                            //重置数据
                            MyApp.uid = "";
                            MyApp.token = "";
                            SharedPreferencesUtils.setParam(getApplicationContext(), "mysex", "");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "mysexual", "");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSex", "");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "mydynamicSexual", "");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSex", "");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "mygroupSexual", "");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "uid", "");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "token", "");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "username", "");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "username", "");
                            SharedPreferencesUtils.setParam(getApplicationContext(), "url_token", "");
                            Intent loginIntent = new Intent(SwitchActivity.this, LoginActivity.class);
                            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginIntent);
                            //switchUserAdapter.notifyDataSetChanged();
                            // RongIM.getInstance().logout();
                            TUIKit.logout(new IUIKitCallBack() {
                                @Override
                                public void onSuccess(Object data) {

                                }

                                @Override
                                public void onError(String module, int errCode, String errMsg) {

                                }
                            });

                            break;
                        default:
                            ToastUtil.show(getApplicationContext(), obj.getString("msg"));
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

    void refreshCurrentUserIndex() {
        for (int i = 0; i < switchMarkBeans.size(); i++) {
            if (switchMarkBeans.get(i).getUser_id().equals(MyApp.uid)) {
                SharedPreferencesUtils.setParam(SwitchActivity.this,"current_user_index",i);
            }
        }
    }

}
