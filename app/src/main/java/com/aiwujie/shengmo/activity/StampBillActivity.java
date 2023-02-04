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
import com.aiwujie.shengmo.fragment.purserecordfragment.FragmentStampBuy;
import com.aiwujie.shengmo.fragment.purserecordfragment.FragmentStampReceive;
import com.aiwujie.shengmo.fragment.purserecordfragment.FragmentStampUse;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.TextViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StampBillActivity extends AppCompatActivity {
    @BindView(R.id.iv_normal_title_back)
    ImageView ivNormalTitleBack;
    @BindView(R.id.tv_normal_title_title)
    TextView tvNormalTitleTitle;
    @BindView(R.id.iv_normal_title_more)
    ImageView ivNormalTitleMore;
    @BindView(R.id.tv_normal_title_more)
    TextView tvNormalTitleMore;
    @BindView(R.id.mStampBill_tabs)
    TabLayout mStampBillTabs;
    @BindView(R.id.mStampBill_viewpager)
    ViewPager mStampBillViewpager;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_ejection_detail);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        setData();
        ivNormalTitleMore.setVisibility(View.INVISIBLE);
        tvNormalTitleTitle.setText("邮票明细");
        ivNormalTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setData() {
        //添加页卡标题
        mTitleList.add("购买记录");
        mTitleList.add("系统赠送");
        mTitleList.add("使用记录");
        //添加页卡视图
        mViewList.add(new FragmentStampBuy());
        mViewList.add(new FragmentStampReceive());
        mViewList.add(new FragmentStampUse());
        mStampBillTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
//        mStampBillTabs.addTab(mStampBillTabs.newTab().setText(mTitleList.get(0)));//添加tab选项卡
//        mStampBillTabs.addTab(mStampBillTabs.newTab().setText(mTitleList.get(1)));
//        mStampBillTabs.addTab(mStampBillTabs.newTab().setText(mTitleList.get(2)));

        mStampBillTabs.setSelectedTabIndicatorHeight(0);
        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(), mTitleList, mViewList);
        mStampBillViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mStampBillViewpager.setOffscreenPageLimit(3);
        mStampBillTabs.setupWithViewPager(mStampBillViewpager);//将TabLayout和ViewPager关联起来。
        //修改下划线的长度
//        mStampBillTabs.post(new Runnable() {
//            @Override
//            public void run() {
//                TablayoutLineWidthUtils.setIndicator(mStampBillTabs, 20, 20);
//            }
//        });

        for (int i = 0; i < mTitleList.size(); i++) {
            mStampBillTabs.getTabAt(i).setCustomView(getTabView(i));
        }
        selectTab(0);
        mStampBillViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mStampBillTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                chooseTab(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                unChooseTab(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

//    @OnClick(R.id.mStampBill_return)
//    public void onViewClicked() {
//        finish();
//    }


    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_custom_tab_layout_punishment, null);
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        txt_title.setText(mTitleList.get(position));
        View viewTabBottom = view.findViewById(R.id.view_item_tab_bottom);
        viewTabBottom.setVisibility(View.GONE);
        return view;
    }

    public void selectTab(int position) {
        for (int i = 0; i < mTitleList.size(); i++) {
            unChooseTab(position);
        }
        for (int i = 0; i < mTitleList.size(); i++) {
            if (i == position) {
                chooseTab(position);
            }
        }
    }

    public void chooseTab(int position) {
        View view = mStampBillTabs.getTabAt(position).getCustomView();
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        View viewTabBottom = view.findViewById(R.id.view_item_tab_bottom);
        viewTabBottom.setVisibility(View.VISIBLE);
        TextViewUtil.setBold(txt_title);
        txt_title.setTextColor(getResources().getColor(R.color.purple_main));
    }

    public void chooseTab(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        View viewTabBottom = view.findViewById(R.id.view_item_tab_bottom);
        viewTabBottom.setVisibility(View.VISIBLE);
        TextViewUtil.setBold(txt_title);
        txt_title.setTextColor(getResources().getColor(R.color.purple_main));
    }

    public void unChooseTab(int position) {
        View view = mStampBillTabs.getTabAt(position).getCustomView();
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        View viewTabBottom = view.findViewById(R.id.view_item_tab_bottom);
        viewTabBottom.setVisibility(View.GONE);
        TextViewUtil.setNormal(txt_title);
        txt_title.setTextColor(getResources().getColor(R.color.normalGray));
    }

    public void unChooseTab(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        View viewTabBottom = view.findViewById(R.id.view_item_tab_bottom);
        viewTabBottom.setVisibility(View.GONE);
        TextViewUtil.setNormal(txt_title);
        txt_title.setTextColor(getResources().getColor(R.color.normalGray));
    }

}
