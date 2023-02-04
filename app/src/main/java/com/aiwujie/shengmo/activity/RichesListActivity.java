package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.fragment.rankinglistfragment.RichesListFragment;
import com.aiwujie.shengmo.fragment.rankinglistfragment.VulgartycoonListFragment;
import com.aiwujie.shengmo.utils.TablayoutLineWidthUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RichesListActivity extends FragmentActivity {

    @BindView(R.id.mRichesList_return)
    ImageView mRichesListReturn;
    @BindView(R.id.mRichesList_tabs)
    TabLayout mRichesListTabs;
    @BindView(R.id.mRichesList_viewpager)
    ViewPager mRichesListViewpager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riches_list);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.purple_main);
        setData();
    }

    private void setData() {
        //添加页卡标题
        mTitleList.add("富豪榜");
        mTitleList.add("魅力榜");
        //添加页卡视图
        mViewList.add(new VulgartycoonListFragment());
        mViewList.add(new RichesListFragment());
        mRichesListTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mRichesListTabs.addTab(mRichesListTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mRichesListTabs.addTab(mRichesListTabs.newTab().setText(mTitleList.get(1)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(),mTitleList,mViewList);
        mRichesListViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mRichesListTabs.setupWithViewPager(mRichesListViewpager);//将TabLayout和ViewPager关联起来。
        //修改下划线的长度
        mRichesListTabs.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineWidthUtils.setIndicator(mRichesListTabs, 60, 60);
            }
        });
    }

    @OnClick(R.id.mRichesList_return)
    public void onClick() {
        finish();
    }

}