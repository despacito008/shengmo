package com.aiwujie.shengmo.fragment.dynamicfragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.FragmentAdapter;
import com.aiwujie.shengmo.kt.ui.fragment.HomePageTopicFragment;
import com.aiwujie.shengmo.utils.TablayoutLineWidthUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 290243232 on 2017/11/24.
 */

public class FragmentDynamicTopic extends Fragment implements  ViewPager.OnPageChangeListener{
    @BindView(R.id.mDynamic_topic_tabs)
    TabLayout mDynamicTopicTabs;
    @BindView(R.id.mDynamic_topic_viewpager)
    ViewPager mDynamicTopicViewpager;
    private List<String> titles;
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
    String [] titleStr =new String []{"关注","杂谈","兴趣","爆照","交友","生活","情感","官方"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = inflater.inflate(R.layout.item_fragment_dynamic_topic, null);
         ButterKnife.bind(this, view);
        isPrepared = true;
        lazyLoad();
        return view;
    }
    private void initViewPager() {
        titles = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();
        mDynamicTopicViewpager.setOffscreenPageLimit(titles.size());
        for (int i = 0; i < titleStr.length; i++) {
            titles.add(titleStr[i]);
            //fragments.add(FragmentTopic.newInstance(i,i));
            fragments.add(HomePageTopicFragment.Companion.newInstance(i));
            mDynamicTopicTabs.addTab(mDynamicTopicTabs.newTab(),i);

        }
        mDynamicTopicTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        FragmentAdapter mFragmentAdapteradapter =
                new FragmentAdapter(getActivity(), getChildFragmentManager(), fragments, titles);
        //给ViewPager设置适配器
        mDynamicTopicViewpager.setAdapter(mFragmentAdapteradapter);
        mDynamicTopicViewpager.addOnPageChangeListener(this);
        //将TabLayout和ViewPager关联起来。
        mDynamicTopicTabs.setupWithViewPager(mDynamicTopicViewpager);

        for (int i = 0; i < titleStr.length; i++) {
           View view =  getTabView(titleStr[i]);
            mDynamicTopicTabs.getTabAt(i).setCustomView(view);
            if (i==0){
                setTabLayoutStyle(mDynamicTopicTabs.getTabAt(i),true);
            } else {
                setTabLayoutStyle(mDynamicTopicTabs.getTabAt(i),false);
            }
        }
        //给TabLayout设置适配器
        mDynamicTopicTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTabLayoutStyle(tab,true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTabLayoutStyle(tab,false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     *
     * @param tab
     * @param isChecked
     */
    public void setTabLayoutStyle(TabLayout.Tab tab,boolean isChecked) {
        View view = tab.getCustomView();
        TextView txt_title = view.findViewById(R.id.item_custom_tab_tv);
        LinearLayout ll = view.findViewById(R.id.ll);
        if (isChecked) {
            txt_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
            txt_title.setTextColor(0xffffffff);
            ll.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_user_info_sex_cdts));

        } else {
            txt_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            txt_title.setTextColor(0xff999999);
            GradientDrawable drawable2 = new GradientDrawable();
            drawable2.setCornerRadius(50);
            drawable2.setColor(Color.parseColor("#F5F5F5"));
            ll.setBackground(drawable2);

        }

    }

    public View getTabView(String title) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_custom_tab_layout2, null);
        TextView txtTitle = view.findViewById(R.id.item_custom_tab_tv);
        txtTitle.setText(title);
        TextView mMainMessageCount = view.findViewById(R.id.mMain_messageCount);
        mMainMessageCount.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (isPrepared && isVisible && isFirstLoad) {

            isFirstLoad = false;
            initViewPager();
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
