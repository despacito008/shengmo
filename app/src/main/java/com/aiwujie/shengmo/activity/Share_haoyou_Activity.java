package com.aiwujie.shengmo.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.fragment.share.FragmentSharefensi;
import com.aiwujie.shengmo.fragment.share.FragmentShareguanzhu;
import com.aiwujie.shengmo.fragment.share.FragmentSharehaoyou;
import com.aiwujie.shengmo.fragment.share.FragmentSharequnzu;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Share_haoyou_Activity extends AppCompatActivity{

    @BindView(R.id.mAt_fans_cancel)
    TextView mAtFansCancel;
    @BindView(R.id.mStampBill_tabs)
    TabLayout mStampBillTabs;
    @BindView(R.id.mStampBill_viewpager)
    ViewPager mStampBillViewpager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_haoyou_);
        ButterKnife.bind(this);
       // X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        setData();

    }
    private void setData() {
        //添加页卡标题
        mTitleList.add("关注(SVIP)");
        mTitleList.add("粉丝(SVIP)");
        mTitleList.add("好友");
        mTitleList.add("群组");
        //添加页卡视图
        mViewList.add(new FragmentShareguanzhu());
        mViewList.add(new FragmentSharefensi());
        mViewList.add(new FragmentSharehaoyou());
        mViewList.add(new FragmentSharequnzu());
        mStampBillTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mStampBillTabs.addTab(mStampBillTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mStampBillTabs.addTab(mStampBillTabs.newTab().setText(mTitleList.get(1)));
        mStampBillTabs.addTab(mStampBillTabs.newTab().setText(mTitleList.get(2)));
        mStampBillTabs.addTab(mStampBillTabs.newTab().setText(mTitleList.get(3)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(),mTitleList,mViewList);
        mStampBillViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mStampBillViewpager.setCurrentItem(2);
        mStampBillViewpager.setOffscreenPageLimit(3);
        mStampBillTabs.setupWithViewPager(mStampBillViewpager);//将TabLayout和ViewPager关联起来。
     /*   //修改下划线的长度
        mStampBillTabs.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineWidthUtils.setIndicator(mStampBillTabs, 20, 20);
            }
        });*/
    }
    @OnClick(R.id.mAt_fans_cancel)
    public void onViewClicked() {
        finish();
    }


}
