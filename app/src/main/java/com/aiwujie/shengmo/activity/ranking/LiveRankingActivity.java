package com.aiwujie.shengmo.activity.ranking;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.kt.ui.fragment.LiveAnchorRankFragment;
import com.aiwujie.shengmo.kt.ui.fragment.LiveAudienceRankFragment;
import com.aiwujie.shengmo.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveRankingActivity extends AppCompatActivity {
    @BindView(R.id.view_pager_live_ranking)
    ViewPager viewPagerLiveRanking;
    @BindView(R.id.iv_live_ranking_return)
    ImageView ivLiveRankingReturn;
    @BindView(R.id.ll_live_ranking_title_bar)
    LinearLayout llLiveRankingTitleBar;
    @BindView(R.id.tv_live_ranking_anchor)
    TextView tvLiveRankingAnchor;
    @BindView(R.id.view_live_ranking_anchor)
    View viewLiveRankingAnchor;
    @BindView(R.id.ll_live_ranking_anchor)
    LinearLayout llLiveRankingAnchor;
    @BindView(R.id.tv_live_ranking_audience)
    TextView tvLiveRankingAudience;
    @BindView(R.id.view_live_ranking_audience)
    View viewLiveRankingAudience;
    @BindView(R.id.ll_live_ranking_audience)
    LinearLayout llLiveRankingAudience;
    @BindView(R.id.ll_live_ranking_tab_type)
    LinearLayout llLiveRankingTabType;
    @BindView(R.id.tv_live_ranking_time_day)
    TextView tvLiveRankingTimeDay;
    @BindView(R.id.tv_live_ranking_time_week)
    TextView tvLiveRankingTimeWeek;
    @BindView(R.id.tv_live_ranking_time_month)
    TextView tvLiveRankingTimeMonth;
    @BindView(R.id.ll_live_ranking_tab_time)
    LinearLayout llLiveRankingTabTime;
    @BindView(R.id.tv_live_ranking_time_all)
    TextView tvLiveRankingTimeAll;
    @BindView(R.id.tv_live_ranking_right)
    TextView tvLiveRankingRight;


    int currentType = 0;
    int currentDay = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_live_ranking);
        StatusBarUtil.showLightStatusBar(this);
        ButterKnife.bind(this);
        setListener();
        initViewPager();

    }

    void initViewPager() {
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(LiveAnchorRankFragment.Companion.newInstance(0));
        fragments.add(LiveAnchorRankFragment.Companion.newInstance(1));
        fragments.add(LiveAnchorRankFragment.Companion.newInstance(3));
        fragments.add(LiveAnchorRankFragment.Companion.newInstance(4));
        fragments.add(LiveAudienceRankFragment.Companion.newInstance(0));
        fragments.add(LiveAudienceRankFragment.Companion.newInstance(1));
        fragments.add(LiveAudienceRankFragment.Companion.newInstance(3));
        fragments.add(LiveAudienceRankFragment.Companion.newInstance(4));
        FragmentStatePagerAdapter fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

        };
        viewPagerLiveRanking.setAdapter(fragmentStatePagerAdapter);
        viewPagerLiveRanking.setOffscreenPageLimit(12);
        selectAnchorTab();
        selectDayTab(0);
    }

    void setListener() {
        ivLiveRankingReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llLiveRankingAnchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = 0;
                selectAnchorTab();
                changeRankPage();
            }
        });
        llLiveRankingAudience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = 1;
                selectAudienceTab();
                changeRankPage();
            }
        });
        tvLiveRankingTimeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 0;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        tvLiveRankingTimeWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 1;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        tvLiveRankingTimeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 2;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        tvLiveRankingTimeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 3;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        viewPagerLiveRanking.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchTabView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tvLiveRankingRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveRankingActivity.this, RewardRankingActivity.class);
                startActivity(intent);
            }
        });
    }

//    public void showRankTab() {
//        if (llRewardRankingTabType.getVisibility() == View.GONE) {
//            llRewardRankingTabType.setVisibility(View.VISIBLE);
//            llRewardRankingTabTime.setVisibility(View.VISIBLE);
//            changeTitleBarColor(false);
//        }
//    }
//
//    public void hideRankTab() {
//        if (llRewardRankingTabType.getVisibility() == View.VISIBLE) {
//            llRewardRankingTabType.setVisibility(View.GONE);
//            llRewardRankingTabTime.setVisibility(View.GONE);
//            changeTitleBarColor(true);
//        }
//    }

    void selectAnchorTab() {
        tvLiveRankingAnchor.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
        tvLiveRankingAudience.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tvLiveRankingAnchor.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvLiveRankingAudience.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        viewLiveRankingAnchor.setVisibility(View.VISIBLE);
        viewLiveRankingAudience.setVisibility(View.INVISIBLE);
    }

    void selectAudienceTab() {
        tvLiveRankingAnchor.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tvLiveRankingAudience.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
        tvLiveRankingAnchor.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvLiveRankingAudience.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        viewLiveRankingAnchor.setVisibility(View.INVISIBLE);
        viewLiveRankingAudience.setVisibility(View.VISIBLE);
    }

    public void switchTabView(int position) {
        if (position < 4) {
            selectAnchorTab();
        } else {
            selectAudienceTab();
        }
        selectDayTab(position % 4);
    }

    public void unSelectDayTab() {
        tvLiveRankingTimeDay.setBackgroundResource(R.color.transparent);
        tvLiveRankingTimeWeek.setBackgroundResource(R.color.transparent);
        tvLiveRankingTimeMonth.setBackgroundResource(R.color.transparent);
        tvLiveRankingTimeAll.setBackgroundResource(R.color.transparent);
        tvLiveRankingTimeDay.setTextColor(getResources().getColor(R.color.titleBlack));
        tvLiveRankingTimeWeek.setTextColor(getResources().getColor(R.color.titleBlack));
        tvLiveRankingTimeMonth.setTextColor(getResources().getColor(R.color.titleBlack));
        tvLiveRankingTimeAll.setTextColor(getResources().getColor(R.color.titleBlack));
    }

    public void selectDayTab(int index) {
        unSelectDayTab();
        if (index == 0) {
            tvLiveRankingTimeDay.setBackgroundResource(R.drawable.bg_round_ranking_tab_purple);
            tvLiveRankingTimeDay.setTextColor(getResources().getColor(R.color.white));
        } else if (index == 1) {
            tvLiveRankingTimeWeek.setBackgroundResource(R.drawable.bg_round_ranking_tab_purple);
            tvLiveRankingTimeWeek.setTextColor(getResources().getColor(R.color.white));
        } else if (index == 2) {
            tvLiveRankingTimeMonth.setBackgroundResource(R.drawable.bg_round_ranking_tab_purple);
            tvLiveRankingTimeMonth.setTextColor(getResources().getColor(R.color.white));
        } else {
            tvLiveRankingTimeAll.setBackgroundResource(R.drawable.bg_round_ranking_tab_purple);
            tvLiveRankingTimeAll.setTextColor(getResources().getColor(R.color.white));
        }
    }

    public void changeRankPage() {
        viewPagerLiveRanking.setCurrentItem(currentType * 4 + currentDay, false);
    }

    void changeTitleBarColor(boolean isDark) {
        if (isDark) {
            llLiveRankingTitleBar.setBackgroundResource(R.color.thumbOrange);
        } else {
            llLiveRankingTitleBar.setBackgroundResource(R.color.transparent);
        }
    }

}
