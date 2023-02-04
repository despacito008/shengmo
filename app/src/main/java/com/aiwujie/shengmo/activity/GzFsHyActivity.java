package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.application.MyApp;
import com.aiwujie.shengmo.customview.BindSvipDialog;
import com.aiwujie.shengmo.fragment.FragmentFans;
import com.aiwujie.shengmo.fragment.FragmentFollow;
import com.aiwujie.shengmo.fragment.FragmentFriend;
import com.aiwujie.shengmo.fragment.FragmentlabelTopic;
import com.aiwujie.shengmo.utils.SharedPreferencesUtils;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GzFsHyActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.mSee_return)
    ImageView mSeeReturn;
    @BindView(R.id.mGzFsHy_gz)
    TextView mGzFsHyGz;
    @BindView(R.id.mGzFsHy_biaoqian)
    ImageView mGzFsHy_biaoqian;
    @BindView(R.id.mGzFsHy_gz_dian)
    ImageView mGzFsHyGzDian;
    @BindView(R.id.mGzFsHy_bq)
    TextView mGzFsHy_bq;
    @BindView(R.id.mGzFsHy_fs)
    TextView mGzFsHyFs;
    @BindView(R.id.tv_labeldefaults)
    TextView tv_labeldefaults;
    @BindView(R.id.mGzFsHy_fs_dian)
    ImageView mGzFsHyFsDian;
    @BindView(R.id.mGzFsHy_hy)
    TextView mGzFsHyHy;
    @BindView(R.id.mGzFsHy_hy_dian)
    ImageView mGzFsHyHyDian;
    @BindView(R.id.mGzFsHy_hy_bq)
    PercentRelativeLayout mGzFsHy_hy_bq;
    @BindView(R.id.mGzFsHy_hy_ll)
    PercentRelativeLayout mGzFsHyHyLl;
    @BindView(R.id.mGzFsHy_viewpager)
    ViewPager mGzFsHyViewpager;
    private List<TextView> tvs;
    private List<ImageView> ivs;
    private List<Fragment> fragments;
    private String uid;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gz_fs_hy);
        ButterKnife.bind(this);
        StatusBarUtil.showLightStatusBar(this);
        setData();
    }

    private void setData() {
       // X_SystemBarUI.initSystemBar(this, R.color.title_color);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        currentIndex = intent.getIntExtra("currentIndex", -1);
        tvs = new ArrayList<>();
        ivs = new ArrayList<>();
        fragments = new ArrayList<>();
        if (uid.equals(MyApp.uid)) {
            tvs.add(mGzFsHyGz);
            tvs.add(mGzFsHyFs);
            tvs.add(mGzFsHyHy);
            tvs.add(mGzFsHy_bq);
            ivs.add(mGzFsHyGzDian);
            ivs.add(mGzFsHyFsDian);
            ivs.add(mGzFsHyHyDian);
            ivs.add(mGzFsHy_biaoqian);
            fragments.add(new FragmentFollow());
            fragments.add(new FragmentFans());
            fragments.add(new FragmentFriend());
            fragments.add(new FragmentlabelTopic());
        } else {
            tvs.add(mGzFsHyGz);
            tvs.add(mGzFsHyFs);
            ivs.add(mGzFsHyGzDian);
            ivs.add(mGzFsHyFsDian);
            fragments.add(new FragmentFollow());
            fragments.add(new FragmentFans());
            mGzFsHyHyLl.setVisibility(View.GONE);
            tv_labeldefaults.setVisibility(View.GONE);
            mGzFsHy_hy_bq.setVisibility(View.GONE);
        }
        mGzFsHyViewpager.setOffscreenPageLimit(3);
        mGzFsHyViewpager.setAdapter(new MyViewpager(getSupportFragmentManager()));
        mGzFsHyViewpager.addOnPageChangeListener(this);
        if (currentIndex == 0) {
            mGzFsHyGz.setSelected(true);
            mGzFsHyGzDian.setVisibility(View.VISIBLE);
            mGzFsHyViewpager.setCurrentItem(0, false);
        } else {
            mGzFsHyFs.setSelected(true);
            mGzFsHyFsDian.setVisibility(View.VISIBLE);
            mGzFsHyViewpager.setCurrentItem(1, false);
        }
    }

    @OnClick({R.id.tv_labeldefaults,R.id.mGzFsHy_bq,R.id.mSee_return, R.id.mGzFsHy_gz, R.id.mGzFsHy_fs, R.id.mGzFsHy_hy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mSee_return:
                finish();
                break;
            case R.id.mGzFsHy_gz:
                Selected(0);
                tv_labeldefaults.setVisibility(View.GONE);
                break;
            case R.id.mGzFsHy_fs:
                tv_labeldefaults.setVisibility(View.GONE);
                Selected(1);
                break;
            case R.id.mGzFsHy_hy:
                tv_labeldefaults.setVisibility(View.GONE);
                Selected(2);
                break;
            case R.id.mGzFsHy_bq:
                tv_labeldefaults.setVisibility(View.VISIBLE);
                Selected(3);
                break;
            case R.id.tv_labeldefaults:
                String svip = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "svip", "0");
                if (svip.equals("1")){
                    startActivity(new Intent(this,LabelDefaultsActivity.class));
                }else {
                    BindSvipDialog.bindAlertDialog(GzFsHyActivity.this, "设置分组限svip可用");
                }

                break;
        }
    }


    private void Selected(int i) {
        if (i == 0) {
            mGzFsHyViewpager.setCurrentItem(0, false);
            mGzFsHyGz.setSelected(true);
            mGzFsHyGzDian.setVisibility(View.VISIBLE);
            mGzFsHyFsDian.setVisibility(View.GONE);
            mGzFsHyHyDian.setVisibility(View.GONE);
            mGzFsHy_biaoqian.setVisibility(View.GONE);
            mGzFsHyGz.setEnabled(false);
            mGzFsHyFs.setEnabled(true);
            mGzFsHyHy.setEnabled(true);
            mGzFsHy_bq.setEnabled(true);
        } else if (i == 1) {
            mGzFsHyViewpager.setCurrentItem(1, false);
            mGzFsHyFs.setSelected(true);
            mGzFsHyGzDian.setVisibility(View.GONE);
            mGzFsHyFsDian.setVisibility(View.VISIBLE);
            mGzFsHyHyDian.setVisibility(View.GONE);
            mGzFsHy_biaoqian.setVisibility(View.GONE);
            mGzFsHyGz.setEnabled(true);
            mGzFsHyFs.setEnabled(false);
            mGzFsHyHy.setEnabled(true);
            mGzFsHy_bq.setEnabled(true);
        } else if (i == 2){
            mGzFsHyViewpager.setCurrentItem(2, false);
            mGzFsHyHy.setSelected(true);
            mGzFsHyGzDian.setVisibility(View.GONE);
            mGzFsHyFsDian.setVisibility(View.GONE);
            mGzFsHyHyDian.setVisibility(View.VISIBLE);
            mGzFsHy_biaoqian.setVisibility(View.GONE);
            mGzFsHyGz.setEnabled(true);
            mGzFsHyFs.setEnabled(true);
            mGzFsHyHy.setEnabled(false);
            mGzFsHy_bq.setEnabled(true);
        } else if (i == 3){
            mGzFsHyViewpager.setCurrentItem(3, false);
            mGzFsHy_bq.setSelected(true);
            mGzFsHyGzDian.setVisibility(View.GONE);
            mGzFsHyFsDian.setVisibility(View.GONE);
            mGzFsHyHyDian.setVisibility(View.GONE);
            mGzFsHy_biaoqian.setVisibility(View.VISIBLE);
            mGzFsHyGz.setEnabled(true);
            mGzFsHyFs.setEnabled(true);
            mGzFsHyHy.setEnabled(true);
            mGzFsHy_bq.setEnabled(false);
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
        if (position==3){
            tv_labeldefaults.setVisibility(View.VISIBLE);
        }else {
            tv_labeldefaults.setVisibility(View.GONE);
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
