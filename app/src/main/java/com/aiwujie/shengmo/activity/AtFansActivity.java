package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.bean.Atbean;
import com.aiwujie.shengmo.fragment.at.FragmentatFensi;
import com.aiwujie.shengmo.fragment.at.FragmentatGuanzhu;
import com.aiwujie.shengmo.fragment.at.ShareChatPeopleFragment;
import com.aiwujie.shengmo.fragment.at.ShareGroupFragment;
import com.aiwujie.shengmo.utils.TablayoutLineWidthUtils;
import com.aiwujie.shengmo.utils.ToastUtil;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AtFansActivity extends AppCompatActivity {

    @BindView(R.id.mAt_fans_cancel)
    TextView mAtFansCancel;
    @BindView(R.id.mAt_fans_confirm)
    TextView mAtFansConfirm;
    @BindView(R.id.mStampBill_tabs)
    TabLayout mStampBillTabs;
    @BindView(R.id.mStampBill_viewpager)
    ViewPager mStampBillViewpager;

    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    List<Atbean.DataBean> dataBeanList = new ArrayList<>();
    private List<Atbean.DataBean> dataBean1 = new ArrayList<>();
    private List<Atbean.DataBean> dataBean2 = new ArrayList<>();
    private List<Atbean.DataBean> dataBean3 = new ArrayList<>();
    private List<Atbean.DataBean> dataBean4 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_fans);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setData();
        //setListener();
    }

    private void setData() {
        //添加页卡标题
        mTitleList.add("聊天");
        mTitleList.add("关注");
        mTitleList.add("粉丝");
        mTitleList.add("群组");
        //添加页卡视图
        mViewList.add(new ShareChatPeopleFragment());
        mViewList.add(new FragmentatGuanzhu());
        mViewList.add(new FragmentatFensi());
        mViewList.add(ShareGroupFragment.newInstance());
        mStampBillTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mStampBillTabs.addTab(mStampBillTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mStampBillTabs.addTab(mStampBillTabs.newTab().setText(mTitleList.get(1)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(),mTitleList,mViewList);
        mStampBillViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mStampBillViewpager.setOffscreenPageLimit(3);
        mStampBillTabs.setupWithViewPager(mStampBillViewpager);//将TabLayout和ViewPager关联起来。
        //修改下划线的长度
        mStampBillTabs.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineWidthUtils.setIndicator(mStampBillTabs, 20, 20);
            }
        });
    }

    @OnClick({R.id.mAt_fans_cancel, R.id.mAt_fans_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mAt_fans_cancel:
                finish();
                break;
            case R.id.mAt_fans_confirm:
                dataBeanList.addAll(dataBean1);
                dataBeanList.addAll(dataBean2);
                dataBeanList.addAll(dataBean3);
                dataBeanList.addAll(dataBean4);
                if(dataBeanList.size()==0){
                    ToastUtil.show(getApplicationContext(),"请选择要@的人...");
                }else {
                    List<Atbean.DataBean> list = removeDuplicate(dataBeanList);
                    Atbean atbean = new Atbean();
                    atbean.setDataBean(list);
                    Message obtain = Message.obtain();
                    if("commentDetail".equals(getIntent().getStringExtra("type"))) {
                        obtain.arg2 = 10001;
                    } else {
                        obtain.arg2 = 11;
                    }
                    obtain.obj=atbean;
                    EventBus.getDefault().post(obtain);

                    String uidstr="";
                    for (int i = 0; i < list.size(); i++) {
                        uidstr+=list.get(i).getUid()+",";
                    }
                    String unamestr="";
                    for (int i = 0; i < list.size(); i++) {
                        unamestr+=list.get(i).getNickname()+",";
                    }
                    Intent intent = new Intent();
                    intent.putExtra("atStr", uidstr);
                    intent.putExtra("atNameStr", unamestr);
                    intent.putExtra("atSize", list.size()+"");
                    setResult(100, intent);
                    finish();
                }
               /* //多选模式下，获取listview选中的集合
                SparseBooleanArray booleanArray = mAtFansListview.getRefreshableView().getCheckedItemPositions();
                if ((booleanArray.size() != 0)) {
                List<Atbean.DataBean> dataBeanList = new ArrayList<>();
                    String uidstr="";
                    for (int j = 0; j < booleanArray.size(); j++) {
                        int key = booleanArray.keyAt(j);
                        //放入SparseBooleanArray，未必选中
                        if (booleanArray.get(key)) {
                            //这样mAdapter.getItem(key)就是选中的项
//                            String json = new Gson ().toJson(fansdatas.get(key-1).getUserInfo());
                            uidstr+=fansdatas.get(key-1).getUid()+",";
                            atsize=atsize+1;

                            String uid = fansdatas.get(key - 1).getUid();
                            String uid1 = fansdatas.get(key - 1).getUserInfo().getNickname();
                            Atbean.DataBean dataBean2 = new Atbean.DataBean();
                            dataBean2.setUid(uid);
                            dataBean2.setNickname(uid1);
                            dataBeanList.add(dataBean2);

                        } else {
                            //这里是用户刚开始选中，后取消选中的项
//                        Log.d("atFansSelectedKey", "" + key + ": false");
                        }
                    }
                    Atbean atbean = new Atbean();
                    atbean.setDataBean(dataBeanList);
                    Message obtain = Message.obtain();
                    obtain.arg2=11;
                    obtain.obj=atbean;
                    EventBus.getDefault().post(obtain);

                    uidstr=uidstr.substring(0,uidstr.length()-1);
                    Intent intent = new Intent();
                    intent.putExtra("atStr", uidstr);
                    intent.putExtra("atSize", atsize+"");
                    setResult(100, intent);
                    finish();
                }else{
                    ToastUtil.show(getApplicationContext(),"请选择要@的人...");
                }*/
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getban(Message message){
        if (message.arg2==12){
            dataBean1.clear();
            Atbean dataBean = (Atbean) message.obj;
            dataBean1.addAll(dataBean.getDataBean());
        }else if (message.arg2==13){
            dataBean2.clear();
            Atbean dataBean = (Atbean) message.obj;
            dataBean2.addAll(dataBean.getDataBean());
        }else if (message.arg2==16){
            dataBean3.clear();
            Atbean dataBean = (Atbean) message.obj;
            dataBean3.addAll(dataBean.getDataBean());
        } else if (message.arg2 == 20) {
            dataBean4.clear();
            Atbean dataBean = (Atbean) message.obj;
            dataBean4.addAll(dataBean.getDataBean());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public   static   List  removeDuplicate(List<Atbean.DataBean> list)  {
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
                if  (list.get(j).getNickname().equals(list.get(i).getNickname()))  {
                    list.remove(j);
                }
            }
        }
        return list;
    }

}
