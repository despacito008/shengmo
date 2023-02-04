package com.aiwujie.shengmo.fragment.dynamicfragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.eventbus.RedPointEvent;
import com.aiwujie.shengmo.eventbus.ViewIsVisibleEvent;
import com.aiwujie.shengmo.fragment.dynamic.PushTopDynamicFragment;
import com.aiwujie.shengmo.fragment.dynamic.RecommendDynamicFragment;
import com.aiwujie.shengmo.kt.ui.fragment.EjectionDynamicFragment;
import com.aiwujie.shengmo.kt.ui.fragment.RecommendationDynamicFragment;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by 290243232 on 2017/1/12.  推荐
 */
public class FragmentDynamicNew extends Fragment implements ViewPager.OnPageChangeListener {


    @BindView(R.id.mVipCenter_tabs)
    TabLayout mVipCenterTabs;
    @BindView(R.id.mVipCenter_viewpager)
    ViewPager mVipCenterViewpager;
    @BindView(R.id.pai_ll)
    LinearLayout llOrder;
    @BindView(R.id.ll_tab)
    LinearLayout llTab;
    @BindView(R.id.pai_tv)
    TextView tvOrder;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    private List<Integer> tabIcons = new ArrayList<>();//页卡Icon集合
    private String CHANNEL;
    private View tabView1;
    private View tabView2;
    int hongdinatuijian = 0;
    int hongdinatuiding = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_fragment_dynamic_hot, null);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        setData();
        return view;
    }


    private void setData() {
        //添加页卡标题
        mTitleList.add("推荐");
        mTitleList.add("推顶");
        //添加页卡标题Icon
        tabIcons.add(R.drawable.tuijian30);
        tabIcons.add(R.drawable.tuiding230);
        //final FragmentDynamicNew_tuijian tuijianFragment = new FragmentDynamicNew_tuijian();
        //final FragmentDynamicNew_tuiding tuidingFragment = new FragmentDynamicNew_tuiding();
        //RecommendDynamicFragment recommendDynamicFragment = RecommendDynamicFragment.newInstance();
       // final PushTopDynamicFragment pushTopDynamicFragment = PushTopDynamicFragment.newInstance();
        final EjectionDynamicFragment pushTopDynamicFragment = new EjectionDynamicFragment();
        //添加页卡视图
        //mViewList.add(recommendDynamicFragment);
        mViewList.add(new RecommendationDynamicFragment());
        mViewList.add(pushTopDynamicFragment);

        mVipCenterTabs.setTabMode(TabLayout.MODE_SCROLLABLE );//设置tab模式，当前为系统默认模式
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getChildFragmentManager(), mTitleList, mViewList);
        mVipCenterViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mVipCenterTabs.setupWithViewPager(mVipCenterViewpager);//将TabLayout和ViewPager关联起来。
        tabView1 = getTabView(0);
        tabView2 = getTabView(1);
        mVipCenterTabs.getTabAt(0).setCustomView(tabView1);
        mVipCenterTabs.getTabAt(1).setCustomView(tabView2);
        setTabLayoutStyle(mVipCenterTabs.getTabAt(0),true);
        setTabLayoutStyle(mVipCenterTabs.getTabAt(1),false);
        TextView mMain_messageCount = tabView1.findViewById(R.id.mMain_messageCount);

        int tuijianrednum = (int) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "tuijianrednum", 0);
        if (tuijianrednum != 0) {
            mMain_messageCount.setText(tuijianrednum + "");
            mMain_messageCount.setVisibility(View.VISIBLE);
        }


        TextView mMain_messageCount2 = tabView2.findViewById(R.id.mMain_messageCount);
        int tuidingrednum = (int) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "tuidingrednum", 0);
        if (tuidingrednum != 0) {
            mMain_messageCount2.setText(tuidingrednum + "");
            mMain_messageCount2.setVisibility(View.VISIBLE);
        }

        mVipCenterViewpager.addOnPageChangeListener(this);
        llOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("时间排序".equals(tvOrder.getText().toString())) {
                    pushTopDynamicFragment.changeOrder(1);
                    tvOrder.setText("推量排序");
                } else {
                    pushTopDynamicFragment.changeOrder(2);
                    tvOrder.setText("时间排序");
                }

            }
        });

        String order = SharedPreferencesUtils.geParam(getActivity(),SharedPreferencesUtils.HOT_SORT,"time");
        if (order.equals("time")) {
            tvOrder.setText("时间排序");
        } else {
            tvOrder.setText("推量排序");
        }
        mVipCenterTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        mVipCenterTabs.getTabAt(0).select();
    }

    /**
     *
     * @param tab
     * @param isChecked
     */
    public void setTabLayoutStyle(TabLayout.Tab tab,boolean isChecked) {
        View view =  tab.getCustomView();
        TextView txt_title = view.findViewById(R.id.item_custom_tab_tv);
        LinearLayout ll = view.findViewById(R.id.ll);
        if (isChecked){
            txt_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
            txt_title.setTextColor(0xffffffff);
            ll.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.bg_user_info_sex_cdts));
            mVipCenterViewpager.setCurrentItem(tab.getPosition());
        }else {
            txt_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
            txt_title.setTextColor(0xff999999);
            GradientDrawable drawable2 = new GradientDrawable();
            drawable2.setCornerRadius(50);
            drawable2.setColor(Color.parseColor("#F5F5F5"));
            ll.setBackground(drawable2);
        }

    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_custom_tab_layout2, null);
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        txt_title.setText(mTitleList.get(position));
        TextView mMain_messageCount = view.findViewById(R.id.mMain_messageCount);
        mMain_messageCount.setVisibility(View.GONE);
        return view;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void yincangxianshi(String str) {
        if (str.equals("shifouyincangshalou")) {
            int currentItem = mVipCenterViewpager.getCurrentItem();
            if (currentItem == 1) {
                EventBus.getDefault().post("yincangshalou");
                // 隐藏筛选功能
            }
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            llOrder.setVisibility(View.INVISIBLE);
        }
        if (position == 1) {
            llOrder.setVisibility(View.VISIBLE);
        }
        if (llTab.getVisibility() == View.GONE) {
            llTab.setVisibility(View.VISIBLE);
            llTab.setAnimation(AnimationUtil.moveToViewLocation1());

        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(RedPointEvent event) {
        if (null != tabView1 && null != tabView2) {
            TextView mMain_messageCount = tabView1.findViewById(R.id.mMain_messageCount);
            TextView mMain_messageCount2 = tabView2.findViewById(R.id.mMain_messageCount);
            if (event.getFlag() == 6) {
                int tuijianrednum = (int) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "tuijianrednum", 0);
                if (tuijianrednum != 0) {
                    mMain_messageCount.setText(tuijianrednum + "");
                    mMain_messageCount.setVisibility(View.VISIBLE);
                }
            }
            if (event.getFlag() == 7) {
                int tuidingrednum = (int) SharedPreferencesUtils.getParam(getActivity().getApplicationContext(), "tuidingrednum", 0);
                if (tuidingrednum != 0) {
                    mMain_messageCount2.setText(tuidingrednum + "");
                    mMain_messageCount2.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    // 这里应该就是控制 顶部选项卡 显示或隐藏的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(ViewIsVisibleEvent event) {
//        switch (event.getIsVisible()) {
//            case 0:
//                if (llTab.getVisibility() == View.VISIBLE) {
//                    llTab.setVisibility(View.GONE);
//                }
//                break;
//            case 1:
//
//                if (llTab.getVisibility() == View.GONE) {
//                    llTab.setVisibility(View.VISIBLE);
//                    llTab.setAnimation(AnimationUtil.moveToViewLocation1());
//                }
//                break;
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessages(String event) {
//        if (event.equals("tuitop")) {
//            if (llTab.getVisibility() == View.GONE) {
//                animateClose2(mVipCenterTabs);
//            }
//        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(String event) {
        TextView mMain_messageCount = tabView1.findViewById(R.id.mMain_messageCount);
        TextView mMain_messageCount2 = tabView2.findViewById(R.id.mMain_messageCount);
        if (event.equals("hongdiantuijian")) {
            hongdinatuijian = 0;
            SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "tuijianrednum", 0);
            mMain_messageCount.setVisibility(View.GONE);
        }
        if (event.equals("hongdiantuiding")) {
            hongdinatuiding = 0;
            SharedPreferencesUtils.setParam(getActivity().getApplicationContext(), "tuidingrednum", 0);
            mMain_messageCount2.setVisibility(View.GONE);
        }
    }

    public static int dp2px(float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置隐藏动画
     *
     * @param view //动画作用的控件
     */
    public void animateClose(final View view) {
//        //获得控件的高度
        int origHeight = view.getHeight();
        //createDropAnimator()自定义的一个动画效果函数
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.setDuration(300);
        //如果你不想实现Animator.AnimatorListener中的所有接口，你可以通过继承AnimatorListenerAdapter。
        //AnimatorListenerAdapter类为所有的方法提供了一个空实现，所以你可以根据需要实现你需要的，覆盖AnimatorListenerAdapter原来的方法
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {  //动画结束时调用
                view.setVisibility(View.GONE);
            }
        });
        animator.start();

    }

    /**
     * 设置显示动画
     *
     * @param view //动画作用的控件
     */
    public void animateClose2(final View view) {
        //获得控件的高度
        int i = dp2px(40);
        int origHeight = view.getHeight();
        //createDropAnimator()自定义的一个动画效果函数
        ValueAnimator animator = createDropAnimator(view, 0, i);
        animator.setDuration(300);
        //如果你不想实现Animator.AnimatorListener中的所有接口，你可以通过继承AnimatorListenerAdapter。
        //AnimatorListenerAdapter类为所有的方法提供了一个空实现，所以你可以根据需要实现你需要的，覆盖AnimatorListenerAdapter原来的方法
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {  //动画结束时调用

            }
        });
        animator.start();
        view.setVisibility(View.VISIBLE);
    }

    /**
     * 自定义的动画效果
     *
     * @param v     //动画作用的控件
     * @param start //动画的开始值
     * @param end   //动画的结束值
     * @return
     */
    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        //这里我们利用ValueAnimator.ofInt创建了一个值从start到end的动画
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        //为ValueAnimator注册AnimatorUpdateListener监听器，在该监听器中可以
        // 监听ValueAnimator计算出来的值的改变，并将这些值应用到指定对象
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                //获取动画当前值
                int value = (int) arg0.getAnimatedValue();
                //得到控件的属性集合
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                //设置控件的高属性
                layoutParams.height = value;
                //把属性绑定到需要动画的控件上
                v.setLayoutParams(layoutParams);
                v.invalidate();
            }
        });
        return animator;
    }


    public void hideTab() {
        if (llTab.getVisibility() == View.VISIBLE) {
            llTab.setAnimation(AnimationUtil.hideTabAnimation(llTab));
            llTab.setVisibility(View.GONE);
            EventBus.getDefault().post(new ViewIsVisibleEvent(0));
        }
    }

    public void showTab() {
        if (llTab.getVisibility() == View.GONE) {
            llTab.setVisibility(View.VISIBLE);
            llTab.setAnimation(AnimationUtil.showTabAnimation(llTab));
            EventBus.getDefault().post(new ViewIsVisibleEvent(1));
        }
    }


}
