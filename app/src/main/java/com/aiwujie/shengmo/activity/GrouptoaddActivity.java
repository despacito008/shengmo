package com.aiwujie.shengmo.activity;

import android.app.AlertDialog;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.aiwujie.shengmo.fragment.groupinvitefragment.InviteChatPeopleFragment;
import com.aiwujie.shengmo.fragment.groupinvitefragment.InviteFensiFragment;
import com.aiwujie.shengmo.fragment.groupinvitefragment.InviteFollow2Fragment;
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

public class GrouptoaddActivity extends AppCompatActivity {

    @BindView(R.id.mInvite_return)
    ImageView mInviteReturn;
    @BindView(R.id.mInvite_tabs)
    TabLayout mInviteTabs;
    @BindView(R.id.mInvite_viewpager)
    ViewPager mInviteViewpager;
    @BindView(R.id.mInvite_done)
    TextView mInviteDone;
    private List<String> mTitleList = new ArrayList<>();//??????????????????
    private List<Fragment> mViewList = new ArrayList<>();//??????????????????
    private String fgid;
    private Handler handler=new Handler();
    private AlertDialog dialog;
    public static int inviteMemberCount=0;
    public static int inviteGroupCount=0;
    public static ArrayList<String> inviteUids=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouptoadd);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        setData();
    }

    private void setData() {
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        fgid=getIntent().getStringExtra("fgid");
        //??????????????????
        mTitleList.add("??????");
        mTitleList.add("??????");
        mTitleList.add("??????");
        //??????????????????
        mViewList.add(new InviteChatPeopleFragment());
        mViewList.add(new InviteFollow2Fragment());
        mViewList.add(new InviteFensiFragment());

        mInviteTabs.setTabMode(TabLayout.MODE_FIXED);//??????tab????????????????????????????????????
        mInviteTabs.addTab(mInviteTabs.newTab().setText(mTitleList.get(0)));//??????tab?????????
        mInviteTabs.addTab(mInviteTabs.newTab().setText(mTitleList.get(1)));
        mInviteTabs.addTab(mInviteTabs.newTab().setText(mTitleList.get(2)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(), mTitleList, mViewList);
        mInviteViewpager.setOffscreenPageLimit(4);
        mInviteViewpager.setAdapter(mAdapter);//???ViewPager???????????????
        mInviteTabs.setupWithViewPager(mInviteViewpager);//???TabLayout???ViewPager???????????????
        //????????????????????????
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

                splitUidStrAndSendMsg();
                break;
        }
    }

    private void splitUidStrAndSendMsg() {
        String allUidStrFinal="";
     /*   String allUidStr = InviteChatPeopleFragment.chatUidstr + "," + InviteFensiFragment.fensiUidstr + "," + InviteFollow2Fragment.followUidstr;
        String[] array = allUidStr.split(",");
        ArrayList<String> allUidList = new ArrayList<>();
        //??????uid???????????????
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
        }*/

        for (int i = 0; i < inviteUids.size(); i++) {
            allUidStrFinal+=inviteUids.get(i)+",";
        }
        if (inviteUids.size()>0){
            showdialog();
            allUidStrFinal=allUidStrFinal.substring(0,allUidStrFinal.length()-1);
        }else {
            ToastUtil.show(GrouptoaddActivity.this,"???????????????");
            return;
        }

        Map<String,String> map=new HashMap<>();
        map.put("uid", MyApp.uid);
        map.put("fgid",fgid);
        map.put("fuid",allUidStrFinal);
        IRequestManager manager= RequestFactory.getRequestManager();
        manager.post(HttpUrl.setfgusers, map, new IRequestCallback() {
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
                                    EventBus.getDefault().post("fenzuhaoyouliebiaoshuaxin");
                                    new CommonDialog(GrouptoaddActivity.this,obj.getString("msg"));
                                    break;
                                case 50001:
                                case 50002:
                                    EventBus.getDefault().post(new TokenFailureEvent());
                                    break;
                                default:
                                    dialog.dismiss();
                                    ToastUtil.show(getApplicationContext(),obj.getString("msg"));
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
        builder.setTitle("?????????...")
                .setCancelable(false)
                .setView(bar);
        dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InviteChatPeopleFragment.chatUidstr="";
        InviteFollow2Fragment.followUidstr="";
        InviteFensiFragment.fensiUidstr="";
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
