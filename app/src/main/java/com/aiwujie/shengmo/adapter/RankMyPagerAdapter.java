package com.aiwujie.shengmo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 290243232 on 2017/5/12.
 */

public class RankMyPagerAdapter extends FragmentPagerAdapter {
    private List<String> mTitleList;
    private List<Fragment> mViewList;
    public RankMyPagerAdapter(FragmentManager fm, List<String> mTitleList, List<Fragment> mViewList) {
        super(fm);
        this.mTitleList=mTitleList;
        this.mViewList=mViewList;

    }

    @Override
    public int getCount() {
        return mViewList.size();//页卡数
    }


    @Override
    public Fragment getItem(int position) {
        return mViewList.get(position);
    }

    //重写这个方法，将设置每个Tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
}

