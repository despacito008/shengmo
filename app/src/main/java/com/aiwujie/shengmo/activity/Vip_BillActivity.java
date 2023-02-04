package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.fragment.purserecordfragment.FragmentVipGmGive;
import com.aiwujie.shengmo.fragment.purserecordfragment.FragmentVipHzGive;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.TablayoutLineWidthUtils;
import com.aiwujie.shengmo.utils.UpLocationUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Vip_BillActivity extends AppCompatActivity {

    @BindView(R.id.mBill_return)
    ImageView mBillReturn;
    @BindView(R.id.mRechargeBill_tabs)
    TabLayout mRechargeBillTabs;
    @BindView(R.id.mRechargeBill_viewpager)
    ViewPager mRechargeBillViewpager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip__bill);
        ButterKnife.bind(this);
       // X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        setData();
    }

    private void setData() {
        //添加页卡标题
        mTitleList.add("购买记录");
        mTitleList.add("获赠记录");
        //添加页卡视图
        mViewList.add(new FragmentVipGmGive());
        mViewList.add(new FragmentVipHzGive());
        mRechargeBillTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mRechargeBillTabs.addTab(mRechargeBillTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mRechargeBillTabs.addTab(mRechargeBillTabs.newTab().setText(mTitleList.get(1)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(),mTitleList,mViewList);
        mRechargeBillViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mRechargeBillTabs.setupWithViewPager(mRechargeBillViewpager);//将TabLayout和ViewPager关联起来。
        //修改下划线的长度
        mRechargeBillTabs.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineWidthUtils.setIndicator(mRechargeBillTabs, 60, 60);
            }
        });
    }


    @OnClick(R.id.mBill_return)
    public void onClick() {
        finish();
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }



    @Override
    protected void onStop() {
        super.onStop();
        UpLocationUtils.LogintimeAndLocation();
    }
}
