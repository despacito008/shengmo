package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.fragment.rankinglistfragment.CommentListFragment;
import com.aiwujie.shengmo.fragment.rankinglistfragment.LaudListFragment;
import com.aiwujie.shengmo.fragment.rankinglistfragment.RecommendListFragment;
import com.aiwujie.shengmo.utils.TablayoutLineWidthUtils;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PopularityActivity extends AppCompatActivity {

    @BindView(R.id.mPopularity_return)
    ImageView mPopularityReturn;
    @BindView(R.id.mPopularity_tabs)
    TabLayout mPopularityTabs;
    @BindView(R.id.mPopularity_viewpager)
    ViewPager mPopularityViewpager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popularity);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.purple_main);
        setData();
    }
    private void setData() {
        //添加页卡标题
        mTitleList.add("热评榜");
        mTitleList.add("热赞榜");
        mTitleList.add("热推榜");
        //添加页卡视图
        mViewList.add(new CommentListFragment());
        mViewList.add(new LaudListFragment());
        mViewList.add(new RecommendListFragment());
        mPopularityTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mPopularityTabs.addTab(mPopularityTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mPopularityTabs.addTab(mPopularityTabs.newTab().setText(mTitleList.get(1)));
        mPopularityTabs.addTab(mPopularityTabs.newTab().setText(mTitleList.get(2)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(),mTitleList,mViewList);
        mPopularityViewpager.setOffscreenPageLimit(3);
        mPopularityViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mPopularityTabs.setupWithViewPager(mPopularityViewpager);//将TabLayout和ViewPager关联起来。
        //修改下划线的长度
        mPopularityTabs.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineWidthUtils.setIndicator(mPopularityTabs, 30, 30);
            }
        });
    }

    @OnClick(R.id.mPopularity_return)
    public void onClick() {
        finish();
    }

}
