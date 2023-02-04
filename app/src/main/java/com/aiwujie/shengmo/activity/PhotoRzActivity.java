package com.aiwujie.shengmo.activity;

import android.content.Intent;
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
import com.aiwujie.shengmo.fragment.Wdrz_sfzrzfragment;
import com.aiwujie.shengmo.fragment.Wdrz_zprzfragment;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.TablayoutLineWidthUtils;
import com.aiwujie.shengmo.utils.TextViewUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoRzActivity extends AppCompatActivity {

    @BindView(R.id.mPhotorz_return)
    ImageView mPhotorzReturn;
    @BindView(R.id.mPresentBill_tabs)
    TabLayout mPresentBillTabs;
    @BindView(R.id.mPresentBill_viewpager)
    ViewPager mPresentBillViewpager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_rz);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
        Intent intent = getIntent();
        int xuan = intent.getIntExtra("xuan", 1);
        setData();
        if (xuan == 2) {
            mPresentBillViewpager.setCurrentItem(1);
        }

        mPresentBillTabs.setVisibility(View.VISIBLE);
    }

    private void setData() {
        //添加页卡标题
        mTitleList.add("自拍认证");
        mTitleList.add("身份认证");
        //添加页卡视图
        mViewList.add(new Wdrz_zprzfragment());
        mViewList.add(new Wdrz_sfzrzfragment());
        mPresentBillTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mPresentBillTabs.addTab(mPresentBillTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mPresentBillTabs.addTab(mPresentBillTabs.newTab().setText(mTitleList.get(1)));
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(), mTitleList, mViewList);
        mPresentBillViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mPresentBillViewpager.setOffscreenPageLimit(3);
        mPresentBillTabs.setupWithViewPager(mPresentBillViewpager);//将TabLayout和ViewPager关联起来。
        mPresentBillTabs.getTabAt(0).setCustomView(getTabView(0));
        mPresentBillTabs.getTabAt(1).setCustomView(getTabView(1));
        changeTab(0);
        mPresentBillViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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


    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_custom_tab_layout_punishment, null);
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        txt_title.setText(mTitleList.get(position));
        View viewTabBottom = view.findViewById(R.id.view_item_tab_bottom);
        viewTabBottom.setVisibility(View.GONE);
        return view;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @OnClick(R.id.mPhotorz_return)
    public void onViewClicked() {
        finish();
    }

    void changeTab(int index) {
        for (int i = 0; i < mPresentBillTabs.getTabCount(); i++) {
            TabLayout.Tab tabAt = mPresentBillTabs.getTabAt(i);
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
