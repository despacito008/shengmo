package com.aiwujie.shengmo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiwujie.shengmo.R;
import com.aiwujie.shengmo.eventbus.MyPresentEvent;
import com.aiwujie.shengmo.fragment.mypursefragment.FragmentPresent;
import com.aiwujie.shengmo.fragment.mypursefragment.FragmentRecharge;
import com.aiwujie.shengmo.fragment.user.GiftFragment;
import com.aiwujie.shengmo.fragment.user.RechargeFragment;
import com.aiwujie.shengmo.kt.ui.activity.statistical.PresentDetailActivity;
import com.aiwujie.shengmo.kt.ui.activity.statistical.RechargeDetailActivity;
import com.aiwujie.shengmo.utils.StatusBarUtil;
import com.aiwujie.shengmo.utils.X_SystemBarUI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyPurseActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.mMypurse_return)
    ImageView mMypurseReturn;
    @BindView(R.id.mMypurse_Moudou)
    TextView mMypurseMoudou;
    @BindView(R.id.mMypurse_Recharge)
    Button mMypurseRecharge;
    @BindView(R.id.mMypurse_Withdrawals)
    Button mMypurseWithdrawals;
    @BindView(R.id.mMypurse_zhangdan)
    TextView mMypurseZhangdan;
    @BindView(R.id.mMypurse_cz)
    TextView mMypurseCz;
    @BindView(R.id.mMypurse_cz_dian)
    ImageView mMypurseCzDian;
    @BindView(R.id.mMypurse_lw)
    TextView mMypurseLw;
    @BindView(R.id.mMypurse_lw_dian)
    ImageView mMypurseLwDian;
    @BindView(R.id.mMypurse_viewpager)
    ViewPager mMypurseViewpager;
    private List<TextView> tvs;
    private List<ImageView> ivs;
    private List<Fragment> fragments;
    Handler handler = new Handler();
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purse);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        StatusBarUtil.showLightStatusBar(this);
        //X_SystemBarUI.initSystemBar(this, R.color.title_color);
        setData();
    }

    private void setData() {
        tvs = new ArrayList<>();
        ivs = new ArrayList<>();
        fragments = new ArrayList<>();
        tvs.add(mMypurseCz);
        tvs.add(mMypurseLw);
//        tvs.add(mMypurseYp);
        ivs.add(mMypurseCzDian);
        ivs.add(mMypurseLwDian);
//        ivs.add(mMypurseYpDian);
        //fragments.add(new FragmentRecharge());
        fragments.add(new RechargeFragment());
        //fragments.add(new FragmentPresent());
        fragments.add(new GiftFragment());
//        fragments.add(new FragmentStamp());
        mMypurseViewpager.setOffscreenPageLimit(2);
        mMypurseViewpager.setAdapter(new MyViewpager(getSupportFragmentManager()));
        mMypurseViewpager.addOnPageChangeListener(this);
        mMypurseCz.setSelected(true);
        mMypurseCzDian.setVisibility(View.VISIBLE);
        mMypurseViewpager.setCurrentItem(0, false);
        manager=getSupportFragmentManager();
        int openPresentPage=getIntent().getIntExtra("openPresentPage",-1);
        if(openPresentPage==1){
            EventBus.getDefault().post(new MyPresentEvent());
        }
    }



    @OnClick({R.id.mMypurse_return,  R.id.mMypurse_zhangdan,R.id.mMypurse_cz,R.id.mMypurse_lw})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mMypurse_return:
                finish();
                break;
            case R.id.mMypurse_zhangdan:
                switch (mMypurseViewpager.getCurrentItem()){
                    case 0:
                        //intent = new Intent(this, RechargeBillActivity.class);
                        intent = new Intent(this, RechargeDetailActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        //intent = new Intent(this, PresentBillActivity.class);
                        intent = new Intent(this, PresentDetailActivity.class);
                        startActivity(intent);
                        break;
//                    case 2:
//                        intent = new Intent(this, StampBillActivity.class);
//                        startActivity(intent);
//                        break;
                }
                break;
            case R.id.mMypurse_cz:
                Selected(0);
                break;
            case R.id.mMypurse_lw:
                Selected(1);
                break;
//            case R.id.mMypurse_yp:
//                Selected(2);
//                break;
        }
    }
    private void Selected(int j) {
        for (int i = 0; i < fragments.size(); i++) {
            tvs.get(i).setSelected(false);
            ivs.get(i).setVisibility(View.GONE);
            tvs.get(i).setEnabled(true);
            tvs.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        }
        tvs.get(j).setSelected(true);
        ivs.get(j).setVisibility(View.VISIBLE);
        tvs.get(j).setEnabled(false);
        tvs.get(j).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        mMypurseViewpager.setCurrentItem(j, false);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = manager.getFragments().get(0);
        fragment.onActivityResult(requestCode,resultCode,data);
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void helloEventBus(MyPresentEvent event) {
        //切换到礼物页
        Selected(1);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
