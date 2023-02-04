package com.aiwujie.shengmo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.fragment.groupfragment.FragmentGroupZh;
import com.aiwujie.shengmo.fragment.groupfragment.FragmentGroupZj;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchGroupActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.mSearchGroup_return)
    ImageView mSearchGroupReturn;
    @BindView(R.id.mSearchGroup_name)
    TextView mSearchGroupName;
    @BindView(R.id.mSearchGroup_zuijin)
    TextView mSearchGroupZuijin;
    @BindView(R.id.id_tab_chat_ll)
    PercentLinearLayout idTabChatLl;
    @BindView(R.id.mSearchGroup_zuihuo)
    TextView mSearchGroupZuihuo;
    @BindView(R.id.id_tab_friend_ll)
    PercentLinearLayout idTabFriendLl;
    @BindView(R.id.id_tab_line_iv)
    ImageView idTabLineIv;
    @BindView(R.id.mSearchGroup_viewpager)
    ViewPager mSearchGroupViewpager;
    private List<Fragment> list_fragment;     //定义要装fragment的列表
    /**
     * ViewPager的当前选中页
     */
    private int currentIndex=0;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;
    private FragmentGroupZj fragmentzj;
    private FragmentGroupZh fragmentzh;
    private String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group);
        ButterKnife.bind(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        StatusBarUtil.showLightStatusBar(this);
        search=getIntent().getStringExtra("search");
        initData();
        setListener();
    }
    private void initData() {
        mSearchGroupName.setText(search);
        list_fragment=new ArrayList<Fragment>();
        fragmentzj=new FragmentGroupZj();
        fragmentzh=new FragmentGroupZh();
        list_fragment.add(fragmentzj);
        list_fragment.add(fragmentzh);
        MyGroupAdapter adapter=new MyGroupAdapter(getSupportFragmentManager(),list_fragment);
        mSearchGroupViewpager.setAdapter(adapter);
        mSearchGroupViewpager.setCurrentItem(0);
        DisplayMetrics dpMetrics = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) idTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / 2;
        idTabLineIv.setLayoutParams(lp);
        idTabLineIv.setBackgroundColor(Color.parseColor("#333333"));
    }
    private void setListener() {
        mSearchGroupReturn.setOnClickListener(this);
        mSearchGroupZuijin.setOnClickListener(this);
        mSearchGroupZuihuo.setOnClickListener(this);
        mSearchGroupViewpager.addOnPageChangeListener(this);
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        mSearchGroupZuijin.setTextColor(Color.parseColor("#a1a1a1"));
        mSearchGroupZuihuo.setTextColor(Color.parseColor("#a1a1a1"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mSearchGroup_return:
                finish();
                break;
            case R.id.mSearchGroup_zuijin:
                mSearchGroupViewpager.setCurrentItem(0);
                break;
            case R.id.mSearchGroup_zuihuo:
                mSearchGroupViewpager.setCurrentItem(1);
                break;
        }

    }
    class MyGroupAdapter extends FragmentPagerAdapter {

        private List<Fragment> datas;

        public MyGroupAdapter(FragmentManager fm, List<Fragment> datas) {
            super(fm);
            this.datas = datas;
        }

        @Override
        public Fragment getItem(int position) {
            return datas.get(position);
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    }
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }
    /**
     * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
     * offsetPixels:当前页面偏移的像素位置
     */
    @Override
    public void onPageScrolled(int position, float offset, int offsetPixels) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) idTabLineIv
                .getLayoutParams();
        /**
         * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
         * 设置mTabLineIv的左边距 滑动场景：
         * 记2个页面,
         * 从左到右分别为0,1,2
         * 0->1; 1->2; 2->1; 1->0
         */

        if (currentIndex == 0 && position == 0)// 0->1
        {
            lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 2) + currentIndex
                    * (screenWidth / 2));

        }
        else if (currentIndex == 1 && position == 0) // 1->0
        {
            lp.leftMargin = (int) (-(1 - offset)
                    * (screenWidth * 1.0 / 2) + currentIndex
                    * (screenWidth / 2));

        }

        idTabLineIv.setLayoutParams(lp);
    }


    @Override
    public void onPageSelected(int position) {
        resetTextView();
        switch (position) {
            case 0:
                mSearchGroupZuijin.setTextColor(Color.parseColor("#333333"));
                break;
            case 1:
                mSearchGroupZuihuo.setTextColor(Color.parseColor("#333333"));
                break;
        }
        currentIndex = position;
    }
}
