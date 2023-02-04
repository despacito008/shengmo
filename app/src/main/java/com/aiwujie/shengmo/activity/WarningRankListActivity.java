package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.fragment.warningfragment.BanUserFragment;
import com.aiwujie.shengmo.fragment.warningfragment.IllegalUserFragment;
import com.aiwujie.shengmo.fragment.warningfragment.PunishmentFragment;
import com.aiwujie.shengmo.fragment.warningfragment.SuspiciousUserFragment;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.TablayoutLineWidthUtils;
import com.aiwujie.shengmo.utils.TextViewUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WarningRankListActivity extends AppCompatActivity {

    @BindView(R.id.mWarning_return)
    ImageView mWarningReturn;
    @BindView(R.id.mWarning_tabs)
    TabLayout mWarningTabs;
    @BindView(R.id.mWarning_viewpager)
    ViewPager mWarningViewpager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    private PunishmentFragment punishmentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_rank_list);
        ButterKnife.bind(this);
       // X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        setData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(mWarningViewpager.getCurrentItem() == 0) {
            punishmentFragment.refreshData();
        }
    }

    private void setData() {
        //添加页卡标题
        mTitleList.add("处罚公示");
        mTitleList.add("违规用户");
        mTitleList.add("可疑用户");
        mTitleList.add("封号用户");
        punishmentFragment = PunishmentFragment.newInstance();
        //添加页卡视图
        mViewList.add(punishmentFragment);
        mViewList.add(new IllegalUserFragment());
        mViewList.add(new SuspiciousUserFragment());
        mViewList.add(new BanUserFragment());
        mWarningTabs.setTabMode(TabLayout.MODE_SCROLLABLE);//设置tab模式，当前为系统默认模式
//        mWarningTabs.addTab(mWarningTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
//        mWarningTabs.addTab(mWarningTabs.newTab().setText(mTitleList.get(1)));
//        mWarningTabs.addTab(mWarningTabs.newTab().setText(mTitleList.get(2)));
//        mWarningTabs.addTab(mWarningTabs.newTab().setText(mTitleList.get(3)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(),mTitleList,mViewList);
        mWarningViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mWarningTabs.setupWithViewPager(mWarningViewpager);//将TabLayout和ViewPager
        mWarningViewpager.setOffscreenPageLimit(4);
        //修改下划线的长度
        mWarningTabs.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineWidthUtils.setIndicator(mWarningTabs, 15, 15);
            }
        });
        for (int i = 0; i < mViewList.size(); i++) {
            mWarningTabs.getTabAt(i).setCustomView(getTabView(i));
        }
        changeTab(0);
        mWarningViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    
    @OnClick(R.id.mWarning_return)
    public void onViewClicked() {
        finish();
    }


    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_custom_tab_layout_punishment, null);
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        txt_title.setText(mTitleList.get(position));
        View viewTabBottom = view.findViewById(R.id.view_item_tab_bottom);
        viewTabBottom.setVisibility(View.GONE);
        return view;
    }

    void changeTab(int index) {
        for (int i = 0; i < mWarningTabs.getTabCount(); i++) {
            TabLayout.Tab tabAt = mWarningTabs.getTabAt(i);
            TextView tvTabTitle = tabAt.getCustomView().findViewById(R.id.item_custom_tab_tv);
            View viewTabBottom = tabAt.getCustomView().findViewById(R.id.view_item_tab_bottom);
            if(i == index) {
                TextViewUtil.setBold(tvTabTitle);
                TextViewUtil.setTextSizeWithDp(tvTabTitle,17);
                tvTabTitle.setTextColor(getResources().getColor(R.color.purple_main));
                viewTabBottom.setVisibility(View.VISIBLE);
            } else {
                TextViewUtil.setNormal(tvTabTitle);
                TextViewUtil.setTextSizeWithDp(tvTabTitle,15);
                tvTabTitle.setTextColor(getResources().getColor(R.color.lightGray));
                viewTabBottom.setVisibility(View.GONE);
            }
        }
    }

}
