package com.aiwujie.shengmo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.adapter.RankMyPagerAdapter;
import com.aiwujie.shengmo.eventbus.BigHornEvent;
import com.aiwujie.shengmo.fragment.noticefragment.NoticePresentFragment;
import com.aiwujie.shengmo.fragment.noticefragment.NoticeTopcardFragment;
import com.aiwujie.shengmo.fragment.noticefragment.NoticeVipFragment;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.TextViewUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DynamicNoticeActivity extends AppCompatActivity {


    @BindView(R.id.mDynamic_notice_return)
    ImageView mDynamicNoticeReturn;
    @BindView(R.id.mDynamic_notice_tabs)
    TabLayout mDynamicNoticeTabs;
    @BindView(R.id.mDynamic_notice_viewpager)
    ViewPager mDynamicNoticeViewpager;
    @BindView(R.id.activity_dynamic_notice)
    LinearLayout activityDynamicNotice;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    private TextView redtv1;
    private TextView redtv2;
    private TextView redtv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_notice);
        //X_SystemBarUI.initSystemBar(this, Color.parseColor("#160122"));
        ImmersionBar.with(this).init();
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setData();
    }

    private void setData() {
        //添加页卡标题
        mTitleList.add("赠送礼物");
        mTitleList.add("赠送会员");
        mTitleList.add("动态推顶");
        //添加页卡视图
        mViewList.add(new NoticePresentFragment());
        mViewList.add(new NoticeVipFragment());
        mViewList.add(new NoticeTopcardFragment());
        mDynamicNoticeTabs.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        View tabView1 = getTabView(0);
        View tabView2 = getTabView(1);
        View tabView3 = getTabView(2);
        redtv1 = tabView1.findViewById(R.id.mMain_messageCount);
        redtv2 = tabView2.findViewById(R.id.mMain_messageCount);
        redtv3 = tabView3.findViewById(R.id.mMain_messageCount);
        String giftnum = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "giftnum", "");
        String topcardnum = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "topcardnum", "");
        String vipnum = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "vipnum", "");
        if (!giftnum.equals("") && !giftnum.equals("0")) {
            redtv1.setText(giftnum + "");
            redtv1.setVisibility(View.VISIBLE);
        } else {
            redtv1.setVisibility(View.GONE);
        }
        if (!vipnum.equals("") && !vipnum.equals("0")) {
            redtv2.setText(vipnum + "");
            redtv2.setVisibility(View.VISIBLE);
        } else {
            redtv2.setVisibility(View.GONE);
        }
        if (!topcardnum.equals("") && !topcardnum.equals("0")) {
            redtv3.setText(topcardnum + "");
            redtv3.setVisibility(View.VISIBLE);
        } else {
            redtv3.setVisibility(View.GONE);
        }

        //  mDynamicNoticeTabs.addTab(mDynamicNoticeTabs.newTab().setCustomView(tabView1));//添加tab选项卡
        // mDynamicNoticeTabs.addTab(mDynamicNoticeTabs.newTab().setCustomView(tabView2));
        // mDynamicNoticeTabs.addTab(mDynamicNoticeTabs.newTab().setCustomView(tabView3));


        RankMyPagerAdapter mAdapter = new RankMyPagerAdapter(getSupportFragmentManager(), mTitleList, mViewList);
        mDynamicNoticeViewpager.setAdapter(mAdapter);//给ViewPager设置适配器
        mDynamicNoticeTabs.setupWithViewPager(mDynamicNoticeViewpager);//将TabLayout和ViewPager关联起来。
        mDynamicNoticeTabs.getTabAt(0).setCustomView(tabView1);
        mDynamicNoticeTabs.getTabAt(1).setCustomView(tabView2);
        mDynamicNoticeTabs.getTabAt(2).setCustomView(tabView3);
        //修改下划线的长度
       /* mDynamicNoticeTabs.post(new Runnable() {
            @Override
            public void run() {
                TablayoutLineWidthUtils.setIndicator(mDynamicNoticeTabs, 60, 60);
            }
        });*/
        SharedPreferencesUtils.setParam(getApplicationContext(), "labaState", "0");
        EventBus.getDefault().post(new BigHornEvent(0));

        mDynamicNoticeViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        changeTab(0);
    }

    @OnClick(R.id.mDynamic_notice_return)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hongdian(String str) {
        if (str.equals("zengsongliwuredmei1")) {
            SharedPreferencesUtils.setParam(getApplicationContext(), "giftnum", "0");
            SharedPreferencesUtils.setParam(getApplicationContext(), "topcardnum", "0");
            SharedPreferencesUtils.setParam(getApplicationContext(), "vipnum", "0");
        }
        if (str.equals("zengsongliwuredmei")) {
            redtv1.setVisibility(View.GONE);
        }
        if (str.equals("dongtaituidingredmei")) {
            redtv3.setVisibility(View.GONE);
        }
        if (str.equals("zengsonghuiyuanredmei")) {
            redtv2.setVisibility(View.GONE);
        }
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_custom_tab_layout3, null);
        TextView txt_title = (TextView) view.findViewById(R.id.item_custom_tab_tv);
        txt_title.setText(mTitleList.get(position));
        TextView mMain_messageCount = view.findViewById(R.id.mMain_messageCount);
        mMain_messageCount.setVisibility(View.GONE);
        View viewTabBottom = view.findViewById(R.id.view_item_tab_bottom);
        viewTabBottom.setVisibility(View.GONE);
        return view;
    }

    void changeTab(int index) {
        for (int i = 0; i < mDynamicNoticeTabs.getTabCount(); i++) {
            TabLayout.Tab tabAt = mDynamicNoticeTabs.getTabAt(i);
            TextView tvTabTitle = tabAt.getCustomView().findViewById(R.id.item_custom_tab_tv);
            View viewTabBottom = tabAt.getCustomView().findViewById(R.id.view_item_tab_bottom);
            if(i == index) {
                TextViewUtil.setBold(tvTabTitle);
                TextViewUtil.setTextSizeWithDp(tvTabTitle,19);
                viewTabBottom.setVisibility(View.VISIBLE);
            } else {
                TextViewUtil.setNormal(tvTabTitle);
                TextViewUtil.setTextSizeWithDp(tvTabTitle,15);
                viewTabBottom.setVisibility(View.GONE);
            }
        }
    }

}
