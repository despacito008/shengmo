package com.aiwujie.shengmo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 290243232 on 2017/5/12.
 */

public class RankMyPagerAdapter2 extends FragmentPagerAdapter {
    private List<String> mTitleList;
    private List<Fragment> mViewList;
    public RankMyPagerAdapter2(FragmentManager fm, List<String> mTitleList, List<Fragment> mViewList) {
        super(fm);
        this.mTitleList=mTitleList;
        this.mViewList=mViewList;
        this.mFragmentManager = fm;
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

    private FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction;
    /**
     * 清除缓存fragment
     * @param container ViewPager
     */
    public void clear(ViewGroup container){
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }

        for(int i=0;i<mViewList.size();i++){
            long itemId = this.getItemId(i);
            String name = makeFragmentName(container.getId(), itemId);
            Fragment fragment = this.mFragmentManager.findFragmentByTag(name);
            if(fragment != null){//根据对应的ID，找到fragment，删除
                mCurTransaction.remove(fragment);
            }
        }
        mCurTransaction.commitNowAllowingStateLoss();
    }

    /**
     * 等同于FragmentPagerAdapter的makeFragmentName方法，
     * 由于父类的该方法是私有的，所以在此重新定义
     * @param viewId
     * @param id
     * @return
     */
    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

}

