package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.fragment.rankinglistfragment.CommentOhterListFragment;
import com.aiwujie.shengmo.fragment.rankinglistfragment.DynamicListFragment;
import com.aiwujie.shengmo.fragment.rankinglistfragment.LaudOhterListFragment;
import com.aiwujie.shengmo.utils.TablayoutLineWidthUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActiveActivity extends AppCompatActivity {

    @BindView(R.id.mActive_return)
    ImageView mActiveReturn;
    @BindView(R.id.mActive_tabs)
    TabLayout mActiveTabs;
    @BindView(R.id.mActive_viewpager)
    ViewPager mActiveViewpager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.purple_main);
        setData();
    }
    private void setData() {
        //添加页卡标题
        mTitleList.add("点评榜");
        mTitleList.add("点赞榜");
        mTitleList.add("动态榜");
        //添加页卡视图
        mViewList.add(new CommentOhterListFragment());
        mViewList.add(new LaudOhterListFragment());
        mViewList.add(new DynamicListFragment());
        mActiveTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mActiveTabs.addTab(mActiveTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mActiveTabs.addTab(mActiveTabs.newTab().setText(mTitleList.get(1)));
        mActiveTabs.addTab(mActiveTabs.newTab().setText(mTitleList.get(2)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(),mTitleList,mViewList);
        mActiveViewpager.setOffscreenPageLimit(3);
        mActiveViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mActiveTabs.setupWithViewPager(mActiveViewpager);//将TabLayout和ViewPager关联起来。
        //修改下划线的长度
        mActiveTabs.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineWidthUtils.setIndicator(mActiveTabs, 30, 30);
            }
        });

    }

    @OnClick(R.id.mActive_return)
    public void onClick() {
        finish();
    }
}
