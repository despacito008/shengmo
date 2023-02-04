package com.aiwujie.shengmo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 290243232 on 2017/5/12.
 */

public class NormalPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mViewList;
    public NormalPagerAdapter(FragmentManager fm,List<Fragment> mViewList) {
        super(fm);
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
}

