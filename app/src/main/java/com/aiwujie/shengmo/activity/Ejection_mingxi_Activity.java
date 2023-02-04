package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.fragment.purserecordfragment.EjectionStampUse;
import com.aiwujie.shengmo.fragment.purserecordfragment.FragmentEjectionBuy;
import com.aiwujie.shengmo.fragment.purserecordfragment.TaRenEjectionStampUse;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.TablayoutLineWidthUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Ejection_mingxi_Activity extends AppCompatActivity {


    @BindView(R.id.iv_normal_title_back)
    ImageView ivNormalTitleBack;
    @BindView(R.id.tv_normal_title_title)
    TextView tvNormalTitleTitle;
    @BindView(R.id.iv_normal_title_more)
    ImageView ivNormalTitleMore;
    @BindView(R.id.tv_normal_title_more)
    TextView tvNormalTitleMore;
    @BindView(R.id.mStampBill_tabs)
    TabLayout mStampBillTabs;
    @BindView(R.id.mStampBill_viewpager)
    ViewPager mStampBillViewpager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_ejection_detail);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        setData();
        tvNormalTitleTitle.setText("推顶卡明细");
        ivNormalTitleMore.setVisibility(View.INVISIBLE);
        ivNormalTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setData() {
        //添加页卡标题
        mTitleList.add("购买记录");
        mTitleList.add("使用记录");
        mTitleList.add("被推记录");
        //添加页卡视图
        mViewList.add(new FragmentEjectionBuy());
        mViewList.add(new EjectionStampUse());
        mViewList.add(new TaRenEjectionStampUse());
        mStampBillTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mStampBillTabs.addTab(mStampBillTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mStampBillTabs.addTab(mStampBillTabs.newTab().setText(mTitleList.get(1)));
        mStampBillTabs.addTab(mStampBillTabs.newTab().setText(mTitleList.get(2)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(), mTitleList, mViewList);
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

//    @OnClick(R.id.mStampBill_return)
//    public void onViewClicked() {
//        finish();
//    }
}
