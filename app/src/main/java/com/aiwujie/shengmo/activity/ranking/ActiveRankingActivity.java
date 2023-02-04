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
import com.aiwujie.shengmo.fragment.rank.ActiveRankingFragment;
import com.aiwujie.shengmo.fragment.rank.PopularityRankingFragment;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActiveRankingActivity extends AppCompatActivity {

    @BindView(R.id.view_pager_popularity_ranking)
    ViewPager viewPagerPopularityRanking;
    @BindView(R.id.iv_popularity_ranking_return)
    ImageView ivPopularityRankingReturn;
    @BindView(R.id.ll_popularity_ranking_title_bar)
    LinearLayout llPopularityRankingTitleBar;
    @BindView(R.id.tv_popularity_ranking_comment)
    TextView tvPopularityRankingComment;
    @BindView(R.id.view_popularity_ranking_comment)
    View viewPopularityRankingComment;
    @BindView(R.id.ll_popularity_ranking_comment)
    LinearLayout llPopularityRankingComment;
    @BindView(R.id.tv_popularity_ranking_thumb)
    TextView tvPopularityRankingThumb;
    @BindView(R.id.view_popularity_ranking_thumb)
    View viewPopularityRankingThumb;
    @BindView(R.id.ll_popularity_ranking_thumb)
    LinearLayout llPopularityRankingThumb;
    @BindView(R.id.tv_popularity_ranking_push)
    TextView tvPopularityRankingPush;
    @BindView(R.id.view_popularity_ranking_push)
    View viewPopularityRankingPush;
    @BindView(R.id.ll_popularity_ranking_push)
    LinearLayout llPopularityRankingPush;
    @BindView(R.id.ll_popularity_ranking_tab_type)
    LinearLayout llPopularityRankingTabType;
    @BindView(R.id.tv_popularity_ranking_time_day)
    TextView tvPopularityRankingTimeDay;
    @BindView(R.id.tv_popularity_ranking_time_week)
    TextView tvPopularityRankingTimeWeek;
    @BindView(R.id.tv_popularity_ranking_time_month)
    TextView tvPopularityRankingTimeMonth;
    @BindView(R.id.tv_popularity_ranking_time_all)
    TextView tvPopularityRankingTimeAll;
    @BindView(R.id.ll_popularity_ranking_tab_time)
    LinearLayout llPopularityRankingTabTime;

    int currentType = 0;
    int currentDay = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_ranking_active);
        ImmersionBar.with(this).init();
        ButterKnife.bind(this);
        setListener();
        initViewPager();
    }

    void initViewPager() {
        final List<Fragment> fragments = new ArrayList<>();
        for (int i = 1; i < 4 ; i++) {
            for (int j = 0; j < 4; j++) {
                fragments.add(ActiveRankingFragment.newInstance(String.valueOf(i),String.valueOf(j)));
            }
        }
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
        viewPagerPopularityRanking.setAdapter(fragmentStatePagerAdapter);
        viewPagerPopularityRanking.setOffscreenPageLimit(6);
        selectCommentTopTab();
        selectDayTab(0);
    }

    void setListener() {
        ivPopularityRankingReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llPopularityRankingComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = 0;
                selectCommentTopTab();
                changeRankPage();
            }
        });
        llPopularityRankingThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = 1;
                selectThumbTopTab();
                changeRankPage();
            }
        });
        llPopularityRankingPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = 2;
                selectDynamicTopTab();
                changeRankPage();
            }
        });

        tvPopularityRankingTimeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 0;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        tvPopularityRankingTimeWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 1;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        tvPopularityRankingTimeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 2;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        tvPopularityRankingTimeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 3;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        viewPagerPopularityRanking.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        if (llPopularityRankingTabType.getVisibility() == View.GONE) {
            llPopularityRankingTabType.setVisibility(View.VISIBLE);
            llPopularityRankingTabTime.setVisibility(View.VISIBLE);
            changeTitleBarColor(false);
        }
    }

    public void hideRankTab() {
        if (llPopularityRankingTabType.getVisibility() == View.VISIBLE) {
            llPopularityRankingTabType.setVisibility(View.GONE);
            llPopularityRankingTabTime.setVisibility(View.GONE);
            changeTitleBarColor(true);
        }
    }

    void selectCommentTopTab() {
        changeTopTabView(true, tvPopularityRankingComment, viewPopularityRankingComment);
        changeTopTabView(false, tvPopularityRankingThumb, viewPopularityRankingThumb);
        changeTopTabView(false, tvPopularityRankingPush, viewPopularityRankingPush);
    }

    void selectThumbTopTab() {
        changeTopTabView(false, tvPopularityRankingComment, viewPopularityRankingComment);
        changeTopTabView(true, tvPopularityRankingThumb, viewPopularityRankingThumb);
        changeTopTabView(false, tvPopularityRankingPush, viewPopularityRankingPush);
    }

    void selectDynamicTopTab() {
        changeTopTabView(false, tvPopularityRankingComment, viewPopularityRankingComment);
        changeTopTabView(false, tvPopularityRankingThumb, viewPopularityRankingThumb);
        changeTopTabView(true, tvPopularityRankingPush, viewPopularityRankingPush);
    }

    void changeTopTabView(boolean isChoose, TextView tvTab, View viewBottom) {
        if (isChoose) {
            tvTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
            tvTab.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            viewBottom.setVisibility(View.VISIBLE);
        } else {
            tvTab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            tvTab.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            viewBottom.setVisibility(View.INVISIBLE);
        }
    }

    void changeTimeTabView(boolean isChoose, TextView tvTab) {
        if (isChoose) {
            tvTab.setBackgroundResource(R.drawable.bg_round_ranking_tab_white);
            tvTab.setTextColor(getResources().getColor(R.color.thumbOrange));
        } else {
            tvTab.setBackgroundResource(R.color.transparent);
            tvTab.setTextColor(getResources().getColor(R.color.white));
        }
    }

    public void switchTabView(int position) {
        if (position < 4) {
            selectCommentTopTab();
        } else if (position < 8) {
            selectThumbTopTab();
        } else {
            selectDynamicTopTab();
        }
        selectDayTab(position % 4);
    }

    public void unSelectDayTab() {
        changeTimeTabView(false, tvPopularityRankingTimeDay);
        changeTimeTabView(false, tvPopularityRankingTimeWeek);
        changeTimeTabView(false, tvPopularityRankingTimeMonth);
        changeTimeTabView(false, tvPopularityRankingTimeAll);
    }


    public void selectDayTab(int index) {
        unSelectDayTab();
        if (index == 0) {
            changeTimeTabView(true, tvPopularityRankingTimeDay);
        } else if (index == 1) {
            changeTimeTabView(true, tvPopularityRankingTimeWeek);
        } else if (index == 2) {
            changeTimeTabView(true, tvPopularityRankingTimeMonth);
        } else {
            changeTimeTabView(true, tvPopularityRankingTimeAll);
        }
    }

    public void changeRankPage() {
        viewPagerPopularityRanking.setCurrentItem(currentType * 4 + currentDay,false);
    }

    void changeTitleBarColor(boolean isDark) {
        if (isDark) {
            llPopularityRankingTitleBar.setBackgroundResource(R.color.boyColor);
        } else {
            llPopularityRankingTitleBar.setBackgroundResource(R.color.transparent);
        }
    }


}
