package com.aiwujie.shengmo.fragment.purserecordfragment;

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
 * Created by 290243232 on 2017/1/11.
 */
public class FragmentPresentExchange extends Fragment {


    @BindView(R.id.mPresentExchange_tabs)
    TabLayout mPresentExchangeTabs;
    @BindView(R.id.mPresentExchange_viewpager)
    ViewPager mPresentExchangeViewpager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    /**
     * 是否可见状态
     */
    private boolean isVisible;
    /**
     * 标志位，View已经初始化完成。
     * 2016/04/29
     * 用isAdded()属性代替
     * 2016/05/03
     * isPrepared还是准一些,isAdded有可能出现onCreateView没走完但是isAdded了
     */
    private boolean isPrepared;
    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;
    /**
     * 判断是否继续刷新
     */
    private boolean isReresh = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_present_exchange, null);
        ButterKnife.bind(this, view);
        setData();
//        isPrepared = true;
//        lazyLoad();
        return view;
    }

    private void setData() {
        //添加页卡标题
        mTitleList.add("兑换");
        mTitleList.add("其他");
        //添加页卡视图
        mViewList.add(new FragmentPresnetExchangeEx());
        mViewList.add(new FragmentPresentExchangeWithdrawals());
        mPresentExchangeTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mPresentExchangeTabs.addTab(mPresentExchangeTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mPresentExchangeTabs.addTab(mPresentExchangeTabs.newTab().setText(mTitleList.get(1)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getChildFragmentManager(), mTitleList, mViewList);
        mPresentExchangeViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mPresentExchangeViewpager.setOffscreenPageLimit(3);
        mPresentExchangeTabs.setupWithViewPager(mPresentExchangeViewpager);//将TabLayout和ViewPager关联起来。
    }

}
