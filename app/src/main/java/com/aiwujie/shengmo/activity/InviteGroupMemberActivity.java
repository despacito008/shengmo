package com.aiwujie.shengmo.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.customview.CommonDialog;
import com.aiwujie.shengmo.eventbus.DialogFinishEvent;
import com.aiwujie.shengmo.eventbus.TokenFailureEvent;
import com.aiwujie.shengmo.fragment.groupinvitefragment.InviteChatFragment;
import com.aiwujie.shengmo.fragment.groupinvitefragment.InviteFollowFragment;
import com.aiwujie.shengmo.fragment.groupinvitefragment.InviteNearFragment;
import com.aiwujie.shengmo.fragment.groupinvitefragment.InviteSeeFragment;
import com.aiwujie.shengmo.http.HttpUrl;
import com.aiwujie.shengmo.net.IRequestCallback;
import com.aiwujie.shengmo.net.IRequestManager;
import com.aiwujie.shengmo.net.RequestFactory;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.TablayoutLineWidthUtils;
import com.aiwujie.shengmo.utils.ToastUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteGroupMemberActivity extends AppCompatActivity {

    @BindView(R.id.mInvite_return)
    ImageView mInviteReturn;
    @BindView(R.id.mInvite_tabs)
    TabLayout mInviteTabs;
    @BindView(R.id.mInvite_viewpager)
    ViewPager mInviteViewpager;
    @BindView(R.id.mInvite_done)
    TextView mInviteDone;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    private String groupId;
    private Handler handler=new Handler();
    private AlertDialog dialog;
    public static int inviteMemberCount=0;
    public static int inviteGroupCount=0;
    public static ArrayList<String> inviteUids=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_group_member);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        setData();
    }

    private void setData() {
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        groupId=getIntent().getStringExtra("groupId");
        //添加页卡标题
        mTitleList.add("聊天");
        mTitleList.add("附近");
        mTitleList.add("查看");
        mTitleList.add("关注");
        //添加页卡视图
        InviteChatFragment inviteChatFragment = new InviteChatFragment();
        Bundle bundle= new Bundle();
        bundle.putString("groupId",groupId);
        inviteChatFragment.setArguments(bundle);
        mViewList.add(inviteChatFragment);
        mViewList.add(new InviteNearFragment());
        mViewList.add(new InviteSeeFragment());
        mViewList.add(new InviteFollowFragment());
        mInviteTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mInviteTabs.addTab(mInviteTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mInviteTabs.addTab(mInviteTabs.newTab().setText(mTitleList.get(1)));
        mInviteTabs.addTab(mInviteTabs.newTab().setText(mTitleList.get(2)));
        mInviteTabs.addTab(mInviteTabs.newTab().setText(mTitleList.get(3)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(), mTitleList, mViewList);
        mInviteViewpager.setOffscreenPageLimit(4);
        mInviteViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mInviteTabs.setupWithViewPager(mInviteViewpager);//将TabLayout和ViewPager关联起来。
        //修改下划线的长度
        mInviteTabs.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineWidthUtils.setIndicator(mInviteTabs, 30, 30);
            }
        });
    }

    @OnClick({R.id.mInvite_return, R.id.mInvite_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mInvite_return:
                finish();
                break;
            case R.id.mInvite_done:
                showdialog();
                splitUidStrAndSendMsg();
                break;
        }
    }

    private void splitUidStrAndSendMsg() {
        String allUidStrFinal="";
        String allUidStr = InviteChatFragment.chatUidstr + "," + InviteNearFragment.nearUidstr + "," + InviteSeeFragment.seeUidstr + "," + InviteFollowFragment.followUidstr;
        String allGroupUidStr=InviteChatFragment.chatGroupIdstr;
        String[] array = allUidStr.split(",");
        ArrayList<String> allUidList = new ArrayList<>();
       //用户uid去除空字符
        for (int i=0;i<array.length;i++) {
            if(!array[i].equals("")) {
                allUidList.add(array[i]);
            }
        }
        for(int i=0;i<allUidList.size();i++){
            allUidStrFinal += allUidList.get(i) + ",";
        }
        if(allUidStrFinal.contains(",")){
            allUidStrFinal=allUidStrFinal.substring(0,allUidStrFinal.length()-1);
        }
        Map<String,String> map=new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("gid",groupId);
        map.put("uidstr",allUidStrFinal);
        map.put("gidstr",allGroupUidStr);
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.InviteOneIntoGroupNew, map, new IRequestCallback() {
            @Override
            public void onSuccess(final String response) {
//                Log.i("inviteGroupResponse", "onSuccess: "+response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj=new JSONObject(response);
                            switch (obj.getInt("retcode")){
                                case 2000:
                                    dialog.dismiss();
                                    new CommonDialog(InviteGroupMemberActivity.this,obj.getString("msg"));
                                    break;
                                case 4001:
                                case 4002:
                                    dialog.dismiss();
                                    ToastUtil.show(getApplicationContext(),obj.getString("msg"));
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(DialogFinishEvent event) {
        finish();
    }


    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ProgressBar bar = new ProgressBar(this);
        builder.setTitle("请稍后...")
                .setCancelable(false)
                .setView(bar);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InviteChatFragment.chatUidstr="";
        InviteChatFragment.chatGroupIdstr="";
        InviteNearFragment.nearUidstr="";
        InviteSeeFragment.seeUidstr="";
        InviteFollowFragment.followUidstr="";
        inviteUids.clear();
        inviteGroupCount=0;
        inviteMemberCount=0;
        EventBus.getDefault().unregister(this);
    }
}
