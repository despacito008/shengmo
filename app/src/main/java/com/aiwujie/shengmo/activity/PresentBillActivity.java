package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.fragment.purserecordfragment.FragmentPresentExchange;
import com.aiwujie.shengmo.fragment.purserecordfragment.FragmentPresentReceive;
import com.aiwujie.shengmo.fragment.purserecordfragment.FragmentPresentSystem;
import com.aiwujie.shengmo.fragment.purserecordfragment.FragmentPresnetExchangeEx;
import com.aiwujie.shengmo.kt.ui.fragment.ReceiveGiftDetailFragment;
import com.aiwujie.shengmo.kt.ui.fragment.SystemGiftDetailsFragment;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.TablayoutLineWidthUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PresentBillActivity extends AppCompatActivity {

    @BindView(R.id.mPresentBill_return)
    ImageView mPresentBillReturn;
    @BindView(R.id.mPresentBill_tabs)
    TabLayout mPresentBillTabs;
    @BindView(R.id.mPresentBill_viewpager)
    ViewPager mPresentBillViewpager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_bill);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        setData();
    }
    private void setData() {
        //添加页卡标题
        mTitleList.add("收到的礼物");
        mTitleList.add("系统赠送");
        mTitleList.add("兑换记录");
        //添加页卡视图
        //mViewList.add(new FragmentPresentReceive());
       // mViewList.add(new FragmentPresentSystem());
        mViewList.add(new ReceiveGiftDetailFragment());
        mViewList.add(new SystemGiftDetailsFragment());
        mViewList.add(new FragmentPresnetExchangeEx());
        mPresentBillTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mPresentBillTabs.addTab(mPresentBillTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mPresentBillTabs.addTab(mPresentBillTabs.newTab().setText(mTitleList.get(1)));
        mPresentBillTabs.addTab(mPresentBillTabs.newTab().setText(mTitleList.get(2)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(),mTitleList,mViewList);
        mPresentBillViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mPresentBillViewpager.setOffscreenPageLimit(3);
        mPresentBillTabs.setupWithViewPager(mPresentBillViewpager);//将TabLayout和ViewPager关联起来。
        //修改下划线的长度
        mPresentBillTabs.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineWidthUtils.setIndicator(mPresentBillTabs, 20, 20);
            }
        });
    }

    @OnClick(R.id.mPresentBill_return)
    public void onViewClicked() {
        finish();
    }
}
