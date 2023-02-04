package com.aiwujie.shengmo.activity.ranking;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
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

public class ActiveAndPopularityRankingActivity extends AppCompatActivity {

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
    @BindView(R.id.view_pager_active_ranking)
    ViewPager viewPagerActiveRanking;
    @BindView(R.id.tv_activity_rank_activity)
    TextView tvActivityRankActivity;
    @BindView(R.id.tv_activity_rank_popularity)
    TextView tvActivityRankPopularity;
    @BindView(R.id.tv_active_ranking_comment)
    TextView tvActiveRankingComment;
    @BindView(R.id.view_active_ranking_comment)
    View viewActiveRankingComment;
    @BindView(R.id.ll_active_ranking_comment)
    LinearLayout llActiveRankingComment;
    @BindView(R.id.tv_active_ranking_thumb)
    TextView tvActiveRankingThumb;
    @BindView(R.id.view_active_ranking_thumb)
    View viewActiveRankingThumb;
    @BindView(R.id.ll_active_ranking_thumb)
    LinearLayout llActiveRankingThumb;
    @BindView(R.id.tv_active_ranking_push)
    TextView tvActiveRankingPush;
    @BindView(R.id.view_active_ranking_push)
    View viewActiveRankingPush;
    @BindView(R.id.ll_active_ranking_push)
    LinearLayout llActiveRankingPush;
    @BindView(R.id.ll_active_ranking_tab_type)
    LinearLayout llActiveRankingTabType;
    @BindView(R.id.tv_active_ranking_time_day)
    TextView tvActiveRankingTimeDay;
    @BindView(R.id.tv_active_ranking_time_week)
    TextView tvActiveRankingTimeWeek;
    @BindView(R.id.tv_active_ranking_time_month)
    TextView tvActiveRankingTimeMonth;
    @BindView(R.id.tv_active_ranking_time_all)
    TextView tvActiveRankingTimeAll;
    @BindView(R.id.ll_active_ranking_tab_time)
    LinearLayout llActiveRankingTabTime;
    @BindView(R.id.group_rank_active)
    Group groupRankActive;
    @BindView(R.id.group_rank_popularity)
    Group groupRankPopularity;

    int currentType2 = 0;
    int currentDay2 = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_ranking_active_popularity);
        ImmersionBar.with(this).init();
        ButterKnife.bind(this);
        setListener();
        initViewPager();


        setListener2();
        initViewPager2();

        tvActivityRankActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvActivityRankActivity.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvActivityRankPopularity.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvActivityRankActivity.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                tvActivityRankPopularity.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                groupRankActive.setVisibility(View.VISIBLE);
                groupRankPopularity.setVisibility(View.GONE);
            }
        });

        tvActivityRankPopularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvActivityRankPopularity.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvActivityRankActivity.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvActivityRankActivity.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                tvActivityRankPopularity.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                groupRankActive.setVisibility(View.GONE);
                groupRankPopularity.setVisibility(View.VISIBLE);
            }
        });

    }

    void initViewPager() {
        final List<Fragment> fragments = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                fragments.add(ActiveRankingFragment.newInstance(String.valueOf(i), String.valueOf(j)));
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
        viewPagerActiveRanking.setAdapter(fragmentStatePagerAdapter);
        viewPagerActiveRanking.setOffscreenPageLimit(6);
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
        llActiveRankingComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = 0;
                selectCommentTopTab();
                changeRankPage();
            }
        });
        llActiveRankingThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = 1;
                selectThumbTopTab();
                changeRankPage();
            }
        });
        llActiveRankingPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = 2;
                selectDynamicTopTab();
                changeRankPage();
            }
        });

        tvActiveRankingTimeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 0;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        tvActiveRankingTimeWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 1;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        tvActiveRankingTimeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 2;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        tvActiveRankingTimeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay = 3;
                selectDayTab(currentDay);
                changeRankPage();
            }
        });
        viewPagerActiveRanking.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        if (llActiveRankingTabType.getVisibility() == View.GONE) {
            llActiveRankingTabType.setVisibility(View.VISIBLE);
            llActiveRankingTabTime.setVisibility(View.VISIBLE);
            changeTitleBarColor(false);
        }
    }

    public void hideRankTab() {
        if (llActiveRankingTabType.getVisibility() == View.VISIBLE) {
            llActiveRankingTabType.setVisibility(View.GONE);
            llActiveRankingTabTime.setVisibility(View.GONE);
            changeTitleBarColor(true);
        }
    }

    void selectCommentTopTab() {
        changeTopTabView(true, tvActiveRankingComment, viewActiveRankingComment);
        changeTopTabView(false, tvActiveRankingThumb, viewActiveRankingThumb);
        changeTopTabView(false, tvActiveRankingPush, viewActiveRankingPush);
    }

    void selectThumbTopTab() {
        changeTopTabView(false, tvActiveRankingComment, viewActiveRankingComment);
        changeTopTabView(true, tvActiveRankingThumb, viewActiveRankingThumb);
        changeTopTabView(false, tvActiveRankingPush, viewActiveRankingPush);
    }

    void selectDynamicTopTab() {
        changeTopTabView(false, tvActiveRankingComment, viewActiveRankingComment);
        changeTopTabView(false, tvActiveRankingThumb, viewActiveRankingThumb);
        changeTopTabView(true, tvActiveRankingPush, viewActiveRankingPush);
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
        if (position < 3) {
            selectCommentTopTab();
        } else if (position < 6) {
            selectThumbTopTab();
        } else {
            selectDynamicTopTab();
        }
        selectDayTab(position % 3);
    }

    public void unSelectDayTab() {
        changeTimeTabView(false, tvActiveRankingTimeDay);
        changeTimeTabView(false, tvActiveRankingTimeWeek);
        changeTimeTabView(false, tvActiveRankingTimeMonth);
        changeTimeTabView(false, tvActiveRankingTimeAll);
    }


    public void selectDayTab(int index) {
        unSelectDayTab();
        if (index == 0) {
            changeTimeTabView(true, tvActiveRankingTimeDay);
        } else if (index == 1) {
            changeTimeTabView(true, tvActiveRankingTimeWeek);
        } else if (index == 2) {
            changeTimeTabView(true, tvActiveRankingTimeMonth);
        } else {
            changeTimeTabView(true, tvActiveRankingTimeAll);
        }
    }

    public void changeRankPage() {
        viewPagerActiveRanking.setCurrentItem(currentType * 3 + currentDay, false);
    }

    void changeTitleBarColor(boolean isDark) {
        if (isDark) {
            llPopularityRankingTitleBar.setBackgroundResource(R.color.boyColor);
        } else {
            llPopularityRankingTitleBar.setBackgroundResource(R.color.transparent);
        }
    }


    // -------------------------------------------------------------------------------------------------------------------------


    void initViewPager2() {
        final List<Fragment> fragments = new ArrayList<>();
        for (int i = 1; i < 4 ; i++) {
            for (int j = 0; j < 3; j++) {
                fragments.add(PopularityRankingFragment.newInstance(String.valueOf(i),String.valueOf(j)));
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
        selectCommentTopTab2();
        selectDayTab2(0);
    }

    void setListener2() {
        ivPopularityRankingReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llPopularityRankingComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType2 = 0;
                selectCommentTopTab2();
                changeRankPage2();
            }
        });
        llPopularityRankingThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType2= 1;
                selectThumbTopTab2();
                changeRankPage2();
            }
        });
        llPopularityRankingPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType2 = 2;
                selectDynamicTopTab2();
                changeRankPage2();
            }
        });

        tvPopularityRankingTimeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay2 = 0;
                selectDayTab2(currentDay);
                changeRankPage2();
            }
        });
        tvPopularityRankingTimeWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay2 = 1;
                selectDayTab(currentDay);
                changeRankPage2();
            }
        });
        tvPopularityRankingTimeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay2 = 2;
                selectDayTab(currentDay);
                changeRankPage2();
            }
        });
        tvPopularityRankingTimeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDay2 = 3;
                selectDayTab2(currentDay);
                changeRankPage2();
            }
        });
        viewPagerPopularityRanking.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchTabView2(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void showRankTab2() {
        if (llPopularityRankingTabType.getVisibility() == View.GONE) {
            llPopularityRankingTabType.setVisibility(View.VISIBLE);
            llPopularityRankingTabTime.setVisibility(View.VISIBLE);
            changeTitleBarColor(false);
        }
    }

    public void hideRankTab2() {
        if (llPopularityRankingTabType.getVisibility() == View.VISIBLE) {
            llPopularityRankingTabType.setVisibility(View.GONE);
            llPopularityRankingTabTime.setVisibility(View.GONE);
            changeTitleBarColor(true);
        }
    }

    void selectCommentTopTab2() {
        changeTopTabView2(true, tvPopularityRankingComment, viewPopularityRankingComment);
        changeTopTabView2(false, tvPopularityRankingThumb, viewPopularityRankingThumb);
        changeTopTabView2(false, tvPopularityRankingPush, viewPopularityRankingPush);
    }

    void selectThumbTopTab2() {
        changeTopTabView2(false, tvPopularityRankingComment, viewPopularityRankingComment);
        changeTopTabView2(true, tvPopularityRankingThumb, viewPopularityRankingThumb);
        changeTopTabView2(false, tvPopularityRankingPush, viewPopularityRankingPush);
    }

    void selectDynamicTopTab2() {
        changeTopTabView2(false, tvPopularityRankingComment, viewPopularityRankingComment);
        changeTopTabView2(false, tvPopularityRankingThumb, viewPopularityRankingThumb);
        changeTopTabView2(true, tvPopularityRankingPush, viewPopularityRankingPush);
    }

    void changeTopTabView2(boolean isChoose, TextView tvTab, View viewBottom) {
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

    void changeTimeTabView2(boolean isChoose, TextView tvTab) {
        if (isChoose) {
            tvTab.setBackgroundResource(R.drawable.bg_round_ranking_tab_white);
            tvTab.setTextColor(getResources().getColor(R.color.thumbOrange));
        } else {
            tvTab.setBackgroundResource(R.color.transparent);
            tvTab.setTextColor(getResources().getColor(R.color.white));
        }
    }

    public void switchTabView2(int position) {
        if (position < 3) {
            selectCommentTopTab2();
        } else if (position < 6) {
            selectThumbTopTab2();
        } else {
            selectDynamicTopTab2();
        }
        selectDayTab2(position % 3);
    }

    public void unSelectDayTab2() {
        changeTimeTabView2(false, tvPopularityRankingTimeDay);
        changeTimeTabView2(false, tvPopularityRankingTimeWeek);
        changeTimeTabView2(false, tvPopularityRankingTimeMonth);
        changeTimeTabView2(false, tvPopularityRankingTimeAll);
    }

    public void selectDayTab2(int index) {
        unSelectDayTab2();
        if (index == 0) {
            changeTimeTabView2(true, tvPopularityRankingTimeDay);
        } else if (index == 1) {
            changeTimeTabView2(true, tvPopularityRankingTimeWeek);
        } else if (index == 2) {
            changeTimeTabView2(true, tvPopularityRankingTimeMonth);
        } else {
            changeTimeTabView2(true, tvPopularityRankingTimeAll);
        }
    }

    public void changeRankPage2() {
        viewPagerPopularityRanking.setCurrentItem(currentType2 * 3 + currentDay2,false);
    }

}
