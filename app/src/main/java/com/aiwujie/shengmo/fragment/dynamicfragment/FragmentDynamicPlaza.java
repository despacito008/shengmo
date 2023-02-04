package com.aiwujie.shengmo.fragment.dynamicfragment;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.eventbus.ViewIsVisibleEvent;
import com.aiwujie.shengmo.fragment.dynamic.SquareDynamicFragment;
import com.aiwujie.shengmo.kt.ui.fragment.NewestDynamicFragment;
import com.aiwujie.shengmo.kt.ui.fragment.VideoDynamicFragment;
import com.aiwujie.shengmo.kt.util.NormalConstant;
import com.aiwujie.shengmo.kt.util.NormalUtilKt;
import com.aiwujie.shengmo.kt.util.SpKey;
import com.aiwujie.shengmo.utils.AnimationUtil;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zq on 2021/4/2 16:19
 * 邮箱：80776234@qq.com
 * 动态->广场页面
 */
public class FragmentDynamicPlaza extends Fragment {

    private LinearLayout llTab;
    private TabLayout plazaTabs;
    /**
     * 页卡标题集合
     */
    private List<String> mTitleList = new ArrayList<>();
    /**
     * 页卡视图集合
     */
    private List<android.support.v4.app.Fragment> mViewList = new ArrayList<>();
    /**
     * 页卡Icon集合
     */
    private List<Integer> tabIcons = new ArrayList<>();
    private View tabView1;
    private View tabView2;
    private ViewPager plazaViewpager;
    private LinearLayout llSort;
    private TextView tvSort;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_fragment_dynamic_plaza, null);
        llTab = view.findViewById(R.id.ll_tab);
        plazaTabs = view.findViewById(R.id.plaza_tabs);
        plazaViewpager = view.findViewById(R.id.plaza_viewpager);
        llSort = view.findViewById(R.id.pai_ll);
        tvSort = view.findViewById(R.id.pai_tv);
        EventBus.getDefault().register(this);
        videoSort = (String) SharedPreferencesUtils.getParam(getActivity(),"videoSort","0");
        if ("1".equals(videoSort)) {
            tvSort.setText("随机排序");
        } else {
            tvSort.setText("时间排序");
        }
        setData();
        return view;
    }

    String videoSort;
    private void setData() {
        //添加页卡标题
        mTitleList.add("最新");
        mTitleList.add("视频");
        //添加页卡视图
        //mViewList.add(new FragmentDynamicHot());
        //final FragmentDynamicVideo fragmentDynamicVideo = FragmentDynamicVideo.newInstance(videoSort);
        final VideoDynamicFragment fragmentDynamicVideo = VideoDynamicFragment.Companion.newInstance(videoSort);
        //mViewList.add(SquareDynamicFragment.newInstance());
        mViewList.add(new NewestDynamicFragment());
        mViewList.add(fragmentDynamicVideo);

        plazaTabs.setTabMode(TabLayout.MODE_SCROLLABLE);

      /*  plazaTabs.addTab(plazaTabs.newTab(),0);
        plazaTabs.addTab(plazaTabs.newTab(),1);*/
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getChildFragmentManager(), mTitleList, mViewList);
        /**
         * 给ViewPager设置适配器
         */
        plazaViewpager.setAdapter(mAdapter);
        /**
         * 将TabLayout和ViewPager关联起来。
         */
        plazaTabs.setupWithViewPager(plazaViewpager);
        tabView1 = getTabView(0);
        tabView2 = getTabView(1);
        plazaTabs.getTabAt(0).setCustomView(tabView1);
        plazaTabs.getTabAt(1).setCustomView(tabView2);
        setTabLayoutStyle(plazaTabs.getTabAt(0),true);
        setTabLayoutStyle(plazaTabs.getTabAt(1),false);
        plazaTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        plazaTabs.getTabAt(0).select();
        plazaViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    EventBus.getDefault().post("xianshishalou");
                    llSort.setVisibility(View.GONE);
                }
                if (position == 1) {
                    EventBus.getDefault().post("yincangshalou");
                    llSort.setVisibility(View.VISIBLE);
                }
                if (llTab.getVisibility() == View.GONE) {
                    llTab.setVisibility(View.VISIBLE);
                    llTab.setAnimation(AnimationUtil.moveToViewLocation1());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tvSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(videoSort)) {
                    videoSort = "0";
                } else {
                    videoSort = "1";
                }
                if ("1".equals(videoSort)) {
                    tvSort.setText("随机排序");
                } else {
                    tvSort.setText("时间排序");
                }
                NormalUtilKt.saveSpValue(SpKey.VIDEO_SORT,videoSort);
                fragmentDynamicVideo.refreshMode(videoSort);
            }
        });

    }

  public void setCheckedTab(int position){
      plazaTabs.getTabAt(position).select();
      if (llTab.getVisibility() == View.GONE) {
          llTab.setVisibility(View.VISIBLE);
          llTab.setAnimation(AnimationUtil.moveToViewLocation1());
      }
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
            plazaViewpager.setCurrentItem(tab.getPosition());
        } else {
            txt_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            txt_title.setTextColor(0xff999999);
            GradientDrawable drawable2 = new GradientDrawable();
            drawable2.setCornerRadius(50);
            drawable2.setColor(Color.parseColor("#F5F5F5"));
            ll.setBackground(drawable2);

        }

    }


    public View getTabView(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_custom_tab_layout2, null);
        TextView txtTitle = view.findViewById(R.id.item_custom_tab_tv);
        txtTitle.setText(mTitleList.get(position));
        TextView mMainMessageCount = view.findViewById(R.id.mMain_messageCount);
        mMainMessageCount.setVisibility(View.GONE);
        return view;
    }
    /**
     * 这里应该就是控制 顶部选项卡 显示或隐藏的
     */

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
//
////                    animateClose2(mVipCenterTabs);
//                }
//                break;
//            default:
//                break;
//        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
