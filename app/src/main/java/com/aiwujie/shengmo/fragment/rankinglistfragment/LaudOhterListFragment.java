package com.aiwujie.shengmo.fragment.rankinglistfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/5/12.
 */

public class LaudOhterListFragment extends Fragment {
    @BindView(R.id.item_fragment_rank_tabs)
    TabLayout itemFragmentRankTabs;
    @BindView(R.id.item_fragment_rank_viewpager)
    ViewPager itemFragmentRankViewpager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_rank_list, null);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }
    private void setData() {
        //添加页卡标题
        mTitleList.add("日榜");
        mTitleList.add("周榜");
        mTitleList.add("月榜");
        mTitleList.add("总榜");
        //添加页卡视图
        for (int i = 0; i < mTitleList.size(); i++) {
            mViewList.add(LaudOhterDayWeekMonthListFragment.newInstance(i));
        }
        itemFragmentRankTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        itemFragmentRankTabs.addTab(itemFragmentRankTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        itemFragmentRankTabs.addTab(itemFragmentRankTabs.newTab().setText(mTitleList.get(1)));
        itemFragmentRankTabs.addTab(itemFragmentRankTabs.newTab().setText(mTitleList.get(2)));
        itemFragmentRankTabs.addTab(itemFragmentRankTabs.newTab().setText(mTitleList.get(3)));
        itemFragmentRankViewpager.setOffscreenPageLimit(4);
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getChildFragmentManager(),mTitleList,mViewList);
        itemFragmentRankViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        itemFragmentRankTabs.setupWithViewPager(itemFragmentRankViewpager);//将TabLayout和ViewPager关联起来。

    }

}
