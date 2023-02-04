package com.aiwujie.shengmo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.fragment.FragmentSee;
import com.aiwujie.shengmo.fragment.FragmentVisit;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SeeActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.mSee_return)
    ImageView mSeeReturn;
    @BindView(R.id.mSee_visit)
    TextView mSeeVisit;
    @BindView(R.id.mSee_visit_dian)
    ImageView mSeeVisitDian;
    @BindView(R.id.mSee_see)
    TextView mSeeSee;
    @BindView(R.id.mSee_see_dian)
    ImageView mSeeSeeDian;
    @BindView(R.id.mSee_viewpager)
    ViewPager mSeeViewpager;
    private List<Fragment> fragments;
    private List<TextView> tvs;
    private List<ImageView> ivs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this, R.color.title_color);
//        EventBus.getDefault().post(new VisitEvent(0));
        setData();
        setListener();
    }

    private void setData() {
        mSeeVisit.setSelected(true);
        fragments = new ArrayList<>();
        fragments.add(new FragmentVisit());
        fragments.add(new FragmentSee());
        tvs = new ArrayList<>();
        tvs.add(mSeeVisit);
        tvs.add(mSeeSee);
        ivs = new ArrayList<>();
        ivs.add(mSeeVisitDian);
        ivs.add(mSeeSeeDian);
        mSeeViewpager.setAdapter(new MyViewpager(getSupportFragmentManager()));
        mSeeViewpager.setCurrentItem(0, false);
    }

    private void setListener() {
        mSeeViewpager.addOnPageChangeListener(this);
    }

    @OnClick({R.id.mSee_return, R.id.mSee_visit, R.id.mSee_see})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mSee_return:
                finish();
                break;
            case R.id.mSee_visit:
                Selected(1);
                break;
            case R.id.mSee_see:
                Selected(2);
                break;
        }
    }

    private void Selected(int i) {
        if (i == 1) {
            mSeeViewpager.setCurrentItem(0,false);
            mSeeVisit.setSelected(true);
            mSeeVisitDian.setVisibility(View.VISIBLE);
            mSeeSeeDian.setVisibility(View.GONE);
            mSeeVisit.setEnabled(false);
            mSeeSee.setEnabled(true);
        } else {
            mSeeViewpager.setCurrentItem(1,false);
            mSeeSee.setSelected(true);
            mSeeVisitDian.setVisibility(View.GONE);
            mSeeSeeDian.setVisibility(View.VISIBLE);
            mSeeVisit.setEnabled(true);
            mSeeSee.setEnabled(false);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < tvs.size(); i++) {
            tvs.get(i).setSelected(false);
            ivs.get(i).setVisibility(View.GONE);
        }
        tvs.get(position).setSelected(true);
        ivs.get(position).setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MyViewpager extends FragmentPagerAdapter {

        public MyViewpager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}
