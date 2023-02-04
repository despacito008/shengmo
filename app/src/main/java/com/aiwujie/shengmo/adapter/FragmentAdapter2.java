package com.aiwujie.shengmo.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 290243232 on 2017/1/21.
 */
public class FragmentAdapter2 extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private List<String> mTitles;
    private Context context;

    public FragmentAdapter2(Context context, FragmentManager fm, List<Fragment> mFragments, List<String> mTitles) {
        super(fm);
        this.context=context;
        this.mFragments = mFragments;
        this.mTitles = mTitles;
        this.mFragmentManager =fm;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position)+"";
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

        for(int i=0;i<mFragments.size();i++){
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
