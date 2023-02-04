package com.aiwujie.shengmo.activity.ranking;

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
import com.aiwujie.shengmo.fragment.rank.RewardRankingCharmFragment;
import com.aiwujie.shengmo.fragment.rank.RewardRankingWealthFragment;
import com.aiwujie.shengmo.fragment.vipcenterfragment.FragmentSvip;
import com.aiwujie.shengmo.fragment.vipcenterfragment.VipFragment;
import com.gyf.immersionbar.ImmersionBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RewardRankingActivity extends AppCompatActivity {
    @BindView(R.id.iv_reward_ranking_return)
    ImageView ivRewardRankingReturn;
    @BindView(R.id.ll_reward_ranking_title_bar)
    LinearLayout llRewardRankingTitleBar;
    @BindView(R.id.tv_reward_ranking_wealth)
    TextView tvRewardRankingWealth;
    @BindView(R.id.view_reward_ranking_wealth)
    View viewRewardRankingWealth;
    @BindView(R.id.ll_reward_ranking_wealth)
    LinearLayout llRewardRankingWealth;
    @BindView(R.id.tv_reward_ranking_charm)
    TextView tvRewardRankingCharm;
    @BindView(R.id.view_reward_ranking_charm)
    View viewRewardRankingCharm;
    @BindView(R.id.ll_reward_ranking_charm)
    LinearLayout llRewardRankingCharm;
    @BindView(R.id.ll_reward_ranking_tab_type)
    LinearLayout llRewardRankingTabType;
    @BindView(R.id.tv_reward_ranking_time_day)
    TextView tvRewardRankingTimeDay;
    @BindView(R.id.tv_reward_ranking_time_week)
    TextView tvRewardRankingTimeWeek;
    @BindView(R.id.tv_reward_ranking_time_all)
    TextView tvRewardRankingTimeAll;
    @BindView(R.id.ll_reward_ranking_tab_time)
    LinearLayout llRewardRankingTabTime;
    @BindView(R.id.view_pager_reward_ranking)
    ViewPager viewPagerRewardRanking;

    int currentType = 0;
    int currentDay = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_ranking_reward);
        ImmersionBar.with(this).init();
        ButterKnife.bind(this);
        setListener();
        initViewPager();
    }

    void initViewPager() {
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(RewardRankingWealthFragment.newInstance("0"));
        fragments.add(RewardRankingWealthFragment.newInstance("1"));
        fragments.add(RewardRankingWealthFragment.newInstance("3"));
        fragments.add(RewardRankingCharmFragment.newInstance("0"));
        fragments.add(RewardRankingCharmFragment.newInstance("1"));
        fragments.add(RewardRankingCharmFragment.newInstance("3"));
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
        viewPagerRewardRanking.setAdapter(fragmentStatePagerAdapter);
        viewPagerRewardRanking.setOffscreenPageLimit(12);
        selectWealthTab();
        selectDayTab(0);
    }

    void setListener() {
        ivRewardRankingReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llRewardRankingWealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = 0;
                selectWealthTab();
                changeRankPage();
            }
        });
        llRewardRankingCharm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = 1;
                selectCharmTopTab();
                changeRankPage();
            }
        });
        tvRewardRankingTimeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 0;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        tvRewardRankingTimeWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 1;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        tvRewardRankingTimeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 2;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        viewPagerRewardRanking.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    }

    public void showRankTab() {
        if(llRewardRankingTabType.getVisibility() == View.GONE) {
            llRewardRankingTabType.setVisibility(View.VISIBLE);
            llRewardRankingTabTime.setVisibility(View.VISIBLE);
            changeTitleBarColor(false);
        }
    }

    public void hideRankTab() {
        if(llRewardRankingTabType.getVisibility() == View.VISIBLE) {
            llRewardRankingTabType.setVisibility(View.GONE);
            llRewardRankingTabTime.setVisibility(View.GONE);
            changeTitleBarColor(true);
        }
    }

    void selectWealthTab() {
        tvRewardRankingWealth.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
        tvRewardRankingCharm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        tvRewardRankingWealth.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvRewardRankingCharm.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        viewRewardRankingWealth.setVisibility(View.VISIBLE);
        viewRewardRankingCharm.setVisibility(View.GONE);
    }

    void selectCharmTopTab() {
        tvRewardRankingWealth.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        tvRewardRankingCharm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
        tvRewardRankingWealth.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        tvRewardRankingCharm.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        viewRewardRankingWealth.setVisibility(View.GONE);
        viewRewardRankingCharm.setVisibility(View.VISIBLE);
    }

    public void switchTabView(int position) {
        if(position < 3) {
            selectWealthTab();
        } else {
            selectCharmTopTab();
        }
        selectDayTab(position%3);
    }

    public void unSelectDayTab() {
        tvRewardRankingTimeDay.setBackgroundResource(R.color.transparent);
        tvRewardRankingTimeWeek.setBackgroundResource(R.color.transparent);
        tvRewardRankingTimeAll.setBackgroundResource(R.color.transparent);
        tvRewardRankingTimeDay.setTextColor(getResources().getColor(R.color.white));
        tvRewardRankingTimeWeek.setTextColor(getResources().getColor(R.color.white));
        tvRewardRankingTimeAll.setTextColor(getResources().getColor(R.color.white));
    }

    public void selectDayTab(int index) {
        unSelectDayTab();
        if(index == 0) {
            tvRewardRankingTimeDay.setBackgroundResource(R.drawable.bg_round_ranking_tab_white);
            tvRewardRankingTimeDay.setTextColor(getResources().getColor(R.color.thumbOrange));
        } else if (index == 1) {
            tvRewardRankingTimeWeek.setBackgroundResource(R.drawable.bg_round_ranking_tab_white);
            tvRewardRankingTimeWeek.setTextColor(getResources().getColor(R.color.thumbOrange));
        } else {
            tvRewardRankingTimeAll.setBackgroundResource(R.drawable.bg_round_ranking_tab_white);
            tvRewardRankingTimeAll.setTextColor(getResources().getColor(R.color.thumbOrange));
        }
    }

    public void changeRankPage() {
        viewPagerRewardRanking.setCurrentItem(currentType * 3 + currentDay,false);
    }

     void changeTitleBarColor(boolean isDark) {
        if(isDark) {
            llRewardRankingTitleBar.setBackgroundResource(R.color.thumbOrange);
        } else {
            llRewardRankingTitleBar.setBackgroundResource(R.color.transparent);
        }
    }


}
